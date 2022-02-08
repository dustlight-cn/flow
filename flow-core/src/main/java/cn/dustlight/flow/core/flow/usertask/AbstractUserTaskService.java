package cn.dustlight.flow.core.flow.usertask;

import cn.dustlight.flow.core.exceptions.ErrorEnum;
import cn.dustlight.flow.core.exceptions.FlowException;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;

@AllArgsConstructor
public abstract class AbstractUserTaskService<T extends UserTask> implements UserTaskService<T> {

    private UserTaskDataValidator validator;

    @Override
    public Mono<Void> complete(String clientId, Long id, String user, Map<String, Object> data) {
        return getTask(clientId, id)
                .switchIfEmpty(Mono.error(ErrorEnum.USER_TASK_NOT_FOUND.getException()))
                .flatMap(userTask ->
                        userTask.getCompletedAt() != null ?
                                Mono.error(ErrorEnum.USER_TASK_ALREADY_DONE.getException()) :
                                validator.verify(userTask.getForm(), clientId, data)
                                        .flatMap(flag -> flag ?
                                                doComplete(clientId, id, user, data, userTask) :
                                                Mono.error(ErrorEnum.USER_TASK_DATA_INVALID.getException())))
                .onErrorMap(throwable -> throwable instanceof FlowException ? throwable :
                        ErrorEnum.USER_TASK_DATA_INVALID.details(throwable).getException());
    }

    protected abstract Mono<Void> doComplete(String clientId,
                                             Long id,
                                             String user,
                                             Map<String, Object> data,
                                             T userTask);

}
