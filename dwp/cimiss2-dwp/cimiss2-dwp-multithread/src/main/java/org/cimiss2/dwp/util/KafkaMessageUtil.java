package org.cimiss2.dwp.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class KafkaMessageUtil {
	private static String TypeTag;//是否为四级资料编码：1 是；0否（必填）
	private static String TYPE;//TYPETag =1四级资料编码;TYPETag = 0 自定义资料编码（必填）
	private static String OTime;//资料时间（必填）
	private static String InTime;//资料到达时间 YYYY-MM-DD HH:MI:SS.sss
	private static String STime;//起报时间
	private static String Aging;//预报时效
	private static String Step;//时间间隔
	private static String DataType;//资料类型
	private static String FileName;//资料文件名（必填）
	private static String NasPath;//原始文件所在路径（必填）
	private static String Format;//文件格式
	private static String FileSize;//文件大小
	private static String MD5;//校验码
	
	/**
	 * 转换byte[]类型数据
	 * @param byteValues
	 */
	public static void parse(byte[] byteValues)
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
	 * 转换String类型数据
	 * @param js
	 */
	public static void parse(String js)
	{
		Map<String, Object> map = JSONObject.parseObject(js);
		mapAssign(map);
	}
	
	private static void mapAssign(Map<String, Object> map) {
		
		TypeTag = (String) map.get("TypeTag");
		TYPE = (String) map.get("TYPE");
		OTime = (String) map.get("OTime");
		InTime = (String) map.get("InTime");
		STime = (String) map.get("STime");
		Aging = (String) map.get("Aging");
		Step = (String) map.get("Step");
		DataType = (String) map.get("DataType");
		FileName = (String) map.get("FileName");
		NasPath = (String) map.get("NasPath");
		NasPath.replace("\\", "/");
		Format = (String) map.get("Format");
		FileSize = (String) map.get("FileSize");
		MD5 = (String) map.get("MD5");
	}
	
	public static String getTypeTag()
	{
		return TypeTag;
	}
	
	public static String getTYPE()
	{
		return TYPE;
	}
	
	public static String getOTime()
	{
		return OTime;
	}
	
	public static String getInTime()
	{
		return InTime;
	}
	
	public static String getSTime()
	{
		return STime;
	}
	
	public static String getAging()
	{
		return Aging;
	}
	
	public static String getStep()
	{
		return Step;
	}
	
	public static String getDataType()
	{
		return DataType;
	}
	
	public static String getFileName()
	{
		return FileName;
	}
	
	public static String getNasPath()
	{
		return NasPath;
	}
	
	public static String getFormat()
	{
		return Format;
	}
	
	public static String getFileSize()
	{
		return FileSize;
	}
	
	public static String getMD5()
	{
		return MD5;
	}
	
	public static String getFilePathAndFileName(){
		return NasPath + "/" + FileName;
	}
}