package com.fdkj.wywxjj.api.model.wf.bus;


import com.fdkj.wywxjj.api.model.wf.WorkflowHistory;
import com.fdkj.wywxjj.api.model.wf.WorkflowInstant;
import com.fdkj.wywxjj.api.model.zhMgr.Xhsq;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 提交销户审批
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class XhSqWf {

    private Xhsq WYWXJJ_XHSQModel;

    private Zh WYWXJJ_ZHModel;
    private WorkflowInstant WYWXJJ_WORKFLOW_SLModel;
    private WorkflowHistory WYWXJJ_WORKFLOW_HISModel;

}
