<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>数据服务列表</title>
<meta name="decorator" content="default"/>
<script src="${ctxStatic}/jquery-ui/jquery-ui.js" type="text/javascript"></script>
<link href="${ctxStatic}/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">	
$(document).ready(function() {
	$( "#treeTable tbody" ).sortable({
	      connectWith: ".connectedSortable"
	    }).disableSelection();
});
function updateSort() {
	loading('正在提交，请稍等...');
	$("#listForm").attr("action", "${ctx}/dataService/updateSort");
	$("#listForm").submit();
}
</script>
</head>
<body>
    <ul class="nav nav-tabs">
		<li><a href="${ctx}/dataService/list">资料种类列表</a></li>
		<li class="active"><a href="javascript:void(0)">排序</a></li>
	</ul>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>中文名</th><th style="text-align:center;">排序</th></tr>
			<c:forEach items="${list}" var="dataCategory">
				<tr id="${dataCategory.categoryid}">
					<td style="width: 20%;">
					<c:if test="${!empty dataCategory.iconurl}">
					<img src="${imgServiceUrl}/${dataCategory.iconurl}">
					</c:if>
					</img>${dataCategory.chnname}</td>
					<td style="text-align:center;width:40%;">${dataCategory.orderno}
					 <input type="hidden" name="ids" value="${dataCategory.categoryid}"/>
					</td>
				</tr>
			</c:forEach>
		</table>
		<div class="form-actions pagination-left">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
		</div>
	 </form>
</body>
</html>