<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>行业应用管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/layui-v2.5.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
	<link href="${ctxStatic}/layui-v2.5.5/layui/css/layui.css" media="all" rel="stylesheet" type="text/css"/>
	<script src="${ctxStatic}/ckeditor/ckeditor.js" type="text/javascript"></script>

	<script type="text/javascript">
		$(document).ready(function() {
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

        layui.use('upload', function() {
            var $ = layui.jquery
                , upload = layui.upload;

            //普通图片上传
            var uploadInst = upload.render({
                elem: '#upload'
                , url: '${ctx}/industry/industryApplication/upload' //改成您自己的上传接口
                , done: function (res) {
                    layer.msg('上传成功');
                    var imgUrl = res.url;
                    console.log(imgUrl);
                    $('#imageurl').val(imgUrl);
                }
                , error: function (res) {
                    layer.msg('上传失败');
                }
            });
        });

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/industry/industryApplication/">行业应用列表</a></li>
		<li class="active"><a href="${ctx}/industry/industryApplication/form?id=${industryApplication.id}">行业应用${not empty industryApplication.id?'修改':'添加'}</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="industryApplication" action="${ctx}/industry/industryApplication/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">图片URL：</label>
			<div class="controls">
				<form:input id="imageurl" path="imageurl" htmlEscape="false" class="input-xlarge required" readonly="true"/>
				<button type="button" class="btn btn-primary" id="upload" style="">上传图片</button>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">标题：</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" class="input-xlarge required" maxlength="15" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">英文标题：</label>
			<div class="controls">
				<form:input path="entitle" htmlEscape="false" class="input-xlarge required"  maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">简介：</label>
			<div class="controls">
				<form:textarea path="content" htmlEscape="false" rows="4" maxlength="58"  class="input-xxlarge required"/>
			</div>
		</div>
		<div class="control-group" id="concheck">
			<label class="control-label">样例展示：</label>
			<div class="controls">
				<textarea name="example" id="example" class="input-xlarge" htmlEscape="true">
						${industryApplication.example}</textarea>
				<tags:ckeditor replace="example" height="150"></tags:ckeditor>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" />&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>

</body>
</html>