package cn.dustlight.flow.core.flow.trigger;

import cn.dustlight.flow.core.flow.instance.Instance;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Map;

public interface FlowTrigger {

    Flux<Instance> onEvent(String clientId,
                           String key,
                           String operation,
                           Map<String, Object> data);

    Collection<String> getSupportOperations();
}
