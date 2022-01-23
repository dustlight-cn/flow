package cn.dustlight.flow.core.flow.instance;

import cn.dustlight.flow.core.flow.QueryResult;
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

    /**
     * 获取实例
     *
     * @param clientId
     * @param id
     * @return
     */
    Mono<Instance> get(String clientId,
                       Long id);

    /**
     * 查找实例
     *
     * @param clientId
     * @param name
     * @param version
     * @param statuses
     * @param page
     * @param size
     * @return
     */
    Mono<QueryResult<Instance>> list(String clientId,
                                     String name,
                                     Integer version,
                                     Set<Instance.Status> statuses,
                                     int page,
                                     int size);

    /**
     * 取消允许
     *
     * @param client
     * @param id
     * @return
     */
    Mono<Void> cancel(String client, Long id);

    /**
     * 重启异常
     *
     * @param client
     * @param id
     * @return
     */
    Mono<Void> resolve(String client, Long id);

    /**
     * 获取变量
     *
     * @param client
     * @param id
     * @param scope
     * @return
     */
    Mono<Map<String, Object>> getVariables(String client, Long id, Long scope);
}
