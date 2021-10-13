package plus.flow.zeebe;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ConfigurationProperties(prefix = "plus.flow.zeebe")
public class ZeebeProperties {

    private String address = "localhost:26500";
    private boolean plaintext = true;
    private boolean multiTenant = true;
    private Set<String> systemPrefix = new HashSet<>();
}
