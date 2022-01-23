package cn.dustlight.flow.triggers.sotre.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "dustlight.flow.triggers.store.elasticsearch")
public class ElasticsearchTriggerStoreProperties {

    private String index = "flow-triggers";

}
