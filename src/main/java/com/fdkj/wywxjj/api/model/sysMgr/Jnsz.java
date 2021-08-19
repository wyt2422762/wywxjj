package com.fdkj.wywxjj.api.model.sysMgr;

import com.fdkj.wywxjj.utils.math.BigDecimalUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 接口返回缴纳设置实体类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Jnsz {

    private String id;
    private String addtime;
    /**
     * 区域编码
     */
    private String fk_qybm;
    private String fk_xtglid;

    /**
     * 缴纳标准
     */
    private String jnbz;

    /**
     * 缴纳方式
     */
    private String jnfs;

    /**
     * 缴纳比例（%）
     */
    private String jnbl;

    /**
     * 缴纳金额(元没平米)
     */
    private String jnje;

    /**
     * 明细
     */
    private List<Jnsz_ls> list;

    /**
     * 根据合同金额计算
     *
     * @param htje 合同金额
     * @return res
     */
    public String calacMoneyByHtje(String htje) {
        return BigDecimalUtil.multiplyAndScale(htje.trim(), this.getJnbl().trim()).toPlainString();
    }

    /**
     * 根据建筑面积计算
     *
     * @param jzmj 建筑面积
     * @return res
     */
    public String calacMoneyByJzmj(String jzmj, String lc) {
        List<Jnsz_ls> jnsz_ls = this.getList();
        //1. 判断缴纳标准
        String jnbz = this.getJnbz();
        switch (jnbz) {
            case "不固定":
            case "按统一标准缴纳":
                return BigDecimalUtil.multiplyAndScale(jzmj.trim(), this.getJnje().trim()).toPlainString();
            case "按面积大小缴纳":
                for (Jnsz_ls jnsz_l : jnsz_ls) {
                    //起始面积
                    String qsmj = jnsz_l.getQsmj();
                    //结束面积
                    String jsmj = jnsz_l.getJsmj();
                    if (BigDecimalUtil.compareTo(jzmj, qsmj) == 1 && BigDecimalUtil.compareTo(jzmj, jsmj) < 1) {
                        //在范围内，计算金额
                        return BigDecimalUtil.multiplyAndScale(jzmj.trim(), jnsz_l.getJnje().trim()).toPlainString();
                    }
                }
                break;
            case "按楼层高低缴纳":
                for (Jnsz_ls jnsz_l : jnsz_ls) {
                    //起始louceng
                    String qslc = jnsz_l.getQslc();
                    //结束面积
                    String jslc = jnsz_l.getJslc();
                    if (BigDecimalUtil.compareTo(lc, qslc) == 1 && BigDecimalUtil.compareTo(lc, jslc) < 1) {
                        //在范围内，计算金额
                        return BigDecimalUtil.multiplyAndScale(jzmj.trim(), jnsz_l.getJnje().trim()).toPlainString();
                    }
                }
                break;
            default:
                return null;
        }
        return null;
    }

    /**
     * 获取缴纳标准
     *
     * @param jzmj 建筑面积
     * @param lc   楼层
     * @return 缴纳标准
     */
    public String getJnBz_jzmj(String jzmj, String lc) {
        List<Jnsz_ls> jnsz_ls = this.getList();
        //1. 判断缴纳标准
        String jnbz = this.getJnbz();
        switch (jnbz) {
            case "不固定":
            case "按统一标准缴纳":
                return this.getJnbz();
            case "按面积大小缴纳":
                for (Jnsz_ls jnsz_l : jnsz_ls) {
                    //起始面积
                    String qsmj = jnsz_l.getQsmj();
                    //结束面积
                    String jsmj = jnsz_l.getJsmj();
                    if (BigDecimalUtil.compareTo(jzmj, qsmj) == 1 && BigDecimalUtil.compareTo(jzmj, jsmj) < 1) {
                        //在范围内，计算金额
                        return jnsz_l.getJnje();
                    }
                }
                break;
            case "按楼层高低缴纳":
                for (Jnsz_ls jnsz_l : jnsz_ls) {
                    //起始louceng
                    String qslc = jnsz_l.getQslc();
                    //结束面积
                    String jslc = jnsz_l.getJslc();
                    if (BigDecimalUtil.compareTo(lc, qslc) == 1 && BigDecimalUtil.compareTo(lc, jslc) < 1) {
                        //在范围内，计算金额
                        return jnsz_l.getJnje();
                    }
                }
                break;
            default:
                return null;
        }
        return null;
    }

}
