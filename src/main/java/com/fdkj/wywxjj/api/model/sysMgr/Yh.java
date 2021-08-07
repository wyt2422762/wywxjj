package com.fdkj.wywxjj.api.model.sysMgr;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 接口返回银行实体类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Yh {
    private String id;
    private String addtime;
    private String fk_xtglid;
    private String fk_qybm;

    private String yxmc;
    private String yxdz;
    private String lxdh;
    private String lxr;
    private String wdmc;
}
