package plus.flow.zeebe.services.adapters;

import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import org.springframework.core.Ordered;

/**
 * Zeebe 过程适配器
 */
public interface ZeebeProcessAdapter extends Ordered {

    /**
     * 适配
     *
     * @param instance
     * @param context
     * @throws Exception
     */
    void adapt(BpmnModelInstance instance, AdapterContext context) throws Exception;

    /**
     * 还原
     *
     * @param instance
     * @param context
     * @throws Exception
     */
    void reverse(BpmnModelInstance instance, AdapterContext context) throws Exception;

}
