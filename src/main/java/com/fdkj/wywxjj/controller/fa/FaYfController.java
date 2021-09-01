package com.fdkj.wywxjj.controller.fa;

import com.fdkj.wywxjj.api.model.fa.Fa;
import com.fdkj.wywxjj.api.model.fa.Fa_fh;
import com.fdkj.wywxjj.api.model.fa.yf.Fa_yf;
import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.constant.Constants;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import com.fdkj.wywxjj.utils.math.BigDecimalUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方案预付
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/FAYF")
public class FaYfController {
    private static final Logger log = LoggerFactory.getLogger(FaYfController.class);

    @Autowired
    private Api api;

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
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("fk_faid", fk_faid);
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
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        Fa faDetail = api.getFaDetail(request, fk_faid);
        request.setAttribute("fa", faDetail);
        return new ModelAndView("faMgr/fayf/yf_add");
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
            Page<Fa_yf> fayfList = api.getFayfList(request, reqBody, page, limit);
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
            List<Fa_yf> fayfList = api.getFayfList(request, reqBody);
            //2. 获取方案数据
            Fa faDetail = api.getFaDetail(request, fk_faid);
            String no = faDetail.getFabh();
            if (fayfList != null && !fayfList.isEmpty()) {
                //按编号降序排列
                fayfList.sort((o1, o2) -> {
                    Long aLong = Long.valueOf(o1.getYfkbh());
                    Long bLong = Long.valueOf(o2.getYfkbh());
                    return bLong.compareTo(aLong);
                });
                String yfkbh = fayfList.get(0).getYfkbh();
                long l = Long.parseLong(yfkbh) + 1;
                no = l + "";
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

    //判断金额是否允许

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
            Fa faDetail = api.getFaDetail(request, fk_faid);
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
            List<Fh> fhList = api.getFhAllList(request, reqBody);
            if (fhList == null || fhList.isEmpty()) {
                if (StringUtils.isBlank(fk_ldxxid)) {
                    throw new BusinessException("请求数据有误", HttpStatus.BAD_REQUEST.value());
                }
            }
            //4. 计算分摊金额
            String ftfs = faDetail.getFtfs();
            String fayjje = faDetail.getFayjje();
            if (StringUtils.isBlank(ftfs) || StringUtils.isBlank(fayjje)) {
                throw new BusinessException("请求数据有误", HttpStatus.BAD_REQUEST.value());
            }
            // 计算
            if (Constants.Ftfs.PJFT.equals(ftfs)) {
                List<String> bgs = new ArrayList<>();
                //获取房间个数
                int fhgs = fhList.size();
                //计算每户的金额
                String divide = BigDecimalUtil.divideDown(fayjje, (fhgs + "")).toPlainString();
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
                    String je = BigDecimalUtil.divideHalfUp(BigDecimalUtil.multiply(fayjje, cmj).toPlainString(), zmj).toPlainString();
                    fh.setFtje(je);
                }
                CusResponseBody cusResponseBody = CusResponseBody.success("获取分摊信息成功", fhList);
                return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
            } else {
                throw new BusinessException("获取方案预付编号失败: 分摊方式错误", HttpStatus.BAD_REQUEST.value());
            }
        } catch (Exception e) {
            log.error("获取方案预付编号失败", e);
            throw new BusinessException("获取方案预付编号失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
