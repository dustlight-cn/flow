package plus.flow.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import plus.auth.client.reactive.ReactiveAuthClient;
import plus.auth.resources.core.AuthPrincipal;
import plus.flow.application.ClientUtils;
import plus.flow.core.flow.Instance;
import plus.flow.core.flow.InstanceService;
import reactor.core.publisher.Flux;
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
    public Mono<Instance> createInstance(@RequestParam(name = "name") String name,
                                         @RequestParam(name = "cid", required = false) String clientId,
                                         @RequestBody Map<String, Object> variables,
                                         ReactiveAuthClient reactiveAuthClient,
                                         AuthPrincipal principal) {
        return ClientUtils.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> instanceService.start(cid, name, variables));
    }

    @Operation(summary = "查询流程实例")
    @GetMapping(value = "/instances")
    public Flux<Instance> getInstances(@RequestParam(name = "name", required = false) String name,
                                       @RequestParam(name = "version", required = false) Integer version,
                                       @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                       @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                       @RequestParam(name = "cid", required = false) String clientId,
                                       ReactiveAuthClient reactiveAuthClient,
                                       AuthPrincipal principal) {
        return ClientUtils.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMapMany(cid -> instanceService.listInstance(cid, name, version, null, page, size));
    }

    @Operation(summary = "通过 ID 获取流程实例")
    @GetMapping(value = "/instance/{id}")
    public Mono<Instance> getInstance(@PathVariable Long id,
                                      @RequestParam(name = "cid", required = false) String clientId,
                                      ReactiveAuthClient reactiveAuthClient,
                                      AuthPrincipal principal) {
        return ClientUtils.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> instanceService.getInstance(cid, id));
    }

    @Operation(summary = "发布消息")
    @PostMapping(value = "/message")
    public Mono<Void> createMessage(@RequestParam(name = "name") String name,
                                    @RequestParam(name = "key") String key,
                                    @RequestParam(name = "cid", required = false) String clientId,
                                    ReactiveAuthClient reactiveAuthClient,
                                    AuthPrincipal principal) {
        return ClientUtils.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> instanceService.publishMessage(cid, name, key))
                .then();
    }
}
