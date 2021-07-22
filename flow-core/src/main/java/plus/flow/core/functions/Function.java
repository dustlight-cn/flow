package plus.flow.core.functions;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.util.Map;

@Getter
@Setter
public abstract class Function implements Executable {

    private String name;
    private String title;
    private String description;

    private InputOutputStruct struct;

    @Override
    public Mono<Result> execute(Map<String, Object> input, Context context) throws ExecutingException {
        try {
            return onExecute(input, context)
                    .onErrorMap(e -> transformException(e, this));
        } catch (Throwable e) {
            return Mono.error(transformException(e, this));
        }
    }

    protected abstract Mono<Result> onExecute(Map<String, Object> input, Context context) throws Throwable;

    public static ExecutingException transformException(Throwable e, Function function) {
        return e instanceof ExecutingException ?
                (ExecutingException) e :
                (function == null ?
                        new ExecutingException(String.format("Fail to execute function cause: %s", e.getMessage()), e) :
                        new ExecutingException(String.format("Fail to execute function '%s' cause: %s", function.name, e.getMessage()), e)
                );
    }

    public static ExecutingException transformException(Throwable e) {
        return transformException(e, null);
    }
}
