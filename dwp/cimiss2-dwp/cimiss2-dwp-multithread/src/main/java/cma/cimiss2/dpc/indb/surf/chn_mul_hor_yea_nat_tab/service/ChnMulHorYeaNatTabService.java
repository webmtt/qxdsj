package cma.cimiss2.dpc.indb.surf.chn_mul_hor_yea_nat_tab.service;

import cma.cimiss2.dpc.decoder.fileDecode.common.bean.DayValueTab;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.HourValueTab;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.MonthValueTab;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.SunLightValueTab;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ChnMulHorYeaNatTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");


    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues() {
        return diQueues;
    }

    public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
        ChnMulHorYeaNatTabService.diQueues = diQueues;
    }


    /**
     * 非标准格式农田小气候（中环天仪）的处理
     *
     * @param
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param filename
     * @return
     */
    public static DataBaseAction processSuccessReport(List<DayValueTab> dayList, List<HourValueTab> hourList,
                                                      List<SunLightValueTab> sunLightList, List<MonthValueTab> monthList,
                                                      Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;
        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        insertDB(dayList, hourList, sunLightList, monthList, connection, recv_time, loggerBuffer, filename, cts_code);
        return DataBaseAction.SUCCESS;

    }

    /**
     * 入库
     *
     * @param
     * @param connection
     * @param
     * @param loggerBuffer
     * @param
     * @param cts_code
     */
    private static void insertDB(List<DayValueTab> dayList, List<HourValueTab> hourList,
                                 List<SunLightValueTab> sunLightList, List<MonthValueTab> monthList,
                                 Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        filename = filename.substring(0, filename.indexOf("."));
        try {
            //日照表不入
            sunLightList=null;
            if (sunLightList != null && !sunLightList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("sunlight_value_table_name") + "(" +
                        "D_RETAIN_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V01301,V01300,V05001,V06001,V07001,V02301,V_ACODE,V14332,V04001,V04002,V04003,V14032_001,V14032_002,V14032_003,V14032_004,V14032_005,V14032_006,V14032_007,V14032_008,V14032_009,V14032_010,V14032_011,V14032_012,V14032_013," +
                        "V14032_014,V14032_015,V14032_016,V14032_017,V14032_018,V14032_019,V14032_020,V14032_021,V14032_022,V14032_023,V14032_024,V14032,Q14032_001,Q14032_002,Q14032_003,Q14032_004,Q14032_005,Q14032_006,Q14032_007,Q14032_008,Q14032_009,Q14032_010,Q14032_011,Q14032_012,Q14032_013,Q14032_014,Q14032_015,Q14032_016,Q14032_017,Q14032_018,Q14032_019," +
                        "Q14032_020,Q14032_021,Q14032_022,Q14032_023,Q14032_024,Q14032" +
                        ")values(" +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                int index = 0;
                for (SunLightValueTab sunLightValueTab : sunLightList) {
                    int ii = 1;
                    index += 1;
                    try {
                        ps.setString(ii++, sunLightValueTab.getD_RECORD_ID());// 记录标识 ( 系统自动生成的流水号 )
                        ps.setString(ii++, cts_code);// 资料标识 ( 资料的4级编码 )
                        ps.setString(ii++, sunLightValueTab.getD_IYMDHM());// 入库时间 ( 插表时的系统时间 )
                        ps.setString(ii++, sunLightValueTab.getD_RYMDHM());// 收到时间 ( DPC消息生成时间 )
                        ps.setString(ii++, sunLightValueTab.getD_UPDATE_TIME());// 更新时间 ( 根据CCx对记录更新时的系统时间 )
                        ps.setString(ii++, sunLightValueTab.getD_DATETIME());// 资料时间 ( 由V04001、04002、V04003组成 )
                        ps.setString(ii++, sunLightValueTab.getV_BBB());// 更正报标志 ( 报头行BBB )
                        ps.setString(ii++, sunLightValueTab.getV01301());// 区站号(字符)
                        if (sunLightValueTab.getV01300() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, sunLightValueTab.getV01300());// 区站号(数字)
                        }
                        if (sunLightValueTab.getV05001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV05001());// 纬度 ( 单位：度 )
                        }
                        if (sunLightValueTab.getV06001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV06001());// 经度 ( 单位：度 )
                        }
                        if (sunLightValueTab.getV07001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV07001());// 测站高度 ( 单位：米 )
                        }
                        if (sunLightValueTab.getV02301() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getV02301());// 台站级别 ( 代码表 )
                        }
                        if (sunLightValueTab.getV_ACODE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getV_ACODE());// 中国行政区划代码 ( 代码表 )
                        }
                        if (sunLightValueTab.getV14332() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getV14332());// 日照时制方式 ( 代码表 )
                        }
                        if (sunLightValueTab.getV04001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getV04001());// 资料观测年
                        }
                        if (sunLightValueTab.getV04002() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getV04002());// 资料观测月
                        }
                        if (sunLightValueTab.getV04003() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getV04003());// 资料观测日
                        }
                        if (sunLightValueTab.getV14032_001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_001());// 00-01时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_002() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_002());// 01-02时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_003() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_003());// 02-03时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_004() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_004());// 03-04时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_005() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_005());// 04-05时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_006() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_006());// 05-06时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_007() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_007());// 06-07时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_008() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_008());// 07-08时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_009() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_009());// 08-09时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_010() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_010());// 09-10时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_011() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_011());// 10-11时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_012() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_012());// 11-12时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_013() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_013());// 12-13时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_014() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_014());// 13_14时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_015() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_015());// 14-15时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_016() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_016());// 15-16时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_017() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_017());// 16-17时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_018() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_018());// 17-18时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_019() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_019());// 18-19时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_020() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_020());// 19-20时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_021() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_021());// 20-21时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_022() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_022());// 21-22时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_023() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_023());// 22-23时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032_024() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032_024());// 23-24时日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getV14032() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, sunLightValueTab.getV14032());// 日日照时数 ( 单位：小时 )
                        }
                        if (sunLightValueTab.getQ14032_001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_001());// 00-01时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_002() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_002());// 01-02时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_003() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_003());// 02-03时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_004() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_004());// 03-04时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_005() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_005());// 04-05时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_006() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_006());// 05-06时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_007() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_007());// 06-07时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_008() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_008());// 07-08时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_009() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_009());// 08-09时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_010() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_010());// 09-10时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_011() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_011());// 10-11时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_012() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_012());// 11-12时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_013() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_013());// 12-13时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_014() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_014());// 13_14时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_015() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_015());// 14-15时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_016() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_016());// 15-16时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_017() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_017());// 16-17时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_018() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_018());// 17-18时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_019() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_019());// 18-19时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_020() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_020());// 19-20时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_021() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_021());// 20-21时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_022() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_022());// 21-22时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_023() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_023());// 22-23时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032_024() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032_024());// 23-24时日照时数质控码 ( 代码表 )
                        }
                        if (sunLightValueTab.getQ14032() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, sunLightValueTab.getQ14032());// 日日照时数质控码 ( 代码表 )
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("列号：" + ii + "有误");
                    }
                    ps.addBatch();
                    if (index % 1000 == 0) {
                        ps.executeBatch();
                        connection.commit();
                        ps.clearBatch();
                    }
                }
                if (index % 1000 != 0) {
                    ps.executeBatch();
                    connection.commit();
                    ps.clearBatch();
                }
            }
            if (hourList != null && !hourList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("hour_value_table_name") + "(" +
                        "D_RETAIN_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V01301,V01300,V05001,V06001,V07001,V07031,V07032_04,V02001,V02301,V_ACODE,V04001,V04002,V04003,V04004,V10004,V10051,V10061,V10062,V10301,V10301_052,V10302,V10302_052,V12001,V12011," +
                        "V12011_052,V12012,V12012_052,V12405,V12016,V12017,balltemp,V12003,V13003,V13007,V13007_052,V13004,V13019,V13020,V13021,V13022,V13023,V04080_04,V13011,V13033,V11290,V11291,V11292,V11293,V11296,V11042,V11042_052,V11201,V11202,V11211,V11046," +
                        "V11046_052,V11503_06,V11504_06,V11503_12,V11504_12,V12120,V12311,V12311_052,V12121,V12121_052,V12013,V12030_005,V12030_010,V12030_015,V12030_020,V12030_040,V12030_080,V12030_160,V12030_320,V12314,V12315,V12315_052,V12316,V12316_052,V20001_701_01,V20001_701_10,V20059,V20059_052,V20001,visibility2,V20010," +
                        "V20051,V20011,V20013,V20350_01,V20350_02,V20350_03,V20350_04,V20350_05,V20350_06,V20350_07,V20350_08,V20350_11,V20350_12,V20350_13,V20003,V04080_05,V20004,V20005,V20062,V13013,V13330,V20330_01,V20331_01,V20330_02,V20331_02,v14320,v14308,Q10004,Q10051,Q10061,Q10062," +
                        "Q10301,Q10301_052,Q10302,Q10302_052,Q12001,Q12011,Q12011_052,Q12012,Q12012_052,Q12405,Q12016,Q12017,Q12003,Q13003,Q13007,Q13007_052,Q13004,Q13019,Q13020,Q13021,Q13022,Q13023,Q04080_04,Q13011,Q13033,Q11290,Q11291,Q11292,Q11293,Q11296,Q11042," +
                        "Q11042_052,Q11201,Q11202,Q11211,Q11046,Q11046_052,Q11503_06,Q11504_06,Q11503_12,Q11504_12,Q12120,Q12311,Q12311_052,Q12121,Q12121_052,Q12013,Q12030_005,Q12030_010,Q12030_015,Q12030_020,Q12030_040,Q12030_080,Q12030_160,Q12030_320,Q12314,Q12315,Q12315_052,Q12316,Q12316_052,Q20001_701_01,Q20001_701_10," +
                        "Q20059,Q20059_052,Q20001,Q20010,Q20051,Q20011,Q20013,Q20350_01,Q20350_02,Q20350_03,Q20350_04,Q20350_05,Q20350_06,Q20350_07,Q20350_08,Q20350_11,Q20350_12,Q20350_13,Q20003,Q04080_05,Q20004,Q20005,Q20062,Q13013,Q13330,Q20330_01,Q20331_01,Q20330_02,Q20331_02" +
                        ")values(" +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);


                int index = 0;

                for (HourValueTab HourValueTab : hourList) {
                    int ii = 1;
                    index += 1;
                    try {
                        ps.setString(ii++, HourValueTab.getD_MAIN_ID());//记录标识 ( 系统自动生成的流水号 )
                        ps.setString(ii++, cts_code);//资料标识 ( 资料的4级编码 )
                        ps.setString(ii++, HourValueTab.getD_IYMDHM());//入库时间 ( 插表时的系统时间 )
                        ps.setString(ii++, HourValueTab.getD_RYMDHM());//收到时间 ( DPC消息生成时间 )
                        ps.setString(ii++, HourValueTab.getD_UPDATE_TIME());//更新时间 ( 根据CCx对记录更新时的系统时间 )
                        ps.setString(ii++, HourValueTab.getD_DATETIME());//资料时间 ( 由V04001、V04002、V04003、V04004 )
                        ps.setString(ii++, HourValueTab.getV_BBB());//更正标志 ( 组成 )
                        ps.setString(ii++, HourValueTab.getV01301());//区站号(字符)
                        ps.setString(ii++, HourValueTab.getV01300());//区站号(数字)
                        if (HourValueTab.getV05001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV05001());//纬度
                        }
                        if (HourValueTab.getV06001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV06001());//经度 ( 单位：度 )
                        }
                        if (HourValueTab.getV07001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV07001());//测站高度 ( 单位：度 )
                        }
                        if (HourValueTab.getV07031() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV07031());//气压传感器海拔高度 ( 单位：米 )
                        }
                        if (HourValueTab.getV07032_04() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV07032_04());//风速传感器距地面高度 ( 单位：米 )
                        }
                        ps.setString(ii++, HourValueTab.getV02001());//测站类型 ( 代码表 )
                        if (HourValueTab.getV02301() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV02301());//台站级别 ( 代码表 )
                        }
                        if (HourValueTab.getV_ACODE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV_ACODE());//中国行政区划代码 ( 代码表 )
                        }
                        if (HourValueTab.getV04001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV04001());//资料观测年 ( 代码表 )
                        }
                        if (HourValueTab.getV04002() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV04002());//资料观测月
                        }
                        if (HourValueTab.getV04003() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV04003());//资料观测日
                        }
                        if (HourValueTab.getV04004() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV04004());//资料观测时
                        }
                        if (HourValueTab.getV10004() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV10004());//本站气压
                        }
                        if (HourValueTab.getV10051() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV10051());//海平面气压 ( 单位：百帕 )
                        }
                        if (HourValueTab.getV10061() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV10061());//3小时变压 ( 单位：百帕 )
                        }
                        if (HourValueTab.getV10062() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV10062());//24小时变压 ( 单位：百帕 )
                        }
                        if (HourValueTab.getV10301() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV10301());//小时内最高本站气压 ( 单位：百帕 )
                        }
                        if (HourValueTab.getV10301_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV10301_052());//小时内最高本站气压出现时间 ( 格式：hhmm )
                        }
                        if (HourValueTab.getV10302() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV10302());//小时内最低本站气压 ( 单位：百帕  )
                        }
                        if (HourValueTab.getV10302_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV10302_052());//小时内最低本站气压出现时间 ( 格式：hhmm )
                        }
                        if (HourValueTab.getV12001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12001());//气温 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12011() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12011());//小时内最高气温 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12011_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV12011_052());//小时内最高气温出现时间 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12012() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12012());//小时内最低气温 (单位：摄氏度  )
                        }
                        if (HourValueTab.getV12012_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV12012_052());//小时内最低气温出现时间 ( 格式：hhmm )
                        }
                        if (HourValueTab.getV12405() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12405());//24小时变温 (单位：摄氏度  )
                        }
                        if (HourValueTab.getV12016() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12016());//过去24小时最高气温 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12017() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12017());//过去24小时最低气温 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getBalltemp() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getBalltemp());//湿球温度
                        }
                        if (HourValueTab.getV12003() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12003());//露点温度 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV13003() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV13003());//相对湿度 (单位：% )
                        }
                        if (HourValueTab.getV13007() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV13007());//小时内最小相对湿度 ( 单位：% )
                        }
                        if (HourValueTab.getV13007_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV13007_052());//小时内最小相对湿度出现时间 (格式：hhmm)
                        }
                        if (HourValueTab.getV13004() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV13004());//水汽压 (单位：百帕 )
                        }
                        if (HourValueTab.getV13019() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV13019());//1小时降水量 ( 单位：毫米   )
                        }
                        if (HourValueTab.getV13020() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV13020());//过去3小时降水量 ( 单位：毫米 )
                        }
                        if (HourValueTab.getV13021() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV13021());//过去6小时降水量 ( 单位：毫米 )
                        }
                        if (HourValueTab.getV13022() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV13022());//过去12小时降水量 ( 单位：毫米 )
                        }
                        if (HourValueTab.getV13023() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV13023());//过去24小时降水量 ( 单位：毫米 )
                        }
                        if (HourValueTab.getV04080_04() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV04080_04());//人工加密观测降水量描述时间周期 ( 单位：毫米 )
                        }
                        if (HourValueTab.getV13011() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV13011());//人工加密观测降水量 ( 单位：小时 )
                        }
                        if (HourValueTab.getV13033() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV13033());//小时蒸发量 ( 单位：毫米 )
                        }
                        ps.setString(ii++, HourValueTab.getV11290());//2分钟平均风向 ( 单位：度 )
                        if (HourValueTab.getV11291() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV11291());//2分钟平均风速 ( 单位：米/秒  )
                        }
                        ps.setString(ii++, HourValueTab.getV11292());//10分钟平均风向 ( 单位：度  )
                        if (HourValueTab.getV11293() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV11293());//10分钟平均风速 (单位：米/秒)
                        }
                        ps.setString(ii++, HourValueTab.getV11296());//小时内最大风速的风向 ( 单位：度 )
                        if (HourValueTab.getV11042() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV11042());//小时内最大风速 (单位：米/秒)
                        }
                        if (HourValueTab.getV11042_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV11042_052());//小时内最大风速出现时间 ( 格式：hhmm )
                        }
                        ps.setString(ii++, HourValueTab.getV11201());//瞬时风向 ( 单位：度 )
                        if (HourValueTab.getV11202() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV11202());//瞬时风速 ( 单位：米/秒 )
                        }
                        if (HourValueTab.getV11211() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV11211());//小时内极大风速的风向 ( 单位：米/秒 )
                        }
                        if (HourValueTab.getV11046() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV11046());//小时内极大风速 ( 单位：度 )
                        }
                        if (HourValueTab.getV11046_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV11046_052());//小时内极大风速出现时间 (格式：hhmm  )
                        }
                        if (HourValueTab.getV11503_06() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV11503_06());//过去6小时极大瞬时风向 (单位：度  )
                        }
                        if (HourValueTab.getV11504_06() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV11504_06());//过去6小时极大瞬时风速 (单位：米/秒  )
                        }
                        if (HourValueTab.getV11503_12() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV11503_12());//过去12小时极大瞬时风向 ( 单位：度  )
                        }
                        if (HourValueTab.getV11504_12() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV11504_12());//过去12小时极大瞬时风速 ( 单位：米/秒)
                        }
                        if (HourValueTab.getV12120() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12120());//地面温度 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12311() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12311());//小时内最高地面温度 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12311_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV12311_052());//小时内最高地面温度出现时间 (  格式：hhmm)
                        }
                        if (HourValueTab.getV12121() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12121());//小时内最低地面温度 (单位：摄氏度  )
                        }
                        if (HourValueTab.getV12121_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV12121_052());//小时内最低地面温度出现时间 ( 格式：hhmm)
                        }
                        if (HourValueTab.getV12013() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12013());//过去12小时最低地面温度 (单位：摄氏度  )
                        }
                        if (HourValueTab.getV12030_005() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12030_005());//5cm地温 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12030_010() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12030_010());//10cm地温 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12030_015() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12030_015());//15cm地温 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12030_020() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12030_020());//20cm地温 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12030_040() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12030_040());//40cm地温 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12030_080() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12030_080());//80cm地温 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12030_160() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12030_160());//160cm地温 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12030_320() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12030_320());//320cm地温 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12314() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12314());//草面（雪面）温度 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12315() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12315());//小时内草面（雪面）最高温度 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12315_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV12315_052());//小时内草面（雪面）最高温度出现时间 ( 格式：hhmm )
                        }
                        if (HourValueTab.getV12316() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV12316());//小时内草面（雪面）最低温度 ( 单位：摄氏度 )
                        }
                        if (HourValueTab.getV12316_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV12316_052());//小时内草面（雪面）最低温度出现时间 ( 格式：hhmm  )
                        }
                        if (HourValueTab.getV20001_701_01() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV20001_701_01());//1分钟平均水平能见度 ( 单位：米 )
                        }
                        if (HourValueTab.getV20001_701_10() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV20001_701_10());//10分钟平均水平能见度 ( 单位：米 )
                        }
                        if (HourValueTab.getV20059() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV20059());//最小水平能见度 ( 单位：米 )
                        }
                        ps.setString(ii++, HourValueTab.getV20059_052());//最小水平能见度出现时间 ( 格式：hhmm  )
                        if (HourValueTab.getV20001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV20001());//水平能见度（人工） ( 单位：千米 )
                        }
                        if (HourValueTab.getVisibility2() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getVisibility2());//能见度级别
                        }
                        if (HourValueTab.getV20010() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20010());//总云量 (单位：%  )
                        }
                        if (HourValueTab.getV20051() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20051());//低云量 ( 单位：% )
                        }
                        if (HourValueTab.getV20011() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV20011());//低云或中云的云量 ( 单位：% )
                        }
                        if (HourValueTab.getV20013() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV20013());//低云或中云的云高 ( 单位：% )
                        }
                        if (HourValueTab.getV20350_01() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20350_01());//云状1 ( 单位：米 )
                        }
                        ps.setString(ii++, HourValueTab.getV20350_02());//云状2 ( 代码表 )
                        if (HourValueTab.getV20350_03() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20350_03());//云状3 ( 代码表 )
                        }
                        if (HourValueTab.getV20350_04() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20350_04());//云状4 ( 代码表 )
                        }
                        if (HourValueTab.getV20350_05() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20350_05());//云状5 ( 代码表 )
                        }
                        if (HourValueTab.getV20350_06() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20350_06());//云状6 ( 代码表 )
                        }
                        if (HourValueTab.getV20350_07() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20350_07());//云状7 ( 代码表 )
                        }
                        if (HourValueTab.getV20350_08() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20350_08());//云状8 ( 代码表 )
                        }
                        if (HourValueTab.getV20350_11() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20350_11());//低云状 ( 代码表 )
                        }
                        if (HourValueTab.getV20350_12() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20350_12());//中云状 ( 代码表 )
                        }
                        if (HourValueTab.getV20350_13() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20350_13());//高云状 ( 代码表 )
                        }
                        if (HourValueTab.getV20003() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20003());//现在天气 ( 代码表 )
                        }
                        if (HourValueTab.getV04080_05() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV04080_05());//过去天气描述时间周期 ( 代码表 )
                        }
                        if (HourValueTab.getV20004() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20004());//过去天气1 ( 单位:小时 )
                        }
                        if (HourValueTab.getV20005() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20005());//过去天气2 ( 代码表 )
                        }
                        if (HourValueTab.getV20062() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getV20062());//地面状态 ( 代码表 )
                        }
                        if (HourValueTab.getV13013() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV13013());//积雪深度 ( 代码表 )
                        }
                        if (HourValueTab.getV13330() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV13330());//雪压 ( 单位：厘米 )
                        }
                        if (HourValueTab.getV20330_01() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV20330_01());//冻土第1冻结层上限值 ( 单位：g/cm2 )
                        }
                        if (HourValueTab.getV20331_01() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV20331_01());//冻土第1冻结层下限值 ( 单位：厘米 )
                        }
                        if (HourValueTab.getV20330_02() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV20330_02());//冻土第2冻结层上限值 ( 单位：厘米 )
                        }
                        if (HourValueTab.getV20331_02() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, HourValueTab.getV20331_02());//冻土第2冻结层下限值 ( 单位：厘米 )
                        }
                        if (HourValueTab.getV14320() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, HourValueTab.getV14320());//总辐射曝辐量
                        }
                        if (HourValueTab.getV14308() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, HourValueTab.getV14308());//净辐射曝辐量
                        }
                        if (HourValueTab.getQ10004() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ10004());//本站气压质量标志 ( 单位：厘米 )
                        }
                        if (HourValueTab.getQ10051() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ10051());//海平面气压质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ10061() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ10061());//3小时变压质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ10062() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ10062());//24小时变压质质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ10301() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ10301());//小时内最高本站气压质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ10301_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ10301_052());//小时内最高本站气压出现时间质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ10302() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ10302());//小时内最低本站气压质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ10302_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ10302_052());//小时内最低本站气压出现时间质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12001());//气温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12011() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12011());//小时内最高气温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12011_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12011_052());//小时内最高气温出现时间质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12012() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12012());//小时内最低气温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12012_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12012_052());//小时内最低气温出现时间质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12405() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12405());//24小时变温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12016() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12016());//过去24小时最高气温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12017() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12017());//过去24小时最低气温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12003() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12003());//露点温度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13003() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13003());//相对湿度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13007() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13007());//小时内最小相对湿度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13007_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13007_052());//小时内最小相对湿度出现时间质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13004() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13004());//水汽压质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13019() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13019());//1小时降水量质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13020() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13020());//过去3小时降水量质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13021() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13021());//过去6小时降水量质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13022() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13022());//过去12小时降水量质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13023() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13023());//24小时降水量质 ( 代码表 )
                        }
                        if (HourValueTab.getQ04080_04() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ04080_04());//人工加密观测降水量描述时间周期质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13011() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13011());//人工加密观测降水量质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13033() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13033());//小时蒸发量质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11290() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11290());//2分钟平均风向质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11291() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11291());//2分钟平均风速质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11292() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11292());//10分钟平均风向质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11293() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11293());//10分钟平均风速质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11296() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11296());//小时内最大风速的风向质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11042() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11042());//小时内最大风速质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11042_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11042_052());//小时内最大风速出现时间质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11201() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11201());//瞬时风向质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11202() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11202());//瞬时风速质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11211() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11211());//小时内极大风速的风向质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11046() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11046());//小时内极大风速质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11046_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11046_052());//小时内极大风速出现时间质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11503_06() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11503_06());//过去6小时极大瞬时风向质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11504_06() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11504_06());//过去6小时极大瞬时风速质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11503_12() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11503_12());//过去12小时极大瞬时风向质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ11504_12() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ11504_12());//过去12小时极大瞬时风速质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12120() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12120());//地面温度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12311() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12311());//小时内最高地面温度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12311_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12311_052());//小时内最高地面温度出现时间质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12121() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12121());//小时内最低地面温度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12121_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12121_052());//小时内最低地面温度出现时间质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12013() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12013());//过去12小时最低地面温度 ( 代码表 )
                        }
                        if (HourValueTab.getQ12030_005() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12030_005());//5cm地温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12030_010() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12030_010());//10cm地温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12030_015() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12030_015());//15cm地温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12030_020() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12030_020());//20cm地温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12030_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12030_040());//40cm地温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12030_080() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12030_080());//80cm地温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12030_160() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12030_160());//160cm地温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12030_320() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12030_320());//320cm地温质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12314() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12314());//草面（雪面）温度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12315() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12315());//小时内草面（雪面）最高温度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12315_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12315_052());//小时内草面（雪面）最高温度出现时间质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12316() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12316());//小时内草面（雪面）最低温度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ12316_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ12316_052());//小时内草面（雪面）最低温度出现时间质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20001_701_01() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20001_701_01());//1分钟平均水平能见度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20001_701_10() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20001_701_10());//10分钟平均水平能见度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20059() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20059());//最小水平能见度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20059_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20059_052());//最小水平能见度出现时间质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20001());//水平能见度（人工）质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20010() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20010());//总云量质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20051() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20051());//低云量质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20011() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20011());//低云或中云的云量质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20013() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20013());//低云或中云的云高质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20350_01() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20350_01());//云状1质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20350_02() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20350_02());//云状2质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20350_03() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20350_03());//云状3质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20350_04() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20350_04());//云状4质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20350_05() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20350_05());//云状5质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20350_06() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20350_06());//云状6质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20350_07() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20350_07());//云状7质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20350_08() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20350_08());//云状8质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20350_11() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20350_11());//低云状质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20350_12() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20350_12());//中云状质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20350_13() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20350_13());//高云状质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20003() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20003());//现在天气质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ04080_05() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ04080_05());//过去天气描述时间周期质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20004() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20004());//过去天气1质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20005() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20005());//过去天气2质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20062() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20062());//地面状态质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13013() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13013());//积雪深度质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ13330() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ13330());//雪压质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20330_01() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20330_01());//冻土第1冻结层上限值质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20331_01() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20331_01());//冻土第1冻结层下限值质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20330_02() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20330_02());//冻土第2冻结层上限值质量标志 ( 代码表 )
                        }
                        if (HourValueTab.getQ20331_02() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, HourValueTab.getQ20331_02());//冻土第2冻结层下限值质量标志 ( 代码表 )
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("列号：" + ii + "有误");
                    }
                    ps.addBatch();
                    if (index % 1000 == 0) {
                        ps.executeBatch();
                        connection.commit();
                        ps.clearBatch();
                    }
                }
                if (index % 1000 != 0) {
                    ps.executeBatch();
                    connection.commit();
                    ps.clearBatch();
                }
            }
            if (dayList != null && !dayList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("day_value_table_name") + "(" +
                        "D_RETAIN_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V01301,V01300,V05001,V06001,V07001,V07031,V02301,V_ACODE,V04001,V04002,V04003,V10004_701,V10301,V10301_052,V10302,V10302_052,V10051_701,V12001_701,V12011,V12011_052,V12012,V12303_701,V12012_052,V13004_701," +
                        "V13004_MAX,V13004_MIN,V13003_701,V13007,V13007_052,htem100,htem200,htem300,obversetype,obsercecode,qualitycode,arcaninehight,rain1,rain10,wetherSymbol,wetherStartTime,wetherEndTime,glazeNSDia,glazeWEDia,glazeNSHight,glazeWEHight,glazeNSWeight,glazeWEWeight,rimeNSDia,rimeWEDia,rimeNSHight,rimeWEHight,rimeNSWeight,rimeWEWeight,V20010_701,V20051_701," +
                        "V20059,V20059_052,V13302_01,V13302_01_052,V13308,V13309,V13305,V13306,V13032,V13033,V13013,V13330,V20305,V20326_NS,V20306_NS,V20307_NS,V20326_WE,V20306_WE,V20307_WE,V12001,V11001,V11002,V11290_CHAR,V11291_701,V11293_701,V11296,V11042,V11042_052,V11211,V11046,V11046_052," +
                        "V12120_701,V12311,V12311_052,V12121,V12121_052,V12030_701_005,V12030_701_010,V12030_701_015,V12030_701_020,V12030_701_040,V12030_701_080,V12030_701_160,V12030_701_320,V20331,V20330_01,V20331_01,V20330_02,V20331_02,V12003_avg,balltemp_avg,balltemp_avg_24,V14032,V14033,V20411,V20412,sunTime,V12314_701,V12315,V12315_052,V12316,V12316_052," +
                        "V20062,V20302_060,V20302_060_052,V20302_070,V20302_070_052,V20302_089,V20302_089_052,V20302_042,V20302_042_052,V20302_010,V20302_001,V20302_002,V20302_056,V20302_056_052,V20302_048,V20302_048_052,V20302_038,V20302_038_052,V20302_039,V20302_039_052,V20302_019,V20302_019_052,V20302_016,V20302_003,V20302_031,V20302_031_052,V20302_007,V20302_007_052,V20302_006,V20302_006_052,V20302_004," +
                        "V20302_005,V20302_008,V20302_076,V20302_017,V20302_017_052,V20302_013,V20302_014,V20302_014_052,V20302_015,V20302_015_052,V20302_018,V20302_018_052,V20303,V20304,v14311,v14311_05,v14021_05_052,v14312,v14312_05,v14312_05_052,v14314,v14308,v14313,Q10004_701,Q10301,Q10301_052,Q10302,Q10302_052,Q10051_701,Q12001_701,Q12011," +
                        "Q12011_052,Q12012,Q12012_052,Q13004_701,Q13003_701,Q13007,Q13007_052,Q20010_701,Q20051_701,Q20059,Q20059_052,Q13302_01,Q13302_01_052,Q13308,Q13309,Q13305,Q13306,Q13032,Q13033,Q13013,Q13330,Q20305,Q20326_NS,Q20306_NS,Q20307_NS,Q20326_WE,Q20306_WE,Q20307_WE,Q12001,Q11001,Q11002," +
                        "Q11290_CHAR,Q11291_701,Q11293_701,Q11296,Q11042,Q11042_052,Q11211,Q11046,Q11046_052,Q12120_701,Q12311,Q12311_052,Q12121,Q12121_052,Q12030_701_005,Q12030_701_010,Q12030_701_015,Q12030_701_020,Q12030_701_040,Q12030_701_080,Q12030_701_160,Q12030_701_320,Q20330_01,Q20331_01,Q20330_02,Q20331_02,Q14032,Q20411,Q20412,Q12314_701,Q12315," +
                        "Q12315_052,Q12316,Q12316_052,Q20062,Q20302_060,Q20302_060_052,Q20302_070,Q20302_070_052,Q20302_089,Q20302_089_052,Q20302_042,Q20302_042_052,Q20302_010,Q20302_001,Q20302_002,Q20302_056,Q20302_056_052,Q20302_048,Q20302_048_052,Q20302_038,Q20302_038_052,Q20302_039,Q20302_039_052,Q20302_019,Q20302_019_052,Q20302_016,Q20302_003,Q20302_031,Q20302_031_052,Q20302_007,Q20302_007_052," +
                        "Q20302_006,Q20302_006_052,Q20302_004,Q20302_005,Q20302_008,Q20302_076,Q20302_017,Q20302_017_052,Q20302_013,Q20302_014,Q20302_014_052,Q20302_015,Q20302_015_052,Q20302_018,Q20302_018_052,Q20303,Q20304" +
                        ")values(" +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                int index = 0;
                for (DayValueTab dayValueTab : dayList) {
                    int ii = 1;
                    index += 1;
                    try {
                        ps.setString(ii++, dayValueTab.getD_RECORD_ID());// 记录标识 ( 系统自动生成的流水号 )
                        ps.setString(ii++, cts_code);// 资料标识 ( 资料的4级编码 )
                        ps.setString(ii++, dayValueTab.getD_IYMDHM());// 入库时间 ( 插表时的系统时间 )
                        ps.setString(ii++, dayValueTab.getD_RYMDHM());// 收到时间 ( DPC消息生成时间 )
                        ps.setString(ii++, dayValueTab.getD_UPDATE_TIME());// 更新时间 ( 根据CCx对记录更新时的系统时间 )
                        ps.setString(ii++, dayValueTab.getD_DATETIME());// 资料时间 ( 由V04001、V04002、V04003组成 )
                        ps.setString(ii++, dayValueTab.getV_BBB());// 更正报标志
                        ps.setString(ii++, dayValueTab.getV01301());// 区站号(字符)
                        if (dayValueTab.getV01300() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, dayValueTab.getV01300());// 区站号(数字)
                        }
                        if (dayValueTab.getV05001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV05001());// 纬度 ( 单位：度 )
                        }
                        if (dayValueTab.getV06001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV06001());// 经度 ( 单位：度 )
                        }
                        if (dayValueTab.getV07001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV07001());// 测站高度 ( 单位：米 )
                        }
                        if (dayValueTab.getV07031() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV07031());// 气压传感器海拔高度 ( 单位：米 )
                        }
                        if (dayValueTab.getV02301() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV02301());// 台站级别 ( 代码表 )
                        }
                        if (dayValueTab.getV_ACODE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV_ACODE());// 中国行政区划代码 ( 代码表 )
                        }
                        if (dayValueTab.getV04001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV04001());// 年
                        }
                        if (dayValueTab.getV04002() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV04002());// 月
                        }
                        if (dayValueTab.getV04003() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV04003());// 日
                        }
                        if (dayValueTab.getV10004_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV10004_701());// 日平均本站气压 ( 单位：百帕 )
                        }
                        if (dayValueTab.getV10301() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV10301());// 日最高本站气压 ( 单位：百帕 )
                        }
                        ps.setString(ii++, dayValueTab.getV10301_052());// 日最高本站气压出现时间 ( 格式：时分 )
                        if (dayValueTab.getV10302() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV10302());// 日最低本站气压 ( 单位：百帕 )
                        }
                        ps.setString(ii++, dayValueTab.getV10302_052());// 日最低本站气压出现时间 ( 格式：时分 )
                        if (dayValueTab.getV10051_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV10051_701());// 日平均海平面气压 ( 单位：百帕 )
                        }
                        if (dayValueTab.getV12001_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12001_701());// 日平均气温 ( 单位：摄氏度 )
                        }
                        if (dayValueTab.getV12011() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12011());// 日最高气温 ( 单位：摄氏度 )
                        }
                        ps.setString(ii++, dayValueTab.getV12011_052());// 日最高气温出现时间 ( 格式：时分 )
                        if (dayValueTab.getV12012() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12012());// 日最低气温 ( 单位：摄氏度 )
                        }
                        if (dayValueTab.getV12303_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12303_701());//日气温日较差（单位：摄氏度）
                        }
                        ps.setString(ii++, dayValueTab.getV12012_052());// 日最低气温出现时间 ( 格式：时分 )
                        if (dayValueTab.getV13004_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV13004_701());// 日平均水汽压 ( 单位：百帕 )
                        }
                        if (dayValueTab.getV13004_MAX() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV13004_MAX());//日最大水汽压
                        }
                        if (dayValueTab.getV13004_MIN() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV13004_MIN());//日最小水气压
                        }
                        if (dayValueTab.getV13003_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV13003_701());// 日平均相对湿度 ( 单位：% )
                        }
                        if (dayValueTab.getV13007() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV13007());// 日最小相对湿度 ( 单位：% )
                        }
                        ps.setString(ii++, dayValueTab.getV13007_052());// 日最小相对湿度出现时间 ( 格式：时分 )
                        if (dayValueTab.getHtem100() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getHtem100());//100cm深层地温
                        }
                        if (dayValueTab.getHtem200() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getHtem200());//200cm深层地温
                        }
                        if (dayValueTab.getHtem300() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getHtem300());//330cm深层地温
                        }
                        if (dayValueTab.getObversetype() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, dayValueTab.getObversetype());// 观测方式和测站类别
                        }
                        if (dayValueTab.getObsercecode() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, dayValueTab.getObsercecode());// 观测项目标识
                        }
                        if (dayValueTab.getQualitycode() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, dayValueTab.getQualitycode());// 质量控制指示码
                        }
                        if (dayValueTab.getArcaninehight() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, dayValueTab.getArcaninehight());// 风速器距地高度
                        }
                        if (dayValueTab.getRain1() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getRain1());//自记1小时降水
                        }
                        if (dayValueTab.getRain10() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getRain10());//自记10分钟最大降水
                        }
                        ps.setString(ii++, dayValueTab.getWetherSymbol());//天气符号
                        ps.setString(ii++, dayValueTab.getWetherStartTime());//天气开始时间-时+分
                        ps.setString(ii++, dayValueTab.getWetherEndTime());//天气结束时间-时+分
                        if (dayValueTab.getGlazeNSDia() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getGlazeNSDia());//雨凇南北直径
                        }
                        if (dayValueTab.getGlazeWEDia() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getGlazeWEDia());//雨凇东西直径mm
                        }
                        if (dayValueTab.getGlazeNSHight() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getGlazeNSHight());//雨凇南北厚度
                        }
                        if (dayValueTab.getGlazeWEHight() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getGlazeWEHight());//雨凇东西厚度mm
                        }
                        if (dayValueTab.getGlazeNSWeight() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getGlazeNSWeight());//雨凇东西重量g/m
                        }
                        if (dayValueTab.getGlazeWEWeight() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getGlazeWEWeight());//雨凇东西重量
                        }
                        if (dayValueTab.getRimeNSDia() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getRimeNSDia());//雾凇南北直径
                        }
                        if (dayValueTab.getRimeWEDia() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getRimeWEDia());//雾凇东西直径
                        }
                        if (dayValueTab.getRimeNSHight() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getRimeNSHight());//雾凇南北厚度
                        }
                        if (dayValueTab.getRimeWEHight() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getRimeWEHight());//雾凇东西厚度
                        }
                        if (dayValueTab.getRimeNSWeight() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getRimeNSWeight());//雾凇东西重量
                        }
                        if (dayValueTab.getRimeWEWeight() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getRimeWEWeight());//雾凇东西重量
                        }
                        if (dayValueTab.getV20010_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20010_701());// 日平均总云量 ( 单位：% )
                        }
                        if (dayValueTab.getV20051_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20051_701());// 日平均低云量 ( 单位：% )
                        }
                        if (dayValueTab.getV20059() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV20059());// 日最小水平能见度 ( 单位：米 )
                        }
                        ps.setString(ii++, dayValueTab.getV20059_052());// 日最小水平能见度出现时间 ( 格式：时分 )
                        if (dayValueTab.getV13302_01() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV13302_01());// 日小时最大降水量 ( 单位：毫米? )
                        }
                        if (dayValueTab.getV13302_01_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV13302_01_052());// 日小时最大降水量出现时间 ( 格式：时分 )
                        }
                        if (dayValueTab.getV13308() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV13308());// 20-08时雨量筒观测降水量 ( 单位：毫米 )
                        }
                        if (dayValueTab.getV13309() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV13309());// 08-20时雨量筒观测降水量 ( 单位：毫米 )
                        }
                        if (dayValueTab.getV13305() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV13305());// 20-20时降水量 ( 单位：毫米 )
                        }
                        if (dayValueTab.getV13306() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV13306());// 08-08时降水量 ( 单位：毫米 )
                        }
                        if (dayValueTab.getV13032() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV13032());// 日蒸发量（小型） ( 单位：毫米 )
                        }
                        if (dayValueTab.getV13033() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV13033());// 日蒸发量（大型） ( 单位：毫米 )
                        }
                        if (dayValueTab.getV13013() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV13013());// 积雪深度 ( 单位：厘米 )
                        }
                        if (dayValueTab.getV13330() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV13330());// 雪压 ( 单位：g/cm2 )
                        }
                        if (dayValueTab.getV20305() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20305());// 电线积冰-现象 ( 代码表 )
                        }
                        if (dayValueTab.getV20326_NS() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV20326_NS());// 电线积冰-南北方向直径 ( 单位：毫米 )
                        }
                        if (dayValueTab.getV20306_NS() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV20306_NS());// 电线积冰-南北方向厚度 ( 单位：毫米 )
                        }
                        if (dayValueTab.getV20307_NS() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV20307_NS());// 电线积冰-南北方向重量 ( 单位：克 )
                        }
                        if (dayValueTab.getV20326_WE() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV20326_WE());// 电线积冰-东西方向直径 ( 单位：毫米 )
                        }
                        if (dayValueTab.getV20306_WE() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV20306_WE());// 电线积冰-东西方向厚度 ( 单位：毫米 )
                        }
                        if (dayValueTab.getV20307_WE() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV20307_WE());// 电线积冰-东西方向重量 ( 单位：克 )
                        }
                        if (dayValueTab.getV12001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12001());// 电线积冰－温度 ( 单位：摄氏度 )
                        }
                        ps.setString(ii++, dayValueTab.getV11001());// 电线积冰－风向 ( 单位：度 )
                        if (dayValueTab.getV11002() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV11002());// 电线积冰－风速 ( 单位：米/秒 )
                        }
                        ps.setString(ii++, dayValueTab.getV11290_CHAR());// 逐小时2分钟平均风向 ( 格式：字符串? )
                        if (dayValueTab.getV11291_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV11291_701());// 日平均2分钟风速 ( 单位：米/秒 )
                        }
                        if (dayValueTab.getV11293_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV11293_701());// 日平均10分钟风速 ( 单位：米/秒 )
                        }
                        ps.setString(ii++, dayValueTab.getV11296());// 日最大风速的风向 ( 单位：度 )
                        if (dayValueTab.getV11042() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV11042());// 日最大风速 ( 单位：米/秒 )
                        }
                        ps.setString(ii++, dayValueTab.getV11042_052());// 日最大风速出现时间 ( 格式：时分 )
                        ps.setString(ii++, dayValueTab.getV11211());// 日极大风速的风向 ( 单位：度 )
                        if (dayValueTab.getV11046() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV11046());// 日极大风速 ( 单位：米/秒 )
                        }
                        ps.setString(ii++, dayValueTab.getV11046_052());// 日极大风速出现时间 ( 格式：时分 )
                        if (dayValueTab.getV12120_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12120_701());// 日平均地面温度 ( 单位：摄氏度 )
                        }
                        if (dayValueTab.getV12311() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12311());// 日最高地面温度 ( 单位：摄氏度 )
                        }
                        ps.setString(ii++, dayValueTab.getV12311_052());// 日最高地面温度出现时间  ( 格式：时分 )
                        if (dayValueTab.getV12121() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12121());// 日最低地面温度 ( 单位：摄氏度 )
                        }
                        ps.setString(ii++, dayValueTab.getV12121_052());// 日最低地面温度出现时间 ( 格式：时分 )
                        if (dayValueTab.getV12030_701_005() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12030_701_005());// 日平均5cm地温 ( 单位：摄氏度 )
                        }
                        if (dayValueTab.getV12030_701_010() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12030_701_010());// 日平均10cm地温 ( 单位：摄氏度 )
                        }
                        if (dayValueTab.getV12030_701_015() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12030_701_015());// 日平均15cm地温 ( 单位：摄氏度 )
                        }
                        if (dayValueTab.getV12030_701_020() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12030_701_020());// 日平均20cm地温 ( 单位：摄氏度 )
                        }
                        if (dayValueTab.getV12030_701_040() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12030_701_040());// 日平均40cm地温 ( 单位：摄氏度 )
                        }
                        if (dayValueTab.getV12030_701_080() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12030_701_080());// 日平均80cm地温 ( 单位：摄氏度 )
                        }
                        if (dayValueTab.getV12030_701_160() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12030_701_160());// 日平均160cm地温 ( 单位：摄氏度 )
                        }
                        if (dayValueTab.getV12030_701_320() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12030_701_320());// 日平均320cm地温 ( 单位：摄氏度 )
                        }
                        if (dayValueTab.getV20331() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV20331());//日最大冻土深度
                        }
                        if (dayValueTab.getV20330_01() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV20330_01());// 第一冻土层上界值 ( 单位：厘米 )
                        }
                        if (dayValueTab.getV20331_01() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV20331_01());// 第一冻土层下界值 ( 单位：厘米 )
                        }
                        if (dayValueTab.getV20330_02() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV20330_02());// 第二冻土层上界值 ( 单位：厘米 )
                        }
                        if (dayValueTab.getV20331_02() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV20331_02());// 第二冻土层下界值 ( 单位：厘米 )
                        }
                        if (dayValueTab.getV12003_avg() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12003_avg());//日平均露点温度
                        }
                        if (dayValueTab.getBalltemp_avg() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getBalltemp_avg());//日平均湿球温度
                        }
                        if (dayValueTab.getBalltemp_avg_24() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getBalltemp_avg_24());//日平均湿球温度(24小时)
                        }
                        if (dayValueTab.getV14032() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV14032());// 日总日照时数 ( 单位：小时 )
                        }
                        if (dayValueTab.getV14033() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV14033());//日照频率
                        }
                        ps.setString(ii++, dayValueTab.getV20411());// 日出时间 ( 格式：时分 )
                        ps.setString(ii++, dayValueTab.getV20412());// 日落时间 ( 格式：时分 )
                        if (dayValueTab.getSunTime() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getSunTime());//日出时长
                        }
                        if (dayValueTab.getV12314_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12314_701());// 日平均草面（雪面）温度 ( 单位：摄氏度 )
                        }
                        if (dayValueTab.getV12315() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12315());// 日草面（雪面）最高温度 ( 单位：摄氏度 )
                        }
                        ps.setString(ii++, dayValueTab.getV12315_052());// 日草面（雪面）最高温度出现时间 ( 格式：时分 )
                        if (dayValueTab.getV12316() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, dayValueTab.getV12316());// 日草面（雪面）最低温度 ( 单位：摄氏度 )
                        }
                        ps.setString(ii++, dayValueTab.getV12316_052());// 日草面（雪面）最低温度出现时间 ( 格式：时分 )
                        if (dayValueTab.getV20062() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20062());// 地面状态（0-31数字） ( 见代码表020062 )
                        }
                        if (dayValueTab.getV20302_060() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_060());// 雨 ( 代码表20302(下同) )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_060_052());// 雨出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_070() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_070());// 雪 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_070_052());// 雪出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_089() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_089());// 冰雹 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_089_052());// 冰雹出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_042() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_042());// 雾 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_042_052());// 雾出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_010() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_010());// 轻雾 ( 代码表 )
                        }
                        if (dayValueTab.getV20302_001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_001());// 露 ( 代码表 )
                        }
                        if (dayValueTab.getV20302_002() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_002());// 霜 ( 代码表 )
                        }
                        if (dayValueTab.getV20302_056() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_056());// 雨凇 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_056_052());// 雨凇出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_048() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_048());// 雾凇 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_048_052());// 雾凇出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_038() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_038());// 吹雪 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_038_052());// 吹雪出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_039() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_039());// 雪暴 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_039_052());// 雪暴出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_019() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_019());// 龙卷风 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_019_052());// 龙卷风出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_016() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_016());// 积雪 ( 代码表 )
                        }
                        if (dayValueTab.getV20302_003() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_003());// 结冰 ( 代码表 )
                        }
                        if (dayValueTab.getV20302_031() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_031());// 沙尘暴 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_031_052());// 沙尘暴出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_007() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_007());// 扬沙 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_007_052());// 扬沙出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_006() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_006());// 浮尘 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_006_052());// 浮尘出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_004() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_004());// 烟 ( 代码表 )
                        }
                        if (dayValueTab.getV20302_005() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_005());// 霾 ( 代码表 )
                        }
                        if (dayValueTab.getV20302_008() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_008());// 尘卷风 ( 代码表 )
                        }
                        if (dayValueTab.getV20302_076() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_076());// 冰针 ( 代码表 )
                        }
                        if (dayValueTab.getV20302_017() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_017());// 雷暴 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_017_052());// 雷暴出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_013() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_013());// 闪电 ( 代码表 )
                        }
                        if (dayValueTab.getV20302_014() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_014());// 极光 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_014_052());// 极光出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_015() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_015());// 大风 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_015_052());// 大风出现时间 ( 格式：字符串 )
                        if (dayValueTab.getV20302_018() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV20302_018());// 飑 ( 代码表 )
                        }
                        ps.setString(ii++, dayValueTab.getV20302_018_052());// 飑出现时间 ( 格式：字符串 )
                        ps.setString(ii++, dayValueTab.getV20303());// 天气现象摘要 ( 字符 )
                        ps.setString(ii++, dayValueTab.getV20304());// 天气现象记录 ( 字符 )
                        if (dayValueTab.getV14311() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, dayValueTab.getV14311());//日太阳总辐射(MJ/m2)
                        }
                        if (dayValueTab.getV14311_05() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, dayValueTab.getV14311_05());//日最大总辐射辐照度(W/m2)
                        }
                        if (dayValueTab.getV14021_05_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV14021_05_052());//日最大总辐射辐照度时间
                        }
                        if (dayValueTab.getV14312() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, dayValueTab.getV14312());//日净全辐射(MJ/m2)
                        }
                        if (dayValueTab.getV14312_05() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, dayValueTab.getV14312_05());//日最大净全辐射辐照度(W/m2)
                        }
                        if (dayValueTab.getV14312_05_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getV14312_05_052());//日最大净全辐射辐照度时间
                        }
                        if (dayValueTab.getV14314() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, dayValueTab.getV14314());//日散射辐射曝辐量(MJ/m2)
                        }
                        if (dayValueTab.getV14308() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, dayValueTab.getV14308());//日净全辐射曝辐量(MJ/m2)
                        }
                        if (dayValueTab.getV14313() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, dayValueTab.getV14313());//日直接辐射曝辐量(MJ/m2)
                        }
                        if (dayValueTab.getQ10004_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ10004_701());// 日平均本站气压质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ10301() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ10301());// 日最高本站气压质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ10301_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ10301_052());// 日最高本站气压出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ10302() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ10302());// 日最低本站气压质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ10302_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ10302_052());// 日最低本站气压出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ10051_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ10051_701());// 日平均海平面气压质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12001_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12001_701());// 日平均气温质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12011() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12011());// 日最高气温质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12011_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12011_052());// 日最高气温出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12012() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12012());// 日最低气温质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12012_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12012_052());// 日最低气温出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13004_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13004_701());// 日平均水汽压质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13003_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13003_701());// 日平均相对湿度质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13007() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13007());// 日最小相对湿度质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13007_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13007_052());// 日最小相对湿度出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20010_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20010_701());// 日平均总云量质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20051_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20051_701());// 日平均低云量质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20059() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20059());// 日最小水平能见度质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20059_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20059_052());// 日最小水平能见度出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13302_01() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13302_01());// 日小时最大降水量质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13302_01_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13302_01_052());// 日小时最大降水量出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13308() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13308());// 20-08时雨量筒观测降水量质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13309() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13309());// 08-20时雨量筒观测降水量质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13305() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13305());// 20-20时降水量质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13306() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13306());// 08-08时降水量质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13032() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13032());// 日蒸发量（小型）质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13033() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13033());// 日蒸发量（大型）质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13013() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13013());// 积雪深度质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ13330() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ13330());// 雪压质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20305() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20305());// 电线积冰-现象质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20326_NS() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20326_NS());// 电线积冰-南北方向直径质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20306_NS() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20306_NS());// 电线积冰-南北方向厚度质控码( 代码表 )
                        }
                        if (dayValueTab.getQ20307_NS() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20307_NS());// 电线积冰-南北方向重量质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20326_WE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20326_WE());// 电线积冰-东西方向直径质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20306_WE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20306_WE());// 电线积冰-东西方向厚度质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20307_WE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20307_WE());// 电线积冰-东西方向重量质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12001());// 电线积冰－温度质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ11001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ11001());// 电线积冰－风向质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ11002() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ11002());// 电线积冰－风速质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ11290_CHAR() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ11290_CHAR());// 日平均2分钟风向质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ11291_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ11291_701());// 日平均2分钟风速质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ11293_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ11293_701());// 日平均10分钟风速质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ11296() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ11296());// 日最大风速的风向质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ11042() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ11042());// 日最大风速质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ11042_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ11042_052());// 日最大风速出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ11211() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ11211());// 日极大风速的风向质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ11046() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ11046());// 日极大风速质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ11046_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ11046_052());// 日极大风速出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12120_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12120_701());// 日平均地面温度质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12311() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12311());// 日最高地面温度质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12311_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12311_052());// 日最高地面温度出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12121() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12121());// 日最低地面温度质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12121_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12121_052());// 日最低地面温度出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12030_701_005() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12030_701_005());// 日平均5cm地温质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12030_701_010() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12030_701_010());// 日平均10cm地温质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12030_701_015() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12030_701_015());// 日平均15cm地温质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12030_701_020() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12030_701_020());// 日平均20cm地温质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12030_701_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12030_701_040());// 日平均40cm地温质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12030_701_080() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12030_701_080());// 日平均80cm地温质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12030_701_160() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12030_701_160());// 日平均160cm地温质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12030_701_320() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12030_701_320());// 日平均320cm地温质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20330_01() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20330_01());// 第一冻土层上界值质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20331_01() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20331_01());// 第一冻土层下界值质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20330_02() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20330_02());// 第二冻土层上界值质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20331_02() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20331_02());// 第二冻土层下界值质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ14032() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ14032());// 日总日照时数质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20411() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20411());// 日出时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20412() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20412());// 日落时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12314_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12314_701());// 日平均草面（雪面）温度质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12315() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12315());// 日草面（雪面）最高温度质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12315_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12315_052());// 日草面（雪面）最高温度出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12316() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12316());// 日草面（雪面）最低温度质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ12316_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ12316_052());// 日草面（雪面）最低温度出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20062() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20062());// 地面状态质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_060() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_060());// 雨质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_060_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_060_052());// 雨出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_070() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_070());// 雪质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_070_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_070_052());// 雪出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_089() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_089());// 冰雹质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_089_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_089_052());// 冰雹出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_042() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_042());// 雾质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_042_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_042_052());// 雾出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_010() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_010());// 轻雾质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_001());// 露质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_002() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_002());// 霜质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_056() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_056());// 雨凇质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_056_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_056_052());// 雨凇出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_048() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_048());// 雾凇质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_048_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_048_052());// 雾凇出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_038() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_038());// 吹雪质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_038_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_038_052());// 吹雪出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_039() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_039());// 雪暴质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_039_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_039_052());// 雪暴出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_019() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_019());// 龙卷风质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_019_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_019_052());// 龙卷风出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_016() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_016());// 积雪质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_003() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_003());// 结冰质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_031() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_031());// 沙尘暴质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_031_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_031_052());// 沙尘暴出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_007() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_007());// 扬沙质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_007_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_007_052());// 扬沙出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_006() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_006());// 浮尘质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_006_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_006_052());// 浮尘出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_004() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_004());// 烟质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_005() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_005());// 霾质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_008() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_008());// 尘卷风质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_076() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_076());// 冰针质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_017() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_017());// 雷暴质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_017_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_017_052());//雷暴出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_013() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_013());// 闪电质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_014() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_014());// 极光质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_014_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_014_052());// 极光出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_015() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_015());// 大风质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_015_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_015_052());// 大风出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_018() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_018());// 飑质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20302_018_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20302_018_052());// 飑出现时间质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20303() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20303());// 天气现象摘要质控码 ( 代码表 )
                        }
                        if (dayValueTab.getQ20304() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, dayValueTab.getQ20304());// 天气现象记录质控码 ( 代码表 )
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("列号：" + ii + "有误");
                    }
                    ps.addBatch();
                    if (index % 1000 == 0) {
                        ps.executeBatch();
                        connection.commit();
                        ps.clearBatch();
                    }
                }
                if (index % 1000 != 0) {
                    ps.executeBatch();
                    connection.commit();
                    ps.clearBatch();
                }
            }
            if (monthList != null && !monthList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("month_value_table_name") + "(" +
                        "D_RETAIN_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDate_TIME,D_DateTIME,V01301,V01300,V05001,V06001,V07001,V07031,V02301,V_ACODE,V04001,V04002,V10004_701,V10301,V10301_040,V10301_060_CHAR,V10302,V10302_040,V10302_060_CHAR,V10301_avg,V10302_avg,V10051_701,V12001_701,V12011_701,V12012_701,V12011,V12011_040," +
                        "V12011_060_CHAR,V12012,V12012_040,V12012_060_CHAR,V12303_701,V12304,V12304_040,V12304_060_CHAR,V12305,V12305_040,V12305_060_CHAR,V12605_30,V12605_35,V12605_40,V12607_02,V12603,V12606_02,V12606_05,V12606_10,V12606_15,V12606_30,V12606_40,V12610_26,V12611_18,V13003_701,V13007,V13007_040,V13007_060_CHAR,V20010_701,V20051_701,V20501_02," +
                        "V20500_08,V20503_02,V20502_08,V13305,V13306,V13052,V13052_040,V13052_060_CHAR,V13353,V13355_001,V13355_005,V13355_010,V13355_025,V13355_050,V13355_100,V13355_150,V13355_250,V20430,V13380,V20431,V20432,V20433,V13381,V20434,V20435,V13302_01,V13302_01_040,V13302_01_060_CHAR,V04330_089,V04330_007,V04330_006," +
                        "V04330_005,V04330_008,V04330_015,V04330_031,V04330_042,V04330_017,V04330_002,V04330_070,V04330_016,V20305_540,V20309_010,V20309_001,V20309_005,V20309_008,V20309_100,V20309_150,V20309_300,V20309_011,V13032,V13033,V13334,V13334_040,V13334_060_CHAR,V13330,V13330_040,V13330_060,V13356_001,V13356_005,V13356_010,V13356_020,V13356_030," +
                        "V20326_NS,V20306_NS,V20307_NS,V20326_WE,V20306_WE,V20307_WE,V20440_040_NS,V20440_060_NS_CHAR,V20440_040_WE,V20440_060_WE_CHAR,V11291_701,V11042,V11296_CHAR,V11042_040,V11042_060_CHAR,V11042_05,V11042_10,V11042_12,V11042_15,V11042_17,V11046,V11211,V11046_040,V11046_060_CHAR,V11350_NNE,V11350_NE,V11350_ENE,V11350_E,V11350_ESE,V11350_SE,V11350_SSE," +
                        "V11350_S,V11350_SSW,V11350_SW,V11350_WSW,V11350_W,V11350_WNW,V11350_NW,V11350_NNW,V11350_N,V11350_C,V11314_CHAR,V11314_061,V11315_CHAR,V11315_061,V11351_NNE,V11351_NE,V11351_ENE,V11351_E,V11351_ESE,V11351_SE,V11351_SSE,V11351_S,V11351_SSW,V11351_SW,V11351_WSW,V11351_W,V11351_WNW,V11351_NW,V11351_NNW,V11351_N,V11042_NNE," +
                        "V11042_NE,V11042_ENE,V11042_E,V11042_ESE,V11042_SE,V11042_SSE,V11042_S,V11042_SSW,V11042_SW,V11042_WSW,V11042_W,V11042_WNW,V11042_NW,V11042_NNW,V11042_N,V12120_701,V12311_701,V12121_701,V12620,V12311,V12311_040,V12311_060_CHAR,V12121,V12121_040,V12121_060_CHAR,V12030_701_005,V12030_701_010,V12030_701_015,V12030_701_020,V12030_701_040,V12030_701_080," +
                        "V12030_701_160,V12030_701_320,balltemp_avg,V13004_701,V13004_MAX,V13004_MAX_D,V13004_MIN,V13004_MIN_D,V12314_701,V12315_701,V12316_701,V12315,V12315_040,V12315_060_CHAR,V12316,V12316_040,V12316_060_CHAR,V20334,V20334_040,V20334_060_CHAR,V14032,sunTime,V14033,V20302_171,V20302_172,v14311,v14311_05,v14021_05_052,v14312,v14312_05,v14312_05_052," +
                        "v14314_avg,v14314,v14314_05,v14322_avg,v14322" +
                        ")values(" +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                int index = 0;
                for (MonthValueTab monthValueTab : monthList) {
                    int ii = 1;
                    index += 1;
                    try {

                        ps.setString(ii++, monthValueTab.getD_RECORD_ID());//记录标识
                        ps.setString(ii++, cts_code);//资料标识
                        ps.setString(ii++, monthValueTab.getD_IYMDHM());//入库时间
                        ps.setString(ii++, monthValueTab.getD_RYMDHM());//收到时间
                        ps.setString(ii++, monthValueTab.getD_UPDate_TIME());//更新时间
                        ps.setString(ii++, monthValueTab.getD_DateTIME());//资料时间
                        ps.setString(ii++, monthValueTab.getV01301());//区站号字符
                        ps.setString(ii++, monthValueTab.getV01300());//区站号数字
                        if (monthValueTab.getV05001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV05001());//纬度
                        }
                        if (monthValueTab.getV06001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV06001());//经度
                        }
                        if (monthValueTab.getV07001() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV07001());//测站高度
                        }
                        if (monthValueTab.getV07031() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV07031());//气压传感器海拔高度
                        }
                        if (monthValueTab.getV02301() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV02301());//台站级别
                        }
                        if (monthValueTab.getV_ACODE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV_ACODE());//中国行政区划代码
                        }
                        if (monthValueTab.getV04001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04001());//年
                        }
                        if (monthValueTab.getV04002() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04002());//月
                        }
                        if (monthValueTab.getV10004_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV10004_701());//月平均本站气压
                        }
                        if (monthValueTab.getV10301() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV10301());//月极端最高本站气压
                        }
                        if (monthValueTab.getV10301_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV10301_040());//月极端最高本站气压出现日数
                        }
                        if (monthValueTab.getV10301_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV10301_060_CHAR());//月极端最高本站气压出现日
                        }
                        if (monthValueTab.getV10302() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV10302());//月极端最低本站气压
                        }
                        if (monthValueTab.getV10302_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV10302_040());//月极端最低本站气压出现日数
                        }
                        if (monthValueTab.getV10302_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV10302_060_CHAR());//月极端最低本站气压出现日
                        }
                        if (monthValueTab.getV10301_avg() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV10301_avg());//月平均最高本站气压
                        }
                        if (monthValueTab.getV10302_avg() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV10302_avg());//月平均最低本站气压
                        }
                        if (monthValueTab.getV10051_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV10051_701());//月平均海平面气压
                        }
                        if (monthValueTab.getV12001_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12001_701());//月平均气温
                        }
                        if (monthValueTab.getV12011_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12011_701());//月平均日最高气温
                        }
                        if (monthValueTab.getV12012_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12012_701());//月平均日最低气温
                        }
                        if (monthValueTab.getV12011() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12011());//本月极端最高气温
                        }
                        if (monthValueTab.getV12011_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12011_040());//本月极端最高气温出现日数
                        }
                        if (monthValueTab.getV12011_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV12011_060_CHAR());//极端最高气温出现日
                        }
                        if (monthValueTab.getV12012() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12012());//本月极端最低气温
                        }
                        if (monthValueTab.getV12012_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12012_040());//本月极端最低气温出现日数
                        }
                        if (monthValueTab.getV12012_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV12012_060_CHAR());//极端最低气温出现日
                        }
                        if (monthValueTab.getV12303_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12303_701());//月平均气温日较差
                        }
                        if (monthValueTab.getV12304() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12304());//月最大气温日较差
                        }
                        if (monthValueTab.getV12304_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12304_040());//月最大气温日较差出现日数
                        }
                        if (monthValueTab.getV12304_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV12304_060_CHAR());//月最大气温日较差出现日
                        }
                        if (monthValueTab.getV12305() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12305());//月最小气温日较差
                        }
                        if (monthValueTab.getV12305_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12305_040());//月最小气温日较差出现日数
                        }
                        if (monthValueTab.getV12305_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV12305_060_CHAR());//月最小气温日较差出现日
                        }
                        if (monthValueTab.getV12605_30() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12605_30());//日最高气温≥30℃日数
                        }
                        if (monthValueTab.getV12605_35() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12605_35());//日最高气温≥35℃日数
                        }
                        if (monthValueTab.getV12605_40() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12605_40());//日最高气温≥40℃日数
                        }
                        if (monthValueTab.getV12607_02() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12607_02());//日最低气温＜2℃日数
                        }
                        if (monthValueTab.getV12603() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12603());//日最低气温＜0℃日数
                        }
                        if (monthValueTab.getV12606_02() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12606_02());//日最低气温＜-2℃日数
                        }
                        if (monthValueTab.getV12606_05() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12606_05());//日最低气温＜-5℃日数 ( 单位：日 )
                        }
                        if (monthValueTab.getV12606_10() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12606_10());//日最低气温＜-10℃日数 ( 单位：日 )
                        }
                        if (monthValueTab.getV12606_15() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12606_15());//日最低气温＜-15℃日数
                        }
                        if (monthValueTab.getV12606_30() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12606_30());//日最低气温＜-30℃日数
                        }
                        if (monthValueTab.getV12606_40() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12606_40());//日最低气温＜-40℃日数
                        }
                        if (monthValueTab.getV12610_26() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12610_26());//冷度日数（日平均气温＞26.0℃）
                        }
                        if (monthValueTab.getV12611_18() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12611_18());//暖度日数（日平均气温＜18.0℃）
                        }
                        if (monthValueTab.getV13003_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13003_701());//月平均相对湿度
                        }
                        if (monthValueTab.getV13007() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13007());//月最小相对湿度
                        }
                        if (monthValueTab.getV13007_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13007_040());//月最小相对湿度出现日数
                        }
                        if (monthValueTab.getV13007_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV13007_060_CHAR());//月最小相对湿度出现日
                        }
                        if (monthValueTab.getV20010_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20010_701());//月平均总云量
                        }
                        if (monthValueTab.getV20051_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20051_701());//月平均低云量
                        }
                        if (monthValueTab.getV20501_02() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20501_02());//日平均总云量< 2.0日数
                        }
                        if (monthValueTab.getV20500_08() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20500_08());//日平均总云量> 8.0日数
                        }
                        if (monthValueTab.getV20503_02() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20503_02());//日平均低云量< 2.0日数
                        }
                        if (monthValueTab.getV20502_08() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20502_08());//日平均低云量> 8.0日数
                        }
                        if (monthValueTab.getV13305() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV13305());//20-20时月总降水量
                        }
                        if (monthValueTab.getV13306() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV13306());//08-08时月总降水量
                        }
                        if (monthValueTab.getV13052() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV13052());//月最大日降水量
                        }
                        if (monthValueTab.getV13052_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13052_040());//月最大日降水量出现日数
                        }
                        if (monthValueTab.getV13052_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV13052_060_CHAR());//月最大日降水量出现日
                        }
                        if (monthValueTab.getV13353() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13353());//日总降水量≥0.1mm日数
                        }
                        if (monthValueTab.getV13355_001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13355_001());//日总降水量≥1mm日数
                        }
                        if (monthValueTab.getV13355_005() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13355_005());//日总降水量≥5mm日数
                        }
                        if (monthValueTab.getV13355_010() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13355_010());//日总降水量≥10mm日数
                        }
                        if (monthValueTab.getV13355_025() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13355_025());//日总降水量≥25mm日数
                        }
                        if (monthValueTab.getV13355_050() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13355_050());//日总降水量≥50mm日数
                        }
                        if (monthValueTab.getV13355_100() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13355_100());//日总降水量≥100mm日数
                        }
                        if (monthValueTab.getV13355_150() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13355_150());//日总降水量≥150mm日数
                        }
                        if (monthValueTab.getV13355_250() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13355_250());//日降水量≥250mm日数 ( 单位：日 )
                        }
                        if (monthValueTab.getV20430() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20430());//月最长连续降水日数
                        }
                        if (monthValueTab.getV13380() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13380());//月最长连续降水量
                        }
                        if (monthValueTab.getV20431() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20431());//月最长连续降水止日
                        }
                        if (monthValueTab.getV20432() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20432());//月最长连续无降水日数
                        }
                        if (monthValueTab.getV20433() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20433());//月最长连续无降水止日
                        }
                        if (monthValueTab.getV13381() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13381());//月最大连续降水量
                        }
                        if (monthValueTab.getV20434() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20434());//月最大连续降水日数
                        }
                        if (monthValueTab.getV20435() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20435());//月最大连续降水止日
                        }
                        if (monthValueTab.getV13302_01() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13302_01());//1小时最大降水量
                        }
                        if (monthValueTab.getV13302_01_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13302_01_040());//1小时最大降水量出现日数
                        }
                        if (monthValueTab.getV13302_01_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV13302_01_060_CHAR());//1小时最大降水量出现日
                        }
                        if (monthValueTab.getV04330_089() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04330_089());//冰雹日数
                        }
                        if (monthValueTab.getV04330_007() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04330_007());//扬沙日数
                        }
                        if (monthValueTab.getV04330_006() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04330_006());//浮尘日数
                        }
                        if (monthValueTab.getV04330_005() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04330_005());//霾日数
                        }
                        if (monthValueTab.getV04330_008() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04330_008());//龙卷风日数
                        }
                        if (monthValueTab.getV04330_015() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04330_015());//大风日数
                        }
                        if (monthValueTab.getV04330_031() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04330_031());//沙尘暴日数
                        }
                        if (monthValueTab.getV04330_042() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04330_042());//雾日数
                        }
                        if (monthValueTab.getV04330_017() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04330_017());//雷暴日数
                        }
                        if (monthValueTab.getV04330_002() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04330_002());//霜日数
                        }
                        if (monthValueTab.getV04330_070() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04330_070());//降雪日数
                        }
                        if (monthValueTab.getV04330_016() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV04330_016());//积雪日数
                        }
                        if (monthValueTab.getV20305_540() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20305_540());//电线积冰（雨凇+雾凇）日数
                        }
                        if (monthValueTab.getV20309_010() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20309_010());//能见度≤10km出现频率
                        }
                        if (monthValueTab.getV20309_001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20309_001());//能见度≤1km出现频率
                        }
                        if (monthValueTab.getV20309_005() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20309_005());//能见度≤5km出现频率
                        }
                        if (monthValueTab.getV20309_008() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20309_008());//月能见度<800米出现次数
                        }
                        if (monthValueTab.getV20309_100() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20309_100());//能见度≤1km出现次数
                        }
                        if (monthValueTab.getV20309_150() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20309_150());//能见度≤1.5km出现次数
                        }
                        if (monthValueTab.getV20309_300() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20309_300());//能见度≤3km出现次数
                        }
                        if (monthValueTab.getV20309_011() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20309_011());//能见度>1km出现次数
                        }
                        if (monthValueTab.getV13032() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV13032());//月总蒸发量小型
                        }
                        if (monthValueTab.getV13033() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV13033());//月总蒸发量大型
                        }
                        if (monthValueTab.getV13334() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV13334());//最大积雪深度
                        }
                        if (monthValueTab.getV13334_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13334_040());//最大积雪深度日数
                        }
                        if (monthValueTab.getV13334_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV13334_060_CHAR());//最大积雪深度出现日
                        }
                        if (monthValueTab.getV13330() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV13330());//最大雪压
                        }
                        if (monthValueTab.getV13330_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13330_040());//最大雪压出现日数
                        }
                        if (monthValueTab.getV13330_060() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV13330_060());//最大雪压出现日
                        }
                        if (monthValueTab.getV13356_001() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13356_001());//积雪深度≥1cm日数
                        }
                        if (monthValueTab.getV13356_005() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13356_005());//积雪深度≥5cm日数
                        }
                        if (monthValueTab.getV13356_010() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13356_010());//积雪深度≥10cm日数
                        }
                        if (monthValueTab.getV13356_020() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13356_020());//积雪深度≥20cm日数
                        }
                        if (monthValueTab.getV13356_030() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13356_030());//积雪深度≥30cm日数
                        }
                        if (monthValueTab.getV20326_NS() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV20326_NS());//电线积冰-南北方向最大重量的相应直径( 单位：毫米 )
                        }
                        if (monthValueTab.getV20306_NS() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV20306_NS());//电线积冰-南北方向最大重量的相应厚度( 单位：毫米 )
                        }
                        if (monthValueTab.getV20307_NS() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV20307_NS());//电线积冰-南北方向最大重量 ( 单位：克 )
                        }
                        if (monthValueTab.getV20326_WE() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV20326_WE());//电线积冰-东西方向最大重量的相应直径( 单位：毫米 )
                        }
                        if (monthValueTab.getV20306_WE() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV20306_WE());//电线积冰-东西方向最大重量的相应厚度 ( 单位：毫米 )
                        }
                        if (monthValueTab.getV20307_WE() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV20307_WE());//电线积冰-东西方向最大重量 ( 单位：克 )
                        }
                        if (monthValueTab.getV20440_040_NS() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20440_040_NS());//电线积冰-南北最大重量出现日数
                        }
                        if (monthValueTab.getV20440_060_NS_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV20440_060_NS_CHAR());//电线积冰-南北最大重量出现日
                        }
                        if (monthValueTab.getV20440_040_WE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20440_040_WE());//电线积冰-东西最大重量出现日数
                        }
                        if (monthValueTab.getV20440_060_WE_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV20440_060_WE_CHAR());//电线积冰-东西最大重量出现日
                        }
                        if (monthValueTab.getV11291_701() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11291_701());//月平均风速（2分钟）
                        }
                        if (monthValueTab.getV11042() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV11042());//月最大风速
                        }
                        if (monthValueTab.getV11296_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV11296_CHAR());//最大风速的风向
                        }
                        if (monthValueTab.getV11042_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_040());//最大风速出现日数
                        }
                        if (monthValueTab.getV11042_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV11042_060_CHAR());//月最大风速之出现日
                        }
                        if (monthValueTab.getV11042_05() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_05());//日最大风速≥5.0m/s日数
                        }
                        if (monthValueTab.getV11042_10() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_10());//日最大风速≥10.0m/s日数
                        }
                        if (monthValueTab.getV11042_12() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_12());//日最大风速≥12.0m/s日数
                        }
                        if (monthValueTab.getV11042_15() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_15());//日最大风速≥15.0m/s日数
                        }
                        if (monthValueTab.getV11042_17() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_17());//日最大风速≥17.0m/s日数
                        }
                        if (monthValueTab.getV11046() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11046());//月极大风速
                        }
                        if (monthValueTab.getV11211() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV11211());//月极大风速之风向
                        }
                        if (monthValueTab.getV11046_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11046_040());//月极大风速出现日数
                        }
                        if (monthValueTab.getV11046_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV11046_060_CHAR());//月极大风速之出现日
                        }
                        if (monthValueTab.getV11350_NNE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_NNE());//NNE风向出现频率
                        }
                        if (monthValueTab.getV11350_NE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_NE());//NE风向出现频率
                        }
                        if (monthValueTab.getV11350_ENE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_ENE());//ENE风向出现频率
                        }
                        if (monthValueTab.getV11350_E() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_E());//E风向出现频率
                        }
                        if (monthValueTab.getV11350_ESE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_ESE());//ESE风向出现频率
                        }
                        if (monthValueTab.getV11350_SE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_SE());//SE风向出现频率
                        }
                        if (monthValueTab.getV11350_SSE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_SSE());//SSE风向出现频率
                        }
                        if (monthValueTab.getV11350_S() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_S());//S风向出现频率
                        }
                        if (monthValueTab.getV11350_SSW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_SSW());//SSW风向出现频率
                        }
                        if (monthValueTab.getV11350_SW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_SW());//SW风向出现频率
                        }
                        if (monthValueTab.getV11350_WSW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_WSW());//WSW风向出现频率
                        }
                        if (monthValueTab.getV11350_W() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_W());//W风向出现频率
                        }
                        if (monthValueTab.getV11350_WNW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_WNW());//WNW风向出现频率
                        }
                        if (monthValueTab.getV11350_NW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_NW());//NW风向出现频率
                        }
                        if (monthValueTab.getV11350_NNW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_NNW());//NNW风向出现频率
                        }
                        if (monthValueTab.getV11350_N() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_N());//N风向出现频率
                        }
                        if (monthValueTab.getV11350_C() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11350_C());//C风向（静风）出现频率
                        }
                        if (monthValueTab.getV11314_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV11314_CHAR());//月最多风向
                        }
                        if (monthValueTab.getV11314_061() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11314_061());//月最多风向出现频率
                        }
                        if (monthValueTab.getV11315_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV11315_CHAR());//月次多风向
                        }
                        if (monthValueTab.getV11315_061() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11315_061());//月次多风向出现频率
                        }
                        if (monthValueTab.getV11351_NNE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_NNE());//NNE风的平均风速
                        }
                        if (monthValueTab.getV11351_NE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_NE());//NE风的平均风速
                        }
                        if (monthValueTab.getV11351_ENE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_ENE());//ENE的平均风速
                        }
                        if (monthValueTab.getV11351_E() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_E());//E风的平均风速
                        }
                        if (monthValueTab.getV11351_ESE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_ESE());//ESE风的平均风速
                        }
                        if (monthValueTab.getV11351_SE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_SE());//SE风的平均风速
                        }
                        if (monthValueTab.getV11351_SSE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_SSE());//SSE风的平均风速
                        }
                        if (monthValueTab.getV11351_S() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_S());//S风的平均风速
                        }
                        if (monthValueTab.getV11351_SSW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_SSW());//SSW风的平均风速
                        }
                        if (monthValueTab.getV11351_SW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_SW());//SW风的平均风速
                        }
                        if (monthValueTab.getV11351_WSW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_WSW());//WSW风的平均风速
                        }
                        if (monthValueTab.getV11351_W() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_W());//W风的平均风速
                        }
                        if (monthValueTab.getV11351_WNW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_WNW());//WNW风的平均风速
                        }
                        if (monthValueTab.getV11351_NW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_NW());//NW风的平均风速
                        }
                        if (monthValueTab.getV11351_NNW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_NNW());//NNW风的平均风速
                        }
                        if (monthValueTab.getV11351_N() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11351_N());//N风的平均风速
                        }
                        if (monthValueTab.getV11042_NNE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_NNE());//NNE风的最大风速
                        }
                        if (monthValueTab.getV11042_NE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_NE());//NE风的最大风速
                        }
                        if (monthValueTab.getV11042_ENE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_ENE());//ENE的最大风速
                        }
                        if (monthValueTab.getV11042_E() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_E());//E风的最大风速
                        }
                        if (monthValueTab.getV11042_ESE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_ESE());//ESE风的最大风速
                        }
                        if (monthValueTab.getV11042_SE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_SE());//SE风的最大风速
                        }
                        if (monthValueTab.getV11042_SSE() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_SSE());//SSE风的最大风速
                        }
                        if (monthValueTab.getV11042_S() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_S());//S风的最大风速
                        }
                        if (monthValueTab.getV11042_SSW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_SSW());//SSW风的最大风速
                        }
                        if (monthValueTab.getV11042_SW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_SW());//SW风的最大风速
                        }
                        if (monthValueTab.getV11042_WSW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_WSW());//WSW风的最大风速
                        }
                        if (monthValueTab.getV11042_W() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_W());//W风的最大风速
                        }
                        if (monthValueTab.getV11042_WNW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_WNW());//WNW风的最大风速
                        }
                        if (monthValueTab.getV11042_NW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_NW());//NW风的最大风速
                        }
                        if (monthValueTab.getV11042_NNW() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_NNW());//NNW风的最大风速
                        }
                        if (monthValueTab.getV11042_N() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV11042_N());//N风的最大风速
                        }
                        if (monthValueTab.getV12120_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12120_701());//月平均地面温度
                        }
                        if (monthValueTab.getV12311_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12311_701());//月平均最高地面温度
                        }
                        if (monthValueTab.getV12121_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12121_701());//月平均最低地面温度
                        }
                        if (monthValueTab.getV12620() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12620());//月内日最低地面温度≤0.0℃日数
                        }
                        if (monthValueTab.getV12311() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12311());//月极端最高地面温度
                        }
                        if (monthValueTab.getV12311_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12311_040());//月极端最高地面温度出现日数
                        }
                        if (monthValueTab.getV12311_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV12311_060_CHAR());//月极端最高地面温度出现日
                        }
                        if (monthValueTab.getV12121() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12121());//月极端最低地面温度
                        }
                        if (monthValueTab.getV12121_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12121_040());//月极端最低地面温度出现日数
                        }
                        if (monthValueTab.getV12121_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV12121_060_CHAR());//月极端最低地面温度出现日
                        }
                        if (monthValueTab.getV12030_701_005() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12030_701_005());//月平均5cm地温
                        }
                        if (monthValueTab.getV12030_701_010() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12030_701_010());//月平均10cm地温
                        }
                        if (monthValueTab.getV12030_701_015() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12030_701_015());//月平均15cm地温
                        }
                        if (monthValueTab.getV12030_701_020() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12030_701_020());//月平均20cm地温
                        }
                        if (monthValueTab.getV12030_701_040() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12030_701_040());//月平均40cm地温
                        }
                        if (monthValueTab.getV12030_701_080() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12030_701_080());//月平均80cm地温
                        }
                        if (monthValueTab.getV12030_701_160() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12030_701_160());//月平均160cm地温
                        }
                        if (monthValueTab.getV12030_701_320() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12030_701_320());//月平均320cm地温
                        }
                        if (monthValueTab.getBalltemp_avg() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getBalltemp_avg());//月平均湿球温度
                        }
                        if (monthValueTab.getV13004_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV13004_701());//月平均水汽压
                        }
                        if (monthValueTab.getV13004_MAX() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV13004_MAX());//月最大水汽压
                        }
                        if (monthValueTab.getV13004_MAX_D() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13004_MAX_D());//月最大水汽压出现日期
                        }
                        if (monthValueTab.getV13004_MIN() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV13004_MIN());//月最小水气压
                        }
                        if (monthValueTab.getV13004_MIN_D() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV13004_MIN_D());// 月最小水汽压出现日期
                        }
                        if (monthValueTab.getV12314_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12314_701());//平均草面（雪面）温度
                        }
                        if (monthValueTab.getV12315_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12315_701());//平均最高草面（雪面）温度
                        }
                        if (monthValueTab.getV12316_701() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12316_701());//平均最低草面（雪面）温度
                        }
                        if (monthValueTab.getV12315() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12315());//极端最高草面（雪面）温度
                        }
                        if (monthValueTab.getV12315_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12315_040());//极端最高草面（雪面）温度出现日数
                        }
                        if (monthValueTab.getV12315_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV12315_060_CHAR());//极端最高草面（雪面）温度出现日
                        }
                        if (monthValueTab.getV12316() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV12316());//极端最低草面（雪面）温度
                        }
                        if (monthValueTab.getV12316_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV12316_040());//极端最低草面（雪面）温度出现日数
                        }
                        if (monthValueTab.getV12316_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV12316_060_CHAR());//极端最低草面（雪面）温度出现日
                        }
                        if (monthValueTab.getV20334() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV20334());//最大冻土深度
                        }
                        if (monthValueTab.getV20334_040() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20334_040());//最大冻土深度出现日数
                        }
                        if (monthValueTab.getV20334_060_CHAR() == null) {
                            ps.setString(ii++, "999999");
                        } else {
                            ps.setString(ii++, monthValueTab.getV20334_060_CHAR());//最大冻土深度出现日
                        }
                        if (monthValueTab.getV14032() == null) {
                            ps.setFloat(ii++, 999999);
                        } else {
                            ps.setFloat(ii++, monthValueTab.getV14032());//月日照总时数
                        }
                        if (monthValueTab.getSunTime() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getSunTime());//月日出总时长
                        }
                        if (monthValueTab.getV14033() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV14033());//月日照百分率
                        }
                        if (monthValueTab.getV20302_171() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20302_171());//日照百分率≥60%日数
                        }
                        if (monthValueTab.getV20302_172() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV20302_172());//日照百分率≤20%日数
                        }
                        if (monthValueTab.getV14311() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, monthValueTab.getV14311());//月太阳总辐射(MJ/m2)
                        }
                        if (monthValueTab.getV14311_05() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, monthValueTab.getV14311_05());// 月最大日总辐射辐照度(W/m2)
                        }
                        if (monthValueTab.getV14021_05_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV14021_05_052());//月最大日总辐射辐照度时间
                        }
                        if (monthValueTab.getV14312() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, monthValueTab.getV14312());//月净全辐射(MJ/m2)
                        }
                        if (monthValueTab.getV14312_05() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, monthValueTab.getV14312_05());//月最大日净全辐射辐照度(W/m2)
                        }
                        if (monthValueTab.getV14312_05_052() == null) {
                            ps.setInt(ii++, 999999);
                        } else {
                            ps.setInt(ii++, monthValueTab.getV14312_05_052());//月最大日净全辐射辐照度时间
                        }
                        if (monthValueTab.getV14314_avg() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, monthValueTab.getV14314_avg());// 月日散射辐射曝辐量平均
                        }
                        if (monthValueTab.getV14314() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, monthValueTab.getV14314());// 月日散射辐射曝辐量合计
                        }
                        if (monthValueTab.getV14314_05() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, monthValueTab.getV14314_05());//最大日散射辐射辐照度
                        }
                        if (monthValueTab.getV14322_avg() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, monthValueTab.getV14322_avg());//月日直接辐射曝辐量平均
                        }
                        if (monthValueTab.getV14322() == null) {
                            ps.setBigDecimal(ii++, new BigDecimal("999999"));
                        } else {
                            ps.setBigDecimal(ii++, monthValueTab.getV14322());//月日直接辐射曝辐量合计
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("列号：" + ii + "有误");
                    }
                    ps.addBatch();
                    if (index % 1000 == 0) {
                        ps.executeBatch();
                        connection.commit();
                        ps.clearBatch();
                    }
                }
                if (index % 1000 != 0) {
                    ps.executeBatch();
                    connection.commit();
                    ps.clearBatch();
                }
            }
            try {
                ps.executeBatch();
                connection.commit();
                loggerBuffer.append("\n Batch submission successful：" + filename);

            } catch (Exception e) {
                e.printStackTrace();
                connection.rollback();
                loggerBuffer.append("\n Batch submission error" + filename);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            loggerBuffer.append("\n Database connection exception: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if(connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
