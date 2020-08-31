package cma.cimiss2.dpc.indb.surf.dc_surf_perf;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.PrecipitationObservationDataReg;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.indb.core.IocBuilder;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.etl.mysqlETL.SurfPERFEtl;
import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.core.tools.LogUtil;
import cma.cimiss2.dpc.indb.surf.BaseThread;

public class PERFMysqlSubThread extends BaseThread<PrecipitationObservationDataReg>{

	private List<Map<String, Object>> horList;
	private List<Map<String, Object>> minList;
	private List<Map<String, Object>> minPreList;
	private List<Map<String, Object>> cumPreList;
	private StatInsert loadHor = null;
	private StatInsert loadMin = null;
	private StatInsert loadMinPre = null;
	private StatInsert loadCumPre = null;
	private StatInsert loadRep = null;
	
	private SurfPERFEtl surfPERFEtlMysql;
	
	public PERFMysqlSubThread() {
		surfPERFEtlMysql = IocBuilder.ioc(ConfigurationManager.getConfigPath()).get(SurfPERFEtl.class);
		surfPERFEtlMysql.setBatchSize(ConfigurationManager.getInteger("insert-batch-size"));
		surfPERFEtlMysql.setDataSource(ConfigurationManager.getString("dataBaseType"));
		surfPERFEtlMysql.setIntervalTime(ConfigurationManager.getLong("intervalTime"));
        DIEISender.LOCAL_DI_OPTION = ConfigurationManager.getInteger("di.option") == 1 ? true : false;
        DIEISender.LOCAL_EI_OPTION = ConfigurationManager.getInteger("ei.option") == 1 ? true : false;
//        TimeCheckUtil.timeCheckUtil.setAfter_day(ConfigurationManager.getInteger("D_DATETIME_AFTER_DAY"));
//        TimeCheckUtil.timeCheckUtil.setBefore_day(ConfigurationManager.getInteger("D_DATETIME_BEFORE_DAY"));
        run();
    }
	
	@Override
    public void formate(String message, StringBuffer buffer, List diInfo, List eiInfo) {
        LogUtil.info("messageInfo",message);
        File file = new File(message.substring(message.indexOf(":") + 1));
        ParseResult<PrecipitationObservationDataReg> result = surfPERFEtlMysql.extract(file);
        surfPERFEtlMysql.setEvent(message.split(":")[0]);
        surfPERFEtlMysql.setFile(file);
        setFile(file);
        setResult(result);
        setErrorList(result.getError());
    }
	
	@Override
	public void write(String message, StringBuffer buffer, List diInfo, List eiInfo) {
		StatTF statPERF = surfPERFEtlMysql.transform(result, file, codeMap, horTable, minTable, minPreTable, cumPreTable, repTable);
		horList = statPERF.getHorList();
		minList = statPERF.getMinList();
		minPreList = statPERF.getMinPreList();
		cumPreList = statPERF.getCumPreList();
		reports = statPERF.getRepList();
		buffer.append(statPERF.getBuffer().toString());
		
		
		loadHor = surfPERFEtlMysql.load(horList, "rdb");
		loadMin = surfPERFEtlMysql.load(minList, "rdb");
		loadMinPre = surfPERFEtlMysql.load(minPreList, "rdb");
		loadCumPre = surfPERFEtlMysql.load(cumPreList, "rdb");
		loadRep = surfPERFEtlMysql.load(reports, "cimiss");
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
	}

	@Override
    public void finallyAccept(StringBuffer buffer) {
        try {
            System.out.println("进入final====ACCEPT");
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
            //System.out.println(buffer.toString());
            LogUtil.info("loggerInfo",buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public void finallyReject(StringBuffer buffer) {
		// TODO Auto-generated method stub
		LogUtil.info("loggerInfo",buffer.toString());
	}

}
