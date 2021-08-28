package com.fdkj.wywxjj.api.model.fa;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 方案明细
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Fa_mx {
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
    private String addtime;

    /**
     * 费项名称
     */
    private String fxmc;

    /**
     * 费项金额
     */
    private String fxje;

    /**
     * 备注
     */
    private String bz;

    /**
     * 方案id
     */
    private String fk_faid;
}
