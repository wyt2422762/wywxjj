package com.fdkj.wywxjj.controller.sys;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.Jnsz;
import com.fdkj.wywxjj.api.model.sysMgr.Jxsz;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.api.util.JnszApi;
import com.fdkj.wywxjj.api.util.JxszApi;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import com.fdkj.wywxjj.utils.DateUtils;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缴纳设置
 *
 * @author wyt
 */
@Controller
@RequestMapping("/CZF/JXSZ")
public class JxszController {
    private static final Logger log = LoggerFactory.getLogger(JnszController.class);

    @Autowired
    private JxszApi jxszApi;

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
        request.setAttribute("cuser", jxszApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        return new ModelAndView("sysMgr/jxsz/jxsz_index");
    }

    /**
     * 获取缴纳设置列表(目前理论上只会有1条)
     *
     * @param request req
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList(HttpServletRequest request,
                                                   @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                   @RequestParam(value = "fk_bankid") String fk_bankid,
                                                   @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            reqBody.put("fk_bankid", fk_bankid);

            Page<Jxsz> jxszList = jxszApi.getJxszList(request, reqBody, page, limit);

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取计息设置成功", jxszList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取计息设置失败", e);
            throw new BusinessException("获取计息设置失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取计息设置详情
     *
     * @param request req
     * @return res
     */
    @RequestMapping("getDetail/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getDetail(HttpServletRequest request,
                                                     @PathVariable String id) {
        try {
            Jxsz jxszDetail = jxszApi.getJxszDetail(request, id);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取计息设置详情成功", jxszDetail);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取计息设置详情失败", e);
            throw new BusinessException("获取计息设置详情失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 添加/编辑计息设置
     *
     * @param request req
     * @param jxsz      请求体
     * @return res
     */
    @RequestMapping("aeJxsz")
    @ResponseBody
    public ResponseEntity<CusResponseBody> aeJxsz(HttpServletRequest request,
                                                @RequestBody JSONObject jxsz) {
        try {
            //调用接口添加编辑数据
            jxszApi.aeJxsz(request, jxsz);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("更新计息设置成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("更新计息设置失败", e);
            throw new BusinessException("更新计息设置失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 删除计息设置
     *
     * @param request req
     * @param id  id
     * @return res
     */
    @RequestMapping("delJxsz/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getLogList(HttpServletRequest request,
                                                      @PathVariable("id") String id) {
        try {
            jxszApi.delJxsz(request, id.trim());
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("删除计息设置成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("删除计息设置失败", e);
            throw new BusinessException("删除计息设置失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
