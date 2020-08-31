package out.ret2excel;

/**
 * 测试文件输出结果Bean
 *
 * @Author: When6passBye
 * @Date: 2019-08-28 15:13
 **/
public class ResultBean {

    /**
     * id
     */
    private String id;
    /**
     * 站点编码
     */
    private String stationCode;
    /**
     * 观测时间
     */
    private String obsTime;
    /**
     * 观测元素
     */
    private String element;
    /**
     * 观测值
     */
    private String obsVal;
    /**
     * 缺测检查质控结果
     */
    private String missing;
    /**
     * 界限检查质控结果
     */
    private String limit;
    /**
     * 范围检查质控结果
     */
    private String range;
    /**
     * 一致性检查质控结果
     */
    private String internal;
    /**
     * 文件级质控结果
     */
    private String fileCode;
    /**
     * 和期望值对比结果
     */
    private String compareResult;
    /**
     * 数据源文件名
     */
    private String fileName;
    /**
     * 花费时间
     */
    private String spendTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getObsTime() {
        return obsTime;
    }

    public void setObsTime(String obsTime) {
        this.obsTime = obsTime;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getObsVal() {
        return obsVal;
    }

    public void setObsVal(String obsVal) {
        this.obsVal = obsVal;
    }

    public String getMissing() {
        return missing;
    }

    public void setMissing(String missing) {
        this.missing = missing;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getInternal() {
        return internal;
    }

    public void setInternal(String internal) {
        this.internal = internal;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getCompareResult() {
        return compareResult;
    }

    public void setCompareResult(String compareResult) {
        this.compareResult = compareResult;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(String spendTime) {
        this.spendTime = spendTime;
    }

    public String[] getStrValues() {
        return new String[]{id, stationCode, obsTime, element, obsVal, missing, limit, range, internal, fileCode, compareResult, fileName, spendTime};
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "id='" + id + '\'' +
                ", stationCode='" + stationCode + '\'' +
                ", obsTime='" + obsTime + '\'' +
                ", element='" + element + '\'' +
                ", obsVal='" + obsVal + '\'' +
                ", missing='" + missing + '\'' +
                ", limit='" + limit + '\'' +
                ", range='" + range + '\'' +
                ", internal='" + internal + '\'' +
                ", fileCode='" + fileCode + '\'' +
                ", compareResult='" + compareResult + '\'' +
                ", fileName='" + fileName + '\'' +
                ", spendTime='" + spendTime + '\'' +
                '}';
    }
}
