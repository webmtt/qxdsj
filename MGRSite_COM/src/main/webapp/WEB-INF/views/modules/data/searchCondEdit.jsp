<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
<title>编辑数据类型</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script type="text/javascript">	
var ctx = "${ctx}";
var ctxStatic = "${ctxStatic}";
$(function(){

	/*
	$("#inputForm").validate({
		rules: {
			searchconfigcode: {remote: "${ctx}/searchCondCfg/checkCode?searchconfigcode=" +$("#searchconfigcode").val())}
		},
		messages: {
			searchconfigcode: {remote: "条件编码名称已经存在！"}
		}
	});
	*/
})
function check(){
	var searchconfigcode=$("#searchconfigcode").val();
	var chndescription=$("#chndescription").val();
	if(searchconfigcode==null||searchconfigcode==""){
		alert("检索条件编码不能为空！");
		return false;
	}else if(chndescription==null||chndescription==""){
		alert("中文描述不能为空！");
		return false;
	}
	$.ajax({
		url : "${ctx}/searchCondCfg/checkCode?searchconfigcode="+searchconfigcode,
		type : "post",
		dataType : "json",
		async : true,
		success : function(result) {
	      var status=result.status;
		  if(status==1){
			  alert("该检索条件编码已经存在！");
			  return false;
		  }
		}
	});
}	
</script>
</head>

<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/searchCondCfg/SearchCondItemslist?itemtype=${itemtype}&pid=${pid}">数据检索条件列表</a></li>
		<li class="active"><a href="">添加数据检索条件</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="searchCondCfg" action="${ctx}/searchCondCfg/saveSearchCondCfg" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
	<input name="pid" id="pid" value="${pid}" type="hidden"/>
	<form:input path="datasource" type="hidden"/>
	<form:input path="engdescription" type="hidden"/>
	<form:input path="captionfield" type="hidden"/>
	<form:input path="valuefield" type="hidden"/>
	<form:input path="isdataspec" type="hidden"/>
	<form:input path="rowitemcount" type="hidden"/>
	<form:input path="wherecond" type="hidden"/>
	<form:input path="orderbycond" type="hidden"/>
	<div class="control-group">
		<label class="control-label">检索条件编码：</label>
		<div class="controls">				
			<input type="text" id="searchconfigcode" name="searchconfigcode" value="${searchCondCfg.searchconfigcode }" htmlEscape="false" cssStyle="width:350px;"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">中文描述：</label>
		<div class="controls">				
			<input type="text" id="chndescription" name="chndescription"  value="${searchCondCfg.chndescription }" htmlEscape="false" cssStyle="width:350px;"/>
		</div>
	</div> 
	<div class="control-group"> 
		<div class="controls">		
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>
	        <input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>   
		</div>     
	</div>		   
			
</form:form>
</body>
</html>

