package plus.flow.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;
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
import plus.flow.zeebe.entities.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

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

        Object results = operations.searchForPage(query,
                        ZeebeInstanceEntity.class,
                        IndexCoordinates.of("zeebe-record-process-instance", "zeebe-record-incident"))
                .flatMapMany(searchHits -> Flux.fromIterable(searchHits.getContent()))
                .map(hit -> {
                    ZeebeInstanceEntity start = hit.getContent();
                    SearchHits<ZeebeInstanceEntity> currentHits = (SearchHits<ZeebeInstanceEntity>) hit.getInnerHits("current");
                    ZeebeInstanceEntity current = currentHits.hasSearchHits() ? currentHits.getSearchHit(0).getContent() : null;
                    return new ZeebeInstance(start, current);
                })
//                .cast(ParsedLongTerms.class)
//                .singleOrEmpty()
//                .flatMapMany(parsedLongTerms -> Flux.fromIterable(parsedLongTerms.getBuckets()))
//                .map(bucket -> Tuple.tuple(bucket.getAggregations().get("current"), bucket.getAggregations().get("start")))
//                .map(tuple -> Tuple.tuple(((ParsedTopHits) tuple.v1()).getHits().getAt(0), ((ParsedTopHits) tuple.v2()).getHits().getAt(0)))
//                .map(tuple -> Tuple.tuple((tuple.v1()).getSourceRef(), (tuple.v2()).getSourceRef()))
//                .map((Function<Tuple<BytesReference, BytesReference>, Object>) tuple -> {
//                    try {
//                        return new ZeebeInstance(mapper.readValue(tuple.v1().streamInput(), ZeebeInstanceEntity.class),
//                                mapper.readValue(tuple.v1().streamInput(), ZeebeInstanceEntity.class));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e.getMessage(), e);
//                    }
//                })
//                .cast(ZeebeInstance.class)
                .collectList()
                .block();

//        for (ZeebeInstance item : results) {
//            System.out.println(mapper.writeValueAsString(
//                    item
//            ));
//        }

        System.out.println(mapper.writeValueAsString(
                results
        ));
    }
}
