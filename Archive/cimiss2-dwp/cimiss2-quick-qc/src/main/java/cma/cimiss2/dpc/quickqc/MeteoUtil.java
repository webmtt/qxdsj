//package cma.cimiss2.dpc.quickqc;
//
//import cma.cimiss2.dpc.quickqc.bean.*;
//import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
//
//import java.io.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Calendar;
//import java.util.Map;
//import java.util.Set;
//
///**
// * 一点三元插值
// *
// * @author cuihongyuan
// * @version 0.0.1
// */
//public class MeteoUtil {
//
//	/**
//	 * 根据基础配置数据aws,查找参考站配置ref，获取记录所在时次对应要素element的范围质控参数数据，
//	 * 需要根据当前数据对质控参数进行修订，T_MAX_MIN 为台站-年序日-阈值 对
//	 * 返回mapMaxMin
//	 */
//	public MaxMinValues get_aws_element_range(BaseStationInfo aws_conf, Map<String, AwsNatStationInfo> stationInfos, MeteoElement element,
//			LocalDateTime datetime, Map<String, MaxMinDaychinaInfo> mapMaxMin){
//		// MaxMinDaychinaInfo: map (序日，阈值)
//
//		BaseStationInfo ref = get_ref_aws_by_element(stationInfos, mapMaxMin, aws_conf, element);
//		if(ref == null){
//			System.out.println("Element has no configuration: " + element);
//			return null;
//		}
////		根据参考站初始化指定日期的最大最小值
//		MaxMinValues range = set_aws_conf_by_ref_day_range_value(aws_conf, ref, element, datetime.getDayOfYear(), mapMaxMin);
////		根据参考站初始化指定月份的最大最小值
////		set_aws_conf_by_ref_month_range_value(aws, ref, element, datetime.getMonthValue());
//		double bgc = aws_conf.getAltitude() - ref.getAltitude();
//		InterpolateSeq interpolateSeq = null;
//		double high = range.getMaxValue();
//		double low = range.getMinValue();
//		LocalDateTime datetimeTemp = LocalDateTime.of(
//				datetime.getYear(),
//				datetime.getMonthValue(),
//				datetime.getDayOfMonth(),
//				datetime.getHour(),
//				datetime.getMinute(),
//				datetime.getSecond());
//		switch (element) {
//		case TEM:
////			24小时插值出本小时的最高最低
//			datetimeTemp = datetimeTemp.plusHours(8);
//			interpolateSeq = getMaxMinOf24hour(element, aws_conf, datetimeTemp, bgc,
//					range.getMaxValue(), range.getMinValue());
//			range.setMaxValue(interpolateSeq.getMaxHour());
//			range.setMinValue(interpolateSeq.getMinHour());
//
//			// 测试用
//			SunRiseSetTime sunTime = getSunRise_H12(aws_conf, datetimeTemp); // 北京时 日出、日落、太阳高度角、插值温度
//			FileOutputStream out = null;
//		    OutputStreamWriter osw = null;
//		    BufferedWriter bw = null;
//		    String datetimeStr = datetimeTemp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//			try {
//				File file = new File("D:\\HUAXIN\\质控方案\\日出-日落-0-23h插值0715(1).csv");
//				out = new FileOutputStream(file, true);
//		        osw = new OutputStreamWriter(out);
//		        bw = new BufferedWriter(osw);
//
//		        datetimeTemp = datetimeTemp.plusHours(-datetimeTemp.getHour()); // 北京时 0时-23时
//		       String lowTem = "";
//		       String highTem = "";
//		       for(int tt = 0; tt < 24; tt ++){
//					InterpolateSeq test = getMaxMinOf24hour(element, aws_conf, datetimeTemp, bgc, high, low);
//					lowTem += ("," + String.format("%.1f", test.getMinHour()));
//					highTem += ("," + String.format("%.1f", test.getMaxHour()));
//					datetimeTemp = datetimeTemp.plusHours(1);
//				}
//		        String l = aws_conf.getStationCode() + ","
//		        		+ datetimeStr + ","
//		        		+ aws_conf.getLongitude() + ","
//		        		+ aws_conf.getLatitude() + ","
//		        		+ String.format("%.2f",sunTime.getSunRiseTime()) + ","
//		        		+ String.format("%.2f",sunTime.getSunSetTime()) + ","
//		        		+ String.format("%.2f",sunTime.getTimeOfMaxSolaAltitudeAngle()) + ","
//		        		+ "L" + lowTem;
//
//		        String h = aws_conf.getStationCode() + ","
//		        		+ datetimeStr + ","
//		        		+ aws_conf.getLongitude() + ","
//		        		+ aws_conf.getLatitude() + ","
//		        		+ String.format("%.2f",sunTime.getSunRiseTime()) + ","
//		        		+ String.format("%.2f",sunTime.getSunSetTime()) + ","
//		        		+ String.format("%.2f",sunTime.getTimeOfMaxSolaAltitudeAngle()) + ","
//		        		+ "H" + highTem;
//
//		       if(file.length() <= 0){
//		    	   bw.append("区站号,日期,经度,纬度,日出时间,日落时间,太阳高度角最大时的时间,低或高值标识,"
//		    	   		+ "0时值,1时值,2时值,"
//		    	   		+ "3时值,4时值,5时值,"
//		    	   		+ "6时值,7时值,8时值,"
//		    	   		+ "9时值,10时值,11时值,"
//		    	   		+ "12时值,13时值,14时值,"
//		    	   		+ "15时值,16时值,17时值,"
//		    	   		+ "18时值,19时值,20时值,"
//		    	   		+ "21时值,22时值,23时值").append("\r");
//		       }
//		       bw.append(l).append("\r").append(h).append("\r");
//		       bw.flush();
//
//		       out.close();
//		       osw.close();
//		       bw.close();
//
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			// 以上测试用结束
//
//			break;
//
//		default:
//			break;
//		}
//
//		ref = null;
//		return range;
//	}
//
//
//	/**
//	 * 日值参数赋值
//	 * 输入：原始站、参考站、要素、资料时间、温度界限值配置；
//	 * 输出：温度界限值
//	 */
//	private MaxMinValues set_aws_conf_by_ref_day_range_value(BaseStationInfo aws_conf, BaseStationInfo ref,
//			MeteoElement element, int dayOfyear, Map<String, MaxMinDaychinaInfo> mapMaxMin) {
//		int days = dayOfyear;
//		int baseDays = days;
//		// 初始化
//		MaxMinValues range0 = null;
//		if(mapMaxMin.get(aws_conf.getStationCode()) == null)
//			range0 = new MaxMinValues();
//		else
//			range0 = mapMaxMin.get(aws_conf.getStationCode()).getMaxMinValueOfDays().get(days);
//		// 计算
//		MaxMinValues range = new MaxMinValues();
//		range.setMaxValue(range0.getMaxValue());
//		range.setMinValue(range0.getMinValue());
//		range.setExceedingThreshold(range0.getExceedingThreshold());
//
//		MaxMinValues ref_range = mapMaxMin.get(ref.getStationCode()).getMaxMinValueOfDays().get(baseDays);
////		double thresh = T_MAX_MIN.get(ref.getStationCode()).getMaxMinValueOfDays().get(days).getExceedingThreshold();
//		// 其它温度相关要素
//		if (element == MeteoElement.TEM) {
//			range.setMaxValue(ref_range.getMaxValue());
//			range.setMinValue(ref_range.getMinValue() - 2); /*考虑大幅降温天气*/
//			range.setExceedingThreshold(ref_range.getExceedingThreshold());
//			if (range.getExceedingThreshold() > 6.0)
//				range.setExceedingThreshold(6.0);
//			if (range.getExceedingThreshold() < 2.5)
//				range.setExceedingThreshold(2.5);
//		}
//		return range;
//	}
//
//
//
//	/**
//	 * TEM:
//	 * 获取由要素element的配置数据的aws的参考临近站。在限制的海拔差范围内，与本站距离最小的站点为本站的临近站。
//	 * 若最近的站点距离本站超过距离限制，则返回NULL
//	 * get_aws_conf_ex和get_aws_conf_ex2、get_aws_conf_record中调用
//	 */
//	public BaseStationInfo get_ref_aws_by_element(Map<String, AwsNatStationInfo> stationInfos,  Map<String, MaxMinDaychinaInfo> mapMaxMin, BaseStationInfo baseStationInfo, MeteoElement element){
//		double altitude_diff_limit = 1000; /*本站与参考站海拔差限制范围*/
//		double altitude_diff = 0; /*本站与参考站海拔差*/
//		double distance_limit = 99999.9; /*两站间最大距离*/
//		double distance_min = distance_limit; /*参考站与与本站最小的距离*/
//		double distance = 0;
//		altitude_diff_limit = get_altitude_diff_limit(baseStationInfo.getAltitude());
//		BaseStationInfo stationInfo = null;
//		Set <String> keyset = stationInfos.keySet();
//		BaseStationInfo tmpStationInfo = null;
//		for(String sta : keyset){
//			tmpStationInfo = stationInfos.get(sta);
//			altitude_diff = Math.abs(baseStationInfo.getAltitude() - tmpStationInfo.getAltitude());
//			/*海拔差在限制范围内，且站点中该要素已配置*/
//			switch (element) {
//			case TEM:
//				if (altitude_diff < altitude_diff_limit && mapMaxMin.get(sta) != null) {
//					distance = MeteoUtil.GetDistance(baseStationInfo.getLongitude(), baseStationInfo.getLatitude(),
//							tmpStationInfo.getLongitude(), tmpStationInfo.getLatitude());
//					if (distance < distance_min) {
//						distance_min = distance;
//
//						stationInfo = (BaseStationInfo) tmpStationInfo.clone();
//					}
//				}
//				break;
//
//			default:
//				break;
//			}
//
//		}
//		if (distance_min >= distance_limit) {
//			stationInfo = null;	// 最近站点距离超限
//		}
//		return stationInfo;
//	}
//
//
//	/**
//	 * 获取地球上两点之间的距离，千米
//	 */
//	private static double GetDistance(double jd1, double wd1, double jd2, double wd2) {
//		double r = 6378.0;
//		jd1 = 3.1415926 * jd1 / 180.0;
//		jd2 = 3.1415926 * jd2 / 180.0;
//		wd1 = 3.1415926 * wd1 / 180.0;
//		wd2 = 3.1415926 * wd2 / 180.0;
//		return r * Math.acos(Math.sin(wd1) * Math.sin(wd2) + Math.cos(wd1) * Math.cos(wd2) * Math.cos(jd2 - jd1));
//	}
//
//	/**
//	 * 获取计算参考站时的海拔差限制
//	 */
//	static double get_altitude_diff_limit(double altitude) {
//		double altitude_diff_limit = 1000;
//		if (altitude < 1200) {
//			altitude_diff_limit = 1000;
//		} else {
//			altitude_diff_limit = 4500;
//		}
//
//		return altitude_diff_limit;
//	}
//
//	/**
//	 * 根据日极值，插值计算24小时的小时极值
//	 * hour(0-23)
//	 * element (D0,D05,D10,P,TG,T)
//	 */
//	private InterpolateSeq getMaxMinOf24hour(MeteoElement element, BaseStationInfo stationInfo,
//			LocalDateTime datetime, double m_bgc, double MaxDay, double MinDay){
//		SunRiseSetTime sunRiseSetTime;
//		sunRiseSetTime = getSunRise_H12(stationInfo, datetime);
//
//		InterpolateSeq interpolateSeq = null;
//		interpolateSeq = getXYSeq(element, MaxDay, MinDay, m_bgc, sunRiseSetTime);
//
//		interpolateSeq.setMaxHour(enlg3(interpolateSeq.getX(), interpolateSeq.getyMax(), 4, datetime.getHour()));
//		interpolateSeq.setMinHour(enlg3(interpolateSeq.getX(), interpolateSeq.getyMin(), 4, datetime.getHour()));
//		return interpolateSeq;
//
//	}
//
//
//	/**
//	 * 获取要素插值需要的x，y的序列
//	 */
//	private InterpolateSeq getXYSeq(MeteoElement element, double MaxDay, double MinDay, double m_bgc, SunRiseSetTime sunRiseSetTime){
//		InterpolateSeq interpolateSeq = new InterpolateSeq();
//
//		if(element == MeteoElement.TEM){
//			double Max, Min;
//			double fff = (MaxDay - MinDay) / 4.0;
//			Max = MaxDay - fff - 0.6 * m_bgc / 100.0;
//			Min = MinDay + fff - 0.6 * m_bgc / 100.0;
//			if (MinDay < -35.0)        //当温度很低时，Tqc数据一般偏高，所以做些许调整 20130629
//				Max += 0.2;
//			if (MinDay < -40.0)        //逐步增加
//				Max += 0.5;
//			if (MinDay < -42.0)        //逐步增加
//				Max += 0.8;
//			if (MinDay < -45.0)        //逐步增加
//				Max += 0.5;
//
//			interpolateSeq.getX()[0] = 0.0;
//			interpolateSeq.getX()[1] = sunRiseSetTime.getSunRiseTime();///////////////////////////
//			interpolateSeq.getX()[2] = sunRiseSetTime.getTimeOfMaxSolaAltitudeAngle() + 2.0;
//			interpolateSeq.getX()[3] = 24.0;        //x的值表示时间 (时间长度为25小时)
//
//			double Max_Min = (Max - Min) / 2.8;
//			double Min_Max = (Max - Min) / 4.0;
//
//			interpolateSeq.getyMax()[2] = Max;
//			interpolateSeq.getyMax()[1] = interpolateSeq.getyMax()[2] - Max_Min;
//			interpolateSeq.getyMax()[0] = interpolateSeq.getyMax()[3] = interpolateSeq.getyMax()[1] / 2.0 + interpolateSeq.getyMax()[2] / 2.0 - 1.8;
//
//			interpolateSeq.getyMin()[1] = Min;
//			interpolateSeq.getyMin()[2] = interpolateSeq.getyMin()[1] + Min_Max;
//			interpolateSeq.getyMin()[0] = interpolateSeq.getyMin()[3] = interpolateSeq.getyMin()[1] / 2.0 + interpolateSeq.getyMin()[2] / 2.0 - 1.8;
//
//		}
//		return interpolateSeq;
//	}
//
//
//
//	/**
//	 * 一元三点不等距插值
//	 */
//	private double enlg3(double x[], double y[], int n, double t) {
//		int i, j, k, m;
//		double z, s;
//		z = 0.0;
//		if (n < 1)
//			return (z);
//		if (n == 1) {
//			z = y[0];
//			return (z);
//		}
//		if (n == 2) {
//			z = (y[0] * (t - x[1]) - y[1] * (t - x[0])) / (x[0] - x[1]);
//			return (z);
//		}
//		if (t <= x[1]) {
//			k = 0;
//			m = 2;
//		} else if (t >= x[n - 2]) {
//			k = n - 3;
//			m = n - 1;
//		} else {
//			k = 1;
//			m = n;
//			while (m - k != 1) {
//				i = (k + m) / 2;
//				if (t < x[i - 1])
//					m = i;
//				else
//					k = i;
//			}
//			k = k - 1;
//			m = m - 1;
//			if (Math.abs(t - x[k]) < Math.abs(t - x[m]))
//				k = k - 1;
//			else
//				m = m + 1;
//		}
//		z = 0.0;
//		for (i = k; i <= m; i++) {
//			s = 1.0;
//			for (j = k; j <= m; j++)
//				if (j != i)
//					s = s * (t - x[j]) / (x[i] - x[j]);
//			z = z + s * y[i];
//		}
//		return (z);
//	}
//
//	/**
//	 * 返回该日日出时间和太阳高度角最大时间
//	 * 年月日为北京时间的
//	 */
//	private SunRiseSetTime getSunRise_H12(BaseStationInfo stationInfo, LocalDateTime datetime) {
//		SunRiseSetTime sunTime = sunriseAndSunsetLocalTime(datetime, stationInfo);
//		int sunrisetime = (int)sunTime.getSunRiseTime(); // （太阳时）日出时间 整数部分
//		int sunsettime = (int)sunTime.getSunSetTime();
//
//		//获取浮点字符串的小数部分，小数点后2位
//		double decimalPart = sunTime.getSunRiseTime() - (int)sunTime.getSunRiseTime();// 日出时间小数部分
//		double decimalPartSet = sunTime.getSunSetTime() - (int)sunTime.getSunSetTime(); //日落时间小数部分
//		BigDecimal bigD = new BigDecimal(decimalPart);
//		BigDecimal bigDSet = new BigDecimal(decimalPartSet);
//		double decimalPartP2 = bigD.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); // 小数部分，四舍五入两位
//		double decimalPartSet2 = bigDSet.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//
//
//		int start_min = (int) (decimalPartP2 * 60.0);   //开始分钟
//		int end_min = (int)(decimalPartSet2 * 60);//结束分钟
//
//		double lon = stationInfo.getLongitude();
//		// 经度值小数点后两位
//		double LonDecimalP2 = (new BigDecimal(lon - (int)lon)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
//		int i_lon;
//		if(Math.round(LonDecimalP2) >= 30){
//			// 经度整数部分加1
//			i_lon = 1 + (int) lon;
//		}else{
//			i_lon = (int)lon;
//		}
//		int year = datetime.getYear();
//		int month = datetime.getMonthValue();
//		int dayOfMonth = datetime.getDayOfMonth();
//
//		// 日出时间北京时
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(year, month -1, dayOfMonth, sunrisetime, start_min + (-4 * (i_lon - 120)), 0);
//		LocalDateTime bjtime_sunraise = LocalDateTime.of(
//				calendar.get(Calendar.YEAR),
//				calendar.get(Calendar.MONTH) + 1,
//				calendar.get(Calendar.DAY_OF_MONTH),
//				calendar.get(Calendar.HOUR_OF_DAY),
//				calendar.get(Calendar.MINUTE),
//				calendar.get(Calendar.SECOND)
//				);
//		calendar.clear();
//		sunTime.setSunRiseTime(bjtime_sunraise.getHour() + bjtime_sunraise.getMinute() / 60.0); //日出时间北京时
//
//		// 日落时间 北京时
//		Calendar calendar2 = Calendar.getInstance();
//		calendar2.set(year, month - 1, dayOfMonth, sunsettime, end_min + (-4 * (i_lon - 120)), 0);
//		LocalDateTime bjtime_sunset = LocalDateTime.of(
//				calendar2.get(Calendar.YEAR),
//				calendar2.get(Calendar.MONTH) + 1,
//				calendar2.get(Calendar.DAY_OF_MONTH),
//				calendar2.get(Calendar.HOUR_OF_DAY),
//				calendar2.get(Calendar.MINUTE),
//				calendar2.get(Calendar.SECOND)
//				);
//		calendar2.clear();
//		sunTime.setSunSetTime(bjtime_sunset.getHour() + bjtime_sunset.getMinute() / 60.0); // 日落时间北京时
//
//		sunTime.setTimeOfMaxSolaAltitudeAngle(sunTime.getSunRiseTime()
//				+ sunTime.getTimeOfMaxSolaAltitudeAngle() / 2.0); // 太阳高度角北京时
//
//		return sunTime;
//	}
//
//	/**
//	 * 地平时日出日落计算,输入：资料时间、台站信息
//	 */
//	private SunRiseSetTime sunriseAndSunsetLocalTime(LocalDateTime datetime, BaseStationInfo stationInfo){
//		SunRiseSetTime setTime = new SunRiseSetTime();
//		double dblEq;	//时差
//		double dblN0;	//修正值
//		double dblD;	//赤纬
//		double dblQ;
//		double dblTemp;
//		double ArcSin;
//		float Tb;	//半日可照时数
//		long lnN = 0;   //按天数排列的积日
//		int year = datetime.getYear();
//
//		lnN = datetime.getDayOfYear() - 1; // getDayOfYear 比c++的值大1
//		dblTemp = (new BigDecimal(0.25 * (year - 1985))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//		dblN0 = 79.6764 + 0.2422 * (year - 1985) - Math.floor(dblTemp);
//		dblQ = 2 * 3.14159 *57.3* (lnN + (12 - stationInfo.getLongitude() / 15) / 24 - dblN0)
//				/ 365.2422;   //按正午12时考虑
//		dblD = 0.3723 + 23.2567 * Math.sin(dblQ*3.14159/180) + 0.1149 * Math.sin(2 * dblQ*3.14159/180)
//				- 0.1712 * Math.sin(3 * dblQ*3.14159/180) - 0.758 * Math.cos(dblQ*3.14159/180)
//				+ 0.3656 * Math.cos(2 * dblQ*3.14159/180) + 0.0201 * Math.cos(3 * dblQ*3.14159/180);
//		dblEq = (0.0028 - 1.9857 * Math.sin(dblQ*3.14159/180) + 9.9059 * Math.sin(2 * dblQ*3.14159/180)
//				- 7.0924 * Math.cos(dblQ*3.14159/180) - 0.6882 * Math.cos(2 * dblQ*3.14159/180))/60;
//		dblTemp = Math.sin(
//				(45 + (stationInfo.getLatitude() - dblD + (float) 34 / (float) 60) / 2) * 3.14159
//				/ 180)* Math.sin((45 - (stationInfo.getLatitude() - dblD - (float) 34 / (float) 60) / 2)
//				* 3.14159 / 180);
//		if (dblTemp < 0) {
//			setTime.setSunRiseTime(12);
//			setTime.setSunSetTime(12);
//			setTime.setTimeOfMaxSolaAltitudeAngle(0);
//		}
//
//		dblTemp = Math.sqrt(dblTemp / (Math.cos(stationInfo.getLatitude() * 3.14159 / 180) * Math.cos(dblD * 3.14159 / 180)));
//		if (dblTemp >= 1) {
//			setTime.setSunRiseTime(0);
//			setTime.setSunSetTime(24);
//			setTime.setTimeOfMaxSolaAltitudeAngle(24);
//		} else {
//			ArcSin = Math.atan(dblTemp / Math.sqrt(1 - dblTemp * dblTemp));
//			Tb = (float) (2 * ArcSin * 180 / 3.14159 / 15);
//
//			BigDecimal bgSunRise = new BigDecimal(12 - Tb - dblEq);
//	        double rise = bgSunRise.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//	        BigDecimal bgSunSet= new BigDecimal(12 + Tb - dblEq);
//	        double set = bgSunSet.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//			setTime.setSunRiseTime(rise);
//			setTime.setSunSetTime(set);
//			setTime.setTimeOfMaxSolaAltitudeAngle(2 * Tb);
//
//		}
//		return setTime;
//	}
//
//	  public static void main(String[] args) {
//		  //UTC与格林尼治平均时(GMT, Greenwich Mean Time)一样，都与英国伦敦的本地时相同。
//		    String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss a";
////		    LocalDateTime ldt = LocalDateTime.now();
//		    LocalDateTime ldt = LocalDateTime.of(2019, 8, 2, 4, 0, 0);
//	        ZoneId shanghaiZoneId = ZoneId.of("Asia/Shanghai");
//	        System.out.println("TimeZone : " + shanghaiZoneId);
//
//	        //LocalDateTime + ZoneId = ZonedDateTime
//	        ZonedDateTime asiaZonedDateTime = ldt.atZone(shanghaiZoneId);
//	        System.out.println("Date (Shanghai) : " + asiaZonedDateTime);
//
//	        ZoneId newYokZoneId = ZoneId.of("America/New_York");
//	        System.out.println("TimeZone : " + newYokZoneId);
//	        //使用这个方法，就可以得到当前的时间
//	        ZonedDateTime nyDateTime = asiaZonedDateTime.withZoneSameInstant(newYokZoneId);
//	        System.out.println("Date (New York) : " + nyDateTime);
//
//	        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_FORMAT);
//	        System.out.println("\n---DateTimeFormatter---");
//	        System.out.println("Date (Shanghai) : " + format.format(asiaZonedDateTime));
//	        System.out.println("Date (New York) : " + format.format(nyDateTime));
//
//
//	        ZonedDateTime utcDatetime = asiaZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
//	        System.out.println();
//	        System.out.println("Date (UTC): " + utcDatetime);
//	        int a = utcDatetime.getDayOfYear();
//	        int b = asiaZonedDateTime.getDayOfYear();
//	        System.out.print(a + "\t" + b);
//	  }
//
//}
