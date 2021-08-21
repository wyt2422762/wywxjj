package com.fdkj.wywxjj.service;

import com.fdkj.wywxjj.api.model.wf.WorkflowLink;
import com.fdkj.wywxjj.api.model.wf.WorkflowNode;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.api.util.WfApi;
import com.fdkj.wywxjj.constant.Constants;
import com.fdkj.wywxjj.model.base.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 工作流相关
 *
 * @author Administrator
 */
@Service("workflowService")
public class WorkflowService {
    private static final Logger log = LoggerFactory.getLogger(WorkflowService.class);

    @Autowired
    private WfApi api;

    /**
     * 获取流程结点
     *
     * @param request req
     * @param fk_wfid 流程id
     * @param nodeId 结点id
     */
    public WorkflowNode getWorkflowNode(HttpServletRequest request, String fk_wfid, String nodeId) throws Exception {
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("fk_wfid", fk_wfid);
        reqBody.put("id", nodeId);
        Page<WorkflowNode> workflowNodeList = api.getWorkflowNodeList(request, reqBody, 1, 1);
        return workflowNodeList.getDataList().get(0);
    }

    /**
     * 获取流程开始结点
     *
     * @param request req
     * @param fk_wfid 流程id
     */
    public WorkflowNode getWorkflowStartNode(HttpServletRequest request, String fk_wfid) throws Exception {
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("fk_wfid", fk_wfid);
        reqBody.put("lx", Constants.WorkflowNodeType.QD);
        Page<WorkflowNode> workflowNodeList = api.getWorkflowNodeList(request, reqBody, 1, 1);
        return workflowNodeList.getDataList().get(0);
    }

    /**
     * 获取流程结束结点
     *
     * @param request req
     * @param fk_wfid 流程id
     */
    public WorkflowNode getWorkflowEndNode(HttpServletRequest request, String fk_wfid) throws Exception {
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("fk_wfid", fk_wfid);
        reqBody.put("lx", Constants.WorkflowNodeType.JS);
        Page<WorkflowNode> workflowNodeList = api.getWorkflowNodeList(request, reqBody, 1, 1);
        return workflowNodeList.getDataList().get(0);
    }

    /**
     * 获取下一个结点
     *
     * @param request req
     * @param fk_wfid 流程定义id
     * @param fk_jdid 结点id
     */
    public WorkflowNode getWorkflowNextNode(HttpServletRequest request, String fk_wfid, String fk_jdid) throws Exception {
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("fk_jdid", fk_jdid);
        reqBody.put("fk_wfid", fk_wfid);

        Page<WorkflowLink> workflowLinks = api.getWorkflowLinkList(request, reqBody, 1, 1);

        WorkflowLink workflowLink = workflowLinks.getDataList().get(0);
        String fk_jdid_next = workflowLink.getFk_jdid_next();
        return getWorkflowNode(request, fk_wfid, fk_jdid_next);
    }

}
