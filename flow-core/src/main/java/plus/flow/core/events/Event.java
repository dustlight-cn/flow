package plus.flow.core.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Serializable {

    private String type;

    private String key;

    private Object data;

    @Override
    public String toString() {
        return "Event{" +
                "type='" + type + '\'' +
                ", key='" + key + '\'' +
                ", data=" + data +
                '}';
    }
}
