package cma.cimiss2.dpc.quickqc.upar.windProfile;

import cma.cimiss2.dpc.quickqc.base.BaseCheck;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;
/**
 * 风向、风速质控码的综合检查
 */
public class ComprehensiveCheck implements BaseCheck{
	
	public static QualityCode[] compreChenck(QualityCode windDirQC, QualityCode windSpeedQC){
		QualityCode qcs[] = {windDirQC, windSpeedQC};
		if(windDirQC != QualityCode.Z_QC_LACK && windSpeedQC != QualityCode.Z_QC_LACK){
			// 质控 取max(0, 2)
			if(windSpeedQC == QualityCode.Z_QC_ERROR || windDirQC == QualityCode.Z_QC_ERROR){
				qcs[0] = QualityCode.Z_QC_ERROR;// 水平风向质控码
				qcs[1] = QualityCode.Z_QC_ERROR;// 水平风速质控码
			}
		}
		return qcs;
	}
}
