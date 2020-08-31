package cma.cimiss2.dpc.indb.surf.dc_surf_day;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataDay;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.indb.core.bean.StatFormate;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.etl.otsETL.DayValueEtl;
import cma.cimiss2.dpc.indb.core.ots.OTS;
import cma.cimiss2.dpc.indb.core.ots.OTSHelper;
import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.core.tools.LogUtil;
import cma.cimiss2.dpc.indb.surf.BaseThreadOTS;
import com.alicloud.openservices.tablestore.AsyncClient;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by xzh on 2017/11/01.
 */

public class DayValueOTSSubThread extends BaseThreadOTS<SurfaceObservationDataDay> {


    private DayValueEtl dayValueEtl;
    protected AsyncClient client = OTS.getClient();

    public DayValueOTSSubThread() {
        this.dayValueEtl = new DayValueEtl();
        DIEISender.LOCAL_DI_OPTION = ConfigurationManager.getInteger("di.option") == 1 ? true : false;
        DIEISender.LOCAL_EI_OPTION = ConfigurationManager.getInteger("ei.option") == 1 ? true : false;
    }
    @Override
    public StatFormate<SurfaceObservationDataDay> formate(String message, StringBuffer buffer, List<RestfulInfo> diInfo, List<RestfulInfo> eiInfo) {
        LogUtil.info("messageInfo", message);
        //File file = new File(message.split(":")[1]);
        File file = new File(message.substring(message.indexOf(":") + 1));
        ParseResult<SurfaceObservationDataDay> result = dayValueEtl.extract(file);
        dayValueEtl.setEvent(message.split(":")[0]);
        dayValueEtl.setFile(file);
        StatFormate<SurfaceObservationDataDay> reFor = new StatFormate<>();
        reFor.setResult(result);
        reFor.setFile(file);
        return reFor;
    }

    @Override
    public HashMap<String, Integer> write(String message, ParseResult<SurfaceObservationDataDay> result, File file, StringBuffer buffer, List<RestfulInfo> diInfo, List<RestfulInfo> eiInfo) {
        OTSHelper dataTableHelper = new OTSHelper(client, dataTable);
        OTSHelper repTableHelper = new OTSHelper(client, repTable);
        StatTF statTF = dayValueEtl.transform(result, file, dataTableHelper, repTableHelper);
        List<Map<String, Object>> dataList = statTF.getMinList();//"dataList"
        List<Map<String, Object>> dataDayList = statTF.getHorList();//"dataDayList"
        List<Map<String, Object>> reports = statTF.getRepList();
        buffer.append(statTF.getBuffer().toString());
        StatInsert loadData = null;
        StatInsert loadRep = null;
        loadData = dayValueEtl.load(dataList, dataTableHelper, true);
        loadRep = dayValueEtl.load(reports, repTableHelper, true);
        diInfo.addAll(loadData.getDiInfo());
        eiInfo.addAll(loadData.getEiInfo());
        int dataCorrectNum = loadData.getCorrectNum();
        int dataErrorNum = loadData.getErrorNum();
        buffer.append(loadData.getBuffer());

        diInfo.addAll(loadRep.getDiInfo());
        eiInfo.addAll(loadRep.getEiInfo());
        int repCorrectNum = loadRep.getCorrectNum();
        int repErrorNum = loadRep.getErrorNum();
        buffer.append(loadRep.getBuffer());
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("datasize", dataList.size());
        map.put("dataCorrectNum", dataCorrectNum);
        map.put("dataErrorNum", dataErrorNum);
        map.put("reportsize", reports.size());
        map.put("repCorrectNum", repCorrectNum);
        map.put("repErrorNum", repErrorNum);

        return map;
    }

    @Override
    public void finallyAccept(HashMap<String, Integer> map, StringBuffer buffer) {
        try {
            System.out.println("进入final====ACCEPT " + Thread.currentThread().getName());
            buffer.append("decode : num= " + map.get("datasize") +
                    " ，insert " + dataTable +
                    " : correct = " + map.get("dataCorrectNum") +
                    " error = " + map.get("dataErrorNum") + "\n");
            buffer.append("decode : num= " + map.get("reportsize") +
                    " ，insert " + repTable +
                    " : correct = " + map.get("repCorrectNum") +
                    " error = " + map.get("repErrorNum") + "\n");
            buffer.append("INFO : " + new Date() + "end" + "\n");
            System.out.println(buffer.toString());
            LogUtil.info("loggerInfo", buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finallyReject(StringBuffer buffer) {
        LogUtil.info("loggerInfo", buffer.toString());
    }

}

