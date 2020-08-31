package org.cimiss2.dwp.tools;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cimiss2.dwp.Grib.SqlUtil;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class GridFileIndex {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

	/**
	 * @Title: indexProcess
	 * @Description: grib数据索引入库操作
	 * @param file void
	 */
	public static int indexProcess(File file, String sod_code) {
		String fileIndexTable = "nafp_product_file_tab";
		// new IndexConf().ReadConfig("config/index.txt");
		Date recv_time = new Date(file.lastModified()); // 文件修改时间，即文件收到时间
		String fileName = file.getName();
		// 查询索引表中是否存在相同的数据
		System.err.println(fileName);
		Matcher m = Pattern.compile("\\d{14}").matcher(fileName);
		String queryId = fileName;
		// 数据中存在两个时间，前边的是资料制作时间，后面是资料时间，有存在制作时间相同资料时间不同的同一种类数据,需将最新制作时间的数据设置标识为1，其他都为空或null
		String makeTime = null;
		if (m.find()) {
			makeTime = m.group(0);
			queryId = queryId.replace(makeTime, "%");
		}

		Date makeTimeDate = TimeUtil.String2Date(makeTime, TimeUtil.DATE_FMT_YMDHMS);

		java.sql.Connection connection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("fileindex");

			String querySql = "select D_FILE_ID,V_RETAIN3_C,V_MAKETIME " //
					+ "from " + fileIndexTable //
					+ " where D_FILE_ID like '" + queryId + "' and D_FILE_ID != '" + fileName + "' and V_RETAIN3_C = '1'";
			// V_RETAIN3_C 等于1的只会存在一条数据或没有
			System.out.println(querySql);
			if (connection != null) {
				Statement stmt = null;
				try {
					// 查询是否存在资料时间为同一时次且标识为1的数据
					stmt = connection.createStatement();
					ResultSet resultSet = stmt.executeQuery(querySql);
					// 需要比较文件名中制作时间，哪一个最新哪一个的标识设置为1，其他均为null或空，故索引表中不可能存在多条同要素同资料时间的标识为1的数据信息，最多一条
					String newFlag = "";
					if (resultSet.next()) {
						String d_file_id = resultSet.getString("D_FILE_ID");
						String v_retain3_c = resultSet.getString("V_RETAIN3_C");
						String v_maketime = resultSet.getString("V_MAKETIME");
						System.out.println("d_file_id: " + d_file_id + ", v_retain3_c: " + v_retain3_c + ",v_maketime: " + v_maketime + ", maketime:" + makeTime);

						Date v_maketime_date = TimeUtil.String2Date(v_maketime, TimeUtil.DATE_FMT_YMDHMS);
						// 如果存在相应条件的数据将其标识改为null或空
						if (makeTimeDate.after(v_maketime_date)) {
							String updateSql = "update " + fileIndexTable + " set V_RETAIN3_C = null where D_FILE_ID = '" + d_file_id + "'";
							stmt.executeUpdate(updateSql);
							newFlag = "1";
						}
					} else {
						newFlag = "1";
					}
					// 如果没有数据，则直接入库即可，并设置标识为1，如果存在数据，但其制作时间晚于当前文件的制作时间，则入库时标识为空或null
					// 开始处理时间使用当前的系统时间
					String insertSql = SqlUtil.genIndexSql(fileIndexTable, StartConfig.ctsCode(), TimeUtil.getSysTime(), fileName, file.length(),
							TimeUtil.date2String(recv_time, TimeUtil.DEFAULT_DATETIME_FORMAT), file.getPath(), newFlag);
					// System.err.println("dataTime: " + sb.toString());
					System.out.println("sql > " + insertSql);

					// boolean execute = stmt.execute(insertSql);
					int executeUpdate;
					try {
						executeUpdate = stmt.executeUpdate(insertSql);
						if (executeUpdate > 0) {
							infoLogger.info("fileindex insert succeeded:" + insertSql);
						} else {
							infoLogger.info("fileindex insert failed:" + insertSql);
						}
					} catch (MySQLIntegrityConstraintViolationException e) {
						infoLogger.warn("fileindex insert warn DuplicateDataException then remove file:" + file.getName(), e);
						file.delete();
						return 0;
					}
					return executeUpdate;
				} catch (SQLException e) {
				//	e.printStackTrace();
					infoLogger.error("Execute insertSql failed: " + e.getMessage());
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;

	}
}
