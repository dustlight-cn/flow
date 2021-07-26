package plus.flow.core.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "plus.flow.serverless")
public class ServerlessProperties {

    private String endpoint;

}
