package cma.cimiss2.dpc.decoder.upar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.upar.WindProfileData;
import cma.cimiss2.dpc.decoder.bean.upar.WindProfileRada;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import ucar.ma2.Array;
import ucar.nc2.Group;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;
/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description:</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName:</b> cimiss2-decode
* <br><b>PackageName:</b> cma.cimiss2.dpc.decoder.upar
* <br><b>ClassName: 解析风廓线雷达数据  NETCDF 格式</b> DecodeWindProfileNC
* <br><b>Date:</b> 2019年5月30日 上午9:28:28
 */
public class DecodeWindProfileNC {
	
	private final String groupNames = "WNDROBSI,WNDHOBSI,WNDOOBSI";
	
	/**
	 * 数据解析函数 
	 * @param filename 文件绝对路径
	 * @return  ParseResult<WindProfileRada>  解析结果集
	 */
	public ParseResult<WindProfileRada> decodeWindProFileNC(String filename){
		ParseResult<WindProfileRada> parseResult = new ParseResult<>(false);
		// 判断文件是否存在
		if(!(new File(filename).exists())){
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}else {
			// 如果文件存在，则解析文件内容
			NetcdfDataset netcdfDataset = null;
			try {
				netcdfDataset = NetcdfDataset.openDataset(filename);
				// 获取全局的属性信息[StationName = "54511", StationNumber = "54511", Longitude = "116.47", Latitude = "39.8069", Altitude = 32.5, RadarType = "LC", Beam_num = 5, ScanBeamF = "RNESW/", BeamE_Angle = 14.2, BeamW_Angle = 14.2, BeamS_Angle = 14.3, BeamN_Angle = 14.3, AngleE_V = 182.0, AngleW_V = 182.0, AngleS_V = 182.0, AngleN_V = 182.0, Mode_num = 3, Mode1_lowhgt = 150, Mode1_highhgt = 3630, Mode1_res = 120, Mode2_lowhgt = 1110, Mode2_highhgt = 4590, Mode2_res = 120, Mode3_lowhgt = 3150, Mode3_highhgt = 10110, Mode3_res = 240, Obstime_start = "2019-05-07 00:00:00", Obstime_end = "2019-05-08 00:00:00", Label = "", version = "01.20", _CoordSysBuilder = "ucar.nc2.dataset.conv.DefaultConvention"]
				Group rootGroup = netcdfDataset.getRootGroup();
				if(rootGroup == null){
					parseResult.put(new ReportError("DOSE NOT FIND ROOT GROUP"));
					return parseResult;
				}
				for (String groupName : groupNames.split(",")) {
					
					Group group = netcdfDataset.findGroup(groupName.trim());
					
					if(group == null) {
						parseResult.put(new ReportError("DOSE NOT FIND GROUP " + groupName));
					}else {
						//  实时采样数据
						Variable timeVariable = null;
						String timeString = null;
						if((timeVariable = group.findVariable("time")) != null){
							timeString = timeVariable.read().toString();
						}
						String[] timeStrings = null;
						if(timeString != null)
							timeStrings = timeString.split(",");
						// long_name = "height in observation";
				        // :units = "m";
						Variable hgt = null;
						Array hgtArray = null;
						if((hgt = group.findVariable("hgt")) != null){
							hgtArray = hgt.read();
						}
						// :standard_name = "Horizontal direction of wind";
				        // :units = "degrees";
						Variable hdir = null;
						Array hdirArray = null;
						if((hdir = group.findVariable("Wdir")) != null)
							hdirArray = hdir.read();
						
						// :standard_name = "Horizontal Speed of wind";
				        // :units = "m/s";
						Variable hspd = null;
						Array hspdArray = null;
						if((hspd = group.findVariable("Wspd")) != null)
							hspdArray = hspd.read();
						
						// :standard_name = "Vertical velocity";
				        // :units = "m/s";
						Variable vspd = null;
						Array vspdArray = null;
						if((vspd = group.findVariable("WvradDataser")) != null){
							vspdArray = vspd.read();
						}
						
						//:standard_name = "Reliability of horizontal wind";
				        // :scale_factor = 100; // int
						Variable hreli = null;
						Array hreliArray = null;
						if((hreli = group.findVariable("Hreli")) != null)
							hreliArray = hreli.read();
						
						// :scale_factor = 100; // int
				        // :_FillValue = 999999.0f; // float
						Variable vreli = null;
						Array vreliArray = null;
						if((vreli = group.findVariable("Vreli")) != null)
							vreliArray = vreli.read();
						
						// :standard_name = "Atmospheric refractive index structure constant";
				        // :_FillValue = 999999.0f; // float
						Variable arefr = null;
						Array arefrArray = null;
						if((arefr = group.findVariable("Arefr")) != null);
							arefrArray = arefr.read();
						
						//
						int [] shape = hdir.getShape();
						for (int i = 0; i < shape[0]; i++) {
							// 台站信息
							WindProfileRada windProfileRada = new WindProfileRada();
							
							// 转换为标准时间赋值
							java.text.SimpleDateFormat isoFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
							isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
							Date obs = null;
							try{
								obs = new Date(1000 * Long.parseLong(timeStrings[i].trim()));
								String tm = isoFormat.format(obs);
								windProfileRada.setObeTime(tm);
							}
							catch (Exception e) {
								parseResult.put(new ReportError("Observation datetime error" + timeString));
								continue;
							}
							if(!TimeCheckUtil.checkTime(obs)){
								ReportError reportError = new ReportError();
								reportError.setMessage("Time check error!");
								reportError.setSegment(timeStrings[i].trim());
								parseResult.put(reportError);
								continue;
							}
							try{
								// 获取全局属性中的经度信息
								String longitude = rootGroup.findAttribute("Longitude").getStringValue(); 
								windProfileRada.setLongitude(ElementValUtil.getLongitude(longitude.trim()));
								// 获取全局属性中的纬度信息
								String latitude = rootGroup.findAttribute("Latitude").getStringValue();
								windProfileRada.setLatitude(ElementValUtil.getlatitude(latitude.trim()));
								// 区站号
								String stationNumber = rootGroup.findAttribute("StationNumber").getStringValue();
								windProfileRada.setStaionId(stationNumber.trim());
								// 测站高度
								String altitude = rootGroup.findAttribute("Altitude").getValue(0).toString();
								windProfileRada.setAltitude(Double.parseDouble(altitude.trim()));
								//产品类型
								windProfileRada.setWND_OBS(groupName.substring(3, 8));
								// 观测标识
								windProfileRada.set_OBS(groupName.substring(3, 4));
								//雷达型号
								windProfileRada.setRadaModel(rootGroup.findAttribute("RadarType").getValue(0).toString());
							}catch (Exception e) {
								parseResult.put(new ReportError("Station info or Radar type or pdt type error!"));
							}
							
							List<WindProfileData> windProfileDatas = new ArrayList<WindProfileData>();
							// hgtnum
							for (int j = 0; j < shape[1]; j ++) {
								WindProfileData windProfileData = new WindProfileData();
								int idx = i * shape[1] + j;
								//观察高度
								windProfileData.setHeight(setDoubleV(hgtArray, idx, 1));
								//水平风向
//								try{
								windProfileData.setWindDirection(setDoubleV(hdirArray, idx, 1));
//								}catch (Exception e) {
//									windProfileData.setWindDirection(999999);
//									parseResult.put(new ReportError("Horizontal direction of wind error: " + hdirArray.toString()));
//									continue;
//								}
								//水平风速
								windProfileData.setWindSpeed(setDoubleV(hspdArray, idx, 1));
								//垂直风速
								windProfileData.setVwindSpeed(setDoubleV(vspdArray, idx, 1));
								// 水平方向可信度 
								windProfileData.setHcredibility((int)setDoubleV(hreliArray, idx, 0.01));
								// 垂直方向可信度
								windProfileData.setVcredibility((int)setDoubleV(vreliArray, idx, 0.01));
								// 折射率结构常数Cn2
								Double temp  = setDoubleV(arefrArray, idx, 1);
//								temp = 0.000001; 测试
								if(temp.isNaN())
									windProfileData.setCn2("999999");
								else
									windProfileData.setCn2(String.valueOf(temp));
								
								windProfileDatas.add(windProfileData);
							}
							
							windProfileRada.setwData(windProfileDatas);
							parseResult.put(windProfileRada);
							parseResult.setSuccess(true);
						}
						
					}
				}
				
			} catch (IOException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
				e.printStackTrace();
			}
			finally {
				try{
					netcdfDataset.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
		return parseResult;
	}
	
	public static double setDoubleV (Array array, int idx, double factor){
		if(array == null){
			return 999999;
		}
		else if(array.getSize() >= idx + 1){
			Double temp = array.getDouble(idx);
			if(temp.isNaN())
				return 999999;
			else
				return array.getDouble(idx) * factor;
		}
		else {
			return 999999;
		}
	}
	
	public static void main(String[] args) {
		DecodeWindProfileNC decodeWindProfileNC = new DecodeWindProfileNC();
		decodeWindProfileNC.decodeWindProFileNC("D:\\HUAXIN\\Panoplywin64\\T_RADA_I_54511_20190813000000_PO_WPRD_LC_OBS-RAD.nc");
	}

}
