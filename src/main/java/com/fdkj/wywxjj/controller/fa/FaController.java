package com.fdkj.wywxjj.controller.fa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.fa.Fa;
import com.fdkj.wywxjj.api.model.fa.Fa_fh;
import com.fdkj.wywxjj.api.model.fa.Fa_mx;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.wf.WorkflowHistory;
import com.fdkj.wywxjj.api.model.wf.WorkflowInstant;
import com.fdkj.wywxjj.api.model.wf.WorkflowNode;
import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import com.fdkj.wywxjj.api.util.FaApi;
import com.fdkj.wywxjj.api.util.FhApi;
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
 * @author 方案管理
 */
@Controller
@RequestMapping("CZF/FAGL")
public class FaController {
    private static final Logger log = LoggerFactory.getLogger(FaController.class);

    @Autowired
    private FaApi faApi;
    @Autowired
    private FhApi fhApi;
    @Autowired
    private WorkflowService workflowService;

    /**
     * 跳转
     *
     * @param request req
     * @param opts    操作权限
     * @return res
     * @throws Exception err
     */
    @RequestMapping("Index")
    public ModelAndView index(HttpServletRequest request,
                              @RequestParam(value = "opts", required = false) List<String> opts) throws Exception {
        request.setAttribute("cuser", faApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        return new ModelAndView("faMgr/fa_index");
    }

    /**
     * 跳转
     *
     * @param request req
     * @param opts    操作权限
     * @return res
     * @throws Exception err
     */
    @RequestMapping("toAdd")
    public ModelAndView toAdd(HttpServletRequest request,
                              @RequestParam(value = "opts", required = false) List<String> opts) throws Exception {
        request.setAttribute("cuser", faApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        return new ModelAndView("faMgr/fa_add");
    }

    /**
     * 跳转
     *
     * @param request req
     * @param opts    操作权限
     * @return res
     * @throws Exception err
     */
    @RequestMapping("toInfo/{id}")
    public ModelAndView toInfo(HttpServletRequest request,
                               @PathVariable("id") String id,
                               @RequestParam(value = "opts", required = false) List<String> opts) throws Exception {
        request.setAttribute("cuser", faApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("id", id);
        return new ModelAndView("faMgr/fa_info");
    }

    /**
     * 跳转
     *
     * @param request   req
     * @param fk_xmxxid 项目信息id
     * @param fk_ldxxid 楼栋信息id
     * @param szdy      所在单元
     * @return res
     * @throws Exception err
     */
    @RequestMapping("toAddAct/{fk_xmxxid}/{fk_ldxxid}")
    public ModelAndView toAddAct(HttpServletRequest request,
                                 @PathVariable(value = "fk_xmxxid") String fk_xmxxid,
                                 @PathVariable(value = "fk_ldxxid") String fk_ldxxid,
                                 @RequestParam(value = "szdy", required = false) String szdy) throws Exception {
        request.setAttribute("cuser", faApi.getUserFromCookie(request));
        request.setAttribute("fk_xmxxid", fk_xmxxid);
        request.setAttribute("fk_ldxxid", fk_ldxxid);
        request.setAttribute("szdy", szdy);
        return new ModelAndView("faMgr/fa_addAct");
    }

    /**
     * 方案金额验证
     *
     * @param request   req
     * @param fk_xmxxid 项目信息id
     * @param fk_ldxxid 楼栋信息id
     * @param szdy      所在单元
     * @return res
     */
    @RequestMapping("fajeYz/{fk_xmxxid}/{fk_ldxxid}")
    public ResponseEntity<CusResponseBody> fajeYz(HttpServletRequest request,
                                                  @PathVariable(value = "fk_xmxxid") String fk_xmxxid,
                                                  @PathVariable(value = "fk_ldxxid") String fk_ldxxid,
                                                  @RequestParam(value = "szdy", required = false) String szdy,
                                                  @RequestParam(value = "fayjje", required = false) String fayjje,
                                                  @RequestParam(value = "ftfs", required = false) String ftfs) {
        try {
            User cuser = faApi.getUserFromCookie(request);
            Map<String, Object> reqBody = new HashMap<>();
            reqBody.put("fk_xmxxid", fk_xmxxid.trim());
            reqBody.put("fk_ldxxid", fk_ldxxid.trim());
            if (StringUtils.isNotBlank(szdy)) {
                reqBody.put("szdy", szdy.trim());
            }
            List<Fh> fhList = fhApi.getFhAllList(request, reqBody);
            if (fhList == null || fhList.isEmpty()) {
                //构造返回数据
                Map<String, Object> res = new HashMap<>();
                res.put("res", Constants.CanSubmit.JJ);
                res.put("data", null);
                CusResponseBody cusResponseBody = CusResponseBody.success("房间列表为空", res);
                return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
            } else {
                if (Constants.Ftfs.PJFT.equals(ftfs)) {
                    List<String> bgs = new ArrayList<>();
                    //获取房间个数
                    int fhgs = fhList.size();
                    //计算每户的金额
                    String divide = BigDecimalUtil.divideHalfUp(fayjje, (fhgs + "")).toPlainString();
                    List<Fa_fh> fa_fhList = new ArrayList<>();
                    //判断金额
                    for (Fh fh : fhList) {
                        String no = fh.getFh();
                        String money = fh.getZh().getMoney();
                        int compareTo = BigDecimalUtil.compareTo(money, divide);
                        if (compareTo < 0) {
                            bgs.add(no);
                        }

                        Fa_fh fa_fh = new Fa_fh();
                        fa_fh.setFk_xtglid(cuser.getFk_xtglid());
                        fa_fh.setFk_fhid(fh.getId());
                        fa_fh.setFk_zhid(fh.getZh().getId());
                        fa_fh.setFtje(divide);
                        fa_fhList.add(fa_fh);
                    }
                    Map<String, Object> res = new HashMap<>();
                    res.put("data", fa_fhList);
                    if (bgs.isEmpty()) {
                        //构造返回数据
                        res.put("res", Constants.CanSubmit.TJ);
                        CusResponseBody cusResponseBody = CusResponseBody.success("房间账户金额全部足够", res);
                        return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
                    } else {
                        //构造返回数据
                        String msg = "房间(" + String.join(",", bgs) + ")账户金额不足，是否继续提交";
                        res.put("res", Constants.CanSubmit.XW);
                        CusResponseBody cusResponseBody = CusResponseBody.success(msg, res);
                        return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
                    }
                } else if (Constants.Ftfs.MJFT.equals(ftfs)) {
                    List<String> bgs = new ArrayList<>();
                    //计算房间的总面积
                    String zmj = "0";
                    List<Fa_fh> fa_fhList = new ArrayList<>();
                    //判断金额
                    for (Fh fh : fhList) {
                        String cmj = fh.getScmj_jzmj();
                        zmj = BigDecimalUtil.add(zmj, cmj).toPlainString();
                    }
                    for (Fh fh : fhList) {
                        String no = fh.getFh();
                        String money = fh.getZh().getMoney();
                        String cmj = fh.getScmj_jzmj();
                        String je = BigDecimalUtil.divideHalfUp(BigDecimalUtil.multiply(fayjje, cmj).toPlainString(), zmj).toPlainString();
                        int compareTo = BigDecimalUtil.compareTo(money, je);
                        if (compareTo < 0) {
                            bgs.add(no);
                        }

                        Fa_fh fa_fh = new Fa_fh();
                        fa_fh.setFk_xtglid(cuser.getFk_xtglid());
                        fa_fh.setFk_fhid(fh.getId());
                        fa_fh.setFk_zhid(fh.getZh().getId());
                        fa_fh.setFtje(je);
                        fa_fhList.add(fa_fh);
                    }
                    Map<String, Object> res = new HashMap<>();
                    res.put("data", fa_fhList);
                    if (bgs.isEmpty()) {
                        //构造返回数据
                        res.put("res", Constants.CanSubmit.TJ);
                        CusResponseBody cusResponseBody = CusResponseBody.success("房间账户金额全部足够", res);
                        return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
                    } else {
                        //构造返回数据
                        String msg = "房间(" + String.join(",", bgs) + ")账户金额不足，是否继续提交";
                        res.put("res", Constants.CanSubmit.XW);
                        CusResponseBody cusResponseBody = CusResponseBody.success(msg, res);
                        return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
                    }
                } else {
                    Map<String, Object> res = new HashMap<>();
                    res.put("res", Constants.CanSubmit.JJ);
                    res.put("data", null);
                    CusResponseBody cusResponseBody = CusResponseBody.success("操作错误，拒绝", res);
                    return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.error("方案数据验证失败", e);
            throw new BusinessException("数据验证失败, 请稍后重试 ", HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取方案列表
     *
     * @param request req
     * @param fk_qybm 区域编码
     * @param fk_wyid 物业公司id
     * @param page    第几页
     * @param limit   每页显示的记录数
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList(HttpServletRequest request,
                                                   @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                   @RequestParam(value = "fk_wyid", required = false) String fk_wyid,
                                                   @RequestParam(value = "fabh", required = false) String fabh,
                                                   @RequestParam(value = "zt", required = false) String fazt,
                                                   @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_wyid)) {
                reqBody.put("fk_wyid", fk_wyid);
            }
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            if (StringUtils.isNotBlank(fabh)) {
                reqBody.put("fabh", fabh);
            }
            if (StringUtils.isNotBlank(fazt)) {
                reqBody.put("zt", fazt);
            }

            Map<String, Object> params = new HashMap<>(4);
            params.put("page", page == null ? 1 : page);
            params.put("pageNum", limit == null ? 10 : limit);

            Page<Fa> faList = faApi.getFaList(request, reqBody, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取方案列表成功", faList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取方案列表失败", e);
            throw new BusinessException("获取方案列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取方案基本信息
     *
     * @param request req
     * @param id      方案id
     * @return res
     */
    @RequestMapping("getDetail/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getBasicDetail(HttpServletRequest request, @PathVariable String id) {
        try {
            Fa faDetail = faApi.getFaDetail(request, id);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取方案基本数据成功", faDetail);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取方案基本数据失败", e);
            throw new BusinessException("获取方案基本数据失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 添加方案
     *
     * @param request    req
     * @param jsonObject 请求体
     * @return res
     */
    @RequestMapping("addFa")
    @ResponseBody
    public ResponseEntity<CusResponseBody> addFa(HttpServletRequest request,
                                                 @RequestBody JSONObject jsonObject) {

        try {
            //登录用户
            User cuser = faApi.getUserFromCookie(request);
            String dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd'T'HH:mm:ss.sss", new Date());

            Fa fa = jsonObject.toJavaObject(Fa.class);

            fa.setAddtime(dateToStr).setSqrq(dateToStr).setFk_xtglid(cuser.getFk_xtglid())
                    .setFk_qybm(cuser.getFk_qybm()).setId(IdUtils.randomUUID())
                    .setZt(Constants.FaZt.SHZ).setFazt(Constants.FaZt.SHZ)
                    .setFk_wyid(cuser.getFk_id()).setYfzt(Constants.Yfzt.WYF)
                    .setJszt(Constants.Jszt.WJS);
            //费项明细
            List<Fa_mx> mXlist = fa.getMXlist();
            if (mXlist != null && !mXlist.isEmpty()) {
                for (Fa_mx fa_mx : mXlist) {
                    fa_mx.setId(IdUtils.randomUUID()).setAddtime(dateToStr).setFk_qybm(cuser.getFk_qybm())
                            .setFk_xtglid(cuser.getFk_xtglid()).setFk_faid(fa.getId());
                }
            }
            //房号列表
            List<Fa_fh> fHlist = fa.getFHlist();
            if (fHlist != null && !fHlist.isEmpty()) {
                for (Fa_fh fa_fh : fHlist) {
                    fa_fh.setId(IdUtils.randomUUID()).setAddtime(dateToStr).setFk_qybm(cuser.getFk_qybm())
                            .setFk_xtglid(cuser.getFk_xtglid()).setFk_faid(fa.getId());
                }
            }

            // 4. 启动流程，获取启动结点信息
            WorkflowNode startNode = workflowService.getWorkflowStartNode(request, Constants.WorkflowId.XH);
            // 5. 获取启动结点的下一个结点
            WorkflowNode nextNode = workflowService.getWorkflowNextNode(request, Constants.WorkflowId.XH, startNode.getId());
            // 6. 构造流程实例
            WorkflowInstant wfi = new WorkflowInstant();
            wfi.setId(IdUtils.randomUUID())
                    .setFk_dqjdid(nextNode.getId())
                    .setFk_qybm(cuser.getFk_qybm())
                    .setFk_xtglid(cuser.getFk_xtglid())
                    .setFk_wfid(Constants.WorkflowId.FA)
                    .setFk_yhid(cuser.getId())
                    .setLx(Constants.WorkflowType.FA)
                    .setZt(Constants.WorkflowStatus.SHZ)
                    .setFqr(cuser.getUsername())
                    .setFk_ywid(fa.getId())
                    .setFqsj(dateToStr).setAddtime(dateToStr);
            // 7. 构造流程历史
            WorkflowHistory wfh = new WorkflowHistory();
            wfh.setId(IdUtils.randomUUID())
                    .setFk_qybm(cuser.getFk_qybm())
                    .setFk_xtglid(cuser.getFk_xtglid())
                    .setFk_jdid(startNode.getId())
                    .setFk_yhid(cuser.getId())
                    .setSpr(cuser.getUsername())
                    .setSpsj(dateToStr)
                    .setCzmc(Constants.WorkflowNodeName.START).setLx(Constants.WorkflowType.FA)
                    .setFk_wkslid(wfi.getId()).setFk_ywid(fa.getId()).setAddtime(dateToStr);

            JSONObject json = new JSONObject();

            //方案基本内容
            JSONObject faJson = JSONObject.parseObject(JSONObject.toJSONString(fa));
            faJson.remove("fHlist");
            faJson.remove("mXlist");
            json.put("model", faJson);
            //费项明细
            JSONArray mxJson = JSONArray.parseArray(JSONArray.toJSONString(mXlist));
            json.put("mXlist", mxJson);
            //房号信息
            JSONArray fhJson = JSONArray.parseArray(JSONArray.toJSONString(fHlist));
            json.put("fHlist", fhJson);
            //流程实例
            JSONObject jsonObject_wfi = JSONObject.parseObject(JSONObject.toJSONString(wfi));
            json.put("sLmodel", jsonObject_wfi);
            //流程历史
            JSONObject jsonObject_wfh = JSONObject.parseObject(JSONObject.toJSONString(wfh));
            json.put("hiSmodel", jsonObject_wfh);

            faApi.aeFa(request, json);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("方案提交成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("方案提交失败", e);
            throw new BusinessException("方案提交失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
