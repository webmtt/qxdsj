package cma.cimiss2.dpc.quickqc.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description:区域站参数配置文件实体类 </b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName:</b> cimiss2-quick-qc
* <br><b>PackageName:</b> cma.cimiss2.dpc.quickqc.bean
* <br><b>ClassName:</b> AwsRegStationInfo
* <br><b>Date:</b> 2019年6月19日 下午2:53:02
 */
public class AwsRegStationInfo extends BaseStationInfo{
	private List<MeteoElementObsStateInfo> meteoElementObsStateInfos;
	
	public AwsRegStationInfo() {
		super();
		meteoElementObsStateInfos = new ArrayList<MeteoElementObsStateInfo>();
	}

	public List<MeteoElementObsStateInfo> getMeteoElementObsStateInfos() {
		return meteoElementObsStateInfos;
	}

	public void setMeteoElementObsStateInfos(List<MeteoElementObsStateInfo> meteoElementObsStateInfos) {
		this.meteoElementObsStateInfos = meteoElementObsStateInfos;
	}
	
	public void putMeteoElementObsStateInfo(MeteoElementObsStateInfo meteoElementObsStateInfo) {
		this.meteoElementObsStateInfos.add(meteoElementObsStateInfo);
	}
	
	public MeteoElementObsStateInfo findMeteoElementObsStateInfo(MeteoElement element) {
		List<MeteoElementObsStateInfo> list =  meteoElementObsStateInfos.stream().filter(ele -> ele.getElement() == element).collect(Collectors.toList());
		if(list != null && list.size() > 0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
}