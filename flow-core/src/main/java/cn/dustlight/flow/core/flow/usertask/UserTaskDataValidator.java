package cn.dustlight.flow.core.flow.usertask;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface UserTaskDataValidator {

    Mono<Boolean> verify(String form,String clientId, Map<String, Object> data);

}
