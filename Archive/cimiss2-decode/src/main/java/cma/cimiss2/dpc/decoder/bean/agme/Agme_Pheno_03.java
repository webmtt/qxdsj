package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
    气象水文现象名称子要素
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象自然物候要素—气象水文现象》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月7日 上午9:52:57   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 * @version 0.0.1
 */
public class Agme_Pheno_03 extends AgmeReportHeader{

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
	 * The hydrological phenomenon name.
	 *
	 * @Fields hydrologicalPhenomenonName : 水文现象名称<br>
	 * unit:  <br>
	 * BUFR FXY: V71300 <br>
	 * descriptionCN: <br>
	 * decode rule: 直接取值<br>
	 * field rule: 直接赋值
	 */
	Double hydrologicalPhenomenonName;
	

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
	 * Gets the hydrological phenomenon name.
	 *
	 * @return the hydrological phenomenon name
	 */
	public Double getHydrologicalPhenomenonName() {
		return hydrologicalPhenomenonName;
	}

	/**
	 * Sets the hydrological phenomenon name.
	 *
	 * @param hydrologicalPhenomenonName the new hydrological phenomenon name
	 */
	public void setHydrologicalPhenomenonName(Double hydrologicalPhenomenonName) {
		this.hydrologicalPhenomenonName = hydrologicalPhenomenonName;
	}
	
}
