package plus.flow.mongo.functions;

import com.mongodb.reactivestreams.client.MongoClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import plus.flow.core.events.Event;
import plus.flow.core.pipelines.Pipeline;
import plus.flow.core.pipelines.PipelineInstance;
import plus.flow.core.pipelines.PipelineService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class MongoPipelineService implements PipelineService {

    private MongoClient mongoClient;

    private ReactiveMongoOperations operations;

    private String collection;
    private String instanceCollection;

    @Override
    public Mono<Pipeline> createPipeline(Pipeline pipeline) {
        return operations.insert(pipeline, collection);
    }

    @Override
    public Mono<Pipeline> getPipeline(Event event) {
        return null;
    }

    @Override
    public Flux<Pipeline> getPipelines(Event event) {
        return operations.find(Query.query(Criteria.where("trigger")
                        .elemMatch(Criteria.where("type").is(event.getType())
                                .and("key").is(event.getKey())))
                , Pipeline.class, collection);
    }

    @Override
    public Flux<PipelineInstance> createPipelineInstances(Collection<PipelineInstance> instances) {
        return operations.insert(instances, instanceCollection);
    }

    @Override
    public Flux<PipelineInstance> updateInstances(Collection<PipelineInstance> instances) {
        return operations.save(instances, instanceCollection).flatMapMany(instances1 -> Flux.fromIterable(instances1));
    }
}
