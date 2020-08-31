package cma.cimiss2.dpc.quickqc.upar.windProfile;

import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;

public class test{
	public static void main(String[] args) {
		
		// 1. 风向缺测、允许值检查
		/**
		double a[] = {-17, 0, 177.3, 360, 477.3, 999999};
		for(int i = 0; i < a.length; i ++){
			QualityCode d = mCheck.missingCheck(MeteoElement.WIN_D, a[i], null);
			if(d == QualityCode.Z_QC_OK)
				d = rCheck.rangeCheck(MeteoElement.WIN_D, a[i], null);
			System.out.println(d);
			// 通过测试
		} 
		*/
		
		//2. 风速缺测、允许值检查
		/*
		double windspeed[] = {999999,
				5,
				-1,
				0,
				10,
				100,
				120,
				-0.87,
				0,
				20,
				120,
				140,
				-45,
				0,
				28,
				150,
				210,
				-9,
				0,
				24,
				180,
				222,
				-8,
				0,
				15,
				170,
				255,
				-1,
				0,
				29,
				110,
				200,
				-7,
				0,
				11,
				140,
				200,
				-56,
				0,
				21,
				170,
				200,
				-9,
				0,
				30,
				220,
				250,
				250};
		double height[] = {-700,
				-800,
				100,
				100,
				100,
				100,
				3000,
				3001,
				3001,
				3001,
				3001,
				5500,
				5501,
				5501,
				5501,
				5501,
				7000,
				7001,
				7001,
				7001,
				7001,
				14000,
				14001,
				14001,
				14001,
				14001,
				22000,
				22001,
				22001,
				22001,
				22001,
				30000,
				30001,
				30001,
				30001,
				30001,
				36000,
				36001,
				36001,
				36001,
				36001,
				39000,
				39001,
				39001,
				39001,
				39001,
				99999,
				100000}; // 测站高度+采样高度
		for(int i = 0; i < windspeed.length; i ++){
			QualityCode qc = mCheck.missingCheck(MeteoElement.WIN_S, windspeed[i], height[i]);
			if(qc ==  QualityCode.Z_QC_OK)
				qc = rCheck.rangeCheck(MeteoElement.WIN_S, windspeed[i], height[i]);
			System.out.println(qc);
		}
		*/
		// 测试通过
		
		//3. 气候学界限值检查
		/*
		String sta = "59072"; // 对应探空站：57972, 经纬度113.033    25.800
		double windspeed2 [] = {-45,
				0,
				28,
				150,
				210};
		double height []= {6291,
				6292,
				6293,
				7000,
				7140,
				};
		// 阈值 0~91， 0~99
		
		for(int i = 0; i < windspeed2.length; i ++){
			QualityCode qc = mCheck.missingCheck(MeteoElement.WIN_S, windspeed2[i], height[i]);
			if(qc ==  QualityCode.Z_QC_OK)
				qc = rCheck.rangeCheck(MeteoElement.WIN_S, windspeed2[i], height[i]);
			if(qc == QualityCode.Z_QC_OK)
				qc = tCheck.thresoldCheck(MeteoElement.WIN_S, windspeed2[i], sta, height[i], null, null);
			System.out.println(qc);
		}
		
		
		// 通过测试
		sta = "xxxxx";// 不存在时
		Double lat = 25.0;
		Double lon = 113.0;
		for(int i = 0; i < windspeed2.length; i ++){
			QualityCode qc = mCheck.missingCheck(MeteoElement.WIN_S, windspeed2[i], height[i]);
			if(qc ==  QualityCode.Z_QC_OK)
				qc = rCheck.rangeCheck(MeteoElement.WIN_S, windspeed2[i], height[i]);
			if(qc == QualityCode.Z_QC_OK)
				qc = tCheck.thresoldCheck(MeteoElement.WIN_S, windspeed2[i], sta, height[i], lat, lon);
			System.out.println(qc);
			// 通过测试
		}
		*/
	//4. 风向或风速缺测
		// 风速缺测
		/*
		double windSpeed = 999999;
		QualityCode qcS = mCheck.missingCheck(MeteoElement.WIN_S, windSpeed, null);
		System.out.println(qcS);
		
		// 风向缺测
		double windDir = 999999;
		QualityCode qcD = mCheck.missingCheck(MeteoElement.WIN_D, windDir, null);
		System.out.println(qcD);
		*/
		
	//5. 综合检查，风向和风速都不缺测
		/*
		//高度-600-3000，允许值范围0～100。59486台站气候学界限值允许值范围：0～32-37
		System.out.println("Wind Dir Check: ");
		QualityCode qcD[] = {QualityCode.Z_QC_NO_QC, QualityCode.Z_QC_NO_QC, QualityCode.Z_QC_NO_QC, QualityCode.Z_QC_NO_QC, QualityCode.Z_QC_NO_QC};
		QualityCode qcS[] = {QualityCode.Z_QC_NO_QC, QualityCode.Z_QC_NO_QC, QualityCode.Z_QC_NO_QC, QualityCode.Z_QC_NO_QC, QualityCode.Z_QC_NO_QC};
		double dir[] = {6, 23, 31, 37, -4};
		Double height = 1000.0;
		for(int i = 0; i < dir.length; i ++){
			// 缺测值，允许值检测
			qcD[i] = mCheck.missingCheck(MeteoElement.WIN_D, dir[i], height);
			if(qcD[i] == QualityCode.Z_QC_OK){
				qcD[i] = rCheck.rangeCheck(MeteoElement.WIN_D, dir[i], height);
				System.out.println(qcD[i]);
			}
		}
		System.out.println("Wind Speed Check: ");
		double windspeed2[] = {-2, 0, 20, 100, 8};
		Double heights[] = {height, height, height, height, height};
		Double lat = 23.0;
		Double lon = 113.0;
		String sta = "59486"; // 探空站  经纬度 113.050    23.667'
//		String sta = "xxxxx";
		QualityCode qcs[] = {QualityCode.Z_QC_NO_QC, QualityCode.Z_QC_NO_QC};
		for(int i = 0; i < windspeed2.length; i ++){
			qcs[0] = qcs[1] = QualityCode.Z_QC_NO_QC;
			qcs = Chn_Upar_Profile_QuickQC0.getWindQC(sta, windspeed2[i], dir[i], heights[i], lat, lon);
			System.out.println(qcs[0] + " \t " + qcs[1]);
//			qcS[i] = mCheck.missingCheck(MeteoElement.WIN_S, windspeed2[i], heights[i]); // 返回值可能为0、8、9
//			if(qcS[i] ==  QualityCode.Z_QC_OK){ // 
//				qcS[i] = rCheck.rangeCheck(MeteoElement.WIN_S, windspeed2[i], heights[i]); // 返回值可能为0、2
//				if(qcS[i] == QualityCode.Z_QC_OK) {
//					QualityCode tmp = tCheck.thresoldCheck(MeteoElement.WIN_S, windspeed2[i], sta, heights[i], lat, lon); // 返回值可能为0、2、9
//					if(tmp != QualityCode.Z_QC_NO_QC) // 如果质控码是9则不更新，不是9，则可以更新
//						qcS[i] = tmp;
//				}
//			}
//			System.out.println(qcS[i]);
		}
		// 综合检测
//		System.out.println("Comprehensive Check:");
//		for(int i = 0; i < qcD.length ; i ++){
//			QualityCode rtn[] = ComprehensiveCheck.compreChenck(qcD[i], qcS[i]);
//			System.out.println(rtn[0] + " \t " + rtn[1]);
//		}
		*/
		QualityCode qualityCode[] = Chn_Upar_Profile_QuickQC0.getWindQC("59072", -2, 999999.0, 50360.0, null, null);
		
	 }
	
}