package plus.flow.zeebe.services;

import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import io.camunda.zeebe.model.bpmn.instance.*;
import io.camunda.zeebe.model.bpmn.instance.zeebe.ZeebeTaskDefinition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;

/**
 * 多租户适配器
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MultiTenantAdapter extends AbstractZeebeProcessAdapter {

    private Set<String> systemPrefix;

    @Override
    public void adapt(BpmnModelInstance instance, AdapterContext context) throws Exception {
        String prefix = String.format("%s.", context.getClientId());

        instance.getDefinitions().getBpmDiagrams().forEach(bpmnDiagram -> {
            BaseElement elem = bpmnDiagram.getBpmnPlane().getBpmnElement();
            elem.setId(String.format("%s%s", prefix, elem.getId()));
        });

        Collection<ZeebeTaskDefinition> zeebeTaskDefinitions = instance.getModelElementsByType(ZeebeTaskDefinition.class);
        if (zeebeTaskDefinitions != null)
            zeebeTaskDefinitions.forEach(zeebeTaskDefinition -> {
                boolean isSystemPrefix = false;
                String type = zeebeTaskDefinition.getType();
                if (systemPrefix != null && systemPrefix.size() > 0) {
                    for (String p : systemPrefix) {
                        if (type.startsWith(p)) {
                            isSystemPrefix = true;
                            break;
                        }
                    }
                }
                if (!isSystemPrefix)
                    zeebeTaskDefinition.setType(String.format("%s%s", prefix, type));
            });

        Collection<Message> messages = instance.getModelElementsByType(Message.class);
        if (messages != null)
            messages.forEach(message -> {
                message.setName(String.format("%s%s", prefix, message.getName()));
            });
    }


    @Override
    public void reverse(BpmnModelInstance instance, AdapterContext context) throws Exception {
        String prefix = String.format("%s.", context.getClientId());

        instance.getDefinitions().getBpmDiagrams().forEach(bpmnDiagram -> {
            BaseElement elem = bpmnDiagram.getBpmnPlane().getBpmnElement();
            if (elem.getId().startsWith(prefix))
                elem.setId(elem.getId().substring(prefix.length()));
        });

        Collection<ZeebeTaskDefinition> zeebeTaskDefinitions = instance.getModelElementsByType(ZeebeTaskDefinition.class);
        if (zeebeTaskDefinitions != null)
            zeebeTaskDefinitions.forEach(zeebeTaskDefinition -> {
                if (zeebeTaskDefinition.getType().startsWith(prefix))
                    zeebeTaskDefinition.setType(zeebeTaskDefinition.getType().substring(prefix.length()));
            });

        Collection<Message> messages = instance.getModelElementsByType(Message.class);
        if (messages != null)
            messages.forEach(message -> {
                if (message.getName().startsWith(prefix))
                    message.setName(message.getName().substring(prefix.length()));
            });
    }

}
