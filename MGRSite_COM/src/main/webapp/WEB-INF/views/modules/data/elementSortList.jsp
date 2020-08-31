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
	$("#listForm").attr("action", "${ctx}/eleSetEleGroup/elementSort");
	$("#listForm").submit();
}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/eleSetEleGroup/elementList?elesetcode=${elesetcode}&elegroupcode=${elegroupcode}">检索结果列表</a></li>
		<li class="active"><a href="javascript:void(0)">排序</a></li> 	
	</ul>
	<form id="listForm" method="post">
	     <input type="hidden" id="elesetcode" name="elesetcode" value="${elesetcode}">
	     <input type="hidden" id="elegroupcode" name="elegroupcode" value="${elegroupcode}">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>中文名</th><th style="text-align:center;">排序</th></tr>
			<tbody>
			<c:forEach items="${list}" var="eleSetElementDef">
				<tr id="${eleSetElementDef.id}">
					<td style="width: 50%;">${eleSetElementDef.elementname}</td>
					<td style="width: 50%;">${eleSetElementDef.orderno}</td>
					 <input type="hidden" name="ids" value="${eleSetElementDef.id}"/>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="form-actions pagination-left">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
		</div>
	 </form>
</body>
</html>