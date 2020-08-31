package cma.cimiss2.dpc.indb.surf.dc_surf_suns;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.SurfWeaChnSsdHor;
import cma.cimiss2.dpc.indb.core.IocBuilder;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.etl.mysqlETL.DayLightEtl;
import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.core.tools.LogUtil;
import cma.cimiss2.dpc.indb.surf.BaseThread;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by xzh on 2017/11/01.
 */

public class DayLightMysqlSubThread extends BaseThread {


    private List<Map<String, Object>> dataList;
    private List<Map<String, Object>> dataDayList;
    private int dataCorrectNum;
    private int dataErrorNum;
    //private String dataSource;
    //private String dataTable;
    //private String dataDayTable;
    //private String repTable;

    private DayLightEtl dayLightEtl;

    public DayLightMysqlSubThread() {
        dayLightEtl = IocBuilder.ioc(ConfigurationManager.getConfigPath()).get(DayLightEtl.class);
        dayLightEtl.setBatchSize(ConfigurationManager.getInteger("insert-batch-size"));
        dayLightEtl.setDataSource(ConfigurationManager.getString("dataBaseType"));
        dayLightEtl.setIntervalTime(ConfigurationManager.getLong("intervalTime"));
        dayLightEtl.setQC(ConfigurationManager.getString("qc_flag"));
        DIEISender.LOCAL_DI_OPTION = ConfigurationManager.getInteger("di.option") == 1 ? true : false;
        DIEISender.LOCAL_EI_OPTION = ConfigurationManager.getInteger("ei.option") == 1 ? true : false;
    }

    @Override
    public void formate(String message, StringBuffer buffer, List diInfo, List eiInfo) {
        LogUtil.info("messageInfo",message);
        //File file = new File(message.split(":")[1]);
        File file = new File(message.substring(message.indexOf(":") + 1));
        ParseResult<SurfWeaChnSsdHor> result = dayLightEtl.extract(file);
        dayLightEtl.setEvent(message.split(":")[0]);
        dayLightEtl.setFile(file);
        setFile(file);
        setResult(result);
        setErrorList(result.getError());
    }

    @Override
    public void write(String message, StringBuffer buffer, List diInfo, List eiInfo) {
        StatTF statTF = dayLightEtl.transform(result, file, codeMap, dataTable, dataDayTable, repTable);
        dataList = statTF.getMinList();//"dataList"
        dataDayList = statTF.getHorList();//"dataDayList"
        reports = statTF.getRepList();
        buffer.append(statTF.getBuffer().toString());
        StatInsert loadData = null;
        StatInsert loadRep = null;
        loadData = dayLightEtl.load(dataList, "rdb");
        loadRep = dayLightEtl.load(reports, "cimiss");
        if ("0".equals(dataSource)) {
            dayLightEtl.loadAndUpdateOrInsert_ali(dataDayList);
        } else if ("1".equals(dataSource)) {
            dayLightEtl.loadAndUpdateOrInsert(dataDayList);
        }
        try {
            diInfo.addAll(loadData.getDiInfo());
            eiInfo.addAll(loadData.getEiInfo());
            dataCorrectNum = loadData.getCorrectNum();
            dataErrorNum = loadData.getErrorNum();
            buffer.append(loadData.getBuffer());

            diInfo.addAll(loadRep.getDiInfo());
            eiInfo.addAll(loadRep.getEiInfo());
            repCorrectNum = loadRep.getCorrectNum();
            repErrorNum = loadRep.getErrorNum();
            buffer.append(loadRep.getBuffer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void finallyAccept(StringBuffer buffer) {
        try {
            System.out.println("进入final====ACCEPT");
            buffer.append("decode : num= " + dataList.size() +
                    " ，insert " + dataTable +
                    " : correct = " + dataCorrectNum +
                    " error = " + dataErrorNum + "\n");
            buffer.append("decode : num= " + reports.size() +
                    " ，insert " + repTable +
                    " : correct = " + repCorrectNum +
                    " error = " + repErrorNum + "\n");
            buffer.append("INFO : " + new Date() + "end" + "\n");
            System.out.println(buffer.toString());
            LogUtil.info("loggerInfo",buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finallyReject(StringBuffer buffer) {
        LogUtil.info("loggerInfo",buffer.toString());
    }
}

