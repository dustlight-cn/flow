package plus.flow.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import plus.flow.core.flow.trigger.TriggerStore;
import plus.flow.zeebe.entities.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class ElasticsearchTest {

    @Autowired
    ReactiveElasticsearchOperations operations;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void test() {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withSourceFilter(new FetchSourceFilterBuilder().withExcludes("value.resource").build())
                .withQuery(new MatchAllQueryBuilder()).withPageable(Pageable.ofSize(10)).build();

        operations.searchForPage(query, ZeebeProcessEntity.class, IndexCoordinates.of("zeebe-record-process"))
                .map(searchHits -> {
                    System.out.println(searchHits.getSize());
                    searchHits.getContent().forEach(searchHit -> {
                        try {
                            System.out.println(mapper.writeValueAsString(new ZeebeProcess(searchHit.getContent())));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    });
                    return Mono.empty();
                }).block();
    }

    @Test
    public void test2() throws JsonProcessingException {
        TermQueryBuilder processId = new TermQueryBuilder("value.bpmnProcessId", "c86c3e34e2030000-order-demo");
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .filter(processId);


        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withSort(new FieldSortBuilder("timestamp").order(SortOrder.ASC))
                .withSort(new FieldSortBuilder("position").order(SortOrder.ASC))
                .withQuery(boolQueryBuilder)
                .build();

        query.setCollapseBuilder(new CollapseBuilder("value.processInstanceKey")
                .setInnerHits(new InnerHitBuilder()
                        .setName("current")
                        .setSize(1)
                        .addSort(new FieldSortBuilder("position").order(SortOrder.DESC))));

        List<ZeebeInstance> results = operations.searchForPage(query,
                        ZeebeInstanceEntity.class,
                        IndexCoordinates.of("zeebe-record-process-instance", "zeebe-record-incident"))
                .flatMapMany(searchHits -> Flux.fromIterable(searchHits.getContent()))
                .map(hit -> {
                    ZeebeInstanceEntity start = hit.getContent();
                    SearchHits<ZeebeInstanceEntity> currentHits = (SearchHits<ZeebeInstanceEntity>) hit.getInnerHits("current");
                    ZeebeInstanceEntity current = currentHits.hasSearchHits() ? currentHits.getSearchHit(0).getContent() : null;
                    return new ZeebeInstance(start, current);
                })
                .collectList()
                .block();

        System.out.println(mapper.writeValueAsString(results));
    }

    @Test
    public void test3() throws JsonProcessingException {
        TermQueryBuilder processId = new TermQueryBuilder("value.processInstanceKey", "2251799813685775");
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .filter(processId);


        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withSort(new FieldSortBuilder("timestamp").order(SortOrder.ASC))
                .withSort(new FieldSortBuilder("position").order(SortOrder.ASC))
                .withQuery(boolQueryBuilder)
                .build();

        query.setCollapseBuilder(new CollapseBuilder("key")
                .setInnerHits(new InnerHitBuilder()
                        .setName("events")
                        .setSize(4)
                        .addSort(new FieldSortBuilder("timestamp").order(SortOrder.ASC))
                        .addSort(new FieldSortBuilder("position").order(SortOrder.ASC))));

        ZeebeInstance instance = operations.searchForPage(query,
                        ZeebeInstanceEntity.class,
                        IndexCoordinates.of("zeebe-record-process-instance", "zeebe-record-incident"))
                .flatMapMany(searchHits -> Flux.fromIterable(searchHits.getContent()))
                .map(hit -> {
                    ZeebeInstanceEntity start = hit.getContent();
                    SearchHits<ZeebeInstanceEntity> currentHits = (SearchHits<ZeebeInstanceEntity>) hit.getInnerHits("events");

                    ZeebeInstanceEntity current = currentHits.hasSearchHits() ? currentHits.getSearchHit(currentHits.getSearchHits().size() - 1).getContent() : null;

                    return new ZeebeInstanceEvent(start, current);
                })
                .collectList()
                .map(events -> new ZeebeInstance(events))
                .block();

        System.out.println(mapper.writeValueAsString(instance));
    }

    @Test
    public void testTriggerStore(@Autowired TriggerStore store) {
        System.out.println(store.setSubscription("gg", "qwe", "zzz", Set.of("qq", "xx", "ee")).block());
    }

    @Test
    public void testTriggerStore2(@Autowired TriggerStore store, @Autowired ObjectMapper mapper) throws JsonProcessingException {
        System.out.println(mapper.writeValueAsString(store.getProcess("gg", "qwe").collectList().block()));
    }

    @Test
    public void testTriggerStore3(@Autowired TriggerStore store, @Autowired ObjectMapper mapper) throws JsonProcessingException {
        System.out.println(mapper.writeValueAsString(store.getSubscription("gg", "qq").collectList().block()));
    }

    @Test
    public void testIncident(@Autowired ReactiveElasticsearchOperations operations,
                             @Autowired ObjectMapper mapper) throws JsonProcessingException {
//        NativeSearchQuery query = new NativeSearchQueryBuilder()
//                .withQuery(new BoolQueryBuilder()
//                        .filter(new TermQueryBuilder("value.processInstanceKey", 6755399441312127L)))
//                .build();
//        List<ZeebeInstanceEntity> result = operations.search(query, ZeebeInstanceEntity.class, IndexCoordinates.of("zeebe-record-incident"))
//                .map(zeebeInstanceEntitySearchHit -> zeebeInstanceEntitySearchHit.getContent())
//                .map(entity -> new ZeebeInstanceEvent(entity))
//                .collectList().block();
//        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
    }
}
