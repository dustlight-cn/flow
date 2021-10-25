package plus.flow.core.flow.usertask;

import reactor.core.publisher.Flux;

/**
 * 用户任务服务
 */
public interface UserTaskService {

    Flux<UserTask> getUserTask(String clientId, String user, int page, int size);

}
