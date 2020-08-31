package org.cimiss2.dwp.Grib;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.cimiss2.dwp.tools.utils.TimeUtil;

/**
 * <br>
 * @Title:  SqlUtil.java
 * @Package com.hitec.osssave.common.utils
 * @Description: sql工具类
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月15日 下午1:19:45   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class SqlUtil {
	protected static final Logger LOGGER = Logger.getLogger(SqlUtil.class);
	private static final String SUB_REGEX = "\\$\\{(.*?)\\}";
	private static String splitRegex = "\\.|-|_";

	/**
	 * @Title: genIndexSql
	 * @Description: 生成入库索引sql语句
	 * @param tableName 入库表名
	 * @param sodType sod编码
	 * @param startTime 开始时间
	 * @param filename 文件名
	 * @param filesize 文件大小
	 * @param recvtime 收到时间
	 * @param newpath 入oss路径
	 * @return String
	 * @throws: 
	 */
	public static String genIndexSql(String tableName, String sodType, String startTime, String filename, long filesize, String recvtime, String newpath, String newFlag) {

		String sql = null;
		String[] fs = filename.split(splitRegex);
		// IniReader iniReader = new IniReader(new FileInputStream(System.getProperty("user.dir") + "/config/index.txt"));
		ReadIni ini = ReadIni.getIni();
		Properties prop = ini.get(sodType); //
		if (prop == null) {
			LOGGER.warn("sodType : " + sodType + "No matches!");
			return null;
		}
		Iterator<Entry<Object, Object>> it = prop.entrySet().iterator();
		StringBuffer columnBuffer = new StringBuffer();
		StringBuffer valueBuffer = new StringBuffer();
		while (it.hasNext()) {
			Entry<Object, Object> next = it.next();
			String columnName = (String) next.getKey();
			String columnTmpValue = (String) next.getValue();

			columnBuffer.append(",`").append(columnName).append("`");
			if (columnTmpValue.contains("$")) {
				String columnValue = "";
				Matcher m = Pattern.compile(SUB_REGEX).matcher(columnTmpValue);

				while (m.find()) {
					String fileIndex = m.group(1);
					String fileIndexAll = m.group(0);

					if (fileIndex.contains(":")) {
						String[] fileIndexs = fileIndex.split(":");
						try{
						columnValue = fs[Integer.parseInt(fileIndexs[0])].substring(Integer.parseInt(fileIndexs[1]),
								Integer.parseInt(fileIndexs[1]) + Integer.parseInt(fileIndexs[2]));
						}catch (Exception e) {
							LOGGER.error("Get columnValue from filename failed!");
//							System.out.println(Integer.parseInt(fileIndexs[0]));
//							System.out.println(Integer.parseInt(fileIndexs[1]));
//							System.out.println(Integer.parseInt(fileIndexs[2]));
						}
					} else {
						columnValue = fs[Integer.parseInt(fileIndex)];
					}
					columnTmpValue = columnTmpValue.replaceFirst(string2Regex(fileIndexAll), columnValue).replace("'", "");
				}
			} else if (columnTmpValue.equalsIgnoreCase("d_file_id")) {
//				columnTmpValue = fs[4] + "_" + filename;
				columnTmpValue = filename;
			} else if (columnTmpValue.equalsIgnoreCase("sod_type")) {
				columnTmpValue = sodType;
			} else if (columnTmpValue.equalsIgnoreCase("start_time")) {
				columnTmpValue = startTime;
			} else if (columnTmpValue.equalsIgnoreCase("filename")) {
				columnTmpValue = filename;
			} else if (columnTmpValue.equalsIgnoreCase("filesize")) {
				columnTmpValue = filesize + "";
			} else if (columnTmpValue.equalsIgnoreCase("recvtime")) {
				columnTmpValue = recvtime;
			} else if (columnTmpValue.equalsIgnoreCase("newpath")) {
				columnTmpValue = newpath;
			} else if (columnTmpValue.equals("sysdate")) {
				columnTmpValue = TimeUtil.getSysTime();
			} else if (columnTmpValue.equals("\"\"") || columnTmpValue.equals("''") || columnTmpValue.equals("") || columnTmpValue.isEmpty()) {
				columnTmpValue = "";
			}else if(columnTmpValue.contains("\"") || columnTmpValue.contains("'") && !columnTmpValue.contains("$")) {
				columnTmpValue = columnTmpValue.substring(1, columnTmpValue.length() - 1);
			}else if(columnTmpValue.equalsIgnoreCase("null") || columnTmpValue.equalsIgnoreCase("'null'")) {
				columnTmpValue = null;
			}else if("newflag".equals(columnTmpValue)) {
				columnTmpValue = newFlag;
			}
			valueBuffer.append(",'").append(columnTmpValue).append("'");
		}
		sql = "insert into " + tableName + " (" + columnBuffer.toString().substring(1) + ") values (" + valueBuffer.toString().substring(1) + ")";
		return sql;
	}
	
	private static String string2Regex(String reg) {
		char[] flags = { '.', '(', ')', '[', ']', '{', '}', '$', '^', '\\' };

		char[] chars = reg.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			boolean isContain = ArrayUtils.contains(flags, c);
			if (isContain) {
				if (i == 0 || chars[i - 1] != '\\') {
					sb.append("\\");
				}
			}
			sb.append(c);
		}

		return sb.toString();
	}
}
