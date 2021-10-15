package plus.flow.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import plus.flow.zeebe.entities.ZeebeProcess;
import plus.flow.zeebe.entities.ZeebeProcessEntity;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
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
        TermQueryBuilder processId = new TermQueryBuilder("value.bpmnProcessId", "_86c3e34e2030000.order-demo");

        TermQueryBuilder valueType = new TermQueryBuilder("value.bpmnElementType", "PROCESS");

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder().must(processId).filter(valueType);



        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).build();

        ArrayList<Map> result = operations.searchForPage(query, Map.class, IndexCoordinates.of("zeebe-record-process-instance"))
                .map(searchHits -> {
                    System.out.println(searchHits.getSize());
                    ArrayList<Map> r = new ArrayList<>();
                    searchHits.getContent().forEach(searchHit -> r.add(searchHit.getContent()));
                    return r;
                }).block();

        System.out.println(mapper.writeValueAsString(result));
    }
}
