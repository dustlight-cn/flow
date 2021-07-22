package plus.flow.datacenter.events;

import plus.flow.core.events.AbstractEventSource;
import plus.flow.core.flows.FlowsService;
import plus.flow.core.flows.TaskService;

public class FormRecordDeletedEventSource extends AbstractEventSource {

    public FormRecordDeletedEventSource(FlowsService flowsService, TaskService taskService) {
        super(flowsService, taskService);
    }

    @Override
    public String getName() {
        return "FORM_RECORD_DELETED";
    }

    @Override
    public String getTitle() {
        return "表单记录删除";
    }

    @Override
    public String getDescription() {
        return "数据中心的表单删除时触发。";
    }

}
