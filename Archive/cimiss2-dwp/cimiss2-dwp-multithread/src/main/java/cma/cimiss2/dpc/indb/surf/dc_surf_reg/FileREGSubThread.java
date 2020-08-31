package cma.cimiss2.dpc.indb.surf.dc_surf_reg;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataReg;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.indb.core.IocBuilder;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.etl.mysqlETL.SurfREGEtl1;
import cma.cimiss2.dpc.indb.core.ots.OTS;
import cma.cimiss2.dpc.indb.core.ots.OTSHelper;
import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.core.tools.LogUtil;
import cma.cimiss2.dpc.indb.surf.FilePolling;
import com.alicloud.openservices.tablestore.AsyncClient;

import java.io.File;
import java.util.*;

import static cma.cimiss2.dpc.indb.core.tools.FileUtil.remove;

/**
 * @Description:
 * @Aouthor: xzh
 * @create: 2018-05-18 17:12
 */
public class FileREGSubThread implements Runnable {
    private Action action;
    String configPath = ConfigurationManager.getConfigPath();
    private String horTable;
    private String minTable;
    private String minPreTable;
    private String cumPreTable;
    private String repTable;
    private Map<String,String> codeMap = new HashMap<String,String>();
    private String dataSource;
    private SurfREGEtl1 surfREGEtlMysql;
    private cma.cimiss2.dpc.indb.core.etl.otsETL.SurfREGEtl surfREGEtlOTS;
    private OTSHelper horTableHelper;
    private OTSHelper minTableHelper;
    private OTSHelper repTableHelper;
    private List<String> messages;
    private String src;
    private String target;

    public FileREGSubThread() {
        horTable = ConfigurationManager.getString("table-hor");
        minTable = ConfigurationManager.getString("table-min");
        minPreTable = ConfigurationManager.getString("table-minPre");
        cumPreTable = ConfigurationManager.getString("table-cumPre");
        repTable = ConfigurationManager.getString("table-rep");
        codeMap.put("cts_code", ConfigurationManager.getString("cts_code"));
        codeMap.put("hor_sod_code", ConfigurationManager.getString("hor_sod_code"));
        codeMap.put("mul_sod_code", ConfigurationManager.getString("mul_sod_code"));
        codeMap.put("pre_sod_code", ConfigurationManager.getString("pre_sod_code"));
        codeMap.put("accu_sod_code", ConfigurationManager.getString("accu_sod_code"));
        codeMap.put("report_sod_code", ConfigurationManager.getString("report_sod_code"));
        dataSource = ConfigurationManager.getString("dataBaseType");
        src = ConfigurationManager.getString("src");
        target = ConfigurationManager.getString("target");
        DIEISender.LOCAL_DI_OPTION = ConfigurationManager.getBoolean("di-option");
        DIEISender.LOCAL_EI_OPTION = ConfigurationManager.getBoolean("ei-option");
        if (dataSource.equals("2")) {
            surfREGEtlOTS = new cma.cimiss2.dpc.indb.core.etl.otsETL.SurfREGEtl();
            surfREGEtlOTS.setPath(configPath);
            AsyncClient  client = OTS.getClient();
            minTableHelper = new OTSHelper(client, minTable);
            horTableHelper = new OTSHelper(client, horTable);
            repTableHelper = new OTSHelper(client, repTable);
        } else {
            surfREGEtlMysql = IocBuilder.ioc(ConfigurationManager.getConfigPath()).get(SurfREGEtl1.class);
            surfREGEtlMysql.setBatchSize(ConfigurationManager.getInteger("insert-batch-size"));
            surfREGEtlMysql.setDataSource(dataSource);
            surfREGEtlMysql.setIntervalTime(ConfigurationManager.getLong("intervalTime"));
        }
    }


    @Override
    public void run() {
        while (!FilePolling.files.isEmpty()) {
            String take = null;
            try {
                take = FilePolling.files.take();
                LogUtil.info("messageInfo", take);
                execute(take);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //入库完成 开始移动文件
            if (target != null && !"".equals(target)) {
                boolean remove = remove(take.split(":")[1], src, target);
            }
        }
    }

    public void execute(String message) {
        List<ReportError> errorList = null;
        List<RestfulInfo> diInfo = new ArrayList<>();
        List<RestfulInfo> eiInfo = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        List<Map<String, Object>> horList = null;
        List<Map<String, Object>> minList = null;
        List<Map<String, Object>> minPreList = null;
        List<Map<String, Object>> cumPreList = null;
        List<Map<String, Object>> reports = null;
        String event = "";
        String path = "";
        StatInsert loadHor = null;
        StatInsert loadMin = null;
        StatInsert loadMinPre = null;
        StatInsert loadCumPre = null;
        StatInsert loadRep = null;
        try {
//            System.out.println("message==="+message);
            //event = message.split(":")[0];
            //path = message.split(":")[1];
        	event = message.split(":")[0];
        	path = message.substring(message.indexOf(":") + 1);
            File file = new File(path);
            if (dataSource.equals("2")) {
                surfREGEtlOTS.setEvent(event);
                surfREGEtlOTS.setFile(file);
            } else {
                surfREGEtlMysql.setEvent(event);
                surfREGEtlMysql.setFile(file);
            }
            buffer.append("INFO : " + new Date() + " begin : " + message + "\n");
            //解码
            ParseResult<SurfaceObservationDataReg> result = null;
            try {
                if (dataSource.equals("2")) {
                    result = surfREGEtlOTS.extract(file);
                } else {
                    result = surfREGEtlMysql.extract(file);
                }
                errorList = result.getError();
                if (result.isSuccess()) {//全部解码成功
                    //格式化数据
                    StatTF statREG = null;
                    if (dataSource.equals("2")) {
                        statREG = surfREGEtlOTS.transform(result, file, horTableHelper, minTableHelper, repTableHelper);
                    } else {
                        statREG = surfREGEtlMysql.transform(result, file, codeMap, horTable, minTable, minPreTable, cumPreTable, repTable);
                    }
                    horList = statREG.getHorList();
                    minList = statREG.getMinList();
                    minPreList = statREG.getMinPreList();
                    cumPreList = statREG.getCumPreList();
                    reports = statREG.getRepList();
                    buffer.append(statREG.getBuffer().toString());
                    //插入数据
                    try {
                        if (dataSource.equals("2")) {
                            loadHor = surfREGEtlOTS.load(horList, horTableHelper, false);
                        } else {
                            //小时表
                            loadHor = surfREGEtlMysql.load(horList, "rdb");
                        }
                    } catch (Exception e) {
                        buffer.append("入库 " + horTable + " 失败:" + message + "\n" +
                                "错误原因:" + e + "\n");
                        return;
                    }
                    //分钟表
                    try {
                        if (dataSource.equals("2")) {
                            loadMin = surfREGEtlOTS.load(minList, minTableHelper, false);
                        } else {
                          loadMin = surfREGEtlMysql.load(minList, "rdb");
                        }
                    } catch (Exception e) {
                        buffer.append("入库 " + minTable + " 失败:" + message + "\n" +
                                "错误原因:" + e + "\n");
                    }
                    //分钟降水表
                    try {
                    	loadMinPre = surfREGEtlMysql.load(minPreList, "rdb");
                    } catch (Exception e) {
                        buffer.append("入库 " + minTable + " 失败:" + message + "\n" +
                                "错误原因:" + e.getMessage() + "\n");
                    }
                    //累计降水表
                    try {
                    	loadCumPre = surfREGEtlMysql.load(cumPreList, "rdb");
                    } catch (Exception e) {
                        buffer.append("入库 " + minTable + " 失败:" + message + "\n" +
                                "错误原因:" + e.getMessage() + "\n");
                    }
                    //报文表
                    try {
                        if (dataSource.equals("2")) {
                            loadRep = surfREGEtlOTS.load(reports, repTableHelper, false);
                        } else {
                         loadRep = surfREGEtlMysql.load(reports, "cimiss");
                        }
                    } catch (Exception e) {
                        buffer.append("入库 " + repTable + " 失败:" + message + "\n" +
                                "错误原因:" + e + "\n");
                    }
                    for (int i = 0; i < errorList.size(); i++) {
                        ReportError reportError = errorList.get(i);
                        reportError.getMessage();
                        buffer.append("解码失败:" + message + "\n" +
                                "错误原因:" + reportError.getMessage() + "\n" +
                                "行号:" + reportError.getPositionx() + "\n" +
                                "错误片段摘录:" + reportError.getSegment() + "\n");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("message", reportError.getMessage());
                        map.put("path", configPath);
                        List<RestfulInfo> info = DIEISender.makeEI(map);
                        eiInfo.addAll(info);
                    }

                    try {
                        diInfo.addAll(loadHor.getDiInfo());
                        eiInfo.addAll(loadHor.getEiInfo());
                        buffer.append(loadHor.getBuffer());

                        diInfo.addAll(loadMin.getDiInfo());
                        eiInfo.addAll(loadMin.getEiInfo());
                        buffer.append(loadMin.getBuffer());
                        
                        diInfo.addAll(loadMinPre.getDiInfo());
                        eiInfo.addAll(loadMinPre.getEiInfo());
                        buffer.append(loadMinPre.getBuffer());
                        
                        diInfo.addAll(loadCumPre.getDiInfo());
                        eiInfo.addAll(loadCumPre.getEiInfo());
                        buffer.append(loadCumPre.getBuffer());

                        diInfo.addAll(loadRep.getDiInfo());
                        eiInfo.addAll(loadRep.getEiInfo());
                        buffer.append(loadRep.getBuffer());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    action = Action.ACCEPT;
                } else {
                    //解码有失败
                    if (errorList != null && errorList.size() > 0) {
                        for (int i = 0; i < errorList.size(); i++) {
                            ReportError reportError = errorList.get(i);
                            reportError.getMessage();
                            buffer.append("解码失败:" + message + "\n" +
                                    "错误原因:" + reportError.getMessage() + "\n" +
                                    "行号:" + reportError.getPositionx() + "\n" +
                                    "错误片段摘录:" + reportError.getSegment() + "\n");
                            HashMap<String, String> map = new HashMap<>();
                            map.put("message", reportError.getMessage());
                            map.put("path", configPath);
                            List<RestfulInfo> info = DIEISender.makeEI(map);
                            eiInfo.addAll(info);
                        }
                        action = Action.REJECT;
                    } else {
                        int code = result.getParseInfo().getCode();
                        switch (code) {
                            case 1:
                                //非法格式
                                buffer.append("解码失败:" + message + "\n" +
                                        "错误原因:非法格式" + "\n");
                                HashMap<String, String> map = new HashMap<>();
                                map.put("message", "非法格式");
                                map.put("path", configPath);
                                List<RestfulInfo> info = DIEISender.makeEI(map);
                                eiInfo.addAll(info);
                                action = Action.REJECT;
                                return;
                            case 2:
                                //文件不存在，空文件
                                buffer.append("解码失败:" + message + "\n" +
                                        "错误原因:文件不存在或为空文件" + "\n");
//                                DIEISender.EI("文件不存在或为空文件", path);
                                HashMap<String, String> map1 = new HashMap<>();
                                map1.put("message", "文件不存在或为空文件");
                                map1.put("path", configPath);
                                List<RestfulInfo> info2 = DIEISender.makeEI(map1);
                                eiInfo.addAll(info2);
                                action = Action.REJECT;
                                return;
                        }
                    }

                }
            } catch (Exception e) {
                action = Action.REJECT;
                buffer.append("解码入库失败:" + message + "\n" +
                        "错误原因:" + e + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            action = Action.REJECT;
            buffer.append("rabbitmq消息错误:" + message + "\n" +
                    "错误原因:消息格式错误");
        } finally {
            if (action == Action.ACCEPT) {
                try {
//                    System.out.println("进入final====ACCEPT");
                    buffer.append("decode : num= " + horList.size() +
                            " ，insert " + horTable +
                            " : correct = " + loadHor.getCorrectNum() +
                            " error = " + loadHor.getErrorNum() + "\n");
                    buffer.append("decode : num= " + minList.size() +
                            " ，insert " + minTable +
                            " : correct = " + loadMin.getCorrectNum() +
                            " error = " + loadMin.getErrorNum() + "\n");
                    buffer.append("decode : num= " + minPreList.size() +
                            " ，insert " + minPreTable +
                            " : correct = " + loadMinPre.getCorrectNum() +
                            " error = " + loadMinPre.getErrorNum() + "\n");
                    buffer.append("decode : num= " + cumPreList.size() +
                            " ，insert " + cumPreTable +
                            " : correct = " + loadCumPre.getCorrectNum() +
                            " error = " + loadCumPre.getErrorNum() + "\n");
                    buffer.append("decode : num= " + reports.size() +
                            " ，insert " + repTable +
                            " : correct = " + loadRep.getCorrectNum() +
                            " error = " + loadRep.getErrorNum() + "\n");
                    buffer.append("INFO : " + new Date() + "end" + "\n");
                    LogUtil.info("loggerInfo", buffer.toString());
                    System.out.println(buffer.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (action == Action.RETRY) {
                try {
                    LogUtil.info("loggerInfo", buffer.toString());
                    System.out.println(buffer.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    buffer.append("消息错误 REJECT" + "\n");
                    buffer.append("INFO : " + new Date() + "end" + "\n");
                    LogUtil.info("loggerInfo", buffer.toString());
                    System.out.println(buffer.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
