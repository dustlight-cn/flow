package cn.dustlight.flow.zeebe.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.StringUtils;
import cn.dustlight.flow.core.exceptions.ErrorEnum;
import cn.dustlight.flow.core.exceptions.FlowException;
import cn.dustlight.flow.core.flow.QueryResult;
import cn.dustlight.flow.core.flow.process.Process;
import cn.dustlight.flow.core.flow.process.ProcessService;
import cn.dustlight.flow.zeebe.entities.DefaultAdapterContext;
import cn.dustlight.flow.zeebe.entities.ZeebeProcess;
import cn.dustlight.flow.zeebe.entities.ZeebeProcessEntity;
import cn.dustlight.flow.zeebe.services.adapters.ZeebeProcessAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.function.Consumer;

public class ZeebeProcessService implements ProcessService<String> {

    private ZeebeClient zeebeClient;

    private ReactiveElasticsearchOperations operations;

    private List<ZeebeProcessAdapter> adapters;

    @Getter
    @Setter
    private String processIndex = "zeebe-record-process";

    private static final String ownerPlaceholder = "%s-%s";

    private static final ZeebeProcess zeebeProcessTemplate = new ZeebeProcess();

    public ZeebeProcessService(ZeebeClient zeebeClient,
                               ReactiveElasticsearchOperations elasticsearchOperations) {
        this(zeebeClient, elasticsearchOperations, null);
    }

    public ZeebeProcessService(ZeebeClient zeebeClient,
                               ReactiveElasticsearchOperations elasticsearchOperations,
                               Set<ZeebeProcessAdapter> zeebeProcessAdapters) {
        this.zeebeClient = zeebeClient;
        this.operations = elasticsearchOperations;
        if (zeebeProcessAdapters != null && zeebeProcessAdapters.size() > 0) {
            adapters = new ArrayList<>();
            adapters.addAll(zeebeProcessAdapters);
            adapters.sort(Comparator.comparingInt(Ordered::getOrder));
        }
    }

    @Override
    public Mono<Process<String>> createProcess(String clientId, String owner, String processData) {
        return adapt(clientId, owner, processData)
                .flatMap(s -> Mono.create((Consumer<MonoSink<DeploymentEvent>>) sink ->
                                sink.onRequest(unused -> zeebeClient.newDeployCommand()
                                        .addResourceBytes(Base64.getDecoder().decode(s), computeOwner(clientId, owner))
                                        .send()
                                        .whenComplete((d, e) -> {
                                            if (e == null)
                                                sink.success(d);
                                            else sink.error(e);
                                        })))
                        .map(d -> {
                            ZeebeProcessEntity zeebeProcessEntity = new ZeebeProcessEntity();
                            ZeebeProcessEntity.Value value = new ZeebeProcessEntity.Value();
                            zeebeProcessEntity.setKey(d.getKey());
                            zeebeProcessEntity.setValue(value);
                            value.setResource(processData);
                            return new ZeebeProcess(zeebeProcessEntity);
                        }));
    }

    @Override
    public Mono<Void> deleteProcess(String clientId, String processName) {
        return null;
    }

    @Override
    public Mono<Process<String>> getProcess(String clientId, String name, Integer version) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .must(new TermQueryBuilder("value.bpmnProcessId", String.format("c%s-%s", clientId, name)));
        if (version != null)
            boolQueryBuilder.filter(new MatchQueryBuilder("value.version", version));
        NativeSearchQuery q = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withMaxResults(1)
                .withSort(new FieldSortBuilder("value.version").order(SortOrder.DESC))
                .build();
        return operations.search(q,
                        ZeebeProcessEntity.class,
                        IndexCoordinates.of(processIndex))
                .singleOrEmpty()
                .switchIfEmpty(Mono.error(ErrorEnum.PROCESS_NOT_FOUND.getException()))
                .map(zeebeProcessEntitySearchHit -> zeebeProcessEntitySearchHit.getContent())
                .map(ZeebeProcessService::cloneAndSet)
                .flatMap(this::reverse);
    }

    @Override
    public Mono<Boolean> isProcessExists(String clientId, Collection<String> name) {
        if (name == null || name.size() == 0)
            return Mono.just(false);
        Set<String> terms = new HashSet<>();
        for (String n : name)
            terms.add(String.format("c%s-%s", clientId, n.trim()));
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(new BoolQueryBuilder().filter(new TermsQueryBuilder("value.bpmnProcessId", terms)))
                .withSourceFilter(new FetchSourceFilterBuilder().withIncludes("value.bpmnProcessId").build())
                .withCollapseField("value.bpmnProcessId")
                .build();
        return operations.searchForPage(query, ZeebeProcessEntity.class, IndexCoordinates.of(processIndex))
                .map(x -> x.getSize() == name.size());
    }

    @Override
    public Mono<QueryResult<ZeebeProcess>> findProcess(String clientId, String keyword, int page, int size) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .filter(StringUtils.hasText(keyword) ? new MatchQueryBuilder("value.bpmnProcessId", String.format("c%s-%s", clientId, keyword)) :
                        new MatchAllQueryBuilder())
                .filter(new PrefixQueryBuilder("value.bpmnProcessId", String.format("c%s-", clientId)));

        NativeSearchQuery q = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withSourceFilter(new FetchSourceFilterBuilder().withExcludes("value.resource").build())
                .withSort(new FieldSortBuilder("position").order(SortOrder.DESC))
                .withPageable(Pageable.ofSize(size).withPage(page))
                .withCollapseField("value.bpmnProcessId")
                .addAggregation(new CardinalityAggregationBuilder("count").field("value.bpmnProcessId"))
                .build();

        return operations.searchForPage(q,
                        ZeebeProcessEntity.class,
                        IndexCoordinates.of(processIndex))
                .flatMap(searchHits -> {
                    long count = searchHits.getSearchHits().getAggregations().get("count") instanceof ParsedCardinality ?
                            ((ParsedCardinality) searchHits.getSearchHits().getAggregations().get("count")).getValue() :
                            searchHits.getTotalElements();
                    return Flux.fromStream(searchHits.stream())
                            .map(hit -> hit.getContent())
                            .map(ZeebeProcessService::cloneAndSet)
                            .flatMap(this::reverse)
                            .collectList()
                            .map(hits -> new QueryResult<>(count, hits));
                });
    }

    protected String computeOwner(String clientId, String val) {
        return String.format(ownerPlaceholder, clientId, val);
    }

    protected Mono<String> adapt(String clientId, String owner, String processData) {
        if (!StringUtils.hasText(processData))
            return Mono.empty();
        if (adapters == null || adapters.size() == 0)
            return Mono.just(processData);
        DefaultAdapterContext context = new DefaultAdapterContext(clientId, owner);
        Mono<BpmnModelInstance> result = Mono.just(Bpmn.readModelFromStream(new ByteArrayInputStream(Base64.getDecoder().decode(processData))));

        for (ZeebeProcessAdapter adapter : adapters) {
            result = result.transform(bpmnModelInstanceMono ->
                    bpmnModelInstanceMono.flatMap(instance -> {
                        try {
                            adapter.adapt(instance, context);
                            return Mono.just(instance);
                        } catch (Exception e) {
                            return Mono.error(e);
                        }
                    }));
        }

        return result.map(instance -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Bpmn.writeModelToStream(out, instance);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        });
    }

    protected Mono<ZeebeProcess> reverse(ZeebeProcess zeebeProcess) {
        if (adapters == null || adapters.size() == 0 || !StringUtils.hasText(zeebeProcess.getData()))
            return Mono.just(zeebeProcess);
        DefaultAdapterContext context = new DefaultAdapterContext(zeebeProcess.getClientId(), zeebeProcess.getOwner());
        Mono<BpmnModelInstance> result = Mono.just(Bpmn.readModelFromStream(new ByteArrayInputStream(Base64.getDecoder().decode(zeebeProcess.getData()))));

        for (ZeebeProcessAdapter adapter : adapters) {
            result = result.transform(bpmnModelInstanceMono ->
                    bpmnModelInstanceMono.flatMap(instance -> {
                        try {
                            adapter.reverse(instance, context);
                            return Mono.just(instance);
                        } catch (Exception e) {
                            return Mono.error(e);
                        }
                    }));
        }

        return result.map(instance -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Bpmn.writeModelToStream(out, instance);
            zeebeProcess.setData(Base64.getEncoder().encodeToString(out.toByteArray()));
            return zeebeProcess;
        });
    }

    private static ZeebeProcess cloneAndSet(ZeebeProcessEntity zeebeProcessEntity) {
        try {
            return ZeebeProcess.cloneAndSet(zeebeProcessTemplate, zeebeProcessEntity);
        } catch (CloneNotSupportedException e) {
            throw new FlowException(e.getMessage(), e);
        }
    }
}
