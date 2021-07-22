package plus.flow.core.functions;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface Executable {

    Mono<Result> execute(Map<String, Object> input, Context context) throws ExecutingException;

}
