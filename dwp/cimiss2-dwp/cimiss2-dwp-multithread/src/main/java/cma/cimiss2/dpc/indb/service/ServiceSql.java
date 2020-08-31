package cma.cimiss2.dpc.indb.service;

import cma.cimiss2.dpc.indb.common.IndexConf;
import cma.cimiss2.dpc.indb.vo.Pair;
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
//					if(fields.get(i).getFirst().equalsIgnoreCase("V04001")){
//						strFieldVal = sp[1].substring(0,4);
//					}
//					if(fields.get(i).getFirst().equalsIgnoreCase("V04002")){
//						strFieldVal = sp[1].substring(4,6);
//					}
//					if(fields.get(i).getFirst().equalsIgnoreCase("V04003")){
//						strFieldVal = sp[1].substring(6,8);
//					}
//					if(fields.get(i).getFirst().equalsIgnoreCase("V_FILE_FORMAT")){
//						if(sp[3].equals("AR2")){
//							strFieldVal = "'AR2'";
//						}
//					}
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
}
