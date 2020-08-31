package cma.cimiss2.dpc.indb.surf.dc_surf_japan.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.JapanStationData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.DataBaseAction;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSBatchResult;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * ************************************
 * 
 * @ClassName:OTSService
 * @Auther: liuyingshuang
 * @Date:2019年4月6日
 * @Description:日本站点数据ots入库
 * @Copyright: All rights reserver. ************************************
 */

public class OTSService {
	
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

	 public static void setDiQueues(BlockingQueue<StatDi> diQueues){
    	OTSService.diQueues = diQueues;
    }
    public static BlockingQueue<StatDi> getDiQueues(){
    	return diQueues;
    }
	/**
	 * ots入库
	 * 
	 * @param pa
	 * @param date
	 * @param filePath
	 * @param log
	 * @return
	 */
	public static DataBaseAction processSuccessReport(ParseResult<JapanStationData> pa, Date date, String filePath,
			StringBuffer log) {

		try {
			String valueTable = StartConfig.valueTable(); // 要素表名
			String sodCode = StartConfig.sodCode(); // 入库值对应D_DATA_ID
			File file = new File(filePath);
			String fileN = file.getName();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<JapanStationData> JSDS = pa.getData();
			// 读取JapanStaion记录
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < JSDS.size(); i++) {
				JapanStationData jsd = JSDS.get(i);
				Map<String, Object> row = insertValueTable(valueTable, jsd, fileN, sodCode, date, sdf);
				rows.add(row);
			}
			OTSBatchResult result = OTSDbHelper.getInstance().insert(valueTable, rows);
			log.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
			log.append(" INSERT SUCCESS COUNT : " + result.getSuccessRowCount() + "\n");
			log.append(" INSERT FAILED COUNT : " + result.getFailedRowCount() + "\n");
			log.append(" INSERT FAILED CONTENT : " + result.getFailedRows() + "\n");
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			return DataBaseAction.INSERT_ERROR;
		}

	}

	/**
	 * 要素表入库
	 * @param sdf
	 * @param valueTable
	 * @param jsd
	 * @param sodCode
	 * @param fileN
	 * @param rece_time
	 */
	private static Map<String, Object> insertValueTable(String valueTable, JapanStationData jsd, String fileN,
			String sodCode, Date rece_time, SimpleDateFormat sdf) {

		Date date1 = TimeUtil.String2Date(jsd.getDataTime(), "yyyyMMddHH");
		String date = TimeUtil.date2String(date1, "yyyy-MM-dd HH:mm:ss");
		int year = TimeUtil.getYear(date1);
		int month = TimeUtil.getMonth(date1);
		int day = TimeUtil.getDayOfMonth(date1);
		int hour = TimeUtil.getHour(date1);

		Map<String, Object> row = new HashMap<>();
		row.put("D_DATA_ID", StartConfig.sodCode());
		// 时间，年月日
		row.put("D_DATETIME", date);
		row.put("V04001_02", year);
		row.put("V04002_02", month);
		row.put("V04003_02", day);
		row.put("V04004_02", hour);
		// 区站号（缺）
		row.put("V01301", jsd.getStation());
		// 纬度、经度、海拔
		row.put("V05001", jsd.getLongitude());
		row.put("V06001", jsd.getLatitude());
		row.put("V07001", jsd.getAltitude());
		// 风向、风速
		row.put("V11001",field(jsd.getWindDirection()) );
		row.put("V11002", jsd.getWindSpeed());
		// 气温、相对湿度、气压
		row.put("V12001", jsd.getTemperature());
		row.put("V13003", jsd.getHumidity());
		row.put("V10004", jsd.getPressure());
		// 24降水、24高温、24低温、积雪、日照数
		row.put("V13023", jsd.getPrecipitation());
		row.put("V12016", jsd.getMaxTEP24());
		row.put("V12017", jsd.getMinTEP24());
		row.put("V13013", jsd.getSnowDepth());
		row.put("V14032_24", jsd.getSunshineDuration());
		// DPC产品表示、报告类别、编报中心(暂时不确定)
		row.put("D_DATA_DPCID", StartConfig.getDPCCode());
		row.put("V_TT", StartConfig.getvtt());
		row.put("C_CCCC", StartConfig.v_cccc());
		// 没有的数据拼接
		//记录标识
		row.put("D_RECORD_ID",
				jsd.getDataTime() + "_" + jsd.getLatitude() + "_" + jsd.getCountyName() + "_" + UUID.randomUUID());
		//入库时间
		row.put("D_IYMDHM", sdf.format(new Date()));
		//收到时间
		row.put("D_RYMDHM", sdf.format(rece_time));
		//更新时间
		row.put("D_UPDATE_TIME", sdf.format(new Date()));
		//更正报标志
		row.put("V_BBB", "000");
		//区站号
		row.put("V01300", 999999);
		//年
		row.put("V04001", year);
		//月
		row.put("V04002", month);
		//日
		row.put("V04003", day);
		//时
		row.put("V04004", hour);
		//分
		row.put("V04005", 0);

		// 1
		row.put("V07031", 999998);      //	气压传感器海拔高度
		row.put("V07032_01", 999998);   	//	温湿传感器距地面/甲板高度
		row.put("V07032_02", 999998);   	//	能见度传感器距地高度
		row.put("V07032_03", 999998);   	//	降水量传感器距地高度
		row.put("V07032_04", 999998);   	//	风速传感器距地面高度
		row.put("V02002", 999998);      	//	测风仪的仪器类型
		row.put("V02004", 999998);      	//	测量蒸发的仪器类型或作物类型
		row.put("V02001", 999998);      	//	测站类型
		row.put("V_NCODE", 999998);     	//	国家代码
		row.put("V01015", 999998);      	//	站名或场地名
		row.put("V02301", 999998);           //	台站级别
		row.put("V20013", 999998);           	//	低云或中云的云高
		row.put("V20001", 999998);           	//	水平能见度
		row.put("V20010", 999998);           	//	总云量
		row.put("V12003", 999998);           	//	露点温度
		row.put("V10051", 999998);           	//	海平面气压
		row.put("V07004", 999998);           	//	气压（测站最接近的标准层）
		row.put("V10009", 999998);           	//	位势高度（标准层）
		row.put("V10063", 999998);           	//	气压倾向的特征
		row.put("V10061", 999998);           	//	3小时变压
		row.put("V13019", 999998);           	//	过去1小时降水量
		row.put("V13011_02", 999998);        	//	过去2小时降水量
		row.put("V13020", 999998);           	//	过去3小时降水量
		row.put("V13021", 999998);           	//	过去6小时降水量
		row.put("V13011_09", 999998);        	//	过去9小时降水量
		row.put("V13022", 999998);           	//	过去12小时降水量
		row.put("V13011_15", 999998);        	//	过去15小时降水量
		row.put("V13011_18", 999998);        	//	过去18小时降水量
		row.put("V20003", 999998);           	//	现在天气
		row.put("V20004", 999998);           	//	过去天气1
		row.put("V20005", 999998);           	//	过去天气2
		row.put("V20011", 999998);           	//	云量(低云或中云)
		row.put("V20350_11", 999998);        	//	低云状
		row.put("V20350_12", 999998);        	//	中云状
		row.put("V20350_13", 999998);        	//	高云状
		row.put("V10062", 999998);           	//	24小时变压
		row.put("V12405", 999998);           	//	过去24小时变温
		row.put("V12014", 999998);           	//	过去12小时最高气温
		row.put("V12015", 999998);           	//	过去12小时最低气温
		row.put("V12013", 999998);           	//	过去12小时地面最低温度
		row.put("V20062", 999998);           	//	地面状态
		row.put("V13340", 999998);           	//	日蒸发量
		row.put("V14032_01", 999998);        	//	过去1小时日照时数
		row.put("V14016_01", 999998);        	//	过去1小时净辐射
		row.put("V14015", 999998);           	//	过去24小时净辐射
		row.put("V14021_01", 999998);        	//	过去1小时总太阳辐射
		row.put("V14020", 999998);           	//	过去24小时总太阳辐射
		row.put("V14023_01", 999998);        	//	过去1小时漫射太阳辐射
		row.put("V14022", 999998);           	//	过去24小时漫射太阳辐射
		row.put("V14002_01", 999998);        	//	过去1小时长波辐射
		row.put("V14001", 999998);           	//	过去24小时长波辐射
		row.put("V14004_01", 999998);        	//	过去1小时短波辐射
		row.put("V14003", 999998);           	//	过去24小时短波辐射
		row.put("V14025_01", 999998);        	//	过去1小时直接太阳辐射
		row.put("V14024", 999998);           	//	过去24小时直接太阳辐射
		row.put("V20054_01", 999998);        	//	低云移向
		row.put("V20054_02", 999998);        	//	中云移向
		row.put("V20054_03", 999998);        	//	高云移向
		row.put("V20012", 999998);           	//	地形云或垂直发展云的云属
		row.put("V05021", 999998);           	//	地形云或垂直发展云的方位
		row.put("V07021", 999998);           	//	地形云或垂直发展云的高度角
		row.put("V20012_01", 999998);        	//	云属1
		row.put("V20011_01", 999998);        	//	云属1的云量
		row.put("V20013_01", 999998);        	//	云属1的云高
		row.put("V20012_02", 999998);        	//	云属2
		row.put("V20011_02", 999998);        	//	云属2的云量
		row.put("V20013_02", 999998);        	//	云属2的云高
		row.put("V20012_03", 999998);        	//	云属3
		row.put("V20011_03", 999998);        	//	云属3的云量
		row.put("V20013_03", 999998);        	//	云属3的云高
		row.put("V20012_04", 999998);        	//	积雨云的云属
		row.put("V20011_04", 999998);        	//	积雨云的云量
		row.put("V20013_04", 999998);        	//	积雨云的云高
		row.put("V20063_01", 999998);        	//	特殊天气现象1
		row.put("V20063_02", 999998);        	//	特殊天气现象2
		row.put("V20063_03", 999998);        	//	特殊天气现象3
		
		row.put("Q20013", 9);            	//	云底高度质量标志
		row.put("Q20001", 9);            	//	水平能见度质量标志
		row.put("Q20010", 9);            	//	总云量质量标志
		row.put("Q11001", 9);            	//	风向质量标志
		row.put("Q11002", 9);            	//	风速质量标志
		row.put("Q12001", 9);            	//	气温质量标志
		row.put("Q12003", 9);            	//	露点温度质量标志
		row.put("Q13003", 9);            	//	相对湿度质量标志
		row.put("Q10004", 9);            	//	本站气压质量标志
		row.put("Q10051", 9);            	//	海平面气压质量标志
		row.put("Q07004", 9);            	//	气压（测站最接近的标准层）质量标志
		row.put("Q10009", 9);            	//	位势高度（标准层）质量标志
		row.put("Q10063", 9);            	//	气压倾向的特征质量标志
		row.put("Q10061", 9);            	//	3小时变压质量标志
		row.put("Q13019", 9);            	//	过去1小时降水量质量标志
		row.put("Q13011_02", 9);  	//	过去2小时降水量质量标志
		row.put("Q13022", 9);  //	过去12小时降水量质量标志
		
	
		row.put("Q13020", 9);            	//	过去3小时降水量质量标志
		row.put("Q13021", 9);            	//	过去6小时降水量质量标志
		row.put("Q13011_09", 9);         	//	过去9小时降水量质量标志
		row.put("Q13011_15", 9);         	//	过去12小时降水量质量标志
		row.put("Q13011_18", 9);         	//	过去15小时降水量质量标志
		row.put("Q13023", 9);            	//	过去18小时降水量质量标志
		row.put("Q20003", 9);            	//	过去24小时降水量质量标志
		row.put("Q20004", 9);            	//	现在天气质量标志
		row.put("Q20005", 9);            	//	过去天气1质量标志
		row.put("Q20011", 9);            	//	过去天气2质量标志
		row.put("Q20350_11", 9);         	//	云量(低云或中云)
		row.put("Q20350_12", 9);         	//	低云状质量标志
		row.put("Q20350_13", 9);         	//	中云状质量标志
		row.put("Q10062", 9);            	//	高云状质量标志
		row.put("Q12405", 9);            	//	24小时变压质量标志
		row.put("Q12014", 9);            	//	过去24小时变温质量标志
		row.put("Q12016", 9);            	//	过去12小时最高气温质量标志
		row.put("Q12015", 9);            	//	过去24小时最高气温质量标志
		row.put("Q12017", 9);            	//	过去12小时最低气温质量标志
		row.put("Q12013", 9);            	//	过去24小时最低气温质量标志
		row.put("Q20062", 9);            	//	过去12小时地面最低温度质量标志
		row.put("Q13013", 9);            	//	地面状态质量标志
		row.put("Q13340", 9);            	//	积雪深度质量标志
		row.put("Q14032_01", 9);         	//	日蒸发量质量标志
		row.put("Q14032_24", 9);         	//	过去1小时日照时数质量标志
		row.put("Q14016_01", 9);         	//	过去24小时日照时数质量标志
		row.put("Q14015", 9);            	//	过去1小时净辐射质量标志
		row.put("Q14021_01", 9);         	//	过去24小时净辐射质量标志
		row.put("Q14020", 9);            	//	过去1小时总太阳辐射质量标志
		row.put("Q14023_01", 9);         	//	过去24小时总太阳辐射质量标志
		row.put("Q14022", 9);            	//	过去1小时漫射太阳辐射质量标志
		row.put("Q14002_01", 9);         	//	过去24小时漫射太阳辐射
		row.put("Q14001", 9);            	//	过去1小时长波辐射质量标志
		row.put("Q14004_01", 9);         	//	过去24小时长波辐射质量标志
		row.put("Q14003", 9);            	//	过去1小时短波辐射质量标志
		row.put("Q14025_01", 9);         	//	过去24小时短波辐射质量标志
		row.put("Q14024", 9);            	//	过去1小时直接太阳辐射质量标志
		row.put("Q20054_01", 9);         	//	过去24小时直接太阳辐射质量标志
		row.put("Q20054_02", 9);         	//	低云移向质量标志
		row.put("Q20054_03", 9);         	//	中云移向质量标志
		row.put("Q20012", 9);            	//	高云移向质量标志
		row.put("Q05021", 9);            	//	地形云或垂直发展云的云属质量标志
		row.put("Q07021", 9);            	//	地形云或垂直发展云的方位质量标志
		row.put("Q20012_01", 9);         	//	地形云或垂直发展云的高度角质量标志
		row.put("Q20011_01", 9);         	//	云属1质量标志
		row.put("Q20013_01", 9);         	//	云属1的云量质量标志
		row.put("Q20012_02", 9);         	//	云属1的云高质量标志
		row.put("Q20011_02", 9);         	//	云属2质量标志
		row.put("Q20013_02", 9);         	//	云属2的云量质量标志
		row.put("Q20012_03", 9);         	//	云属2的云高质量标志
		row.put("Q20011_03", 9);         	//	云属3质量标志
		row.put("Q20013_03", 9);         	//	云属3的云量质量标志
		row.put("Q20012_04", 9);         	//	云属3的云高质量标志
		row.put("Q20011_04", 9);         	//	积雨云的云属质量标志
		row.put("Q20013_04", 9);         //		积雨云的云量质量标志
		row.put("Q20063_01", 9);         //		积雨云的云高质量标志
		row.put("Q20063_02", 9);         //		特殊天气现象1质量标志
		row.put("Q20063_03", 9);         //		特殊天气现象2质量标志
           
		row.put("D_SOURCE_ID", StartConfig.ctsCode());
		// di
		StatDi di = new StatDi();
		di.setFILE_NAME_O(fileN);
		di.setDATA_TYPE(sodCode);
		di.setDATA_TYPE_1(StartConfig.ctsCode());
		di.setTT("日本站点资料");
		di.setTRAN_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(fileN);
		di.setBUSINESS_STATE("0"); // 0成功，1失败
		di.setPROCESS_STATE("0"); // 0成功，1失败

		di.setDATA_TIME(TimeUtil.date2String(date1, "yyyy-MM-dd HH:mm:ss"));
		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		di.setRECORD_TIME(TimeUtil.getSysTime());
		di.setLATITUDE(String.valueOf(jsd.getLatitude()));
		di.setLONGTITUDE(String.valueOf(jsd.getLongitude()));

		diQueues.offer(di);
		return row;
	}
	/**
	 * 风数据转换
	 * @param key
	 * @return
	 */
	public static double field(String key){
		if(key.toUpperCase().equals("N")) 
			return  0; 
		else if(key.toUpperCase().equals("NNE"))
			return  22.5; 
		else if(key.toUpperCase().equals("NE"))
			return  45; 
		else if(key.toUpperCase().equals("ENE"))
			return 67.5; 
		else if(key.toUpperCase().equals("E"))
			return 90; 
		else if(key.toUpperCase().equals("ESE"))
			return 112.5;
		else if(key.toUpperCase().equals("SE"))
			return  135; 
		else if(key.toUpperCase().equals("SSE"))
			return 157.5; 
		else if(key.toUpperCase().equals("S"))
			return  180; 
		else if(key.toUpperCase().equals("SSW"))
			return 202.5; 
		else if(key.toUpperCase().equals("SW"))
			return  225; 
		else if(key.toUpperCase().equals("WSW"))
			return 247.5;
		else if(key.toUpperCase().equals("W"))
			return  270; 
		else if(key.toUpperCase().equals("WNW"))
			return  292.5; 
		else if(key.toUpperCase().equals("NW"))
			return  315; 
		else if(key.toUpperCase().equals("NNW"))
			return 337.5;
		else if(key.toUpperCase().equals("C") || key.toUpperCase().equals("CALM"))
			return 999017;
		else 
			return 999999;
		
	}
}
