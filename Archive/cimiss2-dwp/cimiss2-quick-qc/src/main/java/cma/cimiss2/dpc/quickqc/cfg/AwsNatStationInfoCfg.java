package cma.cimiss2.dpc.quickqc.cfg;

import cma.cimiss2.dpc.quickqc.bean.AwsNatStationInfo;
import cma.cimiss2.dpc.quickqc.bean.MeteoElementObsStateInfo;
import cma.cimiss2.dpc.quickqc.bean.SSTime;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description: 加载国家站参数文件</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* 	1.		区站号(*)	iiiii	nvarchar(5)	5位数字或第1位为字母，第2-5位为数字
	2.		经度	longitude	nvarchar(13)	单位：度，保留2位小数
	3.		纬度	latitude	nvarchar(11)	单位：度，保留2位小数
	4.		观测场拔海高度	altitude	nvarchar(6)	单位为“0.1m”，保留1位小数，缺测时录入“/////”
	5.		气压感应器拔海高度	altitude_p	nvarchar(6)	单位为“0.1m”，保留1位小数，无气压传感器时录入“/////”
	6.		测站类别	stationclass	nvarchar(1)	基准站：1；基本站：2；一般站（4次） ：3；一般站 （3次）：4；无人自动站：5
	7.		本站气压观测标识	item_p	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	8.		海平面气压标识	item_sealevelp	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	9.		气温观测标识	item_t	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	10.		露点温度标识	item_td	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	11.		水汽压标识	item_e	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	12.		相对湿度观测标识	item_u	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	13.		总云量观测标识	item_nncloud	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	14.		低云量观测标识	item_nlcloud	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	15.		云状观测标识	item_cloudform	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	16.		云高观测标识	item_cloudheight	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	17.		人工能见度观测标识	item_v	nvarchar(1)	无观测：0；人工观测：2；自动观测：1
	18.		自动能见度观测标识	item_v_auto	nvarchar(1)	无观测：0；自动观测：1
	19.		人工定时降水观测标识	item_rain	nvarchar(1)	无观测：0；人工观测：2
	20.		自记降水观测标识	item_autorain	nvarchar(1)	无观测：0；自动观测：1
	21.		自记降水开始停用及启动时间	time_autorain	nvarchar(5)	开始停用月份：启用月份（11，02]
	22.		天气现象观测标识	item_phenomena	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	23.		小型蒸发观测标识	item_lsmall	nvarchar(1)	无观测：0；人工观测：2
	24.		大型蒸发（人工）观测标识	item_llarge	nvarchar(1)	无观测：0；人工观测：2
	25.		大型蒸发（人工）开始停用及启动时间	time_llarge	varchar(10)	开始停用月份+半角逗号+启用月份（比如，11月份停用，2月份启用则填写11,02，若无此项目填写半角逗号
	26.		自动蒸发观测标识	item_lauto	nvarchar(1)	无观测：0；自动观测：1
	27.		自动蒸发开始停用及启动时间	time_lauto	varchar(10)	开始停用月份+半角逗号+启用月份（比如，11月份停用，2月份启用则填写11,02，若无此项目填写半角逗号
	28.		雪深观测标识	item_snowdepth	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	29.		雪压观测标识	item_snowpressure	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	30.		电线积冰观测标识	item_wireicing	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	31.		定时风（2分钟风）观测标识	item_f	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	32.		自记风（10分钟风）观测标识	item_fauto	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	33.		极大风观测标识	item_fmost	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	34.		最大风观测标识	item_fmaximum	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	35.		地表温度观测标识	item_d0	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	36.		5cm地温观测标识	item_d05	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	37.		10cm地温观测标识	item_d10	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	38.		15cm地温观测标识	item_d15	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	39.		20cm地温观测标识	item_d20	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	40.		40cm地温观测标识	item_d40	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	41.		80cm地温观测标识	item_d80	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	42.		160cm地温观测标识	item_d160	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	43.		320cm地温观测标识	item_d320	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	44.		冻土深度观测标识	item_frozensoil	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	45.		日照观测标识	item_sunshine	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	46.		草面（雪面）温度观测标识	item_tg	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	47.		湿球温度观测标识	item_wett	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	48.		地面状态观测标识	item_groundstate	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	49.		总辐射观测标识	item_totalradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	50.		净辐射观测标识	item_netradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	51.		散射观测标识	item_scatterradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	52.		直接辐射观测标识	item_directradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	53.		反射观测标识	item_reflectradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
	54.		紫外线状态观测标识	item_alstatusradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
* <b>ProjectName:</b> cimiss2-quick-qc
* <br><b>PackageName:</b> cma.cimiss2.dpc.quickqc.cfg
* <br><b>ClassName:</b> AwsNatStationInfoCfg
* <br><b>Date:</b> 2019年6月19日 下午4:28:43
 */
public class AwsNatStationInfoCfg {
	private Map<String, AwsNatStationInfo> awsNatStationInfoMaps;
	
	private static AwsNatStationInfoCfg awsNatStationInfoCfg;
	
	private AwsNatStationInfoCfg() {
		awsNatStationInfoMaps = new HashMap<String, AwsNatStationInfo>();
		loadAwsNatStationInfoCfg("config/INFO_STATION_BCWH.TXT");
	}
	/**
	    *    加载配置文件
	 * @param cfgFileName 配置文件的路径
	 */
	private void loadAwsNatStationInfoCfg(String cfgFileName) {
		
		File file = new File(cfgFileName);
		// 判断传入的配置文件是否存在
		if(file != null && file.exists() && file.isFile()) {
			FileReader fileReader = null;
			BufferedReader bufferedReader = null;
			// BufferedReader读取配置文件
			try {
				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				String line=null;
				
				while((line = bufferedReader.readLine()) != null) {
					String[] items = line.trim().split("[|]");
					// 检查要素个数
					if(items.length == 54) {
						AwsNatStationInfo awsNatStationInfo = new AwsNatStationInfo();
						// 1.		区站号(*)	iiiii	nvarchar(5)	5位数字或第1位为字母，第2-5位为数字
						awsNatStationInfo.setStationCode(items[0].trim());
						// 2.		经度	longitude	nvarchar(13)	单位：度，保留2位小数
						awsNatStationInfo.setLongitude(items[1].trim().contains("/") ? Double.MAX_VALUE : Double.parseDouble(items[1].trim()));
						// 3.		纬度	latitude	nvarchar(11)	单位：度，保留2位小数
						awsNatStationInfo.setLatitude(items[2].trim().contains("/") ? Double.MAX_VALUE : Double.parseDouble(items[2].trim()));
						// 4.		观测场拔海高度	altitude	nvarchar(6)	单位为“0.1m”，保留1位小数，缺测时录入“/////”
						awsNatStationInfo.setAltitude(items[3].trim().contains("/") ? Double.MAX_VALUE : Double.parseDouble(items[3].trim()));
						// 5.		气压感应器拔海高度	altitude_p	nvarchar(6)	单位为“0.1m”，保留1位小数，无气压传感器时录入“/////”
						awsNatStationInfo.setAltitudeP(items[4].trim().contains("/") ? Double.MAX_VALUE : Double.parseDouble(items[4].trim()));
						// 6.		测站类别	stationclass	nvarchar(1)	基准站：1；基本站：2；一般站（4次） ：3；一般站 （3次）：4；无人自动站：5
						awsNatStationInfo.setStationClass(items[5].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[5].trim()));
						// 7.		本站气压观测标识	item_p	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[6].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[6].trim()), MeteoElement.PRS));
						// 8.		海平面气压标识	item_sealevelp	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[7].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[7].trim()), MeteoElement.PRS_Sea));
						// 9.		气温观测标识	item_t	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[8].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[8].trim()), MeteoElement.TEM));
						// 10.		露点温度标识	item_td	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[9].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[9].trim()), MeteoElement.DPT));
						// 11.		水汽压标识	item_e	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[10].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[10].trim()),	 MeteoElement.VAP));
						// 12.		相对湿度观测标识	item_u	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[11].trim().contains("/") ? Integer.MAX_VALUE :  Integer.parseInt(items[11].trim()), MeteoElement.RHU));
						// 13.		总云量观测标识	item_nncloud	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[12].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[12].trim()), MeteoElement.CLO_Cov));

						// 14.		低云量观测标识	item_nlcloud	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[13].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[13].trim()), MeteoElement.CLO_Cov_Low));

						// 15.		云状观测标识	item_cloudform	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[14].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[14].trim()), MeteoElement.CLO_FOME));

						// 16.		云高观测标识	item_cloudheight	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[15].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[15].trim()), MeteoElement.CLO_Height));

						// 17.		人工能见度观测标识	item_v	nvarchar(1)	无观测：0；人工观测：2；自动观测：1
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[16].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[16].trim()), MeteoElement.VIS));

						// 18.		自动能见度观测标识	item_v_auto	nvarchar(1)	无观测：0；自动观测：1
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[17].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[17].trim()), MeteoElement.VIS_AUTO));

						// 19.		人工定时降水观测标识	item_rain	nvarchar(1)	无观测：0；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[18].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[18].trim()), MeteoElement.PRE));

						// 20.		自记降水观测标识	item_autorain	nvarchar(1)	无观测：0；自动观测：1
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[19].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[19].trim()), MeteoElement.PRE_AUTO));

						// 21.		自记降水开始停用及启动时间	time_autorain	nvarchar(5)	开始停用月份：启用月份（11，02]
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo( new SSTime(items[20]), MeteoElement.PRE_AUTO_TIME));

						// 22.		天气现象观测标识	item_phenomena	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[21].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[21].trim()), MeteoElement.PHENOMENA));

						// 23.		小型蒸发观测标识	item_lsmall	nvarchar(1)	无观测：0；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[22].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[22].trim()), MeteoElement.EVP_SMALL));

						// 24.		大型蒸发（人工）观测标识	item_llarge	nvarchar(1)	无观测：0；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[23].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[23].trim()), MeteoElement.EVP_BIG));

						// 25.		大型蒸发（人工）开始停用及启动时间	time_llarge	varchar(10)	开始停用月份+半角逗号+启用月份（比如，11月份停用，2月份启用则填写11,02，若无此项目填写半角逗号
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(new SSTime(items[24]), MeteoElement.EVP_BIG_TIME));

						// 26.		自动蒸发观测标识	item_lauto	nvarchar(1)	无观测：0；自动观测：1
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[25].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[25].trim()), MeteoElement.EVP_AUTO));

						// 27.		自动蒸发开始停用及启动时间	time_lauto	varchar(10)	开始停用月份+半角逗号+启用月份（比如，11月份停用，2月份启用则填写11,02，若无此项目填写半角逗号
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(new SSTime(items[26]), MeteoElement.EVP_AUTO_TIME));

						// 28.		雪深观测标识	item_snowdepth	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[27].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[27].trim()), MeteoElement.SNOW_DEPTH));

						// 29.		雪压观测标识	item_snowpressure	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[28].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[28].trim()), MeteoElement.SNOW_PRS));

						// 30.		电线积冰观测标识	item_wireicing	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[29].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[29].trim()), MeteoElement.EICE));

						// 31.		定时风（2分钟风）观测标识	item_f	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[30].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[30].trim()), MeteoElement.WIN_2mi));

						// 32.		自记风（10分钟风）观测标识	item_fauto	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[31].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[31].trim()), MeteoElement.WIN_10mi));

						// 33.		极大风观测标识	item_fmost	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[32].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[32].trim()), MeteoElement.WIN_INST_Max));

						// 34.		最大风观测标识	item_fmaximum	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[33].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[33].trim()), MeteoElement.WIN_Max));

						// 35.		地表温度观测标识	item_d0	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[34].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[34].trim()), MeteoElement.GST));
						// 36.		5cm地温观测标识	item_d05	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[35].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[35].trim()), MeteoElement.GST_5cm));
						// 37.		10cm地温观测标识	item_d10	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[36].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[36].trim()), MeteoElement.GST_10cm));
						// 38.		15cm地温观测标识	item_d15	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[37].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[37].trim()), MeteoElement.GST_15cm));
						// 39.		20cm地温观测标识	item_d20	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[38].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[38].trim()), MeteoElement.GST_20cm));
						// 40.		40cm地温观测标识	item_d40	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[39].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[39].trim()), MeteoElement.GST_40cm));
						// 41.		80cm地温观测标识	item_d80	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[40].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[40].trim()), MeteoElement.GST_80cm));
						// 42.		160cm地温观测标识	item_d160	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[41].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[41].trim()), MeteoElement.GST_160cm));

						// 43.		320cm地温观测标识	item_d320	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[42].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[42].trim()), MeteoElement.GST_320cm));

						// 44.		冻土深度观测标识	item_frozensoil	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[43].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[43].trim()), MeteoElement.FRS));

						// 45.		日照观测标识	item_sunshine	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[44].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[44].trim()), MeteoElement.SSH));

						// 46.		草面（雪面）温度观测标识	item_tg	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[45].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[45].trim()), MeteoElement.LGST));

						// 47.		湿球温度观测标识	item_wett	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[46].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[46].trim()), MeteoElement.WETT));

						// 48.		地面状态观测标识	item_groundstate	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[47].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[47].trim()), MeteoElement.GROUND_STATE));

						// 49.		总辐射观测标识	item_totalradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[48].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[48].trim()), MeteoElement.TOTAL_RADIA));

						// 50.		净辐射观测标识	item_netradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[49].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[49].trim()), MeteoElement.NET_DADIA));
						// 51.		散射观测标识	item_scatterradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[50].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[50].trim()), MeteoElement.SCATTER_RADIA));
						// 52.		直接辐射观测标识	item_directradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[51].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[51].trim()), MeteoElement.DIRECT_RADIA));
						// 53.		反射观测标识	item_reflectradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[52].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[52].trim()), MeteoElement.REFLECT_RADIA));
						// 54.		紫外线状态观测标识	item_alstatusradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
						awsNatStationInfo.putMeteoElementObsStateInfo(new MeteoElementObsStateInfo(items[53].trim().contains("/") ? Integer.MAX_VALUE : Integer.parseInt(items[53].trim()), MeteoElement.ALSTATUS_RADIA));
						
						awsNatStationInfoMaps.put(awsNatStationInfo.getStationCode(), awsNatStationInfo);
					}else {
						
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	public static AwsNatStationInfoCfg getAwsNatStationInfoCfg() {
		if(awsNatStationInfoCfg == null) {
			awsNatStationInfoCfg = new AwsNatStationInfoCfg();
		}
		return awsNatStationInfoCfg;
	}
	
	public Map<String, AwsNatStationInfo> getAwsNatStationInfoMaps() {
		return awsNatStationInfoMaps;
	}
	public void setAwsNatStationInfoMaps(Map<String, AwsNatStationInfo> awsNatStationInfoMaps) {
		this.awsNatStationInfoMaps = awsNatStationInfoMaps;
	}
	
	public static void main(String[] args) {
		Map<String, AwsNatStationInfo>  map = AwsNatStationInfoCfg.getAwsNatStationInfoCfg().getAwsNatStationInfoMaps();
		int sz = map.size();
		System.out.println(sz);
		Set<String> a = map.keySet(); // 台站及观测方式（无观测0，自动观测1，人工观测2
		for(String aString : a){ 
			List<MeteoElementObsStateInfo> list = map.get(aString).getMeteoElementObsStateInfos();
			
			for(int i = 0; i < list.size(); i ++){
				if(list.get(i).getElement() == MeteoElement.TEM)
					System.out.println(list.get(i).getFlag());
			}
			
		}
			
	}

}
