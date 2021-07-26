package plus.flow.core.nodes.impls;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import plus.flow.core.context.Context;
import plus.flow.core.nodes.ExecuteLog;
import plus.flow.core.nodes.NodeType;
import plus.flow.core.nodes.Result;
import plus.flow.core.nodes.Node;
import reactor.core.publisher.Mono;

import javax.script.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Map;

@Getter
@Setter
public class ScriptNode extends Node {

    private final static Gson gson = new Gson();

    private String language;
    private String script;
    private String main;

    @Override
    public Mono<Result> onExecute(Map<String, Object> input, Context context, ApplicationContext applicationContext) throws Throwable {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(language);
        Invocable inv = (Invocable) engine;
        ScriptContext scriptContext = new SimpleScriptContext();

        ByteArrayOutputStream info = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        scriptContext.setWriter(new OutputStreamWriter(info));
        scriptContext.setWriter(new OutputStreamWriter(error));

        Bindings bindings = new SimpleBindings();
        Logger logger = new Logger(new PrintStream(info), new PrintStream(error));
        bindings.put("logger", logger);
        bindings.put("console", logger);

        scriptContext.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);

        engine.setContext(scriptContext);

        engine.eval(script);
        Object invokeResult = inv.invokeFunction(main, input, context);

        ExecuteLog log = new ExecuteLog();
        log.setInfo(info.toString(Charset.forName("UTF-8")));
        log.setError(error.toString(Charset.forName("UTF-8")));

        Result result = new Result();
        result.setLog(log);
        if (invokeResult != null) {
            result.setOutput(gson.fromJson(gson.toJson(invokeResult), Map.class));
        }
        result.setSuccess(true);
        return Mono.just(result);
    }

    @Schema(defaultValue = "SCRIPT")
    @Override
    public NodeType getType() {
        return NodeType.SCRIPT;
    }

    @Override
    public void setType(NodeType type) {
        super.setType(NodeType.SCRIPT);
    }

    @AllArgsConstructor
    public static class Logger {
        private PrintStream info, error;

        public void log(Object... objs) {
            if (objs == null)
                return;
            for (Object obj : objs)
                info.println(obj);
        }

        public void info(Object... objs) {
            if (objs == null)
                return;
            for (Object obj : objs)
                info.println(obj);
        }

        public void error(Object... objs) {
            if (objs == null)
                return;
            for (Object obj : objs)
                error.println(obj);
        }
    }
}
