<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新增物品</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript">
var serverUrl='${imgUrl}';
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
		<li ><a href="${ctx}/portalImage/getProductList?type=${type}">数据列表</a></li>
		 <li class="active"><a href="${ctx}/portalImage/editportalImage?type=${type}">编辑数据</a></li> 
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="portalImageProductDef" action="${ctx}/portalImage/savePortalImage" method="post" class="form-horizontal" >
  		<form:hidden path="id" />
  		<form:hidden path="IsStatic" />
  		<form:hidden path="productCode" />
  		<form:hidden path="dataCode" />
  		<form:hidden path="imageUrl" />
  		<input type="hidden" name="type" value="${type}"/>
		<div class="control-group">
			<label class="control-label">标题:</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">提供单位:</label>
			<div class="controls">
				<form:input path="dataSourse" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">链接地址:</label>
			<div class="controls">
				<form:input path="linkUrl" htmlEscape="false" maxlength="100"
						/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">展开类型:</label>
			<div class="controls">
				<form:radiobuttons path="showType" items="${fns:getDictList('showType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
  </form:form>
</body>
</html>