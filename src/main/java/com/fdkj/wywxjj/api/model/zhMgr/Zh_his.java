package com.fdkj.wywxjj.api.model.zhMgr;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账户明细
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Zh_his {
    private String id;
    private String addtime;
    private String fk_qybm;
    private String fk_xtglid;
    private String fk_zhid;

    /**
     * 操作类型
     */
    private String czlx;
    /**
     * 收入
     */
    private String sr;
    /**
     * 支出
     */
    private String zc;
    /**
     * 账户余额
     */
    private String zhye;
    /**
     * 记账日期
     */
    private String jzrq;
}
