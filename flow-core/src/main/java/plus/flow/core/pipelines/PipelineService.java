package plus.flow.core.pipelines;

import plus.flow.core.events.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Collection;

public interface PipelineService extends Serializable {

    Mono<Pipeline> createPipeline(Pipeline pipeline);

    Mono<Pipeline> getPipeline(String id);

    Mono<Void> deletePipeline(String id);

    Mono<Pipeline> updatePipeline(String id, Pipeline pipeline);

    Flux<Pipeline> getPipelines(Event event);

    Flux<PipelineInstance> createInstances(Collection<PipelineInstance> instances);

    Flux<PipelineInstance> getInstancesByRecord(Event event, String clientId, String recordId);

    Mono<PipelineInstance> getInstance(String id);

    Mono<PipelineInstance> updateInstance(String id, PipelineInstance instance);

    Flux<PipelineInstance> updateInstances(Collection<PipelineInstance> instances);

}
