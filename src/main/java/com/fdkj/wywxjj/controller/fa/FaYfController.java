package com.fdkj.wywxjj.controller.fa;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.cells.PdfSaveOptions;
import com.fdkj.wywxjj.api.model.fa.Fa;
import com.fdkj.wywxjj.api.model.fa.yf.Fa_yf;
import com.fdkj.wywxjj.api.model.fa.yf.Fa_yf_ft;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.api.model.zhMgr.Zh_his;
import com.fdkj.wywxjj.api.util.FaApi;
import com.fdkj.wywxjj.api.util.FaYfApi;
import com.fdkj.wywxjj.api.util.FhApi;
import com.fdkj.wywxjj.api.util.ZhApi;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.constant.Constants;
import com.fdkj.wywxjj.controller.BaseController;
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
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * 方案预付
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/FAYF")
public class FaYfController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(FaYfController.class);

    @Autowired
    private FaYfApi faYfApi;
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
     * @param fk_faid 方案id
     * @return res
     * @throws Exception err
     */
    @RequestMapping("Index/{fk_faid}")
    public ModelAndView index(HttpServletRequest request,
                              @RequestParam(value = "opts", required = false) List<String> opts,
                              @PathVariable("fk_faid") String fk_faid) throws Exception {
        request.setAttribute("cuser", faYfApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        Fa faDetail = faApi.getFaDetail(request, fk_faid);
        request.setAttribute("fa", faDetail);

        return new ModelAndView("faMgr/fayf/yf_index");
    }

    /**
     * 跳转
     *
     * @param request req
     * @param opts    操作权限
     * @param fk_faid 方案id
     * @return res
     * @throws Exception err
     */
    @RequestMapping("toAdd/{fk_faid}")
    public ModelAndView toAdd(HttpServletRequest request,
                              @RequestParam(value = "opts", required = false) List<String> opts,
                              @PathVariable("fk_faid") String fk_faid) throws Exception {
        request.setAttribute("cuser", faYfApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        Fa faDetail = faApi.getFaDetail(request, fk_faid);
        request.setAttribute("fa", faDetail);
        return new ModelAndView("faMgr/fayf/yf_add");
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
        request.setAttribute("cuser", faYfApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("id", id);
        return new ModelAndView("faMgr/fayf/yf_info");
    }

    /**
     * 获取预付list(分页)
     *
     * @param request req
     * @param fk_qybm 区域编码
     * @param fk_faid 方案id
     * @param page    第几页
     * @param limit   每页显示的条数
     * @return res
     */
    @RequestMapping("getList/{fk_faid}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList(HttpServletRequest request,
                                                   @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                   @PathVariable("fk_faid") String fk_faid,
                                                   @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            reqBody.put("fk_faid", fk_faid);
            Page<Fa_yf> fayfList = faYfApi.getFayfList(request, reqBody, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取方案列表成功", fayfList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取方案预付列表失败", e);
            throw new BusinessException("获取方案预付列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    //获取预付编号
    @RequestMapping("getNo/{fk_faid}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getNo(HttpServletRequest request,
                                                 @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                 @PathVariable("fk_faid") String fk_faid) {
        try {
            //1. 获取方案预付信息
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            reqBody.put("fk_faid", fk_faid);
            List<Fa_yf> fayfList = faYfApi.getFayfList(request, reqBody);
            //2. 获取方案数据
            Fa faDetail = faApi.getFaDetail(request, fk_faid);
            String no = faDetail.getFabh();
            if (fayfList != null && !fayfList.isEmpty()) {
                //按编号降序排列
                String finalNo = no;
                fayfList.sort((o1, o2) -> {
                    Long aLong = Long.valueOf(o1.getYfkbh().replace(finalNo, ""));
                    Long bLong = Long.valueOf(o2.getYfkbh().replace(finalNo, ""));
                    return bLong.compareTo(aLong);
                });
                String yfkbh = fayfList.get(0).getYfkbh().replace(finalNo, "");
                long l = Long.parseLong(yfkbh) + 1;
                no = faDetail.getFabh() + String.format("%03d", l);
            } else {
                no += "001";
            }

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取方案预付编号成功", no);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取方案预付编号失败", e);
            throw new BusinessException("获取方案预付编号失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 预付分摊
     *
     * @param request req
     * @param fa_yf   预付信息
     * @return res
     */
    @RequestMapping("buildFtData")
    @ResponseBody
    public ResponseEntity<CusResponseBody> buildFtData(HttpServletRequest request,
                                                       @RequestBody Fa_yf fa_yf) {
        try {
            if (fa_yf == null) {
                throw new BusinessException("请求体为空", HttpStatus.BAD_REQUEST.value());
            }
            String fk_faid = fa_yf.getFk_faid();
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
            //预付款金额
            String yfkje = fa_yf.getYfkje();
            if (StringUtils.isBlank(ftfs) || StringUtils.isBlank(fayjje)) {
                throw new BusinessException("请求数据有误", HttpStatus.BAD_REQUEST.value());
            }
            // 计算
            if (Constants.Ftfs.PJFT.equals(ftfs)) {
                List<String> bgs = new ArrayList<>();
                //获取房间个数
                int fhgs = fhList.size();
                //计算每户的金额
                String divide = BigDecimalUtil.divideDown(yfkje, (fhgs + "")).toPlainString();
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
                    String je = BigDecimalUtil.divideDown(BigDecimalUtil.multiply(yfkje, cmj).toPlainString(), zmj).toPlainString();
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
     * 添加方案预付
     *
     * @param request req
     * @param reqBody 请求体
     * @return res
     */
    @RequestMapping("addFayf")
    @ResponseBody
    public ResponseEntity<CusResponseBody> buildFtData(HttpServletRequest request,
                                                       @RequestBody JSONObject reqBody) {
        try {
            //登录用户
            User cuser = faYfApi.getUserFromCookie(request);
            String dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd'T'HH:mm:ss.sss", new Date());

            //方案预付信息
            Fa_yf fa_yf = reqBody.getObject("wywxjJ_FA_YFKmodel", Fa_yf.class);
            //分摊信息
            List<Fh> fhList = reqBody.getJSONArray("wywxjJ_FA_YFK_FTlist").toJavaList(Fh.class);

            //1. 获取方案信息
            String fk_faid = fa_yf.getFk_faid();
            Fa faDetail = faApi.getFaDetail(request, fk_faid);
            //2. 判断方案状态，如果方案已结算，不能添加预付
            if (Constants.Jszt.YJS.equals(faDetail.getJszt())) {
                log.error("方案(" + faDetail.getId() + ")已结算，无法提交预付");
                throw new BusinessException("该方案已结算，无法提交预付", HttpStatus.BAD_REQUEST.value());
            }
            //3. 判断分摊信息
            if (fhList == null || fhList.isEmpty()) {
                throw new BusinessException("分摊信息为空", HttpStatus.BAD_REQUEST.value());
            }
            //4. 构造方案预付信息
            fa_yf.setId(IdUtils.randomUUID()).setAddtime(dateToStr).setFk_yhid(cuser.getId())
                    .setCzr(cuser.getUsername()).setZt(Constants.Zfzt.WZF);
            //5. 修改方案数据(预付状态，预付款)
            String yfk_fa = faDetail.getYfk();
            if (StringUtils.isBlank(yfk_fa)) {
                //方案预付金额为空，直接设置
                faDetail.setYfk(fa_yf.getYfkje());
            } else {
                //预付款计算
                BigDecimal add = BigDecimalUtil.add(yfk_fa.trim(), fa_yf.getYfkje().trim());
                faDetail.setYfk(add.toPlainString());
            }
            faDetail.setYfzt(Constants.Yfzt.YYF);
            //6. 构造方案预付分摊信息
            List<Fa_yf_ft> ftList = new ArrayList<>();
            List<Zh> zhList = new ArrayList<>();
            List<Zh_his> zh_hisList = new ArrayList<>();
            for (Fh fh : fhList) {
                Fa_yf_ft fa_yf_ft = new Fa_yf_ft();
                fa_yf_ft.setId(IdUtils.randomUUID()).setAddtime(dateToStr)
                        .setFk_qybm(fa_yf.getFk_qybm()).setFk_xtglid(fa_yf.getFk_xtglid())
                        .setFk_yfkid(fa_yf.getId()).setFk_faid(faDetail.getId())
                        .setFk_fhid(fh.getId()).setFk_zhid(fh.getZh().getId())
                        .setFtje(fh.getFtje());
                ftList.add(fa_yf_ft);

                //处理账户信息(计算钱，历史信息)
                String fk_zhid = fh.getZh().getId();
                Zh zhDetail = zhApi.getZhDetail(request, fk_zhid);
                String money = zhDetail.getMoney();
                //计算余额
                BigDecimal subtract = BigDecimalUtil.subtract(money, fa_yf_ft.getFtje());
                zhDetail.setMoney(subtract.toPlainString());

                zhList.add(zhDetail);

                //账户历史信息
                Zh_his zh_his = new Zh_his();
                zh_his.setFk_zhid(zhDetail.getId()).setAddtime(dateToStr).setJzrq(dateToStr)
                        .setCzlx(Constants.JzLb.WXYF)
                        .setZc(fa_yf_ft.getFtje()).setZhye(zhDetail.getMoney());

                zh_hisList.add(zh_his);
            }

            //7. 构造请求接口数据
            JSONObject json = new JSONObject();
            //预付款信息
            json.put("wywxjJ_FA_YFKmodel", JSONObject.parseObject(JSONObject.toJSONString(fa_yf)));
            //方案信息
            json.put("wywxjJ_FAmodel", JSONObject.parseObject(JSONObject.toJSONString(faDetail)));
            //预付款分摊信息
            json.put("wywxjJ_FA_YFK_FTlist", JSONArray.parseArray(JSONArray.toJSONString(ftList)));
            //账户信息
            json.put("wywxjJ_ZHmodel", JSONArray.parseArray(JSONArray.toJSONString(zhList)));
            //账户分摊信息
            json.put("wywxjJ_ZH_HISmodel", JSONArray.parseArray(JSONArray.toJSONString(zh_hisList)));
            //请求接口
            faYfApi.addFa_yf(request, json);

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("添加方案预付成功", fhList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("添加方案预付失败", e);
            if (e instanceof BusinessException) {
                throw (BusinessException) e;
            } else {
                throw new BusinessException("添加方案预付失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
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
            User cuser = faYfApi.getUserFromCookie(request);
            //预付详情
            Fa_yf fayfDetail = faYfApi.getFayfDetail(request, id);
            //分摊列表
            List<Fa_yf_ft> ftList = fayfDetail.getFtList();
            List<Fh> fhList = new ArrayList<>();
            for (Fa_yf_ft fa_yf_ft : ftList) {
                String fk_fhid = fa_yf_ft.getFk_fhid();
                String fk_zhid = fa_yf_ft.getFk_zhid();

                Fh fhDetail = fhApi.getFhDetail(request, fk_fhid);
                fhDetail.setFtje(fa_yf_ft.getFtje());

                Zh zhDetail = zhApi.getZhDetail(request, fk_zhid);
                fhDetail.setZh(zhDetail);

                fhList.add(fhDetail);
            }
            fayfDetail.setFtList2(fhList);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取方案预付详情成功", fayfDetail);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取方案预付详情失败", e);
            if (e instanceof BusinessException) {
                throw (BusinessException) e;
            } else {
                throw new BusinessException("获取方案预付详情失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            }
        }
    }

    /**
     * 删除方案预付
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
            User cuser = faYfApi.getUserFromCookie(request);
            //时间
            String dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd'T'HH:mm:ss.sss", new Date());
            //预付详情
            Fa_yf fayfDetail = faYfApi.getFayfDetail(request, id);
            //判断预付状态
            if(Constants.Zfzt.YZF.equals(fayfDetail.getZt())) {
                throw new BusinessException("该预付已支付，无法删除", HttpStatus.BAD_REQUEST.value());
            }
            //分摊列表
            List<Fa_yf_ft> ftList = fayfDetail.getFtList();
            // 获取方案信息
            String fk_faid = fayfDetail.getFk_faid();
            Fa faDetail = faApi.getFaDetail(request, fk_faid);
            //1. 判断是否要修改方案预付状态
            //获取方案的预付列表
            Map<String, String> reqBody = new HashMap<>();
            reqBody.put("fk_faid", fk_faid);
            List<Fa_yf> fayfList = faYfApi.getFayfList(request, reqBody);
            if (fayfList == null || fayfList.size() <= 1) {
                faDetail.setYfzt(Constants.Yfzt.WYF);
            }
            //2. 计算方案的预付款
            BigDecimal subtract = BigDecimalUtil.subtract(faDetail.getYfk().trim(), fayfDetail.getYfkje().trim());
            if (BigDecimalUtil.compareTo(subtract, new BigDecimal(0)) <= 0) {
                faDetail.setYfk("");
            } else {
                faDetail.setYfk(subtract.toPlainString());
            }
            //3. 算钱(账户余额，历史)
            List<Zh> zhList = new ArrayList<>();
            List<Zh_his> zh_hisList = new ArrayList<>();
            for (Fa_yf_ft fa_yf_ft : ftList) {
                //处理账户信息(计算钱，历史信息)
                String fk_zhid = fa_yf_ft.getFk_zhid();
                Zh zhDetail = zhApi.getZhDetail(request, fk_zhid);
                String money = zhDetail.getMoney();
                //计算余额
                BigDecimal add = BigDecimalUtil.add(money, fa_yf_ft.getFtje());
                zhDetail.setMoney(add.toPlainString());

                zhList.add(zhDetail);

                //账户历史信息
                Zh_his zh_his = new Zh_his();
                zh_his.setFk_zhid(zhDetail.getId()).setAddtime(dateToStr).setJzrq(dateToStr)
                        .setCzlx(Constants.JzLb.YFFK)
                        .setSr(fa_yf_ft.getFtje()).setZhye(zhDetail.getMoney());

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
            faYfApi.delFa_yf(request, json);

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("删除方案预付成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("删除方案预付失败", e);
            if (e instanceof BusinessException) {
                throw (BusinessException) e;
            } else {
                throw new BusinessException("删除方案预付失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            }
        }
    }

    /**
     * 打印单据
     * @param request req
     * @param response res
     * @param id 预付id
     */
    @RequestMapping("printReceipt/{id}")
    public void printReceipt(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable("id") String id) {
        try {
            //单据模板
            ClassLoader classLoader = getClass().getClassLoader();
            URL url = classLoader.getResource("receipt/template/预付单据.xlsx");
            String path = URLDecoder.decode(url.getPath(),"utf-8");
            //模板参数
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);

            //打印
            PdfSaveOptions pdfSaveOptions = new PdfSaveOptions();
            pdfSaveOptions.setOnePagePerSheet(true);
            printExcel2pdf(response, path, params, "预付测试单据.pdf", pdfSaveOptions);
        } catch (Exception e) {
            log.error("生成预付测试单据失败", e);
        }
    }
}
