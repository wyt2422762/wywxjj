package com.fdkj.wywxjj.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.Role;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.sysMgr.WyGs;
import com.fdkj.wywxjj.api.model.sysMgr.Yh;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
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
 * 用户管理
 *
 * @author wyt
 */
@Controller
@RequestMapping("GYFW/YHGL")
public class UserMgrController {

    private static final Logger log = LoggerFactory.getLogger(UserMgrController.class);

    @Autowired
    private Api api;

    @RequestMapping("Index")
    public ModelAndView index(HttpServletRequest request, @RequestParam(value = "opts", required = false) List<String> opts) throws Exception {
        request.setAttribute("user", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        return new ModelAndView("sysMgr/userMgr/userMgr_index");
    }

    /**
     * 获取用户管理列表
     *
     * @param request  req
     * @param username username
     * @param page     第几页
     * @param limit    每页显示多少条
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getUserMgrList(HttpServletRequest request,
                                                          @RequestParam(value = "userId", required = false) String userId,
                                                          @RequestParam(value = "username", required = false) String username,
                                                          @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Page<User> userPage = api.getUserList(request, userId, username, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取用户列表成功", userPage);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取用户列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @RequestMapping("toAdd")
    public ModelAndView toAdd(HttpServletRequest request) throws Exception {
        //1. 获取角色信息
        List<Role> allRoleList = api.getAllRoleList(request);
        request.setAttribute("roleList", allRoleList);
        //2. 获取银行信息
        List<Yh> yhAllList = api.getYhAllList(request);
        request.setAttribute("yhList", yhAllList);
        //3. 获取物业公司信息
        List<WyGs> wyGsAllList = api.getWyGsAllList(request);
        request.setAttribute("wyGsList", wyGsAllList);
        //4. 当前登陆用户
        request.setAttribute("user", api.getUserFromCookie(request));
        return new ModelAndView("sysMgr/userMgr/userMgr_add");
    }

    @RequestMapping("toEdit/{id}")
    public ModelAndView toEdit(HttpServletRequest request,
                               @PathVariable("id") String id) throws Exception {
        //1. 获取角色信息
        List<Role> allRoleList = api.getAllRoleList(request);
        request.setAttribute("roleList", allRoleList);
        //2. 获取银行信息
        List<Yh> yhAllList = api.getYhAllList(request);
        request.setAttribute("yhList", yhAllList);
        //3. 获取物业公司信息
        List<WyGs> wyGsAllList = api.getWyGsAllList(request);
        request.setAttribute("wyGsList", wyGsAllList);
        //4. 当前登陆用户
        request.setAttribute("user", api.getUserFromCookie(request));
        //5. 对应的用户信息
        User user = api.getUserDetail(request, id);
        request.setAttribute("user", user);
        return new ModelAndView("sysMgr/userMgr/userMgr_edit");
    }

    /**
     * 添加用户
     *
     * @param request req
     * @param json    请求体
     * @return res
     */
    @RequestMapping("addUser")
    @ResponseBody
    public ResponseEntity<CusResponseBody> addUser(HttpServletRequest request,
                                                   @RequestBody JSONObject json) {
        try {
            api.addUser(request, json);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("添加用户成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("添加用户失败", e);
            throw new BusinessException("添加用户失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 编辑用户
     *
     * @param request req
     * @param json    请求体
     * @return res
     */
    @RequestMapping("editUser")
    @ResponseBody
    public ResponseEntity<CusResponseBody> editUser(HttpServletRequest request,
                                                    @RequestBody JSONObject json) {
        try {
            api.editUser(request, json);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("更新用户成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("更新用户失败", e);
            throw new BusinessException("更新用户失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 修改密码(当前登陆用户)
     *
     * @param request     req
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return ret
     */
    @RequestMapping("editUserPassword")
    @ResponseBody
    public ResponseEntity<CusResponseBody> editUserPassword(HttpServletRequest request,
                                                            @RequestParam("oldPassword") String oldPassword,
                                                            @RequestParam("newPassword") String newPassword) {
        try {
            api.editUserPassword(request, oldPassword, newPassword);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("修改密码成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("修改密码失败", e);
            throw new BusinessException("修改密码失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

}
