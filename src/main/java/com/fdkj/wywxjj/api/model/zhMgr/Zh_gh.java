package com.fdkj.wywxjj.api.model.zhMgr;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账户过户记录
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Zh_gh {
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
     * 银行id
     */
    private String fk_yhid;

    /**
     * 账户id
     */
    private String fk_zhid;

    /**
     * 账号
     */
    private String no;

    /**
     * 原业主名称
     */
    private String yzmc_old;

    /**
     * 新业主名称
     */
    private String yzmc_new;

    /**
     * 原证件号
     */
    private String yzzjh_old;

    /**
     * 新证件号
     */
    private String yzzjh_new;

    /**
     * 过户日期
     */
    private String ghrq;

    /**
     * 账户
     */
    private Zh zh;
}
