package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * <br>.
 *
 * @author wangzunpeng
 * @Title:  Agme_Soil.java
 * @Package cma.cimiss2.dpc.decoder.bean.agme.soil
 * @Description:    TODO(用一句话描述该类做什么)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月28日 上午9:26:02   wangzunpeng    Initial creation.
 * </pre>
 */
public class Agme_Soil {

	/**
	 * The agme soil 01 s.
	 *
	 * @Fields agme_Soil_01s : 土壤水文物理特性
	 */
	public List<Agme_Soil_01> agme_Soil_01s = new ArrayList<Agme_Soil_01>(0);
	
	/**
	 * The agme soil 02 s.
	 *
	 * @Fields agme_Soil_02s : 土壤相对湿度
	 */
	public List<Agme_Soil_02> agme_Soil_02s = new ArrayList<Agme_Soil_02>(0);
	
	/**
	 * The agme soil 03 s.
	 *
	 * @Fields agme_Soil_03s : 水分总储存量
	 */
	public List<Agme_Soil_03> agme_Soil_03s = new ArrayList<Agme_Soil_03>(0);
	
	/**
	 * The agme soil 04 s.
	 *
	 * @Fields agme_Soil_04s : 有效水分储存量
	 */
	public List<Agme_Soil_04> agme_Soil_04s = new ArrayList<Agme_Soil_04>(0);
	
	/**
	 * The agme soil 05 s.
	 *
	 * @Fields agme_Soil_05s : 土壤冻结与解冻
	 */
	public List<Agme_Soil_05> agme_Soil_05s = new ArrayList<Agme_Soil_05>(0);
	
	/**
	 * The agme soil 06 s.
	 *
	 * @Fields agme_Soil_06s : 土壤重量含水率
	 */
	public List<Agme_Soil_06> agme_Soil_06s = new ArrayList<Agme_Soil_06>(0);
	
	/**
	 * The agme soil 07 s.
	 *
	 * @Fields agme_Soil_07s : 干土层与地下水位
	 */
	public List<Agme_Soil_07> agme_Soil_07s = new ArrayList<Agme_Soil_07>(0);
	
	/**
	 * The agme soil 08 s.
	 *
	 * @Fields agme_Soil_08s : 降水灌溉与渗透
	 */
	public List<Agme_Soil_08> agme_Soil_08s = new ArrayList<Agme_Soil_08>(0);
	
	/**
	 * The soil types.
	 *
	 * @Fields soilTypes : 存储一个土壤水分文件中共有哪几种数据
	 */
	public List<String> soilTypes = new ArrayList<String>(0);
	
}
