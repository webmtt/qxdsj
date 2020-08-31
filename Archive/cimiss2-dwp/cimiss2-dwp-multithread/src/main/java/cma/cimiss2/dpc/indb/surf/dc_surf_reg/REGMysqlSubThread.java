package cma.cimiss2.dpc.indb.surf.dc_surf_reg;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataReg;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.indb.core.IocBuilder;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.etl.mysqlETL.SurfREGEtl;
import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.core.tools.LogUtil;
import cma.cimiss2.dpc.indb.surf.BaseThread;

public class REGMysqlSubThread extends BaseThread<SurfaceObservationDataReg>{

	private List<Map<String, Object>> horList;
	private List<Map<String, Object>> minList;
	private List<Map<String, Object>> minPreList;
	private List<Map<String, Object>> cumPreList;
	private StatInsert loadHor = null;
	private StatInsert loadMin = null;
	private StatInsert loadMinPre = null;
	private StatInsert loadCumPre = null;
	private StatInsert loadRep = null;
	
	private SurfREGEtl surfREGEtlMysql;
	
	public REGMysqlSubThread() {
		System.out.println("Init surfREGEtlMysql: -------------------");
		surfREGEtlMysql = IocBuilder.ioc(ConfigurationManager.getConfigPath()).get(SurfREGEtl.class);
		surfREGEtlMysql.setBatchSize(ConfigurationManager.getInteger("insert-batch-size"));
		surfREGEtlMysql.setDataSource(ConfigurationManager.getString("dataBaseType"));
		surfREGEtlMysql.setIntervalTime(ConfigurationManager.getLong("intervalTime"));
        DIEISender.LOCAL_DI_OPTION = ConfigurationManager.getInteger("di.option") == 1 ? true : false;
        DIEISender.LOCAL_EI_OPTION = ConfigurationManager.getInteger("ei.option") == 1 ? true : false;
//        TimeCheckUtil.timeCheckUtil.setAfter_day(ConfigurationManager.getInteger("D_DATETIME_AFTER_DAY"));
//        TimeCheckUtil.timeCheckUtil.setBefore_day(ConfigurationManager.getInteger("D_DATETIME_BEFORE_DAY"));
        System.out.println("Finish init surfREGEtlMysql: -------------------");
        run();
	}
	
	@Override
    public void formate(String message, StringBuffer buffer, List diInfo, List eiInfo) {
        LogUtil.info("messageInfo",message);
        System.out.println("msg: " + message);
        File file = new File(message.substring(message.indexOf(":") + 1));
        ParseResult<SurfaceObservationDataReg> result = surfREGEtlMysql.extract(file);
        surfREGEtlMysql.setEvent(message.split(":")[0]);
        surfREGEtlMysql.setFile(file);
        setFile(file);
        setResult(result);
        setErrorList(result.getError());
    }

	@Override
	public void write(String message, StringBuffer buffer, List diInfo, List eiInfo) {
		// 2020-4-7 chy 只入两个表
		StatTF statREG = surfREGEtlMysql.transform(result, file, codeMap, horTable, minTable, minPreTable, cumPreTable, repTable);
//		StatTF statREG = surfREGEtlMysql.transform(result, file, codeMap, horTable, repTable);
		
		horList = statREG.getHorList();
		minList = statREG.getMinList();
		minPreList = statREG.getMinPreList();
		cumPreList = statREG.getCumPreList();
		reports = statREG.getRepList();
		buffer.append(statREG.getBuffer().toString());
		
		
		loadHor = surfREGEtlMysql.load(horList, "rdb");
		loadMin = surfREGEtlMysql.load(minList, "rdb");
		loadMinPre = surfREGEtlMysql.load(minPreList, "rdb");
		loadCumPre = surfREGEtlMysql.load(cumPreList, "rdb");
		loadRep = surfREGEtlMysql.load(reports, "cimiss");
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
