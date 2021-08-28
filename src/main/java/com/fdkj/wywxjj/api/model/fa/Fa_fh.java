package com.fdkj.wywxjj.api.model.fa;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 方案房号关系表
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Fa_fh {
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
     * 方案id
     */
    private String fk_faid;

    /**
     * 房号id
     */
    private String fk_fhid;

    /**
     * 账号id
     */
    private String fk_zhid;

    /**
     * 分摊金额
     */
    private String ftje;
}
