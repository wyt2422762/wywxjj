package com.fdkj.wywxjj.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.Role;
import com.fdkj.wywxjj.api.model.sysMgr.View_PT_XT_MK_Model;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理
 *
 * @author wyt
 */
@Controller
@RequestMapping("PTXT/JSGL")
public class RoleMgrController {

    private static final Logger log = LoggerFactory.getLogger(RoleMgrController.class);

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
        request.setAttribute("user", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        return new ModelAndView("sysMgr/roleMgr/roleMgr_index");
    }

    /**
     * 获取角色管理列表
     *
     * @param request  req
     * @param roleName 角色名称
     * @param page     第几页
     * @param limit    每页显示多少条
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getLogList(HttpServletRequest request,
                                                      @RequestParam(value = "roleName", required = false) String roleName,
                                                      @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Page<Role> rolePage = api.getRoleList(request, roleName, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取角色列表成功", rolePage);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取角色列表失败", e);
            throw new BusinessException("获取角色列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 跳转到修改角色页面
     *
     * @param request  req
     * @param roleId   角色id
     * @param roleName 角色名称
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toEdit")
    public ModelAndView toEdit(HttpServletRequest request, @RequestParam("roleId") String roleId, @RequestParam("roleName") String roleName) throws Exception {
        request.setAttribute("user", api.getUserFromCookie(request));
        request.setAttribute("roleId", roleId);
        request.setAttribute("roleName", roleName);
        //获取角色的模块权限
        List<View_PT_XT_MK_Model> roleModel = api.getRoleModel(request, roleId);
        buildPT_XT_MK_Models(request, roleModel);
        return new ModelAndView("sysMgr/roleMgr/roleMgr_edit");
    }

    private void buildPT_XT_MK_Models(HttpServletRequest request, List<View_PT_XT_MK_Model> roleModel) {
        List<View_PT_XT_MK_Model> res = new ArrayList<>();
        for (View_PT_XT_MK_Model xtMkModel : roleModel) {
            res.add(xtMkModel);
            List<View_PT_XT_MK_Model> childList = xtMkModel.getChildList();
            if (childList != null) {
                res.addAll(childList);
            }
        }
        request.setAttribute("roleModel", res);
    }

    /**
     * 跳转到添加角色页面
     *
     * @param request req
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toAdd")
    public ModelAndView toAdd(HttpServletRequest request) throws Exception {
        request.setAttribute("user", api.getUserFromCookie(request));
        //获取角色的模块权限
        List<View_PT_XT_MK_Model> roleModel = api.getAllModels(request);
        buildPT_XT_MK_Models(request, roleModel);
        return new ModelAndView("sysMgr/roleMgr/roleMgr_add");
    }

    /**
     * 添加/编辑角色
     *
     * @param request req
     * @param json    请求体
     * @return res
     */
    @RequestMapping("aeRole")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getLogList(HttpServletRequest request,
                                                      @RequestBody JSONObject json) {
        try {
            api.aeRole(request, json);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("更新角色成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("更新角色失败", e);
            throw new BusinessException("更新角色失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 删除角色
     *
     * @param request req
     * @param roleId  角色Id
     * @return res
     */
    @RequestMapping("delRole")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getLogList(HttpServletRequest request,
                                                      @RequestParam("roleId") String roleId) {
        try {
            if (StringUtils.isBlank(roleId)) {
                throw new BusinessException("角色id不能为空", HttpStatus.BAD_REQUEST.value());
            }
            api.delRole(request, roleId.trim());
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("删除角色成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("删除角色失败", e);
            throw new BusinessException("删除角色失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
