package com.fdkj.wywxjj.base;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 * 数据返回格式
 *
 * @author wyt
 */
public class CusResponseBody extends HashMap<String, Object> {
    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 信息
     */
    public static final String MSG_TAG = "msg";

    /**
     * 是否成功
     */
    public static final String SUCCESS_TAG = "success";

    /**
     * 数据
     */
    public static final String DATA_TAG = "data";

    public static final String DEFAULT_SUCCESS_MSG = "操作成功";
    public static final String DEFAULT_ERROR_MSG = "操作失败";

    public CusResponseBody() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code    状态码
     * @param msg     信息
     * @param success 是否成功
     */
    public CusResponseBody(int code, boolean success, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(SUCCESS_TAG, success);
        if (StringUtils.isNotBlank(msg)) {
            super.put(MSG_TAG, msg);
        }
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 成功返回结果
     *
     * @param msg  信息
     * @param data 数据
     * @return res
     */
    public static CusResponseBody success(String msg, Object data) {
        return new CusResponseBody(HttpStatus.OK.value(), true, msg, data);
    }

    /**
     * 成功返回结果
     *
     * @param msg 信息
     * @return res
     */
    public static CusResponseBody success(String msg) {
        return success(msg, null);
    }

    /**
     * 成功返回结果
     *
     * @param data 数据
     * @return res
     */
    public static CusResponseBody success(Object data) {
        return success(DEFAULT_SUCCESS_MSG, data);
    }

    /**
     * 成功返回结果
     *
     * @return res
     */
    public static CusResponseBody success() {
        return success(DEFAULT_SUCCESS_MSG, null);
    }

    /**
     * 返回错误消息
     *
     * @param code 错误妈
     * @param msg  信息
     * @param data 数据
     * @return res
     */
    public static CusResponseBody error(int code, String msg, Object data) {
        return new CusResponseBody(code, false, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 错误妈
     * @param msg  信息
     * @return res
     */
    public static CusResponseBody error(int code, String msg) {
        return new CusResponseBody(code, false, msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param code 错误妈
     * @param data 数据
     * @return res
     */
    public static CusResponseBody error(int code, Object data) {
        return new CusResponseBody(code, false, DEFAULT_ERROR_MSG, data);
    }

    /**
     * 返回错误消息(code默认为500)
     *
     * @param msg  信息
     * @param data 数据
     * @return res
     */
    public static CusResponseBody error(String msg, Object data) {
        return new CusResponseBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, msg, data);
    }

    /**
     * 返回错误消息(code默认为500)
     *
     * @param data 数据对象
     * @return res
     */
    public static CusResponseBody error(Object data) {
        return new CusResponseBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, DEFAULT_ERROR_MSG, data);
    }

    /**
     * 返回错误消息(code默认为500)
     *
     * @param msg 信息
     * @return res
     */
    public static CusResponseBody error(String msg) {
        return new CusResponseBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, msg, null);
    }

    /**
     * 返回错误消息(code默认为500)
     *
     * @return res
     */
    public static CusResponseBody error() {
        return new CusResponseBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, DEFAULT_ERROR_MSG, null);
    }
}
