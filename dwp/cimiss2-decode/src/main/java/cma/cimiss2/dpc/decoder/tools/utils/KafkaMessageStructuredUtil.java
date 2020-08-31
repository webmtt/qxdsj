package cma.cimiss2.dpc.decoder.tools.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class KafkaMessageStructuredUtil {
	
	private String TypeTag;//是否为四级资料编码：1 是；0否（必填）
	private String TYPE;//TYPETag =1四级资料编码;TYPETag = 0 自定义资料编码（必填）
	private String IIIII;//台站编号
	private String CCCC;//编报中心
	private String OTime;//资料时间 YYYY-MM-DD HH:MI:SS
	private String InTime;//资料接入时间 YYYY-MM-DD HH:MI:SS.sss
	private String STime;//接口发送时间 YYYY-MM-DD HH:MI:SS.sss
	private String FileType;//文件类型，O为观测类；R为状态类
	private String DataType;//资料类型
	private String FileName;//资料文件名（必填）
	private String BBB;//更正标识eg：CCX。数据更正标识，可选标志，CC为固定代码；x取值为A～X；
	private String PQC;//质量控制标识 =1 已质控 =0 未质控
	private String NasPath;//原始文件所在路径（必填）
	private String Format;//文件格式
	private String FileSize;//文件大小
	private String MD5;//校验码
	private String Lenth;//数据块长度
	
	/**
	 * 转换byte[]类型数据
	 * @param byteValues
	 */
	public void parseByte(byte[] byteValues)
	{
		String strData = "";
		try {
			strData = new String(byteValues, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Map<String, Object> map = JSONObject.parseObject(strData);
		mapAssign(map);
	}

	/**
	 * 转换Map类型数据
	 * @param map
	 */
	public void parseMap(Map<String, Object> map)
	{
		mapAssign(map);
	}
	
	/**
	 * 转换String类型数据
	 * @param js
	 */
	public void parseJson(String js)
	{
		Map<String, Object> map = JSONObject.parseObject(js);
		mapAssign(map);
	}
	
	/**
	 * 给成员变量赋值
	 * @param map
	 */
	private void mapAssign(Map<String, Object> map) {
		
		TypeTag = (String) map.get("TypeTag");
		TYPE = (String) map.get("TYPE");
		IIIII = (String) map.get("IIIII");
		CCCC = (String) map.get("CCCC");
		OTime = (String) map.get("OTime");
		InTime = (String) map.get("InTime");
		STime = (String) map.get("STime");
		FileType = (String) map.get("FileType");
		DataType = (String) map.get("DataType");
		FileName = (String) map.get("FileName");
		BBB = (String) map.get("BBB");
		PQC = (String) map.get("PQC");
		NasPath = (String) map.get("NasPath");
		Format = (String) map.get("Format");
		FileSize = (String) map.get("FileSize");
		MD5 = (String) map.get("MD5");
		Lenth = (String) map.get("Lenth");
	}

	public String getTypeTag() {
		return TypeTag;
	}

	public String getTYPE() {
		return TYPE;
	}

	public String getIIIII() {
		return IIIII;
	}

	public String getCCCC() {
		return CCCC;
	}

	public String getOTime() {
		return OTime;
	}

	public String getInTime() {
		return InTime;
	}

	public String getSTime() {
		return STime;
	}

	public String getFileType() {
		return FileType;
	}

	public String getDataType() {
		return DataType;
	}

	public String getFileName() {
		return FileName;
	}

	public String getBBB() {
		return BBB;
	}

	public String getPQC() {
		return PQC;
	}

	public String getNasPath() {
		return NasPath;
	}

	public String getFormat() {
		return Format;
	}

	public String getFileSize() {
		return FileSize;
	}

	public String getMD5() {
		return MD5;
	}

	public String getLenth() {
		return Lenth;
	}
}