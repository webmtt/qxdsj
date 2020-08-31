package cma.cimiss2.dpc.decoder.bean.surf;

/**
 * 水利部河道水情资料Z.2001.0003.R001文件  实体类
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年3月28日 17:33:00   fengmingyang    Initial creation.
 * </pre>
 * 
 * @author fengmingyang
 * @version 0.0.1
 */
public class HdsqBean {

    /**
     * NO: 1 <br>
     * nameCN: 文件信息 <br>
     * unit: <br>
     * descriptionCN: char（8），不包括文件后缀 <br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
     */
    private String fileInfo = "999998";
    
    /**
     * NO: 2 <br>
     * nameCN: 观测时间 <br>
     * unit: <br>
     * descriptionCN:  yyyy-MM-dd HH:mm:ss<br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
     */
    private String obsTime = "999998";
    
    /**
     * NO: 3 <br>
     * nameCN: 站号 <br>
     * unit: <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
     */
    private String stationNumber;

    /**
     * NO: 4 <br>
     * nameCN: 站名 <br>
     * unit: <br>
     * descriptionCN: <br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
     */
    private String stationName = "999998";

    /**
     * NO: 5 <br>
     * nameCN: 时间 <br>
     * unit: <br>
     * descriptionCN: 记录实时时间<br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
     */
    private String time = "999998";
    /**
     * NO: 6  <br>
     * nameCN: 水位 <br>
     * unit: 未知<br>
     * descriptionCN: 水位<br>
     * decode rule: 直接取值<br>
     * field rule: 直接赋值
     */
    private Double waterLevel = 999998.0;

    /**
     * NO: 7  <br>
     * nameCN: 流量 <br>
     * unit: C<br>
     * descriptionCN: 河道流量<br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
     */
    private Double traffic = 999998.0;

    /**
     * NO: 8 <br>
     * nameCN: 超警 <br>
     * unit: <br>
     * descriptionCN:<br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
     */
    private Double chaoJing = 999998.0;

    /**
     * NO: 9  <br>
     * nameCN: 水势 <br>
     * unit: <br>
     * descriptionCN: 涨、落、平<br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
     */
    private String shuiShi = "999998";

    /**
     * NO: 10  <br>
     * nameCN: 入流 <br>
     * unit: <br>
     * descriptionCN:<br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
     */
    private Double inflow = 999998.0;
    
    /**
     * NO: 11  <br>
     * nameCN: 出流 <br>
     * unit: <br>
     * descriptionCN:<br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
     */
    private Double outflow = 999998.0;
    
    /**
     * NO: 12  <br>
     * nameCN: 蓄水量 <br>
     * unit: <br>
     * descriptionCN:<br>
     * decode rule:直接取值<br>
     * field rule: 直接赋值
     */
    private Double pondage = 999998.0;
    
    
    public HdsqBean() {
        super();
    }

    /**
     * nameCN: 文件信息 <br>
     * unit: <br>
     * descriptionCN: char（8），不包括文件后缀 <br>
     */
    public String getFileInfo() {
        return fileInfo;
    }
    /**
     * nameCN: 文件信息 <br>
     * unit: <br>
     * descriptionCN: char（8），不包括文件后缀 <br>
     */
    public void setFileInfo(String fileInfo) {
        this.fileInfo = fileInfo;
    }

    /**
     * nameCN: 观测时间 <br>
     * unit: <br>
     * descriptionCN:  yyyy-MM-dd HH:mm:ss<br>
     */
    public String getObsTime() {
        return obsTime;
    }

    /**
     * nameCN: 观测时间 <br>
     * unit: <br>
     * descriptionCN:  yyyy-MM-dd HH:mm:ss<br>
     */
    public void setObsTime(String obsTime) {
        this.obsTime = obsTime;
    }

    /**
     * nameCN: 站号 <br>
     * unit: <br>
     * descriptionCN: <br>
     */
    public String getStationNumber() {
        return stationNumber;
    }

    /**
     * nameCN: 站号 <br>
     * unit: <br>
     * descriptionCN: <br>
     */
    public void setStationNumber(String stationNumber) {
        this.stationNumber = stationNumber;
    }

    /**
     * nameCN: 站名 <br>
     * unit: <br>
     * descriptionCN: <br>
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * nameCN: 站名 <br>
     * unit: <br>
     * descriptionCN: <br>
     */
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    /**
     * nameCN: 记录时间 <br>
     * unit: <br>
     * descriptionCN: <br>
     */
    public String getTime() {
        return time;
    }

    /**
     * nameCN: 记录时间 <br>
     * unit: <br>
     * descriptionCN: <br>
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * nameCN: 水位 <br>
     * unit: 未知<br>
     * descriptionCN: <br>
     */
    public double getWaterLevel() {
        return waterLevel;
    }

    /**
     * nameCN: 水位 <br>
     * unit: 未知<br>
     * descriptionCN: <br>
     */
    public void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }

    /**
     * nameCN: 流量 <br>
     * unit: <br>
     * descriptionCN: <br>
     */
    public double getTraffic() {
        return traffic;
    }

    /**
     * nameCN: 流量 <br>
     * unit: <br>
     * descriptionCN:<br>
     */
    public void setTraffic(double traffic) {
        this.traffic = traffic;
    }

    /**
     * nameCN: 超警 <br>
     * unit: <br>
     * descriptionCN:<br>
     */
    public double getChaoJing() {
        return chaoJing;
    }

    /**
     * nameCN: 超警 <br>
     * unit: <br>
     * descriptionCN:<br>
     */
    public void setChaoJing(double chaoJing) {
        this.chaoJing = chaoJing;
    }

    /**
     * nameCN: 水势 <br>
     * unit: <br>
     * descriptionCN: 涨、落、平<br>
     */
    public String getShuiShi() {
        return shuiShi;
    }

    /**
     * nameCN: 水势 <br>
     * unit: <br>
     * descriptionCN: 涨、落、平<br>
     */
    public void setShuiShi(String shuiShi) {
        this.shuiShi = shuiShi;
    }

    /**
     * nameCN: 入流 <br>
     * unit: <br>
     * descriptionCN:<br>
     */
    public double getInflow() {
        return inflow;
    }

    /**
     * nameCN: 入流 <br>
     * unit: <br>
     * descriptionCN:<br>
     */
    public void setInflow(double inflow) {
        this.inflow = inflow;
    }

    /**
     * nameCN: 出流 <br>
     * unit: <br>
     * descriptionCN:<br>
     */
    public double getOutflow() {
        return outflow;
    }

    /**
     * nameCN: 出流 <br>
     * unit: <br>
     * descriptionCN:<br>
     */
    public void setOutflow(double outflow) {
        this.outflow = outflow;
    }

    /**
     * nameCN: 蓄水量 <br>
     * unit: <br>
     * descriptionCN:<br>
     */
    public double getPondage() {
        return pondage;
    }

    /**
     * nameCN: 蓄水量 <br>
     * unit: <br>
     * descriptionCN:<br>
     */
    public void setPondage(double pondage) {
        this.pondage = pondage;
    }

    @Override
    public String toString() {
        return "HdsqBean [fileInfo=" + fileInfo + ", obsTime=" + obsTime + ", stationNumber="
                + stationNumber + ", stationName=" + stationName + ", time=" + time
                + ", waterLevel=" + waterLevel + ", traffic=" + traffic + ", chaoJing=" + chaoJing
                + ", shuiShi=" + shuiShi + ", inflow=" + inflow + ", outflow=" + outflow
                + ", pondage=" + pondage + "]";
    }

}
