<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<style type="text/css">.table td i{margin:0 2px;}</style>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 3});
		});
    	function updateSort() {
			loading('正在提交，请稍等...');
	    	$("#listForm").attr("action", "${ctx}/configure/funItmes/updateSort");
	    	$("#listForm").submit();
    	}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/configure/funItmes/list?areaItem=${areaItem}">菜单列表</a></li>
	</ul>
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
	<input type="hidden" name="areaItems" value="${areaItem}"/>
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>名称</th><th>链接</th><th style="text-align:center;">排序</th><th>可见</th><th>操作</th></tr>
			<c:forEach items="${list}" var="funItmes">
				<tr id="${funItmes.id}" pId="${funItmes.parent.id ne '0' ? funItmes.parent.id : '0'}">
					<td><i class="icon-hide"></i><a href="${ctx}/configure/funItmes/form?id=${funItmes.id}">${funItmes.name}</a></td>
					<td>${funItmes.link}</td>
					<td style="text-align:center;">
							<input type="hidden" name="ids" value="${funItmes.id}"/>
							<input name="sorts" type="text" value="${funItmes.orderno}" style="width:50px;margin:0;padding:0;text-align:center;">
					</td>
					<td>${funItmes.showType eq '1'?'显示':'隐藏'}</td>
					<td>
						<a href="${ctx}/configure/funItmes/form?id=${funItmes.id}&areaItem=${areaItem}">修改</a>
						<a href="${ctx}/configure/funItmes/delete?id=${funItmes.id}&areaItem=${areaItem}" onclick="return confirmx('要删除该菜单及所有子菜单项吗？', this.href)">删除</a>
						<c:if test="${funItmes.layer<=2}">
						<a href="${ctx}/configure/funItmes/form?parent.id=${funItmes.id}&areaItem=${areaItem}">添加下级菜单</a> 
					    </c:if>
					</td>
				</tr>
			</c:forEach>
		</table>
	 </form>
</body>
</html>
