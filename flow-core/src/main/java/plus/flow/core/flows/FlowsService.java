package plus.flow.core.flows;

import reactor.core.publisher.Flux;

public interface FlowsService {

    Flux<Flow> getFlowsByEvent(String source);


}
