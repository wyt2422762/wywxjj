package com.fdkj.wywxjj.error;

import com.fdkj.wywxjj.base.CusResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一错误处理
 *
 * @author wyt
 */
@ControllerAdvice
public class CusExceptionHandler {

    /**
     * 判断是否是Ajax请求
     *
     * @param request req
     * @return 是否是ajax请求
     */
    private boolean isAjax(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null &&
                "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
    }

    /**
     * 处理自定义异常
     *
     * @param req req
     * @param e   异常
     * @return ret
     */
    @ExceptionHandler(value = BusinessException.class)
    public Object cusException(HttpServletRequest req, BusinessException e) {
        if (isAjax(req)) {
            CusResponseBody cusResponseBody = new CusResponseBody();
            CusResponseBody.error(e.getCode(), e.getMessage());
            HttpStatus httpStatus = HttpStatus.resolve(e.getCode());
            return new ResponseEntity<>(cusResponseBody, httpStatus != null ? httpStatus : HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("error");
            modelAndView.addObject("errorMsg", e.getMessage());
            modelAndView.addObject("errorCode", e.getCode());
            return modelAndView;
        }
    }

    /**
     * 处理其他异常
     *
     * @param req req
     * @param e   异常
     * @return ret
     */
    @ExceptionHandler(value = Exception.class)
    public Object exception(HttpServletRequest req, Exception e) {
        if (isAjax(req)) {
            CusResponseBody cusResponseBody = CusResponseBody.error(500, e.getMessage());
            return new ResponseEntity<>(cusResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("error");
            modelAndView.addObject("errorMsg", e.getMessage());
            modelAndView.addObject("errorCode", 500);
            return modelAndView;
        }
    }
}
