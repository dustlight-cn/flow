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
import plus.flow.core.nodes.ExecuteLog;
import plus.flow.core.nodes.ExecutingException;
import plus.flow.core.nodes.Node;
import plus.flow.core.nodes.Result;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
            Context context = new Context(event, new HashSet<>());

            PipelineInstance instance = PipelineInstance.from(line);
            instances.add(instance);

            instance.setStatus(StatusType.RUNNING);
            instance.setContext(context);

            Stage[] stages = instance.getStages();
            if (stages == null || stages.length == 0) {
                instance.setStatus(StatusType.SUCCESS);
                continue;
            }
            int i = 0;
            for (Stage stage : stages) {
                instance.setCurrent(i++);

                Node[] before = stage.getBefore();
                Collection<Result> results1 = executeNodes(before, context, null);
                log(String.format("%s:%s", instance.getPipeline(), stage.getTitle()), results1);
                Check check = stage.getCheck();
                if (check != null) {
                    instance.setStatus(StatusType.BLOCKING);
                    break;
                }

                Node[] when = stage.getWhen();
                Collection<Result> results2 = executeNodes(when, context, null);
                if (!log(String.format("%s:%s", instance.getPipeline(), stage.getTitle()), results2)) {
                    instance.setStatus(StatusType.FAILED);
                    break;
                }

                Node[] after = stage.getAfter();
                Collection<Result> results3 = executeNodes(after, context, null);
                log(String.format("%s:%s", instance.getPipeline(), stage.getTitle()), results3);
            }
            if (instance.getStatus() == StatusType.RUNNING)
                instance.setStatus(StatusType.SUCCESS);
        }
        pipelineService.createPipelineInstances(instances).collectList().block();
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
}
