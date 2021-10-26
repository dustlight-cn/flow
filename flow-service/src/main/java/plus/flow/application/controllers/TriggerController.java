package plus.flow.application.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import plus.auth.client.reactive.ReactiveAuthClient;
import plus.auth.resources.core.AuthPrincipal;
import plus.flow.application.ClientUtils;
import plus.flow.core.exceptions.ErrorEnum;
import plus.flow.core.flow.trigger.FlowTrigger;
import plus.flow.core.flow.trigger.TriggerStore;
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

    @PutMapping("/process/{name}/trigger")
    public Mono<Void> setTrigger(@PathVariable(name = "name") String processes,
                                 @RequestParam(name = "key") String key,
                                 @RequestParam(name = "opt") String operation,
                                 @RequestParam(name = "cid") String clientId,
                                 AuthPrincipal principal,
                                 ReactiveAuthClient client) {
        if (!trigger.getSupportOperations().contains(operation))
            return Mono.error(ErrorEnum.TRIGGER_OPERATION_NOT_FOUND.getException());
        return ClientUtils.obtainClientId(client, clientId, principal)
                .flatMap(cid -> triggerStore.setSubscription(cid, key, operation, Set.of(processes)));
    }

    @GetMapping("/trigger-operations")
    public Mono<Collection<String>> getOperations() {
        return Mono.just(trigger.getSupportOperations());
    }
}
