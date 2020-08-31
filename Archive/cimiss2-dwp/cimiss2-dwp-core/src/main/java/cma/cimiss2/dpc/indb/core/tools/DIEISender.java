package cma.cimiss2.dpc.indb.core.tools;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;

/**
 * @Description: diei 发送工具类
 * @Aouthor: xzh
 * @create: 2017-12-29 11:25
 */
public class DIEISender {
    public static boolean GLOBAL_DI_OPTION = true;
    public static boolean GLOBAL_EI_OPTION = true;
    public static boolean LOCAL_DI_OPTION = false;
    public static boolean LOCAL_EI_OPTION = false;

    static {

    }

    /**
     * 发送单条DI
     */
    public static void DI(Map<String, String> diMsg) {
        List<RestfulInfo> diInfo = makeDI(diMsg);
        RestfulSendData.getInstance("config/RestfulInfo.js").SendData(diInfo, 0);
    }


    /**
     * 发送单条EI
     */
    public static void EI(Map<String, String> eiMsg) {
        List<RestfulInfo> eiInfo = makeEI(eiMsg);
        RestfulSendData.getInstance("config/RestfulInfo.js").SendData(eiInfo, 1);

    }

    /**
     * EI 格式化
     *
     * @return
     */
    public static List<RestfulInfo> makeEI(Map<String, String> eiMsg) {
    	
        String event_type = EIEventType.OP_FILE_ERROR.getCode();
        EI ei = EIConfig.getEiConfig(eiMsg.get("path")).getEiMaps().get(event_type);
        List<RestfulInfo> restfulInfos = new ArrayList<>();
        if (ei == null) {
//            logger.error("\n EI配置文件中没有事件类型："+event_type);
        } else {
            ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            ei.setKObject("org.cimiss2.dwp");
            ei.setKEvent("解码入库失败");
            ei.setKIndex("详细信息：" + eiMsg.get("message"));
//            ei.setEVENT_EXT1(fileN);
            RestfulInfo restfulInfo = new RestfulInfo();
            restfulInfo.setType("SYSTEM.ALARM.EI ");
            restfulInfo.setName("数据解码入库EI告警信息");
            restfulInfo.setMessage("数据解码入库EI告警信息");
            restfulInfo.setFields(ei);
            restfulInfos.add(restfulInfo);
        }
        return restfulInfos;
    }
    /**
     * EI 格式化
     *
     * @return
     */
    public static List<RestfulInfo> makeDBEI(Map<String, String> eiMsg) {
    	
        String event_type = EIEventType.DATABASE_CONNECT_ERROR.getCode();
        EI ei = EIConfig.getEiConfig(eiMsg.get("path")).getEiMaps().get(event_type);
        List<RestfulInfo> restfulInfos = new ArrayList<>();
        if (ei == null) {
//            logger.error("\n EI配置文件中没有事件类型："+event_type);
        } else {
            ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            ei.setKObject("org.cimiss2.dwp");
            ei.setKEvent("数据库连接失败");
            ei.setKIndex("详细信息：" + eiMsg.get("message"));
//            ei.setEVENT_EXT1(fileN);
            RestfulInfo restfulInfo = new RestfulInfo();
            restfulInfo.setType("SYSTEM.ALARM.EI ");
            restfulInfo.setName("数据解码入库EI告警信息");
            restfulInfo.setMessage("数据解码入库EI告警信息");
            restfulInfo.setFields(ei);
            restfulInfos.add(restfulInfo);
        }
        return restfulInfos;
    }
    /**
     * DI 格式化 入库成功
     *
     * @return
     */
    public static List<RestfulInfo> makeDI(Map<String, String> diMsg) {

//加入DI
        StatDi di = new StatDi();

        RestfulInfo diInfo = new RestfulInfo();
        diInfo.setType("RT.DPC.STATION.DI");
        diInfo.setName("台站级资料处理详细信息");
        diInfo.setMessage("台站级资料处理详细信息");
        try {
			diInfo.setOccur_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(diMsg.get("time")).getTime());
		} catch (ParseException e) {
			System.out.println("time Transform error!");
		}
        di.setFILE_NAME_O(diMsg.get("fileName"));
        di.setDATA_TYPE(diMsg.get("event_type"));
        di.setDATA_TYPE_1(diMsg.get("event_type1"));
        di.setTT("");
        di.setTRAN_TIME(diMsg.get("time"));
        di.setPROCESS_START_TIME(diMsg.get("time"));
        di.setFILE_NAME_N(diMsg.get("fileName"));
        //di.setBUSINESS_STATE("0"); //0成功，1失败
        //di.setPROCESS_STATE("0");  //0成功，1失败
        di.setBUSINESS_STATE("1"); //1成功，0失败
        di.setPROCESS_STATE("1");  //1成功，0失败
        
        di.setIIiii(diMsg.get("stationNumberChina")); //没有台站，怎么办？
        di.setLONGTITUDE(diMsg.get("longitude"));//经度
        di.setLATITUDE(diMsg.get("latitude"));//纬度
        di.setHEIGHT(diMsg.get("height"));//高度
        di.setDATA_TIME(diMsg.get("time").substring(0,diMsg.get("time").lastIndexOf(":")));
        //di.setPROCESS_END_TIME(diMsg.get("time"));
        di.setPROCESS_END_TIME(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        di.setRECORD_TIME(diMsg.get("time"));
//        di.setFILE_SIZE(diMsg.get("file_size"));
        di.setDATA_FLOW(diMsg.get("dataFlow"));
        diInfo.setFields(di);
        List<RestfulInfo> restfulInfos = new ArrayList<>();
        restfulInfos.add(diInfo);
        return restfulInfos;
    } /**
     * DI 格式化 入库失败
     *
     * @return
     */
    public static List<RestfulInfo> makeDI_fail(Map<String, String> diMsg) {

//加入DI
        StatDi di = new StatDi();

        RestfulInfo diInfo = new RestfulInfo();
        diInfo.setType("RT.DPC.STATION.DI");
        diInfo.setName("台站级资料处理详细信息");
        diInfo.setMessage("台站级资料处理详细信息");
        try {
			diInfo.setOccur_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(diMsg.get("time")).getTime());
		} catch (ParseException e) {
			System.out.println("time Transform error!");
		}
        di.setFILE_NAME_O(diMsg.get("fileName"));
        di.setDATA_TYPE(diMsg.get("event_type"));
        di.setDATA_TYPE_1(diMsg.get("event_type1"));
        di.setTT("");
        di.setTRAN_TIME(diMsg.get("time"));
        di.setPROCESS_START_TIME(diMsg.get("time"));
        di.setFILE_NAME_N(diMsg.get("fileName"));
        //di.setBUSINESS_STATE("0"); //0成功，1失败
        //di.setPROCESS_STATE("1");  //0成功，1失败
        di.setBUSINESS_STATE("1"); //1成功，0失败
        di.setPROCESS_STATE("0");  //1成功，0失败
        
        di.setIIiii(diMsg.get("stationNumberChina")); //没有台站，怎么办？
        di.setLONGTITUDE(diMsg.get("longitude"));//经度
        di.setLATITUDE(diMsg.get("latitude"));//纬度
        di.setHEIGHT(diMsg.get("height"));//高度
        di.setDATA_TIME(diMsg.get("time").substring(0,diMsg.get("time").lastIndexOf(":")));
        //di.setPROCESS_END_TIME(diMsg.get("time"));
        di.setPROCESS_END_TIME(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        di.setRECORD_TIME(diMsg.get("time"));
//        di.setFILE_SIZE(diMsg.get("file_size"));
        di.setDATA_FLOW(diMsg.get("dataFlow"));
        diInfo.setFields(di);
        List<RestfulInfo> restfulInfos = new ArrayList<>();
        restfulInfos.add(diInfo);
        return restfulInfos;
    }

    public static void main(String[] args) {
//       DIEISender.EI("ll","",0);
//        DIEISender.DI("sdfdsf", "dfsdf", "sdfds");
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "xzhTest------");
        map.put("event_type", "event_type------");
        map.put("event_type1", "event_type1------");
        map.put("time", "time------");
        map.put("stationNumberChina", "stationNumberChina------");
        map.put("fileName", "fileName------");
        map.put("longitude","V06001");
        map.put("latitude", "V05001");
        map.put("height", null);
        map.put("file_size", "222222");
//        DI(map);
        List<RestfulInfo> restfulInfos = makeDI(map);
        StatDi fields = (StatDi) restfulInfos.get(0).getFields();
        System.out.println(fields.getDATA_TIME());
        System.out.println(fields.getIIiii());

    }
}
