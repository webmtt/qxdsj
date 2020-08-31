<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>关于我们管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
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
		function checktel()
		{
			var flag=true;
			var strTel = $("#telephone").val();
			var pattern =/^0\d{2,3}-?\d{7,8}$/;
			if(strTel!="")
			{
				if(!pattern.exec(strTel))
				{
					flag=false;
					alert('请输入正确的电话号码');
					object.value="";
					object.focus();
				}
			}
			return flag;
		}
		function checkecode()
		{
			var flag=true;
			var strCode = $("#postcode").val();
			var pattern =/^[0-9]{6}$/;
			if(strCode!="")
			{
				if(!pattern.exec(strCode))
				{
					flag=false;
					alert('请输入正确的邮编地址');
					object.value="";
					object.focus();
				}
			}
			return flag;
		}
		function checkEmail()
		{
			var flag=true;
			var strEmail = $("#email").val();
			var pattern =/^\w+((-\w+) (\.\w+))*\@[A-Za-z0-9]+((\. -)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
			if(strEmail!="")
			{
				if(!pattern.exec(strEmail))
				{
					flag=false;
					alert('请输入正确的邮箱地址');
					object.value="";
					object.focus();
				}
			}
			return flag;
		}
		function check(){
			var flag=true;
			flag=checktel();
			if(!flag){
				return flag;
			}
			flag=checkecode();
			if(!flag){
				return flag;
			}
			flag=checkEmail();
			if(!flag){
				return flag;
			}
			var f = document.getElementsByTagName("form")[0];
			f.submit();
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/aboutus/aboutUs/">关于我们列表</a></li>
		<li class="active"><a href="${ctx}/aboutus/aboutUs/form?id=${aboutUs.id}">关于我们${not empty aboutUs.id?'修改':'添加'}</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="aboutUs" action="${ctx}/aboutus/aboutUs/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">平台简介：</label>
			<div class="controls">
				<form:textarea path="platformIntroduction" htmlEscape="false" class="input-xlarge required "/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电话：</label>
			<div class="controls">
				<form:input path="telephone" name="telephone" id="telephone" htmlEscape="false" maxlength="255" class="input-xlarge required " onblur="checktel()"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮编：</label>
			<div class="controls">
				<form:input path="postcode"  name="postcode" id="postcode" htmlEscape="false" maxlength="255" class="input-xlarge required "  onblur="checkecode()"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱：</label>
			<div class="controls">
				<form:input path="email" id="email" htmlEscape="false" maxlength="255" class="input-xlarge required " onblur="checkEmail()"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="check()"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>