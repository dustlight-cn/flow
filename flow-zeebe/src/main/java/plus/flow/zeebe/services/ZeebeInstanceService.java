package plus.flow.zeebe.services;

import io.camunda.zeebe.client.ZeebeClient;
import plus.flow.core.flow.InstanceService;
import reactor.core.publisher.Mono;

import java.util.Map;

public class ZeebeInstanceService implements InstanceService {

    private ZeebeClient zeebeClient;

    public ZeebeInstanceService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @Override
    public Mono<Void> start(String clientId, String name, Map<String, Object> variables) {
        return Mono.create(sink ->
                sink.onRequest(unused ->
                        zeebeClient.newCreateInstanceCommand()
                                .bpmnProcessId(String.format("c%s-%s", clientId, name))
                                .latestVersion()
                                .variables(variables)
                                .send()
                                .whenComplete((processInstanceEvent, throwable) -> {
                                    if (throwable != null)
                                        sink.error(throwable);
                                    else
                                        sink.success();
                                }))
        );
    }

    @Override
    public Mono<Void> publishMessage(String clientId, String messageName, String key) {
        return Mono.create(sink ->
                sink.onRequest(unused ->
                        zeebeClient.newPublishMessageCommand()
                                .messageName(String.format("c%s-%s", clientId, messageName))
                                .correlationKey(key)
                                .send()
                                .whenComplete(((publishMessageResponse, throwable) -> {
                                    if (throwable != null)
                                        sink.error(throwable);
                                    else
                                        sink.success();
                                }))));
    }

}
