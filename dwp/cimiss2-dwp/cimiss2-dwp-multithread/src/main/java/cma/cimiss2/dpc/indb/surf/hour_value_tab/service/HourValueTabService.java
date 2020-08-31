package cma.cimiss2.dpc.indb.surf.hour_value_tab.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutMyerTab;
import cma.cimiss2.dpc.decoder.bean.upar.UparChnMulFtmMadTab;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.HourValueTab;
import cma.cimiss2.dpc.decoder.fileDecode.util.CommonUtil;
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
 * @date ：Created in 2019/10/29 0029 13:20
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class HourValueTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");


    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        HourValueTabService.diQueues = diQueues;
    }

    /**
     * 执行错误时，可能数据冲突，分批执行
     * @param ps
     * @param cts_code
     */
    public static void excuteByDataSingle(PreparedStatement ps, List<HourValueTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<HourValueTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (HourValueTab HourValueTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(HourValueTab);
                try {
                    ps.setString(ii++,HourValueTab.getD_MAIN_ID());//记录标识
                    if(null!=HourValueTab.getD_DATA_ID()) {
                        ps.setString(ii++, HourValueTab.getD_DATA_ID());//资料标识
                    }else{
                        ps.setString(ii++,"999999");//资料标识
                    }
                    ps.setString(ii++,HourValueTab.getD_IYMDHM());//入库时间
                    ps.setString(ii++,HourValueTab.getD_RYMDHM());//收到时间
                    ps.setString(ii++,HourValueTab.getD_UPDATE_TIME());//更新时间
                    ps.setString(ii++,HourValueTab.getD_DATETIME());//资料时间
                    ps.setString(ii++,"999999");//数据来源
                    ps.setInt(ii++,HourValueTab.getV04001());//年
                    ps.setInt(ii++,HourValueTab.getV04002());//月
                    ps.setInt(ii++,HourValueTab.getV04003());//日
                    ps.setInt(ii++,HourValueTab.getV04004());//时
                    ps.setString(ii++,HourValueTab.getV01301());//区站号/观测平台标识(字符)
                    if(CommonUtil.isNumeric(HourValueTab.getV01300())){
                        ps.setInt(ii++,Integer.parseInt(HourValueTab.getV01300()));//区站号/观测平台标识(数字)
                    }else{
                        ps.setInt(ii++,999999);//区站号/观测平台标识(数字)
                    }

                    ps.setFloat(ii++,HourValueTab.getV05001());//纬度
                    ps.setFloat(ii++,HourValueTab.getV06001());//经度
                    ps.setFloat(ii++,HourValueTab.getV07001());//测站高度
                    ps.setFloat(ii++,HourValueTab.getV07031());//气压传感器海拔高度
                    ps.setFloat(ii++,HourValueTab.getV07032_04());//风速传感器距地面高度
                    ps.setFloat(ii++,999999);//温湿传感器离地面高度
                    ps.setFloat(ii++,999999);//能见度传感器离地面高度
                    ps.setInt(ii++,Integer.parseInt(HourValueTab.getV02001()));//测站类型
                    ps.setInt(ii++,HourValueTab.getV02301());//测站级别
                    ps.setString(ii++,HourValueTab.getV_ACODE()+"");//中国行政区划代码
                    ps.setFloat(ii++,999999);//地面限定符（温度数据）
                    ps.setFloat(ii++,999999);//云探测系统
                    ps.setString(ii++,HourValueTab.getV_BBB());//更正报标志
                    ps.setFloat(ii++,HourValueTab.getV10004());//气压
                    ps.setFloat(ii++,HourValueTab.getV10051());//海平面气压
                    ps.setFloat(ii++,HourValueTab.getV10061());//3小时变压
                    ps.setFloat(ii++,HourValueTab.getV10062());//24小时变压
                    ps.setFloat(ii++,HourValueTab.getV10301());//最高本站气压
                    ps.setInt(ii++,HourValueTab.getV10301_052());//最高本站气压出现时间
                    ps.setFloat(ii++,HourValueTab.getV10302());//最低本站气压
                    ps.setInt(ii++,HourValueTab.getV10302_052());//最低本站气压出现时间
                    ps.setFloat(ii++,HourValueTab.getV12001());//温度/气温
                    ps.setFloat(ii++,HourValueTab.getV12011());//最高气温
                    ps.setInt(ii++,HourValueTab.getV12011_052());//最高气温出现时间
                    ps.setFloat(ii++,HourValueTab.getV12012());//最低气温
                    ps.setInt(ii++,HourValueTab.getV12012_052());//最低气温出现时间
                    ps.setFloat(ii++,HourValueTab.getV12405());//过去24小时变温
                    ps.setFloat(ii++,HourValueTab.getV12016());//过去24小时最高气温
                    ps.setFloat(ii++,HourValueTab.getV12017());//过去24小时最低气温
                    ps.setFloat(ii++,HourValueTab.getV12003());//露点温度
                    ps.setInt(ii++,HourValueTab.getV13003());//相对湿度
                    if(null!=HourValueTab.getV13007()) {
                        String t = HourValueTab.getV13007() + "";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0, t.indexOf("."))));//最小相对湿度
                    }else{
                        ps.setInt(ii++,999999);//最小相对湿度
                    }
                    ps.setInt(ii++,HourValueTab.getV13007_052());//最小相对湿度出现时间
                    ps.setFloat(ii++,HourValueTab.getV13004());//水汽压
                    ps.setFloat(ii++,HourValueTab.getV13019());//过去1小时降水量
                    ps.setFloat(ii++,HourValueTab.getV13020());//过去3小时降水量
                    ps.setFloat(ii++,HourValueTab.getV13021());//过去6小时降水量
                    ps.setFloat(ii++,HourValueTab.getV13022());//过去12小时降水量
                    ps.setFloat(ii++,HourValueTab.getV13023());//过去24小时降水量
                    ps.setInt(ii++,HourValueTab.getV04080_04());//人工加密观测降水量描述周期
                    ps.setFloat(ii++,HourValueTab.getV13011());//降水量
                    ps.setFloat(ii++,HourValueTab.getV13033());//蒸发(大型)
                    ps.setInt(ii++,Integer.parseInt(HourValueTab.getV11290()));//2分钟平均风向(角度)
                    ps.setFloat(ii++,HourValueTab.getV11291());//2分钟平均风速
                    ps.setInt(ii++,Integer.parseInt(HourValueTab.getV11292()));//10分钟平均风向(角度)
                    ps.setFloat(ii++,HourValueTab.getV11293());//10分钟平均风速
                    ps.setFloat(ii++,Float.parseFloat(HourValueTab.getV11296()));//日最大风速的风向(角度)
                    ps.setFloat(ii++,HourValueTab.getV11042());//最大风速
                    ps.setInt(ii++,HourValueTab.getV11042_052());//最大风速出现时间
                    ps.setInt(ii++,Integer.parseInt(HourValueTab.getV11201()));//瞬时风向(角度)
                    ps.setFloat(ii++,HourValueTab.getV11202());//瞬时风速
                    if(null!=HourValueTab.getV11211()){
                        String t=HourValueTab.getV11211()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))+""));//极大风速的风向(角度)
                    }else{
                        ps.setInt(ii++,999999);//极大风速的风向(角度)
                    }
                    if(null!=HourValueTab.getV11046()){
                        ps.setFloat(ii++,HourValueTab.getV11046());//极大风速
                    }else{
                        ps.setFloat(ii++,999999);//极大风速的风向(角度)
                    }
                    ps.setInt(ii++,HourValueTab.getV11046_052());//极大风速出现时间
                    ps.setFloat(ii++,HourValueTab.getV11503_06());//过去6小时极大瞬时风向
                    ps.setFloat(ii++,HourValueTab.getV11504_06());//过去6小时极大瞬时风速
                    ps.setFloat(ii++,HourValueTab.getV11503_12());//过去12小时极大瞬时风向
                    ps.setFloat(ii++,HourValueTab.getV11504_12());//过去12小时极大瞬时风速
                    ps.setFloat(ii++,HourValueTab.getV12120());//地面温度
                    ps.setFloat(ii++,HourValueTab.getV12311());//最高地面温度
                    ps.setInt(ii++,HourValueTab.getV12311_052());//最高地面温度出现时间
                    ps.setFloat(ii++,HourValueTab.getV12121());//最低地面温度
                    ps.setInt(ii++,HourValueTab.getV12121_052());//最低地面温度出现时间
                    ps.setFloat(ii++,HourValueTab.getV12013());//过去12小时地面最低温度
                    ps.setFloat(ii++,HourValueTab.getV12030_005());//5cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_010());//10cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_015());//15cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_020());//20cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_040());//40cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_080());//80cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_160());//160cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_320());//320cm地温
                    ps.setFloat(ii++,HourValueTab.getV12314());//草面(雪面)温度
                    ps.setFloat(ii++,HourValueTab.getV12315());//草面(雪面)最高温度
                    ps.setInt(ii++,HourValueTab.getV12315_052());//草面(雪面)最高温度出现时间
                    ps.setFloat(ii++,HourValueTab.getV12316());//草面(雪面)最低温度
                    ps.setInt(ii++,HourValueTab.getV12316_052());//草面(雪面)最低温度出现时间
                    if(null!=HourValueTab.getV20001_701_01()){
                        String t=HourValueTab.getV20001_701_01()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))));//1分钟平均能见度
                    }else{
                        ps.setInt(ii++,999999);//1分钟平均能见度
                    }
                    if(null!=HourValueTab.getV20001_701_10()){
                        String t=HourValueTab.getV20001_701_10()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))));//10分钟平均能见度
                    }else{
                        ps.setInt(ii++,999999);//10分钟平均能见度
                    }
                    if(null!=HourValueTab.getV20059()){
                        String t=HourValueTab.getV20059()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))));//最小水平能见度
                    }else{
                        ps.setInt(ii++,999999);//最小水平能见度
                    }
                    if(null!=HourValueTab.getV20059_052()){
                        String t=HourValueTab.getV20059_052()+"";
                        ps.setInt(ii++,Integer.parseInt(t));//最小水平能见度出现时间
                    }else{
                        ps.setInt(ii++,999999);//最小水平能见度出现时间
                    }
                    if(null!=HourValueTab.getV20001()){
                        String t=HourValueTab.getV20001()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))));//水平能见度(人工)
                    }else{
                        ps.setInt(ii++,999999);//水平能见度(人工)
                    }
                    ps.setInt(ii++,HourValueTab.getV20010());//总云量
                    ps.setInt(ii++,HourValueTab.getV20051());//低云量
                    if(null!=HourValueTab.getV20011()){
                        String t=HourValueTab.getV20011()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))));//云量(低云或中云)
                    }else{
                        ps.setInt(ii++,999999);//云量(低云或中云)
                    }
                    if(null!=HourValueTab.getV20013()){
                        String t=HourValueTab.getV20013()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))));//云底高度
                    }else{
                        ps.setInt(ii++,999999);//云底高度
                    }

                    ps.setInt(ii++,HourValueTab.getV20350_01());//云状1
                    ps.setInt(ii++,Integer.parseInt(HourValueTab.getV20350_02()));//云状2
                    ps.setInt(ii++,HourValueTab.getV20350_03());//云状3
                    ps.setInt(ii++,HourValueTab.getV20350_04());//云状4
                    ps.setInt(ii++,HourValueTab.getV20350_05());//云状5
                    ps.setInt(ii++,HourValueTab.getV20350_06());//云状6
                    ps.setInt(ii++,HourValueTab.getV20350_07());//云状7
                    ps.setInt(ii++,HourValueTab.getV20350_08());//云状8
                    ps.setInt(ii++,HourValueTab.getV20350_11());//低云状
                    ps.setInt(ii++,HourValueTab.getV20350_12());//中云状
                    ps.setInt(ii++,HourValueTab.getV20350_13());//高云状
                    ps.setInt(ii++,HourValueTab.getV20003());//现在天气
                    ps.setInt(ii++,HourValueTab.getV04080_05());//过去天气描述事件周期
                    ps.setInt(ii++,HourValueTab.getV20004());//过去天气1
                    ps.setInt(ii++,HourValueTab.getV20005());//过去天气2
                    ps.setInt(ii++,HourValueTab.getV20062());//地面状态
                    ps.setFloat(ii++,HourValueTab.getV13013());//积雪深度
                    ps.setFloat(ii++,HourValueTab.getV13330());//雪压
                    ps.setFloat(ii++,HourValueTab.getV20330_01());//第一冻土层上界值
                    ps.setFloat(ii++,HourValueTab.getV20331_01());//第一冻土层下界值
                    ps.setFloat(ii++,HourValueTab.getV20330_02());//第二冻土层上界值
                    ps.setFloat(ii++,HourValueTab.getV20331_02());//第二冻土层下界值
                    ps.setInt(ii++,HourValueTab.getQ10004());//气压质控码
                    ps.setInt(ii++,HourValueTab.getQ10051());//海平面气压质量控制标志
                    ps.setInt(ii++,HourValueTab.getQ10061());//3小时变压质控码
                    ps.setInt(ii++,HourValueTab.getQ10062());//24小时变压质控码
                    ps.setInt(ii++,HourValueTab.getQ10301());//日最高本站气压质控码
                    ps.setInt(ii++,HourValueTab.getQ10301_052());//日最高本站气压出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ10302());//日最低本站气压质控码
                    ps.setInt(ii++,HourValueTab.getQ10302_052());//日最低本站气压出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ12001());//温度/气温质控码
                    ps.setInt(ii++,HourValueTab.getQ12011());//日最高气温质控码
                    ps.setInt(ii++,HourValueTab.getQ12011_052());//日最高气温出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ12012());//1小时内最低气温质控码
                    ps.setInt(ii++,HourValueTab.getQ12012_052());//小时内最低气温出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ12405());//24小时变温质控码
                    ps.setInt(ii++,HourValueTab.getQ12016());//过去24小时最高气温质控码
                    ps.setInt(ii++,HourValueTab.getQ12017());//过去24小时最低气温质控码
                    ps.setInt(ii++,HourValueTab.getQ12003());//露点温度质控码
                    ps.setInt(ii++,HourValueTab.getQ13003());//相对湿度质控码
                    ps.setInt(ii++,HourValueTab.getQ13007());//最小相对湿度质控码
                    ps.setInt(ii++,HourValueTab.getQ13007_052());//最小相对湿度出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ13004());//水汽压质控码
                    ps.setInt(ii++,HourValueTab.getQ13019());//小时降水量质控码
                    ps.setInt(ii++,HourValueTab.getQ13020());//过去3小时降水量质控码
                    ps.setInt(ii++,HourValueTab.getQ13021());//过去6小时降水量质控码
                    ps.setInt(ii++,HourValueTab.getQ13022());//过去12小时降水量质控码
                    ps.setInt(ii++,HourValueTab.getQ13023());//24小时降水量质控码
                    ps.setInt(ii++,HourValueTab.getQ04080_04());//人工加密观测降水量描述时间周期质控码
                    ps.setInt(ii++,HourValueTab.getQ13011());//分钟降水量质控码
                    ps.setInt(ii++,HourValueTab.getQ13033());//日蒸发量（大型）质控码
                    ps.setInt(ii++,HourValueTab.getQ11290());//2分钟平均风向质控码值
                    ps.setInt(ii++,HourValueTab.getQ11291());//2分钟平均风速成质控码值
                    ps.setInt(ii++,HourValueTab.getQ11292());//10分钟风向质控码
                    ps.setInt(ii++,HourValueTab.getQ11293());//10分钟平均风速质控码
                    ps.setInt(ii++,HourValueTab.getQ11296());//日最大风速的风向质控码
                    ps.setInt(ii++,HourValueTab.getQ11042());//日最大风速质控码
                    ps.setInt(ii++,HourValueTab.getQ11042_052());//日最大风速出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ11201());//瞬时风向(角度)质控码
                    ps.setInt(ii++,HourValueTab.getQ11202());//瞬时风速质控码
                    ps.setInt(ii++,HourValueTab.getQ11211());//日极大风速的风向质控码
                    ps.setInt(ii++,HourValueTab.getQ11046());//日极大风速质控码
                    ps.setInt(ii++,HourValueTab.getQ11046_052());//日极大风速出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ11503_06());//过去6小时极大瞬时风向质控码
                    ps.setInt(ii++,HourValueTab.getQ11504_06());//过去6小时极大瞬时风速质控码
                    ps.setInt(ii++,HourValueTab.getQ11503_12());//过去12小时极大瞬时风向质控码
                    ps.setInt(ii++,HourValueTab.getQ11504_12());//过去12小时极大瞬时风速质控码
                    ps.setInt(ii++,HourValueTab.getQ12120());//地面温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12311());//日最高地面温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12311_052());//日最高地面温度出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ12121());//日最低地面温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12121_052());//日最低地面温度出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ12013());//过去12小时最低地面温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_005());//5cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_010());//10cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_015());//15cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_020());//20cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_040());//40cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_080());//80cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_160());//160cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_320());//320cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12314());//草面（雪面）温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12315());//日草面（雪面）最高温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12315_052());//日草面（雪面）最高温度出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ12316());//日草面（雪面）最低温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12316_052());//日草面（雪面）最低温度出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ20001_701_01());//1分钟平均水平能见度质控码
                    ps.setInt(ii++,HourValueTab.getQ20001_701_10());//10分钟平均水平能见度质控码
                    ps.setInt(ii++,HourValueTab.getQ20059());//日最小水平能见度质控码
                    ps.setInt(ii++,HourValueTab.getQ20059_052());//日最小水平能见度出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ20001());//水平能见度质控码
                    ps.setInt(ii++,HourValueTab.getQ20010());//总云量质控码
                    ps.setInt(ii++,HourValueTab.getQ20051());//低云量质控码
                    ps.setInt(ii++,HourValueTab.getQ20011());//低云或中云的云量质控码
                    ps.setInt(ii++,HourValueTab.getQ20013());//云底高度质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_01());//云状1质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_02());//云状2质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_03());//云状3质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_04());//云状4质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_05());//云状5质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_06());//云状6质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_07());//云状7质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_08());//云状8质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_11());//低云状质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_12());//中云状质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_13());//高云状质控码
                    ps.setInt(ii++,HourValueTab.getQ20003());//现在天气质控码
                    ps.setInt(ii++,HourValueTab.getQ04080_05());//过去天气描述时间周期质控码
                    ps.setInt(ii++,HourValueTab.getQ20004());//过去天气1质控码
                    ps.setInt(ii++,HourValueTab.getQ20005());//过去天气2质控码
                    ps.setInt(ii++,HourValueTab.getQ20062());//地面状态质控码
                    ps.setInt(ii++,HourValueTab.getQ13013());//路面雪层厚度质控码
                    ps.setInt(ii++,HourValueTab.getQ13330());//雪压质控码
                    ps.setInt(ii++,HourValueTab.getQ20330_01());//第一冻土层上界值质控码
                    ps.setInt(ii++,HourValueTab.getQ20331_01());//第一冻土层下界值质控码
                    ps.setInt(ii++,HourValueTab.getQ20330_02());//第二冻土层上界值质控码
                    ps.setInt(ii++,HourValueTab.getQ20331_02());//第二冻土层下界值质控码
                    ps.setFloat(ii++,999999);//保留字段1
                    ps.setFloat(ii++,999999);//保留字段2
                    ps.setFloat(ii++,999999);//保留字段3
                    ps.setFloat(ii++,999999);//保留字段4
                    ps.setFloat(ii++,999999);//保留字段5
                    ps.setFloat(ii++,999999);//保留字段6
                    ps.setFloat(ii++,999999);//保留字段7
                    ps.setFloat(ii++,999999);//保留字段8
                    ps.setFloat(ii++,999999);//保留字段9
                    ps.setFloat(ii++,999999);//保留字段10
                    ps.setInt(ii++,9);//冰雹的最大重量质控码
                    ps.setFloat(ii++,999999);//天气现象检测系统
                    ps.setFloat(ii++,999999);//蒸发水位
                    ps.setFloat(ii++,999999);//地面状态测量方法
                    ps.setFloat(ii++,999999);//积雪深度的测量方法
                    ps.setFloat(ii++,999999);//降水测量方法
                    ps.setFloat(ii++,999999);//冰雹的最大重量
                    ps.setInt(ii++,999999);//电线积冰-现象
                    ps.setFloat(ii++,999999);//电线积冰-南北方向直径
                    ps.setFloat(ii++,999999);//电线积冰-南北方向厚度
                    ps.setFloat(ii++,999999);//电线积冰-南北方向重量
                    ps.setFloat(ii++,999999);//电线积冰-东西方向直径
                    ps.setFloat(ii++,999999);//电线积冰-东西方向厚度
                    ps.setFloat(ii++,999999);//电线积冰-东西方向重量
                    ps.setFloat(ii++,999999);//电线积冰－风向
                    ps.setFloat(ii++,999999);//电线积冰－风速
                    ps.setString(ii++,"999999");//天气现象记录
                    ps.setInt(ii++,9);//电线积冰-现象质控码
                    ps.setInt(ii++,9);//电线积冰-南北方向直径质控码
                    ps.setInt(ii++,9);//电线积冰-南北方向厚度质控码
                    ps.setInt(ii++,9);//电线积冰-南北方向重量质控码
                    ps.setInt(ii++,9);//电线积冰-东西方向直径质控码
                    ps.setInt(ii++,9);//电线积冰-东西方向厚度质控码
                    ps.setInt(ii++,9);//电线积冰-东西方向重量质控码
                    ps.setInt(ii++,9);//电线积冰－风向质控码
                    ps.setInt(ii++,9);//电线积冰－风速质控码
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
                            excuteByDataSingle(ps, list1, cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=HourValueTab.getV01301()+" "+HourValueTab.getD_DATETIME()+"  ";
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
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param filename
     * @return
     */
    public static DataBaseAction processSuccessReport(ParseResult<HourValueTab> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;
        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        List<HourValueTab> pollenLists = parseResult.getData();
        insertDB(pollenLists, connection, recv_time, loggerBuffer, filename, cts_code);
        return DataBaseAction.SUCCESS;

    }

    /**
     * 入库
     *  @param parseResult  数据
     * @param connection
     * @param recv_time    消息接收时间
     * @param loggerBuffer
     * @param filename
     * @param cts_code
     */
    private static void insertDB(List<HourValueTab> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("R022_value_table_name")  + "("
                    + "D_RETAIN_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,D_SOURCE_ID," +
                    "V04001,V04002,V04003,V04004,V01301,V01300,V05001,V06001," +
                    "V07001,V07031,V07032_04,V07032_01,V07032_02,V02001,V02301,V_ACODE,V08010,V02183,V_BBB,V10004,V10051,V10061,V10062," +
                    "V10301,V10301_052,V10302,V10302_052,V12001,V12011,V12011_052,V12012,V12012_052,V12405,V12016," +
                    "V12017,V12003,V13003,V13007,V13007_052,V13004,V13019,V13020,V13021,V13022," +
                    "V13023,V04080_04,V13011,V13033,V11290,V11291,V11292,V11293,V11296,V11042,V11042_052," +
                    "V11201,V11202,V11211,V11046,V11046_052,V11503_06,V11504_06,V11503_12,V11504_12," +
                    "V12120,V12311,V12311_052,V12121,V12121_052,V12013,V12030_005,V12030_010," +
                    "V12030_015,V12030_020,V12030_040,V12030_080,V12030_160,V12030_320,V12314," +
                    "V12315,V12315_052,V12316,V12316_052,V20001_701_01,V20001_701_10,V20059,V20059_052,V20001,V20010,V20051," +
                    "V20011,V20013,V20350_01,V20350_02,V20350_03,V20350_04,V20350_05,V20350_06,V20350_07,V20350_08,V20350_11,V20350_12," +
                    "V20350_13,V20003,V04080_05,V20004,V20005,V20062,V13013,V13330,V20330_01,V20331_01,V20330_02,V20331_02,Q10004,Q10051," +
                    "Q10061,Q10062,Q10301,Q10301_052,Q10302,Q10302_052,Q12001,Q12011,Q12011_052,Q12012,Q12012_052,Q12405,Q12016,Q12017," +
                    "Q12003,Q13003,Q13007,Q13007_052,Q13004,Q13019,Q13020,Q13021,Q13022,Q13023,Q04080_04,Q13011,Q13033,Q11290,Q11291," +
                    "Q11292,Q11293,Q11296,Q11042,Q11042_052,Q11201,Q11202,Q11211,Q11046,Q11046_052,Q11503_06,Q11504_06,Q11503_12," +
                    "Q11504_12,Q12120,Q12311,Q12311_052,Q12121,Q12121_052,Q12013,Q12030_005,Q12030_010,Q12030_015,Q12030_020," +
                    "Q12030_040,Q12030_080,Q12030_160,Q12030_320,Q12314,Q12315,Q12315_052,Q12316,Q12316_052,Q20001_701_01," +
                    "Q20001_701_10,Q20059,Q20059_052,Q20001,Q20010,Q20051,Q20011,Q20013,Q20350_01,Q20350_02,Q20350_03," +
                    "Q20350_04,Q20350_05,Q20350_06,Q20350_07,Q20350_08,Q20350_11,Q20350_12,Q20350_13,Q20003,Q04080_05," +
                    "Q20004,Q20005,Q20062,Q13013,Q13330,Q20330_01,Q20331_01,Q20330_02,Q20331_02," +
                    "V_RETAIN1,V_RETAIN2,V_RETAIN3,V_RETAIN4,V_RETAIN5,V_RETAIN6,V_RETAIN7,V_RETAIN8,V_RETAIN9,V_RETAIN10," +
                    "Q20214," +
                    "V02180,V13196,V02176,V02177,V02175,V20214,V20305,V20326_NS,V20306_NS,V20307_NS,V20326_WE,V20306_WE,V20307_WE," +
                    "V11001,V11002,V20304,Q20305,Q20326_NS,Q20306_NS,Q20307_NS,Q20326_WE,Q20306_WE,Q20307_WE,Q11001,Q11002)VALUES (" +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;
            List<HourValueTab> list=new ArrayList();
            for (HourValueTab HourValueTab : parseResult) {
                int ii = 1;
                index += 1;
                list.add(HourValueTab);
                try {
                    ps.setString(ii++,HourValueTab.getD_MAIN_ID());//记录标识
                    if(null!=HourValueTab.getD_DATA_ID()) {
                        ps.setString(ii++, HourValueTab.getD_DATA_ID());//资料标识
                    }else{
                        ps.setString(ii++,"999999");//资料标识
                    }
                    ps.setString(ii++,HourValueTab.getD_IYMDHM());//入库时间
                    ps.setString(ii++,HourValueTab.getD_RYMDHM());//收到时间
                    ps.setString(ii++,HourValueTab.getD_UPDATE_TIME());//更新时间
                    ps.setString(ii++,HourValueTab.getD_DATETIME());//资料时间
                    ps.setString(ii++,"999999");//数据来源
                    ps.setInt(ii++,HourValueTab.getV04001());//年
                    ps.setInt(ii++,HourValueTab.getV04002());//月
                    ps.setInt(ii++,HourValueTab.getV04003());//日
                    ps.setInt(ii++,HourValueTab.getV04004());//时
                    ps.setString(ii++,HourValueTab.getV01301());//区站号/观测平台标识(字符)
                    if(CommonUtil.isNumeric(HourValueTab.getV01300())){
                        ps.setInt(ii++,Integer.parseInt(HourValueTab.getV01300()));//区站号/观测平台标识(数字)
                    }else{
                        ps.setInt(ii++,999999);//区站号/观测平台标识(数字)
                    }

                    ps.setFloat(ii++,HourValueTab.getV05001());//纬度
                    ps.setFloat(ii++,HourValueTab.getV06001());//经度
                    ps.setFloat(ii++,HourValueTab.getV07001());//测站高度
                    ps.setFloat(ii++,HourValueTab.getV07031());//气压传感器海拔高度
                    ps.setFloat(ii++,HourValueTab.getV07032_04());//风速传感器距地面高度
                    ps.setFloat(ii++,999999);//温湿传感器离地面高度
                    ps.setFloat(ii++,999999);//能见度传感器离地面高度
                    ps.setInt(ii++,Integer.parseInt(HourValueTab.getV02001()));//测站类型
                    ps.setInt(ii++,HourValueTab.getV02301());//测站级别
                    ps.setString(ii++,HourValueTab.getV_ACODE()+"");//中国行政区划代码
                    ps.setFloat(ii++,999999);//地面限定符（温度数据）
                    ps.setFloat(ii++,999999);//云探测系统
                    ps.setString(ii++,HourValueTab.getV_BBB());//更正报标志
                    ps.setFloat(ii++,HourValueTab.getV10004());//气压
                    ps.setFloat(ii++,HourValueTab.getV10051());//海平面气压
                    ps.setFloat(ii++,HourValueTab.getV10061());//3小时变压
                    ps.setFloat(ii++,HourValueTab.getV10062());//24小时变压
                    ps.setFloat(ii++,HourValueTab.getV10301());//最高本站气压
                    ps.setInt(ii++,HourValueTab.getV10301_052());//最高本站气压出现时间
                    ps.setFloat(ii++,HourValueTab.getV10302());//最低本站气压
                    ps.setInt(ii++,HourValueTab.getV10302_052());//最低本站气压出现时间
                    ps.setFloat(ii++,HourValueTab.getV12001());//温度/气温
                    ps.setFloat(ii++,HourValueTab.getV12011());//最高气温
                    ps.setInt(ii++,HourValueTab.getV12011_052());//最高气温出现时间
                    ps.setFloat(ii++,HourValueTab.getV12012());//最低气温
                    ps.setInt(ii++,HourValueTab.getV12012_052());//最低气温出现时间
                    ps.setFloat(ii++,HourValueTab.getV12405());//过去24小时变温
                    ps.setFloat(ii++,HourValueTab.getV12016());//过去24小时最高气温
                    ps.setFloat(ii++,HourValueTab.getV12017());//过去24小时最低气温
                    ps.setFloat(ii++,HourValueTab.getV12003());//露点温度
                    ps.setInt(ii++,HourValueTab.getV13003());//相对湿度
                    if(null!=HourValueTab.getV13007()) {
                        String t = HourValueTab.getV13007() + "";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0, t.indexOf("."))));//最小相对湿度
                    }else{
                        ps.setInt(ii++,999999);//最小相对湿度
                    }
                    ps.setInt(ii++,HourValueTab.getV13007_052());//最小相对湿度出现时间
                    ps.setFloat(ii++,HourValueTab.getV13004());//水汽压
                    ps.setFloat(ii++,HourValueTab.getV13019());//过去1小时降水量
                    ps.setFloat(ii++,HourValueTab.getV13020());//过去3小时降水量
                    ps.setFloat(ii++,HourValueTab.getV13021());//过去6小时降水量
                    ps.setFloat(ii++,HourValueTab.getV13022());//过去12小时降水量
                    ps.setFloat(ii++,HourValueTab.getV13023());//过去24小时降水量
                    ps.setInt(ii++,HourValueTab.getV04080_04());//人工加密观测降水量描述周期
                    ps.setFloat(ii++,HourValueTab.getV13011());//降水量
                    ps.setFloat(ii++,HourValueTab.getV13033());//蒸发(大型)
                    ps.setInt(ii++,Integer.parseInt(HourValueTab.getV11290()));//2分钟平均风向(角度)
                    ps.setFloat(ii++,HourValueTab.getV11291());//2分钟平均风速
                    ps.setInt(ii++,Integer.parseInt(HourValueTab.getV11292()));//10分钟平均风向(角度)
                    ps.setFloat(ii++,HourValueTab.getV11293());//10分钟平均风速
                    ps.setFloat(ii++,Float.parseFloat(HourValueTab.getV11296()));//日最大风速的风向(角度)
                    ps.setFloat(ii++,HourValueTab.getV11042());//最大风速
                    ps.setInt(ii++,HourValueTab.getV11042_052());//最大风速出现时间
                    ps.setInt(ii++,Integer.parseInt(HourValueTab.getV11201()));//瞬时风向(角度)
                    ps.setFloat(ii++,HourValueTab.getV11202());//瞬时风速
                    if(null!=HourValueTab.getV11211()){
                        String t=HourValueTab.getV11211()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))+""));//极大风速的风向(角度)
                    }else{
                        ps.setInt(ii++,999999);//极大风速的风向(角度)
                    }
                    if(null!=HourValueTab.getV11046()){
                        ps.setFloat(ii++,HourValueTab.getV11046());//极大风速
                    }else{
                        ps.setFloat(ii++,999999);//极大风速的风向(角度)
                    }
                    ps.setInt(ii++,HourValueTab.getV11046_052());//极大风速出现时间
                    ps.setFloat(ii++,HourValueTab.getV11503_06());//过去6小时极大瞬时风向
                    ps.setFloat(ii++,HourValueTab.getV11504_06());//过去6小时极大瞬时风速
                    ps.setFloat(ii++,HourValueTab.getV11503_12());//过去12小时极大瞬时风向
                    ps.setFloat(ii++,HourValueTab.getV11504_12());//过去12小时极大瞬时风速
                    ps.setFloat(ii++,HourValueTab.getV12120());//地面温度
                    ps.setFloat(ii++,HourValueTab.getV12311());//最高地面温度
                    ps.setInt(ii++,HourValueTab.getV12311_052());//最高地面温度出现时间
                    ps.setFloat(ii++,HourValueTab.getV12121());//最低地面温度
                    ps.setInt(ii++,HourValueTab.getV12121_052());//最低地面温度出现时间
                    ps.setFloat(ii++,HourValueTab.getV12013());//过去12小时地面最低温度
                    ps.setFloat(ii++,HourValueTab.getV12030_005());//5cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_010());//10cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_015());//15cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_020());//20cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_040());//40cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_080());//80cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_160());//160cm地温
                    ps.setFloat(ii++,HourValueTab.getV12030_320());//320cm地温
                    ps.setFloat(ii++,HourValueTab.getV12314());//草面(雪面)温度
                    ps.setFloat(ii++,HourValueTab.getV12315());//草面(雪面)最高温度
                    ps.setInt(ii++,HourValueTab.getV12315_052());//草面(雪面)最高温度出现时间
                    ps.setFloat(ii++,HourValueTab.getV12316());//草面(雪面)最低温度
                    ps.setInt(ii++,HourValueTab.getV12316_052());//草面(雪面)最低温度出现时间
                    if(null!=HourValueTab.getV20001_701_01()){
                        String t=HourValueTab.getV20001_701_01()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))));//1分钟平均能见度
                    }else{
                        ps.setInt(ii++,999999);//1分钟平均能见度
                    }
                    if(null!=HourValueTab.getV20001_701_10()){
                        String t=HourValueTab.getV20001_701_10()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))));//10分钟平均能见度
                    }else{
                        ps.setInt(ii++,999999);//10分钟平均能见度
                    }
                    if(null!=HourValueTab.getV20059()){
                        String t=HourValueTab.getV20059()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))));//最小水平能见度
                    }else{
                        ps.setInt(ii++,999999);//最小水平能见度
                    }
                    if(null!=HourValueTab.getV20059_052()){
                        String t=HourValueTab.getV20059_052()+"";
                        ps.setInt(ii++,Integer.parseInt(t));//最小水平能见度出现时间
                    }else{
                        ps.setInt(ii++,999999);//最小水平能见度出现时间
                    }
                    if(null!=HourValueTab.getV20001()){
                        String t=HourValueTab.getV20001()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))));//水平能见度(人工)
                    }else{
                        ps.setInt(ii++,999999);//水平能见度(人工)
                    }
                    ps.setInt(ii++,HourValueTab.getV20010());//总云量
                    ps.setInt(ii++,HourValueTab.getV20051());//低云量
                    if(null!=HourValueTab.getV20011()){
                        String t=HourValueTab.getV20011()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))));//云量(低云或中云)
                    }else{
                        ps.setInt(ii++,999999);//云量(低云或中云)
                    }
                    if(null!=HourValueTab.getV20013()){
                        String t=HourValueTab.getV20013()+"";
                        ps.setInt(ii++,Integer.parseInt(t.substring(0,t.indexOf("."))));//云底高度
                    }else{
                        ps.setInt(ii++,999999);//云底高度
                    }

                    ps.setInt(ii++,HourValueTab.getV20350_01());//云状1
                    ps.setInt(ii++,Integer.parseInt(HourValueTab.getV20350_02()));//云状2
                    ps.setInt(ii++,HourValueTab.getV20350_03());//云状3
                    ps.setInt(ii++,HourValueTab.getV20350_04());//云状4
                    ps.setInt(ii++,HourValueTab.getV20350_05());//云状5
                    ps.setInt(ii++,HourValueTab.getV20350_06());//云状6
                    ps.setInt(ii++,HourValueTab.getV20350_07());//云状7
                    ps.setInt(ii++,HourValueTab.getV20350_08());//云状8
                    ps.setInt(ii++,HourValueTab.getV20350_11());//低云状
                    ps.setInt(ii++,HourValueTab.getV20350_12());//中云状
                    ps.setInt(ii++,HourValueTab.getV20350_13());//高云状
                    ps.setInt(ii++,HourValueTab.getV20003());//现在天气
                    ps.setInt(ii++,HourValueTab.getV04080_05());//过去天气描述事件周期
                    ps.setInt(ii++,HourValueTab.getV20004());//过去天气1
                    ps.setInt(ii++,HourValueTab.getV20005());//过去天气2
                    ps.setInt(ii++,HourValueTab.getV20062());//地面状态
                    ps.setFloat(ii++,HourValueTab.getV13013());//积雪深度
                    ps.setFloat(ii++,HourValueTab.getV13330());//雪压
                    ps.setFloat(ii++,HourValueTab.getV20330_01());//第一冻土层上界值
                    ps.setFloat(ii++,HourValueTab.getV20331_01());//第一冻土层下界值
                    ps.setFloat(ii++,HourValueTab.getV20330_02());//第二冻土层上界值
                    ps.setFloat(ii++,HourValueTab.getV20331_02());//第二冻土层下界值
                    ps.setInt(ii++,HourValueTab.getQ10004());//气压质控码
                    ps.setInt(ii++,HourValueTab.getQ10051());//海平面气压质量控制标志
                    ps.setInt(ii++,HourValueTab.getQ10061());//3小时变压质控码
                    ps.setInt(ii++,HourValueTab.getQ10062());//24小时变压质控码
                    ps.setInt(ii++,HourValueTab.getQ10301());//日最高本站气压质控码
                    ps.setInt(ii++,HourValueTab.getQ10301_052());//日最高本站气压出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ10302());//日最低本站气压质控码
                    ps.setInt(ii++,HourValueTab.getQ10302_052());//日最低本站气压出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ12001());//温度/气温质控码
                    ps.setInt(ii++,HourValueTab.getQ12011());//日最高气温质控码
                    ps.setInt(ii++,HourValueTab.getQ12011_052());//日最高气温出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ12012());//1小时内最低气温质控码
                    ps.setInt(ii++,HourValueTab.getQ12012_052());//小时内最低气温出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ12405());//24小时变温质控码
                    ps.setInt(ii++,HourValueTab.getQ12016());//过去24小时最高气温质控码
                    ps.setInt(ii++,HourValueTab.getQ12017());//过去24小时最低气温质控码
                    ps.setInt(ii++,HourValueTab.getQ12003());//露点温度质控码
                    ps.setInt(ii++,HourValueTab.getQ13003());//相对湿度质控码
                    ps.setInt(ii++,HourValueTab.getQ13007());//最小相对湿度质控码
                    ps.setInt(ii++,HourValueTab.getQ13007_052());//最小相对湿度出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ13004());//水汽压质控码
                    ps.setInt(ii++,HourValueTab.getQ13019());//小时降水量质控码
                    ps.setInt(ii++,HourValueTab.getQ13020());//过去3小时降水量质控码
                    ps.setInt(ii++,HourValueTab.getQ13021());//过去6小时降水量质控码
                    ps.setInt(ii++,HourValueTab.getQ13022());//过去12小时降水量质控码
                    ps.setInt(ii++,HourValueTab.getQ13023());//24小时降水量质控码
                    ps.setInt(ii++,HourValueTab.getQ04080_04());//人工加密观测降水量描述时间周期质控码
                    ps.setInt(ii++,HourValueTab.getQ13011());//分钟降水量质控码
                    ps.setInt(ii++,HourValueTab.getQ13033());//日蒸发量（大型）质控码
                    ps.setInt(ii++,HourValueTab.getQ11290());//2分钟平均风向质控码值
                    ps.setInt(ii++,HourValueTab.getQ11291());//2分钟平均风速成质控码值
                    ps.setInt(ii++,HourValueTab.getQ11292());//10分钟风向质控码
                    ps.setInt(ii++,HourValueTab.getQ11293());//10分钟平均风速质控码
                    ps.setInt(ii++,HourValueTab.getQ11296());//日最大风速的风向质控码
                    ps.setInt(ii++,HourValueTab.getQ11042());//日最大风速质控码
                    ps.setInt(ii++,HourValueTab.getQ11042_052());//日最大风速出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ11201());//瞬时风向(角度)质控码
                    ps.setInt(ii++,HourValueTab.getQ11202());//瞬时风速质控码
                    ps.setInt(ii++,HourValueTab.getQ11211());//日极大风速的风向质控码
                    ps.setInt(ii++,HourValueTab.getQ11046());//日极大风速质控码
                    ps.setInt(ii++,HourValueTab.getQ11046_052());//日极大风速出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ11503_06());//过去6小时极大瞬时风向质控码
                    ps.setInt(ii++,HourValueTab.getQ11504_06());//过去6小时极大瞬时风速质控码
                    ps.setInt(ii++,HourValueTab.getQ11503_12());//过去12小时极大瞬时风向质控码
                    ps.setInt(ii++,HourValueTab.getQ11504_12());//过去12小时极大瞬时风速质控码
                    ps.setInt(ii++,HourValueTab.getQ12120());//地面温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12311());//日最高地面温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12311_052());//日最高地面温度出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ12121());//日最低地面温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12121_052());//日最低地面温度出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ12013());//过去12小时最低地面温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_005());//5cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_010());//10cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_015());//15cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_020());//20cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_040());//40cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_080());//80cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_160());//160cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12030_320());//320cm地温质控码
                    ps.setInt(ii++,HourValueTab.getQ12314());//草面（雪面）温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12315());//日草面（雪面）最高温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12315_052());//日草面（雪面）最高温度出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ12316());//日草面（雪面）最低温度质控码
                    ps.setInt(ii++,HourValueTab.getQ12316_052());//日草面（雪面）最低温度出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ20001_701_01());//1分钟平均水平能见度质控码
                    ps.setInt(ii++,HourValueTab.getQ20001_701_10());//10分钟平均水平能见度质控码
                    ps.setInt(ii++,HourValueTab.getQ20059());//日最小水平能见度质控码
                    ps.setInt(ii++,HourValueTab.getQ20059_052());//日最小水平能见度出现时间质控码
                    ps.setInt(ii++,HourValueTab.getQ20001());//水平能见度质控码
                    ps.setInt(ii++,HourValueTab.getQ20010());//总云量质控码
                    ps.setInt(ii++,HourValueTab.getQ20051());//低云量质控码
                    ps.setInt(ii++,HourValueTab.getQ20011());//低云或中云的云量质控码
                    ps.setInt(ii++,HourValueTab.getQ20013());//云底高度质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_01());//云状1质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_02());//云状2质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_03());//云状3质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_04());//云状4质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_05());//云状5质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_06());//云状6质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_07());//云状7质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_08());//云状8质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_11());//低云状质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_12());//中云状质控码
                    ps.setInt(ii++,HourValueTab.getQ20350_13());//高云状质控码
                    ps.setInt(ii++,HourValueTab.getQ20003());//现在天气质控码
                    ps.setInt(ii++,HourValueTab.getQ04080_05());//过去天气描述时间周期质控码
                    ps.setInt(ii++,HourValueTab.getQ20004());//过去天气1质控码
                    ps.setInt(ii++,HourValueTab.getQ20005());//过去天气2质控码
                    ps.setInt(ii++,HourValueTab.getQ20062());//地面状态质控码
                    ps.setInt(ii++,HourValueTab.getQ13013());//路面雪层厚度质控码
                    ps.setInt(ii++,HourValueTab.getQ13330());//雪压质控码
                    ps.setInt(ii++,HourValueTab.getQ20330_01());//第一冻土层上界值质控码
                    ps.setInt(ii++,HourValueTab.getQ20331_01());//第一冻土层下界值质控码
                    ps.setInt(ii++,HourValueTab.getQ20330_02());//第二冻土层上界值质控码
                    ps.setInt(ii++,HourValueTab.getQ20331_02());//第二冻土层下界值质控码
                    ps.setFloat(ii++,999999);//保留字段1
                    ps.setFloat(ii++,999999);//保留字段2
                    ps.setFloat(ii++,999999);//保留字段3
                    ps.setFloat(ii++,999999);//保留字段4
                    ps.setFloat(ii++,999999);//保留字段5
                    ps.setFloat(ii++,999999);//保留字段6
                    ps.setFloat(ii++,999999);//保留字段7
                    ps.setFloat(ii++,999999);//保留字段8
                    ps.setFloat(ii++,999999);//保留字段9
                    ps.setFloat(ii++,999999);//保留字段10
                    ps.setInt(ii++,9);//冰雹的最大重量质控码
                    ps.setFloat(ii++,999999);//天气现象检测系统
                    ps.setFloat(ii++,999999);//蒸发水位
                    ps.setFloat(ii++,999999);//地面状态测量方法
                    ps.setFloat(ii++,999999);//积雪深度的测量方法
                    ps.setFloat(ii++,999999);//降水测量方法
                    ps.setFloat(ii++,999999);//冰雹的最大重量
                    ps.setInt(ii++,999999);//电线积冰-现象
                    ps.setFloat(ii++,999999);//电线积冰-南北方向直径
                    ps.setFloat(ii++,999999);//电线积冰-南北方向厚度
                    ps.setFloat(ii++,999999);//电线积冰-南北方向重量
                    ps.setFloat(ii++,999999);//电线积冰-东西方向直径
                    ps.setFloat(ii++,999999);//电线积冰-东西方向厚度
                    ps.setFloat(ii++,999999);//电线积冰-东西方向重量
                    ps.setFloat(ii++,999999);//电线积冰－风向
                    ps.setFloat(ii++,999999);//电线积冰－风速
                    ps.setString(ii++,"999999");//天气现象记录
                    ps.setInt(ii++,9);//电线积冰-现象质控码
                    ps.setInt(ii++,9);//电线积冰-南北方向直径质控码
                    ps.setInt(ii++,9);//电线积冰-南北方向厚度质控码
                    ps.setInt(ii++,9);//电线积冰-南北方向重量质控码
                    ps.setInt(ii++,9);//电线积冰-东西方向直径质控码
                    ps.setInt(ii++,9);//电线积冰-东西方向厚度质控码
                    ps.setInt(ii++,9);//电线积冰-东西方向重量质控码
                    ps.setInt(ii++,9);//电线积冰－风向质控码
                    ps.setInt(ii++,9);//电线积冰－风速质控码
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("列号：" + ii + "有误");
                }
                ps.addBatch();
                if (index % 1000== 0) {
                    try {
                        ps.executeBatch();
                        connection.commit();
                        ps.clearBatch();
                    }catch (Exception e){
                        ps.clearBatch();
                        //执行错误时，单条执行
                        excuteByDataSingle(ps,list,cts_code,connection);
                    }
                    list.clear();
                }
            }
            try {
                ps.executeBatch();
                connection.commit();
            }catch (Exception e){
                ps.clearBatch();
                //执行错误时，单条执行
                excuteByDataSingle(ps,list,cts_code,connection);
            }
            list.clear();
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
