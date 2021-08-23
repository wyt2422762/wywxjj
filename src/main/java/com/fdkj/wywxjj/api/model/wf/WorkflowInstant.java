package com.fdkj.wywxjj.api.model.wf;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 工作流实例
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class WorkflowInstant {

    private String id;

    /**
     * 系统id
     */
    private String fk_xtglid;
    /**
     * 区域编码
     */
    private String fk_qybm;

    /**
     * 添加时间
     */
    private String add_time;

    /**
     * 流程定义id
     */
    private String fk_wfid;

    /**
     * 当前结点id
     */
    private String fk_dqjdid;

    /**
     * 业务id
     */
    private String fk_ywid;

    /**
     * 角色id
     */
    private String fk_jsid;

    /**
     * 发起人id
     */
    private String fk_yhid;

    /**
     * 流程状态(审核中，审核完成)
     */
    private String zt;

    /**
     * 发起时间
     */
    private String fqsj;

    /**
     * 发起用户
     */
    private String fqr;

    /**
     * 审批结果(通过，未通过)
     */
    private String jg;

    /**
     * 业务类型
     */
    private String lx;

    /**
     * 对应的业务数据
     */
    private Object data;

    /**
     * 当前结点id
     */
    private WorkflowNode currentNode;
}
