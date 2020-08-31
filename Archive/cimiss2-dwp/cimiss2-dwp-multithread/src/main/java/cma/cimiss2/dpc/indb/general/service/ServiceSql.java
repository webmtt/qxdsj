package cma.cimiss2.dpc.indb.general.service;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.indb.general.common.IndexConf;
import cma.cimiss2.dpc.indb.general.vo.Pair;
/**
 * <br>
 * @Title:  ServiceSql.java   
 * @Package org.cimiss2.dwp.RADAR.service   
 * @Description:    TODO(生成sql语句)
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月15日 下午5:42:05   wuzuoqiang    Initial creation.
 * </pre>
 * @author wuzuoqiang
 */
public class ServiceSql {
	public static final Logger logger = LoggerFactory.getLogger("loggerInfo"); //消息处理日志
	/**
	 * @Title: genIndexSql 
	 * @Description: TODO(根据配置策略生成入索引库的sql) 
	 * @param strTabName  索引库表名
	 * @param strCtsType  cts 四级编码
	 * @param strSodType  sod 四级编码
	 * @param sp          文件名分割数据
	 * @param startTime 开始处理时间
	 * @param srcFilename 源文件名 
	 * @param filesize 文件大小
	 * @param recvtime 接收时间
	 * @param newpath 文件存储路径
	 * @param dataTime 资料时间
	 * @return  
	 *    String
	 * @throws
	 */

	@SuppressWarnings("deprecation")
	public static String genIndexSql(String strTabName, String strCtsType, String strSodType, 
			String[] sp,String startTime,String srcFilename, String filesize,
			String recvtime,String newpath,StringBuffer dataTime){
		System.out.println("  genIndexSql  " +  StringUtils.join(sp, "  --->>  "));
		String newFileName = new File(newpath).getName();
		String sql = ""; //sql语句
		try {
			Vector<Pair <String ,String> > fields = IndexConf.sm_indexConf.get(strCtsType);
			if (fields == null) {
				logger.error("can not find cts type:" + strCtsType + " in the index config file.");
				return "";
			}
			int [] iInxArray = new int[3];	//索引截取位置
			String strField = "insert into "+ strTabName+"(";
			String strVal = "values(";
			String strFieldVal; //字段值
			for(int i=0;i<fields.size();i++){
				int begin,end;
				strFieldVal=fields.get(i).getSecond().trim();
				if(strFieldVal.contains("$")){
					while(strFieldVal.contains("$")){
						begin=strFieldVal.indexOf("$");
						end=strFieldVal.indexOf("}");
						String var=strFieldVal.substring(begin, end + 1);
						if(var.contains(":")){
							if((var.contains("${")) && (var.contains("}"))){
								String[] index = strFieldVal.substring(begin+2, end).split(":");
								for(int m=0; m<index.length; m++) {
									iInxArray[m] = Integer.parseInt(index[m]);
								}
								strFieldVal = strFieldVal.replace(var,sp[iInxArray[0]].substring(iInxArray[1],iInxArray[1]+iInxArray[2]));
							}
							else{
								logger.error("cts type:" + strCtsType + " wrong format of the index config\n");
								return "";
							}
						}
						else{
							iInxArray[0] = Integer.parseInt(strFieldVal.substring(begin+2,end));
							strFieldVal=strFieldVal.replace(var, sp[iInxArray[0]]);
						}
					}
					
					
					
				}
				else if (strFieldVal.equalsIgnoreCase("d_file_id")) {
					strFieldVal="'" + newFileName + "'";
				}
				else if(strFieldVal.equalsIgnoreCase("cts_type"))
					strFieldVal="'"+strCtsType+"'";
				else if(strFieldVal.equalsIgnoreCase("sod_type"))
					strFieldVal="'"+strSodType+"'";
				else if(strFieldVal.equalsIgnoreCase("start_time"))
					strFieldVal="'"+startTime+"'";
//				else if(strFieldVal.equalsIgnoreCase("end_time"))
//					strFieldVal="'"+endTime+"'";
				else if(strFieldVal.equalsIgnoreCase("filename"))
					strFieldVal="'" + newFileName + "'";
				else if(strFieldVal.equalsIgnoreCase("srcfilename"))
					strFieldVal="'" + srcFilename + "'";
				else if(strFieldVal.equalsIgnoreCase("filesize"))
					strFieldVal=filesize;
				else if(strFieldVal.equalsIgnoreCase("recvtime"))
					strFieldVal="'"+recvtime+"'";
				else if(strFieldVal.equalsIgnoreCase("newpath"))
					strFieldVal="'"+newpath+"'";
				else if(strFieldVal.equals("sysdate")){
					strFieldVal="'"+TimeUtil.getSysTime()+"'";
				}
				else if (strFieldVal.equals("fileext")) {
					strFieldVal = "'" + sp[sp.length-1] + "'";
				}
				else if(strFieldVal.equals("\"\"") || strFieldVal.isEmpty() ){
					strFieldVal ="''";
				}
				else
					strFieldVal=fields.get(i).getSecond();

				if (fields.get(i).getFirst().equals("D_DATETIME") || fields.get(i).getFirst().equals("V_FNTIME") || fields.get(i).getFirst().equals("V_FILETIME") || fields.get(i).getFirst().equals("V_RETAIN1_C") ) {
					if(strFieldVal.contains("YYYYDDD")) {
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						String[] itemStrings = strFieldVal.substring(10, strFieldVal.length()-2).split("[|]");
						for ( int n = 0; n < itemStrings.length; n++) {
							if(n == 0) {
								String yearString = itemStrings[0].trim();
								if(yearString.contains(":")) {
									String[] tempStrings = yearString.substring(1, yearString.length()-1).split(":");
									int index = Integer.parseInt(tempStrings[0].trim());
									int start_index = Integer.parseInt(tempStrings[1].trim());
									int end_index = Integer.parseInt(tempStrings[2].trim());
									calendar.set(Calendar.YEAR, Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
								}else {
									int index = Integer.parseInt(yearString.substring(1, yearString.length()-1));
									calendar.set(Calendar.YEAR, Integer.parseInt(sp[index]));
								}
							}else if (n == 1) {
								String day = itemStrings[1].trim();
								if(day.contains(":")) {
									String[] tempStrings = day.substring(1, day.length()-1).split(":");
									int index = Integer.parseInt(tempStrings[0].trim());
									int start_index = Integer.parseInt(tempStrings[1].trim());
									int end_index = Integer.parseInt(tempStrings[2].trim());
									System.out.println(Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
									calendar.set(Calendar.DAY_OF_YEAR, Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
								}else {
									int index = Integer.parseInt(day.substring(1, day.length()-1));
									calendar.set(Calendar.DAY_OF_YEAR, Integer.parseInt(sp[index]));
								}
							}else if (n == 2) {
								String hour = itemStrings[2].trim();
								if(hour.contains(":")) {
									String[] tempStrings = hour.substring(1, hour.length()-1).split(":");
									int index = Integer.parseInt(tempStrings[0].trim());
									int start_index = Integer.parseInt(tempStrings[1].trim());
									int end_index = Integer.parseInt(tempStrings[2].trim());
									System.out.println(Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
									calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
								}else {
									int index = Integer.parseInt(hour.substring(1, hour.length()-1));
									calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sp[index]));
								}
							}else if (n == 3) {
								String minte = itemStrings[3].trim();
								if(minte.contains(":")) {
									String[] tempStrings = minte.substring(1, minte.length()-1).split(":");
									int index = Integer.parseInt(tempStrings[0].trim());
									int start_index = Integer.parseInt(tempStrings[1].trim());
									int end_index = Integer.parseInt(tempStrings[2].trim());
									System.out.println(Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
									calendar.set(Calendar.MINUTE, Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
								}else {
									int index = Integer.parseInt(minte.substring(1, minte.length()-1));
									calendar.set(Calendar.MINUTE, Integer.parseInt(sp[index]));
								}
							}else if (n == 4) {
								String second = itemStrings[4].trim();
								if(second.contains(":")) {
									String[] tempStrings = second.substring(1, second.length()-1).split(":");
									int index = Integer.parseInt(tempStrings[0].trim());
									int start_index = Integer.parseInt(tempStrings[1].trim());
									int end_index = Integer.parseInt(tempStrings[2].trim());
									System.out.println(Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
									calendar.set(Calendar.SECOND, Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
								}else {
									int index = Integer.parseInt(second.substring(1, second.length()-1));
									calendar.set(Calendar.SECOND, Integer.parseInt(sp[index]));
								}
							}
							
							
						}
						
						
						strFieldVal="'" + TimeUtil.date2String(calendar.getTime(), "yyyy-MM-dd HH:mm:ss") + "'";
					}else {
//						dataTime.append(TimeUtil.date2String(TimeUtil.String2Date(strFieldVal.substring(1, strFieldVal.length()-1), "yyyyMMddHHmmss".substring(0, strFieldVal.length()-2)), "yyyy-MM-dd HH:mm:ss"));
						if(fields.get(i).getFirst().equals("V_FNTIME") || fields.get(i).getFirst().equals("V_FILETIME") ||fields.get(i).getFirst().equals("V_RETAIN1_C") )
//						System.out.println(fields.get(i).getFirst());
							if(!strFieldVal.equalsIgnoreCase("") && strFieldVal != null && !strFieldVal.equalsIgnoreCase("''") && strFieldVal.length() >=8) {
//								System.out.println(strFieldVal);
								if(isInteger(strFieldVal.substring(1, strFieldVal.length()-1))) {
									strFieldVal ="'" + TimeUtil.date2String(TimeUtil.String2Date(strFieldVal.substring(1, strFieldVal.length()-1), "yyyyMMddHHmmss".substring(0, strFieldVal.length()-2)), "yyyy-MM-dd HH:mm:ss") + "'";
								}
							}
					}
					
					
				}
				
				
				if(fields.get(i).getFirst().contains("V04002")) {
					if(strFieldVal.contains("YYYYDDD")) {
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						String[] itemStrings = strFieldVal.substring(10, strFieldVal.length()-2).split("[|]");
						for ( int n = 0; n < itemStrings.length; n++) {
							if(n == 0) {
								String yearString = itemStrings[0].trim();
								if(yearString.contains(":")) {
									String[] tempStrings = yearString.substring(1, yearString.length()-1).split(":");
									int index = Integer.parseInt(tempStrings[0].trim());
									int start_index = Integer.parseInt(tempStrings[1].trim());
									int end_index = Integer.parseInt(tempStrings[2].trim());
									calendar.set(Calendar.YEAR, Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
								}else {
									int index = Integer.parseInt(yearString.substring(1, yearString.length()-1));
									calendar.set(Calendar.YEAR, Integer.parseInt(sp[index]));
								}
							}else if (n == 1) {
								String day = itemStrings[1].trim();
								if(day.contains(":")) {
									String[] tempStrings = day.substring(1, day.length()-1).split(":");
									int index = Integer.parseInt(tempStrings[0].trim());
									int start_index = Integer.parseInt(tempStrings[1].trim());
									int end_index = Integer.parseInt(tempStrings[2].trim());
									System.out.println(Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
									calendar.set(Calendar.DAY_OF_YEAR, Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
								}else {
									int index = Integer.parseInt(day.substring(1, day.length()-1));
									calendar.set(Calendar.DAY_OF_YEAR, Integer.parseInt(sp[index]));
								}
							}
						}
						strFieldVal = "'" + (calendar.getTime().getMonth()+1) +"'";
					}
				}
				
				if(fields.get(i).getFirst().contains("V04003")) {
					if(strFieldVal.contains("YYYYDDD")) {
						
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						String[] itemStrings = strFieldVal.substring(10, strFieldVal.length()-2).split("[|]");
						for ( int n = 0; n < itemStrings.length; n++) {
							if(n == 0) {
								String yearString = itemStrings[0].trim();
								if(yearString.contains(":")) {
									String[] tempStrings = yearString.substring(1, yearString.length()-1).split(":");
									int index = Integer.parseInt(tempStrings[0].trim());
									int start_index = Integer.parseInt(tempStrings[1].trim());
									int end_index = Integer.parseInt(tempStrings[2].trim());
									calendar.set(Calendar.YEAR, Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
								}else {
									int index = Integer.parseInt(yearString.substring(1, yearString.length()-1));
									calendar.set(Calendar.YEAR, Integer.parseInt(sp[index]));
								}
							}else if (n == 1) {
								String day = itemStrings[1].trim();
								if(day.contains(":")) {
									String[] tempStrings = day.substring(1, day.length()-1).split(":");
									int index = Integer.parseInt(tempStrings[0].trim());
									int start_index = Integer.parseInt(tempStrings[1].trim());
									int end_index = Integer.parseInt(tempStrings[2].trim());
									System.out.println(Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
									calendar.set(Calendar.DAY_OF_YEAR, Integer.parseInt(sp[index].substring(start_index, start_index + end_index)));
								}else {
									int index = Integer.parseInt(day.substring(1, day.length()-1));
									calendar.set(Calendar.DAY_OF_YEAR, Integer.parseInt(sp[index]));
								}
							}
						}
						strFieldVal = "'" + calendar.getTime().getDate() +"'";
					}
				}
				
				if(fields.get(i).getFirst().contains("V04004") || fields.get(i).getFirst().contains("V04005")) {
					if(!isInteger(strFieldVal)) {
						strFieldVal = "''";
					}
				}
				//F.0054.0001.S001:G字段值=“G”，取固定值“GLB”；=“A”，固定值“NEHE”
				//F.0054.0001.S002:同上

				if((strSodType.equalsIgnoreCase("F.0054.0001.S001") ||strSodType.equalsIgnoreCase("F.0054.0001.S002"))&& fields.get(i).getFirst().equals("V05310")) {
					strFieldVal= strFieldVal.replace("G", "GLB").replace("A", "NEHE");
				}
				
				if((strSodType.equalsIgnoreCase("F.0049.0012.S001") || strSodType.equalsIgnoreCase("F.0049.0013.S001") || strSodType.equalsIgnoreCase("F.0049.0014.S001")) && fields.get(i).getFirst().equals("V_GRIB_VERSION")) {
					strFieldVal = strFieldVal.replace("grib", "1").replace("GRIB", "1").replace("nc", "").replace("NC", "");
				}
				
				//F.0054.0003.S001:LLL字段值，仅当LLL=000时，应取值1000。
				if(strSodType.equalsIgnoreCase("F.0054.0003.S001") && fields.get(i).getFirst().equals("V40410_1")) {
					strFieldVal= strFieldVal.replace("000", "1000");
				}
				
				if(strSodType.equalsIgnoreCase("M.0053.0002.S001") 
						|| strSodType.equalsIgnoreCase("A.0042.0008.S001")
						|| strSodType.equalsIgnoreCase("F.0042.0002.S001")
						|| strSodType.equalsIgnoreCase("F.0042.0002.S002")
						|| strSodType.equalsIgnoreCase("F.0042.0002.S003")
						|| strSodType.equalsIgnoreCase("F.0042.0002.S004")
						|| strSodType.equalsIgnoreCase("F.0042.0002.S005")){
					if(fields.get(i).getFirst().equals("D_DATETIME")|| fields.get(i).getFirst().equals("V_MAKETIME")|| fields.get(i).getFirst().equals("V_FNTIME")) {
						Calendar calendar = Calendar.getInstance();
						if(strFieldVal.contains("-") || strFieldVal.contains(":")) {
							Date date = TimeUtil.String2Date(strFieldVal.replace("'", ""), "yyyy-MM-dd HH:mm:ss");
							calendar.setTime(date);
							calendar.add(Calendar.HOUR_OF_DAY, -8);
						}else {
							Date date = TimeUtil.String2Date(strFieldVal.replace("'", ""), "yyyyMMddHHmmss");
							calendar.setTime(date);
							calendar.add(Calendar.HOUR_OF_DAY, -8);
						}
						
						strFieldVal="'" + TimeUtil.date2String(calendar.getTime(), "yyyy-MM-dd HH:mm:ss") + "'";
					}else if (fields.get(i).getFirst().equals("V_CCCC") && strFieldVal.contains("-")) {
						strFieldVal = "'"+strFieldVal.replace("'", "").split("-")[0] + "'";
					}else if (fields.get(i).getFirst().equals("V_RETAIN1_C") && strFieldVal.contains("-")) {
						strFieldVal = "'" + strFieldVal.replace("'", "").split("-")[1] +"'";
					}else if (fields.get(i).getFirst().equals("V_RETAIN1_C") && !strFieldVal.contains("-")) {
						strFieldVal = "''";
					}
				}
				
				
				if(strSodType.equalsIgnoreCase("K.0781.0001.S001")) {
					
					if(fields.get(i).getFirst().equals("V02019_01")) {
						strFieldVal = strFieldVal.replace("'", "");
						if(strFieldVal.length() == 8) {
							strFieldVal = "'"+strFieldVal.substring(0, 5) +"'";
						}else {
							strFieldVal = "'"+strFieldVal.substring(0, 4) +"'";
						}
					}
					
					if(fields.get(i).getFirst().equals("V01007_01")) {
						strFieldVal = strFieldVal.replace("'", "");
						if(strFieldVal.length() == 8) {
							strFieldVal = "'"+strFieldVal.substring(5) +"'";
						}else {
							strFieldVal = "'"+strFieldVal.substring(4) +"'";
						}
					}
				}
				
				if(fields.get(i).getFirst().equals("V_FILETIME") 
						|| fields.get(i).getFirst().equals("V_FNTIME")
						|| fields.get(i).getFirst().equals("V_RETAIN1_C") 
						|| fields.get(i).getFirst().equals("V_MAKETIME")) {
					strFieldVal= strFieldVal.replace("-", "").replace(":", "").replace(" ", "");
				}
				
				if(i!=fields.size()-1){
					strField = strField + fields.get(i).getFirst()+",";
					DecimalFormat decimalFormat=new DecimalFormat("#.#");
					if(strFieldVal.contains("*")) {
						int index = strFieldVal.indexOf("*");
						double temp_value = Double.parseDouble(strFieldVal.substring(0, index));
						double temp_scale = Double.parseDouble(strFieldVal.substring(index+1));
						strFieldVal = decimalFormat.format(temp_value*temp_scale);
//						strFieldVal = temp_value * temp_scale + "";
					}
					
					if (fields.get(i).getFirst().equalsIgnoreCase("D_DATETIME")) {
						strFieldVal = strFieldVal.replace("'", "").replace(" ", "").replace(":", "").replace("-", "");
						if (strFieldVal.length() == 12) {
							strFieldVal = strFieldVal+"00";
						}else if (strFieldVal.length() == 10) {
							strFieldVal = strFieldVal+"0000";
						}else if (strFieldVal.length() == 8){
							strFieldVal = strFieldVal+"000000";
						}else if (strFieldVal.length() == 6) {
							strFieldVal = strFieldVal + "01000000";
						}
						//TOAD：固定代码，表示日累计产品；AOAD：固定代码，表示日平均产品；AOFD：固定代码，表示候平均产品；AOTD：固定代码，表示旬平均产品；AOAM：固定代码，表示月平均产品
						strFieldVal = strFieldVal.replace("TOAD", "0000").replace("AOAD", "0000").replace("AOFD", "0000").replace("AOTD", "0000").replace("AOAM", "0000").replace("POAD", "0000");
						
						
						
						strFieldVal = TimeUtil.date2String(TimeUtil.String2Date(strFieldVal, "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss");

						dataTime.append(strFieldVal);
						System.out.println(dataTime.toString()+"+++++++++++++++++>>>>>>>>>>>>>>>>>>");
						if(!TimeCheckUtil.checkTime(TimeUtil.String2Date(strFieldVal, "yyyy-MM-dd HH:mm:ss"))) {
							
							logger.error("D_DATETIME ERROR :" + TimeUtil.String2Date(strFieldVal, "yyyy-MM-dd HH:mm:ss") );
							return "";
						}
						strFieldVal = "'" + strFieldVal + "'";
					}
					strVal = strVal + strFieldVal + ",";
				}
				else{
					strField = strField + fields.get(i).getFirst() + ")";
					strVal = strVal + strFieldVal + ")";
				}
			}
			sql = strField + strVal;		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("cts type:" + strCtsType + " get index sql error:" + e.getMessage());
			return "";
		}

		return sql;
	}
	
	public static boolean isInteger(String str) {  
		str = str.replace("'", "");
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
        return pattern.matcher(str).matches();  
	}

	public static String genNafpSql(String D_FILE_ID,String D_DATA_ID,
			String D_IYMDHM,
			String D_RYMDHM,
			String D_DATETIME,
			String D_FILE_SIZE,
			String D_STORAGE_SITE,
			String V_FNTIME,
			String V_PROD_SORT,
			String V_CCCC,
			String V_PROD_CONTENT,
			String V_PROD_SYSTEM,
			String V_PROD_CODE,
			String V05310,
			String V40410_1,
			String V40410_2,
			String V40410_3,
			String V_RETAIN1_C,
			String V_RETAIN2_C,
			String V_RETAIN3_C,
			String V_FILE_FORMAT,
			String V_FILE_NAME)
	{
		if(!TimeCheckUtil.checkTime(TimeUtil.String2Date(D_DATETIME.substring(0, 14), "yyyyMMddHHmmss"))) {
			logger.error("D_DATETIME ERROR :" + TimeUtil.String2Date(D_DATETIME.substring(0, 14), "yyyyMMddHHmmss") );
			return "";
		}
		String V40410_2_n ="" ,V40410_3_n="";
		if(V40410_2.startsWith("G"))
			V40410_3_n = V40410_2;
		else
			V40410_2_n = V40410_2;
		String sql = "INSERT INTO NAFP_GDAS_FILE_TAB(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,D_FILE_SIZE," 
		       + "D_STORAGE_SITE,V_FNTIME,V_PROD_SORT,V_CCCC,V_PROD_CONTENT,V_PROD_SYSTEM,V_PROD_CODE,"
			   + "V05310,V40410_1,V40410_2,V40410_3,V_RETAIN1_C,V_RETAIN2_C,V_RETAIN3_C,V_FILE_FORMAT,V_FILE_NAME) VALUES(";
		sql = sql +  "'"  + D_DATA_ID + "','" 
			+ TimeUtil.getSysTime() + "','" 
			+ TimeUtil.date2String(TimeUtil.String2Date(D_RYMDHM.substring(0, 14), "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss") + "','"
			+  TimeUtil.date2String(TimeUtil.String2Date(D_DATETIME.substring(0, 14), "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss") + "'," + 
			D_FILE_SIZE + ",'"  +
			D_STORAGE_SITE + "','"  + V_FNTIME.substring(0,14) + "','"  +
			V_PROD_SORT + "','"  +
			V_CCCC + "','"  +
			V_PROD_CONTENT + "','"  +
			V_PROD_SYSTEM + "','"  +
			V_PROD_CODE + "','"  +
			V05310 + "','"  +
			V40410_1 + "','"  +
			V40410_2_n + "','"  +
			V40410_3_n + "','"  +
			V_RETAIN1_C + "','"  +
			V_RETAIN2_C + "','"  +
			V_RETAIN3_C + "','"  +
			V_FILE_FORMAT + "','"  +
			V_FILE_NAME  + "')" ;
		
		return sql;
	}
	
//	public static String genRadaTempSql(String D_FILE_ID,
//			String D_DATA_ID,
//			String D_IYMDHM,
//			String D_RYMDHM,
//			String D_DATETIME,
//			String V_RADAR_MODEL,
//			String V_SCANNING_MODE,
//			String V01301,
//			String V_FNTIME,
//			String V_FILE_FORMAT,
//			String V_COMPRESS_METHOD,
//			String V_FILE_NAME,
//			String D_STORAGE_SITE,
//			String D_FILE_SIZE,
//			String V_RETAIN1_C,
//			String V_RETAIN2_C,
//			String V_RETAIN3_C)
//		{
//			String sql = "INSERT INTO RADA_CHN_DOR_L2_TEMP_F_TAB(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,V_RADAR_MODEL,"
//			    + "V_SCANNING_MODE,V01301,V_FNTIME,V_FILE_FORMAT,V_COMPRESS_METHOD,V_FILE_NAME,D_STORAGE_SITE,"
//			    + " D_FILE_SIZE,V_RETAIN1_C,V_RETAIN2_C,V_RETAIN3_C) VALUES(";
//			sql = sql +  "'"  + D_DATA_ID + "','" +
//				TimeUtil.getSysTime() + "','" +
//				D_RYMDHM.substring(0,14) + "','" +
//				D_DATETIME.substring(0,14) + "','" + 
//				V_RADAR_MODEL+ "','"  +
//				V_SCANNING_MODE+ "','"  +
//				V01301+ "','"  +
//				V_FNTIME+ "','"  +
//				V_FILE_FORMAT+ "','"  +
//				V_COMPRESS_METHOD+ "','"  +
//				V_FILE_NAME+ "','"  +
//				D_STORAGE_SITE+ "',"  +
//				D_FILE_SIZE+ ",'"  +
//				V_RETAIN1_C+ "','"  +
//				V_RETAIN2_C+ "','"  +
//				V_RETAIN3_C+ "')" ;
//			
//			return sql;
//		}

	public static String genRadaPupSql(String D_FILE_ID,String D_DATA_ID,String D_IYMDHM,String D_RYMDHM,String D_DATETIME,String D_STORAGE_SITE,String D_FILE_SIZE,
									   String V01301,String V_FNTIME,String V_RADAR_MODEL,String V_FILE_FORMAT,String V_PROD_TYPE,String V_DPI,String V_COVERING_AREA,
									   String V07021,String V_FILE_NAME,String V_RETAIN1_C,String V_RETAIN2_C,String V_RETAIN3_C,String D_SOURCE_ID,String tableName){
			if(!TimeCheckUtil.checkTime(TimeUtil.String2Date(D_DATETIME.substring(0, 14), "yyyyMMddHHmmss"))) {
				logger.error("D_DATETIME ERROR :" + TimeUtil.String2Date(D_DATETIME.substring(0, 14), "yyyyMMddHHmmss") );
				return "";
			}
		
			String temp_v07021=V07021;
			if(temp_v07021.equals("NUL"))
				temp_v07021="NULL";
			else {
				
				// 雷达仰角的转换
				float elevation = Float.parseFloat(V07021);
				if(elevation >= 0 && elevation < 2700) {
					temp_v07021 = (elevation * 0.1)+"";
				}else {
					temp_v07021 = (elevation * 0.1 - 360)+"";
				}
				// 
				temp_v07021 = (Float.parseFloat(V07021) * 0.1) +"";
			}
			String sql = "INSERT INTO "+tableName+"(D_FILE_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM," +
			       "D_DATETIME,D_STORAGE_SITE,D_FILE_SIZE,V01301,V_FNTIME,V_RADAR_MODEL,V_FILE_FORMAT," +
			       "V_PROD_TYPE,V_DPI,V_COVERING_AREA,V07021,V_FILE_NAME,V_RETAIN1_C,V_RETAIN2_C,D_SOURCE_ID,V_RETAIN3_C) VALUES(";
			sql = sql + "'" + D_FILE_ID + "','" +
				D_DATA_ID + "','" +
				TimeUtil.getSysTime() + "','" +
				TimeUtil.date2String(TimeUtil.String2Date(D_RYMDHM.substring(0, 14), "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss") + "','" +
				TimeUtil.date2String(TimeUtil.String2Date(D_DATETIME.substring(0, 14), "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss") + "','" + 
				D_STORAGE_SITE + "',"  +
				D_FILE_SIZE + ",'"  +
				V01301 + "','"  +
				V_FNTIME.substring(0,14) + "','"  +
				V_RADAR_MODEL + "','"  +
				V_FILE_FORMAT + "','"  +
				V_PROD_TYPE + "','"  +
				V_DPI + "','"  +
				V_COVERING_AREA + "',"  +
				temp_v07021 + ",'"  +
				V_FILE_NAME + "','"  +
				V_RETAIN1_C + "','"  +
				V_RETAIN2_C + "','"  +
				D_SOURCE_ID + "','"  +
				V_RETAIN3_C + "')" ;
			
			return sql;
		}

	public static String genRadaWprdSql(String D_FILE_ID,
				String D_DATA_ID,
				String D_IYMDHM,
				String D_RYMDHM,
				String D_DATETIME,
				String D_STORAGE_SITE,
				String D_FILE_SIZE,
				String V_FNTIME,
				String V_RADAR_MODEL,
				String V01301,
				String V_PROD_SORT,
				String V_PROD_CONTENT,
				String V_FILE_FORMAT,
				String V_COMPRESS_METHOD,
				String V_FILE_NAME,
				String V_RETAIN1_C,
				String V_RETAIN2_C,
				String V_RETAIN3_C,
				String D_SOURCE_ID
				)
		{
		
			if(!TimeCheckUtil.checkTime(TimeUtil.String2Date(D_DATETIME.substring(0, 14), "yyyyMMddHHmmss"))) {
				logger.error("D_DATETIME ERROR :" + TimeUtil.String2Date(D_DATETIME.substring(0, 14), "yyyyMMddHHmmss") );
				return "";
			}
			String sql = "INSERT INTO RADA_CHN_WPRD_FILE_TAB(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME," +
			       "D_STORAGE_SITE,D_FILE_SIZE,V_FNTIME,V_RADAR_MODEL,V01301,V_PROD_SORT,V_PROD_CONTENT," +
			       "V_FILE_FORMAT,V_COMPRESS_METHOD,V_FILE_NAME,V_RETAIN1_C,V_RETAIN2_C,V_RETAIN3_C) VALUES(";
			sql = sql +  "'"  + D_DATA_ID + "','" +
				TimeUtil.getSysTime() + "','" +
				TimeUtil.date2String(TimeUtil.String2Date(D_RYMDHM.substring(0, 14), "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss") + "','" +
				TimeUtil.date2String(TimeUtil.String2Date(D_DATETIME.substring(0, 14), "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss") + "','" + 
				D_STORAGE_SITE + "',"  +
				D_FILE_SIZE + ",'"  +
				V_FNTIME.substring(0,14) + "','"  +
				V_RADAR_MODEL + "','"  +
				V01301 + "','"  +
				V_PROD_SORT + "','"  +
				V_PROD_CONTENT + "','"  +
				V_FILE_FORMAT + "','"  +
				V_COMPRESS_METHOD + "','"  +
				V_FILE_NAME + "','"  +
				V_RETAIN1_C + "','"  +
				V_RETAIN2_C + "','"  +
				D_SOURCE_ID + "','"  +
				V_RETAIN3_C + "')" ;
			
			return sql;
		}	

}
