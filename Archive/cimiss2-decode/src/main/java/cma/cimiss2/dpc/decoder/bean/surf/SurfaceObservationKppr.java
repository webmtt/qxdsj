package cma.cimiss2.dpc.decoder.bean.surf;

import java.util.Date;
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	朝鲜降水资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《朝鲜降水数据表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 下午1:56:29   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class SurfaceObservationKppr {
	/**
	 * NO: 1  <br>
	 * nameCN: 区站号 <br>
	 * unit: 无  <br>
	 * BUFR FXY: V01301 <br>
	 * descriptionCN: 5位数字或第1位为字母，第2-5位为数字<br>
	 * decode rule: 直接取值<br>
     * field rule: 直接赋值 <br>
	 */
	private String stationNumberChina;
	
	/**
	 * NO: 2  <br>
	 * nameCN: 资料时间  <br>
	 * unit: 无 <br>
	 * BUFR FXY: V04001,V04002, V04003, V04004 <br>
	 * descriptionCN: 分、秒均为0 <br>
	 * decode rule: 直接取值<br>
     * field rule: 使用java时间转化工具类进行转化,标准形式为：yyyyMMddHHmmss <br>
	 */
	private Date ObservationTime;

	/**
	 * NO: 3  <br>
	 * nameCN:  小时降水量  <br>
	 * unit: 毫米 <br>
	 * BUFR FXY: V13019 <br>
	 * descriptionCN: 小时降水量<br>
	 * decode rule: 直接取值 <br>
     * field rule: 直接赋值 <br>
	 */
	private int FKPPR_V13019 = 999999;
	
	/**
	 * NO: 4  <br>
	 * nameCN: 24小时降水量(06时-06时) <br>
	 * unit: 毫米 <br>
	 * BUFR FXY: V13023 <br>
	 * descriptionCN: 24小时降水量(06时-06时)
	 * decode rule: 直接取值<br>
     * field rule: 直接赋值
	 */
	private int FKPPR_V13023 = 999999;
	
	public String getStationNumberChina() {
		return stationNumberChina;
	}

	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}

	public int getFKPPR_V13019() {
		return FKPPR_V13019;
	}

	public void setFKPPR_V13019(int fKPPR_V13019) {
		FKPPR_V13019 = fKPPR_V13019;
	}

	public int getFKPPR_V13023() {
		return FKPPR_V13023;
	}

	public void setFKPPR_V13023(int fKPPR_V13023) {
		FKPPR_V13023 = fKPPR_V13023;
	}

	public Date getObservationTime() {
		return ObservationTime;
	}

	public void setObservationTime(Date observationTime) {
		ObservationTime = observationTime;
	}
	
}