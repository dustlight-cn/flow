package plus.flow.datacenter.events;

import plus.flow.core.events.AbstractEventSource;
import plus.flow.core.flows.FlowsService;
import plus.flow.core.flows.TaskService;

public class FormRecordUpdatedEventSource extends AbstractEventSource {

    public FormRecordUpdatedEventSource(FlowsService flowsService, TaskService taskService) {
        super(flowsService, taskService);
    }

    @Override
    public String getName() {
        return "FORM_RECORD_UPDATED";
    }

    @Override
    public String getTitle() {
        return "表单记录更新";
    }

    @Override
    public String getDescription() {
        return "数据中心的表单更新时触发。";
    }

}
