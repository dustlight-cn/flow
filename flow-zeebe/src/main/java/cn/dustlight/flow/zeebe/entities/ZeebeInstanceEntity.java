package cn.dustlight.flow.zeebe.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ZeebeInstanceEntity implements Serializable, Cloneable {

    private Long key;
    private Long timestamp;
    private String rejectionReason;
    private String rejectionType;
    private String valueType;
    private String recordType;
    private String intent;
    private Long position;

    private Long sourceRecordPosition;
    private Value value;

    @Getter
    @Setter
    public static class Value implements Serializable, Cloneable {

        private String errorMessage;
        private String errorType;
        private Long jobKey;
        private Long variableScopeKey;
        private Long processInstanceKey;
        private String elementId;
        private String elementInstanceKey;
        private Long flowScopeKey;
        private Long parentElementInstanceKey;
        private String bpmnProcessId;
        private Integer version;
        private String bpmnElementType;
        private Long parentProcessInstanceKey;
        private Long processDefinitionKey;


        @Override
        protected Value clone() throws CloneNotSupportedException {
            return (Value) super.clone();
        }
    }

    @Override
    public ZeebeInstanceEntity clone() throws CloneNotSupportedException {
        ZeebeInstanceEntity instance = (ZeebeInstanceEntity) super.clone();
        instance.setValue(this.value == null ? null : this.value.clone());
        return instance;
    }
}
