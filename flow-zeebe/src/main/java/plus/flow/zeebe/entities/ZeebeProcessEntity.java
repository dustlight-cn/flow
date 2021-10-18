package plus.flow.zeebe.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ZeebeProcessEntity implements Serializable, Cloneable {

    private Long key;
    private Long timestamp;
    private Value value;

    @Getter
    @Setter
    public static class Value implements Serializable, Cloneable {

        private String resource;
        private Integer version;
        private String resourceName;
        private String bpmnProcessId;

        @Override
        protected Value clone() throws CloneNotSupportedException {
            return (Value) super.clone();
        }
    }

    @Override
    public ZeebeProcessEntity clone() throws CloneNotSupportedException {
        ZeebeProcessEntity instance = (ZeebeProcessEntity) super.clone();
        instance.setValue(this.value == null ? null : this.value.clone());
        return instance;
    }
}
