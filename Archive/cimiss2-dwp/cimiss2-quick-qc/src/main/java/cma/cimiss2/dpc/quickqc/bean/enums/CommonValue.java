package cma.cimiss2.dpc.quickqc.bean.enums;

/**
 * ************************************
 * @EnumName: CommonValue
 * @Auther: dangjinhu
 * @Date: 2019/7/26 11:51
 * @Description: 存放固定参数值
 * @Copyright: All rights reserver.
 * ************************************
 */

public enum CommonValue {
    /**
     * 地温5cm-320cm
     */
    GST_5CM(0.05),
    GST_10CM(0.1),
    GST_15CM(0.15),
    GST_20CM(0.2),
    GST_40CM(0.4),
    GST_80CM(0.8),
    GST_160CM(1.6),
    GST_320CM(3.2),
    DDDINS_MIN(0),
    DDDINS_MAX(0),
    PRS_Sea7(7),
    PRS_Sea13(13),
    PRS_Sea30(30);

    private double value;

    CommonValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }


}
