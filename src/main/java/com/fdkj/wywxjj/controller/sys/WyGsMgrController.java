package com.fdkj.wywxjj.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.WyGs;
import com.fdkj.wywxjj.api.util.WygsApi;
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
 * 物业公司管理
 *
 * @author wyt
 */
@Controller
@RequestMapping("/PTXT/WYGSGL")
public class WyGsMgrController {
    private static final Logger log = LoggerFactory.getLogger(WyGsMgrController.class);

    @Autowired
    private WygsApi wygsApi;

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
        request.setAttribute("cuser", wygsApi.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        return new ModelAndView("sysMgr/wyGsMgr/wyGsMgr_index");
    }

    /**
     * 获取物业公司列表
     *
     * @param request req
     * @param qymc    角色名称
     * @param page    第几页
     * @param limit   每页显示多少条
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getWyGsList(HttpServletRequest request,
                                                       @RequestParam(value = "qymc", required = false) String qymc,
                                                       @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                       @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if(StringUtils.isNotBlank(qymc)){
                reqBody.put("qymc", qymc);
            }
            if(StringUtils.isNotBlank(fk_qybm)){
                reqBody.put("fk_qybm", fk_qybm);
            }

            Page<WyGs> wyGsPage = wygsApi.getWyGsList(request, reqBody, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取物业公司列表成功", wyGsPage);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取物业公司列表失败", e);
            throw new BusinessException("获取物业公司列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取物业详情
     *
     * @param request req
     * @param id      物业公司id
     * @return res
     */
    @RequestMapping("getDetail/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getDetail(HttpServletRequest request,
                                                     @PathVariable("id") String id) {
        try {
            WyGs wyGs = wygsApi.getWyGsDetail(request, id);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取物业公司详情成功", wyGs);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取物业公司详情失败", e);
            throw new BusinessException("获取物业公司详情失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    @RequestMapping("toAdd")
    public ModelAndView toAdd(HttpServletRequest request) throws Exception {
        //1. 当前登陆用户
        request.setAttribute("cuser", wygsApi.getUserFromCookie(request));
        return new ModelAndView("sysMgr/wyGsMgr/wyGsMgr_add");
    }

    @RequestMapping("toEdit/{id}")
    public ModelAndView toEdit(HttpServletRequest request,
                               @PathVariable("id") String id) throws Exception {
        //1. 当前登陆用户
        request.setAttribute("cuser", wygsApi.getUserFromCookie(request));
        //2. 对应的物业公司信息
        WyGs wyGs = wygsApi.getWyGsDetail(request, id);
        request.setAttribute("wyGs", wyGs);
        return new ModelAndView("sysMgr/wyGsMgr/wyGsMgr_edit");
    }

    /**
     * 添加/编辑物业公司
     *
     * @param request req
     * @param json    请求体
     * @return res
     */
    @RequestMapping("aeWyGs")
    @ResponseBody
    public ResponseEntity<CusResponseBody> aeWyGs(HttpServletRequest request,
                                                  @RequestBody JSONObject json) {
        try {
            wygsApi.aeWyGs(request, json);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("更新物业公司成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("更新物业公司失败", e);
            throw new BusinessException("更新物业公司失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 删除角色
     *
     * @param request req
     * @param id      物业公司Id
     * @return res
     */
    @RequestMapping("delWyGs/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getLogList(HttpServletRequest request,
                                                      @PathVariable("id") String id) {
        try {
            if (StringUtils.isBlank(id)) {
                throw new BusinessException("物业公司id不能为空", HttpStatus.BAD_REQUEST.value());
            }
            wygsApi.delWyGs(request, id.trim());
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("删除物业公司成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("删除物业公司失败", e);
            throw new BusinessException("删除物业公司失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
