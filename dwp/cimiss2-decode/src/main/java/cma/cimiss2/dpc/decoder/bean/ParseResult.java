package cma.cimiss2.dpc.decoder.bean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * parse result of Test file or message with meteo data. <br>
 * 气象数据文件（或消息）解码结果
 * 
 * @author shevawen
 *
 * @param <T>
 *            具体的数据结构类，如地面、高空等
 */
public class ParseResult<T> implements Serializable {

	/** The is success. */
	private boolean isSuccess;

	/** The parse info. */
	private ParseInfo parseInfo;

	/** 数据列表，每条报文一个数据项. */
	private List<T> data = new LinkedList<T>();
	
	/** 错误信息列表，每条错误报文一个数据项. */
	private List<ReportError> error = new LinkedList<ReportError>();
	
	/** 原始报告. */
	@SuppressWarnings("rawtypes")
	private List<ReportInfo> reports = new LinkedList<ReportInfo>();

	/**
	 * Instantiates a new parses the result.
	 *
	 * @param isSuccess the is success
	 */
	public ParseResult(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return data.size() + error.size();
	}
	
	/**
	 * Put.
	 *
	 * @param reoprtInfo the reoprt info
	 */
	public void put(ReportInfo<?> reoprtInfo) {
		this.reports.add(reoprtInfo);
	}
	
	/**
	 * Gets the reports.
	 *
	 * @return the reports
	 */
	@SuppressWarnings("rawtypes")
	public List<ReportInfo> getReports(){
		return this.reports;
	}

	/**
	 * Put.
	 *
	 * @param data the data
	 */
	public void put(T data) {
		this.data.add(data);
	}

	/**
	 * Put.
	 *
	 * @param error the error
	 */
	public void put(ReportError error) {
		this.error.add(error);
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return data.size() + error.size() > 0;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(List<T> data) {
		this.data = data;
	}

	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	public List<ReportError> getError() {
		return error;
	}

	/**
	 * Sets the error.
	 *
	 * @param error the new error
	 */
	public void setError(List<ReportError> error) {
		this.error = error;
	}
	
	/**
	 * 文件（或消息）整体错误信息
	 * TODO 逐步补充完整.
	 */
	public enum ParseInfo {
		
		/** The illegal form. */
		ILLEGAL_FORM(1, "非法格式"), 
 /** The file not exsit. */
 FILE_NOT_EXSIT(2, "文件不存在"), 
 /** The empty file. */
 EMPTY_FILE(2, "空文件");
		
		/** The code. */
		int code;

		/** The description. */
		String description;

		/**
		 * Instantiates a new parses the info.
		 *
		 * @param code the code
		 * @param description the description
		 */
		ParseInfo(int code, String description) {
			this.code = code;
			this.description = description;
		}
		
		/**
		 * Gets the parses the info by code.
		 *
		 * @param code the code
		 * @return the parses the info by code
		 */
		public static ParseInfo getParseInfoByCode(int code) {
			for (ParseInfo i : ParseInfo.values()) {
				if (i.getCode() == code) {
					return i;
				}
			}
			throw new IllegalArgumentException();
		}

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return this.name();
		}
		
		/**
		 * Gets the code.
		 *
		 * @return the code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * Sets the code.
		 *
		 * @param code the new code
		 */
		public void setCode(int code) {
			this.code = code;
		}

		/**
		 * Gets the description.
		 *
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * Sets the description.
		 *
		 * @param description the new description
		 */
		public void setDescription(String description) {
			this.description = description;
		}

	}




	/**
	 * Checks if is success.
	 *
	 * @return true, if is success
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * Sets the success.
	 *
	 * @param isSuccess the new success
	 */
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 * Gets the parses the info.
	 *
	 * @return the parses the info
	 */
	public ParseInfo getParseInfo() {
		return parseInfo;
	}

	/**
	 * Sets the parses the info.
	 *
	 * @param parseInfo the new parses the info
	 */
	public void setParseInfo(ParseInfo parseInfo) {
		this.parseInfo = parseInfo;
	}

}
