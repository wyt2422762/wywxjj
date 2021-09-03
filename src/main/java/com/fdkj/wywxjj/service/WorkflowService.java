package com.fdkj.wywxjj.service;

import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.wf.WorkflowHistory;
import com.fdkj.wywxjj.api.model.wf.WorkflowInstant;
import com.fdkj.wywxjj.api.model.wf.WorkflowLink;
import com.fdkj.wywxjj.api.model.wf.WorkflowNode;
import com.fdkj.wywxjj.api.util.UserApi;
import com.fdkj.wywxjj.api.util.WfApi;
import com.fdkj.wywxjj.constant.Constants;
import com.fdkj.wywxjj.model.base.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
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
    private WfApi wfApi;

    @Autowired
    private UserApi userApi;

    /**
     * 获取流程实例
     *
     * @param request   req
     * @param instantId 流程实例id
     * @return res
     * @throws Exception err
     */
    public WorkflowInstant getWorkflowInstantById(HttpServletRequest request, String instantId) throws Exception {
        WorkflowInstant wfslDetail = wfApi.getWfslDetail(request, instantId);
        String fk_dqjdid = wfslDetail.getFk_dqjdid();
        WorkflowNode workflowNodeById = getWorkflowNodeById(request, fk_dqjdid);
        wfslDetail.setCurrentNode(workflowNodeById);
        return wfslDetail;
    }

    /**
     * 获取流程结点
     *
     * @param request req
     * @param nodeId  结点id
     */
    public WorkflowNode getWorkflowNodeById(HttpServletRequest request, String nodeId) throws Exception {
        return wfApi.getWorkflowNodeDetail(request, nodeId);
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
        Page<WorkflowNode> workflowNodeList = wfApi.getWorkflowNodeList(request, reqBody, 1, 1);
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
        Page<WorkflowNode> workflowNodeList = wfApi.getWorkflowNodeList(request, reqBody, 1, 1);
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

        Page<WorkflowLink> workflowLinks = wfApi.getWorkflowLinkList(request, reqBody, 1, 1);

        WorkflowLink workflowLink = workflowLinks.getDataList().get(0);
        String fk_jdid_next = workflowLink.getFk_jdid_next();
        return getWorkflowNodeById(request, fk_jdid_next);
    }

    /**
     * 获取下一个结点
     *
     * @param request req
     * @param fk_wfid 流程定义id
     * @param fk_jdid 结点id
     * @param dz 动作
     */
    public WorkflowNode getWorkflowNextNode(HttpServletRequest request, String fk_wfid, String fk_jdid, String dz) throws Exception {
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("fk_jdid", fk_jdid);
        reqBody.put("fk_wfid", fk_wfid);
        reqBody.put("dz", dz);

        Page<WorkflowLink> workflowLinks = wfApi.getWorkflowLinkList(request, reqBody, 1, 1);

        WorkflowLink workflowLink = workflowLinks.getDataList().get(0);
        String fk_jdid_next = workflowLink.getFk_jdid_next();
        return getWorkflowNodeById(request, fk_jdid_next);
    }

    /**
     * 获取待审核列表(分页)
     *
     * @param request  req
     * @param reqBody  请求体
     * @param params   请求参数
     * @param pageNo   页数
     * @param pageSize 每页显示的记录数
     * @return res
     * @throws Exception err
     */
    public Page<WorkflowInstant> getPendingReviewList(HttpServletRequest request, Map<String, String> reqBody, Map<String, String> params, Integer pageNo, Integer pageSize) throws Exception {
        return wfApi.getWorkflowInstanceList(request, reqBody, params, pageNo, pageSize);
    }

    //获取角色对应的审批结点(全部)

    /**
     * 获取角色对应的审批结点(全部)
     *
     * @param request req
     * @param fk_jsid 角色id
     * @param fk_wfid 流程定义id
     * @return res
     */
    public List<WorkflowNode> getRoleNodeList(HttpServletRequest request, String fk_jsid, String fk_wfid) throws Exception {
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("fk_wfid", fk_wfid);
        reqBody.put("fk_jsid", fk_jsid);
        return wfApi.getWorkflowNodeAllList(request, reqBody);
    }

    /**
     * 获取审核历史记录(根据流程实例id)
     *
     * @param request   req
     * @param fk_wkslid 流程实例id
     * @return res
     * @throws Exception err
     */
    public List<WorkflowHistory> getWorkflowHistoryListByWfslId(HttpServletRequest request, String fk_wkslid) throws Exception {
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("fk_wkslid", fk_wkslid);
        List<WorkflowHistory> workflowHistoryList = wfApi.getWorkflowHistoryList(request, reqBody);
        if(workflowHistoryList != null && !workflowHistoryList.isEmpty()) {
            for (WorkflowHistory workflowHistory : workflowHistoryList) {
                String fk_yhid = workflowHistory.getFk_yhid();
                User userDetail = userApi.getUserDetail(request, fk_yhid);
                workflowHistory.setUser(userDetail);
            }
        }
        return workflowHistoryList;
    }

    /**
     * 获取审核历史记录(根据业务id，业务类型)
     *
     * @param request   req
     * @param fk_ywid 业务id
     * @return res
     * @throws Exception err
     */
    public List<WorkflowHistory> getWorkflowHistoryListByYwId(HttpServletRequest request, String fk_ywid) throws Exception {
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("fk_ywid", fk_ywid);
        List<WorkflowHistory> workflowHistoryList = wfApi.getWorkflowHistoryList(request, reqBody);
        if(workflowHistoryList != null && !workflowHistoryList.isEmpty()) {
            for (WorkflowHistory workflowHistory : workflowHistoryList) {
                String fk_yhid = workflowHistory.getFk_yhid();
                User userDetail = userApi.getUserDetail(request, fk_yhid);
                workflowHistory.setUser(userDetail);
            }
        }
        return workflowHistoryList;
    }

}
