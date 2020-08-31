package cma.cimiss2.dpc.indb.storm.tools;


import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.hitec.bufr.util.StationInfo;

import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataReg;

public class DecodeRegUtil {
    /* public static Properties properties = new Properties();
     public static Map<String, Object> proMap = new HashMap<String, Object>();
     public static final String CONFIG_COLLECT = "config/StationInfo_Config.lua";

     static {
         InputStream is = null;
         try {
             is = DecodeNationUtil.class.getClassLoader().getResource(CONFIG_COLLECT).openStream();
             properties.load(is);
         } catch (IOException e) {
             e.printStackTrace();
         } finally {

             proMap = (Map) properties;
             try {
                 is.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }

     public static Map<String, Object> getProMap() {
         return proMap;
     }*/
    public static void SetRegHorValue(SurfaceObservationDataReg bean, Long lastModifiedTime, PreparedStatement psinsert) throws SQLException, ParseException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        //1资料时间  取观测年、月、日、时分、秒组合
//        psinsert.setTimestamp(1, new Timestamp(Long.valueOf(sdf.format(bean.getObservationTime()))));
        psinsert.setTimestamp(1, new Timestamp(bean.getObservationTime().getTime()));
//        psinsert.setTimestamp(1, new Timestamp(sdf.parse(String.valueOf(bean.getObservationTime())).getTime()));
        //2资料标识  固定为 A.0012.0001.S001
        psinsert.setString(2, "A.0012.0001.S001");
        //3入库时间
//        psinsert.setTimestamp(3,  new Timestamp(Long.valueOf(sdf.format(date))));
        psinsert.setTimestamp(3, new Timestamp(date.getTime()));
        //4收到时间 取自文件属性，即收到文件的时间?
//        psinsert.setTimestamp(4, new Timestamp(Long.valueOf(sdf.format(new Date(lastModifiedTime)))));
        psinsert.setTimestamp(4, new Timestamp(new Date(lastModifiedTime).getTime()));
        //5更新时间
//        psinsert.setTimestamp(5, new Timestamp(Long.valueOf(sdf.format(date))));
        psinsert.setTimestamp(5, new Timestamp(date.getTime()));
        //时间 年月日
        psinsert.setBigDecimal(6, new BigDecimal(bean.getObservationTime().getYear()+1900));
        psinsert.setBigDecimal(7, new BigDecimal(bean.getObservationTime().getMonth()+1));
        psinsert.setBigDecimal(8, new BigDecimal(bean.getObservationTime().getDate()));
        psinsert.setBigDecimal(9, new BigDecimal(bean.getObservationTime().getHours()));
        //区站号 字符 数字
        String sid = bean.getStationNumberChina();//
        psinsert.setString(10, sid);
        String sid0 = sid.substring(0, 1);
        if (sid0.matches("^[a-zA-Z]*")) {
            int ascii = (int) sid0.toCharArray()[0];
            String sid1 = "" + ascii + sid.substring(1);
            psinsert.setBigDecimal(11, new BigDecimal(sid1));
        } else {
            psinsert.setBigDecimal(11, new BigDecimal(sid));
        }
        psinsert.setBigDecimal(12, new BigDecimal(bean.getLatitude()));
        psinsert.setBigDecimal(13, new BigDecimal(bean.getLongitude()));
        psinsert.setBigDecimal(14, new BigDecimal(bean.getHeightOfSationGroundAboveMeanSeaLevel()));
        psinsert.setBigDecimal(15, new BigDecimal(bean.getHeightOfBarometerAboveMeanSeaLevel()));

        psinsert.setBigDecimal(16, new BigDecimal("999998"));
        psinsert.setBigDecimal(17, new BigDecimal("999998"));
        psinsert.setBigDecimal(18, new BigDecimal("999998"));

        psinsert.setBigDecimal(19, new BigDecimal("0"));//文件查找 固定值4

        Map<String, Object> proMap = StationInfo.getProMap();
        //System.out.println(map==null);
        // System.out.println("=========================="+proMap.containsKey(sid + "+01"));
        if (proMap.containsKey(sid + "+01")) {
            //System.out.println("=========查到到==========="+sid);
            String s = (String) proMap.get(sid + "+01");
            psinsert.setBigDecimal(20, new BigDecimal(s.split(",")[6]==null?"000":s.split(",")[6]));
//            psinsert.setBigDecimal(20, new BigDecimal("999999"));//
            psinsert.setBigDecimal(21, new BigDecimal(s.split(",")[5]));//
        } else {
            //System.out.println("=========未查到到==========="+sid);
            psinsert.setBigDecimal(20, new BigDecimal("999999"));//
            psinsert.setBigDecimal(21, new BigDecimal("999999"));//
        }
        psinsert.setBigDecimal(22, new BigDecimal("999998"));
        psinsert.setBigDecimal(23, new BigDecimal("999998"));
        //字段长度越界
//        psinsert.setString(24, String.valueOf(bean.getCorrectionIndicator()));
//        psinsert.setString(24, "000");//更正标志
        psinsert.setString(24, bean.getCorrectionIndicator());//更正标志

        psinsert.setBigDecimal(25, new BigDecimal(bean.getStationPressure().getValue()));
        psinsert.setBigDecimal(26, new BigDecimal(bean.getPressureReducedToMeanSeaLevel().getValue()));
        psinsert.setBigDecimal(27, new BigDecimal("999998"));
        psinsert.setBigDecimal(28, new BigDecimal("999998"));
        psinsert.setBigDecimal(29, new BigDecimal(bean.getStationPressureMax().getValue()));
        psinsert.setBigDecimal(30, new BigDecimal(bean.getOccurrenceTimeOfStationPressureMax().getValue()));
        psinsert.setBigDecimal(31, new BigDecimal(bean.getStationPressureMin().getValue()));
        //可能出现问题
        psinsert.setBigDecimal(32, new BigDecimal(bean.getOccurrenceTimeOfStationPressureMin().getValue()));
        psinsert.setBigDecimal(33, new BigDecimal(bean.getTemperature().getValue()));
        psinsert.setBigDecimal(34, new BigDecimal(bean.getTemperatureMax().getValue()));
        psinsert.setBigDecimal(35, new BigDecimal(bean.getTemperatureMaxOccurrenceTime().getValue()));
        psinsert.setBigDecimal(36, new BigDecimal(bean.getTemperatureMin().getValue()));

        ///Cannot format given Object as a Date
        psinsert.setBigDecimal(37, new BigDecimal(bean.getTemperatureMinOccurrenceTime().getValue()));
       /* String lowTime = sdf.format(bean.getTemperatureMinOccurrenceTime().getValue());//4位整数
        if ("999999".equals(lowTime)) {
            psinsert.setBigDecimal(37, new BigDecimal(lowTime));
        } else {
            if (lowTime.length() == 1) {
                psinsert.setBigDecimal(37, new BigDecimal("0" + lowTime + "00"));
            }
            if (lowTime.length() == 2) {
                psinsert.setBigDecimal(37, new BigDecimal(lowTime + "00"));
            }
            if (lowTime.length() == 3) {
                psinsert.setBigDecimal(37, new BigDecimal("0" + lowTime));
            }
            if (lowTime.length() == 4) {
                psinsert.setBigDecimal(37, new BigDecimal(lowTime));
            }
        }*/

        psinsert.setBigDecimal(38, new BigDecimal("999998"));
        psinsert.setBigDecimal(39, new BigDecimal("999998"));
        psinsert.setBigDecimal(40, new BigDecimal("999998"));
        psinsert.setBigDecimal(41, new BigDecimal(bean.getWetBulbTemperature().getValue()));
        psinsert.setBigDecimal(42, new BigDecimal(bean.getRelativeHumidity().getValue()));
        psinsert.setBigDecimal(43, new BigDecimal(bean.getRelativeHumidityMin().getValue()));
        psinsert.setBigDecimal(44, new BigDecimal(bean.getOccurrenceTimeOfRelativeHumidityMin().getValue()));
        psinsert.setBigDecimal(45, new BigDecimal(bean.getVapourPressur().getValue()));
        psinsert.setBigDecimal(46, new BigDecimal(bean.getPrecipitation1Hour().getValue()));
        psinsert.setBigDecimal(47, new BigDecimal("999998"));
        psinsert.setBigDecimal(48, new BigDecimal("999998"));
        psinsert.setBigDecimal(49, new BigDecimal("999998"));
        psinsert.setBigDecimal(50, new BigDecimal("999998"));
        psinsert.setBigDecimal(51, new BigDecimal("999998"));
        psinsert.setBigDecimal(52, new BigDecimal("999998"));
        psinsert.setBigDecimal(53, new BigDecimal(bean.getEvaporation().getValue()));
        psinsert.setBigDecimal(54, new BigDecimal(bean.getWindDirectionAt2m().getValue()));
        psinsert.setBigDecimal(55, new BigDecimal(bean.getWindSpeedAvg2m().getValue()));
        psinsert.setBigDecimal(56, new BigDecimal(bean.getWindDirectionAt10().getValue()));
        psinsert.setBigDecimal(57, new BigDecimal(bean.getWindSpeedAt10().getValue()));
        psinsert.setBigDecimal(58, new BigDecimal(bean.getWindDirectionOfMaxSpeed().getValue()));
        psinsert.setBigDecimal(59, new BigDecimal(bean.getWindSpeedMax().getValue()));
        psinsert.setBigDecimal(60, new BigDecimal(bean.getWindSpeedMaxOccurrenceTime().getValue()));
        psinsert.setBigDecimal(61, new BigDecimal(bean.getWindDirectionCurrent().getValue()));
        psinsert.setBigDecimal(62, new BigDecimal(bean.getWindSpeedCurrent().getValue()));
        psinsert.setBigDecimal(63, new BigDecimal(bean.getWindDirectionWhenSpeedMax().getValue()));
        psinsert.setBigDecimal(64, new BigDecimal(bean.getExtremeWindSpeed1Hour().getValue()));
        psinsert.setBigDecimal(65, new BigDecimal(bean.getWindSpeedExtremeOccurrenceTime().getValue()));
        psinsert.setBigDecimal(66, new BigDecimal("999998"));
        psinsert.setBigDecimal(67, new BigDecimal("999998"));
        psinsert.setBigDecimal(68, new BigDecimal("999998"));
        psinsert.setBigDecimal(69, new BigDecimal("999998"));
        psinsert.setBigDecimal(70, new BigDecimal(bean.getLandSurfaceTemperature().getValue()));
        psinsert.setBigDecimal(71, new BigDecimal(bean.getLandSurfaceTemperatureMax().getValue()));
        psinsert.setBigDecimal(72, new BigDecimal(bean.getLandSurfaceTemperatureMaxOccurrenceTime().getValue()));
        psinsert.setBigDecimal(73, new BigDecimal(bean.getLandSurfaceTemperatureMin().getValue()));
        psinsert.setBigDecimal(74, new BigDecimal(bean.getLandSurfaceTemperatureMinOccurrenceTime().getValue()));
        psinsert.setBigDecimal(75, new BigDecimal("999998"));
        psinsert.setBigDecimal(76, new BigDecimal(bean.getSoilTemperature5CM().getValue()));
        psinsert.setBigDecimal(77, new BigDecimal(bean.getSoilTemperature10CM().getValue()));
        psinsert.setBigDecimal(78, new BigDecimal(bean.getSoilTemperature15CM().getValue()));
        psinsert.setBigDecimal(79, new BigDecimal(bean.getSoilTemperature20CM().getValue()));
        psinsert.setBigDecimal(80, new BigDecimal(bean.getSoilTemperature40CM().getValue()));
        psinsert.setBigDecimal(81, new BigDecimal(bean.getSoilTemperature80CM().getValue()));
        psinsert.setBigDecimal(82, new BigDecimal(bean.getSoilTemperature160CM().getValue()));
        psinsert.setBigDecimal(83, new BigDecimal(bean.getSoilTemperature320CM().getValue()));
        psinsert.setBigDecimal(84, new BigDecimal(bean.getGrassTemperature().getValue()));
        psinsert.setBigDecimal(85, new BigDecimal(bean.getGrassTemperatureMax().getValue()));
        psinsert.setBigDecimal(86, new BigDecimal(bean.getGrassTemperatureMaxOccurrenceTime().getValue()));
        psinsert.setBigDecimal(87, new BigDecimal(bean.getGrassTemperatureMin().getValue()));
        psinsert.setBigDecimal(88, new BigDecimal(bean.getGrassTemperatureMinOccurrenceTime().getValue()));
        psinsert.setBigDecimal(89, new BigDecimal("999998"));
        psinsert.setBigDecimal(90, new BigDecimal("999998"));
        psinsert.setBigDecimal(91, new BigDecimal(bean.getHorizontalVisibility1HourMin().getValue()));
        psinsert.setBigDecimal(92, new BigDecimal(bean.getHorizontalVisibility1HourMinOccurrenceTime().getValue()));
        psinsert.setBigDecimal(93, new BigDecimal(bean.getHorizontalVisibilityHourly()));
        psinsert.setBigDecimal(94, new BigDecimal(bean.getCloudAmount()));
        psinsert.setBigDecimal(95, new BigDecimal(bean.getCloudAmountLow()));
        psinsert.setBigDecimal(96, new BigDecimal(bean.getCloudAmountLowAndMiddle()));
        psinsert.setBigDecimal(97, new BigDecimal(bean.getCloudHeightLowAndMiddle()));

        //云状
        String[] cloudType = bean.getCloudType();
        /*for(String s:cloudType){

            System.out.println(s);
        }*/
        psinsert.setBigDecimal(98, new BigDecimal(cloudType[0]));
        psinsert.setBigDecimal(99, new BigDecimal(cloudType[1]));
        psinsert.setBigDecimal(100, new BigDecimal(cloudType[2]));
        psinsert.setBigDecimal(101, new BigDecimal(cloudType[3]));
        psinsert.setBigDecimal(102, new BigDecimal(cloudType[4]));
        psinsert.setBigDecimal(103, new BigDecimal(cloudType[5]));
        psinsert.setBigDecimal(104, new BigDecimal(cloudType[6]));
        psinsert.setBigDecimal(105, new BigDecimal(cloudType[7]));

        /*psinsert.setBigDecimal(98, new BigDecimal("999999"));
        psinsert.setBigDecimal(99, new BigDecimal("999999"));
        psinsert.setBigDecimal(100, new BigDecimal("999999"));
        psinsert.setBigDecimal(101, new BigDecimal("999999"));
        psinsert.setBigDecimal(102, new BigDecimal("999999"));
        psinsert.setBigDecimal(103, new BigDecimal("999999"));
        psinsert.setBigDecimal(104, new BigDecimal("999999"));
        psinsert.setBigDecimal(105, new BigDecimal("999999"));*/


        //云状编码
        String cloudType1 = bean.getCloudType1();
        psinsert.setBigDecimal(106, new BigDecimal("999998"));
        psinsert.setBigDecimal(107, new BigDecimal("999998"));
        psinsert.setBigDecimal(108, new BigDecimal("999998"));


        psinsert.setBigDecimal(109, new BigDecimal(bean.getPresentWeather()));
        psinsert.setBigDecimal(110, new BigDecimal("999998"));
        psinsert.setBigDecimal(111, new BigDecimal("999998"));
        psinsert.setBigDecimal(112, new BigDecimal("999998"));
        psinsert.setBigDecimal(113, new BigDecimal(bean.getGroundState()));
        psinsert.setBigDecimal(114, new BigDecimal(bean.getSnowDepth()));
        psinsert.setBigDecimal(115, new BigDecimal(bean.getSnowPressure()));
        psinsert.setBigDecimal(116, new BigDecimal("999998"));
        psinsert.setBigDecimal(117, new BigDecimal("999998"));
        psinsert.setBigDecimal(118, new BigDecimal("999998"));
        psinsert.setBigDecimal(119, new BigDecimal("999998"));

        //1级质控
        psinsert.setBigDecimal(120, new BigDecimal(bean.getStationPressure().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(121, new BigDecimal(bean.getPressureReducedToMeanSeaLevel().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(122, new BigDecimal("7"));
        psinsert.setBigDecimal(123, new BigDecimal("7"));
        psinsert.setBigDecimal(124, new BigDecimal(bean.getStationPressureMax().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(125, new BigDecimal(bean.getOccurrenceTimeOfStationPressureMax().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(126, new BigDecimal(bean.getStationPressureMin().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(127, new BigDecimal(bean.getOccurrenceTimeOfStationPressureMin().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(128, new BigDecimal(bean.getTemperature().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(129, new BigDecimal(bean.getTemperatureMax().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(130, new BigDecimal(bean.getTemperatureMaxOccurrenceTime().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(131, new BigDecimal(bean.getTemperatureMin().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(132, new BigDecimal(bean.getTemperatureMinOccurrenceTime().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(133, new BigDecimal("7"));
        psinsert.setBigDecimal(134, new BigDecimal("7"));
        psinsert.setBigDecimal(135, new BigDecimal("7"));
        psinsert.setBigDecimal(136, new BigDecimal(bean.getWetBulbTemperature().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(137, new BigDecimal(bean.getRelativeHumidity().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(138, new BigDecimal(bean.getRelativeHumidityMin().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(139, new BigDecimal(bean.getOccurrenceTimeOfRelativeHumidityMin().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(140, new BigDecimal(bean.getVapourPressur().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(141, new BigDecimal(bean.getPrecipitation1Hour().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(142, new BigDecimal("7"));
        psinsert.setBigDecimal(143, new BigDecimal("7"));
        psinsert.setBigDecimal(144, new BigDecimal("7"));
        psinsert.setBigDecimal(145, new BigDecimal("7"));
        psinsert.setBigDecimal(146, new BigDecimal("7"));
        psinsert.setBigDecimal(147, new BigDecimal("7"));
        psinsert.setBigDecimal(148, new BigDecimal(bean.getEvaporation().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(149, new BigDecimal(bean.getWindDirectionAt2m().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(150, new BigDecimal(bean.getWindSpeedAvg2m().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(151, new BigDecimal(bean.getWindDirectionAt10().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(152, new BigDecimal(bean.getWindSpeedAt10().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(153, new BigDecimal(bean.getWindDirectionOfMaxSpeed().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(154, new BigDecimal(bean.getWindSpeedMax().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(155, new BigDecimal(bean.getWindSpeedMaxOccurrenceTime().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(156, new BigDecimal(bean.getWindDirectionCurrent().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(157, new BigDecimal(bean.getWindSpeedCurrent().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(158, new BigDecimal(bean.getWindDirectionWhenSpeedMax().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(159, new BigDecimal(bean.getExtremeWindSpeed1Hour().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(160, new BigDecimal(bean.getWindSpeedExtremeOccurrenceTime().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(161, new BigDecimal("7"));
        psinsert.setBigDecimal(162, new BigDecimal("7"));
        psinsert.setBigDecimal(163, new BigDecimal("7"));
        psinsert.setBigDecimal(164, new BigDecimal("7"));
        psinsert.setBigDecimal(165, new BigDecimal(bean.getLandSurfaceTemperatureMax().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(166, new BigDecimal(bean.getLandSurfaceTemperatureMaxOccurrenceTime().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(167, new BigDecimal(bean.getLandSurfaceTemperatureMin().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(168, new BigDecimal(bean.getLandSurfaceTemperatureMinOccurrenceTime().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(169, new BigDecimal("7"));
        psinsert.setBigDecimal(170, new BigDecimal(bean.getSoilTemperature5CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(171, new BigDecimal(bean.getSoilTemperature10CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(172, new BigDecimal(bean.getSoilTemperature15CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(173, new BigDecimal(bean.getSoilTemperature20CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(174, new BigDecimal(bean.getSoilTemperature40CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(175, new BigDecimal(bean.getSoilTemperature80CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(176, new BigDecimal(bean.getSoilTemperature160CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(177, new BigDecimal(bean.getSoilTemperature320CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(178, new BigDecimal(bean.getGrassTemperature().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(179, new BigDecimal(bean.getGrassTemperatureMax().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(180, new BigDecimal(bean.getGrassTemperatureMaxOccurrenceTime().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(181, new BigDecimal(bean.getGrassTemperatureMin().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(182, new BigDecimal(bean.getGrassTemperatureMinOccurrenceTime().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(183, new BigDecimal("7"));
        psinsert.setBigDecimal(184, new BigDecimal("7"));
        psinsert.setBigDecimal(185, new BigDecimal(bean.getHorizontalVisibility1HourMin().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(186, new BigDecimal(bean.getHorizontalVisibility1HourMinOccurrenceTime().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(187, new BigDecimal("7"));
        psinsert.setBigDecimal(188, new BigDecimal("7"));
        psinsert.setBigDecimal(189, new BigDecimal("7"));
        psinsert.setBigDecimal(190, new BigDecimal("7"));
        psinsert.setBigDecimal(191, new BigDecimal("7"));

//        psinsert.setBigDecimal(192, new BigDecimal(bean.getCloudHeightLowAndMiddle().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(192, new BigDecimal("7"));
        //云质量
        psinsert.setBigDecimal(193, new BigDecimal("7"));
        psinsert.setBigDecimal(194, new BigDecimal("7"));
        psinsert.setBigDecimal(195, new BigDecimal("7"));
        psinsert.setBigDecimal(196, new BigDecimal("7"));
        psinsert.setBigDecimal(197, new BigDecimal("7"));
        psinsert.setBigDecimal(198, new BigDecimal("7"));
        psinsert.setBigDecimal(199, new BigDecimal("7"));
        psinsert.setBigDecimal(200, new BigDecimal("7"));
        psinsert.setBigDecimal(201, new BigDecimal("7"));
        psinsert.setBigDecimal(202, new BigDecimal("7"));
        psinsert.setBigDecimal(203, new BigDecimal("7"));

        psinsert.setBigDecimal(204, new BigDecimal("7"));
        psinsert.setBigDecimal(205, new BigDecimal("7"));
        psinsert.setBigDecimal(206, new BigDecimal("7"));
        psinsert.setBigDecimal(207, new BigDecimal("7"));
        psinsert.setBigDecimal(208, new BigDecimal("7"));
        psinsert.setBigDecimal(209, new BigDecimal("7"));
        psinsert.setBigDecimal(210, new BigDecimal("7"));
        psinsert.setBigDecimal(211, new BigDecimal("7"));
        psinsert.setBigDecimal(212, new BigDecimal("7"));
        psinsert.setBigDecimal(213, new BigDecimal("7"));
        psinsert.setBigDecimal(214, new BigDecimal("7"));

//保留字段
       /* psinsert.setBigDecimal(215, new BigDecimal("5"));
        psinsert.setBigDecimal(216, new BigDecimal("5"));
        psinsert.setBigDecimal(217, new BigDecimal("5"));
        psinsert.setBigDecimal(218, new BigDecimal("5"));
        psinsert.setBigDecimal(219, new BigDecimal("5"));
        psinsert.setBigDecimal(220, new BigDecimal("5"));
        psinsert.setBigDecimal(221, new BigDecimal("5"));
        psinsert.setBigDecimal(222, new BigDecimal("5"));
        psinsert.setBigDecimal(223, new BigDecimal("5"));
        psinsert.setBigDecimal(224, new BigDecimal("5"));*/
    }

    public static void SetRegMinValue(SurfaceObservationDataReg bean, Long lastModifiedTime, PreparedStatement psinsert) throws SQLException, ParseException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        //1资料标识  固定为 A.0012.0001.S001
        psinsert.setString(1, "A.0012.0001.S001");
        //2入库时间
        psinsert.setTimestamp(2, new Timestamp(date.getTime()));
        //3收到时间 取自文件属性，即收到文件的时间?
        psinsert.setTimestamp(3, new Timestamp(new Date(lastModifiedTime).getTime()));
        //4更新时间
        psinsert.setTimestamp(4, new Timestamp(date.getTime()));
        //5资料时间  取观测年、月、日、时分、秒组合
        psinsert.setTimestamp(5, new Timestamp(bean.getObservationTime().getTime()));
        //时间 年月日
        psinsert.setBigDecimal(6, new BigDecimal(bean.getObservationTime().getYear()+1900));
        psinsert.setBigDecimal(7, new BigDecimal(bean.getObservationTime().getMonth()+1));
        psinsert.setBigDecimal(8, new BigDecimal(bean.getObservationTime().getDate()));
        psinsert.setBigDecimal(9, new BigDecimal(bean.getObservationTime().getHours()));
        psinsert.setBigDecimal(10, new BigDecimal(bean.getObservationTime().getMinutes()));
        //区站号 字符 数字
        String sid = bean.getStationNumberChina();//
        psinsert.setString(11, sid);
        String sid0 = sid.substring(0, 1);
        if (sid0.matches("^[a-zA-Z]*")) {
            int ascii = (int) sid0.toCharArray()[0];
            String sid1 = "" + ascii + sid.substring(1);
            psinsert.setBigDecimal(12, new BigDecimal(sid1));
        } else {
            psinsert.setBigDecimal(12, new BigDecimal(sid));
        }

        psinsert.setBigDecimal(13, new BigDecimal(bean.getLatitude()));
        psinsert.setBigDecimal(14, new BigDecimal(bean.getLongitude()));
        psinsert.setBigDecimal(15, new BigDecimal(bean.getHeightOfSationGroundAboveMeanSeaLevel()));
//
        psinsert.setBigDecimal(16, new BigDecimal("0"));


        Map<String, Object> proMap = StationInfo.getProMap();
        //System.out.println(map==null);
        // System.out.println("=========================="+proMap.containsKey(sid + "+01"));
        if (proMap.containsKey(sid + "+01")) {
            //System.out.println("=========查到到==========="+sid);
            String s = (String) proMap.get(sid + "+01");
            psinsert.setBigDecimal(17, new BigDecimal(s.split(",")[6]==null?"000":s.split(",")[6]));//有可能为null
            psinsert.setBigDecimal(18, new BigDecimal(s.split(",")[5]));//
        } else {
            //System.out.println("=========未查到到==========="+sid);
            psinsert.setBigDecimal(17, new BigDecimal("999999"));//
            psinsert.setBigDecimal(18, new BigDecimal("999999"));//
        }
        psinsert.setString(19, "000");
        psinsert.setBigDecimal(20, new BigDecimal("999999"));
//        psinsert.setString(24, "000");


        psinsert.setBigDecimal(21, new BigDecimal(bean.getHeightOfBarometerAboveMeanSeaLevel()));
        psinsert.setBigDecimal(22, new BigDecimal("999999"));
        psinsert.setBigDecimal(23, new BigDecimal("999999"));
        psinsert.setBigDecimal(24, new BigDecimal("999999"));
        psinsert.setBigDecimal(25, new BigDecimal("999998"));
        psinsert.setBigDecimal(26, new BigDecimal("999999"));
        psinsert.setBigDecimal(27, new BigDecimal("999998"));
        psinsert.setBigDecimal(28, new BigDecimal("7"));
        psinsert.setBigDecimal(29, new BigDecimal(bean.getStationPressure().getValue()));
        psinsert.setBigDecimal(30, new BigDecimal(bean.getPressureReducedToMeanSeaLevel().getValue()));
        psinsert.setBigDecimal(31, new BigDecimal("999998"));
        psinsert.setBigDecimal(32, new BigDecimal("999998"));
        psinsert.setBigDecimal(33, new BigDecimal("999998"));
        psinsert.setBigDecimal(34, new BigDecimal(bean.getWindSpeedCurrent().getValue()));
        psinsert.setBigDecimal(35, new BigDecimal("999998"));
        psinsert.setBigDecimal(36, new BigDecimal("999998"));
        psinsert.setBigDecimal(37, new BigDecimal(bean.getWindDirectionAt2m().getValue()));
        psinsert.setBigDecimal(38, new BigDecimal(bean.getWindSpeedAvg2m().getValue()));
        psinsert.setBigDecimal(39, new BigDecimal(bean.getWindDirectionAt10().getValue()));
        psinsert.setBigDecimal(40, new BigDecimal(bean.getWindSpeedAt10().getValue()));
        psinsert.setBigDecimal(41, new BigDecimal(bean.getWindDirectionOfMaxSpeed().getValue()));
        psinsert.setBigDecimal(42, new BigDecimal(bean.getTemperature().getValue()));
        psinsert.setBigDecimal(43, new BigDecimal(bean.getWetBulbTemperature().getValue()));
        psinsert.setBigDecimal(44, new BigDecimal(bean.getSoilTemperature5CM().getValue()));
        psinsert.setBigDecimal(45, new BigDecimal(bean.getSoilTemperature10CM().getValue()));
        psinsert.setBigDecimal(46, new BigDecimal(bean.getSoilTemperature15CM().getValue()));
        psinsert.setBigDecimal(47, new BigDecimal(bean.getSoilTemperature20CM().getValue()));
        psinsert.setBigDecimal(48, new BigDecimal(bean.getSoilTemperature40CM().getValue()));
        psinsert.setBigDecimal(49, new BigDecimal(bean.getSoilTemperature80CM().getValue()));
        psinsert.setBigDecimal(50, new BigDecimal(bean.getSoilTemperature160CM().getValue()));
        psinsert.setBigDecimal(51, new BigDecimal(bean.getSoilTemperature320CM().getValue()));
        psinsert.setBigDecimal(52, new BigDecimal(bean.getLandSurfaceTemperature().getValue()));
        psinsert.setBigDecimal(53, new BigDecimal(bean.getGrassTemperature().getValue()));
        psinsert.setBigDecimal(54, new BigDecimal(bean.getRelativeHumidity().getValue()));
        psinsert.setBigDecimal(55, new BigDecimal(bean.getVapourPressur().getValue()));
        psinsert.setBigDecimal(56, new BigDecimal("999998"));
        psinsert.setBigDecimal(57, new BigDecimal(bean.getSnowDepth()));
        psinsert.setBigDecimal(58, new BigDecimal(bean.getEvaporation().getValue()));
        psinsert.setBigDecimal(59, new BigDecimal("999998"));
        psinsert.setBigDecimal(60, new BigDecimal("999998"));
        psinsert.setBigDecimal(61, new BigDecimal(bean.getCloudAmount()));
        psinsert.setBigDecimal(62, new BigDecimal(bean.getCloudHeightLowAndMiddle()));
        psinsert.setString(63, "999998");
        psinsert.setBigDecimal(64, new BigDecimal(bean.getStationPressure().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(65, new BigDecimal(bean.getPressureReducedToMeanSeaLevel().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(66, new BigDecimal(bean.getTemperature().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(67, new BigDecimal(bean.getWindSpeedCurrent().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(68, new BigDecimal("7"));
        psinsert.setBigDecimal(69, new BigDecimal("7"));
        psinsert.setBigDecimal(70, new BigDecimal(bean.getWindDirectionAt2m().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(71, new BigDecimal(bean.getWindSpeedAvg2m().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(72, new BigDecimal(bean.getWindDirectionAt10().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(73, new BigDecimal(bean.getWindSpeedAt10().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(74, new BigDecimal(bean.getWindDirectionOfMaxSpeed().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(75, new BigDecimal(bean.getWindDirectionCurrent().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(76, new BigDecimal(bean.getWetBulbTemperature().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(77, new BigDecimal(bean.getSoilTemperature5CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(78, new BigDecimal(bean.getSoilTemperature10CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(79, new BigDecimal(bean.getSoilTemperature15CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(80, new BigDecimal(bean.getSoilTemperature20CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(81, new BigDecimal(bean.getSoilTemperature40CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(82, new BigDecimal(bean.getSoilTemperature80CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(83, new BigDecimal(bean.getSoilTemperature160CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(84, new BigDecimal(bean.getSoilTemperature320CM().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(85, new BigDecimal(bean.getLandSurfaceTemperature().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(86, new BigDecimal(bean.getGrassTemperature().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(87, new BigDecimal(bean.getRelativeHumidity().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(88, new BigDecimal(bean.getVapourPressur().getQuality().get(0).getCode()));
        psinsert.setBigDecimal(89, new BigDecimal("7"));
        psinsert.setBigDecimal(90, new BigDecimal("7"));
        psinsert.setBigDecimal(91, new BigDecimal("7"));
        psinsert.setBigDecimal(92, new BigDecimal("7"));
        psinsert.setBigDecimal(93, new BigDecimal("7"));
        psinsert.setBigDecimal(94, new BigDecimal("7"));

        /*psinsert.setBigDecimal(95, new BigDecimal("5"));
        psinsert.setBigDecimal(96, new BigDecimal("5"));
        psinsert.setBigDecimal(97, new BigDecimal("5"));
        psinsert.setBigDecimal(98, new BigDecimal("5"));
        psinsert.setBigDecimal(99, new BigDecimal("5"));
        psinsert.setBigDecimal(100, new BigDecimal("5"));
        psinsert.setBigDecimal(101, new BigDecimal("5"));
        psinsert.setBigDecimal(102, new BigDecimal("5"));
        psinsert.setBigDecimal(103, new BigDecimal("5"));
        psinsert.setBigDecimal(104, new BigDecimal("5"));*/


    }

}
