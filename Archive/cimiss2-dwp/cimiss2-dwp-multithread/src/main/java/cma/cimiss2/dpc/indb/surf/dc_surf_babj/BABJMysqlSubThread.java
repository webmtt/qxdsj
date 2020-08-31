package cma.cimiss2.dpc.indb.surf.dc_surf_babj;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.indb.core.IocBuilder;
import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.etl.mysqlETL.SurfBABJEtl_new;
import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.core.tools.LogUtil;
import cma.cimiss2.dpc.indb.surf.BaseThread;

public class BABJMysqlSubThread extends BaseThread<SurfaceObservationDataNation> {

	private List<Map<String, Object>> horList;
	private List<Map<String, Object>> minList;
	private List<Map<String, Object>> minPreList;
	private List<Map<String, Object>> cumPreList;
	// 2020-3-23 chy
	private List<Map<String, Object>> glbList;
	// 2020-3-31 chy
	private List<Map<String, Object>> weatherList;
	
	private StatInsert loadHor = null;
	private StatInsert loadMin = null;
	private StatInsert loadMinPre = null;
	private StatInsert loadCumPre = null;
	private StatInsert loadRep = null;
    //2020-03-23 chy
	private StatInsert loadGlb = null;
	//2020-3-31 chy
	private StatInsert loadWeather = null;
	
    private SurfBABJEtl_new surfBABJEtl;
    
    public BABJMysqlSubThread() {
    	surfBABJEtl = IocBuilder.ioc(ConfigurationManager.getConfigPath()).get(SurfBABJEtl_new.class);
    	surfBABJEtl.setBatchSize(ConfigurationManager.getInteger("insert-batch-size"));
    	surfBABJEtl.setDataSource(ConfigurationManager.getString("dataBaseType"));
    	surfBABJEtl.setIntervalTime(ConfigurationManager.getLong("intervalTime"));
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
        ParseResult<SurfaceObservationDataNation> result = surfBABJEtl.extract(file);
        surfBABJEtl.setEvent(message.split(":")[0]);
        surfBABJEtl.setFile(file);
        setFile(file);
        setResult(result);
        setErrorList(result.getError());
    }
	
	@Override
	public void write(String message, StringBuffer buffer, List diInfo, List eiInfo) {
		StatTF statBABJ = surfBABJEtl.transform(result, file, codeMap, horTable, minTable, minPreTable, cumPreTable, repTable,glbTable,weatherTable);
		horList = statBABJ.getHorList();
		minList = statBABJ.getMinList();
		minPreList = statBABJ.getMinPreList();
		cumPreList = statBABJ.getCumPreList();
		reports = statBABJ.getRepList();
	    // 2020-3-23
		glbList = statBABJ.getGlbHorList();
		//2020-3-31 chy
		weatherList = statBABJ.getWeatherList();
		
		buffer.append(statBABJ.getBuffer().toString());
		
		
		loadHor = surfBABJEtl.load(horList, "rdb");
		loadMin = surfBABJEtl.load(minList, "rdb");
		loadMinPre = surfBABJEtl.load(minPreList, "rdb");
		loadCumPre = surfBABJEtl.load(cumPreList, "rdb");
		//2020-3-23 chy
		loadGlb = surfBABJEtl.load(glbList, "rdb");
		//2020-3-31 chy
		loadWeather = surfBABJEtl.load(weatherList, "rdb");
		
		loadRep = surfBABJEtl.load(reports, "cimiss");
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
			// 2020-3-23 chy
			diInfo.addAll(loadGlb.getDiInfo());
			eiInfo.addAll(loadRep.getEiInfo());
			buffer.append(loadGlb.getBuffer());
			
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
            buffer.append("decode : num=" + reports.size() + 
            		", insert " +glbTable + 
            		" : correct " + loadGlb.getCorrectNum() + 
            		" error = " + loadGlb.getErrorNum() + "\n");
            buffer.append("decode : num=" + weatherList.size() + 
            		", insert " +weatherTable + 
            		" : correct " + loadWeather.getCorrectNum() + 
            		" error = " + loadWeather.getErrorNum() + "\n");
            
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
