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
	var ftpIp=$("#ftpIp").val();
	var ftpPort=$("#ftpPort").val();
	var ftpUser=$("#ftpUser").val();
	var ftpPwd=$("#ftpPwd").val();
	if(""==ftpIp||null==ftpIp){
		alert("ftp地址Ip不能为空");
		return false;
	}else if(""==ftpPort||null==ftpPort){
		alert("ftp地址端口不能为空");
		return false;
	}else if(""==ftpUser||null==ftpUser){
		alert("ftp用户名不能为空");
		return false;
	}else if(""==ftpPwd||null==ftpPwd){
		alert("ftp密码不能为空");
		return false;
	}else{
		return true;
		
	}
}

</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/dt/ddsFtpInfo/flist">ftp地址列表</a></li>
		 <li class="active"><a href="${ctx}/dt/ddsFtpInfo/edit?ftpId=${ftpId}">编辑ftp地址</a></li> 
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="ddsFtpInfo" action="${ctx}/dt/ddsFtpInfo/SaveDdsFtpInfo" method="post" class="form-horizontal" onsubmit="return check()">
		<div class="control-group">
		<label class="control-label">ftp地址ID:</label>
			<div class="controls">
						<input id="ftpId" name="ftpId" type="text" htmlEscape="false" maxlength="50"
						class="required" readonly="readonly" value="${ddsFtpInfo.ftpId}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">ftp地址Ip:</label>
			<div class="controls">
					<form:input path="ftpIp" htmlEscape="false" 
						maxlength="50" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">ftp地址端口:</label>
			<div class="controls">
				<form:input path="ftpPort" htmlEscape="false" maxlength="50"
						class="required" />
						<br>
				<span class="detailtext">
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">ftp用户名:</label>
			<div class="controls">
				<form:input path="ftpUser" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">ftp密码:</label>
			<div class="controls">
				<form:input path="ftpPwd" htmlEscape="false" maxlength="50"
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