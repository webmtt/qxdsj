package cma.cimiss2.dpc.split_msg.bean;

/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description: 文件分类规则</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note     sod_code ： 资料存储的四级编码
*           split_index ： 需要检测文件名拆分的关键信息索引位置
*           			 ${n} 表示取自原文件名拆分后数组下标为n的字符串
						 ${11:0:2}表示取自原文件名拆分后数组下标为12的字符串 0字符开始，取两个字符长度的字符串。
		    split_content ：split_index中根据索引提取的内容是否与split_content配置内容一致

* <split_filename>
*	<!-- 该类文件的SOD编码 -->
*	<sod_code>F.0027.0001.S001</sod_code>
*	<!-- 文件名中分割后的  取文件的-->
*	<split_index>${6}-${7}</split_index>
*	<split_content>all-RMAPS</split_content>
*  </split_filename>
* <b>ProjectName:</b> cimiss2-dwp-split-msg
* <br><b>PackageName:</b> org.cimiss2.dwp.split.msg.bean
* <br><b>ClassName:</b> SplitFileName
* <br><b>Date:</b> 2019年9月17日 下午5:53:50
 */
public class SplitFileName {
	
	private String sod_code;
	private String split_index;
	private String split_content;
	public String getSod_code() {
		return sod_code;
	}
	public void setSod_code(String sod_code) {
		this.sod_code = sod_code;
	}
	public String getSplit_index() {
		return split_index;
	}
	public void setSplit_index(String split_index) {
		this.split_index = split_index;
	}
	public String getSplit_content() {
		return split_content;
	}
	public void setSplit_content(String split_content) {
		this.split_content = split_content;
	}
}
