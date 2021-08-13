package com.fdkj.wywxjj.api.model.xmMgr;

import com.fdkj.wywxjj.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 接口返回房号实体类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Fh {
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
     * 项目(小区)信息id
     */
    private String fk_xmxxid;

    /**
     * 楼栋信息id
     */
    private String fk_ldxxid;

    /**
     * 管理物业公司id
     */
    private String fk_wyid;

    /**
     * 户顺序号
     */
    @Excel(name = "户顺序号", cellType = Excel.ColumnType.STRING, prompt = "户顺序号")
    private String hsxh;

    /**
     * 房号
     */
    @Excel(name = "房号", cellType = Excel.ColumnType.STRING, prompt = "房号")
    private String fh;

    /**
     * 所在单元
     */
    @Excel(name = "所在单元", cellType = Excel.ColumnType.STRING, prompt = "所在单元")
    private String szdy;

    /**
     * 所在楼层
     */
    @Excel(name = "所在楼层", cellType = Excel.ColumnType.STRING, prompt = "所在楼层")
    private String szlc;

    /**
     * 所在功能区（住宅、商业、办公、公寓、酒店、商铺、社区、物业、车库、人防、地下室、公共租赁住宅、消防值班室、水箱间）
     */
    @Excel(name = "所在功能区", cellType = Excel.ColumnType.STRING, prompt = "所在功能区")
    private String szgnq;

    /**
     * 预测面积_建筑面积 decimal
     */
    @Excel(name = "预测面积_建筑面积", cellType = Excel.ColumnType.NUMERIC, prompt = "预测面积_建筑面积")
    private String ycmj_jzmj;

    /**
     * 预测面积_套内面积 decimal
     */
    @Excel(name = "预测面积_套内面积", cellType = Excel.ColumnType.NUMERIC, prompt = "预测面积_套内面积")
    private String ycmj_tnmj;

    /**
     * 预测面积_公摊面积 decimal
     */
    @Excel(name = "预测面积_公摊面积", cellType = Excel.ColumnType.NUMERIC, prompt = "预测面积_公摊面积")
    private String ycmj_gtmj;

    /**
     * 预测面积_其中阳台面积 decimal
     */
    @Excel(name = "预测面积_其中阳台面积", cellType = Excel.ColumnType.NUMERIC, prompt = "预测面积_其中阳台面积")
    private String ycmj_qzytmj;

    /**
     * 预测面积_分摊系数 decimal
     */
    @Excel(name = "预测面积_分摊系数", cellType = Excel.ColumnType.NUMERIC, prompt = "预测面积_分摊系数")
    private String ycmj_ftxs;

    /**
     * 房间状态
     */
    @Excel(name = "房间状态", cellType = Excel.ColumnType.STRING, prompt = "房间状态")
    private String fjzt;

    /**
     * 实测面积_建筑面积 decimal
     */
    @Excel(name = "实测面积_建筑面积", cellType = Excel.ColumnType.NUMERIC, prompt = "实测面积_建筑面积")
    private String scmj_jzmj;

    /**
     * 实测面积_套内面 decimal
     */
    @Excel(name = "实测面积_套内面", cellType = Excel.ColumnType.NUMERIC, prompt = "实测面积_套内面")
    private String scmj_tnmj;

    /**
     * 实测面积_公摊面积 decimal
     */
    @Excel(name = "实测面积_公摊面积", cellType = Excel.ColumnType.NUMERIC, prompt = "实测面积_公摊面积")
    private String scmj_gtmj;

    /**
     * 实测面积_其中阳台面 decimal
     */
    @Excel(name = "实测面积_其中阳台面", cellType = Excel.ColumnType.NUMERIC, prompt = "实测面积_其中阳台面")
    private String scmj_qzytmj;

    /**
     * 实测面积_分摊系数 decimal
     */
    @Excel(name = "实测面积_分摊系数", cellType = Excel.ColumnType.NUMERIC, prompt = "实测面积_分摊系数")
    private String scmj_ftxs;

    /**
     * 业主名称
     */
    @Excel(name = "业主名称", cellType = Excel.ColumnType.STRING, prompt = "业主名称")
    private String yzmc;

    /**
     * 业主证件号码
     */
    @Excel(name = "业主证件号码", cellType = Excel.ColumnType.STRING, prompt = "业主证件号码")
    private String yzzjh;

    /**
     * 业主联系电话
     */
    @Excel(name = "业主联系电话", cellType = Excel.ColumnType.STRING, prompt = "业主联系电话")
    private String yzlxdh;

    /**
     * 业主地址
     */
    @Excel(name = "业主地址", cellType = Excel.ColumnType.STRING, prompt = "业主地址")
    private String yzdz;

    /**
     * 合同备案号
     */
    @Excel(name = "合同备案号", cellType = Excel.ColumnType.STRING, prompt = "合同备案号")
    private String htbah;

    /**
     * 户型
     */
    @Excel(name = "户型", cellType = Excel.ColumnType.STRING, prompt = "户型")
    private String hx;

    /**
     * 类型
     */
    @Excel(name = "房屋类型", cellType = Excel.ColumnType.STRING, prompt = "房屋类型")
    private String lx;

    /**
     * 用途
     */
    @Excel(name = "房屋用途", cellType = Excel.ColumnType.STRING, prompt = "房屋用途")
    private String yt;

    /**
     * 朝向
     */
    @Excel(name = "房屋朝向", cellType = Excel.ColumnType.STRING, prompt = "房屋朝向")
    private String cx;

    /**
     * 房屋间数
     */
    @Excel(name = "房屋间数", cellType = Excel.ColumnType.STRING, prompt = "房屋间数")
    private String fwjs;

    /**
     * 装修水平
     */
    @Excel(name = "装修水平", cellType = Excel.ColumnType.STRING, prompt = "装修水平")
    private String zxsp;

    /**
     * 装修水平
     */
    @Excel(name = "合同金额(元)", cellType = Excel.ColumnType.NUMERIC, prompt = "合同金额(元)")
    private String htje;

}
