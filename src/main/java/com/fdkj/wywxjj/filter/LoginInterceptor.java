package com.fdkj.wywxjj.filter;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.controller.IndexController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 登录验证
 *
 * @author wyt
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String token = null;
        //获取当前登陆的用户信息(从cookie中获取)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        String requestUri = request.getRequestURI();
        if ("/login".contains(requestUri)) {
            return true;
        }
        if (StringUtils.isBlank(token)) {
            if (isAjaxRequest(request)) {
                response.setHeader("noAuthentication", "true");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                CusResponseBody cusResponseBody = CusResponseBody.error(HttpStatus.UNAUTHORIZED.value(), "登陆成功");

                PrintWriter writer = response.getWriter();
                writer.write(JSONObject.toJSONString(cusResponseBody));
                writer.close();
                response.flushBuffer();
                return false;
            } else {
                response.sendRedirect(requestUri + "login/toLogin");
            }
            return false;
        }
        return true;
    }

    private boolean isAjaxRequest(HttpServletRequest req) {
        return !StringUtils.isBlank(req.getHeader("x-requested-with"))
                && "XMLHttpRequest".equals(req.getHeader("x-requested-with"));
    }

}
