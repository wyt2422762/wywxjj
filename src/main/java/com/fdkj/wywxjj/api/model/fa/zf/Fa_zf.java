package com.fdkj.wywxjj.api.model.fa.zf;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 方案预付支付
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Fa_zf {
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
     * 支付类型(预付款，结算款)
     */
    private String zflx;

    /**
     * 预付款id
     */
    private String fk_yfkid;

    /**
     * 结算id
     */
    private String fk_jsid;

    /**
     * 支付编号
     */
    private String zfbh;

    /**
     * 支付金额
     */
    private String zfje;

    /**
     * 支付登记
     */
    private String zfdj;

    /**
     * 款项支取
     */
    private String kxzq;

    /**
     * 身份证号
     */
    private String sfzh;

    /**
     * 支付日期
     */
    private String zfrq;

    /**
     * 工作单位
     */
    private String gzdw;

    /**
     * 备注
     */
    private String bz;

    /**
     * 方案id
     */
    private String fk_faid;

    /**
     * 状态
     */
    private String zt;

    /**
     * 操作人id
     */
    private String fk_yhid;

    /**
     * 操作人
     */
    private String czr;
}
