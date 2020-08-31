<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script type="text/javascript">
	$(document).ready(function() {
		//alert("${comparas.keyid}");
		
		$("#save").click(function(){
			var str1 = $("input[name='keyid']").val();
			var str2 = $("input[name='description']").val();
			var str3 = $("input[name='stringvalue']").val();
		if(str1!=null&&str1!=""&&str2!=null&&str2!=""&&str3!=null&&str3!=""){
			$("#inputForm").attr("action","${ctx}/sys/comparas/save");
			$("#inputForm").submit();	
		}else if (str1=="") {
			alert("请填写键值");
			return false;
		}else if (str2=="") {
			alert("请填写描述");
			return false;
		}else if (str3=="") {
			alert("请填写路径");
		}
		

		});
		
	
	});
 	function  check() {
		var keyid=$("#keyid").val();
		$.ajax({
			url: '${ctx}/sys/comparas/checkKeyId',
			type: 'post',
			data: {'keyid': keyid},
			asyn:true,
			success: function (result) {
				if (result != "1") {
					$("#keyid").val("");
					alert("键值"+keyid+"已存在");
				}
			}
		});
	}
	
	
 

	
	</script>
  </head>
  
  <body>
  	<ul class="nav nav-tabs">
 		<li><a href="${ctx}/sys/comparas/list">参数列表</a></li>
		<li class="active"><a href="${ctx}/sys/comparas/add">参数添加</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="comparas" method="post" class="form-horizontal">

		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">键值</label>
			<div class="controls">
				<form:input path="keyid" htmlEscape="false" maxlength="100" class="organization" onchange="check()"/></div>
		    </div>
		<div class="control-group">
			<label class="control-label">名称</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="userName"/></div>
		    </div>
		<div class="control-group">
			<label class="control-label">类型</label>
			<div class="controls">
				<form:input path="type" htmlEscape="false" maxlength="100" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">描述</label>
			<div class="controls">
				<form:input path="description" htmlEscape="false" maxlength="100" class="email"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">布尔参数:</label>
			<div class="controls">
				<form:input path="booleanvalue" htmlEscape="false" maxlength="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">整型参数:</label>
			<div class="controls">
				<form:input path="intvalue" htmlEscape="false" maxlength="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">路径:</label>
			<div class="controls">
				<form:input path="stringvalue" htmlEscape="false" maxlength="2000"/>
			</div>
		</div>
		
		<div class="form-actions">
			<input id="save" class="btn btn-primary" type="button" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>

	
  </body>
</html>
