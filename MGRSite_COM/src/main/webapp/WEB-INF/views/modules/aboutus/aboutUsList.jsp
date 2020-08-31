<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>关于我们管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/aboutus/aboutUs/">关于我们列表</a></li>
		<li><a href="${ctx}/aboutus/aboutUs/form">关于我们添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="aboutUs" action="${ctx}/aboutus/aboutUs/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>平台简介</th>
				<th>电话</th>
				<th>邮编</th>
				<th>邮箱</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="aboutUs">
			<tr>
				<td><a href="${ctx}/aboutus/aboutUs/form?id=${aboutUs.id}">
					<label title="${aboutUs.platformIntroduction}">平台简介</label>
				</a></td>
				<td>
					${aboutUs.telephone}
				</td>
				<td>
					${aboutUs.postcode}
				</td>
				<td>
					${aboutUs.email}
				</td>
				<td>
    				<a href="${ctx}/aboutus/aboutUs/form?id=${aboutUs.id}">修改</a>
					<a href="${ctx}/aboutus/aboutUs/delete?id=${aboutUs.id}" onclick="return confirmx('确认要删除该关于我们吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>