package plus.flow.zeebe.entities;

import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import plus.flow.core.flow.Instance;
import plus.flow.core.flow.InstanceError;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
public class ZeebeInstance implements Instance, Cloneable {

    private ZeebeInstanceEntity start;
    private ZeebeInstanceEntity current;

    @Override
    public Integer getVersion() {
        return start == null || start.getValue() == null ? null : start.getValue().getVersion();
    }

    @Override
    public Long getId() {
        return start == null || start.getValue() == null ? null : start.getValue().getProcessInstanceKey();
    }

    @Override
    public String getName() {
        if (start == null || start.getValue() == null)
            return null;
        ZeebeInstanceEntity.Value value = start.getValue();
        String str = value.getBpmnProcessId();
        if (!StringUtils.hasText(str))
            return null;
        return ZeebeProcess.getSuffix(str, '-');
    }

    @Override
    public String getClientId() {
        if (start == null || start.getValue() == null)
            return null;
        ZeebeInstanceEntity.Value value = start.getValue();
        String str = value.getBpmnProcessId();
        if (!StringUtils.hasText(str))
            return null;
        String prefix = ZeebeProcess.getPrefix(str, '-');
        if (prefix == null)
            return null;
        return prefix.startsWith("c") ? prefix.substring(1) : prefix;
    }

    public static final Collection<String> VALUE_TYPE_ERROR = Set.of("ERROR", "INCIDENT");
    public static final Collection<String> INTENT_COMPLETED = Set.of("ELEMENT_COMPLETED");
    public static final Collection<String> INTENT_CANCELED = Set.of("ELEMENT_TERMINATED");

    @Override
    public Status getStatus() {
        if (current == null)
            return start == null ? null : Status.ACTIVE;
        String intent = current.getIntent();
        String valueType = current.getValueType();
        if (VALUE_TYPE_ERROR.contains(valueType))
            return Status.INCIDENT;
        ZeebeInstanceEntity.Value value = current.getValue();
        if (value != null) {
            String eType = value.getBpmnElementType();
            if ("PROCESS".equals(eType)) {
                if (INTENT_COMPLETED.contains(intent))
                    return Status.COMPLETED;
                if (INTENT_CANCELED.contains(intent))
                    return Status.CANCELED;
            }
        }
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
}
