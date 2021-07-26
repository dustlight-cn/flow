package plus.flow.core.nodes.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import plus.flow.core.configurations.ServerlessProperties;
import plus.flow.core.context.Context;
import plus.flow.core.nodes.ExecuteLog;
import plus.flow.core.nodes.Node;
import plus.flow.core.nodes.Result;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

public class ServerlessNode extends Node {

    @Getter
    @Setter
    private String function;

    private final static WebClient client = WebClient.builder().build();

    @Override
    protected Mono<Result> onExecute(Map<String, Object> input, Context context, ApplicationContext applicationContext) throws Throwable {
        ServerlessProperties properties = applicationContext.getBean(ServerlessProperties.class);
        Assert.notNull(properties, "ServerlessProperties can not be null");
        return client.post().uri(properties.getEndpoint(), function)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ServerlessData(input, context).toJson())
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.headers().contentType().isPresent() &&
                            clientResponse.headers().contentType().get().isCompatibleWith(MediaType.APPLICATION_JSON))
                        return clientResponse.bodyToMono(Map.class);
                    return clientResponse.bodyToMono(String.class);
                })
                .map(r -> {
                    Result result = new Result();
                    if (r != null) {
                        if (r instanceof Map)
                            result.setOutput((Map) r);
                        else {
                            ExecuteLog log = new ExecuteLog();
                            log.setInfo(r.toString());
                            result.setLog(log);
                        }
                    }
                    result.setSuccess(true);
                    return result;
                });
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class ServerlessData {

        private Map<String, Object> input;
        private Context context;

        private final static ObjectMapper mapper = new ObjectMapper();

        public String toJson() {
            try {
                return mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(
                        String.format("Fail to convert ServerlessData to json, cause: %s", e.getMessage())
                        , e);
            }
        }
    }

    public interface FunctionService {

        Mono<Collection<String>> getFunctionNames();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DefaultFunctionService implements FunctionService {

        private DiscoveryClient discoveryClient;

        @Override
        public Mono<Collection<String>> getFunctionNames() {
            return Mono.just(discoveryClient.getServices());
        }
    }
}
