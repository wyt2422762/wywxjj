package com.fdkj.wywxjj.api.model.wf;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 工作流历史
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class WorkflowHistory {
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
     * 流程实例id
     */
    private String fk_wkslid;

    /**
     * 业务id
     */
    private String fk_ywid;

    /**
     * 结点id
     */
    private String fk_jdid;

    /**
     * 审批人(对应的用户id)
     */
    private String fk_yhid;

    /**
     * 审批人(对应的角色id)
     */
    private String fk_jsid;

    /**
     * 审批人
     */
    private String spr;

    /**
     * 审批时间
     */
    private String spsj;

    /**
     * 审批描述
     */
    private String czmc;

    /**
     * 审批意见
     */
    private String yj;

    /**
     * 业务类型
     */
    private String lx;
}
