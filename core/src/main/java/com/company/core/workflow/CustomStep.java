package com.company.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.osgi.service.component.annotations.Component;

import javax.jcr.Session;
import java.util.List;

@Component(service=WorkflowProcess.class, property = {"process.label=Setting permissions to change events"})
public class CustomStep implements WorkflowProcess
{
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        final String initiator1 = workItem.getWorkflow().getInitiator();
        Session s;
        /* Get actual user who initiated workflow */
        final String initiator = workItem.getWorkflowData().getMetaDataMap().get("userId", String.class);

        /* Get workflow history */
        final List<HistoryItem> histories = workflowSession.getHistory(workItem.getWorkflow());

        /* Get first item in workflow history */
        final HistoryItem firstItem = histories.get(0);

        /* Get the user that participated in the last item */
        final String firstUser = firstItem.getUserId();

        /* Get impersonated session */

    }
}
