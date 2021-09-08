package com.fdkj.wywxjj.controller.zh;

import com.fdkj.wywxjj.api.model.sysMgr.Jnsz;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.sysMgr.Yh;
import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import com.fdkj.wywxjj.api.model.xmMgr.Ld;
import com.fdkj.wywxjj.api.model.xmMgr.Xm;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.api.util.*;
import com.fdkj.wywxjj.controller.BaseController;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.utils.DateUtils;
import com.fdkj.wywxjj.utils.text.Convert;
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
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账户开户
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/ZHKH")
public class ZhKhController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ZhKhController.class);

    @Autowired
    private ZhApi zhApi;
    @Autowired
    private XmApi xmApi;
    @Autowired
    private LdApi ldApi;
    @Autowired
    private FhApi fhApi;
    @Autowired
    private JnszApi jnszApi;
    @Autowired
    private YhApi yhApi;

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
        request.setAttribute("cuser", zhApi.getUserFromCookie(request));
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
        request.setAttribute("cuser", zhApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        Xm xm = xmApi.getXmDetail(request, fk_xmxxid);
        request.setAttribute("xm", xm);
        return new ModelAndView("zhMgr/kh/zhkh_kh");
    }

    /**
     * 跳转开户
     *
     * @param request   req
     * @param opts      opts
     * @param fk_fhxxid 房号信息id
     * @return res
     * @throws Exception err
     */
    @RequestMapping("toActKh/{fk_fhxxid}")
    public ModelAndView toActKh(HttpServletRequest request,
                                @RequestParam(value = "opts", required = false) List<String> opts,
                                @PathVariable("fk_fhxxid") String fk_fhxxid) throws Exception {
        User cuser = zhApi.getUserFromCookie(request);
        request.setAttribute("cuser", cuser);
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }

        //获取房号信息
        Fh fh = fhApi.getFhDetail(request, fk_fhxxid);
        if (fh == null) {
            throw new BusinessException("房间信息为空", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        request.setAttribute("fh", fh);
        //获取缴纳设置
        Map<String, String> reqBody = new HashMap<>();
        //reqBody.put("fk_qybm", cuser.getFk_qybm());
        Jnsz jnsz = jnszApi.getJnszList(request, reqBody, 1, 1);
        if (jnsz == null) {
            throw new BusinessException("缴纳设置信息为空", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        jnsz = jnszApi.getJnszDetail(request, jnsz.getId());
        request.setAttribute("jnsz", jnsz);
        //获取银行信息
        Yh yh = yhApi.getYhDetail(request, cuser.getFk_id());
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
        if ("不固定".equals(jnbz)) {
            res.put("bgd", true);
        }
        //5. 获取缴纳比例/金额
        if ("按合同总价缴纳".equals(jnfs)) {
            res.put("jnbz", jnsz.getJnbl());
        } else if ("按建筑面积缴纳".equals(jnfs)) {
            res.put("jnbz", jnsz.getJnBz_jzmj(jzmj, lc));
        }

        //6. 计算钱
        if ("按合同总价缴纳".equals(jnfs)) {
            res.put("money", jnsz.calacMoneyByHtje(htje));
        } else if ("按建筑面积缴纳".equals(jnfs)) {
            res.put("money", jnsz.calacMoneyByJzmj(jzmj, lc));
        }

        return res;
    }

    /**
     * 打印单据
     * @param request req
     * @param response res
     * @param id 账户id
     */
    @RequestMapping("printReceipt/{id}")
    public void printReceipt(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable("id") String id) {
        try {
            //单据模板
            ClassLoader classLoader = getClass().getClassLoader();
            URL url = classLoader.getResource("receipt/template/维修基金收据.xlsx");
            String path = URLDecoder.decode(url.getPath(),"utf-8");

            //获取账户信息
            Zh zhDetail = zhApi.getZhDetail(request, id);
            String fk_fhxxid = zhDetail.getFk_fhxxid();
            //房号信息
            Fh fhDetail = fhApi.getFhDetail(request, fk_fhxxid);
            String fk_ldxxid = fhDetail.getFk_ldxxid();
            //楼栋信息
            Ld ldDetail = ldApi.getLdDetail(request, fk_ldxxid);
            String fk_xmxxid = ldDetail.getFk_xmxxid();
            //项目信息
            Xm xmDetail = xmApi.getXmDetail(request, fk_xmxxid);

            //模板参数
            Map<String, Object> params = new HashMap<>();
            params.put("tfrq", DateUtils.parseDateToStr("yyyy年MM月dd日", new Date()));
            params.put("fkdw", zhDetail.getYzmc());
            params.put("zzzl", xmDetail.getXmmc() + ldDetail.getCh() + "幢" + fhDetail.getSzdy() + "单元" + fhDetail.getSzlc() + "层" + fhDetail.getFh());
            params.put("htbh", fhDetail.getHtbah());

            //合同金额
            String htje = fhDetail.getHtje();
            //合同金额大写
            String htje_dx = Convert.digitUppercase(new Double(htje));
            DecimalFormat decimalFormat = new DecimalFormat("###,###.00");
            //合同金额(格式化)
            String format = decimalFormat.format(new Double(htje));

            params.put("htgfk", "￥" + format + "  大写：" + htje_dx);
            params.put("kh", "公共维修基金");
            params.put("wxjjbl", zhDetail.getJzbl());
            params.put("wxjjje", zhDetail.getCjje());
            params.put("wxjjje_dx", StringUtils.isNotBlank(zhDetail.getCjje()) ? Convert.digitUppercase(new Double(zhDetail.getCjje())) : null);

            //打印
            downLoadReceipt(response, path, params, "维修基金收据.pdf");
        } catch (Exception e) {
            log.error("生成维修基金收据失败", e);
        }
    }
}
