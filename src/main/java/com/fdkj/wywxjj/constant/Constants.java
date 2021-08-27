package com.fdkj.wywxjj.constant;

/**
 * 通用常量信息
 *
 * @author wyt
 */
public class Constants {

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 账号状态
     */
    public static class ZhZt {
        /**
         * 正常
         */
        public static final String ZC = "正常";
        /**
         * 销户登记
         */
        public static final String XHDJ = "销户登记";
        /**
         * 销户初审
         */
        public static final String XHCS = "销户初审";
        /**
         * 销户中审
         */
        public static final String XHZS = "销户中审";
        /**
         * 销户终审
         */
        public static final String XHZZS = "销户终审";
        /**
         * 已销户
         */
        public static final String YXH = "已销户";
        /**
         * 销户不通过
         */
        public static final String XHBTG = "销户不通过";
    }

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
     * 流程分类
     */
    public static class WorkflowType {
        /**
         * 销户
         */
        public static final String XH = "销户";
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
     * 流程结点说明
     */
    public static class WorkflowNodeName {
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

    /**
     * 流程动作
     */
    public static class WorkflowAction {
        /**
         * 通过
         */
        public static final String PASS = "通过";
        /**
         * 不通过
         */
        public static final String REJECT = "不通过";
    }

}
