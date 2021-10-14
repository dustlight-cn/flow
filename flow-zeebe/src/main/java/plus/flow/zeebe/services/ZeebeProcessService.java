package plus.flow.zeebe.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.StringUtils;
import plus.flow.core.exceptions.ErrorEnum;
import plus.flow.core.exceptions.FlowException;
import plus.flow.core.flow.Process;
import plus.flow.core.flow.ProcessService;
import plus.flow.zeebe.entities.DefaultAdapterContext;
import plus.flow.zeebe.entities.ZeebeProcess;
import plus.flow.zeebe.entities.ZeebeProcessEntity;
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

    private String processIndex = "zeebe-record-process";

    private static final String prefix = "%s.%s";

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
                                        .addResourceBytes(Base64.getDecoder().decode(s), computePrefix(clientId, owner))
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
                .must(new TermQueryBuilder("value.bpmnProcessId", String.format("_%s.%s", clientId, name)));
        if (version != null)
            boolQueryBuilder.filter(new MatchQueryBuilder("value.version", version));
        NativeSearchQuery q = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withMaxResults(1)
                .withSort(new FieldSortBuilder("value.version"))
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
    public Flux<Process<String>> findProcess(String clientId, String keyword, int page, int size) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .must(StringUtils.hasText(keyword) ? new MatchQueryBuilder("value.bpmnProcessId", keyword) :
                        new MatchAllQueryBuilder())
                .filter(new PrefixQueryBuilder("value.bpmnProcessId", String.format("_%s.", clientId)));

        NativeSearchQuery q = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withSort(new FieldSortBuilder("timestamp"))
                .withPageable(Pageable.ofSize(size).withPage(page))
                .build();
        return operations.search(q,
                        ZeebeProcessEntity.class,
                        IndexCoordinates.of(processIndex))
                .map(zeebeProcessEntitySearchHit -> zeebeProcessEntitySearchHit.getContent())
                .map(ZeebeProcessService::cloneAndSet)
                .flatMap(this::reverse);
    }

    @Override
    public Flux<Process<String>> findProcessLatest(String clientId, String keyword, int page, int size) {
        return null;
    }

    protected String computePrefix(String clientId, String val) {
        return String.format(prefix, clientId, val);
    }

    protected Mono<String> adapt(String clientId, String owner, String processData) {
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
        if (adapters == null || adapters.size() == 0)
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
