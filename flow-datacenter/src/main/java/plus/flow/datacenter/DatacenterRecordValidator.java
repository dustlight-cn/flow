package plus.flow.datacenter;

import plus.flow.core.flow.usertask.UserTaskDataValidator;
import reactor.core.publisher.Mono;

import java.util.Map;

public class DatacenterRecordValidator implements UserTaskDataValidator {

    @Override
    public Mono<Void> verify(String form, Map<String, Object> data) {
        return null;
    }

}
