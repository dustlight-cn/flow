package plus.flow.zeebe;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "plus.flow.zeebe")
public class ZeebeProperties {

    private String address = "localhost:26500";
    private boolean plaintext = true;

}
