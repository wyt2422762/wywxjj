package com.fdkj.wywxjj.api.model.wf;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 工作流结点
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class WorkflowNode {
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
     * 结点名称
     */
    private String jdmc;

    /**
     * 审批角色id
     */
    private String fk_jsid;

    /**
     * 备注
     */
    private String bz;

    /**
     * 结点类型
     */
    private String lx;
}
