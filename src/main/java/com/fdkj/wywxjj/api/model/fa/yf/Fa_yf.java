package com.fdkj.wywxjj.api.model.fa.yf;

import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 方案预付信息
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Fa_yf {
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
     * 预付款编号
     */
    private String yfkbh;

    /**
     * 预付款金额(元)
     */
    private String yfkje;

    /**
     * 预付款日期
     */
    private String yfkrq;

    /**
     * 预付款签署
     */
    private String yfkqs; //预付款签署

    /**
     * 方案预算(元)
     */
    private String fays;

    /**
     * 备注
     */
    private String bz;

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
    private List<Fa_yf_ft> ftList;

    /**
     * 分摊信息列表
     */
    private List<Fh> ftList2;

    /**
     * 分摊合计(个)
     */
    private String ftHj;

    /**
     * 分摊金额合计(元)
     */
    private String ftTotalMoney;
}
