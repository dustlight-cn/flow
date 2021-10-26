package plus.flow.triggers.sotre.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ElasticsearchTriggerEntity implements Serializable {

    @Id
    private String key;

    @Id
    private String operation;

    @Id
    private String clientId;

    private Set<String> process;

}
