package cma.cimiss2.dpc.decoder.agme;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCropAuto;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;

import org.nutz.repo.Base64;
// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	农作物自动观测数据解码
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>所有运算采用java double进行基本运算。
 * <li>无特殊说明时，所有包含”/”的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.agme.ZAgmeCropAuto}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * <?xml version="1.0" encoding="UTF-8"?>
<AgrMetObservationData><ObservationData><BasicInformation><V01300>WS001</V01300><V04001>2018</V04001><V04002>1</V04002><V04003>2</V04003><V04004>9</V04004><V06001>83.8</V06001><V05001>44.4333</V05001><V07030>466.0</V07030><V07031></V07031><V33256>9</V33256></BasicInformation><CroplandClimateElement><Pressure><V07259></V07259><V10004></V10004><Q10004>9</Q10004><V10301></V10301><Q10301>9</Q10301><V26273></V26273><Q26273>9</Q26273><V10302></V10302><Q10302>9</Q10302><V26274></V26274><Q26274>9</Q26274></Pressure><Temperature_Humidity><V07256></V07256><V12001>-15.3</V12001><Q12001>9</Q12001><V12011>-14.0</V12011><Q12011>9</Q12011><V26275>0801</V26275><Q26275>9</Q26275><V12012>-15.3</V12012><Q12012>9</Q12012><V26276>0858</V26276><Q26276>9</Q26276><V13003>83</V13003><Q13003>9</Q13003><V13007>83</V13007><Q13007>9</Q13007><V26277>0847</V26277><Q26277>9</Q26277><V13004>1.5</V13004><Q13004>9</Q13004><V12003>-17.5</V12003><Q12003>9
 	<br>
 * <strong>code:</strong><br>
 * DecodeCropXML decodeXML = new DecodeCropXML();<br>
 * ParseResult<ZAgmeCropAuto> parseResult = decodeXML.DecodeFile(new File("String filepaht"));<br>
 * List<ZAgmeCropAuto> list = parseResult.getData();<br>
 * System.out.println(list.size());<br>
 * System.out.println(list.get(0).getStationNumberChina());<br>
 * 
 * <strong>output:</strong><br>
 * 4<br>
 * WS001<br>
		
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月4日 下午6:44:48   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeCropXML {

	/** 存放数据解析的结果集. */
	private ParseResult<ZAgmeCropAuto> parseResult = new ParseResult<ZAgmeCropAuto>(false);
	
	/**
	 * 缺失替代值.
	 *
	 * @param file the file
	 * @return the parses the result
	 */
//	double defaultD = 999999;
	/**
	 * 农作物自动观测数据解码函数 
	 * @param file 文件对象
	 * @return  
	 *         ParseResult<ZAgmeCropAuto>  解码结果集
	 */
	public ParseResult<ZAgmeCropAuto> DecodeFile(File file){
		if(file != null && file.exists() && file.isFile()){
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			Set<String> sets = null;
			SAXReader reader = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
			try{
				reader = new SAXReader();
				Document doc = reader.read(file);
				Element root = doc.getRootElement();
				Element obser;
				Element basicInfo;
				Element cropEle;
				Element cropGen;
				Element deviceStatus;
				Element crop11;
				for(@SuppressWarnings("rawtypes")
				// 从ObservationData节点开始遍历
				Iterator iterator = root.elementIterator("ObservationData"); iterator.hasNext();){
					ZAgmeCropAuto zAgmeCropAuto = new ZAgmeCropAuto();
					obser = (Element)iterator.next();
					// 1. basicInformation 出现一次
					basicInfo = obser.element("BasicInformation");
					if(basicInfo == null){
						obser = obser.element("ObservationData");
						basicInfo = obser.element("BasicInformation");
					}
					sets = BasicInformation.keySet();
					for(String key: sets)
						BasicInformation.put(key, basicInfo.elementText(key));
					zAgmeCropAuto.setStationNumberChina(BasicInformation.get("V01300"));  //台站
					Date time = new Date();
					String year = BasicInformation.get("V04001");
					String month = BasicInformation.get("V04002");
					String day = BasicInformation.get("V04003");
					String hour = BasicInformation.get("V04004");
					if(month.length() == 1)
						month = "0" + month;
					if(day.length() == 1)
						day = "0" + day;
					if(hour.length() == 1)
						hour = "0" + hour;
					String dt = year + month + day + hour;
					try{
						time = simpleDateFormat.parse(dt);
					}catch(ParseException e){
						ReportError re = new ReportError();
						re.setMessage("Time format conversion exception");
						re.setSegment(dt);
						parseResult.put(re);
						continue;
					}
					zAgmeCropAuto.setObservationTime(time);//1
					
					//2019-7-16 cuihongyuan
//					if(!TimeCheckUtil.checkTime(time)){
//						ReportError reportError = new ReportError();
//						reportError.setMessage("time check error!");
//						reportError.setSegment(dt);
//						parseResult.put(reportError);
//						continue;
//					}
					
					zAgmeCropAuto.setLongitude(getValueD(BasicInformation.get("V06001")));//纬度
					zAgmeCropAuto.setLatitude(getValueD(BasicInformation.get("V05001")));//经度
					
					zAgmeCropAuto.setHeightOfStaionAboveSeaLevel(getValueD(BasicInformation.get("V07030")));//测站高度
					zAgmeCropAuto.setHeightOfPressureSensor(getValueD(BasicInformation.get("V07031")));//压力传感器高度
					zAgmeCropAuto.setQualityControl(getValueI(BasicInformation.get("V33256")));//质控码
					zAgmeCropAuto.setCorrectMarker(BasicInformation.get("V35024"));// 更正报标识
					
					// 2. CropElement 之 CropGeneralObservation 出现0次或一次
					cropEle = obser.element("CropElement");// 出现0次或一次
					if(cropEle == null){
						parseResult.put(zAgmeCropAuto);
						parseResult.setSuccess(true);
						continue ; //没有作物要素字段
					}
					
					//2019-11-18
					int imageNumber = 0;
					if(cropEle.elementIterator("CropGeneralObservation").hasNext()){
						sets = CropGeneralObservation.keySet();
						for(@SuppressWarnings("rawtypes")
						Iterator it = cropEle.elementIterator("CropGeneralObservation"); it.hasNext();){
							cropGen = (Element)it.next();							
							for(String key: sets){
								CropGeneralObservation.put(key, cropGen.elementText(key));
							}
							imageNumber += getValueI(CropGeneralObservation.get("V02259"));//图像传感器标识
						}
					}
					
					cropGen = cropEle.element("CropGeneralObservation");// 出现0次或一次
					if(cropEle != null){
						sets = CropGeneralObservation.keySet();
						for(String key: sets)
							CropGeneralObservation.put(key, cropGen.elementText(key));
						zAgmeCropAuto.setImageSensorHeightAboveGround(getValueD(CropGeneralObservation.get("V07256")));//图像传感器高度
						zAgmeCropAuto.setImageSensorFocus(getValueI(CropGeneralObservation.get("V02271")));//图像传感器焦距
						zAgmeCropAuto.setObvArea(getValueD(CropGeneralObservation.get("V48002")));//观测面积
						zAgmeCropAuto.setObvMethod(getValueI(CropGeneralObservation.get("V02256")));//观测方法
						zAgmeCropAuto.setImageSensorID(getValueS(CropGeneralObservation.get("V02259")));//图像传感器标识
						zAgmeCropAuto.setImageSensorConnectionStatus(getValueI(CropGeneralObservation.get("V02270"))); // 图像传感器连接标识
						zAgmeCropAuto.setGPSTimingMarker(getValueI(CropGeneralObservation.get("V02257"))); //GPS授时标识
						zAgmeCropAuto.setCFPeripheralStorageMarker(getValueI(CropGeneralObservation.get("V02258"))); //外部存储器标识
						zAgmeCropAuto.setAgmeReportSoftwareVersion(getValueS(CropGeneralObservation.get("V02260"))); // 软件版本
					}
					
					// 3. CropElement 之  DeviceStatus 出现0次或一次
					deviceStatus = cropEle.element("DeviceStatus") ; 
					if(deviceStatus != null){
						sets = DeviceStatus.keySet();
						for(String key: sets)
							DeviceStatus.put(key, deviceStatus.elementText(key));
						zAgmeCropAuto.setColllectorRunningState(getValueI(DeviceStatus.get("V02261")));// 采集器运行状态
						zAgmeCropAuto.setCollectorVoltage(getValueD(DeviceStatus.get("V02262")));// 采集器电压
						zAgmeCropAuto.setCollectorPowerSupplyType(getValueI(DeviceStatus.get("V02263")));// 采集器电力供应类型
						zAgmeCropAuto.setCollectorMainboardTemperature(getValueD(DeviceStatus.get("V02264")));// 采集器主板温度
						zAgmeCropAuto.setCollectorCFstate(getValueI(DeviceStatus.get("V02265"))); // 采集器CF状态
						zAgmeCropAuto.setCollectorCFRemainSpace(getValueI(DeviceStatus.get("V02266")));// 采集器CF剩余空间
						zAgmeCropAuto.setCollectorGPSWorkingstate(getValueI(DeviceStatus.get("V02267")));// 采集器GPS工作状态
						zAgmeCropAuto.setCollectorGateswitchState(getValueI(DeviceStatus.get("V02268")));// 采集器门开关状态
						zAgmeCropAuto.setCollectorLANterminalCommunicationState(getValueI(DeviceStatus.get("V02269")));//采集器LAN终端通讯状态
					}
					
					// 4. CropElement 之 CROP11  出现0次或n次
					if(!cropEle.elementIterator("CROP11").hasNext()){
						parseResult.put(zAgmeCropAuto);
						parseResult.setSuccess(true);;
					}
					else{
						sets = CROP11.keySet();
						for(@SuppressWarnings("rawtypes")
						Iterator it = cropEle.elementIterator("CROP11"); it.hasNext();){
							crop11 = (Element) it.next();
							for(String key: sets){
								CROP11.put(key, crop11.elementText(key));
							}
//							zAgmeCropAuto.setCropName(getValueI(CROP11.get("V48000"))); // 作物名称
//							zAgmeCropAuto.setCropGrowthPeriod(getValueI(CROP11.get("V48001"))); //作物生长期
//							zAgmeCropAuto.setCropGrowthPeriodQC(getQC(CROP11.get("Q48001"))); // 作物发育期质控码
//							zAgmeCropAuto.setGrowthStarttime(getValueS(CROP11.get("V26256"))); //发育开始时间
//							zAgmeCropAuto.setGrowthStarttimeQC(getQC(CROP11.get("Q26256"))); // 发育开始时间质控码
//							zAgmeCropAuto.setGrowthDuration(getValueI(CROP11.get("V26257")));//生长时长
//							zAgmeCropAuto.setGrowthDurationQC(getQC(CROP11.get("Q26257"))); //生成时长质控码
//							zAgmeCropAuto.setGrowthPeriodPercent(getValueI(CROP11.get("V48009")));//发育期百分率
//							zAgmeCropAuto.setGrowthPeriodPercentQC(getQC(CROP11.get("Q48009"))); //发育期百分率质控码
//							zAgmeCropAuto.setPlantCoverage(getValueI(CROP11.get("V48008"))); //植株覆盖
//							zAgmeCropAuto.setPlantCoverageQC(getQC(CROP11.get("Q48008"))); //植株覆盖质控码
//							zAgmeCropAuto.setLeafAreaIndex(getValueD(CROP11.get("V48010"))); //叶面积指数
//							zAgmeCropAuto.setLeafAreaIndexQC(getQC(CROP11.get("Q48010"))); // 叶面积指数质控码
//							zAgmeCropAuto.setCanopyHeight(getValueI(CROP11.get("V48034"))); //冠层高度
//							zAgmeCropAuto.setCanopyHeightQC(getQC(CROP11.get("Q48034"))); //观测高度指数码
//							zAgmeCropAuto.setPlantDensity(getValueD(CROP11.get("V48007"))); //植株密度
//							zAgmeCropAuto.setPlantDensityQC(getQC(CROP11.get("Q48007"))); // 植株密度质控码
//							zAgmeCropAuto.setDryWeight(getValueD(CROP11.get("V48301"))); //干重
//							zAgmeCropAuto.setDryWeightQC(getQC(CROP11.get("Q48301")));// 干重质控码
//							zAgmeCropAuto.setGrowthState(getValueI(CROP11.get("V48006"))); //生长状态
//							zAgmeCropAuto.setGrowthStateQC(getQC(CROP11.get("Q48006")));// 生长状态质控码
//							zAgmeCropAuto.setDisaName(getValueI(CROP11.get("V51000"))); //灾害名称
//							zAgmeCropAuto.setDisaLevel(getValueI(CROP11.get("V51009"))); //灾害等级
//							zAgmeCropAuto.setDisaLevelQC(getQC(CROP11.get("Q51002"))); // 灾害等级质控码
//							zAgmeCropAuto.setDisaStarttime(getValueS(CROP11.get("V26269"))); //灾害开始时间
//							zAgmeCropAuto.setDisaStarttimeQC(getQC(CROP11.get("Q26269")));//灾害开始时间质控码
//							zAgmeCropAuto.setDisaDuration(getValueI(CROP11.get("V26270")));//灾害持续时间
//							zAgmeCropAuto.setDisaDurationQC(getQC(CROP11.get("Q26270"))); //灾害持续时间质控码
//							zAgmeCropAuto.setCropImageFormat(getValueS(CROP11.get("V30256"))); //灾害影像格式
//							String image = CROP11.get("V48033");
//							if(image == null)
//								image = crop11.elementText("V71038");
//							if(image != null && !image.isEmpty()){
//								byte[] bytes = Base64.decode(image);
//								zAgmeCropAuto.setCropImage(bytes);
////								zAgmeCropAuto.setCropImage(image.getBytes());
//							}
							String ele = CROP11.get("V48000");
							if(ele == null)
								ele = crop11.elementText("V71001");
							zAgmeCropAuto.setCropName(getValueI(ele)); // 作物名称
							
							ele = CROP11.get("V48001");
							if(ele == null)
								ele = crop11.elementText("V71002");
							zAgmeCropAuto.setCropGrowthPeriod(getValueI(ele)); //作物生长期
							zAgmeCropAuto.setCropGrowthPeriodQC(getQC(CROP11.get("Q48001"))); // 作物发育期质控码
							
							zAgmeCropAuto.setGrowthStarttime(getValueS(CROP11.get("V26256"))); //发育开始时间
							zAgmeCropAuto.setGrowthStarttimeQC(getQC(CROP11.get("Q26256"))); // 发育开始时间质控码
							
							ele = CROP11.get("V26257");
							if(ele == null)
								ele = crop11.elementText("V71036");
							zAgmeCropAuto.setGrowthDuration(getValueI(ele));//生长时长
							zAgmeCropAuto.setGrowthDurationQC(getQC(CROP11.get("Q26257"))); //生成时长质控码
							
							ele = CROP11.get("V48009");
							if(ele == null)
								ele = crop11.elementText("V71010");
							zAgmeCropAuto.setGrowthPeriodPercent(getValueI(ele));//发育期百分率
							zAgmeCropAuto.setGrowthPeriodPercentQC(getQC(CROP11.get("Q48009"))); //发育期百分率质控码
							
							ele = CROP11.get("V48008");
							if(ele == null)
								ele = crop11.elementText("V71009");
							zAgmeCropAuto.setPlantCoverage(getValueI(ele)); //植株覆盖
							zAgmeCropAuto.setPlantCoverageQC(getQC(CROP11.get("Q48008"))); //植株覆盖质控码
							
							ele = CROP11.get("V48010");
							if(ele == null)
								ele = crop11.elementText("V71011");
							zAgmeCropAuto.setLeafAreaIndex(getValueD(ele)); //叶面积指数
							zAgmeCropAuto.setLeafAreaIndexQC(getQC(CROP11.get("Q48010"))); // 叶面积指数质控码
							
							ele = CROP11.get("V48034");
							if(ele == null)
								ele = crop11.elementText("V71037");
							zAgmeCropAuto.setCanopyHeight(getValueI(ele)); //冠层高度
							zAgmeCropAuto.setCanopyHeightQC(getQC(CROP11.get("Q48034"))); //观测高度指数码
							
							ele = CROP11.get("V48007");
							if(ele == null)
								ele = crop11.elementText("V71008");
							zAgmeCropAuto.setPlantDensity(getValueD(ele)); //植株密度
							zAgmeCropAuto.setPlantDensityQC(getQC(CROP11.get("Q48007"))); // 植株密度质控码
							
							ele = CROP11.get("V48301");
							if(ele == null)
								ele = crop11.elementText("V71301");
							zAgmeCropAuto.setDryWeight(getValueD(ele)); //干重
							zAgmeCropAuto.setDryWeightQC(getQC(CROP11.get("Q48301")));// 干重质控码
							
							ele = CROP11.get("V48006");
							if(ele == null)
								ele = crop11.elementText("V71007");
							zAgmeCropAuto.setGrowthState(getValueI(ele)); //生长状态
							zAgmeCropAuto.setGrowthStateQC(getQC(CROP11.get("Q48006")));// 生长状态质控码
							
							ele = CROP11.get("V51000");
							if(ele == null)
								ele = crop11.elementText("V71400");
							zAgmeCropAuto.setDisaName(getValueI(ele)); //灾害名称
							
							ele = CROP11.get("V51009");
							if(ele == null)
								ele = crop11.elementText("V71406");
							zAgmeCropAuto.setDisaLevel(getValueI(ele)); //灾害等级
							zAgmeCropAuto.setDisaLevelQC(getQC(CROP11.get("Q51002"))); // 灾害等级质控码
							
							ele = CROP11.get("V26269");
							if(ele == null)
								ele = crop11.elementText("V71401");
							zAgmeCropAuto.setDisaStarttime(getValueS(ele)); //灾害开始时间
							zAgmeCropAuto.setDisaStarttimeQC(getQC(CROP11.get("Q26269")));//灾害开始时间质控码
							
							ele = CROP11.get("V26270");
							if(ele == null)
								ele = crop11.elementText("V71405");
							zAgmeCropAuto.setDisaDuration(getValueI(ele));//灾害持续时间
							zAgmeCropAuto.setDisaDurationQC(getQC(CROP11.get("Q26270"))); //灾害持续时间质控码
							// 格式更改 
							Element OnImages = crop11.element("Image");
							if(OnImages != null){
								if(imageNumber == 1 || imageNumber >= 10000){
									// 图像时间
									ele = OnImages.elementText("V26282");
									zAgmeCropAuto.setImageTime(getValueLong(ele));
									// 图像格式
									ele = OnImages.elementText("V30256");
									zAgmeCropAuto.setCropImageFormat(getValueS(ele));
									// 图像
									ele = OnImages.elementText("V71038");
									if(ele != null && !ele.isEmpty()){
										byte[] bytes= Base64.decode(ele);
										zAgmeCropAuto.setCropImage(bytes);
									}
									
									parseResult.setSuccess(true);
									parseResult.put(zAgmeCropAuto);
								}
								else if(imageNumber > 1){
									int cnt = 1;
									for(@SuppressWarnings("rawtypes")
									Iterator itor =  OnImages.elementIterator("Image"); itor.hasNext();){
										if(cnt > 1){
											Element img = (Element) itor.next();
											ZAgmeCropAuto zAgmeCropAuto2 = (ZAgmeCropAuto) zAgmeCropAuto.clone();
											// 图像数时间
											ele = img.elementText("V26282");
											zAgmeCropAuto2.setImageTime(getValueLong(ele));
											// 图像格式
											ele = img.elementText("V30256");
											zAgmeCropAuto2.setCropImageFormat(getValueS(ele));
											// 图像
											ele = img.elementText("V71038");
											if(ele != null && !ele.isEmpty()){
												byte[] bytes= Base64.decode(ele);
												zAgmeCropAuto2.setCropImage(bytes);
											}
											// 传感器标识2,3,4,5,
											zAgmeCropAuto2.setImageSensorID(String.valueOf(cnt));
											cnt ++;
											parseResult.setSuccess(true);
											parseResult.put(zAgmeCropAuto2);
										}
										else {
											Element img = (Element) itor.next();
											// 图像数时间
											ele = img.elementText("V26282");
											zAgmeCropAuto.setImageTime(getValueLong(ele));
											// 图像格式
											ele = img.elementText("V30256");
											zAgmeCropAuto.setCropImageFormat(getValueS(ele));
											// 图像
											ele = img.elementText("V71038");
											if(ele != null && !ele.isEmpty()){
												byte[] bytes= Base64.decode(ele);
												zAgmeCropAuto.setCropImage(bytes);
											}
											// 传感器标识1
											zAgmeCropAuto.setImageSensorID(String.valueOf(cnt));
											cnt ++;
											parseResult.setSuccess(true);
											parseResult.put(zAgmeCropAuto);
										}
									}
								}								
								else{
									// do nothing
								}
							}
							else {//OnImages == null
								ZAgmeCropAuto zAgmeCropAuto2 = (ZAgmeCropAuto) zAgmeCropAuto.clone();
								zAgmeCropAuto2.setCropImage(null);
								zAgmeCropAuto2.setImageTime(999999);
								zAgmeCropAuto2.setCropImageFormat("999999");
								parseResult.setSuccess(true);
								parseResult.put(zAgmeCropAuto2);
							}
//							ZAgmeCropAuto zAgmeCropAuto2 = new ZAgmeCropAuto();
//							zAgmeCropAuto2 = zAgmeCropAuto;
//							parseResult.setSuccess(true);
//							parseResult.put(zAgmeCropAuto2);
						}
					} 
				} 
			}
			catch (DocumentException e1) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch(Exception e2){
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			} 
		}else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	/**
	 * 解析整型数值   
	 * @param: @param str 待解析字符串
	 * @param: @return      解析得到整型数值
	 * @return long     解析结果  
	 */
	long getValueLong(String str){
		if(str == null || str.isEmpty())
			return 999999;
		else
			try{
				return Long.parseLong(str);
			}catch (Exception e) {
				return 999999;
			}
	}
	/**  BasicInformation节点 结构体. */
	@SuppressWarnings("serial")
	Map<String, String> BasicInformation = new HashMap<String, String>(){
	{
		put("V01300","");
		put("V04001","");
		put("V04002","");
		put("V04003","");
		put("V04004","");
		put("V06001","");
		put("V05001","");
		put("V07030","");
		put("V07031","");
		put("V33256","");
		put("V35024","");
	}};
	
	/** CropGeneralObservation节点 结构体. */
	 @SuppressWarnings("serial")
	Map<String,String> CropGeneralObservation = new HashMap<String,String>(){ 
	{
		put("V07256", "");
		put("V02271", "");
		put("V48002", "");
		put("V02256", "");
		put("V02259", "");
		put("V02270", "");
		put("V02257", "");
		put("V02258", "");
		put("V02260", "");
	}};
	
	/**  DeviceStatus节点 结构体. */
	 @SuppressWarnings("serial")
	Map<String, String> DeviceStatus = new HashMap<String, String>(){ 
	{
		put("V02261", "");
		put("V02262", "");
		put("V02263", "");
		put("V02264", "");
		put("V02265", "");
		put("V02266", "");
		put("V02267", "");
		put("V02268", "");
		put("V02269", "");
	}};
	
	/**  CROP11节点  结构体. */
	 @SuppressWarnings("serial")
	Map<String, String> CROP11 = new HashMap<String, String>(){ 
	{
		put("V48000", "");
		put("V48001", "");
		put("Q48001", "");
		put("V26256", "");
		put("Q26256", "");
		put("V26257", "");
		put("Q26257", "");
		put("V48009", "");
		put("Q48009", "");
		put("V48008", "");
		put("Q48008", "");
		put("V48010", "");
		put("Q48010", "");
		put("V48034", "");
		put("Q48034", "");
		put("V48007", "");
		put("Q48007", "");
		put("V48301", "");
		put("Q48301", "");
		put("V48006", "");
		put("Q48006", "");
		put("V51000", "");
		put("V51009", "");
		put("Q51002", "");
		put("V26269", "");
		put("Q26269", "");
		put("V26270", "");
		put("Q26270", "");
		put("V30256", "");
		put("V48033", "");
	}};
	
	/**
	 * 解析质控码   .
	 *
	 * @param str the str
	 * @return int      解码结果
	 * @param: @param str  待解析字符串
	 * @param: @return      解析得到质控码
	 */
	int getQC(String str){
		if(str == null || str.isEmpty())
			return 9;
		else 
			return Integer.parseInt(str);
	}
	
	/**
	 * 解析整型数值   .
	 *
	 * @param str the str
	 * @return int     解析结果
	 * @param: @param str 待解析字符串
	 * @param: @return      解析得到整型数值
	 */
	int getValueI(String str){
		if(str == null)
			return 999998;
		else if(str.isEmpty())
			return 999999;
		else 
			return Integer.parseInt(str);
	}
	
	/**
	 * 解析浮点型数值   .
	 *
	 * @param str the str
	 * @return double       解析结果
	 * @param: @param str  待解析字符串
	 * @param: @return      解析得到浮点型值
	 */
	double getValueD(String str){
		if(str == null )
			return 999998;
		else if(str.isEmpty())
			return 999999;
		else 
			return Double.parseDouble(str);
	}
	
	/**
	 * 字符串类型解析   .
	 *
	 * @param str 待解析字符串
	 * @return String      解析结果
	 */
	String getValueS(String str){
		if(str == null)
			return "999998";
		else if(str.isEmpty())
			return "999999";
		else 
			return str;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		DecodeCropXML decodeXML = new DecodeCropXML();
		ParseResult<ZAgmeCropAuto> parseResult = decodeXML.DecodeFile(new File("D:\\tmp\\agme\\T_AGME_I_Y5733_20190808120000_O_AAT.xml"));
		List<ZAgmeCropAuto> list = parseResult.getData();
		System.out.println(list.size());
		System.out.println(list.get(0).getStationNumberChina());
	}
}

