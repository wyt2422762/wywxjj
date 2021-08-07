package com.fdkj.wywxjj.error;

/**
 * 业务异常
 *
 * @author wyt
 */
public class BusinessException extends RuntimeException {

    private int code;

    public int getCode() {
        return code;
    }

    public BusinessException() {
        super();
    }

    /**
     * 返回自定义异常
     *
     * @param message 异常消息
     * @param code    错误码
     */
    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

    /**
     * 返回自定义异常
     *
     * @param message 异常消息
     * @param code    错误码
     * @param cause   异常
     */
    public BusinessException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
