package com.fdkj.wywxjj.api.model.wf;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 工作流流转
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class WorkflowLink {
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
    private String fk_jdid;

    /**
     * 动作
     */
    private String dz;

    /**
     * 描述
     */
    private String ms;

    /**
     * 下一结点id
     */
    private String fk_jdid_next;

    /**
     * 下一结点对应的角色id
     */
    private String fk_jsid_next;
}
