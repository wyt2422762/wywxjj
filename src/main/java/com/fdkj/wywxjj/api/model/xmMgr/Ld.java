package com.fdkj.wywxjj.api.model.xmMgr;

import com.fdkj.wywxjj.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 接口返回楼栋实体类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Ld {
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
    private String add_time;

    /**
     * 添加人
     */
    private String add_user;

    /**
     * 项目(小区id)
     */
    private String fk_xmxxid;

    /**
     * 幢号
     */
    @Excel(name = "幢号", cellType = Excel.ColumnType.NUMERIC, prompt = "幢号")
    private String ch;

    /**
     * 幢名称
     */
    @Excel(name = "幢名称", cellType = Excel.ColumnType.STRING, prompt = "小区名称")
    private String cmc;

    /**
     * 建筑结构
     */
    @Excel(name = "建筑结构", cellType = Excel.ColumnType.STRING, prompt = "建筑结构")
    private String jzjg;

    /**
     * 建筑性质
     */
    @Excel(name = "建筑性质", cellType = Excel.ColumnType.STRING, prompt = "建筑性质")
    private String jzxz;

    /**
     * 住宅建筑面积
     */
    @Excel(name = "住宅建筑面积", cellType = Excel.ColumnType.NUMERIC, prompt = "住宅建筑面积")
    private String zzjzmj;

    /**
     * 住宅套内面积
     */
    @Excel(name = "住宅套内面积", cellType = Excel.ColumnType.NUMERIC, prompt = "住宅套内面积")
    private String zztnmj;

    /**
     * 住宅共有面积
     */
    @Excel(name = "住宅共有面积", cellType = Excel.ColumnType.NUMERIC, prompt = "住宅共有面积")
    private String zzgymj;

    /**
     * 非住宅面积
     */
    @Excel(name = "非住宅面积", cellType = Excel.ColumnType.NUMERIC, prompt = "非住宅面积")
    private String fzzmj;

    /**
     * 总建筑面积
     */
    @Excel(name = "总建筑面积", cellType = Excel.ColumnType.NUMERIC, prompt = "总建筑面积")
    private String zjzmj;

    /**
     * 分摊系数
     */
    @Excel(name = "分摊系数", cellType = Excel.ColumnType.NUMERIC, prompt = "分摊系数")
    private String ftxs;

    /**
     * 地上层数
     */
    @Excel(name = "地上层数", cellType = Excel.ColumnType.NUMERIC, prompt = "地上层数")
    private String dscs;

    /**
     * 地下层数
     */
    @Excel(name = "地下层数", cellType = Excel.ColumnType.NUMERIC, prompt = "地下层数")
    private String dxcs;

    /**
     * 总层数
     */
    @Excel(name = "总层数", cellType = Excel.ColumnType.NUMERIC, prompt = "总层数")
    private String zcs;

    /**
     * 单元数
     */
    @Excel(name = "单元数", cellType = Excel.ColumnType.NUMERIC, prompt = "单元数")
    private String dys;

    /**
     * 总套数
     */
    @Excel(name = "总套数", cellType = Excel.ColumnType.NUMERIC, prompt = "总套数")
    private String zts;
}
