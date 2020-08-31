package cma.cimiss2.dpc.decoder.grib;

import java.io.File;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 数值预报解码配置辅助工具类,主要提供以下功能: 
 * 1:GRIB1,根据GRIB1的参数编码值查找具体的要素英文代码；根据层次类别,检索自定义要素英文代码;
 * 2:GRIB2,根据GRIB2的科目、分类和参数编码值查找具体的要素英文代码;根据层次类别,检索自定义要素英文代码;
 * 3:根据经纬度范围,确定资料所属的区域代码;
 *
 * @author zhoujun_sun@tom.com 
 */
public class GribDecoderConfigureHelper {
	//private final Logger								log								= LoggerFactory
	//		.getLogger(GribDecoderConfigureHelper.class);
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	public final static String					GRIB1_ROOT_NODE_KEY				= "grib1";
	public final static String					GRIB1_COMMONS_NODE_KEY			= "common";
	public final static String					GRIB2_ROOT_NODE_KEY				= "grib2";
	public final static String					GRIB2_COMMONS_NODE_KEY			= "common";
	public final static String					AREA_NODE_KEY					= "area";
	public final static String					GRIB_SHORTNAME_NODE_KEY			= "shortName";
	public final static String					GRIB_DESCRIPTION_NODE_KEY		= "description";
	public final static String					GRIB_SUFFIX_PROCESS_NODE_KEY	= "suffixProcess";
	public final static String 					cimiss2_dwp_decode_grib_info    = "config/cimiss2_dwp_decode_grib_info.json";
	private static GribDecoderConfigureHelper	instance						= null;
	private Configuration						configurationRoot				= null;

	/**
	 * 私有构造函数
	 */
	private GribDecoderConfigureHelper() {
		if (null == configurationRoot) {
			configurationRoot = Configuration.from(new File(cimiss2_dwp_decode_grib_info));
		}
	}

	/**
	 * 单实例模式
	 * 
	 * @return
	 */
	public static GribDecoderConfigureHelper instance() {
		if (null == instance) {
			instance = new GribDecoderConfigureHelper();
		}
		return instance;
	}

	/**
	 * 根据资料四级编码检索Grib1的相关定义
	 * 
	 * @param dataId
	 *            CIMISS系统中定义的资料四级编码
	 * @param grig1ParameterNumber
	 *            参数编号
	 * @param levelType
	 *            层次类型
	 * @return
	 */
	public String getGrib1ElementShortName(String dataId, int grib1ParameterNumber, int levelType) {
		dataId = dataId.replace(".", "_");
		// 检索自定义部分
		String shortName = getGrib1ElementShortNameFromCommonsAndCustomers(dataId, grib1ParameterNumber, levelType);
		if (null == shortName) {
			dataId = GRIB1_COMMONS_NODE_KEY;
			shortName = getGrib1ElementShortNameFromCommonsAndCustomers(dataId, grib1ParameterNumber, levelType);
		}
		return shortName;
	}

	/**
	 * 检索Grib1配置里面的数值预报相关定义
	 * 
	 * @param commonsOrDataId
	 *            commons定义段或数值预报自定义段
	 * @param grib1PrameterNumber
	 *            参数编号
	 * @param levelType
	 *            层次类型
	 * @return
	 */
	private String getGrib1ElementShortNameFromCommonsAndCustomers(String commonsOrDataId, int grib1PrameterNumber,
			int levelType) {
		String shortName = null;
		String suffixProcess = null;
		// JSONPATH:grib1.commons.grib1PrameterNumber检索配置信息
		String parameterJsonPath = String.format("%s.%s.%d", GRIB1_ROOT_NODE_KEY, commonsOrDataId, grib1PrameterNumber);
		// JSONPATH:grib1.commons.grib1ParameterNumber.suffixProcess.levelType
		String suffixJsonPath = String.format("%s.%s.%d.%s.%d", GRIB1_ROOT_NODE_KEY, commonsOrDataId,
				grib1PrameterNumber, GRIB_SUFFIX_PROCESS_NODE_KEY, levelType);
		Map<String, Object> obj = configurationRoot.getMap(parameterJsonPath);
		if (null != obj) {
			shortName = (String) obj.get(GRIB_SHORTNAME_NODE_KEY);
			suffixProcess = configurationRoot.getString(suffixJsonPath);
			return (null != suffixProcess) ? shortName + "_" + suffixProcess : shortName;
		} else {
			logger.warn("配置文件中Grib1区域未找到对应项[jsonPah={"+parameterJsonPath+"}],请检查Json配置文件:");
			return null;
		}
	}

	/**
	 * 根据科目、分类和参数编码获取要素的英文名称
	 * 
	 * @param dataId
	 *            CIMISS系统中资料的四级编码
	 * @param discipline
	 *            科目
	 * @param catagroy
	 *            分类
	 * @param grib2ParameterNumber
	 *            参数编号
	 * @param levelType
	 *            层次类型
	 * @return
	 */
	public String getGrib2ElementShortName(String dataId, int discipline, int catagroy, int grib2ParameterNumber,
			int levelType) {
		dataId = dataId.replace(".", "_");
		// 检索自定义部分
		String shortName = getGrib2ElementShortNameFromCommonsAndCustomers(dataId, discipline, catagroy,
				grib2ParameterNumber, levelType);
		if (null == shortName) {
			dataId = GRIB2_COMMONS_NODE_KEY;
			shortName = getGrib2ElementShortNameFromCommonsAndCustomers(dataId, discipline, catagroy,
					grib2ParameterNumber, levelType);
		}
		return shortName;
	}

	/**
	 * 
	 * 根据科目、分类和参数编码获取要素的英文名称
	 * 
	 * @param commonsOrDataId
	 *            commons定义段或数值预报自定义段
	 * @param discipline
	 *            科目
	 * @param catagroy
	 *            分类
	 * @param grib2ParameterNumber
	 *            参数编号
	 * @param levelType
	 *            层次类型
	 * @return
	 */
	private String getGrib2ElementShortNameFromCommonsAndCustomers(String commonsOrDataId, int discipline, int catagroy,
			int grib2ParameterNumber, int levelType) {
		String shortName = null;
		String suffixProcess = null;
		// JSONPATH:grib2.commons|dataId.discipline_catagroy_grib2ParameterNumber检索配置信息
		String parameterJsonPath = String.format("%s.%s.%d_%d_%d", GRIB2_ROOT_NODE_KEY, commonsOrDataId, discipline,
				catagroy, grib2ParameterNumber);
		// JSONPATH:grib2.commons|dataId.discipline_catagroy_grib2ParameterNumber.suffixProcess.levelType
		String suffixJsonPath = String.format("%s.%s.%d_%d_%d.%s.%d", GRIB2_ROOT_NODE_KEY, commonsOrDataId, discipline,
				catagroy, grib2ParameterNumber, GRIB_SUFFIX_PROCESS_NODE_KEY, levelType);
		Map<String, Object> obj = configurationRoot.getMap(parameterJsonPath);
		if (null != obj) {
			shortName = (String) obj.get(GRIB_SHORTNAME_NODE_KEY);
			suffixProcess = configurationRoot.getString(suffixJsonPath);
			return (null != suffixProcess) ? shortName + "_" + suffixProcess : shortName;
		} else {
			logger.warn("配置文件中Grib2区域未找到对应项[jsonPah={"+parameterJsonPath+"}],请检查Json配置文件:");
			return null;
		}
	}

	/**
	 * 根据提供的经纬度范围,确定数据的具体区域代码
	 * 
	 * @param strLa
	 *            第一个点的纬度
	 * @param endLa
	 *            最后一个点的纬度
	 * @param strLo
	 *            第一个点的经度
	 * @param endLo
	 *            最好一个点的
	 * @return
	 */
	public String getGribArea(BigDecimal strLa, BigDecimal endLa, BigDecimal strLo, BigDecimal endLo) {
		boolean findSuccess = false;
		String area = null;
		// JSONPATH:area.检索配置信息
		String jsonPath = String.format("%s", AREA_NODE_KEY);
		Map<String, Object> areas = configurationRoot.getMap(jsonPath);
		Iterator<String> areaJsonKeys = areas.keySet().iterator();
		while (areaJsonKeys.hasNext()) {
			String areaKey = areaJsonKeys.next();
			area = (String) areas.get(areaKey);
			// 匹配成功
			if (compareArea(areaKey, strLa, endLa, strLo, endLo)) {
				findSuccess = true;
				break;
			}
		}
		if (!findSuccess) {
			logger.warn("配置文件中未找到区域配置,请检查Json配置[jsonPah={"+jsonPath+"},strLa={"+strLa+
					"},endLa={"+endLa+"},strLo={"+strLo+"},endLo={"+endLo+"}]:");
			area = null;
		}
		return area;
	}

	/**
	 * 根据Area段的key格式{strLa_endLa_strLo_endLo}与用户传输的参数进行匹配具体的区域关键字
	 * 
	 * @param areaKey
	 *            key格式{strLa_endLa_strLo_endLo}
	 * @param strLa
	 *            第一个点的纬度
	 * @param endLa
	 *            最后一个点的纬度
	 * @param strLo
	 *            第一个点的经度
	 * @param endLo
	 *            最好一个点的
	 * @return
	 */
	private boolean compareArea(String areaKey, BigDecimal strLa, BigDecimal endLa, BigDecimal strLo,
			BigDecimal endLo) {
		String[] str = areaKey.split("_");
		if (4 != str.length) {
			logger.warn("配置文件格式错误,请检查area配置[{"+areaKey+"}],正确的格式为:strLa_endLa_strLo_endLo");
			return false;
		}
		BigDecimal jsonStrLa = new BigDecimal(str[0]);
		BigDecimal jsonEndLa = new BigDecimal(str[1]);
		BigDecimal jsonStrLo = new BigDecimal(str[2]);
		BigDecimal jsonEndLo = new BigDecimal(str[3]);
		return 0 == jsonStrLa.compareTo(strLa) && 0 == jsonEndLa.compareTo(endLa) && 0 == jsonStrLo.compareTo(strLo)
				&& 0 == jsonEndLo.compareTo(endLo);
	}
	
	public static void main(String[] arges) {
		GribDecoderConfigureHelper.instance();
	}
}
