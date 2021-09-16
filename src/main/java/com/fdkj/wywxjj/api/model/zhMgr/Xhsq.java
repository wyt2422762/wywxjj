package com.fdkj.wywxjj.api.model.zhMgr;

import com.fdkj.wywxjj.api.model.wf.WorkflowInstant;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 销户申请
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Xhsq {
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
     * 账号id
     */
    private String fk_zhid;

    /**
     * 账号
     */
    private String no;

    /**
     * 销户申请
     */
    private String xhsq;

    /**
     * 证件号码
     */
    private String zjhm;

    /**
     * 与账户所有人关系
     */
    private String gx;

    /**
     * 联系电话
     */
    private String lxdh;

    /**
     * 销户原因
     */
    private String xhyy;

    /**
     * 申请日期
     */
    private String sqrq;

    /**
     * 登记日期
     */
    private String djrq;

    /**
     * 银行id
     */
    private String fk_bankid;

    /**
     * 操作人id
     */
    private String fk_yhid;

    /**
     * 操作人
     */
    private String czr;

    /**
     * 状态
     */
    private String zt;

    /**
     * 账号
     */
    private Zh zh;

    /**
     * 流程实例
     */
    private WorkflowInstant wfi;
}
