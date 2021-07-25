package plus.flow.core.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import plus.flow.core.events.Event;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class Context implements Serializable {

    private Event event;

    private Map<String, Object> outputs;

    @Override
    public String toString() {
        return "Context{" +
                "event=" + event +
                '}';
    }
}
