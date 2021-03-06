package cn.dustlight.flow.core.flow.process;

import cn.dustlight.flow.core.flow.QueryResult;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface ProcessService<T> {

    /**
     * 创建过程
     *
     * @param clientId
     * @param owner
     * @param processData
     * @return
     */
    Mono<Process<T>> createProcess(String clientId, String owner, T processData);

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
     * 过程是否存在
     *
     * @param clientId
     * @param name
     * @return
     */
    Mono<Boolean> isProcessExists(String clientId, Collection<String> name);

    /**
     * 查找过程
     *
     * @param clientId
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    <P extends Process<T>> Mono<QueryResult<P>> findProcess(String clientId, String keyword, int page, int size);

}
