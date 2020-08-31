<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>页面管理</title>
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
		<li class="active"><a href="${ctx}/product/pageInfoConfigure/">页面列表</a></li>
		<li><a href="${ctx}/product/pageInfoConfigure/form">页面添加</a></li>
	</ul>
	<form id="searchForm" modelAttribute="pageInfoConfigure" action="${ctx}/product/pageInfoConfigure/" method="post"
		  class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>页面名称：</label><input path="pageName" htmlEscape="false" maxlength="50" class="input-medium"/>
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>页面名称</th>
				<th>页面内容</th>
				<th>页面类型</th>
				<th>创建者</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="product:pageInfoConfigure:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="pageInfoConfigure">
			<tr>
				<td><a href="${ctx}/product/pageInfoConfigure/form?id=${pageInfoConfigure.id}">
					${pageInfoConfigure.pageName}
				</a></td>
				<td>
					${pageInfoConfigure.pageContent}
				</td>
				<td>
					${fns:getDictLabel(pageInfoConfigure.pageType, 'page_type', '')}
				</td>
				<td>
					${fns:getDictLabel(pageInfoConfigure.createBy.id, 'sys_user_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${pageInfoConfigure.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${pageInfoConfigure.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${pageInfoConfigure.remarks}
				</td>
				<td>
    				<a href="${ctx}/product/pageInfoConfigure/form?id=${pageInfoConfigure.id}">修改</a>
					<a href="${ctx}/product/pageInfoConfigure/delete?id=${pageInfoConfigure.id}" onclick="return confirmx('确认要删除该页面吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>