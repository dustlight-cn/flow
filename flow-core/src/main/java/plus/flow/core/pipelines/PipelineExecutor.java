package plus.flow.core.pipelines;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import plus.flow.core.context.Context;
import plus.flow.core.events.Event;
import plus.flow.core.events.EventListener;
import plus.flow.core.events.EventSource;
import plus.flow.core.exceptions.ErrorEnum;
import plus.flow.core.nodes.ExecuteLog;
import plus.flow.core.nodes.ExecutingException;
import plus.flow.core.nodes.Node;
import plus.flow.core.nodes.Result;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class PipelineExecutor implements EventListener {

    private Collection<EventSource> sources;
    private PipelineService pipelineService;

    @Override
    public void onEvent(Event event) {
        List<Pipeline> lines = pipelineService.getPipelines(event).collectList().block();
        if (lines == null || lines.isEmpty())
            return;
        Collection<PipelineInstance> instances = new HashSet<>();
        for (Pipeline line : lines) {
            Context context = new Context(event, new HashMap<>());

            PipelineInstance instance = PipelineInstance.from(line);
            instances.add(instance);

            instance.setStatus(StatusType.RUNNING);
            instance.setContext(context);

            Stage[] stages = instance.getStages();
            if (stages == null || stages.length == 0) {
                Node[] onSuccessNodes = instance.getOnSuccess();
                Collection<Result> results = executeNodes(onSuccessNodes, context, null);
                log(String.format("%s-ON_SUCCESS", instance.getPipeline()), results);
                instance.setStatus(StatusType.SUCCESS);
                continue;
            }
            int i = 0;
            for (Stage stage : stages) {
                instance.setCurrent(i++);

                Node[] before = stage.getBefore();
                Collection<Result> results1 = executeNodes(before, context, null);
                context.getOutputs().putAll(collectOutputs(results1));
                log(String.format("%s:%s", instance.getPipeline(), stage.getTitle()), results1);
                Check check = stage.getCheck();
                if (check != null) {
                    instance.setStatus(StatusType.BLOCKING);
                    break;
                }

                Node[] when = stage.getWhen();
                Collection<Result> results2 = executeNodes(when, context, null);
                context.getOutputs().putAll(collectOutputs(results2));
                if (!log(String.format("%s:%s", instance.getPipeline(), stage.getTitle()), results2)) {
                    instance.setStatus(StatusType.FAILED);
                    break;
                }

                Node[] after = stage.getAfter();
                Collection<Result> results3 = executeNodes(after, context, null);
                context.getOutputs().putAll(collectOutputs(results3));
                log(String.format("%s:%s", instance.getPipeline(), stage.getTitle()), results3);
            }
            if (instance.getStatus() == StatusType.RUNNING) {
                Node[] onSuccessNodes = instance.getOnSuccess();
                Collection<Result> results = executeNodes(onSuccessNodes, context, null);
                log(String.format("%s-ON_SUCCESS", instance.getPipeline()), results);
                instance.setStatus(StatusType.SUCCESS);
            } else if (instance.getStatus() == StatusType.FAILED) {
                Node[] onFailedNodes = instance.getOnFailed();
                Collection<Result> results = executeNodes(onFailedNodes, context, null);
                log(String.format("%s-ON_FAILED", instance.getPipeline()), results);
            }
        }
        pipelineService.createInstances(instances).collectList().block();
    }

    public Mono<Void> onCheckpoint(String instanceId, boolean confirm, Map<String, Object> inputs, String token) {
        return pipelineService.getInstance(instanceId)
                .flatMap(instance -> {
                    if (instance.getStatus() != StatusType.BLOCKING)
                        return Mono.error(ErrorEnum.INSTANCE_NOT_BLOCKING.getException());
                    Context context = instance.getContext();
                    context.setToken(token);
                    if (context.getOutputs() == null)
                        context.setOutputs(new HashMap<>());

                    if (confirm) {

                        Integer current = instance.getCurrent();
                        if (current == null)
                            return Mono.error(ErrorEnum.UNKNOWN.details("current is null").getException());

                        instance.setStatus(StatusType.RUNNING);
                        Stage[] stages = instance.getStages();

                        for (int i = current, len = stages.length; i < len; i++) {
                            instance.setCurrent(i);
                            Stage stage = stages[i];
                            if (i != current) {
                                Node[] before = stage.getBefore();
                                Collection<Result> results1 = executeNodes(before, context, inputs);
                                context.getOutputs().putAll(collectOutputs(results1));
                                log(String.format("%s:%s", instance.getPipeline(), stage.getTitle()), results1);
                                Check check = stage.getCheck();
                                if (check != null) {
                                    instance.setStatus(StatusType.BLOCKING);
                                    break;
                                }
                            }


                            Node[] when = stage.getWhen();
                            Collection<Result> results2 = executeNodes(when, context, inputs);
                            context.getOutputs().putAll(collectOutputs(results2));
                            if (!log(String.format("%s:%s", instance.getPipeline(), stage.getTitle()), results2)) {
                                instance.setStatus(StatusType.FAILED);
                                break;
                            }

                            Node[] after = stage.getAfter();
                            Collection<Result> results3 = executeNodes(after, context, inputs);
                            context.getOutputs().putAll(collectOutputs(results3));
                            log(String.format("%s:%s", instance.getPipeline(), stage.getTitle()), results3);
                        }
                    } else {
                        instance.setStatus(StatusType.FAILED);
                    }
                    if (instance.getStatus() == StatusType.RUNNING) {
                        Node[] onSuccessNodes = instance.getOnSuccess();
                        Collection<Result> results = executeNodes(onSuccessNodes, context, inputs);
                        log(String.format("%s-ON_SUCCESS", instance.getPipeline()), results);
                        instance.setStatus(StatusType.SUCCESS);
                    } else if (instance.getStatus() == StatusType.FAILED) {
                        Node[] onFailedNodes = instance.getOnFailed();
                        Collection<Result> results = executeNodes(onFailedNodes, context, inputs);
                        log(String.format("%s-ON_FAILED", instance.getPipeline()), results);
                    }

                    return pipelineService.updateInstance(instanceId, (instance)).then();
                });
    }

    protected Collection<Result> executeNodes(Node[] nodes, Context context, Map<String, Object> inputs) {
        Collection<Result> results = new HashSet<>();
        if (nodes == null)
            return results;
        for (Node node : nodes) {
            try {
                Result result = node.execute(inputs, context).block();
                results.add(result);
            } catch (Throwable e) {
                Result result = new Result();
                result.setSuccess(false);
                result.setException(new ExecutingException(e.getMessage(), e));
                results.add(result);
            }
        }
        return results;
    }

    protected Flux<Result> executeNodesReactive(Node[] nodes, Context context, Map<String, Object> inputs) {
        if (nodes == null)
            return Flux.empty();
        return Flux.create(sink -> {
            sink.onRequest(value -> {
                Mono<Result> tmp = null;
                for (Node node : nodes) {
                    if (tmp == null)
                        tmp = node.execute(inputs, context)
                                .flatMap(result -> {
                                    sink.next(result);
                                    return Mono.just(result);
                                });
                    else
                        tmp = tmp.flatMap(result -> node.execute(inputs, context)
                                .flatMap(result1 -> {
                                    sink.next(result1);
                                    return Mono.just(result1);
                                })
                        );
                    tmp.onErrorResume(throwable -> {
                        sink.error(throwable);
                        return Mono.error(throwable);
                    });
                }
                tmp.flatMap(result -> {
                    sink.complete();
                    return Mono.just(result);
                });
            });
        });
    }

    protected boolean log(String name, Collection<Result> results) {
        if (results == null)
            return true;
        boolean success = true;
        for (Result result : results) {
            if (!result.isSuccess())
                success = false;

            if (result.getException() != null)
                LogFactory.getLog(name).error(result.getException());

            ExecuteLog log = result.getLog();
            if (log == null)
                continue;
            if (StringUtils.hasText(log.getInfo()))
                LogFactory.getLog(name).info(log.getInfo());
            if (StringUtils.hasText(log.getError()))
                LogFactory.getLog(name).error(log.getError());
        }
        return success;
    }

    protected Map<String, Object> collectOutputs(Collection<Result> results) {
        HashMap<String, Object> result = new HashMap<>();
        for (Result result1 : results) {
            if (result1.getOutput() != null)
                result.putAll(result1.getOutput());
        }
        return result;
    }
}
