package plus.flow.zeebe.services;

import io.camunda.zeebe.client.ZeebeClient;
import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.StringUtils;
import plus.flow.core.exceptions.ErrorEnum;
import plus.flow.core.flow.QueryResult;
import plus.flow.core.flow.instance.Instance;
import plus.flow.core.flow.instance.InstanceEvent;
import plus.flow.core.flow.instance.InstanceService;
import plus.flow.zeebe.entities.ZeebeInstance;
import plus.flow.zeebe.entities.ZeebeInstanceEntity;
import plus.flow.zeebe.entities.ZeebeInstanceEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ZeebeInstanceService implements InstanceService {

    private ZeebeClient zeebeClient;
    private ReactiveElasticsearchOperations operations;

    @Getter
    @Setter
    private String instanceIndex = "zeebe-record-process-instance";

    @Getter
    @Setter
    private String incidentIndex = "zeebe-record-incident";

    public ZeebeInstanceService(ZeebeClient zeebeClient,
                                ReactiveElasticsearchOperations operations) {
        this.zeebeClient = zeebeClient;
        this.operations = operations;
    }

    @Override
    public Mono<Instance> start(String clientId, String name, Map<String, Object> variables) {
        return Mono.create(sink ->
                sink.onRequest(unused ->
                        zeebeClient.newCreateInstanceCommand()
                                .bpmnProcessId(String.format("c%s-%s", clientId, name))
                                .latestVersion()
                                .variables(variables)
                                .send()
                                .whenComplete((processInstanceEvent, throwable) -> {
                                    ZeebeInstanceEntity entity = new ZeebeInstanceEntity();
                                    entity.setKey(processInstanceEvent.getProcessInstanceKey());
                                    ZeebeInstanceEntity.Value value = new ZeebeInstanceEntity.Value();
                                    entity.setValue(value);
                                    value.setBpmnProcessId(processInstanceEvent.getBpmnProcessId());
                                    value.setProcessInstanceKey(processInstanceEvent.getProcessInstanceKey());
                                    value.setVersion(processInstanceEvent.getVersion());
                                    value.setProcessDefinitionKey(processInstanceEvent.getProcessDefinitionKey());
                                    if (throwable != null)
                                        sink.error(throwable);
                                    else
                                        sink.success(new ZeebeInstance(entity, null));
                                }))
        );
    }

    @Override
    public Mono<Instance> get(String clientId, Long id) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .filter(new PrefixQueryBuilder("value.bpmnProcessId", String.format("c%s-", clientId)))
                .filter(new TermQueryBuilder("value.processInstanceKey", id));
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withSort(new FieldSortBuilder("timestamp").order(SortOrder.ASC))
                .withSort(new FieldSortBuilder("position").order(SortOrder.ASC))
                .build();
        query.setCollapseBuilder(new CollapseBuilder("key")
                .setInnerHits(new InnerHitBuilder()
                        .setName("events")
                        .setSize(4)
                        .addSort(new FieldSortBuilder("timestamp").order(SortOrder.ASC))
                        .addSort(new FieldSortBuilder("position").order(SortOrder.ASC))));

        return operations.searchForPage(query, ZeebeInstanceEntity.class, IndexCoordinates.of(incidentIndex, instanceIndex))
                .flatMapMany(searchHits -> searchHits.hasContent() ? Flux.fromIterable(searchHits.getSearchHits()) : Flux.error(ErrorEnum.INSTANCE_NOT_FOUND.getException()))
                .map(hit -> {
                    ZeebeInstanceEntity start = hit.getContent();
                    SearchHits<ZeebeInstanceEntity> currentHits = (SearchHits<ZeebeInstanceEntity>) hit.getInnerHits("events");
                    ZeebeInstanceEntity current = currentHits.hasSearchHits() ? currentHits.getSearchHit(currentHits.getSearchHits().size() - 1).getContent() : null;
                    return new ZeebeInstanceEvent(start, current);
                })
                .collectList()
                .map(events -> new ZeebeInstance(events));
    }


    @Override
    public Mono<QueryResult<Instance>> list(String clientId,
                                            String name,
                                            Integer version,
                                            Set<Instance.Status> statuses,
                                            int page,
                                            int size) {
        BoolQueryBuilder statusFilter = null;
        if (statuses != null && !statuses.isEmpty()) {
            statusFilter = new BoolQueryBuilder();
            Set<QueryBuilder> statusQueries = new HashSet<>();
            for (InstanceEvent.Status status : statuses) {
                switch (status) {
                    case ACTIVE:
                        statusQueries.add(StatusQuery.ACTIVE);
                        break;
                    case CANCELED:
                        statusQueries.add(StatusQuery.CANCELED);
                        break;
                    case COMPLETED:
                        statusQueries.add(StatusQuery.COMPLETED);
                        break;
                    case INCIDENT:
                        statusQueries.add(StatusQuery.INCIDENT);
                        break;
                    default:
                        break;
                }
            }
            for (QueryBuilder builder : statusQueries)
                statusFilter.should(builder);
        }
        return listInstance(clientId, name, version, page, size, statusFilter);
    }

    @Override
    public Mono<Void> cancel(String client, Long id) {
        return getEntity(client, id, instanceIndex)
                .switchIfEmpty(Mono.error(ErrorEnum.INSTANCE_NOT_FOUND.getException()))
                .map(entity -> new ZeebeInstance(entity, entity))
                .flatMap(instance -> Mono.create(sink ->
                        sink.onRequest(unused -> zeebeClient
                                .newCancelInstanceCommand(instance.getId())
                                .send()
                                .whenComplete((unused2, e) -> {
                                    if (e == null)
                                        sink.success();
                                    else
                                        sink.error(e);
                                }))
                ));
    }

    @Override
    public Mono<Void> resolve(String client, Long id) {
        return getEntity(client, id, instanceIndex)
                .switchIfEmpty(Mono.error(ErrorEnum.RESOURCE_NOT_FOUND.getException()))
                .map(entity -> new ZeebeInstanceEvent(entity, entity))
                .flatMap(instance -> Mono.create(sink ->
                        sink.onRequest(unused -> zeebeClient
                                .newResolveIncidentCommand(instance.getId())
                                .send()
                                .whenComplete((unused2, e) -> {
                                    if (e == null)
                                        sink.success();
                                    else
                                        sink.error(e);
                                }))
                ));
    }

    public Mono<QueryResult<Instance>> listInstance(String clientId,
                                                    String name,
                                                    Integer version,
                                                    int page,
                                                    int size,
                                                    QueryBuilder... filters) {
        QueryBuilder processId = StringUtils.hasText(name) ?
                new TermQueryBuilder("value.bpmnProcessId", String.format("c%s-%s", clientId, name)) :
                new PrefixQueryBuilder("value.bpmnProcessId", String.format("c%s-", clientId));

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .filter(processId);

        if (version != null) {
            boolQueryBuilder.filter(new TermQueryBuilder("value.version", version));
        }
        if (filters != null)
            for (QueryBuilder f : filters)
                if (f != null)
                    boolQueryBuilder.filter(f);

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withSourceFilter(new FetchSourceFilterBuilder().build())
//                .withSort(new FieldSortBuilder("timestamp").order(SortOrder.ASC))
//                .withSort(new FieldSortBuilder("position").order(SortOrder.ASC))
                .withPageable(Pageable.ofSize(size).withPage(page))
                .addAggregation(new CardinalityAggregationBuilder("count").field("value.processInstanceKey"))
                .build();


        query.setCollapseBuilder(new CollapseBuilder("value.processInstanceKey")
                .setInnerHits(Arrays.asList(
                        new InnerHitBuilder()
                                .setName("current")
                                .setSize(1)
                                .addSort(new FieldSortBuilder("position").order(SortOrder.DESC)),
                        new InnerHitBuilder()
                                .setName("start")
                                .setSize(1)
                                .addSort(new FieldSortBuilder("position").order(SortOrder.ASC)))));

        return operations.searchForPage(query,
                        ZeebeInstanceEntity.class,
                        IndexCoordinates.of(incidentIndex, instanceIndex))
                .flatMap(searchHits -> {
                    long count = searchHits.getSearchHits().getAggregations().get("count") instanceof ParsedCardinality ?
                            ((ParsedCardinality) searchHits.getSearchHits().getAggregations().get("count")).getValue() :
                            searchHits.getTotalElements();
                    return Flux.fromIterable(searchHits.getContent())
                            .map(hit -> {
                                ZeebeInstanceEntity start = ((SearchHits<ZeebeInstanceEntity>) hit.getInnerHits("start")).getSearchHit(0).getContent();
                                SearchHits<ZeebeInstanceEntity> currentHits = (SearchHits<ZeebeInstanceEntity>) hit.getInnerHits("current");
                                ZeebeInstanceEntity current = currentHits.hasSearchHits() ? currentHits.getSearchHit(0).getContent() : null;
                                return new ZeebeInstance(start, current);
                            })
                            .collectList()
                            .map(hits -> new QueryResult<>(count, hits));
                });
    }

    protected Mono<ZeebeInstanceEntity> getEntity(String clientId, Long key, String... index) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .filter(new PrefixQueryBuilder("value.bpmnProcessId", String.format("c%s-", clientId)))
                .filter(new TermQueryBuilder("key", key));
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(Pageable.ofSize(1))
                .build();

        return operations.searchForPage(query, ZeebeInstanceEntity.class, IndexCoordinates.of(index))
                .flatMapMany(searchHits -> searchHits.hasContent() ? Flux.fromIterable(searchHits.getSearchHits()) : Flux.empty())
                .singleOrEmpty()
                .map(zeebeInstanceEntitySearchHit -> zeebeInstanceEntitySearchHit.getContent());
    }

    protected static class StatusQuery {


        public static BoolQueryBuilder CANCELED;
        public static BoolQueryBuilder COMPLETED;
        public static BoolQueryBuilder INCIDENT;
        public static BoolQueryBuilder ACTIVE;

        static {
            CANCELED = new BoolQueryBuilder()
                    .must(new TermQueryBuilder("value.bpmnElementType", "PROCESS"))
                    .must(new TermQueryBuilder("intent", "ELEMENT_TERMINATED"));

            COMPLETED = new BoolQueryBuilder()
                    .must(new TermQueryBuilder("value.bpmnElementType", "PROCESS"))
                    .must(new TermQueryBuilder("intent", "ELEMENT_COMPLETED"));

            INCIDENT = new BoolQueryBuilder()
                    .must(new TermQueryBuilder("valueType", "INCIDENT"))
                    .mustNot(new TermQueryBuilder("intent", "RESOLVED"));

            ACTIVE = new BoolQueryBuilder()
                    .mustNot(CANCELED)
                    .mustNot(COMPLETED)
                    .mustNot(INCIDENT);
        }

    }
}
