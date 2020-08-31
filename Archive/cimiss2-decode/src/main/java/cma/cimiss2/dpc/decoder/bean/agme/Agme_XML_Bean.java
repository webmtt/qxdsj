package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.List;
import java.util.Map;
// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	农气XML资料解析结果对象类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《数据库逻辑结构设计-农气分册-新流程_20180108.docx》 </a>
 *      <li> <a href=" "> 《附件：1．农业气象观测站XML上传数据文件内容与传输规定（暂行） v1.0.doc》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年10月11日 上午10:34:01   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class Agme_XML_Bean {
	
	/** 资料类别. */
	private String Type;
	
	/** 要素编码——要素值列表. */
	private List<Map<String, Object>> sql;
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return Type;
	}
	
	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		Type = type;
	}
	
	/**
	 * Gets the sql.
	 *
	 * @return the sql
	 */
	public List<Map<String, Object>> getSql() {
		return sql;
	}
	
	/**
	 * Sets the sql.
	 *
	 * @param sql the sql
	 */
	public void setSql(List<Map<String, Object>> sql) {
		this.sql = sql;
	}
}
