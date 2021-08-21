package com.fdkj.wywxjj.constant;

/**
 * 通用常量信息
 *
 * @author ruoyi
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = "sub";

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 小程序token前缀（客户）
     */
    public static final String MP_CONSUMER_KEY = "mp_consumer_key";
    /**
     * 小程序token前缀（护工）
     */
    public static final String MP_NURSE_KEY = "mp_nurse_key";

    /**
     * 记账类别
     */
    public static class JzLb {
        /**
         * 资金初缴
         */
        public static final String ZJCJ = "资金初缴";
        /**
         * 缴费
         */
        public static final String JF = "缴费";
        /**
         * 资金结息
         */
        public static final String ZJJX = "资金结息";
        /**
         * 预付返款
         */
        public static final String YFFK = "预付返款";
        /**
         * 维修支出
         */
        public static final String WXZC = "维修支出";
        /**
         * 销户退款
         */
        public static final String XHTK = "销户退款";
    }

    /**
     * 流程结点类型
     */
    public static class WorkflowNodeType {
        /**
         * 启动
         */
        public static final String QD = "启动";
        /**
         * 结束
         */
        public static final String JS = "结束";
    }

    /**
     * 流程定义id
     */
    public static class WorkflowId {
        /**
         * 销户
         */
        public static final String XH = "0A0204F1-E899-43E9-AC5E-B6E47C03393B";

    }

    /**
     * 流程状态
     */
    public static class WorkflowStatus {
        /**
         * 审核中
         */
        public static final String SHZ = "审核中";
        /**
         * 已审核
         */
        public static final String YSH = "已审核";

    }

    /**
     * 流程操作
     */
    public static class WorkflowOpt {
        /**
         * 提交申请
         */
        public static final String START = "提交申请";

        /**
         * 初审
         */
        public static final String CS = "初审";

        /**
         * 中审
         */
        public static final String ZS = "中审";

        /**
         * 终审
         */
        public static final String ZZS = "终审";

        /**
         * 结束
         */
        public static final String END = "结束";

    }
}
