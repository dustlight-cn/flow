package cn.dustlight.flow.zeebe.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import cn.dustlight.flow.core.flow.usertask.UserTask;
import cn.dustlight.flow.core.flow.usertask.UserTaskTarget;

import java.time.Instant;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class ZeebeUserTask implements UserTask {

    private ZeebeUserTaskEntity entity;

    @Override
    public String getForm() {
        return entity == null || entity.getCustomHeaders() == null ?
                null : entity.getCustomHeaders().get(ZeebeUserTaskEntity.FORM_KEY);
    }

    @Override
    public Long getProcessId() {
        return entity == null ? null : entity.getProcessDefinitionKey();
    }

    @Override
    public Long getInstanceId() {
        return entity == null ? null : entity.getProcessInstanceKey();
    }

    @Override
    public Long getId() {
        return entity == null ? null : entity.getKey();
    }

    @Override
    public String getUser() {
        return entity == null ? null : entity.getDoneBy();
    }

    public ZeebeUserTask complete(String user, Map<String, Object> data, Instant completedAt) {
        if (entity == null)
            throw new NullPointerException("ZeebeUserTaskEntity is null");
        entity.setDoneBy(user);
        entity.setDoneAt(completedAt);
        entity.setData(data);
        return this;
    }

    public ZeebeUserTask complete(String user, Map<String, Object> data) {
        return complete(user, data, Instant.now());
    }

    public ZeebeUserTaskEntity entity() {
        return entity;
    }

    @Override
    public Instant getCompletedAt() {
        return entity == null ? null : entity.getDoneAt();
    }

    @Override
    public UserTaskTarget getTarget() {
        return entity == null ? null : entity.getTarget();
    }

}
