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
	
});

function check(){
  	/* alert("1"); */
	var searchCond=$("#searchCond").val();
	 var reg = new RegExp("^[0-9]*$");  
	if(""==ftpIp||null==ftpIp){
		alert("检索条件不能为空");
		return false;
	}else if(!reg.test(orderNo)){
		alert("请在序号栏输入0~65534数字");
		return false;
		
	}else{
		return true;
		
	}
}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/dt/ddsSearchCondInfo/slist?dataId=${dataId}">检索条件列表</a></li>
		<li  class="active"><a href="${ctx}/dt/ddsSearchCondInfo/edit?dataId=${dataId}&id=${id}">编辑检索条件</a></li> 
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="ddsSearchCondInfo" action="${ctx}/dt/ddsSearchCondInfo/SaveDdsSearchCondInfo" method="post" class="form-horizontal" onsubmit="return check()">
		<form:input path="id" type="hidden"/>
		<input id="dataId" name="dataId" type="hidden" value="${dataId}"/>
		<div class="control-group">
			<label class="control-label">检索条件:</label>
			<div class="controls">
					<form:textarea path="searchCond" htmlEscape="false" maxlength="1000" rows="10"
						 class="input-xxlarge" />
			</div>
		
		</div>
		<div class="control-group">
			<label class="control-label">序号:</label>
			<div class="controls">
				<form:input path="orderNo" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
  </form:form>
</body>
</html>