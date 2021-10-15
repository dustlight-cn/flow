package plus.flow.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.adjacency.AdjacencyMatrix;
import org.elasticsearch.search.aggregations.bucket.adjacency.AdjacencyMatrixAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import plus.flow.zeebe.entities.ZeebeProcess;
import plus.flow.zeebe.entities.ZeebeProcessEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ElasticsearchTest {

    @Autowired
    ReactiveElasticsearchOperations operations;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void test() {
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(new MatchAllQueryBuilder()).build();

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
        TermQueryBuilder valueType = new TermQueryBuilder("value.bpmnElementType", "PROCESS");
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder().filter(processId).filter(valueType);

        AbstractAggregationBuilder aggregationBuilder = new TermsAggregationBuilder("process")
                .field("key")
                .subAggregation(new TopHitsAggregationBuilder("current")
                        .size(1)
                        .sort("position", SortOrder.DESC));

        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).addAggregation(aggregationBuilder).build();

        List results = operations.aggregate(query, Map.class, IndexCoordinates.of("zeebe-record-process-instance"))
                .cast(ParsedLongTerms.class)
                .singleOrEmpty()
                .flatMapMany(parsedLongTerms -> Flux.fromIterable(parsedLongTerms.getBuckets()))
                .filter(bucket -> bucket.getAggregations() != null && bucket.getAggregations().get("current") != null)
                .map(buckets -> buckets.getAggregations().get("current"))
                .cast(ParsedTopHits.class)
                .filter(parsedTopHits -> parsedTopHits.getHits().getHits().length > 0)
                .map(parsedTopHits -> parsedTopHits.getHits().getAt(0))
                .map(documentFields -> documentFields.getSourceAsMap())
                .collectList()
                .block();

        System.out.println(mapper.writeValueAsString(results));
    }
}
