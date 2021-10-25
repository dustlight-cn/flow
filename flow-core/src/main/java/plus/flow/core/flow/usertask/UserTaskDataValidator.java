package plus.flow.core.flow.usertask;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface UserTaskDataValidator {

    Mono<Void> verify(String form, Map<String, Object> data);

}
