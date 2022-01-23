package cn.dustlight.flow.zeebe.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import cn.dustlight.flow.core.flow.instance.InstanceError;
import cn.dustlight.flow.core.flow.instance.InstanceEvent;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class ZeebeInstanceEvent implements InstanceEvent, Cloneable {

    protected ZeebeInstanceEntity start;
    protected ZeebeInstanceEntity current;

    public static final Collection<String> VALUE_TYPE_ERROR = Set.of("ERROR", "INCIDENT");
    public static final Collection<String> INCIDENT_RESOLVED = Set.of("RESOLVED");
    public static final Collection<String> INTENT_COMPLETED = Set.of("ELEMENT_COMPLETED", "SEQUENCE_FLOW_TAKEN");
    public static final Collection<String> INTENT_CANCELED = Set.of("ELEMENT_TERMINATED");

    @Override
    public Long getId() {
        return start == null ? null : start.getKey();
    }

    @Override
    public String getElementType() {
        return start == null || start.getValue() == null ? null : start.getValue().getBpmnElementType();
    }

    @Override
    public String getElementId() {
        return ZeebeProcess.getSuffix(start == null || start.getValue() == null ? null : start.getValue().getElementId(), '-');
    }

    @Override
    public Status getStatus() {
        if (current == null)
            return start == null ? null : Status.ACTIVE;
        String intent = current.getIntent();
        String valueType = current.getValueType();
        if (VALUE_TYPE_ERROR.contains(valueType))
            return INCIDENT_RESOLVED.contains(intent) ? Status.RESOLVED : Status.INCIDENT;
        if (INTENT_COMPLETED.contains(intent))
            return Status.COMPLETED;
        if (INTENT_CANCELED.contains(intent))
            return Status.CANCELED;
        return Status.ACTIVE;
    }


    @Override
    public Instant getCreatedAt() {
        return start == null || start.getTimestamp() == null ? null : Instant.ofEpochMilli(start.getTimestamp());
    }

    @Override
    public Instant getUpdatedAt() {
        return current == null || current.getTimestamp() == null ? null : Instant.ofEpochMilli(current.getTimestamp());
    }

    @Override
    public InstanceError getError() {
        ZeebeInstanceEntity.Value value;
        return current == null || (value = current.getValue()) == null ||
                (!StringUtils.hasText(value.getErrorMessage()) && !StringUtils.hasText(value.getErrorType())) ?
                null : new ZeebeInstanceError(value.getErrorMessage(), value.getErrorType());
    }

    @Override
    protected ZeebeInstanceEvent clone() throws CloneNotSupportedException {
        return (ZeebeInstanceEvent) super.clone();
    }
}
