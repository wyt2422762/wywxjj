package com.fdkj.wywxjj.controller.tj;

import com.fdkj.wywxjj.api.model.fa.Fa;
import com.fdkj.wywxjj.api.model.fa.js.Fa_js;
import com.fdkj.wywxjj.api.model.fa.js.Fa_js_ft;
import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import com.fdkj.wywxjj.api.model.xmMgr.Ld;
import com.fdkj.wywxjj.api.model.xmMgr.Xm;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.api.util.*;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.constant.Constants;
import com.fdkj.wywxjj.controller.BaseController;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import com.fdkj.wywxjj.utils.DateUtils;
import com.fdkj.wywxjj.utils.math.BigDecimalUtil;
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
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 方案结算统计
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/TJ/FAJSTJ")
public class FaJsTjController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(FaJsTjController.class);

    @Autowired
    private FaApi faApi;
    @Autowired
    private FaJsApi faJsApi;
    @Autowired
    private XmApi xmApi;
    @Autowired
    private LdApi ldApi;
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
        request.setAttribute("cuser", faApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }

        return new ModelAndView("tj/fajstj/index");
    }


    /**
     * 获取方案列表
     *
     * @param request   req
     * @param fk_qybm   区域编码
     * @param fk_wyid   物业id
     * @param fk_xmxxid 项目信息id
     * @param fk_ldxxid 楼栋信息id
     * @param dyh       单元号
     * @return res
     */
    @RequestMapping("getFaList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getFaList(HttpServletRequest request,
                                                     @RequestParam(value = "startDate", required = false) String startDate,
                                                     @RequestParam(value = "endDate", required = false) String endDate,
                                                     @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                     @RequestParam(value = "fk_wyid", required = false) String fk_wyid,
                                                     @RequestParam(value = "fk_xmxxid", required = false) String fk_xmxxid,
                                                     @RequestParam(value = "fk_ldxxid", required = false) String fk_ldxxid,
                                                     @RequestParam(value = "dyh", required = false) String dyh,
                                                     @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_wyid)) {
                reqBody.put("fk_wyid", fk_wyid);
            }
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            if (StringUtils.isNotBlank(fk_xmxxid)) {
                reqBody.put("fk_xmxxid", fk_xmxxid);
            }
            if (StringUtils.isNotBlank(fk_ldxxid)) {
                reqBody.put("fk_ldxxid", fk_ldxxid);
            }
            if (StringUtils.isNotBlank(dyh)) {
                reqBody.put("dyh", dyh);
            }

            reqBody.put("jszt", Constants.Jszt.YJS);

            Page<Fa> faList = faApi.getFaList2(request, reqBody, page, limit, startDate, endDate);

            List<Fa> dataList = faList.getDataList();
            //查询明细
            if(dataList != null && !dataList.isEmpty()) {
                for (Fa fa : dataList) {
                    String fk_xmxxid1 = fa.getFk_xmxxid();
                    String fk_ldxxid1 = fa.getFk_ldxxid();
                    String dyh1 = fa.getDyh();

                    Xm xmDetail = xmApi.getXmDetail(request, fk_xmxxid1);
                    Ld ldDetail = ldApi.getLdDetail(request, fk_ldxxid1);
                    StringBuilder sb = new StringBuilder();
                    sb.append(xmDetail.getXmmc()).append("-").append(ldDetail.getCmc());
                    if (StringUtils.isNotBlank(dyh1)) {
                        sb.append(dyh1);
                    }
                    fa.setDesc(sb.toString());
                }
            }

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取方案列表成功", faList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取方案列表失败", e);
            throw new BusinessException("获取方案列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取方案结算列表
     *
     * @param request req
     * @param id      方案id
     * @return res
     */
    @RequestMapping("getFaJsList/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getFaJsList(HttpServletRequest request, @PathVariable String id) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            reqBody.put("fk_faid", id);

            List<Fa_js> fajsList = faJsApi.getFajsList(request, reqBody);

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取方案结算数据成功", fajsList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取方案结算数据失败", e);
            throw new BusinessException("获取方案结算数据失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取方案结算分摊列表
     *
     * @param request req
     * @param id      方案结算id
     * @return res
     */
    @RequestMapping("getFaJsFtList/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getFaJsFtList(HttpServletRequest request, @PathVariable String id) {
        try {
            //结算详情
            Fa_js faJsDetail = faJsApi.getFajsDetail(request, id);

            //分摊列表
            List<Fa_js_ft> ftList = faJsDetail.getFtList();
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
            faJsDetail.setFtList2(fhList);

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取方案结算分摊数据成功", faJsDetail);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取方案结算分摊数据失败", e);
            throw new BusinessException("获取方案结算分摊数据失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 打印
     * @param request req
     * @param response res
     */
    @RequestMapping("print")
    public void print(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "startDate", required = false) String startDate,
                             @RequestParam(value = "endDate", required = false) String endDate,
                             @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                             @RequestParam(value = "fk_wyid", required = false) String fk_wyid,
                             @RequestParam(value = "fk_xmxxid", required = false) String fk_xmxxid,
                             @RequestParam(value = "fk_ldxxid", required = false) String fk_ldxxid,
                             @RequestParam(value = "dyh", required = false) String dyh) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_wyid)) {
                reqBody.put("fk_wyid", fk_wyid);
            }
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            if (StringUtils.isNotBlank(fk_xmxxid)) {
                reqBody.put("fk_xmxxid", fk_xmxxid);
            }
            if (StringUtils.isNotBlank(fk_ldxxid)) {
                reqBody.put("fk_ldxxid", fk_ldxxid);
            }
            if (StringUtils.isNotBlank(dyh)) {
                reqBody.put("dyh", dyh);
            }

            reqBody.put("jszt", Constants.Jszt.YJS);

            List<Fa> faList = faApi.getFaList2(request, reqBody, startDate, endDate);

            int hj = 0;
            String totalMoney = "0.00";

            //查询明细
            if(faList != null && !faList.isEmpty()) {
                for (Fa fa : faList) {
                    String fk_xmxxid1 = fa.getFk_xmxxid();
                    String fk_ldxxid1 = fa.getFk_ldxxid();
                    String dyh1 = fa.getDyh();

                    Xm xmDetail = xmApi.getXmDetail(request, fk_xmxxid1);
                    Ld ldDetail = ldApi.getLdDetail(request, fk_ldxxid1);
                    StringBuilder sb = new StringBuilder();
                    sb.append(xmDetail.getXmmc()).append("-").append(ldDetail.getCmc());
                    if (StringUtils.isNotBlank(dyh1)) {
                        sb.append(dyh1);
                    }

                    fa.setDesc(sb.toString());

                    hj++;
                    totalMoney = BigDecimalUtil.add(totalMoney, fa.getFayjje()).toPlainString();

                    //获取结算信息
                    String fk_faid = fa.getId();
                    Map<String, String> reqBody_yf = new HashMap<>();
                    reqBody_yf.put("fk_faid", fk_faid);
                    List<Fa_js> fajsList = faJsApi.getFajsList(request, reqBody_yf);
                    if(fajsList != null && !fajsList.isEmpty()) {
                        fa.setJslist(fajsList);

                        int hj_js = 0;
                        String jsTotalMoney = "0.00";
                        String jsTotalMoney_yf = "0.00";
                        String jsTotalMoney_zf = "0.00";

                        for (Fa_js fa_js : fajsList) {
                            hj_js++;
                            jsTotalMoney = BigDecimalUtil.add(jsTotalMoney, fa_js.getJsje()).toPlainString();
                            jsTotalMoney_yf = BigDecimalUtil.add(jsTotalMoney_yf, fa_js.getYfje()).toPlainString();
                            jsTotalMoney_zf = BigDecimalUtil.add(jsTotalMoney_zf, fa_js.getYzfje()).toPlainString();
                        }

                        fa.setJsHj("共" + hj_js + "个");
                        fa.setJsTotalMoney(jsTotalMoney);
                        fa.setJsTotalMoney_yf(jsTotalMoney_yf);
                        fa.setJsTotalMoney_zf(jsTotalMoney_zf);
                    }

                }
            }

            //单据模板
            //模板参数
            Map<String, Object> params = new HashMap<>();
            if(StringUtils.isNotBlank(fk_xmxxid)) {
                Xm xmDetail = xmApi.getXmDetail(request, fk_xmxxid);
                params.put("fk_xmxxid", xmDetail.getXmmc());
            }
            if(StringUtils.isNotBlank(fk_ldxxid)) {
                Ld ldDetail = ldApi.getLdDetail(request, fk_ldxxid);
                params.put("fk_ldxxid", ldDetail.getCh());
            }
            if(StringUtils.isNotBlank(dyh)) {
                params.put("dyh", dyh);
            }

            if(faList != null && !faList.isEmpty()) {
                params.put("faList", faList);
            }

            params.put("hj", "共" + hj + "个");
            params.put("totalMoney", totalMoney);

            params.put("dyrq", DateUtils.parseDateToStr("yyyy年MM月dd日", new Date()));

            String staticSection = buildStaticSection(startDate, endDate);
            if(StringUtils.isNotBlank(staticSection)) {
                params.put("tjqj", staticSection.trim());
            }

            //打印
            printWord2pdf(response, "方案结算打印.xml", params, "方案结算统计.pdf");

        } catch (Exception e) {
            log.error("方案结算统计打印失败", e);
        }
    }


    /**
     * 打印明细
     * @param request req
     * @param response res
     */
    @RequestMapping("printDetail")
    public void printDetail(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "startDate", required = false) String startDate,
                             @RequestParam(value = "endDate", required = false) String endDate,
                             @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                             @RequestParam(value = "fk_wyid", required = false) String fk_wyid,
                             @RequestParam(value = "fk_xmxxid", required = false) String fk_xmxxid,
                             @RequestParam(value = "fk_ldxxid", required = false) String fk_ldxxid,
                             @RequestParam(value = "dyh", required = false) String dyh) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_wyid)) {
                reqBody.put("fk_wyid", fk_wyid);
            }
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            if (StringUtils.isNotBlank(fk_xmxxid)) {
                reqBody.put("fk_xmxxid", fk_xmxxid);
            }
            if (StringUtils.isNotBlank(fk_ldxxid)) {
                reqBody.put("fk_ldxxid", fk_ldxxid);
            }
            if (StringUtils.isNotBlank(dyh)) {
                reqBody.put("dyh", dyh);
            }

            reqBody.put("jszt", Constants.Jszt.YJS);

            List<Fa> faList = faApi.getFaList2(request, reqBody, startDate, endDate);

            int hj = 0;
            String jsTotalMoney = "0.00";
            String jsTotalMoney_yf = "0.00";
            String jsTotalMoney_zf = "0.00";

            List<Fa_js> jsList = new ArrayList<>();

            //查询明细
            if(faList != null && !faList.isEmpty()) {
                for (Fa fa : faList) {

                    //获取结算信息
                    String fk_faid = fa.getId();
                    Map<String, String> reqBody_yf = new HashMap<>();
                    reqBody_yf.put("fk_faid", fk_faid);
                    List<Fa_js> fajsList = faJsApi.getFajsList(request, reqBody_yf);
                    if(fajsList != null && !fajsList.isEmpty()) {
                        fa.setJslist(fajsList);

                        for (Fa_js fa_js : fajsList) {
                            hj++;
                            jsTotalMoney = BigDecimalUtil.add(jsTotalMoney, fa_js.getJsje()).toPlainString();
                            jsTotalMoney_yf = BigDecimalUtil.add(jsTotalMoney_yf, fa_js.getYfje()).toPlainString();
                            jsTotalMoney_zf = BigDecimalUtil.add(jsTotalMoney_zf, fa_js.getYzfje()).toPlainString();

                            //获取分摊信息
                            //结算详情
                            Fa_js fajsDetail = faJsApi.getFajsDetail(request, fa_js.getId());
                            //分摊列表
                            List<Fa_js_ft> ftList = fajsDetail.getFtList();

                            int hj_js_ft = 0;
                            String totalMoney_js_ft = "0.00";

                            List<Fh> fhList = new ArrayList<>();
                            for (Fa_js_ft fa_js_ft : ftList) {
                                String fk_fhid = fa_js_ft.getFk_fhid();
                                String fk_zhid = fa_js_ft.getFk_zhid();

                                Fh fhDetail = fhApi.getFhDetail(request, fk_fhid);
                                fhDetail.setFtje(fa_js_ft.getFtje());

                                Zh zhDetail = zhApi.getZhDetail(request, fk_zhid);
                                fhDetail.setZh(zhDetail);

                                fhList.add(fhDetail);

                                hj_js_ft++;
                                totalMoney_js_ft = BigDecimalUtil.add(totalMoney_js_ft, fa_js_ft.getFtje()).toPlainString();

                            }
                            fajsDetail.setFtList2(fhList);

                            fajsDetail.setFtHj("共" + hj_js_ft + "个");
                            fajsDetail.setFtTotalMoney(totalMoney_js_ft);

                            jsList.add(fajsDetail);
                        }

                    }
                }
            }

            //单据模板
            //模板参数
            Map<String, Object> params = new HashMap<>();
            if(StringUtils.isNotBlank(fk_xmxxid)) {
                Xm xmDetail = xmApi.getXmDetail(request, fk_xmxxid);
                params.put("fk_xmxxid", xmDetail.getXmmc());
            }
            if(StringUtils.isNotBlank(fk_ldxxid)) {
                Ld ldDetail = ldApi.getLdDetail(request, fk_ldxxid);
                params.put("fk_ldxxid", ldDetail.getCh());
            }
            if(StringUtils.isNotBlank(dyh)) {
                params.put("dyh", dyh);
            }

            if(!jsList.isEmpty()) {
                params.put("faJsList", jsList);
            }

            params.put("hj", "共" + hj + "个");
            params.put("jsTotalMoney", jsTotalMoney);
            params.put("jsTotalMoney_yf", jsTotalMoney_yf);
            params.put("jsTotalMoney_zf", jsTotalMoney_zf);

            params.put("dyrq", DateUtils.parseDateToStr("yyyy年MM月dd日", new Date()));

            String staticSection = buildStaticSection(startDate, endDate);
            if(StringUtils.isNotBlank(staticSection)) {
                params.put("tjqj", staticSection.trim());
            }

            //打印
            printWord2pdf(response, "方案结算明细打印.xml", params, "方案结算明细统计.pdf");

        } catch (Exception e) {
            log.error("方案结算明细统计打印失败", e);
        }
    }

    /**
     * 打印明细
     * @param request req
     * @param response res
     */
    @RequestMapping("printDetail2")
    public void printDetail2(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "startDate", required = false) String startDate,
                            @RequestParam(value = "endDate", required = false) String endDate,
                            @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                            @RequestParam(value = "fk_wyid", required = false) String fk_wyid,
                            @RequestParam(value = "fk_xmxxid", required = false) String fk_xmxxid,
                            @RequestParam(value = "fk_ldxxid", required = false) String fk_ldxxid,
                            @RequestParam(value = "dyh", required = false) String dyh) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_wyid)) {
                reqBody.put("fk_wyid", fk_wyid);
            }
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            if (StringUtils.isNotBlank(fk_xmxxid)) {
                reqBody.put("fk_xmxxid", fk_xmxxid);
            }
            if (StringUtils.isNotBlank(fk_ldxxid)) {
                reqBody.put("fk_ldxxid", fk_ldxxid);
            }
            if (StringUtils.isNotBlank(dyh)) {
                reqBody.put("dyh", dyh);
            }

            reqBody.put("jszt", Constants.Jszt.YJS);

            List<Fa> faList = faApi.getFaList2(request, reqBody, startDate, endDate);

            int hj = 0;
            String jsTotalMoney = "0.00";
            String jsTotalMoney_yf = "0.00";
            String jsTotalMoney_zf = "0.00";

            List<Fa_js> jsList = new ArrayList<>();

            //查询明细
            if(faList != null && !faList.isEmpty()) {
                for (Fa fa : faList) {

                    //获取结算信息
                    String fk_faid = fa.getId();
                    Map<String, String> reqBody_yf = new HashMap<>();
                    reqBody_yf.put("fk_faid", fk_faid);
                    List<Fa_js> fajsList = faJsApi.getFajsList(request, reqBody_yf);
                    if(fajsList != null && !fajsList.isEmpty()) {
                        fa.setJslist(fajsList);

                        for (Fa_js fa_js : fajsList) {
                            hj++;
                            jsTotalMoney = BigDecimalUtil.add(jsTotalMoney, fa_js.getJsje()).toPlainString();
                            jsTotalMoney_yf = BigDecimalUtil.add(jsTotalMoney_yf, fa_js.getYfje()).toPlainString();
                            jsTotalMoney_zf = BigDecimalUtil.add(jsTotalMoney_zf, fa_js.getYzfje()).toPlainString();

                            //获取分摊信息
                            //结算详情
                            Fa_js fajsDetail = faJsApi.getFajsDetail(request, fa_js.getId());
                            //分摊列表
                            List<Fa_js_ft> ftList = fajsDetail.getFtList();

                            int hj_js_ft = 0;
                            String totalMoney_js_ft = "0.00";

                            List<Fh> fhList = new ArrayList<>();
                            for (Fa_js_ft fa_js_ft : ftList) {
                                String fk_fhid = fa_js_ft.getFk_fhid();
                                String fk_zhid = fa_js_ft.getFk_zhid();

                                Fh fhDetail = fhApi.getFhDetail(request, fk_fhid);
                                fhDetail.setFtje(fa_js_ft.getFtje());

                                Zh zhDetail = zhApi.getZhDetail(request, fk_zhid);
                                fhDetail.setZh(zhDetail);

                                fhList.add(fhDetail);

                                hj_js_ft++;
                                totalMoney_js_ft = BigDecimalUtil.add(totalMoney_js_ft, fa_js_ft.getFtje()).toPlainString();

                            }
                            fajsDetail.setFtList2(fhList);

                            fajsDetail.setFtHj("共" + hj_js_ft + "个");
                            fajsDetail.setFtTotalMoney(totalMoney_js_ft);

                            jsList.add(fajsDetail);
                        }

                    }
                }
            }

            //单据模板
            //模板参数
            Map<String, Object> params = new HashMap<>();
            if(StringUtils.isNotBlank(fk_xmxxid)) {
                Xm xmDetail = xmApi.getXmDetail(request, fk_xmxxid);
                params.put("fk_xmxxid", xmDetail.getXmmc());
            }
            if(StringUtils.isNotBlank(fk_ldxxid)) {
                Ld ldDetail = ldApi.getLdDetail(request, fk_ldxxid);
                params.put("fk_ldxxid", ldDetail.getCh());
            }
            if(StringUtils.isNotBlank(dyh)) {
                params.put("dyh", dyh);
            }

            if(!jsList.isEmpty()) {
                params.put("faJsList", jsList);
            }

            params.put("hj", "共" + hj + "个");
            params.put("jsTotalMoney", jsTotalMoney);
            params.put("jsTotalMoney_yf", jsTotalMoney_yf);
            params.put("jsTotalMoney_zf", jsTotalMoney_zf);

            params.put("dyrq", DateUtils.parseDateToStr("yyyy年MM月dd日", new Date()));

            String staticSection = buildStaticSection(startDate, endDate);
            if(StringUtils.isNotBlank(staticSection)) {
                params.put("tjqj", staticSection.trim());
            }

            //打印
            printHtml2pdf(response, "方案结算明细打印.html", params, "方案结算明细统计.pdf");

        } catch (Exception e) {
            log.error("方案结算明细统计打印失败", e);
        }
    }

}
