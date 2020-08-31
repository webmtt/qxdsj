package cma.cimiss2.dpc.quickqc.bean.enums;

public enum ElementBound {
	/*气压上限*/
	BOUND_P_HIGH(1200, "气压：百帕"),
	/*气压下限*/
	BOUND_P_LOW(500, "气压：百帕"),
	BOUND_T_HIGH(55, "气温，单位：摄氏度"), 
	BOUND_T_LOW(-55, "气温，单位：摄氏度"),
	BOUND_TG_HIGH(800, ""),
	BOUND_TG_LOW(-600, ""),
	BOUND_D0_HIGH(800, ""),
	BOUND_D0_LOW(-600, ""),
	BOUND_D05_HIGH(800, ""),
	BOUND_D05_LOW(-600, ""),
	BOUND_D10_HIGH(600, ""),
	BOUND_D10_LOW(-450, ""),
	BOUND_D15_HIGH(600, ""),
	BOUND_D15_LOW(-450, ""),
	BOUND_D20_HIGH(500, ""),
	BOUND_D20_LOW(-450, ""),
	BOUND_D40_HIGH(500, ""),
	BOUND_D40_LOW(-450, ""),
	BOUND_D80_HIGH(500, ""),
	BOUND_D80_LOW(-450, ""),
	BOUND_D160_HIGH(450, ""),
	BOUND_D160_LOW(-350, ""),
	BOUND_D320_HIGH(450, ""),
	BOUND_D320_LOW(-350, ""),
	BOUND_U_HIGH(100,  ""),
	BOUND_U_LOW(0, ""),
	BOUND_R_HIGH(3600, ""),
	BOUND_R_LOW(0, ""),
	// 过去3、6小时降水量最大值（单位0.1mm）
	BOUND_R03_06_HIGH(6000, ""),
	// 过去3、6小时降水量最小值（单位0.1mm）
	BOUND_R03_06_LOW(0, ""),
	// 过去12小时降水量最大值（单位0.1mm）
	BOUND_R12_HIGH(9000, ""),
	BOUND_R12_LOW(0, ""),
	// 过去24小时降水量最大值(单位0.1mm)
	BOUND_R24_HIGH(12000,	 ""),
	// 过去24小时降水量最小值(单位0.1mm)
	BOUND_R24_LOW(0, ""),
	BOUND_RMAN_LOW(0, ""),
	/*人工加密观测降水量*/
	BOUND_RMAN_HIGH(12000, ""),
	BOUND_FFF_HIGH(1500, ""),
	BOUND_FFF_LOW(0, ""),
	/*水汽压*/
	BOUND_E_HIGH(650,  ""),
	BOUND_E_LOW(0, ""),
	/*蒸发*/
	BOUND_L_HIGH(300, ""),
	BOUND_L_LOW(0, ""),
	/*能见度（米）*/
	BOUND_V_HIGH(99999, ""),
	BOUND_V_LOW(0, ""),
	/*云量*/
	BOUND_NN_HIGH(10, ""),
	BOUND_NN_LOW(0, ""),
	/*云高*/
	BOUND_CLOUDHIGH_HIGH(29999, ""),
	BOUND_CLOUDHIGH_LOW(0, ""),
	/*雪深*/
	BOUND_SNOWDEPTH_HIGH(9999, ""),
	BOUND_SNOWDEPTH_LOW(0, ""),
	BOUND_SNOWPRESSURE_HIGH(999, ""), /*雪压*/
	BOUND_SNOWPRESSURE_LOW(0, ""),
	BOUND_SOILDEPTH_HIGH(999, ""),/*冻土cm*/
	BOUND_SOILDEPTH_LOW(0,""),
	BOUND_WIREICEINGDIA_HIGH(999, ""), /*电线结冰mm*/
	BOUND_WIREICEINGDIA_DOUBT(200, ""),
	BOUND_WIREICEINGDIA_LOW(27, ""),
	BOUND_HAILDIA_HIGH(999, ""),/*最大冰雹直径*/
	BOUND_HAILDIA_LOW(0, ""),
	BOUND_DAY_R_HIGH(6000, ""),/*日降水量*/
	BOUND_DAY_R_LOW(0, ""),
	BOUND_WIREICINGWEIGHT_HIGH(30000, ""),/*电线积冰重量*/
	BOUND_WIREICINGWEIGHT_LOW(0, ""),
	BOUND_WIREICINGT_HIGH(50, ""),/*电线积冰温度*/
	BOUND_WIREICINGT_LOW(-550, ""),
	BOUND_WIREICINGDDD_HIGH(360, ""),/*电线积冰风向*/
	BOUND_WIREICINGDDD_LOW(0, ""),
	BOUND_WIREICINGFFF_HIGH(750, ""),/*电线积冰风速*/
	BOUND_WIREICINGFFF_LOW(0, ""),
	BOUND_SS_HIGH(10, ""),/*小时最大日照时数，单位：0.1小时*/
	BOUND_SS_LOW(0, ""),
	BOUND_TRE_HIGH(600, ""),/*总辐射曝辐量气候学界限值*/
	BOUND_TRE_LOW(0, ""),
	BOUND_NRE_HIGH(450, ""),/*净辐射曝辐量*/
	BOUND_NRE_LOW(-150, ""),
	BOUND_DRE_HIGH(520, ""),/*直接辐射曝辐量*/
	BOUND_DRE_LOW(-0, ""),
	BOUND_SRE_HIGH(400, ""),/*散射辐射曝辐量*/
	BOUND_SRE_LOW(-0, ""),
	BOUND_RRE_HIGH(400, ""),/*反射辐射曝辐量*/
	BOUND_RRE_LOW(-0, ""),
	BOUND_URE_HIGH(36, ""),/*紫外线辐射曝辐量*/
	BOUND_URE_LOW(-0, ""),
	BOUND_TRI_HIGH(2100, ""),/*总辐射照度及其极值气候学界限值*/
	BOUND_TRI_LOW(0, ""),
	BOUND_NRI_HIGH(1500, ""),/*净辐射照度及其极值气候学界限值*/
	BOUND_NRI_LOW(-400, ""),
	BOUND_DRI_HIGH(1450, ""),/*直接辐射照度及其极值气候学界限值*/
	BOUND_DRI_LOW(0, ""),
	BOUND_SRI_HIGH(1300, ""),/*散辐射照度及其极值气候学界限值*/
	BOUND_SRI_LOW(0, ""),
	BOUND_RRI_HIGH(1100, ""),/*反辐射照度及其极值气候学界限值*/
	BOUND_RRI_LOW(0, ""),
	BOUND_URI_HIGH(100, ""),/*紫外辐射照度及其极值气候学界限值*/
	BOUND_URI_LOW(0, ""),
	BOUND_AT_HIGH(1000, ""),/*大气浑浊度*/
	BOUND_AT_LOW(0, ""),
	BOUND_AR_R_LOW(0, ""),/*酸雨观测样品对应的降水量*/
	BOUND_AR_R_HIGH(12000, ""),
	BOUND_AR_T_LOW(0, ""),/*酸雨观测样品温度*/
	BOUND_AR_T_HIGH(500, ""),
	BOUND_AR_PH_LOW(0, ""),/*酸雨观测样品PH值*/
	BOUND_AR_PH_HIGH(1400, ""),
	BOUND_AR_K_LOW(0, ""),/*酸雨观测样品K值*/
	BOUND_AR_K_HIGH(20000, ""),
	BOUND_SEN_IDE_LOW(0, ""),/*传感器高度 */
	BOUND_SEN_IDE_HIGH(7, ""),/*传感器高度*/
	BOUND_P_CHA_LOW(0, ""),/*气压倾向特征*/
	BOUND_P_CHA_HIGH(14, ""),
	BOUND_HPA_HEIGH_LOW(0, ""),/*测站的位势高度*/
	BOUND_HPA_HEIGH_HIGH(9999, ""),
	BOUND_FIRST_STA_LOW(0, ""),/*一阶统计*/
	BOUND_FIRST_STA_HIGH(62, ""),
	BOUND_FIRST_STA_MAX_LOW(2, ""),/*一阶统计(最大值）*/
	BOUND_FIRST_STA_MAX_HIGH(2, ""),
	BOUND_FIRST_STA_MIN_LOW(3, ""),/*一阶统计(最小值）*/
	BOUND_FIRST_STA_MIN_HIGH(3, ""),
	BOUND_FIRST_STA_MEAN_LOW(4, ""),/*一阶统计(平均值)*/
	BOUND_FIRST_STA_MEAN_HIGH(4, ""),
	BOUND_FIRST_STA_STA_LOW(10, ""),/*一阶统计(标准差)*/
	BOUND_FIRST_STA_STA_HIGH(10, ""),
	BOUND_HG_SEN_LOW(0, ""),/*传感器离地面高度*/
	BOUND_HG_SEN_HIGH(9999, ""),
	BOUND_HW_SEN_LOW(0, ""),/*传感器离水面高度*/
	BOUND_HW_SEN_HIGH(9999, ""),
	BOUND_METHOD_R_LOW(0, ""),/*降水测量方法*/
	BOUND_METHOD_R_HIGH(14, ""),
	BOUND_DEV_TYPE_L_LOW(0, ""),/*测量蒸发的仪器类型或作物类型*/
	BOUND_DEV_TYPE_L_HIGH(9999, ""),
	BOUND_L_WL_LOW(0, ""),/*蒸发水位*/
	BOUND_L_WL_HIGH(9999, ""),
	BOUND_TIME_MEAN_LOW(0, ""),/*时间意义*/
	BOUND_TIME_MEAN_HIGH(30, ""),
	BOUND_RT_LOW(-600, ""),/*路温*/
	BOUND_RT_HIGH(800, ""),
	BOUND_V_BACK_ATR_LOW(0, ""),/*能见度后面值属性*/
	BOUND_V_BACK_ATR_HIGH(2, ""),
	BOUND_VER_MEAN_LOW(0, ""),/*垂直探测意义*/
	BOUND_VER_MEAN_HIGH(63, ""),
	BOUND_CLOUD_SYSTEM_LOW(0, ""),/*云探测系统*/
	BOUND_CLOUD_SYSTEM_HIGH(14, ""),
	BOUND_CLOUD_CLASS_LOW(0, ""),/*云类型*/
	BOUND_CLOUD_CLASS_HIGH(63, ""),/*云类型*/
	BOUND_DEC_METHOD_GROUDSTATE_LOW(0, ""),/*地面状态测量方法*/
	BOUND_DEC_METHOD_GROUDSTATE_HIGH(14, ""),
	BOUND_GROUDSTATE_LOW(0, ""),/*地面状态*/
	BOUND_GROUDSTATE_HIGH(19, ""),
	BOUND_DEC_METHOD_SNOWDEPTH_LOW(0, ""),/*雪深测量方法*/
	BOUND_DEC_METHOD_SNOWDEPTH_HIGH(14, ""),
	BOUND_ROAD_STATE_LOW(0, ""),/*路面状况*/
	BOUND_ROAD_STATE_HIGH(19, ""),
	BOUND_DEPTH_SNOW_LOW(0, ""),/*路面雪层厚度*/
	BOUND_DEPTH_SNOW_HIGH(9999, ""),
	BOUND_DEPTH_WATER_LOW(0, ""),/*路面水层厚度*/
	BOUND_DEPTH_WATER_HIGH(9999, ""),
	BOUND_DEPTH_ICE_LOW(0, ""),/*路面冰层厚度*/
	BOUND_DEPTH_ICE_HIGH(9999, ""),
	BOUND_T_ICEPOINT_LOW(-600, ""),/*路面冰点温度*/
	BOUND_T_ICEPOINT_HIGH(800, ""),
	BOUND_SNOWMELT_CON_LOW(0, ""),/*融雪剂浓度*/
	BOUND_SNOWMELT_CON_HIGH(9999, ""),
	BOUND_PHE_DEC_SYSTEM_LOW(0, ""),/*主要天气现象检测系统*/
	BOUND_PHE_DEC_SYSTEM_HIGH(14, ""),
	BOUND_PHENOMENACODE_LOW(0, ""),/*现在天气现象编码*/
	BOUND_PHENOMENACODE_HIGH(90, ""),
	BOUND_TORNADO_DISTANCE_LOW(0, ""),/*龙卷风距离*/
	BOUND_TORNADO_DISTANCE_HIGH(9999, ""),
	BOUND_TORNADO_POSITION_LOW(0, ""),/*龙卷风方向*/
	BOUND_TORNADO_POSITION_HIGH(360, ""),
	BOUND_PASTWEATHER_LOW(0, ""),/*过去天气现象*/
	BOUND_PASTWEATHER_HIGH(8, ""),
	BOUND_YEAR_LOW(1, ""),/*年*/
	BOUND_YEAR_HIGH(2100, ""),
	BOUND_MONTH_LOW(1, ""),/*月*/
	BOUND_MONTH_HIGH(12, ""),
	BOUND_DAY_LOW(1, ""),/*日*/
	BOUND_DAY_HIGH(31, ""),
	BOUND_TIME_ADD_LOW(-9999, ""),/*时间增量*/
	BOUND_TIME_ADD_HIGH(0, ""),
	BOUND_SHORT_TIME_ADD_LOW(0, ""),/*短时间增量*/
	BOUND_SHORT_TIME_ADD_HIGH(9999, ""),

	BOUND_EUVA_LOW(0, ""),//UVA辐射
	BOUND_EUVA_HIGH(9999, ""),
	BOUND_EUVB_LOW(0, ""),//UVB辐射
	BOUND_EUVB_HIGH(9999, ""),
	BOUND_EALR_LOW(0, ""),//大气长波辐射
	BOUND_EALR_HIGH(9999, ""),
	BOUND_ESLR_LOW(0, ""),//地面长波辐射
	BOUND_ESLR_HIGH(9999, ""),
	BOUND_EPAR_LOW(0, ""),//光合作用辐射照度
	BOUND_EPAR_HIGH(9999, ""),
	//太阳直接辐射
	BOUND_ESI_LOW(0, ""),
	BOUND_ESI_HIGH(9999, ""),
	//UVA辐射曝辐量
	BOUND_HUVA_LOW(0, ""),
	BOUND_HUVA_HIGH(9999, ""),
	//UVB辐射曝辐量
	BOUND_HUVB_LOW(0, ""),
	BOUND_HUVB_HIGH(9999, ""),
	//大气长波辐射曝辐量
	BOUND_HALR_LOW(0, ""),
	BOUND_HALR_HIGH(9999, ""),
	//地面长波辐射曝辐量
	BOUND_HSLR_LOW(0, ""),
	BOUND_HSLR_HIGH(9999, ""),
	//光合作用辐射曝辐量
	BOUND_HPAR_LOW(0, ""),
	BOUND_HPAR_HIGH(9999, ""),
	//作用层情况
	BOUND_LEVEL_CON_LOW(0, ""),
	BOUND_LEVEL_CON_HIGH(6, ""),
	//作用层状况
	BOUND_LEVEL_STA_LOW(0, ""),
	BOUND_LEVEL_STA_HIGH(14, ""),
	//辐射表风速
	BOUND_RADI_METER_F_LOW(0, ""),
	BOUND_RADI_METER_F_HIGH(9999, ""),
	//辐射表温度
	BOUND_RADI_METER_T_LOW(0, ""),
	BOUND_RADI_METER_T_HIGH(9999, ""),
	/*总降水量*/
	BOUND_R_TOTAL_LOW(0, ""),
	BOUND_R_TOTAL_HIGH(9999, ""),
	/*附加字段含义*/
	BOUND_DES_MEANING_LOW(0, ""),
	BOUND_DES_MEANING_HIGH(63, "");
	
	private double value;
	private String description;
	ElementBound(double value, String description) {
		this.value = value;
		this.description = description;
	}
	public double getValue() {
		return value;
	}
	public String getDescription() {
		return description;
	}
	
}
