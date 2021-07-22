package plus.flow.core.flows;

import reactor.core.publisher.Mono;

import java.util.Collection;

public interface TaskStarter {

    Mono<Void> startTasks(Collection<Task> tasks);
}
