package cn.dustlight.flow.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import cn.dustlight.auth.client.reactive.ReactiveAuthClient;
import cn.dustlight.auth.resources.AuthPrincipalUtil;
import cn.dustlight.auth.resources.core.AuthPrincipal;
import cn.dustlight.flow.core.exceptions.ErrorEnum;
import cn.dustlight.flow.core.flow.process.ProcessService;
import cn.dustlight.flow.core.flow.trigger.FlowTrigger;
import cn.dustlight.flow.core.flow.trigger.TriggerStore;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/v1")
@SecurityRequirement(name = "auth")
@Tag(name = "Triggers", description = "触发器相关")
@CrossOrigin
public class TriggerController {

    @Autowired
    private TriggerStore triggerStore;

    @Autowired
    private FlowTrigger trigger;

    @Autowired
    private ProcessService processService;

    @Operation(summary = "设置触发器")
    @PutMapping("/trigger/{key}")
    public Mono<Void> setTriggerProcess(@PathVariable(name = "key") String key,
                                        @RequestParam(name = "opt") String operation,
                                        @RequestBody Set<String> processes,
                                        @RequestParam(name = "cid", required = false) String clientId,
                                        AuthPrincipal principal,
                                        ReactiveAuthClient client) {
        if (!trigger.getSupportOperations().contains(operation))
            return Mono.error(ErrorEnum.TRIGGER_OPERATION_NOT_FOUND.getException());
        return AuthPrincipalUtil.obtainClientId(client, clientId, principal)
                .flatMap(cid ->
                        processService.isProcessExists(cid, processes)
                                .flatMap(flag -> (Boolean) flag ?
                                        triggerStore.setSubscription(cid, key, operation, processes) :
                                        Mono.error(ErrorEnum.PROCESS_NOT_FOUND.getException())
                                ));
    }

    @Operation(summary = "获取触发器的目标流程")
    @GetMapping("/trigger/{key}")
    public Mono<Collection<String>> getTriggerProcess(@PathVariable(name = "key") String key,
                                                      @RequestParam(name = "opt", required = false) String operation,
                                                      @RequestParam(name = "cid", required = false) String clientId,
                                                      AuthPrincipal principal,
                                                      ReactiveAuthClient client) {
        if (!StringUtils.hasText(operation))
            operation = null;
        if (StringUtils.hasText(operation) && !trigger.getSupportOperations().contains(operation))
            return Mono.error(ErrorEnum.TRIGGER_OPERATION_NOT_FOUND.getException());
        String finalOperation = operation;
        return AuthPrincipalUtil.obtainClientId(client, clientId, principal)
                .flatMap(cid -> triggerStore.getProcess(cid, key, finalOperation).collectList());
    }

    @Operation(summary = "获取流程关联的触发器")
    @GetMapping("/process/{process}/trigger")
    public Mono<Collection<String>> getTriggerKeys(@PathVariable(name = "process") String process,
                                                   @RequestParam(name = "opt", required = false) String operation,
                                                   @RequestParam(name = "cid", required = false) String clientId,
                                                   AuthPrincipal principal,
                                                   ReactiveAuthClient client) {
        if (!StringUtils.hasText(operation))
            operation = null;
        if (StringUtils.hasText(operation) && !trigger.getSupportOperations().contains(operation))
            return Mono.error(ErrorEnum.TRIGGER_OPERATION_NOT_FOUND.getException());
        String finalOperation = operation;
        return AuthPrincipalUtil.obtainClientId(client, clientId, principal)
                .flatMap(cid -> triggerStore.getSubscription(cid, process, finalOperation).collectList());
    }

    @Operation(summary = "获取触发器支持的事件")
    @GetMapping("/trigger-operations")
    public Mono<Collection<String>> getOperations() {
        return Mono.just(trigger.getSupportOperations());
    }

}
