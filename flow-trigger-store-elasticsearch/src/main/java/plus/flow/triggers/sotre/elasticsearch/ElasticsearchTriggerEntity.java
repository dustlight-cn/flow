package plus.flow.triggers.sotre.elasticsearch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ElasticsearchTriggerEntity implements Serializable {

    private String key;

    private String operation;

    private String clientId;

    private Set<String> process;

    @Id
    private String id;

    public ElasticsearchTriggerEntity(String key, String operation, String clientId, Set<String> process) {
        this.key = key;
        this.operation = operation;
        this.clientId = clientId;
        this.process = process;
        resetId();
    }

    public void resetId() {
        this.id = String.format("%s-%s-%s", clientId, key, operation);
    }
}
