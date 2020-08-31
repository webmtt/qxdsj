<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>树结构管理</title>
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
					if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")){
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
		<li><a href="${ctx}/interf/sysInterface/">API列表</a></li>
		<li class="active"><a href="${ctx}/interf/sysInterface/form?id=${sysInterface.id}&parent.id=${sysInterfaceparent.id}">API${not empty sysInterface.id?'修改':'添加'}</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="sysInterface" action="${ctx}/interf/sysInterface/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">上级名称:</label>
			<div class="controls">
				<tags:treeselect id="parent" name="parent.id" value="${sysInterface.parent.id}" labelName="parent.name" labelValue="${sysInterface.parent.name}"
					title="上级名称" url="/interf/sysInterface/treeData" extId="${sysInterface.id}" cssClass="" allowClear="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">资料类型：</label>
			<div class="controls">
				<form:input path="dataType" htmlEscape="false" class="input-xlarge required"/>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">资料编码：</label>
			<div class="controls">
				<form:input path="dataEncoding" htmlEscape="false" class="input-xlarge required"/>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			</div>
		</div>
		<c:if test="${sysInterface.sort!=1}">
			<div class="control-group">
				<label class="control-label">接口ID：</label>
				<div class="controls">
					<form:input path="inerfaceId" htmlEscape="false" class="input-xlarge "/>
				</div>
			</div>
		</c:if>
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" class="input-xlarge required"/>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			</div>
		</div>
		<c:if test="${sysInterface.sort!=2 and sysInterface.sort!=1}">
		<div class="control-group">
			<label class="control-label">要素编码：</label>
			<div class="controls">
				<form:input path="parameterId" htmlEscape="false" class="input-xlarge required"/>
					<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">要素类型：</label>
			<div class="controls">
				<form:input path="parameterType" htmlEscape="false" class="input-xlarge required"/>
					<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			</div>
		</div>
		</c:if>
		<%--<div class="control-group">
			<label class="control-label">是否显示：</label>
			<div class="controls">
				<form:input path="isShow" htmlEscape="false" maxlength="1" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">权限标识：</label>
			<div class="controls">
				<form:input path="permission" htmlEscape="false" maxlength="200" class="input-xlarge "/>
			</div>
		</div>--%>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>