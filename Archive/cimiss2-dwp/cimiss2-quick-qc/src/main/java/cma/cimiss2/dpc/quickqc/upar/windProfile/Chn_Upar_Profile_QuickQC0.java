package cma.cimiss2.dpc.quickqc.upar.windProfile;

import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;

/**
 * 业务逻辑,计算风向、风速的质控码
 */
public class Chn_Upar_Profile_QuickQC0 {
	/**
	 * 输入： 风廓线台站号，风速、风向、采样高度+测站高度、台站纬度、台站经度
	 * 输出：风速、风向 综合质控码
	 * 返回:QualityCode[], QualityCode[0]对应风向质控码，QualityCode[1]对应风速质控码
	 */
	public static QualityCode[] getWindQC(String sta, double windSpeed, double windDir, Double height, Double lat, Double lon){
		//质控码初始化
		QualityCode qcs[] = {QualityCode.Z_QC_NO_QC, QualityCode.Z_QC_NO_QC};
		MissingCheck mCheck = new MissingCheck(); // 缺测检查
 		RangeCheck rCheck = new RangeCheck(); // 范围检查 
		ThresholdCheck tCheck = new ThresholdCheck(); //阈值检查
		// 风向质控码
		// 缺测值、允许值检查
		qcs[0] = mCheck.missingCheck(MeteoElement.WIN_D, windDir, height); // 返回0或8
		if(qcs[0] ==  QualityCode.Z_QC_OK)
			qcs[0] = rCheck.rangeCheck(MeteoElement.WIN_D, windDir, height); // 返回0或2
		// 风速质控
		// 缺失值检查
		qcs[1] = mCheck.missingCheck(MeteoElement.WIN_S, windSpeed, height); //返回0或8
		
		//1.允许值检查
//		if(qcs[1] ==  QualityCode.Z_QC_OK){
//			qcs[1] = rCheck.rangeCheck(MeteoElement.WIN_S, windSpeed, height); // 返回值可能为0、2
//		}
		
		//2.界限值检查
//		if(qcs[1] == QualityCode.Z_QC_OK) {
//			qcs[1] = tCheck.thresoldCheck(MeteoElement.WIN_S, windSpeed, sta, height, lat, lon);
//		}
		
		//3. 1 + 2 检查
		if(qcs[1] ==  QualityCode.Z_QC_OK){
			
			qcs[1] = rCheck.rangeCheck(MeteoElement.WIN_S, windSpeed, height); // 返回值可能为0、2
			if(qcs[1] == QualityCode.Z_QC_OK) {
				
				qcs[1] = tCheck.thresoldCheck(MeteoElement.WIN_S, windSpeed, sta, height, lat, lon);
				if(qcs[1] == QualityCode.Z_QC_NO_QC)
					qcs[1] = QualityCode.Z_QC_OK;// 返回范围值检查结果
			}
		}
		
		qcs = ComprehensiveCheck.compreChenck(qcs[0], qcs[1]);
		return qcs;
		
	} 
}
