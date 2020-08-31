<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>数据服务列表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}
.title{color:red;}
</style>
<script type="text/javascript">	
$(document).ready(function() {
	$("#treeTable").treeTable({expandLevel : 3});
	if($("#chk_value").val()!=null&&$("#chk_value").val()!=""){
		var chk_value = $("#chk_value").val().split(",");
		for(var i=0; i<chk_value.length; i++){
			$("input:checkbox[value='"+chk_value[i]+"']").attr('checked','true');
		}
	}

});
$(function() {
     var $subBox = $("input[name='checkbox']");
     $subBox.click(function(){
    	 if($(this).val()=='routine'){
    		 $("[name='checkbox']").attr("checked",false);
        	 $(this).attr("checked",true); 
    	 }else{
    		 $("#routine").attr("checked",false);
    		 var temp=0;
    		 $('input[name="checkbox"]:checked').each(function(){
    			 temp++;
    	     });
    		 if(temp>2){
    			 alert("最多选择两种过程类型！");
    			 return false;
    		 }
    	 }
     });
 });
function publish() {
	if (confirm("确定要发布吗？")) {
		var types="";
		$('input[name="checkbox"]:checked').each(function(){
			types+=$(this).val()+",";
		});
		if(types!=null){
			types=types.substring(0, types.length-1);
		}
		if(types==null){
			alert("请选择过程类型！");
			return false;
		}else if(types.split(",").length!=1&&types.indexOf("routine")>-1){
			alert("选择常规后不能选择其他过程类型！");
			return false;
		}
		loading('正在提交，请稍等...');
		$("#form1").attr("action", "${ctx}/portalImage/publish?types="+types);
		$("#form1").submit();
	} 
}
function selectById(id){
	$("#temp"+id).val("");
};
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0)">重要业务产品列表</a></li>
	</ul>
	<tags:message content="${message}"/>
	<form id="form1"  method="post">
	<input id="chk_value" name="chk_value" type="hidden"
		value="${chk_value}" />
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead><tr><th></th><th style="text-align:center;">过程类型</th>
			<th style="text-align:center;width:35%">开始时间设置</th><th style="text-align:center;">区域选择</th><th style='text-align:center'>发布状态</th><th style='text-align:center'>操作</th></tr></thead>
			<tbody>
			<c:forEach items="${list}" var="obj" varStatus="status">
				<tr>
				    <td style="text-align:center;width:5%;">
				       <c:if test="${obj.type == 'routine'}">
				           <input id="routine" type="checkbox" name="checkbox" value="${obj.type}" />
				       </c:if>
				      <c:if test="${obj.type != 'routine'}">
				           <input type="checkbox" name="checkbox" value="${obj.type}" />
				       </c:if>
				       <input type="hidden" name="ids" value="${obj.type}"/>
				    </td> 
					<td style="text-align:center;">${obj.typeName}</td>
					<td style="text-align:center;width:25%;">
					   <c:if test="${obj.type == 'routine'}">
					      <input id="temp${status.index + 1}" type="hidden" name="startTimes" class="input-small Wdate" style="margin:0" value="${obj.startTime}"/>
					   </c:if>
					   <c:if test="${obj.type !='routine'}">
					      <input id="temp${status.index + 1}" type="text" name="startTimes" class="input-small Wdate"  style="margin:0" value="${obj.startTime}" onclick="WdatePicker({dateFmt:'yyyyMMdd',isShowClear:false});"/>
					      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					      <a href="javascript:void(0)"  onclick="selectById('${status.index + 1}')"`>
					      <input  type="button" style="margin-top: -10px;" value="清空" />
					      </a>
					   </c:if>
					</td>
					<td style="text-align:center;">
					<c:if test="${obj.type =='Typhoon'}">
					     <select name="Typhoon" class="input-small"  style="margin:0">
	    				    <option value="AREA_CODE:CHN" <c:if test="${obj.area=='AREA_CODE:CHN'}">selected = "selected"</c:if>>全国</option>
	    				    <option value="AREA_CODE:CCN" <c:if test="${obj.area=='AREA_CODE:CCN'}">selected = "selected"</c:if>>华中</option>
	    				    <option value="AREA_CODE:ECN" <c:if test="${obj.area=='AREA_CODE:ECN'}">selected = "selected"</c:if>>华东</option>
	    				    <option value="AREA_CODE:NCN" <c:if test="${obj.area=='AREA_CODE:NCN'}">selected = "selected"</c:if>>华北</option>
	    				    <option value="AREA_CODE:NEC" <c:if test="${obj.area=='AREA_CODE:NEC'}">selected = "selected"</c:if>>东北</option>
	    				    <option value="AREA_CODE:NWC" <c:if test="${obj.area=='AREA_CODE:NWC'}">selected = "selected"</c:if>>西北</option>
	    				    <option value="AREA_CODE:SCN" <c:if test="${obj.area=='AREA_CODE:SCN'}">selected = "selected"</c:if>>华南</option>
	    				    <option value="AREA_CODE:SWC" <c:if test="${obj.area=='AREA_CODE:SWC'}">selected = "selected"</c:if>>西南</option>
	    				    <option value="AREA_CODE:BCJ" <c:if test="${obj.area=='AREA_CODE:BCJ'}">selected = "selected"</c:if>>长江流域</option>
	    				    <option value="AREA_CODE:BHH" <c:if test="${obj.area=='AREA_CODE:BHH'}">selected = "selected"</c:if>>黄淮流域</option>
	    				    <option value="AREA_CODE:CES" <c:if test="${obj.area=='AREA_CODE:CES'}">selected = "selected"</c:if>>东南沿海</option>
	    				</select>
					</c:if>
					<c:if test="${obj.type =='Rainstorm'}">
					     <select name="Rainstorm" class="input-small">
	    				    <option value="AREA_CODE:CHN" <c:if test="${obj.area=='AREA_CODE:CHN'}">selected = "selected"</c:if>>全国</option>
	    				    <option value="AREA_CODE:CCN" <c:if test="${obj.area=='AREA_CODE:CCN'}">selected = "selected"</c:if>>华中</option>
	    				    <option value="AREA_CODE:ECN" <c:if test="${obj.area=='AREA_CODE:ECN'}">selected = "selected"</c:if>>华东</option>
	    				    <option value="AREA_CODE:NCN" <c:if test="${obj.area=='AREA_CODE:NCN'}">selected = "selected"</c:if>>华北</option>
	    				    <option value="AREA_CODE:NEC" <c:if test="${obj.area=='AREA_CODE:NEC'}">selected = "selected"</c:if>>东北</option>
	    				    <option value="AREA_CODE:NWC" <c:if test="${obj.area=='AREA_CODE:NWC'}">selected = "selected"</c:if>>西北</option>
	    				    <option value="AREA_CODE:SCN" <c:if test="${obj.area=='AREA_CODE:SCN'}">selected = "selected"</c:if>>华南</option>
	    				    <option value="AREA_CODE:SWC" <c:if test="${obj.area=='AREA_CODE:SWC'}">selected = "selected"</c:if>>西南</option>
	    				    <option value="AREA_CODE:BCJ" <c:if test="${obj.area=='AREA_CODE:BCJ'}">selected = "selected"</c:if>>长江流域</option>
	    				    <option value="AREA_CODE:BHH" <c:if test="${obj.area=='AREA_CODE:BHH'}">selected = "selected"</c:if>>黄淮流域</option>
	    				    <option value="AREA_CODE:CES" <c:if test="${obj.area=='AREA_CODE:CES'}">selected = "selected"</c:if>>东南沿海</option>
	    				</select>
					</c:if>
					</td>
					<td style='text-align:center'>
					<c:if test="${obj.invalid==0}">
	    				   <span style="color:red;display:inline-block;width:60px;text-align:center">发布</span>
    				</c:if>
    				<c:if test="${obj.invalid==1}">
    				   <span style="display:inline-block;width:60px;text-align:center">未发布</span>
    				</c:if>
					</td>
					<td style='text-align:center'><a href="${ctx}/portalImage/getProductList?type=${obj.type}">查看</a></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<p  class="title">选择常规过程类型后不能选择其他过程类型，除常规类型之外的类型为多选,并且最多选择两种！</p>
		<div class="form-actions pagination-left">
			&nbsp;
			&nbsp;
			&nbsp;
			&nbsp;
			&nbsp;
			&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="发布" onclick="return publish();"/>
		</div>
	</form>
</body>
</html>