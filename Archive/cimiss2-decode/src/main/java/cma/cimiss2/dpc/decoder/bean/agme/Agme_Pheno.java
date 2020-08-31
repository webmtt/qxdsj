package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
               农业气象自然物候要素
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象自然物候要素—植(动)物物候期》 </a>
 *      <li> <a href=" "> 《农业气象自然物候要素—气象水文现象》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月7日 上午9:34:11   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 * @version 0.0.1
 */
public class Agme_Pheno {

	/**
	 * The agme pheno 01 s.
	 *
	 * @Fields agme_pheno_01s :  动植物物候期（木本植物、草本植物、动物）
	 */
	public List<Object> agme_pheno_01s = new ArrayList<Object>(0);
	
	/**
	 * The agme pheno 02 s.
	 *
	 * @Fields agme_pheno_01s :  动植物物候期（木本植物、草本植物、动物）
	 */
	public List<Object> agme_pheno_02s = new ArrayList<Object>(0);
	
	/**
	 * The agme pheno 04 s.
	 *
	 * @Fields agme_pheno_01s :  动植物物候期（木本植物、草本植物、动物）
	 */
	public List<Object> agme_pheno_04s = new ArrayList<Object>(0);
	
	/**
	 * The agme pheno 03 s.
	 *
	 * @Fields agme_pheno_03s : 气象水文现象
	 */
	public List<Object> agme_pheno_03s = new ArrayList<Object>(0);
	
	/**
	 * The pheno types.
	 *
	 * @Fields soilTypes : 存储一个自然物候气象文件中共有哪几种数据
	 */
	public List<String> phenoTypes = new ArrayList<String>(0);
}
