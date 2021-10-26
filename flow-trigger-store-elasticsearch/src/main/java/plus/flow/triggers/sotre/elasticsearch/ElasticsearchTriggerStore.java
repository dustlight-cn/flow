package plus.flow.triggers.sotre.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import plus.flow.core.flow.trigger.TriggerStore;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class ElasticsearchTriggerStore implements TriggerStore {

    private ReactiveElasticsearchOperations operations;
    private String index;

    @Override
    public Mono<Void> subscribe(String clientId, String key, String operation, Set<String> process) {
        return operations.save(new ElasticsearchTriggerEntity(key, operation, clientId, process),
                        IndexCoordinates.of(index))
                .then();
    }

    @Override
    public Mono<Void> unsubscribe(String clientId, String key, String operation, Set<String> process) {
//        UpdateQuery updateQuery = UpdateQuery.builder().build()
        return null;
    }

    @Override
    public Flux<String> getProcess(String clientId, String key, String operation) {
        return null;
    }

    @Override
    public Flux<String> getSubscription(String clientId, String process, String operation) {
        return null;
    }

    @Override
    public Flux<String> getProcess(String clientId, String key) {
        return null;
    }

    @Override
    public Flux<String> getSubscription(String clientId, String process) {
        return null;
    }

}
