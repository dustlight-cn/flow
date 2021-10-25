package plus.flow.core.flow.usertask;

import reactor.core.publisher.Flux;

/**
 * 用户任务服务
 */
public interface UserTaskService<T> {

    Flux<UserTask<T>> getUserTask(String clientId, int page, int size);

}
