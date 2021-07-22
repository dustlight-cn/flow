package plus.flow.core.flows;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class Line implements Serializable {

    private String from;
    private String to;

    private Map<String,String> mapping;
}
