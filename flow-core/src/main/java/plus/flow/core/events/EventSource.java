package plus.flow.core.events;

import reactor.core.publisher.Mono;

public interface EventSource {

    String getName();

    String getTitle();

    String getDescription();

    Mono<Void> handle(Object event);
}
