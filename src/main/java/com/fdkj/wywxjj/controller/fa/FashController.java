package com.fdkj.wywxjj.controller.fa;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.fa.Fa;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.wf.WorkflowHistory;
import com.fdkj.wywxjj.api.model.wf.WorkflowInstant;
import com.fdkj.wywxjj.api.model.wf.WorkflowNode;
import com.fdkj.wywxjj.api.model.zhMgr.Xhsq;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.constant.Constants;
import com.fdkj.wywxjj.controller.zh.XhshController;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import com.fdkj.wywxjj.service.WorkflowService;
import com.fdkj.wywxjj.utils.DateUtils;
import com.fdkj.wywxjj.utils.uuid.IdUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 方案审核
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/FASH")
public class FashController {
    private static final Logger log = LoggerFactory.getLogger(FashController.class);

    @Autowired
    private Api api;
    @Autowired
    private WorkflowService workflowService;

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @return res
     * @throws Exception e
     */
    @RequestMapping("Index")
    public ModelAndView index(HttpServletRequest request, @RequestParam(value = "opts", required = false) List<String> opts) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        return new ModelAndView("faMgr/fash/dsh_index");
    }

    /**
     * 跳转到
     *
     * @param request   req
     * @param fk_wfslid 流程实例id
     * @param opts      操作权限信息
     * @return res
     * @throws Exception err
     */
    @RequestMapping("toSh/{fk_wfslid}")
    public ModelAndView toSh(HttpServletRequest request, @PathVariable("fk_wfslid") String fk_wfslid, @RequestParam(value = "opts", required = false) List<String> opts) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("fk_wkslid", fk_wfslid);

        //获取流程实例信息
        WorkflowInstant wfi = workflowService.getWorkflowInstantById(request, fk_wfslid);
        String fk_ywid = wfi.getFk_ywid();
        Fa faDetail = api.getFaDetail(request, fk_ywid);
        wfi.setData(faDetail);
        request.setAttribute("wfi", wfi);

        return new ModelAndView("faMgr/fash/sh");
    }

    /**
     * 查询待审核列表
     *
     * @param request req
     * @param fk_qybm 区域编码
     * @param page    页数
     * @param limit   每页显示的记录数
     */
    @RequestMapping("getPendingReviewList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getPendingReviewList(HttpServletRequest request,
                                                                @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                                @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            //当前登录用户
            User cuser = api.getUserFromCookie(request);
            //获取用户对应的审批结点
            List<WorkflowNode> roleNodeList = workflowService.getRoleNodeList(request, cuser.getFk_jsid(), Constants.WorkflowId.XH);

            //流程定义id
            String fk_wfid = Constants.WorkflowId.FA;

            Map<String, String> params = new HashMap<>();
            params.put("fk_wfid", fk_wfid);
            if (roleNodeList != null && !roleNodeList.isEmpty()) {
                List<String> jdids = new ArrayList<>();
                for (WorkflowNode workflowNode : roleNodeList) {
                    jdids.add(workflowNode.getId());
                }
                params.put("fk_dqjdid", String.join(",", jdids));
            }

            Page<WorkflowInstant> pendingReviewList = workflowService.getPendingReviewList(request, null, params, page, limit);
            List<WorkflowInstant> dataList = pendingReviewList.getDataList();
            for (WorkflowInstant workflowInstant : dataList) {
                String fk_ywid = workflowInstant.getFk_ywid();
                String fk_dqjdid = workflowInstant.getFk_dqjdid();
                //销户申请
                Fa faDetail = api.getFaDetail(request, fk_ywid);
                workflowInstant.setData(faDetail);
                WorkflowNode workflowNodeById = workflowService.getWorkflowNodeById(request, fk_dqjdid);
                workflowInstant.setCurrentNode(workflowNodeById);
            }

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取待审核列表成功", pendingReviewList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取待审核列表失败", e);
            throw new BusinessException("获取待审核列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 查询审核历史
     *
     * @param request req
     * @param fk_ywid 流程实例id
     * @param fk_qybm 区域编码
     * @return res
     */
    @RequestMapping("getHistoryList_yw/{fk_ywid}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getHistoryList_yw(HttpServletRequest request,
                                                          @PathVariable("fk_ywid") String fk_ywid,
                                                          @RequestParam(value = "fk_qybm", required = false) String fk_qybm) {
        try {
            //当前登录用户
            User cuser = api.getUserFromCookie(request);
            List<WorkflowHistory> workflowHistoryListByWfslId = workflowService.getWorkflowHistoryListByYwId(request, fk_ywid);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取审核历史列表成功", workflowHistoryListByWfslId);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("查询审核历史失败", e);
            throw new BusinessException("查询审核历史失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 查询审核历史
     *
     * @param request req
     * @param fk_wkslid 流程实例id
     * @param fk_qybm 区域编码
     * @return res
     */
    @RequestMapping("getHistoryList_wfi/{fk_wkslid}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getHistoryList_wfi(HttpServletRequest request,
                                                          @PathVariable("fk_wkslid") String fk_wkslid,
                                                          @RequestParam(value = "fk_qybm", required = false) String fk_qybm) {
        try {
            //当前登录用户
            User cuser = api.getUserFromCookie(request);
            List<WorkflowHistory> workflowHistoryListByWfslId = workflowService.getWorkflowHistoryListByWfslId(request, fk_wkslid);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取审核历史列表成功", workflowHistoryListByWfslId);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("查询审核历史失败", e);
            throw new BusinessException("查询审核历史失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 审核
     *
     * @param request   req
     * @param fk_wkslid 流程实例id
     * @param fk_qybm   区域编码
     * @param action    动作
     * @return res
     */
    @RequestMapping("sh/{fk_wkslid}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> sh(HttpServletRequest request, @PathVariable("fk_wkslid") String fk_wkslid,
                                              @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                              @RequestParam(value = "action") String action,
                                              @RequestParam(value = "yj") String yj,
                                              @RequestParam(value = "dqjdid") String dqjdid) {
        try {
            //当前登录用户
            User cuser = api.getUserFromCookie(request);
            String dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd'T'HH:mm:ss.sss", new Date());
            //1. 获取流程实例信息
            WorkflowInstant wfi = workflowService.getWorkflowInstantById(request, fk_wkslid);
            //2. 判断流程实例结点是否发生变化
            if (!wfi.getFk_dqjdid().equals(dqjdid) || wfi.getZt().equals(Constants.WorkflowStatus.YSH)) {
                throw new BusinessException("流程状态发生变化，请刷新页面重试", HttpStatus.BAD_REQUEST.value());
            }
            //3. 查询下一个结点(根据当前结点id，动作，流程定义id)
            WorkflowNode workflowNextNode = workflowService.getWorkflowNextNode(request, Constants.WorkflowId.XH, dqjdid, action);
            //4. 获取当前的结点
            WorkflowNode workflowNode = workflowService.getWorkflowNodeById(request, dqjdid);
            //5. 修改流程实例
            //修改流程实例当前结点
            wfi.setFk_dqjdid(workflowNextNode.getId());
            if (workflowNextNode.getLx().equals(Constants.WorkflowNodeName.END)) {
                //如果流程结束，修改流程实例状态
                wfi.setZt(Constants.WorkflowStatus.YSH);
            }
            //6. 修改审核历史
            WorkflowHistory wfh = new WorkflowHistory();
            wfh.setId(IdUtils.randomUUID())
                    .setFk_qybm(wfi.getFk_qybm())
                    .setFk_xtglid(wfi.getFk_xtglid())
                    .setFk_jdid(workflowNode.getId())
                    .setFk_yhid(cuser.getId())
                    .setSpr(cuser.getUsername())
                    .setSpsj(dateToStr).setAddtime(dateToStr)
                    .setCzmc(workflowNode.getJdmc() + action)
                    .setLx(Constants.WorkflowType.XH)
                    .setFk_wkslid(wfi.getId()).setFk_ywid(wfi.getFk_ywid())
                    .setYj(yj);
            //7. 获取fa
            String fk_ywid = wfi.getFk_ywid();
            Fa faDetail = api.getFaDetail(request, fk_ywid);
            //8. 修改方案状态
            //9. 修改账户状态
            if (workflowNextNode.getLx().equals(Constants.WorkflowNodeName.END)) {
                if (action.equals(Constants.WorkflowAction.PASS)) {
                    faDetail.setFazt("审核通过").setZt("审核通过");
                } else if (action.equals(Constants.WorkflowAction.REJECT)) {
                    faDetail.setFazt("审核不通过").setZt("审核不通过");
                }
            } else {
                faDetail.setZt("方案" + workflowNode.getJdmc()).setFazt("方案" + workflowNode.getJdmc());
            }

            //10. 提交数据
            JSONObject json = new JSONObject();

            //流程实例
            JSONObject jsonObject_wfi = JSONObject.parseObject(JSONObject.toJSONString(wfi));
            json.put("wywxjJ_WORKFLOW_SLmodel", jsonObject_wfi);
            //流程历史
            JSONObject jsonObject_wfh = JSONObject.parseObject(JSONObject.toJSONString(wfh));
            json.put("wywxjJ_WORKFLOW_HISmodel", jsonObject_wfh);
            //方案
            JSONObject jsonObject_fa = JSONObject.parseObject(JSONObject.toJSONString(faDetail));
            json.put("wywxjJ_FAmodel", jsonObject_fa);

            //发送请求
            api.spFa(request, json);

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("方案审批成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("方案审批失败", e);
            throw new BusinessException("方案审批失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
