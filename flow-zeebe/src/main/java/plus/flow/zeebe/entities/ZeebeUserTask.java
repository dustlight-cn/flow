package plus.flow.zeebe.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import plus.flow.core.flow.usertask.UserTask;
import plus.flow.core.flow.usertask.UserTaskTarget;

import java.time.Instant;

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

    @Override
    public Instant getCompletedAt() {
        return entity == null ? null : entity.getDoneAt();
    }

    @Override
    public UserTaskTarget getTarget() {
        return entity == null ? null : entity.getTarget();
    }

}
