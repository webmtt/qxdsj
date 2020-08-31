<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>气象业务列表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">	
$(document).ready(function() {
	$("#treeTable").treeTable({expandLevel : 3});
});
function updateSort() {
	loading('正在提交，请稍等...');
	$("#listForm").attr("action", "${ctx}/portalitemCategoryDef/updateSort");
	$("#listForm").submit();
}
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/portalitemCategoryDef/list">气象业务栏目列表</a></li>
	</ul>
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>中文名</th><th style="text-align:center;">排序</th><th style="text-align:center;">链接地址</th><th style="text-align:center;">操作</th></tr>
			<c:forEach items="${pcds}" var="portalitemCategoryDef">
				<tr id="${portalitemCategoryDef.funcitemid}" pId="${portalitemCategoryDef.parent.funcitemid}">
					<td style="width: 25%;">${portalitemCategoryDef.chnname}</td>
					<td style="text-align:left;width:5%;">
					   <input type="hidden" name="ids" value="${portalitemCategoryDef.funcitemid}"/>
					   <input name="sorts" type="text" value="${portalitemCategoryDef.orderno}" style="width:50px;margin:0;padding:0;text-align:center;">
					</td>
					<td style="text-align:left;width:55%;">${portalitemCategoryDef.linkurl}</td>
					<td style="text-align:left;">
						<a href="${ctx}/portalitemCategoryDef/toEdit?funcitemid=${portalitemCategoryDef.funcitemid}&pid=${portalitemCategoryDef.parent.funcitemid}">修改</a> 
						<c:choose>
					 	<c:when test="${(portalitemCategoryDef.layer  eq '1')||(portalitemCategoryDef.layer  eq '2')}">
					 		&nbsp;&nbsp;<a href="${ctx}/portalitemCategoryDef/toAdd?pid=${portalitemCategoryDef.parent.funcitemid}&funcitemid=${portalitemCategoryDef.funcitemid}">添加下级</a>&nbsp;&nbsp;
					 		<a onclick="return confirmx('要删除该类型及所有子类型吗？', this.href)" href="${ctx}/portalitemCategoryDef/delete?pid=${portalitemCategoryDef.parent.funcitemid}&funcitemid=${portalitemCategoryDef.funcitemid}">删除</a>
					 	</c:when>
					 	<c:otherwise>
						&nbsp;&nbsp;<a href="${ctx}/portalitemCategoryDef/getDetail?pid=${portalitemCategoryDef.parent.funcitemid}&funcitemid=${portalitemCategoryDef.funcitemid}">查看详情</a>&nbsp;&nbsp;
						<a onclick="return confirmx('要删除该类型及其所有资料吗？', this.href)" href="${ctx}/portalitemCategoryDef/delete?pid=${portalitemCategoryDef.parent.funcitemid}&funcitemid=${portalitemCategoryDef.funcitemid}">删除</a>
					 	</c:otherwise>
					 	</c:choose>
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