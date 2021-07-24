package plus.flow.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "plus.flow.mongo")
public class FlowMongoProperties {

    private String pipelineCollection = "pipeline";
    private String pipelineInstanceCollection = "pipeline_instance";

}
