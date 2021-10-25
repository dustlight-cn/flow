package plus.flow.zeebe.services;

import io.camunda.zeebe.client.ZeebeClient;
import plus.flow.core.flow.message.MessageService;
import reactor.core.publisher.Mono;

public class ZeebeMessageService implements MessageService {

    private ZeebeClient zeebeClient;

    public ZeebeMessageService(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
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
