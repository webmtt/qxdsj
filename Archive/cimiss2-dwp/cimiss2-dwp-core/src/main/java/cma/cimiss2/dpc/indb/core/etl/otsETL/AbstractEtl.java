package cma.cimiss2.dpc.indb.core.etl.otsETL;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.ots.OTSBatchResult;
import cma.cimiss2.dpc.indb.core.ots.OTSHelper;
import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.core.tools.LogUtil;
import cma.cimiss2.dpc.indb.core.tools.StationInfo;
import com.alicloud.openservices.tablestore.ClientException;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 缓冲库为ots抽象类。
 *
 * @param <T>
 */
public abstract class AbstractEtl<T> implements Serializable {

    private static final long serialVersionUID = 4927559591371928671L;
    /**
     * 站点信息文件路径 多线程不需要传值
     */
    protected String path = "";
    /**
     * 站点信息
     */
    private static Map<String, Object> proMap;
    /**
     * 正确数量
     */
    ThreadLocal<Integer> errorNum = new ThreadLocal<Integer>() {
        public Integer initialValue() {
            return 0;
        }
    };
    /**
     * 错误数量
     */
    ThreadLocal<Integer> correctNum = new ThreadLocal<Integer>() {
        public Integer initialValue() {
            return 0;
        }
    };
    /**
     * 解码数据类型 rabbit的队列名
     */
    protected String event = null;
    /**
     * 文件
     */
    protected File file = null;
    /**
     * 批量数量
     */
    protected int batchSize = ConfigurationManager.getInteger("insert-batch-size");
    /**
     * 数据库选择
     */
    protected String dataSource = ConfigurationManager.getString("dataBaseType");
    /**
     * 站点信息刷新频率
     */
    protected Long intervalTime = ConfigurationManager.getLong("intervalTime");
    
    /**
     * 业务流程标识
     */
    protected String dataFlow = ConfigurationManager.getString("data_flow");
    
    
    public String getDataFlow() {
		return dataFlow;
	}

	public void setDataFlow(String dataFlow) {
		this.dataFlow = dataFlow;
	}

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public AbstractEtl() {
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public abstract ParseResult<T> extract(File file);

    public abstract StatTF transform(ParseResult<T> beans, File file, OTSHelper... tablesHelper);

    public Map<String, Object> getStationInfo() {
        if (proMap == null || proMap.size() == 0) {
            proMap = StationInfo.getProMap(path);
        }

        long judgeEndTime = System.currentTimeMillis();
        if (judgeEndTime - StationInfo.getJudgeTime() > intervalTime) {
            String jarWholePath;
            if ("".equals(path)) {
                jarWholePath = ConfigurationManager.getConfigPath() + "StationInfo_Config.lua";
            } else {
                jarWholePath = path + "config/StationInfo_Config.lua";
            }
            File file = new File(jarWholePath);
            long fileEndTime = file.lastModified();
            long fileStartTime = (Long) proMap.get("time");
            System.out.println("进入reload--" + (fileStartTime - fileEndTime) + "--" + fileStartTime + "--" + fileEndTime);
            if (fileStartTime - fileEndTime != 0) {
                System.out.println("重新加载--StationInfo_Config.lua-");
                LogUtil.info("loggerInfo","重新加载--StationInfo_Config.lua-");
                proMap = StationInfo.getProMap(path);
                proMap.put("time", file.lastModified());//加入文件修改时间
            }

            StationInfo.setJudgeTime(judgeEndTime);
        }

        return proMap;
    }

    /**
     * 站点信息更新
     * 间隔时间到后判断文件修改时间，时间修改重新加载站点信息文件
     *
     * @param intervalTime 间隔时间 毫秒
     */
//    public void reload(Long intervalTime) {
//        long judgeEndTime = System.currentTimeMillis();
//        if (judgeEndTime - StationInfo.getJudgeTime() > intervalTime) {
//            String jarWholePath;
//            long fileEndTime = file.lastModified();
//            long fileStartTime = (Long) getStationInfo().get("time");
//            System.out.println("进入reload--" + (fileStartTime - fileEndTime) + "--" + fileStartTime + "--" + fileEndTime);
//            if (fileStartTime - fileEndTime != 0) {
//                System.out.println("从新加载---");
//                proMap = StationInfo.getProMap(path);
//                proMap.put("time", file.lastModified());//加入文件修改时间
//            }
//            StationInfo.setJudgeTime(judgeEndTime);
//        }
//    }
//    public void reload(Long intervalTime) {
//        long judgeEndTime = System.currentTimeMillis();
////        System.out.println("进入reload--" + (judgeEndTime - judgeStartTime) + "--" + intervalTime + "--" + path);
//        if (judgeEndTime - StationInfo.getJudgeTime() > intervalTime) {
//            String jarWholePath;
//            if ("".equals(path)) {
//                jarWholePath = ConfigurationManager.getConfigPath() + "StationInfo_Config.lua";
//            } else {
//                jarWholePath = path + "config/StationInfo_Config.lua";
//            }
//            File file = new File(jarWholePath);
//            long fileEndTime = file.lastModified();
//            long fileStartTime = (Long) getStationInfo().get("time");
//            System.out.println("进入reload--" + (fileStartTime - fileEndTime) + "--" + fileStartTime + "--" + fileEndTime);
//            if (fileStartTime - fileEndTime != 0) {
//                System.out.println("从新加载---");
//                proMap = StationInfo.getProMap(path);
//                proMap.put("time", file.lastModified());//加入文件修改时间
//            }
//
//            StationInfo.setJudgeTime(judgeEndTime);
//        }
//    }

    /**
     * 统一数据入库方法，根据getDataSource（）判断使用哪个数据库、万里 1 阿里 0
     *
     * @param datas               格式化后数据
     * @param helper              ots连接
     * @param overwriteIfNotExist ots 是否覆盖
     * @return
     */
    public StatInsert load(List<Map<String, Object>> datas, OTSHelper helper, boolean overwriteIfNotExist) {
        StatInsert statInsert = new StatInsert();
        if (datas.size() <= batchSize) {
            subload(datas, helper, overwriteIfNotExist, statInsert);
        } else {
            int ceil = (int) Math.ceil(datas.size() / Double.valueOf(batchSize));
            for (int i = 0; i < ceil; i++) {
                if (i == ceil - 1) {
                    List<Map<String, Object>> subList = datas.subList(i * batchSize, datas.size());
                    subload(subList, helper, overwriteIfNotExist, statInsert);
                    continue;
                }
                List<Map<String, Object>> subList = datas.subList(i * batchSize, (i + 1) * (batchSize));
                subload(subList, helper, overwriteIfNotExist, statInsert);
            }
        }
        return statInsert;
    }

    private void subload(List<Map<String, Object>> datas, OTSHelper helper, boolean overwriteIfNotExist, StatInsert statInsert) {
        try {
            OTSBatchResult insertResult = helper.insert(datas, overwriteIfNotExist);
            statInsert.setErrorNum(insertResult.getFailedRowCount());
            statInsert.setCorrectNum(insertResult.getSuccessRowCount());
            List<OTSBatchResult.FailedRowResult> failedRows = insertResult.getFailedRows();
            Set<String> failKey = new HashSet<>();
            if (failedRows.size() > 0) {
                for (int i = 0; i < failedRows.size(); i++) {
                    //配置ei
                    HashMap<String, String> map1 = new HashMap<>();
                    String v01301 = (String) failedRows.get(i).getRow().get("V01301");
                    failKey.add(v01301);//目的为记录错误的站号，需要不发正常di，发错误di
                    String message = failedRows.get(i).getException().toString();
//                    if (message.contains("UnknownHostException")){
//                        statInsert.setAction(1);
//                    }
                    if (failedRows.get(i).getException() instanceof ClientException) {//ots断开 不ack 消息
                        statInsert.setAction(1);
                    }
                    map1.put("message", message);
                    map1.put("path", getPath());
                    statInsert.getEiInfo().addAll(DIEISender.makeEI(map1));
                    statInsert.getBuffer().append(v01301 + " 错误原因：" + message + "\n");
                    //配置入库失败的di
                    HashMap<String, String> map = new HashMap<>();
                    map.put("stationNumberChina", (String) datas.get(i).get("V01301"));
                    map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datas.get(i).get("D_DATETIME")));
                    map.put("fileName", file.getName());
                    map.put("event_type", String.valueOf(datas.get(i).get("D_DATA_ID")));
                    map.put("event_type1", event);
                    map.put("longitude", String.valueOf(datas.get(i).get("V06001")));
                    map.put("latitude", String.valueOf(datas.get(i).get("V05001")));
                    map.put("height", String.valueOf(datas.get(i).get("V07001")));
                    map.put("file_size", "");
                    map.put("dataFlow", dataFlow);
                    statInsert.getDiInfo().addAll(DIEISender.makeDI_fail(map));

                }
            }

            System.out.println("大批量成功1 数量：" + datas.size());
            //配置正常的DI

            for (int i = 0; i < datas.size(); i++) {
                String v01301 = (String) datas.get(i).get("V01301");
                if (!failKey.contains(v01301)) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("stationNumberChina", v01301);
                    map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datas.get(i).get("D_DATETIME")));
                    map.put("fileName", file.getName());
                    map.put("event_type", String.valueOf(datas.get(i).get("D_DATA_ID")));
                    map.put("event_type1", event);
                    map.put("longitude", String.valueOf(datas.get(i).get("V06001")));
                    map.put("latitude", String.valueOf(datas.get(i).get("V05001")));
                    map.put("height", String.valueOf(datas.get(i).get("V07001")));
                    map.put("file_size", "");
                    map.put("dataFlow", dataFlow);
                    statInsert.getDiInfo().addAll(DIEISender.makeDI(map));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 更正报判断
     *
     * @param D_RECORD_ID 主键
     * @param
     */
    public boolean corrMessage(String D_RECORD_ID, String V_BBB, OTSHelper helper) {
        try {
            Map<String, Object> primaryKey = new HashMap<>();
            primaryKey.put("D_RECORD_ID", D_RECORD_ID);
            Map<String, Object> row = helper.query(primaryKey);
            String v_bbb = (String) row.get("V_BBB");

//            if (v_bbb != null && v_bbb.hashCode() < V_BBB.hashCode()) {
//                helper.update(row);
//                return true;
//            }
            if(v_bbb == null){
                return true;
            }else if (v_bbb.hashCode() < V_BBB.hashCode()){
                return false;
            }else {//需要更新 updae
                return false;
            }
        } catch (Exception e) {
            System.out.println("更正报处理错误！！");
        }
        return false;
    }


    /**
     * 报文格式化
     *
     * @param reports   报文集合
     * @param D_DATA_ID D_DATA_ID
     * @param file      报文文件
     * @return
     * @throws ParseException
     */
    public List<Map<String, Object>> transformReports_ats(List<ReportInfo> reports, String D_DATA_ID, File file) throws ParseException {
        //报文集合
        List<Map<String, Object>> staDatas = new ArrayList<>();
        //文件名处理
        String fileName = file.getName();
        String[] fileNameSplit = fileName.split("_");
        //报文处理
        for (int i = 0; i < reports.size(); i++) {
            String report = reports.get(i).getReport();
            Map<String, Object> repmap = (Map<String, Object>) reports.get(i).getT();
            Map<String, Object> repData = new HashMap<>();
            String stationNumberChina = String.valueOf(repmap.get("V01301"));
            int num = stationNumberChina.substring(0, 1).hashCode();
            String observationTime = (String) repmap.get("D_DATETIME");
            String V_TT = fileNameSplit[6].split("-")[0];
            String V_CCCC = fileNameSplit[3].substring(0, 4);
            String D_RECORD_ID = observationTime + "_" + stationNumberChina + "_" + getEvent() + "_" + V_TT;
            repData.put("D_DATA_ID", D_DATA_ID);
            repData.put("D_RECORD_ID", D_RECORD_ID);
            repData.put("D_IYMDHM", new Date());//入库时间
            repData.put("D_RYMDHM", new Date(file.lastModified()));//
            repData.put("D_UPDATE_TIME", new Date());//更新时间
            repData.put("D_DATETIME", new SimpleDateFormat("yyyyMMddHHmmss").parse(observationTime));
            repData.put("V_BBB", String.valueOf(repmap.get("V_BBB")));
            repData.put("V_CCCC", V_CCCC);
            repData.put("V_TT", V_TT);//待确定
            repData.put("V01301", stationNumberChina);
            if (num >= 48 & num <= 57) {
                repData.put("V01300", stationNumberChina);
            } else {
                repData.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
            }
            repData.put("V05001", String.valueOf(repmap.get("V5001")));
            repData.put("V06001", String.valueOf(repmap.get("V6001")));
            repData.put("V_NCODE", "1");
            if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                String s = (String) getStationInfo().get(stationNumberChina + "+01");
                repData.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
            } else {
                repData.put("V_ACODE", "999999");
            }
            repData.put("V04001", observationTime.substring(0, 4));//资料观测年
            repData.put("V04002", observationTime.substring(4, 6));//资料观测月
            repData.put("V04003", observationTime.substring(6, 8));//资料观测日
            repData.put("V04004", observationTime.substring(8, 10));//资料观测时
            repData.put("V04005", observationTime.substring(10, 12));//资料观测分
            repData.put("V_LEN", String.valueOf(report.length()));
            repData.put("V_REPORT", report);

            staDatas.add(repData);
        }
        return staDatas;
    }

    public int getCloudType1Detail(String cloudType1, int i) {
        char c = cloudType1.charAt(i);
        switch (i) {
            case 0:
                if (c == '/') {
                    return 999999;
                } else {
                    int temp = Integer.parseInt(String.valueOf(c));
                    return (temp + 30);
                }
            case 1:
                if (c == '/') {
                    return 999999;
                } else {
                    int temp = Integer.parseInt(String.valueOf(c));
                    return (temp + 20);
                }
            case 2:
                if (c == '/') {
                    return 999999;
                } else {
                    int temp = Integer.parseInt(String.valueOf(c));
                    return (temp + 10);
                }
            default:
                return -1;
        }
    }
}

