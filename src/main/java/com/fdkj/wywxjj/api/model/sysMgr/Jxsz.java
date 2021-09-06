package com.fdkj.wywxjj.api.model.sysMgr;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 利息设置
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Jxsz {
    private String id;

    private String addtime;

    /**
     * 区域编码
     */
    private String fk_qybm;

    private String fk_xtglid;

    /**
     * 银行id
     */
    private String fk_bankid;

    /**
     * 生效日期
     */
    private String sxrq;

    /**
     * 利率
     */
    private String rate;
}
