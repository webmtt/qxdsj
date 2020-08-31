package cma.cimiss2.dpc.decoder.sevp;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;



public class ForecastUtil {
	/**
	 * @Title: time_transform
	 * @Description: TODO(时间转换函数)
	 * @param:  yygggg   YYGGgg
	 * @param:  rep_forcast_time  预报时间
	 * @return: List<Date> 资料时间和观测时间的集合
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public static List<Date> time_transform(String yygggg, String rep_forcast_time)  {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHH");
		String V04001=null;
		String V04002=null;
		String V04003=null;
		String V04004=null;
		String V04005=null;
		String  V04001_02=null;
		String  V04002_02=null;
		String  V04003_02 = null;
		String  V04004_02=null;
		java.util.Date forcast_time = null;
		java.util.Date observetime=null;
		V04003=yygggg.substring(0, 2);
		V04004=yygggg.substring(2, 4);
		V04005=yygggg.substring(4, 6);
		V04001_02 =rep_forcast_time.substring(0, 4);
	    V04002_02 =rep_forcast_time.substring(4, 6);
		V04003_02 =rep_forcast_time.substring(6, 8);
		V04004_02 =rep_forcast_time.substring(8, 10);
		if((Integer.parseInt(V04003_02)-Integer.parseInt(V04003))<0){
			if (Integer.parseInt(V04002_02)-1 <= 0) {
				V04002=String.valueOf(12);
				V04001=String.valueOf(Integer.parseInt(V04001_02)-1);	
			} else {
				V04002=String.valueOf(Integer.parseInt(V04002_02)-1);
				V04001=V04001_02;
			}		
		}else{
			V04002=V04002_02;
			V04001=V04001_02;
		}
		if (V04002.length()==1) {
			V04002=0+""+V04002;
			
		}
		try {
			forcast_time= sdf1.parse(V04001_02+V04002_02+V04003_02+V04004_02);
			observetime= sdf.parse(V04001+V04002+V04003+V04004+V04005);
		} catch (Exception e) {
			return null;		
		}
		

		@SuppressWarnings("rawtypes")
		List list = new ArrayList();
		list.add(forcast_time);
		list.add(observetime);	
		return list;	
	
	}
	
	/**
	 * 
	 * @Title: ToBeValidDouble
	 * @Description: TODO(缺测转换函数)
	 * @param: @param str
	 * @param: @return
	 * @return: double
	 * @throws
	 */
	public static double ToBeValidDouble(String str){
		if (Double.parseDouble(str)==999.9 || Double.parseDouble(str)==9999.0 ) {
			return 999999 ;
		}
		else {
			return Double.parseDouble(str);
		}
			
		
	}
	
	/**
	 * 
	 * @Title: savereport
	 * @Description: TODO(保存报文内容函数)
	 * @param: @param i
	 * @param: @param j
	 * @param: @param reports
	 * @param: @return
	 * @return: String 报文内容 list
	 * @throws
	 */
	public static String savereport(int i,int j,String[] reports){
		String head = reports[i];
		String list = head;
		for(int a=1;a<=j;a++){
		    list = list +"\n"+ reports[i+a];
		}
		return list;
	}
	

}
