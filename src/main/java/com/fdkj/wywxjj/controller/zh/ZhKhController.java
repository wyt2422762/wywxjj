package com.fdkj.wywxjj.controller.zh;

import com.fdkj.wywxjj.api.model.sysMgr.Jnsz;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.sysMgr.Yh;
import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import com.fdkj.wywxjj.api.model.xmMgr.Xm;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.utils.math.BigDecimalUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账户
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/ZHKH")
public class ZhKhController {

    private static final Logger log = LoggerFactory.getLogger(ZhKhController.class);

    @Autowired
    private Api api;

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
        return new ModelAndView("zhMgr/kh/zhkh_index");
    }

    /**
     * 跳转到
     *
     * @param request   req
     * @param opts      操作权限信息
     * @param fk_xmxxid 项目(小区id)
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toKh/{fk_xmxxid}")
    public ModelAndView toKh(HttpServletRequest request,
                             @RequestParam(value = "opts", required = false) List<String> opts,
                             @PathVariable("fk_xmxxid") String fk_xmxxid) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        Xm xm = api.getXmDetail(request, fk_xmxxid);
        request.setAttribute("xm", xm);
        return new ModelAndView("zhMgr/kh/zhkh_kh");
    }

    /**
     * 跳转开户
     *
     * @param request req
     * @param opts  opts
     * @param fk_fhxxid 房号信息id
     * @return res
     * @throws Exception err
     */
    @RequestMapping("toActKh/{fk_fhxxid}")
    public ModelAndView toActKh(HttpServletRequest request,
                                @RequestParam(value = "opts", required = false) List<String> opts,
                                @PathVariable("fk_fhxxid") String fk_fhxxid) throws Exception {
        User cuser = api.getUserFromCookie(request);
        request.setAttribute("cuser", cuser);
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }

        //获取房号信息
        Fh fh = api.getFhDetail(request, fk_fhxxid);
        if (fh == null) {
            throw new BusinessException("房间信息为空", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        request.setAttribute("fh", fh);
        //获取缴纳设置
        Map<String, String> reqBody = new HashMap<>();
        //reqBody.put("fk_qybm", cuser.getFk_qybm());
        Jnsz jnsz = api.getJnszList(request, reqBody, 1, 1);
        if (jnsz == null) {
            throw new BusinessException("缴纳设置信息为空", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        jnsz = api.getJnszDetail(request, jnsz.getId());
        request.setAttribute("jnsz", jnsz);
        //获取银行信息
        Yh yh = api.getYhDetail(request, cuser.getFk_id());
        if (yh == null) {
            throw new BusinessException("银行信息为空", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        request.setAttribute("yh", yh);

        Map<String, Object> map = clacKhMoney(jnsz, fh);

        request.setAttribute("money", map.getOrDefault("money", null));
        request.setAttribute("bgd", map.getOrDefault("bgd", false));
        request.setAttribute("jnbz", map.getOrDefault("jnbz", null));

        return new ModelAndView("zhMgr/kh/zhkh_actkh");
    }

    /**
     * 计算缴纳设置相关
     *
     * @param jnsz 缴纳设置
     * @param fh   房间信息
     * @return res
     */
    public Map<String, Object> clacKhMoney(Jnsz jnsz, Fh fh) {
        Map<String, Object> res = new HashMap<>();

        //1. 获取房间建筑面积
        String jzmj = fh.getScmj_jzmj();
        if (StringUtils.isBlank(jzmj)) {
            throw new BusinessException("建筑面积为空", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        //2. 获取房间合同金额
        String htje = fh.getHtje();
        if (StringUtils.isBlank(htje)) {
            throw new BusinessException("合同金额", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        //3. 获取房间的楼层
        String lc = fh.getSzlc();
        if (StringUtils.isBlank(jzmj)) {
            throw new BusinessException("楼层为空", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        //4. 获取缴纳设置信息
        String jnfs = jnsz.getJnfs();
        String jnbz = jnsz.getJnbz();
        if("不固定".equals(jnbz)) {
            res.put("bgd", true);
        }
        //5. 获取缴纳比例/金额
        if("按合同总价缴纳".equals(jnfs)){
            res.put("jnbz", jnsz.getJnbl());
        } else if("按建筑面积缴纳".equals(jnfs)){
            res.put("jnbz", jnsz.getJnBz_jzmj(jzmj, lc));
        }

        //6. 计算钱
        if("按合同总价缴纳".equals(jnfs)){
            res.put("money", jnsz.calacMoneyByHtje(htje));
        } else if("按建筑面积缴纳".equals(jnfs)){
            res.put("money", jnsz.calacMoneyByJzmj(jzmj, lc));
        }

        return res;
    }

    public static void main(String[] args) {
        String ht = "1430000.47";
        String bl = "10.74";
        BigDecimal ht_bd = new BigDecimal(ht.trim());
        BigDecimal bl_bd = new BigDecimal(bl.trim());
        BigDecimal multiply = ht_bd.multiply(bl_bd);
        System.out.println(multiply);
        BigDecimal scale = multiply.setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println(scale);

        BigDecimal bigDecimal = BigDecimalUtil.multiplyAndScale(ht, bl);
        System.out.println(bigDecimal);

    }

}
