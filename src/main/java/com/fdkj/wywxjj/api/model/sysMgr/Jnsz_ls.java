package com.fdkj.wywxjj.api.model.sysMgr;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 接口返回缴纳设置实体明细类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Jnsz_ls {

    private String id;
    private String addtime;
    /**
     * 区域编码
     */
    private String fk_qybm;
    private String fk_xtglid;

    private String fk_jnszid;

    /**
     * 起始楼层
     */
    private String qslc;

    /**
     * 结束楼层
     */
    private String jslc;

    /**
     * 起始面积（平米）
     */
    private String qsmj;

    /**
     * 结束面积（平米）
     */
    private String jsmj;

    /**
     * 缴纳比例（%）
     */
    private String jnbl;

    /**
     * 缴纳金额（元每平米）
     */
    private String jnje;

}
