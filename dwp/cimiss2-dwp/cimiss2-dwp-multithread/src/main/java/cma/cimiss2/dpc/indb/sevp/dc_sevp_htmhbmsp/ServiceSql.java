package cma.cimiss2.dpc.indb.sevp.dc_sevp_htmhbmsp;

import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Vector;

/**
 * 
 * <br>
 * @Title:  ServiceSql.java   
 * @Package org.cimiss2.dwp.RADAR.service   
 * @Description:    TODO(生成sql语句)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月15日 下午5:42:05   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
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
	public static String genIndexSql(String strTabName, String strCtsType, String strSodType, 
			String[] sp,String startTime,String srcFilename, String filesize,
			String recvtime,String newpath,StringBuffer dataTime)
	{
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
			for(int i=0;i<fields.size();i++)
			{
				int begin,end;
				strFieldVal=fields.get(i).getSecond().trim();
				if(strFieldVal.contains("$"))
				{
					while(strFieldVal.contains("$"))
					{
						begin=strFieldVal.indexOf("$");
						end=strFieldVal.indexOf("}");
						String var=strFieldVal.substring(begin, end + 1);
						
						
						
						
//						if(var.contains("${10}")){//用于测试
//							strFieldVal = "'test'";
//						}
						if(var.contains(":"))
						{
							if((var.contains("${")) && (var.contains("}")))
							{
								String[] index = strFieldVal.substring(begin+2, end).split(":");
								for(int m=0; m<index.length; m++) {
									iInxArray[m] = Integer.parseInt(index[m]);
								}
								strFieldVal = strFieldVal.replace(var,sp[iInxArray[0]].substring(iInxArray[1],iInxArray[1]+iInxArray[2]));
							}
							else
							{
								logger.error("cts type:" + strCtsType + " wrong format of the index config\n");
								return "";
							}
						}
						else
						{
							if(var.contains("${10}")&&sp[iInxArray[0]].length()<10){//用于测试，判断压缩格式
								strFieldVal = "''";
							}else{
								iInxArray[0] = Integer.parseInt(strFieldVal.substring(begin+2,end));
								strFieldVal=strFieldVal.replace(var, sp[iInxArray[0]]);
							}
							
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
				else if(strFieldVal.equalsIgnoreCase("source"))//用于测试
					strFieldVal="'"+"测试"+"'";
				else if(strFieldVal.equalsIgnoreCase("test"))//用于测试
					strFieldVal="'"+"测试"+"'";
				else if(strFieldVal.equals("sysdate"))
				{
					strFieldVal="'"+TimeUtil.getSysTime()+"'";
				}
				else if(strFieldVal.equals("\"\"") || strFieldVal.isEmpty() )
				{
					strFieldVal ="''";
				}
				else
					strFieldVal=fields.get(i).getSecond();

				if (fields.get(i).getFirst().equals("D_DATETIME")) {
					dataTime.append(TimeUtil.date2String(TimeUtil.String2Date(strFieldVal.substring(1, strFieldVal.length()-1), "yyyyMMddHHmmss".substring(0, strFieldVal.length()-2)), "yyyy-MM-dd HH:mm:ss"));
					strFieldVal="'" + dataTime + "'";
				}
				
				if(fields.get(i).getFirst().equals("V_FILETIME")) {
					strFieldVal="'" + TimeUtil.date2String(TimeUtil.String2Date(strFieldVal.substring(1, strFieldVal.length()-1), "yyyyMMddHHmmss".substring(0, strFieldVal.length()-2)), "yyyy-MM-dd HH:mm:ss") + "'";
				}
				
				
				if(i!=fields.size()-1)
				{
					strField = strField + fields.get(i).getFirst()+",";
					strVal = strVal + strFieldVal + ",";
				}
				else
				{
					strField = strField + fields.get(i).getFirst() + ")";
					strVal = strVal + strFieldVal + ")";
				}
			}
			sql = strField + strVal;		
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("cts type:" + strCtsType + " get index sql error:" + e.getMessage());
			return "";
		}

		return sql;
	}
	
	/**
	 * @param strTabName  索引库表名
	 * @param strCtsType  cts 四级编码
	 * @param strSodType  sod 四级编码
	 * @param sp          文件名分割数据
	 * @param startTime   开始处理时间
	 * @param srcFilename 源文件名
	 * @param filesize    文件大小
	 * @param recvtime    接收时间
	 * @param newpath     文件存储路径
	 * @param dataTime    资料时间
	 * @return
	 */
	//地面资料
	public static String genIndexOtherSqldimian(String strTabName, String strCtsType, String strSodType,
										   String[] sp, String startTime, String srcFilename, String filesize,
										   String recvtime, String newpath, StringBuffer dataTime) {
		String newFileName = new File(newpath).getName();
		//sql语句
		String sql = "";
		try {
//			Vector<Pair<String, String>> fields = IndexConf.sm_indexConf.get(strCtsType);
			// 加载索引库策略的配置文件
			Vector<Pair<String, String>> fields = IndexConf.sm_indexConf.get(strCtsType);
			if (fields == null) {
				logger.error("can not find cts type:" + strCtsType + " in the index config file.");
				return "";
			}
			//索引截取位置
			int[] iInxArray = new int[3];
			String strField = "insert into " + strTabName + "(";
			String strVal = "values(";
			String strFieldVal; //字段值
			for (int i = 0; i < fields.size(); i++) {
				int begin, end;
				strFieldVal = fields.get(i).getSecond().trim();
				if (strFieldVal.contains("$")) {
					while (strFieldVal.contains("$")) {
						begin = strFieldVal.indexOf("$");
						end = strFieldVal.indexOf("}");
						String var = strFieldVal.substring(begin, end + 1);

						if (var.contains(":")) {
							if ((var.contains("${")) && (var.contains("}"))) {
								String[] index = strFieldVal.substring(begin + 2, end).split(":");
								for (int m = 0; m < index.length; m++) {
									iInxArray[m] = Integer.parseInt(index[m]);
								}
								strFieldVal = strFieldVal.replace(var, sp[iInxArray[0]].substring(iInxArray[1], iInxArray[1] + iInxArray[2]));
							} else {
								logger.error("cts type:" + strCtsType + " wrong format of the index config\n");
								return "";
							}
						} else {
							iInxArray[0] = Integer.parseInt(strFieldVal.substring(begin + 2, end));
							strFieldVal = strFieldVal.replace(var, sp[iInxArray[0]]);

						}
					}
				} else if (strFieldVal.equalsIgnoreCase("d_file_id")) {
					strFieldVal = "'" + newFileName + "'";
				} else if (strFieldVal.equalsIgnoreCase("cts_type")) {
					strFieldVal = "'" + strCtsType + "'";
				} else if (strFieldVal.equalsIgnoreCase("sod_type")) {
					strFieldVal = "'" + strSodType + "'";
				} else if (strFieldVal.equalsIgnoreCase("start_time")) {
					strFieldVal = "'" + startTime + "'";
//                } else if (strFieldVal.equalsIgnoreCase("end_time")) {
//					strFieldVal="'"+endTime+"'";
				} else if (strFieldVal.equalsIgnoreCase("filename")) {
					strFieldVal = "'" + newFileName + "'";
				} else if (strFieldVal.equalsIgnoreCase("srcfilename")) {
					strFieldVal = "'" + srcFilename + "'";
				} else if (strFieldVal.equalsIgnoreCase("filesize")) {
					strFieldVal = filesize;
				} else if (strFieldVal.equalsIgnoreCase("recvtime")) {
					strFieldVal = "'" + recvtime + "'";
				} else if (strFieldVal.equalsIgnoreCase("newpath")) {
					strFieldVal = "'" + newpath + "'";
				} else if (strFieldVal.equals("sysdate")) {
					strFieldVal = "'" + TimeUtil.getSysTime() + "'";
				} else if (strFieldVal.equals("\"\"") || strFieldVal.isEmpty()) {
					strFieldVal = "''";
				} else {
					strFieldVal = fields.get(i).getSecond();
				}
				if (fields.get(i).getFirst().equals("D_DATETIME") && (strFieldVal.length()<=14)) {
					dataTime.append(TimeUtil.date2String(TimeUtil.String2Date(strFieldVal.substring(1, strFieldVal.length() - 1), "yyyyMMddHHmmss".substring(0, strFieldVal.length() - 2)), "yyyy-MM-dd HH:mm:ss"));
					strFieldVal = "'" + dataTime + "'";
				}

				if (fields.get(i).getFirst().equals("V_FILETIME") && (strFieldVal.length() <= 14)) {
					strFieldVal = "'" + TimeUtil.date2String(TimeUtil.String2Date(strFieldVal.substring(1, strFieldVal.length() - 1), "yyyyMMddHHmmss".substring(0, strFieldVal.length() - 2)), "yyyy-MM-dd HH:mm:ss") + "'";
				}


				if (i != fields.size() - 1) {
					strField = strField + fields.get(i).getFirst() + ",";
					if(fields.get(i).getFirst().equalsIgnoreCase("V40410_3")){
						strFieldVal = sp[7]+"."+sp[8];
					}
					strVal = strVal + strFieldVal + ",";
				} else {
					strField = strField + fields.get(i).getFirst() + ")";
					strVal = strVal + strFieldVal + ")";
				}
			}
			sql = strField + strVal;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("cts type:" + strCtsType + " get index sql error:" + e.getMessage());
			e.printStackTrace();
			return "";
		}

		return sql;
	}
	//雷达资料
	public static String genIndexOtherSqls(String strTabName, String strCtsType, String strSodType,
										  String[] sp, String startTime, String srcFilename, String filesize,
										  String recvtime, String newpath, StringBuffer dataTime) {
		String newFileName = new File(newpath).getName();
		//sql语句
		String sql = "";
		try {
//			Vector<Pair<String, String>> fields = IndexConf.sm_indexConf.get(strCtsType);
			// 加载索引库策略的配置文件
			Vector<Pair<String, String>> fields = IndexConf.sm_indexConf.get(strCtsType);
			if (fields == null) {
				logger.error("can not find cts type:" + strCtsType + " in the index config file.");
				return "";
			}
			//索引截取位置
			int[] iInxArray = new int[3];
			String strField = "insert into " + strTabName + "(";
			String strVal = "values(";
			String strFieldVal; //字段值
			for (int i = 0; i < fields.size(); i++) {
				int begin, end;
				strFieldVal = fields.get(i).getSecond().trim();
				if (strFieldVal.contains("$")) {
					while (strFieldVal.contains("$")) {
						begin = strFieldVal.indexOf("$");
						end = strFieldVal.indexOf("}");
						String var = strFieldVal.substring(begin, end + 1);

						if (var.contains(":")) {
							if ((var.contains("${")) && (var.contains("}"))) {
								String[] index = strFieldVal.substring(begin + 2, end).split(":");
								for (int m = 0; m < index.length; m++) {
									iInxArray[m] = Integer.parseInt(index[m]);
								}
								strFieldVal = strFieldVal.replace(var, sp[iInxArray[0]].substring(iInxArray[1], iInxArray[1] + iInxArray[2]));
							} else {
								logger.error("cts type:" + strCtsType + " wrong format of the index config\n");
								return "";
							}
						} else {
							iInxArray[0] = Integer.parseInt(strFieldVal.substring(begin + 2, end));
							strFieldVal = strFieldVal.replace(var, sp[iInxArray[0]]);

						}
					}
				} else if (strFieldVal.equalsIgnoreCase("d_file_id")) {
					strFieldVal = "'" + newFileName + "'";
				} else if (strFieldVal.equalsIgnoreCase("cts_type")) {
					strFieldVal = "'" + strCtsType + "'";
				} else if (strFieldVal.equalsIgnoreCase("sod_type")) {
					strFieldVal = "'" + strSodType + "'";
				} else if (strFieldVal.equalsIgnoreCase("start_time")) {
					strFieldVal = "'" + startTime + "'";
//                } else if (strFieldVal.equalsIgnoreCase("end_time")) {
//					strFieldVal="'"+endTime+"'";
				} else if (strFieldVal.equalsIgnoreCase("filename")) {
					strFieldVal = "'" + newFileName + "'";
				} else if (strFieldVal.equalsIgnoreCase("srcfilename")) {
					strFieldVal = "'" + srcFilename + "'";
				} else if (strFieldVal.equalsIgnoreCase("filesize")) {
					strFieldVal = filesize;
				} else if (strFieldVal.equalsIgnoreCase("recvtime")) {
					strFieldVal = "'" + recvtime + "'";
				} else if (strFieldVal.equalsIgnoreCase("newpath")) {
					strFieldVal = "'" + newpath + "'";
				} else if (strFieldVal.equals("sysdate")) {
					strFieldVal = "'" + TimeUtil.getSysTime() + "'";
				} else if (strFieldVal.equals("\"\"") || strFieldVal.isEmpty()) {
					strFieldVal = "''";
				} else {
					strFieldVal = fields.get(i).getSecond();
				}
				if (fields.get(i).getFirst().equals("D_DATETIME") && (strFieldVal.length()<=14)) {
					dataTime.append(TimeUtil.date2String(TimeUtil.String2Date(strFieldVal.substring(1, strFieldVal.length() - 1), "yyyyMMddHHmmss".substring(0, strFieldVal.length() - 2)), "yyyy-MM-dd HH:mm:ss"));
					strFieldVal = "'" + dataTime + "'";
				}

				if (fields.get(i).getFirst().equals("V_FILETIME") && (strFieldVal.length() <= 14)) {
					strFieldVal = "'" + TimeUtil.date2String(TimeUtil.String2Date(strFieldVal.substring(1, strFieldVal.length() - 1), "yyyyMMddHHmmss".substring(0, strFieldVal.length() - 2)), "yyyy-MM-dd HH:mm:ss") + "'";
				}


				if (i != fields.size() - 1) {
					strField = strField + fields.get(i).getFirst() + ",";
					if(fields.get(i).getFirst().equalsIgnoreCase("V04001")){
						strFieldVal = sp[1].substring(0,4);
					}
					if(fields.get(i).getFirst().equalsIgnoreCase("V04002")){
						strFieldVal = sp[1].substring(4,6);
					}
					if(fields.get(i).getFirst().equalsIgnoreCase("V04003")){
						strFieldVal = sp[1].substring(6,8);
					}
					if(fields.get(i).getFirst().equalsIgnoreCase("V_FILE_FORMAT")){
						if(sp[3].equals("AR2")){
							strFieldVal = "'AR2'";
						}
					}
					strVal = strVal + strFieldVal + ",";
				} else {
					strField = strField + fields.get(i).getFirst() + ")";
					strVal = strVal + strFieldVal + ")";
				}
			}
			sql = strField + strVal;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("cts type:" + strCtsType + " get index sql error:" + e.getMessage());
			e.printStackTrace();
			return "";
		}

		return sql;
	}
	//数值分析
	public static String genIndexOtherSql(String strTabName, String strCtsType, String strSodType,
										  String[] sp, String startTime, String srcFilename, String filesize,
										  String recvtime, String newpath, StringBuffer dataTime) {
		String newFileName = new File(newpath).getName();
		//sql语句
		String sql = "";
		try {
//			Vector<Pair<String, String>> fields = IndexConf.sm_indexConf.get(strCtsType);
			// 加载索引库策略的配置文件
			Vector<Pair<String, String>> fields = IndexConf.sm_indexConf.get(strCtsType);
			if (fields == null) {
				logger.error("can not find cts type:" + strCtsType + " in the index config file.");
				return "";
			}
			//索引截取位置
			int[] iInxArray = new int[3];
			String strField = "insert into " + strTabName + "(";
			String strVal = "values(";
			String strFieldVal; //字段值
			for (int i = 0; i < fields.size(); i++) {
				int begin, end;
				strFieldVal = fields.get(i).getSecond().trim();
				if (strFieldVal.contains("$")) {
					while (strFieldVal.contains("$")) {
						begin = strFieldVal.indexOf("$");
						end = strFieldVal.indexOf("}");
						String var = strFieldVal.substring(begin, end + 1);

						if (var.contains(":")) {
							if ((var.contains("${")) && (var.contains("}"))) {
								String[] index = strFieldVal.substring(begin + 2, end).split(":");
								for (int m = 0; m < index.length; m++) {
									iInxArray[m] = Integer.parseInt(index[m]);
								}
								strFieldVal = strFieldVal.replace(var, sp[iInxArray[0]].substring(iInxArray[1], iInxArray[1] + iInxArray[2]));
							} else {
								logger.error("cts type:" + strCtsType + " wrong format of the index config\n");
								return "";
							}
						} else {
							iInxArray[0] = Integer.parseInt(strFieldVal.substring(begin + 2, end));
							strFieldVal = strFieldVal.replace(var, sp[iInxArray[0]]);

						}
					}
				} else if (strFieldVal.equalsIgnoreCase("d_file_id")) {
					strFieldVal = "'" + newFileName + "'";
				} else if (strFieldVal.equalsIgnoreCase("cts_type")) {
					strFieldVal = "'" + strCtsType + "'";
				} else if (strFieldVal.equalsIgnoreCase("sod_type")) {
					strFieldVal = "'" + strSodType + "'";
				} else if (strFieldVal.equalsIgnoreCase("start_time")) {
					strFieldVal = "'" + startTime + "'";
//                } else if (strFieldVal.equalsIgnoreCase("end_time")) {
//					strFieldVal="'"+endTime+"'";
				} else if (strFieldVal.equalsIgnoreCase("filename")) {
					strFieldVal = "'" + newFileName + "'";
				} else if (strFieldVal.equalsIgnoreCase("srcfilename")) {
					strFieldVal = "'" + srcFilename + "'";
				} else if (strFieldVal.equalsIgnoreCase("filesize")) {
					strFieldVal = filesize;
				} else if (strFieldVal.equalsIgnoreCase("recvtime")) {
					strFieldVal = "'" + recvtime + "'";
				} else if (strFieldVal.equalsIgnoreCase("newpath")) {
					strFieldVal = "'" + newpath + "'";
				} else if (strFieldVal.equals("sysdate")) {
					strFieldVal = "'" + TimeUtil.getSysTime() + "'";
				} else if (strFieldVal.equals("\"\"") || strFieldVal.isEmpty()) {
					strFieldVal = "''";
				} else {
					strFieldVal = fields.get(i).getSecond();
				}
				if (fields.get(i).getFirst().equals("D_DATETIME") && (strFieldVal.length()<=14)) {
					dataTime.append(TimeUtil.date2String(TimeUtil.String2Date(strFieldVal.substring(1, strFieldVal.length() - 1), "yyyyMMddHHmmss".substring(0, strFieldVal.length() - 2)), "yyyy-MM-dd HH:mm:ss"));
					strFieldVal = "'" + dataTime + "'";
				}

				if (fields.get(i).getFirst().equals("V_FILETIME") && (strFieldVal.length() <= 14)) {
					strFieldVal = "'" + TimeUtil.date2String(TimeUtil.String2Date(strFieldVal.substring(1, strFieldVal.length() - 1), "yyyyMMddHHmmss".substring(0, strFieldVal.length() - 2)), "yyyy-MM-dd HH:mm:ss") + "'";
				}


				if (i != fields.size() - 1) {
					if(sp[4].length() == 10){
						if(srcFilename.endsWith(".grd")){
							if(fields.get(i).getFirst().equalsIgnoreCase("D_DATETIME")){
								strFieldVal = sp[4];
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04001")){
								strFieldVal = sp[4].substring(0,4);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04002")){
								strFieldVal = sp[4].substring(4,6);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04003")){
								strFieldVal = sp[4].substring(6,8);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04004")){
								strFieldVal = sp[4].substring(8,10);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V_FILE_FORMAT")){
								if(sp[10].equals("grd")){
									strFieldVal = "'grd'";
								}
							}
						}else if(srcFilename.endsWith(".gz")){
							if(fields.get(i).getFirst().equalsIgnoreCase("D_DATETIME")){
								strFieldVal = sp[4];
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04001")){
								strFieldVal = sp[4].substring(0,4);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04002")){
								strFieldVal = sp[4].substring(4,6);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04003")){
								strFieldVal = sp[4].substring(6,8);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04004")){
								strFieldVal = sp[4].substring(8,10);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V_FILE_FORMAT")){
								if(sp[10].equals("gz")){
									strFieldVal = "'gz'";
								}
							}
						}else if(srcFilename.endsWith(".bz2")){
							if(fields.get(i).getFirst().equalsIgnoreCase("D_DATETIME")){
								strFieldVal = sp[4];
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04001")){
								strFieldVal = sp[4].substring(0,4);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04002")){
								strFieldVal = sp[4].substring(4,6);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04003")){
								strFieldVal = sp[4].substring(6,8);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04004")){
								strFieldVal = sp[4].substring(8,10);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V_FILE_FORMAT")){
								if(sp[11].equals("bz2")){
									strFieldVal = "'bz2'";
								}
							}
						}else if(srcFilename.endsWith(".ctl")){
							if(fields.get(i).getFirst().equalsIgnoreCase("D_DATETIME")){
								strFieldVal = sp[4];
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04001")){
								strFieldVal = sp[4].substring(0,4);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04002")){
								strFieldVal = sp[4].substring(4,6);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04003")){
								strFieldVal = sp[4].substring(6,8);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04004")){
								strFieldVal = sp[4].substring(8,10);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V_FILE_FORMAT")){
								if(sp[9].equals("ctl")){
									strFieldVal = "'ctl'";
								}
							}
						}
					}else {
						if(srcFilename.endsWith(".grd")){
							if(fields.get(i).getFirst().equalsIgnoreCase("D_DATETIME")){
								strFieldVal = sp[4];
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04001")){
								strFieldVal = sp[4].substring(0,4);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04002")){
								strFieldVal = sp[4].substring(4,6);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04003")){
								strFieldVal = sp[4].substring(6,8);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04004")){
								strFieldVal = sp[4].substring(8,10);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04005")){
								strFieldVal = sp[4].substring(10,12);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04006")){
								strFieldVal = sp[4].substring(12,14);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V_FILE_FORMAT")){
								if(sp[10].equals("grd")){
									strFieldVal = "'grd'";
								}
							}
						}else if(srcFilename.endsWith(".gz")){
							if(fields.get(i).getFirst().equalsIgnoreCase("D_DATETIME")){
								strFieldVal = sp[4];
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04001")){
								strFieldVal = sp[4].substring(0,4);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04002")){
								strFieldVal = sp[4].substring(4,6);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04003")){
								strFieldVal = sp[4].substring(6,8);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04004")){
								strFieldVal = sp[4].substring(8,10);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04004")){
								strFieldVal = sp[4].substring(8,10);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04005")){
								strFieldVal = sp[4].substring(10,12);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04006")){
								strFieldVal = sp[4].substring(12,14);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V_FILE_FORMAT")){
								if(sp[11].equals("gz")){
									strFieldVal = "'gz'";
								}
							}

						}else if(srcFilename.endsWith(".bz2")){
							if(fields.get(i).getFirst().equalsIgnoreCase("D_DATETIME")){
								strFieldVal = sp[4];
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04001")){
								strFieldVal = sp[4].substring(0,4);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04002")){
								strFieldVal = sp[4].substring(4,6);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04003")){
								strFieldVal = sp[4].substring(6,8);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04004")){
								strFieldVal = sp[4].substring(8,10);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04004")){
								strFieldVal = sp[4].substring(8,10);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04005")){
								strFieldVal = sp[4].substring(10,12);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04006")){
								strFieldVal = sp[4].substring(12,14);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V_FILE_FORMAT")){
								if(sp[11].equals("bz2")){
									strFieldVal = "'bz2'";
								}
							}
						}else if(srcFilename.endsWith(".ctl")){
							if(fields.get(i).getFirst().equalsIgnoreCase("D_DATETIME")){
								strFieldVal = sp[4];
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04001")){
								strFieldVal = sp[4].substring(0,4);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04002")){
								strFieldVal = sp[4].substring(4,6);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04003")){
								strFieldVal = sp[4].substring(6,8);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04004")){
								strFieldVal = sp[4].substring(8,10);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04004")){
								strFieldVal = sp[4].substring(8,10);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04005")){
								strFieldVal = sp[4].substring(10,12);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V04006")){
								strFieldVal = sp[4].substring(12,14);
							}
							if(fields.get(i).getFirst().equalsIgnoreCase("V_FILE_FORMAT")){
								if(sp[9].equals("ctl")){
									strFieldVal = "'ctl'";
								}
							}
						}
					}

					strField = strField + fields.get(i).getFirst() + ",";
					strVal = strVal + strFieldVal + ",";
				} else {
					strField = strField + fields.get(i).getFirst() + ")";
					strVal = strVal + strFieldVal + ")";
				}
			}
			sql = strField + strVal;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("cts type:" + strCtsType + " get index sql error:" + e.getMessage());
			e.printStackTrace();
			return "";
		}

		return sql;
	}

	public static String genIndexOtherSql123(String strTabName, String strCtsType, String strSodType,
										  String[] sp, String startTime, String srcFilename, String filesize,
										  String recvtime, String newpath, StringBuffer dataTime) {
		String newFileName = new File(newpath).getName();
		//sql语句
		String sql = "";
		try {
//			Vector<Pair<String, String>> fields = IndexConf.sm_indexConf.get(strCtsType);
			// 加载索引库策略的配置文件
			Vector<Pair<String, String>> fields = IndexConf.sm_indexConf.get(strCtsType);
			if (fields == null) {
				logger.error("can not find cts type:" + strCtsType + " in the index config file.");
				return "";
			}
			//索引截取位置
			int[] iInxArray = new int[3];
			String strField = "insert into " + strTabName + "(";
			String strVal = "values(";
			String strFieldVal; //字段值
			for (int i = 0; i < fields.size(); i++) {
				int begin, end;
				strFieldVal = fields.get(i).getSecond().trim();
				if (strFieldVal.contains("$")) {
					while (strFieldVal.contains("$")) {
						begin = strFieldVal.indexOf("$");
						end = strFieldVal.indexOf("}");
						String var = strFieldVal.substring(begin, end + 1);

						if (var.contains(":")) {
							if ((var.contains("${")) && (var.contains("}"))) {
								String[] index = strFieldVal.substring(begin + 2, end).split(":");
								for (int m = 0; m < index.length; m++) {
									iInxArray[m] = Integer.parseInt(index[m]);
								}
								strFieldVal = strFieldVal.replace(var, sp[iInxArray[0]].substring(iInxArray[1], iInxArray[1] + iInxArray[2]));
							} else {
								logger.error("cts type:" + strCtsType + " wrong format of the index config\n");
								return "";
							}
						} else {
							iInxArray[0] = Integer.parseInt(strFieldVal.substring(begin + 2, end));
							strFieldVal = strFieldVal.replace(var, sp[iInxArray[0]]);

						}
					}
				} else if (strFieldVal.equalsIgnoreCase("d_file_id")) {
					strFieldVal = "'" + newFileName + "'";
				} else if (strFieldVal.equalsIgnoreCase("cts_type")) {
					strFieldVal = "'" + strCtsType + "'";
				} else if (strFieldVal.equalsIgnoreCase("sod_type")) {
					strFieldVal = "'" + strSodType + "'";
				} else if (strFieldVal.equalsIgnoreCase("start_time")) {
					strFieldVal = "'" + startTime + "'";
//                } else if (strFieldVal.equalsIgnoreCase("end_time")) {
//					strFieldVal="'"+endTime+"'";
				} else if (strFieldVal.equalsIgnoreCase("filename")) {
					strFieldVal = "'" + newFileName + "'";
				} else if (strFieldVal.equalsIgnoreCase("srcfilename")) {
					strFieldVal = "'" + srcFilename + "'";
				} else if (strFieldVal.equalsIgnoreCase("filesize")) {
					strFieldVal = filesize;
				} else if (strFieldVal.equalsIgnoreCase("recvtime")) {
					strFieldVal = "'" + recvtime + "'";
				} else if (strFieldVal.equalsIgnoreCase("newpath")) {
					strFieldVal = "'" + newpath + "'";
				} else if (strFieldVal.equals("sysdate")) {
					strFieldVal = "'" + TimeUtil.getSysTime() + "'";
				} else if (strFieldVal.equals("\"\"") || strFieldVal.isEmpty()) {
					strFieldVal = "''";
				} else {
					strFieldVal = fields.get(i).getSecond();
				}
				if (fields.get(i).getFirst().equals("D_DATETIME") && (strFieldVal.length()<=14)) {
					dataTime.append(TimeUtil.date2String(TimeUtil.String2Date(strFieldVal.substring(1, strFieldVal.length() - 1), "yyyyMMddHHmmss".substring(0, strFieldVal.length() - 2)), "yyyy-MM-dd HH:mm:ss"));
					strFieldVal = "'" + dataTime + "'";
				}

				if (fields.get(i).getFirst().equals("V_FILETIME") && (strFieldVal.length() <= 14)) {
					strFieldVal = "'" + TimeUtil.date2String(TimeUtil.String2Date(strFieldVal.substring(1, strFieldVal.length() - 1), "yyyyMMddHHmmss".substring(0, strFieldVal.length() - 2)), "yyyy-MM-dd HH:mm:ss") + "'";
				}


				if (i != fields.size() - 1) {
					strField = strField + fields.get(i).getFirst() + ",";
					strVal = strVal + strFieldVal + ",";
				} else {
					strField = strField + fields.get(i).getFirst() + ")";
					strVal = strVal + strFieldVal + ")";
				}
			}
			sql = strField + strVal;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("cts type:" + strCtsType + " get index sql error:" + e.getMessage());
			e.printStackTrace();
			return "";
		}

		return sql;
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

	public static String genRadaPupSql(String D_FILE_ID,
				String D_DATA_ID,
				String D_IYMDHM,
				String D_RYMDHM,
				String D_DATETIME,
				String D_STORAGE_SITE,
				String D_FILE_SIZE,
				String V01301,
				String V_FNTIME,
				String V_RADAR_MODEL,
				String V_FILE_FORMAT,
				String V_PROD_TYPE,
				String V_DPI,
				String V_COVERING_AREA,
				String V07021,
				String V_FILE_NAME,
				String V_RETAIN1_C,
				String V_RETAIN2_C,
				String V_RETAIN3_C
				)
		{
			String temp_v07021=V07021;
			if(temp_v07021.equals("NUL"))
				temp_v07021="NULL";
			String sql = "INSERT INTO RADA_CHN_DOR_L3_PUP_FILE_TAB(D_FILE_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM," +
			       "D_DATETIME,D_STORAGE_SITE,D_FILE_SIZE,V01301,V_FNTIME,V_RADAR_MODEL,V_FILE_FORMAT," +
			       "V_PROD_TYPE,V_DPI,V_COVERING_AREA,V07021,V_FILE_NAME,V_RETAIN1_C,V_RETAIN2_C,V_RETAIN3_C) VALUES(";
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
				String V_RETAIN3_C
				)
		{
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
				V_RETAIN3_C + "')" ;
			
			return sql;
		}	

}
