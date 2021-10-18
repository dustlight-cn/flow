package plus.flow.zeebe.services;

import io.camunda.zeebe.client.ZeebeClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.StringUtils;
import plus.flow.core.exceptions.ErrorEnum;
import plus.flow.core.flow.Instance;
import plus.flow.core.flow.InstanceService;
import plus.flow.zeebe.entities.ZeebeInstance;
import plus.flow.zeebe.entities.ZeebeInstanceEntity;
import plus.flow.zeebe.entities.ZeebeInstanceEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public class ZeebeInstanceService implements InstanceService {

    private ZeebeClient zeebeClient;
    private ReactiveElasticsearchOperations operations;

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
    public Mono<Instance> getInstance(String clientId, Long id) {
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

        return operations.searchForPage(query, ZeebeInstanceEntity.class, IndexCoordinates.of("zeebe-record-process-instance", "zeebe-record-incident"))
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
    public Flux<Instance> listInstance(String clientId,
                                       String name,
                                       Integer version,
                                       Instance.Status status,
                                       int page,
                                       int size) {

        QueryBuilder processId = StringUtils.hasText(name) ?
                new TermQueryBuilder("value.bpmnProcessId", String.format("c%s-%s", clientId, name)) :
                new PrefixQueryBuilder("value.bpmnProcessId", String.format("c%s-", clientId));

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .filter(processId);

        if (status != null) {

        }

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withSort(new FieldSortBuilder("timestamp").order(SortOrder.ASC))
                .withSort(new FieldSortBuilder("position").order(SortOrder.ASC))
                .withPageable(Pageable.ofSize(size).withPage(page))
                .build();

        query.setCollapseBuilder(new CollapseBuilder("value.processInstanceKey")
                .setInnerHits(new InnerHitBuilder()
                        .setName("current")
                        .setSize(1)
                        .addSort(new FieldSortBuilder("position").order(SortOrder.DESC))));

        return operations.searchForPage(query,
                        ZeebeInstanceEntity.class,
                        IndexCoordinates.of("zeebe-record-process-instance", "zeebe-record-incident"))
                .flatMapMany(searchHits -> Flux.fromIterable(searchHits.getContent()))
                .map(hit -> {
                    ZeebeInstanceEntity start = hit.getContent();
                    SearchHits<ZeebeInstanceEntity> currentHits = (SearchHits<ZeebeInstanceEntity>) hit.getInnerHits("current");
                    ZeebeInstanceEntity current = currentHits.hasSearchHits() ? currentHits.getSearchHit(0).getContent() : null;
                    return new ZeebeInstance(start, current);
                });
    }

    @Override
    public Mono<Void> publishMessage(String clientId, String messageName, String key) {
        return Mono.create(sink ->
                sink.onRequest(unused ->
                        zeebeClient.newPublishMessageCommand()
                                .messageName(String.format("c%s-%s", clientId, messageName))
                                .correlationKey(key)
                                .send()
                                .whenComplete(((publishMessageResponse, throwable) -> {
                                    if (throwable != null)
                                        sink.error(throwable);
                                    else
                                        sink.success();
                                }))));
    }

}
