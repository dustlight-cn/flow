package cn.dustlight.flow.zeebe.services.usertask;

import cn.dustlight.flow.zeebe.entities.ZeebeUserTask;
import cn.dustlight.flow.zeebe.entities.ZeebeUserTaskEntity;
import io.camunda.zeebe.client.ZeebeClient;
import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.Assert;
import cn.dustlight.flow.core.exceptions.ErrorEnum;
import cn.dustlight.flow.core.flow.usertask.AbstractUserTaskService;
import cn.dustlight.flow.core.flow.usertask.UserTaskDataValidator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class ZeebeUserTaskService extends AbstractUserTaskService<ZeebeUserTask> implements InitializingBean {

    private ReactiveElasticsearchOperations operations;

    private ZeebeClient zeebeClient;

    private String index = "flow-user-task";

    public ZeebeUserTaskService(UserTaskDataValidator validator) {
        super(validator);
    }

    public ZeebeUserTaskService(UserTaskDataValidator validator,
                                ZeebeClient zeebeClient,
                                ReactiveElasticsearchOperations operations) {
        this(validator);
        this.operations = operations;
        this.zeebeClient = zeebeClient;
    }

    @Override
    protected Mono<Void> doComplete(String clientId,
                                    Long id,
                                    String user,
                                    Map<String, Object> data,
                                    ZeebeUserTask userTask) {
        return Mono.create(sink -> sink.onRequest(unused -> zeebeClient.newCompleteCommand(userTask.getId())
                        .variables(data)
                        .send()
                        .whenComplete((completeJobResponse, throwable) -> {
                            if (throwable != null)
                                sink.error(ErrorEnum.UNKNOWN.details(throwable).getException());
                            else
                                sink.success(System.currentTimeMillis());
                        })))
                .flatMap((unused) -> operations.save(userTask.complete(user, data).entity(), IndexCoordinates.of(index)))
                .then();
    }

    @Override
    public Flux<ZeebeUserTask> getTasks(String clientId,
                                        Collection<String> users,
                                        Collection<String> roles,
                                        int page,
                                        int size) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.filter(new PrefixQueryBuilder("bpmnProcessId.keyword", String.format("c%s-", clientId)));
        if (users != null && users.size() > 0)
            boolQueryBuilder.filter(new TermsQueryBuilder("target.users", users));
        if (roles != null && roles.size() > 0)
            boolQueryBuilder.filter(new TermsQueryBuilder("target.roles", roles));
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(Pageable.ofSize(size).withPage(page))
                .build();
        return operations.searchForPage(query, ZeebeUserTaskEntity.class, IndexCoordinates.of(index))
                .flatMapMany(searchHits -> Flux.fromIterable(searchHits.getContent()))
                .map(zeebeUserTaskEntitySearchHit -> new ZeebeUserTask(zeebeUserTaskEntitySearchHit.getContent()));
    }

    @Override
    public Mono<ZeebeUserTask> getTask(String clientId, Long id) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.filter(new TermQueryBuilder("key", id))
                .filter(new PrefixQueryBuilder("bpmnProcessId.keyword", String.format("c%s-", clientId)));
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(Pageable.ofSize(1))
                .build();
        return operations.search(query, ZeebeUserTaskEntity.class, IndexCoordinates.of(index))
                .singleOrEmpty()
                .switchIfEmpty(Mono.error(ErrorEnum.USER_TASK_NOT_FOUND.getException()))
                .map(zeebeUserTaskEntitySearchHit -> new ZeebeUserTask(zeebeUserTaskEntitySearchHit.getContent()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(operations, "ReactiveElasticsearchOperations must be set!");
        Assert.notNull(zeebeClient, "ZeebeClient must be set!");
    }
}