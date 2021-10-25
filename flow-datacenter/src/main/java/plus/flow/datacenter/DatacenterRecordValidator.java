package plus.flow.datacenter;

import plus.flow.core.flow.usertask.UserTaskDataValidator;
import reactor.core.publisher.Mono;

import java.util.Map;

public class DatacenterRecordValidator implements UserTaskDataValidator {

    @Override
    public Mono<Boolean> verify(String form, Map<String, Object> data) {
        return Mono.just(true);
    }

}
