package cma.cimiss2.dpc.indb.surf.dc_mul_dy.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.Multl;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class MuldyService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;
    static String type = StartConfig.sodCode();

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        MuldyService.diQueues = diQueues;
    }


    /**
     *
     * @Title: processSuccessReport
     * @Description:(3830飞机云气溶胶粒子探头CAS观测资料)
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param fileN
     * @return DataBaseAction
     * @throws：
     */
    public static DataBaseAction processSuccessReport(ParseResult<Multl> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileN) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<Multl> cliCsv = parseResult.getData();
            insert_db(cliCsv, connection, recv_time, loggerBuffer,fileN,cts_code);
            return DataBaseAction.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            loggerBuffer.append("\n Database connection error");
            return DataBaseAction.CONNECTION_ERROR;
        }
        finally{
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    loggerBuffer.append("\n Database connection close error"+e.getMessage());
                }
            }

        }
    }

    /**
     * @param cts_code
     * @param loggerBuffer
     * @param cts_code
     *
     * @Title: insert_db
     * @Description:(3830飞机云气溶胶粒子探头CAS观测资料)
     * @param: @param list 数据结合
     * @param: @param recv_time  消息接收时间
     * @param: @param is_Batch  是否Batch提交
     * @param: @return
     * @return: DataBaseAction  返回处理状态
     * @throws
     */
    private static void insert_db(List<Multl> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable() + "("
                    + "ID,OBSERVATION_DATA_DATE,LAYER,STATION_NUMBER,COMMAND_NAME,CREATE_DATE,AVE_WD_2MIN,AVE_WS_2MIN,AVE_WD_10MIN,AVE_WS_10MIN,MOST_WD,MOST_WS,MOST_WS_DATE,INST_WD,INST_WS,MAX_WD,MAX_WS,MAX_WS_DATE,BUCKET_RAIN_COUNT,WEIGHT_RAIN_COUNT,TEMP,MOST_TEMP,MOST_TEMP_DATE,LEAST_TEMP,LEAST_TEMP_DATE,WET_BALL_TEMP,RELA_HUMI,LEAST_RELA_HUMI,LEAST_RELA_HUMI_DATE,VAPOR_PRES,EXPO_POINT_TEMP,PRES,MOST_PRES,MOST_PRES_DATE,LEAST_PRES,LEAST_PRES_DATE,GRASS_TEMP,MOST_GRASS_TEMP,MOST_GRASS_TEMP_DATE,LEAST_GRASS_TEMP,LEAST_GRASS_TEMP_DATE,LAND_PT_TEMP,MOST_LAND_PT_TEMP,MOST_LAND_PT_TEMP_DATE,LEAST_LAND_PT_TEMP,LEAST_LAND_PT_TEMP_DATE,LAND_IR_TEMP,MOST_LAND_IR_TEMP,MOST_LAND_IR_TEMP_DATE,LEAST_LAND_IR_TEMP,LEAST_LAND_IR_TEMP_DATE,LAND_5CM_TEMP,LAND_10CM_TEMP,LAND_15CM_TEMP,LAND_20CM_TEMP,LAND_30CM_TEMP,LAND_40CM_TEMP,LAND_50CM_TEMP,LAND_80CM_TEMP,LAND_160CM_TEMP,LAND_320CM_TEMP,EVAPORATION,SUN_EXPOSURE,VISIBILITY,LEAST_VISIBILITY,LEAST_VISIBILITY_DATE,CLOUD_LEVEL,TOTAL_CLOUD_COVERAGE,CURRENT_METEO_CODE,EXT_1,EXT_2,EXT_3,EXT_4,EXT_5,SEA_LEVEL_PRES,BUCKET_ACC_RAIN_COUNT,ACC_EVAPORATION,ACC_SUN_EXPOSURE_TIME,WEIGHT_ACC_RAIN_COUNT,BUCKET_MIN_RAIN_CNT,WEIGHT_MIN_RAIN_CNT,AVE_WD_1MIN,AVE_WS_1MIN,ACC_METEO_CODE,ACC_METEO_CODE_15,DAY_ACC_RAIN_CNT,BUCKET_RAIN_COUNT2,BUCKET_ACC_RAIN_COUNT2,BUCKET_MIN_RAIN_CNT2,EVA_WL,LOW_CC,SNOW_DEPTH,FROZEN_RAIN,WIRE_IAT,FROZEN_SD,LIGHTNING_FREQ,CTEMP,MOST_CTEMP,MOST_CTEMP_DATE,LEAST_CTEMP,LEAST_CTEMP_DATE,WS_VENTILATION,AVE_WS_2MIN_15,AVE_WS_10MIN_15,MOST_WS_15,MOST_WS_DATE_15,MAX_WS_1MIN_15,MAX_WS_15,MAX_WS_DATE_15,AVE_10MIN_VISI,QC_STR_BR,QC_STR_B2R,QC_STR_WR,LEAST_VISI_10,LEAST_VISI_10_DATE,INST_W1D,INST_W1S,NEG_OIONS,POS_OIONS)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Multl multl : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, multl.getId());
                    ps.setTimestamp(ii++, multl.getObservationDataDate());
                    ps.setString(ii++, multl.getLayer());
                    ps.setString(ii++, multl.getStationNumber());
                    ps.setString(ii++, multl.getCommandName());
                    ps.setTimestamp(ii++, multl.getCreateDate());
                    ps.setInt(ii++, multl.getAveWd2min());
                    ps.setDouble(ii++, multl.getAveWs2min());
                    ps.setInt(ii++, multl.getAveWd10min());
                    ps.setDouble(ii++, multl.getAveWs10min());
                    ps.setInt(ii++, multl.getMostWd());
                    ps.setDouble(ii++, multl.getMostWs());
                    ps.setTimestamp(ii++, multl.getMostWsDate());
                    ps.setInt(ii++, multl.getInstWd());
                    ps.setDouble(ii++, multl.getInstWs());
                    ps.setInt(ii++, multl.getMaxWd());
                    ps.setDouble(ii++, multl.getMaxWs());
                    ps.setTimestamp(ii++, multl.getMaxWsDate());
                    ps.setInt(ii++, multl.getBucketRainCount());
                    ps.setString(ii++, multl.getWeightRainCount());
                    ps.setDouble(ii++, multl.getTemp());
                    ps.setDouble(ii++, multl.getMostTemp());
                    ps.setTimestamp(ii++, multl.getMostTempDate());
                    ps.setDouble(ii++, multl.getLeastTemp());
                    ps.setTimestamp(ii++, multl.getLeastTempDate());
                    ps.setString(ii++, multl.getWetBallTemp());
                    ps.setString(ii++, multl.getRelaHumi());
                    ps.setString(ii++, multl.getLeastRelaHumi());
                    ps.setString(ii++, multl.getLeastRelaHumiDate());
                    ps.setString(ii++, multl.getVaporPres());
                    ps.setString(ii++, multl.getExpoPointTemp());
                    ps.setString(ii++, multl.getPres());
                    ps.setString(ii++, multl.getMostPres());
                    ps.setString(ii++, multl.getMostPresDate());
                    ps.setString(ii++, multl.getLeastPres());
                    ps.setString(ii++, multl.getLeastPresDate());
                    ps.setString(ii++, multl.getGrassTemp());
                    ps.setString(ii++, multl.getMostGrassTemp());

                    ps.setString(ii++, multl.getMostGrassTempDate());
                    ps.setString(ii++, multl.getLeastGrassTemp());
                    ps.setString(ii++, multl.getLeastGrassTempDate());
                    ps.setString(ii++, multl.getLandPtTemp());
                    ps.setString(ii++, multl.getMostLandPtTemp());
                    ps.setString(ii++, multl.getMostLandPtTempDate());
                    ps.setString(ii++, multl.getLeastLandPtTemp());
                    ps.setString(ii++, multl.getLeastLandPtTempDate());
                    ps.setString(ii++, multl.getLandIrTemp());
                    ps.setString(ii++, multl.getMostLandIrTemp());
                    ps.setString(ii++, multl.getMostLandIrTempDate());
                    ps.setString(ii++, multl.getLeastLandIrTemp());
                    ps.setString(ii++, multl.getLeastLandIrTempDate());

                    ps.setString(ii++, multl.getLand5cmTemp());
                    ps.setString(ii++, multl.getLand10cmTemp());
                    ps.setString(ii++, multl.getLand15cmTemp());
                    ps.setString(ii++, multl.getLand20cmTemp());
                    ps.setString(ii++, multl.getLand30cmTemp());
                    ps.setString(ii++, multl.getLand40cmTemp());
                    ps.setString(ii++, multl.getLand50cmTemp());
                    ps.setString(ii++, multl.getLand80cmTemp());
                    ps.setString(ii++, multl.getLand160cmTemp());
                    ps.setString(ii++, multl.getLand320cmTemp());

                    ps.setString(ii++, multl.getEvaporation());
                    ps.setString(ii++, multl.getSunExposure());
                    ps.setString(ii++, multl.getVisibility());
                    ps.setString(ii++, multl.getLeastVisibility());
                    ps.setString(ii++, multl.getLeastVisibilityDate());
                    ps.setString(ii++, multl.getCloudLevel());
                    ps.setString(ii++, multl.getTotalCloudCoverage());
                    ps.setString(ii++, multl.getCurrentMeteoCode());

                    ps.setString(ii++, multl.getExt1());
                    ps.setString(ii++, multl.getExt2());
                    ps.setString(ii++, multl.getExt3());
                    ps.setString(ii++, multl.getExt4());
                    ps.setString(ii++, multl.getExt5());
                    ps.setString(ii++, multl.getSeaLevelPres());
                    ps.setString(ii++, multl.getBucketAccRainCount());
                    ps.setString(ii++, multl.getAccEvaporation());

                    ps.setString(ii++, multl.getAccSunExposureTime());
                    ps.setString(ii++, multl.getWeightAccRainCount());
                    ps.setString(ii++, multl.getBucketMinRainCnt());
                    ps.setString(ii++, multl.getWeightMinRainCnt());
                    ps.setString(ii++, multl.getAveWd1min());
                    ps.setString(ii++, multl.getAveWs1min());
                    ps.setString(ii++, multl.getAccMeteoCode());
                    ps.setString(ii++, multl.getAccMeteoCode15());

                    ps.setString(ii++, multl.getDayAccRainCnt());
                    ps.setString(ii++, multl.getBucketRainCount2());
                    ps.setString(ii++, multl.getBucketAccRainCount2());
                    ps.setString(ii++, multl.getBucketMinRainCnt2());
                    ps.setString(ii++, multl.getEvaWl());
                    ps.setString(ii++, multl.getLowCc());
                    ps.setString(ii++, multl.getSnowDepth());
                    ps.setString(ii++, multl.getFrozenRain());

                    ps.setString(ii++, multl.getWireIat());
                    ps.setString(ii++, multl.getFrozenSd());
                    ps.setString(ii++, multl.getLightningFreq());
                    ps.setString(ii++, multl.getCtemp());
                    ps.setString(ii++, multl.getMostCtemp());
                    ps.setString(ii++, multl.getMostCtempDate());
                    ps.setString(ii++, multl.getLeastCtemp());
                    ps.setString(ii++, multl.getLeastCtempDate());

                    ps.setString(ii++, multl.getWsVentilation());
                    ps.setString(ii++, multl.getAveWs2min15());
                    ps.setString(ii++, multl.getAveWs10min15());
                    ps.setString(ii++, multl.getMostWs15());
                    ps.setString(ii++, multl.getMostWsDate15());
                    ps.setString(ii++, multl.getMaxWs1min15());
                    ps.setString(ii++, multl.getMaxWs15());
                    ps.setString(ii++, multl.getMaxWsDate15());

                    ps.setString(ii++, multl.getAve10minVisi());
                    ps.setString(ii++, multl.getQcStrBr());
                    ps.setString(ii++, multl.getQcStrB2r());
                    ps.setString(ii++, multl.getQcStrWr());
                    ps.setString(ii++, multl.getLeastVisi10());
                    ps.setString(ii++, multl.getLeastVisi10Date());
                    ps.setInt(ii++, multl.getInstW1d());
                    ps.setDouble(ii++, multl.getInstW1s());
                    ps.setString(ii++, multl.getNegOions());
                    ps.setString(ii++, multl.getPosOions());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("列号：" + ii + "有误");
                }
                ps.addBatch();
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
