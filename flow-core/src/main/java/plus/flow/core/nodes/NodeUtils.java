package plus.flow.core.nodes;

import plus.flow.core.nodes.impls.ScriptNode;

public class NodeUtils {


    public static Class<? extends Node> getNodeClass(NodeType type) {
        switch (type) {
            case SCRIPT:
            default:
                return ScriptNode.class;
        }
    }

    public static NodeType getNodeType(String type) {
        if (type == null)
            return NodeType.SCRIPT;
        switch (type.toUpperCase()) {
            case "SCRIPT":
            default:
                return NodeType.SCRIPT;
        }
    }
}
