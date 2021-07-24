package plus.flow.core.nodes;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;

@JacksonStdImpl
public class NodeDeserializer extends StdScalarDeserializer<Node> {

    private Gson gson = Converters.registerInstant(new GsonBuilder()).create();

    protected NodeDeserializer() {
        super(Node.class);
    }

    protected NodeDeserializer(Class<?> vc) {
        super(vc);
    }

    protected NodeDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected NodeDeserializer(StdScalarDeserializer<?> src) {
        super(src);
    }

    @Override
    public Node deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        HashMap tmp = jsonParser.readValueAs(HashMap.class);
        if (tmp == null)
            return null;
        NodeType type = NodeUtils.getNodeType(tmp.get("type") == null ? "SCRIPT" : tmp.get("type").toString());
        String json = gson.toJson(tmp);
        Node node = gson.fromJson(json, NodeUtils.getNodeClass(type));
        return node;
    }
}
