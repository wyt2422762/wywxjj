package com.fdkj.wywxjj.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.Yh;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行管理
 *
 * @author wyt
 */
@Controller
@RequestMapping("/CZF/YHGL")
public class YhMgrController {

    private static final Logger log = LoggerFactory.getLogger(YhMgrController.class);

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
        return new ModelAndView("sysMgr/yhMgr/yhMgr_index");
    }

    /**
     * 获取银行列表
     *
     * @param request req
     * @param yxmc    银行名称
     * @param page    第几页
     * @param limit   每页显示多少条
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getWyGsList(HttpServletRequest request,
                                                       @RequestParam(value = "yxmc", required = false) String yxmc,
                                                       @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                       @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(yxmc)) {
                reqBody.put("yxmc", yxmc);
            }
            Page<Yh> yhPage = api.getYhList(request, reqBody, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取银行列表成功", yhPage);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取银行列表失败", e);
            throw new BusinessException("获取银行列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取银行详情
     *
     * @param request req
     * @param id      银行id
     * @return res
     */
    @RequestMapping("getDetail/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getDetail(HttpServletRequest request,
                                                     @PathVariable("id") String id) {
        try {
            Yh yh = api.getYhDetail(request, id);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取银行详情成功", yh);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取银行详情失败", e);
            throw new BusinessException("获取银行详情失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    @RequestMapping("toAdd")
    public ModelAndView toAdd(HttpServletRequest request) throws Exception {
        //1. 当前登陆用户
        request.setAttribute("cuser", api.getUserFromCookie(request));
        return new ModelAndView("sysMgr/yhMgr/yhMgr_add");
    }

    @RequestMapping("toEdit/{id}")
    public ModelAndView toEdit(HttpServletRequest request,
                               @PathVariable("id") String id) throws Exception {
        //1. 当前登陆用户
        request.setAttribute("cuser", api.getUserFromCookie(request));
        //2. 对应的银行信息
        Yh yh = api.getYhDetail(request, id);
        request.setAttribute("yh", yh);
        return new ModelAndView("sysMgr/yhMgr/yhMgr_edit");
    }

    /**
     * 添加/编辑银行
     *
     * @param request req
     * @param json    请求体
     * @return res
     */
    @RequestMapping("aeYh")
    @ResponseBody
    public ResponseEntity<CusResponseBody> aeYh(HttpServletRequest request,
                                                @RequestBody JSONObject json) {
        try {
            api.aeYh(request, json);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("更新银行成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("更新银行失败", e);
            throw new BusinessException("更新银行失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 删除银行
     *
     * @param request req
     * @param id      银行Id
     * @return res
     */
    @RequestMapping("delYh/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getLogList(HttpServletRequest request,
                                                      @PathVariable("id") String id) {
        try {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException("银行id不能为空", HttpStatus.BAD_REQUEST.value());
            }
            api.delYh(request, id.trim());
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("删除银行成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("删除银行失败", e);
            throw new BusinessException("删除银行失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

}