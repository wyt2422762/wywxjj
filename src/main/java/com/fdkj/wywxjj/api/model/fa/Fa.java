package com.fdkj.wywxjj.api.model.fa;

import com.alibaba.fastjson.annotation.JSONField;
import com.fdkj.wywxjj.api.model.fa.js.Fa_js;
import com.fdkj.wywxjj.api.model.fa.yf.Fa_yf;
import com.fdkj.wywxjj.api.model.xmMgr.Ld;
import com.fdkj.wywxjj.api.model.xmMgr.Xm;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 方案基本信息
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Fa {
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
     * 方案编号
     */
    private String fabh;

    /**
     * 方案名称
     */
    private String famh;

    /**
     * 方案预计金额(预算)
     */
    private String fayjje;

    /**
     * 方案状态(待审核，初审通过，初审不通过，复审通过，复审不通过，终审不通过，终审通过)
     */
    private String fazt;

    /**
     * 计划开工日期
     */
    @JSONField(format="yyyy-MM-dd")
    private String kgrq;

    /**
     * 计划完工日期
     */
    @JSONField(format="yyyy-MM-dd")
    private String wgrq;

    /**
     * 开发商代码
     */
    private String fk_kfsdm;

    /**
     * 项目信息id
     */
    private String fk_xmxxid;

    /**
     * 楼栋信息id
     */
    private String fk_ldxxid;

    /**
     * 单元号(暂时以单元为单位建立方案)
     */
    private String dyh;

    /**
     * 施工单位
     */
    private String sgdw;

    /**
     * 施工单位法人
     */
    private String sgdwfr;

    /**
     * 施工单位营业执照
     */
    private String sgdwyyzz;

    /**
     * 施工单位资质证号
     */
    private String sgdwzzzh;

    /**
     * 开户账号
     */
    private String khzh;

    /**
     * 工程款支付方式
     */
    private String gckzzfs;

    /**
     * 工程款使用原因
     */
    private String gcksyyy;

    /**
     * 分摊方式
     */
    private String ftfs;

    /**
     * 申请日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private String sqrq;

    /**
     * 登记日期
     */
    @JSONField(format="yyyy-MM-dd")
    private String djrq;

    /**
     * 备注
     */
    private String bz;

    /**
     * 方案起草
     */
    private String faqc;

    /**
     * 业主委员会意见
     */
    private String yzwyhyj;

    /**
     * 业主委员会(签名?)
     */
    private String yzwyh;

    /**
     * 签字日期
     */
    private String qzrq;

    /**
     * 预付状态(未预付，已预付)
     */
    private String yfzt;

    /**
     * 结算状态(未结算，已结算)
     */
    private String jszt;

    /**
     * 房号信息id
     */
    private String fk_fhxxid;

    /**
     * 物业公司id
     */
    private String fk_wyid;

    /**
     * 方案状态
     */
    private String zt;

    /**
     * 预付款
     */
    private String yfk;

    /**
     * 房号账户信息
     */
    private List<Fa_fh> fHlist;

    /**
     * 明细信息
     */
    private List<Fa_mx> mXlist;

    /**
     * 预付信息
     */
    private List<Fa_yf> yflist;

    /**
     * 结算信息
     */
    private List<Fa_js> jslist;

    /**
     * 结算信息
     */
    private Fa_js faJs;

    /**
     * 项目(小区)信息
     */
    private Xm xm;

    /**
     * 楼栋信息
     */
    private Ld ld;

    /**
     * 方案描述
     */
    private String desc;

    /**
     * 预付合计(个)
     */
    private String yfHj;

    /**
     * 预付金额合计(元)
     */
    private String yfTotalMoney;

    /**
     * 结算合计(个)
     */
    private String jsHj;

    /**
     * 结算金额合计(元)
     */
    private String jsTotalMoney;

    /**
     * 结算已预付金额合计(元)
     */
    private String jsTotalMoney_yf;

    /**
     * 结算支付金额合计(元)
     */
    private String jsTotalMoney_zf;
}
