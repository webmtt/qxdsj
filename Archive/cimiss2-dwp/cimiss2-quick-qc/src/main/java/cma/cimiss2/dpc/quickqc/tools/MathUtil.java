package cma.cimiss2.dpc.quickqc.tools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ************************************
 *
 * @ClassName: MathUtil
 * @Auther: dangjinhu
 * @Date: 2019/7/24 14:24
 * @Description: 高精度计算util
 * @Copyright: All rights reserver.
 * ************************************
 */

public class MathUtil {

    /**
     * 加法运算
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return num1 + num2
     */
    public static double add(double num1, double num2) {
        BigDecimal b1 = BigDecimal.valueOf(num1);
        BigDecimal b2 = BigDecimal.valueOf(num2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 减法运算
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return num1 - num2
     */
    public static double sub(double num1, double num2) {
        BigDecimal b1 = BigDecimal.valueOf(num1);
        BigDecimal b2 = BigDecimal.valueOf(num2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 乘法运算
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return num1 * num2
     */
    public static double mul(double num1, double num2) {
        BigDecimal b1 = BigDecimal.valueOf(num1);
        BigDecimal b2 = BigDecimal.valueOf(num2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 除法运算
     *
     * @param num1 数字1
     * @param num2 数字2
     * @param len  保留小数点
     * @return num1 / num2
     */
    public static double div(double num1, double num2, int len) {
        BigDecimal b1 = BigDecimal.valueOf(num1);
        BigDecimal b2 = BigDecimal.valueOf(num2);
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 进行四舍五入
     *
     * @param num 数字
     * @param len 保留小数点
     * @return 计算结果
     */
    public static String round(double num, int len) {
        BigDecimal b1 = BigDecimal.valueOf(num);
        BigDecimal b2 = new BigDecimal(1);
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 求最大值
     *
     * @param obseArray
     * @param element
     * @return
     */
    public static double getMaxDouble(List<Map<String, Double>> obseArray, String element) {
        List<Double> list = new ArrayList();
        for (Map<String, Double> map : obseArray) {
            list.add(map.get(element));
        }
        return Collections.max(list);
    }

    /**
     * 求最小值
     *
     * @param obseArray
     * @param element
     * @return
     */
    public static double getMinDouble(List<Map<String, Double>> obseArray, String element) {
        List<Double> list = new ArrayList();
        for (Map<String, Double> map : obseArray) {
            list.add(map.get(element));
        }
        return Collections.min(list);
    }

    /**
     * 求两个值得最小值
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return 小值
     */
    public static double getMinDouble(double num1, double num2) {
        if (num1 > num2) {
            return num2;
        } else {
            return num1;
        }
    }

    /**
     * 求两个值得最大值
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return 大值
     */
    public static double getMaxDouble(double num1, double num2) {
        if (num1 > num2) {
            return num1;
        } else {
            return num2;
        }
    }

}
