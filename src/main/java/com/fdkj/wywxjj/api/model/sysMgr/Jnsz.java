package com.fdkj.wywxjj.api.model.sysMgr;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 接口返回缴纳设置实体类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Jnsz {

    private String id;
    private String addtime;
    /**
     * 区域编码
     */
    private String fk_qybm;
    private String fk_xtglid;

    /**
     * 缴纳标准
     */
    private String jnbz;

    /**
     * 缴纳方式
     */
    private String jnfs;

    /**
     * 缴纳比例（%）
     */
    private String jnbl;

    /**
     * 缴纳金额(元没平米)
     */
    private String jnje;

    /**
     * 明细
     */
    private List<Jnsz_ls> list;

}
