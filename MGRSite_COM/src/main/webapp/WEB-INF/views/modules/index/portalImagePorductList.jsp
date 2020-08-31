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
	$("#form1").attr("action", "${ctx}/portalImage/updateSort");
	$("#form1").submit();
}
</script>
</head>

<body>

	<ul class="nav nav-tabs">
	    <li ><a href="${ctx}/portalImage/getTypeList">重要产品类型列表</a></li>
		<li class="active"><a href="javascript:void(0)">图片产品数据列表</a></li>
	</ul>
	<tags:message content="${message}"/>
	<form id="form1"  method="post">
	    <input type="hidden" name="type" value="${types}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead><tr><th>标题</th><th>提供单位</th><th>链接地址</th><th>排序</th><th>操作</th></tr></thead>
			<tbody>
			<c:forEach items="${list}" var="portalImageProductDef">
				<tr>
					<td>${portalImageProductDef[1]}</td>
					<td>${portalImageProductDef[3]}</td>
					<td>${portalImageProductDef[4]}</td>
					<td>
						<input type="hidden" name="ids" value="${portalImageProductDef[0]}"/>
						<input name="sorts" type="text" value="${portalImageProductDef[2]}" style="width:50px;margin:0;padding:0;text-align:center;">
					</td>
					<td>
	    				<a href="${ctx}/portalImage/editPortalImage?id=${portalImageProductDef[5]}&type=${types}">修改</a>
	    				<a href="${ctx}/portalImage/viewPortalImage?id=${portalImageProductDef[5]}&type=${types}">查看</a>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div align="left">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
		</div>
	</form>
</body>
</html>