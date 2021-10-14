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
    public static class Value {
        private String resource;
        private Integer version;
        private String resourceName;
        private String bpmnProcessId;
    }

    @Override
    public ZeebeProcessEntity clone() throws CloneNotSupportedException {
        return (ZeebeProcessEntity) super.clone();
    }
}
