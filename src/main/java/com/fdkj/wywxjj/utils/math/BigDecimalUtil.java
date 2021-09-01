package com.fdkj.wywxjj.utils.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal计算工具类
 *
 * @author wyt
 */
public class BigDecimalUtil {

    // 除法运算默认精度
    private static final int DEF_DIV_SCALE = 2;

    // 四舍五入默认精度
    private static final int DEF_SCALE = 2;

    private BigDecimalUtil() {
    }

    /**
     * 精确加法
     */
    public static BigDecimal add(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.add(b2);
    }

    /**
     * 精确加法
     */
    public static BigDecimal add(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2);
    }

    /**
     * 精确减法
     */
    public static BigDecimal subtract(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.subtract(b2);
    }

    /**
     * 精确减法
     */
    public static BigDecimal subtract(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.subtract(b2);
    }

    /**
     * 精确乘法
     */
    public static BigDecimal multiply(BigDecimal value1, BigDecimal value2) {
        return value1.multiply(value2);
    }

    /**
     * 精确乘法
     */
    public static BigDecimal multiply(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.multiply(b2);
    }

    /**
     * 精确乘法
     */
    public static BigDecimal multiply(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.multiply(b2);
    }

    /**
     * 乘法 四舍五入
     *
     * @param value1 v1
     * @param value2 v2
     * @param scale  精度
     * @return res
     */
    public static BigDecimal multiplyAndScale(BigDecimal value1, BigDecimal value2, int scale) {
        BigDecimal multiply = multiply(value1, value2);
        return scale(multiply, scale);
    }

    /**
     * 乘法 四舍五入 默认进度
     *
     * @param value1 v1
     * @param value2 v2
     * @return res
     */
    public static BigDecimal multiplyAndScale(BigDecimal value1, BigDecimal value2) {
        return multiplyAndScale(value1, value2, DEF_SCALE);
    }

    /**
     * 乘法 四舍五入
     *
     * @param value1 v1
     * @param value2 v2
     * @param scale  精度
     * @return res
     */
    public static BigDecimal multiplyAndScale(double value1, double value2, int scale) {
        BigDecimal multiply = multiply(value1, value2);
        return scale(multiply, scale);
    }

    /**
     * 乘法 四舍五入 默认精度
     *
     * @param value1 v1
     * @param value2 v2
     * @return res
     */
    public static BigDecimal multiplyAndScale(double value1, double value2) {
        return multiplyAndScale(value1, value2, DEF_SCALE);
    }

    /**
     * 乘法 四舍五入
     *
     * @param value1 v1
     * @param value2 v2
     * @param scale  精度
     * @return res
     */
    public static BigDecimal multiplyAndScale(String value1, String value2, int scale) {
        BigDecimal multiply = multiply(value1, value2);
        return scale(multiply, scale);
    }

    /**
     * 乘法 四舍五入 默认精度
     *
     * @param value1 v1
     * @param value2 v2
     * @return res
     */
    public static BigDecimal multiplyAndScale(String value1, String value2) {
        return multiplyAndScale(value1, value2, DEF_SCALE);
    }

    /**
     * 除法(四舍五入)
     *
     * @param value1 v1
     * @param value2 v2
     * @return res
     */
    public static BigDecimal divideHalfUp(double value1, double value2) {
        return divideHalfUp(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 除法(四舍五入)
     *
     * @param value1 v1
     * @param value2 v2
     * @return res
     */
    public static BigDecimal divideHalfUp(String value1, String value2) {
        return divideHalfUp(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 除法(四舍五入)
     *
     * @param value1 v1
     * @param value2 v2
     * @param scale  精度
     * @return res
     */
    public static BigDecimal divideHalfUp(BigDecimal value1, BigDecimal value2, int scale) {
        return value1.divide(value2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 除法(四舍五入)
     *
     * @param value1 v1
     * @param value2 v2
     * @param scale  精度
     * @return res
     */
    public static BigDecimal divideHalfUp(double value1, double value2, int scale) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.divide(b2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 除法(四舍五入)
     *
     * @param value1 v1
     * @param value2 v2
     * @param scale  精度
     * @return res
     */
    public static BigDecimal divideHalfUp(String value1, String value2, int scale) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 除法(直接舍位，不考虑任何进位舍位操作)
     *
     * @param value1 v1
     * @param value2 v2
     * @return res
     */
    public static BigDecimal divideDown(double value1, double value2) {
        return divideDown(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 除法(直接舍位，不考虑任何进位舍位操作)
     *
     * @param value1 v1
     * @param value2 v2
     * @return res
     */
    public static BigDecimal divideDown(String value1, String value2) {
        return divideDown(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 除法(直接舍位，不考虑任何进位舍位操作)
     *
     * @param value1 v1
     * @param value2 v2
     * @param scale  精度
     * @return res
     */
    public static BigDecimal divideDown(BigDecimal value1, BigDecimal value2, int scale) {
        return value1.divide(value2, scale);
    }

    /**
     * 除法(直接舍位，不考虑任何进位舍位操作)
     *
     * @param value1 v1
     * @param value2 v2
     * @param scale  精度
     * @return res
     */
    public static BigDecimal divideDown(double value1, double value2, int scale) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.divide(b2, scale, RoundingMode.DOWN);
    }

    /**
     * 除法(直接舍位，不考虑任何进位舍位操作)
     *
     * @param value1 v1
     * @param value2 v2
     * @param scale  精度
     * @return res
     */
    public static BigDecimal divideDown(String value1, String value2, int scale) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale, RoundingMode.DOWN);
    }

    /**
     * 保留n位小数(四舍五入)
     *
     * @param value v
     * @param scale 小数点后保留几位
     * @return res
     */
    public static BigDecimal scale(BigDecimal value, int scale) {
        return value.setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 保留n位小数(四舍五入)
     *
     * @param value v
     * @param scale 小数点后保留几位
     * @return res
     */
    public static BigDecimal scaleUp(double value, int scale) {
        return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 保留n位小数(四舍五入)
     *
     * @param value v
     * @param scale 小数点后保留几位
     * @return res
     */
    public static BigDecimal scaleUp(String value, int scale) {
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 比较相等
     *
     * @param value1 v1
     * @param value2 v2
     * @return res
     */
    public static boolean equalTo(BigDecimal value1, BigDecimal value2) {
        return 0 == value1.compareTo(value2);
    }

    /**
     * 比较相等
     *
     * @param value1 v1
     * @param value2 v2
     * @return res
     */
    public static boolean equalTo(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return 0 == b1.compareTo(b2);
    }

    /**
     * 比较相等
     *
     * @param value1 v1
     * @param value2 v2
     * @return res
     */
    public static boolean equalTo(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return 0 == b1.compareTo(b2);
    }

    /**
     * 比大小
     *
     * @param value1 v1
     * @param value2 v2
     * @return res (-1 小于，0 等于，1 大于 )
     */
    public static int compareTo(BigDecimal value1, BigDecimal value2) {
        return value1.compareTo(value2);
    }

    /**
     * 比大小
     *
     * @param value1 v1
     * @param value2 v2
     * @return res (-1 小于，0 等于，1 大于 )
     */
    public static int compareTo(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return compareTo(b1, b2);
    }

    /**
     * 比大小
     *
     * @param value1 v1
     * @param value2 v2
     * @return res (-1 小于，0 等于，1 大于 )
     */
    public static int compareTo(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return compareTo(b1, b2);
    }
}
