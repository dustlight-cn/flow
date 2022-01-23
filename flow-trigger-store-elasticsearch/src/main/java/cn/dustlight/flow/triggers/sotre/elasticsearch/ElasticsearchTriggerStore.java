package cn.dustlight.flow.triggers.sotre.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.util.StringUtils;
import cn.dustlight.flow.core.flow.trigger.TriggerStore;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class ElasticsearchTriggerStore implements TriggerStore {

    private ReactiveElasticsearchOperations operations;
    private String index;

    @Override
    public Mono<Void> setSubscription(String clientId, String key, String operation, Set<String> process) {
        return operations.save(new ElasticsearchTriggerEntity(key, operation, clientId, process),
                        IndexCoordinates.of(index))
                .then();
    }

    @Override
    public Flux<String> getProcess(String clientId, String key, String operation) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .filter(new TermQueryBuilder("clientId.keyword", clientId))
                .filter(new TermQueryBuilder("key.keyword", key));

        if (StringUtils.hasText(operation))
            boolQueryBuilder.filter(new TermQueryBuilder("operation.keyword", operation));

        return operations.search(new NativeSearchQuery(boolQueryBuilder), ElasticsearchTriggerEntity.class,
                        IndexCoordinates.of(index))
                .map(elasticsearchTriggerEntitySearchHit -> elasticsearchTriggerEntitySearchHit.getContent())
                .map(elasticsearchTriggerEntity -> elasticsearchTriggerEntity.getProcess())
                .collectList()
                .flatMapMany(sets -> {
                    Set<String> result = new HashSet<>();
                    sets.forEach(strings -> result.addAll(strings));
                    return Flux.fromIterable(result);
                });
    }

    @Override
    public Flux<String> getSubscription(String clientId, String process, String operation) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .filter(new TermQueryBuilder("clientId.keyword", clientId))
                .filter(new TermsQueryBuilder("process.keyword", process));

        if (StringUtils.hasText(operation))
            boolQueryBuilder.filter(new TermQueryBuilder("operation.keyword", operation));

        return operations.search(new NativeSearchQuery(boolQueryBuilder), ElasticsearchTriggerEntity.class,
                        IndexCoordinates.of(index))
                .map(elasticsearchTriggerEntitySearchHit -> elasticsearchTriggerEntitySearchHit.getContent())
                .map(elasticsearchTriggerEntity -> elasticsearchTriggerEntity.getKey())
                .collectList()
                .flatMapMany(key -> {
                    Set<String> result = new HashSet<>();
                    result.addAll(key);
                    return Flux.fromIterable(result);
                });
    }

    @Override
    public Flux<String> getProcess(String clientId, String key) {
        return getProcess(clientId, key, null);
    }

    @Override
    public Flux<String> getSubscription(String clientId, String process) {
        return getSubscription(clientId, process, null);
    }

}
