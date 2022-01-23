package cn.dustlight.flow.zeebe.services.adapters;

import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import io.camunda.zeebe.model.bpmn.instance.zeebe.ZeebeFormDefinition;
import io.camunda.zeebe.model.bpmn.instance.zeebe.ZeebeUserTaskForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTaskFormAdapter extends AbstractZeebeProcessAdapter {

    private String prefix = "camunda-forms:bpmn:";

    @Override
    public void adapt(BpmnModelInstance instance, AdapterContext context) throws Exception {

        Collection<ZeebeUserTaskForm> zeebeUserTaskForms = instance.getModelElementsByType(ZeebeUserTaskForm.class);
        Map<String, String> userTaskIdMap = new HashMap<>();
        if (zeebeUserTaskForms != null)
            zeebeUserTaskForms.forEach(zeebeUserTaskForm -> userTaskIdMap.put(zeebeUserTaskForm.getId(), zeebeUserTaskForm.getRawTextContent()));

        Collection<ZeebeFormDefinition> formDefinitions = instance.getModelElementsByType(ZeebeFormDefinition.class);
        if (formDefinitions != null)
            formDefinitions.forEach(formDefinition -> {
                if (!StringUtils.hasText(formDefinition.getFormKey()) ||
                        !formDefinition.getFormKey().startsWith(prefix))
                    return;
                String formKey = formDefinition.getFormKey();
                if (formKey.startsWith(prefix))
                    formKey = formKey.substring(prefix.length());
                String form = userTaskIdMap.get(formKey);
                if (form != null)
                    formDefinition.setFormKey(form);
            });
    }

    @Override
    public void reverse(BpmnModelInstance instance, AdapterContext context) throws Exception {
        Collection<ZeebeUserTaskForm> zeebeUserTaskForms = instance.getModelElementsByType(ZeebeUserTaskForm.class);
        Map<String, Queue<String>> userTaskFormMap = new HashMap<>();
        if (zeebeUserTaskForms != null)
            zeebeUserTaskForms.forEach(userTask -> {
                String key = userTask.getRawTextContent();
                Queue<String> list;
                if ((list = userTaskFormMap.get(key)) == null)
                    userTaskFormMap.put(key, (list = new LinkedList<>()));
                list.add(userTask.getId());
            });

        Collection<ZeebeFormDefinition> formDefinitions = instance.getModelElementsByType(ZeebeFormDefinition.class);
        if (formDefinitions != null)
            formDefinitions.forEach(formDefinition -> {
                Queue<String> idQueue = userTaskFormMap.get(formDefinition.getFormKey());
                if (idQueue == null || idQueue.isEmpty())
                    return;
                String id = idQueue.poll();
                formDefinition.setFormKey(String.format("%s%s", prefix, id));
            });
    }
}
