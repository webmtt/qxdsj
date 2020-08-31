package cma.cimiss2.dpc.quickqc.cfg;

import cma.cimiss2.dpc.quickqc.bean.AwsRegStationInfo;
import cma.cimiss2.dpc.quickqc.bean.MeteoElementObsStateInfo;
import cma.cimiss2.dpc.quickqc.bean.SSTime;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description: 加载区域站台站参数</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* 1.		区站号	iiiii	nvarchar(5)	5位数字或第1位为字母，第2-5位为数字
* 2.		纬度	latitude	nvarchar(11)	单位：度，保留2位小数
* 3.		经度	longitude	nvarchar(13)	单位：度，保留2位小数
* 4.		观测场拔海高度	altitude	nvarchar(6)	单位为“0.1m”，保留1位小数，缺测时录入“/////”
* 5.		气压感应器拔海高度	altitude_p	nvarchar(6)	单位为“0.1m”，保留1位小数，无气压传感器时录入“/////”
* 6.		测站类别	stationclass	nvarchar(1)	基准站：1；基本站：2；一般站（4次） ：3；一般站 （3次）：4；无人自动站：5
* 7.		气温观测标识	item_t	nvarchar(1)	无观测：0；自动：1
* 8.		本站气压观测标识	item_p	nvarchar(1)	无观测：0；自动：1
* 9.		海平面气压观测标识	item_sealevelp	nvarchar(1)	无观测：0；自动：1
* 10.		相对湿度观测标识	item_u	nvarchar(1)	无观测：0；自动：1
* 11.		露点温度观测标识	item_td	nvarchar(1)	无观测：0；自动：1
* 12.		水汽压观测标识	item_e	nvarchar(1)	无观测：0；自动：1
* 13.		自动蒸发观测标识	item_lauto	nvarchar(1)	无观测：0；自动：1
* 14.		自动蒸发开始停用及启动时间	time_lauto	nvarchar(5)	开始停用月份+半角逗号+启用月份（比如，11月份停用，2月份启用则填写11,02，若无此项目填写半角逗号
* 15.		定时风（2分钟风）观测标识	item_f	nvarchar(1)	无观测：0；自动观测1
* 16.		自记风（10分钟风）观测标识	item_fauto	nvarchar(1)	无观测：0；自动观测1
* 17.		极大风观测标识	item_fmost	nvarchar(1)	无观测：0；自动观测1
* 18.		最大风观测标识	item_fmaximum	nvarchar(1)	无观测：0；自动观测1
* 19.		自记降水观测标识	item_autorain	nvarchar(1)	无观测：0；自动观测1
* 20.		自记降水开始停用及启动时间	time_autorain	nvarchar(5)	开始停用月份+半角逗号+启用月份（比如，11月份停用，2月份启用则填写11,02，若无此项目填写半角逗号
* 21.		地表温度观测标识	item_d0	nvarchar(1)	无观测：0；自动：1
* 22.		5cm地温观测标识	item_d05	nvarchar(1)	无观测：0；自动：1
* 23.		10cm地温观测标识	item_d10	nvarchar(1)	无观测：0；自动：1
* 24.		15cm地温观测标识	item_d15	nvarchar(1)	无观测：0；自动：1
* 25.		20cm地温观测标识	item_d20	nvarchar(1)	无观测：0；自动：1
* 26.		40cm地温观测标识	item_d40	nvarchar(1)	无观测：0；自动：1
* 27.		80cm地温观测标识	item_d80	nvarchar(1)	无观测：0；自动：1
* 28.		160cm地温观测标识	item_d160	nvarchar(1)	无观测：0；自动：1
* 29.		320cm地温观测标识	item_d320	nvarchar(1)	无观测：0；自动：1
* 30.		草面（雪面）温度观测标识	item_tg	nvarchar(1)	无观测：0；自动：1
* 31.		自动能见度观测标识	item_v_auto	nvarchar(1)	无观测：0；自动：1
* <b>ProjectName:</b> cimiss2-quick-qc
* <br><b>PackageName:</b> cma.cimiss2.dpc.quickqc.cfg
* <br><b>ClassName:</b> AwsRegStationInfoCfg
* <br><b>Date:</b> 2019年6月19日 下午2:51:17
 */
public class AwsRegStationInfoCfg {
	/**
	 * // 气象要素配置文件 
	 */
	private Map<String, AwsRegStationInfo> awsRegStationInfoMaps;
	private static AwsRegStationInfoCfg awsRegStationInfoCfg;
	
	private AwsRegStationInfoCfg() {
		awsRegStationInfoMaps = new HashMap<String, AwsRegStationInfo>(); 
		loadAwsRegStationInfoCfg("config/INFO_REG_STATION_BCWH.TXT");
	}

	private void loadAwsRegStationInfoCfg(String cfgFileName) {
		File file = new File(cfgFileName);
		if(file != null && file.exists() && file.isFile()) {
			FileReader fileReader = null;
			BufferedReader bufferedReader = null;
			try {
				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				String line=null;
				
				while((line = bufferedReader.readLine()) != null) {
					String[] items = line.trim().split("[|]");
					// 验证数据长度
					if(items.length == 31) {
						AwsRegStationInfo awsRegStationInfo = new AwsRegStationInfo();
					    // 解析区站号
						awsRegStationInfo.setStationCode(items[0].trim());
						// 解析站点纬度
						awsRegStationInfo.setLatitude(items[1].trim().contains("/") ? Double.MAX_VALUE : Double.parseDouble(items[1].trim()));
						// 解析站点经度
						awsRegStationInfo.setLongitude(items[2].trim().contains("/") ? Double.MAX_VALUE : Double.parseDouble(items[2].trim()));
						// 解析站点海拔高度
						awsRegStationInfo.setAltitude(items[3].trim().contains("/") ? Double.MAX_VALUE : Double.parseDouble(items[3].trim()));
						// 解析站点气压传感器高度
						awsRegStationInfo.setAltitudeP(items[4].trim().contains("/") ? Double.MAX_VALUE : Double.parseDouble(items[4].trim()));
						// 解析站点级别
						awsRegStationInfo.setStationClass(items[5].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[5].trim()));
						// 气温观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[6].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[6].trim()), MeteoElement.TEM));
						// 本站气压观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[7].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[7].trim()), MeteoElement.PRS));
						// 海平面气压观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[8].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[8].trim()), MeteoElement.PRS_Sea));
						// 相对湿度观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[9].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[9].trim()), MeteoElement.RHU));
					    // 露点温度观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[10].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[10].trim()), MeteoElement.DPT));
						// 水汽压观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[11].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[11].trim()), MeteoElement.VAP));
						// 自动蒸发观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[12].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[12].trim()), MeteoElement.EVP_AUTO));
						// 自动蒸发开始停用及启动时间
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(new SSTime(items[13].trim()), MeteoElement.EVP_AUTO_TIME));
						// 定时风（2分钟风）观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[14].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[14].trim()), MeteoElement.WIN_2mi));
						// 自记风（10分钟风）观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[15].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[15].trim()), MeteoElement.WIN_10mi));
						// 极大风观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[16].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[16].trim()), MeteoElement.WIN_INST_Max));
						// 最大风观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[17].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[17].trim()), MeteoElement.WIN_Max));
						// 自记降水观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[18].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[18].trim()), MeteoElement.PRE_AUTO));
						// 自记降水开始停用及启动时间
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(new SSTime(items[19].trim()), MeteoElement.PRE_AUTO_TIME));
						// 地表温度观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[20].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[20].trim()), MeteoElement.GST));
						// 5cm地温观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[21].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[21].trim()), MeteoElement.GST_5cm));
						// 10cm地温观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[22].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[22].trim()), MeteoElement.GST_10cm));
						// 15cm地温观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[23].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[23].trim()), MeteoElement.GST_15cm));
						// 20cm地温观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[24].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[24].trim()), MeteoElement.GST_20cm));
						// 40cm地温观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[25].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[25].trim()), MeteoElement.GST_40cm));
						// 80cm地温观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[26].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[26].trim()), MeteoElement.GST_80cm));
						// 160cm地温观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[27].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[27].trim()), MeteoElement.GST_160cm));
						// 320cm地温观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[28].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[28].trim()), MeteoElement.GST_320cm));
						// 草面（雪面）温度观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[29].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[29].trim()), MeteoElement.LGST));
						// 自动能见度观测标识
						awsRegStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[30].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[30].trim()), MeteoElement.VIS_AUTO));
						
						awsRegStationInfoMaps.put(awsRegStationInfo.getStationCode(), awsRegStationInfo);
					}else {
						
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally {
				if(bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(fileReader != null) {
					try {
						fileReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}
		
	}
	
	public static AwsRegStationInfoCfg getAwsRegStationInfoCfg() {
		if(awsRegStationInfoCfg == null) {
			awsRegStationInfoCfg = new AwsRegStationInfoCfg();
		}
		return awsRegStationInfoCfg;
	}
	
	

	public Map<String, AwsRegStationInfo> getAwsRegStationInfoMaps() {
		return awsRegStationInfoMaps;
	}

	public void setAwsRegStationInfoMaps(Map<String, AwsRegStationInfo> awsRegStationInfoMaps) {
		this.awsRegStationInfoMaps = awsRegStationInfoMaps;
	}

	public static void main(String[] args) {
		System.out.println(AwsRegStationInfoCfg.getAwsRegStationInfoCfg().awsRegStationInfoMaps.get("N0960").findMeteoElementObsStateInfo(MeteoElement.TEM).getFlag());
	}

}
