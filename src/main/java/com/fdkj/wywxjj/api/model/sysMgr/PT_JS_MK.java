package com.fdkj.wywxjj.api.model.sysMgr;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class PT_JS_MK {
    private String id;
    //操作权限
    private String actionType;
    private String addtime;
    //角色id
    private String fk_jsid;
    //系统管理id
    private String fk_xtglid;
    //系统模块id
    private String fk_XTMKid;
    private String IsZMTB;
}
