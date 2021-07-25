package plus.flow.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import plus.auth.resources.AuthPrincipalUtil;
import plus.auth.resources.core.AuthPrincipal;
import plus.flow.core.events.Event;
import plus.flow.core.pipelines.Pipeline;
import plus.flow.core.pipelines.PipelineService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Tag(name = "Pipeline", description = "流水线")
@RestController
@RequestMapping("/v1/")
@SecurityRequirement(name = "auth")
@CrossOrigin
public class PipelineController {

    @Autowired
    PipelineService pipelineService;

    @Operation(summary = "创建流水线")
    @PostMapping("pipelines")
    public Mono<Pipeline> createPipeline(@RequestBody Pipeline pipeline,
                                         AbstractOAuth2TokenAuthenticationToken token) {
        AuthPrincipal principal = AuthPrincipalUtil.getAuthPrincipal(token);
        pipeline.setId(null);
        if (pipeline.getOwner() == null)
            pipeline.setOwner(new HashSet<>());
        pipeline.getOwner().add(principal.getUidString());
        pipeline.setClientId(principal.getClientId());
        return pipelineService.createPipeline(pipeline);
    }

    @Operation(summary = "获取流水线")
    @GetMapping("pipelines/{id}")
    public Mono<Pipeline> getPipeline(@PathVariable String id,
                                      AbstractOAuth2TokenAuthenticationToken token) {
        AuthPrincipal principal = AuthPrincipalUtil.getAuthPrincipal(token);
        return pipelineService.getPipeline(id);
    }

    @Operation(summary = "删除流水线")
    @DeleteMapping("pipelines/{id}")
    public Mono<Void> deletePipeline(@PathVariable String id,
                                     AbstractOAuth2TokenAuthenticationToken token) {
        AuthPrincipal principal = AuthPrincipalUtil.getAuthPrincipal(token);
        return pipelineService.deletePipeline(id);
    }

    @Operation(summary = "更新流水线")
    @PutMapping("pipelines/{id}")
    public Mono<Pipeline> updatePipeline(@PathVariable String id,
                                         @RequestBody Pipeline pipeline,
                                         AbstractOAuth2TokenAuthenticationToken token) {
        AuthPrincipal principal = AuthPrincipalUtil.getAuthPrincipal(token);
        pipeline.setClientId(null);
        pipeline.setCreatedAt(null);
        if (pipeline.getOwner() != null)
            pipeline.getOwner().add(principal.getUidString());
        return pipelineService.updatePipeline(id, pipeline);
    }

    @Operation(summary = "查找表单流水线")
    @GetMapping("forms/{formName}/instances")
    public Flux<Pipeline> getFormPipelines(@PathVariable String formName,
                                           @RequestParam(defaultValue = "CREATE_RECORD") String eventType,
                                           AbstractOAuth2TokenAuthenticationToken token) {
        AuthPrincipal principal = AuthPrincipalUtil.getAuthPrincipal(token);
        return pipelineService.getPipelines(new Event(eventType, formName, null));
    }

}
