package cma.cimiss2.dpc.indb.core.etl.mysqlETL;


import java.io.File;
import java.nio.file.Path;
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
import cma.cimiss2.dpc.decoder.bean.detection.RadiMulChnMonTab;
import cma.cimiss2.dpc.decoder.detection.DecodeM;
import cma.cimiss2.dpc.indb.core.bean.StatTF;


/**
 * 降水CTC检验评估结果类 mysql缓冲库
 */
@IocBean(fields = {"rdb", "cimiss"},singleton = false)
public class MonEtl extends AbstractEtlNew<RadiMulChnMonTab> {

    private static final long serialVersionUID = -7535794683834971193L;
    static Log log = Logs.get();


    public MonEtl() {
    }

    @Override
    public ParseResult<RadiMulChnMonTab> extract(File file) {
        DecodeM decodeSS = new DecodeM();
        String path = file.getPath();
        ParseResult<RadiMulChnMonTab> decode = (ParseResult<RadiMulChnMonTab>) decodeSS.decode(file, new HashSet<String>());
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
    public StatTF transform(ParseResult<RadiMulChnMonTab> result, File file, String... tables) {
//        reload(intervalTime);
        //返回集合
        Map<String, Object> allMap = new HashMap<>();
        //解码数据集合
        List<RadiMulChnMonTab> beans = result.getData();
        //报文集合
        List<ReportInfo> reports = result.getReports();

        //数据表
        String dataTable = tables[0];
        //日值表
//        String databeanTable = tables[1];
        //报文表
//        String repTable = tables[2];

        StatTF statTF = new StatTF();

        String D_DATA_ID = "F.0052.0002.S001";
//        String D_DATA_ID = "F.0052.0006.S001";

        //数据处理
        for (int i = 0; i < beans.size(); i++) {
            RadiMulChnMonTab bean = beans.get(i);
            Date observationTime = bean.getoEndTime();
            String format = new SimpleDateFormat("yyyyMMdd").format(observationTime);
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
            
            String D_RECORD_ID = time + "000000_" + ssss+"_" +bean.getFcstTime()+"_"+bean.getFcstVar()+"_"+bean.getVxMask()+"_"+bean.getPnts()+"_"+ bean.getfThresh();

            
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
			data.put("V_FYOY", bean.getFyOy());
			data.put("V_FYON", bean.getFyOn());
			data.put("V_FNOY", bean.getFnOy());
			data.put("V_FNON", bean.getFnOn());
			
			
			Double ab = bean.getFyOy()+bean.getFyOn();
			Double ac = bean.getFyOy() + bean.getFnOy();
			Double ad = bean.getFyOy() + bean.getFnOn();
			Double abc = bean.getFyOy()+bean.getFyOn()+bean.getFnOy();
			Double abcd = bean.getFyOy()+ bean.getFyOn()+ bean.getFnOy() + bean.getFnOn();
			
			Double r = ab*ac/abcd;
			Double ar = bean.getFyOy()-r;
			Double abcr = abc -r;
			
			Double ts = bean.getFyOy()/abc;  //TS评分
			if(bean.getFyOy()==0.0&&abc==0.0){
				ts=0.0;
			}else if(bean.getFyOy()!=0.0&&abc==0.0){
				ts = 999999.0;
			}
            
			Double ets = ar/abcr;   //ETS评分
			if(ar==0.0&&abcr==0.0){
				ets=0.0;
			}else if(ar!=0.0&&abcr==0.0){
				ets = 999999.0;
			}
			
			Double accuracy = ad/abcd; //准确率
			if(ad==0.0&&abcd==0.0){
				accuracy=0.0;
			}else if(ad!=0.0&&abcd==0.0){
				accuracy = 999999.0;
			}
			
			Double miss = bean.getFnOy()/ac;  //漏报率
			if(bean.getFnOy()==0.0&&ac==0.0){
				miss=0.0;
			}else if(bean.getFnOy()!=0.0&&ac==0.0){
				miss = 999999.0;
			}
			
			Double blank = bean.getFyOn()/ab; //空报率
			if(bean.getFyOn()==0.0&&ab==0.0){
				blank=0.0;
			}else if(bean.getFyOn()!=0.0&&ab==0.0){
				blank = 999999.0;
			}
			
			
			Double deviation = ab/ac ; //预报偏差
			if(ab==0.0&&ac==0.0){
				deviation=0.0;
			}else if(ab!=0.0&&ac==0.0){
				deviation = 999999.0;
			}
			
			data.put("V_TS",ts);
			data.put("V_ETS", ets);
			data.put("V_ACCURACY", accuracy);
			data.put("V_MISS", miss);
			data.put("V_BLANK", blank);
			data.put("V_DEVIATION", deviation);
			
//			Double nbr = (Math.sqrt(bean.getPnts())+1)*5/2;
//			data.put("V_NBR",nbr);
			
			
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
            statTF.getBuffer().append(time + "  " + "000" + "\n");

        }
//        try {
//            statTF.setRepList(transformReports(reports, D_DATA_ID, file, repTable));
//        } catch (ParseException e) {
//            statTF.getBuffer().append("reports inssrt error!");
//        }
        return statTF;
    }

}
