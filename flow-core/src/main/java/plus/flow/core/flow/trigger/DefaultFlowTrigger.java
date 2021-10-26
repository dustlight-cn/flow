package plus.flow.core.flow.trigger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import plus.flow.core.flow.instance.Instance;
import plus.flow.core.flow.instance.InstanceService;
import reactor.core.publisher.Flux;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class DefaultFlowTrigger implements FlowTrigger {

    private InstanceService instanceService;
    private TriggerStore triggerStore;

    @Override
    public Flux<Instance> onEvent(String clientId,
                                  String key,
                                  String operation,
                                  Map<String, Object> data) {
        return triggerStore.getProcess(clientId, key, operation)
                .flatMap(process -> instanceService.start(clientId, process, data));
    }

}
