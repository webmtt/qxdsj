package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	农业气象灾害资料实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象灾害要素—农业灾害观测》 </a>
 *      <li> <a href=" "> 《农业气象灾害要素—农业灾害调查》 </a>
 *      <li> <a href=" "> 《农业气象灾害要素—牧草与家畜灾害》 </a>
 *      <li> <a href=" "> 《农业气象灾害要素—植物灾害子要素》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月6日 下午3:38:18   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class ZAgmeDisa {
	
	/** 农业灾害观测要素对象封装. */
	public List<ZAgmeDisa01> zAgmeDisa01s = new ArrayList<>();
	
	/** 农业灾害调查要素对象封装. */
	public List<ZAgmeDisa02> zAgmeDisa02s = new ArrayList<>();
	
	/** 牧草灾害要素对象封装. */
	public List<ZAgmeDisa03> zAgmeDisa03s = new ArrayList<>();
	
	/** 家畜灾害要素对象封装. */
	public List<ZAgmeDisa04> zAgmeDisa04s = new ArrayList<>();
	
	/** 植物灾害要素对象封装. */
	public List<ZAgmeDisa05> zAgmeDisa05s = new ArrayList<>();
	
	/** 农气灾害数据类型. */
	public List<String> disaTypes = new ArrayList<>();
}
