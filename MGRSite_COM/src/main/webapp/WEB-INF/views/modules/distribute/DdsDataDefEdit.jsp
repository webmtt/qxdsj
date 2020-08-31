<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新增物品</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<style type="text/css">
.detailtext{color:red;}
</style>
<script type="text/javascript">
$(function(){
	$("#attach").hide();
});
function check(){
  	/* alert("1"); */
	var nameFormat=$("#nameFormat").val();
	var SearchType=$("#SearchType").val();
	//var reg = new RegExp("^[0-9]*$"); 
	var  hostName = $("#hostName").val();
	var reg = new RegExp("^[1-9]([0-9]){0,}$");
	//var reg = new RegExp("^[0-9]*$");  
	var  timeSpan = $("#timeSpan").val();
	var  spanUnit = $("#spanUnit").val();
	var  delay = $("#delay").val();
	var  delayMinute = $("#delayMinute").val();
	var  timeRate = $("#timeRate").val();
	var  rateUnit = $("#rateUnit").val();
	if(""==nameFormat||null==nameFormat){
		alert("命名格式不能为空");
		return false;
	}else if(""==SearchType||null==SearchType){
		alert("检索类型不能为空");
		return false;
	}else{
		if(SearchType=="0"||SearchType=="1"||SearchType=="2"){
			//return true;
		}else{
			alert("检索类型不正确");
			return false;
		}
		
	}
	if(""==hostName||null==hostName){
		alert("主机名不能为空");
		return false;
	}
	
	if(""==timeSpan||null==timeSpan){
		alert("时间跨度不能为空");
		return false;
	}else{
		if(!reg.test(timeSpan)){
			alert("时间跨度请输入大于零的整数");
			return false;
		}
	}
	
	if(""==spanUnit||null==spanUnit){
		alert("时间跨度单位不能为空");
		return false;
	}
	if(""==delay||null==delay){
		alert("时间延迟不能为空");
		return false;
	}else{
		if(!reg.test(delay)){
			alert("时间延迟请输入大于零的整数");
			return false;	
		}
	}
	if(""!=delayMinute||null!=delayMinute){
		
	}else if(!reg.test(delayMinute)){
		alert("延迟分钟请输入大于零的整数");
		return false;
	}
	
	
	var  delayUnit = $("#delayUnit").val();
	if(""==delayUnit||null==delayUnit){
		alert("时间延迟单位不能为空");
		return false;
	}
	
	if(""==timeRate||null==timeRate){
		alert("执行频率时间不能为空");
		return false;
	}else{
		if(!reg.test(timeRate)){
			alert("执行频率时间请输入大于零的整数");
			return false;
		}
	}
	
	if(""==rateUnit||null==rateUnit){
		alert("执行频率时间单位不能为空");
		return false;
	}
}

</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/dt/distribute/list">分发定义列表</a></li>
		 <li class="active"><a href="${ctx}/dt/distribute/edit?dataId=${dataId}">编辑分发类型</a></li> 
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="ddsDataDef" action="${ctx}/dt/distribute/UpdateDdsDataDef" method="post" class="form-horizontal" onsubmit="return check()">
		<form:input path="SearchType" type="hidden" value="0" />
		<div class="control-group">
			<label class="control-label">数据ID:</label>
			<div class="controls">
						<input id="dataId" name="dataId" type="text" htmlEscape="false" maxlength="50"
						class="required" readonly="readonly" value="${ddsDataDef.dataId}"/>
						<input id="id" name="id" type="hidden" htmlEscape="false" maxlength="50"
						class="required" readonly="readonly" value="${ddsDataDef.id}"/>
						<input id="invalid" name="invalid" type="hidden" htmlEscape="false" maxlength="50"
						class="required" readonly="readonly" value="${ddsDataDef.invalid}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">接口类型:</label>
			<div class="controls">
			   <input type="radio" id="interfaceType1" name="interfaceType" value="MUSIC" <c:if test="${ddsDataDef.interfaceType eq 'MUSIC' }">checked="checked"</c:if> />		
				MUSIC接口
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">数据格式:</label>
			<div class="controls">
				<input type="radio" id="dataFormat1" name="dataFormat" value="txt" <c:if test="${ddsDataDef.dataFormat eq 'txt' }">checked="checked"</c:if> />		
				支持txt
				<input type="radio" id="dataFormat2" name="dataFormat" value="csv" <c:if test="${ddsDataDef.dataFormat eq 'csv' }">checked="checked"</c:if>/>
				支持csv
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">命名格式:</label>
			<div class="controls">
				<form:input path="nameFormat" htmlEscape="false" maxlength="50"
						class="required" />
						<br>
				<span class="detailtext"> &lt;beginTime&gt;表示开始时间</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">时间格式:</label>
			<div class="controls">
				<form:input path="timeFormat" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推送目标路径:</label>
			<div class="controls">
				<form:input path="pushTargetPath" htmlEscape="false" maxlength="50"
						class="required" />
						<br>
						<span class="detailtext">推送目标ftp的目标路径</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">检索文件路径:</label>
			<div class="controls">
				<form:input path="searchTargetPath" htmlEscape="false" maxlength="50"
						class="required" />
				 
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">主机名:</label>
			<div class="controls">
				<form:input path="hostName" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">时间跨度:</label>
			<div class="controls">
				<form:input path="timeSpan" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">时间跨度单位:</label>
			<div class="controls">
				<%-- <form:input path="spanUnit" htmlEscape="false" maxlength="50"class="required" /> --%>
				<form:select path="spanUnit" class="required" >
					<form:option value="">请选择</form:option>
					<form:option value="Second">秒</form:option>
					<form:option value="Minute">分</form:option>
					<form:option value="Hour">时</form:option>
					<form:option value="Day">天</form:option>
					<form:option value="Ten">旬</form:option>
					<form:option value="Month">月</form:option>
					<form:option value="Year">年</form:option>
				</form:select>
				
						
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">时间延迟:</label>
			<div class="controls">
				<form:input path="delay" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">时间延迟单位:</label>
			<div class="controls">
				<%-- <form:input path="delayUnit" htmlEscape="false" maxlength="50" class="required" /> --%>
				<form:select path="delayUnit" class="required" >
					<form:option value="">请选择</form:option>
					<form:option value="Second">秒</form:option>
					<form:option value="Minute">分</form:option>
					<form:option value="Hour">时</form:option>
					<form:option value="Day">天</form:option>
					<form:option value="Ten">旬</form:option>
					<form:option value="Month">月</form:option>
					<form:option value="Year">年</form:option>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">延迟分钟:</label>
			<div class="controls">
				<form:input path="delayMinute" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">执行频率:</label>
			<div class="controls">
				<form:input path="timeRate" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">执行频率单位:</label>
			<div class="controls">
				<%-- <form:input path="rateUnit" htmlEscape="false" maxlength="50" class="required" /> --%>
				<form:select path="rateUnit" class="required" >
					<form:option value="">请选择</form:option>
					<form:option value="Second">秒</form:option>
					<form:option value="Minute">分</form:option>
					<form:option value="Hour">时</form:option>
					<form:option value="Day">天</form:option>
					<form:option value="Ten">旬</form:option>
					<form:option value="Month">月</form:option>
					<form:option value="Year">年</form:option>
				</form:select>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
  </form:form>
</body>
</html>