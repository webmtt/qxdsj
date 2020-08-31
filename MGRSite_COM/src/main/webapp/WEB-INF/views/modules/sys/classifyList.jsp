<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分类维护列表</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<style type="text/css">.table td i{margin:0 2px;}</style>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 3});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/classify/">分类维护</a></li>
		<shiro:hasPermission name="sys:menu:edit"><li><a href="${ctx}/sys/classify/form">分类查看</a></li></shiro:hasPermission>
	</ul>
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
				<tr><th>名称</th><th>parentId</th><th style="text-align:center;">排序</th><th>可见</th><th>categoryID</th><shiro:hasPermission name="sys:menu:edit"><th>操作</th></shiro:hasPermission></tr>
			<c:forEach items="${list}" var="dataCategoryDef">
				<tr id="${dataCategoryDef.categoryID}" pId="${dataCategoryDef.parentID}">
				<td><i class="miusic"></i><a href="${ctx}/sys/classify/form?id=${dataCategoryDef.parentID}">${dataCategoryDef.chnName}</a></td>
					<td>${dataCategoryDef.parentID}</td>
					<td style="text-align:center;">
						<shiro:hasPermission name="sys:menu:edit">
							<input type="hidden" name="ids" value="${dataCategoryDef.parentID}"/>
							<input name="sorts" type="text" value="${dataCategoryDef.orderNo}" style="width:50px;margin:0;padding:0;text-align:center;">
						</shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit">
							${dataCategoryDef.orderNo}
						</shiro:lacksPermission>
					</td>
					<td>${dataCategoryDef.invalid eq '0'?'显示':'隐藏'}</td>
					<td>${dataCategoryDef.categoryID}</td>
					<shiro:hasPermission name="sys:menu:edit"><td>
						<a href="${ctx}/sys/classify/form?id=${dataCategoryDef.categoryID}">修改</a>
						<a href="${ctx}/sys/classify/delete?id=${dataCategoryDef.categoryID}" onclick="return confirmx('要删除该菜单及所有子菜单项吗？', this.href)">删除</a>
						<a href="${ctx}/sys/classify/form?parentId=${dataCategoryDef.parentID}">添加资料</a> 
					</td></shiro:hasPermission>
				</tr>
			</c:forEach>
		</table>
	 </form>
</body>
</html>
