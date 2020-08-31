package com.thinkgem.jeesite.mybatis.modules.filedecode.decode;


import com.thinkgem.jeesite.mybatis.modules.filedecode.Interface.IFileDecoding;
import com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean.*;
import com.thinkgem.jeesite.mybatis.modules.filedecode.common.decode.Decoding;
import com.thinkgem.jeesite.mybatis.modules.filedecode.util.CommonUtil;
import com.thinkgem.jeesite.mybatis.modules.filedecode.util.Encoding;
import com.thinkgem.jeesite.mybatis.modules.filedecode.util.FileEncodeUtil;
import com.thinkgem.jeesite.mybatis.modules.filedecode.util.FileHadleUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A0文件数据处理
 */
public class A0Decoding implements IFileDecoding {
    /**
     * 台站参数
     */
    private FirstData sf = null;
    /**
     * key-day，value-Collections
     */
    private Map<String, ParamCollections> A0Decoding=new ConcurrentHashMap<>();

    /**
     * A0文件记录信息
     */
    private String dataTypeAll="PTIEUNHCVRWLZGFDKAS";

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
    public Map<String,Object> assemblyData(File file, String dayCtsCode, String hourCts, String monthCts, String meadowCts, String filename, List<RadiDigChnMulTab> Rlist){

        Map<String,Object> resultMap=new HashMap<>();
        ParseResult<String> decodingInfo = new ParseResult<String>(false) ;
        if (file == null ||(file.isFile())||(!filename.startsWith("A0"))) {
            if (filename.length() <= 0) {
                decodingInfo.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                resultMap.put("decodingInfo",decodingInfo);
                return resultMap;
            }
        }
        if(!file.exists()){
            decodingInfo.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
            resultMap.put("decodingInfo",decodingInfo);
            return resultMap;
        }
        FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
        String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
        fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
        System.out.println(filename+"文件开始解码！");
        //解码
        a0DataDecoding(file,fileCode);
        System.out.println(filename+"文件解码完成！");
        /**
         * 组装数据
         */
        System.out.println(filename+"文件开始组装数据！");
        MonthValueTab monthValueTab=new MonthValueTab();
        List<DayValueTab> dayList=new ArrayList<>();
        List<HourValueTab> hourList=new ArrayList<>();
        List<SunLightValueTab> sunLightList=new ArrayList<>();
        List<MeadowValueTab> meadowList=new ArrayList<>();

        SunLightValueTab sunLightValueTab=null;
        HourValueTab hourValueTab=null;
        Iterator<Map.Entry<String, ParamCollections>> entries = A0Decoding.entrySet().iterator();
        Decoding decoding=new Decoding();
        while(entries.hasNext()) {//日值处理
            sunLightValueTab = new SunLightValueTab();
            Map.Entry<String, ParamCollections> entry = entries.next();
            ParamCollections value = entry.getValue();
            ViewParamDayEntity dayEntity = value.getDayEntity();
            TimeEntity timeEntity = value.getTimeEntity();
            timeEntity.setDay(entry.getKey());
            if(Integer.parseInt(entry.getKey())>31){
                System.out.println(122131);
            }
            /**
             * 日照
             */
            sunLightValueTab.setD_RECORD_ID(UUID.randomUUID().toString());
            sunLightValueTab.setD_DATA_ID(hourCts);
            sunLightValueTab.setV01300(sf.getAreacode());
            sunLightValueTab.setV05001(Float.parseFloat(sf.getWd()));
            sunLightValueTab.setV06001(Float.parseFloat(sf.getJd()));
            sunLightValueTab.setV07001(Float.parseFloat(sf.getViewhight()));
            sunLightValueTab.setV04001(Integer.parseInt(sf.getYear()));
            sunLightValueTab.setV04002(Integer.parseInt(sf.getMonth()));
            sunLightValueTab.setV04003(Integer.parseInt(timeEntity.getDay()));
            sunLightValueTab.setV14032(dayEntity.getSunshine());
            /**
             * 小时值组装
             */
            Map<String, ViewParamsHourEntity> hour = value.getMapHour();
            Iterator<Map.Entry<String, ViewParamsHourEntity>> hoursEntries = hour.entrySet().iterator();
            List<HourValueTab> hour1=new ArrayList<>();
            while (hoursEntries.hasNext()) {//小时值处理
                Map.Entry<String, ViewParamsHourEntity> entryHour = hoursEntries.next();
                ViewParamsHourEntity hourValue = entryHour.getValue();
                String hours = entryHour.getKey();
                hourValueTab = decoding.hourValueHandle(hourCts, timeEntity, hourValue, sf);
                sunLightValueTab = setSunLightToSunTable(hours, hourValue.getSunLightHour(), sunLightValueTab);
                hourList.add(hourValueTab);
                hour1.add(hourValueTab);
            }
            /**
             * 组装日值数据
             */
            DayValueTab dayValueTab =new DayValueTab();

            int size=hour1.size();
            for (HourValueTab hourValueTab1:hour1){
               decoding.dayVlaueHandle(dayValueTab,dayCtsCode, timeEntity, dayEntity, sf,hourValueTab1,size);
            }
            if(dayValueTab.getD_RECORD_ID()!=null){
                dayList.add(dayValueTab);
            }

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
            if(dayValue.getV04003()==null){
                continue;
            }
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
        System.out.println(filename+"文件数据组装完成！");
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
     * A0文件读取
     *
     * @param path
     */
    private void a0DataDecoding(File path,String fileCode) {

        List<String> list = FileHadleUtil.readFromTextFile(path,fileCode);
        // 数据存储
        List<String> dataList = new ArrayList<>();
        // 时间
        TimeEntity te = new TimeEntity();
        // 观测参数标识
        String viewCode = null;
        // 数据处理开始标识
        boolean isstart = false;
        // 数据标识符
        String type = "";
        for (int i = 0, size = list.size(); i < size; i++) {
            String str = list.get(i);
            String[] arr = str.split("\\s+",-1);
            if (isstart) {
                if (str.endsWith("=") ) {
                    dataList.add(str);
                    if (i < size - 1) { // 判断下一条数据
                        str = list.get(i + 1);
                        arr = str.split("\\s+", -1);
                        String check =null;
                        if(!"".equals(arr[0])) {
                            check = arr[0].substring(0, 1);
                        }
                        if (arr.length < 2 && check!=null&&dataTypeAll.indexOf(check) > -1) { // 当前要素完成
                            isstart = false;
                            viewCode = null;
                            A0handle(dataList, te, type);
                            // 清理数据
                            dataList.clear();
                            type = "";
                        }
                    }

                }else {
                    dataList.add(str);
                }
                continue;
            }
            if (i == 0) { // 台站参数信息
                sf = new FirstData();
                sf.setAreacode(arr[0]);
                sf.setWd(arr[1].substring(0, 4));
                sf.setJd(arr[1].substring(4));
                sf.setViewhight(arr[2]);
                sf.setQuickhight(arr[3]);
                sf.setYear(arr[4]);
                sf.setMonth(arr[5]);
                te.setYear(arr[4]);
                te.setMonth(arr[5]);
            } else { // 观测参数
                if (null == viewCode) {
                    if(arr[0].endsWith("=")){
                        continue;
                    }
                    viewCode = arr[0];
                    dataList.add(str);
                    isstart = true;
                    if (viewCode.startsWith("P")) { // 气压
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
                    }else if (viewCode.startsWith("R")) { // 降水
                        type = "R";
                    }else if (viewCode.startsWith("W")) { // 天气现象
                        type = "W";
                    }else if (viewCode.startsWith("L")) { // 蒸发量
                        type = "L";
                    }else if (viewCode.startsWith("Z")) { // 积雪
                        type = "Z";
                    }else if (viewCode.startsWith("G")) { // 电线结冰
                        type = "G";
                    }else if (viewCode.startsWith("F")) { // 风
                        type = "F";
                    }else if (viewCode.startsWith("D")) { // 浅层地温
                        type = "D";
                    }else if (viewCode.startsWith("K")) { // 深层地温
                        type = "K";
                    }else if (viewCode.startsWith("A")) { // 冻土深度
                        type = "A";
                    }else if (viewCode.startsWith("S")) { // 冻土深度
                        type = "S";
                    }
                }
            }
        }
    }
    /**
     * A0文件处理方法
     * @param list
     * @param te
     * @param type
     */
    private void A0handle(List<String> list, TimeEntity te, String type) {
        switch (type){
            case "P":// 气压
                gansHandle(list, te);
                break;
            case "T": // 气温
                tempHandle(list, te);
                break;
            case "I": // 湿球温度
                iballHandle(list, te);
                break;
            case "E": // 水气压
                waterHandle(list, te);
                break;
            case "U"://相对湿度
                wetHandle(list, te);
                break;
            case "N":// 云量
                cloudHandle(list, te);
                break;
            case "H":// 云高
                cloudHightHandle(list, te);
                break;
            case "C": // 云状
                cloudShapHandle(list, te);
                break;
            case "V":// 能见度
                visibilityHandle(list, te);
                break;
            case "R":// 降水量
                rainHandle(list, te);
                break;
            case "W": // 天气现象
                wetherHandle(list, te);
                break;
            case "L":// 蒸发量
                evaporatHandle(list, te);
                break;
            case "Z": // 积雪
                snowHandle(list, te);
                break;
            case "G":// 电线积冰
                iceHandle(list, te);
                break;
            case "F":// 风
                windHandle(list, te);
                break;
            case "D"://  浅层地温
                zoneHandle(list, te);
                break;
            case "K"://  深层地温
                deepHandle(list, te);
                break;
            case "A"://  冻土深度
                frozenHandle(list, te);
                break;
            case "S":// 日照时数
                sunHandle(list, te);
                break;
            default:
                break;
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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        if (viewCode.endsWith("0")||viewCode.endsWith("6")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                te.setDay(i+"");
                arr = str.split("\\s+");
                extremumHandle(arr,"A",te);
            }
        }else if(viewCode.endsWith("1")||viewCode.endsWith("7")||viewCode.endsWith("8")){
            String[] arr1=null;
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                te.setDay(i + "");
                arr = str.split("\\s+");
                if (arr.length > 3) {
                    arr1 = new String[4];
                    if (arr[3].endsWith("=")) {
                        arr[3] = arr[3].substring(0, arr[3].indexOf("="));
                    }
                    arr1[2] = arr[3];
                    if(arr.length==5) {
                        arr1[3] = arr[4];
                    }
                } else{
                    arr1 = new String[2];
                    if (arr[1].endsWith("=")) {
                        arr[1] = arr[1].substring(0, arr[1].indexOf("="));
                    }
                }
                arr1[0] = arr[0];
                arr1[1]=arr[1];
                extremumHandle(arr1, "A", te);
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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        if (viewCode.endsWith("0")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                te.setDay(""+i);
                extremumHandle(arr, "S", te);
            }
        }
    }

    /**
     * 风数据处理
     * @param list
     * @param te
     */
    private void windHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        int count=te.getDayCountOnMonth();
        if (viewCode.endsWith("0")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                if(i>count){
                    //自记
                    te.setDay(""+(i-count));
                    extremumHandle(arr, "F", te);
                }else{
                    if(i%count==0){
                        te.setDay(""+count);
                    }else{
                        te.setDay(""+i%count);
                    }
                    timerTaskHandle(arr, "F",te);
                }
            }
        }else  if (viewCode.endsWith("4")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                if(i>count){
                    if(i%count==0){
                        te.setDay(""+count);
                    }else{
                        te.setDay(""+i%count);
                    }
                    extremumHandle(arr, "F4", te);
                }else{
                    te.setDay(""+i);
                    timerTaskHandle(arr, "F",te);
                }

            }
        }else  if (viewCode.endsWith("5")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                if(i>count){
                    if(i%count==0){
                        te.setDay(""+count);
                    }else{
                        te.setDay(""+i%count);
                    }
                    extremumHandle(arr, "F5", te);
                }else{
                    te.setDay(""+i);
                    timerTaskHandle(arr, "F",te);
                }

            }
        }else  if (viewCode.endsWith("9")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                if(i>count){
                    break;
                }else{
                    te.setDay(""+i);
                    timerTaskHandle(arr, "F",te);
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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        int currentType=1;
        if (viewCode.endsWith("0")) {
            int day=1;
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                te.setDay(day+"");
                if(currentType==2){
                    extremumHandle(arr, "LB", te);
                }
                if(currentType==1){
                    extremumHandle(arr, "LL", te);
                }
                if(str.endsWith("=")){
                    currentType=2;
                    day=0;
                }
                day++;
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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        int currentMonthDayCount=te.getDayCountOnMonth();
        if (viewCode.endsWith("0")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                te.setDay(""+i);
                extremumHandle(arr, "Z", te);
            }
        }else if(viewCode.endsWith("1")||viewCode.endsWith("9")){
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                te.setDay(arr[0]);
                if(currentMonthDayCount<Integer.parseInt(arr[0])){
                    continue;
                }
                String[] arr1={arr[1],arr[2]};
                extremumHandle(arr1, "Z", te);

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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        String str1=null;
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
                String type="";
                if(currentType==2){//雾凇
                    type="G2";
                }else if(currentType==1){//雨凇
                    type="G1";
                }
                String[] arr1=null;
                boolean flag=false;
                if(viewCode.endsWith("0")||viewCode.endsWith("2")) {
                    te.setDay(day + "");
                }else if(viewCode.endsWith("1")){
                    te.setDay(arr[0]+"");
                    flag=false;
                   arr1=new String[6];
                    for (int j=1,k=arr.length;j<k;j++){
                        arr1[j-1]=arr[j];
                        flag=true;
                    }
                }else if(viewCode.endsWith("3")){
                    te.setDay(arr[0]+"");
                    arr1=new String[9];
                    flag=false;
                    for (int j=1,k=arr.length;j<k;j++){
                        arr1[j-1]=arr[j];
                        flag=true;
                    }
                }
                if(arr1!=null&&flag) {
                    extremumHandle(arr1, type, te);
                }else if(flag){
                    extremumHandle(arr, type, te);
                }
                if(str.endsWith("=")){
                    currentType=2;
                    day=1;
                }
                day++;
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
        for (int i = 1, size = list.size(); i < size; i++) {
            str = list.get(i);
            arr = str.split(",",-1);
            if (viewCode.endsWith("0")) {
                te.setDay(""+i);
                timerTaskHandle(arr, "C",te);
            } else if (viewCode.endsWith("9")) {
                timerTaskHandle(arr, "C",te);
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
     *
     * @param list
     * @param te
     */
    private void cloudHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        for (int i = 1, size = list.size(); i < size; i++) {
            str = list.get(i);
            arr = str.split("\\s+",-1);
            int count=te.getDayCountOnMonth();
            if (viewCode.endsWith("0")) {
                String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                if(i>count){
                    if(i%count==0){
                        te.setDay(count+"");
                    }else{
                        te.setDay(""+i%count);
                    }
                    timerTaskHandle(arr1, "NL",te);
                }else{
                    te.setDay(""+i);
                    timerTaskHandle(arr1, "NT",te);
                }
            } else if (viewCode.endsWith("9")) {
                if (i > count) {
                    if(i%count==0){
                        te.setDay(count+"");
                    }else{
                        te.setDay(""+i%count);
                    }
                    timerTaskHandle(arr, "NL",te);
                } else {
                    te.setDay(""+i);
                    timerTaskHandle(arr, "NT",te);
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
        int day = 1,type=1;
        for (int i = 1, length = list.size(); i < length; i++) {
            str = list.get(i);
            arr = str.split("\\s+");
            if(arr[arr.length-1].endsWith("=")){
                type++;
                day=1;
            }
            if("=".equals(arr[0])){
                type++;
                day=1;
                continue;
            }
            if(type==6){//40cm
                te.setDay(day+"");
                timerTaskHandle(arr,"D6", te);
            }else if(type==5){
                te.setDay(day+"");
                timerTaskHandle(arr,"D5", te);
            }else if(type==4){
                te.setDay(day+"");
                timerTaskHandle(arr,"D4", te);
            }else if(type==3){
                te.setDay(day+"");
                timerTaskHandle(arr,"D3", te);
            }else if(type==2){
                te.setDay(day+"");
                timerTaskHandle(arr,"D2", te);
            }else if(type==1) {
                te.setDay(day + "");
                String[] arr1 = null;
                String[] arr2 = null;
                if (viewCode.endsWith("0")) {
                    arr1 = new String[]{arr[0], arr[1], arr[2], arr[3]};
                    arr2 = new String[]{arr[4], arr[5]};
                } else if (viewCode.endsWith("9")) {
                    arr1 = new String[]{arr[0], arr[1], arr[2]};
                    arr2 = new String[]{arr[3], arr[4]};
                }
                if (arr1 != null && arr2 != null) {
                    extremumHandle(arr2, "D1", te);
                    timerTaskHandle(arr1, "D1", te);
                }
            }
        }
        day++;
    }
    /**
     * 深层数据处理
     *
     * @param list
     * @param te
     */
    private void deepHandle(List<String> list, TimeEntity te) {
        String str = list.get(0);
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        if (viewCode.endsWith("0")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                DtemKByHour(arr, "K", te,"14");
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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        if (viewCode.endsWith("0")) {
            int day=1;
            String[] arr1 = null;
            String[] arr2=null;
            String[] arr3=null;
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                if(str.endsWith(".")||str.endsWith("=")) {
                    if(arr1!=null) {
                        arr2 = str.split("\\s+");
                        arr3 = new String[arr1.length + arr2.length];
                        System.arraycopy(arr1, 0, arr3, 0, arr1.length);
                        System.arraycopy(arr2, 0, arr3, arr1.length, arr2.length);
                        arr1 = new String[arr3.length];
                        System.arraycopy(arr3, 0, arr1, 0, arr3.length);
                    }else{
                        arr1 = str.split("\\s+");
                    }
                    te.setDay(day + "");
                    extremumHandle(arr1, "W", te);
                    arr1=null;
                    day++;
                }else{
                   if(arr1==null){
                       arr1 = str.split("\\s+");
                   }else{
                        arr2= str.split("\\s+");
                       arr3=new String[arr1.length+arr2.length];
                       System.arraycopy(arr1, 0, arr3, 0, arr1.length);
                       System.arraycopy(arr2, 0, arr3, arr1.length, arr2.length);
                       arr1=new String[arr3.length];
                       System.arraycopy(arr3, 0, arr1, 0, arr3.length);
                   }

                }
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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        if (viewCode.endsWith("0")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                if (arr.length > 2) { //自记降水量

                } else { // 3组-定时降水量
                    timerTaskHandle(arr, "R",te);
                }
            }
        } else if (viewCode.endsWith("1")) {
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                if (arr.length > 2) { //自记降水量

                } else { // 3组-定时降水量
                    timerTaskHandle(arr, "R",te);
                }
            }
        } else if (viewCode.endsWith("5")) { // 08、14、20三组或者24组
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                if (arr.length > 3) { //自记降水量

                } else { // 3组-定时降水量
                    timerTaskHandle(arr, "R",te);
                }
            }
        }else if(viewCode.endsWith("3")){//无自记，有定时
            for (int i = 1, length = list.size(); i < length; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                te.setDay(arr[0]);
                String[] arr1={arr[1],arr[2],arr[3]};
                extremumHandle(arr1, "R",te);
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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        if (viewCode.endsWith("0")) {//KM
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "V1",te);
            }
        } else if (viewCode.endsWith("7")) {//级别
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                String[] arr1={arr[0],arr[1],arr[2]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "V2",te);
            }
        } else if (viewCode.endsWith("8")) {//级别
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "V2",te);
            }
        } else if (viewCode.endsWith("9")) {//km
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                String[] arr1={arr[0],arr[1],arr[2]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "V1",te);
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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        if (viewCode.endsWith("0")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                String[] arr2={arr[4],arr[5]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "P",te);
                extremumHandle(arr2, "P", te);
            }
        } else if (viewCode.endsWith("2")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "P",te);
            }
        } else if (viewCode.endsWith("9")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
//                String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                te.setDay(""+i);
                timerTaskHandle(arr, "P",te);
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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        if (viewCode.endsWith("0")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                String[] arr2={arr[4],arr[5]};
                if(i<10){
                    te.setDay("0" + i);
                }else {
                    te.setDay("" + i);
                }
                timerTaskHandle(arr1, "T",te);
                extremumHandle(arr2, "T", te);
            }
        } else if (viewCode.endsWith("9")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                try {
                    String[] arr1 = {arr[0], arr[1], arr[2]};
                    String[] arr2 = {arr[3], arr[4]};
                    te.setDay("" + i);
                    timerTaskHandle(arr1, "T", te);
                    extremumHandle(arr2, "T", te);
                }catch (Exception e){
                    e.printStackTrace();
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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        if (viewCode.endsWith("0")||viewCode.endsWith("9")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                te.setDay(i+"");
                timerTaskHandle(arr, "I",te);
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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        if (viewCode.endsWith("0")||viewCode.endsWith("9")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                te.setDay(i+"");
                timerTaskHandle(arr, "E",te);
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
        String[] arr = str.split("\\s+",-1);
        String viewCode = arr[0];
        if (viewCode.endsWith("0")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                String[] arr2={arr[4]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "U",te);
                extremumHandle(arr2, "P", te);
            }
        } else if (viewCode.endsWith("2")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                String[] arr1={arr[0],arr[1],arr[2],arr[3]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "U",te);
            }
        } else if (viewCode.endsWith("9")) {
            for (int i = 1, size = list.size(); i < size; i++) {
                str = list.get(i);
                arr = str.split("\\s+",-1);
                String[] arr1={arr[0],arr[1],arr[2]};
                te.setDay(""+i);
                timerTaskHandle(arr1, "U",te);
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
        paramCollections=A0Decoding.get(te.getDay());
        if(paramCollections==null){
            paramCollections=new ParamCollections();
            paramCollections.setTimeEntity(te);
        }
        mapHour=paramCollections.getMapHour();
        ViewParamsHourEntity paramsHourEntity=null;
        int size=arr.length-1;
        for (int i =size ; i >=0 ; i--) {
            if(arr[i].contains(",,,,")||"".equals(arr[i])){
                //无记录
                continue;
            }
            if(arr[i].contains("////")||"".equals(arr[i])||arr[i].contains("//")||arr[i].contains("///")){
                //无记录
                continue;
            }
            if(arr[i].contains("%%")||"".equals(arr[i])){
                //无记录
                continue;
            }
            if(arr[i].endsWith("=")){
                arr[i]=arr[i].substring(0,arr[i].indexOf("="));
            }
            if("".equals(arr[i])){
                continue;
            }
            float value=0;
            int valueInt=0;
            String valueStr="";
            if("C".equals(type)||"F".equals(type)||"I".equals(type)){
                valueStr=arr[i];
            }else {
                if(!"/".equals(arr[i])) {
                    if(arr[i].endsWith(".")){
                        arr[i]=arr[i].substring(0, arr[i].indexOf("."));
                    }
                    value = (float) (Float.parseFloat(arr[i]) * 0.1);
                    if (arr[i].startsWith(".")) {
                        valueInt = 0;
                    } else {
                        valueInt = Integer.parseInt(arr[i]);
                    }
                }
            }
            String hours="";
            if (i == size) {
                hours="20";
            } else if (i == size-1) {
                hours="14";
            } else if (i == size-2) {
                hours="08";
            } else {
                hours="02";
            }
            paramsHourEntity=mapHour.get(hours);
            if(paramsHourEntity==null){
                paramsHourEntity=new ViewParamsHourEntity();
                paramsHourEntity.setHour(hours);
            }
                // 值
                if ("P".equals(type)) {
                    paramsHourEntity.setGans(value);
                }else if("T".equals(type)){
                    paramsHourEntity.setTemp(value);
                }else if("I".equals(type)){
                    if(valueStr.startsWith(",")){
                        paramsHourEntity.setBallIceTemp(Float.parseFloat(valueStr.substring(1)));
                    }else {
                        paramsHourEntity.setBalltemp(Float.parseFloat(valueStr));
                    }
                }else if("E".equals(type)){
                    paramsHourEntity.setWatergans(value);
                }else if("U".equals(type)){
                    paramsHourEntity.setWetness(valueInt);
                }else if("NT".equals(type)){//总云量
                    paramsHourEntity.setCloudt(valueInt);
                }else if("NL".equals(type)){//低云量
                    paramsHourEntity.setCloudl(valueInt);
                }else if("C".equals(type)){
                    paramsHourEntity.setShapeCloud(valueStr);
                }else if("V1".equals(type)){
                    paramsHourEntity.setVisibility1(value);
                }else if("V2".equals(type)){
                    paramsHourEntity.setVisibility2(valueInt);
                }else if("F".equals(type)){
                    String str=valueStr.substring(0,3);//风向
                    Float valF=(float) (Float.parseFloat(valueStr.substring(3)) * 0.1);//风速
                    paramsHourEntity.setWindSpeed(valF);
                    paramsHourEntity.setWind(str);
                }else if("D".equals(type)){
                    paramsHourEntity.setDtem(value);
                }else if("D5".equals(type)){
                    paramsHourEntity.setDtem5(value);
                }else if("D10".equals(type)){
                    paramsHourEntity.setDtem10(value);
                }else if("D15".equals(type)){
                    paramsHourEntity.setDtem15(value);
                }else if("D20".equals(type)){
                    paramsHourEntity.setDtem20(value);
                }else if("D40".equals(type)){
                    paramsHourEntity.setDtem40(value);
                }
            mapHour.put(hours,paramsHourEntity);
        }
        if(Integer.parseInt(te.getDay())>31){
            System.out.println("type3="+type);
        }
        A0Decoding.put(te.getDay(),paramCollections);
    }

    /**
     * 极值处理或者实有日期
     *
     * @param extremum
     * @param type
     */
    private void extremumHandle(String[] extremum, String type, TimeEntity te) {
        ViewParamDayEntity vpde = null;
        int length = extremum.length;
        ParamCollections paramCollections=null;
        paramCollections=A0Decoding.get(te.getDay());
        if(paramCollections==null){
            paramCollections=new ParamCollections();
            paramCollections.setTimeEntity(te);
        }
        vpde=paramCollections.getDayEntity();
        vpde.setYear(te.getYear());
        vpde.setMonth(te.getMonth());
        vpde.setDay(te.getDay());

        if(extremum[length-1]!=null&&extremum[length-1].endsWith("=")){
            extremum[length-1]=extremum[length-1].substring(0,extremum[length-1].indexOf("="));
        }
        if(extremum.length==1&&"".equals(extremum[0])){
            return;
        }
        if (length > 2) {
            if ("P".equals(type)) {
                if(!"////".equals(extremum[0])) {
                    vpde.setGanshight((float) (Float.parseFloat(extremum[0]) * 0.1));
                    vpde.setGanshighthour(extremum[1]);
                }
                if(!"////".equals(extremum[1])) {
                    vpde.setGanslow((float) (Float.parseFloat(extremum[2]) * 0.1));
                    vpde.setGanshighthour(extremum[3]);
                }
            }else if("W".equals(type)){
                vpde.setWetherSymbol(extremum[0]);
                vpde.setWetherStartTime(extremum[1]);
                vpde.setWetherEndTime(extremum[2]);
            }else if("G1".equals(type)){
                if(!"///".equals(extremum[0])&&(!"/////".equals(extremum[2]))) {
                    vpde.setGlazeNSDia(Integer.parseInt(extremum[0]));
                }
                if(!"///".equals(extremum[1])&&(!"/////".equals(extremum[2]))) {
                    vpde.setGlazeNSHight(Integer.parseInt(extremum[1]));
                }
                if(!"///".equals(extremum[2])&&(!"/////".equals(extremum[2]))) {
                    vpde.setGlazeNSWeight(Integer.parseInt(extremum[2]));
                }
                if(!"///".equals(extremum[3])&&(!"/////".equals(extremum[2]))) {
                    vpde.setGlazeWEDia(Integer.parseInt(extremum[3]));
                }
                if(!"///".equals(extremum[4])&&(!"/////".equals(extremum[2]))) {
                    vpde.setGlazeWEHight(Integer.parseInt(extremum[4]));
                }
                if(!"///".equals(extremum[5])&&(!"/////".equals(extremum[2]))) {
                    vpde.setGlazeWEWeight(Integer.parseInt(extremum[5]));
                }
                if(extremum.length>6){
                    vpde.setIceTemp((float) (Float.parseFloat(extremum[6]) * 0.1));
                    String windSpeed=extremum[7].substring(3);
                    String wind=extremum[7].substring(0,3);
                    vpde.setIceWind(wind);
                    vpde.setIceWindSpeed((float) (Float.parseFloat(windSpeed) * 0.1));
                }
            }else if("G2".equals(type)){
                if(!"///".equals(extremum[0])&&(!"/////".equals(extremum[2]))) {
                    vpde.setRimeNSDia(Integer.parseInt(extremum[0]));
                }
                if(!"///".equals(extremum[1])&&(!"/////".equals(extremum[2]))) {
                    vpde.setRimeNSHight(Integer.parseInt(extremum[1]));
                }
                if(!"///".equals(extremum[2])&&(!"/////".equals(extremum[2]))) {
                    vpde.setRimeNSWeight(Integer.parseInt(extremum[2]));
                }
                if(!"///".equals(extremum[3])&&(!"/////".equals(extremum[2]))) {
                    vpde.setRimeWEDia(Integer.parseInt(extremum[3]));
                }
                if(!"///".equals(extremum[4])&&(!"/////".equals(extremum[2]))) {
                    vpde.setRimeWEHight(Integer.parseInt(extremum[4]));
                }
                if(!"///".equals(extremum[5])&&(!"/////".equals(extremum[2]))) {
                    vpde.setRimeWEWeight(Integer.parseInt(extremum[5]));
                }
                if(extremum.length>6){
                    vpde.setIceTemp((float) (Float.parseFloat(extremum[6]) * 0.1));
                    String windSpeed=extremum[7].substring(3);
                    String wind=extremum[7].substring(0,3);
                    vpde.setIceWind(wind);
                    vpde.setIceWindSpeed((float) (Float.parseFloat(windSpeed) * 0.1));
                }
            }else if("A".equals(type)){
                if(extremum[0]!=null&&(!",,,".endsWith(extremum[0]))&&(!"///".equals(extremum[0]))) {
                    vpde.setPermafrost1Ceiling(Float.parseFloat(extremum[0]));
                }
                if(extremum[1]!=null&&(!",,,".endsWith(extremum[1]))&&(!"///".equals(extremum[1]))) {
                    vpde.setPermafrost1Floor(Float.parseFloat(extremum[1]));
                }
                if(extremum[2]!=null&&(!",,,".endsWith(extremum[2]))&&(!"///".equals(extremum[2]))) {
                    vpde.setPermafrost2Ceiling(Float.parseFloat(extremum[2]));
                }
                if(extremum[3]!=null&&(!",,,".endsWith(extremum[3]))&&(!"///".equals(extremum[3]))) {
                    vpde.setPermafrost2Floor(Float.parseFloat(extremum[3]));
                }
            }
        } else if(length==2){
            if ("U".equals(type)) {
                vpde.setWetness(Integer.parseInt(extremum[0]));
            }else if("T".equals(type)){
                if(!"////".equals(extremum[1])) {
                    vpde.setTemplow(((float) (Float.parseFloat(extremum[1]) * 0.1)));
                }
                if(!"////".equals(extremum[0])) {
                    vpde.setTemphight(((float) (Float.parseFloat(extremum[0]) * 0.1)));
                }
            }else if("P".equals(type)){
                if(!"////".equals(extremum[1])) {
                    vpde.setGanslow((float) (Float.parseFloat(extremum[1]) * 0.1));
                }
                if(!"////".equals(extremum[0])) {
                    vpde.setGanshight((float) (Float.parseFloat(extremum[0]) * 0.1));
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
                if(!"//////".equals(extremum[1])) {
                    valF1=(float) (Float.parseFloat(extremum[1].substring(0, 3)) * 0.1);//风速
                    str1=extremum[1].substring(3);//风向
                }
                vpde.setBigwind(str0);
                vpde.setMaxwind(str1);
                vpde.setBigwindSpeed(valF0);
                vpde.setMaxwindSpeed(valF1);
            }else if("D".equals(type)){
                vpde.setDtemH((float) (Float.parseFloat(extremum[0]) * 0.1));
                vpde.setDtemL((float) (Float.parseFloat(extremum[1]) * 0.1));
            }else if("Z".equals(type)){
                Float v1=null;
                Float v2=null;
                if(!"///".equals(extremum[1])&&(!",,,".equals(extremum[0]))) {
                    v2 = (float) (Float.parseFloat(extremum[1]) * 0.1);
                }
                if(!",,,".equals(extremum[0])&&(!"///".equals(extremum[0]))){
                    v1=(float)(Float.parseFloat(extremum[0])*0.1);;
                    vpde.setSnowHight(v1);
                }else{
                    v2 =(float)0;
                }
                if(v1!=null&&v1<5) {
                    vpde.setSnowHight((float) 0);
                }
                    if(v2!=null) {
                        vpde.setSnowStress(v2);
                    }

            }else if("A".equals(type)){
                if(!",,,".endsWith(extremum[0])&&(!"///".equals(extremum[0]))) {
                    vpde.setPermafrost1Ceiling(Float.parseFloat(extremum[0]));
                }
                if(!",,,".endsWith(extremum[1])&&(!"///".equals(extremum[1]))) {
                    vpde.setPermafrost1Floor(Float.parseFloat(extremum[1]));
                }
            }
        }else{
            if ("U".equals(type)) {
                vpde.setWetness(Integer.parseInt(extremum[0]));
            }else if("LL".equals(type)&&(!"///".equals(extremum[0]))&&(!",,,".equals(extremum[0]))){
                vpde.setEvaporatl((float) (Float.parseFloat(extremum[0]) * 0.1));
            }else if("LB".equals(type)&&(!"///".equals(extremum[0]))&&(!",,,".equals(extremum[0]))){
                vpde.setEvaporath((float) (Float.parseFloat(extremum[0]) * 0.1));
            }else if("F4".equals(type)){//最大风
                String str0=extremum[0].substring(3);//风向
                if(!"///".equals(extremum[0].substring(0,3))) {
                    Float valF0 = (float) (Float.parseFloat(extremum[0].substring(0,3)) * 0.1);//风速
                    vpde.setBigwind(str0);
                    vpde.setBigwindSpeed(valF0);
                }
            }else if("F5".equals(type)){//极大风
                String str1=extremum[0].substring(3);//风向
                if(!"///".equals(extremum[0].substring(0,3))) {
                    Float valF1 = (float) (Float.parseFloat(extremum[0].substring(0, 3)) * 0.1);//风速
                    vpde.setMaxwind(str1);
                    vpde.setMaxwindSpeed(valF1);
                }
            }else if("S".equals(type)){//日照
                vpde.setSunshine((float) (Float.parseFloat(extremum[0]) * 0.1));
            }
        }
        if(Integer.parseInt(te.getDay())>31){
            System.out.println("type2="+type);
        }
        A0Decoding.put(te.getDay(),paramCollections);
    }
    /**
     * 指定时间的信息处理--小时
     */
    private void DtemKByHour(String[] extremum, String type, TimeEntity te,String hour){
        Map<String, ViewParamsHourEntity> mapHour=null;
        ParamCollections paramCollections=null;
        paramCollections=A0Decoding.get(te.getDay());
        if(paramCollections==null){
            paramCollections=new ParamCollections();
            paramCollections.setTimeEntity(te);
        }
        mapHour=paramCollections.getMapHour();
        ViewParamsHourEntity paramsHourEntity=null;
        paramsHourEntity=mapHour.get(hour);
        if(paramsHourEntity==null){
            paramsHourEntity=new ViewParamsHourEntity();
            paramsHourEntity.setHour(hour);
        }
        int length=extremum.length;
        if("K".equals(type)) {
            switch (length) {
                case 3:
                    if (CommonUtil.isNumeric(extremum[2])) {
                        paramsHourEntity.setDtem320((float) (Float.parseFloat(extremum[2]) * 0.1));
                    }
                case 2:
                    if (CommonUtil.isNumeric(extremum[1])) {
                         paramsHourEntity.setDtem160((float) (Float.parseFloat(extremum[1]) * 0.1));
                    }
                case 1:
                    if (CommonUtil.isNumeric(extremum[0])) {
                        paramsHourEntity.setDtem80((float) (Float.parseFloat(extremum[0]) * 0.1));
                    }
                default:
            }
        }
        mapHour.put("14",paramsHourEntity);
        if(Integer.parseInt(te.getDay())>31){
            System.out.println("type1="+type);
        }
        A0Decoding.put(te.getDay(),paramCollections);
    }
    public static void main(String[] args) {
        //新产品图片路径
        List<String> list=new ArrayList<>();
//        getFilePath("E:\\filedecode\\53463\\1951",list);
        int count=0;
//        for(String path:list) {
            File file = new File("E:\\filedecode\\53463\\已解\\A0\\1951\\A053463.A51");
//            File file = new File(path);
            if(file.getName().startsWith("A0")) {
                A0Decoding A0 = new A0Decoding();
                A0.assemblyData(file, null, null, null, null, file.getName(), null);
            }else if(file.getName().startsWith("A")){

//                A.aDataDecoding(file, "ASCII");


            }

//        }
    }
    public static void getFilePath(String path,List<String> list) {
        File file = null;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}