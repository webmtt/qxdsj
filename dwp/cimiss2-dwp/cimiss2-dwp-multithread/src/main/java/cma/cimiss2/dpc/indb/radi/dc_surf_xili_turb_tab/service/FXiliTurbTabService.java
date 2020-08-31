package cma.cimiss2.dpc.indb.radi.dc_surf_xili_turb_tab.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliTurbHcTab;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliTurbTab;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliTurbTlTab;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliWindenerHfsTab;
import cma.cimiss2.dpc.decoder.bean.upar.UparLightAdtdMulTab;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;


/**
 * @author ：YCK
 * @date ：Created in 2019/11/6 0006 10:20
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class FXiliTurbTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        FXiliTurbTabService.diQueues = diQueues;
    }

    static String type = StartConfig.sodCode();

    /**
     * 执行错误时，可能数据冲突，分批执行
     * @param ps
     * @param cts_code
     */
    public static void excuteByDataHcSingle(PreparedStatement ps, List<SurfXiliTurbHcTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<SurfXiliTurbHcTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (SurfXiliTurbHcTab surfXiliTurbHcTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(surfXiliTurbHcTab);
                try {
                    ps.setString(ii++, surfXiliTurbHcTab.getdRecordId());

                    ps.setString(ii++, StartConfig.ctsCode("R008_hc_cts_code"));//四级编码

                    ps.setString(ii++, surfXiliTurbHcTab.getdDatetime());//资料时间

                    ps.setString(ii++, surfXiliTurbHcTab.getV01301());

                    ps.setString(ii++, surfXiliTurbHcTab.getV04001());

                    ps.setString(ii++, surfXiliTurbHcTab.getV04002());

                    ps.setString(ii++, surfXiliTurbHcTab.getV04003());

                    ps.setString(ii++, surfXiliTurbHcTab.getV04004());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getV06001());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getV05001());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getV07001());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getHeithThreeWind());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getAngleThreeWind());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getHeithRedH2oco2());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getV07031());

                    ps.setString(ii++, surfXiliTurbHcTab.getHourmin());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getxV11202());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getyV11202());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getzV11202());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getCo2Density());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getVaporDensity());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getVtou());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getFlutrm());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getV10004());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getVtouWindtem());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getRedH2oco2Vue());

                    ps.setBigDecimal(ii++, surfXiliTurbHcTab.getRedH2oco2Agc());

                    ps.addBatch();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("列号：" + ii + "有误");
                }
                if(size>4) {
                    if (middle != 0 && indexSingle % middle == 0) {
                        try {
                            ps.executeBatch();
                            connection.commit();
                            ps.clearBatch();
                        } catch (Exception e) {
                            ps.clearBatch();
                            excuteByDataHcSingle(ps, list1, cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=surfXiliTurbHcTab.getV01301()+" "+surfXiliTurbHcTab.getdDatetime();
                        ps.executeBatch();
                        ps.clearBatch();
                    }catch (Exception e){
                        ps.clearBatch();
                        System.out.println("舍弃重复录入数据！"+data);
                    }
                    list1.clear();
                }
            }
            connection.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 执行错误时，可能数据冲突，分批执行
     * @param ps
     * @param cts_code
     */
    public static void excuteByDataTlSingle(PreparedStatement ps, List<SurfXiliTurbTlTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<SurfXiliTurbTlTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (SurfXiliTurbTlTab surfXiliTurbTlTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(surfXiliTurbTlTab);
                try {
                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getdRecordId());

                    ps.setString(ii++, StartConfig.ctsCode("R008_tl_cts_code"));//四级编码

                    ps.setString(ii++, surfXiliTurbTlTab.getdDatetime());//资料时间

                    ps.setString(ii++, surfXiliTurbTlTab.getV01301());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV04001());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV04002());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV06001());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV05001());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV07001());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getHeithThreeWind());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAngleThreeWind());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getHeithRedH2oco2());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV07031());

                    ps.setString(ii++, surfXiliTurbTlTab.getV02320());

                    ps.setString(ii++, surfXiliTurbTlTab.getVersion());

                    ps.setString(ii++, surfXiliTurbTlTab.getThreeWindMode());

                    ps.setString(ii++, surfXiliTurbTlTab.getAnalyzerModelH20co2());

                    ps.setString(ii++, surfXiliTurbTlTab.getQ48008());

                    ps.setString(ii++, surfXiliTurbTlTab.getHeighVegetation());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getCo2Wpl());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getLhfWpl());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getVtouShf());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getMomflux());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getFriV11202());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getNonco2Wpl());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getNonlhfWpl());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getMoiCo2lhfWpl());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getMoiCo2shfWpl());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getMoiLhf());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getMoiShf());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzV11202Vari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzUxVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzUyVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzV11202Co2densityVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzV11202VaporVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzV11202VtouVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUxV11202Vari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUxUyVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUxV11202Co2densityVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUxV11202VaporVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUxV11202VtouVar());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUyV11202Vari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUyV11202Co2densityVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUyV11202VaporVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUyV11202VtouVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getCo2densityVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getVaporDensityVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getVtouVari());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUxV11202());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUyV11202());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzV11202());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getCo2densityAvg());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getVaporDensityAvg());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getVtouAvg());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getPohsAvg());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAirDensityAvg());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getWaterVaporAvg());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAirTemMeanAtmAvg());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getMeanAirTrm());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAveVapPressure());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV11201Xavg());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getVchV11201());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getCompassV11201());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getSdsWind());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUwsV11201());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getNumSamples());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUwsWarningnum());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAnalyzerH2oco2Warningnum());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUwsTemWarningnum());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUwsLockWarningnum());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUwsLWarningnum());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUwsHWarningnum());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAnalyzerH2oco2Brnum());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAnalyzerH2oco2Testwarningnum());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAnalyzerPlcH2oco2());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAnalyzerH2oco2Syncnum());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAnalyzerH2oco2Avg());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV02262Avg());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV02264Avg());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV04003());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV04004());

                    ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV04005());

                    ps.addBatch();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("列号：" + ii + "有误");
                }
                if(size>4) {
                    if (middle != 0 && indexSingle % middle == 0) {
                        try {
                            ps.executeBatch();
                            connection.commit();
                            ps.clearBatch();
                        } catch (Exception e) {
                            ps.clearBatch();
                            excuteByDataTlSingle(ps, list1, cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=surfXiliTurbTlTab.getV01301()+" "+surfXiliTurbTlTab.getdDatetime();
                        ps.executeBatch();
                        ps.clearBatch();
                    }catch (Exception e){
                        ps.clearBatch();
                        System.out.println("舍弃重复录入数据！"+data);
                    }
                    list1.clear();
                }
            }
            connection.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 非标准格式农田小气候（中环天仪）的处理
     *
     */
    public static DataBaseAction processSuccessReport(List<SurfXiliTurbHcTab> surfXiliTurbHcTabList, List<SurfXiliTurbTlTab> surfXiliTurbTlTabList, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;
        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        insertDB(surfXiliTurbHcTabList,surfXiliTurbTlTabList, connection, recv_time, loggerBuffer, filename, cts_code);
        return DataBaseAction.SUCCESS;

    }

    /**
     * 入库
     */
    private static void insertDB(List<SurfXiliTurbHcTab> surfXiliTurbHcTabList, List<SurfXiliTurbTlTab> surfXiliTurbTlTabList, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            if (surfXiliTurbHcTabList!=null && !surfXiliTurbHcTabList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("R008_hc_value_table_name") + "("
                        + "D_RETAIN_ID,D_DATA_ID,D_DATETIME,V01301, V04001, \n" +
                        "      V04002, V04003, V04004, \n" +
                        "      V06001, V05001, V07001, \n" +
                        "      HEITH_THREE_WIND, ANGLE_THREE_WIND, HEITH_RED_H2OCO2, \n" +
                        "      V07031, HOURMIN, X_V11202, \n" +
                        "      Y_V11202, Z_V11202, CO2_DENSITY, \n" +
                        "      VAPOR_DENSITY, VTOU, FLUTRM, \n" +
                        "      V10004, VTOU_WINDTEM, RED_H2OCO2_VUE, \n" +
                        "      RED_H2OCO2_AGC) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                filename = filename.substring(0, filename.indexOf("."));

                int index = 0;
                List<SurfXiliTurbHcTab> list=new ArrayList<>();
                for (SurfXiliTurbHcTab surfXiliTurbHcTab : surfXiliTurbHcTabList) {
                    int ii = 1;
                    index += 1;
                    list.add(surfXiliTurbHcTab);
                    try {

                        ps.setString(ii++, surfXiliTurbHcTab.getdRecordId());

                        ps.setString(ii++, StartConfig.ctsCode("R008_hc_cts_code"));//四级编码

                        ps.setString(ii++, surfXiliTurbHcTab.getdDatetime());//资料时间

                        ps.setString(ii++, surfXiliTurbHcTab.getV01301());

                        ps.setString(ii++, surfXiliTurbHcTab.getV04001());

                        ps.setString(ii++, surfXiliTurbHcTab.getV04002());

                        ps.setString(ii++, surfXiliTurbHcTab.getV04003());

                        ps.setString(ii++, surfXiliTurbHcTab.getV04004());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getV06001());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getV05001());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getV07001());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getHeithThreeWind());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getAngleThreeWind());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getHeithRedH2oco2());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getV07031());

                        ps.setString(ii++, surfXiliTurbHcTab.getHourmin());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getxV11202());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getyV11202());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getzV11202());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getCo2Density());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getVaporDensity());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getVtou());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getFlutrm());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getV10004());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getVtouWindtem());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getRedH2oco2Vue());

                        ps.setBigDecimal(ii++, surfXiliTurbHcTab.getRedH2oco2Agc());

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("列号：" + ii + "有误");
                    }
                    ps.addBatch();
                    if (index % 1000 == 0) {
                        try {
                            ps.executeBatch();
                            connection.commit();
                            ps.clearBatch();
                        } catch (Exception e) {
                            ps.clearBatch();
                            //执行错误时，单条执行
                            excuteByDataHcSingle(ps, list, cts_code, connection);
                        }
                        list.clear();
                    }
                }
                try {
                    ps.executeBatch();
                    connection.commit();
                } catch (Exception e) {
                    ps.clearBatch();
                    //执行错误时，单条执行
                    excuteByDataHcSingle(ps, list, cts_code, connection);
                }
                list.clear();
            }
            if (surfXiliTurbTlTabList!=null && !surfXiliTurbTlTabList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("R008_tl_value_table_name") + "("
                        + "D_RETAIN_ID,D_DATA_ID,D_DATETIME, V01301, V04001, \n" +
                        "      V04002, V06001, V05001, \n" +
                        "      V07001, HEITH_THREE_WIND, ANGLE_THREE_WIND, \n" +
                        "      HEITH_RED_H2OCO2, V07031, V02320, \n" +
                        "      VERSION, THREE_WIND_MODE, ANALYZER_MODEL_H20CO2, \n" +
                        "      Q48008, HEIGH_VEGETATION, \n" +
                        "      CO2_WPL, LHF_WPL, VTOU_SHF, \n" +
                        "      MOMFLUX, FRI_V11202, NONCO2_WPL, \n" +
                        "      NONLHF_WPL, MOI_CO2LHF_WPL, MOI_CO2SHF_WPL, \n" +
                        "      MOI_LHF, MOI_SHF, UZ_V11202_VARI, \n" +
                        "      UZ_UX_VARI, UZ_UY_VARI, UZ_V11202_CO2DENSITY_VARI, \n" +
                        "      UZ_V11202_VAPOR_VARI, UZ_V11202_VTOU_VARI, UX_V11202_VARI, \n" +
                        "      UX_UY_VARI, UX_V11202_CO2DENSITY_VARI, UX_V11202_VAPOR_VARI, \n" +
                        "      UX_V11202_VTOU_VAR, UY_V11202_VARI, UY_V11202_CO2DENSITY_VARI, \n" +
                        "      UY_V11202_VAPOR_VARI, UY_V11202_VTOU_VARI, CO2DENSITY_VARI, \n" +
                        "      VAPOR_DENSITY_VARI, VTOU_VARI, UX_V11202, \n" +
                        "      UY_V11202, UZ_V11202, CO2DENSITY_AVG, \n" +
                        "      VAPOR_DENSITY_AVG, VTOU_AVG, POHS_AVG, \n" +
                        "      AIR_DENSITY_AVG, WATER_VAPOR_AVG, AIR_TEM_MEAN_ATM_AVG, \n" +
                        "      MEAN_AIR_TRM, AVE_VAP_PRESSURE, V11201_XAVG, \n" +
                        "      VCH_V11201, COMPASS_V11201, SDS_WIND, \n" +
                        "      UWS_V11201, NUM_SAMPLES, UWS_WARNINGNUM, \n" +
                        "      ANALYZER_H2OCO2_WARNINGNUM, UWS_TEM_WARNINGNUM, \n" +
                        "      UWS_LOCK_WARNINGNUM, UWS_L_WARNINGNUM, UWS_H_WARNINGNUM, \n" +
                        "      ANALYZER_H2OCO2_BRNUM, ANALYZER_H2OCO2_TESTWARNINGNUM, \n" +
                        "      ANALYZER_PLC_H2OCO2, ANALYZER_H2OCO2_SYNCNUM, \n" +
                        "      ANALYZER_H2OCO2_AVG, V02262_AVG, V02264_AVG,V04003,V04004,V04005) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                filename = filename.substring(0, filename.indexOf("."));

                int index = 0;
                List<SurfXiliTurbTlTab> list = new ArrayList<>();
                for (SurfXiliTurbTlTab surfXiliTurbTlTab : surfXiliTurbTlTabList) {
                    int ii = 1;
                    index += 1;
                    list.add(surfXiliTurbTlTab);
                    try {
                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getdRecordId());

                        ps.setString(ii++, StartConfig.ctsCode("R008_tl_cts_code"));//四级编码

                        ps.setString(ii++, surfXiliTurbTlTab.getdDatetime());//资料时间

                        ps.setString(ii++, surfXiliTurbTlTab.getV01301());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV04001());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV04002());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV06001());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV05001());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV07001());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getHeithThreeWind());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAngleThreeWind());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getHeithRedH2oco2());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV07031());

                        ps.setString(ii++, surfXiliTurbTlTab.getV02320());

                        ps.setString(ii++, surfXiliTurbTlTab.getVersion());

                        ps.setString(ii++, surfXiliTurbTlTab.getThreeWindMode());

                        ps.setString(ii++, surfXiliTurbTlTab.getAnalyzerModelH20co2());

                        ps.setString(ii++, surfXiliTurbTlTab.getQ48008());

                        ps.setString(ii++, surfXiliTurbTlTab.getHeighVegetation());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getCo2Wpl());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getLhfWpl());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getVtouShf());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getMomflux());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getFriV11202());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getNonco2Wpl());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getNonlhfWpl());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getMoiCo2lhfWpl());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getMoiCo2shfWpl());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getMoiLhf());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getMoiShf());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzV11202Vari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzUxVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzUyVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzV11202Co2densityVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzV11202VaporVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzV11202VtouVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUxV11202Vari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUxUyVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUxV11202Co2densityVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUxV11202VaporVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUxV11202VtouVar());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUyV11202Vari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUyV11202Co2densityVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUyV11202VaporVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUyV11202VtouVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getCo2densityVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getVaporDensityVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getVtouVari());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUxV11202());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUyV11202());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUzV11202());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getCo2densityAvg());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getVaporDensityAvg());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getVtouAvg());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getPohsAvg());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAirDensityAvg());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getWaterVaporAvg());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAirTemMeanAtmAvg());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getMeanAirTrm());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAveVapPressure());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV11201Xavg());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getVchV11201());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getCompassV11201());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getSdsWind());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUwsV11201());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getNumSamples());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUwsWarningnum());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAnalyzerH2oco2Warningnum());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUwsTemWarningnum());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUwsLockWarningnum());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUwsLWarningnum());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getUwsHWarningnum());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAnalyzerH2oco2Brnum());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAnalyzerH2oco2Testwarningnum());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAnalyzerPlcH2oco2());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAnalyzerH2oco2Syncnum());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getAnalyzerH2oco2Avg());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV02262Avg());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV02264Avg());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV04003());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV04004());

                        ps.setBigDecimal(ii++, surfXiliTurbTlTab.getV04005());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("列号：" + ii + "有误");
                    }
                    ps.addBatch();
                    if (index % 1000 == 0) {
                        try {
                            ps.executeBatch();
                            connection.commit();
                            ps.clearBatch();
                        } catch (Exception e) {
                            ps.clearBatch();
                            //执行错误时，单条执行
                            excuteByDataTlSingle(ps, list, cts_code, connection);
                        }
                        list.clear();
                    }
                }
                try {
                    ps.executeBatch();
                    connection.commit();
                } catch (Exception e) {
                    ps.clearBatch();
                    //执行错误时，单条执行
                    excuteByDataTlSingle(ps, list, cts_code, connection);
                }
                list.clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            loggerBuffer.append("\n Database connection exception: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
