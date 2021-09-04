package com.fdkj.wywxjj.controller.fa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.fa.Fa;
import com.fdkj.wywxjj.api.model.fa.js.Fa_js;
import com.fdkj.wywxjj.api.model.fa.js.Fa_js_ft;
import com.fdkj.wywxjj.api.model.fa.yf.Fa_yf;
import com.fdkj.wywxjj.api.model.fa.yf.Fa_yf_ft;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.api.model.zhMgr.Zh_his;
import com.fdkj.wywxjj.api.util.FaApi;
import com.fdkj.wywxjj.api.util.FaJsApi;
import com.fdkj.wywxjj.api.util.FhApi;
import com.fdkj.wywxjj.api.util.ZhApi;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.constant.Constants;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
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
import java.math.BigDecimal;
import java.util.*;

/**
 * 方案结算
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/FAJS")
public class FaJsController {
    private static final Logger log = LoggerFactory.getLogger(FaJsController.class);

    @Autowired
    private FaJsApi faJsApi;
    @Autowired
    private FaApi faApi;
    @Autowired
    private FhApi fhApi;
    @Autowired
    private ZhApi zhApi;

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
        request.setAttribute("cuser", faJsApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        return new ModelAndView("faMgr/fajs/js_index");
    }

    /**
     * 跳转
     *
     * @param request req
     * @param opts    操作权限
     * @return res
     * @throws Exception err
     */
    @RequestMapping("toAdd/{fk_faid}")
    public ModelAndView toAdd(HttpServletRequest request,
                              @RequestParam(value = "opts", required = false) List<String> opts,
                              @PathVariable("fk_faid") String fk_faid) throws Exception {
        request.setAttribute("cuser", faJsApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        Fa faDetail = faApi.getFaDetail(request, fk_faid);
        request.setAttribute("fa", faDetail);
        return new ModelAndView("faMgr/fajs/js_add");
    }

    /**
     * 跳转
     *
     * @param request req
     * @param opts    操作权限
     * @param id      方案预付id
     * @return res
     * @throws Exception err
     */
    @RequestMapping("toInfo/{id}")
    public ModelAndView toInfo(HttpServletRequest request,
                               @RequestParam(value = "opts", required = false) List<String> opts,
                               @PathVariable("id") String id) throws Exception {
        request.setAttribute("cuser", faJsApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("id", id);
        return new ModelAndView("faMgr/fajs/js_info");
    }

    /**
     * 判断有没有结算单
     *
     * @param request req
     * @param fk_qybm 区域编码
     * @param fk_faid 方案id
     * @return res
     */
    @RequestMapping("hasJsData/{fk_faid}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> hasJsData(HttpServletRequest request,
                                                     @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                     @PathVariable("fk_faid") String fk_faid) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            reqBody.put("fk_faid", fk_faid);
            //请求接口
            Page<Fa_js> fajsList = faJsApi.getFajsList(request, reqBody, 1, 1);

            String fa_js_id = null;
            //如果有，则结果改为true
            if (fajsList.getTotalRecord() > 0) {
                fa_js_id = fajsList.getDataList().get(0).getId();
            }
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("判断是否有结算信息成功", fa_js_id);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("判断是否有结算信息失败", e);
            throw new BusinessException("判断是否有结算信息失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 结算分摊
     *
     * @param request req
     * @param fa_js   预付信息
     * @return res
     */
    @RequestMapping("buildFtData")
    @ResponseBody
    public ResponseEntity<CusResponseBody> buildFtData(HttpServletRequest request,
                                                       @RequestBody Fa_js fa_js) {
        try {
            if (fa_js == null) {
                throw new BusinessException("请求体为空", HttpStatus.BAD_REQUEST.value());
            }
            String fk_faid = fa_js.getFk_faid();
            if (StringUtils.isBlank(fk_faid)) {
                throw new BusinessException("请求数据有误", HttpStatus.BAD_REQUEST.value());
            }
            //1. 获取方案信息
            Fa faDetail = faApi.getFaDetail(request, fk_faid);
            //2. 获取方案对应的账户房间信息
            String fk_xmxxid = faDetail.getFk_xmxxid();
            String fk_ldxxid = faDetail.getFk_ldxxid();
            if (StringUtils.isBlank(fk_xmxxid) || StringUtils.isBlank(fk_ldxxid)) {
                throw new BusinessException("请求数据有误", HttpStatus.BAD_REQUEST.value());
            }
            String szdy = faDetail.getDyh();
            //请求接口获取房间账户信息
            Map<String, Object> reqBody = new HashMap<>();
            reqBody.put("fk_xmxxid", fk_xmxxid.trim());
            reqBody.put("fk_ldxxid", fk_ldxxid.trim());
            if (StringUtils.isNotBlank(szdy)) {
                reqBody.put("szdy", szdy.trim());
            }
            List<Fh> fhList = fhApi.getFhAllList(request, reqBody);
            if (fhList == null || fhList.isEmpty()) {
                if (StringUtils.isBlank(fk_ldxxid)) {
                    throw new BusinessException("请求数据有误", HttpStatus.BAD_REQUEST.value());
                }
            }
            //4. 计算分摊金额
            //分摊金额
            String ftfs = faDetail.getFtfs();
            //方案预算
            String fayjje = faDetail.getFayjje();
            //结算金额
            String jsje = fa_js.getJsje();
            //应支付金额
            String yzfje = fa_js.getYzfje();
            if (StringUtils.isBlank(ftfs) || StringUtils.isBlank(fayjje)) {
                throw new BusinessException("请求数据有误", HttpStatus.BAD_REQUEST.value());
            }
            // 计算
            if (Constants.Ftfs.PJFT.equals(ftfs)) {
                List<String> bgs = new ArrayList<>();
                //获取房间个数
                int fhgs = fhList.size();
                //计算每户的金额
                String divide = BigDecimalUtil.divideDown(yzfje, (fhgs + "")).toPlainString();
                //判断金额
                for (Fh fh : fhList) {
                    fh.setFtje(divide);
                }
                CusResponseBody cusResponseBody = CusResponseBody.success("获取分摊信息成功", fhList);
                return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
            } else if (Constants.Ftfs.MJFT.equals(ftfs)) {
                //计算房间的总面积
                String zmj = "0";
                //判断金额
                for (Fh fh : fhList) {
                    String cmj = fh.getScmj_jzmj();
                    zmj = BigDecimalUtil.add(zmj, cmj).toPlainString();
                }
                for (Fh fh : fhList) {
                    String cmj = fh.getScmj_jzmj();
                    String je = BigDecimalUtil.divideDown(BigDecimalUtil.multiply(yzfje, cmj).toPlainString(), zmj).toPlainString();
                    fh.setFtje(je);
                }
                CusResponseBody cusResponseBody = CusResponseBody.success("获取分摊信息成功", fhList);
                return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
            } else {
                throw new BusinessException("分摊生成失败: 分摊方式错误", HttpStatus.BAD_REQUEST.value());
            }
        } catch (Exception e) {
            log.error("分摊生成失败", e);
            if (e instanceof BusinessException) {
                throw (BusinessException) e;
            } else {
                throw new BusinessException("分摊生成失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            }
        }
    }

    /**
     * 添加方案结算
     *
     * @param request req
     * @param reqBody 请求体
     * @return res
     */
    @RequestMapping("addFajs")
    @ResponseBody
    public ResponseEntity<CusResponseBody> buildFtData(HttpServletRequest request,
                                                       @RequestBody JSONObject reqBody) {
        try {
            //登录用户
            User cuser = faJsApi.getUserFromCookie(request);
            String dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd'T'HH:mm:ss.sss", new Date());

            //方案预付信息
            Fa_js fa_js = reqBody.getObject("wywxjJ_FA_JSKmodel", Fa_js.class);
            //分摊信息
            List<Fh> fhList = reqBody.getJSONArray("wywxjJ_FA_JS_FTlist").toJavaList(Fh.class);

            //1. 获取方案信息
            String fk_faid = fa_js.getFk_faid();
            Fa faDetail = faApi.getFaDetail(request, fk_faid);
            //2. 判断方案状态，如果方案已结算，不能添加预付
            if (Constants.Jszt.YJS.equals(faDetail.getJszt())) {
                log.error("方案(" + faDetail.getId() + ")已结算，无法提交预付");
                throw new BusinessException("该方案已结算，无法提交预付", HttpStatus.BAD_REQUEST.value());
            }
            //3. 判断方案的预付款是否发生变化
            if(faDetail.getYfk() != null && fa_js.getYfje() != null) {
                if(!faDetail.getYfk().trim().equals(fa_js.getYfje().trim())) {
                    log.error("方案(" + faDetail.getId() + ")预付款信息发生变化，无法提交结算");
                    throw new BusinessException("该方案预付款信息发生变化，无法提交结算", HttpStatus.BAD_REQUEST.value());
                }
            }
            //4. 判断分摊信息
            if (fhList == null || fhList.isEmpty()) {
                throw new BusinessException("分摊信息为空", HttpStatus.BAD_REQUEST.value());
            }
            //5. 构造方案结算信息
            fa_js.setId(IdUtils.randomUUID()).setAddtime(dateToStr).setFk_yhid(cuser.getId())
                    .setCzr(cuser.getUsername()).setZt(Constants.Zfzt.WZF);
            //6. 修改方案数据(预付状态，预付款)
            faDetail.setJszt(Constants.Jszt.YJS);
            //7. 构造方案预付分摊信息
            List<Fa_js_ft> ftList = new ArrayList<>();
            List<Zh> zhList = new ArrayList<>();
            List<Zh_his> zh_hisList = new ArrayList<>();
            for (Fh fh : fhList) {
                Fa_js_ft fa_js_ft = new Fa_js_ft();
                fa_js_ft.setId(IdUtils.randomUUID()).setAddtime(dateToStr)
                        .setFk_qybm(fa_js.getFk_qybm()).setFk_xtglid(fa_js.getFk_xtglid())
                        .setFk_jsid(fa_js.getId()).setFk_faid(faDetail.getId())
                        .setFk_fhid(fh.getId()).setFk_zhid(fh.getZh().getId())
                        .setFtje(fh.getFtje());
                ftList.add(fa_js_ft);

                //处理账户信息(计算钱，历史信息)
                String fk_zhid = fh.getZh().getId();
                Zh zhDetail = zhApi.getZhDetail(request, fk_zhid);
                String money = zhDetail.getMoney();
                //计算余额
                BigDecimal subtract = BigDecimalUtil.subtract(money, fa_js_ft.getFtje());
                zhDetail.setMoney(subtract.toPlainString());

                zhList.add(zhDetail);

                //账户历史信息
                Zh_his zh_his = new Zh_his();
                zh_his.setFk_zhid(zhDetail.getId()).setAddtime(dateToStr).setJzrq(dateToStr)
                        .setCzlx(Constants.JzLb.WXJS)
                        .setZc(fa_js_ft.getFtje()).setZhye(zhDetail.getMoney());

                zh_hisList.add(zh_his);
            }

            //7. 构造请求接口数据
            JSONObject json = new JSONObject();
            //预付款信息
            json.put("wywxjJ_FA_JSKmodel", JSONObject.parseObject(JSONObject.toJSONString(fa_js)));
            //方案信息
            json.put("wywxjJ_FAmodel", JSONObject.parseObject(JSONObject.toJSONString(faDetail)));
            //预付款分摊信息
            json.put("wywxjJ_FA_JS_FTlist", JSONArray.parseArray(JSONArray.toJSONString(ftList)));
            //账户信息
            json.put("wywxjJ_ZHmodel", JSONArray.parseArray(JSONArray.toJSONString(zhList)));
            //账户分摊信息
            json.put("wywxjJ_ZH_HISmodel", JSONArray.parseArray(JSONArray.toJSONString(zh_hisList)));
            //请求接口
            faJsApi.addFa_js(request, json);

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("添加方案结算成功", fhList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("添加方案结算失败", e);
            if (e instanceof BusinessException) {
                throw (BusinessException) e;
            } else {
                throw new BusinessException("添加方案结算失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            }
        }
    }

    //获取方案预付详情
    @RequestMapping("getDetail/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getDetail(HttpServletRequest request,
                                                     @PathVariable("id") String id) {
        try {
            //登录用户
            User cuser = faJsApi.getUserFromCookie(request);
            //预付详情
            Fa_js fajsDetail = faJsApi.getFajsDetail(request, id);
            //分摊列表
            List<Fa_js_ft> ftList = fajsDetail.getFtList();
            List<Fh> fhList = new ArrayList<>();
            for (Fa_js_ft fa_js_ft : ftList) {
                String fk_fhid = fa_js_ft.getFk_fhid();
                String fk_zhid = fa_js_ft.getFk_zhid();

                Fh fhDetail = fhApi.getFhDetail(request, fk_fhid);
                fhDetail.setFtje(fa_js_ft.getFtje());

                Zh zhDetail = zhApi.getZhDetail(request, fk_zhid);
                fhDetail.setZh(zhDetail);

                fhList.add(fhDetail);
            }
            fajsDetail.setFtList2(fhList);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取方案结算详情成功", fajsDetail);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取方案结算详情失败", e);
            if (e instanceof BusinessException) {
                throw (BusinessException) e;
            } else {
                throw new BusinessException("获取方案结算详情失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            }
        }
    }

    /**
     * 删除方案结算
     *
     * @param request req
     * @param id      预付id
     * @return res
     */
    @RequestMapping("del/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> del(HttpServletRequest request,
                                               @PathVariable("id") String id) {
        try {
            //登录用户
            User cuser = faJsApi.getUserFromCookie(request);
            //时间
            String dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd'T'HH:mm:ss.sss", new Date());
            //结算详情
            Fa_js fajsDetail = faJsApi.getFajsDetail(request, id);
            //分摊列表
            List<Fa_js_ft> ftList = fajsDetail.getFtList();
            // 获取方案信息
            String fk_faid = fajsDetail.getFk_faid();
            Fa faDetail = faApi.getFaDetail(request, fk_faid);
            //1. 修改方案结算状态
            faDetail.setJszt(Constants.Jszt.WJS);
            //2. 算钱(账户余额，历史)
            List<Zh> zhList = new ArrayList<>();
            List<Zh_his> zh_hisList = new ArrayList<>();
            for (Fa_js_ft fa_js_ft : ftList) {
                //处理账户信息(计算钱，历史信息)
                String fk_zhid = fa_js_ft.getFk_zhid();
                Zh zhDetail = zhApi.getZhDetail(request, fk_zhid);
                String money = zhDetail.getMoney();
                //计算余额
                BigDecimal add = BigDecimalUtil.add(money, fa_js_ft.getFtje());
                zhDetail.setMoney(add.toPlainString());

                zhList.add(zhDetail);

                //账户历史信息
                Zh_his zh_his = new Zh_his();
                zh_his.setFk_zhid(zhDetail.getId()).setAddtime(dateToStr).setJzrq(dateToStr)
                        .setCzlx(Constants.JzLb.JSFK)
                        .setSr(fa_js_ft.getFtje()).setZhye(zhDetail.getMoney());

                zh_hisList.add(zh_his);
            }

            //4. 构造请求接口数据
            JSONObject json = new JSONObject();
            //预付款id
            json.put("id", id);
            //方案信息
            json.put("wywxjJ_FAmodel", JSONObject.parseObject(JSONObject.toJSONString(faDetail)));
            //账户信息
            json.put("wywxjJ_ZHmodel", JSONArray.parseArray(JSONArray.toJSONString(zhList)));
            //账户分摊信息
            json.put("wywxjJ_ZH_HISmodel", JSONArray.parseArray(JSONArray.toJSONString(zh_hisList)));
            //请求接口
            faJsApi.delFa_js(request, json);

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("删除方案结算成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("删除方案结算失败", e);
            if (e instanceof BusinessException) {
                throw (BusinessException) e;
            } else {
                throw new BusinessException("删除方案预付失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            }
        }
    }
}
