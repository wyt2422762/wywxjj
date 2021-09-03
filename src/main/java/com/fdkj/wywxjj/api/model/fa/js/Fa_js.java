package com.fdkj.wywxjj.api.model.fa.js;

import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 方案结算信息
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Fa_js {
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
     * 结算编号
     */
    private String jsbh;

    /**
     * 已预付金额
     */
    private String yfje;

    /**
     * 结算金额
     */
    private String jsje;

    /**
     * 应支付金额
     */
    private String yzfje;

    /**
     * 结算日期
     */
    private String jsrq;

    /**
     * 结算登记
     */
    private String jsdj;

    /**
     * 结算批示
     */
    private String jsps;

    /**
     * 备注
     */
    private String bz;

    /**
     * 方案预算
     */
    private String fays;

    /**
     * 结算批示人
     */
    private String jspsr;

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

    /**
     * 分摊信息列表
     */
    private List<Fa_js_ft> ftList;

    /**
     * 分摊信息列表
     */
    private List<Fh> ftList2;
}
