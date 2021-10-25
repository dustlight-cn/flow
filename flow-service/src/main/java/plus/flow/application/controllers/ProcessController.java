package plus.flow.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import plus.auth.client.reactive.ReactiveAuthClient;
import plus.auth.resources.core.AuthPrincipal;
import plus.flow.application.ClientUtils;
import plus.flow.core.flow.process.Process;
import plus.flow.core.flow.process.ProcessService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/v1")
@SecurityRequirement(name = "auth")
@Tag(name = "Processes", description = "流程资源")
@CrossOrigin
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @Operation(summary = "创建流程")
    @PostMapping(value = "/process"
            , consumes = {"application/xml", "text/plain; charset: utf-8", "text/xml; charset: utf-8"}
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> createProcess(@RequestParam(name = "cid", required = false) String clientId,
                                    @RequestParam(name = "base64", required = false, defaultValue = "false") boolean isBase64,
                                    @RequestBody String data,
                                    ReactiveAuthClient reactiveAuthClient,
                                    AuthPrincipal principal) {
        return ClientUtils.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> processService.createProcess(cid,
                        principal.getUidString(),
                        isBase64 ? data : Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8))
                ))
                .then();
    }

    @Operation(summary = "通过名称获取流程")
    @GetMapping("/process/{name}")
    public Mono<Process> getProcess(@PathVariable String name,
                                    @RequestParam(name = "cid", required = false) String clientId,
                                    ReactiveAuthClient reactiveAuthClient,
                                    AuthPrincipal principal) {
        return ClientUtils.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> processService.getProcess(cid, name, null));
    }

    @Operation(summary = "通过名称与版本号获取流程")
    @GetMapping("/process/{name}/{version}")
    public Mono<Process> getProcess(@PathVariable String name,
                                    @PathVariable Integer version,
                                    @RequestParam(name = "cid", required = false) String clientId,
                                    ReactiveAuthClient reactiveAuthClient,
                                    AuthPrincipal principal) {
        return ClientUtils.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> processService.getProcess(cid, name, version));
    }

    @Operation(summary = "获取流程列表")
    @GetMapping("/processes")
    public Flux<Process> getProcesses(@RequestParam(name = "q", required = false) String keyword,
                                      @RequestParam(name = "cid", required = false) String clientId,
                                      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                      ReactiveAuthClient reactiveAuthClient,
                                      AuthPrincipal principal) {
        return ClientUtils.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMapMany(cid -> processService.findProcess(cid, keyword, page, size));
    }
}
