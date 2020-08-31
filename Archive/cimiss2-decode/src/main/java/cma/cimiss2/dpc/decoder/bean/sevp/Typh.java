package cma.cimiss2.dpc.decoder.bean.sevp;

import java.util.List;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	台风实况与预报要素表与键表构成的实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《台风实况与预报资料数据要素表》 </a>
 *      <li> <a href=" "> 《台风实况与预报要素资料数据键表》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 上午11:38:02   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Typh {
	/**
	 * 台风键值要素
	 */
	private TyphKey typhKey;
	/**
	 * 台风时次要素值集合
	 */
	private List<TyphEle> typhEles;
	
	public TyphKey getTyphKey() {
		return typhKey;
	}
	public void setTyphKey(TyphKey typhKey) {
		this.typhKey = typhKey;
	}
	public List<TyphEle> getTyphEles() {
		return typhEles;
	}
	public void setTyphEles(List<TyphEle> typhEles) {
		this.typhEles = typhEles;
	}
}
