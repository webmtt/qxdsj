package cma.cimiss2.dpc.indb.upar.dc_upar_wind_rada_nc.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.WindProfileData;
import cma.cimiss2.dpc.decoder.bean.upar.WindProfileRada;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;
import cma.cimiss2.dpc.quickqc.upar.windProfile.Chn_Upar_Profile_QuickQC0;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年7月31日 下午3:05:27   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DbService {
	
	/**
	 *  di 队列
	 */
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	/**
	 * 消息日志
	 */
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    /**
     * 处理消息日志
     */
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    /**
     * Di 队列
     */
    public static BlockingQueue<StatDi> diQueues;

    
    public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
		/**
	     * 报文数据入库
	     * @param pr
	     * @param rece_time
	     * @return
	     */
	    public static DataBaseAction processSuccessReport(ParseResult<WindProfileRada> pr, Date rece_time, String fileName, StringBuffer log) {
	        DruidPooledConnection connection = null;
	        try {
	            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
	            File file = new File(fileName);
	            String fileN = file.getName();
	            List<WindProfileRada> data = pr.getData();
	            DataBaseAction action = insertDB(fileN, connection, data, rece_time, fileName, log);
	            if (DataBaseAction.SUCCESS == action) {
	                return action;
	            } else {
	                return DataBaseAction.INSERT_ERROR;
	            }
	        } catch (Exception e) {
	            log.append(e.getMessage());
	            e.printStackTrace();
	            return DataBaseAction.CONNECTION_ERROR;
	        } finally {
	            for (StatDi aListDi : listDi) {
	                diQueues.offer(aListDi);
	            }
	            listDi.clear();
	            if (connection != null) {
	                try {
	                    connection.close();
	                } catch (SQLException e) {
	                    log.append("\n close connection error!").append(e.getMessage());
	                }
	            }
	        }
	    }
	    /**
	     * 入库详细
	     * @param connection
	     * @param WindProfileRada
	     * @param rece_time
	     * @return
	     */
		private static DataBaseAction insertDB(String fileN, DruidPooledConnection connection,
				List<WindProfileRada> windRadas, Date rece_time, String fileName, StringBuffer log) {
			String tableName = StartConfig.valueTable();
	        // 一个文件可能多个站点数据
	        List<Boolean> list = new ArrayList<>();
	        for (int i = 0; i < windRadas.size(); i++) {
	            WindProfileRada windRada = windRadas.get(i);
	            if (windRada.getObeTime()!=null&&windRada.getObeTime().length() == 14) {
	                boolean value = insertTable(fileName, fileN, connection, windRada, rece_time, tableName, log);
	                list.add(value);
	            } else {
	            	if(windRada.getObeTime()!=null){
	            		log.append(fileN+" obserTime length not equals 14：").append(windRada.getObeTime().length()).append("\n");
	            	}else{
	            		log.append(fileN+" 获取测站基本参数中的观测时间异常\n");
	            	}
	            }
	        }
	        // 统计插入成功的站点个数
	        long count = list.stream().filter(t -> t).count();
	        log.append(fileN+":"+windRadas.size()).append("个站点数据,").append("插入成功").append(count).append("个");
	        // 成功站点个数等于文件中的站点个数,所有插入成功
	        // 至少一条成功,既返回success
	        if (count >= 1) {
	            return DataBaseAction.SUCCESS;
	        } else {
	            return DataBaseAction.INSERT_ERROR;
	        }
		}

		/**
	     * 入库
	     * @param fileN
	     * @param connection
	     * @param WindProfileRada
	     * @param rece_time
	     * @param tableName
	     * @param log
	     */
     private static boolean insertTable(String fileName, String fileN, DruidPooledConnection connection, WindProfileRada windRada,
				Date rece_time, String tableName, StringBuffer log) {
    	 PreparedStatement ps = null;
         try {
             if (!connection.getAutoCommit()) {
                 connection.setAutoCommit(true);
             }
             ps = new LoggableStatement(connection, getSql(tableName));
             if (StartConfig.getDatabaseType() == 1) {
                 ps.execute("select last_txc_xid()");
             }
             // 质控步骤
             // 1：根据风廓线雷达站号查找探空站号
             String wStation = windRada.getStaionId();
             // 探空站点参数对象
             String recId = windRada.getStaionId()+ "_" + windRada.getObeTime() + "_" + windRada.get_OBS() + "_";
             List<WindProfileData> windData = windRada.getwData();
             String[] ymdhm = TimeUtil.getYmdhms(windRada.getObeTime());
             
             boolean flag = false;
             for (WindProfileData data : windData) {
                 // di
                 StatDi di = new StatDi();
                 di.setFILE_NAME_O(fileN);
                 di.setDATA_TYPE(getDataID(windRada.get_OBS()));
                 di.setDATA_TYPE_1(StartConfig.ctsCode());
                 di.setTT("WPRD");
                 di.setTRAN_TIME(TimeUtil.date2String(rece_time, "yyyy-MM-dd HH:mm"));
                 di.setPROCESS_START_TIME(TimeUtil.getSysTime());
                 di.setFILE_NAME_N(fileN);
                 di.setBUSINESS_STATE("1"); //1成功，0失败
                 di.setPROCESS_STATE("1");  //1成功，0失败
                 int ii = 1;
                 ps.setString(ii++, recId + String.valueOf(data.getHeight()));// 记录标识
                 ps.setTimestamp(ii++, new Timestamp(new Date().getTime()));// 入库时间
                 ps.setTimestamp(ii++, new Timestamp(rece_time.getTime()));// 收到时间
                 ps.setTimestamp(ii++, new Timestamp(new Date().getTime()));// 更新时间
                 ps.setTimestamp(ii++, new Timestamp(TimeUtil.String2Date(windRada.getObeTime(), "yyyyMMddHHmmss").getTime()));// 观测时间
                 ps.setString(ii++, getDataID(windRada.get_OBS()));// 资料标识
                 ps.setString(ii++, StartConfig.ctsCode() + "_"  + fileN); // 文件名，没有路径
                 //
                 ps.setString(ii++, windRada.getStaionId());// WMO区站号
                 ps.setInt(ii++, 999998); //测站类型  未编报
                 ps.setString(ii++, "BATC"); //编报（加工）中心，未编报
                 ps.setString(ii++, "999998"); //报告类别，未编报
                 ps.setString(ii++, windRada.getRadaModel());// 风廓线雷达型号
                 ps.setString(ii++, windRada.getWND_OBS());// 产品类型
                 ps.setInt(ii++, Integer.parseInt(ymdhm[0]));// 年
                 ps.setInt(ii++, Integer.parseInt(ymdhm[1]));// 月
                 ps.setInt(ii++, Integer.parseInt(ymdhm[2]));// 日
                 ps.setInt(ii++, Integer.parseInt(ymdhm[3]));// 时
                 //
                 ps.setInt(ii++, Integer.parseInt(ymdhm[4]));// 分
                 ps.setInt(ii++, Integer.parseInt(ymdhm[5]));// 秒
                 ps.setBigDecimal(ii++, new BigDecimal(windRada.getLatitude()).setScale(6, BigDecimal.ROUND_HALF_UP));// 纬度
                 ps.setBigDecimal(ii++, new BigDecimal(windRada.getLongitude()).setScale(6, BigDecimal.ROUND_HALF_UP));// 经度
                 ps.setBigDecimal(ii++, new BigDecimal(windRada.getAltitude()).setScale(6, BigDecimal.ROUND_HALF_UP));// 测站高度
                 ps.setInt(ii++, 999998); // 探测设备类型， 未编报
                 ps.setInt(ii++, 999998); // 天线类型，未编报
                 ps.setDouble(ii++, 999998);// 波束宽度， 未编报
                 ps.setDouble(ii++, 999998); // 设备频率，未编报
                 ps.setDouble(ii++, 999998); // 库长，未编报
                 //
                 ps.setInt(ii++, 999998); // 平均速度估计，未编报
                 ps.setInt(ii++, 999998); // 风计算增强，未编报
                 ps.setDouble(ii++, 999998); // 脉冲重复频率，未编报
                 ps.setInt(ii++, 999998); // 脉冲个数，未编报
                 ps.setInt(ii++, 999998); // 配套设备,未编报
                 ps.setDouble(ii++, 999998); // 虚温，未编报
                 ps.setInt(ii++, 999998); // 模式类别，未编报
                 ps.setInt(ii++, 999998); // 垂直探测意义，未编报
                 ps.setDouble(ii++, data.getHeight());// 采样高度(测站以上高度)
                 if( data.getHeight() != 999999 && windRada.getAltitude() != 999999)
                	 ps.setDouble(ii++, data.getHeight() + windRada.getAltitude()); // 采样海拔高度 
                 else 
                	 ps.setDouble(ii++, 999999);
                 //
//                 System.out.println(new BigDecimal(data.getWindDirection()).setScale(4, BigDecimal.ROUND_HALF_UP));
                 ps.setBigDecimal(ii++, new BigDecimal(data.getWindDirection()).setScale(4, BigDecimal.ROUND_HALF_UP));// 水平风向
                 ps.setBigDecimal(ii++, new BigDecimal(data.getWindSpeed()).setScale(4, BigDecimal.ROUND_HALF_UP));// 水平风速
                 ps.setBigDecimal(ii++, new BigDecimal(data.getVwindSpeed()).setScale(4, BigDecimal.ROUND_HALF_UP));// 垂直风速
                 ps.setBigDecimal(ii++, new BigDecimal(String.valueOf(data.getHcredibility())));// 水平方向可信度
                 ps.setBigDecimal(ii++, new BigDecimal(String.valueOf(data.getVcredibility())));// 垂直方向可信度
                 ps.setString(ii++, data.getCn2());// 折射率结构常数Cn2
                 // 风向质控
                 int windDQC;
                 // 风速质控
                 int windSQC;
                 // 高度为采样高度+测站海拔高度
                 double hight = data.getHeight() + windRada.getAltitude();
                 QualityCode qc[] = Chn_Upar_Profile_QuickQC0.getWindQC(windRada.getStaionId(), data.getWindSpeed(), data.getWindDirection(), hight, windRada.getLatitude(), windRada.getLongitude());
                 windDQC = qc[0].getCode();
                 windSQC = qc[1].getCode();
                 ps.setInt(ii++, windDQC);// 水平风向质控码
                 ps.setInt(ii++, windSQC);// 水平风速质控码
                 ps.setInt(ii++, 9);// 垂直风速质控码
                 ps.setDouble(ii++, 999998);//谱宽
                 //
                 ps.setDouble(ii++, 999998); //信噪比
                 ps.setDouble(ii++, 999998); // 径向速度
                 ps.setInt(ii++, 999998); // 水平反射率
                 ps.setDouble(ii++, 9); // 水平反射率质控码
                 
                 // 补全di
                 di.setDATA_FLOW(StartConfig.getDataFlow());
                 di.setIIiii(wStation);
                 di.setLONGTITUDE(String.valueOf(windRada.getLongitude()));
                 di.setLATITUDE(String.valueOf(windRada.getLatitude()));
                 di.setDATA_TIME(TimeUtil.date2String(TimeUtil.String2Date(windRada.getObeTime(), "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm"));
                 di.setPROCESS_END_TIME(TimeUtil.getSysTime());
                 di.setRECORD_TIME(TimeUtil.getSysTime());
                 
                 di.setSEND("BFDB");
 				 di.setSEND_PHYS("DRDS");
 				 di.setFILE_SIZE(String.valueOf(new File(fileName).length()));
 				 di.setDATA_UPDATE_FLAG("000");
 				 di.setHEIGHT(String.valueOf(windRada.getAltitude()));
                 
                 
                 listDi.add(di);
//                 System.out.println(((LoggableStatement)ps).getQueryString());
                 try {
                     ps.execute();
                     // 至少一条成功
                     flag = true;
                 } catch (SQLException e) {
                     // ps.execute()异常
                     log.append("\n filename：" + fileN
                             + "\n " + di.getIIiii() + " " + di.getDATA_TIME()
                             + "\n execute sql error：" + ((LoggableStatement) ps).getQueryString() + e.getMessage() + "\n ");
                     di.setPROCESS_STATE("0");
                 }
             }
             return flag;
         } catch (SQLException e) {
             log.append("\n create Statement error" + e.getMessage());
             return false;
         } finally {
             if (ps != null) {
                 try {
                     ps.close();
                 } catch (SQLException e) {
                     log.append("\n close Statement error" + e.getMessage());
                 }
             }
         }
    	 
		}

	/**
     * 获取键表头部字段
     * @param tableName
     * @return
     */
    private static String getSql(String tableName) {
        String sqlhead = "insert into " + tableName
                + " (D_RECORD_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,D_DATA_ID,D_SOURCE_ID,"
                + "V01301,V02001,V_CCCC,V_TT,V_RADAR_MODEL,V01398,V04001,V04002,V04003,V04004,"
                + "V04005,V04006,V05001,V06001,V07001,V02003,V02101,V02106,V02121,V25001,"
                + "V25020,V25021,V02125,V25003,V25093,V12007,V25257,V08042,V07007,V07002,"
                + "V11001,V11002,V11006,V73107,V73108,V25091,Q11001,Q11002,Q11006,V21017,"
                + "V21030,V21014,V21001,Q21001)"
                + " VALUES ( ?, ?, ?, ?, ?, ?, ?,"
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
                + "?, ?, ?, ?) ";
        return sqlhead;
    }
    
    /**
     * 获取资料标识
     * @param obs
     * @return
     */
    private static String getDataID(String obs) {
        String d_data_id = null;//_OBS(包括R,H,O)
        switch (obs.charAt(0)) {
            case 'R':
                d_data_id = "B.0015.0003.S002";
                break;
            case 'H':
                d_data_id = "B.0015.0004.S002";
                break;
            case 'O':
                d_data_id = "B.0015.0005.S002";
                break;
        }
        return d_data_id;
    }
    
}
