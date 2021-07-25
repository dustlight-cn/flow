package plus.flow.mongo.services;

import com.mongodb.reactivestreams.client.MongoClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import plus.flow.core.events.Event;
import plus.flow.core.exceptions.ErrorEnum;
import plus.flow.core.pipelines.Pipeline;
import plus.flow.core.pipelines.PipelineInstance;
import plus.flow.core.pipelines.PipelineService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
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
        pipeline.setCreatedAt(Instant.now());
        pipeline.setUpdatedAt(pipeline.getCreatedAt());
        return operations.insert(pipeline, collection)
                .onErrorMap(throwable -> ErrorEnum.CREATE_PIPELINE_FAILED.details(throwable.getMessage()).getException())
                .switchIfEmpty(Mono.error(ErrorEnum.CREATE_PIPELINE_FAILED.getException()));
    }

    @Override
    public Mono<Pipeline> getPipeline(String id) {
        return operations.findById(id, Pipeline.class, collection)
                .onErrorMap(throwable -> ErrorEnum.UNKNOWN.details(throwable.getMessage()).getException())
                .switchIfEmpty(Mono.error(ErrorEnum.PIPELINE_NOT_FOUND.getException()));
    }

    @Override
    public Mono<Void> deletePipeline(String id) {
        return operations.findAndRemove(Query.query(Criteria.where("_id").is(id)), Pipeline.class, collection)
                .onErrorMap(throwable -> ErrorEnum.DELETE_PIPELINE_FAILED.details(throwable.getMessage()).getException())
                .switchIfEmpty(Mono.error(ErrorEnum.PIPELINE_NOT_FOUND.getException()))
                .then();
    }

    @Override
    public Mono<Pipeline> updatePipeline(String id, Pipeline pipeline) {
        pipeline.setId(null);
        pipeline.setUpdatedAt(Instant.now());

        Update update = Update.update("updatedAt", pipeline.getUpdatedAt());
        if (pipeline.getTitle() != null)
            update.set("title", pipeline.getTitle());
        if (pipeline.getDescription() != null)
            update.set("description", pipeline.getDescription());
        if (pipeline.getStages() != null)
            update.set("stages", pipeline.getStages());
        if (pipeline.getTrigger() != null)
            update.set("trigger", pipeline.getTrigger());
        return operations.findAndModify(Query.query(Criteria.where("_id").is(id)), update, Pipeline.class, collection)
                .onErrorMap(throwable -> ErrorEnum.UPDATE_PIPELINE_FAILED.details(throwable.getMessage()).getException())
                .switchIfEmpty(Mono.error(ErrorEnum.PIPELINE_NOT_FOUND.getException()));
    }

    @Override
    public Flux<Pipeline> getPipelines(Event event) {
        return operations.find(Query.query(Criteria.where("trigger")
                        .elemMatch(Criteria.where("type").is(event.getType())
                                .and("key").is(event.getKey())))
                , Pipeline.class, collection);
    }

    @Override
    public Flux<PipelineInstance> createInstances(Collection<PipelineInstance> instances) {
        Instant t = Instant.now();
        instances.forEach(pipelineInstance -> {
            pipelineInstance.setCreatedAt(t);
            pipelineInstance.setUpdatedAt(t);
        });
        return Mono.from(mongoClient.startSession())
                .flatMap(clientSession -> Mono.fromRunnable(() -> clientSession.startTransaction()).then(Mono.just(clientSession)))
                .flatMapMany(clientSession -> operations.withSession(clientSession)
                        .insert(instances, instanceCollection)
                        .onErrorMap(throwable -> ErrorEnum.CREATE_INSTANCE_FAILED.details(throwable.getMessage()).getException())
                        .onErrorResume(throwable -> Mono.from(clientSession.abortTransaction()).then(Mono.error(throwable)))
                        .collectList()
                        .flatMapMany(pipelineInstances -> Flux.from(clientSession.commitTransaction()).thenMany(Flux.fromIterable(pipelineInstances)))
                        .doFinally(signalType -> clientSession.close())
                );
    }

    @Override
    public Flux<PipelineInstance> getInstancesByRecord(Event event, String clientId, String recordId) {
        return operations.find(Query.query(Criteria.where("context.event.type").is(event.getType())
                        .and("context.event.key").is(event.getKey())
                        .and("context.event.data.recordId").is(recordId)
                        .and("clientId").is(clientId))
                , PipelineInstance.class, instanceCollection);
    }

    @Override
    public Mono<PipelineInstance> getInstance(String id) {
        return operations.findById(id, PipelineInstance.class, instanceCollection)
                .switchIfEmpty(Mono.error(ErrorEnum.INSTANCE_NOT_FOUND.getException()));
    }

    @Override
    public Mono<PipelineInstance> updateInstance(String id, PipelineInstance instance) {
        instance.setUpdatedAt(Instant.now());
        Update update = new Update();

        update.set("updatedAt", instance.getUpdatedAt());
        if (instance.getStatus() != null)
            update.set("status", instance.getStatus());
        if (instance.getContext() != null)
            update.set("context", instance.getContext());
        if (instance.getCurrent() != null)
            update.set("current", instance.getCurrent());

        return operations.findAndModify(Query.query(Criteria.where("id").is(id)), update, PipelineInstance.class, instanceCollection)
                .onErrorMap(throwable -> ErrorEnum.UPDATE_INSTANCE_FAILED.details(throwable.getMessage()).getException())
                .switchIfEmpty(Mono.error(ErrorEnum.INSTANCE_NOT_FOUND.getException()));
    }

    @Override
    public Flux<PipelineInstance> updateInstances(Collection<PipelineInstance> instances) {
        Instant t = Instant.now();
        instances.forEach(pipelineInstance -> pipelineInstance.setUpdatedAt(t));
        return Mono.from(mongoClient.startSession())
                .flatMap(clientSession -> Mono.fromRunnable(() -> clientSession.startTransaction()).then(Mono.just(clientSession)))
                .flatMapMany(clientSession -> operations.withSession(clientSession)
                        .save(instances, instanceCollection).flatMapMany(instances1 -> Flux.fromIterable(instances1))
                        .onErrorMap(throwable -> ErrorEnum.UPDATE_INSTANCE_FAILED.details(throwable.getMessage()).getException())
                        .onErrorResume(throwable -> Mono.from(clientSession.abortTransaction()).then(Mono.error(throwable)))
                        .collectList()
                        .flatMapMany(pipelineInstances -> Flux.from(clientSession.commitTransaction()).thenMany(Flux.fromIterable(pipelineInstances)))
                        .doFinally(signalType -> clientSession.close())
                );
    }
}
