<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>行业应用管理</title>
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
		<li class="active"><a href="${ctx}/industry/industryApplication/">行业应用列表</a></li>
		<li><a href="${ctx}/industry/industryApplication/form">行业应用添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="industryApplication" action="${ctx}/industry/industryApplication/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="ul-form">
			<li><label>标题：</label>
				<form:input path="title" htmlEscape="false" class="input-medium"/>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			</li>
			<li class="clearfix"></li>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标题</th>
				<th>简介</th>
				<th>更新时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="industryApplication">
			<tr>
				<td><a href="${ctx}/industry/industryApplication/form?id=${industryApplication.id}">
					${industryApplication.title}
				</a></td>
				<td>${industryApplication.content}</td>
				<td>${industryApplication.creatTime}</td>
				<td>
    				<a href="${ctx}/industry/industryApplication/form?id=${industryApplication.id}">修改</a>
					<a href="${ctx}/industry/industryApplication/delete?id=${industryApplication.id}" onclick="return confirmx('确认要删除该行业应用吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>