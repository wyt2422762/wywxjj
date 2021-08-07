package com.fdkj.wywxjj.api.model.sysMgr;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 接口返回物业公司实体类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class WyGs {
    private String id;
    private String addtime;
    private String fk_xtglid;
    private String fk_qybm;
    private String qymc;
    private String clsj;
    private String zcdz;
    private String frdb;
    private String lxdh;
    private String bgdz;
    private String txdz;
    private String yyzzh;
}
