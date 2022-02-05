package cn.dustlight.flow.datacenter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "dustlight.flow.datacenter")
public class FlowDatacenterProperties {

    private String endpoint = "https://datacenter.dustlight.cn";

}
