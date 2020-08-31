package cma.cimiss2.dpc.decoder.fileDecode.Rfile.decode;

import cma.cimiss2.dpc.decoder.fileDecode.Rfile.bean.*;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.TimeEntity;
import cma.cimiss2.dpc.decoder.fileDecode.util.CommonUtil;
import cma.cimiss2.dpc.decoder.fileDecode.util.FileHadleUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * R文件数据处理
 */
public class RDecoding {
    // 存储小时值
    private static Map<String, RViewParamsHourEntity> mapHour =
            new ConcurrentHashMap<String, RViewParamsHourEntity>();
    // 存储日极值
    private static Map<String, RViewParamsDayEntity> mapPeak =
            new ConcurrentHashMap<String, RViewParamsDayEntity>();
    // 存储封面信息
    private static ROverHeadInfo ohi=new ROverHeadInfo();
    //存储仪器信息
    private static Map<String, RInstrumentInfo> IMap=new HashMap<String, RInstrumentInfo>();
    /**
     * R文件读取
     *
     * @param path
     */
    public void rDataDecoding(File path,String fileCode) {
        List<String> list = FileHadleUtil.readFromTextFile(path,fileCode);
        RFirstData sf = null;
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
            if (isstart) {
                if (str.endsWith("=") && arr[0].indexOf("=") != 0) {
                    dataList.add(str.substring(0, str.length() - 1));
                    if (i < size - 1) { // 判断下一条数据
                        str = list.get(i + 1);
                        arr = str.split("\\s+");
                        if (arr.length < 2) { // 当前要素完成
                            isstart = false;
                            Rhandle(dataList, te, type);
                        }
                    }
                    type = "";
                } else {
                    dataList.add(str);
                }
                continue;
            }
            // 清理数据
            dataList.clear();
            if (i == 0) { // 台站参数信息
                sf = new RFirstData();
                sf.setAreacode(arr[0]);
                sf.setWd(arr[1]);
                sf.setJd(arr[2]);
                sf.setViewhight(arr[3]);
                sf.setDepoyrank(arr[4]);
                sf.setQualitycode(arr[5]);
                sf.setYear(arr[6]);
                sf.setMonth(arr[7]);
            } else { // 观测参数
                if (null == viewCode) {
                    viewCode = arr[0];
                } else if (viewCode.startsWith("YD")) { // 散射辐射表类型
                    isstart = true;
                    dataList.add(str);
                    type = "YD";
                } else if (viewCode.startsWith("YR")) { // 反射辐射表类型
                    isstart = true;
                    dataList.add(str);
                    type = "YR";
                } else if (viewCode.startsWith("YS")) { // 直射辐射表类型
                    isstart = true;
                    dataList.add(str);
                    type = "YS";
                } else if (viewCode.startsWith("CZ")) { // 场地周围环境变化
                    isstart = true;
                    dataList.add(str);
                    type = "CZ";
                }else if (viewCode.startsWith("BZ")) { // 备注
                    isstart = true;
                    dataList.add(str);
                    type = "BZ";
                }else if (viewCode.startsWith("YQ")) { // 总辐射表类型
                    isstart = true;
                    dataList.add(str);
                    type = "YQ";
                }else if (viewCode.startsWith("YN")) { // 净辐射表类型
                    isstart = true;
                    dataList.add(str);
                    type = "YN";
                }else if (viewCode.startsWith("YJ")) { // 记录器
                    isstart = true;
                    dataList.add(str);
                    type = "YJ";
                }else if (viewCode.startsWith("YX")) { // 仪器类型性能
                    isstart = true;
                    dataList.add(str);
                    type = "YX";
                }else if (viewCode.startsWith("FM")) { //封面
                    isstart = true;
                    dataList.add(str);
                    type = "FM";
                }else if (viewCode.startsWith("QZ")) { // 作用层状态质量控制码
                    isstart = true;
                    dataList.add(str);
                    type = "QZ";
                }else if (viewCode.startsWith("QQ")) { // 总辐射质量控制码
                    isstart = true;
                    dataList.add(str);
                    type = "QQ";
                }else if (viewCode.startsWith("QN")) { // 净全辐射质量控制码
                    isstart = true;
                    dataList.add(str);
                    type = "QN";
                }else if (viewCode.startsWith("Z")) { // 作用层状态
                    isstart = true;
                    dataList.add(str);
                    type = "Z";
                }else if (viewCode.startsWith("Q")) { // 总辐射
                    isstart = true;
                    dataList.add(str);
                    type = "Q";
                }else if (viewCode.startsWith("N")) { // 净全辐射
                    isstart = true;
                    dataList.add(str);
                    type = "N";
                }else if (viewCode.startsWith("D")) { // 散射辐射
                    isstart = true;
                    dataList.add(str);
                    type = "N";
                }else if (viewCode.startsWith("S")) { // 直射辐射
                    isstart = true;
                    dataList.add(str);
                    type = "N";
                }else if (viewCode.startsWith("R")) { // 反射辐射
                    isstart = true;
                    dataList.add(str);
                    type = "N";
                }
            }
        }
    }
    /**
     * R文件处理方法
     * @param list
     * @param te
     * @param type
     */
    private void Rhandle(List<String> list, TimeEntity te, String type) {
        if ("YD".startsWith(type)) { // 散射辐射表类型
            scatterTypeHandle(list, te);
        } else if ("YR".startsWith(type)) { // 反射辐射表类型
            reflectTypeHandle(list, te);
        } else if ("YS".startsWith(type)) { // 直射辐射表类型
            projectivityTypeHandle(list, te);
        } else if ("CZ".startsWith(type)) { // 场地周围环境变化
            siteHandle(list, te);
        } else if ("BZ".startsWith(type)) { // 备注
            noteHandle(list, te);
        }else if ("YQ".startsWith(type)) { // 总辐射表类型
            globalchartHandle(list, te);
        }else if ("YN".startsWith(type)) { // 净辐射表类型
            netchartHandle(list, te);
        }else if ("YJ".startsWith(type)) { // 记录器
            loggerHandle(list, te);
        }else if ("YX".startsWith(type)) { // 仪器类型性能
            apparatusTypeHandle(list, te);
        }else if ("FM".startsWith(type)) { // 封面
            FMHandle(list, te);
        }else if ("QZ".startsWith(type)) { // 作用层状态质量控制码
            activeQulityHandle(list, te);
        }else if ("QQ".startsWith(type)) { // 总辐射质量控制码
            globalQulityHandle(list, te);
        }else if ("QN".startsWith(type)) { // 净全辐射质量控制码
            netQulityHandle(list, te);
        }else if ("QD".startsWith(type)) { // 净全辐射质量控制码
            scatterQulityHandle(list, te);
        }else if ("QS".startsWith(type)) { // 净全辐射质量控制码
            projectivityQulityHandle(list, te);
        }else if ("QR".startsWith(type)) { // 净全辐射质量控制码
            reflectQulityHandle(list,te);
        }else if ("Z".startsWith(type)) { // 作用层状态
            activeHandle(list, te);
        }else if ("Q".startsWith(type)) { //type, 总辐射
            globalHandle(list, te);
        }else if ("N".startsWith(type)) { // 净全辐射
            netHandle(list, te);
        }else if ("D".startsWith(type)) { // 散射辐射
            scatterHandle(list, te);
        }else if ("S".startsWith(type)) { // 直射辐射
            projectivityHandle(list, te);
        }else if ("R".startsWith(type)) { // 反射辐射
            reflectHandle(list,te);
        }
    }
    /**
     * 散射辐射处理
     * @param list
     * @param te
     */
  private void scatterHandle(List<String> list, TimeEntity te) {
        String str = null;
        String[] arr = null;
        String dayTime=null;
        int day=0;
        for(int i=0,length=list.size();i<length;i++){
            day++;
            if(day<10){
                dayTime="0"+day;
            }else{
                dayTime=""+day;
            }
            te.setDay(dayTime);
            str = list.get(i);
            if(str.length()>96){//有极值
                String str1=str.substring(0,96);//24小时数据
                arr=str1.split("\\s+");
                if(arr.length>0) {
                    dayValueByHour(arr, "D", te);
                }
                String str2=str.substring(96);
                arr=str2.split("\\s+");
                if(arr.length>0) {
                    extremumHandle(arr, "D", te);
                }
            }
        }
    }
    /**
     * 散射辐射质控码处理
     * @param list
     * @param te
     */
  private void scatterQulityHandle(List<String> list, TimeEntity te) {
        String str = null;
        String[] arr = null;
        String dayTime=null;
        int day=0;
        for(int i=0,length=list.size();i<length;i++){
            day++;
            if(day<10){
                dayTime="0"+day;
            }else{
                dayTime=""+day;
            }
            te.setDay(dayTime);
            str = list.get(i);
            if(str.length()>96){//有极值
                String str1=str.substring(0,96);//24小时数据
                arr=str1.split("\\s+");
                if(arr.length>0) {
                    dayValueByHourQulity(arr, "D", te);
                }
                String str2=str.substring(96);
                arr=str2.split("\\s+");
                if(arr.length>0) {
                    extremumQulityHandle(arr, "D", te);
                }
            }
        }
    }
    /**
     * 反射辐射处理
     */
  private void reflectHandle(List<String> list, TimeEntity te){
        String str = null;
        String[] arr = null;
        String dayTime=null;
        int day=0;
        for(int i=0,length=list.size();i<length;i++){
            day++;
            if(day<10){
                dayTime="0"+day;
            }else{
                dayTime=""+day;
            }
            te.setDay(dayTime);
            str = list.get(i);
            if(str.length()>120){//有极值
                String str1=str.substring(0,120);//24小时数据
                arr=str1.split("\\s+");
                if(arr.length>0) {
                    dayValueByHour(arr, "R", te);
                }
                String str2=str.substring(120,138);
                arr=str2.split("\\s+");
                if(arr.length>0) {
                    extremumHandle(arr, "R", te);
                }
                String str3=str.substring(138);
                arr=str3.split("\\s+");
                if(arr.length>0) {
                    timingHandle(arr, "R", te);
                }
            }
        }
    }
    /**
     * 反射辐射处理
     */
  private void reflectQulityHandle(List<String> list, TimeEntity te){
        String str = null;
        String[] arr = null;
        String dayTime=null;
        int day=0;
        for(int i=0,length=list.size();i<length;i++){
            day++;
            if(day<10){
                dayTime="0"+day;
            }else{
                dayTime=""+day;
            }
            te.setDay(dayTime);
            str = list.get(i);
            if(str.length()>96){//有极值
                String str1=str.substring(0,96);//24小时数据
                arr=str1.split("\\s+");
                if(arr.length>0) {
                    dayValueByHourQulity(arr, "QR", te);
                }
                String str2=str.substring(96,108);
                arr=str2.split("\\s+");
                if(arr.length>0) {
                    extremumQulityHandle(arr, "QR", te);
                }
                String str3=str.substring(108);
                arr=str3.split("\\s+");
                if(arr.length>0) {
                    timingHandle(arr, "QR", te);
                }
            }
        }
    }
    /**
     * 直射辐射处理
     * @param list
     * @param te
     */
  private void projectivityHandle(List<String> list, TimeEntity te){
        String str = null;
        String[] arr = null;
        String dayTime=null;
        int day=0;
        for(int i=0,length=list.size();i<length;i++){
            day++;
            if(day<10){
                dayTime="0"+day;
            }else{
                dayTime=""+day;
            }
            te.setDay(dayTime);
            str = list.get(i);
            if(str.length()>96){//有极值
                String str1=str.substring(0,120);//24小时数据
                arr=str1.split("\\s+");
                if(arr.length>0) {
                    dayValueByHour(arr, "S", te);
                }
                String str2=str.substring(96);
                arr=str2.split("\\s+");
                if(arr.length>0) {
                    extremumHandle(arr, "S", te);
                }
            }
        }
    }
    /**
     * 直射辐射控制码处理
     * @param list
     * @param te
     */
  private void projectivityQulityHandle(List<String> list, TimeEntity te){
        String str = null;
        String[] arr = null;
        String dayTime=null;
        int day=0;
        for(int i=0,length=list.size();i<length;i++){
            day++;
            if(day<10){
                dayTime="0"+day;
            }else{
                dayTime=""+day;
            }
            te.setDay(dayTime);
            str = list.get(i);
            if(str.length()>96){//有极值
                String str1=str.substring(0,96);//24小时数据
                arr=str1.split("\\s+");
                if(arr.length>0) {
                    dayValueByHourQulity(arr, "QS", te);
                }
                String str2=str.substring(96);
                arr=str2.split("\\s+");
                if(arr.length>0) {
                    extremumQulityHandle(arr, "QS", te);
                }
            }
        }
    }

    /**
     * 场地周围环境变化
     * @param list
     * @param te
     */
  private void siteHandle(List<String> list, TimeEntity te){
        for(int i=0,size=list.size();i<size;i++) {
            String t1 = list.get(i);
            String[] arr = t1.split("\\s+");
            if ("01".equals(arr[0])) {
                ohi.setDescribe(arr[1]);
            } else if ("02".equals(arr[0])) {
                ohi.setOther(arr[1]);
            }
        }
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
     * 仪器类型处理
     * @param list
     * @param te
     */
  private void projectivityTypeHandle(List<String> list, TimeEntity te) {
        String [] arr= CommonUtil.listToArray(list);
        RInstrumentInfo isi=new RInstrumentInfo();
        isi.setType("YS");
        isi.setModel(arr[0]);
        isi.setNumber(arr[1]);
        isi.setSensitivity(arr[2]);
        isi.setResTime(arr[3]);
        isi.setResistance(arr[4]);
        isi.setCheckTime(arr[5]);
        isi.setStartWorkTime(arr[6]);
        IMap.put("YS",isi);
    }

    /**
     * 反射表类型处理
     * @param list
     * @param te
     */
  private void reflectTypeHandle(List<String> list, TimeEntity te) {
        String [] arr= CommonUtil.listToArray(list);
        RInstrumentInfo isi=new RInstrumentInfo();
        isi.setType("YR");
        isi.setModel(arr[0]);
        isi.setNumber(arr[1]);
        isi.setSensitivity(arr[2]);
        isi.setResTime(arr[3]);
        isi.setResistance(arr[4]);
        isi.setCheckTime(arr[5]);
        isi.setStartWorkTime(arr[6]);
        IMap.put("YR",isi);
    }

    /**
     * 散射表类型处理
     * @param list
     * @param te
     */
  private void scatterTypeHandle(List<String> list, TimeEntity te) {
        String [] arr= CommonUtil.listToArray(list);
        RInstrumentInfo isi=new RInstrumentInfo();
        isi.setType("YD");
        isi.setModel(arr[0]);
        isi.setNumber(arr[1]);
        isi.setSensitivity(arr[2]);
        isi.setResTime(arr[3]);
        isi.setResistance(arr[4]);
        isi.setCheckTime(arr[5]);
        isi.setStartWorkTime(arr[6]);
        IMap.put("YD",isi);
    }
    /**
     * 总辐射表类型处理
     * @param list
     * @param te
     */
  private void globalchartHandle(List<String> list, TimeEntity te){
        String [] arr= CommonUtil.listToArray(list);
        RInstrumentInfo isi=new RInstrumentInfo();
        isi.setType("YQ");
        isi.setModel(arr[0]);
        isi.setNumber(arr[1]);
        isi.setSensitivity(arr[2]);
        isi.setResTime(arr[3]);
        isi.setResistance(arr[4]);
        isi.setCheckTime(arr[5]);
        isi.setStartWorkTime(arr[6]);
        IMap.put("YQ",isi);
    }

    /**
     * 记录器处理
     * @param list
     * @param te
     */
  private void loggerHandle(List<String> list, TimeEntity te){
        String [] arr= CommonUtil.listToArray(list);
        RInstrumentInfo isi=new RInstrumentInfo();
        isi.setType("YJ");
        isi.setModel(arr[0]);
        isi.setNumber(arr[1]);
        isi.setCheckTime(arr[2]);
        isi.setStartWorkTime(arr[3]);
        IMap.put("YJ",isi);
    }
    /**
     * 净辐射表类型处理
     * @param list
     * @param te
     */
  private void netchartHandle(List<String> list, TimeEntity te){
        String [] arr= CommonUtil.listToArray(list);
        RInstrumentInfo isi=new RInstrumentInfo();
        isi.setType("YN");
        isi.setModel(arr[0]);
        isi.setNumber(arr[1]);
        isi.setSensitivity(arr[2]+" "+arr[3]);
        isi.setResTime(arr[4]);
        isi.setResistance(arr[5]);
        isi.setCheckTime(arr[6]);
        isi.setStartWorkTime(arr[7]);
        IMap.put("YN",isi);
    }
    /**
     * 仪器类型性能处理
     * @param list
     * @param te
     */
  private void apparatusTypeHandle(List<String> list, TimeEntity te){
        if(list.size()==0){
            return;
        }

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
        ohi.setTotalHeight(arr[5]);
        ohi.setReflectHeight(arr[6]);
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
     * 作用层状态质量控制码处理
     * @param list
     * @param te
     */
  private void activeQulityHandle(List<String> list, TimeEntity te){
        RViewParamsDayEntity vprd = null;
        String str=list.get(0);
        String[] arr=str.split("\\s+");
        int day=0;
        for(int i=0,length=arr.length;i<length;i++){
            day++;
            if(day<10) {
                vprd = mapPeak.get("0"+day);
                te.setDay("0"+day);
            }else{
                vprd=mapPeak.get(""+day);
                te.setDay(""+day);
            }
            String value=arr[i];
            if(vprd==null){
                vprd=new RViewParamsDayEntity();
            }
            vprd.setTime(te.getTime());
            vprd.setActiveQuality(value);
            mapPeak.put(te.getDay(),vprd);
            vprd=null;
        }
    }

    /**
     * 总辐射质量控制码处理
     * @param list
     * @param te
     */
  private void globalQulityHandle(List<String> list, TimeEntity te){
        String str = null;
        String[] arr = null;
        String dayTime=null;
        int day=0;
        for(int i=0,length=list.size();i<length;i++){
            day++;
            if(day<10){
                dayTime="0"+day;
            }else{
                dayTime=""+day;
            }
            te.setDay(dayTime);
            str = list.get(i);
            if(str.length()>96){//有极值
                String str1=str.substring(0,96);//24小时数据
                arr=str1.split("\\s+");
                if(arr.length>0) {
                    dayValueByHourQulity(arr, "QQ", te);
                }
                String str2=str.substring(96);
                arr=str2.split("\\s+");
                if(arr.length>0) {
                    extremumQulityHandle(arr, "QQ", te);
                }
            }
        }
    }



    /**
     * 净全辐射质量控制码处理
     * @param list
     * @param te
     */
  private void netQulityHandle(List<String> list, TimeEntity te){
        String str = null;
        String[] arr = null;
        String dayTime=null;
        int day=0;
        for(int i=0,length=list.size();i<length;i++){
            day++;
            if(day<10){
                dayTime="0"+day;
            }else{
                dayTime=""+day;
            }
            te.setDay(dayTime);
            str = list.get(i);
            if(str.length()>96){//有极值
                String str1=str.substring(0,96);//24小时数据
                arr=str1.split("\\s+");
                if(arr.length>0) {
                    dayValueByHourQulity(arr, "QN", te);
                }
                String str2=str.substring(96);
                arr=str2.split("\\s+");
                if(arr.length>0) {
                    extremumQulityHandle(arr, "QN", te);
                }
            }
        }
    }
    /**
     * 作用层状态处理
     * @param list
     * @param te
     */
  private void activeHandle(List<String> list, TimeEntity te){
        RViewParamsDayEntity vprd = null;
        String str=list.get(0);
        String[] arr=str.split("\\s+");
        int day=0;
      for(int i=0,length=arr.length;i<length;i++){
          day++;
          if(day<10) {
              vprd = mapPeak.get("0"+day);
              te.setDay("0"+day);
          }else{
              vprd=mapPeak.get(""+day);
              te.setDay(""+day);
          }
          String value=arr[i];
          if(vprd==null){
              vprd=new RViewParamsDayEntity();
          }
          vprd.setTime(te.getTime());
          vprd.setActiveState(value);
          mapPeak.put(te.getDay(),vprd);
          vprd=null;
      }

    }
    /**
     * 总辐射处理
     * @param list
     * @param te
     */
  private void globalHandle(List<String> list, TimeEntity te){
        String str = null;
        String[] arr = null;
        String dayTime=null;
        int day=0;
        for(int i=0,length=list.size();i<length;i++){
            day++;
            if(day<10){
                dayTime="0"+day;
            }else{
                dayTime=""+day;
            }
            te.setDay(dayTime);
            str = list.get(i);
            if(str.length()>96){//有极值
                String str1=str.substring(0,96);//24小时数据
                arr=str1.split("\\s+");
                if(arr.length>0) {
                    dayValueByHour(arr, "Q", te);
                }
               String str2=str.substring(96);
               arr=str2.split("\\s+");
               if(arr.length>0) {
                   extremumHandle(arr, "Q", te);
               }
            }
        }
    }


    /**
     * 净全辐射处理
     * @param list
     * @param te
     */
  private void netHandle(List<String> list, TimeEntity te){
        String str = null;
        String[] arr = null;
        String dayTime=null;
        int day=0;
        for(int i=0,length=list.size();i<length;i++){
            day++;
            if(day<10){
                dayTime="0"+day;
            }else{
                dayTime=""+day;
            }
            te.setDay(dayTime);
            str = list.get(i);
            if(str.length()>120){//有极值
                String str1=str.substring(0,120);//24小时数据
                arr=str1.split("\\s+");
                if(arr.length>0) {
                    dayValueByHour(arr, "N", te);
                }
                String str2=str.substring(120);
                arr=str2.split("\\s+");
                if(arr.length>0) {
                    extremumHandle(arr, "N", te);
                }
            }
        }
    }

    /**
     * 9,12,15时指定值
     * @param arr
     * @param type
     * @param te
     */
    private static void timingHandle(String[] arr, String type,TimeEntity te){
        String hours=null;
        RViewParamsHourEntity vpr=mapHour.get("09");
        if(vpr==null){
            vpr=new RViewParamsHourEntity();
        }
        if("R".equals(type)){
            vpr.setSunProjectivelyHour(arr[0]);
            vpr.setGasTurbidity(arr[3]);
        }else if("QR".equals(type)){
            vpr.setSunProjectivelyHourQualify(arr[0]);
            vpr.setGasTurbidityQuality(arr[3]);
        }
        mapHour.put(hours,vpr);

        vpr=mapHour.get("12");
        if(vpr==null){
            vpr=new RViewParamsHourEntity();
        }
        if("R".equals(type)){
            vpr.setSunProjectivelyHour(arr[1]);
            vpr.setGasTurbidity(arr[4]);
        }else if("QR".equals(type)){
            vpr.setSunProjectivelyHourQualify(arr[1]);
            vpr.setGasTurbidityQuality(arr[4]);
        }
        mapHour.put(hours,vpr);

        vpr=mapHour.get("15");
        if(vpr==null){
            vpr=new RViewParamsHourEntity();
        }
        if("R".equals(type)){
            vpr.setSunProjectivelyHour(arr[2]);
            vpr.setGasTurbidity(arr[5]);
        }else if("QR".equals(type)){
            vpr.setSunProjectivelyHourQualify(arr[2]);
            vpr.setGasTurbidityQuality(arr[5]);
        }
        mapHour.put(hours,vpr);
    }
    /**
     * 0-23小时的数据（24小时数据）
     * @param arr
     * @param te
     */
    private static void dayValueByHour(String[] arr, String type,TimeEntity te) {
        String hours=null;
        for(int i=0;i<arr.length;i++){
            String value=arr[i];
            hours=i+"";
            if(hours.length()<2){
                hours="0"+i;
            }
            RViewParamsHourEntity vpr=mapHour.get(hours);
            if(vpr==null){
                vpr=new RViewParamsHourEntity();
            }
            if("Q".equals(type)) {
                vpr.setGlobal(value);
            }else if("N".equals(type)){
                vpr.setNet(value);
            }else if("D".equals(type)){
                vpr.setScatter(value);
            }else if("S".equals(type)){
                vpr.setProjective(value);
            }else if("R".equals(type)){
                vpr.setReflect(value);
            }
            mapHour.put(hours,vpr);
        }
    }

    /**
     * 日值处理
     *
     * @param extremum
     * @param type
     */
    private static void extremumHandle(String[] extremum, String type, TimeEntity te) {
        RViewParamsDayEntity vpde = null;
        int length = extremum.length;
        vpde = mapPeak.get(te.getDay());
        if (vpde == null) {
            vpde = new RViewParamsDayEntity();
            vpde.setTime(te.getTime());
        }
        if ("Q".equals(type)) {
            vpde.setMaxGlobal(extremum[1]);
            vpde.setGlobal(extremum[0]);
            vpde.setSunHours(extremum[3]);
            vpde.setMaxGlobalTime(extremum[2]);
        }else if("N".equals(type)){
            vpde.setNet(extremum[0]);
            vpde.setMaxNet(extremum[1]);
            vpde.setMaxNetTime(extremum[2]);
            vpde.setMinNet(extremum[3]);
            vpde.setMinNetTime(extremum[4]);
        }else if("D".equals(type)){
            vpde.setScatter(extremum[0]);
            vpde.setMaxScatter(extremum[1]);
            vpde.setMaxScatterTime(extremum[2]);
        }else if("S".equals(type)){
            vpde.setProjective(extremum[0]);
            vpde.setMaxProjectively(extremum[1]);
            vpde.setMaxProjectivelyTime(extremum[2]);
            vpde.setPlatProjective(extremum[3]);
        }else if("R".equals(type)){
            vpde.setReflect(extremum[0]);
            vpde.setReflectPercent(extremum[1]);
            vpde.setMaxReflect(extremum[2]);
            vpde.setMaxReflectTime(extremum[3]);
        }
        mapPeak.put(te.getDay(), vpde);
    }
    /**
     * 日值质控处理
     * @param extremum
     * @param type
     * @param te
     */
    private static void extremumQulityHandle(String[] extremum, String type, TimeEntity te) {
        RViewParamsDayEntity vpde = null;
        int length = extremum.length;
        vpde = mapPeak.get(te.getDay());
        if (vpde == null) {
            vpde = new RViewParamsDayEntity();
            vpde.setTime(te.getTime());
        }
        if ("QQ".equals(type)) {
            vpde.setMaxGlobalQuality(extremum[1]);
            vpde.setGlobalQuality(extremum[0]);
            vpde.setSunHoursQuality(extremum[3]);
            vpde.setMaxGlobalTimeQuality(extremum[2]);
        }else if("QN".equals(type)){
            vpde.setNetQuality(extremum[0]);
            vpde.setMaxNetQuality(extremum[1]);
            vpde.setMaxNetTimeQuality(extremum[2]);
            vpde.setMinNetQuality(extremum[3]);
            vpde.setMinNetTimeQuality(extremum[4]);
        }else if("QD".equals(type)){
            vpde.setScatterQuality(extremum[0]);
            vpde.setMaxScatterQuality(extremum[1]);
            vpde.setMaxScatterTimeQuality(extremum[2]);
        }else if("QS".equals(type)){
            vpde.setProjectivelyQualify(extremum[0]);
            vpde.setMaxProjectivelyQualify(extremum[1]);
            vpde.setMaxProjectiveTimeQuilts(extremum[2]);
            vpde.setPlatProjectivelyQualify(extremum[3]);
        }else if("QR".equals(type)){
            vpde.setReflectQuality(extremum[0]);
            vpde.setReflectPercentQuality(extremum[1]);
            vpde.setMaxReflectQuality(extremum[2]);
            vpde.setMaxReflectTimeQuality(extremum[3]);
        }
        mapPeak.put(te.getDay(), vpde);
    }

    /**
     * 时值质控处理
     * @param arr
     * @param type
     * @param te
     */
    private static void dayValueByHourQulity(String[] arr, String type,TimeEntity te) {
        String hours=null;
        for(int i=0;i<arr.length;i++){
            String value=arr[i];
            hours=i+"";
            if(hours.length()<2){
                hours="0"+i;
            }
            RViewParamsHourEntity vpr=mapHour.get(hours);
            if(vpr==null){
                vpr=new RViewParamsHourEntity();
            }
            if("QQ".equals(type)) {
                vpr.setGlobalQuality(value);
            }else if("QN".equals(type)){
                vpr.setNetQuality(value);
            }else if("QD".equals(type)){
                vpr.setScatterQuality(value);
            }else if("QS".equals(type)){
                vpr.setProjectiveQuilts(value);
            }else if("QR".equals(type)){
                vpr.setReflectQuality(value);
            }
            mapHour.put(hours,vpr);
        }
    }

}
