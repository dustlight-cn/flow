package plus.flow.core.flows;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public abstract class AbstractTaskService implements TaskService {

    private TaskStarter taskStarter;

    protected Mono<Void> beforeCommit(Collection<Task> tasks) {
        return taskStarter.startTasks(tasks);
    }
}
