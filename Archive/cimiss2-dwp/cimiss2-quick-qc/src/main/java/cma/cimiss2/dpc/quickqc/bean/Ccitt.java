package cma.cimiss2.dpc.quickqc.bean;

/**
 * @description:
 * @author: When6passBye
 * @create: 2019-07-23 19:09
 **/
public class Ccitt {
    private String str;
    private double val;

    public Ccitt() {
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "Ccitt_record_s{" +
                "str='" + str + '\'' +
                ", val=" + val +
                '}';
    }
}
