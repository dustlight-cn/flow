package plus.flow.core.flow.message;

import reactor.core.publisher.Mono;

public interface MessageService {

    /**
     * 发布消息
     *
     * @param clientId
     * @param messageName
     * @param key
     * @return
     */
    Mono<Void> publishMessage(String clientId, String messageName, String key);

}
