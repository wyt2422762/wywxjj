package com.fdkj.wywxjj.api.model.sysMgr;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口返回的系统模块的实体类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class View_PT_XT_MK_Model {

    private String id;
    private String PT_XT_MKid;
    private String iszm;
    private String xtlx;
    /**
     * 父级id
     */
    private String fid;
    /**
     * 模块名称
     */
    private String mkmc;
    private String mklx;
    private String mkurl;
    /**
     * 模块功能按钮
     */
    private String mkgnan;
    private String mkimg;
    /**
     * 模块排序id
     */
    private Integer mkpxid;
    /**
     * 模块级别
     */
    private Integer mkjb;
    private Boolean iswblj;

    /**
     * 是否显示（默认不显示）
     */
    private boolean show = false;

    /**
     * 操作权限(show add del update export等)
     */
    private List<String> opts;

    private List<View_PT_XT_MK_Model> childList;

    public List<String> getMkAnnius() {
        if (StringUtils.isBlank(getMkgnan())) {
            return null;
        }
        return Arrays.asList(getMkgnan().trim().split(","));
    }

    public String getOptStr() {
        List<String> opts = getOpts();
        if (opts == null || opts.isEmpty()) {
            return "";
        }
        String res = opts.stream().collect(Collectors.joining(","));
        return res;
    }

}
