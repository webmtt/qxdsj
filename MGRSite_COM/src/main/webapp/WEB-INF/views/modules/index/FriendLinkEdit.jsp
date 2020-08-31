<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新增物品</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	
});
function check() {
	var name=$("#chnname").val();
	var linktype=$('input:radio:checked').val();
	var linkurl=$("#linkurl").val();
	if(name==null||linktype==null||linkurl==null){
		alert("必填项不能为空！");
		return false;
	}
	return true;
}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/friendLink/list">相关链接列表</a></li>
		 <li class="active"><a href="${ctx}/friendLink/add">编辑相关链接</a></li> 
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="friendLink" action="${ctx}/friendLink/save" method="post" class="form-horizontal" onsubmit="return check()">
  		<form:hidden path="linkid" />
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<form:input path="chnname" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">相关链接类型:</label>
			<div class="controls">
				<form:radiobuttons path="linktype" items="${fns:getDictList('friendLink')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">链接地址:</label>
			<div class="controls">
				<form:input path="linkurl" htmlEscape="false" maxlength="50"
						class="required"/ >
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">序号:</label>
			<div class="controls">
				<form:input path="orderno" htmlEscape="false" maxlength="50"
						class="required" name="orderno" />
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
  </form:form>
</body>
</html>