package plus.flow.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plus.flow.core.nodes.impls.ServerlessNode;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Tag(name = "Serverless", description = "无服务资源")
@RestController
@RequestMapping("/v1/serverless")
@SecurityRequirement(name = "auth")
@CrossOrigin
public class FunctionController {

    @Autowired
    ServerlessNode.FunctionService functionService;

    @Operation(summary = "获取全部函数")
    @GetMapping("/functions")
    public Mono<Collection<String>> getSources() {
        return functionService.getFunctionNames();
    }

}
