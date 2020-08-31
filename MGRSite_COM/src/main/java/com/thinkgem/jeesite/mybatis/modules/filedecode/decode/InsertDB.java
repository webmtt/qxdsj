package com.thinkgem.jeesite.mybatis.modules.filedecode.decode;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.mybatis.common.utils.ConnectionPoolFactory;
import com.thinkgem.jeesite.mybatis.common.utils.DateUtils;
import com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean.*;
import com.thinkgem.jeesite.mybatis.modules.filedecode.util.LoggableStatement;
import com.thinkgem.jeesite.mybatis.modules.report.entity.ReportLogInfo;
import com.thinkgem.jeesite.mybatis.modules.report.service.ReportLogServices;

import org.apache.lucene.util.RamUsageEstimator;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 文件数据入库
 * @author yang.kq
 * @version 1.0
 * @date 2020/7/21 16:13
 */

public class InsertDB {
    /**
     * 删除数据库中数据
     * @param sf
     */
    public void deleteDb(FirstData sf, List<MeadowValueTab> meadowList, MonthValueTab mvt, List<DayValueTab> dayList, List<HourValueTab> hourList,
                         List<SunLightValueTab> sunLightList){
        Connection connection = null;
        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
            String dayValueTable = Global.getConfig("day_value_table_name");
            String hourValueTable = Global.getConfig("hour_value_table_name");
            String sunlightValueTable = Global.getConfig("sunlight_value_table_name");
            String monthValueTable = Global.getConfig("month_value_table_name");
            String meadowValueTable = Global.getConfig("meadow_value_table_name");
            String station_code=sf.getAreacode();
            String year=sf.getYear();
            String month=sf.getMonth();
            String meadowSql=null;
            String mvtSql=null;
            String daySql=null;
            String hourSql=null;
            String sunLightSql=null;
            connection.setAutoCommit(false);
            Statement stm = connection.createStatement();
            if(meadowList.size()>0){
                meadowSql="delete from "+meadowValueTable+" where V01300="+station_code+" and V04001="+year+" and V04002="+month;
                stm.execute(meadowSql);
            }
            if(mvt!=null){
                mvtSql ="delete from "+monthValueTable+" where V01300="+station_code+" and V04001="+year+" and V04002="+month;
                stm.execute(mvtSql);
            }
            if(dayList.size()>0){
                daySql ="delete from "+dayValueTable+" where V01300="+station_code+" and V04001="+year+" and V04002="+month;
                stm.execute(daySql);
            }
            if(hourList.size()>0){
                hourSql ="delete from "+hourValueTable+" where V01300="+station_code+" and V04001="+year+" and V04002="+month;
                stm.execute(hourSql);
            }
            if(sunLightList.size()>0){
                sunLightSql ="delete from "+sunlightValueTable+" where V01300="+station_code+" and V04001="+year+" and V04002="+month;
                stm.execute(sunLightSql);
            }
            connection.commit();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(connection!=null){
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * A0文件数据处理
     *
     *
     * @param recv_time
     * @param filename
     * @return
     */
    public  void processDaySuccessReport(FirstData sf,List<RadiDigChnMulTab> rlist, List<MeadowValueTab> meadowList, MonthValueTab mvt, List<DayValueTab> dayList, List<HourValueTab> hourList,
                                          List<SunLightValueTab> sunLightList,
                                          Date recv_time, String filename,ReportLogServices reportLogServices) {
        String currentTime= DateUtils.formatDateTime(new Date());
        //删除库中已有数据
        deleteDb(sf,meadowList,mvt,dayList,hourList,sunLightList);
        //入库
        insertDB(rlist,meadowList,mvt,dayList,hourList,sunLightList,  recv_time,currentTime);
        //添加入库日志
        packReportLog(meadowList,mvt,dayList,hourList,currentTime,reportLogServices);
    }

    private void packReportLog(List<MeadowValueTab> meadowList, MonthValueTab mvt, List<DayValueTab> dayList, List<HourValueTab> hourList,String currentTime,ReportLogServices reportLogServices) {
        List<ReportLogInfo> list=new ArrayList<>();
        ReportLogInfo rlog=null;
        InetAddress ia=null;

        try {
            ia = InetAddress.getLocalHost();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(meadowList.size()>0){
            rlog=new ReportLogInfo();
            rlog.setId(UUID.randomUUID()+"");
            rlog.setTime(currentTime);
            rlog.setOperitorType("1");
            rlog.setStationInfo(meadowList.get(0).getV01300()+"");
            rlog.setDataNum(RamUsageEstimator.humanSizeOf(meadowList));
            rlog.setDataType("3");
            if(ia!=null){
                rlog.setAddr(ia.getHostAddress());
            }

            list.add(rlog);
        }
        if(mvt!=null){
            rlog=new ReportLogInfo();
            rlog.setId(UUID.randomUUID()+"");
            rlog.setTime(currentTime);
            rlog.setOperitorType("1");
            rlog.setStationInfo(mvt.getV01300()+"");
            rlog.setDataNum(RamUsageEstimator.humanSizeOf(mvt));
            rlog.setDataType("4");
            if(ia!=null){
                rlog.setAddr(ia.getHostAddress());
            }
            list.add(rlog);
        }
        if(dayList.size()>0){
            rlog=new ReportLogInfo();
            rlog.setId(UUID.randomUUID()+"");
            rlog.setTime(currentTime);
            rlog.setOperitorType("1");
            rlog.setStationInfo(dayList.get(0).getV01300()+"");
            rlog.setDataNum(RamUsageEstimator.humanSizeOf(dayList));
            rlog.setDataType("2");
            if(ia!=null){
                rlog.setAddr(ia.getHostAddress());
            }
            list.add(rlog);
        }
        if(hourList.size()>0){
            rlog=new ReportLogInfo();
            rlog.setId(UUID.randomUUID()+"");
            rlog.setTime(currentTime);
            rlog.setOperitorType("1");
            rlog.setStationInfo(hourList.get(0).getV01300()+"");
            rlog.setDataNum(RamUsageEstimator.humanSizeOf(hourList));
            rlog.setDataType("1");
            if(ia!=null){
                rlog.setAddr(ia.getHostAddress());
            }
            list.add(rlog);
        }
        reportLogServices.insertLogBatch(list);

    }


    /**
     * 入库
     * @param dayList 日值数据
     * @param hourList 小时值数据
     * @param sunLightList 日照数据
     */
    private void insertDB(List<RadiDigChnMulTab> rlist,List<MeadowValueTab> meadowList,MonthValueTab mvt,List<DayValueTab> dayList,List<HourValueTab> hourList,
                          List<SunLightValueTab> sunLightList, Date recv_time,String currentTime) {
        PreparedStatement psDay = null;
        PreparedStatement psHour = null;
        PreparedStatement psSun = null;
        PreparedStatement psMonth = null;
        PreparedStatement psMeadow = null;
        PreparedStatement psRadi = null;
        Connection connection = null;
        try {
            connection= ConnectionPoolFactory.getInstance().getConnection("xugu");
            String dayValueTable = Global.getConfig("day_value_table_name");
            String hourValueTable = Global.getConfig("hour_value_table_name");
            String sunlightValueTable = Global.getConfig("sunlight_value_table_name");
            String monthValueTable=Global.getConfig("month_value_table_name");
            String meadowValueTable=Global.getConfig("meadow_value_table_name");
            String radiValueTable=Global.getConfig("R011_value_table_name");
            String daysql=null;
            String hoursql=null;
            String sunsql=null;
            String monthsql=null;
            String meadowsql=null;
            String rsql=null;
            if(rlist!=null&&rlist.size()>0){
                rsql = " INSERT INTO " + radiValueTable + "("
                        + "D_RETAIN_ID, COUNTRYNAME, PROVINCENAME, " +
                        "      CITYNAME, CNTYNAME, TOWNNAME, " +
                        "      D_SOURCE_ID, CNAME, D_DATA_ID, " +
                        "      D_IYMDHM, D_RYMDHM, D_UPDATE_TIME, " +
                        "      D_DATETIME, V_BBB, V01301, " +
                        "      V01300, V05001, V06001, " +
                        "      V07001, V04001, V04002, " +
                        "      V04003, V04004, V04005, " +
                        "      V14311, V14312, V14313, " +
                        "      V14314, V14315, V14316, " +
                        "      V14320, V14311_05, V14021_05_052, " +
                        "      V14308, V14312_05, V14312_05_052, " +
                        "      V14312_06, V14312_06_052, V14322, " +
                        "      V14313_05, V14313_05_052, V14309, " +
                        "      V14314_05, V14314_05_052, V14306, " +
                        "      V14315_05, V14315_05_052, V14307, " +
                        "      V14316_05, V14316_05_052, V14032, " +
                        "      V15483, Q14311, Q14312, " +
                        "      Q14313, Q14314, Q14315, " +
                        "      Q14316, Q14320, Q14311_05, " +
                        "      Q14021_05_052, Q14308, Q14312_05, " +
                        "      Q14312_05_052, Q14312_06, Q14312_06_052, " +
                        "      Q14322, Q14313_05, Q14313_05_052, " +
                        "      Q14309, Q14314_05, Q14314_05_052, " +
                        "      Q14306, Q14315_05, Q14315_05_052, " +
                        "      Q14307, Q14316_05, Q14316_05_052, " +
                        "      Q14032, Q15483) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                psRadi = new LoggableStatement(connection, rsql);
            }
            if(meadowList!=null&&meadowList.size()>0){
                meadowsql="insert INTO "+meadowValueTable+"( " +
                        "D_RETAIN_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,V05001,V06001,V07001,V07031,V02301,V_ACODE,V04001,V04002,V04290,V10004_701,V10301_avg,V10302_avg,V10301,V10301_040,V10301_060,V10302,V10302_040,V10302_060,V10051_701,V12001_701,V12011_avg,V12012_avg,V12011," +
                        "V12011_040,V12011_060_CHAR,V12012,V12012_040,V12012_060_CHAR,V13004_701,V13003_701,V20010_701,V20051_701,V13305,V13306,V13305_SD,V13306_SD,V13353,V13355_025,V13355_050,V12120_701,V12311,V12311_040,V12311_060_CHAR,V12121,V12121_040,V12121_060_CHAR,V12311_avg,V12121_avg,V12314_701,V12315,V12315_avg,V12315_040,V12315_060_CHAR,V12316," +
                        "V12316_avg,V12316_040,V12316_060_CHAR,V12030_701_005,V12030_701_010,V12030_701_015,V12030_701_020,V12030_701_040,V12030_701_080,V12030_701_160,V12030_701_320,V11291_701,V11042,V11296,V11042_040,V11042_060,V11046,V11211,V11046_040,V11211_060,V11293_701,V04330_015,V13334,V13334_040,V13334_060_CHAR,V13330,V13330_040,V13330_060,V13032,V13033,V14032," +
                        "sunTime,V14033,V12011_701,V12012_701,v14311,v14312" +
                        ")values(" +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?)";
                psMeadow = new LoggableStatement(connection, meadowsql);
            }
            if(mvt!=null){
                monthsql="insert INTO "+monthValueTable+"( " +
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
                psMonth = new LoggableStatement(connection, monthsql);
            }
            if(dayList!=null&&dayList.size()>0){
                daysql="insert INTO "+dayValueTable+"( " +
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
                psDay = new LoggableStatement(connection, daysql);
            }
            if(hourList!=null&&hourList.size()>0) {
                hoursql="insert INTO "+hourValueTable+"( " +
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
                psHour = new LoggableStatement(connection, hoursql);
            }
            if(sunLightList!=null&&sunLightList.size()>0) {
                sunsql="insert INTO "+sunlightValueTable+"( " +
                        "D_RETAIN_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V01301,V01300,V05001,V06001,V07001,V02301,V_ACODE,V14332,V04001,V04002,V04003,V14032_001,V14032_002,V14032_003,V14032_004,V14032_005,V14032_006,V14032_007,V14032_008,V14032_009,V14032_010,V14032_011,V14032_012,V14032_013," +
                        "V14032_014,V14032_015,V14032_016,V14032_017,V14032_018,V14032_019,V14032_020,V14032_021,V14032_022,V14032_023,V14032_024,V14032,Q14032_001,Q14032_002,Q14032_003,Q14032_004,Q14032_005,Q14032_006,Q14032_007,Q14032_008,Q14032_009,Q14032_010,Q14032_011,Q14032_012,Q14032_013,Q14032_014,Q14032_015,Q14032_016,Q14032_017,Q14032_018,Q14032_019," +
                        "Q14032_020,Q14032_021,Q14032_022,Q14032_023,Q14032_024,Q14032" +
                        ")values(" +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?)";
                psSun = new LoggableStatement(connection, sunsql);
            }
            if(psRadi!=null){

                for (RadiDigChnMulTab radiDigChnMulTab : rlist) {
                    int ii = 1;
                    try {
                        psRadi.setString(ii++, radiDigChnMulTab.getdRecordId());//记录标识
                        psRadi.setString(ii++, radiDigChnMulTab.getCountryname());//国家名称
                        psRadi.setString(ii++, radiDigChnMulTab.getProvincename());//省名
                        psRadi.setString(ii++, radiDigChnMulTab.getCityname());//地市名
                        psRadi.setString(ii++, radiDigChnMulTab.getCntyname());//区县名
                        psRadi.setString(ii++, radiDigChnMulTab.getTownname());//乡镇名
                        psRadi.setString(ii++, radiDigChnMulTab.getdSourceId());//数据来源
                        psRadi.setString(ii++, radiDigChnMulTab.getCname());//站名
                        psRadi.setString(ii++, radiDigChnMulTab.getdDataId());//资料标识
                        if(radiDigChnMulTab.getdIymdhm()==null){
                            psRadi.setString(ii++, currentTime);
                        }else{
                            psRadi.setString(ii++, radiDigChnMulTab.getdIymdhm());//入库时间
                        }
                        psRadi.setString(ii++, currentTime);//收到时间
                        psRadi.setString(ii++, currentTime);//更新时间
                        psRadi.setString(ii++, radiDigChnMulTab.getdDatetime());//资料时间
                        psRadi.setString(ii++, radiDigChnMulTab.getvBbb());//更正报标志
                        psRadi.setString(ii++, radiDigChnMulTab.getV01301());//区站号/观测平台标识(字符)
                        if(radiDigChnMulTab.getV01300()==null){
                            psRadi.setString(ii++, "999999");
                        }else{
                            psRadi.setString(ii++, radiDigChnMulTab.getV01300());//区站号/观测平台标识(数字)
                        }
                        if(radiDigChnMulTab.getV05001()==null){
                            psRadi.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV05001());//纬度
                        }
                        if(radiDigChnMulTab.getV06001()==null){
                            psRadi.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV06001());//经度
                        }
                        if(radiDigChnMulTab.getV07001()==null){
                            psRadi.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV07001());//测站高度
                        }
                        if(radiDigChnMulTab.getV04001()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV04001());//年
                        }
                        if(radiDigChnMulTab.getV04002()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV04002());//月
                        }
                        if(radiDigChnMulTab.getV04003()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV04003());//日
                        }
                        if(radiDigChnMulTab.getV04004()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV04004());//时
                        }
                        if(radiDigChnMulTab.getV04005()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV04005());//分
                        }
                        if(radiDigChnMulTab.getV14311()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV14311());//总辐射辐照度
                        }
                        if(radiDigChnMulTab.getV14312()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV14312());//净辐射辐照度
                        }
                        if(radiDigChnMulTab.getV14313()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV14313());//直接辐射辐照度
                        }
                        if(radiDigChnMulTab.getV14314()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV14314());//散射辐射辐照度
                        }
                        if(radiDigChnMulTab.getV14315()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV14315());//反射辐射辐照度
                        }
                        if(radiDigChnMulTab.getV14316()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV14316());//紫外辐射辐照度
                        }
                        if(radiDigChnMulTab.getV14320()==null){
                            psRadi.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV14320());//总辐射曝辐量
                        }
                        if(radiDigChnMulTab.getV1431105()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV1431105());//总辐射辐照度最大值
                        }
                        if(radiDigChnMulTab.getV1402105052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV1402105052());//总辐射辐照度最大出现时间
                        }
                        if(radiDigChnMulTab.getV14308()==null){
                            psRadi.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV14308());//净辐射曝辐量
                        }
                        if(radiDigChnMulTab.getV1431205()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV1431205());//净辐射辐照度最大值
                        }
                        if(radiDigChnMulTab.getV1431205052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV1431205052());//净辐射辐照度最大出现时间
                        }
                        if(radiDigChnMulTab.getV1431206()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV1431206());//净辐射辐照度最小值
                        }
                        if(radiDigChnMulTab.getV1431206052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV1431206052());//净辐射辐照度最小出现时间
                        }
                        if(radiDigChnMulTab.getV14322()==null){
                            psRadi.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV14322());//直接辐射曝辐量
                        }
                        if(radiDigChnMulTab.getV1431305()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV1431305());//直接辐射辐照度最大值
                        }
                        if(radiDigChnMulTab.getV1431305052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV1431305052());//直接辐射辐照度最大出现时间
                        }
                        if(radiDigChnMulTab.getV14309()==null){
                            psRadi.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV14309());//散射辐射曝辐量
                        }
                        if(radiDigChnMulTab.getV1431405()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV1431405());//散射辐射辐照度最大值
                        }
                        if(radiDigChnMulTab.getV1431405052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV1431405052());//散射辐射辐照度最大出现时间
                        }
                        if(radiDigChnMulTab.getV14306()==null){
                            psRadi.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV14306());//反射辐射曝辐量
                        }
                        if(radiDigChnMulTab.getV1431505()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV1431505());//反射辐射辐照度最大值
                        }
                        if(radiDigChnMulTab.getV1431505052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV1431505052());//反射辐射辐照度最大出现时间
                        }
                        if(radiDigChnMulTab.getV14307()==null){
                            psRadi.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV14307());//紫外辐射曝辐量
                        }
                        if(radiDigChnMulTab.getV1431605()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV1431605());//紫外辐射辐照度最大值
                        }
                        if(radiDigChnMulTab.getV1431605052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getV1431605052());//紫外辐射辐照度最大出现时间
                        }
                        if(radiDigChnMulTab.getV14032()==null){
                            psRadi.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV14032());//日照时数（直接辐射计算值）
                        }
                        if(radiDigChnMulTab.getV15483()==null){
                            psRadi.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                        }else{
                            psRadi.setBigDecimal(ii++, radiDigChnMulTab.getV15483());//大气浑浊度
                        }
                        if(radiDigChnMulTab.getQ14311()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14311());//总辐射辐照度质控码
                        }
                        if(radiDigChnMulTab.getQ14312()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14312());//净全辐射辐照度质控码
                        }
                        if(radiDigChnMulTab.getQ14313()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14313());//直射辐射辐照度质控码
                        }
                        if(radiDigChnMulTab.getQ14314()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14314());//散射辐射辐照度质控码
                        }
                        if(radiDigChnMulTab.getQ14315()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14315());//反射辐射辐照度质控码
                        }
                        if(radiDigChnMulTab.getQ14316()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14316());//紫外辐射辐照度质控码
                        }
                        if(radiDigChnMulTab.getQ14320()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14320());//总辐射曝辐量质控码
                        }
                        if(radiDigChnMulTab.getQ1431105()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431105());//总辐射辐照度最大值质控码
                        }
                        if(radiDigChnMulTab.getQ1402105052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1402105052());//总辐射辐照度最大出现时间质控码
                        }
                        if(radiDigChnMulTab.getQ14308()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14308());//净辐射曝辐量质控码
                        }
                        if(radiDigChnMulTab.getQ1431205()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431205());//净辐射辐照度最大值质控码
                        }
                        if(radiDigChnMulTab.getQ1431205052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431205052());//净辐射辐照度最大出现时间质控码
                        }
                        if(radiDigChnMulTab.getQ1431206()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431206());//净辐射辐照度最小值质控码
                        }
                        if(radiDigChnMulTab.getQ1431206052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431206052());//净辐射辐照度最小出现时间质控码
                        }
                        if(radiDigChnMulTab.getQ14322()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14322());//直接辐射曝辐量质控码
                        }
                        if(radiDigChnMulTab.getQ1431305()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431305());//直接辐射辐照度最大值质控码
                        }
                        if(radiDigChnMulTab.getQ1431305052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431305052());//直接辐射辐照度最大出现时间质控码
                        }
                        if(radiDigChnMulTab.getQ14309()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14309());//散射辐射曝辐量质控码
                        }
                        if(radiDigChnMulTab.getQ1431405()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431405());//散射辐射辐照度最大值质控码
                        }
                        if(radiDigChnMulTab.getQ1431405052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431405052());//散射辐射辐照度最大出现时间质控码
                        }
                        if(radiDigChnMulTab.getQ14306()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14306());//反射辐射曝辐量质控码
                        }
                        if(radiDigChnMulTab.getQ1431505()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431505());//反射辐射辐照度最大值质控码
                        }
                        if(radiDigChnMulTab.getQ1431505052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431505052());//反射辐射辐照度最大出现时间质控码
                        }
                        if(radiDigChnMulTab.getQ14307()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14307());//紫外辐射曝辐量质控码
                        }
                        if(radiDigChnMulTab.getQ1431605()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431605());//紫外辐射辐照度最大值质控码
                        }
                        if(radiDigChnMulTab.getQ1431605052()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ1431605052());//紫外辐射辐照度最大出现时间质控码
                        }
                        if(radiDigChnMulTab.getQ14032()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ14032());//日总日照时数质控码
                        }
                        if(radiDigChnMulTab.getQ15483()==null){
                            psRadi.setInt(ii++, 999999);
                        }else{
                            psRadi.setInt(ii++, radiDigChnMulTab.getQ15483());//大气浑浊度质控码
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println("辐射资料插入列号：" + ii + "有误");
                    }
                    psRadi.addBatch();
                }
            }
            if(psMeadow!=null){
                for (MeadowValueTab meadowValueTab : meadowList) {
                    int ii = 1;
                    try {
                        psMeadow.setString(ii++, meadowValueTab.getD_RECORD_ID());//记录标识 （ 系统自动生成的流水号 ）
                        psMeadow.setString(ii++, meadowValueTab.getD_DATA_ID());//资料标识 （ 资料的4级编码 ）
                        if(meadowValueTab.getD_IYMDHM()==null){
                            psMeadow.setString(ii++, currentTime);
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getD_IYMDHM());//入库时间 （ 插表时的系统时间 ）
                        }
                        if(meadowValueTab.getD_RYMDHM()==null){
                            psMeadow.setString(ii++, currentTime);
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getD_RYMDHM());//收到时间 （ DPC消息生成时间 ）
                        }
                        if(meadowValueTab.getD_UPDATE_TIME()==null){
                            psMeadow.setString(ii++, currentTime);
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getD_UPDATE_TIME());//更新时间 （ 根据CCx对记录更新时的系统时间 ）
                        }
                            psMeadow.setString(ii++, meadowValueTab.getD_DATETIME());//资料时间 （ 由V04001、V04002组成 ）
                        if(meadowValueTab.getV01301()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV01301());//区站号(字符)
                        }
                        if(meadowValueTab.getV01300()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV01300());//区站号(数字)
                        }
                        if(meadowValueTab.getV05001()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV05001());//纬度 （ 单位：度 ）
                        }
                        if(meadowValueTab.getV06001()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV06001());//经度 （ 单位：度 ）
                        }
                        if(meadowValueTab.getV07001()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV07001());//测站高度 （ 单位：米 ）
                        }
                        if(meadowValueTab.getV07031()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV07031());//气压传感器海拔高度 （ 单位：米 ）
                        }
                        if(meadowValueTab.getV02301()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV02301());//台站级别 （ 代码表 ）
                        }
                        if(meadowValueTab.getV_ACODE()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV_ACODE());//中国行政区划代码 （ 代码表 ）
                        }
                        if(meadowValueTab.getV04001()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV04001());//年
                        }
                        if(meadowValueTab.getV04002()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV04002());//月
                        }
                        if(meadowValueTab.getV04290()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV04290());//旬
                        }
                        if(meadowValueTab.getV10004_701()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV10004_701());//旬平均本站气压 （ 单位：百帕 ）
                        }
                        if(meadowValueTab.getV10301_avg()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV10301_avg());// 旬平均最高本站气压 ( 单位：百帕 )
                        }
                        if(meadowValueTab.getV10302_avg()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV10302_avg());// 旬平均最低本站气压 ( 单位：百帕 )
                        }
                        if(meadowValueTab.getV10301()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV10301());//旬极端最高本站气压 （ 单位：百帕 ）
                        }
                        if(meadowValueTab.getV10301_040()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV10301_040());//旬极端最高本站气压出现日数
                        }
                        if(meadowValueTab.getV10301_060()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV10301_060());//旬极端最高本站气压出现日 （ 字符 ）
                        }
                        if(meadowValueTab.getV10302()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV10302());//旬极端最低本站气压 （ 单位：百帕 ）
                        }
                        if(meadowValueTab.getV10302_040()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV10302_040());//旬极端最低本站气压出现日数 （  ）
                        }
                        if(meadowValueTab.getV10302_060()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV10302_060());//旬极端最低本站气压出现日 （ 字符 ）
                        }
                        if(meadowValueTab.getV10051_701()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV10051_701());//旬平均海平面气压 （ 单位：百帕 ）
                        }
                        if(meadowValueTab.getV12001_701()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12001_701());//旬平均气温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12011_avg()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12011_avg());// 旬平均最高气温 ( 单位：摄氏度 )
                        }
                        if(meadowValueTab.getV12012_avg()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12012_avg());// 旬平均最低气温 ( 单位：摄氏度 )
                        }
                        if(meadowValueTab.getV12011()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12011());//旬极端最高气温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12011_040()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV12011_040());//旬极端最高气温出现日数 （  ）
                        }
                        if(meadowValueTab.getV12011_060_CHAR()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV12011_060_CHAR());//旬极端最高气温出现日 （ 字符 ）
                        }
                        if(meadowValueTab.getV12012()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12012());//旬极端最低气温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12012_040()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV12012_040());//旬极端最低气温出现日数 （  ）
                        }
                        if(meadowValueTab.getV12012_060_CHAR()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV12012_060_CHAR());//旬极端最低气温出现日 （ 字符 ）
                        }
                        if(meadowValueTab.getV13004_701()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV13004_701());//旬平均水汽压 （ 单位：百帕 ）
                        }
                        if(meadowValueTab.getV13003_701()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV13003_701());//旬平均相对湿度 （ 单位：% ）
                        }
                        if(meadowValueTab.getV20010_701()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV20010_701());//旬平均总云量 （ 单位：% ）
                        }
                        if(meadowValueTab.getV20051_701()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV20051_701());//旬平均低云量 （ 单位：% ）
                        }
                        if(meadowValueTab.getV13305()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV13305());//20-20时降水量 （ 单位：毫米 ）
                        }
                        if(meadowValueTab.getV13306()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV13306());//08-08时降水量 （ 单位：毫米 ）
                        }
                        if(meadowValueTab.getV13305_SD()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV13305_SD());//20-20时固态降水量 （ 单位：毫米 ）
                        }
                        if(meadowValueTab.getV13306_SD()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV13306_SD());//08-08时固态降水量 （ 单位：毫米 ）
                        }
                        if(meadowValueTab.getV13353()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV13353());//旬内日降水量≥0.1mm的日数 （ 单位：日 ）
                        }
                        if(meadowValueTab.getV13355_025()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV13355_025());//旬内日降水量≥25mm的日数 （ 单位：日 ）
                        }
                        if(meadowValueTab.getV13355_050()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV13355_050());//旬内日降水量≥50mm的日数 （ 单位：日 ）
                        }
                        if(meadowValueTab.getV12120_701()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12120_701());//旬平均地面温度 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12311()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12311());//旬极端最高地面温度 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12311_040()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV12311_040());//旬极端最高地面温度出现日数 （  ）
                        }
                        if(meadowValueTab.getV12311_060_CHAR()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV12311_060_CHAR());//旬极端最高地面温度出现日 （ 字符 ）
                        }
                        if(meadowValueTab.getV12121()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12121());//旬极端最低地面温度 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12121_040()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV12121_040());//旬极端最低地面温度出现日数 （  ）
                        }
                        if(meadowValueTab.getV12121_060_CHAR()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV12121_060_CHAR());//旬极端最低地面温度出现日 （ 字符 ）
                        }
                        if(meadowValueTab.getV12311_avg()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12311_avg());// 旬平均最高地面温度 ( 单位：摄氏度 )
                        }
                        if(meadowValueTab.getV12121_avg()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12121_avg());// 旬平均最低地面温度 ( 单位：摄氏度 )
                        }
                        if(meadowValueTab.getV12314_701()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12314_701());//旬平均草面温度 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12315()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12315());//旬极端最高草面（雪面）温度 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12315_avg()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12315_avg());//旬平均最高草面（雪面）温度 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12315_040()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV12315_040());//旬极端最高草面（雪面）温度出现日数 （  ）
                        }
                        if(meadowValueTab.getV12315_060_CHAR()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV12315_060_CHAR());//旬极端最高草面（雪面）温度出现日 （ 字符 ）
                        }
                        if(meadowValueTab.getV12316()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12316());//旬极端最低草面（雪面）温度 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12316_avg()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12316_avg());//旬平均最低草面（雪面）温度 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12316_040()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV12316_040());//旬极端最低草面（雪面）温度出现日数 （  ）
                        }
                        if(meadowValueTab.getV12316_060_CHAR()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV12316_060_CHAR());//旬极端最低草面（雪面）温度出现日 （ 字符 ）
                        }
                        if(meadowValueTab.getV12030_701_005()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12030_701_005());//旬平均5cm地温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12030_701_010()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12030_701_010());//旬平均10cm地温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12030_701_015()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12030_701_015());//旬平均15cm地温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12030_701_020()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12030_701_020());//旬平均20cm地温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12030_701_040()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12030_701_040());//旬平均40cm地温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12030_701_080()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12030_701_080());//旬平均80cm地温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12030_701_160()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12030_701_160());//旬平均160cm地温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12030_701_320()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12030_701_320());//旬平均320cm地温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV11291_701()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV11291_701());//旬平均2分钟风速 （ 单位：米/秒 ）
                        }
                        if(meadowValueTab.getV11042()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV11042());//旬最大风速 （ 单位：米/秒 ）
                        }
                        if(meadowValueTab.getV11296()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV11296());//旬最大风速的风向 （ 单位：度 ）
                        }
                        if(meadowValueTab.getV11042_040()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV11042_040());//旬最大风速出现日数 （  ）
                        }
                        if(meadowValueTab.getV11042_060()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV11042_060());//旬最大风速之出现日 （ 字符 ）
                        }
                        if(meadowValueTab.getV11046()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV11046());//旬极大风速 （ 单位：米/秒 ）
                        }
                        if(meadowValueTab.getV11211()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV11211());//旬极大风速的风向 （ 单位：度 ）
                        }
                        if(meadowValueTab.getV11046_040()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV11046_040());//旬极大风速出现日数 （  ）
                        }
                        if(meadowValueTab.getV11211_060()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV11211_060());//旬极大风速之出现日 （ 字符 ）
                        }
                        if(meadowValueTab.getV11293_701()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV11293_701());//旬平均10分钟风速 （ 单位：米/秒 ）
                        }
                        if(meadowValueTab.getV04330_015()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV04330_015());//旬内大风日数 （ 单位：日 ）
                        }
                        if(meadowValueTab.getV13334()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV13334());//旬内最大积雪深度 （ 单位：厘米 ）
                        }
                        if(meadowValueTab.getV13334_040()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV13334_040());//旬内最大积雪深度出现日数 （  ）
                        }
                        if(meadowValueTab.getV13334_060_CHAR()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV13334_060_CHAR());//旬内最大积雪深度出现日 （ 字符 ）
                        }
                        if(meadowValueTab.getV13330()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV13330());//旬内最大雪压 （ 单位：克/平方厘米 ）
                        }
                        if(meadowValueTab.getV13330_040()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV13330_040());//旬内最大雪压出现日数 （  ）
                        }
                        if(meadowValueTab.getV13330_060()==null){
                            psMeadow.setString(ii++, "999999");
                        }else{
                            psMeadow.setString(ii++, meadowValueTab.getV13330_060());//旬内最大雪压出现日 （ 字符 ）
                        }
                        if(meadowValueTab.getV13032()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV13032());//旬蒸发量(小型) （ 单位：毫米 ）
                        }
                        if(meadowValueTab.getV13033()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV13033());//旬蒸发量(大型) （ 单位：毫米 ）
                        }
                        if(meadowValueTab.getV14032()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV14032());//旬日照时数 （ 单位：小时 ）
                        }
                        if(meadowValueTab.getSunTime()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getSunTime());//日出时长
                        }
                        if(meadowValueTab.getV14033()==null){
                            psMeadow.setInt(ii++, 999999);
                        }else{
                            psMeadow.setInt(ii++, meadowValueTab.getV14033());//旬日照百分率 （ 单位：% ）
                        }
                        if(meadowValueTab.getV12011_701()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12011_701());//旬平均日最高气温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV12012_701()==null){
                            psMeadow.setFloat(ii++, 999999);
                        }else{
                            psMeadow.setFloat(ii++, meadowValueTab.getV12012_701());//旬平均日最低气温 （ 单位：摄氏度 ）
                        }
                        if(meadowValueTab.getV14311()==null){
                            psMeadow.setBigDecimal(ii++, new BigDecimal(999999));
                        }else{
                            psMeadow.setBigDecimal(ii++, meadowValueTab.getV14311());//旬太阳总辐射(MJ/m2)
                        }
                        if(meadowValueTab.getV14312()==null){
                            psMeadow.setBigDecimal(ii++, new BigDecimal(999999));
                        }else{
                            psMeadow.setBigDecimal(ii++, meadowValueTab.getV14312());//旬净全辐射(MJ/m2)
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("旬值插入列号：" + ii + "有误");
                    }
                    psMeadow.addBatch();
                }
            }
            if(psMonth!=null){
                int ii = 1;
                try {
                    psMonth.setString(ii++, mvt.getD_RECORD_ID());//记录标识
                    psMonth.setString(ii++, mvt.getD_DATA_ID());//资料标识
                    if(mvt.getD_IYMDHM()==null){
                        psMonth.setString(ii++, currentTime);
                    }else{
                        psMonth.setString(ii++, mvt.getD_IYMDHM());//入库时间
                    }
                    if(mvt.getD_RYMDHM()==null){
                        psMonth.setString(ii++, currentTime);
                    }else{
                        psMonth.setString(ii++, mvt.getD_RYMDHM());//收到时间
                    }
                    if(mvt.getD_UPDate_TIME()==null){
                        psMonth.setString(ii++, currentTime);
                    }else{
                        psMonth.setString(ii++, mvt.getD_UPDate_TIME());//更新时间
                    }
                        psMonth.setString(ii++, mvt.getD_DateTIME());//资料时间
                    if(mvt.getV01301()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV01301());//区站号字符
                    }
                    if(mvt.getV01300()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV01300());//区站号数字
                    }
                    if(mvt.getV05001()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV05001());//纬度
                    }
                    if(mvt.getV06001()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV06001());//经度
                    }
                    if(mvt.getV07001()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV07001());//测站高度
                    }
                    if(mvt.getV07031()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV07031());//气压传感器海拔高度
                    }
                    if(mvt.getV02301()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV02301());//台站级别 
                    }
                    if(mvt.getV_ACODE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV_ACODE());//中国行政区划代码 
                    }
                    if(mvt.getV04001()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04001());//年 
                    }
                    if(mvt.getV04002()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04002());//月 
                    }
                    if(mvt.getV10004_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV10004_701());//月平均本站气压
                    }
                    if(mvt.getV10301()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV10301());//月极端最高本站气压
                    }
                    if(mvt.getV10301_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV10301_040());//月极端最高本站气压出现日数 
                    }
                    if(mvt.getV10301_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV10301_060_CHAR());//月极端最高本站气压出现日 
                    }
                    if(mvt.getV10302()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV10302());//月极端最低本站气压
                    }
                    if(mvt.getV10302_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV10302_040());//月极端最低本站气压出现日数 
                    }
                    if(mvt.getV10302_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV10302_060_CHAR());//月极端最低本站气压出现日
                    }
                    if(mvt.getV10301_avg()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV10301_avg());//月平均最高本站气压
                    }
                    if(mvt.getV10302_avg()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV10302_avg());//月平均最低本站气压
                    }
                    if(mvt.getV10051_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV10051_701());//月平均海平面气压
                    }
                    if(mvt.getV12001_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12001_701());//月平均气温
                    }
                    if(mvt.getV12011_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12011_701());//月平均日最高气温
                    }
                    if(mvt.getV12012_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12012_701());//月平均日最低气温
                    }
                    if(mvt.getV12011()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12011());//本月极端最高气温
                    }
                    if(mvt.getV12011_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12011_040());//本月极端最高气温出现日数 
                    }
                    if(mvt.getV12011_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV12011_060_CHAR());//极端最高气温出现日 
                    }
                    if(mvt.getV12012()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12012());//本月极端最低气温
                    }
                    if(mvt.getV12012_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12012_040());//本月极端最低气温出现日数 
                    }
                    if(mvt.getV12012_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV12012_060_CHAR());//极端最低气温出现日 
                    }
                    if(mvt.getV12303_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12303_701());//月平均气温日较差
                    }
                    if(mvt.getV12304()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12304());//月最大气温日较差
                    }
                    if(mvt.getV12304_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12304_040());//月最大气温日较差出现日数 
                    }
                    if(mvt.getV12304_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV12304_060_CHAR());//月最大气温日较差出现日 
                    }
                    if(mvt.getV12305()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12305());//月最小气温日较差
                    }
                    if(mvt.getV12305_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12305_040());//月最小气温日较差出现日数 
                    }
                    if(mvt.getV12305_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV12305_060_CHAR());//月最小气温日较差出现日 
                    }
                    if(mvt.getV12605_30()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12605_30());//日最高气温≥30℃日数 
                    }
                    if(mvt.getV12605_35()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12605_35());//日最高气温≥35℃日数 
                    }
                    if(mvt.getV12605_40()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12605_40());//日最高气温≥40℃日数 
                    }
                    if(mvt.getV12607_02()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12607_02());//日最低气温＜2℃日数 
                    }
                    if(mvt.getV12603()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12603());//日最低气温＜0℃日数 
                    }
                    if(mvt.getV12606_02()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12606_02());//日最低气温＜-2℃日数
                    }
                    if(mvt.getV12606_05()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12606_05());//日最低气温＜-5℃日数 ( 单位：日 )
                    }
                    if(mvt.getV12606_10()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12606_10());//日最低气温＜-10℃日数 ( 单位：日 )
                    }
                    if(mvt.getV12606_15()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12606_15());//日最低气温＜-15℃日数 
                    }
                    if(mvt.getV12606_30()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12606_30());//日最低气温＜-30℃日数 
                    }
                    if(mvt.getV12606_40()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12606_40());//日最低气温＜-40℃日数 
                    }
                    if(mvt.getV12610_26()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12610_26());//冷度日数（日平均气温＞26.0℃） 
                    }
                    if(mvt.getV12611_18()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12611_18());//暖度日数（日平均气温＜18.0℃）
                    }
                    if(mvt.getV13003_701()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13003_701());//月平均相对湿度
                    }
                    if(mvt.getV13007()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13007());//月最小相对湿度 
                    }
                    if(mvt.getV13007_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13007_040());//月最小相对湿度出现日数 
                    }
                    if(mvt.getV13007_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV13007_060_CHAR());//月最小相对湿度出现日 
                    }
                    if(mvt.getV20010_701()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20010_701());//月平均总云量 
                    }
                    if(mvt.getV20051_701()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20051_701());//月平均低云量 
                    }
                    if(mvt.getV20501_02()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20501_02());//日平均总云量< 2.0日数 
                    }
                    if(mvt.getV20500_08()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20500_08());//日平均总云量> 8.0日数 
                    }
                    if(mvt.getV20503_02()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20503_02());//日平均低云量< 2.0日数 
                    }
                    if(mvt.getV20502_08()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20502_08());//日平均低云量> 8.0日数 
                    }
                    if(mvt.getV13305()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV13305());//20-20时月总降水量
                    }
                    if(mvt.getV13306()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV13306());//08-08时月总降水量
                    }
                    if(mvt.getV13052()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV13052());//月最大日降水量
                    }
                    if(mvt.getV13052_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13052_040());//月最大日降水量出现日数 
                    }
                    if(mvt.getV13052_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV13052_060_CHAR());//月最大日降水量出现日 
                    }
                    if(mvt.getV13353()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13353());//日总降水量≥0.1mm日数
                    }
                    if(mvt.getV13355_001()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13355_001());//日总降水量≥1mm日数
                    }
                    if(mvt.getV13355_005()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13355_005());//日总降水量≥5mm日数
                    }
                    if(mvt.getV13355_010()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13355_010());//日总降水量≥10mm日数
                    }
                    if(mvt.getV13355_025()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13355_025());//日总降水量≥25mm日数
                    }
                    if(mvt.getV13355_050()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13355_050());//日总降水量≥50mm日数
                    }
                    if(mvt.getV13355_100()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13355_100());//日总降水量≥100mm日数
                    }
                    if(mvt.getV13355_150()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13355_150());//日总降水量≥150mm日数
                    }
                    if(mvt.getV13355_250()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13355_250());//日降水量≥250mm日数 ( 单位：日 )
                    }
                    if(mvt.getV20430()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20430());//月最长连续降水日数 
                    }
                    if(mvt.getV13380()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13380());//月最长连续降水量 
                    }
                    if(mvt.getV20431()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20431());//月最长连续降水止日 
                    }
                    if(mvt.getV20432()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20432());//月最长连续无降水日数 
                    }
                    if(mvt.getV20433()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20433());//月最长连续无降水止日 
                    }
                    if(mvt.getV13381()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13381());//月最大连续降水量 
                    }
                    if(mvt.getV20434()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20434());//月最大连续降水日数 
                    }
                    if(mvt.getV20435()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20435());//月最大连续降水止日 
                    }
                    if(mvt.getV13302_01()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13302_01());//1小时最大降水量 
                    }
                    if(mvt.getV13302_01_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13302_01_040());//1小时最大降水量出现日数 
                    }
                    if(mvt.getV13302_01_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV13302_01_060_CHAR());//1小时最大降水量出现日 
                    }
                    if(mvt.getV04330_089()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04330_089());//冰雹日数 
                    }
                    if(mvt.getV04330_007()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04330_007());//扬沙日数 
                    }
                    if(mvt.getV04330_006()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04330_006());//浮尘日数 
                    }
                    if(mvt.getV04330_005()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04330_005());//霾日数 
                    }
                    if(mvt.getV04330_008()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04330_008());//龙卷风日数 
                    }
                    if(mvt.getV04330_015()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04330_015());//大风日数 
                    }
                    if(mvt.getV04330_031()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04330_031());//沙尘暴日数 
                    }
                    if(mvt.getV04330_042()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04330_042());//雾日数 
                    }
                    if(mvt.getV04330_017()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04330_017());//雷暴日数 
                    }
                    if(mvt.getV04330_002()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04330_002());//霜日数 
                    }
                    if(mvt.getV04330_070()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04330_070());//降雪日数 
                    }
                    if(mvt.getV04330_016()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV04330_016());//积雪日数 
                    }
                    if(mvt.getV20305_540()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20305_540());//电线积冰（雨凇+雾凇）日数
                    }
                    if(mvt.getV20309_010()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20309_010());//能见度≤10km出现频率
                    }
                    if(mvt.getV20309_001()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20309_001());//能见度≤1km出现频率
                    }
                    if(mvt.getV20309_005()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20309_005());//能见度≤5km出现频率
                    }
                    if(mvt.getV20309_008()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20309_008());//月能见度<800米出现次数
                    }
                    if(mvt.getV20309_100()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20309_100());//能见度≤1km出现次数
                    }
                    if(mvt.getV20309_150()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20309_150());//能见度≤1.5km出现次数
                    }
                    if(mvt.getV20309_300()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20309_300());//能见度≤3km出现次数
                    }
                    if(mvt.getV20309_011()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20309_011());//能见度>1km出现次数
                    }
                    if(mvt.getV13032()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV13032());//月总蒸发量小型
                    }
                    if(mvt.getV13033()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV13033());//月总蒸发量大型
                    }
                    if(mvt.getV13334()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV13334());//最大积雪深度
                    }
                    if(mvt.getV13334_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13334_040());//最大积雪深度日数 
                    }
                    if(mvt.getV13334_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV13334_060_CHAR());//最大积雪深度出现日 
                    }
                    if(mvt.getV13330()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV13330());//最大雪压
                    }
                    if(mvt.getV13330_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13330_040());//最大雪压出现日数 
                    }
                    if(mvt.getV13330_060()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV13330_060());//最大雪压出现日 
                    }
                    if(mvt.getV13356_001()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13356_001());//积雪深度≥1cm日数 
                    }
                    if(mvt.getV13356_005()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13356_005());//积雪深度≥5cm日数 
                    }
                    if(mvt.getV13356_010()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13356_010());//积雪深度≥10cm日数 
                    }
                    if(mvt.getV13356_020()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13356_020());//积雪深度≥20cm日数 
                    }
                    if(mvt.getV13356_030()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13356_030());//积雪深度≥30cm日数
                    }
                    if(mvt.getV20326_NS()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV20326_NS());//电线积冰-南北方向最大重量的相应直径( 单位：毫米 )
                    }
                    if(mvt.getV20306_NS()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV20306_NS());//电线积冰-南北方向最大重量的相应厚度( 单位：毫米 )
                    }
                    if(mvt.getV20307_NS()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV20307_NS());//电线积冰-南北方向最大重量 ( 单位：克 )
                    }
                    if(mvt.getV20326_WE()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV20326_WE());//电线积冰-东西方向最大重量的相应直径( 单位：毫米 )
                    }
                    if(mvt.getV20306_WE()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV20306_WE());//电线积冰-东西方向最大重量的相应厚度 ( 单位：毫米 )
                    }
                    if(mvt.getV20307_WE()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV20307_WE());//电线积冰-东西方向最大重量 ( 单位：克 )
                    }
                    if(mvt.getV20440_040_NS()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20440_040_NS());//电线积冰-南北最大重量出现日数
                    }
                    if(mvt.getV20440_060_NS_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV20440_060_NS_CHAR());//电线积冰-南北最大重量出现日
                    }
                    if(mvt.getV20440_040_WE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20440_040_WE());//电线积冰-东西最大重量出现日数
                    }
                    if(mvt.getV20440_060_WE_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV20440_060_WE_CHAR());//电线积冰-东西最大重量出现日
                    }
                    if(mvt.getV11291_701()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11291_701());//月平均风速（2分钟） 
                    }
                    if(mvt.getV11042()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV11042());//月最大风速
                    }
                    if(mvt.getV11296_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV11296_CHAR());//最大风速的风向 
                    }
                    if(mvt.getV11042_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_040());//最大风速出现日数 
                    }
                    if(mvt.getV11042_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV11042_060_CHAR());//月最大风速之出现日
                    }
                    if(mvt.getV11042_05()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_05());//日最大风速≥5.0m/s日数 
                    }
                    if(mvt.getV11042_10()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_10());//日最大风速≥10.0m/s日数 
                    }
                    if(mvt.getV11042_12()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_12());//日最大风速≥12.0m/s日数 
                    }
                    if(mvt.getV11042_15()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_15());//日最大风速≥15.0m/s日数 
                    }
                    if(mvt.getV11042_17()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_17());//日最大风速≥17.0m/s日数 
                    }
                    if(mvt.getV11046()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV11046());//月极大风速
                    }
                    if(mvt.getV11211()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV11211());//月极大风速之风向
                    }
                    if(mvt.getV11046_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11046_040());//月极大风速出现日数 
                    }
                    if(mvt.getV11046_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV11046_060_CHAR());//月极大风速之出现日 
                    }
                    if(mvt.getV11350_NNE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_NNE());//NNE风向出现频率 
                    }
                    if(mvt.getV11350_NE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_NE());//NE风向出现频率 
                    }
                    if(mvt.getV11350_ENE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_ENE());//ENE风向出现频率 
                    }
                    if(mvt.getV11350_E()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_E());//E风向出现频率 
                    }
                    if(mvt.getV11350_ESE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_ESE());//ESE风向出现频率 
                    }
                    if(mvt.getV11350_SE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_SE());//SE风向出现频率 
                    }
                    if(mvt.getV11350_SSE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_SSE());//SSE风向出现频率 
                    }
                    if(mvt.getV11350_S()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_S());//S风向出现频率 
                    }
                    if(mvt.getV11350_SSW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_SSW());//SSW风向出现频率 
                    }
                    if(mvt.getV11350_SW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_SW());//SW风向出现频率 
                    }
                    if(mvt.getV11350_WSW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_WSW());//WSW风向出现频率 
                    }
                    if(mvt.getV11350_W()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_W());//W风向出现频率 
                    }
                    if(mvt.getV11350_WNW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_WNW());//WNW风向出现频率 
                    }
                    if(mvt.getV11350_NW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_NW());//NW风向出现频率 
                    }
                    if(mvt.getV11350_NNW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_NNW());//NNW风向出现频率 
                    }
                    if(mvt.getV11350_N()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_N());//N风向出现频率 
                    }
                    if(mvt.getV11350_C()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11350_C());//C风向（静风）出现频率 
                    }
                    if(mvt.getV11314_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV11314_CHAR());//月最多风向 
                    }
                    if(mvt.getV11314_061()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11314_061());//月最多风向出现频率 
                    }
                    if(mvt.getV11315_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV11315_CHAR());//月次多风向 
                    }
                    if(mvt.getV11315_061()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11315_061());//月次多风向出现频率 
                    }
                    if(mvt.getV11351_NNE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_NNE());//NNE风的平均风速 
                    }
                    if(mvt.getV11351_NE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_NE());//NE风的平均风速 
                    }
                    if(mvt.getV11351_ENE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_ENE());//ENE的平均风速 
                    }
                    if(mvt.getV11351_E()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_E());//E风的平均风速 
                    }
                    if(mvt.getV11351_ESE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_ESE());//ESE风的平均风速 
                    }
                    if(mvt.getV11351_SE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_SE());//SE风的平均风速 
                    }
                    if(mvt.getV11351_SSE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_SSE());//SSE风的平均风速 
                    }
                    if(mvt.getV11351_S()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_S());//S风的平均风速 
                    }
                    if(mvt.getV11351_SSW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_SSW());//SSW风的平均风速 
                    }
                    if(mvt.getV11351_SW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_SW());//SW风的平均风速 
                    }
                    if(mvt.getV11351_WSW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_WSW());//WSW风的平均风速 
                    }
                    if(mvt.getV11351_W()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_W());//W风的平均风速 
                    }
                    if(mvt.getV11351_WNW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_WNW());//WNW风的平均风速 
                    }
                    if(mvt.getV11351_NW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_NW());//NW风的平均风速 
                    }
                    if(mvt.getV11351_NNW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_NNW());//NNW风的平均风速 
                    }
                    if(mvt.getV11351_N()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11351_N());//N风的平均风速 
                    }
                    if(mvt.getV11042_NNE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_NNE());//NNE风的最大风速 
                    }
                    if(mvt.getV11042_NE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_NE());//NE风的最大风速 
                    }
                    if(mvt.getV11042_ENE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_ENE());//ENE的最大风速 
                    }
                    if(mvt.getV11042_E()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_E());//E风的最大风速 
                    }
                    if(mvt.getV11042_ESE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_ESE());//ESE风的最大风速 
                    }
                    if(mvt.getV11042_SE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_SE());//SE风的最大风速 
                    }
                    if(mvt.getV11042_SSE()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_SSE());//SSE风的最大风速 
                    }
                    if(mvt.getV11042_S()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_S());//S风的最大风速 
                    }
                    if(mvt.getV11042_SSW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_SSW());//SSW风的最大风速 
                    }
                    if(mvt.getV11042_SW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_SW());//SW风的最大风速 
                    }
                    if(mvt.getV11042_WSW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_WSW());//WSW风的最大风速 
                    }
                    if(mvt.getV11042_W()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_W());//W风的最大风速 
                    }
                    if(mvt.getV11042_WNW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_WNW());//WNW风的最大风速 
                    }
                    if(mvt.getV11042_NW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_NW());//NW风的最大风速 
                    }
                    if(mvt.getV11042_NNW()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_NNW());//NNW风的最大风速 
                    }
                    if(mvt.getV11042_N()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV11042_N());//N风的最大风速 
                    }
                    if(mvt.getV12120_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12120_701());//月平均地面温度
                    }
                    if(mvt.getV12311_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12311_701());//月平均最高地面温度
                    }
                    if(mvt.getV12121_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12121_701());//月平均最低地面温度
                    }
                    if(mvt.getV12620()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12620());//月内日最低地面温度≤0.0℃日数
                    }
                    if(mvt.getV12311()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12311());//月极端最高地面温度
                    }
                    if(mvt.getV12311_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12311_040());//月极端最高地面温度出现日数 
                    }
                    if(mvt.getV12311_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV12311_060_CHAR());//月极端最高地面温度出现日 
                    }
                    if(mvt.getV12121()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12121());//月极端最低地面温度
                    }
                    if(mvt.getV12121_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12121_040());//月极端最低地面温度出现日数 
                    }
                    if(mvt.getV12121_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV12121_060_CHAR());//月极端最低地面温度出现日 
                    }
                    if(mvt.getV12030_701_005()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12030_701_005());//月平均5cm地温
                    }
                    if(mvt.getV12030_701_010()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12030_701_010());//月平均10cm地温
                    }
                    if(mvt.getV12030_701_015()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12030_701_015());//月平均15cm地温
                    }
                    if(mvt.getV12030_701_020()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12030_701_020());//月平均20cm地温
                    }
                    if(mvt.getV12030_701_040()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12030_701_040());//月平均40cm地温
                    }
                    if(mvt.getV12030_701_080()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12030_701_080());//月平均80cm地温
                    }
                    if(mvt.getV12030_701_160()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12030_701_160());//月平均160cm地温
                    }
                    if(mvt.getV12030_701_320()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12030_701_320());//月平均320cm地温
                    }
                    if(mvt.getBalltemp_avg()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getBalltemp_avg());//月平均湿球温度
                    }
                    if(mvt.getV13004_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV13004_701());//月平均水汽压
                    }
                    if(mvt.getV13004_MAX()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV13004_MAX());//月最大水汽压
                    }
                    if(mvt.getV13004_MAX_D()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13004_MAX_D());//月最大水汽压出现日期
                    }
                    if(mvt.getV13004_MIN()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV13004_MIN());//月最小水气压
                    }
                    if(mvt.getV13004_MIN_D()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV13004_MIN_D());// 月最小水汽压出现日期
                    }
                    if(mvt.getV12314_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12314_701());//平均草面（雪面）温度
                    }
                    if(mvt.getV12315_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12315_701());//平均最高草面（雪面）温度
                    }
                    if(mvt.getV12316_701()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12316_701());//平均最低草面（雪面）温度
                    }
                    if(mvt.getV12315()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12315());//极端最高草面（雪面）温度
                    }
                    if(mvt.getV12315_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12315_040());//极端最高草面（雪面）温度出现日数 
                    }
                    if(mvt.getV12315_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV12315_060_CHAR());//极端最高草面（雪面）温度出现日 
                    }
                    if(mvt.getV12316()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV12316());//极端最低草面（雪面）温度
                    }
                    if(mvt.getV12316_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV12316_040());//极端最低草面（雪面）温度出现日数 
                    }
                    if(mvt.getV12316_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV12316_060_CHAR());//极端最低草面（雪面）温度出现日 
                    }
                    if(mvt.getV20334()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV20334());//最大冻土深度
                    }
                    if(mvt.getV20334_040()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20334_040());//最大冻土深度出现日数 
                    }
                    if(mvt.getV20334_060_CHAR()==null){
                        psMonth.setString(ii++, "999999");
                    }else{
                        psMonth.setString(ii++, mvt.getV20334_060_CHAR());//最大冻土深度出现日 
                    }
                    if(mvt.getV14032()==null){
                        psMonth.setFloat(ii++, 999999);
                    }else{
                        psMonth.setFloat(ii++, mvt.getV14032());//月日照总时数
                    }
                    if(mvt.getSunTime()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getSunTime());//月日出总时长
                    }
                    if(mvt.getV14033()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV14033());//月日照百分率 
                    }
                    if(mvt.getV20302_171()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20302_171());//日照百分率≥60%日数
                    }
                    if(mvt.getV20302_172()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV20302_172());//日照百分率≤20%日数
                    }
                    if(mvt.getV14311()==null){
                        psMonth.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psMonth.setBigDecimal(ii++, mvt.getV14311());//月太阳总辐射(MJ/m2)
                    }
                    if(mvt.getV14311_05()==null){
                          psMonth.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psMonth.setBigDecimal(ii++, mvt.getV14311_05());// 月最大日总辐射辐照度(W/m2)
                    }
                    if(mvt.getV14021_05_052()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV14021_05_052());//月最大日总辐射辐照度时间
                    }
                    if(mvt.getV14312()==null){
                          psMonth.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psMonth.setBigDecimal(ii++, mvt.getV14312());//月净全辐射(MJ/m2)
                    }
                    if(mvt.getV14312_05()==null){
                          psMonth.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psMonth.setBigDecimal(ii++, mvt.getV14312_05());//月最大日净全辐射辐照度(W/m2)
                    }
                    if(mvt.getV14312_05_052()==null){
                        psMonth.setInt(ii++, 999999);
                    }else{
                        psMonth.setInt(ii++, mvt.getV14312_05_052());//月最大日净全辐射辐照度时间
                    }
                    if(mvt.getV14314_avg()==null){
                          psMonth.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psMonth.setBigDecimal(ii++, mvt.getV14314_avg());// 月日散射辐射曝辐量平均
                    }
                    if(mvt.getV14314()==null){
                          psMonth.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psMonth.setBigDecimal(ii++, mvt.getV14314());// 月日散射辐射曝辐量合计
                    }
                    if(mvt.getV14314_05()==null){
                          psMonth.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psMonth.setBigDecimal(ii++, mvt.getV14314_05());//最大日散射辐射辐照度
                    }
                    if(mvt.getV14322_avg()==null){
                          psMonth.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psMonth.setBigDecimal(ii++, mvt.getV14322_avg());//月日直接辐射曝辐量平均
                    }
                    if(mvt.getV14322()==null){
                          psMonth.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psMonth.setBigDecimal(ii++, mvt.getV14322());//月日直接辐射曝辐量合计
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("月值插入列号：" + ii + "有误");
                }
                psMonth.addBatch();
            }


            for (DayValueTab dayValue : dayList) {
                int ii = 1;
                try {
                    psDay.setString(ii++, dayValue.getD_RECORD_ID());// 记录标识 ( 系统自动生成的流水号 )
                    psDay.setString(ii++, dayValue.getD_DATA_ID());// 资料标识 ( 资料的4级编码 )

                    if(dayValue.getD_IYMDHM()==null){
                        psDay.setString(ii++, currentTime);
                    }else{
                        psDay.setString(ii++, dayValue.getD_IYMDHM());// 入库时间 ( 插表时的系统时间 )
                    }
                    if(dayValue.getD_RYMDHM()==null){
                        psDay.setString(ii++, currentTime);
                    }else{
                        psDay.setString(ii++, dayValue.getD_RYMDHM());// 收到时间 ( DPC消息生成时间 )
                    }
                    if(dayValue.getD_UPDATE_TIME()==null){
                        psDay.setString(ii++, currentTime);
                    }else{
                        psDay.setString(ii++, dayValue.getD_UPDATE_TIME());// 更新时间 ( 根据CCx对记录更新时的系统时间 )
                    }
                        psDay.setString(ii++, dayValue.getD_DATETIME());// 资料时间 ( 由V04001、V04002、V04003组成 )
                    if(dayValue.getV_BBB()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV_BBB());// 更正报标志
                    }
                    if(dayValue.getV01301()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV01301());// 区站号(字符)
                    }
                    if(dayValue.getV01300()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV01300());// 区站号(数字)
                    }
                    if(dayValue.getV05001()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV05001());// 纬度 ( 单位：度 )
                    }
                    if(dayValue.getV06001()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV06001());// 经度 ( 单位：度 )
                    }
                    if(dayValue.getV07001()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV07001());// 测站高度 ( 单位：米 )
                    }
                    if(dayValue.getV07031()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV07031());// 气压传感器海拔高度 ( 单位：米 )
                    }
                    if(dayValue.getV02301()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV02301());// 台站级别 ( 代码表 )
                    }
                    if(dayValue.getV_ACODE()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV_ACODE());// 中国行政区划代码 ( 代码表 )
                    }
                    if(dayValue.getV04001()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV04001());// 年
                    }
                    if(dayValue.getV04002()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV04002());// 月
                    }
                    if(dayValue.getV04003()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV04003());// 日
                    }
                    if(dayValue.getV10004_701()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV10004_701());// 日平均本站气压 ( 单位：百帕 )
                    }
                    if(dayValue.getV10301()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV10301());// 日最高本站气压 ( 单位：百帕 )
                    }
                    if(dayValue.getV10301_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV10301_052());// 日最高本站气压出现时间 ( 格式：时分 )
                    }
                    if(dayValue.getV10302()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV10302());// 日最低本站气压 ( 单位：百帕 )
                    }
                    if(dayValue.getV10302_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV10302_052());// 日最低本站气压出现时间 ( 格式：时分 )
                    }
                    if(dayValue.getV10051_701()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV10051_701());// 日平均海平面气压 ( 单位：百帕 )
                    }
                    if(dayValue.getV12001_701()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12001_701());// 日平均气温 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12011()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12011());// 日最高气温 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12011_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV12011_052());// 日最高气温出现时间 ( 格式：时分 )
                    }
                    if(dayValue.getV12012()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12012());// 日最低气温 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12303_701()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12303_701());//日气温日较差（单位：摄氏度）
                    }
                    if(dayValue.getV12012_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV12012_052());// 日最低气温出现时间 ( 格式：时分 )
                    }
                    if(dayValue.getV13004_701()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV13004_701());// 日平均水汽压 ( 单位：百帕 )
                    }
                    if(dayValue.getV13004_MAX()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV13004_MAX());//日最大水汽压
                    }
                    if(dayValue.getV13004_MIN()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV13004_MIN());//日最小水气压
                    }
                    if(dayValue.getV13003_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV13003_701());// 日平均相对湿度 ( 单位：% )
                    }
                    if(dayValue.getV13007()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV13007());// 日最小相对湿度 ( 单位：% )
                    }
                    if(dayValue.getV13007_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV13007_052());// 日最小相对湿度出现时间 ( 格式：时分 )
                    }
                    if(dayValue.getHtem100()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getHtem100());//100cm深层地温
                    }
                    if(dayValue.getHtem200()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getHtem200());//200cm深层地温
                    }
                    if(dayValue.getHtem300()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getHtem300());//330cm深层地温
                    }
                    if(dayValue.getObversetype()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getObversetype());// 观测方式和测站类别
                    }
                    if(dayValue.getObsercecode()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getObsercecode());// 观测项目标识
                    }
                    if(dayValue.getQualitycode()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getQualitycode());// 质量控制指示码
                    }
                    if(dayValue.getArcaninehight()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getArcaninehight());// 风速器距地高度
                    }
                    if(dayValue.getRain1()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getRain1());//自记1小时降水
                    }
                    if(dayValue.getRain10()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getRain10());//自记10分钟最大降水
                    }
                    if(dayValue.getWetherSymbol()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getWetherSymbol());//天气符号
                    }
                    if(dayValue.getWetherStartTime()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getWetherStartTime());//天气开始时间-时+分
                    }
                    if(dayValue.getWetherEndTime()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getWetherEndTime());//天气结束时间-时+分
                    }
                    if(dayValue.getGlazeNSDia()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getGlazeNSDia());//雨凇南北直径
                    }
                    if(dayValue.getGlazeWEDia()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getGlazeWEDia());//雨凇东西直径mm
                    }
                    if(dayValue.getGlazeNSHight()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getGlazeNSHight());//雨凇南北厚度
                    }
                    if(dayValue.getGlazeWEHight()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getGlazeWEHight());//雨凇东西厚度mm
                    }
                    if(dayValue.getGlazeNSWeight()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getGlazeNSWeight());//雨凇东西重量g/m
                    }
                    if(dayValue.getGlazeWEWeight()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getGlazeWEWeight());//雨凇东西重量
                    }
                    if(dayValue.getRimeNSDia()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getRimeNSDia());//雾凇南北直径
                    }
                    if(dayValue.getRimeWEDia()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getRimeWEDia());//雾凇东西直径
                    }
                    if(dayValue.getRimeNSHight()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getRimeNSHight());//雾凇南北厚度
                    }
                    if(dayValue.getRimeWEHight()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getRimeWEHight());//雾凇东西厚度
                    }
                    if(dayValue.getRimeNSWeight()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getRimeNSWeight());//雾凇东西重量
                    }
                    if(dayValue.getRimeWEWeight()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getRimeWEWeight());//雾凇东西重量
                    }
                    if(dayValue.getV20010_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20010_701());// 日平均总云量 ( 单位：% )
                    }
                    if(dayValue.getV20051_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20051_701());// 日平均低云量 ( 单位：% )
                    }
                    if(dayValue.getV20059()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV20059());// 日最小水平能见度 ( 单位：米 )
                    }
                    if(dayValue.getV20059_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20059_052());// 日最小水平能见度出现时间 ( 格式：时分 )
                    }
                    if(dayValue.getV13302_01()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV13302_01());// 日小时最大降水量 ( 单位：毫米? )
                    }
                    if(dayValue.getV13302_01_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV13302_01_052());// 日小时最大降水量出现时间 ( 格式：时分 )
                    }
                    if(dayValue.getV13308()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV13308());// 20-08时雨量筒观测降水量 ( 单位：毫米 )
                    }
                    if(dayValue.getV13309()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV13309());// 08-20时雨量筒观测降水量 ( 单位：毫米 )
                    }
                    if(dayValue.getV13305()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV13305());// 20-20时降水量 ( 单位：毫米 )
                    }
                    if(dayValue.getV13306()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV13306());// 08-08时降水量 ( 单位：毫米 )
                    }
                    if(dayValue.getV13032()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV13032());// 日蒸发量（小型） ( 单位：毫米 )
                    }
                    if(dayValue.getV13033()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV13033());// 日蒸发量（大型） ( 单位：毫米 )
                    }
                    if(dayValue.getV13013()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV13013());// 积雪深度 ( 单位：厘米 )
                    }
                    if(dayValue.getV13330()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV13330());// 雪压 ( 单位：g/cm2 )
                    }
                    if(dayValue.getV20305()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20305());// 电线积冰-现象 ( 代码表 )
                    }
                    if(dayValue.getV20326_NS()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV20326_NS());// 电线积冰-南北方向直径 ( 单位：毫米 )
                    }
                    if(dayValue.getV20306_NS()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV20306_NS());// 电线积冰-南北方向厚度 ( 单位：毫米 )
                    }
                    if(dayValue.getV20307_NS()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV20307_NS());// 电线积冰-南北方向重量 ( 单位：克 )
                    }
                    if(dayValue.getV20326_WE()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV20326_WE());// 电线积冰-东西方向直径 ( 单位：毫米 )
                    }
                    if(dayValue.getV20306_WE()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV20306_WE());// 电线积冰-东西方向厚度 ( 单位：毫米 )
                    }
                    if(dayValue.getV20307_WE()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV20307_WE());// 电线积冰-东西方向重量 ( 单位：克 )
                    }
                    if(dayValue.getV12001()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12001());// 电线积冰－温度 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV11001()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV11001());// 电线积冰－风向 ( 单位：度 )
                    }
                    if(dayValue.getV11002()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV11002());// 电线积冰－风速 ( 单位：米/秒 )
                    }
                    if(dayValue.getV11290_CHAR()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV11290_CHAR());// 逐小时2分钟平均风向 ( 格式：字符串? )
                    }
                    if(dayValue.getV11291_701()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV11291_701());// 日平均2分钟风速 ( 单位：米/秒 )
                    }
                    if(dayValue.getV11293_701()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV11293_701());// 日平均10分钟风速 ( 单位：米/秒 )
                    }
                    if(dayValue.getV11296()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV11296());// 日最大风速的风向 ( 单位：度 )
                    }
                    if(dayValue.getV11042()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV11042());// 日最大风速 ( 单位：米/秒 )
                    }
                    if(dayValue.getV11042_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV11042_052());// 日最大风速出现时间 ( 格式：时分 )
                    }
                    if(dayValue.getV11211()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV11211());// 日极大风速的风向 ( 单位：度 )
                    }
                    if(dayValue.getV11046()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV11046());// 日极大风速 ( 单位：米/秒 )
                    }
                    if(dayValue.getV11046_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV11046_052());// 日极大风速出现时间 ( 格式：时分 )
                    }
                    if(dayValue.getV12120_701()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12120_701());// 日平均地面温度 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12311()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12311());// 日最高地面温度 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12311_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV12311_052());// 日最高地面温度出现时间  ( 格式：时分 )
                    }
                    if(dayValue.getV12121()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12121());// 日最低地面温度 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12121_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV12121_052());// 日最低地面温度出现时间 ( 格式：时分 )
                    }
                    if(dayValue.getV12030_701_005()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12030_701_005());// 日平均5cm地温 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12030_701_010()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12030_701_010());// 日平均10cm地温 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12030_701_015()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12030_701_015());// 日平均15cm地温 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12030_701_020()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12030_701_020());// 日平均20cm地温 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12030_701_040()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12030_701_040());// 日平均40cm地温 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12030_701_080()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12030_701_080());// 日平均80cm地温 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12030_701_160()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12030_701_160());// 日平均160cm地温 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12030_701_320()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12030_701_320());// 日平均320cm地温 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV20331()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV20331());//日最大冻土深度
                    }
                    if(dayValue.getV20330_01()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV20330_01());// 第一冻土层上界值 ( 单位：厘米 )
                    }
                    if(dayValue.getV20331_01()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV20331_01());// 第一冻土层下界值 ( 单位：厘米 )
                    }
                    if(dayValue.getV20330_02()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV20330_02());// 第二冻土层上界值 ( 单位：厘米 )
                    }
                    if(dayValue.getV20331_02()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV20331_02());// 第二冻土层下界值 ( 单位：厘米 )
                    }
                    if(dayValue.getV12003_avg()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12003_avg());//日平均露点温度
                    }
                    if(dayValue.getBalltemp_avg()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getBalltemp_avg());//日平均湿球温度
                    }
                    if(dayValue.getBalltemp_avg_24()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getBalltemp_avg_24());//日平均湿球温度(24小时)
                    }
                    if(dayValue.getV14032()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV14032());// 日总日照时数 ( 单位：小时 )
                    }
                    if(dayValue.getV14033()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV14033());//日照频率
                    }
                    if(dayValue.getV20411()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20411());// 日出时间 ( 格式：时分 )
                    }
                    if(dayValue.getV20412()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20412());// 日落时间 ( 格式：时分 )
                    }
                    if(dayValue.getSunTime()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getSunTime());//日出时长
                    }
                    if(dayValue.getV12314_701()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12314_701());// 日平均草面（雪面）温度 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12315()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12315());// 日草面（雪面）最高温度 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12315_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV12315_052());// 日草面（雪面）最高温度出现时间 ( 格式：时分 )
                    }
                    if(dayValue.getV12316()==null){
                        psDay.setFloat(ii++, 999999);
                    }else{
                        psDay.setFloat(ii++, dayValue.getV12316());// 日草面（雪面）最低温度 ( 单位：摄氏度 )
                    }
                    if(dayValue.getV12316_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV12316_052());// 日草面（雪面）最低温度出现时间 ( 格式：时分 )
                    }
                    if(dayValue.getV20062()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20062());// 地面状态（0-31数字） ( 见代码表020062 )
                    }
                    if(dayValue.getV20302_060()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_060());// 雨 ( 代码表20302(下同) )
                    }
                    if(dayValue.getV20302_060_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_060_052());// 雨出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_070()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_070());// 雪 ( 代码表 )
                    }
                    if(dayValue.getV20302_070_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_070_052());// 雪出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_089()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_089());// 冰雹 ( 代码表 )
                    }
                    if(dayValue.getV20302_089_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_089_052());// 冰雹出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_042()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_042());// 雾 ( 代码表 )
                    }
                    if(dayValue.getV20302_042_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_042_052());// 雾出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_010()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_010());// 轻雾 ( 代码表 )
                    }
                    if(dayValue.getV20302_001()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_001());// 露 ( 代码表 )
                    }
                    if(dayValue.getV20302_002()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_002());// 霜 ( 代码表 )
                    }
                    if(dayValue.getV20302_056()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_056());// 雨凇 ( 代码表 )
                    }
                    if(dayValue.getV20302_056_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_056_052());// 雨凇出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_048()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_048());// 雾凇 ( 代码表 )
                    }
                    if(dayValue.getV20302_048_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_048_052());// 雾凇出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_038()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_038());// 吹雪 ( 代码表 )
                    }
                    if(dayValue.getV20302_038_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_038_052());// 吹雪出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_039()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_039());// 雪暴 ( 代码表 )
                    }
                    if(dayValue.getV20302_039_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_039_052());// 雪暴出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_019()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_019());// 龙卷风 ( 代码表 )
                    }
                    if(dayValue.getV20302_019_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_019_052());// 龙卷风出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_016()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_016());// 积雪 ( 代码表 )
                    }
                    if(dayValue.getV20302_003()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_003());// 结冰 ( 代码表 )
                    }
                    if(dayValue.getV20302_031()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_031());// 沙尘暴 ( 代码表 )
                    }
                    if(dayValue.getV20302_031_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_031_052());// 沙尘暴出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_007()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_007());// 扬沙 ( 代码表 )
                    }
                    if(dayValue.getV20302_007_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_007_052());// 扬沙出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_006()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_006());// 浮尘 ( 代码表 )
                    }
                    if(dayValue.getV20302_006_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_006_052());// 浮尘出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_004()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_004());// 烟 ( 代码表 )
                    }
                    if(dayValue.getV20302_005()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_005());// 霾 ( 代码表 )
                    }
                    if(dayValue.getV20302_008()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_008());// 尘卷风 ( 代码表 )
                    }
                    if(dayValue.getV20302_076()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_076());// 冰针 ( 代码表 )
                    }
                    if(dayValue.getV20302_017()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_017());// 雷暴 ( 代码表 )
                    }
                    if(dayValue.getV20302_017_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_017_052());// 雷暴出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_013()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_013());// 闪电 ( 代码表 )
                    }
                    if(dayValue.getV20302_014()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_014());// 极光 ( 代码表 )
                    }
                    if(dayValue.getV20302_014_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_014_052());// 极光出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_015()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_015());// 大风 ( 代码表 )
                    }
                    if(dayValue.getV20302_015_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_015_052());// 大风出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20302_018()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV20302_018());// 飑 ( 代码表 )
                    }
                    if(dayValue.getV20302_018_052()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20302_018_052());// 飑出现时间 ( 格式：字符串 )
                    }
                    if(dayValue.getV20303()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20303());// 天气现象摘要 ( 字符 )
                    }
                    if(dayValue.getV20304()==null){
                        psDay.setString(ii++, "999999");
                    }else{
                        psDay.setString(ii++, dayValue.getV20304());// 天气现象记录 ( 字符 )
                    }
                    if(dayValue.getV14311()==null){
                        psDay.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psDay.setBigDecimal(ii++, dayValue.getV14311());//日太阳总辐射(MJ/m2)
                    }
                    if(dayValue.getV14311_05()==null){
                        psDay.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psDay.setBigDecimal(ii++, dayValue.getV14311_05());//日最大总辐射辐照度(W/m2)
                    }
                    if(dayValue.getV14021_05_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV14021_05_052());//日最大总辐射辐照度时间
                    }
                    if(dayValue.getV14312()==null){
                        psDay.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psDay.setBigDecimal(ii++, dayValue.getV14312());//日净全辐射(MJ/m2)
                    }
                    if(dayValue.getV14312_05()==null){
                        psDay.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psDay.setBigDecimal(ii++, dayValue.getV14312_05());//日最大净全辐射辐照度(W/m2)
                    }
                    if(dayValue.getV14312_05_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getV14312_05_052());//日最大净全辐射辐照度时间
                    }
                    if(dayValue.getV14314()==null){
                        psDay.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psDay.setBigDecimal(ii++, dayValue.getV14314());//日散射辐射曝辐量(MJ/m2)
                    }
                    if(dayValue.getV14308()==null){
                        psDay.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psDay.setBigDecimal(ii++, dayValue.getV14308());//日净全辐射曝辐量(MJ/m2)
                    }
                    if(dayValue.getV14313()==null){
                        psDay.setBigDecimal(ii++, new BigDecimal(999999));
                    }else{
                        psDay.setBigDecimal(ii++, dayValue.getV14313());//日直接辐射曝辐量(MJ/m2)
                    }
                    if(dayValue.getQ10004_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ10004_701());// 日平均本站气压质控码 ( 代码表 )
                    }
                    if(dayValue.getQ10301()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ10301());// 日最高本站气压质控码 ( 代码表 )
                    }
                    if(dayValue.getQ10301_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ10301_052());// 日最高本站气压出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ10302()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ10302());// 日最低本站气压质控码 ( 代码表 )
                    }
                    if(dayValue.getQ10302_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ10302_052());// 日最低本站气压出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ10051_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ10051_701());// 日平均海平面气压质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12001_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12001_701());// 日平均气温质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12011()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12011());// 日最高气温质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12011_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12011_052());// 日最高气温出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12012()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12012());// 日最低气温质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12012_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12012_052());// 日最低气温出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13004_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13004_701());// 日平均水汽压质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13003_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13003_701());// 日平均相对湿度质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13007()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13007());// 日最小相对湿度质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13007_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13007_052());// 日最小相对湿度出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20010_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20010_701());// 日平均总云量质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20051_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20051_701());// 日平均低云量质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20059()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20059());// 日最小水平能见度质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20059_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20059_052());// 日最小水平能见度出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13302_01()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13302_01());// 日小时最大降水量质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13302_01_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13302_01_052());// 日小时最大降水量出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13308()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13308());// 20-08时雨量筒观测降水量质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13309()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13309());// 08-20时雨量筒观测降水量质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13305()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13305());// 20-20时降水量质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13306()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13306());// 08-08时降水量质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13032()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13032());// 日蒸发量（小型）质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13033()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13033());// 日蒸发量（大型）质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13013()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13013());// 积雪深度质控码 ( 代码表 )
                    }
                    if(dayValue.getQ13330()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ13330());// 雪压质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20305()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20305());// 电线积冰-现象质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20326_NS()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20326_NS());// 电线积冰-南北方向直径质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20306_NS()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20306_NS());// 电线积冰-南北方向厚度质控码( 代码表 )
                    }
                    if(dayValue.getQ20307_NS()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20307_NS());// 电线积冰-南北方向重量质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20326_WE()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20326_WE());// 电线积冰-东西方向直径质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20306_WE()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20306_WE());// 电线积冰-东西方向厚度质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20307_WE()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20307_WE());// 电线积冰-东西方向重量质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12001()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12001());// 电线积冰－温度质控码 ( 代码表 )
                    }
                    if(dayValue.getQ11001()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ11001());// 电线积冰－风向质控码 ( 代码表 )
                    }
                    if(dayValue.getQ11002()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ11002());// 电线积冰－风速质控码 ( 代码表 )
                    }
                    if(dayValue.getQ11290_CHAR()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ11290_CHAR());// 日平均2分钟风向质控码 ( 代码表 )
                    }
                    if(dayValue.getQ11291_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ11291_701());// 日平均2分钟风速质控码 ( 代码表 )
                    }
                    if(dayValue.getQ11293_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ11293_701());// 日平均10分钟风速质控码 ( 代码表 )
                    }
                    if(dayValue.getQ11296()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ11296());// 日最大风速的风向质控码 ( 代码表 )
                    }
                    if(dayValue.getQ11042()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ11042());// 日最大风速质控码 ( 代码表 )
                    }
                    if(dayValue.getQ11042_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ11042_052());// 日最大风速出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ11211()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ11211());// 日极大风速的风向质控码 ( 代码表 )
                    }
                    if(dayValue.getQ11046()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ11046());// 日极大风速质控码 ( 代码表 )
                    }
                    if(dayValue.getQ11046_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ11046_052());// 日极大风速出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12120_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12120_701());// 日平均地面温度质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12311()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12311());// 日最高地面温度质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12311_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12311_052());// 日最高地面温度出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12121()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12121());// 日最低地面温度质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12121_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12121_052());// 日最低地面温度出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12030_701_005()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12030_701_005());// 日平均5cm地温质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12030_701_010()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12030_701_010());// 日平均10cm地温质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12030_701_015()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12030_701_015());// 日平均15cm地温质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12030_701_020()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12030_701_020());// 日平均20cm地温质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12030_701_040()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12030_701_040());// 日平均40cm地温质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12030_701_080()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12030_701_080());// 日平均80cm地温质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12030_701_160()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12030_701_160());// 日平均160cm地温质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12030_701_320()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12030_701_320());// 日平均320cm地温质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20330_01()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20330_01());// 第一冻土层上界值质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20331_01()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20331_01());// 第一冻土层下界值质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20330_02()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20330_02());// 第二冻土层上界值质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20331_02()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20331_02());// 第二冻土层下界值质控码 ( 代码表 )
                    }
                    if(dayValue.getQ14032()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ14032());// 日总日照时数质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20411()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20411());// 日出时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20412()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20412());// 日落时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12314_701()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12314_701());// 日平均草面（雪面）温度质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12315()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12315());// 日草面（雪面）最高温度质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12315_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12315_052());// 日草面（雪面）最高温度出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12316()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12316());// 日草面（雪面）最低温度质控码 ( 代码表 )
                    }
                    if(dayValue.getQ12316_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ12316_052());// 日草面（雪面）最低温度出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20062()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20062());// 地面状态质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_060()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_060());// 雨质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_060_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_060_052());// 雨出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_070()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_070());// 雪质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_070_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_070_052());// 雪出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_089()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_089());// 冰雹质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_089_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_089_052());// 冰雹出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_042()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_042());// 雾质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_042_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_042_052());// 雾出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_010()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_010());// 轻雾质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_001()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_001());// 露质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_002()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_002());// 霜质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_056()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_056());// 雨凇质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_056_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_056_052());// 雨凇出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_048()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_048());// 雾凇质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_048_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_048_052());// 雾凇出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_038()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_038());// 吹雪质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_038_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_038_052());// 吹雪出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_039()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_039());// 雪暴质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_039_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_039_052());// 雪暴出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_019()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_019());// 龙卷风质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_019_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_019_052());// 龙卷风出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_016()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_016());// 积雪质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_003()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_003());// 结冰质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_031()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_031());// 沙尘暴质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_031_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_031_052());// 沙尘暴出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_007()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_007());// 扬沙质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_007_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_007_052());// 扬沙出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_006()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_006());// 浮尘质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_006_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_006_052());// 浮尘出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_004()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_004());// 烟质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_005()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_005());// 霾质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_008()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_008());// 尘卷风质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_076()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_076());// 冰针质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_017()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_017());// 雷暴质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_017_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_017_052());//雷暴出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_013()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_013());// 闪电质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_014()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_014());// 极光质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_014_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_014_052());// 极光出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_015()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_015());// 大风质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_015_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_015_052());// 大风出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_018()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_018());// 飑质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20302_018_052()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20302_018_052());// 飑出现时间质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20303()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20303());// 天气现象摘要质控码 ( 代码表 )
                    }
                    if(dayValue.getQ20304()==null){
                        psDay.setInt(ii++, 999999);
                    }else{
                        psDay.setInt(ii++, dayValue.getQ20304());// 天气现象记录质控码 ( 代码表 )
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("日值插入列号：" + ii + "有误");
                }
                psDay.addBatch();
            }
            int index=0;
            for (HourValueTab hourValue : hourList) {
                int ii = 1;
                index+=1;
                try {
                    psHour.setString(ii++, hourValue.getD_MAIN_ID());//记录标识 ( 系统自动生成的流水号 )
                    psHour.setString(ii++, hourValue.getD_DATA_ID());//资料标识 ( 资料的4级编码 )
                    if(hourValue.getD_IYMDHM()==null){
                        psHour.setString(ii++, currentTime);//入库时间 ( 插表时的系统时间 )
                    }else{
                        psHour.setString(ii++, hourValue.getD_IYMDHM());//入库时间 ( 插表时的系统时间 )
                    }
                    if(hourValue.getD_RYMDHM()==null) {
                        psHour.setString(ii++, currentTime);//收到时间 ( DPC消息生成时间 )
                    }else{
                        psHour.setString(ii++, hourValue.getD_RYMDHM());//收到时间 ( DPC消息生成时间 )
                    }
                    if(hourValue.getD_RYMDHM()==null) {
                        psHour.setString(ii++, currentTime);//更新时间 ( 根据CCx对记录更新时的系统时间 )
                    }else{
                        psHour.setString(ii++, hourValue.getD_UPDATE_TIME());//更新时间 ( 根据CCx对记录更新时的系统时间 )
                    }
                    psHour.setString(ii++, hourValue.getD_DATETIME());//资料时间 ( 由V04001、V04002、V04003、V04004 )
                    psHour.setString(ii++, hourValue.getV_BBB());//更正标志 ( 组成 )
                    psHour.setString(ii++, hourValue.getV01301());//区站号(字符)
                    if(hourValue.getV01300()==null){
                        psHour.setString(ii++, "999999");
                    }else{
                        psHour.setString(ii++, hourValue.getV01300());//区站号(数字)
                    }
                    if(hourValue.getV05001()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV05001());//纬度
                    }
                    if(hourValue.getV06001()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV06001());//经度 ( 单位：度 )
                    }
                    if(hourValue.getV07001()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV07001());//测站高度 ( 单位：度 )
                    }
                    if(hourValue.getV07031()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV07031());//气压传感器海拔高度 ( 单位：米 )
                    }
                    if(hourValue.getV07032_04()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV07032_04());//风速传感器距地面高度 ( 单位：米 )
                    }
                    if(hourValue.getV02001()==null){
                        psHour.setString(ii++, "999999");
                    }else{
                        psHour.setString(ii++, hourValue.getV02001());//测站类型 ( 代码表 )
                    }
                    if(hourValue.getV02301()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV02301());//台站级别 ( 代码表 )
                    }
                    if(hourValue.getV_ACODE()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV_ACODE());//中国行政区划代码 ( 代码表 )
                    }
                    if(hourValue.getV04001()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV04001());//资料观测年 ( 代码表 )
                    }
                    if(hourValue.getV04002()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV04002());//资料观测月
                    }
                    if(hourValue.getV04003()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV04003());//资料观测日
                    }
                    if(hourValue.getV04004()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV04004());//资料观测时
                    }
                    if(hourValue.getV10004()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV10004());//本站气压
                    }
                    if(hourValue.getV10051()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV10051());//海平面气压 ( 单位：百帕 )
                    }
                    if(hourValue.getV10061()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV10061());//3小时变压 ( 单位：百帕 )
                    }
                    if(hourValue.getV10062()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV10062());//24小时变压 ( 单位：百帕 )
                    }
                    if(hourValue.getV10301()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV10301());//小时内最高本站气压 ( 单位：百帕 )
                    }
                    if(hourValue.getV10301_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV10301_052());//小时内最高本站气压出现时间 ( 格式：hhmm )
                    }
                    if(hourValue.getV10302()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV10302());//小时内最低本站气压 ( 单位：百帕  )
                    }
                    if(hourValue.getV10302_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV10302_052());//小时内最低本站气压出现时间 ( 格式：hhmm )
                    }
                    if(hourValue.getV12001()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12001());//气温 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12011()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12011());//小时内最高气温 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12011_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV12011_052());//小时内最高气温出现时间 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12012()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12012());//小时内最低气温 (单位：摄氏度  )
                    }
                    if(hourValue.getV12012_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV12012_052());//小时内最低气温出现时间 ( 格式：hhmm )
                    }
                    if(hourValue.getV12405()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12405());//24小时变温 (单位：摄氏度  )
                    }
                    if(hourValue.getV12016()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12016());//过去24小时最高气温 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12017()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12017());//过去24小时最低气温 ( 单位：摄氏度 )
                    }
                    if(hourValue.getBalltemp()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getBalltemp());//湿球温度
                    }
                    if(hourValue.getV12003()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12003());//露点温度 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV13003()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV13003());//相对湿度 (单位：% )
                    }
                    if(hourValue.getV13007()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV13007());//小时内最小相对湿度 ( 单位：% )
                    }
                    if(hourValue.getV13007_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV13007_052());//小时内最小相对湿度出现时间 (格式：hhmm)
                    }
                    if(hourValue.getV13004()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV13004());//水汽压 (单位：百帕 )
                    }
                    if(hourValue.getV13019()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV13019());//1小时降水量 ( 单位：毫米   )
                    }
                    if(hourValue.getV13020()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV13020());//过去3小时降水量 ( 单位：毫米 )
                    }
                    if(hourValue.getV13021()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV13021());//过去6小时降水量 ( 单位：毫米 )
                    }
                    if(hourValue.getV13022()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV13022());//过去12小时降水量 ( 单位：毫米 )
                    }
                    if(hourValue.getV13023()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV13023());//过去24小时降水量 ( 单位：毫米 )
                    }
                    if(hourValue.getV04080_04()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV04080_04());//人工加密观测降水量描述时间周期 ( 单位：毫米 )
                    }
                    if(hourValue.getV13011()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV13011());//人工加密观测降水量 ( 单位：小时 )
                    }
                    if(hourValue.getV13033()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV13033());//小时蒸发量 ( 单位：毫米 )
                    }
                    if(hourValue.getV11290()==null){
                        psHour.setString(ii++, "999999");//2分钟平均风向 ( 单位：度 )
                    }else{
                        psHour.setString(ii++, hourValue.getV11290());//2分钟平均风向 ( 单位：度 )
                    }

                    if(hourValue.getV11291()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV11291());//2分钟平均风速 ( 单位：米/秒  )
                    }
                    if(hourValue.getV11292()==null){
                        psHour.setString(ii++, "999999");//10分钟平均风向 ( 单位：度  )
                    }else{
                        psHour.setString(ii++, hourValue.getV11292());//10分钟平均风向 ( 单位：度  )
                    }

                    if(hourValue.getV11293()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV11293());//10分钟平均风速 (单位：米/秒)
                    }
                    if(hourValue.getV11296()==null){
                        psHour.setString(ii++, "999999");//小时内最大风速的风向 ( 单位：度 )
                    }else{
                        psHour.setString(ii++, hourValue.getV11296());//小时内最大风速的风向 ( 单位：度 )
                    }

                    if(hourValue.getV11042()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV11042());//小时内最大风速 (单位：米/秒)
                    }
                    if(hourValue.getV11042_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV11042_052());//小时内最大风速出现时间 ( 格式：hhmm )
                    }
                    if(hourValue.getV11201()==null){
                        psHour.setString(ii++, "999999");//瞬时风向 ( 单位：度 )
                    }else{
                        psHour.setString(ii++, hourValue.getV11201());//瞬时风向 ( 单位：度 )
                    }

                    if(hourValue.getV11202()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV11202());//瞬时风速 ( 单位：米/秒 )
                    }
                    if(hourValue.getV11211()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV11211());//小时内极大风速的风向 ( 单位：米/秒 )
                    }
                    if(hourValue.getV11046()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV11046());//小时内极大风速 ( 单位：度 )
                    }
                    if(hourValue.getV11046_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV11046_052());//小时内极大风速出现时间 (格式：hhmm  )
                    }
                    if(hourValue.getV11503_06()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV11503_06());//过去6小时极大瞬时风向 (单位：度  )
                    }
                    if(hourValue.getV11504_06()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV11504_06());//过去6小时极大瞬时风速 (单位：米/秒  )
                    }
                    if(hourValue.getV11503_12()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV11503_12());//过去12小时极大瞬时风向 ( 单位：度  )
                    }
                    if(hourValue.getV11504_12()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV11504_12());//过去12小时极大瞬时风速 ( 单位：米/秒)
                    }
                    if(hourValue.getV12120()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12120());//地面温度 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12311()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12311());//小时内最高地面温度 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12311_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV12311_052());//小时内最高地面温度出现时间 (  格式：hhmm)
                    }
                    if(hourValue.getV12121()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12121());//小时内最低地面温度 (单位：摄氏度  )
                    }
                    if(hourValue.getV12121_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV12121_052());//小时内最低地面温度出现时间 ( 格式：hhmm)
                    }
                    if(hourValue.getV12013()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12013());//过去12小时最低地面温度 (单位：摄氏度  )
                    }
                    if(hourValue.getV12030_005()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12030_005());//5cm地温 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12030_010()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12030_010());//10cm地温 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12030_015()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12030_015());//15cm地温 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12030_020()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12030_020());//20cm地温 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12030_040()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12030_040());//40cm地温 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12030_080()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12030_080());//80cm地温 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12030_160()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12030_160());//160cm地温 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12030_320()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12030_320());//320cm地温 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12314()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12314());//草面（雪面）温度 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12315()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12315());//小时内草面（雪面）最高温度 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12315_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV12315_052());//小时内草面（雪面）最高温度出现时间 ( 格式：hhmm )
                    }
                    if(hourValue.getV12316()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV12316());//小时内草面（雪面）最低温度 ( 单位：摄氏度 )
                    }
                    if(hourValue.getV12316_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV12316_052());//小时内草面（雪面）最低温度出现时间 ( 格式：hhmm  )
                    }
                    if(hourValue.getV20001_701_01()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV20001_701_01());//1分钟平均水平能见度 ( 单位：米 )
                    }
                    if(hourValue.getV20001_701_10()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV20001_701_10());//10分钟平均水平能见度 ( 单位：米 )
                    }
                    if(hourValue.getV20059()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV20059());//最小水平能见度 ( 单位：米 )
                    }
                    if(hourValue.getV20059_052()==null){
                        psHour.setString(ii++, "999999");
                    }else{
                        psHour.setString(ii++, hourValue.getV20059_052());//最小水平能见度出现时间 ( 格式：hhmm  )
                    }
                    if(hourValue.getV20001()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV20001());//水平能见度（人工） ( 单位：千米 )
                    }
                    if(hourValue.getVisibility2()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getVisibility2());//能见度级别
                    }
                    if(hourValue.getV20010()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20010());//总云量 (单位：%  )
                    }
                    if(hourValue.getV20051()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20051());//低云量 ( 单位：% )
                    }
                    if(hourValue.getV20011()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV20011());//低云或中云的云量 ( 单位：% )
                    }
                    if(hourValue.getV20013()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV20013());//低云或中云的云高 ( 单位：% )
                    }
                    if(hourValue.getV20350_01()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20350_01());//云状1 ( 单位：米 )
                    }
                    if(hourValue.getV20350_02()==null){
                        psHour.setString(ii++, "999999");//云状2 ( 代码表 )
                    }else{
                        psHour.setString(ii++, hourValue.getV20350_02());//云状2 ( 代码表 )
                    }

                    if(hourValue.getV20350_03()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20350_03());//云状3 ( 代码表 )
                    }
                    if(hourValue.getV20350_04()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20350_04());//云状4 ( 代码表 )
                    }
                    if(hourValue.getV20350_05()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20350_05());//云状5 ( 代码表 )
                    }
                    if(hourValue.getV20350_06()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20350_06());//云状6 ( 代码表 )
                    }
                    if(hourValue.getV20350_07()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20350_07());//云状7 ( 代码表 )
                    }
                    if(hourValue.getV20350_08()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20350_08());//云状8 ( 代码表 )
                    }
                    if(hourValue.getV20350_11()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20350_11());//低云状 ( 代码表 )
                    }
                    if(hourValue.getV20350_12()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20350_12());//中云状 ( 代码表 )
                    }
                    if(hourValue.getV20350_13()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20350_13());//高云状 ( 代码表 )
                    }
                    if(hourValue.getV20003()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20003());//现在天气 ( 代码表 )
                    }
                    if(hourValue.getV04080_05()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV04080_05());//过去天气描述时间周期 ( 代码表 )
                    }
                    if(hourValue.getV20004()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20004());//过去天气1 ( 单位:小时 )
                    }
                    if(hourValue.getV20005()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20005());//过去天气2 ( 代码表 )
                    }
                    if(hourValue.getV20062()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getV20062());//地面状态 ( 代码表 )
                    }
                    if(hourValue.getV13013()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV13013());//积雪深度 ( 代码表 )
                    }
                    if(hourValue.getV13330()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV13330());//雪压 ( 单位：厘米 )
                    }
                    if(hourValue.getV20330_01()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV20330_01());//冻土第1冻结层上限值 ( 单位：g/cm2 )
                    }
                    if(hourValue.getV20331_01()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV20331_01());//冻土第1冻结层下限值 ( 单位：厘米 )
                    }
                    if(hourValue.getV20330_02()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV20330_02());//冻土第2冻结层上限值 ( 单位：厘米 )
                    }
                    if(hourValue.getV20331_02()==null){
                        psHour.setFloat(ii++, 999999);
                    }else{
                        psHour.setFloat(ii++, hourValue.getV20331_02());//冻土第2冻结层下限值 ( 单位：厘米 )
                    }
                    if(hourValue.getV14320()==null){
                        psHour.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                    }else{
                        psHour.setBigDecimal(ii++, hourValue.getV14320());//总辐射曝辐量
                    }
                    if(hourValue.getV14308()==null){
                        psHour.setBigDecimal(ii++, BigDecimal.valueOf(999999));
                    }else{
                        psHour.setBigDecimal(ii++, hourValue.getV14308());//净辐射曝辐量
                    }
                    if(hourValue.getQ10004()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ10004());//本站气压质量标志 ( 单位：厘米 )
                    }
                    if(hourValue.getQ10051()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ10051());//海平面气压质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ10061()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ10061());//3小时变压质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ10062()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ10062());//24小时变压质质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ10301()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ10301());//小时内最高本站气压质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ10301_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ10301_052());//小时内最高本站气压出现时间质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ10302()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ10302());//小时内最低本站气压质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ10302_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ10302_052());//小时内最低本站气压出现时间质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12001()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12001());//气温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12011()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12011());//小时内最高气温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12011_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12011_052());//小时内最高气温出现时间质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12012()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12012());//小时内最低气温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12012_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12012_052());//小时内最低气温出现时间质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12405()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12405());//24小时变温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12016()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12016());//过去24小时最高气温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12017()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12017());//过去24小时最低气温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12003()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12003());//露点温度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13003()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13003());//相对湿度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13007()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13007());//小时内最小相对湿度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13007_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13007_052());//小时内最小相对湿度出现时间质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13004()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13004());//水汽压质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13019()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13019());//1小时降水量质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13020()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13020());//过去3小时降水量质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13021()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13021());//过去6小时降水量质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13022()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13022());//过去12小时降水量质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13023()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13023());//24小时降水量质 ( 代码表 )
                    }
                    if(hourValue.getQ04080_04()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ04080_04());//人工加密观测降水量描述时间周期质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13011()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13011());//人工加密观测降水量质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13033()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13033());//小时蒸发量质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11290()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11290());//2分钟平均风向质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11291()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11291());//2分钟平均风速质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11292()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11292());//10分钟平均风向质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11293()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11293());//10分钟平均风速质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11296()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11296());//小时内最大风速的风向质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11042()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11042());//小时内最大风速质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11042_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11042_052());//小时内最大风速出现时间质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11201()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11201());//瞬时风向质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11202()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11202());//瞬时风速质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11211()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11211());//小时内极大风速的风向质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11046()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11046());//小时内极大风速质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11046_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11046_052());//小时内极大风速出现时间质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11503_06()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11503_06());//过去6小时极大瞬时风向质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11504_06()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11504_06());//过去6小时极大瞬时风速质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11503_12()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11503_12());//过去12小时极大瞬时风向质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ11504_12()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ11504_12());//过去12小时极大瞬时风速质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12120()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12120());//地面温度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12311()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12311());//小时内最高地面温度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12311_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12311_052());//小时内最高地面温度出现时间质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12121()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12121());//小时内最低地面温度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12121_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12121_052());//小时内最低地面温度出现时间质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12013()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12013());//过去12小时最低地面温度 ( 代码表 )
                    }
                    if(hourValue.getQ12030_005()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12030_005());//5cm地温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12030_010()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12030_010());//10cm地温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12030_015()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12030_015());//15cm地温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12030_020()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12030_020());//20cm地温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12030_040()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12030_040());//40cm地温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12030_080()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12030_080());//80cm地温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12030_160()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12030_160());//160cm地温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12030_320()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12030_320());//320cm地温质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12314()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12314());//草面（雪面）温度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12315()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12315());//小时内草面（雪面）最高温度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12315_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12315_052());//小时内草面（雪面）最高温度出现时间质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12316()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12316());//小时内草面（雪面）最低温度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ12316_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ12316_052());//小时内草面（雪面）最低温度出现时间质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20001_701_01()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20001_701_01());//1分钟平均水平能见度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20001_701_10()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20001_701_10());//10分钟平均水平能见度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20059()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20059());//最小水平能见度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20059_052()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20059_052());//最小水平能见度出现时间质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20001()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20001());//水平能见度（人工）质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20010()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20010());//总云量质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20051()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20051());//低云量质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20011()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20011());//低云或中云的云量质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20013()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20013());//低云或中云的云高质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20350_01()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20350_01());//云状1质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20350_02()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20350_02());//云状2质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20350_03()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20350_03());//云状3质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20350_04()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20350_04());//云状4质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20350_05()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20350_05());//云状5质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20350_06()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20350_06());//云状6质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20350_07()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20350_07());//云状7质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20350_08()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20350_08());//云状8质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20350_11()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20350_11());//低云状质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20350_12()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20350_12());//中云状质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20350_13()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20350_13());//高云状质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20003()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20003());//现在天气质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ04080_05()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ04080_05());//过去天气描述时间周期质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20004()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20004());//过去天气1质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20005()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20005());//过去天气2质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20062()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20062());//地面状态质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13013()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13013());//积雪深度质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ13330()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ13330());//雪压质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20330_01()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20330_01());//冻土第1冻结层上限值质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20331_01()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20331_01());//冻土第1冻结层下限值质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20330_02()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20330_02());//冻土第2冻结层上限值质量标志 ( 代码表 )
                    }
                    if(hourValue.getQ20331_02()==null){
                        psHour.setInt(ii++, 999999);
                    }else{
                        psHour.setInt(ii++, hourValue.getQ20331_02());//冻土第2冻结层下限值质量标志 ( 代码表 )
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("小时值插入列号：" + ii + "有误");
                }
                psHour.addBatch();
                if (index % 1000 == 0) {
                    psHour.executeBatch();
                    connection.commit();
                    psHour.clearBatch();
                }
            }
            if(sunLightList!=null) {
                for (SunLightValueTab sunValue : sunLightList) {
                    int ii = 1;
                    try {
                        psSun.setString(ii++, sunValue.getD_RECORD_ID());// 记录标识 ( 系统自动生成的流水号 )
                        psSun.setString(ii++, sunValue.getD_DATA_ID());// 资料标识 ( 资料的4级编码 )
                        psSun.setDate(ii++, sunValue.getD_IYMDHM());// 入库时间 ( 插表时的系统时间 )
                        psSun.setDate(ii++, sunValue.getD_RYMDHM());// 收到时间 ( DPC消息生成时间 )
                        psSun.setDate(ii++, sunValue.getD_UPDATE_TIME());// 更新时间 ( 根据CCx对记录更新时的系统时间 )
                        psSun.setDate(ii++, sunValue.getD_DATETIME());// 资料时间 ( 由V04001、04002、V04003组成 )
                        psSun.setString(ii++, sunValue.getV_BBB());// 更正报标志 ( 报头行BBB )
                        psSun.setString(ii++, sunValue.getV01301());// 区站号(字符)
                        if (sunValue.getV01300() == null) {
                            psSun.setString(ii++, "999999");
                        } else {
                            psSun.setString(ii++, sunValue.getV01300());// 区站号(数字)
                        }
                        if (sunValue.getV05001() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV05001());// 纬度 ( 单位：度 )
                        }
                        if (sunValue.getV06001() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV06001());// 经度 ( 单位：度 )
                        }
                        if (sunValue.getV07001() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV07001());// 测站高度 ( 单位：米 )
                        }
                        if (sunValue.getV02301() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getV02301());// 台站级别 ( 代码表 )
                        }
                        if (sunValue.getV_ACODE() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getV_ACODE());// 中国行政区划代码 ( 代码表 )
                        }
                        if (sunValue.getV14332() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getV14332());// 日照时制方式 ( 代码表 )
                        }
                        if (sunValue.getV04001() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getV04001());// 资料观测年
                        }
                        if (sunValue.getV04002() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getV04002());// 资料观测月
                        }
                        if (sunValue.getV04003() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getV04003());// 资料观测日
                        }
                        if (sunValue.getV14032_001() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_001());// 00-01时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_002() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_002());// 01-02时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_003() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_003());// 02-03时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_004() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_004());// 03-04时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_005() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_005());// 04-05时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_006() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_006());// 05-06时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_007() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_007());// 06-07时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_008() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_008());// 07-08时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_009() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_009());// 08-09时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_010() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_010());// 09-10时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_011() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_011());// 10-11时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_012() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_012());// 11-12时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_013() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_013());// 12-13时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_014() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_014());// 13_14时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_015() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_015());// 14-15时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_016() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_016());// 15-16时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_017() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_017());// 16-17时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_018() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_018());// 17-18时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_019() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_019());// 18-19时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_020() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_020());// 19-20时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_021() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_021());// 20-21时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_022() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_022());// 21-22时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_023() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_023());// 22-23时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032_024() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032_024());// 23-24时日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getV14032() == null) {
                            psSun.setFloat(ii++, 999999);
                        } else {
                            psSun.setFloat(ii++, sunValue.getV14032());// 日日照时数 ( 单位：小时 )
                        }
                        if (sunValue.getQ14032_001() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_001());// 00-01时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_002() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_002());// 01-02时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_003() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_003());// 02-03时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_004() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_004());// 03-04时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_005() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_005());// 04-05时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_006() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_006());// 05-06时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_007() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_007());// 06-07时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_008() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_008());// 07-08时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_009() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_009());// 08-09时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_010() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_010());// 09-10时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_011() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_011());// 10-11时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_012() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_012());// 11-12时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_013() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_013());// 12-13时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_014() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_014());// 13_14时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_015() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_015());// 14-15时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_016() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_016());// 15-16时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_017() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_017());// 16-17时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_018() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_018());// 17-18时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_019() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_019());// 18-19时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_020() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_020());// 19-20时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_021() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_021());// 20-21时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_022() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_022());// 21-22时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_023() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_023());// 22-23时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032_024() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032_024());// 23-24时日照时数质控码 ( 代码表 )
                        }
                        if (sunValue.getQ14032() == null) {
                            psSun.setInt(ii++, 999999);
                        } else {
                            psSun.setInt(ii++, sunValue.getQ14032());// 日日照时数质控码 ( 代码表 )
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("日照值插入列号：" + ii + "有误");
                    }
                    psSun.addBatch();
                }
            }
            if(psRadi!=null){
                psRadi.executeBatch();
            }
            if(psMeadow!=null){
                psMeadow.executeBatch();
            }
            if(psMonth!=null){
                psMonth.executeBatch();
            }
            if(psHour!=null){
                psHour.executeBatch();
            }
            if(psDay!=null){
                psDay.executeBatch();
            }
            if(psHour!=null){
                psHour.executeBatch();
            }
            if(psSun!=null){
                psSun.executeBatch();
            }
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(psMeadow!=null){
                    psMeadow.clearBatch();
                    psMeadow.close();
                }
                if(psMonth!=null){
                    psMonth.clearBatch();
                    psMonth.close();
                }
                if (psDay != null) {
                    psDay.clearBatch();
                    psDay.close();
                }
                if(psHour!=null){
                    psHour.clearBatch();
                    psHour.close();
                }
                if(psSun!=null){
                    psSun.clearBatch();
                    psSun.close();
                }
                if(psRadi!=null){
                    psRadi.clearBatch();
                    psRadi.close();
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
