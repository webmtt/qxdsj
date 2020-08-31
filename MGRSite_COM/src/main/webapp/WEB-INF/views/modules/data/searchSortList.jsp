<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>条件分组排序</title>
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
	$("#listForm").attr("action", "${ctx}/dataSearchDef/searchSort");
	$("#listForm").submit();
}
function closeWin(){
	 parent.closeModal2();
}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0)">排序</a></li> 	
	</ul>
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
	     <input type="hidden" id="searchsetcode" name="searchsetcode" value="${searchsetcode}">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>中文名</th><th style="text-align:center;">排序</th></tr>
			<tbody>
				<c:forEach items="${list}" var="searchSetDef">
					<tr id="${eleSetElementDef.id}">
						<td style="width: 50%;">${searchSetDef.chnname}</td>
						<td style="width: 50%;">${searchSetDef.orderno}</td>
						 <input type="hidden" name="ids" value="${searchSetDef.id}"/>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="form-actions pagination-left">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  id="btnCancel" class="btn" type="button" value="关闭" onclick="closeWin();" />
		</div>
	 </form>
</body>
</html>