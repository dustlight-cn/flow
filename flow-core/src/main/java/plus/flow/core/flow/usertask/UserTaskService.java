package plus.flow.core.flow.usertask;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

/**
 * 用户任务服务
 */
public interface UserTaskService {

    Flux<UserTask> getTasks(String clientId,
                            Collection<String> users,
                            Collection<String> roles,
                            int page,
                            int size);

    Mono<UserTask> getTask(String clientId, Long id);

    Mono<Void> complete(String clientId, Long id, String user, Map<String, Object> data);
}
