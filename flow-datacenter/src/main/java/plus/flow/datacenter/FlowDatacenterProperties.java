package plus.flow.datacenter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "plus.flow.datacenter")
public class FlowDatacenterProperties {

    private String exchange = "datacenter";

}
