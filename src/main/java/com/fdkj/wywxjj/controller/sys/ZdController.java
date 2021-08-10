package com.fdkj.wywxjj.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.Yh;
import com.fdkj.wywxjj.api.model.sysMgr.Zd;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.controller.xm.XmController;
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
import java.util.List;

/**
 * 字典管理
 *
 * @author wyt
 */
@Controller
@RequestMapping("/CZF/ZDGL")
public class ZdController {
    private static final Logger log = LoggerFactory.getLogger(XmController.class);

    @Autowired
    private Api api;

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @return res
     * @throws Exception err
     */
    @RequestMapping("Index")
    public ModelAndView index(HttpServletRequest request, @RequestParam(value = "opts", required = false) List<String> opts) throws Exception {
        request.setAttribute("user", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        return new ModelAndView("sysMgr/zdMgr/zdMgr_index");
    }

    /**
     * 获取银行列表
     *
     * @param request req
     * @param zdm    字典名称
     * @param page    第几页
     * @param limit   每页显示多少条
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList(HttpServletRequest request,
                                                       @RequestParam(value = "zdm", required = false) String zdm,
                                                       @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Page<Zd> yhPage = api.getZdList(request, zdm, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取字典列表成功", yhPage);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取字典列表失败", e);
            throw new BusinessException("获取字典列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    @RequestMapping("toAdd")
    public ModelAndView toAdd(HttpServletRequest request) throws Exception {
        //1. 当前登陆用户
        request.setAttribute("user", api.getUserFromCookie(request));
        return new ModelAndView("sysMgr/zdMgr/zdMgr_add");
    }

    @RequestMapping("toEdit/{id}")
    public ModelAndView toEdit(HttpServletRequest request,
                               @PathVariable("id") String id) throws Exception {
        //1. 当前登陆用户
        request.setAttribute("user", api.getUserFromCookie(request));
        //2. 对应的银行信息
        Zd zd = api.getZdDetail(request, id);
        request.setAttribute("zd", zd);
        return new ModelAndView("sysMgr/zdMgr/zdMgr_edit");
    }

    /**
     * 添加/编辑字典
     *
     * @param request req
     * @param json    请求体
     * @return res
     */
    @RequestMapping("aeZd")
    @ResponseBody
    public ResponseEntity<CusResponseBody> aeZd(HttpServletRequest request,
                                                @RequestBody JSONObject json) {
        try {
            api.aeZd(request, json);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("更新字典成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("更新字典失败", e);
            throw new BusinessException("更新字典失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
