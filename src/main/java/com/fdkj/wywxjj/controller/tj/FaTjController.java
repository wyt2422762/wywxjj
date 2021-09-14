package com.fdkj.wywxjj.controller.tj;

import com.fdkj.wywxjj.api.model.fa.Fa;
import com.fdkj.wywxjj.api.model.xmMgr.Ld;
import com.fdkj.wywxjj.api.model.xmMgr.Xm;
import com.fdkj.wywxjj.api.util.FaApi;
import com.fdkj.wywxjj.api.util.FhApi;
import com.fdkj.wywxjj.api.util.LdApi;
import com.fdkj.wywxjj.api.util.XmApi;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.controller.BaseController;
import com.fdkj.wywxjj.controller.fa.FaYfController;
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
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方案统计
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/TJ/FATJ")
public class FaTjController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(FaYfController.class);

    @Autowired
    private FaApi faApi;
    @Autowired
    private XmApi xmApi;
    @Autowired
    private LdApi ldApi;
    @Autowired
    private FhApi fhApi;

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

        return new ModelAndView("tj/fatj/index");
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
     * @param zt        方案状态
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
                                                     @RequestParam(value = "zt", required = false) String zt,
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
            if (StringUtils.isNotBlank(zt)) {
                reqBody.put("zt", zt);
            }

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
     * 获取方案费项明细
     *
     * @param request req
     * @param id      方案id
     * @return res
     */
    @RequestMapping("getFaDetail/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getFaMx(HttpServletRequest request, @PathVariable String id) {
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
     * 打印单据
     * @param request req
     * @param response res
     */
    @RequestMapping("print")
    public void printReceipt(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "startDate", required = false) String startDate,
                             @RequestParam(value = "endDate", required = false) String endDate,
                             @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                             @RequestParam(value = "fk_wyid", required = false) String fk_wyid,
                             @RequestParam(value = "fk_xmxxid", required = false) String fk_xmxxid,
                             @RequestParam(value = "fk_ldxxid", required = false) String fk_ldxxid,
                             @RequestParam(value = "dyh", required = false) String dyh,
                             @RequestParam(value = "zt", required = false) String zt) {
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
            if (StringUtils.isNotBlank(zt)) {
                reqBody.put("zt", zt);
            }

            List<Fa> faList = faApi.getFaList2(request, reqBody, startDate, endDate);

            int hj = 0;
            String totalMoney = "0.00";

            //查询明细
            if(faList != null && !faList.isEmpty()) {
                for (Fa fa : faList) {
                    String id = fa.getId();
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
                }
            }

            //单据模板
            ClassLoader classLoader = getClass().getClassLoader();
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
            if(StringUtils.isNotBlank(zt)) {
                params.put("zt", zt);
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
            freemarkerWord(response, "方案打印.xml", params, "方案统计.pdf");

        } catch (Exception e) {
            log.error("法案统计打印失败", e);
        }
    }



}
