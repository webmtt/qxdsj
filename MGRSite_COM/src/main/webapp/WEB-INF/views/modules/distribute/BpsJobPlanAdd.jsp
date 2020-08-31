<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新增物品</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<style type="text/css">
#checkName{color:red}
</style>
<script type="text/javascript">
var status;
$(function(){
	$("#jobName").blur(function(){
		var str = $("input[name='jobName']").val();
		delAttach();
		if (str=="") {
			$("##checkName").html("作业名称不能为空！");
			return false;  
		}else{
			if(status==1){
				$("#checkName").html("作业名称已经存在！");
				return false;  
			}else{
				$("#checkName").empty();
				return true;  
			}
		}
	});
});
function check(){
	var jobName=$("#jobName").val();
	var triGgerPolicy=$("#triGgerPolicy").val();
	 var reg = new RegExp("^[0-9]*$");  
	if(""==jobName||null==jobName){
		alert("作业名称不能为空");
		return false;
	}else if(""==triGgerPolicy||null==triGgerPolicy){
		alert("作业时间配置不能为空");
		return false;
	}else{
		if(status==1){
			alert("作业名称已经存在！");
			return false;
		}else{
			$("#checkName").empty();
			return true;
		}
	}
}

function delAttach(){
	var jobName=$("#jobName").val();
	$.ajax({
		url:'${ctx}/dt/bpsJobPlan/checkJobName',
		type:'GET',
		data:{jobName:jobName},
		async: false,
		dataType:'json',
		success:function(result){
			status=result.status;
		},
	});
}

</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/dt/bpsJobPlan/jlist">分发定义列表</a></li>
		 <li class="active"><a href="${ctx}/dt/bpsJobPlan/add">新增分发类型</a></li> 
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="bpsJobPlan" action="${ctx}/dt/bpsJobPlan/SaveBpsJobPlan" method="post" class="form-horizontal" onsubmit="return check()">
		<div class="control-group">
			<label class="control-label">作业名称:</label>
			<div class="controls">
				<form:input path="jobName" htmlEscape="false" maxlength="50"
						class="required" /><span id="checkName"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">作业描述:</label>
			<div class="controls">
					<form:textarea path="description" htmlEscape="false"  rows="4"
						maxlength="400" class="input-xxlarge" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">作业参数:</label>
			<div class="controls">
				<form:textarea path="jobParameter" htmlEscape="false" maxlength="400" rows="4"
						 class="input-xxlarge" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">作业时间配置:</label>
			<div class="controls">
				<form:input path="triGgerPolicy" htmlEscape="false" maxlength="100"
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