package cma.cimiss2.dpc.indb.vo;

import java.util.List;

/**
 * <br>
 *
 * @author wuzuoqiang
 * @Title: TableConfig.java
 * @Package org.cimiss2.dwp.RADAR.bean
 * @Description: TODO(资料存储索引库和ATS表名配置)
 *
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月15日 下午7:29:16   wuzuoqiang    Initial creation.
 * 2018年01月18日 下午2:52:10   wufy    	   add sodDataType storyPath diSendFlag.
 * </pre>
 */
public class TableConfig {

    /**
     * RabbitMQ.queueName
     **/
    private String queueName;
    private String dataType;
    /**
     * moveFileType
     * 是否搬移文件	0：搬，1：不搬
     **/
    private String moveFileType;
    /**
     * fileloop
     * 程序处理方式 0：接收消息，1：目录轮询
     **/
    private String fileloop;
    private String storeType;
    private String indexTable;
    private String atsTable;
    private String sodDataType; //存储四级编码
    private String storyPath; //存储路径
    //	private List<PathInfo> storyPath; //存储路径
    private String diSendFlag; //是否发生DI标识
    private String splitRegex; //文件分割符
    private String retweetDir; //轮询目录
    private String newFileName; //重命名文件名

    public TableConfig() {

    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getMoveFileType() {
        return moveFileType;
    }

    public void setMoveFileType(String moveFileType) {
        this.moveFileType = moveFileType;
    }

    public String getFileloop() {
        return fileloop;
    }

    public void setFileloop(String fileloop) {
        this.fileloop = fileloop;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getIndexTable() {
        return indexTable;
    }

    public void setIndexTable(String indexTable) {
        this.indexTable = indexTable;
    }

    public String getAtsTable() {
        return atsTable;
    }

    public void setAtsTable(String atsTable) {
        this.atsTable = atsTable;
    }

    public String getSodDataType() {
        return sodDataType;
    }

    public void setSodDataType(String sodDataType) {
        this.sodDataType = sodDataType;
    }

//	public List<PathInfo> getStoryPath() {
//		return storyPath;
//	}
//	public void setStoryPath(List<PathInfo> storyPath) {
//		this.storyPath = storyPath;
//	}


    public String getStoryPath() {
        return storyPath;
    }

    public void setStoryPath(String storyPath) {
        this.storyPath = storyPath;
    }

    public String getDiSendFlag() {
        return diSendFlag;
    }

    public void setDiSendFlag(String diSendFlag) {
        this.diSendFlag = diSendFlag;
    }

    public String getSplitRegex() {
        return splitRegex;
    }

    public void setSplitRegex(String splitRegex) {
        this.splitRegex = splitRegex;
    }

    public String getRetweetDir() {
        return retweetDir;
    }

    public void setRetweetDir(String retweetDir) {
        this.retweetDir = retweetDir;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }


}
