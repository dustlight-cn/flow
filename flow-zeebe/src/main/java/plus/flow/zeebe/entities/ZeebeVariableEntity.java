package plus.flow.zeebe.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ZeebeVariableEntity implements Serializable {

    private Long key;
    private Long timestamp;
    private Value value;

    @Getter
    @Setter
    public static class Value implements Serializable {

        private String name;
        private Object value;

        @Override
        protected Value clone() throws CloneNotSupportedException {
            return (Value) super.clone();
        }
    }

}
