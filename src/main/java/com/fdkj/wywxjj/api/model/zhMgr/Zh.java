package com.fdkj.wywxjj.api.model.zhMgr;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 账户
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Zh {

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
     * 房号信息id
     */
    private String fk_fhxxid;

    /**
     * 银行id
     */
    private String fk_yhid;

    /**
     * 账号
     */
    private String no;
    /**
     * 开户日期
     */
    private String khrq;
    /**
     * 开户银行
     */
    private String khyh;
    /**
     * 房屋总价
     */
    private String fwzj;
    /**
     * 缴纳比例
     */
    private String jzbl;
    /**
     * 账户金额
     */
    private String money;
    /**
     * 账户状态(0正常 1已销户)
     */
    private String zt;
    /**
     * 是否已退款(0未退款 1已退款)
     */
    private String sftk;
    /**
     * 续费日期
     */
    private String xfrq;
    /**
     * 过户日期
     */
    private String ghrq;

    /**
     * 业主名称
     */
    private String yzmc;

    /**
     * 业主证件号
     */
    private String yzzjh;

    /**
     * 联系电话
     */
    private String lxdh;

    /**
     * 明细
     */
    private List<Zh_his> list;
}
