package cma.cimiss2.dpc.decoder.bean.upar;

/**
 * ************************************
 * @ClassName: WindData
 * @Auther: dangjinhu
 * @Date: 2019/3/20 14:02
 * @Description: 实时、半小时平均、一小时平均的采样高度产品数据文件实体,第3段数据
 * @Copyright: All rights reserver.
 * ************************************
 */

public class WindData {

    /**
     * 采样高度
     * 五位整数
     */
    private int height;

    /**
     * 水平风向（度）
     * 三位整数,一位小数
     * 需质控
     */
    private double windDirection;

    /**
     * 水平风速（米/秒）
     * 三位整数,一位小数
     * 需质控
     */
    private double windSpeed;

    /**
     * 垂直风速（米/秒）
     * 第一位为符号位
     * 垂直风向下为正,向上为负
     * 三位整数,一位小数
     */
    private double VwindSpeed;

    /**
     * 水平方向可信度
     * 三位整数
     * 单位为%,为0~100的整数
     */
    private int Hcredibility;

    /**
     * 垂直方向可信度
     * 三位整数
     * 单位为%,为0~100的整数
     */
    private int Vcredibility;

    /**
     * 垂直方向Cn2,例如2.6e-024
     */
    private String cn2;

    public WindData() {
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getVwindSpeed() {
        return VwindSpeed;
    }

    public void setVwindSpeed(double vwindSpeed) {
        VwindSpeed = vwindSpeed;
    }

    public int getHcredibility() {
        return Hcredibility;
    }

    public void setHcredibility(int hcredibility) {
        Hcredibility = hcredibility;
    }

    public int getVcredibility() {
        return Vcredibility;
    }

    public void setVcredibility(int vcredibility) {
        Vcredibility = vcredibility;
    }

    public String getCn2() {
        return cn2;
    }

    public void setCn2(String cn2) {
        this.cn2 = cn2;
    }

    @Override
    public String toString() {
        return "ProductData{" +
                "height=" + height +
                ", windDirection=" + windDirection +
                ", windSpeed=" + windSpeed +
                ", VwindSpeed=" + VwindSpeed +
                ", Hcredibility=" + Hcredibility +
                ", Vcredibility=" + Vcredibility +
                ", cn2='" + cn2 + '\'' +
                '}';
    }
}
