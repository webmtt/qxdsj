package com.thinkgem.jeesite.mybatis.modules.filedecode.AFile.decode;



import com.thinkgem.jeesite.mybatis.modules.filedecode.AFile.bean.AOverHeadInfo;
import com.thinkgem.jeesite.mybatis.modules.filedecode.Interface.IFileDecoding;
import com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean.*;
import com.thinkgem.jeesite.mybatis.modules.filedecode.common.decode.Decoding;
import com.thinkgem.jeesite.mybatis.modules.filedecode.decode.A0Decoding;
import com.thinkgem.jeesite.mybatis.modules.filedecode.util.CommonUtil;
import com.thinkgem.jeesite.mybatis.modules.filedecode.util.Encoding;
import com.thinkgem.jeesite.mybatis.modules.filedecode.util.FileEncodeUtil;
import com.thinkgem.jeesite.mybatis.modules.filedecode.util.FileHadleUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A文件数据处理
 */
public class ADecoding implements IFileDecoding {
    // 存储封面信息
    private AOverHeadInfo ohi=new AOverHeadInfo();
    /**
     * 台站参数
     */
    private FirstData sf = null;
    /**
     * 月值信息存储
     */

    /**
     * A文件记录信息
     */
    private String dataTypeAll="PTIEUNHCVRWLZGFDKASB";
    /**
     * key-day，value-Collections
     */
    private Map<String, ParamCollections> aDecoding=new ConcurrentHashMap<>();

    /**
     * 组装数据
     * @param file
     * @param dayCtsCode 日值四级编码
     * @param hourCts 小时值四级编码
     * @param monthCts 月值四级编码
     * @param filename 文件名称
     * @return
     */
    @Override
    public  Map<String,Object> assemblyData(File file, String dayCtsCode, String hourCts, String monthCts, String meadowCts, String filename, List<RadiDigChnMulTab> Rlist){
        System.out.println("A文件开始组装数据！");
        Map<String,Object> resultMap=new HashMap<>();
        ParseResult<String> decodingInfo = new ParseResult<String>(false) ;
        if (file == null ||(file.isFile())||(!filename.startsWith("A"))) {
            if (filename.length() <= 0) {
                decodingInfo.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                resultMap.put("decodingInfo",decodingInfo);
                return resultMap;
            }
        }
        if(!file.exists()){
            decodingInfo.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
            resultMap.put("decodingInfo",decodingInfo);
            return resultMap;
        }
        FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
        String fileCode =  Encoding.javaname[fileEncodeUtil.detectEncoding(file)];

        fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
        System.out.println("A文件开始解码！");
        //解码
        aDataDecoding(file,fileCode);
        System.out.println("A文件解码完成！");
        /**
         * 组装数据
         */
        MonthValueTab monthValueTab=new MonthValueTab();
        List<DayValueTab> dayList=new ArrayList<>();
        List<HourValueTab> hourList=new ArrayList<>();
        List<SunLightValueTab> sunLightList=new ArrayList<>();
        List<MeadowValueTab> meadowList=new ArrayList<>();
        SunLightValueTab sunLightValueTab=null;
        DayValueTab dayValueTab=null;
        HourValueTab hourValueTab=null;
        Iterator<Map.Entry<String, ParamCollections>> entries = aDecoding.entrySet().iterator();
//        System.out.println(filename+"数据条数："+aDecoding.size());
        Decoding decoding=new Decoding();
        while(entries.hasNext()){//日值处理
            dayValueTab=new DayValueTab();
            sunLightValueTab=new SunLightValueTab();
            Map.Entry<String, ParamCollections> entry = entries.next();
            ParamCollections value = entry.getValue();
            /**
             * 组装日值数据
             */
            ViewParamDayEntity dayEntity= value.getDayEntity();
            TimeEntity timeEntity=value.getTimeEntity();
            timeEntity.setDay(entry.getKey());
            /**
             * 日照
             */
            sunLightValueTab.setD_RECORD_ID(UUID.randomUUID().toString());
            sunLightValueTab.setD_DATA_ID(dayCtsCode);
            sunLightValueTab.setV14032(dayEntity.getSunlightTotal());
            sunLightValueTab.setV01300(sf.getAreacode());
            String wd=sf.getWd();
            if(wd.endsWith("N")){
                wd=wd.substring(0, wd.length()-1);
            }
            sunLightValueTab.setV05001(Float.parseFloat(wd));
            String jd=sf.getJd();
            if(jd.endsWith("E")){
                jd=jd.substring(0, jd.length()-1);
            }
            sunLightValueTab.setV06001(Float.parseFloat(jd));
            sunLightValueTab.setV07001(Float.parseFloat(sf.getViewhight()));
            sunLightValueTab.setV04001(Integer.parseInt(sf.getYear()));
            sunLightValueTab.setV04002(Integer.parseInt(sf.getMonth()));
            sunLightValueTab.setV04003(Integer.parseInt(timeEntity.getDay()));
            /**
             * 小时值组装
             */
            Map<String, ViewParamsHourEntity> hour=value.getMapHour();
            Iterator<Map.Entry<String, ViewParamsHourEntity>> hoursEntries = hour.entrySet().iterator();
            List<HourValueTab> hour1=new ArrayList<>();
            while(hoursEntries.hasNext()){//小时值处理
                Map.Entry<String, ViewParamsHourEntity> entryHour = hoursEntries.next();
                ViewParamsHourEntity hourValue = entryHour.getValue();
                String hours=entryHour.getKey();
                hourValueTab= decoding.hourValueHandle(hourCts,timeEntity,hourValue,sf);
                sunLightValueTab=setSunLightToSunTable(hours,hourValue.getSunLightHour(),sunLightValueTab);
                hourList.add(hourValueTab);
                hour1.add(hourValueTab);
            }
            /**
             * 组装日值数据
             */
            dayValueTab=new DayValueTab();
            int size=hour1.size();
            for (HourValueTab hourValueTab1:hourList){
                dayValueTab = decoding.dayVlaueHandle(dayValueTab,dayCtsCode, timeEntity, dayEntity, sf,hourValueTab1,size);
            }
            dayList.add(dayValueTab);
            sunLightList.add(sunLightValueTab);
        }

        /**
         * 月值
         */
        for(DayValueTab dayValueTab1:dayList) {
            decoding.monthValueHandle(monthCts, dayValueTab1, sf, monthValueTab);
        }
        /**
         * 月极值日数、日组装
         */
        decoding.monthStatisticalDays(monthValueTab,dayList);
        /**
         * 风频统计
         */
        decoding.monthFFrequencyHandle(monthValueTab, hourList);
        /**
         * 旬值
         */
        //下旬日数据
        List<DayValueTab> maxMeadowDayValue=new ArrayList<>();
        //上旬日数据
        List<DayValueTab> minMeadowDayValue=new ArrayList<>();
        //中旬日数据
        List<DayValueTab> meadowDayValue=new ArrayList<>();
        for(int i=0,size=dayList.size();i<size;i++){
            DayValueTab dayValue=dayList.get(i);
            if(dayValue.getV04003()>20){//下旬
                maxMeadowDayValue.add(dayValue);
            }else if(dayValue.getV04003()>10){//中旬
                meadowDayValue.add(dayValue);
            }else{//上旬
                minMeadowDayValue.add(dayValue);
            }
        }
        MeadowValueTab maxMeadowValue=decoding.meadowValueHandle(meadowCts,maxMeadowDayValue,sf,"next");
        MeadowValueTab minMeadowValue=decoding.meadowValueHandle(meadowCts,minMeadowDayValue,sf,"last");;
        MeadowValueTab meadowValue=decoding.meadowValueHandle(meadowCts,meadowDayValue,sf,"mid");;
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
        if(!sunLightList.isEmpty()){
            resultMap.put("sunLight",sunLightList);
        }
        if(sf!=null){
            resultMap.put("dataTime",sf);
        }
        decodingInfo.setSuccess(true);
        resultMap.put("decodingInfo",decodingInfo);
        System.out.println("A文件组装数据完成！");
        return  resultMap;
    }
    private SunLightValueTab setSunLightToSunTable(String hours,Float sunLightHour,SunLightValueTab sunLightValueTab){
            sunLightValueTab.setV14032_001(sunLightHour);
            switch(hours) {
                case "00":
                    sunLightValueTab.setV14032_001(sunLightHour);
                    break;
                case "02":
                    sunLightValueTab.setV14032_003(sunLightHour);
                    break;
                case "03":
                    sunLightValueTab.setV14032_004(sunLightHour);
                    break;
                case "04":
                    sunLightValueTab.setV14032_005(sunLightHour);
                    break;
                case "05":
                    sunLightValueTab.setV14032_006(sunLightHour);
                    break;
                case "06":
                    sunLightValueTab.setV14032_007(sunLightHour);
                    break;
                case "07":
                    sunLightValueTab.setV14032_008(sunLightHour);
                    break;
                case "08":
                    sunLightValueTab.setV14032_009(sunLightHour);
                    break;
                case "09":
                    sunLightValueTab.setV14032_010(sunLightHour);
                    break;
                case "10":
                    sunLightValueTab.setV14032_011(sunLightHour);
                    break;
                case "11":
                    sunLightValueTab.setV14032_012(sunLightHour);
                    break;
                case "12":
                    sunLightValueTab.setV14032_013(sunLightHour);
                    break;
                case "13":
                    sunLightValueTab.setV14032_014(sunLightHour);
                    break;
                case "14":
                    sunLightValueTab.setV14032_015(sunLightHour);
                    break;
                case "15":
                    sunLightValueTab.setV14032_016(sunLightHour);
                    break;
                case "16":
                    sunLightValueTab.setV14032_017(sunLightHour);
                    break;
                case "17":
                    sunLightValueTab.setV14032_018(sunLightHour);
                    break;
                case "18":
                    sunLightValueTab.setV14032_019(sunLightHour);
                    break;
                case "19":
                    sunLightValueTab.setV14032_020(sunLightHour);
                    break;
                case "20":
                    sunLightValueTab.setV14032_021(sunLightHour);
                    break;
                case "21":
                    sunLightValueTab.setV14032_022(sunLightHour);
                    break;
                case "22":
                    sunLightValueTab.setV14032_023(sunLightHour);
                    break;
                case "23":
                    sunLightValueTab.setV14032_024(sunLightHour);
                    break;
                default: //可选
                    //语句
            }
            return sunLightValueTab;
    }
    /**
     * A文件读取
     *
     * @param path
     */
    private void aDataDecoding(File path, String fileCode) {

        List<String> list = FileHadleUtil.readFromTextFile(path,fileCode);
        // 数据存储
        List<String> dataList = new ArrayList<String>();
        // 时间
        TimeEntity te = null;
        // 观测参数标识
        String viewCode = null;
        // 数据处理开始标识
        boolean isstart = false;
        // 数据标识符
        String type = "";
        for (int i = 0, size = list.size(); i < size; i++) {
            String str = list.get(i);
            String[] arr = str.split("\\s+");
            if (isstart) { // 气压
                if (str.endsWith("=")) {
                    dataList.add(str);
                    if (i < size - 1) { // 判断下一条数据
                        str = list.get(i + 1);
                        arr = str.split("\\s+");
                        String check =null;
                        if(!"".equals(arr[0])) {
                            check = arr[0].substring(0, 1);
                        }
                        if (arr.length < 2&&check!=null&&dataTypeAll.indexOf(check) > -1) { // 当前要素完成
                            isstart = false;
                            viewCode = null;
                            Ahandle(dataList, te, type);
                            // 清理数据
                            dataList.clear();
                            type = "";
                        }
                    }
                } else {
                    dataList.add(str);
                }
                continue;
            }

            if (i == 0) { // 台站参数信息
                te = new TimeEntity();
                sf = new FirstData();
                sf.setAreacode(arr[0]);
                sf.setWd(arr[1]);
                sf.setJd(arr[2]);
                sf.setViewhight(arr[3]);
                sf.setGanshight(arr[4]);
                sf.setArcaninehight(arr[5]);
                sf.setObservehight(arr[6]);
                sf.setObversetype(arr[7]);
                sf.setObsercecode(arr[8]);
                if(arr.length>10) {
                    te.setYear(arr[10]);
                    sf.setQualitycode(arr[9]);
                    te.setMonth(arr[11]);
                    sf.setYear(arr[10]);
                    sf.setMonth(arr[11]);
                }
            } else { // 观测参数
                if (null == viewCode) {
                    if(arr[0].endsWith("=")){
                        continue;
                    }
                    viewCode = arr[0];
                    isstart = true;
                    dataList.add(str);
                    if (viewCode.startsWith("YF")) { // 封面
                        type = "YF";
                    } else if (viewCode.startsWith("JY")) { // 纪要
                        type = "JY";
                    } else if (viewCode.startsWith("BZ")) { // 备注
                        type = "BZ";
                    } else if (viewCode.startsWith("GK")) { // 本月天气气候概况
                        type = "GK";
                    } else if (viewCode.startsWith("P")) { // 气压
                        type = "P";
                    } else if (viewCode.startsWith("T")) { // 气温
                        type = "T";
                    } else if (viewCode.startsWith("I")) { // 湿球温度
                        type = "I";
                    } else if (viewCode.startsWith("E")) { // 水气压
                        type = "E";
                    } else if (viewCode.startsWith("U")) { // 相对湿度
                        type = "U";
                    } else if (viewCode.startsWith("N")) { // 云量
                        type = "N";
                    } else if (viewCode.startsWith("H")) { // 云高
                        type = "H";
                    } else if (viewCode.startsWith("C")) { // 云状
                        type = "C";
                    }else if (viewCode.startsWith("V")) { // 能见度
                        type = "V";
                    } else if (viewCode.startsWith("R")) { // 降水量
                        type = "R";
                    } else if (viewCode.startsWith("W")) { // 天气现象
                        type = "W";
                    } else if (viewCode.startsWith("L")) { // 蒸发量
                        type = "L";
                    } else if (viewCode.startsWith("Z")) { // 积雪
                        type = "Z";
                    } else if (viewCode.startsWith("G")) { // 电线积冰
                        type = "G";
                    } else if (viewCode.startsWith("F")) { // 风
                        type = "F";
                    } else if (viewCode.startsWith("D")) { // 浅层地温
                        type = "D";
                    } else if (viewCode.startsWith("K")) { // 深层地温
                        type = "K";
                    } else if (viewCode.startsWith("A")) { // 冻土深度
                        type = "A";
                    } else if (viewCode.startsWith("S")) { // 日照时数
                        type = "S";
                    } else if (viewCode.startsWith("B")) { // 草面温度
                        type = "B";
                    }
                }
            }
        }
    }
    /**
     * A文件处理方法
     * @param list
     * @param te
     * @param type
     */
    private void Ahandle(List<String> list, TimeEntity te, String type) {
        if ("YF".startsWith(type)) { // 封面
            FMHandle(list, te);
        } else if ("JY".startsWith(type)) { // 纪要
            summaryHandle(list, te);
        } else if ("BZ".startsWith(type)) { // 备注
            noteHandle(list, te);
        } else if ("GK".startsWith(type)) { // 本月天气气候概况
            climateProbablyHandle(list, te);
        } else if ("P".startsWith(type)) { // 气压
            gansHandle(list, te);
        } else if ("T".startsWith(type)) { // 气温
            tempHandle(list, te);
        } else if ("I".startsWith(type)) { // 湿球温度
            iballHandle(list, te);
        } else if ("E".startsWith(type)) { // 水气压
            waterHandle(list, te);
        } else if ("U".startsWith(type)) { // 相对湿度
            wetHandle(list, te);
        } else if ("N".startsWith(type)) { // 云量
            cloudHandle(list,te);
        } else if ("H".startsWith(type)) { // 云高
            cloudHightHandle(list, te);
        } else if (type.startsWith("C")) { // 云状
            cloudShapHandle(list, te);
        } else if ("V".startsWith(type)) { // 能见度
            visibilityHandle(list, te);
        } else if ("R".startsWith(type)) { // 降水量
            rainHandle(list, te);
        } else if ("W".startsWith(type)) { // 天气现象
            wetherHandle(list, te);
        } else if ("L".startsWith(type)) { // 蒸发量
            evaporatHandle(list, te);
        } else if ("Z".startsWith(type)) { // 积雪
            snowHandle(list, te);
        } else if ("G".startsWith(type)) { // 电线积冰
            iceHandle(list, te);
        } else if ("F".startsWith(type)) { // 风
            windHandle(list, te);
        } else if ("D".startsWith(type)) { // 浅层地温
            zoneHandle(list, te);
        } else if ("K".startsWith(type)) { // 深层地温
            deepHandle(list, te);
        } else if ("A".startsWith(type)) { // 冻土深度
            frozenHandle(list, te);
        } else if ("S".startsWith(type)) { // 日照时数
            sunHandle(list, te);
        } else if ("B".startsWith(type)) { // 草面温度
            grassHandle(list, te);
        }
    }
    /**
     * 云状处理
     * @param list
     * @param te
     */
    private void cloudShapHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split(",");
        String viewCode = arr[0];
        if (viewCode.endsWith("0")||viewCode.endsWith("9")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split(",", -1);
                te.setDay("" + i);
                timerTaskHandle(arr, "C", te);
            }
        } else if (viewCode.endsWith("A")) {
            int day=1;
            for (int i = 1, size = list.size(); i < size; i+=4) {
                str = list.get(i);
                arr = str.split(",", -1);
                te.setDay(day+"");
                day++;
                //8个小时（21-4时）
                currentHourHandlle(arr, "C",21, te);
                //5个小时（5-9时）
                str = list.get(i+1);
                arr = str.split(",", -1);
                currentHourHandlle(arr, "C",5, te);
                //10-14时
                str = list.get(i+2);
                arr = str.split(",", -1);
                currentHourHandlle(arr, "C",10, te);
                //6个时次（15-20时）
                str = list.get(i+3);
                arr = str.split(",", -1);
                currentHourHandlle(arr, "C",15, te);
            }
        }

    }

    /**
     * 云高数据处理
     * @param list
     * @param te
     */
    private void cloudHightHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        if (viewCode.endsWith("1")) {

        }
    }
    /**
     * 云量数据处理
     * @param list
     * @param te
     */
    private void cloudHandle(List<String> list, TimeEntity te){
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        int dayCount=te.getDayCountOnMonth();
        if (viewCode.endsWith("0")||viewCode.endsWith("9")) {
            int currentType=1;
            int day=1;
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
              te.setDay(day+"");
                if (currentType==2) {
                    timerTaskHandle(arr,"N2", te);
                }
                if(currentType==1){
                    timerTaskHandle(arr, "N1", te);
                }
                day++;
                if(str.endsWith("=")){
                    currentType=2;
                    day=1;
                }
            }
        }else if (viewCode.endsWith("A")) {
            int currentType=1;
            int day=1;
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                    //21时
                    String[] arr1=new String[12];
                    //09时
                    String[] arr2=new String[12];
                    for(int j=0,length1=arr.length;j<length1;j++){
                        if(j<12){
                            arr1[j]=arr[j];
                        }else{
                            arr2[j-12]=arr[j];
                        }
                    }
                    te.setDay(day+"");
                    if(currentType==1){
                        currentHourHandlle(arr2, "N1",9, te);
                        currentHourHandlle(arr1, "N1",21, te);
                    }
                    if(currentType==2){
                        currentHourHandlle(arr2, "N2",9, te);
                        currentHourHandlle(arr1, "N2",21, te);
                    }

                    day++;

                if(str.endsWith("=")){
                    currentType=2;
                    day=1;
                }
            }
        }
    }
    /**
     * 冻土数据处理
     *
     * @param list
     * @param te
     */
    private void frozenHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        if (viewCode.endsWith("0")||viewCode.endsWith("6")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                te.setDay(i+"");
                arr = str.split("\\s+");
                extremumHandle(arr,"A",te);
            }
        }
    }
    /**
     * 日照数据处理
     *
     * @param list
     * @param te
     */
    private void sunHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        String[] arr1=null;
        String[] arr2=null;
        if (viewCode.endsWith("0")||viewCode.endsWith("2")||viewCode.endsWith("A")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                te.setDay(i+"");
                arr = str.split("\\s+");
                if(arr.length>19) {
                    arr1=new String[24];
                    arr2 =new String[3];
                    for(int j=0,k=arr.length;j<k;j++){
                        if(j>23){
                            arr2[j-24]=arr[j];
                        }else{
                            arr1[j]=arr[j];
                        }
                    }
                    currentHourHandlle(arr1,"S",1, te);
                }else if(arr.length>2){
                    arr1=new String[18];
                    arr2=new String[1];
                    for(int j=0,k=arr.length;j<k;j++){
                        if(j>17){
                            arr2[j-18]=arr[j];
                        }else{
                            arr1[j]=arr[j];
                        }
                    }
                    currentHourHandlle(arr1,"S",3, te);
                }else{
                    arr2=arr;
                }
                extremumHandle(arr2, "S", te);
            }
        }
    }
    /**
     * 草面数据处理
     *
     * @param list
     * @param te
     */
    private void grassHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        String[] arr1=null;
        String[] arr2=null;
        int day=1;
        if (viewCode.endsWith("A")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                te.setDay(i + "");
                arr = str.split("\\s+");
                te.setDay(day+"");
                if(i%2==0){
                    arr1=new String[12];
                    arr2=new String[4];
                    for(int j=0,k=arr.length;j<k;j++){
                        if(j>11){
                            arr2[j-12]=arr[j];
                        }else{
                            arr1[j]=arr[j];
                        }
                    }
                    currentHourHandlle(arr1,"B",9, te);
                    extremumHandle(arr2,"B",te);
                    day++;
                }else{
                    currentHourHandlle(arr,"B",21, te);
                }
            }
        }
    }
    /**
     * 蒸发量数据处理
     *
     * @param list
     * @param te
     */
    private void evaporatHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        int dayCount=te.getDayCountOnMonth();
        String str1=null;
        if (viewCode.endsWith("0")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str1=str.substring(0,str.indexOf("="));
                    arr = str1.split("\\s+",-1);
                }else {
                    arr = str.split("\\s+");
                }
                if(arr.length==1&&"".equals(arr[0])){
                    continue;
                }
                if("".equals(arr[0])&&arr.length==0) {
                    continue;
                }
                if (i>dayCount) {
                    te.setDay((i-dayCount)+"");
                    extremumHandle(arr, "L2", te);
                } else {
                    te.setDay(i+"");
                    extremumHandle(arr,"L1",te);
                }
            }
        } else if (viewCode.endsWith("A")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str1=str.substring(0,str.indexOf("="));
                    arr = str1.split("\\s+",-1);
                }else {
                    arr = str.split("\\s+");
                }
                if (i>dayCount) {
                    te.setDay((i-dayCount)+"");
                    if(i%2==0){
                        String[] arr1=new String[12];
                        String[] arr2=new String[1];
                        for(int j=0,k=arr.length;j<k;j++){
                            if(j>12){
                                arr2[j-12]=arr[j];
                            }else{
                                arr1[j]=arr[j];
                            }
                        }
                        if(!"".equals(arr2[0])&&arr2[0]!=null) {
                            extremumHandle(arr2, "L2", te);
                        }
                        currentHourHandlle(arr1,"L2",9, te);
                    }else{
                        currentHourHandlle(arr,"L2",21, te);
                    }
                } else {
                    te.setDay(i+"");
                    extremumHandle(arr,"L1",te);
                }
            }
        } else if (viewCode.endsWith("B")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str1=str.substring(0,str.indexOf("="));
                    arr = str1.split("\\s+",-1);
                }else {
                    arr = str.split("\\s+");
                }
                if (i>dayCount) {
                    te.setDay((i-dayCount)+"");
                    if(i%2==0){
                        currentHourHandlle(arr,"L2",9, te);
                    }else{
                        currentHourHandlle(arr,"L2",21, te);
                    }
                    extremumHandle(arr,"L2",te);
                } else {
                    te.setDay(i+"");
                    extremumHandle(arr,"L1",te);
                }
            }
        }
    }
    /**
     * 积雪数据处理
     *
     * @param list
     * @param te
     */
    private void snowHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        String str1=null;
        if (viewCode.endsWith("0")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str1=str.substring(0,str.indexOf("="));
                    arr = str1.split("\\s+",-1);
                }else {
                    arr = str.split("\\s+");
                }
                if(i<10){
                    te.setDay("0"+i);
                }else{
                    te.setDay(i+"");
                }
                extremumHandle(arr,"Z",te);
            }
        }
    }
    /**
     * 电线积冰数据处理
     *
     * @param list
     * @param te
     */
    private void iceHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        String str1=null;
        int dayCount=te.getDayCountOnMonth();
        if (viewCode.endsWith("0")) {
            int currentType=1;
            int day=1;
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str1=str.substring(0,str.indexOf("="));
                    arr = str1.split("\\s+",-1);
                }else {
                    arr = str.split("\\s+");
                }
              te.setDay(day+"");
                if(currentType==2){//雾凇
                    extremumHandle(arr,"G2",te);
                }
                if(currentType==1){//雨凇
                    extremumHandle(arr,"G1",te);
                }
                if(str.endsWith("=")){
                    currentType=2;
                    day=1;
                }
                day++;
            }
        } else if (viewCode.endsWith("2")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str1=str.substring(0,str.indexOf("="));
                    arr = str1.split("\\s+",-1);
                }else {
                    arr = str.split("\\s+");
                }
                String rimeType=arr[0].substring(1);
                String glazeType=arr[0].substring(0,2);
                String[] arr1=new String[8];
                for (int j=1,k=arr.length;j<k;j++){
                    arr1[j-1]=arr[j];
                }
                te.setDay(i+"");
                if(!"00".equals(rimeType)){//雾凇
                    extremumHandle(arr1,"G2",te);
                }else if(!"00".equals(glazeType)){//雨凇
                    extremumHandle(arr1,"G1",te);
                }
            }
        }
    }
    /**
     * 风数据处理
     *
     * @param list
     * @param te
     */
    private void windHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        int dayCount=te.getDayCountOnMonth();
        List<String> dayList=new ArrayList<>();
        boolean flag=false;
        //21-8
        String[] arr1=null;
        //9-20
        String[] arr2=null;
        String str1=null;
        if (viewCode.endsWith("E")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str1=str.substring(0,str.indexOf("="));
                    arr = str1.split("\\s+",-1);
                }else {
                    arr = str.split("\\s+");
                }

                if(i>5*dayCount){//极大最大风段
                    te.setDay((i-7*dayCount)+"");
                    extremumHandle(arr,"F",te);
                }else if(i>dayCount){
                    for (int j = 0, k = arr.length; j < k; j++) {
                        dayList.add(arr[j]);
                    }
                    if (str.endsWith(".")) {
                        flag = true;
                    }
                    if (dayList.size() == 12) {
                        if (!flag) {
                            arr1 = CommonUtil.listToArray(dayList);
                            currentHourHandlle(arr1,"F2",21, te);
                            dayList.clear();
                        } else {
                            arr2 = CommonUtil.listToArray(dayList);
                            //10分钟平均风速
                            currentHourHandlle(arr2,"F2",9, te);
                        }
                    }
                }else{
                    timerTaskHandle(arr,"F1", te);
                }
            }
        } else if (viewCode.endsWith("H")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str1=str.substring(0,str.indexOf("="));
                    arr = str1.split("\\s+",-1);
                }else {
                    arr = str.split("\\s+");
                }
                if(i>5*dayCount){//极大最大风段
                    te.setDay((i-7*dayCount)+"");
                    extremumHandle(arr,"F",te);
                }else if(i>dayCount){
                    for (int j = 0, k = arr.length; j < k; j++) {
                        dayList.add(arr[j]);
                    }
                    if (str.endsWith(".")) {
                        flag = true;
                    }
                    if (dayList.size() == 12) {
                        if (!flag) {
                            arr1 = CommonUtil.listToArray(dayList);
                            currentHourHandlle(arr1,"F2",21, te);
                            dayList.clear();
                        } else {
                            arr2 = CommonUtil.listToArray(dayList);
                            //10分钟平均风速
                            currentHourHandlle(arr2,"F2",9, te);
                        }
                    }
                }else{
                    timerTaskHandle(arr,"F1", te);
                }
            }
        } else if (viewCode.endsWith("K")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str1=str.substring(0,str.indexOf("="));
                    arr = str1.split("\\s+",-1);
                }else {
                    arr = str.split("\\s+");
                }
                if(i>8*dayCount){//极大最大风段
                    te.setDay((i-8*dayCount)+"");
                    extremumHandle(arr,"F",te);
                }else {
                    for (int j = 0, k = arr.length; j < k; j++) {
                        dayList.add(arr[j]);
                    }
                    if (str.endsWith(".")) {
                        flag = true;
                    }
                    if (dayList.size() == 12) {
                        if (!flag) {
                            arr1 = CommonUtil.listToArray(dayList);
                            if(i>4*dayCount){//10分钟平均风速
                                currentHourHandlle(arr1,"F2",21, te);
                            }else{
                                currentHourHandlle(arr1,"F1",21, te);
                            }
                            dayList.clear();
                        } else {
                            arr2 = CommonUtil.listToArray(dayList);
                            if(i>4*dayCount){//10分钟平均风速
                                currentHourHandlle(arr2,"F2",9, te);
                            }else{
                                currentHourHandlle(arr2,"F1",9, te);
                            }
                        }
                    }
                }

            }
        } else if (viewCode.endsWith("N")) {
            int day=1;
            for (int i = 1, length = list.size(); i < length; i++) {
                if(day<10){
                    te.setDay("0"+day);
                }else{
                    te.setDay(day+"");
                }
                if(i<8*dayCount) {
                    if (i % 4 == 0) {
                        day++;
                    }
                }
                str = list.get(i);
                if(str.endsWith("=")){
                    str1=str.substring(0,str.indexOf("="));
                    arr = str1.split("\\s+",-1);
                }else {
                    arr = str.split("\\s+");
                }
                if(i>8*dayCount){//极大最大风段
                    te.setDay((i-8*dayCount)+"");
                    extremumHandle(arr,"F",te);
                }else {
                    for (int j = 0, k = arr.length; j < k; j++) {
                        dayList.add(arr[j]);
                    }
                    if (str.endsWith(".")) {
                        flag = true;
                    }
                    if (dayList.size() == 12) {
                        if (!flag) {
                            arr1 = CommonUtil.listToArray(dayList);
                            if(i>4*dayCount){//10分钟平均风速
                                currentHourHandlle(arr1,"F2",21, te);
                            }else{
                                currentHourHandlle(arr1,"F1",21, te);
                            }
                            dayList.clear();
                        } else {
                            arr2 = CommonUtil.listToArray(dayList);
                            if(i>4*dayCount){//10分钟平均风速
                                currentHourHandlle(arr2,"F2",9, te);
                            }else{
                                currentHourHandlle(arr2,"F1",9, te);
                            }
                        }
                    }
                }

            }
        }
    }
    /**
     * 浅层地温数据处理
     *
     * @param list
     * @param te
     */
    private void zoneHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        int dayCount=te.getDayCountOnMonth();
        if (viewCode.endsWith("0")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                if(i>5*dayCount){//40cm
                    te.setDay(i-dayCount*5+"");
                    timerTaskHandle(arr,"D6", te);
                }else if(i>4*dayCount){
                    te.setDay(i-dayCount*4+"");
                    timerTaskHandle(arr,"D5", te);
                }else if(i>3*dayCount){
                    te.setDay(i-dayCount*3+"");
                    timerTaskHandle(arr,"D4", te);
                }else if(i>2*dayCount){
                    te.setDay(i-dayCount*2+"");
                    timerTaskHandle(arr,"D3", te);
                }else if(i>dayCount){
                    te.setDay(i-dayCount+"");
                    timerTaskHandle(arr,"D2", te);
                }else{
                    te.setDay(i+"");
                    String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                    String[] arr2={arr[4],arr[5]};
                    extremumHandle(arr2,"D1",te);
                    timerTaskHandle(arr1,"D1", te);
                }
            }
        } else if (viewCode.endsWith("1")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                if(i>5*dayCount){//40cm
                    te.setDay(i-dayCount*5+"");
                    timerTaskHandle(arr,"D6", te);
                }else if(i>4*dayCount){
                    te.setDay(i-dayCount*4+"");
                    timerTaskHandle(arr,"D5", te);
                }else if(i>3*dayCount){
                    te.setDay(i-dayCount*3+"");
                    timerTaskHandle(arr,"D4", te);
                }else if(i>2*dayCount){
                    te.setDay(i-dayCount*2+"");
                    timerTaskHandle(arr,"D3", te);
                }else if(i>dayCount){
                    te.setDay(i-dayCount+"");
                    timerTaskHandle(arr,"D2", te);
                }else{
                    te.setDay(i+"");
                    String[] arr1={arr[0],arr[1],arr[2]};
                    String[] arr2={arr[3],arr[4]};
                    extremumHandle(arr2,"D1",te);
                    timerTaskHandle(arr1,"D1", te);
                }
            }
        } else if (viewCode.endsWith("2")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                if(i>5*dayCount){//40cm
                    te.setDay(i-dayCount*5+"");
                    timerTaskHandle(arr,"D6", te);
                }else if(i>4*dayCount){
                    te.setDay(i-dayCount*4+"");
                    timerTaskHandle(arr,"D5", te);
                }else if(i>3*dayCount){
                    te.setDay(i-dayCount*3+"");
                    timerTaskHandle(arr,"D4", te);
                }else if(i>2*dayCount){
                    te.setDay(i-dayCount*2+"");
                    timerTaskHandle(arr,"D3", te);
                }else if(i>dayCount){
                    te.setDay(i-dayCount+"");
                    timerTaskHandle(arr,"D2", te);
                }else{
                    te.setDay(i+"");
                    timerTaskHandle(arr,"D1", te);
                }
            }
        } else if (viewCode.endsWith("7")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                if(i>5*dayCount){//40cm
                    te.setDay(i-dayCount*5+"");
                    timerTaskHandle(arr,"D6", te);
                }else if(i>4*dayCount){
                    te.setDay(i-dayCount*4+"");
                    timerTaskHandle(arr,"D5", te);
                }else if(i>3*dayCount){
                    te.setDay(i-dayCount*3+"");
                    timerTaskHandle(arr,"D4", te);
                }else if(i>2*dayCount){
                    te.setDay(i-dayCount*2+"");
                    timerTaskHandle(arr,"D3", te);
                }else if(i>dayCount){
                    te.setDay(i-dayCount+"");
                    timerTaskHandle(arr,"D2", te);
                }else{
                    te.setDay(i+"");
                    String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                    String[] arr2={arr[4],arr[5]};
                    extremumHandle(arr2,"D1",te);
                    timerTaskHandle(arr1,"D1", te);
                }
            }
        } else if (viewCode.endsWith("8")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                if(i>5*dayCount){//40cm
                    te.setDay(i-dayCount*5+"");
                    timerTaskHandle(arr,"D6", te);
                }else if(i>4*dayCount){
                    te.setDay(i-dayCount*4+"");
                    timerTaskHandle(arr,"D5", te);
                }else if(i>3*dayCount){
                    te.setDay(i-dayCount*3+"");
                    timerTaskHandle(arr,"D4", te);
                }else if(i>2*dayCount){
                    te.setDay(i-dayCount*2+"");
                    timerTaskHandle(arr,"D3", te);
                }else if(i>dayCount){
                    te.setDay(i-dayCount+"");
                    timerTaskHandle(arr,"D2", te);
                }else{
                    te.setDay(i+"");
                    timerTaskHandle(arr,"D1", te);
                }
            }
        } else if (viewCode.endsWith("9")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                if(i>5*dayCount){//40cm
                    te.setDay(i-dayCount*5+"");
                    timerTaskHandle(arr,"D6", te);
                }else if(i>4*dayCount){
                    te.setDay(i-dayCount*4+"");
                    timerTaskHandle(arr,"D5", te);
                }else if(i>3*dayCount){
                    te.setDay(i-dayCount*3+"");
                    timerTaskHandle(arr,"D4", te);
                }else if(i>2*dayCount){
                    te.setDay(i-dayCount*2+"");
                    timerTaskHandle(arr,"D3", te);
                }else if(i>dayCount){
                    te.setDay(i-dayCount+"");
                    timerTaskHandle(arr,"D2", te);
                }else{
                    te.setDay(i+"");
                    String[] arr1={arr[0],arr[1],arr[2]};
                    String[] arr2={arr[3],arr[4]};
                    extremumHandle(arr2,"D1",te);
                    timerTaskHandle(arr1,"D1", te);
                }
            }
        } else if (viewCode.endsWith("B")) {
            int day=1;
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                if(i>10*dayCount){//40cm
                    if(i%2==0){
                        day++;
                        currentHourHandlle(arr,"D6",9, te);
                    }else{
                        te.setDay(day-dayCount*5+"");
                        currentHourHandlle(arr,"D6",21, te);
                    }
                }else if(i>8*dayCount){
                    if(i%2==0){
                        day++;
                        currentHourHandlle(arr,"D5",9, te);
                    }else{
                        te.setDay(day-dayCount*4+"");
                        currentHourHandlle(arr,"D5",21, te);
                    }
                }else if(i>6*dayCount){
                    if(i%2==0){
                        day++;
                        currentHourHandlle(arr,"D4",9, te);
                    }else{
                        te.setDay(day-dayCount*3+"");
                        currentHourHandlle(arr,"D4",21, te);
                    }
                }else if(i>4*dayCount){
                    if(i%2==0){
                        day++;
                        currentHourHandlle(arr,"D3",9, te);
                    }else{
                        te.setDay(day-dayCount*2+"");
                        currentHourHandlle(arr,"D3",21, te);
                    }
                }else if(i>dayCount*2){
                    if(i%2==0){
                        day++;
                        currentHourHandlle(arr,"D2",9, te);
                    }else{
                        te.setDay(day-dayCount+"");
                        currentHourHandlle(arr,"D2",21, te);
                    }
                }else{
                    String[] arr1=new String[12];
                    String[] arr2=new String[4];
                   if(i%2==0){
                       for(int j=0,k=arr.length;j<k;j++){
                           if(j>11){
                               arr2[j-12]=arr[j];
                           }else{
                               arr1[j]=arr[j];
                           }
                       }
                       if(arr2[0]!=null){
                           extremumHandle(arr2,"D1",te);
                       }
                       if(arr1[0]!=null) {
                           currentHourHandlle(arr1, "D1", 9, te);
                       }
                       day++;
                   }else{
                       te.setDay(day+"");
                       currentHourHandlle(arr,"D1",21, te);
                   }
                }
            }
        }
    }
    /**
     * 深层数据处理
     *
     * @param list
     * @param te
     */
    private void deepHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        int dayCount=te.getDayCountOnMonth();
        if (viewCode.endsWith("0")||viewCode.endsWith("1")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                te.setDay(""+i);
                extremumHandle(arr,"K",te);
            }
        } else if (viewCode.endsWith("B")) {
            int day3=1;
            int day2=1;
            int day1=1;
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+");

               if(i>4*dayCount){//320cm
                    if(i%2==0){
                        day3++;
                        currentHourHandlle(arr,"k3",9, te);
                    }else{
                        te.setDay(day3+"");
                        currentHourHandlle(arr,"k3",21, te);
                    }
               }else if(i>dayCount*2){//160cm
                   if(i%2==0){
                       day2++;
                       currentHourHandlle(arr,"k2",9, te);
                   }else{
                       te.setDay(day2+"");
                       currentHourHandlle(arr,"k2",21, te);
                   }
               }else{//80cm
                   if(i%2==0){
                       day1++;
                       currentHourHandlle(arr,"k1",9, te);
                   }else{
                       te.setDay(day1+"");
                       currentHourHandlle(arr,"k1",21, te);
                   }
               }
            }
        }
    }
    /**
     * 天气现象数据处理
     *
     * @param list
     * @param te
     */
    private void wetherHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        if (viewCode.endsWith("0")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                te.setDay(i+"");
                extremumHandle(arr,"W",te);
            }
        }
    }
    /**
     * 降水量数据处理
     *
     * @param list
     * @param te
     */
    private void rainHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        String str1=null;
        int dayCount=te.getDayCountOnMonth();
        if (viewCode.endsWith("0")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str1=str.substring(0,str.indexOf("="));
                    arr = str1.split("\\s+",-1);
                }else {
                    arr = str.split("\\s+");
                }
                if (i> dayCount) { // 自记降水量
                    te.setDay((i-dayCount)+"");
                    extremumHandle(arr,"R",te);
                } else { // 3组-定时降水量
                    te.setDay(i+"");
                    extremumHandle(arr, "R", te);
                }
            }
        }  else if (viewCode.endsWith("6")||viewCode.endsWith("2")) { // 08、14、20三组或者24组
            int flag=1;
            int day=1;
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str1=str.substring(0,str.indexOf("="));
                    arr = str1.split("\\s+",-1);
                }else {
                    arr = str.split("\\s+");
                }

                if (flag==3) { // 连接值段降水
                    System.out.println("降水连接值段："+1231231);
                }
                if(flag==2) { // 24组-定时降水量
                    if (str.endsWith(".")||str.endsWith("=")) {
                        day++;
                        currentHourHandlle(arr, "R", 9, te);
                    } else {
                        te.setDay(day + "");
                        currentHourHandlle(arr, "R", 21, te);
                    }
                }
                if(flag==1){ // 3组-定时降水量
                    te.setDay(i+"");
                    extremumHandle(arr, "R", te);
                }
                if(str.endsWith("=")){
                    flag++;
                    continue;
                }
            }
        }
    }
    /**
     * 能见度数据处理
     *
     * @param list
     * @param te
     */
    private void visibilityHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        int dayCount=te.getDayCountOnMonth();
        if (viewCode.endsWith("0")) {//KM
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "V1", te);
            }
        } else if (viewCode.endsWith("7")) {//级别
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                String[] arr1={arr[0],arr[1],arr[2]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "V2", te);
            }
        } else if (viewCode.endsWith("8")) {//级别
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "V2", te);
            }
        } else if (viewCode.endsWith("9")) {//km
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                String[] arr1={arr[0],arr[1],arr[2]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "V1", te);
            }
        } else if (viewCode.endsWith("A")) {//km
            int day=1;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                if(i%2==0){
                    day++;
                    currentHourHandlle(arr,"V1",9, te);
                }else{
                    te.setDay(""+day);
                    currentHourHandlle(arr,"V1",21, te);
                }
            }
        }
    }

    /**
     * 气压处理
     *
     * @param list
     * @param te 年-月
     */
    private void gansHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        int dayCount=te.getDayCountOnMonth();
        if (viewCode.endsWith("3")) {
            int day=1;
            int currentType=1;
            String str2=null;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str2=str.substring(0,str.indexOf("="));
                    arr = str2.split("\\s+");
                }else {
                    arr = str.split("\\s+");
                }

                if(day<10) {
                    te.setDay("0"+day );
                }else if(day<=dayCount){
                    te.setDay(""+day);
                }
                if(currentType==2) {
                    //海平面气压
                    timerTaskHandle(arr,"P0", te);
                }
                if(currentType==1){
                    //本站气压
                    String[] arr1 = {arr[0], arr[1], arr[2], arr[3]};
                    String[] arr2 = {arr[4], arr[5]};
                    timerTaskHandle(arr1,"P", te);
                    extremumHandle(arr2,"P",te);
                }
                day++;
                if(str.endsWith("=")){
                    currentType=2;
                    day = 1;
                }
            }
        } else if (viewCode.endsWith("4")) {
            int day=1;
            int currentType=1;
            String str2=null;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str2=str.substring(0,str.indexOf("="));
                    arr = str2.split("\\s+");
                }else {
                    arr = str.split("\\s+");
                }

                if(day<10) {
                    te.setDay("0"+day );
                }else if(day<=dayCount){
                    te.setDay(""+day);
                }

                if(currentType==2) {
                    //海平面气压
                    timerTaskHandle(arr,"P0", te);
                }
                if(currentType==1){
                    //本站气压
                    timerTaskHandle(arr,"P", te);
                }
                day++;
                if(str.endsWith("=")){
                    currentType=2;
                    day = 1;
                }
            }
        } else if (viewCode.endsWith("6")) {
            int day=1;
            int currentType=1;
            String str2=null;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str2=str.substring(0,str.indexOf("="));
                    arr = str2.split("\\s+");
                }else {
                    arr = str.split("\\s+");
                }


                if(i<10) {
                    te.setDay("0"+day );
                }else  if(day<=dayCount){
                    te.setDay(""+day);
                }
                if(currentType==2) {
                    //海平面气压
                    timerTaskHandle(arr,"P0", te);
                }
                if(currentType==1){
                    //本站气压
                    String[] arr1 = {arr[0], arr[1], arr[2]};
                    String[] arr2 = {arr[3], arr[4]};
                    extremumHandle(arr2,"P",te);
                    timerTaskHandle(arr1,"P", te);
                }
                day++;
                if(str.endsWith("=")){
                    currentType=2;
                    day = 1;
                }
            }
        } else if (viewCode.endsWith("8")) {
            int day=1;
            int currentType=1;
            String str2=null;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str2=str.substring(0,str.indexOf("="));
                    arr = str2.split("\\s+");
                }else {
                    arr = str.split("\\s+");
                }

                if(day<10) {
                    te.setDay("0"+day );
                }else if(day<=dayCount){
                    te.setDay(""+day);
                }
                if(currentType==2) {
                    //海平面气压
                    timerTaskHandle(arr,"P0", te);
                }
                if(currentType==1){
                    //本站气压
                    timerTaskHandle(arr,"P", te);
                }
                day++;
                if(str.endsWith("=")){
                    currentType=2;
                    day = 1;
                }
            }
        } else if (viewCode.endsWith("B")||viewCode.endsWith("C")) {
            int day=1;
            int currentType=1;
            String str2=null;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str2=str.substring(0,str.indexOf("="));
                    arr = str2.split("\\s+");
                }else {
                    arr = str.split("\\s+");
                }
                int length=arr.length;

                if(day<10) {
                    te.setDay("0"+day );
                }else  if(day<=dayCount){
                    te.setDay(""+day);
                }
                if(currentType==2){// 海平面气压
                    timerTaskHandle(arr, "P0", te);
                    day++;
                }else {
                    if (length > 12) {
                        if (i % 2 == 0) {
                            day++;
                        }
                        //09~20
                        String[] arr2 = new String[12];
                        //极值
                        String[] arr3 = new String[4];
                        for (int j = 0, k = arr.length; j < k; j++) {
                            if (j < 12) {
                                arr2[j] = arr[j];
                            } else {
                                arr3[j - 12] = arr[j];
                            }
                        }
                        // 09-20时记录
                        currentHourHandlle(arr2, "P", 9, te);
                        //最值
                        extremumHandle(arr3, "P", te);
                    } else if (length > 4) {
                        // 21-08时数据
                        currentHourHandlle(arr, "P", 21, te);
                    }
                }
                if(str.endsWith("=")){
                    currentType=2;
                    day = 1;
                }
            }
        }else if (viewCode.endsWith("D")) {
            int day=1;
            int currentType=1;
            String str2=null;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str2=str.substring(0,str.indexOf("="));
                    arr = str2.split("\\s+");
                }else {
                    arr = str.split("\\s+");
                }
                int length=arr.length;

                if(day<10) {
                    te.setDay("0"+day );
                }else if(day<=dayCount){
                    te.setDay(""+day);
                }
                if(currentType==1){//气压
                    if(length>12){
                        if(i%2==0) {
                            day++;
                        }
                        //09~20
                        String[] arr2=new String[12];
                        //极值
                        String[] arr3=new String[12];
                        for(int j=0,k=arr.length;j<k;j++){
                            if(j<12){
                                arr2[j]=arr[j];
                            }else{
                                arr[j-12]=arr[j];
                            }
                        }
                        // 09-20时记录
                        currentHourHandlle(arr2, "P",9, te);
                        //最值
                        extremumHandle(arr3, "P", te);
                        day++;
                    }else{
                        // 21-08时数据
                        currentHourHandlle(arr, "P",21, te);
                    }
                }
                if(currentType==2){//海平面气压
                    if(i%2==0){
                        // 21-08时数据
                        currentHourHandlle(arr, "P0",21, te);
                    }else { // 海平面气压
                        // 09-20时记录
                        currentHourHandlle(arr, "P0",9, te);
                        day++;
                    }
                }
                if(str.endsWith("=")){
                    currentType=2;
                    day = 1;
                }
            }
        }
    }

    /**
     * 气温处理
     *
     * @param list
     * @param te
     */
    private void tempHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        String str2=null;
        if (viewCode.endsWith("0")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str2=str.substring(0,str.indexOf("="));
                    arr = str2.split("\\s+");
                }else {
                    arr = str.split("\\s+");
                }
                te.setDay(i+"");
                String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                String[] arr2={arr[4],arr[5]};
                timerTaskHandle(arr1,"T", te);
                extremumHandle(arr2,"T",te);
            }
        } else if (viewCode.endsWith("9")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str2=str.substring(0,str.indexOf("="));
                    arr = str2.split("\\s+");
                }else {
                    arr = str.split("\\s+");
                }
                te.setDay(i+"");
                String[] arr1={arr[0],arr[1],arr[2]};
                String[] arr2={arr[3],arr[4]};
                timerTaskHandle(arr1,"T", te);
                extremumHandle(arr2,"T",te);
            }
        } else if (viewCode.endsWith("A")||viewCode.endsWith("B")) {
            int day=1;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                if(str.endsWith("=")){
                    str2=str.substring(0,str.indexOf("="));
                    arr = str2.split("\\s+");
                }else {
                    arr = str.split("\\s+");
                }
                if (arr.length > 12) { // 带最值
                    day++;
                    String[] arr1=new String[12];
                    String[] arr2=new String[4];
                    for(int j=0,k=arr.length;j<k;j++){
                        if(j<12){
                            arr1[j]=arr[j];
                        }else{
                            arr2[j-12]=arr[j];
                        }
                    }
                    currentHourHandlle(arr1,"T",9, te);
                    extremumHandle(arr2,"T",te);
                } else {
                    te.setDay(day+"");
                    // 21-08时记录
                    currentHourHandlle(arr, "T",21, te);
                }
            }
        }
    }

    /**
     * 湿球温度处理
     *
     * @param list
     * @param te
     */
    private void iballHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];

        int dayCount=te.getDayCountOnMonth();
        if (viewCode.endsWith("2")||viewCode.endsWith("7")||viewCode.endsWith("8")) {
            int currentType=1;
            int day=1;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                if(day<10){
                    te.setDay("0"+day);
                }else{
                    te.setDay(i+"");
                }
                if(currentType==2){//露点温度
                    timerTaskHandle(arr,"I2", te);
                }
                if(currentType==1){//湿球
                    timerTaskHandle(arr,"I1", te);
                }
                day++;
                if(str.endsWith("=")){
                    currentType=2;
                    day=1;
                }
            }
        } else if (viewCode.endsWith("B")) {
            int day=1;
            int currentType=1;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                if(currentType==2){//露点
                    if (str.endsWith(".")||str.endsWith("=")) { // 09-20时
                        day++;
                        currentHourHandlle(arr, "I2",9,te);
                    }else{
                        te.setDay(day+"");
                        currentHourHandlle(arr, "I2",21,te);

                    }
                }
                if(currentType==1){//湿球
                    if (str.endsWith(".")) { // 09-20时
                        day++;
                        currentHourHandlle(arr, "I1",9,te);
                    } else { // 21-08时
                        te.setDay(day+"");
                        currentHourHandlle(arr, "I1",21,te);
                    }
                }
                if(str.endsWith("=")){
                    currentType=2;
                    day=1;
                }
            }
        }
    }

    /**
     * 水汽压数据处理
     *
     * @param list
     * @param te
     */
    private void waterHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        if (viewCode.endsWith("0")||viewCode.endsWith("9")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                te.setDay(i+"");
                timerTaskHandle(arr, "E",te);
            }
        }else if (viewCode.endsWith("A")) {
            int day=1;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                if(i%2==0){
                    day++;
                    currentHourHandlle(arr, "E",9, te);
                }else{
                    te.setDay(day+"");
                    currentHourHandlle(arr,"E",21, te);
                }
            }
        }
    }
    /**
     * 相对湿度数据处理
     *
     * @param list
     * @param te
     */
    private void wetHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+");
        String viewCode = arr[0];
        if (viewCode.endsWith("0")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                te.setDay(i+"");
               String[] arr1={arr[0],arr[1],arr[2],arr[3]};
               String[] arr2={arr[4]};
                timerTaskHandle(arr1,"U", te);
                extremumHandle(arr2,"U",te);
            }
        } else if (viewCode.endsWith("2")||viewCode.endsWith("9")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                te.setDay(i+"");
                timerTaskHandle(arr,"U", te);
            }
        } else if (viewCode.endsWith("7")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                te.setDay(i+"");
                String[] arr1={arr[0],arr[1],arr[2]};
                String[] arr2={arr[3]};
                timerTaskHandle(arr1,"U", te);
                extremumHandle(arr2,"U",te);
            }
        }else if (viewCode.endsWith("A")) {
            int day = 1;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                te.setDay(""+day);
                if(i%2==0){
                    day++;
                }
               if(i%2==0){
                   String[] arr1=new String[12];
                   String[] arr2=new String[1];
                   for(int j=0,k=arr.length; j<k ;j++){
                       if(j>12){
                           arr2[0]=arr[j];
                       }else {
                           arr1[j]=arr[j];
                       }
                   }
                   currentHourHandlle(arr,"U",9, te);
                   extremumHandle(arr2,"U",te);
               }else {
                   currentHourHandlle(arr,"U",21, te);
               }
                timerTaskHandle(arr,"U", te);
            }
        } else if (viewCode.endsWith("B")) {
            int day = 1;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+");
                te.setDay(""+day);
                if(i%2==0){
                    day++;
                }
                if (arr.length > 12) { // 带最值
                    String[] arr1=new String[12];
                    String[] arr2=new String[2];
                    for(int j=0,k=arr.length; j<k ;j++){
                        if(j>11){
                            arr2[j-12]=arr[j];
                        }else {
                            arr1[j]=arr[j];
                        }
                    }
                    // 09-20时记录
                    currentHourHandlle(arr1, "U",9, te);
                    extremumHandle(arr2, "U", te);
                } else {
                    // 21-08时记录
                    currentHourHandlle(arr, "U",21,te);
                }
            }
        }
    }

    /**
     * 定时处理-02，08，14，20或者08，14，20
     *
     * @param arr
     * @param type
     */
    private void timerTaskHandle(String[] arr, String type,TimeEntity te) {
        Map<String, ViewParamsHourEntity> mapHour=null;
        ParamCollections paramCollections=null;
        paramCollections=aDecoding.get(te.getDay());
        if(paramCollections==null){
            paramCollections=new ParamCollections();
            paramCollections.setTimeEntity(te);
        }
        mapHour=paramCollections.getMapHour();
        ViewParamsHourEntity vp=null;
        int size=arr.length-1;
        if(size==0){
            return;
        }
        for (int i = size-1; i < 0; i--) {
            if (arr[i].equals(",,,,")) {
                continue;
            }
            int value1=Integer.parseInt(arr[i]);
            float value = (float) (Float.parseFloat(arr[i]) * 0.1);
            String hour = "";
            if (i == size) {
                hour="20";
            } else if (i == size-1) {
                hour="14";
            } else if (i == size-2) {
                hour="08";
            } else {
                hour="02";
            }
            vp = mapHour.get(hour);
            if (vp == null) {
                vp = new ViewParamsHourEntity();
            }
            vp.setHour(hour);
            // 值
            if ("P0".equals(type)) {
                vp.setSeaGans(value);
            }else if("P".equals(type)){
                vp.setGans(value);
            }else if("T".equals(type)){
                vp.setTemp(value);
            }else if("I1".equals(type)){
                vp.setBallTemp(value);
            }else if("I2".equals(type)){
                vp.setPointTemp(value);
            }else if("E".equals(type)){
                vp.setWaterGans(value);
            }else if("U".equals(type)){
                vp.setWetness(value1);
            }else if("N1".equals(type)){//总云量
                if("10".equals(arr[i])||"10-".equals(arr[i])){
                    vp.setCloudT(11);
                }else{
                    vp.setCloudT(value1);
                }
            }else if("N2".equals(type)){//低云量
                if("10".equals(arr[i])||"10-".equals(arr[i])){
                    vp.setCloudL(11);
                }else{
                    vp.setCloudL(value1);
                }
            }else if("V1".equals(type)){
                if(value>=100){
                    vp.setVisibility1(Float.parseFloat("999"));
                }else{
                    vp.setVisibility1(value);
                }
            }else if("V2".equals(type)){
                vp.setVisibility2(value1);
            }else if("R".equals(type)){
                vp.setRain(value);
            }else if("F1".equals(type)){
                String v1=arr[i].substring(0,3);
                Float v2=(float)(Float.parseFloat(arr[i].substring(3))*0.1);
                vp.setWind2(v1);
                vp.setWind2Speed(v2);
            }else if("D1".equals(type)){
                vp.setDtem(value);
            }else if("D2".equals(type)){
                vp.setDtem5(value);
            }else if("D3".equals(type)){
                vp.setDtem10(value);
            }else if("D4".equals(type)){
                vp.setDtem15(value);
            }else if("D5".equals(type)){
                vp.setDtem20(value);
            }else if("D6".equals(type)){
                vp.setDtem40(value);
            }
            mapHour.put(hour, vp);
        }
        aDecoding.put(te.getDay(),paramCollections);
        if(Integer.parseInt(te.getDay())>31){
            System.out.println("数据类型："+type);
        }
    }

    /**
     * 极值处理
     *
     * @param extremum
     * @param type
     */
    private void extremumHandle(String[] extremum, String type, TimeEntity te) {
        ViewParamDayEntity vpde = null;
        int length = extremum.length;
        ParamCollections paramCollections=null;
        paramCollections=aDecoding.get(te.getDay());
        if(paramCollections==null){
            paramCollections=new ParamCollections();
            paramCollections.setTimeEntity(te);
        }
        vpde = paramCollections.getDayEntity();
        vpde.setYear(te.getYear());
        vpde.setMonth(te.getMonth());
        vpde.setDay(te.getDay());
        if(extremum[length-1].endsWith("=")){
            extremum[length-1]=extremum[length-1].substring(0,extremum[length-1].indexOf("="));
        }
        if (length > 2) {
            if ("P".equals(type)) {
                if(!"////".equals(extremum[0])) {
                    vpde.setGanshight((float) (Float.parseFloat(extremum[0]) * 0.1));
                    vpde.setGanshighthour(extremum[1]);
                }
                if(!"////".equals(extremum[2])) {
                    vpde.setGanslow((float) (Float.parseFloat(extremum[2]) * 0.1));
                    vpde.setGanslowHour(extremum[3].substring(0, 4));
                }
            }else if("T".equals(type)){
                vpde.setTemphight((float) (Float.parseFloat(extremum[0]) * 0.1));
                vpde.setTemphighthour(extremum[1]);
                vpde.setTemplow((float) (Float.parseFloat(extremum[2]) * 0.1));
                vpde.setTemplowhour(extremum[3]);
            }else if("W".equals(type)){
                vpde.setWetherSymbol(extremum[0]);
                vpde.setWetherStartTime(extremum[1]);
                vpde.setWetherEndTime(extremum[2]);
            }else if("G1".equals(type)){
                vpde.setGlazeNSDia(Integer.parseInt(extremum[0]));
                vpde.setGlazeNSHight(Integer.parseInt(extremum[1]));
                vpde.setGlazeNSWeight(Integer.parseInt(extremum[2]));
                vpde.setGlazeWEDia(Integer.parseInt(extremum[3]));
                vpde.setGlazeWEHight(Integer.parseInt(extremum[4]));
                vpde.setGlazeWEWeight(Integer.parseInt(extremum[5]));
                if(extremum.length>6){
                    vpde.setIceTemp((float) (Float.parseFloat(extremum[6]) * 0.1));
                    String windSpeed=extremum[7].substring(3);
                    String wind=extremum[7].substring(0,3);
                    vpde.setIceWind(wind);
                    vpde.setIceWindSpeed((float) (Float.parseFloat(windSpeed) * 0.1));
                }
            }else if("G2".equals(type)){
                vpde.setRimeNSDia(Integer.parseInt(extremum[0]));
                vpde.setRimeNSHight(Integer.parseInt(extremum[1]));
                vpde.setRimeNSWeight(Integer.parseInt(extremum[2]));
                vpde.setRimeWEDia(Integer.parseInt(extremum[3]));
                vpde.setRimeWEHight(Integer.parseInt(extremum[4]));
                vpde.setRimeWEWeight(Integer.parseInt(extremum[5]));
                if(extremum.length>6){
                    vpde.setIceTemp((float) (Float.parseFloat(extremum[6]) * 0.1));
                    String windSpeed=extremum[7].substring(3);
                    String wind=extremum[7].substring(0,3);
                    vpde.setIceWind(wind);
                    vpde.setIceWindSpeed((float) (Float.parseFloat(windSpeed) * 0.1));
                }
            }else if("F".equals(type)){

                String str0=null;
                Float valF1 =null;
                Float valF0=null;
                if(!"//////".equals(extremum[0])) {
                    str0=extremum[0].substring(3);//风向
                    valF0 = (float) (Float.parseFloat(extremum[0].substring(0, 3)) * 0.1);//风速
                }
                String str1=null;//风向
                if(!"//////".equals(extremum[2])) {
                    valF1=(float) (Float.parseFloat(extremum[2].substring(0, 3)) * 0.1);//风速
                    str1=extremum[2].substring(3);//风向
                }
                vpde.setBigwind(str0);
                vpde.setMaxwind(str1);
                vpde.setBigwindTime(extremum[1]);
                vpde.setMaxwindTime(extremum[3]);
                vpde.setBigwindSpeed(valF0);
                vpde.setMaxwindSpeed(valF1);
            }else if("D1".equals(type)){
                vpde.setDtemH((float) (Float.parseFloat(extremum[0]) * 0.1));
                vpde.setDtemHTime(extremum[1]);
                vpde.setDtemL((float) (Float.parseFloat(extremum[2]) * 0.1));
                vpde.setDtemLTime(extremum[3]);
            }else if("K".equals(type)){
               if(length>3){
                   vpde.setHtem50((float) (Float.parseFloat(extremum[0]) * 0.1));
                   vpde.setHtem100((float) (Float.parseFloat(extremum[1]) * 0.1));
                   vpde.setHtem200((float) (Float.parseFloat(extremum[2]) * 0.1));
                   vpde.setHtem300((float) (Float.parseFloat(extremum[2]) * 0.1));
               }else{
                   vpde.setHtem80((float) (Float.parseFloat(extremum[0]) * 0.1));
                   vpde.setHtem160((float) (Float.parseFloat(extremum[1]) * 0.1));
                   vpde.setHtem320((float) (Float.parseFloat(extremum[2]) * 0.1));
               }
            }else if("A".equals(type)){
                if(!"///".equals(extremum[0])) {
                    vpde.setPermafrost1Ceiling((float) Float.parseFloat(extremum[0]));
                }
                if(!"///".equals(extremum[1])) {
                    vpde.setPermafrost1Floor((float) Float.parseFloat(extremum[1]));
                }
                if(!"///".equals(extremum[2])) {
                    vpde.setPermafrost2Ceiling((float) Float.parseFloat(extremum[2]));
                }
                if(!"///".equals(extremum[3])) {
                    vpde.setPermafrost2Floor((float) Float.parseFloat(extremum[3]));
                }
            }else if("S".equals(type)){
                vpde.setSunRise(extremum[0]);
                vpde.setSunFall(extremum[1]);
                if(!"///".equals(extremum[2])) {
                    vpde.setSunlightTotal((float) (Float.parseFloat(extremum[2]) * 0.1));
                }
            }else if("B".equals(type)){
                vpde.setGrassMaxHour((float) (Float.parseFloat(extremum[0]) * 0.1));
                vpde.setGrassMaxTime(extremum[1]);
                vpde.setGrassMinHour((float) (Float.parseFloat(extremum[2]) * 0.1));
                vpde.setGrassMinTime(extremum[3]);
            }else if("R".equals(type)){
                if(!",,,,".equals(extremum[0])&&(!"//////////".equals(extremum[0]))&&(!"////".equals(extremum[0]))){
                    vpde.setRain20((float) (Float.parseFloat(extremum[0]) * 0.1));
                }
                if(!",,,,".equals(extremum[1])&&(!"//////////".equals(extremum[1]))&&(!"////".equals(extremum[0]))) {
                    vpde.setRain08((float) (Float.parseFloat(extremum[1]) * 0.1));
                }
                if(!",,,,".equals(extremum[2])&&(!"//////////".equals(extremum[2]))&&(!"////".equals(extremum[0]))) {
                    vpde.setRain202((float) (Float.parseFloat(extremum[2]) * 0.1));
                }
            }
        } else if(length==2){
            if ("P".equals(type)) {
                if(!"////".equals(extremum[0])) {
                    vpde.setGanshight((float) (Float.parseFloat(extremum[0]) * 0.1));
                }
                if(!"////".equals(extremum[1])) {
                    vpde.setGanslow((float) (Float.parseFloat(extremum[1]) * 0.1));
                }
            }else if("T".equals(type)){
                vpde.setTemphight((float) (Float.parseFloat(extremum[0]) * 0.1));
                vpde.setTemplow((float) (Float.parseFloat(extremum[1]) * 0.1));
            }else if("R".equals(type)&&(!"////".equals(extremum[0]))){
                vpde.setRain1((float) (Float.parseFloat(extremum[0]) * 0.1));
                vpde.setRain10((float) (Float.parseFloat(extremum[1]) * 0.1));
            }else if("Z".equals(type)){
                Float v1=null;
                Float v2=null;
                if(!",,,".equals(extremum[0])){
                    v1=(float)(Float.parseFloat(extremum[0])*0.1);;
                    vpde.setSnowHight(v1);
                }
                if(!"///".equals(extremum[1])) {
                  v2 = (float) (Float.parseFloat(extremum[1]) * 0.1);
                }

                if(v1!=null&&v1<5){
                    vpde.setSnowHight((float) 0);
                }
                if(v2!=null){
                    vpde.setSnowPressure(v2);
                }
            }else if("D1".equals(type)){
                vpde.setDtemH((float) (Float.parseFloat(extremum[0]) * 0.1));
                vpde.setDtemL((float) (Float.parseFloat(extremum[1]) * 0.1));
            }else if("A".equals(type)){
                if(!"///".equals(extremum[0])) {
                    vpde.setPermafrost1Ceiling((float) Float.parseFloat(extremum[0]));
                }
                if(!"///".equals(extremum[1])) {
                    vpde.setPermafrost1Floor((float) Float.parseFloat(extremum[1]));
                }
            }else if("U".equals(type)){
                vpde.setWetness(Integer.parseInt(extremum[0]));
                if(extremum[1].endsWith(".")){
                    extremum[1]=extremum[1].substring(0,extremum[1].indexOf("."));
                }
                vpde.setWetlowhour(extremum[1]);
            }
        } else {
            if ("U".equals(type)) {
                if(!"".equals(extremum[0])) {
                    vpde.setWetness(Integer.parseInt(extremum[0]));
                }
            }else if("L1".equals(type)){
                if(!"".equals(extremum[0])&&(!"///".equals(extremum[0]))&&(!",,,".equals(extremum[0]))) {
                    vpde.setEvaporationSmall((float) (Float.parseFloat(extremum[0]) * 0.1));
                }
            }else if("L2".equals(type)){
                if(!"".equals(extremum[0])&&(!"///".equals(extremum[0]))&&(!",,,".equals(extremum[0]))) {
                    vpde.setEvaporationBig((float) (Float.parseFloat(extremum[0]) * 0.1));
                }
            }else if("S".equals(type)){
                if(!"".equals(extremum[0])) {
                    vpde.setSunlightTotal((float) (Float.parseFloat(extremum[0]) * 0.1));
                }
            }
        }
        aDecoding.put(te.getDay(),paramCollections);

    }

    /**
     * 从当前时开始的定时12小时
     * @param arr
     * @param type
     * @param currenthour
     */

    private void currentHourHandlle(String[] arr, String type,int currenthour,TimeEntity te) {
        Map<String, ViewParamsHourEntity> mapHour=null;
        ParamCollections paramCollections=null;
        paramCollections=aDecoding.get(te.getDay());
        if(paramCollections==null){
            paramCollections=new ParamCollections();
            paramCollections.setTimeEntity(te);
        }
        mapHour=paramCollections.getMapHour();
        ViewParamsHourEntity vpe=null;
            int size = arr.length;
            for (int k = 0; k < size; k++) {
                // 时
                String hours = currenthour + "";
                if (currenthour >= 24) {
                    hours = currenthour % 24 + "";
                } else {
                    hours = currenthour + "";
                }
                if (hours.length() < 2) {
                    hours = "0" + hours;
                }
                vpe = mapHour.get(hours);
                if(arr[k]==null){
                    continue;
                }
                if(arr[k].endsWith(".")){
                    arr[k]=arr[k].substring(0,arr[k].indexOf("."));
                }else if(arr[k].endsWith("=")) {
                    arr[k] = arr[k].substring(0, arr[k].indexOf("="));
                }
                if("".equals(arr[k])||"NN".equals(arr[k])||"////".equals(arr[k])||"//".equals(arr[k])){
                    continue;
                }
                // 值
                Integer value1=null;
                float value=0;
                String arr1=null;
                if(!"C".equals(type)&&!"F1".equals(type)&&(!"///".equals(arr[k]))&&(!"//".equals(arr[k]))&&(!",,,,".equals(arr[k]))&&(!"%%".equals(arr[k]))&&(!"---".equals(arr[k]))){

                    if(arr[k].contains(",")){
                        arr1=arr[k].substring(1);
                    }else{
                        arr1=arr[k];
                    }
                        value1 = Integer.parseInt(arr1);
                        value = (float) (Float.valueOf(arr1) * 0.1);

                }

                if (vpe == null) {
                    vpe = new ViewParamsHourEntity();
                }
                    vpe.setHour(hours);
                // 值
                if ("P".equals(type)) {
                    vpe.setGans(value);
                } else if ("P0".equals(type)) {
                    vpe.setSeaGans(value);
                } else if ("T".equals(type)) {
                    vpe.setTemp(value);
                } else if ("I1".equals(type)) {
                    if(arr[k].startsWith(",")){
                        vpe.setBallIceTemp(value);
                    }else{
                        vpe.setBallTemp(value);
                    }

                } else if ("I2".equals(type)) {
                    if(arr[k].startsWith(",")){
                        vpe.setBallIceTemp(value);
                    }else {
                        vpe.setPointTemp(value);
                    }
                } else if ("E".equals(type)) {
                    vpe.setWaterGans(value);
                } else if ("U".equals(type)) {
                    vpe.setWetness(value1);
                } else if("N1".equals(type)){//总云量
                    if("10".equals(arr[k])||"10-".equals(arr[k])){
                        vpe.setCloudT(11);
                    }else{
                        vpe.setCloudT(value1);
                    }
                }else if("N2".equals(type)){//低云量
                    if("10".equals(arr[k])||"10-".equals(arr[k])){
                        vpe.setCloudL(11);
                    }else{
                        vpe.setCloudL(value1);
                    }
                }else if("C".equals(type)){
                    vpe.setCloudShade(arr[k]);
                }else if("V1".equals(type)){
                    if(value>=100){
                        vpe.setVisibility1(Float.parseFloat("999"));
                    }else{
                        vpe.setVisibility1(value);
                    }
                }else if("R".equals(type)){
                    vpe.setRain(value);
                }else if("L2".equals(type)){
                    if(!"///".equals(arr[k])&&(!"---".equals(arr[k]))) {
                        vpe.setEvaporationBig((float) (Float.parseFloat(arr[k]) * 0.1));
                    }
                }else if("F2".equals(type)){
                    String v1=arr[k].substring(0,3);
                    Float v2=(float)(Float.parseFloat(arr[k].substring(3))*0.1);
                    vpe.setWind10(v1);
                    vpe.setWind10Speed(v2);
                }else if("F1".equals(type)){
                    String v1=arr[k].substring(0,3);
                    Float v2=(float)(Float.parseFloat(arr[k].substring(3))*0.1);
                    vpe.setWind2(v1);
                    vpe.setWind2Speed(v2);
                }else if("D1".equals(type)){
                    vpe.setDtem((float) (Float.parseFloat(arr[k]) * 0.1));
                }else if("D2".equals(type)){
                    vpe.setDtem5((float) (Float.parseFloat(arr[k]) * 0.1));
                }else if("D3".equals(type)){
                    vpe.setDtem10((float) (Float.parseFloat(arr[k]) * 0.1));
                }else if("D4".equals(type)){
                    vpe.setDtem15((float) (Float.parseFloat(arr[k]) * 0.1));
                }else if("D5".equals(type)){
                    vpe.setDtem20((float) (Float.parseFloat(arr[k]) * 0.1));
                }else if("D6".equals(type)){
                    vpe.setDtem40((float) (Float.parseFloat(arr[k]) * 0.1));
                }else if("k1".equals(type)){
                    vpe.setHtem80((float) (Float.parseFloat(arr[k]) * 0.1));
                }else if("k2".equals(type)){
                    vpe.setHtem160((float) (Float.parseFloat(arr[k]) * 0.1));
                }else if("k3".equals(type)){
                    vpe.setHtem320((float) (Float.parseFloat(arr[k]) * 0.1));
                }else if ("S".equals(type)) {
                    vpe.setSunLightHour((float) (Float.parseFloat(arr[k]) * 0.1));
                }else if ("B".equals(type)) {
                    vpe.setGrassTemp((float) (Float.parseFloat(arr[k]) * 0.1));
                }
                mapHour.put(hours, vpe);
                currenthour++;
            }
        aDecoding.put(te.getDay(),paramCollections);
    }
    /**
     * 封面处理
     * @param list
     * @param te
     */
    private void FMHandle(List<String> list, TimeEntity te){
        String [] arr= CommonUtil.listToArray(list);
        ohi.setCode(arr[0]);
        ohi.setProvinceName(arr[1]);
        ohi.setStationName(arr[2]);
        ohi.setAddress(arr[3]);
        ohi.setGeographicalEnvironment(arr[4]);
        ohi.setStationLeader(arr[7]);
        ohi.setEntryName(arr[8]);
        ohi.setCheckName(arr[9]);
        ohi.setInquireName(arr[10]);
        ohi.setAuditName(arr[11]);
        ohi.setSendName(arr[12]);
        ohi.setSendTime(arr[13]);
        ohi.setYear(te.getYear());
        ohi.setMonth(te.getMonth());
    }
    /**
     * 备注
     * @param list
     * @param te
     */
    private void noteHandle(List<String> list, TimeEntity te){
        String info="";
        for(int i=0,size=list.size();i<size;i++){
            info+=list.get(i);
        }
        ohi.setNote(info);
    }

    /**
     * 气候概况
     * @param list
     * @param te
     */
    private void climateProbablyHandle(List<String> list, TimeEntity te){
        String info="";
        for(int i=0,size=list.size();i<size;i++){
            info+=list.get(i);
        }
        ohi.setClimateProbably(info);
    }

    /**
     * 纪要信息处理
     * @param list
     * @param te
     */
    private void summaryHandle(List<String> list, TimeEntity te){
        String info="";
        for(int i=0,size=list.size();i<size;i++){
            info+=list.get(i);
        }
        ohi.setSummary(info);
    }
    public static void main(String[] args) {
        //新产品图片路径
        List<String> list=new ArrayList<>();
        getFilePath("E:\\filedecode",list);
        int count=0;
        for(String path:list) {
//            File file = new File("E:\\filedecode\\53463\\A053463.A61");
            File file = new File(path);
            if(file.getName().startsWith("A0")) {

            }else if(file.getName().startsWith("A")){
                ADecoding A = new ADecoding();
            A.assemblyData(file, null, null, null, null, "A53463.A61", null);
//                A.aDataDecoding(file, "ASCII");
                System.out.println(path);
                count++;
            }

        }

    }
    public static void getFilePath(String path,List<String> list){
        File file=null;
        try {
            file = new File(path);
            File[] fs = file.listFiles();
            if (fs != null) {
                for (int i = 0; i < fs.length; i++) {
                    if (fs[i].isFile()) {
                        list.add(fs[i].getAbsolutePath());
                    } else if (fs[i].isDirectory()) {
                        getFilePath(fs[i].getAbsolutePath(), list);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
