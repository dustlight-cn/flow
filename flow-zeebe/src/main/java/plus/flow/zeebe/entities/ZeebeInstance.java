package plus.flow.zeebe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;
import plus.flow.core.flow.Instance;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ZeebeInstance extends ZeebeInstanceEvent implements Instance<ZeebeInstanceEvent> {

    private List<ZeebeInstanceEvent> events;

    public ZeebeInstance(List<ZeebeInstanceEvent> events) {
        this.events = events;
        if (events != null && events.size() > 0) {
            events.forEach(event -> {
                ZeebeInstanceEntity eventStart = event == null || event.start == null ? null : event.start;
                ZeebeInstanceEntity eventCurrent = event == null ? null :
                        (event.current == null ? (event.start == null ? null : event.start) : event.current);

                if (this.start == null ||
                        (eventStart != null &&
                                eventStart.getTimestamp() != null &&
                                eventStart.getTimestamp() <= this.start.getTimestamp() &&
                                (eventStart.getPosition() != null && eventStart.getPosition() < this.start.getPosition())))
                    this.start = eventStart;
                if (this.current == null ||
                        (eventCurrent != null &&
                                eventCurrent.getTimestamp() != null &&
                                eventCurrent.getTimestamp() >= this.current.getTimestamp() &&
                                (eventCurrent.getPosition() != null && eventCurrent.getPosition() > this.current.getPosition())))
                    this.current = eventCurrent;
            });
        }
    }

    public ZeebeInstance(ZeebeInstanceEntity start, ZeebeInstanceEntity current) {
        super(start, current);
    }

    @Override
    public Integer getVersion() {
        return start == null || start.getValue() == null ? null : start.getValue().getVersion();
    }

    @JsonIgnore
    @Override
    public String getElementType() {
        return super.getElementType();
    }

    @JsonIgnore
    @Override
    public String getElementId() {
        return super.getElementId();
    }

    @Override
    public Long getId() {
        return start == null || start.getValue() == null ? null : start.getValue().getProcessInstanceKey();
    }

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

}
