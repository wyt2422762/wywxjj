package com.fdkj.wywxjj.api.model.sysMgr;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 接口返回的角色信息实体类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Role {
    private String id;
    private String addtime;
    private String fk_xtglid;
    private String JSJB;
    private String JSMC;
}
