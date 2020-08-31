<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>数据服务列表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">	
$(document).ready(function() {
	$("#treeTable").treeTable({expandLevel : 3});
});
function updateSort() {
	loading('正在提交，请稍等...');
	$("#listForm").attr("action", "${ctx}/portalData/updateSort");
	$("#listForm").submit();
}
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0)">数据下载服务列表</a></li>
		<li><a href="${ctx}/portalData/addPortalData">新增数据</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>中文名</th><th style="text-align:center;">排序</th><th style="text-align:center;">链接地址</th><th style="text-align:center;">操作</th></tr>
			<c:forEach items="${PortalDataCategoryDefs}" var="portalDataCategoryDef">
				<tr id="${portalDataCategoryDef.id}" pId="${portalDataCategoryDef.parent.id}">
					<td style="width: 20%;">
					</img>${portalDataCategoryDef.chnName}</td>
					<td style="text-align:center;width:20%;">
					   <input type="hidden" name="ids" value="${portalDataCategoryDef.id}"/>
					   <input name="sorts" type="text" value="${portalDataCategoryDef.orderNo}" style="width:50px;margin:0;padding:0;text-align:center;">
					</td>
					<td style="text-align:center;width:20%;">${portalDataCategoryDef.linkurl}</td>
					<td style="text-align:center;">
					<a href="${ctx}/portalData/editPortalData?id=${portalDataCategoryDef.id}&pid=${portalDataCategoryDef.parent.id}">修改</a> 
				 	<c:choose>
				 	<c:when test="${portalDataCategoryDef.parent.id eq '0'  }">
				 		&nbsp;&nbsp;<a href="${ctx}/portalData/addPortalData?pid=${portalDataCategoryDef.id}">添加子类型</a>&nbsp;&nbsp;
				 	</c:when>
				 	<c:otherwise>
					&nbsp;<a href="${ctx}/portalData/getDataDetail?id=${portalDataCategoryDef.id}&pid=${portalDataCategoryDef.parent.id}">查看</a>&nbsp;
					<a onclick="return confirmx('要删除该类型吗？', this.href)" href="${ctx}/portalData/deletePortalData?pid=${portalDataCategoryDef.parent.id}&id=${portalDataCategoryDef.id}">删除</a>
				 	</c:otherwise>
				 	</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
		<div class="form-actions pagination-left">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存排序" onclick="return updateSort();"/>
		</div>
	 </form>
</body>
</html>