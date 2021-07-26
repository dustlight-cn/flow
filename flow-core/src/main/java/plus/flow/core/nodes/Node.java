package plus.flow.core.nodes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import plus.flow.core.context.Context;
import plus.flow.core.nodes.impls.ScriptNode;
import plus.flow.core.nodes.impls.ServerlessNode;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Map;

@Schema(oneOf = {
        ScriptNode.class,
        ServerlessNode.class
})
@Getter
@Setter
@JsonDeserialize(using = NodeDeserializer.class)
public abstract class Node implements Executable, Serializable {

    private String title;
    private NodeType type;

    private Map<String, String> parameters;

    @Override
    public Mono<Result> execute(Map<String, Object> input, Context context, ApplicationContext applicationContext) throws ExecutingException {
        try {
            return onExecute(input, context, applicationContext)
                    .onErrorMap(e -> transformException(e, this));
        } catch (Throwable e) {
            e.printStackTrace();
            return Mono.error(transformException(e, this));
        }
    }

    protected abstract Mono<Result> onExecute(Map<String, Object> input, Context context, ApplicationContext applicationContext) throws Throwable;

    public static ExecutingException transformException(Throwable e, Node function) {
        return e instanceof ExecutingException ?
                (ExecutingException) e :
                (function == null ?
                        new ExecutingException(String.format("Fail to execute node cause: %s", e.getMessage()), e) :
                        new ExecutingException(String.format("Fail to execute node '%s (%s)' cause: %s", function.title, function.type, e.getMessage()), e)
                );
    }

    public static ExecutingException transformException(Throwable e) {
        return transformException(e, null);
    }
}
