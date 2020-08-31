package cma.cimiss2.dpc.decoder.grib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.grib.di_ei.SendEiProcess;


public class IndexDbOperation {
	//private static final Logger logger = LoggerFactory.getLogger(GribDecode.class);
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	private static String SystemDate = GribConfig.getSysDate();

	private Connection conn = null;
	private PreparedStatement prestmt_nas_file = null;
	private PreparedStatement prestmt_field_file = null;

	private String nas_file_table_name = "";
	private String field_file_table_name = "";

	// nas文件索引表：nafp_ec_for_ftm_file_k_tab
	private String insert_sql_nas_file_index = "";
	// field单场信息索引表：nafp_ec_for_ftm_file_tab
	private String insert_sql_field_file_index = "";

	// 更新nas文件索引表
	private PreparedStatement prestmt_nas_file_update = null;
	private String update_sql_nas_file_index = "";

	// 查询FILE_ID是否存在
	private PreparedStatement prestmt_select_FILE_ID_exist = null;
	private String sql_select_FILE_ID_exit = "";
	
	//入智能网格产品文件索引表
	private PreparedStatement prestmt_nwfd_insert = null;
	private String sql_insert_nwfd_index = "";
	private String nwfd_index_table = "";

	public IndexDbOperation(String nas_file_table_name, String field_file_table_name) {
		this.nas_file_table_name = nas_file_table_name;
		this.field_file_table_name = field_file_table_name;

		insert_sql_nas_file_index = "insert into " + nas_file_table_name
				+ "(D_FILE_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATETIME,D_DATETIME,"
				+ "V04001,V04002,V04003,V04004,V04005,V04006,V01033,V01034,V_GENPRO_TYPE,V_ELE_CODE,V_LEVELTYPE,V_AREACODE,"
				+ "D_STORAGE_SITE,V_FILE_NAME,D_FILE_SIZE,D_ARCHIVE_FLAG,V_FILE_FORMAT,D_FILE_SAVE_HIERARCHY,"
				+ "V_LATSTART,V_LONSTART,V_LATEND,V_LONEND,V_NUMBER_X,V_NUMBER_Y,V_IINCREMENT,V_JINCREMENT,"
				+ "V_GRIB_VERSION,V_ELE_CODE_SERV,"
				+ "V_FIELD_TYPE,V_GENPROCESS_TYPE,d_source_id) "
				//+ "values(?,?,sysdate(),DATE_FORMAT(?,'%Y-%m-%d %H:%i:%s'),sysdate(),DATE_FORMAT(?,'%Y-%m-%d %H:%i:%s')," //阿里库
				+ "values(?,?,sysdate(),?,sysdate(),?," //虚谷库
				+ "?,?,?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?," + "?,?,?,?,?,?,?,?," + "?,?," + "?,?,?)";

		insert_sql_field_file_index = "insert into " + field_file_table_name
				+ "(D_IYMDHM,V_LEVEL1,V_LEVEL2,V04320,V_FIELD_TYPE,"
				+ "V_FILE_NAME_SOURCE,V_FIELD_FILE_NAME_SOURCE,V_RETAIN1,V_RETAIN2,V_RETAIN3,D_FILE_ID,D_DATETIME) "
				//+ "values(sysdate(),?,?,?,?," + "?,?,?,?,?,?,DATE_FORMAT(?,'%Y-%m-%d %H:%i:%s'))";
				+ "values(sysdate(),?,?,?,?," + "?,?,?,?,?,?,?)";

		// 更新nas文件大小
		update_sql_nas_file_index = "update " + nas_file_table_name
				+ " set D_UPDATETIME=sysdate(),D_FILE_SIZE=? where D_FILE_ID=? and D_DATETIME=?";

		// 查询FILE_ID是否存在
		sql_select_FILE_ID_exit = "select * from " + nas_file_table_name + " where D_FILE_ID=? and D_DATETIME=?";
		
		//入智能网格产品文件索引表
		nwfd_index_table = "NAFP_PRODUCT_FILE_TAB";
		sql_insert_nwfd_index = "insert into " + nwfd_index_table + "(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,"
				+ "D_FILE_SIZE,D_STORAGE_SITE,V_CCCC,V_MAKETIME,V_PROD_SORT,V_PROD_SYSTEM,V_PROD_CONTENT,"
				+ "V05310,V40410_3,V_FNTIME,V04320_C,V_INTERVAL,V_FILE_FORMAT,V_FILE_NAME,"
				+ "V_RETAIN1_C,V_RETAIN2_C,V_RETAIN3_C,D_FILE_ID,D_SOURCE_ID,V40410_1) "
				+ "values(?,sysdate(),?,?,"
				+ "?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,'')";
	}

	public void start(String database_id) throws Exception {
		conn = GribDBUtil.getINSTANCE().getConnection(database_id);
		if (conn == null) {
			// 如果数据库连接出错，发送EI信息
			String log_data = "Error:cannot find a proper coonection:" + database_id + "," + GribConfig.getSysTime();
			logger_e.error(log_data);
			
			//使用单独的线程池来入Rest EI
			if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
			{
				SendEiProcess sendEiProcess = SendEiProcess.getInstance();
				String KEvent = "连接索引库" + database_id + "失败";
				sendEiProcess.process_ei("OP_DPC_A_2_33_01", KEvent, "", "");
			}
			
			String error_log_file = GribConfig.getLogFilePath() + "error_" + SystemDate + ".log";
			GribConfig.write_log_to_file(error_log_file, log_data);
			throw new Exception(log_data);
		} else {
			//logger.info("sucess to get connection " + database_id);
		}
	}

	// 将索引信息写入数据库
	// 同时写：大文件总索引表和单场文件信息表
	// 事务性：两个同时成功或都不成功
	// 返回：0：失败 1：成功
	public int insertIndexInfo(Grib_Struct_Data grib_struc_data) {

		int ret = 0;
		try {
			logger.info("start to insert " + nas_file_table_name + " and " + field_file_table_name);

			boolean ifcommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			// 入nas文件的索引表
			prestmt_nas_file = conn.prepareStatement(insert_sql_nas_file_index);
			// 入field文件的索引表
			prestmt_field_file = conn.prepareStatement(insert_sql_field_file_index);

			// 如果是阿里DRDS的索引库，需要加这句
			if (GribConfig.getdataBaseType() == 1) {
				prestmt_nas_file.execute("select last_txc_xid()");
				// prestmt_field_file.execute("select last_txc_xid()");
			}

			// 设置prestmt_nas_file的set属性
			setNasFileIndexAttr(grib_struc_data);
			prestmt_nas_file.addBatch();

			// 设置prestmt_field_file的set属性
			setFieldFileIndexAttr(grib_struc_data);
			prestmt_field_file.addBatch();

			prestmt_nas_file.executeBatch();
			prestmt_field_file.executeBatch();

			conn.commit();
			ret = 1;
			logger.info("sucess to insert " + nas_file_table_name + " and " + field_file_table_name);
			conn.setAutoCommit(ifcommit);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			if (e.getErrorCode() == 13001) // Errorcode=13001时，重复键(Duplicate Key "D_FILE_ID")，不算错误
			{
				ret = 1;
			} else {
				ret = 0;
			}

			logger_e.error("error to insert " + nas_file_table_name + " and " + field_file_table_name + ", ErrorCode="
					+ e.getErrorCode() + "," + e.getMessage());

		}

		logger.info("insert ret=" + ret);

		return ret;
	}

	// 设置nas文件表的set参数
	private void setNasFileIndexAttr(Grib_Struct_Data grib_struc_data) throws SQLException {
		prestmt_nas_file.setString(1, grib_struc_data.getD_FILE_ID());
		prestmt_nas_file.setString(2, grib_struc_data.getdata_id().replace(".R", ".S"));
		prestmt_nas_file.setString(3, grib_struc_data.getD_RYMDHM());
		prestmt_nas_file.setString(4, grib_struc_data.getDATETIME());
		prestmt_nas_file.setInt(5, grib_struc_data.getV04001());
		prestmt_nas_file.setInt(6, grib_struc_data.getV04002());
		prestmt_nas_file.setInt(7, grib_struc_data.getV04003());
		prestmt_nas_file.setInt(8, grib_struc_data.getV04004());
		prestmt_nas_file.setInt(9, grib_struc_data.getV04005());
		prestmt_nas_file.setInt(10, grib_struc_data.getV04006());
		prestmt_nas_file.setInt(11, grib_struc_data.getV_CCCC_N());
		prestmt_nas_file.setInt(12, grib_struc_data.getV_CCCC_SN());
		prestmt_nas_file.setInt(13, grib_struc_data.getV_GENPRO_TYPE());
		prestmt_nas_file.setString(14, grib_struc_data.getElement());
		prestmt_nas_file.setInt(15, grib_struc_data.getLevelType());
		prestmt_nas_file.setString(16, grib_struc_data.getV_AREACODE());
		prestmt_nas_file.setString(17, grib_struc_data.getD_STORAGE_SITE());
		prestmt_nas_file.setString(18, grib_struc_data.getV_FILE_NAME());
		prestmt_nas_file.setLong(19, grib_struc_data.getD_FILE_SIZE());
		prestmt_nas_file.setInt(20, grib_struc_data.getD_ARCHIVE_FLAG());
		prestmt_nas_file.setString(21, grib_struc_data.getV_FILE_FORMAT());
		prestmt_nas_file.setInt(22, grib_struc_data.getD_FILE_SAVE_HIERARCHY());
		prestmt_nas_file.setFloat(23, grib_struc_data.getStartY());
		prestmt_nas_file.setFloat(24, grib_struc_data.getStartX());
		prestmt_nas_file.setFloat(25, grib_struc_data.getEndY());
		prestmt_nas_file.setFloat(26, grib_struc_data.getEndX());
		prestmt_nas_file.setInt(27, grib_struc_data.getXCount());
		prestmt_nas_file.setInt(28, grib_struc_data.getYCount());
		prestmt_nas_file.setFloat(29, grib_struc_data.getXStep());
		prestmt_nas_file.setFloat(30, grib_struc_data.getYStep());
		prestmt_nas_file.setInt(31, grib_struc_data.getgrib_version());
		prestmt_nas_file.setString(32, grib_struc_data.getelement_serv());
		prestmt_nas_file.setInt(33, grib_struc_data.getV_FIELD_TYPE());
		prestmt_nas_file.setInt(34, grib_struc_data.getV_RETAIN1());
		prestmt_nas_file.setString(35, grib_struc_data.getdata_id());
	}

	// 设置field场文件表的set参数
	private void setFieldFileIndexAttr(Grib_Struct_Data grib_struc_data) throws SQLException {
		prestmt_field_file.setInt(1, grib_struc_data.getLevel1());
		prestmt_field_file.setInt(2, grib_struc_data.getLevel2());
		prestmt_field_file.setInt(3, grib_struc_data.getV04320());
		prestmt_field_file.setInt(4, grib_struc_data.getV_FIELD_TYPE());
		prestmt_field_file.setString(5, grib_struc_data.getV_FILE_NAME_SOURCE());
		prestmt_field_file.setString(6, grib_struc_data.getV_FIELD_FILE_NAME_SOURCE());
		prestmt_field_file.setInt(7, grib_struc_data.getV_RETAIN1());
		prestmt_field_file.setInt(8, grib_struc_data.getV_RETAIN2());
		prestmt_field_file.setInt(9, grib_struc_data.getV_RETAIN3());
		prestmt_field_file.setString(10, grib_struc_data.getD_FILE_ID());
		prestmt_field_file.setString(11, grib_struc_data.getDATETIME());
	}

	// 将索引信息更新至数据库
	// 更新：nas文件索引表
	// 插入：field文件索引表
	// 事务性：两个同时成功或都不成功
	// 返回：0：失败 1：成功
	public int UpdateIndexInfo(Grib_Struct_Data grib_struc_data) {
		int ret = 0;

		try {
			logger.info("start to update " + nas_file_table_name + " and " + field_file_table_name);

			boolean ifcommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			// 更新nas文件的索引表
			prestmt_nas_file_update = conn.prepareStatement(update_sql_nas_file_index);
			// 写入field文件的索引表
			prestmt_field_file = conn.prepareStatement(insert_sql_field_file_index);

			// 如果是阿里DRDS的索引库，需要加这句
			if (GribConfig.getdataBaseType() == 1) {
				prestmt_nas_file_update.execute("select last_txc_xid()");
				// prestmt_field_file.execute("select last_txc_xid()");
			}

			// 设置prestmt_nas_file_update的set属性
			setNasFileIndexAttr_update(grib_struc_data);
			prestmt_nas_file_update.addBatch();

			// 设置prestmt_field_file的set属性
			setFieldFileIndexAttr(grib_struc_data);
			prestmt_field_file.addBatch();

			prestmt_nas_file_update.executeBatch();
			prestmt_field_file.executeBatch();

			conn.commit();
			ret = 1;
			logger.info("sucess to update " + nas_file_table_name + " and " + field_file_table_name);
			conn.setAutoCommit(ifcommit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			if (e.getErrorCode() == 13001) // Errorcode=13001时，重复键(Duplicate Key "D_FILE_ID")，不算错误
			{
				ret = 1;
			} else {
				ret = 0;
			}

			logger_e.error("error to update " + nas_file_table_name + " and " + field_file_table_name + ", ErrorCode="
					+ e.getErrorCode() + "," + e.getMessage() + "\n");

		}

		logger.info("update ret=" + ret);

		return ret;
	}

	// 设置nas文件表的set参数
	private void setNasFileIndexAttr_update(Grib_Struct_Data grib_struc_data) throws SQLException {
		prestmt_nas_file_update.setLong(1, grib_struc_data.getD_FILE_SIZE());
		prestmt_nas_file_update.setString(2, grib_struc_data.getD_FILE_ID());
		prestmt_nas_file_update.setString(3, grib_struc_data.getDATETIME());

	}

	// 将索引信息写入数据库
	// 写：大文件总索引表
	// 返回：0：失败 1：成功
	public int insertIndexInfo_nasInfo(Grib_Struct_Data grib_struc_data) {

		int ret = 0;
		try {
			//logger.info("start to insert " + nas_file_table_name);

			boolean ifcommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			// 入nas文件的索引表
			prestmt_nas_file = conn.prepareStatement(insert_sql_nas_file_index);

			// 如果是阿里DRDS的索引库，需要加这句
			if (GribConfig.getdataBaseType() == 1) {
				prestmt_nas_file.execute("select last_txc_xid()");
			}

			// 设置prestmt_nas_file的set属性
			setNasFileIndexAttr(grib_struc_data);
			prestmt_nas_file.addBatch();

			prestmt_nas_file.executeBatch();

			conn.commit();
			ret = 1;
			//logger.info("sucess to insert " + nas_file_table_name);
			conn.setAutoCommit(ifcommit);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			if (e.getErrorCode() == 13001) // Errorcode=1062时，如重复键(Duplicate Key "D_FILE_ID")，不算错误
			{
				ret = 1;
			} 
			else 
			{
				ret = 0;
				
			}

			logger_e.error("error to insert " + nas_file_table_name + ", ErrorCode=" + e.getErrorCode() + ","
					+ e.getMessage());
			
			if(ret!=1)
			{
				//使用单独的线程池来入Rest EI
				if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
				{
					SendEiProcess sendEiProcess = SendEiProcess.getInstance();
					String KEvent = "insert索引表失败，" + nas_file_table_name + "," + e.getMessage();
					sendEiProcess.process_ei("OP_DPC_A_2_33_06", KEvent, grib_struc_data.getDATETIME(), grib_struc_data.getdata_id());
				}
			}

		}

		logger.info("insert ret=" + ret);

		return ret;
	}

	// 将索引信息更新至数据库
	// 更新：nas文件索引表
	// 返回：0：失败 1：成功
	public int UpdateIndexInfo_nafInfo(Grib_Struct_Data grib_struc_data) {
		int ret = 0;

		try {
			//logger.info("start to update " + nas_file_table_name);

			boolean ifcommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			// 更新nas文件的索引表
			prestmt_nas_file_update = conn.prepareStatement(update_sql_nas_file_index);

			// 如果是阿里DRDS的索引库，需要加这句
			if (GribConfig.getdataBaseType() == 1) {
				prestmt_nas_file_update.execute("select last_txc_xid()");
			}

			// 设置prestmt_nas_file_update的set属性
			setNasFileIndexAttr_update(grib_struc_data);
			prestmt_nas_file_update.addBatch();

			prestmt_nas_file_update.executeBatch();

			conn.commit();
			ret = 1;
			//logger.info("sucess to update " + nas_file_table_name);
			conn.setAutoCommit(ifcommit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			if (e.getErrorCode() == 13001) // Errorcode=1062时，重复键(Duplicate Key "D_FILE_ID")，不算错误
			{
				ret = 1;
			} 
			else 
			{
				ret = 0;
			}

			logger_e.error("error to update " + nas_file_table_name + ", ErrorCode=" + e.getErrorCode() + ","
					+ e.getMessage() + "\n");
			
			if(ret!=1)
			{
				//使用单独的线程池来入Rest EI
				if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
				{
					SendEiProcess sendEiProcess = SendEiProcess.getInstance();
					String KEvent = "update索引表失败，" + nas_file_table_name + "," + e.getMessage();
					sendEiProcess.process_ei("OP_DPC_A_2_33_06", KEvent, grib_struc_data.getDATETIME(), grib_struc_data.getdata_id());
				}
			}

		}

		logger.info("update ret=" + ret);

		return ret;
	}

	// 将索引信息写入数据库
	// 写：单场文件信息表
	// 返回：0：失败 1：成功
	public int insertIndexInfo_fieldInfo(Grib_Struct_Data grib_struc_data) {

		int ret = 0;
		try {
			//logger.info("start to insert " + field_file_table_name);

			boolean ifcommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			// 入field文件的索引表
			prestmt_field_file = conn.prepareStatement(insert_sql_field_file_index);

			// 如果是阿里DRDS的索引库，需要加这句
			if (GribConfig.getdataBaseType() == 1) {
				prestmt_field_file.execute("select last_txc_xid()");
			}

			// 设置prestmt_field_file的set属性
			setFieldFileIndexAttr(grib_struc_data);
			prestmt_field_file.addBatch();

			prestmt_field_file.executeBatch();

			conn.commit();
			ret = 1;
			//logger.info("sucess to insert " + field_file_table_name);
			conn.setAutoCommit(ifcommit);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			if (e.getErrorCode() == 13001) // Errorcode=13001时，重复键(Duplicate Key "D_FILE_ID")，不算错误
			{
				ret = 1;
			} else {
				ret = 0;
			}

			logger_e.error("error to insert " + field_file_table_name + ", ErrorCode=" + e.getErrorCode() + ","
					+ e.getMessage());

		}

		logger.info("insert ret=" + ret);

		return ret;
	}

	// 判断此FILE_ID在nas索引表中是否存在
	public boolean exist_FILE_ID(Grib_Struct_Data grib_struc_data) {
		boolean is_exist = false;
		try {
			//logger.info("start to query FILE_ID " + grib_struc_data.getD_FILE_ID() + " exist?");

			boolean ifcommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			//sql
			prestmt_select_FILE_ID_exist = conn.prepareStatement(sql_select_FILE_ID_exit);

			// 如果是阿里DRDS的索引库，需要加这句
			if (GribConfig.getdataBaseType() == 1) {
				prestmt_select_FILE_ID_exist.execute("select last_txc_xid()");
			}

			// 设置prestmt_field_file的set属性
			setQueryFILE_ID_exist(grib_struc_data);
			//prestmt_select_FILE_ID_exist.addBatch(); //查询不带此条批量语句
			

			ResultSet rs = prestmt_select_FILE_ID_exist.executeQuery();

			if (rs.next()) {
				is_exist = true;
			}

			//logger.info("sucess to query FILE_ID ");

			conn.setAutoCommit(ifcommit);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			logger_e.error("error to query FILE_ID " + ", ErrorCode=" + e.getErrorCode() + "," + e.getMessage());

		}

		//logger.info("query FILE_ID " + grib_struc_data.getD_FILE_ID() + " is_exist=" + is_exist);

		return is_exist;
	}

	// 设置查询FILE_ID是否存在的set参数
	private void setQueryFILE_ID_exist(Grib_Struct_Data grib_struc_data) throws SQLException {
		//logger.info("grib_struc_data.getD_FILE_ID()=" + grib_struc_data.getD_FILE_ID());
		prestmt_select_FILE_ID_exist.setString(1, grib_struc_data.getD_FILE_ID());
		//logger.info("grib_struc_data.getDATETIME()=" + grib_struc_data.getDATETIME());
		prestmt_select_FILE_ID_exist.setString(2, grib_struc_data.getDATETIME());		

	}
	
	//
	//
	//返回：0：失败  1：成功
	/**
	 * 将索引信息更新至数据库
	 * 更新：nwfd智能网格文件索引表
	 * @param grib_struc_data 结构体
	 * @return 入库是否成功
	 *         1：成功，0：失败
	 */
	public int Insert_Nwfd_Index(Grib_Struct_Data grib_struc_data)
	{
		int ret = 0;	
		
		try
		{
			logger.info("start to insert nwfd index table:"+ nas_file_table_name);
			
			boolean ifcommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			
			//更新nwfd文件的索引表
			prestmt_nwfd_insert = conn.prepareStatement(sql_insert_nwfd_index);
				
			//如果是阿里DRDS的索引库，需要加这句
			if(GribConfig.getdataBaseType() == 1) {
				prestmt_nwfd_insert.execute("select last_txc_xid()");				
			}
			
			//设置prestmt_nwfd_insert的set属性
			set_nwfd_insert(grib_struc_data);
			prestmt_nwfd_insert.addBatch();
			
			
			prestmt_nwfd_insert.executeBatch();
					
			
			conn.commit();
			ret = 1;
			logger.info("sucess to insert "+ nwfd_index_table);
			conn.setAutoCommit(ifcommit);	
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			if(e.getErrorCode()==13001) //Errorcode=13001时，[E13001] 违反唯一值约束，不算错误
			{
				ret = 1;	
			}
			else
			{
				ret = 0;
			}
			
			logger_e.error("error to insert "+ nwfd_index_table + ", ErrorCode="+e.getErrorCode()+"," + e.getMessage()+"\n");	
			
			
			
		}
			
		logger.info("insert ret=" + ret);		
		
		return ret;
	}
	
	private void set_nwfd_insert(Grib_Struct_Data grib_struc_data) throws SQLException
	{

		prestmt_nwfd_insert.setString(1, grib_struc_data.getdata_id().replace(".R", ".S"));
		//System.out.println("getdata_id:" + grib_struc_data.getdata_id().replace(".R", ".S"));
		prestmt_nwfd_insert.setString(2, grib_struc_data.getD_RYMDHM());
		//System.out.println("getD_RYMDHM:" + grib_struc_data.getD_RYMDHM());
		prestmt_nwfd_insert.setString(3, grib_struc_data.getDATETIME());
		//System.out.println("getDATETIME:" + grib_struc_data.getDATETIME());
		prestmt_nwfd_insert.setLong(4, grib_struc_data.getD_FILE_SIZE());
		//System.out.println("getD_FILE_SIZE():" + grib_struc_data.getD_FILE_SIZE());
		prestmt_nwfd_insert.setString(5, grib_struc_data.getD_STORAGE_SITE());
		//System.out.println("getD_STORAGE_SITE():" + grib_struc_data.getD_STORAGE_SITE());
		prestmt_nwfd_insert.setString(6, grib_struc_data.getv_cccc());
		//System.out.println("getv_cccc():" + grib_struc_data.getv_cccc());
		prestmt_nwfd_insert.setString(7, grib_struc_data.getmaketime());
		//System.out.println("getmaketime():" + grib_struc_data.getmaketime());
		prestmt_nwfd_insert.setString(8, grib_struc_data.getprodSort());
		//System.out.println("getprodSort():" + grib_struc_data.getprodSort());
		prestmt_nwfd_insert.setString(9, grib_struc_data.getV_PROD_SYSTEM());
		//System.out.println("getV_PROD_SYSTEM():" + grib_struc_data.getV_PROD_SYSTEM());
		prestmt_nwfd_insert.setString(10, grib_struc_data.getprodContent());
		//System.out.println("getprodContent():" + grib_struc_data.getprodContent());
		prestmt_nwfd_insert.setString(11, grib_struc_data.getV_AREACODE());
		//System.out.println("getV_AREACODE():" + grib_struc_data.getV_AREACODE());
		prestmt_nwfd_insert.setString(12, grib_struc_data.getdensity());
		//System.out.println("getdensity():" + grib_struc_data.getdensity());
		prestmt_nwfd_insert.setString(13, grib_struc_data.getfntime());
		//System.out.println("getfntime():" + grib_struc_data.getfntime());
		prestmt_nwfd_insert.setString(14, grib_struc_data.getv04320_c());
		//System.out.println("getv04320_c():" + grib_struc_data.getv04320_c());
		prestmt_nwfd_insert.setString(15, grib_struc_data.getinternal());
		//System.out.println("getinternal():" + grib_struc_data.getinternal());
		prestmt_nwfd_insert.setString(16, grib_struc_data.getV_FILE_FORMAT());
		//System.out.println("getV_FILE_FORMAT():" + grib_struc_data.getV_FILE_FORMAT());
		//prestmt_nwfd_insert.setString(17, grib_struc_data.getV_FILE_NAME());
		//prestmt_nwfd_insert.setString(17, grib_struc_data.getD_STORAGE_SITE()); //modify,2020.5.29:v_file_name赋值全路径
		prestmt_nwfd_insert.setString(17, grib_struc_data.getretain1() + "/" + grib_struc_data.getV_FILE_NAME()); //modify,20205.6.1:v_file_name:cccc/file_name
		//System.out.println("getV_FILE_NAME():" + grib_struc_data.getV_FILE_NAME());
		prestmt_nwfd_insert.setString(18, grib_struc_data.getretain1());
		//System.out.println("getretain1():" + grib_struc_data.getretain1());
		prestmt_nwfd_insert.setString(19, grib_struc_data.getretain2());
		//System.out.println("getretain2():" + grib_struc_data.getretain2());
		prestmt_nwfd_insert.setString(20, grib_struc_data.getretain3());
		//System.out.println("getretain3():" + grib_struc_data.getretain3());
		prestmt_nwfd_insert.setString(21, grib_struc_data.getD_FILE_ID());
		//System.out.println("getD_FILE_ID():" + grib_struc_data.getD_FILE_ID());
		prestmt_nwfd_insert.setString(22, grib_struc_data.getD_SOURCE_ID());
		//System.out.println("getD_SOURCE_ID():" + grib_struc_data.getD_SOURCE_ID());


	}

	// 关闭数据库的连接
	public void close() {
		if (prestmt_nas_file != null) {
			try {
				prestmt_nas_file.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger_e.error("cannot close prestmt_nas_file!");
				logger_e.error(e.getMessage());
			}
			prestmt_nas_file = null;
		}

		if (prestmt_field_file != null) {
			try {
				prestmt_field_file.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger_e.error("cannot close prestmt_field_file!");
				logger_e.error(e.getMessage());
			}
			prestmt_field_file = null;
		}

		if (prestmt_nas_file_update != null) {
			try {
				prestmt_nas_file_update.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger_e.error("cannot get close a connection!");
				logger_e.error(e.getMessage());
			}
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger_e.error("cannot get close a connection!");
				logger_e.error(e.getMessage());
			}
		}

	}
}