package com.fdkj.wywxjj.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.Jnsz;
import com.fdkj.wywxjj.api.util.JnszApi;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.error.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缴纳设置
 *
 * @author wyt
 */
@Controller
@RequestMapping("/CZF/JNSZ")
public class JnszController {
    private static final Logger log = LoggerFactory.getLogger(JnszController.class);

    @Autowired
    private JnszApi jnszApi;

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
        request.setAttribute("cuser", jnszApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        return new ModelAndView("sysMgr/jnsz/jnsz_index");
    }

    /**
     * 获取缴纳设置列表(目前理论上只会有1条)
     *
     * @param request req
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getWyGsList(HttpServletRequest request,
                                                       @RequestParam(value = "fk_qybm", required = false) String fk_qybm) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            Jnsz jnsz = jnszApi.getJnszList(request, reqBody, 1, 1);
            jnsz = jnszApi.getJnszDetail(request, jnsz.getId());

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取缴纳设置成功", jnsz);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取缴纳设置失败", e);
            throw new BusinessException("获取缴纳设置失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 添加/编辑缴纳设置
     *
     * @param request req
     * @param jnsz    请求体
     * @return res
     */
    @RequestMapping("aeJnsz")
    @ResponseBody
    public ResponseEntity<CusResponseBody> aeJnsz(HttpServletRequest request,
                                                  @RequestBody JSONObject jnsz) {
        try {
            jnszApi.aeJnsz(request, jnsz);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("更新缴纳设置成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("更新缴纳设置失败", e);
            throw new BusinessException("更新缴纳设置失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

}
