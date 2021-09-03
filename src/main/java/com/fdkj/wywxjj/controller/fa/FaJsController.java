package com.fdkj.wywxjj.controller.fa;

import com.fdkj.wywxjj.api.model.fa.js.Fa_js;
import com.fdkj.wywxjj.api.model.fa.yf.Fa_yf;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Api api;

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
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        return new ModelAndView("faMgr/fajs/js_index");
    }

    //判断有没有结算单

    /**
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
            Page<Fa_js> fajsList = api.getFajsList(request, reqBody, 1, 1);

            boolean res = false;
            //如果有，则结果改为true
            if(fajsList.getTotalRecord() > 0) {
                res = true;
            }
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("判断是否有结算信息成功", res);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("判断是否有结算信息失败", e);
            throw new BusinessException("判断是否有结算信息失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
