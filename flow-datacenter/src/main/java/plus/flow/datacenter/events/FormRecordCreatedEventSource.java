package plus.flow.datacenter.events;

import plus.flow.core.events.AbstractEventSource;
import plus.flow.core.flows.FlowsService;
import plus.flow.core.flows.TaskService;

public class FormRecordCreatedEventSource extends AbstractEventSource {

    public FormRecordCreatedEventSource(FlowsService flowsService, TaskService taskService) {
        super(flowsService, taskService);
    }

    @Override
    public String getName() {
        return "FORM_RECORD_CREATED";
    }

    @Override
    public String getTitle() {
        return "表单记录提交";
    }

    @Override
    public String getDescription() {
        return "数据中心的表单记录时触发。";
    }

}
