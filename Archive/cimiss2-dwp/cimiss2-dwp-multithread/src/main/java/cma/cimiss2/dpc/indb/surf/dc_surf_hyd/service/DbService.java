package cma.cimiss2.dpc.indb.surf.dc_surf_hyd.service;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.HdsqBean;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.core.tools.TimeUtil;

/**
 * ************************************
 * 
 * @ClassName: DbService
 * @Auther: fengmingyang
 * @Date: 2019/4/1 10:22
 * @Description: 水利部河道水情资料解析--数据入库
 * @Copyright: All rights reserver. ************************************
 */

public class DbService {

    private static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
   
    public static BlockingQueue<StatDi> diQueues;

    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
    	DbService.diQueues = diQueues;
    }
    
    public static BlockingQueue<StatDi> getDiQueues(){
    	return diQueues;
    }
    /**
     * 报文数据入库
     * @param pr
     * @param filePath
     * @return DataBaseAction
     */
    public static DataBaseAction processSuccessReport(ParseResult<HdsqBean> pr, String filePath, Date recv) {
        DruidPooledConnection connection = null;
        try {
            try {
//                if (StartConfig.getDatabaseType() == 0) {
//                    connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
//                } else if (StartConfig.getDatabaseType() == 1) {
                    connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
//                }
                List<HdsqBean> datas = pr.getData();
                // 批量插入
                DataBaseAction action = insertDB(connection, datas, filePath, recv);
                if (DataBaseAction.SUCCESS == action) {
                    return DataBaseAction.SUCCESS;
                } else {
                    return DataBaseAction.INSERT_ERROR;
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            infoLogger.error("数据库关闭失败！", e);
            return DataBaseAction.SUCCESS;
        } catch (Exception e) {
            infoLogger.error("Error", e);
            return DataBaseAction.CONNECTION_ERROR;
        }
    }

    /**
     * 入库详情
     * 
     * @param connection
     * @param hdsqBeans
     * @param filePath
     * @return DataBaseAction
     * @throws Exception
     */
    private static DataBaseAction insertDB(Connection connection, List<HdsqBean> hdsqBeans,
            String filePath, Date recv) throws Exception {
    	
        Statement st = null;
        List<String> sqls = new ArrayList<>();
        List<StatDi> dis = new ArrayList<>();
        parseData(hdsqBeans, sqls, dis, filePath, recv);
        // 是否存入数据成功，存入一条即成功
        boolean flag = false;
        // 为了在批量执行失败后，可以改用一条条插入 1、把数据都存起来备用 commit的时机是关键
        // 将list分组
        List<List<String>> splitSqls = splitList(sqls, 500);
        List<List<StatDi>> splitDiss = splitList(dis, 500);
        for (int i = 0; i < splitSqls.size(); i++) {
            // 来一组
            List<String> sqlList = splitSqls.get(i);
            List<StatDi> disList = splitDiss.get(i);
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            st = connection.createStatement();
            if (StartConfig.getDatabaseType() == 1) {
                st.execute("select last_txc_xid()");
            }
            try {
                for (String sql : sqlList) {
                    st.addBatch(sql);
                }
                int[] arr = st.executeBatch();
                connection.commit();
                // 把di信息提交
                for (StatDi di : disList) {
                    diQueues.offer(di);
                }
                if (!flag) {
                    flag = checkSuccess(arr);
                }
            } catch (SQLException e) {
                connection.rollback();
                if (!connection.getAutoCommit()) {
                    connection.setAutoCommit(true);
                }
                for (int j = 0; j < sqlList.size(); j++) {
                    String sql = sqlList.get(j);
                    try {
                        st.execute(sql);
                        if (disList.size() >= j) {
                            diQueues.offer(disList.get(j));
                        }
                        flag = true;
                    } catch (SQLException sQLException) {
                        // 一次提交出现问题
                        infoLogger.error("error in:" + sql+"\n"+ sQLException.getMessage());
                        if (disList != null && disList.size() > 0 && disList.size() >= j) {
                            StatDi statDi = disList.get(j);
                            statDi.setBUSINESS_STATE("0");// 1成功，0失败
                            statDi.setPROCESS_STATE("0");// 1成功，0失败
                            diQueues.offer(statDi);
                        }
                    }
                }
            }
            finally {
				if(st != null){
					try{
						st.close();
					}catch (Exception e) {
						infoLogger.error("close statement error!");
					}
				}
			}
        }
        System.out.println("批量插入完成！");
        if (flag) {
            return DataBaseAction.SUCCESS;
        } else {
            return DataBaseAction.INSERT_ERROR;
        }
    }

    /**
     * 把list切片分组
     * 
     * @param srcList
     * @param splitSize 分组大小
     * @return
     * @throws Exception
     */
    public static <T> List<List<T>> splitList(List<T> srcList, int splitSize) throws Exception {
        List<List<T>> lList = new ArrayList<>();
        if (splitSize <= 0) {
            throw new Exception("分片参数错误！");
        }
        // 获取被拆分的数组个数
        int arrSize = srcList.size() % splitSize == 0 ? srcList.size() / splitSize
                : srcList.size() / splitSize + 1;
        for (int i = 0; i < arrSize; i++) {
            List<T> subList = new ArrayList<T>();
            // 把指定索引数据放入到list中
            for (int j = i * splitSize; j <= splitSize * (i + 1) - 1; j++) {
                if (j <= srcList.size() - 1) {
                    subList.add(srcList.get(j));
                }
            }
            lList.add(subList);
        }
        return lList;
    }

    /**
     * 解析数据
     * 
     * @param hdsqBeans
     * @param sqls
     * @param dis
     * @param filePath
     * @throws ParseException
     */
    private static void parseData(List<HdsqBean> hdsqBeans, List<String> sqls, List<StatDi> dis,
            String filePath, Date recv) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String ctsCode = StartConfig.ctsCode();
        String sodCode = StartConfig.sodCode();// 入库值对应D_DATA_ID
        String reportTable = StartConfig.valueTable();
        File file = new File(filePath);
        String fileName = file.getName();
        for (HdsqBean hdsqBean : hdsqBeans) {
            // 获取站点经纬度信息
            String longtitude =
                    getStationInfoFromConfig(hdsqBean.getStationNumber(), "20", "longtitude");// 经度
            String latitude =
                    getStationInfoFromConfig(hdsqBean.getStationNumber(), "20", "latitude");// 纬度
            // 记录标识 时间+ 经纬度 + 区站号
            String recId = sdf2.format(sdf.parse(hdsqBean.getObsTime())) + "_" + longtitude + "_"
                    + latitude + "_" + hdsqBean.getStationNumber();
            StatDi di = new StatDi();
            di.setFILE_NAME_O(fileName);
            di.setDATA_TYPE(sodCode);
            di.setDATA_TYPE_1(ctsCode);
            di.setTT("水利部河道水情");
            di.setTRAN_TIME(TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm"));
            di.setPROCESS_START_TIME(TimeUtil.getSysTime());
            di.setFILE_NAME_N(fileName);
            di.setBUSINESS_STATE("1"); // 0成功，1失败
            di.setPROCESS_STATE("1"); // 0成功，1失败
            // 补全di
            di.setIIiii(hdsqBean.getStationNumber());
            di.setLONGTITUDE(longtitude);
            di.setLATITUDE(latitude);
            di.setDATA_TIME(TimeUtil.date2String(sdf.parse(hdsqBean.getObsTime()), "yyyy-MM-dd HH:mm"));
            di.setPROCESS_END_TIME(TimeUtil.getSysTime());
            di.setRECORD_TIME(TimeUtil.getSysTime());
            
            di.setSEND("BFDB");
			di.setSEND_PHYS("DRDS");
			di.setFILE_SIZE(String.valueOf(new File(filePath).length()));
			di.setDATA_UPDATE_FLAG("000");
			di.setHEIGHT("999999");
            
            
            dis.add(di);

            StringBuffer sql = new StringBuffer();
            sql.append(getValueSqlHead(reportTable));
            sql.append("'").append(recId).append("'"); // 记录标识
            sql.append("'").append(sodCode).append("'"); // 资料标识
            sql.append(",'").append(sdf.format(new Date())).append("'"); // 入库时间
            sql.append(",'").append(sdf.format(new Date(file.lastModified()))).append("'"); // 收到时间
            sql.append(",'").append(sdf.format(new Date())).append("'"); // 更新时间
            sql.append(",'").append(hdsqBean.getObsTime()).append("'"); // 资料时间
            sql.append(",'").append(hdsqBean.getFileInfo().replaceAll("Re", "")).append("'"); // 文件年月日
            sql.append(",'").append(hdsqBean.getStationNumber()).append("'"); // 区站号（字符）
            sql.append(",").append(getNumberTypeStationNumber(hdsqBean.getStationNumber())); // 区站号（数字）
            sql.append(",").append(latitude); // 纬度
            sql.append(",").append(longtitude); // 经度
            sql.append(",").append(999999); // 测站高度
//            sql.append(",").append("+20"); // 测站类型
            sql.append(",").append("999999"); // 测站类型
            sql.append(",").append(999999); // 台站级别
            sql.append(",").append(999999); // 中国行政区划代码
            String obsTime = hdsqBean.getObsTime();
            sql.append(",").append(
                    Integer.valueOf("".equals(obsTime) ? "999999" : obsTime.substring(0, 4))); // 年
            sql.append(",").append(
                    Integer.valueOf("".equals(obsTime) ? "999999" : obsTime.substring(5, 7))); // 月
            sql.append(",").append(
                    Integer.valueOf("".equals(obsTime) ? "999999" : obsTime.substring(8, 10))); // 日
            sql.append(",").append(
                    Integer.valueOf("".equals(obsTime) ? "999999" : obsTime.substring(11, 13))); // 时
            sql.append(",").append(hdsqBean.getWaterLevel()); // 水位或闸上水位
            sql.append(",").append(hdsqBean.getTraffic()); // 流量
            sql.append(",").append(hdsqBean.getChaoJing()); // 超警戒水位
            sql.append(",").append(getShuishiCode(hdsqBean.getShuiShi())); // 水势或闸上水势
            sql.append(",").append(hdsqBean.getInflow()); // 入流
            sql.append(",").append(hdsqBean.getOutflow()); // 出流
            sql.append(",").append(hdsqBean.getPondage()); // 蓄水量
            sql.append(",'").append(ctsCode).append("')"); // CTS编码
            sqls.add(sql.toString());
        }
    }

    /**
     * 获取数字类型区站号 区站号中存在字母，将字母进行ascii码转换
     * @param stationNumber
     * @return
     */
    private static String getNumberTypeStationNumber(String stationNumber) {
        String number = "999999";
        try {
            StringBuffer sb = new StringBuffer();
            char[] chars = stationNumber.toCharArray();
            for (char c : chars) {
                if (isLetter(c)) {
                    sb.append((int) c);
                } else {
                    sb.append(c);
                }
            }
            number = sb.toString();
        } catch (Exception e) {
            infoLogger.error("区站号不符合规则->", e);
        }
        return number;
    }

    /**
     * 字符是否为字母
     * 
     * @param c
     * @return
     */
    private static boolean isLetter(char c) {
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        }
        return false;
    }

    /**
     * 获取键表头部字段
     * 
     * @param valueTable
     * @return
     */
    private static String getValueSqlHead(String valueTable) {
        String sqlhead = "insert into " + valueTable
                + " (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_CYMD,V01301,V01300,V05001"
                + ",V06001,V07001,V02001,V02301,V_ACODE,V04001,V04002,V04003,V04004,V70011,V70012,V70013,V70017"
                + ",V70014,V70015,V70016,D_SOURCE_ID)" + " VALUES (";
        return sqlhead;
    }

    /**
     * 检查批量插入是否成功插入一条数据
     * 
     * @param arr
     * @return
     */
    private static boolean checkSuccess(int[] arr) {
        for (int i : arr) {
            if (i == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取水情码
     * 
     * @param shuishiValue
     * @return int
     */
    private static int getShuishiCode(String shuishiValue) {
    	if(shuishiValue.equals("999998"))
    		return 999998;
    	else{
	        int code = 999999;
	        switch (shuishiValue) {
	            case "平":
	                code = 0;
	                break;
	            case "涨":
	                code = 1;
	                break;
	            case "落":
	                code = 2;
	                break;
	            case "缺测":
	                code = 9;
	                break;
	            default:
	                break;
	        }
	        return code;
    	}
    }

    /**
     * 获取经纬度信息
     * 
     * @param stationNumberChina
     * @param stationTypeNo
     * @param key
     * @return String
     */
    private static String getStationInfoFromConfig(String stationNumberChina, String stationTypeNo,
            String key) {
        String resValue = "999999";
        try {
            Map<String, Object> proMap = StationInfo.getProMap();
            String info = (String) proMap.get(stationNumberChina + "+" + stationTypeNo);
            if(info != null){
	            String[] infos = info.split(",");
	           
	            if (infos.length < 10) {
	                return resValue;
	            } else {
	                key = key.toLowerCase();
	                switch (key) {
	                    case "longtitude":
	                        resValue = "null".equals(infos[1]) ? "999999" : infos[1];
	                        break;
	                    case "latitude":
	                        resValue = "null".equals(infos[2]) ? "999999" : infos[2];
	                        break;
	                    default:
	                        break;
	                }
	            }
            }
            else
            	infoLogger.error("Station " + stationNumberChina + " not exist in StationInfo_Config.lua");
        } catch (Exception e) {
            infoLogger.error("获取StationInfo_Config.lua 配置信息失败", e);
        }
        return resValue;
    }
}
