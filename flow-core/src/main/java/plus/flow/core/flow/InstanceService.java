package plus.flow.core.flow;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

/**
 * 实例服务
 */
public interface InstanceService {

    /**
     * 启动过程实例
     *
     * @param clientId
     * @param name
     * @param variables
     * @return
     */
    Mono<Instance> start(String clientId,
                         String name,
                         Map<String, Object> variables);

    Mono<Instance> getInstance(String clientId,
                               Long id);

    Flux<Instance> listInstance(String clientId,
                                String name,
                                Integer version,
                                Set<Instance.Status> statuses,
                                int page,
                                int size);

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
