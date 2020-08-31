package org.cimiss2.dwp.tools.utils;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>

 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《》 </a>
 *      <li> <a href=" "> 《》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年12月3日 上午11:33:23   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class UparLevel {
	
	public static void main(String[] args) {
//		String aString = toBinary(2048, 18);
//		System.out.println(aString);
		UparLevel uparLevel = new UparLevel();
		setLevels(uparLevel, "131072", 18);
		System.out.println(uparLevel.getConvectionLvs());
	}
	
	/**
	 * 2 标准层层数
	 */
	private int standardLvs = 0;
	/**
	 * 3 对流层层数
	 */
	private int convectionLvs = 0;
	/**
	 * 4 最大风层
	 */
	private int maxWindLvs = 0;
	/**
	 * 5温度特性层层数
	 */
	private int temperatureLvs = 0;
	/**
	 * 7 风特性层
	 */
	private int windLvs = 0;
	/**
	 * 15 零度层
	 */
	private int zeroLvs = 0;
	/**
	 * 16 近地面层
	 */
	private int heightLvs = 0;
	
	public static void setLevels(UparLevel levels, String detectMeans, int bitWidth){
		try{
			int means = (int)Double.parseDouble(detectMeans);
			String aString = toBinary(means, bitWidth);
			int length = aString.length();
			for(int i = 0; i < length; i ++){
				if(aString.charAt(i) == '1'){
					if(i == 1) //  
						levels.setStandardLvs(levels.getStandardLvs() + 1);
					else if(i == 2) // 
						levels.setConvectionLvs(levels.getConvectionLvs() + 1);
					else if(i == 3) //  
						levels.setMaxWindLvs(levels.getMaxWindLvs() + 1);
					else if(i == 4) // 
						levels.setTemperatureLvs(levels.getTemperatureLvs() + 1);
					else if(i == 6) //  
						levels.setWindLvs(levels.getWindLvs() + 1);
					else if(i == 14) //  
						levels.setZeroLvs(levels.getZeroLvs() + 1);
					else if(i == 15) //  
						levels.setHeightLvs(levels.getHeightLvs() + 1);
				}
			}
				
		}catch (Exception e) {
			//未指明探测意义，不知道层次含义，什么也不做
		}
		
	}
	
    /**
     * 将一个int数字转换为二进制的字符串形式。
     * @param detectMeans 需要转换的int类型数据
     * @param bitWidth 要转换的二进制位数，位数不足则在前面补0
     * @return 二进制的字符串形式
     */
	 public static String toBinary(int detectMeans, int bitWidth) {
        int value = 1 << bitWidth | detectMeans;
        String bs = Integer.toBinaryString(value); 
        return  bs.substring(1);
    }
	
	
	public int getStandardLvs() {
		return standardLvs;
	}
	public void setStandardLvs(int standardLvs) {
		this.standardLvs = standardLvs;
	}
	public int getConvectionLvs() {
		return convectionLvs;
	}
	public void setConvectionLvs(int convectionLvs) {
		this.convectionLvs = convectionLvs;
	}
	public int getMaxWindLvs() {
		return maxWindLvs;
	}
	public void setMaxWindLvs(int maxWindLvs) {
		this.maxWindLvs = maxWindLvs;
	}
	public int getTemperatureLvs() {
		return temperatureLvs;
	}
	public void setTemperatureLvs(int temperatureLvs) {
		this.temperatureLvs = temperatureLvs;
	}
	public int getWindLvs() {
		return windLvs;
	}
	public void setWindLvs(int windLvs) {
		this.windLvs = windLvs;
	}
	public int getZeroLvs() {
		return zeroLvs;
	}
	public void setZeroLvs(int zeroLvs) {
		this.zeroLvs = zeroLvs;
	}

	public int getHeightLvs() {
		return heightLvs;
	}

	public void setHeightLvs(int heightLvs) {
		this.heightLvs = heightLvs;
	}
	
	
}
