package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
          动植物物候期子要素（木本植物、草本植物、动物）
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象自然物候要素—植(动)物物候期》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月7日 上午9:35:05   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 * @version 0.0.1
 */
public class Agme_Pheno_01 extends AgmeReportHeader {

	/**
	 * The appear time.
	 *
	 * @Fields appearTime : 出现时间<br>
	 * unit: <br>
	 * BUFR FXY: D_DATETIME <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值 <br>
	 * field rule: 使用java时间转化工具类进行转化, 标准形式为：yyyyMMddHHmmss <br>
	 */
	Date appearTime;

	/**
	 * The plant or animal name.
	 *
	 * @Fields plantName : 植物名称<br>
	 * unit: 代码表 <br>
	 * BUFR FXY: V71501 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值
	 */
	Double plantOrAnimalName;

	/**
	 * The phenology name.
	 *
	 * @Fields phenologyName : 物候期名称<br>
	 * unit: 代码表<br>
	 * BUFR FXY: V71618<br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值
	 */
	Double phenologyName;

	/**
	 * Gets the appear time.
	 *
	 * @return the appear time
	 */
	public Date getAppearTime() {
		return appearTime;
	}

	/**
	 * Sets the appear time.
	 *
	 * @param appearTime the new appear time
	 */
	public void setAppearTime(Date appearTime) {
		this.appearTime = appearTime;
	}
 
	/**
	 * Gets the plant or animal name.
	 *
	 * @return the plant or animal name
	 */
	public Double getPlantOrAnimalName() {
		return plantOrAnimalName;
	}

	
	/**
	 * Sets the plant or animal name.
	 *
	 * @param plantOrAnimalName the new plant or animal name
	 */
	public void setPlantOrAnimalName(Double plantOrAnimalName) {
		this.plantOrAnimalName = plantOrAnimalName;
	}

	/**
	 * Gets the phenology name.
	 *
	 * @return the phenology name
	 */
	public Double getPhenologyName() {
		return phenologyName;
	}

	/**
	 * Sets the phenology name.
	 *
	 * @param phenologyName the new phenology name
	 */
	public void setPhenologyName(Double phenologyName) {
		this.phenologyName = phenologyName;
	}

}
