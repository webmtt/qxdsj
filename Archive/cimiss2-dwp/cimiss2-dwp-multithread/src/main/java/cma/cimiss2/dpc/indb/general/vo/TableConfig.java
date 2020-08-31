package cma.cimiss2.dpc.indb.general.vo;
import java.util.List;
/**
 * 
 * <br>
 * @Title:  TableConfig.java   
 * @Package org.cimiss2.dwp.RADAR.bean   
 * @Description:    TODO(资料存储索引库和ATS表名配置)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月15日 下午7:29:16   wuzuoqiang    Initial creation.
 * 2018年01月18日 下午2:52:10   wufy    	   add sodDataType storyPath diSendFlag.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class TableConfig {
	
	private String dataType;
	private String storeType;
	private String indexTable;
	private String atsTable;
	private String sodDataType; //存储四级编码
	private String ctsDataType = null;
	private List<PathInfo> storyPath; //存储路径
	private String diSendFlag; //是否发生DI标识
	private String splitRegex; //文件分割符
	private String retweetDir; //轮询目录
	private String newFileName; //重命名文件名
	private String stationCode;   // 站号
	
	public TableConfig(){
		
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

	public List<PathInfo> getStoryPath() {
		return storyPath;
	}
	public void setStoryPath(List<PathInfo> storyPath) {
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
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public String getCtsDataType() {
		return ctsDataType;
	}
	public void setCtsDataType(String ctsDataType) {
		this.ctsDataType = ctsDataType;
	}
	
	
}
