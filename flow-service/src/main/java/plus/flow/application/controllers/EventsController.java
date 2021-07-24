package plus.flow.application.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plus.flow.core.events.EventSource;
import plus.flow.core.pipelines.PipelineExecutor;
import reactor.core.publisher.Flux;

@Tag(name = "Events", description = "事件源")
@RestController
@RequestMapping("/v1/events")
@SecurityRequirement(name = "auth")
@CrossOrigin
public class EventsController {

    @Autowired
    PipelineExecutor executor;

    @GetMapping("/sources")
    public Flux<EventSource> getSources() {
        return Flux.fromIterable(executor.getSources());
    }

}
