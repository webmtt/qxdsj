<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>栏目排序</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
	table td{word-wrap: break-word;}
	table{width: 600px !important;}
</style>
<script type="text/javascript">
	
	function updateSort(){
		var count = "${count}";
		if(count != "0"){
			loading('正在提交，请稍等...');
			$("#listForm").attr("action", "${ctx}/funcItmesDef/sortOrder");
			$("#listForm").submit();
		}else{
			alert("没有需要排序的栏目！");
			return false;
		}
	}
	
</script>
</head>

<body>
	
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/funcItmesDef/list">栏目维护列表</a></li>
		<li class="active"><a href="${ctx}/funcItmesDef/toSortOrder?funcitemid=${funcitemid}">栏目排序</a></li>  
	</ul>
	
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="tablelist" class="table table-striped table-bordered table-condensed" style="table-layout:fixed;">
			<tr>
				<th style="width: 80%; text-align: center;">中文名称</th>
				<th style="width: 20%; text-align: center;">排序号</th>
				
			</tr>
			<c:forEach items="${list}" var="funcItmesDef">
				<tr id="${funcItmesDef.funcItemID}" pid="${funcItmesDef.parentID}">
					<td>${funcItmesDef.CHNName}</td>
					<td style="text-align: center;">
						<input type="hidden" name="ids" value="${funcItmesDef.funcItemID}"/>
						<input name="sorts" type="text" value="${funcItmesDef.orderNo}" style="width:50px;margin:0;padding:0;text-align:center;">
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<div class="control-group"> 
	    	<div class="controls">		
				<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
				<input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
			</div>
		</div>
		
		<script type="text/javascript">
			if($("#messageBox").length>0){
				$("#messageBox").show();
			}
		</script>
		
	</form>

</body>

</html>