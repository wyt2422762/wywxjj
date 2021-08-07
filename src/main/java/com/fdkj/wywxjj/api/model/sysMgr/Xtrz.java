package com.fdkj.wywxjj.api.model.sysMgr;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 接口返回的系统日志实体类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Xtrz {
    private String id;
    private String action_type;
    private String addtime;
    private String fk_qybm;
    private String fk_xtglid;
    private String remark;
    private String user_ip;
    private String user_name;
}
