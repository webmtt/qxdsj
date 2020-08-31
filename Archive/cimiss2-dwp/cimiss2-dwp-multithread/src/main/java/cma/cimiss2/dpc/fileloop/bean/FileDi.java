package cma.cimiss2.dpc.fileloop.bean;

import java.io.Serializable;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class FileDi.
 * 积压文件夹处理发送DI信息类
 */
public class FileDi implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The system. */
	// 业务系统名称
	private String system;
	
	/** The data class. */
	// 待处理的资料编码，CTS四级编码大类
	private String data_class;
	
	/** The dir name. */
	// 文件夹的唯一标识名称
	private String dir_name;
	
	/** The data date. */
	// 资料业务日期(yyyy-mm-dd HH)，NAS下资料目录下的时间子文件夹，多级用/连接
	private String data_date;
	
	/** The file num. */
	// 该四级编码大类的时间文件夹下的文件数
	private int file_num;
	
	/** The file size. */
	// 该四级编码大类的时间文件夹下的文件总大小，KB单位
	private float file_size;
	// 可选，具体文件名和大小，如
	// {“FILE1”:10,
	//	“FILE2”:20,
	//	…}

	/** The file list. */
	private Map<String, Long> file_list;
	
	/** The recorde time. */
	// 目录情况采集时间	RECORDE_TIME	如“2019-04-10 01:00”到分钟
	private String record_time;
	
	
	/**
	 * Instantiates a new file di.
	 */
	public FileDi() {
		setSystem("DPC");
	}
	
	/**
	 * Gets the system.
	 *
	 * @return the system
	 */
	public String getSystem() {
		return system;
	}
	
	/**
	 * Sets the system.
	 *
	 * @param system the new system
	 */
	public void setSystem(String system) {
		this.system = system;
	}
	
	/**
	 * Gets the data class.
	 *
	 * @return the data class
	 */
	public String getData_class() {
		return data_class;
	}
	
	/**
	 * Sets the data class.
	 *
	 * @param data_class the new data class
	 */
	public void setData_class(String data_class) {
		this.data_class = data_class;
	}
	
	/**
	 * Gets the dir name.
	 *
	 * @return the dir name
	 */
	public String getDir_name() {
		return dir_name;
	}
	
	/**
	 * Sets the dir name.
	 *
	 * @param dir_name the new dir name
	 */
	public void setDir_name(String dir_name) {
		this.dir_name = dir_name;
	}
	
	/**
	 * Gets the data date.
	 *
	 * @return the data date
	 */
	public String getData_date() {
		return data_date;
	}
	
	/**
	 * Sets the data date.
	 *
	 * @param data_date the new data date
	 */
	public void setData_date(String data_date) {
		this.data_date = data_date;
	}
	
	/**
	 * Gets the file num.
	 *
	 * @return the file num
	 */
	public int getFile_num() {
		return file_num;
	}
	
	/**
	 * Sets the file num.
	 *
	 * @param file_num the new file num
	 */
	public void setFile_num(int file_num) {
		this.file_num = file_num;
	}
	
	/**
	 * Gets the file size.
	 *
	 * @return the file size
	 */
	public float getFile_size() {
		return file_size;
	}
	
	/**
	 * Sets the file size.
	 *
	 * @param file_size the new file size
	 */
	public void setFile_size(Float file_size) {
		this.file_size = file_size;
	}
	
	/**
	 * Gets the file list.
	 *
	 * @return the file list
	 */
	public Map<String, Long> getFile_list() {
		return file_list;
	}
	
	/**
	 * Sets the file list.
	 *
	 * @param file_list the file list
	 */
	public void setFile_list(Map<String, Long> file_list) {
		this.file_list = file_list;
	}

	/**
	 * Gets the record time.
	 *
	 * @return the record time
	 */
	public String getRecord_time() {
		return record_time;
	}

	/**
	 * Sets the record time.
	 *
	 * @param record_time the new record time
	 */
	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}

	/**
	 * Sets the file size.
	 *
	 * @param file_size the new file size
	 */
	public void setFile_size(float file_size) {
		this.file_size = file_size;
	}
	
	
	
	
}
