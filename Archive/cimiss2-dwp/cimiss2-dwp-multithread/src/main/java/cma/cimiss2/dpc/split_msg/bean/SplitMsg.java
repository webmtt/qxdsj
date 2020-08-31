package cma.cimiss2.dpc.split_msg.bean;

import java.util.ArrayList;
import java.util.List;

/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description: 需要拆分资料的配置</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* cts_code ： 资料的CTS四级编码  
* split_expression ： 拆分文件名的正则表达式
* <!-- 需要拆分的消息的  属性中配置cts_code 检查接收的消息是否需要拆分 -->
	<split-msg cts_code="F.0027.0001.R001" split_expression="- . _">
		<!-- 每一类文件名规则的拆分策略  split_expression 文件名拆分表达式-->
		<split_filename>
			<!-- 该类文件的SOD编码 -->
			<sod_code>F.0027.0001.S001</sod_code>
			<!-- 文件名中分割后的  取文件的-->
			<split_index>${6}-${7}</split_index>
			<split_content>all-RMAPS</split_content>
		</split_filename>
		
		<split_filename>
			<sod_code>F.0027.0001.S002</sod_code>
			<split_index>${6}-${7}</split_index>
			<split_content>3km-RMAPS</split_content>
		</split_filename>
		
		<split_filename>
			<sod_code>F.0027.0001.S003</sod_code>
			<split_index>${6}-${7}</split_index>
			<split_content>EMC-NNC</split_content>
		</split_filename>
		
	</split-msg>
* 
* <b>ProjectName:</b> cimiss2-dwp-split-msg
* <br><b>PackageName:</b> org.cimiss2.dwp.split.msg.bean
* <br><b>ClassName:</b> SplitMsg
* <br><b>Date:</b> 2019年9月17日 下午6:05:49
 */
public class SplitMsg {
	
	private String cts_code;
	private String split_expression;
	private List<SplitFileName> splitFileNames;
	
	public SplitMsg() {
		splitFileNames = new ArrayList<SplitFileName>();
	}
	public String getCts_code() {
		return cts_code;
	}
	public void setCts_code(String cts_code) {
		this.cts_code = cts_code;
	}
	public String getSplit_expression() {
		return split_expression;
	}
	public void setSplit_expression(String split_expression) {
		this.split_expression = split_expression;
	}
	public List<SplitFileName> getSplitFileNames() {
		return splitFileNames;
	}
	public void setSplitFileNames(List<SplitFileName> splitFileNames) {
		this.splitFileNames = splitFileNames;
	}
	
	public void put(SplitFileName splitFileName) {
		this.splitFileNames.add(splitFileName);
	}
}
