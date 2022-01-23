package cn.dustlight.flow.core.flow.trigger;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface TriggerStore {

    Mono<Void> setSubscription(String clientId,
                               String key,
                               String operation,
                               Set<String> process);

    Flux<String> getSubscription(String clientId,
                                 String process,
                                 String operation);

    Flux<String> getSubscription(String clientId,
                                 String process);

    Flux<String> getProcess(String clientId,
                            String key,
                            String operation);

    Flux<String> getProcess(String clientId,
                            String key);

}
