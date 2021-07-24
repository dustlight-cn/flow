package plus.flow.core.nodes;

import plus.flow.core.context.Context;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface Executable {

    Mono<Result> execute(Map<String, Object> input, Context context) throws ExecutingException;

}
