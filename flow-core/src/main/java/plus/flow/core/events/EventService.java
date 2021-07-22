package plus.flow.core.events;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface EventService {

    Mono<Map<String,EventSource>> getSources();

}
