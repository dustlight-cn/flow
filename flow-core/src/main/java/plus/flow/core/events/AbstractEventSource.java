package plus.flow.core.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import plus.flow.core.flows.*;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
public abstract class AbstractEventSource implements EventSource {

    private FlowsService flowsService;
    private TaskService taskService;

    @Override
    public Mono<Void> handle(Object event) {
        return flowsService.getFlowsByEvent(getName())
                .collectList()
                .flatMap(flows -> {
                    Instant t = Instant.now();
                    Collection<Task> tasks = new HashSet<>();
                    for (Flow flow : flows) {
                        Task task = new Task();
                        tasks.add(task);
                        task.setCurrent(flow.getMain());
                        task.setCreatedAt(t);

                        task.setFlowId(flow.getId());
                        task.setFlowName(flow.getName());
                        task.setFlowVersion(flow.getVersion());

                        task.setOwner(flow.getOwner());
                        task.setMembers(flow.getMembers());
                    }
                    return taskService.saveTasks(tasks).then();
                });
    }
}
