package cma.cimiss2.dpc.decoder.fileDecode.Rfile;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiDigChnMulTab;
import cma.cimiss2.dpc.decoder.fileDecode.Interface.IFileDecoding;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.*;
import cma.cimiss2.dpc.decoder.fileDecode.common.decode.Decoding;

import java.io.File;
import java.util.*;

/**
 * 辐射资料数据组装
 * @author yang.kq
 * @version 1.0
 * @date 2020/4/23 15:20
 */
public class RnumberDecoding implements IFileDecoding {

    @Override
    public Map<String, Object> assemblyData(File file, String sunCtsCode,String dayCtsCode, String hourCts, String monthCts, String meadowCts, String filename, List<RadiDigChnMulTab> Rlist) {
        Map<String,Object> resultMap=new HashMap<>();
        FirstData sf = new FirstData();
        if(Rlist!=null&&Rlist.size()!=0) {
            RadiDigChnMulTab radiDigChnMulTab=Rlist.get(0);
            sf.setAreacode(radiDigChnMulTab.getV01300() + "");
            sf.setJd(radiDigChnMulTab.getV06001() + "");
            sf.setWd(radiDigChnMulTab.getV05001() + "");
            sf.setViewhight(radiDigChnMulTab.getV07001() + "");
            sf.setQuickhight("");
            sf.setYear(radiDigChnMulTab.getV04001() + "");
            sf.setMonth(radiDigChnMulTab.getV04002() + "");
        }
        /**
         * 组装数据
         */
        MonthValueTab monthValueTab=new MonthValueTab();
        List<MeadowValueTab> meadowList=new ArrayList<>();
        List<DayValueTab> dayList=new ArrayList<>();
        List<HourValueTab> hourList=new ArrayList<>();

        Map<Integer,List<RadiDigChnMulTab>> dayMap=new HashMap<>();
        for(int i=0,size=Rlist.size();i<size;i++){
            RadiDigChnMulTab radiDigChnMulTab=Rlist.get(i);
            int day=radiDigChnMulTab.getV04003();
            List<RadiDigChnMulTab> list=dayMap.get(day);
            if(list==null){
                list=new ArrayList<>();
                list.add(radiDigChnMulTab);
                dayMap.put(day,list);
            }else{
                list.add(radiDigChnMulTab);
            }
        }
        Decoding decoding=new Decoding();
        /**
         * 日值数据组装
         */
        for(RadiDigChnMulTab radiDigChnMulTab: Rlist){
            HourValueTab hourValueTab=null;
            if(radiDigChnMulTab.getV04004()!=null){
                hourValueTab=new HourValueTab();
                hourValueTab.setD_MAIN_ID(UUID.randomUUID().toString());
                hourValueTab.setD_DATA_ID(hourCts);
                hourValueTab.setV01300(sf.getAreacode());
                hourValueTab.setV05001(Float.parseFloat(sf.getWd()));
                hourValueTab.setV06001(Float.parseFloat(sf.getJd()));
                if(sf.getViewhight()!=null&&(!"".equals(sf.getViewhight()))) {
                    hourValueTab.setV07001(Float.parseFloat(sf.getViewhight()));
                }
                if(sf.getQuickhight()!=null&&(!"".equals(sf.getQuickhight()))) {
                    hourValueTab.setV07031(Float.parseFloat(sf.getQuickhight()));
                }
                hourValueTab.setV04001(Integer.parseInt(sf.getYear()));
                hourValueTab.setV04002(Integer.parseInt(sf.getMonth()));
                hourValueTab.setV04003(Integer.parseInt(radiDigChnMulTab.getV04003()+""));
                hourValueTab.setV04004(Integer.parseInt(radiDigChnMulTab.getV04004()+""));
                String time=hourValueTab.getV04001()+"-"+hourValueTab.getV04002()+"-"+hourValueTab.getV04003()+" "+hourValueTab.getV04004();
                hourValueTab.setD_DATETIME(time);
                //总辐射曝辐量
                hourValueTab.setV14320(radiDigChnMulTab.getV14320());
                //净辐射曝辐量
                hourValueTab.setV14320(radiDigChnMulTab.getV14308());
            }
            if(hourValueTab!=null){
                hourList.add(hourValueTab);
            }
        }
        /**
         * 组装日值辐射数据
         */
        Set<Integer> keys=dayMap.keySet();
        DayValueTab dayValueTab=null;
        for(int t:keys){
           List<RadiDigChnMulTab> list= dayMap.get(t);
            dayValueTab= decoding.dayRadiData(dayCtsCode,list);
            if(dayValueTab.getD_DATA_ID()!=null){
                dayList.add(dayValueTab);
            }
        }
        /**
         * 组装月值辐射数据
         */
        monthValueTab.setD_DATA_ID(monthCts);
        monthValueTab.setD_RECORD_ID(UUID.randomUUID().toString());
        monthValueTab.setV01300(sf.getAreacode());
        monthValueTab.setV05001(Float.parseFloat(sf.getWd()));
        monthValueTab.setV06001(Float.parseFloat(sf.getJd()));
        if(sf.getViewhight()!=null&&(!"".equals(sf.getViewhight()))) {
            monthValueTab.setV07001(Float.parseFloat(sf.getViewhight()));
        }
        if(sf.getQuickhight()!=null&&(!"".equals(sf.getQuickhight()))) {
            monthValueTab.setV07031(Float.parseFloat(sf.getQuickhight()));
        }
        monthValueTab.setV04001(Integer.parseInt(sf.getYear()));
        monthValueTab.setV04002(Integer.parseInt(sf.getMonth()));
        String dataTime=monthValueTab.getV04001()+"-"+monthValueTab.getV04002();
        monthValueTab.setD_DateTIME(dataTime);
        decoding.monthRadiDatas(monthValueTab,Rlist);
        /**
         * 组装旬值辐射数据
         */
        //下旬日数据
        List<RadiDigChnMulTab> maxMeadowDayValue=new ArrayList<>();
        //上旬日数据
        List<RadiDigChnMulTab> minMeadowDayValue=new ArrayList<>();
        //中旬日数据
        List<RadiDigChnMulTab> meadowDayValue=new ArrayList<>();

        for(int i=0,size=Rlist.size();i<size;i++){
            RadiDigChnMulTab radiDigChnMulTab=Rlist.get(i);

            if(radiDigChnMulTab.getV04003()>20){//下旬
                maxMeadowDayValue.add(radiDigChnMulTab);
            }else if(radiDigChnMulTab.getV04003()>10){//中旬
                meadowDayValue.add(radiDigChnMulTab);
            }else{//上旬
                minMeadowDayValue.add(radiDigChnMulTab);
            }
        }
        MeadowValueTab maxMeadowValue=decoding.meadowRadiHandle(meadowCts,maxMeadowDayValue,sf,"next");
        MeadowValueTab minMeadowValue=decoding.meadowRadiHandle(meadowCts,minMeadowDayValue,sf,"last");;
        MeadowValueTab meadowValue=decoding.meadowRadiHandle(meadowCts,meadowDayValue,sf,"mid");;
        meadowList.add(maxMeadowValue);
        meadowList.add(minMeadowValue);
        meadowList.add(meadowValue);
        if(!meadowList.isEmpty()){
            resultMap.put("meadowValue",meadowList);
        }
        if(monthValueTab!=null){//月值处理
            resultMap.put("monthValue",monthValueTab);
        }
        if(!dayList.isEmpty()){
            resultMap.put("dayValue",dayList);
        }
        if(!hourList.isEmpty()){
            resultMap.put("hourValue",hourList);
        }
        resultMap.put("radiValue",Rlist);
        ParseResult<String> decodingInfo = new ParseResult<String>(false) ;
        decodingInfo.setSuccess(true);
        decodingInfo.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
        resultMap.put("decodingInfo",decodingInfo);
        return resultMap;
    }
}
