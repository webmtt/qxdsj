package cma.cimiss2.dpc.quickqc.upar.windProfile;

import cma.cimiss2.dpc.quickqc.base.BaseCheck;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;

/** 
 * 缺失值检查
 */
public class MissingCheck implements BaseCheck{
	
	public QualityCode missingCheck(MeteoElement element, Object value, Double height) {
		if(element == MeteoElement.WIN_D)
			return WindDirMissingCheck(Double.parseDouble(value.toString()));
		else if(element == MeteoElement.WIN_S)
			return WindSpeedMissingCheck(Double.parseDouble(value.toString()), height);
		else {
			System.out.println("Missing Check element is neither windSpeed or windDir!");
			return QualityCode.Z_QC_NO_QC;
		}
	}
	/**
	 * 风速质控码：缺测检查
	 */
	private QualityCode WindSpeedMissingCheck(double windSpeed, Double height){
		if(windSpeed == 999999){
			return QualityCode.Z_QC_LACK;
		}
		else{
			return QualityCode.Z_QC_OK;
		}
	}
	/**
	 * 风向质控码：缺测检查,
	 */
	private QualityCode WindDirMissingCheck(double windDir){
		if(windDir == 999999){
			return QualityCode.Z_QC_LACK; // 缺测
		}
		else{
			return QualityCode.Z_QC_OK; // 不缺测
		}
	}

}
