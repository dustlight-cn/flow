package cn.dustlight.flow.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dustlight.auth.resources.core.AuthPrincipal;
import cn.dustlight.flow.core.flow.usertask.UserTask;
import cn.dustlight.flow.core.flow.usertask.UserTaskService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequestMapping("/v1")
@SecurityRequirement(name = "auth")
@Tag(name = "UserTasks", description = "用户任务")
@CrossOrigin
public class UserTaskController {

    @Autowired
    private UserTaskService userTaskService;

    @Operation(summary = "获取用户任务")
    @GetMapping(value = "/task/{id}")
    public Mono<UserTask> getUserTask(@PathVariable(name = "id") Long id,
                                      AuthPrincipal principal) {
        return userTaskService.getTask(principal.getClientId(), id);
    }

    @Operation(summary = "获取用户任务")
    @GetMapping(value = "/tasks")
    public Flux<UserTask> getUserTasks(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                       @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                       AuthPrincipal principal) {
        return userTaskService.getTasks(principal.getClientId(),
                Set.of(principal.getUidString()),
                getRoles(principal),
                page,
                size);
    }

    @Operation(summary = "完成用户任务")
    @PostMapping(value = "/task/{id}/completion")
    public Mono<Void> completeUserTask(@PathVariable(name = "id") Long id,
                                       @RequestBody Map<String, Object> data,
                                       AuthPrincipal principal) {
        return userTaskService.complete(principal.getClientId(), id, principal.getUidString(), data);
    }

    public static Collection<String> getRoles(AuthPrincipal principal) {
        Collection<String> authorities;
        if (principal == null || (authorities = principal.getAuthorities()) == null || authorities.size() == 0)
            return Collections.emptySet();
        Set<String> result = new HashSet<>();
        for (String authority : authorities) {
            if (authority.startsWith("ROLE_"))
                result.add(authority.substring("ROLE_".length()));
        }
        return result;
    }
}
