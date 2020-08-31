<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>列表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">	
$(document).ready(function() {
	$("#treeTable").treeTable({expandLevel : 3});
});
function updateSort() {
	loading('正在提交，请稍等...');
	$("#listForm").attr("action", "${ctx}/portalMenuDef/updateSort");
	$("#listForm").submit();
}
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/portalMenuDef/list">导航菜单栏目列表</a></li>
	</ul>
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>中文名</th><th>排序</th><th>链接地址</th><th>操作</th></tr>
			<c:forEach items="${pmds}" var="portalMenuDef">
				<tr id="${portalMenuDef.menuid}" pId="${portalMenuDef.parent.menuid}">
					<td style="width: 25%;">${portalMenuDef.chnname}</td>
					<td style="text-align:left;width:5%;">
					   <input type="hidden" name="ids" value="${portalMenuDef.menuid}"/>
					   <input name="sorts" type="text" value="${portalMenuDef.orderno}" style="width:50px;margin:0;padding:0;text-align:center;">
					</td>
					<td style="text-align:left;width:55%;">${portalMenuDef.linkurl}</td>
					<td style="text-align:left;">
						<a href="${ctx}/portalMenuDef/toEdit?menuid=${portalMenuDef.menuid}&pid=${portalMenuDef.parent.menuid}">修改</a> 
						<c:choose>
						<c:when test="${(portalMenuDef.menuid  eq '1')}">
						&nbsp;&nbsp;<a href="${ctx}/portalMenuDef/getDetail?pid=${portalMenuDef.parent.menuid}&menuid=${portalMenuDef.menuid}">查看详情</a>&nbsp;&nbsp;
						</c:when>
					 	<c:when test="${(portalMenuDef.parent.menuid  eq '0')}">
					 		&nbsp;&nbsp;<a href="${ctx}/portalMenuDef/toAdd?pid=${portalMenuDef.parent.menuid}&menuid=${portalMenuDef.menuid}">添加下级</a>&nbsp;&nbsp;
					 		<a onclick="return confirmx('要删除该类型及所有子类型吗？', this.href)" href="${ctx}/portalMenuDef/delete?pid=${portalMenuDef.parent.menuid}&menuid=${portalMenuDef.menuid}">删除</a>
					 	</c:when>
					 	<c:otherwise>
						&nbsp;&nbsp;<a href="${ctx}/portalMenuDef/getDetail?pid=${portalMenuDef.parent.menuid}&menuid=${portalMenuDef.menuid}">查看详情</a>&nbsp;&nbsp;
						<a onclick="return confirmx('要删除该类型及其所有资料吗？', this.href)" href="${ctx}/portalMenuDef/delete?pid=${portalMenuDef.parent.menuid}&menuid=${portalMenuDef.menuid}">删除</a>
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