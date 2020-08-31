package cma.cimiss2.dpc.indb.upar.dc_upar_wind_rada.service;

import cma.cimiss2.dpc.decoder.StationData;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.WindData;
import cma.cimiss2.dpc.decoder.bean.upar.WindRada;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.upar.DecodeWindRada;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;
import cma.cimiss2.dpc.quickqc.upar.windProfile.Chn_Upar_Profile_QuickQC0;

import com.alibaba.druid.pool.DruidPooledConnection;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import static org.cimiss2.dwp.tools.utils.QCUtil.*;
/**
 * ************************************
 * @ClassName: DbService
 * @Auther: dangjinhu
 * @Date: 2019/3/21 09:14
 * @Description: 风廓线雷达资料解析--数据入库
 * @Copyright: All rights reserver.
 * ************************************
 */

public class DbService {

    private static List<StatDi> listDi = new ArrayList<StatDi>();
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
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
    public static DataBaseAction processSuccessReport(ParseResult<WindRada> pr, Date rece_time, String fileName, StringBuffer log) {
        DruidPooledConnection connection = null;
        try {
//            if (StartConfig.getDatabaseType() == 1) {
//                connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
//            } else if (StartConfig.getDatabaseType() == 0) {
                connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
//            }
            File file = new File(fileName);
            String fileN = file.getName();
            List<WindRada> data = pr.getData();
            DataBaseAction action = insertDB(fileN, connection, data, rece_time, fileName, log);
            if (DataBaseAction.SUCCESS == action) {
                return action;
            } else {
                return DataBaseAction.INSERT_ERROR;
            }
        } catch (Exception e) {
            log.append(e.getMessage());
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
                    log.append("\n 关闭数据库连接失败").append(e.getMessage());
                }
            }
        }
    }

    /**
     * 入库详细
     * @param connection
     * @param windRadas
     * @param rece_time
     * @return
     */
    private static DataBaseAction insertDB(String fileN, Connection connection, List<WindRada> windRadas, Date rece_time, String fileName, StringBuffer log) {
        String tableName = StartConfig.valueTable();
        // 一个文件可能多个站点数据
        List<Boolean> list = new ArrayList<>();
        for (int i = 0; i < windRadas.size(); i++) {
            WindRada windRada = windRadas.get(i);
            if (windRada.getObeTime()!=null&&windRada.getObeTime().length() == 14) {
                boolean value = insertTable(fileN, connection, windRada, rece_time, tableName, log);
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
     * @param windRada
     * @param rece_time
     * @param tableName
     * @param log
     */
    private static boolean insertTable(String fileN, Connection connection, WindRada windRada, Date rece_time, String tableName, StringBuffer log) {
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
            StationData tStation = null;
//            boolean exist = isExist(wStation,log);
//            if (exist) {
//                tStation = getTStation();
//            } else {
//                // 不存在,记录区站号
//                log.append("\nNo corresponding sounding station was found").append(windRada.getStaionId());
//            }
            String recId =windRada.getStaionId()+ "_" + windRada.getObeTime() + "_" + windRada.get_OBS() + "_";
            List<WindData> windData = windRada.getwData();
            String[] ymdhm = TimeUtil.getYmdhms(windRada.getObeTime());
            
            boolean flag = false;
            for (WindData data : windData) {
                // di
                StatDi di = new StatDi();
                di.setFILE_NAME_O(fileN);
                di.setDATA_TYPE(getDataID(windRada.get_OBS()));
                di.setDATA_TYPE_1(getSourceID(windRada.get_OBS()));
                di.setTT("风廓线雷达");
                di.setTRAN_TIME(TimeUtil.date2String(rece_time, "yyyy-MM-dd HH:mm"));
                di.setPROCESS_START_TIME(TimeUtil.getSysTime());
                di.setFILE_NAME_N(fileN);
                di.setBUSINESS_STATE("1"); //1成功，0失败
                di.setPROCESS_STATE("1");  //1成功，0失败
                int ii = 1;
                ps.setString(ii++, recId + data.getHeight());// 记录标识
                ps.setTimestamp(ii++, new Timestamp(new Date().getTime()));// 入库时间
                ps.setTimestamp(ii++, new Timestamp(rece_time.getTime()));// 收到时间
                ps.setTimestamp(ii++, new Timestamp(new Date().getTime()));// 更新时间
                ps.setTimestamp(ii++, new Timestamp(TimeUtil.String2Date(windRada.getObeTime(), "yyyyMMddHHmmss").getTime()));// 观测时间
                ps.setString(ii++, getDataID(windRada.get_OBS()));// 资料标识
                ps.setString(ii++, windRada.getStaionId());// WMO区站号
                ps.setString(ii++, windRada.getRadaModel());// 风廓线雷达型号
                ps.setString(ii++, windRada.get_OBS());// 产品类型
                ps.setInt(ii++, Integer.parseInt(ymdhm[0]));// 年
                ps.setInt(ii++, Integer.parseInt(ymdhm[1]));// 月
                ps.setInt(ii++, Integer.parseInt(ymdhm[2]));// 日
                ps.setInt(ii++, Integer.parseInt(ymdhm[3]));// 时
                ps.setInt(ii++, Integer.parseInt(ymdhm[4]));// 分
                ps.setInt(ii++, Integer.parseInt(ymdhm[5]));// 秒
                ps.setBigDecimal(ii++, new BigDecimal(String.valueOf(windRada.getLatitude())));// 纬度
                ps.setBigDecimal(ii++, new BigDecimal(String.valueOf(windRada.getLongitude())));// 经度
                ps.setBigDecimal(ii++, new BigDecimal(String.valueOf(windRada.getAltitude())));// 测站高度
                ps.setDouble(ii++, 999998);// 气压传感器高度
                ps.setInt(ii++, data.getHeight());// 采样高度
                ps.setBigDecimal(ii++, new BigDecimal(String.valueOf(data.getWindDirection())));// 水平风向
                ps.setBigDecimal(ii++, new BigDecimal(String.valueOf(data.getWindSpeed())));// 水平风速
                ps.setBigDecimal(ii++, new BigDecimal(String.valueOf(data.getVwindSpeed())));// 垂直风速
                ps.setBigDecimal(ii++, new BigDecimal(String.valueOf(data.getHcredibility())));// 水平方向可信度
                ps.setBigDecimal(ii++, new BigDecimal(String.valueOf(data.getVcredibility())));// 垂直方向可信度
                ps.setString(ii++, data.getCn2());// 折射率结构常数Cn2
                // 风向质控
//              int windDQC = DQC(data.getWindDirection());
//              ps.setInt(ii++, windDQC);// 水平风向质控码
                
                // 风向质控
                int windDQC;
                // 风速质控
                int windSQC;
                // 高度为采样高度+测站海拔高度
                double hight = data.getHeight() + windRada.getAltitude();
//                if (exist) {
//                    windSQC = SQC(hight, data.getWindSpeed(), tStation, windRada);
//                } else {
//                    windSQC = SQC(hight, data.getWindSpeed());
//                }
                
                QualityCode qc[]=Chn_Upar_Profile_QuickQC0.getWindQC(windRada.getStaionId(), data.getWindSpeed(), data.getWindDirection(), hight, windRada.getLatitude(), windRada.getLongitude());
                windDQC=qc[0].getCode();
                windSQC=qc[1].getCode();
                ps.setInt(ii++, windDQC);// 水平风向质控码
                ps.setInt(ii++, windSQC);// 水平风速质控码
                ps.setInt(ii++, 9);// 垂直风速质控码
//                ps.setString(ii++, "999999");// 保留字段1
//                ps.setString(ii++, "999999");// 保留字段2
//                ps.setString(ii++, "999999");// 保留字段3
//                ps.setString(ii++, "999999");// 保留字段4
//                ps.setString(ii++, "999999");// 保留字段5
//                ps.setString(ii++, "999999");// 保留字段6
//                ps.setString(ii++, "999999");// 保留字段7
//                ps.setString(ii++, "999999");// 保留字段8
//                ps.setString(ii++, "999999");// 保留字段9
//                ps.setInt(ii++, 999999);// 保留字段10
//                ps.setInt(ii++, 999999);// 保留字段11
//                ps.setInt(ii++, 999999);// 保留字段12
//                ps.setInt(ii++, 999999);// 保留字段13
//                ps.setInt(ii++, 999999);// 保留字段14
//                ps.setInt(ii++, 999999);// 保留字段15
                ps.setString(ii++, getSourceID(windRada.get_OBS()));//资料四级编码
                // 补全di
                di.setIIiii(wStation);
                di.setLONGTITUDE(String.valueOf(windRada.getLongitude()));
                di.setLATITUDE(String.valueOf(windRada.getLatitude()));
                di.setDATA_TIME(TimeUtil.date2String(TimeUtil.String2Date(windRada.getObeTime(), "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm"));
                di.setPROCESS_END_TIME(TimeUtil.getSysTime());
                di.setRECORD_TIME(TimeUtil.getSysTime());
                
                di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
				di.setDATA_UPDATE_FLAG("000");
				di.setHEIGHT(String.valueOf(windRada.getAltitude()));
                
                
                listDi.add(di);
                System.out.println(((LoggableStatement)ps).getQueryString());
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
     * 获取资料标识
     * @param obs
     * @return
     */
    private static String getDataID(String obs) {
        String d_data_id = null;//_OBS(包括R,H,O)
        switch (obs.charAt(0)) {
            case 'R':
                d_data_id = "B.0015.0003.S001";
                break;
            case 'H':
                d_data_id = "B.0015.0004.S001";
                break;
            case 'O':
                d_data_id = "B.0015.0005.S001";
                break;
        }
        return d_data_id;
    }
    /**
     * 获取资料四级编码
     * @param obs
     * @return
     */
    private static String getSourceID(String obs) {
        String d_source_id = null;//_OBS(包括R,H,O)
        switch (obs.charAt(0)) {
            case 'R':
            	d_source_id = "B.0007.0003.R001";
                break;
            case 'H':
            	d_source_id = "B.0007.0004.R001";
                break;
            case 'O':
            	d_source_id = "B.0007.0005.R001";
                break;
        }
        return d_source_id;
    }

    /**
     * 获取键表头部字段
     * @param tableName
     * @return
     */
    private static String getSql(String tableName) {
        String sqlhead = "insert into " + tableName
                + " (D_RECORD_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,D_DATA_ID,V01301,V_RADAR_MODEL,V01398,V04001,V04002,V04003,V04004,V04005,V04006,V05001"
                + ",V06001,V07001,V07031,V07002,V11001,V11002,V11006,V73107,V73108,V25091,Q11001,Q11002,Q11006,D_SOURCE_ID)"
                + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        return sqlhead;
    }


    /**
     * 风速质控
     * 0 正确
     * 1 可疑
     * 8 缺测
     * 2 数据错误
     * @return
     */
    private static int SQC(int h, double s) {
        if (999999 == s) {
            return 8; // 数据缺测
        }
        int[] height = new int[]{-600, 3000, 5500, 7000, 14000, 22000, 30000, 36000, 39000, 99999};
        int[] value = new int[]{0, 100, 120, 150, 180, 170, 110, 140, 170, 220};
        int c = -1;
        // 找出采样高度所在区间
        for (int i = 1; i < height.length; i++) {
            if (h > height[i - 1] && h <= height[i]) {
                c = i - 1;
                break;
            }
        }
        // 为-1,值不在范围内
        if (c == -1) {
            return 2;
        }
        // 通过区间去允许值范围查找
        if (s >= 0 && s <= value[c + 1]) {
            return 0;
        }
        return 2;
    }

    /**
     * 风速质控
     * @param height
     * @param windSpeed
     * @param tStation
     * @param windRada
     * @return
     */
    private static int SQC(int height, double windSpeed, StationData tStation, WindRada windRada) {
        // 风速质控1
        int windSQC1 = SQC(height, windSpeed);
        // 风速质控2
        if (windSQC1 == 0) {
            int wind = getWSpeed(height);
            if (windSpeed <= wind) {
                return windSQC1;
            } else {
                return 2;
            }
        }
        return windSQC1;
    }

    /**
     * 风向质控
     * 允许值范围 0 ~ 360
     * 单位：度
     * @param a
     * @return
     */
    private static int DQC(double a) {
        if (999999 == (int) a) {
            return 8; // 数据缺测
        }
        if (a >= 0 && a <= 360) {
            return 0;
        }
        return 2;
    }
    public static void main(String[] args) {
//    	String fileName="D:\\TEMP\\fengkuoxian\\test-file\\Z_RADA_I_59072_20190101003014_P_WPRD_LC_ROBS.TXT";
//    	String fileName="D:\\TEMP\\fengkuoxian\\test-file\\Z_RADA_I_59072_20190101005504_P_WPRD_LC_ROBS.TXT";
//    	String fileName="D:\\TEMP\\fengkuoxian\\test-file\\Z_RADA_I_59486_20190101000000_P_WPRD_LC_OOBS.TXT";
//    	String fileName="D:\\TEMP\\fengkuoxian\\test-file\\Z_RADA_I_59486_20190101010000_P_WPRD_LC_OOBS.TXT";
//    	String fileName="D:\\TEMP\\fengkuoxian\\test-file\\Z_RADA_I_59486_20190101230000_P_WPRD_LC_HOBS.TXT";
    	String fileName="D:\\TEMP\\B.7.3.1\\Z_RADA_I_59476_20190413050000_P_WPRD_LC_HOBS.TXT";
    	File file = new File(fileName);
        Date rece_time = new Date(file.lastModified());
    	 DecodeWindRada windRada = new DecodeWindRada();
         ParseResult<WindRada> pr = windRada.decodeFile(fileName);
         StringBuffer log = new StringBuffer();
         if (pr.isSuccess()) {
        	 DataBaseAction action = DbService.processSuccessReport(pr, rece_time, fileName, log);
         }
//    	double v02=0;
//    	double V01=999999;
//    	double height=24291;
//    	double lat=24.8;
//    	double lon=112.37;
//    	QualityCode qc[]=Chn_Upar_Profile_QuickQC0.getWindQC("59072",v02, V01, height, lat, lon);
//        int windDQC=qc[0].getCode();
//        int windSQC=qc[1].getCode();
//        System.out.println(windDQC+" "+windSQC);
	}
}
