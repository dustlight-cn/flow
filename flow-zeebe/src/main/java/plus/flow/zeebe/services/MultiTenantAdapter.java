package plus.flow.zeebe.services;

import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import io.camunda.zeebe.model.bpmn.instance.*;
import io.camunda.zeebe.model.bpmn.instance.zeebe.ZeebeTaskDefinition;

import java.util.Collection;

/**
 * 多租户适配器
 */
public class MultiTenantAdapter extends AbstractZeebeProcessAdapter {

    @Override
    public void adapt(BpmnModelInstance instance, AdapterContext context) throws Exception {
        String prefix = String.format("%s.", context.getClientId());

        instance.getDefinitions().getBpmDiagrams().forEach(bpmnDiagram -> {
            BaseElement elem = bpmnDiagram.getBpmnPlane().getBpmnElement();
            if (!elem.getId().startsWith(prefix))
                elem.setId(String.format("%s%s", prefix, elem.getId()));
        });

        Collection<ZeebeTaskDefinition> zeebeTaskDefinitions = instance.getModelElementsByType(ZeebeTaskDefinition.class);
        if (zeebeTaskDefinitions != null)
            zeebeTaskDefinitions.forEach(zeebeTaskDefinition -> {
                if (!zeebeTaskDefinition.getType().startsWith(prefix))
                    zeebeTaskDefinition.setType(String.format("%s%s", prefix, zeebeTaskDefinition.getType()));
            });

        Collection<Message> messages = instance.getModelElementsByType(Message.class);
        if (messages != null)
            messages.forEach(message -> {
                if (!message.getName().startsWith(prefix))
                    message.setName(String.format("%s%s", prefix, message.getName()));
            });
    }


    @Override
    public void reverse(BpmnModelInstance instance, AdapterContext context) throws Exception {
        String prefix = String.format("%s.", context.getClientId());

    }

}
