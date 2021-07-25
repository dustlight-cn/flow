package plus.flow.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import plus.auth.resources.AuthPrincipalUtil;
import plus.auth.resources.core.AuthPrincipal;
import plus.flow.core.events.Event;
import plus.flow.core.pipelines.PipelineExecutor;
import plus.flow.core.pipelines.PipelineInstance;
import plus.flow.core.pipelines.PipelineService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Tag(name = "Instance", description = "流水线实例")
@RestController
@RequestMapping("/v1/")
@SecurityRequirement(name = "auth")
@CrossOrigin
public class InstanceController {

    @Autowired
    PipelineService pipelineService;

    @Autowired
    PipelineExecutor executor;

    @Operation(summary = "获取流水线实例")
    @GetMapping("instances/{id}")
    public Mono<PipelineInstance> getInstance(@PathVariable String id,
                                              AbstractOAuth2TokenAuthenticationToken token) {
        AuthPrincipal principal = AuthPrincipalUtil.getAuthPrincipal(token);
        return pipelineService.getInstance(id);
    }

    @Operation(summary = "确认检查点",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Object.class)))
    )
    @PostMapping("instances/{id}/checkpoint")
    public Mono<Void> confirmCheckpoint(@PathVariable String id,
                                        @RequestBody(required = false) Map<String, Object> data,
                                        AbstractOAuth2TokenAuthenticationToken token) {
        AuthPrincipal principal = AuthPrincipalUtil.getAuthPrincipal(token);
        return executor.onCheckpoint(id, true, data);
    }

    @Operation(summary = "否认检查点",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Object.class)))
    )
    @DeleteMapping("instances/{id}/checkpoint")
    public Mono<Void> denyCheckpoint(@PathVariable String id,
                                     @RequestBody(required = false) Map<String, Object> data,
                                     AbstractOAuth2TokenAuthenticationToken token) {
        AuthPrincipal principal = AuthPrincipalUtil.getAuthPrincipal(token);
        return executor.onCheckpoint(id, false, data);
    }

    @Operation(summary = "查找记录流水线实例")
    @GetMapping("record/{recordId}/instances")
    public Flux<PipelineInstance> getInstancesByRecord(@PathVariable String recordId,
                                                       @RequestParam String eventKey,
                                                       @RequestParam(defaultValue = "CREATE_RECORD") String eventType,
                                                       AbstractOAuth2TokenAuthenticationToken token) {
        AuthPrincipal principal = AuthPrincipalUtil.getAuthPrincipal(token);
        return pipelineService.getInstancesByRecord(new Event(eventType, eventKey, null),
                principal.getClientId(),
                recordId);
    }

}
