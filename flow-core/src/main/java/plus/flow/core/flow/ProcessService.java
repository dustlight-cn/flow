package plus.flow.core.flow;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProcessService<T> {

    /**
     * 创建过程
     *
     * @param process
     * @return
     */
    Mono<Process<T>> createProcess(Process<T> process);

    /**
     * 更新过程
     *
     * @param process
     * @return
     */
    Mono<Void> updateProcess(Process<T> process);

    /**
     * 删除过程
     *
     * @param clientId
     * @param processName
     * @return
     */
    Mono<Void> deleteProcess(String clientId, String processName);

    /**
     * 获取过程
     *
     * @param clientId
     * @param name
     * @param version
     * @return
     */
    Mono<Process<T>> getProcess(String clientId, String name, Integer version);

    /**
     * 查找过程
     *
     * @param clientId
     * @param keyword
     * @param offset
     * @param limit
     * @return
     */
    Flux<Process<T>> findProcess(String clientId, String keyword, int offset, int limit);

    /**
     * 查找最新版本过程
     *
     * @param clientId
     * @param keyword
     * @param offset
     * @param limit
     * @return
     */
    Flux<Process<T>> findProcessLatest(String clientId, String keyword, int offset, int limit);
}
