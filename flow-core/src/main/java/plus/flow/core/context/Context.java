package plus.flow.core.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import plus.flow.core.events.Event;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Context implements Serializable {

    private Event event;

    private Map<String, Object> outputs;

    private Map<String, String> env;

    @JsonIgnore
    private String token;

    public Context(Event event) {
        this.event = event;
    }

    public Context(Event event, Map<String, Object> outputs) {
        this.event = event;
        this.outputs = outputs;
    }

    public Context(Event event, Map<String, Object> outputs, Map<String, String> env) {
        this.event = event;
        this.outputs = outputs;
        this.env = env;
    }

    @Override
    public String toString() {
        return "Context{" +
                "event=" + event +
                ", outputs=" + outputs +
                ", token='" + token + '\'' +
                '}';
    }
}
