package com.fdkj.wywxjj.api.model.xmMgr;

import com.fdkj.wywxjj.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 接口返回项目(小区)实体类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Xm {

    private String id;
    /**
     * 区域编码
     */
    private String fk_qybm;

    /**
     * 系统id
     */
    private String fk_xtglid;

    /**
     * 添加时间
     */
    private String add_time;

    /**
     * 添加人
     */
    private String add_user;

    /**
     * 机构编码
     */
    private String fk_jgbm;

    /**
     * 项目名称
     */
    @Excel(name = "小区名称", cellType = Excel.ColumnType.STRING, prompt = "小区名称")
    private String xmmc;

    /**
     * 项目类型
     */
    @Excel(name = "项目类型", cellType = Excel.ColumnType.STRING, prompt = "项目类型")
    private String xmlx;

    /**
     * 项目地址
     */
    @Excel(name = "项目地址", cellType = Excel.ColumnType.STRING, prompt = "项目地址")
    private String xmdz;

    /**
     * 是否在建
     */
    @Excel(name = "是否在建", cellType = Excel.ColumnType.STRING, prompt = "是否在建")
    private String sfzj;

    /**
     * 项目性质
     */
    @Excel(name = "项目性质", cellType = Excel.ColumnType.STRING, prompt = "项目性质")
    private String xmxz;

    /**
     * 交付标准
     */
    @Excel(name = "交付标准", cellType = Excel.ColumnType.STRING, prompt = "交付标准")
    private String jfbz;

    /**
     * 销售地址
     */
    @Excel(name = "销售地址", cellType = Excel.ColumnType.STRING, prompt = "销售地址")
    private String xsdz;

    /**
     * 土地使用权取得方式
     */
    @Excel(name = "土地使用权取得方式", cellType = Excel.ColumnType.STRING, prompt = "土地使用权取得方式")
    private String tdsyqqdfs;

    /**
     * 土地规划用途
     */
    @Excel(name = "土地规划用途", cellType = Excel.ColumnType.STRING, prompt = "土地规划用途")
    private String tdghyt;

    /**
     * 投资商
     */
    @Excel(name = "投资商", cellType = Excel.ColumnType.STRING, prompt = "投资商")
    private String tzs;

    /**
     * 计划总投资
     */
    @Excel(name = "计划总投资", cellType = Excel.ColumnType.NUMERIC, prompt = "计划总投资")
    private String jhztz;

    /**
     * 土地投资
     */
    @Excel(name = "土地投资", cellType = Excel.ColumnType.NUMERIC, prompt = "土地投资(元)")
    private String tdtz;

    /**
     * 平均价格
     */
    @Excel(name = "平均价格", cellType = Excel.ColumnType.NUMERIC, prompt = "平均价格(元)")
    private String pjjg;

    /**
     * 计划开工日期
     */
    @Excel(name = "计划开工日期", cellType = Excel.ColumnType.STRING, prompt = "计划开工日期", dateFormat = "yyyy-MM-dd\'T\'HH:mm:ss.SSS")
    private String jhkgrq;

    /**
     * 计划竣工日期
     */
    @Excel(name = "计划竣工日期", cellType = Excel.ColumnType.STRING, prompt = "计划竣工日期", dateFormat = "yyyy-MM-dd\'T\'HH:mm:ss.SSS")
    private String jhjgrq;

    /**
     * 土地使用年限（自***至***）
     */
    @Excel(name = "地使用年限", cellType = Excel.ColumnType.STRING, prompt = "土地使用年限（自***至***）")
    private String tdsynx;

    /**
     * 土地面积
     */
    @Excel(name = "土地面积", cellType = Excel.ColumnType.NUMERIC, prompt = "土地面积")
    private String tdmj;

    /**
     * 建筑面积
     */
    @Excel(name = "建筑面积", cellType = Excel.ColumnType.NUMERIC, prompt = "建筑面积")
    private String jzmj;

    /**
     * 容积率
     */
    @Excel(name = "容积率", cellType = Excel.ColumnType.STRING, prompt = "容积率")
    private String rjl;

    /**
     * 绿化率
     */
    @Excel(name = "绿化率", cellType = Excel.ColumnType.STRING, prompt = "绿化率")
    private String lhl;

    /**
     * 商品房预售许可证
     */
    @Excel(name = "商品房预售许可证", cellType = Excel.ColumnType.STRING, prompt = "商品房预售许可证")
    private String spfysxkz;

    /**
     * 建设工程规划许可证
     */
    @Excel(name = "建设工程规划许可证", cellType = Excel.ColumnType.STRING, prompt = "建设工程规划许可证")
    private String jzgcghxkz;

    /**
     * 施工许可证
     */
    @Excel(name = "施工许可证", cellType = Excel.ColumnType.STRING, prompt = "施工许可证")
    private String sgxkz;

    /**
     * 开工证明
     */
    @Excel(name = "开工证明", cellType = Excel.ColumnType.STRING, prompt = "开工证明")
    private String kgzm;

    /**
     * 建设用地规划许可证
     */
    @Excel(name = "建设用地规划许可证", cellType = Excel.ColumnType.STRING, prompt = "建设用地规划许可证")
    private String jsydghxkz;

    /**
     * 土地编号
     */
    @Excel(name = "土地编号", cellType = Excel.ColumnType.STRING, prompt = "土地编号")
    private String tdbh;

    /**
     * 土地使用权证
     */
    @Excel(name = "土地使用权证", cellType = Excel.ColumnType.STRING, prompt = "土地使用权证")
    private String tdsyqz;

    /**
     * 开发商名称
     */
    @Excel(name = "开发商名称", cellType = Excel.ColumnType.STRING, prompt = "开发商名称")
    private String kfsmc;

    /**
     * 开发商法人
     */
    @Excel(name = "开发商法人", cellType = Excel.ColumnType.STRING, prompt = "开发商法人")
    private String kfsfr;

    /**
     * 开发商联系电话
     */
    @Excel(name = "开发商联系电话", cellType = Excel.ColumnType.STRING, prompt = "开发商联系电话")
    private String kfsdh;

    /**
     * 开发商地址
     */
    @Excel(name = "开发商地址", cellType = Excel.ColumnType.STRING, prompt = "开发商地址")
    private String kfsdz;

    /**
     * 开发商营业执照
     */
    @Excel(name = "开发商营业执照", cellType = Excel.ColumnType.STRING, prompt = "开发商营业执照")
    private String kfszz;

    /**
     * 物业公司ID
     */
    private String fk_wyid;
}
