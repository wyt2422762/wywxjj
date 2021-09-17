package com.fdkj.wywxjj.controller.zh;

import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.sysMgr.Yh;
import com.fdkj.wywxjj.api.model.wf.WorkflowInstant;
import com.fdkj.wywxjj.api.model.wf.WorkflowNode;
import com.fdkj.wywxjj.api.model.zhMgr.Xhsq;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.api.util.YhApi;
import com.fdkj.wywxjj.api.util.ZhApi;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.constant.Constants;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import com.fdkj.wywxjj.service.WorkflowService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销户管理
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/XHGL")
public class XhglController {
    private static final Logger log = LoggerFactory.getLogger(XhglController.class);

    @Autowired
    private ZhApi zhApi;
    @Autowired
    private YhApi yhApi;
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
        User cuser = zhApi.getUserFromCookie(request);
        request.setAttribute("cuser", cuser);
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        if(!Constants.YhLx.YH.equals(cuser.getYhType())) {
            //获取银行列表
            String fk_qybm = cuser.getFk_qybm();
            Map<String, String> yhParam = new HashMap<>();
            if(StringUtils.isNotBlank(fk_qybm)) {
                yhParam.put("fk_qybm", fk_qybm);
            }
            List<Yh> yhAllList = yhApi.getYhAllList(request, yhParam);
            request.setAttribute("yhList", yhAllList);
        }
        return new ModelAndView("zhMgr/xhgl/xhgl_index");
    }

    /**
     * 获取审核列表
     * @param request req
     * @param fk_qybm 区域编码
     * @param page 页数
     * @param limit 每页显示的记录数
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList(HttpServletRequest request,
                                                                @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                                @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            //当前登录用户
            User cuser = zhApi.getUserFromCookie(request);

            //流程定义id
            String fk_wfid = Constants.WorkflowId.XH;

            Map<String, String> params = new HashMap<>();
            params.put("fk_wfid", fk_wfid);

            Page<WorkflowInstant> shList = workflowService.getPendingReviewList(request, null, params, page, limit);
            List<WorkflowInstant> dataList = shList.getDataList();
            for (WorkflowInstant workflowInstant : dataList) {
                String fk_ywid = workflowInstant.getFk_ywid();
                String fk_dqjdid = workflowInstant.getFk_dqjdid();
                //销户申请
                Xhsq xhsqDetail = zhApi.getXhsqDetail(request, fk_ywid);
                //账号
                Zh zhDetail = zhApi.getZhDetail(request, xhsqDetail.getFk_zhid());
                xhsqDetail.setZh(zhDetail);
                workflowInstant.setData(xhsqDetail);
                WorkflowNode workflowNodeById = workflowService.getWorkflowNodeById(request, fk_dqjdid);
                workflowInstant.setCurrentNode(workflowNodeById);
            }

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取销户列表成功", shList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取销户列表失败", e);
            throw new BusinessException("获取销户列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取审核列表
     * @param request req
     * @param fk_qybm 区域编码
     * @param page 页数
     * @param limit 每页显示的记录数
     * @return res
     */
    @RequestMapping("getList2")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList2(HttpServletRequest request,
                                                    @RequestParam(value = "zt", required = false) String zt,
                                                    @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                    @RequestParam(value = "fk_bankid", required = false) String fk_bankid,
                                                    @RequestParam(value = "startDate", required = false) String startDate,
                                                    @RequestParam(value = "endDate", required = false) String endDate,
                                                    @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            //当前登录用户
            User cuser = zhApi.getUserFromCookie(request);

            Map<String, String> params = new HashMap<>();
            if(StringUtils.isNotBlank(zt)) {
                params.put("zt", zt);
            }
            if(StringUtils.isNotBlank(fk_bankid)) {
                params.put("fk_bankid", fk_bankid);
            }

            Page<Xhsq> xhsqList = zhApi.getXhsqList(request, params, page, limit, startDate, endDate);
            List<Xhsq> dataList = xhsqList.getDataList();
            if(dataList != null) {
                for (Xhsq xhsq : dataList) {
                    String fk_zhid = xhsq.getFk_zhid();
                    Zh zhDetail = zhApi.getZhDetail(request, fk_zhid);
                    xhsq.setZh(zhDetail);
                }
            }

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取销户列表成功", xhsqList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取销户列表失败", e);
            throw new BusinessException("获取销户列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
