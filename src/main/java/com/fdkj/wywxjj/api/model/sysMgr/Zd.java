package com.fdkj.wywxjj.api.model.sysMgr;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 接口返回字典实体类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Zd {
    private String id;
    private String addtime;
    private String fk_qybm;
    private String fk_xtglid;

    private String fid;
    private String px;
    private String sfqy;
    private String tjr;
    private String tjsj;
    private String xgr;
    private String xgsj;
    private String zdm;
    private String zdz;
}
