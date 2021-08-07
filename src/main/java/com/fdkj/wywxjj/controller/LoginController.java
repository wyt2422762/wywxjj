package com.fdkj.wywxjj.controller;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.error.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * 登录
 *
 * @author wyt
 */
@Controller
@RequestMapping("login")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private Api api;

    @RequestMapping("toLogin")
    public ModelAndView toLogin() {
        return new ModelAndView("login");
    }

    @RequestMapping("login")
    @ResponseBody
    public ResponseEntity<CusResponseBody> login(@RequestParam("userName") String userName,
                                                 @RequestParam("password") String password,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        try {
            if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
                throw new BusinessException("参数不正确", HttpStatus.BAD_REQUEST.value());
            }

            JSONObject res = api.getUserToken(userName, password);
            String token = res.getString("token");
            User user = res.getObject("user", User.class);

            if (StringUtils.isBlank(token) || user == null) {
                log.error("获取用户token失败");
                throw new BusinessException("获取用户token失败", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }

            //token cookie
            Cookie tokenCookie = new Cookie("token", token);
            tokenCookie.setPath(request.getContextPath());
            //用户名 cookie
            Cookie userCookie = new Cookie("user", URLEncoder.encode(JSONObject.toJSONString(user), "utf-8"));
            userCookie.setPath(request.getContextPath());
            //存cookie
            response.addCookie(tokenCookie);
            response.addCookie(userCookie);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("登录成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("登录失败", e);
            throw new BusinessException("登录失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    @RequestMapping("logout")
    @ResponseBody
    public ResponseEntity<CusResponseBody> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 将Cookie的值设置为null
            Cookie tokenCookie = new Cookie("token", null);
            Cookie userNameCookie = new Cookie("username", null);
            //将`Max-Age`设置为0
            tokenCookie.setMaxAge(0);
            tokenCookie.setPath(request.getContextPath());
            userNameCookie.setMaxAge(0);
            userNameCookie.setPath(request.getContextPath());

            response.addCookie(tokenCookie);
            response.addCookie(userNameCookie);

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("退出成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("退出失败", e);
            throw new BusinessException("退出失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
