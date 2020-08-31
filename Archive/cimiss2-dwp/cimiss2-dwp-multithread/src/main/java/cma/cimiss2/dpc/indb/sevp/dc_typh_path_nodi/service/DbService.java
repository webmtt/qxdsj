package cma.cimiss2.dpc.indb.sevp.dc_typh_path_nodi.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.sevp.Typhoon1;
import cma.cimiss2.dpc.decoder.sevp.DecodeTyphoon1;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

// TODO: Auto-generated Javadoc
/**
 * ************************************.
 *
 * @ClassName: BufrServiceImpl
 * @Auther: dangjinhu
 * @Date: 2019/3/11 15:47
 * @Description: 台风路径和台风预测--入库--xg--rdb
 *全球台风强度预报资料 M.0004.0003.R001 
 *全球台风强度预报资料 M.0004.0002.R001
 * @Copyright: All rights reserver.
 * ************************************
 */

public class DbService {

    /** The list di. */
    private static List<StatDi> listDi = new ArrayList<StatDi>();
    
    /** The Constant messageLogger. */
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    
    /** The Constant infoLogger. */
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    
    /** The di queues. */
    public static BlockingQueue<StatDi> diQueues;
    

    public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	/**
     * 报文数据入库.
     *
     * @param log the log
     * @param pr the pr
     * @param rece_time the rece time
     * @param fileName the file name
     * @return the data base action
     */
    public static DataBaseAction processSuccessReport(StringBuffer log, ParseResult<Typhoon1> pr, Date rece_time, String fileName) {
        DruidPooledConnection connection = null;
        try {
//            if (StartConfig.getDatabaseType() == 1) {
//                connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
//            } else if (StartConfig.getDatabaseType() == 0) {
                connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
//            }
            List<Typhoon1> typhoons = pr.getData();
            DataBaseAction action = insertDB(log, connection, typhoons, rece_time, fileName);
            if (DataBaseAction.SUCCESS == action) {
                return action;
            } else {
                return DataBaseAction.INSERT_ERROR;
            }
        } catch (Exception e) {
            log.append(e.getMessage());
            return DataBaseAction.CONNECTION_ERROR;
        } finally {
            // 发送di
            for (int j = 0; j < listDi.size(); j++) {
                diQueues.offer(listDi.get(j));
            }
            listDi.clear();
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.append("\n 关闭数据库连接失败").append(e.getMessage());
                }
            }
        }
    }

    /**
     * 入库详细.
     *
     * @param log the log
     * @param connection the connection
     * @param typhoons   解码数据集合
     * @param rece_time  资料收到时间
     * @param fileName   文件路径 +文件名
     * @return the data base action
     */
    private static DataBaseAction insertDB(StringBuffer log, Connection connection, List<Typhoon1> typhoons, Date rece_time, String fileName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String reportTable = StartConfig.reportTable();
        String sodCode = StartConfig.sodCode(); // 入库值对应D_DATA_ID
        String ctsCode = StartConfig.ctsCode();
        File file = new File(fileName);
        String fileN = file.getName();
        Statement st = null;
        List<String> sqls = new ArrayList<>();
        try {
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            st = connection.createStatement();
            if (StartConfig.getDatabaseType() == 1) {
                st.execute("select last_txc_xid()");
            }
            // 获取文件类型,实况active : fileType : true
            boolean fileType = getFileTyle(fileN);
            StringBuffer head = sqlhead(sdf, rece_time, reportTable, sodCode, fileType);
            // 一个测站文件对于多条记录
            List<Boolean> list = new ArrayList<>();
            for (int i = 0; i < typhoons.size(); i++) {
                Typhoon1 typhoon = typhoons.get(i);
                String date = typhoon.getDate();
                if (date!=null && date.length() != 12) {
                    log.append(fileN+"报文时间长度错误：").append(date).append("\n");
                    list.add(false); // 失败
                    continue; // 读取下一条记录
                }else if(date==null){
                	log.append(fileN+"报文时间获取为null \n");
                    list.add(false); // 失败
                    continue; // 读取下一条记录
                }
                // di
                StatDi di = new StatDi();
                di.setFILE_NAME_O(fileN);
                di.setDATA_TYPE(sodCode);
                di.setDATA_TYPE_1(ctsCode);
                di.setTT("台风预测|实况");
                di.setTRAN_TIME(TimeUtil.date2String(rece_time, "yyyy-MM-dd HH:mm"));
                di.setPROCESS_START_TIME(TimeUtil.getSysTime());
                di.setFILE_NAME_N(fileN);
                di.setBUSINESS_STATE("1"); //1成功，0失败
                di.setPROCESS_STATE("1");  //1成功，0失败
                String recordId;
                // 生成记录标识
                if (fileType) {//active
                    recordId = date + "_" + typhoon.getTyphoonNumber()+ "_"+ typhoon.getLatitude() + "_" + typhoon.getLongitude();
                } else {//forcast
                    recordId = date + "_" + typhoon.getForestTime() + "_" + typhoon.getTyphoonNumber() + "_" + typhoon.getLatitude() + "_" + typhoon.getLongitude();
                }
                String[] ymdhm = TimeUtil.getYmdhm(date);
                Date datetime = TimeUtil.String2Date(date, "yyyyMMddHHmm");
                // 拼接sql
                StringBuffer value = getsql(sdf, fileType, head, typhoon, recordId, ymdhm, datetime);
                // 补全di
                di.setIIiii(typhoon.getTyphoonNumber());
                di.setLONGTITUDE(String.valueOf(typhoon.getLongitude()));
                di.setLATITUDE(String.valueOf(typhoon.getLatitude()));
                di.setDATA_TIME(TimeUtil.date2String(TimeUtil.String2Date(date, "yyyyMMddHHmm"), "yyyy-MM-dd HH:mm"));
                di.setPROCESS_END_TIME(TimeUtil.getSysTime());
                di.setRECORD_TIME(TimeUtil.getSysTime());
                
                di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
				di.setDATA_UPDATE_FLAG("000");
				di.setHEIGHT("999999");
                
                listDi.add(di);
                try {
                    st.addBatch(value.toString());
                    sqls.add(value.toString());
                    list.add(true); // 成功
                } catch (SQLException e) {
                    // 批处理添加异常
                    sqls.add(value.toString());
                    list.add(false); // 添加失败,存放到sqls
                }

            }
            st.executeBatch();
            connection.commit();
            // 统计插入成功的记录
            long count = list.stream().filter(t -> t).count();
            log.append(typhoons.size()).append("个站点数据,").append("插入成功").append(count).append("站点数据");
            // 成功站点个数等于文件中的站点个数,所有插入成功
            // 至少一条成功,既返回success
            if (count >= 1) {
                return DataBaseAction.SUCCESS;
            } else {
                return DataBaseAction.INSERT_ERROR;
            }
        } catch (Exception e) {
            log.append("批处理提交sql异常：").append(e.getMessage());
            try {
            	if(st != null)
            		st.clearBatch();
                connection.rollback();
                if (!connection.getAutoCommit()) {
                    connection.setAutoCommit(true);
                }
                // 有成功变为true
                boolean flag = false;
                for (int i = 0; i < sqls.size(); i++) {
                    try {// 单独捕获execute异常
                        st.execute(sqls.get(i));
                        flag = true;
                    } catch (SQLException e1) {
                        if (listDi.size() > 0 && listDi.size() == sqls.size()) {
                            log.append("\n File name：" + fileN
                                    + "\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
                                    + "\n execute sql error: " + sqls.get(i) + "\n " + e.getMessage());
                            listDi.get(i).setPROCESS_STATE("0");
                        }
                    }
                }
                return flag ? DataBaseAction.SUCCESS : DataBaseAction.INSERT_ERROR;
            } catch (SQLException e1) {
                log.append(e1.getMessage()); // connection异常和execute异常
                return DataBaseAction.INSERT_ERROR;
            }

        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    log.append("pstatement 关闭异常：").append(e.getMessage());
                }
            }
        }

    }

    /**
     * 拼接sql.
     *
     * @param sdf      日期转换格式
     * @param fileType 若为实况数据为true
     * @param head     数据头
     * @param typhoon  台风数据
     * @param recordId 记录标识
     * @param ymdhm    年月日时分
     * @param datetime 资料时间
     * @return the sql
     */
    private static StringBuffer getsql(SimpleDateFormat sdf, boolean fileType, StringBuffer head, Typhoon1 typhoon, String recordId, String[] ymdhm, Date datetime) {
        StringBuffer value = new StringBuffer();
        value.append(head);
        String datatime = sdf.format(datetime);
        value.append(",'").append(recordId).append("'");
        value.append(",'").append(datatime).append("'");
        value.append(",").append(ymdhm[0]);
        value.append(",").append(ymdhm[1]);
        value.append(",").append(ymdhm[2]);
        value.append(",").append(ymdhm[3]);
        value.append(",").append(ymdhm[4]);
        value.append(",").append(0);
        value.append(",'").append(typhoon.getArea()).append("'");
        value.append(",'").append(typhoon.getTyphoonName()).append("'");
        value.append(",").append(ymdhm[0]);
        value.append(",").append(ymdhm[1]);
        value.append(",").append(ymdhm[2]);
        value.append(",").append(ymdhm[3]);
        value.append(",").append(ymdhm[4]);
        value.append(",").append(0);
        value.append(",'").append(typhoon.getTyphoonNumber()).append("'");
        value.append(",").append(typhoon.getLatitude());
        value.append(",").append(typhoon.getLongitude());
        value.append(",").append(typhoon.getStrength());
        if (!fileType) {
            value.append(",").append(typhoon.getForestTime());
        }
        value.append(",'999999'");//台风国际编号V01332
        value.append(",'ATOF'");//V_MODE_CODE
        value.append(",'").append(StartConfig.ctsCode()).append("'");
        value.append(")");
        return value;
    }

    /**
     * 获取文件类型：active 为 true 和 forecast.
     *
     * @param name the name
     * @return the file tyle
     */
    private static boolean getFileTyle(String name) {
        if (name.contains("active")) {
            return true;
        }
        return false;
    }

    /**
     * sql固定值.
     *
     * @param sdf the sdf
     * @param rece_time the rece time
     * @param reportTable the report table
     * @param sodCode the sod code
     * @param fileType the file type
     * @return the string buffer
     */
    private static StringBuffer sqlhead(SimpleDateFormat sdf, Date rece_time, String reportTable, String sodCode, boolean fileType) {
        StringBuffer head = new StringBuffer();
        String sql = "";
        if (fileType) {//active
            sql = "insert into " + reportTable
                    + " (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME"
                    + ",V04001_02,V04002_02,V04003_02,V04004_02,V04005_02,V04006_02"
                    + ",V05310,V_TYPH_NAME,V04001,V04002,V04003,V04004,V04005,V04006"
                    + ",V01334,V05001,V06001,V11042,V01332,V_MODE_CODE,D_SOURCE_ID)"
                    + " VALUES (";
        } else {//forcast
            sql = "insert into " + reportTable
                    + " (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME"
                    + ",V04001_02,V04002_02,V04003_02,V04004_02,V04005_02,V04006_02"
                    + ",V05310,V_TYPH_NAME,V04001,V04002,V04003,V04004,V04005,V04006"
                    + ",V01334,V05001,V06001,V11042,V04320,V01332,V_MODE_CODE,D_SOURCE_ID)"
                    + " VALUES (";

        }
        head.append(sql);
        head.append("'").append(sodCode).append("'");
        head.append(",'").append(sdf.format(new Date())).append("'");
        head.append(",'").append(sdf.format(rece_time)).append("'");
        head.append(",'").append(sdf.format(new Date())).append("'");
        return head;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
    	 String fileName = "D:\\TEMP\\M.4.3.1\\5-24\\forecast_SouthernHemisphere_201901070600_NOAA_SH092019_20190107174542.txt";
         StringBuffer log = new StringBuffer(); 
         File file = new File(fileName);
         Date rece_time = new Date(file.lastModified());
    	 DecodeTyphoon1 decodeTyphoon = new DecodeTyphoon1();
         ParseResult<Typhoon1> pr = decodeTyphoon.decodeFile(fileName);
         if (pr.isSuccess()) {
         DataBaseAction action = DbService.processSuccessReport(log,pr, rece_time, fileName);
         }

    }
}
