package plus.flow.zeebe.services.adapters;

import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import io.camunda.zeebe.model.bpmn.instance.*;
import io.camunda.zeebe.model.bpmn.instance.zeebe.ZeebeTaskDefinition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

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
        String prefix = String.format("c%s-", context.getClientId());

        Collection<BaseElement> baseElements = instance.getModelElementsByType(BaseElement.class);
        if (baseElements != null)
            for (BaseElement baseElement : baseElements) {
                if (baseElement.getId() != null)
                    baseElement.setId(String.format("%s%s", prefix, baseElement.getId()));
            }

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
                if (message.getName() != null)
                    message.setName(String.format("%s%s", prefix, message.getName()));
            });
    }


    @Override
    public void reverse(BpmnModelInstance instance, AdapterContext context) throws Exception {
        String prefix = String.format("c%s-", context.getClientId());

        Collection<BaseElement> baseElements = instance.getModelElementsByType(BaseElement.class);
        if (baseElements != null)
            baseElements.forEach(baseElement -> {
                if (StringUtils.hasText(baseElement.getId()) && baseElement.getId().startsWith(prefix))
                    baseElement.setId(baseElement.getId().substring(prefix.length()));
            });

        Collection<ZeebeTaskDefinition> zeebeTaskDefinitions = instance.getModelElementsByType(ZeebeTaskDefinition.class);
        if (zeebeTaskDefinitions != null)
            zeebeTaskDefinitions.forEach(zeebeTaskDefinition -> {
                if (StringUtils.hasText(zeebeTaskDefinition.getType()) && zeebeTaskDefinition.getType().startsWith(prefix))
                    zeebeTaskDefinition.setType(zeebeTaskDefinition.getType().substring(prefix.length()));
            });

        Collection<Message> messages = instance.getModelElementsByType(Message.class);
        if (messages != null)
            messages.forEach(message -> {
                if (StringUtils.hasText(message.getName()) && message.getName().startsWith(prefix))
                    message.setName(message.getName().substring(prefix.length()));
            });
    }

}
