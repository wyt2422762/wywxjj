package com.fdkj.wywxjj.controller.zh;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.wf.WorkflowHistory;
import com.fdkj.wywxjj.api.model.wf.WorkflowInstant;
import com.fdkj.wywxjj.api.model.wf.WorkflowNode;
import com.fdkj.wywxjj.api.model.zhMgr.Xhsq;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.api.model.zhMgr.Zh_his;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.constant.Constants;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import com.fdkj.wywxjj.service.WorkflowService;
import com.fdkj.wywxjj.utils.DateUtils;
import com.fdkj.wywxjj.utils.math.BigDecimalUtil;
import com.fdkj.wywxjj.utils.uuid.IdUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 账户
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/ZHGL")
public class ZhController {

    private static final Logger log = LoggerFactory.getLogger(ZhController.class);

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
        return new ModelAndView("zhMgr/zh_index");
    }


    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @param id      账户id
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toInfo/{id}")
    public ModelAndView toInfo(HttpServletRequest request,
                               @RequestParam(value = "opts", required = false) List<String> opts,
                               @PathVariable("id") String id) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("id", id);
        return new ModelAndView("zhMgr/zh_info");
    }

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @param id      账户id
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toJf/{id}")
    public ModelAndView toJf(HttpServletRequest request,
                             @RequestParam(value = "opts", required = false) List<String> opts,
                             @PathVariable("id") String id) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("id", id);
        return new ModelAndView("zhMgr/zh_jf");
    }

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @param id      账户id
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toEdit/{id}")
    public ModelAndView toEdit(HttpServletRequest request,
                               @RequestParam(value = "opts", required = false) List<String> opts,
                               @PathVariable("id") String id) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("id", id);
        return new ModelAndView("zhMgr/zh_edit");
    }

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @param id      账户id
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toXh/{id}")
    public ModelAndView toXh(HttpServletRequest request,
                             @RequestParam(value = "opts", required = false) List<String> opts,
                             @PathVariable("id") String id) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("id", id);
        return new ModelAndView("zhMgr/zh_xh");
    }

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @param id      账户id
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toXhjl/{id}")
    public ModelAndView toXhjl(HttpServletRequest request,
                               @RequestParam(value = "opts", required = false) List<String> opts,
                               @PathVariable("id") String id) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("id", id);
        return new ModelAndView("zhMgr/zh_xhjl");
    }

    /**
     * 获取账户列表
     *
     * @param request req
     * @param fk_qybm 区域编码
     * @param fk_yhid 银行id
     * @param no      账号
     * @param page    第几页
     * @param limit   每页显示多少条
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList(HttpServletRequest request,
                                                   @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                   @RequestParam(value = "fk_yhid", required = false) String fk_yhid,
                                                   @RequestParam(value = "no", required = false) String no,
                                                   @RequestParam(value = "zt", required = false) String zt,
                                                   @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_yhid)) {
                reqBody.put("fk_yhid", fk_yhid);
            }
            if (StringUtils.isNotBlank(no)) {
                reqBody.put("no", no);
            }
            if (StringUtils.isNotBlank(zt)) {
                reqBody.put("zt", zt);
            }
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            Page<Zh> zhPage = api.getZhList(request, reqBody, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取账户列表成功", zhPage);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取账户列表失败", e);
            throw new BusinessException("获取账户列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取账户详情
     *
     * @param request req
     * @param id      账户id
     * @return res
     */
    @RequestMapping("getDetail/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getDetail(HttpServletRequest request,
                                                     @PathVariable String id) {
        try {
            Zh zhDetail = api.getZhDetail(request, id);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取账户详情成功", zhDetail);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取账户详情失败", e);
            throw new BusinessException("获取账户详情失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 添加/编辑账户
     *
     * @param request req
     * @param xf      是否是续费
     * @param zh      请求体
     * @return res
     */
    @RequestMapping("aeZh")
    @ResponseBody
    public ResponseEntity<CusResponseBody> aeZh(HttpServletRequest request,
                                                @RequestParam(value = "xf", required = false) Boolean xf,
                                                @RequestBody JSONObject zh) {
        try {
            //判断账户是否存在
            Map<String, String> reqBody = new HashMap<>();
            String fk_yhid = zh.getJSONObject("model").getString("fk_yhid");
            String no = zh.getJSONObject("model").getString("no");

            reqBody.put("fk_yhid", fk_yhid);
            reqBody.put("no", no);

            Page<Zh> zhList = api.getZhList(request, reqBody, 1, 1);
            if (zhList != null && zhList.getTotalRecord() > 0) {
                //说明账号存在
                Zh zh1 = zhList.getDataList().get(0);
                if (zh1.getNo().equals(no) && !zh1.getId().equals(zh.getString("id"))) {
                    throw new BusinessException("更新账户失败: 账号已存在", HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
            }

            String khrq = zh.getJSONObject("model").getString("khrq");
            if (StringUtils.isBlank(khrq)) {
                khrq = DateUtils.parseDateToStr("yyyy-MM-dd'T'HH:mm:ss.sss", new Date());
                zh.getJSONObject("model").remove("khrq");
                zh.getJSONObject("model").put("khrq", khrq);

                zh.getJSONObject("model").remove("addtime");
                zh.getJSONObject("model").put("addtime", khrq);

            }
            JSONArray list = zh.getJSONArray("list");
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    JSONObject jsonObject = list.getJSONObject(i);
                    String dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd'T'HH:mm:ss.sss", new Date());
                    jsonObject.putIfAbsent("jzrq", dateToStr);
                    jsonObject.putIfAbsent("addtime", dateToStr);
                }
            }
            //调用接口添加编辑数据
            api.aeZh(request, zh);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("更新账户成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("更新账户失败", e);
            throw new BusinessException("更新账户失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 缴费
     *
     * @param request req
     * @param id      账户id
     * @param jfje    缴费金额
     * @return res
     */
    @RequestMapping("jf/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> aeZh(HttpServletRequest request,
                                                @PathVariable String id,
                                                @RequestParam("jfje") String jfje) {
        try {
            Zh zhDetail = api.getZhDetail(request, id);
            String money = zhDetail.getMoney();
            String add = BigDecimalUtil.add(money, jfje).toPlainString();
            zhDetail.setMoney(add);

            String dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd'T'HH:mm:ss.sss", new Date());

            zhDetail.setXfrq(dateToStr);

            List<Zh_his> list = zhDetail.getList();
            Zh_his zh_his = new Zh_his();
            zh_his.setCzlx(Constants.JzLb.JF)
                    .setSr(jfje)
                    .setZhye(add)
                    .setFk_zhid(zhDetail.getId())
                    .setJzrq(dateToStr).setAddtime(dateToStr);

            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(zh_his);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("model", (JSONObject) JSONObject.toJSON(zhDetail));

            JSONArray ja = (JSONArray) JSONArray.toJSON(list);
            for (int i = 0; i < ja.size(); i++) {
                JSONObject jsonObject1 = ja.getJSONObject(i);
                jsonObject1.remove("id");
            }

            jsonObject.put("list", ja);

            api.aeZh(request, jsonObject);

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("账户缴费成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("账户缴费失败", e);
            throw new BusinessException("账户缴费失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 提交销户申请
     *
     * @param request req
     * @param id      账户id
     * @param xhsq    销户申请
     * @return res
     */
    @RequestMapping("xh/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> xh(HttpServletRequest request,
                                              @PathVariable String id,
                                              @RequestBody Xhsq xhsq) {
        try {
            //登录用户
            User cuser = api.getUserFromCookie(request);
            String dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd'T'HH:mm:ss.sss", new Date());
            //1. 获取账户信息
            Zh zhDetail = api.getZhDetail(request, id);
            // 2. 改账户状态
            zhDetail.setZt(Constants.ZhZt.XHDJ);
            // 3. 设置销户申请
            xhsq.setId(IdUtils.randomUUID()).setSqrq(dateToStr);
            // 4. 启动流程，获取启动结点信息
            WorkflowNode startNode = workflowService.getWorkflowStartNode(request, Constants.WorkflowId.XH);
            // 5. 获取启动结点的下一个结点
            WorkflowNode nextNode = workflowService.getWorkflowNextNode(request, Constants.WorkflowId.XH, startNode.getId());
            // 6. 构造流程实例
            WorkflowInstant wfi = new WorkflowInstant();
            wfi.setId(IdUtils.randomUUID())
                    .setFk_dqjdid(nextNode.getId())
                    .setFk_qybm(zhDetail.getFk_qybm())
                    .setFk_xtglid(zhDetail.getFk_xtglid())
                    .setFk_wfid(Constants.WorkflowId.XH)
                    .setFk_yhid(cuser.getId())
                    .setLx(Constants.WorkflowType.XH)
                    .setZt(Constants.WorkflowStatus.SHZ)
                    .setFqr(cuser.getUsername())
                    .setFk_ywid(xhsq.getId())
                    .setFqsj(dateToStr).setAddtime(dateToStr);
            // 7. 构造流程历史
            WorkflowHistory wfh = new WorkflowHistory();
            wfh.setId(IdUtils.randomUUID())
                    .setFk_qybm(zhDetail.getFk_qybm())
                    .setFk_xtglid(zhDetail.getFk_xtglid())
                    .setFk_jdid(startNode.getId())
                    .setFk_yhid(cuser.getId())
                    .setSpr(cuser.getUsername())
                    .setSpsj(dateToStr)
                    .setCzmc(Constants.WorkflowNodeName.START).setLx(Constants.WorkflowType.XH)
                    .setFk_wkslid(wfi.getId()).setFk_ywid(xhsq.getId()).setAddtime(dateToStr);

            JSONObject json = new JSONObject();

            //销户申请内容
            JSONObject jsonObject_xhsq = JSONObject.parseObject(JSONObject.toJSONString(xhsq));
            json.put("wywxjJ_XHSQModel", jsonObject_xhsq);

            //账户内容
            JSONObject jsonObject_zh = JSONObject.parseObject(JSONObject.toJSONString(zhDetail));
            json.put("wywxjJ_ZHModel", JSONObject.toJSON(jsonObject_zh));

            //流程实例
            JSONObject jsonObject_wfi = JSONObject.parseObject(JSONObject.toJSONString(wfi));
            json.put("wywxjJ_WORKFLOW_SLModel", jsonObject_wfi);

            //流程历史
            JSONObject jsonObject_wfh = JSONObject.parseObject(JSONObject.toJSONString(wfh));
            json.put("wywxjJ_WORKFLOW_HISModel", jsonObject_wfh);

            //发送请求
            api.submitXhSq(request, json);

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("销户申请提交成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("销户申请提交失败", e);
            throw new BusinessException("销户申请提交失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }


    /**
     * 获取销户记录
     *
     * @param request req
     * @param id      账户id
     * @return res
     */
    @RequestMapping("xhjl/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getXhjuList(HttpServletRequest request,
                                                       @PathVariable String id,
                                                       @RequestParam("page") Integer page,
                                                       @RequestParam("limit") Integer limit) {
        try {
            //登录用户
            User cuser = api.getUserFromCookie(request);
            // 1. 获取账户信息
            Zh zh = api.getZhDetail(request, id);
            // 2. 获取销户记录
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(id)) {
                reqBody.put("fk_zhid", id);
            }
            Page<Xhsq> xhsqPage = api.getXhsqList(request, reqBody, page, limit);
            List<Xhsq> dataList = xhsqPage.getDataList();
            if (dataList != null && !dataList.isEmpty()) {
                for (Xhsq xhsq : dataList) {
                    xhsq.setZh(zh);
                    Map<String, String> params = new HashMap<>();
                    params.put("fk_ywid", xhsq.getId());
                    Page<WorkflowInstant> pendingReviewList = workflowService.getPendingReviewList(request, null, params, page, limit);
                    List<WorkflowInstant> wfiList = pendingReviewList.getDataList();
                    if (wfiList != null && !wfiList.isEmpty()) {
                        xhsq.setWfi(wfiList.get(0));
                    }
                }
            }
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取账户列表成功", xhsqPage);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取销户记录失败", e);
            throw new BusinessException("获取销户记录失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
