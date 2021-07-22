package plus.flow.core.flows;

import reactor.core.publisher.Flux;

import java.util.Collection;

public interface TaskService {

    Flux<Task> saveTasks(Collection<Task> tasks);

}
