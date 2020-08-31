package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
       农业气象畜牧要素实体类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《农业气象畜牧要素—牧草发育期》 </a>
 *      <li> <a href=" "> 《农业气象畜牧要素—牧草生长高度》 </a>
 *      <li> <a href=" "> 《农业气象畜牧要素—牧草产量》 </a>
 *      <li> <a href=" "> 《农业气象畜牧要素—牧草覆盖度及草层采食度》 </a>
 *      <li> <a href=" "> 《农业气象畜牧要素—灌木半灌木密度》 </a>
 *      <li> <a href=" "> 《农业气象畜牧要素—家畜膘情等级与羯羊重调查》 </a>
 *      <li> <a href=" "> 《农业气象畜牧要素—家畜羯羊重调查》 </a>
 *      <li> <a href=" "> 《农业气象畜牧要素—畜群基本情况调查》 </a>
 *      <li> <a href=" "> 《农业气象畜牧要素—牧事活动调查》 </a>
 *      <li> <a href=" "> 《农业气象畜牧要素—草层高度子要素》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年9月6日 下午4:38:19   dengyongliang    Initial creation.
 * </pre>
 * 
 * @author dengyongliang
 * @version 0.0.1
 */
public class Agme_Grass {
	
	/**  牧草发育期子要素. */
	List<Agme_Grass_01> agmeGrass_01 = new ArrayList<Agme_Grass_01>(0);
	
	/**   牧草生长高度子要素. */
	List<Agme_Grass_02> agmeGrass_02 = new ArrayList<Agme_Grass_02>(0);
	
	/**   牧草产量子要素. */
	List<Agme_Grass_03> agmeGrass_03 = new ArrayList<Agme_Grass_03>(0);
	
	/**   覆盖度及草层采食度子要素. */
	List<Agme_Grass_04> agmeGrass_04 = new ArrayList<Agme_Grass_04>(0);
	
	/**   灌木、半灌木密度子要素. */
	List<Agme_Grass_05> agmeGrass_05 = new ArrayList<Agme_Grass_05>(0);
	
	/**   家畜膘情等级调查子要素. */
	List<Agme_Grass_06> agmeGrass_06 = new ArrayList<Agme_Grass_06>(0);
	
	/**   家畜羯羊重调查. */
	List<Agme_Grass_07> agmeGrass_07 = new ArrayList<Agme_Grass_07>(0);
	
	/**   畜群基本情况调查. */
	List<Agme_Grass_08> agmeGrass_08 = new ArrayList<Agme_Grass_08>(0);
	
	/**  牧事活动调查. */
	List<Agme_Grass_09> agmeGrass_09 = new ArrayList<Agme_Grass_09>(0);
	
	/**   草层高度子要素. */
	List<Agme_Grass_10> agmeGrass_10 = new ArrayList<Agme_Grass_10>(0);
	
	/**  牧草观测类型. */
	public List<String> grassTypes = new ArrayList<>();
	
	/**
	 * Gets the agme grass 01.
	 *
	 * @return the agme grass 01
	 */
	public List<Agme_Grass_01> getAgmeGrass_01() {
		return agmeGrass_01;
	}
	
	/**
	 * Sets the agme grass 01.
	 *
	 * @param agmeGrass_01 the new agme grass 01
	 */
	public void setAgmeGrass_01(List<Agme_Grass_01> agmeGrass_01) {
		this.agmeGrass_01 = agmeGrass_01;
	}
	
	/**
	 * Gets the agme grass 02.
	 *
	 * @return the agme grass 02
	 */
	public List<Agme_Grass_02> getAgmeGrass_02() {
		return agmeGrass_02;
	}
	
	/**
	 * Sets the agme grass 02.
	 *
	 * @param agmeGrass_02 the new agme grass 02
	 */
	public void setAgmeGrass_02(List<Agme_Grass_02> agmeGrass_02) {
		this.agmeGrass_02 = agmeGrass_02;
	}
	
	/**
	 * Gets the agme grass 03.
	 *
	 * @return the agme grass 03
	 */
	public List<Agme_Grass_03> getAgmeGrass_03() {
		return agmeGrass_03;
	}
	
	/**
	 * Sets the agme grass 03.
	 *
	 * @param agmeGrass_03 the new agme grass 03
	 */
	public void setAgmeGrass_03(List<Agme_Grass_03> agmeGrass_03) {
		this.agmeGrass_03 = agmeGrass_03;
	}
	
	/**
	 * Gets the agme grass 04.
	 *
	 * @return the agme grass 04
	 */
	public List<Agme_Grass_04> getAgmeGrass_04() {
		return agmeGrass_04;
	}
	
	/**
	 * Sets the agme grass 04.
	 *
	 * @param agmeGrass_04 the new agme grass 04
	 */
	public void setAgmeGrass_04(List<Agme_Grass_04> agmeGrass_04) {
		this.agmeGrass_04 = agmeGrass_04;
	}
	
	/**
	 * Gets the agme grass 05.
	 *
	 * @return the agme grass 05
	 */
	public List<Agme_Grass_05> getAgmeGrass_05() {
		return agmeGrass_05;
	}
	
	/**
	 * Sets the agme grass 05.
	 *
	 * @param agmeGrass_05 the new agme grass 05
	 */
	public void setAgmeGrass_05(List<Agme_Grass_05> agmeGrass_05) {
		this.agmeGrass_05 = agmeGrass_05;
	}
	
	/**
	 * Gets the agme grass 06.
	 *
	 * @return the agme grass 06
	 */
	public List<Agme_Grass_06> getAgmeGrass_06() {
		return agmeGrass_06;
	}
	
	/**
	 * Sets the agme grass 06.
	 *
	 * @param agmeGrass_06 the new agme grass 06
	 */
	public void setAgmeGrass_06(List<Agme_Grass_06> agmeGrass_06) {
		this.agmeGrass_06 = agmeGrass_06;
	}
	
	/**
	 * Gets the agme grass 07.
	 *
	 * @return the agme grass 07
	 */
	public List<Agme_Grass_07> getAgmeGrass_07() {
		return agmeGrass_07;
	}
	
	/**
	 * Sets the agme grass 07.
	 *
	 * @param agmeGrass_07 the new agme grass 07
	 */
	public void setAgmeGrass_07(List<Agme_Grass_07> agmeGrass_07) {
		this.agmeGrass_07 = agmeGrass_07;
	}
	
	/**
	 * Gets the agme grass 08.
	 *
	 * @return the agme grass 08
	 */
	public List<Agme_Grass_08> getAgmeGrass_08() {
		return agmeGrass_08;
	}
	
	/**
	 * Sets the agme grass 08.
	 *
	 * @param agmeGrass_08 the new agme grass 08
	 */
	public void setAgmeGrass_08(List<Agme_Grass_08> agmeGrass_08) {
		this.agmeGrass_08 = agmeGrass_08;
	}
	
	/**
	 * Gets the agme grass 09.
	 *
	 * @return the agme grass 09
	 */
	public List<Agme_Grass_09> getAgmeGrass_09() {
		return agmeGrass_09;
	}
	
	/**
	 * Sets the agme grass 09.
	 *
	 * @param agmeGrass_09 the new agme grass 09
	 */
	public void setAgmeGrass_09(List<Agme_Grass_09> agmeGrass_09) {
		this.agmeGrass_09 = agmeGrass_09;
	}
	
	/**
	 * Gets the agme grass 10.
	 *
	 * @return the agme grass 10
	 */
	public List<Agme_Grass_10> getAgmeGrass_10() {
		return agmeGrass_10;
	}
	
	/**
	 * Sets the agme grass 10.
	 *
	 * @param agmeGrass_10 the new agme grass 10
	 */
	public void setAgmeGrass_10(List<Agme_Grass_10> agmeGrass_10) {
		this.agmeGrass_10 = agmeGrass_10;
	}
	
	


}
