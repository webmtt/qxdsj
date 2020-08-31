<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/feed/list">用户建议列表</a></li>
		<li class="active"><a href="${ctx}/feed/form?id=${userFeedBack.id}">回复建议</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="userFeedBack" action="${ctx}/feed/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">标题:</label>
			<div class="controls">
			    <label class="lbl">${userFeedBack.fDTitle}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">建议提出时间:</label>
			<div class="controls">
			    <label class="lbl">${userFeedBack.fDTime}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">建议内容:</label>
			<div class="controls">
			    <label class="lbl">${userFeedBack.fDContext}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">目标单位:</label>
			<div class="controls">
			    <label class="lbl">${fns:getDictLabel(userFeedBack.unitId, 'unitId','')}</label>
			</div>
		</div>
		<hr style=" height:2px;border:none;border-top:2px dotted #185598;">
		<div class="control-group">
			<label class="control-label">建议回复:</label>
			<div class="controls">
			    <textarea name="respContext" id="respContext" htmlEscape="false" rows="3" maxlength="500" class="input-xlarge"></textarea>
                <tags:ckeditor replace="respContext" height="250" ></tags:ckeditor>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>