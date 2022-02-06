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
    public String getProcessName() {
        return entity == null ? null : ZeebeProcess.getSuffix(entity.getBpmnProcessId(), '-');
    }

    @Override
    public String getClientId() {
        String clientId = entity == null ? null : ZeebeProcess.getPrefix(entity.getBpmnProcessId(), '-');
        return clientId == null ? null : clientId.substring(1);
    }

    @Override
    public String getElementId() {
        return entity == null ? null : ZeebeProcess.getSuffix(entity.getElementId(), '-');
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

    @Override
    public Map<String, Object> getVariables() {
        return entity == null ? null : entity.getVariables();
    }

}
