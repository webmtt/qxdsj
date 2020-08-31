<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
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
		<li><a href="${ctx}/configure/funItmes/list?areaItem=${areaItem}">菜单列表</a></li>
		<li class="active"><a href="${ctx}/configure/funItmes/form?id=${funItmes.id}&parent.id=${funItmes.parent.id}">菜单${not empty funItmes.id?'修改':'添加'}查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="funItmes" action="${ctx}/configure/funItmes/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="areaItem"/>
		<input type="hidden" name="areaItems" value="${areaItem}"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">上级菜单:</label>
			<div class="controls">
                <tags:treeselect id="funItmes" name="parent.id" value="${funItmes.parent.id}" labelName="parent.name" labelValue="${funItmes.parent.name}"
					title="菜单" url="/configure/funItmes/treeData?areaItem=${areaItem}" extId="${funItmes.id}"  cssClass="required">
					
					</tags:treeselect>
					
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">链接:</label>
			<div class="controls">
				<form:input path="link" htmlEscape="false" maxlength="200" class=""/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序:</label>
			<div class="controls">
				<form:input path="orderno" htmlEscape="false" maxlength="50" class="required digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">可见:</label>
			<div class="controls">
				<form:radiobuttons path="showType" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>