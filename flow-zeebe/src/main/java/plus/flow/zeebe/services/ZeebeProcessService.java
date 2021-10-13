package plus.flow.zeebe.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import lombok.Getter;
import lombok.Setter;
import plus.flow.core.flow.Process;
import plus.flow.core.flow.ProcessService;
import plus.flow.zeebe.entities.ZeebeProcess;
import plus.flow.zeebe.entities.ZeebeProcessEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.Base64;
import java.util.function.Consumer;

public class ZeebeProcessService implements ProcessService<String> {

    private ZeebeClient zeebeClient;

    @Getter
    @Setter
    private String prefix = "%s_%s";

    public ZeebeProcessService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @Override
    public Mono<Process<String>> createProcess(String clientId, String owner, String name, String processData) {
        return Mono.create((Consumer<MonoSink<DeploymentEvent>>) sink ->
                        sink.onRequest(unused -> zeebeClient.newDeployCommand()
                                .addResourceBytes(Base64.getDecoder().decode(processData), computePrefix(clientId, name))
                                .send()
                                .whenComplete((d, e) -> {
                                    if (e == null)
                                        sink.success(d);
                                    else sink.error(e);
                                })))
                .map(d -> {
                    ZeebeProcessEntity zeebeProcessEntity = new ZeebeProcessEntity();
                    ZeebeProcessEntity.Value value = new ZeebeProcessEntity.Value();
                    zeebeProcessEntity.setKey(d.getKey());
                    zeebeProcessEntity.setValue(value);
                    value.setResource(processData);
                    value.setResourceName(name);
                    return new ZeebeProcess(zeebeProcessEntity);
                });
    }

    @Override
    public Mono<Void> deleteProcess(String clientId, String processName) {
        return null;
    }

    @Override
    public Mono<Process<String>> getProcess(String clientId, String name, Integer version) {
        return null;
    }

    @Override
    public Flux<Process<String>> findProcess(String clientId, String keyword, int offset, int limit) {
        return null;
    }

    @Override
    public Flux<Process<String>> findProcessLatest(String clientId, String keyword, int offset, int limit) {
        return null;
    }

    protected String computePrefix(String clientId, String val) {
        return String.format(prefix, clientId, val);
    }
}
