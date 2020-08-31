package com.thinkgem.jeesite.mybatis.modules.filedecode.common.decode;

import com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean.*;
import com.thinkgem.jeesite.mybatis.modules.filedecode.util.ReflectUtil;

import java.util.List;
import java.util.UUID;

/**
 * 公共信息封装类
 * @author yangkq
 * @version 1.0
 * @date 2020/4/9
 */
public class Decoding {
    private Integer getDayCountOnMonth(String month,String year){
        if("04".equals(month)||"06".equals(month)||"09".equals(month)||"11".equals(month)){
            return 30;
        }else if("02".equals(month)&&Integer.parseInt(year)%4==0){
            return 29;
        }else if("02".equals(month)){
            return 28;
        }else{
            return 31;
        }
    }

    /**
     * 辐射资料日值组装
     * @param dayCtsCode
     * @param list
     */
    public DayValueTab dayRadiData(String dayCtsCode, List<RadiDigChnMulTab> list) {
        DayValueTab dayValueTab=new DayValueTab();
        boolean flag=false;
        for(RadiDigChnMulTab radiDigChnMulTab:list){
            //日太阳总辐射(MJ/m2)
            totalHandle(dayValueTab,"v14311",radiDigChnMulTab);
            //日最大总辐射辐照度(W/m2)、日最大总辐射辐照度时间
            extremeHandle(dayValueTab,"v14311_05","v14311_05","v14021_05_052","v14021_05_052",radiDigChnMulTab,"b");
            //日净全辐射(MJ/m2)
            totalHandle(dayValueTab, "v14312", radiDigChnMulTab);
            //日最大净全辐射辐照度(W/m2)、日最大净全辐射辐照度时间
            extremeHandle(dayValueTab,"v14312_05","v14312_05","v14312_05_052","v14312_05_052",radiDigChnMulTab,"b");
            //日散射辐射曝辐量(MJ/m2)
            totalHandle(dayValueTab, "v14314", radiDigChnMulTab);
            //日净全辐射曝辐量(MJ/m2)
            totalHandle(dayValueTab, "v14308", radiDigChnMulTab);
            //日直接辐射曝辐量(MJ/m2)
            totalHandle(dayValueTab, "v14313", radiDigChnMulTab);
            if(!flag){
                flag=true;
            }
        }
        if(flag) {
            RadiDigChnMulTab radiDigChnMulTab=list.get(0);
            dayValueTab.setD_RECORD_ID(UUID.randomUUID().toString());
            dayValueTab.setD_DATA_ID(dayCtsCode);
            dayValueTab.setV01300(radiDigChnMulTab.getV01300());
            dayValueTab.setV01301(radiDigChnMulTab.getV01300()+"");
            dayValueTab.setV05001(Float.parseFloat(radiDigChnMulTab.getV05001()+""));
            dayValueTab.setV06001(Float.parseFloat(radiDigChnMulTab.getV06001()+""));
            dayValueTab.setV07001(Float.parseFloat(radiDigChnMulTab.getV07001()+""));
            dayValueTab.setV04001(Integer.parseInt(radiDigChnMulTab.getV04001()+""));
            dayValueTab.setV04002(Integer.parseInt(radiDigChnMulTab.getV04002()+""));
            dayValueTab.setV04003(Integer.parseInt(radiDigChnMulTab.getV04003()+""));
        }
        return dayValueTab;
    }
    /**
     * 旬值辐射数据组装
     * @return
     */
    public MeadowValueTab meadowRadiHandle(String meadowCts, List<RadiDigChnMulTab> radiValueList, FirstData sf, String type) {
        MeadowValueTab meadowValueTab=new MeadowValueTab();
        TimeEntity te = new TimeEntity();
        te.setMonth(sf.getMonth());
        //当月天数
        int dayMonth = te.getDayCountOnMonth();
        meadowValueTab.setD_DATA_ID(meadowCts);
        meadowValueTab.setD_RECORD_ID(UUID.randomUUID().toString());
        meadowValueTab.setV01300(sf.getAreacode());
        meadowValueTab.setV01301(sf.getAreacode());
        meadowValueTab.setV05001(Float.parseFloat(sf.getWd()));
        meadowValueTab.setV06001(Float.parseFloat(sf.getJd()));
        if(sf.getViewhight()!=null&&(!"".equals(sf.getViewhight()))) {
            meadowValueTab.setV07001(Float.parseFloat(sf.getViewhight()));
        }
        if(sf.getQuickhight()!=null&&(!"".equals(sf.getQuickhight()))) {
            meadowValueTab.setV07031(Float.parseFloat(sf.getQuickhight()));
        }
        meadowValueTab.setV04001(Integer.parseInt(sf.getYear()));
        meadowValueTab.setV04002(Integer.parseInt(sf.getMonth()));
        String monthTime=meadowValueTab.getV04002()+"";
        if(monthTime.length()<2){
            monthTime="0"+monthTime;
        }
        String dataTime=meadowValueTab.getV04001()+"-"+monthTime;
        meadowValueTab.setD_DATETIME(dataTime);
        String meadow=null;
        int days=10;
        if("next".equals(type)){
            meadow="下旬";
        }else if("last".equals(type)){
            meadow="上旬";
        }else if("mid".equals(type)){
            meadow="中旬";
        }
        meadowValueTab.setV04290(meadow);
        for(RadiDigChnMulTab radiDigChnMulTab:radiValueList) {
            //旬太阳总辐射(MJ/m2)
            totalHandle(meadowValueTab, "v14311", radiDigChnMulTab);
            //旬净全辐射(MJ/m2)
            totalHandle(meadowValueTab, "v14312", radiDigChnMulTab);
        }
        return meadowValueTab;
    }
    /**
     * 辐射月值信息组装
     * @param monthValueTab
     * @param rList
     */
    public void monthRadiDatas(MonthValueTab monthValueTab, List<RadiDigChnMulTab> rList) {
        for(RadiDigChnMulTab radiDigChnMulTab:rList){
            int daycount=getDayCountOnMonth(radiDigChnMulTab.getV04002()+"",radiDigChnMulTab.getV04001()+"");
            //月太阳总辐射(MJ/m2)
            totalHandle(monthValueTab, "v14311", radiDigChnMulTab);
            // 月最大日总辐射辐照度(W/m2)、月最大日总辐射辐照度时间
            extremeHandle(monthValueTab,"v14311_05","v14311_05","v14021_05_052","v14021_05_052",radiDigChnMulTab,"b");
            //月净全辐射(MJ/m2)
            totalHandle(monthValueTab, "v14312", radiDigChnMulTab);
           //月最大日净全辐射辐照度(W/m2)、月最大日净全辐射辐照度时间
            extremeHandle(monthValueTab,"v14312_05","v14312_05","v14312_05_052","v14312_05_052",radiDigChnMulTab,"b");
            // 月日散射辐射曝辐量平均
            averageHandle(monthValueTab, "v14314_avg","v14314",radiDigChnMulTab, daycount);
            // 月日散射辐射曝辐量合计
            totalHandle(monthValueTab, "v14314",radiDigChnMulTab);
            //最大日散射辐射辐照度
            extremeHandle(monthValueTab,"v14314_05","v14314_05",null,null,radiDigChnMulTab,"b");
            //月日直接辐射曝辐量平均
            averageHandle(monthValueTab, "v14322_avg","v14322",radiDigChnMulTab, daycount);
            //月日直接辐射曝辐量合计
            totalHandle(monthValueTab, "v14322",radiDigChnMulTab);
        }
    }
    /**
     * 月值信息组装
     */
    public void monthValueHandle(String monthCts, DayValueTab dayValueTab, FirstData sf,MonthValueTab monthValueTab){
        TimeEntity te = new TimeEntity();
        te.setYear(sf.getYear());
        te.setMonth(sf.getMonth());
        //当月天数
        int dayMonth = te.getDayCountOnMonth();
        monthValueTab.setD_DATA_ID(monthCts);
        monthValueTab.setD_RECORD_ID(UUID.randomUUID().toString());
        monthValueTab.setV01300(sf.getAreacode());
        monthValueTab.setV01301(sf.getAreacode());
        String wd=sf.getWd();
        if(wd.endsWith("N")){
            wd=wd.substring(0, wd.length()-1);
        }
        monthValueTab.setV05001(Float.parseFloat(wd));
        String jd=sf.getJd();
        if(jd.endsWith("E")){
            jd=jd.substring(0, jd.length()-1);
        }
        monthValueTab.setV06001(Float.parseFloat(jd));
        monthValueTab.setV07001(Float.parseFloat(sf.getViewhight()));
        if(sf.getQuickhight()!=null){
            monthValueTab.setV07031(Float.parseFloat(sf.getQuickhight()));
        }
        monthValueTab.setV04001(Integer.parseInt(sf.getYear()));
        monthValueTab.setV04002(Integer.parseInt(sf.getMonth()));
        String monthTime=dayValueTab.getV04002()+"";
        if(monthTime.length()<2){
            monthTime="0"+monthTime;
        }
        String dataTime=dayValueTab.getV04001()+"-"+monthTime;
        monthValueTab.setD_DateTIME(dataTime);
        //月平均本站气压
        averageHandle(monthValueTab,"V10004_701","V10004_701",dayValueTab,dayMonth);
        //月极端最高本站气压
        extremeHandle(monthValueTab,"V10301","V10301",null,null,dayValueTab,"b");
        //月极端最低本站气压
        extremeHandle(monthValueTab,"V10302","V10302",null,null,dayValueTab,"s");
        //月平均海平面气压
        averageHandle(monthValueTab,"V10051_701","V10051_701",dayValueTab,dayMonth);
        //月平均最高本站气压
        averageHandle(monthValueTab,"V10301_avg","V10301",dayValueTab,dayMonth);
        //月平均最低本站气压
        averageHandle(monthValueTab,"V10302_avg","V10302",dayValueTab,dayMonth);
        //月平均湿球温度
        averageHandle(monthValueTab,"balltemp_avg","balltemp_avg",dayValueTab,dayMonth);
        //月最大水汽压、月最大水汽压出现日期
        extremeHandle(monthValueTab, "V13004_MAX","V13004_MAX","V13004_MAX_D","V04003",dayValueTab,"b");
        //月最小水气压、月最小水汽压出现日期
        extremeHandle(monthValueTab, "V13004_MIN","V13004_MIN","V13004_MIN_D","V04003",dayValueTab,"b");
        //月平均气温
        averageHandle(monthValueTab,"V12001_701","V12001_701",dayValueTab,dayMonth);
        // 月平均日最高气温
        averageHandle(monthValueTab,"V12011_701","V12011_701",dayValueTab,dayMonth);
        //月平均日最低气温
        averageHandle(monthValueTab,"V12012_701","V12012_701",dayValueTab,dayMonth);
        //月本月极端最高气温
        extremeHandle(monthValueTab,"V12011","V12011",null,null,dayValueTab,"b");
        //本月极端最低气温
        extremeHandle(monthValueTab,"V12012","V12012",null,null,dayValueTab,"s");
        //月平均气温日较差
        averageHandle(monthValueTab,"V12303_701","V12303_701",dayValueTab,dayMonth);
        //月最大气温日较差
        extremeHandle(monthValueTab,"V12304","V12303_701",null,null,dayValueTab,"b");
        //月最小气温日较差
        extremeHandle(monthValueTab,"V12305","V12303_701",null,null,dayValueTab,"s");
        //日最高气温≥30℃日数
        compareDaysHandle(monthValueTab, "V12011","V12605_30",dayValueTab,30,"b");
        //日最高气温≥35℃日数
        compareDaysHandle(monthValueTab, "V12011","V12605_35",dayValueTab,35,"b");
        //日最高气温≥40℃日数
        compareDaysHandle(monthValueTab, "V12011","V12605_40",dayValueTab,40,"b");
        //日最低气温＜2℃日数
        compareDaysHandle(monthValueTab, "V12012","V12607_02",dayValueTab,2,"s");
        //日最低气温＜0℃日数
        compareDaysHandle(monthValueTab, "V12012","V12603",dayValueTab,0,"s");
        //日最低气温＜-2℃日数
        compareDaysHandle(monthValueTab, "V12012","V12606_02",dayValueTab,-2,"s");
        // 日最低气温＜-5℃日数 ( 单位：日 )
        compareDaysHandle(monthValueTab, "V12012","V12606_05",dayValueTab,-2,"s");
        // 日最低气温＜-10℃日数 ( 单位：日 )
        compareDaysHandle(monthValueTab, "V12012","V12606_10",dayValueTab,-2,"s");
        //日最低气温＜-15℃日数
        compareDaysHandle(monthValueTab, "V12012","V12606_15",dayValueTab,-15,"s");
        //日最低气温＜-30℃日数
        compareDaysHandle(monthValueTab, "V12012","V12606_30",dayValueTab,-30,"s");
        //日最低气温＜-40℃日数
        compareDaysHandle(monthValueTab, "V12012","V12606_40",dayValueTab,-40,"s");
        //冷度日数（日平均气温＞26.0℃）
        compareDaysHandle(monthValueTab, "V12001_701","V12610_26",dayValueTab,26,"b");
        //暖度日数（日平均气温＜18.0℃）
        compareDaysHandle(monthValueTab, "V12001_701","V12611_18",dayValueTab,18,"s");
        //月平均水汽压
        averageHandle(monthValueTab,"V13004_701","V13004_701",dayValueTab,dayMonth);
        //月平均相对湿度
        averageHandle(monthValueTab,"V13003_701","V13003_701",dayValueTab,dayMonth);
        //月最小相对湿度
        extremeHandle(monthValueTab,"V13007","V13007",null,null,dayValueTab,"s");
        //月平均总云量
        averageHandle(monthValueTab,"V20010_701","V20010_701",dayValueTab,dayMonth);
        //月平均低云量
        averageHandle(monthValueTab,"V20051_701","V20051_701",dayValueTab,dayMonth);
        //日平均总云量< 2.0日数
        compareDaysHandle(monthValueTab, "V20010_701","V20501_02",dayValueTab,2,"s");
        //日平均总云量> 8.0日数
        compareDaysHandle(monthValueTab, "V20010_701","V20500_08",dayValueTab,8,"b");
        //日平均低云量< 2.0日数
        compareDaysHandle(monthValueTab, "V20051_701","V20503_02",dayValueTab,2,"s");
        //日平均低云量> 8.0日数
        compareDaysHandle(monthValueTab, "V20051_701","V20502_08",dayValueTab,8,"b");
        //20-20时月总降水量
        totalHandle(monthValueTab,"V13305",dayValueTab);
        //08-08时月总降水量
        totalHandle(monthValueTab,"V13306",dayValueTab);
        //月最大日降水量
        extremeHandle(monthValueTab,"V13052","V13052",null,null,dayValueTab,"b");
        //日总降水量≥0.1mm日数
        compareDaysHandle(monthValueTab, "V13305","V13353",dayValueTab, (float) 0.1,"b");
        //日总降水量≥1mm日数
        compareDaysHandle(monthValueTab, "V13305","V13355_001",dayValueTab,1,"b");
        //日总降水量≥5mm日数
        compareDaysHandle(monthValueTab, "V13305","V13355_005",dayValueTab,5,"b");
        //日总降水量≥10mm日数
        compareDaysHandle(monthValueTab, "V13305","V13355_010",dayValueTab,10,"b");
        //日总降水量≥25mm日数
        compareDaysHandle(monthValueTab, "V13305","V13355_025",dayValueTab,25,"b");
        //日总降水量≥50mm日数
        compareDaysHandle(monthValueTab, "V13305","V13355_050",dayValueTab,50,"b");
        //日总降水量≥100mm日数
        compareDaysHandle(monthValueTab, "V13305","V13355_100",dayValueTab,100,"b");
        //日总降水量≥150mm日数
        compareDaysHandle(monthValueTab, "V13305","V13355_150",dayValueTab,150,"b");
        //日总降水量≥250mm日数
        compareDaysHandle(monthValueTab, "V13305","V13355_250",dayValueTab,250,"b");
        //月能见度<800米出现次数
        compareDaysHandle(monthValueTab, "V20001_701_10","V20309_008",dayValueTab,800,"s");
        //能见度≤1km出现次数
        compareDaysHandle(monthValueTab, "V20001_701_10","V20309_100",dayValueTab,1000,"s");
        //能见度≤1.5km出现次数
        compareDaysHandle(monthValueTab, "V20001_701_10","V20309_150",dayValueTab,1500,"s");
        //能见度≤3km出现次数
        compareDaysHandle(monthValueTab, "V20001_701_10","V20309_300",dayValueTab,3000,"s");
        //能见度>1km出现次数
        compareDaysHandle(monthValueTab, "V20001_701_10","V20309_011",dayValueTab,1000,"b");
        //1小时最大降水量
        extremeHandle(monthValueTab,"V13302_01","V13302_01",null,null,dayValueTab,"b");
        //冰雹日数
        compareDaysHandle(monthValueTab, "V20302_089","V04330_089",dayValueTab,0,"b");
        //扬沙日数
        compareDaysHandle(monthValueTab, "V20302_007","V04330_007",dayValueTab,0,"b");
        //浮尘日数
        compareDaysHandle(monthValueTab, "V20302_006","V04330_006",dayValueTab,0,"b");
        //霾日数
        compareDaysHandle(monthValueTab, "V20302_005","V04330_005",dayValueTab,0,"b");
        //龙卷风日数
        compareDaysHandle(monthValueTab, "V20302_019","V04330_008",dayValueTab,0,"b");
        //大风日数
        compareDaysHandle(monthValueTab, "V20302_015","V04330_015",dayValueTab,0,"b");
        //沙尘暴日数
        compareDaysHandle(monthValueTab, "V20302_031","V04330_031",dayValueTab,0,"b");
        //雾日数
        compareDaysHandle(monthValueTab, "V20302_042","V04330_042",dayValueTab,0,"b");
        //雷暴日数
        compareDaysHandle(monthValueTab, "V20302_017","V04330_017",dayValueTab,0,"b");
        //霜日数
        compareDaysHandle(monthValueTab, "V20302_002","V04330_002",dayValueTab,0,"b");
        //降雪日数
        compareDaysHandle(monthValueTab, "V20302_070","V04330_070",dayValueTab,0,"b");
        //积雪日数
        compareDaysHandle(monthValueTab, "V20302_016","V04330_016",dayValueTab,0,"b");
        //电线积冰（雨凇+雾凇）日数
        compareDaysHandle(monthValueTab, "V20305","V20305_540",dayValueTab,0,"b");
        //月总蒸发量小型
        totalHandle(monthValueTab,"V13032",dayValueTab);
        //月总蒸发量大型
        totalHandle(monthValueTab,"V13033",dayValueTab);
        //月最大小型蒸发量、月最大小型蒸发量出现日期
        extremeHandle(monthValueTab,"V13032_MAX","V13032","V13032_MAX_D","V04003",dayValueTab,"b");
        // 月最大大型蒸发量、 月最大大型蒸发量出现日期
        extremeHandle(monthValueTab,"V13033_MAX","V13033","V13033_MAX_D","V04003",dayValueTab,"b");
        //最大积雪深度
        extremeHandle(monthValueTab,"V13334","V13013",null,null,dayValueTab,"b");
        //最大雪压
        extremeHandle(monthValueTab,"V13330","V13330",null,null,dayValueTab,"b");
        //积雪深度≥1cm日数
        compareDaysHandle(monthValueTab, "V13013","V13356_001",dayValueTab,1,"b");
        //积雪深度≥5cm日数
        compareDaysHandle(monthValueTab, "V13013","V13356_005",dayValueTab,5,"b");
        //积雪深度≥10cm日数
        compareDaysHandle(monthValueTab, "V13013","V13356_010",dayValueTab,10,"b");
        //积雪深度≥20cm日数
        compareDaysHandle(monthValueTab, "V13013","V13356_020",dayValueTab,20,"b");
        //积雪深度≥30cm日数
        compareDaysHandle(monthValueTab, "V13013","V13356_030",dayValueTab,30,"b");
        //电线积冰南北最大重量、电线积冰南北最大重量的相应厚度、电线积冰南北最大重量的相应直径
        extremeHandle(monthValueTab,"V20307_NS","V20307_NS","V20306_NS,V20326_NS,V20440_060_NS_CHAR","V20306_NS,V20326_NS,V20440_060_NS_CHAR",dayValueTab,"b");
        //电线积冰东西最大重量、电线积冰东西最大重量的相应厚度、电线积冰东西最大重量的相应直径
        extremeHandle(monthValueTab,"V20307_WE","V20307_WE","V20306_WE,V20326_WE,V20440_060_WE_CHAR","V20306_WE,V20326_WE,V20440_060_WE_CHAR",dayValueTab,"b");
        //月平均风速（2分钟）
        averageHandle(monthValueTab,"V11291_701","V11291_701",dayValueTab,dayMonth);
        //月最大风速、最大风速的风向
        extremeHandle(monthValueTab,"V11042","V11042","V11296_CHAR","V11296",dayValueTab,"b");
        //月极大风速、月极大风速之风向
        extremeHandle(monthValueTab,"V11046","V11046","V11211","V11211",dayValueTab,"b");
        //日最大风速≥5.0m/s日数
        compareDaysHandle(monthValueTab, "V11042","V11042_05",dayValueTab,5,"b");
        //日最大风速≥10.0m/s日数
        compareDaysHandle(monthValueTab, "V11042","V11042_10",dayValueTab,10,"b");
        //日最大风速≥12.0m/s日数
        compareDaysHandle(monthValueTab, "V11042","V11042_12",dayValueTab,10,"b");
        //日最大风速≥15.0m/s日数
        compareDaysHandle(monthValueTab, "V11042","V11042_15",dayValueTab,15,"b");
        //日最大风速≥17.0m/s日数
        compareDaysHandle(monthValueTab, "V11042","V11042_17",dayValueTab,17,"b");
        //月极大风速、月极大风速之风向
        extremeHandle(monthValueTab,"V11046","V11046","V11211","V11211",dayValueTab,"b");
        if("NNE".equals(dayValueTab.getV11290_CHAR())){//NNE风的平均风速
            averageHandle(monthValueTab,"V11351_NNE","V11291_701",dayValueTab,dayMonth);
        }else if("NE".equals(dayValueTab.getV11290_CHAR())){//NE风的平均风速
            averageHandle(monthValueTab,"V11351_NE","V11291_701",dayValueTab,dayMonth);
        }else if("ENE".equals(dayValueTab.getV11290_CHAR())){//ENE风的平均风速
            averageHandle(monthValueTab,"V11351_ENE","V11291_701",dayValueTab,dayMonth);
        }else if("E".equals(dayValueTab.getV11290_CHAR())){//E风的平均风速
            averageHandle(monthValueTab,"V11351_E","V11291_701",dayValueTab,dayMonth);
        }else if("ESE".equals(dayValueTab.getV11290_CHAR())){//ESE风的平均风速
            averageHandle(monthValueTab,"V11351_ESE","V11291_701",dayValueTab,dayMonth);
        }else if("SE".equals(dayValueTab.getV11290_CHAR())){//SE风的平均风速
            averageHandle(monthValueTab,"V11351_NE","V11291_701",dayValueTab,dayMonth);
        }else if("SSE".equals(dayValueTab.getV11290_CHAR())){//SSE风的平均风速
            averageHandle(monthValueTab,"V11351_SSE","V11291_701",dayValueTab,dayMonth);
        }else if("S".equals(dayValueTab.getV11290_CHAR())){//S风的平均风速
            averageHandle(monthValueTab,"V11351_NE","V11291_701",dayValueTab,dayMonth);
        }else if("SSW".equals(dayValueTab.getV11290_CHAR())){//SSW风的平均风速
            averageHandle(monthValueTab,"V11351_NE","V11291_701",dayValueTab,dayMonth);
        }else if("SW".equals(dayValueTab.getV11290_CHAR())){//SW风的平均风速
            averageHandle(monthValueTab,"V11351_NE","V11291_701",dayValueTab,dayMonth);
        }else if("WSW".equals(dayValueTab.getV11290_CHAR())){//WSW风的平均风速
            averageHandle(monthValueTab,"V11351_NE","V11291_701",dayValueTab,dayMonth);
        }else if("W".equals(dayValueTab.getV11290_CHAR())){//W风的平均风速
            averageHandle(monthValueTab,"V11351_W","V11291_701",dayValueTab,dayMonth);
        }else if("WNW".equals(dayValueTab.getV11290_CHAR())){//WNW风的平均风速
            averageHandle(monthValueTab,"V11351_WNW","V11291_701",dayValueTab,dayMonth);
        }else if("NW".equals(dayValueTab.getV11290_CHAR())){//NW风的平均风速
            averageHandle(monthValueTab,"V11351_NW","V11291_701",dayValueTab,dayMonth);
        }else if("NNW".equals(dayValueTab.getV11290_CHAR())){//NNW风的平均风速
            averageHandle(monthValueTab,"V11351_NNW","V11291_701",dayValueTab,dayMonth);
        }else if("N".equals(dayValueTab.getV11290_CHAR())){//N风的平均风速
            averageHandle(monthValueTab,"V11351_N","V11291_701",dayValueTab,dayMonth);
        }
        if("NNE".equals(dayValueTab.getV11296())){//NNE风的最大风速
            extremeHandle(monthValueTab,"V11042_NNE","V11042",null,null,dayValueTab,"b");
        }else if("ENE".equals(dayValueTab.getV11296())){//ENE风的最大风速
            extremeHandle(monthValueTab,"V11042_ENE","V11042",null,null,dayValueTab,"b");
        }else if("NE".equals(dayValueTab.getV11296())){//NE风的最大风速
            extremeHandle(monthValueTab,"V11042_NE","V11042",null,null,dayValueTab,"b");
        }else if("E".equals(dayValueTab.getV11296())){//NE风的最大风速
            extremeHandle(monthValueTab,"V11042_E","V11042",null,null,dayValueTab,"b");
        }else if("ESE".equals(dayValueTab.getV11296())){//ESE风的最大风速
            extremeHandle(monthValueTab,"V11042_ESE","V11042",null,null,dayValueTab,"b");
        }else if("SE".equals(dayValueTab.getV11296())){//SE风的最大风速
            extremeHandle(monthValueTab,"V11042_SE","V11042",null,null,dayValueTab,"b");
        }else if("SSE".equals(dayValueTab.getV11296())){//SSE风的最大风速
            extremeHandle(monthValueTab,"V11042_SSE","V11042",null,null,dayValueTab,"b");
        }else if("S".equals(dayValueTab.getV11296())){//S风的最大风速
            extremeHandle(monthValueTab,"V11042_S","V11042",null,null,dayValueTab,"b");
        }else if("SSW".equals(dayValueTab.getV11296())){//SSW风的最大风速
            extremeHandle(monthValueTab,"V11042_SSW","V11042",null,null,dayValueTab,"b");
        }else if("SW".equals(dayValueTab.getV11296())){//SW风的最大风速
            extremeHandle(monthValueTab,"V11042_SW","V11042",null,null,dayValueTab,"b");
        }else if("WSW".equals(dayValueTab.getV11296())){//WSW风的最大风速
            extremeHandle(monthValueTab,"V11042_WSW","V11042",null,null,dayValueTab,"b");
        }else if("W".equals(dayValueTab.getV11296())){//W风的最大风速
            extremeHandle(monthValueTab,"V11042_W","V11042",null,null,dayValueTab,"b");
        }else if("WNW".equals(dayValueTab.getV11296())){//WNW风的最大风速
            extremeHandle(monthValueTab,"V11042_WNW","V11042",null,null,dayValueTab,"b");
        }else if("NW".equals(dayValueTab.getV11296())){//NW风的最大风速
            extremeHandle(monthValueTab,"V11042_NW","V11042",null,null,dayValueTab,"b");
        }else if("NNW".equals(dayValueTab.getV11296())){//NNW风的最大风速
            extremeHandle(monthValueTab,"V11042_NNW","V11042",null,null,dayValueTab,"b");
        }else if("N".equals(dayValueTab.getV11296())){//N风的最大风速
            extremeHandle(monthValueTab,"V11042_N","V11042",null,null,dayValueTab,"b");
        }
        //	月平均地面温度
        averageHandle(monthValueTab,"V12120_701","V12120_701",dayValueTab,dayMonth);
        //	月平均最高地面温度
        averageHandle(monthValueTab,"V12311_701","V12311",dayValueTab,dayMonth);
        //	月平均最低地面温度
        averageHandle(monthValueTab,"V12121_701","V12121",dayValueTab,dayMonth);
        //	月极端最高地面温度
        extremeHandle(monthValueTab,"V12311","V12311",null,null,dayValueTab,"b");
        //	月极端最低地面温度
        extremeHandle(monthValueTab,"V12121","V12121",null,null,dayValueTab,"b");
        //	月平均5cm地温
        averageHandle(monthValueTab,"V12030_701_005","V12030_701_005",dayValueTab,dayMonth);
        //	月平均10cm地温
        averageHandle(monthValueTab,"V12030_701_010","V12030_701_010",dayValueTab,dayMonth);
        //	月平均15cm地温
        averageHandle(monthValueTab,"V12030_701_015","V12030_701_015",dayValueTab,dayMonth);
        //	月平均20cm地温
        averageHandle(monthValueTab,"V12030_701_020","V12030_701_020",dayValueTab,dayMonth);
        //	月平均40cm地温
        averageHandle(monthValueTab,"V12030_701_040","V12030_701_040",dayValueTab,dayMonth);
        //	月平均80cm地温
        averageHandle(monthValueTab,"V12030_701_080","V12030_701_080",dayValueTab,dayMonth);
        //	月平均160cm地温
        averageHandle(monthValueTab,"V12030_701_160","V12030_701_160",dayValueTab,dayMonth);
        //	月平均320cm地温
        averageHandle(monthValueTab,"V12030_701_320","V12030_701_320",dayValueTab,dayMonth);
        //	平均草面（雪面）温度
        averageHandle(monthValueTab,"V12314_701","V12314_701",dayValueTab,dayMonth);
        //	平均最高草面（雪面）温度
        averageHandle(monthValueTab,"V12315_701","V12315",dayValueTab,dayMonth);
        //	平均最低草面（雪面）温度
        averageHandle(monthValueTab,"V12316_701","V12316",dayValueTab,dayMonth);
        //	极端最高草面（雪面）温度
        extremeHandle(monthValueTab,"V12315","V12315",null,null,dayValueTab,"b");
        //	极端最低草面（雪面）温度
        extremeHandle(monthValueTab,"V12316","V12316",null,null,dayValueTab,"s");
        //	最大冻土深度
        extremeHandle(monthValueTab,"V20334","V20331_01",null,null,dayValueTab,"b");
        //	月日照总时数
        totalHandle(monthValueTab,"V14032",dayValueTab);
        //	月日照总时长
        totalHandle(monthValueTab,"sunTime",dayValueTab);
        //	月日照百分率
        if(monthValueTab.getV14032()!=null&&monthValueTab.getSunTime()!=null) {
            monthValueTab.setV14033(Integer.parseInt(monthValueTab.getV14032()+"") / monthValueTab.getSunTime());
        }
        //	月日照百分率≥60%日数
        compareDaysHandle(monthValueTab, "V14033","V20302_171",dayValueTab,60,"b");
        //	月日照百分率≤20%日数
        compareDaysHandle(monthValueTab, "V14033","V20302_172",dayValueTab,20,"s");
    }
    /**
     * 旬值数据组装
     * @return
     */
    public MeadowValueTab meadowValueHandle(String meadowCts,List<DayValueTab> dayValueList,FirstData sf, String type) {
        MeadowValueTab meadowValueTab=new MeadowValueTab();
        TimeEntity te = new TimeEntity();
        te.setMonth(sf.getMonth());
        te.setYear(sf.getYear());
        //当月天数
        int dayMonth = te.getDayCountOnMonth();
        meadowValueTab.setD_DATA_ID(meadowCts);
        meadowValueTab.setD_RECORD_ID(UUID.randomUUID().toString());
        meadowValueTab.setV01300(sf.getAreacode());
        meadowValueTab.setV01301(sf.getAreacode());
        String wd=sf.getWd();
        if(wd.endsWith("N")){
            wd=wd.substring(0, wd.length()-1);
        }
        meadowValueTab.setV05001(Float.parseFloat(wd));
        String jd=sf.getJd();
        if(jd.endsWith("E")){
            jd=jd.substring(0, jd.length()-1);
        }
        meadowValueTab.setV06001(Float.parseFloat(jd));
        meadowValueTab.setV07001(Float.parseFloat(sf.getViewhight()));
        if(sf.getQuickhight()!=null) {
            meadowValueTab.setV07031(Float.parseFloat(sf.getQuickhight()));
        }
        meadowValueTab.setV04001(Integer.parseInt(sf.getYear()));
        meadowValueTab.setV04002(Integer.parseInt(sf.getMonth()));
        //资料时间
        String monthTime=meadowValueTab.getV04002()+"";
        if(monthTime.length()<2){
            monthTime="0"+monthTime;
        }
        String dataTime=meadowValueTab.getV04001()+"-"+monthTime;
        meadowValueTab.setD_DATETIME(dataTime);
        String meadow=null;
        int days=10;
        if("next".equals(type)){
            meadow="下旬";
            if(dayMonth==31){
                days=11;
            }else if(dayMonth==29){
                days=9;
            }else if(dayMonth==28){
                days=8;
            }
        }else if("last".equals(type)){
            meadow="上旬";
        }else if("mid".equals(type)){
            meadow="中旬";
        }
        meadowValueTab.setV04290(meadow);
        for(DayValueTab dayValueTab:dayValueList) {
            // 旬平均本站气压 （ 单位：百帕 ）
            averageHandle(meadowValueTab, "V10004_701", "V10004_701", dayValueTab, days);
            // 旬平均最高本站气压 ( 单位：百帕 )
            averageHandle(meadowValueTab, "V10301_avg", "V10301", dayValueTab, days);
            // 旬平均最低本站气压 ( 单位：百帕 )
            averageHandle(meadowValueTab, "V10302_avg", "V10302", dayValueTab, days);
            // 旬极端最高本站气压 （ 单位：百帕 ）
            extremeHandle(meadowValueTab,"V10301","V10301",null,null,dayValueTab,"b");
            // 旬极端最低本站气压 （ 单位：百帕 ）
            extremeHandle(meadowValueTab,"V10302","V10302",null,null,dayValueTab,"s");
            // 旬平均海平面气压 （ 单位：百帕 ）
            averageHandle(meadowValueTab,"V10051_701","V10051_701",dayValueTab,days);
            // 旬平均气温 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12001_701","V12001_701",dayValueTab,days);
            // 旬平均最高气温 ( 单位：摄氏度 )
            averageHandle(meadowValueTab,"V12011_avg","V12011",dayValueTab,days);
            // 旬平均最低气温 ( 单位：摄氏度 )
            averageHandle(meadowValueTab,"V12012_avg","V12012",dayValueTab,days);
            // 旬极端最高气温 （ 单位：摄氏度 ）
            extremeHandle(meadowValueTab,"V12011","V12011",null,null,dayValueTab,"b");
            // 旬极端最低气温 （ 单位：摄氏度 ）
            extremeHandle(meadowValueTab,"V12012","V12012",null,null,dayValueTab,"s");
            // 旬平均水汽压 （ 单位：百帕 ）
            averageHandle(meadowValueTab,"V13004_701","V13004_701",dayValueTab,days);
            // 旬平均相对湿度 （ 单位：% ）
            averageHandle(meadowValueTab,"V13003_701","V13003_701",dayValueTab,days);
            // 旬平均总云量 （ 单位：% ）
            averageHandle(meadowValueTab,"V20010_701","V20010_701",dayValueTab,days);
            // 旬平均低云量 （ 单位：% ）
            averageHandle(meadowValueTab,"V20051_701","V20051_701",dayValueTab,days);
            // 20-20时降水量 （ 单位：毫米 ）
            totalHandle(meadowValueTab,"V13305",dayValueTab);
            // 08-08时降水量 （ 单位：毫米 ）
            totalHandle(meadowValueTab,"V13306",dayValueTab);
            // 旬内日降水量≥0.1mm的日数 （ 单位：日 ）
            compareDaysHandle(meadowValueTab, "V13305","V13353",dayValueTab, (float) 0.1,"b");
            // 旬内日降水量≥25mm的日数 （ 单位：日 ）
            compareDaysHandle(meadowValueTab, "V13305","V13355_025",dayValueTab,1,"b");
            // 旬内日降水量≥50mm的日数 （ 单位：日 ）
            compareDaysHandle(meadowValueTab, "V13305","V13355_050",dayValueTab,1,"b");
            // 旬平均地面温度 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12120_701","V12120_701",dayValueTab,days);
            // 旬极端最高地面温度 （ 单位：摄氏度 ）
            extremeHandle(meadowValueTab,"V12311","V12311",null,null,dayValueTab,"b");
            // 旬极端最低地面温度 （ 单位：摄氏度 ）
            extremeHandle(meadowValueTab,"V12121","V12121",null,null,dayValueTab,"s");
            // 旬平均最高地面温度 ( 单位：摄氏度 )
            averageHandle(meadowValueTab,"V12311_avg","V12311",dayValueTab,days);
            // 旬平均最低地面温度 ( 单位：摄氏度 )
            averageHandle(meadowValueTab,"V12121_avg","V12121",dayValueTab,days);
            // 旬平均草面温度 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12314_701","V12314_701",dayValueTab,days);
            // 旬平均最高草面温度 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12315_avg","V12315",dayValueTab,days);
            // 旬平均最低草面温度 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12316_avg","V12316",dayValueTab,days);
            // 旬极端最高草面（雪面）温度 （ 单位：摄氏度 ）
            extremeHandle(meadowValueTab,"V12315","V12315",null,null,dayValueTab,"b");
            // 旬极端最低草面（雪面）温度 （ 单位：摄氏度 ）
            extremeHandle(meadowValueTab,"V12316","V12316",null,null,dayValueTab,"s");
            // 旬平均5cm地温 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12030_701_005","V12030_701_005",dayValueTab,days);
            // 旬平均10cm地温 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12030_701_010","V12030_701_010",dayValueTab,days);
            // 旬平均15cm地温 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12030_701_015","V12030_701_015",dayValueTab,days);
            // 旬平均20cm地温 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12030_701_020","V12030_701_020",dayValueTab,days);
            // 旬平均40cm地温 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12030_701_040","V12030_701_040",dayValueTab,days);
            // 旬平均80cm地温 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12030_701_080","V12030_701_080",dayValueTab,days);
            // 旬平均160cm地温 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12030_701_160","V12030_701_160",dayValueTab,days);
            // 旬平均320cm地温 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12030_701_320","V12030_701_320",dayValueTab,days);
            // 旬平均2分钟风速 （ 单位：米/秒 ）
            averageHandle(meadowValueTab,"V11291_701","V11291_701",dayValueTab,days);
            // 旬最大风速、最大风速的风向
            extremeHandle(meadowValueTab,"V11042","V11042","V11296","V11296",dayValueTab,"b");
            // 旬极大风速、月极大风速之风向
            extremeHandle(meadowValueTab,"V11046","V11046","V11211","V11211",dayValueTab,"b");
            // 旬平均10分钟风速 （ 单位：米/秒 ）
            averageHandle(meadowValueTab,"V11293_701","V11293_701",dayValueTab,days);
            // 旬内大风日数 （ 单位：日 ）
            compareDaysHandle(meadowValueTab, "V20302_015","V04330_015",dayValueTab,0,"b");
            // 旬内最大积雪深度 （ 单位：厘米 ）
            extremeHandle(meadowValueTab,"V13334","V13013",null,null,dayValueTab,"b");
            // 旬内最大雪压 （ 单位：克/平方厘米 ）
            extremeHandle(meadowValueTab,"V13330","V13330",null,null,dayValueTab,"b");
            // 旬蒸发量(小型) （ 单位：毫米 ）
            totalHandle(meadowValueTab,"V13032",dayValueTab);
            // 旬蒸发量(大型) （ 单位：毫米 ）
            totalHandle(meadowValueTab,"V13033",dayValueTab);
            // 旬日照时数 （ 单位：小时 ）
            totalHandle(meadowValueTab,"V14032",dayValueTab);
            // 日出时长
            totalHandle(meadowValueTab,"sunTime",dayValueTab);
            // 旬日照百分率 （ 单位：% ）
            if(meadowValueTab.getV14032()!=null&&meadowValueTab.getSunTime()!=null) {
                meadowValueTab.setV14033(Integer.parseInt(meadowValueTab.getV14032()+"")/ meadowValueTab.getSunTime());
            }
            // 旬平均日最高气温 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12011_701","V12011_701",dayValueTab,days);
            // 旬平均日最低气温 （ 单位：摄氏度 ）
            averageHandle(meadowValueTab,"V12012_701","V12012_701",dayValueTab,days);
        }
        /**
         * 旬日数、日统计
         */
        for(DayValueTab dayValueTab:dayValueList) {
            // 旬极端最高本站气压出现日数、旬极端最高本站气压出现日 （ 字符 ）
            statisticalDays(meadowValueTab, "V10301", dayValueTab, "V10301", "V10301_040,V10301_060");
            // 旬极端最低本站气压出现日数、旬极端最低本站气压出现日 （ 字符 ）
            statisticalDays(meadowValueTab, "V10302", dayValueTab, "V10302", "V10302_040,V10302_060");
            // 旬极端最高气温出现日数 、旬极端最高气温出现日 （ 字符 ）
            statisticalDays(meadowValueTab, "V12011", dayValueTab, "V12011", "V12011_040,V12011_060_CHAR");
            // 旬极端最低气温出现日数、旬极端最低气温出现日 （ 字符 ）
            statisticalDays(meadowValueTab, "V12012", dayValueTab, "V12012", "V12012_040,V12012_060_CHAR");
            // 旬极端最高地面温度出现日数、旬极端最高地面温度出现日 （ 字符 ）
            statisticalDays(meadowValueTab, "V12311", dayValueTab, "V12311", "V12311_040,V12311_060_CHAR");
            // 旬极端最低地面温度出现日数、旬极端最低地面温度出现日 （ 字符 ）
            statisticalDays(meadowValueTab, "V12121", dayValueTab, "V12121", "V12121_040,V12121_060_CHAR");
            // 旬极端最高草面（雪面）温度出现日数、旬极端最高草面（雪面）温度出现日 （ 字符 ）
            statisticalDays(meadowValueTab, "V12315", dayValueTab, "V12315", "V12315_040,V12315_060_CHAR");
            // 旬极端最低草面（雪面）温度出现日数、旬极端最低草面（雪面）温度出现日 （ 字符 ）
            statisticalDays(meadowValueTab, "V12316", dayValueTab, "V12316", "V12316_040,V12316_060_CHAR");
            // 旬最大风速出现日数 、旬最大风速之出现日 （ 字符 ）
            statisticalDays(meadowValueTab, "V11042", dayValueTab, "V11042", "V11042_040,V11042_060");
            // 旬极大风速出现日数、旬极大风速之出现日 （ 字符 ）
            statisticalDays(meadowValueTab, "V11046", dayValueTab, "V11046", "V11046_040,V11211_060");
            // 旬内最大积雪深度出现日数、旬内最大积雪深度出现日 （ 字符 ）
            statisticalDays(meadowValueTab, "V13334", dayValueTab, "V13013", "V13334_040,V13334_060_CHAR");
            // 旬内最大雪压出现日数、旬内最大雪压出现日 （ 字符 ）
            statisticalDays(meadowValueTab, "V13330", dayValueTab, "V13330", "V13330_040,V13330_060");
        }
        return meadowValueTab;
    }


    /**
     * 风频统计
     * @param monthValueTab
     * @param hourList
     */
    public void monthFFrequencyHandle(MonthValueTab monthValueTab,List<HourValueTab> hourList){

        //	NNE风向出现频率
        frequencyHandle(monthValueTab,"NNE","V11350_NNE",hourList,"f");
        maxOriMaxHandle(monthValueTab,"NNE","V11350_NNE","f");
        //	NE风向出现频率
        frequencyHandle(monthValueTab,"NE","V11350_NE",hourList,"f");
        maxOriMaxHandle(monthValueTab,"NE","V11350_NE","f");
        //	ENE风向出现频率
        frequencyHandle(monthValueTab,"ENE","V11350_ENE",hourList,"f");
        maxOriMaxHandle(monthValueTab,"ENE","V11350_ENE","f");
        //	E风向出现频率
        frequencyHandle(monthValueTab,"E","V11350_E",hourList,"f");
        maxOriMaxHandle(monthValueTab,"E","V11350_E","f");
        //	ESE风向出现频率
        frequencyHandle(monthValueTab,"ESE","V11350_ESE",hourList,"f");
        maxOriMaxHandle(monthValueTab,"ESE","V11350_ESE","f");
        //	SE风向出现频率
        frequencyHandle(monthValueTab,"SE","V11350_SE",hourList,"f");
        maxOriMaxHandle(monthValueTab,"","V11350_SE","f");
        //	SSE风向出现频率
        frequencyHandle(monthValueTab,"SSE","V11350_SSE",hourList,"f");
        maxOriMaxHandle(monthValueTab,"SSE","V11350_SSE","f");
        //	S风向出现频率
        frequencyHandle(monthValueTab,"S","V11350_S",hourList,"f");
        maxOriMaxHandle(monthValueTab,"S","V11350_S","f");
        //	SSW风向出现频率
        frequencyHandle(monthValueTab,"SSW","V11350_SSW",hourList,"f");
        maxOriMaxHandle(monthValueTab,"SSW","V11350_SSW","f");
        //	SW风向出现频率
        frequencyHandle(monthValueTab,"SW","V11350_SW",hourList,"f");
        maxOriMaxHandle(monthValueTab,"SW","V11350_SW","f");
        //	WSW风向出现频率
        frequencyHandle(monthValueTab,"WSW","V11350_WSW",hourList,"f");
        maxOriMaxHandle(monthValueTab,"WSW","V11350_WSW","f");
        //	W风向出现频率
        frequencyHandle(monthValueTab,"W","V11350_W",hourList,"f");
        maxOriMaxHandle(monthValueTab,"W","V11350_W","f");
        //	WNW风向出现频率
        frequencyHandle(monthValueTab,"WNW","V11350_WNW",hourList,"f");
        maxOriMaxHandle(monthValueTab,"WNW","V11350_WNW","f");
        //	NW风向出现频率
        frequencyHandle(monthValueTab,"NW","V11350_NW",hourList,"f");
        maxOriMaxHandle(monthValueTab,"NW","V11350_NW","f");
        //	NNW风向出现频率
        frequencyHandle(monthValueTab,"NNW","V11350_NNW",hourList,"f");
        maxOriMaxHandle(monthValueTab,"NNW","V11350_NNW","f");
        //	N风向出现频率
        frequencyHandle(monthValueTab,"N","V11350_N",hourList,"f");
        maxOriMaxHandle(monthValueTab,"N","V11350_N","f");
        //	C风向（静风）出现频率
        frequencyHandle(monthValueTab,"C","V11350_C",hourList,"f");
        maxOriMaxHandle(monthValueTab,"C","V11350_C","f");
        //	能见度≤10km出现频率
        frequencyHandle( monthValueTab,null,"V20309_010",hourList,"v10");
        //	能见度≤5km出现频率
        frequencyHandle( monthValueTab,null,"V20309_005",hourList,"v5");
        //	能见度≤1km出现频率
        frequencyHandle( monthValueTab,null,"V20309_001",hourList,"v1");
    }

    /**
     * 最多、次多参数统计
     * @param monthValueTab
     * @param fType
     * @param mfield
     * @param type
     */
    private void maxOriMaxHandle(Object monthValueTab,String fType,String mfield,String type){
        try {
            Object monthValue = ReflectUtil.getValue(monthValueTab, mfield);
            if("f".equals(type)) {
                if (monthValue != null) {
                    Integer value=Integer.parseInt(monthValue+"");
                    //最多频率
                    Object max = ReflectUtil.getValue(monthValueTab, "V11314_061");
                    //最多风向
                    Object maxParam= ReflectUtil.getValue(monthValueTab, "V11314_CHAR");
                    //次多频率
                    Object mix = ReflectUtil.getValue(monthValueTab, "V11315_061");
                    //次多风向
                    Object mixParam = ReflectUtil.getValue(monthValueTab, "V11315_CHAR");
                    if(max!=null) {
                        Integer maxvalue = Integer.parseInt(max + "");
                        Integer mixvalue = Integer.parseInt(mix + "");
                        if (maxvalue < value) {
                            ReflectUtil.setValue(monthValueTab, "V11314_CHAR", fType);
                            ReflectUtil.setValue(monthValueTab, "V11314_061", value);//最多
                            ReflectUtil.setValue(monthValueTab, "V11315_061", maxvalue);//次多
                            ReflectUtil.setValue(monthValueTab, "V11315_CHAR", maxParam);
                        }else if(mixvalue<value){
                            ReflectUtil.setValue(monthValueTab, "V11315_061", value);//次多
                            ReflectUtil.setValue(monthValueTab, "V11315_CHAR", fType);
                        }
                    }else{
                        ReflectUtil.setValue(monthValueTab, "V11314_CHAR", fType);
                        ReflectUtil.setValue(monthValueTab, "V11314_061", value);//最多
                        ReflectUtil.setValue(monthValueTab, "V11315_061", value);//次多
                        ReflectUtil.setValue(monthValueTab, "V11315_CHAR", fType);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 频率统计
     * @param monthValueTab
     * @param hourList
     * @param type
     */
    private void frequencyHandle(Object monthValueTab,String fType,String mfield,List<HourValueTab> hourList,String type){
        int count=0;
        for(HourValueTab hourValueTab:hourList){
            String value=null;
            if("f".equals(type)) {
                value=hourValueTab.getV11290();
            }else if("v10".equals(type)||"v1".equals(type)||"v5".equals(type)){//小于10km或小于1km
                Float value10=hourValueTab.getV20001();
                if(value10!=null&&value10<=10){
                    value="101";
                }
            }
            if(value!=null){
                count++;
            }
        }
        try {
            int countParam=0;
            for (HourValueTab hourValueTab : hourList) {
                String value=null;
                if ("f".equals(type)) {//风频统计
                   value = hourValueTab.getV11290();
                    if (value!=null&&fType.equals(value)) {
                        countParam++;
                    }
                }else if("v10".equals(type)){
                    Float value10=hourValueTab.getV20001();
                    if(value10!=null&&value10<=10){
                        countParam++;
                    }
                }else if("v5".equals(type)){
                    Float value1=hourValueTab.getV20001();
                    if(value1!=null&&value1<=5){
                        countParam++;
                    }
                }else if("v1".equals(type)){
                    Float value1=hourValueTab.getV20001();
                    if(value1!=null&&value1<=1){
                        countParam++;
                    }
                }

            }
            if(count!=0){
                ReflectUtil.setValue(monthValueTab, mfield, countParam/count);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 月极值日数、日组装
     * @param monthValueTab
     * @param dayList
     */
    public void monthStatisticalDays(Object monthValueTab, List<DayValueTab> dayList){
        for(DayValueTab dayValueTab:dayList){
            //月极端最高本站气压出现日数、月极端最高本站气压出现日
            statisticalDays(monthValueTab,"V10301", dayValueTab,"V10301","V10301_040,V10301_060_CHAR");
            //	月极端最低本站气压出现日数、月极端最低本站气压出现日
            statisticalDays(monthValueTab,"V10302", dayValueTab,"V10302","V10302_040,V10302_060_CHAR");
            //	本月极端最高气温出现日数、极端最高气温出现日
            statisticalDays(monthValueTab,"V12011", dayValueTab,"V12011","V12011_040,V12011_060_CHAR");
            //	本月极端最低气温出现日数、极端最低气温出现日
            statisticalDays(monthValueTab,"V12012", dayValueTab,"V12012","V12012_040,V12012_060_CHAR");
            //	月最大气温日较差出现日数、月最大气温日较差出现日
            statisticalDays(monthValueTab,"V12304", dayValueTab,"V12303_701","V12304_040,V12304_060_CHAR");
            //	月最小气温日较差出现日数、月最小气温日较差出现日
            statisticalDays(monthValueTab,"V12305", dayValueTab,"V12303_701","V12305_040,V12305_060_CHAR");
            //	月最小相对湿度出现日数、月最小相对湿度出现日
            statisticalDays(monthValueTab,"V13007", dayValueTab,"V13007","V13007_040,V13007_060_CHAR");
            //	月最大日降水量出现日数、月最大日降水量出现日
            statisticalDays(monthValueTab,"V13052", dayValueTab,"V13305","V13052_040,V13052_060_CHAR");
            //	1小时最大降水量出现日数、1小时最大降水量出现日
            statisticalDays(monthValueTab,"V13302_01", dayValueTab,"V13302_01","V13302_01_040,V13302_01_060_CHAR");
            //	最大积雪深度日数、最大积雪深度出现日
            statisticalDays(monthValueTab,"V13334", dayValueTab,"V13013","V13334_040,V13334_060_CHAR");
            //	最大雪压出现日数、最大雪压出现日
            statisticalDays(monthValueTab,"V13330", dayValueTab,"V13330","V13330_040,V13330_060");
            //	电线积冰-南北最大重量出现日数、南北最大重量出现日
            statisticalDays(monthValueTab,"V20307_NS", dayValueTab,"V20307_NS","V20440_040_NS,V20440_060_NS_CHAR");
            //	电线积冰-东西最大重量出现日数、东西最大重量出现日
            statisticalDays(monthValueTab,"V20307_WE", dayValueTab,"V20307_WE","V20440_040_WE,V20440_060_WE_CHAR");
            //	最大风速出现日数、月最大风速之出现日
            statisticalDays(monthValueTab,"V11042", dayValueTab,"V11042","V11042_040,V11042_060_CHAR");
            //	月极大风速出现日数、月极大风速之出现日
            statisticalDays(monthValueTab,"V11046", dayValueTab,"V11046","V11046_040,V11046_060_CHAR");
            //	月极端最高地面温度出现日数、月极端最高地面温度出现日
            statisticalDays(monthValueTab,"V12311", dayValueTab,"V12311","V12311_040,V12311_060_CHAR");
            //	月极端最低地面温度出现日数、月极端最低地面温度出现日
            statisticalDays(monthValueTab,"V12121", dayValueTab,"V12121","V12121_040,V12121_060_CHAR");
            //	极端最高草面（雪面）温度出现日数、极端最高草面（雪面）温度出现日
            statisticalDays(monthValueTab,"V12315", dayValueTab,"V12315","V12315_040,V12315_060_CHAR");
            //	极端最低草面（雪面）温度出现日数、极端最低草面（雪面）温度出现日
            statisticalDays(monthValueTab,"V12316", dayValueTab,"V12316","V12316_040,V12316_060_CHAR");
            //	最大冻土深度出现日数、最大冻土深度出现日
            statisticalDays(monthValueTab,"V20334", dayValueTab,"V20331_01","V20334_040,V20334_060_CHAR");

        }

    }

    /**
     * 日值数据组装
     */
    public DayValueTab dayVlaueHandle(DayValueTab dayValueTab,String dayCtsCode,TimeEntity timeEntity, ViewParamDayEntity dayEntity,FirstData sf,HourValueTab hourValueTab,int size) {

        dayValueTab.setD_RECORD_ID(UUID.randomUUID().toString());
        dayValueTab.setD_DATA_ID(dayCtsCode);
        dayValueTab.setV01300(sf.getAreacode());
        dayValueTab.setV01301(sf.getAreacode());
        dayValueTab.setV04001(Integer.parseInt(sf.getYear()));
        dayValueTab.setV04002(Integer.parseInt(sf.getMonth()));
        dayValueTab.setV04003(Integer.parseInt(timeEntity.getDay()));
        String wd=sf.getWd();
        if(wd.endsWith("N")){
            wd=wd.substring(0, wd.length()-1);
        }
        dayValueTab.setV05001(Float.parseFloat(wd));
        String jd=sf.getJd();
        if(jd.endsWith("E")){
            jd=jd.substring(0, jd.length()-1);
        }
        dayValueTab.setV06001(Float.parseFloat(jd));
        dayValueTab.setV07001(Float.parseFloat(sf.getViewhight()));
        if(sf.getQuickhight()!=null) {
            dayValueTab.setV07031(Float.parseFloat(sf.getQuickhight()));
        }
        //资料时间
        String monthTime=dayValueTab.getV04002()+"";
        String dayTime=dayValueTab.getV04003()+"";
        if(monthTime.length()<2){
            monthTime="0"+monthTime;
        }
        if(dayTime.length()<2){
            dayTime="0"+dayTime;
        }
        String time=dayValueTab.getV04001()+"-"+monthTime+"-"+dayTime;
        dayValueTab.setD_DATETIME(time);
        dayValueTab.setV10301(dayEntity.getGanshight());//日最高本站气压
        dayValueTab.setV10302(dayEntity.getGanslow());//日最低本站气压
        dayValueTab.setV12011(dayEntity.getTemphight());//日最高气温
        dayValueTab.setV12012(dayEntity.getTemplow());//日最低气温
        if(dayEntity.getTemphight()!=null&&dayEntity.getTemplow()!=null) {
            dayValueTab.setV12303_701(dayEntity.getTemphight() - dayEntity.getTemplow());//日气温日较差
        }
        dayValueTab.setV13007(dayEntity.getWetness());//日最小相对湿度
        dayValueTab.setV13032(dayEntity.getEvaporatl());// 日蒸发量（小型） ( 单位：毫米 )
        dayValueTab.setV13033(dayEntity.getEvaporath());// 日蒸发量（大型） ( 单位：毫米 )
        dayValueTab.setV11296(dayEntity.getBigwind());// 日最大风速的风向 ( 单位：度 )
        dayValueTab.setV11042(dayEntity.getBigwindSpeed());//日最大风速
        dayValueTab.setV11211(dayEntity.getMaxwind());// 日极大风速的风向 ( 单位：度 )
        dayValueTab.setV11046(dayEntity. getMaxwindSpeed());// 日极大风速 ( 单位：米/秒 )
        dayValueTab.setV12121(dayEntity. getDtemL());// 日最低地面温度 ( 单位：摄氏度 )
        dayValueTab.setV12311(dayEntity. getDtemH());// 日最高地面温度 ( 单位：摄氏度 )
        dayValueTab.setV13013(dayEntity.getSnowHight());// 积雪深度 ( 单位：厘米 )
        dayValueTab.setV13330(dayEntity. getSnowStress());// 雪压 ( 单位：g/cm2 )
        dayValueTab.setObversetype(sf.getObversetype()); // 观测方式和测站类别
        dayValueTab.setObsercecode(sf.getObsercecode()); // 观测项目标识
        dayValueTab.setQualitycode(sf.getQualitycode()); // 质量控制指示码
        dayValueTab.setArcaninehight(sf.getArcaninehight()); //风速器距地高度
        dayValueTab.setV10301_052(dayEntity.getGanshighthour());// 气压最高值出现时间
        dayValueTab.setV10302_052(dayEntity.getGanslowHour());// 气压最低值出现时间
        dayValueTab.setV12011_052(dayEntity.getTemphighthour());// 气温最高值出现时间
        dayValueTab.setV12012_052(dayEntity.getTemplowhour()); // 气温最低值出现时间
        dayValueTab.setV13007_052(dayEntity.getWetlowhour());// 相对湿度最小值时间hhmm
        dayValueTab.setRain1(dayEntity.getRain1());
        dayValueTab.setRain10(dayEntity.getRain10());
        dayValueTab.setV13308(dayEntity.getRain20()); // 20-08时雨量筒观测降水量 ( 单位：毫米 )
        dayValueTab.setV13309(dayEntity.getRain08()); // 08-20时雨量筒观测降水量 ( 单位：毫米 )
        dayValueTab.setV13305(dayEntity.getRain202()) ; // 20-20时降水量 ( 单位：毫米 )
        String weatherSymbol=dayEntity.getWetherSymbol();
        if(weatherSymbol!=null) {
            dayValueTab.setWetherSymbol(weatherSymbol);
            weatherDaysHandle(dayValueTab, weatherSymbol, dayEntity.getWetherStartTime(), dayEntity.getWetherEndTime());//天气现象统计
        }
        dayValueTab.setWetherStartTime(dayEntity.getWetherStartTime());
        dayValueTab.setWetherEndTime(dayEntity.getWetherEndTime());
        dayValueTab.setGlazeNSDia(dayEntity.getGlazeNSDia());
        dayValueTab.setGlazeNSHight(dayEntity.getGlazeNSHight());
        dayValueTab.setGlazeNSWeight(dayEntity.getGlazeNSWeight());
        dayValueTab.setGlazeWEDia(dayEntity.getGlazeWEDia());
        dayValueTab.setGlazeWEHight(dayEntity.getGlazeWEHight());
        dayValueTab.setGlazeWEWeight(dayEntity.getGlazeWEHight());
        dayValueTab.setRimeNSDia(dayEntity.getRimeNSDia());
        dayValueTab.setRimeNSHight(dayEntity.getRimeNSHight());
        dayValueTab.setRimeNSWeight(dayEntity.getRimeNSWeight());
        dayValueTab.setRimeWEDia(dayEntity.getRimeWEDia());
        dayValueTab.setRimeWEHight(dayEntity.getRimeWEHight());
        dayValueTab.setRimeWEWeight(dayEntity.getRimeWEWeight());
        dayValueTab.setV12001(dayEntity.getIceTemp());
        dayValueTab.setV11001(dayEntity.getIceWind());
        dayValueTab.setV11002(dayEntity.getIceWindSpeed());
        dayValueTab.setV11042_052(dayEntity.getWindMax1Time());
        dayValueTab.setV11046_052(dayEntity.getWindMax2Time());
        dayValueTab.setV12311_052(dayEntity.getDtemHTime());
        dayValueTab.setV12121_052(dayEntity.getDtemLTime());
        dayValueTab.setV12030_701_080(dayEntity.getHtem80());
        dayValueTab.setV12030_701_160(dayEntity.getHtem160());
        dayValueTab.setV12030_701_320(dayEntity.getHtem320());
        dayValueTab.setV20330_01(dayEntity.getPermafrost1Ceiling());
        dayValueTab.setV20331_01(dayEntity.getPermafrost1Floor());
        dayValueTab.setV20330_02(dayEntity.getPermafrost2Ceiling());
        dayValueTab.setV20331_02(dayEntity.getPermafrost2Floor());
        dayValueTab.setV14032(dayEntity.getSunlightTotal());

        //日出时间
        String rise=dayEntity.getSunRise();
        String fall=dayEntity.getSunRise();
        dayValueTab.setV20411(rise);
        dayValueTab.setV20412(fall);
        if(rise!=null&&fall!=null) {
            rise = rise.substring(0, 2);
            fall = fall.substring(0, 2);
            //日照时长
            Integer sunTime = Integer.parseInt(fall + "") - Integer.parseInt(rise + "");
            dayValueTab.setSunTime(sunTime);
            //日照频率
            dayValueTab.setV14033((int) (dayEntity.getSunlightTotal() / sunTime));
        }
        dayValueTab.setV12315(dayEntity.getGrassMaxHour());// 日草面（雪面）最高温度 ( 单位：摄氏度 )
        dayValueTab.setV12315_052(dayEntity.getGrassMaxTime());// 日草面（雪面）最高温度出现时间 ( 格式：时分 )
        dayValueTab.setV12316(dayEntity.getGrassMinHour());// 日草面（雪面）最低温度 ( 单位：摄氏度 )
        dayValueTab.setV12316_052(dayEntity.getGrassMinTime());// 日草面（雪面）最低温度出现时间 ( 格式：时分 )
        dayValueTab.setHtem100(dayEntity.getHtem100());//100cm深层地温
        dayValueTab.setHtem200(dayEntity.getHtem200());//200cm深层地温
        dayValueTab.setHtem300(dayEntity.getHtem300());//300cm深层地温


        //时
        Integer hour=hourValueTab.getV04004();
        /**
         * 4次定时
         */
        if(hour==2||hour==8||hour==14||hour==20){
            // 日平均本站气压 ( 单位：百帕 )
            averageHandle(dayValueTab,"V10004_701","V10004",hourValueTab,4);
            // 日平均海平面气压 ( 单位：百帕 )
            averageHandle(dayValueTab,"V10051_701","V10051",hourValueTab,4);
            // 日平均气温 ( 单位：摄氏度 )
            averageHandle(dayValueTab,"V12001_701","V12001",hourValueTab,4);
            // 日平均水汽压 ( 单位：百帕 )
            averageHandle(dayValueTab, "V13004_701", "V13004", hourValueTab, 4);
            //日平均地面温度
            averageHandle(dayValueTab, "V12120_701", "V12120", hourValueTab, 4);
            //日平均相对湿度
            averageHandle(dayValueTab,"V13003_701","V13003",hourValueTab,4);
            //日平均湿球温度
            averageHandle(dayValueTab, "balltemp_avg", "balltemp", hourValueTab, 4);
            //	平均草面（雪面）温度
            averageHandle(dayValueTab,"V12314_701","V12314",hourValueTab,4);
            //日平均露点温度
            averageHandle(dayValueTab, "V12003_avg", "V12003", hourValueTab, 4);
            // 日平均5cm地温 ( 单位：摄氏度 )
            averageHandle(dayValueTab, "V12030_701_005", "V12030_005", hourValueTab, 4);
           // 日平均10cm地温 ( 单位：摄氏度 )
            averageHandle(dayValueTab, "V12030_701_010", "V12030_010", hourValueTab, 4);
            // 日平均15cm地温 ( 单位：摄氏度 )
            averageHandle(dayValueTab, "V12030_701_015", "V12030_015", hourValueTab, 4);
            // 日平均20cm地温 ( 单位：摄氏度 )
            averageHandle(dayValueTab, "V12030_701_020", "V12030_020", hourValueTab, 4);
            // 日平均40cm地温 ( 单位：摄氏度 )
            averageHandle(dayValueTab, "V12030_701_040", "V12030_040", hourValueTab, 4);
            // 日平均80cm地温 ( 单位：摄氏度 )
            averageHandle(dayValueTab, "V12030_701_080", "V12030_080", hourValueTab, 4);
            // 日平均160cm地温 ( 单位：摄氏度 )
            averageHandle(dayValueTab, "V12030_701_160", "V12030_160", hourValueTab, 4);
            // 日平均320cm地温 ( 单位：摄氏度 )
            averageHandle(dayValueTab, "V12030_701_320", "V12030_320", hourValueTab, 4);
        }
        if(hour==8||hour==14||hour==20){
            // 日平均总云量 ( 单位：% )
            averageHandle(dayValueTab, "V20010_701", "V20010", hourValueTab, 3);
            // 日平均低云量 ( 单位：% )
            averageHandle(dayValueTab, "V20051_701", "V20051", hourValueTab, 3);
        }
        // 日最小相对湿度 ( 单位：% )、日最小相对湿度出现时间 ( 格式：时分 )
        extremeHandle(dayValueTab,"V13007","V13007","V13007_052","V13007_052",dayValueTab,"s");
        //日最大水汽压
        extremeHandle(dayValueTab, "V13004_MAX", "V13004",null,null,hourValueTab, "b");
        //日最小水气压
        extremeHandle(dayValueTab, "V13004_MIN", "V13004",null,null,hourValueTab, "s");
        // 日最小水平能见度 ( 单位：米 )、日最小水平能见度出现时间 ( 格式：时分 )
        extremeHandle(dayValueTab,"V20059","V20059","V20059_052","V20059_052",dayValueTab,"s");
        //日平均湿球温度(24小时)
        averageHandle(dayValueTab, "balltemp_avg_24", "balltemp", hourValueTab, size);
        //	最大冻土深度
        extremeHandle(dayValueTab,"V20331","V20331_01",null,null,dayValueTab,"b");

        return  dayValueTab;
    }

    /**
     * 天气现象日值信息统计
     * @param dayValueTab
     * @param weatherSymbol
     */
    public void weatherDaysHandle(DayValueTab dayValueTab,String weatherSymbol,String startTime,String endTime) {
        if(weatherSymbol.contains(")")){//夜间天气现象
            weatherSymbol= weatherSymbol.replace("(", ",");
            weatherSymbol= weatherSymbol.replace(")", ",");
        }
        String[] weathers=weatherSymbol.split(",");
        for(String w : weathers) {
            if(!"".equals(w)) {
                Integer symbol = Integer.parseInt(w);
                String daysField = "V20302_0" + w;
                String daysStartTime = daysField + "_052";
                ReflectUtil.setValue(dayValueTab, daysField, symbol);
                ReflectUtil.setValue(dayValueTab, daysStartTime, startTime);
            }
        }
    }
    /**
     * 小时值数据组装
     */
    public HourValueTab hourValueHandle(String hourCts, TimeEntity timeEntity, ViewParamsHourEntity hourValue, FirstData sf){
        HourValueTab hourValueTab=new HourValueTab();
        hourValueTab.setD_MAIN_ID(UUID.randomUUID().toString());
        hourValueTab.setD_DATA_ID(hourCts);
        hourValueTab.setV01300(sf.getAreacode());
        hourValueTab.setV01301(sf.getAreacode());
        String wd=sf.getWd();
        if(wd.endsWith("N")){
            wd=wd.substring(0, wd.length()-1);
        }
        hourValueTab.setV05001(Float.parseFloat(wd));
        String jd=sf.getJd();
        if(jd.endsWith("E")){
            jd=jd.substring(0, jd.length()-1);
        }
        hourValueTab.setV06001(Float.parseFloat(jd));
        hourValueTab.setV07001(Float.parseFloat(sf.getViewhight()));
        if(sf.getQuickhight()!=null) {
            hourValueTab.setV07031(Float.parseFloat(sf.getQuickhight()));
        }
        hourValueTab.setV04001(Integer.parseInt(sf.getYear()));
        hourValueTab.setV04002(Integer.parseInt(sf.getMonth()));
        hourValueTab.setV04003(Integer.parseInt(timeEntity.getDay()));
        hourValueTab.setV04004(Integer.parseInt(hourValue.getHour()));
        String monthTime=hourValueTab.getV04002()+"";
        String dayTime=hourValueTab.getV04003()+"";
        if(monthTime.length()<2){
            monthTime="0"+monthTime;
        }
        if(dayTime.length()<2){
            dayTime="0"+dayTime;
        }
        String time=hourValueTab.getV04001()+"-"+monthTime+"-"+dayTime;
        hourValueTab.setD_DATETIME(time);
        hourValueTab.setV10004(hourValue.getGans());
        hourValueTab.setV10051(hourValue.getSeagans());
        hourValueTab.setV12001(hourValue.getTemp());
        hourValueTab.setBalltemp(hourValue.getBalltemp());
        hourValueTab.setV13004(hourValue.getWatergans());
        hourValueTab.setV13003(hourValue.getWetness());
        hourValueTab.setV13019(hourValue.getRain());
        hourValueTab.setV20010(hourValue.getCloudt());
        hourValueTab.setV20051(hourValue.getCloudl());
        hourValueTab.setV20350_02(hourValue.getShapeCloud());
        hourValueTab.setV20001(hourValue.getVisibility1());
        hourValueTab.setVisibility2(hourValue.getVisibility2());
        hourValueTab.setV11201(hourValue.getWind());
        hourValueTab.setV11202(hourValue.getWindSpeed());
        hourValueTab.setV12120(hourValue.getDtem());
        hourValueTab.setV12030_005(hourValue.getDtem5());
        hourValueTab.setV12030_010(hourValue.getDtem10());
        hourValueTab.setV12030_015(hourValue.getDtem15());
        hourValueTab.setV12030_020(hourValue.getDtem20());
        hourValueTab.setV12030_040(hourValue.getDtem40());
        hourValueTab.setV12030_080(hourValue.getDtem80());
        hourValueTab.setV12030_160(hourValue.getDtem160());
        hourValueTab.setV12030_320(hourValue.getDtem320());
        String ar= sf.getArcaninehight();
        if(ar!=null) {
            hourValueTab.setV07032_04(Float.parseFloat(ar));
        }
        String obType=sf.getObversetype();
        if(obType!=null) {
            hourValueTab.setV02001(obType);
        }
        hourValueTab.setV12003(hourValue.getPointTemp());
        hourValueTab.setV11290(hourValue.getWind2());
        hourValueTab.setV11291(hourValue.getWind2Speed());
        hourValueTab.setV11292(hourValue.getWind10());
        hourValueTab.setV11293(hourValue.getWind10Speed());
        hourValueTab.setV13033(hourValue.getEvaporationBig());
        hourValueTab.setV12314(hourValue.getGrassTemp());
        return hourValueTab;
    }
    /**
     * 总值
     * @param monthValueTab 月值
     * @param name 属性名
     * @param dayValueTab 日值
     * @throws Exception
     */
    private void totalHandle(Object monthValueTab, String name, Object dayValueTab) {
        try {
            Object monthValue = ReflectUtil.getValue(monthValueTab, name);
            Object dayValue = ReflectUtil.getValue(dayValueTab, name);
            Object currentValue = null;
            if (dayValue != null) {
                if (dayValue instanceof Integer) {
                    Integer dayEntity1 = Integer.parseInt(dayValue + "");
                    Integer monthEntity1 = 0;
                    if (monthValue != null) {
                        monthEntity1 = Integer.parseInt(monthValue + "");
                    }
                    Integer currentValue1 = monthEntity1 + dayEntity1;
                    currentValue = currentValue1;
                } else if (dayValue instanceof Float) {
                    Float dayEntity1 = Float.parseFloat(dayValue + "");
                    Float monthEntity1 = Float.parseFloat(0 + "");
                    if (monthValue != null) {
                        monthEntity1 = Float.parseFloat(monthValue + "");
                    }
                    Float currentValue1 = monthEntity1 + dayEntity1;
                    currentValue = currentValue1;
                }
                if(currentValue!=null) {
                    ReflectUtil.setValue(monthValueTab, name, currentValue);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 平均值
     * @param monthValueTab 月值
     * @param mfield 月属性名
     * @param dfield 月属性名
     * @param dayValueTab 日值
     * @param dayMonth 月日总数
     * @throws Exception
     */
    private  void averageHandle(Object monthValueTab, String mfield, String dfield, Object dayValueTab, int dayMonth) {
        try {
            Object monthValue = ReflectUtil.getValue(monthValueTab, mfield);
            Object dayValue = ReflectUtil.getValue(dayValueTab, dfield);
            Object currentValue = null;
            if (dayValue != null) {
                if (dayValue instanceof Integer) {
                    Integer dayEntity1 = Integer.parseInt(dayValue + "");
                    Integer monthEntity1 = 0;
                    if (monthValue != null) {
                        monthEntity1 = Integer.parseInt(monthValue + "");
                    }
                    Integer currentValue1 = monthEntity1 + dayEntity1/dayMonth;
                    currentValue = currentValue1;
                } else if (dayValue instanceof Float) {
                    Float dayEntity1 = Float.parseFloat(dayValue + "");
                    Float monthEntity1 = Float.parseFloat(0 + "");
                    if (monthValue != null) {
                        monthEntity1 = Float.parseFloat(monthValue + "");
                    }
                    Float currentValue1 = monthEntity1 + dayEntity1/dayMonth;
                    currentValue = currentValue1;
                }
                if(currentValue!=null) {
                    ReflectUtil.setValue(monthValueTab, mfield, currentValue);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 极值日数、日组装
     * @param monthValueTab
     * @param mField
     * @param dayValueTab
     * @param dField
     * @param additional 附加信息，第一个是日数，第二个是日
     */
    private void  statisticalDays(Object monthValueTab,String mField, Object dayValueTab,String dField,String additional) {
        try {
            Object monthValue = ReflectUtil.getValue(monthValueTab, mField);
            Object dayValue = ReflectUtil.getValue(monthValueTab, dField);
            boolean flag=false;
            if(dayValue==monthValue){
                flag=true;
            }
            if(flag){
                String[] ditions=additional.split(",");
                if(ditions.length>1){//日
                    String m=ditions[1];
                    Object mday=ReflectUtil.getValue(dayValueTab, m);
                    Object day = ReflectUtil.getValue(dayValueTab, "V04003");
                    if(mday==null) {
                        ReflectUtil.setValue(monthValueTab, m, day+"");
                    }else{
                       String value= mday+","+day;
                        ReflectUtil.setValue(monthValueTab, m, value);
                    }
                }
                if(ditions.length>0){//日数
                    String m=ditions[0];
                    Object mday=ReflectUtil.getValue(dayValueTab, m);
                    if(mday==null) {
                        ReflectUtil.setValue(monthValueTab, m, 1);
                    }else{
                        int mdays=Integer.parseInt(mday+"");
                        Integer value= mdays++;
                        ReflectUtil.setValue(monthValueTab, m, value);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 极值处理
     * @param monthValueTab
     * @param mField 月值信息极值字段
     * @param dField 日值信息极值字段
     * @param mAdditional 月极值出现的附加字段，多个逗号隔开
     * @param dAdditional 日极值出现的附加字段，多个逗号隔开
     * 日、月极值出现的附加字段一一对应
     * @param dayValueTab
     * @param type
     */
    private void extremeHandle(Object monthValueTab, String mField,String dField,String mAdditional,String dAdditional, Object dayValueTab,String type){
        try {
            Object monthValue = ReflectUtil.getValue(monthValueTab, mField);
            Object dayValue = ReflectUtil.getValue(dayValueTab, dField);
            Object currentValue = null;
            if (dayValue != null) {
                if (monthValue != null) {
                    if (dayValue instanceof Integer) {
                        Integer monthValue1=Integer.parseInt(monthValue+"");
                        Integer dayValue1=Integer.parseInt(dayValue+"");
                        Integer currentValue1=null;
                        if("b".equals(type)) {//极端最大
                            if (monthValue1 <= dayValue1) {
                                currentValue1 = dayValue1;
                            }
                        }else if("s".equals(type)){//极端最小
                            if (monthValue1 >=dayValue1) {
                                currentValue1 = dayValue1;
                            }
                        }
                        currentValue=currentValue1;
                    } else if (dayValue instanceof Float) {
                        Float monthValue1=Float.parseFloat(monthValue+"");
                        Float dayValue1=Float.parseFloat(dayValue+"");
                        Float currentValue1=null;
                        if("b".equals(type)) {//极端最大
                            if (monthValue1 <= dayValue1) {
                                currentValue1 = dayValue1;
                            }
                        }else if("s".equals(type)){//极端最小
                            if (monthValue1 >=dayValue1) {
                                currentValue1 = dayValue1;
                            }
                        }
                        currentValue=currentValue1;
                    }
                 }else{
                    currentValue=dayValue;
                }
                if (currentValue != null) {
                    ReflectUtil.setValue(monthValueTab, mField, currentValue);
                    if(mAdditional!=null) {
                        //附加字段处理
                        String[] mDitions=mAdditional.split(",");
                        String[] dDitions=dAdditional.split(",");
                        for(int i=0,size=mDitions.length;i<size;i++) {
                            String m=mDitions[i];
                            String d=dDitions[i];
                            Object time1 = ReflectUtil.getValue(dayValueTab, d);
                            ReflectUtil.setValue(monthValueTab, m, time1);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 比较值处理
     * @param monthValueTab 月实体类
     * @param dField 当前值
     * @param mField 统计值
     * @param dayValueTab 日实体类
     * @param comparaValue 比较值
     * @param type 比较类型 s：当前值小于比较值，b：当前值大于比较值
     */
   private void compareDaysHandle(Object monthValueTab, String dField, String mField,Object dayValueTab,float comparaValue,String type){
       try {
           Object monthValue = ReflectUtil.getValue(monthValueTab, mField);
           Object dayValue = ReflectUtil.getValue(dayValueTab, dField);
           Object currentValue = null;
           if (dayValue != null) {
               Integer monthValue1=null;
               Integer currentValue1=null;
               boolean flag=false;
                if (dayValue instanceof Integer) {
                    Integer dayValue1=Integer.parseInt(dayValue+"");
                    if("s".equals(type)) {//  name1小于comparaValue
                        if (dayValue1 < comparaValue) {
                            flag=true;
                        }
                    }else if("b".equals(type)){  //name1大于comparaValue
                        if (dayValue1 > comparaValue) {
                            flag=true;
                        }
                    }
                    if(flag){
                        if (monthValue != null) {
                            monthValue1 =Integer.parseInt(monthValue+"");
                            currentValue1 = monthValue1 + 1;
                        } else {
                            currentValue1 = 1;
                        }
                    }
                    if(currentValue1!=null){
                        currentValue=currentValue1;
                    }
                }
               if (currentValue != null) {
                   ReflectUtil.setValue(monthValueTab, mField, currentValue);
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    public static void main(String[] args) throws Exception {
        DayValueTab dayValueTab=new DayValueTab();
        dayValueTab.setV10004_701((float) 256.0);
        dayValueTab.setV13003_701(12);
        MonthValueTab monthValueTab=new MonthValueTab();
        Object dayValue = ReflectUtil.getValue(dayValueTab, "V10004_701");
        ReflectUtil.setValue(monthValueTab,"V10004_701",dayValue);

        System.out.println(monthValueTab.getV10004_701());

    }


}
