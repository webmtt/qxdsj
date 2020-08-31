<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新增物品</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#attach").hide();
});
function check(){
  	/* alert("1"); */
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
		return true;
	}
}

function delAttach(area,columnImageURL,columnItemID){
	$.ajax({
		url:'${ctx}/column/columnarrange/delById',
		type:'GET',
		data:{ID:area,docUrl:columnImageURL,docId:columnItemID},
		dataType:'json',
		success:function(result){
			if(result.flag=='true'){
				$("#"+result.ID).hide();
				alert("删除附件成功");
				$("#attach").hide();
				$("#upload").show();
			}else if(result.flag=='false'){
     			alert("删除附件失败");
			}
				
		},
	});
}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/dt/bpsJobPlan/jlist">作业计划列表</a></li>
		 <li class="active"><a href="${ctx}/dt/bpsJobPlan/edit?jobName=${jobName}">编辑作业计划</a></li> 
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="bpsJobPlan" action="${ctx}/dt/bpsJobPlan/SaveBpsJobPlan" method="post" class="form-horizontal" onsubmit="return check()">
		<div class="control-group">
			<label class="control-label">作业名称:</label>
			<div class="controls">
				<input id="jobName" name="jobName" type="text" htmlEscape="false" maxlength="50"
						class="required" readonly="readonly" value="${bpsJobPlan.jobName}"/>
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