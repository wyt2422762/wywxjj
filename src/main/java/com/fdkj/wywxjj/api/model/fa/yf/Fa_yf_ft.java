package com.fdkj.wywxjj.api.model.fa.yf;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 方案预付分摊信息
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Fa_yf_ft {
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
     * 方案id
     */
    private String fk_faid;

    /**
     * 预付款id
     */
    private String fk_yfkid;

    /**
     * 账户id
     */
    private String fk_zhid;

    /**
     * 房号id
     */
    private String fk_fhid;

    /**
     * 分摊金额
     */
    private String ftje;

    /**
     * 分摊比例
     */
    private String ftbl;
}
