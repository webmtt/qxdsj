package cma.cimiss2.dpc.decoder.tools.enumeration;

public enum Weather {
	W0(0, "保留"),
	W1(1, "Highest wind speed gusts greater than 11.5 m/s阵风的最高风速大于11.5 米/秒"),
	W2(2, "Highest mean wind speed greater than 17.5 m/s最高平均风速大于17.5 米/秒"),
	W7(7, "Visibility greater than 100 000 m 能见度大于100000米"),
	W10(10, "海市蜃楼 – 不规范"),
	W11(11, "海市蜃楼 – 远处物体的图象出现(上现蜃景)"),
	W12(12, "海市蜃楼 - 远处物体的图象清楚在出现在地平线上"),
	W13(13, "海市蜃楼 – 远处物体的图象出现倒影"),
	W14(14, "海市蜃楼 – 复杂, 多个远处物体的图象出现 (图象不倒)"),
	W15(15, "海市蜃楼 - 复杂, 多个远处物体的图象出现 (有些图象倒影)"),
	W16(16, "海市蜃楼 - 太阳或月亮略微畸变"),
	W17(17, "海市蜃楼 - 太阳可见，但在天文地平线以下"),
	W18(18, "海市蜃楼 – 月亮可见，但在天文地平线以下"),
	W20(20, "白天变暗，方向判定的最坏情况：坏"),
	W21(21, "白天变暗，方向判定的最坏情况：很坏"),
	W22(22, "白天变暗，方向判定的最坏情况：黑"),
	W31(31, "在日出时与热带扰动有关的云轻微着色"),
	W32(32, "在日出时与热带扰动有关的云呈深红色"),
	W33(33, "在日落时与热带扰动有关的云轻微着色"),
	W34(34, "在日落时与热带扰动有关的云呈深红色"),
	W35(35, "在45°下方形成或增强的热带扰动的CH云的辐合"),
	W36(36, "在45°上方热带扰动的CH云的辐合 （Convergence of CH clouds at Test point above 45° associated with Test tropical disturbance）"),
	W37(37, "在45°下方消散或减少的热带扰动的CH云的辐合"),
	W38(38, "在45°上方热带扰动的CH云的辐合"),
	W39(39, "保留"),
	W40(40, "白霜在水平表面"),
	W41(41, "白霜在水平和垂直表面"),
	W42(42, "包含沙或沙尘降水"),
	W43(43, "包含火山灰降水"),
	W50(50, "无风或微风后出现飑 "),
	W51(51, "无风或微风后一连串的飑"),
	W52(52, "阵风天气后出现飑"),
	W53(53, "阵风天气后一连串的飑"),
	W54(54, "飑后为阵风天气"),
	W55(55, "阵风天气伴有飑间隔出现"),
	W56(56, "飑快到观测站"),
	W57(57, "线飑"),
	W58(58, "飑伴随低吹或高吹砂或尘 "),
	W59(59, "线飑伴随低吹或高吹砂或尘"),
	W60(60, "温度稳定"),
	W61(61, "温度下降， 不到 0°C 以下"),
	W62(62, "温度上升， 不到 0°C 以上"),
	W63(63, "温度下降到 0°C 以下"),
	W64(64, "温度上升到 0°C 以下"),
	W65(65, "不规则变化，温度在0℃ 左右震荡"),
	W66(66, "不规则变化，温度在0℃左右震荡"),
	W67(67, "没有观测到温度变化"),
	W68(68, "不分配"),
	W69(69, "不明原因的温度变化，由于缺乏温度计"),
	W70(70, "能见度在指定的方向无变化（太阳*可见）"),
	W71(71, "能见度在指定的方向无变化（太阳*不可见）"),
	W72(72, "能见度在指定的方向增强（太阳*可见）"),
	W73(73, "能见度在指定的方向增强（太阳*不可见）"),
	W74(74, "能见度在指定的方向减弱（太阳*可见）"),
	W75(75, "能见度在指定的方向减弱（太阳*可见）"),
	W76(76, "雾来自指定的方向 "),
	W77(77, "雾已上升，但没有消散"),
	W78(78, "雾已消散，没有注意方向"),
	W79(79, "雾的碎片或堆积在移动"),
	W80(80, "峨嵋宝光"),
	W81(81, "虹"),
	W82(82, "日或月晕"),
	W83(83, "幻日 或 反日"),
	W84(84, "日柱"),
	W85(85, "日冕"),
	W86(86, "幽光"),
	W87(87, "在山上的幽光（高山辉）"),
	W88(88, "海市蜃楼"),
	W89(89, "黄道光"),
	W90(90, "圣埃尔莫的火"),
	W1023(91, "缺测值");
	
	private int code;
	private String description;

	Weather(int code, String description){
		this.code = code;
		this.description = description;
	}
	
	public Weather getWeatherByCode(int code) {
		for (Weather w : Weather.values()) {
			if (w.getCode() == code) {
				return w;
			}
		}
		throw new IllegalArgumentException();
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}