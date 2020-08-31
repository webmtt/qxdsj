package cma.cimiss2.dpc.indb.core.etl.mysqlETL;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.detection.RadiMulChnPenTab;
import cma.cimiss2.dpc.decoder.detection.DecodeP;
import cma.cimiss2.dpc.indb.core.bean.StatTF;


/**
 * 地面要素MPR检验评估结果类 mysql缓冲库
 */
@IocBean(fields = {"rdb", "cimiss"},singleton = false)
public class PenEtl extends AbstractEtlNew<RadiMulChnPenTab> {

    private static final long serialVersionUID = -7535794683834971193L;
    static Log log = Logs.get();


    public PenEtl() {
    }

    @Override
    public ParseResult<RadiMulChnPenTab> extract(File file) {
        DecodeP decodeSS = new DecodeP();
        String path = file.getPath();
        ParseResult<RadiMulChnPenTab> decode = (ParseResult<RadiMulChnPenTab>) decodeSS.decode(file, new HashSet<String>());
        return decode;
    }
    /**
     * 报文格式化
     * @param result   报文集合
     * @param file      报文文件
     * @param tables    表名
     * @return  格式化后的实体类
     */
    @Override
    public StatTF transform(ParseResult<RadiMulChnPenTab> result, File file, String... tables) {
//        reload(intervalTime);
        //返回集合
        Map<String, Object> allMap = new HashMap<>();
        //解码数据集合
        List<RadiMulChnPenTab> beans = result.getData();
        //报文集合
        List<ReportInfo> reports = result.getReports();

        //数据表
        String dataTable = tables[0];
        //日值表
//        String databeanTable = tables[1];
        //报文表
//        String repTable = tables[2];

        StatTF statTF = new StatTF();

        String D_DATA_ID = "F.0052.0003.S001";

        //数据处理
        for (int i = 0; i < beans.size(); i++) {
            RadiMulChnPenTab bean = beans.get(i);
            Date observationTime = bean.getoEndTime();
            String format = new SimpleDateFormat("yyyyMMdd").format(observationTime);
            String stationNumberChina = bean.getStationNumberC().toUpperCase();
            String path = file.getPath();
            String ss =path.substring(0, path.lastIndexOf(file.separator));
            String sss = ss.substring(0, ss.lastIndexOf(file.separator));
            String ssss = sss.substring(sss.lastIndexOf(file.separator)+1, sss.length());
            int a = path.lastIndexOf(file.separator)-10;
            String time  = path.substring(ss.lastIndexOf(file.separator)+1,path.lastIndexOf(file.separator));
			if(time.length()!=10||time.contains("-")){
				time = time.substring(0,10);
			}
			Long tm = Long.valueOf(time);

            String D_RECORD_ID = time + "000000_" + stationNumberChina + "_"+ ssss+ "_"+bean.getFcstTime()+"_"+bean.getFcstVar()+"_"+bean.getVxMask()+"_" +bean.getIndex();
            
            int num = stationNumberChina.substring(0, 1).toUpperCase().hashCode();

            
            Map<String, Object> data = new HashMap<String, Object>();
//            try {
//                data = DataMapMaker.make(bean, 0);
//            } catch (IllegalArgumentException | IllegalAccessException e) {
//                log.debug("SurfWeaChnSsdHor is a illegal bean.");
//                return null;
//            }
            data.put(".table", dataTable);
            
			data.put("D_RECORD_ID", D_RECORD_ID);
			data.put("D_DATA_ID", D_DATA_ID);//资料标识
			data.put("D_IYMDHM", new Date());//入库时间
			data.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
			data.put("D_UPDATE_TIME",new Date());//更新时间
			
			data.put("V01301", bean.getStationNumberC());
//			data.put("V01300", bean.getStationNumberC());
			if (num >= 48 & num <= 57) {
                data.put("V01300", stationNumberChina);
            } else {
                data.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
            }
			
			data.put("V05001", bean.getLatitude());
			data.put("V06001", bean.getLongitude());
			data.put("V07001", bean.getElevationAltitude());
			
			data.put("V_VERSION", bean.getVersion());
//			data.put("V_MODEL", bean.getModel());
			data.put("V_MODEL", ssss);
			data.put("V_FCSTTIME", bean.getFcstTime());
			data.put("V_FBEGTIME", bean.getfBegTime());
			data.put("V_FENDTIME", bean.getfEndTime());
			data.put("V_OBSTIME", bean.getObsTime());
			data.put("V_OBEGTIME", bean.getoBegTime());
			data.put("V_OENDTIME", bean.getoEndTime());
			data.put("V_FCSTVAR", bean.getFcstVar());
			data.put("V_FCSTLEV", bean.getFcstLev());
			data.put("V_OBSVAR", bean.getObsVar());
			data.put("V_OBSLEV", bean.getObsLev());
			data.put("V_OBTYPE", bean.getObtype());
			data.put("V_VXMASK", bean.getVxMask());
			data.put("V_MTHD", bean.getMthd());
			data.put("V_PNTS", bean.getPnts());
			data.put("V_FTHRESH", bean.getfThresh());
			data.put("V_OTHRESH", bean.getoThresh());
			data.put("V_CTHRESH", bean.getcThresh());
			data.put("V_ALPHA", bean.getAlpha());
			data.put("V_LINETYPE", bean.getLineType());
			data.put("V_TOTAL", bean.getTotal());
			data.put("V_INDEX", bean.getIndex());
			data.put("V_OBSLVL", bean.getObsLvl());
			data.put("V_FCST", bean.getFcst());
			data.put("V_OBS", bean.getObs());
            
//			int a = path.lastIndexOf(file.separator)-10;
//			String time  = path.substring(a,path.lastIndexOf(file.separator));
//			Long tm = Long.valueOf(time);					
			data.put("V_BEGTIME",tm);//资料起报时间
            data.put("V_ENDTIME",tm);//资料起报日期
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHH");
            try {
				data.put("D_DATETIME",sdf2.parse(time)); //资料时间
			} catch (ParseException e) {
				e.printStackTrace();
			}
            data.put("V04001", Long.valueOf(time.substring(0,4)));
            data.put("V04002", Long.valueOf(time.substring(4,6)));
            data.put("V04003", Long.valueOf(time.substring(6,8)));
            data.put("V04004", Long.valueOf(time.substring(8,10)));
            
            statTF.getMinList().add(data);
            statTF.getBuffer().append(stationNumberChina + "  " + time + "  " + "000" + "\n");

        }
//        try {
//            statTF.setRepList(transformReports(reports, D_DATA_ID, file, repTable));
//        } catch (ParseException e) {
//            statTF.getBuffer().append("reports inssrt error!");
//        }
        return statTF;
    }

}