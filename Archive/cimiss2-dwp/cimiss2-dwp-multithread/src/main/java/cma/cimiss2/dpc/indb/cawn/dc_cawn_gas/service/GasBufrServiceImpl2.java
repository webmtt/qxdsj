package cma.cimiss2.dpc.indb.cawn.dc_cawn_gas.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrSurfCawnDecoder;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;

// TODO: Auto-generated Javadoc
/**
 * The Class BufrServiceImpl.
 */
public class GasBufrServiceImpl2 implements BufrService {
	/** The pro map. */
	static Map<String, Object> proMap = StationInfo.getProMap();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/** The Constant COLUMN_EXP_REGEX. */
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	
	/** The Constant THIS_VALUE_EXP. */
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The di. */
	private static List<StatDi> DI = new ArrayList<StatDi>();
	
	/** The ymdhm. */
	SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat ymdhm1 = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		List<String> tableList = new ArrayList<String>();
		String tableSection = "G.0016.0002.R001";
		tableList.add("CAWN_CHN_PMM_BUFR_TAB");
		String fileName = "C:\\BaiduNetdiskDownload\\test\\gastest\\G.0016.0002.R001\\Z_CAWN_I_54594_20191101140000_O_FLD.bin";
		GasBufrServiceImpl2 bufrServiceImpl = new GasBufrServiceImpl2();
		boolean decode = bufrServiceImpl.decode(fileName, tableSection, tableList);
		if(decode){
			System.out.println("入库成功！");
		}else
			System.err.println("入库失败！");
//		bufrDecoder.bufrDecoder("D:\\A_ISAC01VHHH200300_C_BABJ_20180920031410_90199.bin");
	}

	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	
	/**
	 * Sets the di queues.
	 *
	 * @param diQueue the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueue){
		GasBufrServiceImpl2.diQueues = diQueue;
	}
	
	/* (non-Javadoc)
	 * @see cma.cimiss2.dpc.indb.surf.dc_surf_global_h_bufr.service.BufrService#decode(java.lang.String, byte[], java.lang.String, java.util.List)
	 */
	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			BufrSurfCawnDecoder bufrDecoder = new BufrSurfCawnDecoder();
			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
			if (isRead) {
				return this.decode(bufrDecoder, name, tableSection, tables);
			} else {
				infoLogger.error("\n Read file error: "+ name);
				String event_type = EIEventType.OP_FILE_ERROR.getCode();
				EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
				if(ei == null) {
					infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
				}else {
					if(StartConfig.isSendEi()) {
						ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
						ei.setKObject("cma.cimiss2.dpc.indb.surf.bufr.service.BufrServiceImpl");
						ei.setKEvent("解码入库源文件异常：" + name);
						ei.setKIndex("详细信息：" + name);
						ei.setEVENT_EXT1(name);
						RestfulInfo restfulInfo = new RestfulInfo();
						restfulInfo.setType("SYSTEM.ALARM.EI ");
						restfulInfo.setName("数据解码入库EI告警信息");
						restfulInfo.setMessage("数据解码入库EI告警信息");
						
						restfulInfo.setFields(ei);
						List<RestfulInfo> restfulInfos = new ArrayList<>();
						restfulInfos.add(restfulInfo);
						RestfulSendData.SendData(restfulInfos, SendType.EI);
					}
				}
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see cma.cimiss2.dpc.indb.surf.dc_surf_global_h_bufr.service.BufrService#decode(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {
//		System.out.println("imp2,start dpc!!!");
		BufrSurfCawnDecoder bufrDecoder = new BufrSurfCawnDecoder();
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		if (isRead) {
			ReportInfoToDb(bufrDecoder, fileName);
//			return true;
			return this.decode(bufrDecoder, fileName, tableSection, tables);
		} else {
			infoLogger.error("\n Read file error: "+ fileName);
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.surf.bufr.service.BufrServiceImpl");
					ei.setKEvent("解码入库源文件异常：" + fileName);
					ei.setKIndex("详细信息：" + fileName);
					ei.setEVENT_EXT1(fileName);
					RestfulInfo restfulInfo = new RestfulInfo();
					restfulInfo.setType("SYSTEM.ALARM.EI ");
					restfulInfo.setName("数据解码入库EI告警信息");
					restfulInfo.setMessage("数据解码入库EI告警信息");
					
					restfulInfo.setFields(ei);
					List<RestfulInfo> restfulInfos = new ArrayList<>();
					restfulInfos.add(restfulInfo);
					RestfulSendData.SendData(restfulInfos, SendType.EI);
				}
			}
			return false;
		}
	}

	 public  void ReportInfoToDb(BufrSurfCawnDecoder bufrDecoder0, String fileName) {
		 	int successOrNot=bufrDecoder0.successOrNot;
			DruidPooledConnection report_connection = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String entrance=bufrDecoder0.entrance;
			File file = new File(fileName);
			String filename = file.getName();
			Date recv = new Date(file.lastModified());
			String ReportTableName="CAWN_WEA_GLB_BULL_BUFR_TAB";
//			String ReportTableName=StartConfig.reportTable();//"SURF_WEA_GLB_BULL_BUFR_TAB";
			String sql="insert into "+ReportTableName+"(D_RECORD_ID,D_DATA_ID,D_SOURCE_id,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
					+ "V01301,V_BBB,V_CCCC,V_YYGGgg,V_TT,V_LEN,V_FILE_SIZE,V_BULLETIN_B,V_FILE_NAME,V_NUM,V01301_list,d_datetime_list,"
					+ "V_decode_flag) values ("
					+ "?,?,?,?,?,"
					+ "?,?,?,?,?,"
					+ "?,?,?,?,?,"
					+ "?,?,?,?,?)";
			
			StringBuffer station_s=new StringBuffer();//存站号list集合
			StringBuffer datetime_s=new StringBuffer();//存观测时间list集合
			String V_CCCC ="000";
			String V_BBB="000";
			String V_TT="999999";
			String V_YYGGgg="999999";
//			String LastModified="9999-00-00 00:00:00";
			PreparedStatement statement = null;
			try {
				report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
				statement = new LoggableStatement(report_connection, sql);
				report_connection.setAutoCommit(false);
				
				if(successOrNot==1){//解码成功
					if(bufrDecoder0.getParseList().size()>0){//解码有值
						for(Map<String, Object> members: bufrDecoder0.getParseList()){
							//对数据做规整
							List<String> least=transMembers(members);
							String sta="999999";
							if(members.get("0-1-1-0")!=null){
							  sta=((BufrBean)(members.get("0-1-1-0"))).getValue().toString();
							}
							if(sta.contains(".")){
								sta=sta.substring(0,sta.lastIndexOf("."));
							}
							if(sta.startsWith("999999")){
								sta="999999";
							}
							station_s.append(sta).append(",");
							Calendar calendar = Calendar.getInstance();
							String year= ((BufrBean)(members.get("0-4-1-0"))).getValue().toString();
							String month= ((BufrBean)(members.get("0-4-2-0"))).getValue().toString();
							String day= ((BufrBean)(members.get("0-4-3-0"))).getValue().toString();
							String hour= ((BufrBean)(members.get("0-4-4-0"))).getValue().toString();
							calendar.set(Calendar.YEAR, (int)Double.parseDouble(year));
							calendar.set(Calendar.MONTH, (int)Double.parseDouble(month)-1);
							calendar.set(Calendar.DAY_OF_MONTH, (int)Double.parseDouble(day));
							calendar.set(Calendar.HOUR_OF_DAY, (int)Double.parseDouble(hour)-1);
							calendar.set(Calendar.MINUTE,0);
							calendar.set(Calendar.SECOND, 0);
							calendar.set(Calendar.MILLISECOND, 0);
							 V_CCCC = members.get("CCCC").toString();
							 V_BBB=members.get("BBB").toString();
							 V_TT=members.get("TTAAii").toString();
							 V_YYGGgg=members.get("YYGGgg").toString();
//							LastModified= ((BufrBean)members.get("LastModified")).getValue().toString();
							StringBuffer datetime=new StringBuffer();//存单个datetime
							
							if("3-1-1".equals(entrance)){
								Object obj=members.get("1-18-0-0");
								if(obj!=null){
									datetime_s=findValue(obj,datetime_s,calendar);
								}else{
									datetime_s.append("9999-01-01 00:00:00,");
								}
							}else if("3-22-194".equals(entrance)) {
								//有两个延迟组
								Object obj=members.get("1-16-0-0");
								Object obj1=members.get("1-24-0-0");
								if(obj!=null||obj1!=null) {
									if(obj!=null) {
										datetime_s=findValue(obj,datetime_s,calendar);
									}
									if(obj1!=null) {
										datetime_s=findValue(obj1,datetime_s,calendar);
									}
								}else{
									datetime_s.append("9999-01-01 00:00:00,");
								}
							}else{
								String[] date_time={"9999","01","01","00","00"};
								for(int i=0;i<5;i++){
									int j=i+1;
									String fxy="0-4-"+j+"-0";
									Object obj=members.get(fxy);
									if(obj!=null){
										String ele=((BufrBean)(members.get(fxy))).getValue().toString();
										 try{
											 if(i==0){
												 if(!ele.startsWith("99999")){
													 date_time[i] = String.format("%04d", (int)Double.parseDouble(ele));
												 }
											 }else{
												 if(!ele.startsWith("99999")){
													 date_time[i]=String.format("%02d", (int)Double.parseDouble(ele));
												 }
											 }
										}catch (Exception e) {
											System.out.println("ReportInfoToDb datetime 转换异常："+e.getMessage());
										}
									}
								}
								datetime.append(date_time[0]).append("-").append(date_time[1]).append("-").append(date_time[2]).append(" ").append(date_time[3]).append(":").append(date_time[4]).append(":").append("00");
	
								datetime_s.append(datetime).append(",");
							}
						}//end for
						String stations=station_s.substring(0,station_s.length()-1);//站号集合
						stations=stations.length()>800?stations.substring(0,800):stations;//长度若超过800，则截断处理
						String station_1=stations.split(",")[0];//第一个站号
						String datetimes=datetime_s.substring(0,datetime_s.length()-1);//datetime集合
						datetimes=datetimes.length()>1500?datetimes.substring(0,1500):datetimes;//长度若超过1500，则截断处理
						String datetime_1=datetimes.split(",")[0];
						Date datetime1=new Date();
						datetime1=simpleDateFormat.parse(datetime_1);//第一个datetime
						byte[] databyte=getContent(fileName);//报文二进制数组
						int ii = 1;
						statement.setString(ii++, station_1 + "_" + sdf.format(datetime1));// d_record_id
						statement.setString(ii++, "G.0016.0001.S001");//D_DATA_ID
						statement.setString(ii++, "G.0016.0002.R001_"+(new File(fileName)).getName());//D_source_id
						statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_IYMDHM
						statement.setTimestamp(ii++, new Timestamp(recv.getTime()));// D_RYMDHM
						statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_UPDATE_TIME
						statement.setTimestamp(ii++, new Timestamp(datetime1.getTime()));//D_DATETIME
						statement.setString(ii++, station_1);//V01301
						statement.setString(ii++, V_BBB);//V_BBB
						statement.setString(ii++, V_CCCC);//V_CCCC
						statement.setString(ii++, V_YYGGgg);//V_YYGGgg
						statement.setString(ii++, V_TT);//V_TT
						statement.setLong(ii++, file.length());//V_LEN    BUFR报文长度 ?????????????
						statement.setLong(ii++, file.length());//V_file_size 文件大小
						statement.setBytes(ii++, databyte);//V_BULLETIN_B 公报内容
//						statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
						statement.setString(ii++,filename);//V_FILE_NAME 文件名
						if("3-1-1".equals(entrance)){
							statement.setInt(ii++, datetimes.split(",").length);//V_NUM 观测记录数
						}else{
							statement.setInt(ii++, bufrDecoder0.getParseList().size());//V_NUM 观测记录数
						}
						statement.setString(ii++,stations);//V01301_list 观测记录站号列表
						statement.setString(ii++,datetimes);//d_datetime_list 观测记录时间列表
						statement.setInt(ii++,1);//V_decode_flag 解码是否成功标志
					}else{
						
					}
				}else if(successOrNot==2){//解码失败
					V_BBB=bufrDecoder0.V_BBB;
					V_CCCC=bufrDecoder0.V_CCCC;
					V_TT=bufrDecoder0.V_TTAAii;
					V_YYGGgg=bufrDecoder0.V_YYGGgg;
					byte[] databyte=getContent(fileName);//报文二进制数组
					int ii = 1;
					statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
					statement.setString(ii++, "G.0016.0001.S001");//D_DATA_ID
					statement.setString(ii++,  "G.0016.0002.R001_"+(new File(fileName)).getName());//D_source_id
//					System.out.println(new Timestamp((Timestamp.valueOf(LastModified)).getTime()));
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_IYMDHM
					statement.setTimestamp(ii++, new Timestamp(recv.getTime()));// D_RYMDHM
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_UPDATE_TIME
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_DATETIME
					statement.setString(ii++, "999999");//V01301
					statement.setString(ii++, V_BBB);//V_BBB
					statement.setString(ii++, V_CCCC);//V_CCCC
					statement.setString(ii++, V_YYGGgg);//V_YYGGgg
					statement.setString(ii++, V_TT);//V_TT
					if(bufrDecoder0.normalFlag==1){//---------------报文有正常BUFR消息，但解码失败
						statement.setLong(ii++, file.length());//V_LEN    BUFR报文长度 ?????????????
					}else{//--------------报文不包含正常的BUFR消息，即文件打不开，或空文件
						statement.setLong(ii++, 0);//V_LEN    BUFR报文长度 ?????????????
					}
					statement.setLong(ii++, file.length());//V_file_size 文件大小
					statement.setBytes(ii++, databyte);//V_BULLETIN_B 公报内容
//					statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
					statement.setString(ii++,filename);//V_FILE_NAME 文件名
					if(bufrDecoder0.normalFlag==1){//--------------报文有正常BUFR消息，但解码失败
						statement.setInt(ii++, -1);//V_NUM 观测记录数
					}else{//-----------------报文不包含正常的BUFR消息，即文件打不开，或空文件
						statement.setInt(ii++, 0);//V_NUM 观测记录数
					}
					statement.setString(ii++,null);//V01301_list 观测记录站号列表
					statement.setString(ii++,null);//d_datetime_list 观测记录时间列表
					if(bufrDecoder0.normalFlag==1){//---------------------报文有正常BUFR消息，但解码失败
						statement.setInt(ii++,-1);//V_decode_flag 解码是否成功标志
					}else{//---------------------------------报文不包含正常的BUFR消息，即文件打不开，或空文件
						statement.setInt(ii++,0);//V_decode_flag 解码是否成功标志
					}
				}else {//未做解码， 即文件不存在
					byte[] databyte=new byte[0];
					int ii = 1;
					statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
					statement.setString(ii++, "G.0016.0001.S001");//D_DATA_ID
					statement.setString(ii++, "G.0016.0002.R001");//D_source_id
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_IYMDHM
					statement.setTimestamp(ii++, new Timestamp(recv.getTime()));// D_RYMDHM
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_UPDATE_TIME
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_DATETIME
					statement.setString(ii++, "999999");//V01301
					statement.setString(ii++, V_BBB);//V_BBB
					statement.setString(ii++, V_CCCC);//V_CCCC
					statement.setString(ii++, V_YYGGgg);//V_YYGGgg
					statement.setString(ii++, V_TT);//V_TT
					statement.setLong(ii++, 0);//V_LEN    BUFR报文长度 ?????????????
					statement.setLong(ii++, 0);//V_file_size 文件大小
					statement.setBytes(ii++, databyte);//V_BULLETIN_B 公报内容
//					statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
					statement.setString(ii++,filename);//V_FILE_NAME 文件名
					statement.setInt(ii++, 0);//V_NUM 观测记录数
					statement.setString(ii++,null);//V01301_list 观测记录站号列表
					statement.setString(ii++,null);//d_datetime_list 观测记录时间列表
					statement.setInt(ii++,0);//V_decode_flag 解码是否成功标志
				}
				
				try{
					statement.execute();
					report_connection.commit();
					infoLogger.info("ReportInfoToDb 数据入报告表"+StartConfig.reportTable()+"成功！:"+fileName);
				}catch (Exception e) {
					infoLogger.error("ReportInfoToDb 数据入报告表"+StartConfig.reportTable()+"失败！:"+fileName+((LoggableStatement)statement).getQueryString()+e.getMessage());
				}
			}catch (Exception e) {
				infoLogger.error("ReportInfoToDb 数据入报告表"+StartConfig.reportTable()+"失败！:"+fileName+e.getMessage());
			}
			finally{
				if(report_connection!=null){
					try {
						report_connection.close();
					} catch (SQLException e) {
						infoLogger.error("report_connection关闭失败！:"+fileName+e.getMessage());
						e.printStackTrace();
					}
				}
				if(statement!=null){
					try {
						statement.close();
					} catch (SQLException e) {
						infoLogger.error("statement关闭失败！:"+fileName+e.getMessage());
						e.printStackTrace();
					}
				}
			}
	 }
	 
	
	 public byte[] getContent(String filePath)throws IOException {  
	        File file = new File(filePath);  
	        long fileSize = file.length();  
	        if (fileSize > Integer.MAX_VALUE) {  
	            System.out.println("file too big...");  
	            return null;  
	        } 
	       
	        FileInputStream fi = new FileInputStream(file);  
	        byte[] buffer = new byte[(int) fileSize];  
	        int offset = 0;  
	        int numRead = 0;  
	        while (offset < buffer.length  
	        && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {  
	            offset += numRead;  
	        }  
	        // 确保所有数据均被读取  
	        if (offset != buffer.length) {  
	        throw new IOException("Could not completely read file "  + file.getName());  
	        }  
	        fi.close();  
	        return buffer;  
	    }  
	

		public StringBuffer findValue(Object obj,StringBuffer datetime_s, Calendar calendar){
		List<BufrBean> bufrList = (List<BufrBean>) obj;
		int span=5;//报文重复间隔
		BigDecimal a=new BigDecimal(bufrList.size()/span).setScale(0,BigDecimal.ROUND_UP);
		int n=a.intValue();//n条数据
		for(int j=0;j<n;j++){
			String[] date_time={"9999","01","01","00","00"};
			for(int i=0;i<1;i++){
	//			List<BufrBean> bufrList = (List<BufrBean>) obj;
				if ((i + 1) > bufrList.size()) {
					if(i==0)
						date_time[i]="9999";
					else if(i==1||i==2){
						date_time[i]="01";
					}else{
						date_time[i]="00";
					}
				}else{
					List<String> ts = new ArrayList<String>();
					List<BufrBean> bufrListCopy=new ArrayList<BufrBean>();
					BufrBean beancopy=new BufrBean();
					for(int p=j*span;p<bufrList.size();p++){
						beancopy=bufrList.get(p);
						bufrListCopy.add(beancopy);
					}	
					for (BufrBean bufrBean : bufrListCopy) {
						ts.add(bufrBean.getValue().toString());
					}
					if(i%5==0) {
						String expression="{"+i+"}";
						String ele = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
						calendar.set(Calendar.MINUTE, (int)Double.parseDouble(ele));
					}
				}
			}
			StringBuffer datetime=new StringBuffer();
			datetime.append(ymdhm.format(calendar.getTime()));
			datetime_s.append(datetime).append(",");
//			for(int c = 0; c < span; c++)
//				bufrList.remove(0);
		}//循环span条数据
		return datetime_s;
	}



	private final String SUCCEEDED_KEY = "SUCCEEDED";
	private final String OTHER_EXCEPTION_KEY = "OTHER_EXCEPTION";
	/**
	 * Decode.
	 *
	 * @param bufrDecoder the bufr decoder
	 * @param fileName the file name
	 * @param tableSection the table section
	 * @param tables the tables
	 * @return true, if successful
	 */
	public boolean decode(BufrSurfCawnDecoder bufrDecoder, String fileName, String tableSection, List<String> tables) {
		// 获取数据库连接
				DruidPooledConnection connection = null;
				Statement statement = null;
				File fileN=new File(fileName);
//				int  idx=fileName.lastIndexOf(".");
//				String prename=fileName.substring(0, idx-1);
//				String endstr =fileName.substring(idx-3,idx);
				
				Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
				try {
//					if(diQueues == null)
//						diQueues = new LinkedBlockingQueue<StatDi>();
					connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
					statement = connection.createStatement();
					
					if(connection.getAutoCommit() == true)
						connection.setAutoCommit(false);
//					statement.execute("select last_txc_xid()");
					boolean dateTimeOK=true;
					int num=1;
					for(Map<String, Object> members: bufrDecoder.getParseList()){
						//对数据做规整
						List<String> least=transMembers(members);
						dateTimeOK=true;
						for (String tableName : tables) {
							dateTimeOK=true;
							String d_record_id="999999";
							String d_datetime="999999";
							tableName = tableName.trim();
							System.out.println("tableName:"+tableName);
							
							int recileNum=0;
							if(tableName.equalsIgnoreCase("CAWN_CHN_PMM_BUFR_TAB")) {
								recileNum=36;
								processIntoPMMTable(connection,statement,dateTimeOK,members,tableName,fileName,d_record_id,d_datetime,configMap,num,least,recileNum);
							}else if(tableName.equalsIgnoreCase("CAWN_CHN_NSD_BUFR_TAB")) {
								recileNum=12;
								processIntoNSDTable(connection,statement,dateTimeOK,members,tableName,fileName,d_record_id,d_datetime,configMap,least,recileNum);
							}else if(tableName.equalsIgnoreCase("CAWN_CHN_AAP_BUFR_TAB")) {
								recileNum=12;
								processIntoAAPTable(connection,statement,dateTimeOK,members,tableName,fileName,d_record_id,d_datetime,configMap,least,recileNum);
							
							}else
								if(tableName.equalsIgnoreCase("CAWN_CHN_ASP_BUFR_TAB")) {
								recileNum=12;
								processIntoASPTable(connection,statement,dateTimeOK,members,tableName,fileName,d_record_id,d_datetime,configMap,least,recileNum);
							
							}
							else {
								recileNum=12;
								processIntoNormalTable(connection,statement,dateTimeOK,members,tableName,fileName,d_record_id,d_datetime,configMap,least,recileNum);
							
							}
							
							
//							for(int i=0;i<recileNum;i++) {
								
//							}
					} // end tables loop
					if(DI != null){
						for (int j = 0; j < DI.size(); j++) {
							diQueues.offer(DI.get(j));
						}
						DI.clear();
					}
				} // end parseList loop 
					return true;
				} catch (SQLException e) {
					infoLogger.error("数据库异常: " + e.getMessage());
					return false;
				} finally {
					try {
						if(statement!=null){
							statement.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						if(connection!=null)
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
	}
	
	private List<String> transMembers(Map<String, Object> members) {
		int num=0;
		int num1=0;
		int num2=0;
		int num3=0;
		int num4=0;
		List<String> least=new ArrayList<String>();
		for(String key : members.keySet()){
			
			Object object = members.get(key);
			if(object!=null) {
				
				if(object instanceof BufrBean) {
					least.add(key +" " + ((BufrBean)object).getValue());
				}else if (object instanceof ArrayList) {
					List list = (List)object;
					List klist = (List)object;
					List vlist = (List)object;
					
					Map<String,String> kmap=new HashMap<String, String>();
				
					
					if(key.startsWith("1-16-0")) {
						int grouplength=5;
						 int i=0;
						 for(int k=0;k<list.size()/grouplength;k++) {
							 StringBuffer aBuffer = new StringBuffer();
							 for(int j=i;j<i+grouplength;j++) {
								 if(list.get(i) instanceof BufrBean) {
										aBuffer.append(((BufrBean)list.get(j)).getFxy() + " : " +((BufrBean)list.get(j)).getValue() + "    ");
									}else {
										Map<String, String> map =getStringToMap(list.get(i).toString());
										for (String string : map.keySet()) {
											aBuffer.append(string + " : " +map.get(string) + "    ");
										}
									}
							 }
							 	least.add("1-16-0-"+num+"======="+aBuffer);
							 i+=grouplength;
							 num++;
						 }
					}else if(key.startsWith("1-8-0")) {
						int grouplength=32;
						 int i=0;
						 for(int k=0;k<list.size()/grouplength;k++) {
							 StringBuffer aBuffer = new StringBuffer();
							 for(int j=i;j<i+grouplength;j++) {
								 if(list.get(i) instanceof BufrBean) {
										aBuffer.append(((BufrBean)list.get(j)).getFxy() + " : " +((BufrBean)list.get(j)).getValue() + "    ");
									}else {
										Map<String, String> map =getStringToMap(list.get(i).toString());
										for (String string : map.keySet()) {
											aBuffer.append(string + " : " +map.get(string) + "    ");
										}
									}
							 }
								least.add("1-8-0-"+num1+"======="+aBuffer);
							 i+=grouplength;
							 num1++;
						 }
					}else if(key.startsWith("1-24-0")) {
						int grouplength=9;
						 int i=0;
						 for(int k=0;k<list.size()/grouplength;k++) {
							 StringBuffer aBuffer = new StringBuffer();
							 for(int j=i;j<i+grouplength;j++) {
								 if(list.get(i) instanceof BufrBean) {
										aBuffer.append(((BufrBean)list.get(j)).getFxy() + " : " +((BufrBean)list.get(j)).getValue() + "    ");
									}else {
//										System.out.println(list.get(j).toString());
										Map<String, String> map =getStringToMap(list.get(i).toString());
//										System.out.println(map);
										for (String string : map.keySet()) {
											aBuffer.append(string + " : " +map.get(string) + "    ");
//											System.out.println("1-16-0"+num+"======="+aBuffer);
										}
									}
							 }
//							 System.out.println("1-24-0-"+num4+"======="+aBuffer);
								least.add("1-24-0-"+num4+"======="+aBuffer);
//							 klist.add(aBuffer.toString());
//							 kmap.put("1-16-0"+num+"=======",klist.toString());
							 i+=grouplength;
							 num4++;
						 }
					} 
					else {
						StringBuffer sBuffer = new StringBuffer();
						if(key.startsWith("1-1-0")) {
							if(list.size()>5) {
								sBuffer.append("1-1-0-"+num2 +"   =====     " );
								num2++;
							}else {
								sBuffer.append("s1-1-0-"+num3+"s" +"   =====     " );
								num3++;
							}
						}else {
						sBuffer.append(key +"   =====     " );
						}
						for (int i = 0; i < list.size(); i++) {
							if(list.get(i) instanceof BufrBean) {
								sBuffer.append(((BufrBean)list.get(i)).getFxy() + " : " +((BufrBean)list.get(i)).getValue() + "    ");
							}else {
								Map<String, BufrBean> map = (Map<String, BufrBean>) list.get(i);
								
								for (String string : map.keySet()) {
									sBuffer.append(string + " : " +map.get(string).getValue() + "    ");
								}
							}
						}
						least.add(sBuffer.toString());
					}

					
				}else {
					System.out.println(key + "   " +  object);
				}
			}
		}
//		System.out.println(least.toString());
		return least;
	}

	/**
	    * 
	    * String转map
	    * @param object
	    * @return
	    */
	   public static Map<String,String> getStringToMap(String object){
	       //根据逗号截取字符串数组
	       String[] str1 = object.split("    ");
	       //创建Map对象
	       Map<String,String> map = new HashMap<>();
	       //循环加入map集合
	       for (int i = 0; i < str1.length; i++) {
	           //根据":"截取字符串数组
	           String[] str2 = str1[i].split(":");
	           //str2[0]为KEY,str2[1]为值
	           map.put(str2[0],str2[1]);
	       }
	       return map;
	   }

	   private void processIntoNSDTable(DruidPooledConnection connection, Statement statement, boolean dateTimeOK,
				Map<String, Object> members, String tableName, String fileName, String d_record_id, String d_datetime, Map<String, XmlBean> configMap, List<String> least, int recileNum) {
			File fileN=new File(fileName);
			XmlBean xmlBean = configMap.get(tableName);
			Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
			Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
		    boolean isDwc=true;
			for(int i=0;i<recileNum;i++) {
				String []msg18=null;
				String []msg1132=null;
				//对规整后的数据排序
				Collections.sort(least);
				for(int j=0;j<least.size();j++) {
//					System.out.println(least.get(j));
					if(least.get(j).startsWith("1-8-0-"+i)) {
						msg18=least.get(j).replace("=======", ":").replace("    ", ":").trim().split(":");
					}
					if(least.get(j).startsWith("1-1-32-"+i)) {
						msg1132=least.get(j).replace("=====", ":").replace("    ", ":").trim().split(":");
					}
				}
//				System.out.println("=========================");
//				System.out.println(num+":"+Arrays.toString(msg116)+"==="+Arrays.toString(msg11));
//				if(num==1&&(msg116[2].equals("5.0")||msg116[2].equals("10.0")||msg116[2].equals("15.0"))) {
//					System.out.println("出现！");
//				}
				// 用于存储入库的字段
				StringBuffer keyBuffer = new StringBuffer();
				// 用于存储入库的字段值
				StringBuffer valueBuffer = new StringBuffer();
				// 遍历配置信息
			Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
//					
					
					StatDi di = new StatDi();
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setTT("BUFR");
					di.setDATA_TYPE_1(StartConfig.ctsCode());
					di.setFILE_NAME_N(fileName);
					di.setFILE_NAME_O(fileName);
					while (it.hasNext()) {
						Entry<String, EntityBean> next = it.next();
						String column = next.getKey(); // 配置文件配置的字段名称
						if(column.equals("V04005_S") ){
							System.out.println();
						}
						EntityBean entityBean = next.getValue(); // 配置的每个字段属性
						String datatype = entityBean.getDatatype(); // 字段类型
						String expression = entityBean.getExpression(); // 此字段的表达式
						String fxy = entityBean.getFxy(); // 解码时存储数据的key
						String defaultValue = entityBean.getDefaultValue(); // 缺省值
						Object obj = members.get(fxy); // 根据fxy获取每一行数据
						Calendar calendar = Calendar.getInstance();
						BufrBean beanYear = (BufrBean)members.get(entityMap.get("V04001").getFxy());
						BufrBean beanMonth = (BufrBean)members.get(entityMap.get("V04002").getFxy());
						BufrBean beanDayOfMonth = (BufrBean)members.get(entityMap.get("V04003").getFxy());
						BufrBean beanHourOfDay = (BufrBean)members.get(entityMap.get("V04004").getFxy());
						BufrBean beanStation = (BufrBean)members.get(entityMap.get("V01301").getFxy());
						calendar.set(Calendar.YEAR, (int)Double.parseDouble((beanYear.getValue().toString())));
						calendar.set(Calendar.MONTH, (int)Double.parseDouble((beanMonth.getValue().toString()))-1);
						calendar.set(Calendar.DAY_OF_MONTH, (int)Double.parseDouble((beanDayOfMonth.getValue().toString())));
						calendar.set(Calendar.HOUR_OF_DAY, (int)Double.parseDouble((beanHourOfDay.getValue().toString()))-1);
						if(msg18!=null&&msg18.length>0) {
							calendar.set(Calendar.MINUTE,(int)Double.parseDouble((msg18[2])));
						}else {
							calendar.set(Calendar.MINUTE,999999);
						}
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
						// 如果获取的对象为null
						Object value = null;
						if (obj == null) {
							// 如果表达式不为空说明此值需要根据表达式进行计算value
							if (StringUtils.isNotBlank(expression)) {
								// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
								Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
								if (em.find()) {
									value = defaultValue;
								} else {// 如果表达式存在，且其中没有以index取值
									value = calExpressionValue(obj, members, entityMap, entityBean);
								}
							} else {
								value = defaultValue;
								if(column.equalsIgnoreCase("D_SOURCE_ID"))
									value ="G.0016.0002.R001"+"_"+fileN.getName();
							}
							
							if(("D_RECORD_ID").equals(column)){
								System.out.println(value+"1");
								
//								ymdhm.format(calendar.getTime());
								String wmo=beanStation.getValue().toString();
									d_record_id=wmo+"_"+ymdhm1.format(calendar.getTime());
									value=d_record_id;
								
							}
							if(("D_DATETIME").equals(column)){
								d_datetime=ymdhm.format(calendar.getTime());
								value=d_datetime;
								try{
									SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									Date dataTime=simpleDateFormat.parse(value.toString());
									if(!TimeCheckUtil.checkTime(dataTime)){
										dateTimeOK = false;
										infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
										break;
									}
								}catch (Exception e) {
									dateTimeOK = false;
									infoLogger.error("资料时间错误！  " + fileName);
									break;
								}
							}
							if(msg1132!=null&&msg1132.length>0) {
								for(int k=1;k<33;k++) {
									String params=null;
									if(k<10) {
										params="0"+k;
									}else {
										params=k+"";
									}
									if(column.equals("V15023_C"+params)) {
										value=msg1132[2*k+1];
									}
								}
							}
							if(msg18!=null&&msg18.length>0) {
								if(column.equals("V04005_S") ){
									value=msg18[2];
								}
								if(column.equals("V04006_S") ){
									value=msg18[4];
								}
								
								if(column.equals("V_STORAGE_PLACE")) {
									value=msg18[8];
								}
								if(column.equals("V_WEIGHT_FACTOR")) {
									value=msg18[10];
								}
								if(column.equals("V_ERROR_CODE")) {
									value=msg18[12];
								}
								if(column.equals("V15752")) {
									value=msg18[14];
								}
								if(column.equals("V15765")) {
									value=msg18[16];
								}
								if(column.equals("V_CORRECT_COUNT")) {
									value=msg18[18];
								}
								if(column.equals("V10004_040")) {
									value=msg18[20];
								}
								if(column.equals("V13003_040")) {
									value=msg18[24];
								}
								if(column.equals("V12001_040")) {
									value=msg18[26];
								}
								if(column.equals("V_TIME_SPACING")) {
									value=msg18[28];
								}
								if(column.equals("V11002_071")) {
									value=msg18[30];
								}
								if(column.equals("V11001_071")) {
									value=msg18[32];
								}
								if(column.equals("V13011_071")) {
									value=msg18[34];
								}
								if(column.equals("V12200")) {
									value=msg18[36];
								}
								if(column.equals("V13198")) {
									value=msg18[38];
								}
								if(column.equals("V10196")) {
									value=msg18[40];
								}
								if(column.equals("V12204")) {
									value=msg18[42];
								}
								if(column.equals("V13199")) {
									value=msg18[44];
								}
								if(column.equals("V10197")) {
									value=msg18[46];
								}
								if(column.equals("V11194")) {
									value=msg18[48];
								}
								if(column.equals("V11195")) {
									value=msg18[50];
								}
								if(column.equals("V02440")) {
									value=msg18[52];
								}
								if(column.equals("V10004")) {
									value=Double.parseDouble(msg18[54])*0.01;
								}
								if(column.equals("V13003")) {
									value=msg18[56];
								}
								if(column.equals("V12001")) {
									value=msg18[58];
								}
								if(column.equals("V11001")) {
									value=Double.parseDouble(msg18[60])-273.15;
								}
								if(column.equals("V11002")) {
									value=msg18[62];
								}
								if(column.equals("V13011")) {
									value=msg18[64];
								}
							}else {
								infoLogger.error("资料观测时间和数据缺测！  " + fileName);
								isDwc=false;
								continue;
							}
							if(column.equals("V04001")) {
								value=calendar.get(Calendar.YEAR);
							}else if(column.equals("V04002")) {
								value=calendar.get(Calendar.MONTH)+1;
							}else if(column.equals("V04003")) {
								value=calendar.get(Calendar.DAY_OF_MONTH);
							}else if(column.equals("V04004")) {
								value=calendar.get(Calendar.HOUR_OF_DAY);
							}else if(column.equals("V04005")) {
								value=calendar.get(Calendar.MINUTE);
							}
							if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
								value = members.get("CCCC");
							if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
								value = members.get("TTAAii");
							if(column.equalsIgnoreCase("V_BBB"))
								value = members.get("BBB");
							if(column.equalsIgnoreCase("D_RYMDHM"))
								value=((BufrBean)members.get("LastModified")).getValue();
							
							keyBuffer.append(",`").append(column).append("`");
							valueBuffer.append(",'").append(value.toString().trim()).append("'");
							if(column.equals("D_DATETIME")){
								value=d_datetime;
								int idx01 = value.toString().lastIndexOf(":");
								di.setDATA_TIME(value.toString().substring(0, idx01));
							}
							if(column.equals("V01301"))
								di.setIIiii(value.toString());
							if(column.equals("V05001"))
								di.setLATITUDE(value.toString());
							if(column.equals("V06001"))
								di.setLONGTITUDE(value.toString());
							else if(column.equals("V07001"))
								di.setHEIGHT(value.toString());
							if(column.equals("D_DATA_ID"))
								di.setDATA_TYPE(value.toString());
							else if(column.equals("V_BBB"))
								di.setDATA_UPDATE_FLAG(value.toString());
							continue;
						}
						
						// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//					System.err.println(column);
						value = calExpressionValue(obj, members, entityMap, entityBean);
						if("V14032".equals(column)&&!(value.toString().startsWith("999999"))){
							value=Double.parseDouble(value.toString());
						}	
						
						
						if(("D_DATETIME").equals(column)){
							d_datetime=ymdhm.format(calendar.getTime());
							try{
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date dataTime=simpleDateFormat.parse(value.toString());
								if(!TimeCheckUtil.checkTime(dataTime)){
									dateTimeOK = false;
									infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
									break;
								}
							}catch (Exception e) {
								dateTimeOK = false;
								infoLogger.error("资料时间错误！  " + fileName);
								break;
							}
						}
						if(msg1132!=null&&msg1132.length>0) {
							for(int k=1;k<33;k++) {
								String params=null;
								if(k<10) {
									params="0"+k;
								}else {
									params=k+"";
								}
								if(column.equals("V15023_C"+params)) {
									value=msg1132[2*k+1];
								}
							}
						}
						
						if(msg18!=null&&msg18.length>0) {
							if(column.equals("V04005_S") ){
								value=msg18[2];
							}
							if(column.equals("V04006_S") ){
								value=msg18[4];
							}
							
							if(column.equals("V_STORAGE_PLACE")) {
								value=msg18[8];
							}
							if(column.equals("V_WEIGHT_FACTOR")) {
								value=msg18[10];
							}
							if(column.equals("V_ERROR_CODE")) {
								value=msg18[12];
							}
							if(column.equals("V15752")) {
								value=msg18[14];
							}
							if(column.equals("V15765")) {
								value=msg18[16];
							}
							if(column.equals("V_CORRECT_COUNT")) {
								value=msg18[18];
							}
							if(column.equals("V10004_040")) {
								value=msg18[20];
							}
							if(column.equals("V13003_040")) {
								value=msg18[24];
							}
							if(column.equals("V12001_040")) {
								value=msg18[26];
							}
							if(column.equals("V_TIME_SPACING")) {
								value=msg18[28];
							}
							if(column.equals("V11002_071")) {
								value=msg18[30];
							}
							if(column.equals("V11001_071")) {
								value=msg18[32];
							}
							if(column.equals("V13011_071")) {
								value=msg18[34];
							}
							if(column.equals("V12200")) {
								value=msg18[36];
							}
							if(column.equals("V13198")) {
								value=msg18[38];
							}
							if(column.equals("V10196")) {
								value=msg18[40];
							}
							if(column.equals("V12204")) {
								value=msg18[42];
							}
							if(column.equals("V13199")) {
								value=msg18[44];
							}
							if(column.equals("V10197")) {
								value=msg18[46];
							}
							if(column.equals("V11194")) {
								value=msg18[48];
							}
							if(column.equals("V11195")) {
								value=msg18[50];
							}
							if(column.equals("V02440")) {
								value=msg18[52];
							}
							if(column.equals("V10004")) {
								value=Double.parseDouble(msg18[54])*0.01;
							}
							if(column.equals("V13003")) {
								value=msg18[56];
							}
							if(column.equals("V12001")) {
								value=msg18[58];
							}
							if(column.equals("V11001")) {
								value=Double.parseDouble(msg18[60])-273.15;
							}
							if(column.equals("V11002")) {
								value=msg18[62];
							}
							if(column.equals("V13011")) {
								value=msg18[64];
							}
							
						}else {
							infoLogger.error("资料观测时间和数据缺测！  " + fileName);
							isDwc=false;
							continue;
						}
						if(column.equals("V04001")) {
							value=calendar.get(Calendar.YEAR);
						}else if(column.equals("V04002")) {
							value=calendar.get(Calendar.MONTH)+1;
						}else if(column.equals("V04003")) {
							value=calendar.get(Calendar.DAY_OF_MONTH);
						}else if(column.equals("V04004")) {
							value=calendar.get(Calendar.HOUR_OF_DAY);
						}else if(column.equals("V04005")) {
							value=calendar.get(Calendar.MINUTE);
						}
						if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
							value = members.get("CCCC");
						if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
							value = members.get("TTAAii");
						if(column.equalsIgnoreCase("V_BBB"))
							value = members.get("BBB");
						if(column.equalsIgnoreCase("D_RYMDHM"))
							value=((BufrBean)members.get("LastModified")).getValue();
						
						keyBuffer.append(",`").append(column).append("`");
						this.appendValue(valueBuffer, datatype, value.toString().trim());
						
						if(column.equals("V_WEIGHT_FACTOR") ){
							System.out.println("V_WEIGHT_FACTOR2");
						}
						
						if(column.equals("D_DATETIME")){
							value=ymdhm.format(calendar.getTime());
							int idx01 = value.toString().lastIndexOf(":");
							di.setDATA_TIME(value.toString().substring(0, idx01));
						}
						if(column.equals("V01301"))
							di.setIIiii(value.toString());
						if(column.equals("V05001"))
							di.setLATITUDE(value.toString());
						if(column.equals("V06001"))
							di.setLONGTITUDE(value.toString());
						else if(column.equals("V07001"))
							di.setHEIGHT(value.toString());
						if(column.equals("D_DATA_ID"))
							di.setDATA_TYPE(value.toString());
						else if(column.equals("V_BBB"))
							di.setDATA_UPDATE_FLAG(value.toString());
//			infoLogger.info("\n connection: fileName  " + fxy  + "-------" + value);
						
						
					}// end while
					Date recv=new Date();
					di.setTRAN_TIME(TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setFILE_SIZE(String.valueOf(fileN.length()));
					di.setBUSINESS_STATE("1");
					di.setPROCESS_STATE("1");
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					
					DI.add(di);
					if(isDwc) {
						// 键表入库
						String sql = "insert into " + tableName //
								+ " (" //
								+ keyBuffer.toString().substring(1) //
								+ ") values ("//
								+ valueBuffer.toString().substring(1) //
								+ ")";
						
						System.out.println("sql:"+sql);
						try {
							int executeUpdate = statement.executeUpdate(sql);
							connection.commit();
							if (executeUpdate > 0) {
								infoLogger.info(tableName + " Insert a record successfully！ " + fileName);
								Integer succeededNum = tmpRecord.get(SUCCEEDED_KEY);
								succeededNum = (succeededNum == null ? 1 : (succeededNum +=1));
								tmpRecord.put(SUCCEEDED_KEY, succeededNum);
							} else {
								infoLogger.info("插入失败:\n  " + sql);
								di.setPROCESS_STATE("0");
								Integer otherExceptionNum = tmpRecord.get(OTHER_EXCEPTION_KEY);
								otherExceptionNum = (otherExceptionNum == null ? 1 : (otherExceptionNum +=1));
								tmpRecord.put(OTHER_EXCEPTION_KEY, otherExceptionNum);
							}
						}catch (Exception e) {
							infoLogger.error("插入失败:\n " + sql + e.getMessage() + " " + fileName);
							di.setPROCESS_STATE("0");
						}
					}
					
				}
//				}
//			}
			
		}

	   private void processIntoAAPTable(DruidPooledConnection connection, Statement statement, boolean dateTimeOK,
				Map<String, Object> members, String tableName, String fileName, String d_record_id, String d_datetime, Map<String, XmlBean> configMap, List<String> least, int recileNum) {
			File fileN=new File(fileName);
			XmlBean xmlBean = configMap.get(tableName);
			Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
			Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
		    boolean isDwc=true;
		    int numbers=1;
		    for(int j=0;j<least.size();j++) {
		    	if(least.get(j).startsWith("1-12-0-1")) {
		    		 numbers = countStr(least.get(j),"0-2-241");
		    	}
		    }
		    for(int m=0;m<numbers;m++) {
		    	for(int i=0;i<recileNum;i++) {
		    		String []msg117=null;
		    		String []msg116=null;
		    		String []msg112=null;
		    		String []msg128=null;
		    		//对规整后的数据排序
		    		Collections.sort(least);
		    		for(int j=0;j<least.size();j++) {
//					System.out.println(least.get(j));
		    			if(least.get(j).startsWith("1-11-7-"+i)) {
		    				msg117=least.get(j).replace("=====", ":").replace("    ", ":").trim().split(":");
		    			}
		    			if(least.get(j).startsWith("1-16-0-"+i)) {
		    				msg116=least.get(j).replace("=====", ":").replace("    ", ":").trim().split(":");
		    			}
		    			if(least.get(j).startsWith("1-12-0-1")) {
		    				msg112=least.get(j).replace("=====", ":").replace("                                    ", ":").replace("    ", ":").trim().split(":");
		    			}
		    			if(least.get(j).startsWith("1-28-0-0")) {
		    				msg128=least.get(j).replace("=====", ":").replace("    ", ":").replace("    ", ":").trim().split(":");
				    		
		    			}
		    		}
//				System.out.println("=========================");
//				System.out.println(num+":"+Arrays.toString(msg116)+"==="+Arrays.toString(msg11));
//				if(num==1&&(msg116[2].equals("5.0")||msg116[2].equals("10.0")||msg116[2].equals("15.0"))) {
//					System.out.println("出现！");
//				}
		    		// 用于存储入库的字段
		    		StringBuffer keyBuffer = new StringBuffer();
		    		// 用于存储入库的字段值
		    		StringBuffer valueBuffer = new StringBuffer();
		    		// 遍历配置信息
		    		Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
//					
		    		
		    		StatDi di = new StatDi();
		    		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		    		di.setTT("BUFR");
		    		di.setDATA_TYPE_1(StartConfig.ctsCode());
		    		di.setFILE_NAME_N(fileName);
		    		di.setFILE_NAME_O(fileName);
		    		while (it.hasNext()) {
		    			Entry<String, EntityBean> next = it.next();
		    			String column = next.getKey(); // 配置文件配置的字段名称
		    			if(column.equals("V04005_S") ){
		    				System.out.println();
		    			}
		    			EntityBean entityBean = next.getValue(); // 配置的每个字段属性
		    			String datatype = entityBean.getDatatype(); // 字段类型
		    			String expression = entityBean.getExpression(); // 此字段的表达式
		    			String fxy = entityBean.getFxy(); // 解码时存储数据的key
		    			String defaultValue = entityBean.getDefaultValue(); // 缺省值
		    			Object obj = members.get(fxy); // 根据fxy获取每一行数据
		    			Calendar calendar = Calendar.getInstance();
		    			BufrBean beanYear = (BufrBean)members.get(entityMap.get("V04001").getFxy());
						BufrBean beanMonth = (BufrBean)members.get(entityMap.get("V04002").getFxy());
						BufrBean beanDayOfMonth = (BufrBean)members.get(entityMap.get("V04003").getFxy());
						BufrBean beanHourOfDay = (BufrBean)members.get(entityMap.get("V04004").getFxy());
						BufrBean beanStation = (BufrBean)members.get(entityMap.get("V01301").getFxy());
						calendar.set(Calendar.YEAR, (int)Double.parseDouble((beanYear.getValue().toString())));
						calendar.set(Calendar.MONTH, (int)Double.parseDouble((beanMonth.getValue().toString()))-1);
						calendar.set(Calendar.DAY_OF_MONTH, (int)Double.parseDouble((beanDayOfMonth.getValue().toString())));
						calendar.set(Calendar.HOUR_OF_DAY, (int)Double.parseDouble((beanHourOfDay.getValue().toString()))-1);
						if(msg116!=null&&msg116.length>0) {
							calendar.set(Calendar.MINUTE,(int)Double.parseDouble((msg116[2])));
						}else {
							calendar.set(Calendar.MINUTE,999999);
						}
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
		    			// 如果获取的对象为null
		    			Object value = null;
		    			if (obj == null) {
		    				// 如果表达式不为空说明此值需要根据表达式进行计算value
		    				if (StringUtils.isNotBlank(expression)) {
		    					// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
		    					Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
		    					if (em.find()) {
		    						value = defaultValue;
		    					} else {// 如果表达式存在，且其中没有以index取值
		    						value = calExpressionValue(obj, members, entityMap, entityBean);
		    					}
		    				} else {
		    					value = defaultValue;
		    					if(column.equalsIgnoreCase("D_SOURCE_ID"))
		    						value ="G.0016.0002.R001"+"_"+fileN.getName();
		    				}
		    				
		    				if(("D_RECORD_ID").equals(column)){
		    					System.out.println(value+"1");
		    					
//								ymdhm.format(calendar.getTime());
		    					String wmo=beanStation.getValue().toString();
		    					d_record_id=wmo+"_"+ymdhm1.format(calendar.getTime());
		    					value=d_record_id;
		    					
		    				}
		    				if(("D_DATETIME").equals(column)){
		    					d_datetime=ymdhm.format(calendar.getTime());
		    					value=d_datetime;
		    					try{
		    						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    						Date dataTime=simpleDateFormat.parse(value.toString());
									if(!TimeCheckUtil.checkTime(dataTime)){
										dateTimeOK = false;
										infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
										break;
									}
		    					}catch (Exception e) {
		    						dateTimeOK = false;
		    						infoLogger.error("资料时间错误！  " + fileName);
		    						break;
		    					}
		    				}
		    				//获取设备型号和序列号
		    				if(msg112!=null&&msg112.length>0) {
		    					if(column.equalsIgnoreCase("V02241")) {
		    						value=msg112[3+m*4];
		    					}
		    					if(column.equalsIgnoreCase("V02208")) {
		    						value=msg112[5+m*4];
		    					}
		    				}else if(msg128!=null&&msg128.length>0){
		    					if(column.equalsIgnoreCase("V02241")) {
		    						value=msg128[4];
		    					}
		    					if(column.equalsIgnoreCase("V02208")) {
		    						value=msg128[6];
		    					}
		    				}
		    				if(msg117!=null&&msg117.length>0) {
		    					int [] figures= {370,470,520,590,660,880,950};
		    					for(int k=0;k<7;k++) {
		    						
		    						if(column.equals("V_VAVLEN_"+figures[k]+"nm")) {
		    							value=msg117[3+k*18];
		    						}
		    						if(column.equals("V_BC_"+figures[k]+"nm")) {
		    							value=msg117[7+k*18];
		    						}
		    						if(column.equals("V_SZ_"+figures[k]+"nm")) {
		    							value=msg117[9+k*18];
		    						}
		    						if(column.equals("V_SB_"+figures[k]+"nm")) {
		    							value=msg117[11+k*18];
		    						}
		    						if(column.equals("V_RZ_"+figures[k]+"nm")) {
		    							value=msg117[13+k*18];
		    						}
		    						if(column.equals("V_RB_"+figures[k]+"nm")) {
		    							value=msg117[15+k*18];
		    						}
		    						if(column.equals("V_Fri_"+figures[k]+"nm")) {
		    							value=msg117[17+k*18];
		    						}
		    						if(column.equals("V_Attn_"+figures[k]+"nm")) {
		    							value=msg117[19+k*18];
		    						}
		    					}
		    				}else {
								infoLogger.error("观测数据缺失！"+fileName);
								isDwc=false;
								continue;
							}
		    				if(msg116!=null&&msg116.length>0) {
		    					if(column.equals("V04005_S") ){
		    						value=msg116[2];
		    					}
		    					if(column.equals("V04006_S") ){
		    						value=msg116[4];
		    					}
		    				}else {
		    					infoLogger.error("资料观测时间和数据缺测！  " + fileName);
		    					isDwc=false;
		    					continue;
		    				}
		    				if(column.equals("V04001")) {
								value=calendar.get(Calendar.YEAR);
							}else if(column.equals("V04002")) {
								value=calendar.get(Calendar.MONTH)+1;
							}else if(column.equals("V04003")) {
								value=calendar.get(Calendar.DAY_OF_MONTH);
							}else if(column.equals("V04004")) {
								value=calendar.get(Calendar.HOUR_OF_DAY);
							}else if(column.equals("V04005")) {
								value=calendar.get(Calendar.MINUTE);
							}
		    				if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
								value = members.get("CCCC");
							if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
								value = members.get("TTAAii");
							if(column.equalsIgnoreCase("V_BBB"))
								value = members.get("BBB");
							if(column.equalsIgnoreCase("D_RYMDHM"))
								value=((BufrBean)members.get("LastModified")).getValue();
							
		    				keyBuffer.append(",`").append(column).append("`");
		    				valueBuffer.append(",'").append(value.toString().trim()).append("'");
		    				if(column.equals("D_DATETIME")){
		    					value=d_datetime;
		    					int idx01 = value.toString().lastIndexOf(":");
		    					di.setDATA_TIME(value.toString().substring(0, idx01));
		    				}
		    				if(column.equals("V01301"))
		    					di.setIIiii(value.toString());
		    				if(column.equals("V05001"))
		    					di.setLATITUDE(value.toString());
		    				if(column.equals("V06001"))
		    					di.setLONGTITUDE(value.toString());
		    				else if(column.equals("V07001"))
		    					di.setHEIGHT(value.toString());
		    				if(column.equals("D_DATA_ID"))
		    					di.setDATA_TYPE(value.toString());
		    				else if(column.equals("V_BBB"))
		    					di.setDATA_UPDATE_FLAG(value.toString());
		    				continue;
		    			}
		    			
		    			// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//					System.err.println(column);
		    			value = calExpressionValue(obj, members, entityMap, entityBean);
		    			if("V14032".equals(column)&&!(value.toString().startsWith("999999"))){
		    				value=Double.parseDouble(value.toString());
		    			}	
		    			
		    			
		    			if(("D_DATETIME").equals(column)){
		    				d_datetime=ymdhm.format(calendar.getTime());
		    				value=d_datetime;
		    				try{
		    					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    					Date dataTime=simpleDateFormat.parse(value.toString());
								if(!TimeCheckUtil.checkTime(dataTime)){
									dateTimeOK = false;
									infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
									break;
								}
		    				}catch (Exception e) {
		    					dateTimeOK = false;
		    					infoLogger.error("资料时间错误！  " + fileName);
		    					break;
		    				}
		    			}
		    			if(msg117!=null&&msg117.length>0) {
		    				int [] figures= {370,470,520,590,660,880,950};
		    				for(int k=0;k<7;k++) {
		    					
		    					if(column.equals("V_VAVLEN_"+figures[k]+"nm")) {
		    						value=msg117[2+k*18];
		    					}
		    					if(column.equals("V_BC_"+figures[k]+"nm")) {
		    						value=msg117[6+k*18];
		    					}
		    					if(column.equals("V_SZ_"+figures[k]+"nm")) {
		    						value=msg117[8+k*18];
		    					}
		    					if(column.equals("V_SB_"+figures[k]+"nm")) {
		    						value=msg117[10+k*18];
		    					}
		    					if(column.equals("V_RZ_"+figures[k]+"nm")) {
		    						value=msg117[12+k*18];
		    					}
		    					if(column.equals("V_RB_"+figures[k]+"nm")) {
		    						value=msg117[14+k*18];
		    					}
		    					if(column.equals("V_Fri_"+figures[k]+"nm")) {
		    						value=msg117[16+k*18];
		    					}
		    					if(column.equals("V_Attn_"+figures[k]+"nm")) {
		    						value=msg117[18+k*18];
		    					}
		    				}
		    			}else {
							infoLogger.error("观测数据缺失！"+fileName);
							isDwc=false;
							continue;
						}
		    			if(msg116!=null&&msg116.length>0) {
		    				if(column.equals("V04005_S") ){
		    					value=msg116[2];
		    				}
		    				if(column.equals("V04006_S") ){
		    					value=msg116[4];
		    				}
		    			}else {
		    				infoLogger.error("资料观测时间和数据缺测！  " + fileName);
		    				isDwc=false;
		    				continue;
		    			}
		    			if(column.equals("V04001")) {
							value=calendar.get(Calendar.YEAR);
						}else if(column.equals("V04002")) {
							value=calendar.get(Calendar.MONTH)+1;
						}else if(column.equals("V04003")) {
							value=calendar.get(Calendar.DAY_OF_MONTH);
						}else if(column.equals("V04004")) {
							value=calendar.get(Calendar.HOUR_OF_DAY);
						}else if(column.equals("V04005")) {
							value=calendar.get(Calendar.MINUTE);
						}
		    			if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
							value = members.get("CCCC");
						if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
							value = members.get("TTAAii");
						if(column.equalsIgnoreCase("V_BBB"))
							value = members.get("BBB");
						if(column.equalsIgnoreCase("D_RYMDHM"))
							value=((BufrBean)members.get("LastModified")).getValue();
						
		    			
		    			keyBuffer.append(",`").append(column).append("`");
		    			this.appendValue(valueBuffer, datatype, value.toString().trim());
		    			
		    			if(column.equals("V_WEIGHT_FACTOR") ){
		    				System.out.println("V_WEIGHT_FACTOR2");
		    			}
		    			
		    			if(column.equals("D_DATETIME")){
		    				value=ymdhm.format(calendar.getTime());
		    				int idx01 = value.toString().lastIndexOf(":");
		    				di.setDATA_TIME(value.toString().substring(0, idx01));
		    			}
		    			if(column.equals("V01301"))
		    				di.setIIiii(value.toString());
		    			if(column.equals("V05001"))
		    				di.setLATITUDE(value.toString());
		    			if(column.equals("V06001"))
		    				di.setLONGTITUDE(value.toString());
		    			else if(column.equals("V07001"))
		    				di.setHEIGHT(value.toString());
		    			if(column.equals("D_DATA_ID"))
		    				di.setDATA_TYPE(value.toString());
		    			else if(column.equals("V_BBB"))
		    				di.setDATA_UPDATE_FLAG(value.toString());
//			infoLogger.info("\n connection: fileName  " + fxy  + "-------" + value);
		    			
		    			
		    		}// end while
		    		Date recv=new Date();
		    		di.setTRAN_TIME(TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm"));
		    		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		    		di.setRECORD_TIME(TimeUtil.getSysTime());
		    		di.setFILE_SIZE(String.valueOf(fileN.length()));
		    		di.setBUSINESS_STATE("1");
		    		di.setPROCESS_STATE("1");
		    		
		    		di.setSEND("BFDB");
		    		di.setSEND_PHYS("DRDS");
		    		
		    		DI.add(di);
		    		if(isDwc) {
		    			// 键表入库
		    			String sql = "insert into " + tableName //
		    					+ " (" //
		    					+ keyBuffer.toString().substring(1) //
		    					+ ") values ("//
		    					+ valueBuffer.toString().substring(1) //
		    					+ ")";
		    			
		    			System.out.println("sql:"+sql);
		    			try {
		    				int executeUpdate = statement.executeUpdate(sql);
		    				connection.commit();
		    				if (executeUpdate > 0) {
		    					infoLogger.info(tableName + " Insert a record successfully！ " + fileName);
		    					Integer succeededNum = tmpRecord.get(SUCCEEDED_KEY);
		    					succeededNum = (succeededNum == null ? 1 : (succeededNum +=1));
		    					tmpRecord.put(SUCCEEDED_KEY, succeededNum);
		    				} else {
		    					infoLogger.info("插入失败:\n  " + sql);
		    					di.setPROCESS_STATE("0");
		    					Integer otherExceptionNum = tmpRecord.get(OTHER_EXCEPTION_KEY);
		    					otherExceptionNum = (otherExceptionNum == null ? 1 : (otherExceptionNum +=1));
		    					tmpRecord.put(OTHER_EXCEPTION_KEY, otherExceptionNum);
		    				}
		    			}catch (Exception e) {
		    				infoLogger.error("插入失败:\n " + sql + e.getMessage() + " " + fileName);
		    				di.setPROCESS_STATE("0");
		    			}
		    		}
		    		
		    	}
		    }
//				}
//			}
			
		}

	   
	   private void processIntoASPTable(DruidPooledConnection connection, Statement statement, boolean dateTimeOK,
				Map<String, Object> members, String tableName, String fileName, String d_record_id, String d_datetime, Map<String, XmlBean> configMap, List<String> least, int recileNum) {
			File fileN=new File(fileName);
			XmlBean xmlBean = configMap.get(tableName);
			Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
			Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
		    boolean isDwc=true;
		
		    	for(int i=0;i<recileNum;i++) {
		    		String []msg124=null;
		    		String []msg128=null;
		    		//对规整后的数据排序
		    		Collections.sort(least);
		    		for(int j=0;j<least.size();j++) {
//		    			System.out.println(least.get(j));
		    			if(least.get(j).startsWith("1-24-0-"+i)) {
		    				msg124=least.get(j).replace("=======", ":").replace("    ", ":").trim().split(":");
		    			}
		    			if(least.get(j).startsWith("1-28-0-0")) {
//		    				System.out.println(least.get(j));
		    				msg128=least.get(j).replace("=====", ":").replace("                                       ", ":").replace("    ", ":").trim().split(":");
				    		
		    			}
		    		}
//				System.out.println("=========================");
//				System.out.println(num+":"+Arrays.toString(msg116)+"==="+Arrays.toString(msg11));
//				if(num==1&&(msg116[2].equals("5.0")||msg116[2].equals("10.0")||msg116[2].equals("15.0"))) {
//					System.out.println("出现！");
//				}
		    		// 用于存储入库的字段
		    		StringBuffer keyBuffer = new StringBuffer();
		    		// 用于存储入库的字段值
		    		StringBuffer valueBuffer = new StringBuffer();
		    		// 遍历配置信息
		    		Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
//					
		    		
		    		StatDi di = new StatDi();
		    		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		    		di.setTT("BUFR");
		    		di.setDATA_TYPE_1(StartConfig.ctsCode());
		    		di.setFILE_NAME_N(fileName);
		    		di.setFILE_NAME_O(fileName);
		    		while (it.hasNext()) {
		    			Entry<String, EntityBean> next = it.next();
		    			String column = next.getKey(); // 配置文件配置的字段名称
		    			if(column.equals("V04005_S") ){
		    				System.out.println();
		    			}
		    			EntityBean entityBean = next.getValue(); // 配置的每个字段属性
		    			String datatype = entityBean.getDatatype(); // 字段类型
		    			String expression = entityBean.getExpression(); // 此字段的表达式
		    			String fxy = entityBean.getFxy(); // 解码时存储数据的key
		    			String defaultValue = entityBean.getDefaultValue(); // 缺省值
		    			Object obj = members.get(fxy); // 根据fxy获取每一行数据
		    			Calendar calendar = Calendar.getInstance();
		    			BufrBean beanYear = (BufrBean)members.get(entityMap.get("V04001").getFxy());
						BufrBean beanMonth = (BufrBean)members.get(entityMap.get("V04002").getFxy());
						BufrBean beanDayOfMonth = (BufrBean)members.get(entityMap.get("V04003").getFxy());
						BufrBean beanHourOfDay = (BufrBean)members.get(entityMap.get("V04004").getFxy());
						BufrBean beanStation = (BufrBean)members.get(entityMap.get("V01301").getFxy());
						calendar.set(Calendar.YEAR, (int)Double.parseDouble((beanYear.getValue().toString())));
						calendar.set(Calendar.MONTH, (int)Double.parseDouble((beanMonth.getValue().toString()))-1);
						calendar.set(Calendar.DAY_OF_MONTH, (int)Double.parseDouble((beanDayOfMonth.getValue().toString())));
						calendar.set(Calendar.HOUR_OF_DAY, (int)Double.parseDouble((beanHourOfDay.getValue().toString()))-1);
						if(msg124!=null&&msg124.length>0) {
							calendar.set(Calendar.MINUTE,(int)Double.parseDouble((msg124[2])));
						}else {
							calendar.set(Calendar.MINUTE,999999);
						}
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
		    			// 如果获取的对象为null
		    			Object value = null;
		    			if (obj == null) {
		    				// 如果表达式不为空说明此值需要根据表达式进行计算value
		    				if (StringUtils.isNotBlank(expression)) {
		    					// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
		    					Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
		    					if (em.find()) {
		    						value = defaultValue;
		    					} else {// 如果表达式存在，且其中没有以index取值
		    						value = calExpressionValue(obj, members, entityMap, entityBean);
		    					}
		    				} else {
		    					value = defaultValue;
		    					if(column.equalsIgnoreCase("D_SOURCE_ID"))
		    						value ="G.0016.0002.R001"+"_"+fileN.getName();
		    				}
		    				
		    				if(("D_RECORD_ID").equals(column)){
		    					System.out.println(value+"1");
		    					
//								ymdhm.format(calendar.getTime());
		    					String wmo=beanStation.getValue().toString();
		    					d_record_id=wmo+"_"+ymdhm1.format(calendar.getTime());
		    					value=d_record_id;
		    					
		    				}
		    				if(("D_DATETIME").equals(column)){
		    					d_datetime=ymdhm.format(calendar.getTime());
		    					value=d_datetime;
		    					try{
		    						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    						Date dataTime=simpleDateFormat.parse(value.toString());
									if(!TimeCheckUtil.checkTime(dataTime)){
										dateTimeOK = false;
										infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
										break;
									}
		    					}catch (Exception e) {
		    						dateTimeOK = false;
		    						infoLogger.error("资料时间错误！  " + fileName);
		    						break;
		    					}
		    				}
		    				//获取设备型号和序列号
		    			 if(msg128!=null&&msg128.length>0){
		    					if(column.equalsIgnoreCase("V02241")) {
		    						value=msg128[3];
		    					}
		    					if(column.equalsIgnoreCase("V02208")) {
		    						value=msg128[5];
		    					}
		    				}
		    			 if(msg124!=null&&msg124.length>0) {
		    				 if(column.equalsIgnoreCase("V04005_S")) {
		    					 value=msg124[2];
		    				 }
		    				 if(column.equalsIgnoreCase("V04006_S")) {
		    					 value=msg124[4];
		    				 }
		    				 if(column.equalsIgnoreCase("V_RECORD_SIGN")) {
		    					 value=msg124[6];
		    				 }
		    				 if(column.equalsIgnoreCase("V15700")) {
		    					 value=msg124[10];
		    				 }
		    				 if(column.equalsIgnoreCase("V12001")) {
		    					 value=Double.parseDouble(msg124[12])-273.15;
		    				 }
		    				 if(column.equalsIgnoreCase("V12202")) {
		    					 value=Double.parseDouble(msg124[14])-273.15;
		    				 }
		    				 if(column.equalsIgnoreCase("V13003")) {
		    					 value=msg124[16];
		    				 }
		    				 if(column.equalsIgnoreCase("V10004")) {
		    					 value=Double.parseDouble(msg124[18])*0.01;
		    				 }
		    			 }else {
		    				 infoLogger.error("观测时间缺失！"+fileName);
		    				 isDwc=false;
		    				 continue;
		    			 }
		    			 if(column.equals("V04001")) {
								value=calendar.get(Calendar.YEAR);
							}else if(column.equals("V04002")) {
								value=calendar.get(Calendar.MONTH)+1;
							}else if(column.equals("V04003")) {
								value=calendar.get(Calendar.DAY_OF_MONTH);
							}else if(column.equals("V04004")) {
								value=calendar.get(Calendar.HOUR_OF_DAY);
							}else if(column.equals("V04005")) {
								value=calendar.get(Calendar.MINUTE);
							}
		    			 if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
								value = members.get("CCCC");
							if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
								value = members.get("TTAAii");
							if(column.equalsIgnoreCase("V_BBB"))
								value = members.get("BBB");
							if(column.equalsIgnoreCase("D_RYMDHM"))
								value=((BufrBean)members.get("LastModified")).getValue();
							
		    				keyBuffer.append(",`").append(column).append("`");
		    				valueBuffer.append(",'").append(value.toString().trim()).append("'");
		    				if(column.equals("D_DATETIME")){
		    					value=d_datetime;
		    					int idx01 = value.toString().lastIndexOf(":");
		    					di.setDATA_TIME(value.toString().substring(0, idx01));
		    				}
		    				if(column.equals("V01301"))
		    					di.setIIiii(value.toString());
		    				if(column.equals("V05001"))
		    					di.setLATITUDE(value.toString());
		    				if(column.equals("V06001"))
		    					di.setLONGTITUDE(value.toString());
		    				else if(column.equals("V07001"))
		    					di.setHEIGHT(value.toString());
		    				if(column.equals("D_DATA_ID"))
		    					di.setDATA_TYPE(value.toString());
		    				else if(column.equals("V_BBB"))
		    					di.setDATA_UPDATE_FLAG(value.toString());
		    				continue;
		    			}
		    			
		    			// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//					System.err.println(column);
		    			value = calExpressionValue(obj, members, entityMap, entityBean);
		    			if("V14032".equals(column)&&!(value.toString().startsWith("999999"))){
		    				value=Double.parseDouble(value.toString());
		    			}	
		    			
		    			
		    			if(("D_DATETIME").equals(column)){
		    				d_datetime=ymdhm.format(calendar.getTime());
		    				value=d_datetime;
		    				try{
		    					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    					Date dataTime=simpleDateFormat.parse(value.toString());
								if(!TimeCheckUtil.checkTime(dataTime)){
									dateTimeOK = false;
									infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
									break;
								}
		    				}catch (Exception e) {
		    					dateTimeOK = false;
		    					infoLogger.error("资料时间错误！  " + fileName);
		    					break;
		    				}
		    			}
		    			
		    			//获取设备型号和序列号
		    			 if(msg128!=null&&msg128.length>0){
		    					if(column.equalsIgnoreCase("V02241")) {
		    						value=msg128[3];
		    					}
		    					if(column.equalsIgnoreCase("V02208")) {
		    						value=msg128[5];
		    					}
		    				}
		    			 if(msg124!=null&&msg124.length>0) {
		    				 if(column.equalsIgnoreCase("V04005_S")) {
		    					 value=msg124[2];
		    				 }
		    				 if(column.equalsIgnoreCase("V04006_S")) {
		    					 value=msg124[4];
		    				 }
		    				 if(column.equalsIgnoreCase("V_RECORD_SIGN")) {
		    					 value=msg124[6];
		    				 }
		    				 if(column.equalsIgnoreCase("V15700")) {
		    					 value=msg124[10];
		    				 }
		    				 if(column.equalsIgnoreCase("V12001")) {
		    					 value=Double.parseDouble(msg124[12])-273.15;
		    				 }
		    				 if(column.equalsIgnoreCase("V12202")) {
		    					 value=Double.parseDouble(msg124[14])-273.15;
		    				 }
		    				 if(column.equalsIgnoreCase("V13003")) {
		    					 value=msg124[16];
		    				 }
		    				 if(column.equalsIgnoreCase("V10004")) {
		    					 value=Double.parseDouble(msg124[18])*0.01;
		    				 }
		    			 }else {
		    				 infoLogger.error("观测时间缺失！"+fileName);
		    				 isDwc=false;
		    				 continue;
		    			 }
		    			 if(column.equals("V04001")) {
								value=calendar.get(Calendar.YEAR);
							}else if(column.equals("V04002")) {
								value=calendar.get(Calendar.MONTH)+1;
							}else if(column.equals("V04003")) {
								value=calendar.get(Calendar.DAY_OF_MONTH);
							}else if(column.equals("V04004")) {
								value=calendar.get(Calendar.HOUR_OF_DAY);
							}else if(column.equals("V04005")) {
								value=calendar.get(Calendar.MINUTE);
							}
		    			 if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
								value = members.get("CCCC");
							if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
								value = members.get("TTAAii");
							if(column.equalsIgnoreCase("V_BBB"))
								value = members.get("BBB");
							if(column.equalsIgnoreCase("D_RYMDHM"))
								value=((BufrBean)members.get("LastModified")).getValue();
							
		    			keyBuffer.append(",`").append(column).append("`");
		    			this.appendValue(valueBuffer, datatype, value.toString().trim());
		    			
//		    			if(column.equals("V_WEIGHT_FACTOR") ){
//		    				System.out.println("V_WEIGHT_FACTOR2");
//		    			}
		    			
		    			if(column.equals("D_DATETIME")){
		    				value=ymdhm.format(calendar.getTime());
		    				int idx01 = value.toString().lastIndexOf(":");
		    				di.setDATA_TIME(value.toString().substring(0, idx01));
		    			}
		    			if(column.equals("V01301"))
		    				di.setIIiii(value.toString());
		    			if(column.equals("V05001"))
		    				di.setLATITUDE(value.toString());
		    			if(column.equals("V06001"))
		    				di.setLONGTITUDE(value.toString());
		    			else if(column.equals("V07001"))
		    				di.setHEIGHT(value.toString());
		    			if(column.equals("D_DATA_ID"))
		    				di.setDATA_TYPE(value.toString());
		    			else if(column.equals("V_BBB"))
		    				di.setDATA_UPDATE_FLAG(value.toString());
//			infoLogger.info("\n connection: fileName  " + fxy  + "-------" + value);
		    			
		    			
		    		}// end while
		    		Date recv=new Date();
		    		di.setTRAN_TIME(TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm"));
		    		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		    		di.setRECORD_TIME(TimeUtil.getSysTime());
		    		di.setFILE_SIZE(String.valueOf(fileN.length()));
		    		di.setBUSINESS_STATE("1");
		    		di.setPROCESS_STATE("1");
		    		
		    		di.setSEND("BFDB");
		    		di.setSEND_PHYS("DRDS");
		    		
		    		DI.add(di);
		    		if(isDwc) {
		    			// 键表入库
		    			String sql = "insert into " + tableName //
		    					+ " (" //
		    					+ keyBuffer.toString().substring(1) //
		    					+ ") values ("//
		    					+ valueBuffer.toString().substring(1) //
		    					+ ")";
		    			
		    			System.out.println("sql:"+sql);
		    			try {
		    				int executeUpdate = statement.executeUpdate(sql);
		    				connection.commit();
		    				if (executeUpdate > 0) {
		    					infoLogger.info(tableName + " Insert a record successfully！ " + fileName);
		    					Integer succeededNum = tmpRecord.get(SUCCEEDED_KEY);
		    					succeededNum = (succeededNum == null ? 1 : (succeededNum +=1));
		    					tmpRecord.put(SUCCEEDED_KEY, succeededNum);
		    				} else {
		    					infoLogger.info("插入失败:\n  " + sql);
		    					di.setPROCESS_STATE("0");
		    					Integer otherExceptionNum = tmpRecord.get(OTHER_EXCEPTION_KEY);
		    					otherExceptionNum = (otherExceptionNum == null ? 1 : (otherExceptionNum +=1));
		    					tmpRecord.put(OTHER_EXCEPTION_KEY, otherExceptionNum);
		    				}
		    			}catch (Exception e) {
		    				infoLogger.error("插入失败:\n " + sql + e.getMessage() + " " + fileName);
		    				di.setPROCESS_STATE("0");
		    			}
		    		}
		    		
		    	}
		
//				}
//			}
			
		}

	   
	 /**
     * @param str 原字符串
     * @param sToFind 需要查找的字符串
     * @return 返回在原字符串中sToFind出现的次数
     */
    private int countStr(String str,String sToFind) {
        int num = 0;
        while (str.contains(sToFind)) {
            str = str.substring(str.indexOf(sToFind) + sToFind.length());
            num ++;
        }
        return num;
    }

	private void processIntoNormalTable(DruidPooledConnection connection, Statement statement, boolean dateTimeOK,
			Map<String, Object> members, String tableName, String fileName, String d_record_id, String d_datetime, Map<String, XmlBean> configMap, List<String> least, int recileNum) {
		File fileN=new File(fileName);
		XmlBean xmlBean = configMap.get(tableName);
		Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
		Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
	    boolean isDwc=true;
		for(int i=0;i<recileNum;i++) {
			
			String []msg116=null;
			
			//对规整后的数据排序
			Collections.sort(least);
			for(int j=0;j<least.size();j++) {
//				System.out.println(least.get(j));
				if(least.get(j).startsWith("1-16-0-"+i)) {
					msg116=least.get(j).replace("=======", ":").replace("    ", ":").trim().split(":");
				}
				
			}
//			System.out.println("=========================");
//			System.out.println(num+":"+Arrays.toString(msg116)+"==="+Arrays.toString(msg11));
//			if(num==1&&(msg116[2].equals("5.0")||msg116[2].equals("10.0")||msg116[2].equals("15.0"))) {
//				System.out.println("出现！");
//			}
			// 用于存储入库的字段
			StringBuffer keyBuffer = new StringBuffer();
			// 用于存储入库的字段值
			StringBuffer valueBuffer = new StringBuffer();
			// 遍历配置信息
		Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
//				
				
				StatDi di = new StatDi();
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setTT("BUFR");
				di.setDATA_TYPE_1(StartConfig.ctsCode());
				di.setFILE_NAME_N(fileName);
				di.setFILE_NAME_O(fileName);
				while (it.hasNext()) {
					Entry<String, EntityBean> next = it.next();
					String column = next.getKey(); // 配置文件配置的字段名称
					if(column.equals("V04005_S") ){
						System.out.println();
					}
					EntityBean entityBean = next.getValue(); // 配置的每个字段属性
					String datatype = entityBean.getDatatype(); // 字段类型
					String expression = entityBean.getExpression(); // 此字段的表达式
					String fxy = entityBean.getFxy(); // 解码时存储数据的key
					String defaultValue = entityBean.getDefaultValue(); // 缺省值
					Object obj = members.get(fxy); // 根据fxy获取每一行数据
					Calendar calendar = Calendar.getInstance();
					BufrBean beanYear = (BufrBean)members.get(entityMap.get("V04001").getFxy());
					BufrBean beanMonth = (BufrBean)members.get(entityMap.get("V04002").getFxy());
					BufrBean beanDayOfMonth = (BufrBean)members.get(entityMap.get("V04003").getFxy());
					BufrBean beanHourOfDay = (BufrBean)members.get(entityMap.get("V04004").getFxy());
					BufrBean beanStation = (BufrBean)members.get(entityMap.get("V01301").getFxy());
					calendar.set(Calendar.YEAR, (int)Double.parseDouble((beanYear.getValue().toString())));
					calendar.set(Calendar.MONTH, (int)Double.parseDouble((beanMonth.getValue().toString()))-1);
					calendar.set(Calendar.DAY_OF_MONTH, (int)Double.parseDouble((beanDayOfMonth.getValue().toString())));
					calendar.set(Calendar.HOUR_OF_DAY, (int)Double.parseDouble((beanHourOfDay.getValue().toString()))-1);
					if(msg116!=null&&msg116.length>0) {
						calendar.set(Calendar.MINUTE,(int)Double.parseDouble((msg116[2])));
					}else {
						calendar.set(Calendar.MINUTE,999999);
					}
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					// 如果获取的对象为null
					Object value = null;
					if (obj == null) {
						// 如果表达式不为空说明此值需要根据表达式进行计算value
						if (StringUtils.isNotBlank(expression)) {
							// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
							Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
							if (em.find()) {
								value = defaultValue;
							} else {// 如果表达式存在，且其中没有以index取值
								value = calExpressionValue(obj, members, entityMap, entityBean);
							}
						} else {
							value = defaultValue;
							if(column.equalsIgnoreCase("D_SOURCE_ID"))
								value ="G.0016.0002.R001"+"_"+fileN.getName();
						}
						
						if(("D_RECORD_ID").equals(column)){
							System.out.println(value+"1");
							
//							ymdhm.format(calendar.getTime());
							String wmo=beanStation.getValue().toString();
								d_record_id=wmo+"_"+ymdhm1.format(calendar.getTime());
								value=d_record_id;
							
						}
						if(("D_DATETIME").equals(column)){
							d_datetime=ymdhm.format(calendar.getTime());
							value=d_datetime;
							try{
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date dataTime=simpleDateFormat.parse(value.toString());
								if(!TimeCheckUtil.checkTime(dataTime)){
									dateTimeOK = false;
									infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
									break;
								}
							}catch (Exception e) {
								dateTimeOK = false;
								infoLogger.error("资料时间错误！  " + fileName);
								break;
							}
						}
						
						if(msg116!=null&&msg116.length>0) {
							if(column.equals("V04005_S") ){
								value=msg116[2];
							}
							if(column.equals("V04006_S") ){
								value=msg116[4];
							}
						}else {
							infoLogger.error("资料观测时间缺失！"+fileName);
							isDwc=false;
							continue;
						}
						if(column.equals("V04001")) {
							value=calendar.get(Calendar.YEAR);
						}else if(column.equals("V04002")) {
							value=calendar.get(Calendar.MONTH)+1;
						}else if(column.equals("V04003")) {
							value=calendar.get(Calendar.DAY_OF_MONTH);
						}else if(column.equals("V04004")) {
							value=calendar.get(Calendar.HOUR_OF_DAY);
						}else if(column.equals("V04005")) {
							value=calendar.get(Calendar.MINUTE);
						}
						if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
							value = members.get("CCCC");
						if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
							value = members.get("TTAAii");
						if(column.equalsIgnoreCase("V_BBB"))
							value = members.get("BBB");
						if(column.equalsIgnoreCase("D_RYMDHM"))
							value=((BufrBean)members.get("LastModified")).getValue();
						
						keyBuffer.append(",`").append(column).append("`");
						valueBuffer.append(",'").append(value.toString().trim()).append("'");
						if(column.equals("D_DATETIME")){
							value=ymdhm.format(calendar.getTime());
							int idx01 = value.toString().lastIndexOf(":");
							di.setDATA_TIME(value.toString().substring(0, idx01));
						}
						if(column.equals("V01301"))
							di.setIIiii(value.toString());
						if(column.equals("V05001"))
							di.setLATITUDE(value.toString());
						if(column.equals("V06001"))
							di.setLONGTITUDE(value.toString());
						else if(column.equals("V07001"))
							di.setHEIGHT(value.toString());
						if(column.equals("D_DATA_ID"))
							di.setDATA_TYPE(value.toString());
						else if(column.equals("V_BBB"))
							di.setDATA_UPDATE_FLAG(value.toString());
						continue;
					}
					
					// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//				System.err.println(column);
					value = calExpressionValue(obj, members, entityMap, entityBean);
					if("V14032".equals(column)&&!(value.toString().startsWith("999999"))){
						value=Double.parseDouble(value.toString());
					}	
					
					
					if(("D_DATETIME").equals(column)){
						d_datetime=ymdhm.format(calendar.getTime());
						value=d_datetime;
						try{
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date dataTime=simpleDateFormat.parse(value.toString());
							if(!TimeCheckUtil.checkTime(dataTime)){
								dateTimeOK = false;
								infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
								break;
							}
						}catch (Exception e) {
							dateTimeOK = false;
							infoLogger.error("资料时间错误！  " + fileName);
							break;
						}
					}
					
					
					if(msg116!=null&&msg116.length>0) {
						if(column.equals("V04005_S") ){
							value=msg116[2];
						}
						if(column.equals("V04006_S") ){
							value=msg116[4];
						}
					}else {
						infoLogger.error("资料观测时间缺失！"+fileName);
						isDwc=false;
						continue;
					}
					if(column.equals("V04001")) {
						value=calendar.get(Calendar.YEAR);
					}else if(column.equals("V04002")) {
						value=calendar.get(Calendar.MONTH)+1;
					}else if(column.equals("V04003")) {
						value=calendar.get(Calendar.DAY_OF_MONTH);
					}else if(column.equals("V04004")) {
						value=calendar.get(Calendar.HOUR_OF_DAY);
					}else if(column.equals("V04005")) {
						value=calendar.get(Calendar.MINUTE);
					}
					if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
						value = members.get("CCCC");
					if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
						value = members.get("TTAAii");
					if(column.equalsIgnoreCase("V_BBB"))
						value = members.get("BBB");
					if(column.equalsIgnoreCase("D_RYMDHM"))
						value=((BufrBean)members.get("LastModified")).getValue();
					
					
					keyBuffer.append(",`").append(column).append("`");
					this.appendValue(valueBuffer, datatype, value.toString().trim());
					
					if(column.equals("V_WEIGHT_FACTOR") ){
						System.out.println("V_WEIGHT_FACTOR2");
					}
					
					if(column.equals("D_DATETIME")){
						value=ymdhm.format(calendar.getTime());
						int idx01 = value.toString().lastIndexOf(":");
						di.setDATA_TIME(value.toString().substring(0, idx01));
					}
					if(column.equals("V01301"))
						di.setIIiii(value.toString());
					if(column.equals("V05001"))
						di.setLATITUDE(value.toString());
					if(column.equals("V06001"))
						di.setLONGTITUDE(value.toString());
					else if(column.equals("V07001"))
						di.setHEIGHT(value.toString());
					if(column.equals("D_DATA_ID"))
						di.setDATA_TYPE(value.toString());
					else if(column.equals("V_BBB"))
						di.setDATA_UPDATE_FLAG(value.toString());
//		infoLogger.info("\n connection: fileName  " + fxy  + "-------" + value);
					
					
				}// end while
				Date recv=new Date();
				di.setTRAN_TIME(TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setFILE_SIZE(String.valueOf(fileN.length()));
				di.setBUSINESS_STATE("1");
				di.setPROCESS_STATE("1");
				
				di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				
				DI.add(di);
				if(isDwc) {
					// 键表入库
					String sql = "insert into " + tableName //
							+ " (" //
							+ keyBuffer.toString().substring(1) //
							+ ") values ("//
							+ valueBuffer.toString().substring(1) //
							+ ")";
					
					System.out.println("sql:"+sql);
					try {
						int executeUpdate = statement.executeUpdate(sql);
						connection.commit();
						if (executeUpdate > 0) {
							infoLogger.info(tableName + " Insert a record successfully！ " + fileName);
							Integer succeededNum = tmpRecord.get(SUCCEEDED_KEY);
							succeededNum = (succeededNum == null ? 1 : (succeededNum +=1));
							tmpRecord.put(SUCCEEDED_KEY, succeededNum);
						} else {
							infoLogger.info("插入失败:\n  " + sql);
							di.setPROCESS_STATE("0");
							Integer otherExceptionNum = tmpRecord.get(OTHER_EXCEPTION_KEY);
							otherExceptionNum = (otherExceptionNum == null ? 1 : (otherExceptionNum +=1));
							tmpRecord.put(OTHER_EXCEPTION_KEY, otherExceptionNum);
						}
					}catch (Exception e) {
						infoLogger.error("插入失败:\n " + sql + e.getMessage() + " " + fileName);
						di.setPROCESS_STATE("0");
					}
				}
				
			}
//			}
//		}
		
	}

	private void processIntoPMMTable(DruidPooledConnection connection, Statement statement, boolean dateTimeOK,
			Map<String, Object> members, String tableName, String fileName, String d_record_id, String d_datetime, Map<String, XmlBean> configMap, int num, List<String> least, int recileNum) {
		File fileN=new File(fileName);
		XmlBean xmlBean = configMap.get(tableName);
		Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
		Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
	    boolean isDwc=true;
		for(int i=0;i<recileNum;i++) {
			
			String []msg116=null;
			String []msg11=null;
			String []msg11s=null;
			//对规整后的数据排序
			Collections.sort(least);
			for(int j=0;j<least.size();j++) {
//				System.out.println(least.get(j));
				if(least.get(j).startsWith("1-16-0-"+i)) {
					msg116=least.get(j).replace("=======", ":").replace("    ", ":").trim().split(":");
				}
				if(least.get(j).startsWith("s1-1-0-"+i)) {
					msg11s=least.get(j).replace("=====", ":").replace("    ", ":").trim().split(":");
				}
				if(least.get(j).startsWith("1-1-0-"+i)) {
					msg11=least.get(j).replace("=====", ":").replace("    ", ":").trim().split(":");
				}
				
			}
			String type=null;
			String dataId=null;
			if(num==1) {
				type="PM10";
				dataId="G.0006.0007.S001";
			}else if(num==2) {
				type="PM2.5";
				dataId="G.0006.0008.S001";
			}else {
				type="PM1";
				dataId="G.0006.0032.S001";
			}
			// 用于存储入库的字段
			StringBuffer keyBuffer = new StringBuffer();
			// 用于存储入库的字段值
			StringBuffer valueBuffer = new StringBuffer();
			// 遍历配置信息
		Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
//				
				
				StatDi di = new StatDi();
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setTT("BUFR");
				di.setDATA_TYPE_1(StartConfig.ctsCode());
				di.setFILE_NAME_N(fileName);
				di.setFILE_NAME_O(fileName);
				while (it.hasNext()) {
					Entry<String, EntityBean> next = it.next();
					String column = next.getKey(); // 配置文件配置的字段名称
					
					EntityBean entityBean = next.getValue(); // 配置的每个字段属性
					String datatype = entityBean.getDatatype(); // 字段类型
					String expression = entityBean.getExpression(); // 此字段的表达式
					String fxy = entityBean.getFxy(); // 解码时存储数据的key
					if(column.equalsIgnoreCase("V02241")||column.equalsIgnoreCase("V02208")||column.equalsIgnoreCase("V02242")) {
						fxy="1-21-0-"+(num-1);
					}
					String defaultValue = entityBean.getDefaultValue(); // 缺省值
					Object obj = members.get(fxy); // 根据fxy获取每一行数据
					// 如果获取的对象为null
					Object value = null;
					Calendar calendar = Calendar.getInstance();
					BufrBean beanYear = (BufrBean)members.get(entityMap.get("V04001").getFxy());
					BufrBean beanMonth = (BufrBean)members.get(entityMap.get("V04002").getFxy());
					BufrBean beanDayOfMonth = (BufrBean)members.get(entityMap.get("V04003").getFxy());
					BufrBean beanHourOfDay = (BufrBean)members.get(entityMap.get("V04004").getFxy());
					BufrBean beanStation = (BufrBean)members.get(entityMap.get("V01301").getFxy());
					calendar.set(Calendar.YEAR, (int)Double.parseDouble((beanYear.getValue().toString())));
					calendar.set(Calendar.MONTH, (int)Double.parseDouble((beanMonth.getValue().toString()))-1);
					calendar.set(Calendar.DAY_OF_MONTH, (int)Double.parseDouble((beanDayOfMonth.getValue().toString())));
					calendar.set(Calendar.HOUR_OF_DAY, (int)Double.parseDouble((beanHourOfDay.getValue().toString()))-1);
					if(msg116!=null&&msg116.length>0) {
						calendar.set(Calendar.MINUTE,(int)Double.parseDouble((msg116[2])));
					}else {
						calendar.set(Calendar.MINUTE,999999);
					}
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					if (obj == null) {
						// 如果表达式不为空说明此值需要根据表达式进行计算value
						if (StringUtils.isNotBlank(expression)) {
							// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
							Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
							if (em.find()) {
								value = defaultValue;
							} else {// 如果表达式存在，且其中没有以index取值
								value = calExpressionValue(obj, members, entityMap, entityBean);
							}
						} else {
							value = defaultValue;
							if(column.equalsIgnoreCase("D_SOURCE_ID"))
								value ="G.0016.0002.R001"+"_"+fileN.getName();
						}
						if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
							value = members.get("CCCC");
						if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
							value = members.get("TTAAii");
						if(column.equalsIgnoreCase("V_BBB"))
							value = members.get("BBB");
						if(column.equalsIgnoreCase("D_RYMDHM"))
							value=((BufrBean)members.get("LastModified")).getValue();
						
						
						if(("D_RECORD_ID").equals(column)){
//							System.out.println(value+"1");
//							ymdhm.format(calendar.getTime());
							String wmo=beanStation.getValue().toString();
								d_record_id=wmo+"_"+ymdhm1.format(calendar.getTime())+"_"+type;
								value=d_record_id;
							
						}
						if(("D_DATA_ID").equals(column)) {
							value=dataId;
						}
						if(("D_DATETIME").equals(column)){
							d_datetime=ymdhm.format(calendar.getTime());
							value=d_datetime;
							try{
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date dataTime=simpleDateFormat.parse(value.toString());
								if(!TimeCheckUtil.checkTime(dataTime)){
									dateTimeOK = false;
									infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
									break;
								}
							}catch (Exception e) {
								dateTimeOK = false;
								infoLogger.error("资料时间错误！  " + fileName);
								break;
							}
						}
						if("V_CAWN_TYPE".equals(column)) {
							value=type;
						}
						if(msg11s!=null&&msg11s.length>0) {
							if(column.equals("V15730")) {
								value=msg11s[3];
							}
						}
						if(msg116!=null&&msg116.length>0) {
							if(column.equals("V04005_S") ){
								value=msg116[2];
//								System.out.println("V04005_S:"+value);
								if(msg116[2].trim().equalsIgnoreCase("55.0")) {
									num++;
								}
							}
							if(column.equals("V04006_S") ){
								value=msg116[4];
							}
							if(column.equals("V15471") ){
								value=msg116[8];
							}
							if(column.equals("V01324") ){
								value=msg116[10];
							}
						}else {
							infoLogger.error("观测时间缺失！"+fileName);
							isDwc=false;
							continue;
						}
						if(msg11!=null&&msg11.length>0) {
							if(column.equals("V_STORAGE_PLACE")) {
								value=msg11[3];
							}
							if(column.equals("V_WEIGHT_FACTOR")) {
								value=msg11[5];
							}
							if(column.equals("V_ERROR_CODE")) {
								value=msg11[7];
							}
							if(column.equals("V15752")) {
								value=msg11[9];
							}
							if(column.equals("V15765")) {
								value=msg11[11];
							}
							if(column.equals("V_CORRECT_COUNT")) {
								value=msg11[13];
							}
							if(column.equals("V10004_040")) {
								value=msg11[15];
							}
							if(column.equals("V13003_040")) {
								value=msg11[19];
							}
							if(column.equals("V12001_040")) {
								value=msg11[21];
							}
							if(column.equals("V_TIME_SPACING")) {
								value=msg11[23];
							}
							if(column.equals("V11002_071")) {
								value=msg11[25];
							}
							if(column.equals("V11001_071")) {
								value=msg11[27];
							}
							if(column.equals("V13011_071")) {
								value=msg11[29];
							}
							if(column.equals("V12200")) {
								value=msg11[31];
							}
							if(column.equals("V13198")) {
								value=msg11[33];
							}
							if(column.equals("V10196")) {
								value=msg11[35];
							}
							if(column.equals("V12204")) {
								value=msg11[37];
							}
							if(column.equals("V13199")) {
								value=msg11[39];
							}
							if(column.equals("V10197")) {
								value=msg11[41];
							}
							if(column.equals("V11194")) {
								value=msg11[43];
							}
							if(column.equals("V11195")) {
								value=msg11[45];
							}
							if(column.equals("V02440")) {
								value=msg11[47];
							}
							if(column.equals("V10004")) {
								value=Double.parseDouble(msg11[49])*0.01;
							}
							if(column.equals("V13003")) {
								value=msg11[51];
							}
							if(column.equals("V12001")) {
								value=msg11[53];
							}
							if(column.equals("V11001")) {
								value=Double.parseDouble(msg11[55])-273.15;
							}
							if(column.equals("V11002")) {
								value=msg11[57];
							}
							if(column.equals("V13011")) {
								if(msg11[59].trim().startsWith("-0.1")) {
									value = "999990";
								}else {
									value=msg11[59];
								}
							}
						}
						if(column.equals("V04001")) {
							value=calendar.get(Calendar.YEAR);
						}else if(column.equals("V04002")) {
							value=calendar.get(Calendar.MONTH)+1;
						}else if(column.equals("V04003")) {
							value=calendar.get(Calendar.DAY_OF_MONTH);
						}else if(column.equals("V04004")) {
							value=calendar.get(Calendar.HOUR_OF_DAY);
						}else if(column.equals("V04005")) {
							value=calendar.get(Calendar.MINUTE);
						}
						keyBuffer.append(",`").append(column).append("`");
						valueBuffer.append(",'").append(value.toString().trim()).append("'");
						if(column.equals("D_DATETIME")){
							value=ymdhm.format(calendar.getTime());
							int idx01 = value.toString().lastIndexOf(":");
							di.setDATA_TIME(value.toString().substring(0, idx01));
						}
						if(column.equals("V01301"))
							di.setIIiii(value.toString());
						if(column.equals("V05001"))
							di.setLATITUDE(value.toString());
						if(column.equals("V06001"))
							di.setLONGTITUDE(value.toString());
						else if(column.equals("V07001"))
							di.setHEIGHT(value.toString());
						if(column.equals("D_DATA_ID"))
							di.setDATA_TYPE(value.toString());
						else if(column.equals("V_BBB"))
							di.setDATA_UPDATE_FLAG(value.toString());
						continue;
					}
					
					// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//				System.err.println(column);
					value = calExpressionValue(obj, members, entityMap, entityBean);
					if("V14032".equals(column)&&!(value.toString().startsWith("999999"))){
						value=Double.parseDouble(value.toString());
					}	
					
					
					if(("D_DATETIME").equals(column)){
						d_datetime=ymdhm.format(calendar.getTime());
						value=d_datetime;
						try{
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date dataTime=simpleDateFormat.parse(value.toString());
							if(!TimeCheckUtil.checkTime(dataTime)){
								dateTimeOK = false;
								infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
								break;
							}
						}catch (Exception e) {
							dateTimeOK = false;
							infoLogger.error("资料时间错误！  " + fileName);
							break;
						}
					}
					if("V_CAWN_TYPE".equals(column)) {
						value=type;
					}
					if(msg11s!=null&&msg11s.length>0) {
						if(column.equals("V15730")) {
							value=msg11s[3];
						}
					}
					if(msg116!=null&&msg116.length>0) {
						if(column.equals("V04005_S") ){
							value=msg116[2];
							System.out.println("V04005_S:"+value);
							if(msg116[2].trim().equalsIgnoreCase("55.0")) {
								num++;
							}
						}
						if(column.equals("V04006_S") ){
							value=msg116[4];
						}
						if(column.equals("V15471") ){
							value=msg116[8];
						}
						if(column.equals("V01324") ){
							value=msg116[10];
						}
					}else {
						infoLogger.error("观测时间缺失！"+fileName);
						isDwc=false;
						continue;
					}
					if(msg11!=null&&msg11.length>0) {
						if(column.equals("V_STORAGE_PLACE")) {
							value=msg11[3];
						}
						if(column.equals("V_WEIGHT_FACTOR")) {
							value=msg11[5];
						}
						if(column.equals("V_ERROR_CODE")) {
							value=msg11[7];
						}
						if(column.equals("V15752")) {
							value=msg11[9];
						}
						if(column.equals("V15765")) {
							value=msg11[11];
						}
						if(column.equals("V_CORRECT_COUNT")) {
							value=msg11[13];
						}
						if(column.equals("V10004_040")) {
							value=msg11[15];
						}
						if(column.equals("V13003_040")) {
							value=msg11[19];
						}
						if(column.equals("V12001_040")) {
							value=msg11[21];
						}
						if(column.equals("V_TIME_SPACING")) {
							value=msg11[23];
						}
						if(column.equals("V11002_071")) {
							value=msg11[25];
						}
						if(column.equals("V11001_071")) {
							value=msg11[27];
						}
						if(column.equals("V13011_071")) {
							value=msg11[29];
						}
						if(column.equals("V12200")) {
							value=msg11[31];
						}
						if(column.equals("V13198")) {
							value=msg11[33];
						}
						if(column.equals("V10196")) {
							value=msg11[35];
						}
						if(column.equals("V12204")) {
							value=msg11[37];
						}
						if(column.equals("V13199")) {
							value=msg11[39];
						}
						if(column.equals("V10197")) {
							value=msg11[41];
						}
						if(column.equals("V11194")) {
							value=msg11[43];
						}
						if(column.equals("V11195")) {
							value=msg11[45];
						}
						if(column.equals("V02440")) {
							value=msg11[47];
						}
						if(column.equals("V10004")) {
							value=Double.parseDouble(msg11[49])*0.01;
						}
						if(column.equals("V13003")) {
							value=msg11[51];
						}
						if(column.equals("V12001")) {
							value=msg11[53];
						}
						if(column.equals("V11001")) {
							value=Double.parseDouble(msg11[55])-273.15;
						}
						if(column.equals("V11002")) {
							value=msg11[57];
						}
						if(column.equals("V13011")) {
							if(msg11[59].trim().startsWith("-0.1")) {
								value = "999990";
							}else {
								value=msg11[59];
							}
						}
					}
					if(column.equals("V04001")) {
						value=calendar.get(Calendar.YEAR);
					}else if(column.equals("V04002")) {
						value=calendar.get(Calendar.MONTH)+1;
					}else if(column.equals("V04003")) {
						value=calendar.get(Calendar.DAY_OF_MONTH);
					}else if(column.equals("V04004")) {
						value=calendar.get(Calendar.HOUR_OF_DAY);
					}else if(column.equals("V04005")) {
						value=calendar.get(Calendar.MINUTE);
					}
					if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
						value = members.get("CCCC");
					if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
						value = members.get("TTAAii");
					if(column.equalsIgnoreCase("V_BBB"))
						value = members.get("BBB");
					if(column.equalsIgnoreCase("D_RYMDHM"))
						value=((BufrBean)members.get("LastModified")).getValue();
					
					keyBuffer.append(",`").append(column).append("`");
					this.appendValue(valueBuffer, datatype, value.toString().trim());
					
				
					if(column.equals("D_DATETIME")){
						value=d_datetime;
						int idx01 = value.toString().lastIndexOf(":");
						di.setDATA_TIME(value.toString().substring(0, idx01));
					}
					if(column.equals("V01301"))
						di.setIIiii(value.toString());
					if(column.equals("V05001"))
						di.setLATITUDE(value.toString());
					if(column.equals("V06001"))
						di.setLONGTITUDE(value.toString());
					else if(column.equals("V07001"))
						di.setHEIGHT(value.toString());
					if(column.equals("D_DATA_ID"))
						di.setDATA_TYPE(value.toString());
					else if(column.equals("V_BBB"))
						di.setDATA_UPDATE_FLAG(value.toString());
//		infoLogger.info("\n connection: fileName  " + fxy  + "-------" + value);
					
					
				}// end while
				Date recv=new Date();
				di.setTRAN_TIME(TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setFILE_SIZE(String.valueOf(fileN.length()));
				di.setBUSINESS_STATE("1");
				di.setPROCESS_STATE("1");
				
				di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				
				DI.add(di);
				if(isDwc) {
					// 键表入库
					String sql = "insert into " + tableName //
							+ " (" //
							+ keyBuffer.toString().substring(1) //
							+ ") values ("//
							+ valueBuffer.toString().substring(1) //
							+ ")";
					
					System.out.println("sql:"+sql);
					try {
						int executeUpdate = statement.executeUpdate(sql);
						connection.commit();
						if (executeUpdate > 0) {
							infoLogger.info(tableName + " Insert a record successfully！ " + fileName);
							Integer succeededNum = tmpRecord.get(SUCCEEDED_KEY);
							succeededNum = (succeededNum == null ? 1 : (succeededNum +=1));
							tmpRecord.put(SUCCEEDED_KEY, succeededNum);
						} else {
							infoLogger.info("插入失败:\n  " + sql);
							di.setPROCESS_STATE("0");
							Integer otherExceptionNum = tmpRecord.get(OTHER_EXCEPTION_KEY);
							otherExceptionNum = (otherExceptionNum == null ? 1 : (otherExceptionNum +=1));
							tmpRecord.put(OTHER_EXCEPTION_KEY, otherExceptionNum);
						}
					}catch (Exception e) {
						infoLogger.error("插入失败:\n " + sql + e.getMessage() + " " + fileName);
						di.setPROCESS_STATE("0");
					}
				}
				
			}
//			}
//		}
		
	}

	/**
	 * Gets the low or mid cloud amount.
	 *
	 * @param string the string
	 * @return the low or mid cloud amount
	 */
	String getLowOrMidCloudAmount(String string){
		String str = string;
		switch(string){
			case "0.0":
				str = "0";
				break;
			case "1.0":
				str = "10";
				break;
			case "2.0":
				str = "25";
				break;
			case "3.0":
				str = "40";
				break;
			case "4.0":
				str = "50";
				break;
			case "5.0":
				str = "60";
				break;
			case "6.0":
				str = "75";
				break;
			case "7.0":
				str = "90";
				break;
			case "8.0":
				str = "100";
				break;
			case "999999.0":
				str = "999999";
			    break;
			default:
				str = "999998";
		}
		return str;
	}
	
	/**
	 * Gets the cycle value.
	 *
	 * @param expression the expression
	 * @param obj the obj
	 * @param entityBean the entity bean
	 * @return the cycle value
	 */
	private Object getCycleValue(String expression, Object obj, EntityBean entityBean) {
		List<BufrBean> bufrList = (List<BufrBean>) obj;
		int index = Integer.parseInt(expression);
		Object value = entityBean.getDefaultValue();
		for(int i = 0; i < bufrList.size(); i += 2){ // 拿到时间周期数
			int cycle = (int)Double.parseDouble(bufrList.get(i).getValue().toString());
			if(index == cycle){
				if(!entityBean.isQc())
					value = bufrList.get(i + 1).getValue();
				else
					if(bufrList.get(i + 1).getQcValues() != null) // 省级质控码
						value = bufrList.get(i + 1).getQcValues().get(0);
				break;
			}
		}
		return value;
	}

	/**
	 * Cal expression value.
	 *
	 * @param obj the obj
	 * @param members the members
	 * @param entityMap the entity map
	 * @param entityBean the entity bean
	 * @return the object
	 */
	private Object calExpressionValue(Object obj, Map<String, Object> members, Map<String, EntityBean> entityMap, EntityBean entityBean) {
		String value = null;
		String expression = entityBean.getExpression();
		if (obj != null) {
			expression = getColumnValue(obj, entityBean).toString();
		}
		if (StringUtils.isNotBlank(expression)) {

			// ${col:V01301}
			// 将带有col的表达式替换掉
			Matcher columnMatcher = Pattern.compile(COLUMN_EXP_REGEX).matcher(expression);
			while (columnMatcher.find()) {
				String cExp = columnMatcher.group(0); // 带有${col:}的表达式
				String expCol = columnMatcher.group(1); // 字段
				// 获取表达式代表的配置
				String fxy =null;
				EntityBean ebean =null;
				if(expCol.equalsIgnoreCase("V04003")) {}
				else {
					ebean = entityMap.get(expCol);
					fxy = ebean.getFxy();
				}
				// 获取表达式代表的对象
				Object obj2 = members.get(fxy);
				if (obj2 == null) {
					// 如果表达式获取字段值为空，直接返回默认值
					return entityBean.getDefaultValue();
				} else {
					// 如果不为空，获取其对应的值
					String columnValue = getColumnValue(obj2, ebean).toString();
					if (columnValue.equals(ebean.getDefaultValue())) {
						return columnValue;
					}
					if(ebean.getDatatype().equals("int")){
						int idx = columnValue.indexOf(".");
						if(idx >= 1)
							columnValue = columnValue.substring(0, idx);
					}
					String afterText = expression.substring(expression.indexOf(cExp));
					String cExpReg = StringUtil.string2Regex(cExp);
					afterText = afterText.replaceFirst(cExpReg, "");
					if (afterText.startsWith(StringUtil.SPLIT_FLAG) || afterText.startsWith(StringUtil.SUBSTRING_FLAG)) {
						// 存储整串表达式代表的值,list最后一个值即为最终结果
						List<String> res = new ArrayList<String>();
						// 用于存储一整串substring和split表达式，最后替换直值用
						StringBuffer regSBuffer = new StringBuffer();
						
						StringUtil.formatString(columnValue, afterText, res, regSBuffer);
						String resByStation = res.get(res.size() - 1); // 最终结果
						expression = expression.replaceFirst(StringUtil.string2Regex(cExp + regSBuffer.toString()), resByStation);
					} else {
						expression = expression.replaceFirst(cExpReg, columnValue);
					}

				}
			}

			// 替换所有默认表达式
			expression = StringUtil.formatExpressionContent(expression);
			// StringUtil.replaceNumberExp(expression, texts);
			// 替换${this}
			Matcher thisMatcher = Pattern.compile(THIS_VALUE_EXP).matcher(expression);
			while (thisMatcher.find()) {
				if (StringUtils.isBlank(value)) {
					return entityBean.getDefaultValue();
				}
				if(value != null && StringUtil.string2Regex(thisMatcher.group(0)) != null)
					expression = expression.replaceFirst(StringUtil.string2Regex(thisMatcher.group(0)), value); // 如果value为null怎么办？
			}
			//转换字符串为ascii码
			expression = StringUtil.formatStringAsciiExp(expression);

		}
		
		value = StringUtil.calculteExp(expression);
		if(value.equalsIgnoreCase("null"))
			value = "999999";
		return value;
	}

	/**
	 * Gets the column value.
	 *
	 * @param obj the obj
	 * @param entityBean the entity bean
	 * @return the column value
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 */
	private Object getColumnValue(Object obj, EntityBean entityBean) throws IndexOutOfBoundsException {
		boolean isQc = entityBean.isQc();
		// int index = entityBean.getIndex();
		Object value = null;
		// 如果是bufrBean对象，直接强制转换
		if (obj instanceof BufrBean) {
			BufrBean bufr = (BufrBean) obj;
			if (isQc) {
				if(bufr.getQcValues() != null)
					value = bufr.getQcValues().get(0);
				else 
					value = 9;
				int parseValue = Integer.parseInt(value.toString());
				value = parseValue > 9 ? 9 : parseValue;
			} else {
				// commented by cuihongyuan
//				value = bufr.getValue();
				String expression = entityBean.getExpression();
				if (StringUtils.isNotBlank(expression)) {
					Matcher numMatcher = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
					if (numMatcher.find()) {
						List<String> ts = new ArrayList<String>();
						String v = bufr.getValue().toString();
						
						if(entityBean.getDatatype().equalsIgnoreCase("double") ){
							BigDecimal bigDecimal = new BigDecimal(v);
							bufr.setValue(bigDecimal.toPlainString());
						}
						ts.add(bufr.getValue().toString());
						value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
					}else {
						infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
					}
				}else {
					value = bufr.getValue();
				}
			}
		} else { // 不是bufrBean对象，那就是存储对象的list集合
			@SuppressWarnings("unchecked")
			List<BufrBean> bufrList = (List<BufrBean>) obj;

			String expression = entityBean.getExpression();
			if (StringUtils.isNotBlank(expression)) {
				Matcher numMatcher = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
				if (numMatcher.find()) {

					String _index = numMatcher.group(1);
					int index = Integer.parseInt(_index);
					// 如果list大小小于index，说明index代表的值不存在，直接返回默认值即可
					if ((index + 1) > bufrList.size()) {
						return entityBean.getDefaultValue();
					}

					List<String> ts = new ArrayList<String>();
					if (isQc) {
						for (BufrBean bufrBean : bufrList) {
							Object value2 = null;
							if(bufrBean.getQcValues() != null)
								value2 = bufrBean.getQcValues().get(0);
							else 
								value2 = 9;
							int parseValue = Integer.parseInt(value2.toString());
							value2 = parseValue > 9 ? 9 : parseValue;
							ts.add(value2.toString());
						}
					} else {
						for (BufrBean bufrBean : bufrList) {
							Object value2 = bufrBean.getValue();						
							if(entityBean.getDatatype().equalsIgnoreCase("double") ){
								BigDecimal bigDecimal = new BigDecimal(value2.toString());
								bufrBean.setValue(bigDecimal.toPlainString());
							}
							ts.add(bufrBean.getValue().toString());
						}
					}

					value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
				} else {
					infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
				}
			} else {
				infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
			}

		}
		return value;
	}

	/**
	 * Append value.
	 *
	 * @param buffer the buffer
	 * @param dataType the data type
	 * @param value the value
	 */
	private void appendValue(StringBuffer buffer, String dataType, Object value) {
		if ("int".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("short".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("byte".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("double".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("float".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("long".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("date".equalsIgnoreCase(dataType)) {
			buffer.append(",'").append(value).append("'"); // TODO
		} else {
			buffer.append(",'").append(value).append("'");
		}
	}


}
