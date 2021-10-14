package plus.flow.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import plus.auth.client.reactive.ReactiveAuthClient;
import plus.auth.resources.core.AuthPrincipal;
import plus.flow.application.ClientUtils;
import plus.flow.core.flow.InstanceService;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/v1")
@SecurityRequirement(name = "auth")
@Tag(name = "流程实例资源", description = "创建和查询流程实例")
public class InstanceController {

    @Autowired
    private InstanceService instanceService;

    @Operation(summary = "创建流程实例")
    @PostMapping(value = "/instance")
    public Mono<Void> createProcess(@RequestParam(name = "name") String name,
                                    @RequestParam(name = "cid", required = false) String clientId,
                                    @RequestBody Map<String, Object> variables,
                                    ReactiveAuthClient reactiveAuthClient,
                                    AuthPrincipal principal) {
        return ClientUtils.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> instanceService.start(cid, name, variables))
                .then();
    }
}
