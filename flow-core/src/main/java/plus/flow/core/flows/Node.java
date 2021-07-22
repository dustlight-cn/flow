package plus.flow.core.flows;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Node implements Serializable {

    private String name;
    private String title;
    private String description;
    private String function;

}
