package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.ArrayList;
import java.util.List;
// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	作物要素数据文件，正文由10个子要素组成<br>
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象作物要素—作物生长发育数据表》 </a>
 *      <li> <a href=" "> 《农业气象作物要素—干物质与叶面积 》 </a>
 *      <li> <a href=" "> 《农业气象作物要素—灌浆速度 》 </a>
 *      <li> <a href=" "> 《农业气象作物要素—产量因素与产量结构 》 </a>
 *      <li> <a href=" "> 《 农业气象作物要素—关键农事活动数据》 </a>
 *      <li> <a href=" "> 《农业气象作物要素—县产量水平 》 </a>
 *      <li> <a href=" "> 《农业气象作物要素—植株分器官干物质重量数据表 》 </a>
 *      <li> <a href=" "> 《 农业气象作物要素—大田生育状况调查数据表》 </a>
 *      <li> <a href=" "> 《农业气象作物要素—大田基本情况数据表 》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月21日 下午11:58:05   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 * @version 0.0.1
 */
public class ZAgmeCrop {
	
	/** 作物生长发育对象封装. */
	public List<ZAgmeCrop01> zAgmeCrop01s = new ArrayList<>();
	
	/** 干物质与叶面积 对象封装. */
	public List<ZAgmeCrop02> zAgmeCrop02s = new ArrayList<>();
	
	/** 灌浆速度对象封装. */
	public List<ZAgmeCrop03> zAgmeCrop03s = new ArrayList<>();
	
	/** 产量因素对象封装. */
	public List<ZAgmeCrop04> zAgmeCrop04s = new ArrayList<>();
	
	/** 产量结构对象封装. */
	public List<ZAgmeCrop05> zAgmeCrop05s = new ArrayList<>();
	
	/** 关键农事活动对象封装. */
	public List<ZAgmeCrop06> zAgmeCrop06s = new ArrayList<>();
	
	/** 县产量水平对象封装. */
	public List<ZAgmeCrop07> zAgmeCrop07s = new ArrayList<>();
	
	/** 植株分器官干物质重量对象封装. */
	public List<ZAgmeCrop08> zAgmeCrop08s = new ArrayList<>();
	
	/** 大田生育状况调查对象封装. */
	public List<ZAgmeCrop09> zAgmeCrop09s = new ArrayList<>();
	
	/** 大田基本情况对象封装. */
	public List<ZAgmeCrop10> zAgmeCrop10s = new ArrayList<>();
	
	/** 农作物观测资料类型. */
	public List<String> cropTypes = new ArrayList<>();

}
