package plus.flow.core.pipelines;

import plus.flow.core.events.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Collection;

public interface PipelineService extends Serializable {

    Mono<Pipeline> createPipeline(Pipeline pipeline);

    Mono<Pipeline> getPipeline(Event event);

    Flux<Pipeline> getPipelines(Event event);

    Flux<PipelineInstance> createPipelineInstances(Collection<PipelineInstance> instances);

    Flux<PipelineInstance> updateInstances(Collection<PipelineInstance> instances);

}
