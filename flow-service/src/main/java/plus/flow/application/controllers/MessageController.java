package plus.flow.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import plus.auth.client.reactive.ReactiveAuthClient;
import plus.auth.resources.AuthPrincipalUtil;
import plus.auth.resources.core.AuthPrincipal;
import plus.flow.core.flow.message.MessageService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
@SecurityRequirement(name = "auth")
@Tag(name = "Messages", description = "消息资源")
@CrossOrigin
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Operation(summary = "发布消息")
    @PostMapping(value = "/message")
    public Mono<Void> createMessage(@RequestParam(name = "name") String name,
                                    @RequestParam(name = "key") String key,
                                    @RequestParam(name = "cid", required = false) String clientId,
                                    ReactiveAuthClient reactiveAuthClient,
                                    AuthPrincipal principal) {
        return AuthPrincipalUtil.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> messageService.publishMessage(cid, name, key))
                .then();
    }
}
