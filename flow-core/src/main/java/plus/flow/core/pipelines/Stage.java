package plus.flow.core.pipelines;

import lombok.Getter;
import lombok.Setter;
import plus.flow.core.nodes.Node;

import java.io.Serializable;

@Getter
@Setter
public class Stage implements Serializable {

    private String title;

    private Node[] before;
    private Node[] when;
    private Node[] after;

    private Check check;
}
