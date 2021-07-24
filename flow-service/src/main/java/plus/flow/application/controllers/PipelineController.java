package plus.flow.application.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import plus.auth.resources.AuthPrincipalUtil;
import plus.auth.resources.core.AuthPrincipal;
import plus.flow.core.pipelines.Pipeline;
import plus.flow.core.pipelines.PipelineInstance;
import plus.flow.core.pipelines.PipelineService;
import reactor.core.publisher.Mono;

@Tag(name = "Pipelines", description = "流水线")
@RestController
@RequestMapping("/v1/pipelines")
@SecurityRequirement(name = "auth")
@CrossOrigin
public class PipelineController {

    @Autowired
    PipelineService pipelineService;

    @PostMapping
    public Mono<Pipeline> createPipeline(@RequestBody Pipeline pipeline,
                                         AbstractOAuth2TokenAuthenticationToken token) {
        AuthPrincipal principal = AuthPrincipalUtil.getAuthPrincipal(token);
        return pipelineService.createPipeline(pipeline);
    }

    @GetMapping
    public Mono<PipelineInstance> get(@RequestBody Pipeline pipeline,
                                                 AbstractOAuth2TokenAuthenticationToken token) {
        AuthPrincipal principal = AuthPrincipalUtil.getAuthPrincipal(token);
        return null;
    }
}
