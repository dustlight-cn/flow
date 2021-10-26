package plus.flow.core.flow.trigger;

import plus.flow.core.flow.instance.Instance;
import reactor.core.publisher.Flux;

import java.util.Map;

public interface FlowTrigger {

    Flux<Instance> onEvent(String clientId,
                           String key,
                           String operation,
                           Map<String, Object> data);

}
