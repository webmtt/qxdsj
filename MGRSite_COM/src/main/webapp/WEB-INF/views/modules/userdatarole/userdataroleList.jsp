<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户角色列表</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<style type="text/css">
		.table td i{
			margin:0 2px;
		}
	</style>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/userAndDataRole/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<div>
		<ul class="nav nav-tabs">
			<li class="active"><a href="javascript:void(0)">角色用户列表</a></li>
			<li><a href="${ctx}/userAndDataRole/addUserAndDataRole">新增角色用户</a></li>
		</ul>		<form id="searchForm" modelAttribute="user" action="${ctx}/userAndDataRole/list" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
			<div>
				<label>角色：</label><input name="roleCodeOrName" htmlEscape="false" maxlength="50" class="input-small" value="${roleCodeOrName}" placeholder="编码/名称"/>
				<label>用户：</label><input name="userCodeOrName" htmlEscape="false" maxlength="50" class="input-small" value="${userCodeOrName}" placeholder="账号/名称"/>
				&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			</div>
		</form>
	</div>
  	<div style="margin-top: 10px;"> 	
	    <table id="tablelist"
			class="table table-striped table-bordered table-condensed"
			style="word-break:break-all; word-wrap:break-word;">
			<thead>
				<th style="text-align: center;">角色编码</th>
				<th style="text-align: center;">角色名称</th>
				<th style="text-align: center;">用户帐号</th>
				<th style="text-align: center;">用户名称</th>
				<th style="text-align: center;">操作</th>
			</thead>
			<tbody>
				<c:forEach items="${page.list}" var="eudr">
					<tr>
						<td style="width: 20%; text-align: center;">${eudr.dataroleId}</td>
						<td style="width: 20%; text-align: center;">${eudr.dataroleName}</td>
						<td style="width: 20%; text-align: center;">${eudr.userName}</td>
						<td style="width: 20%; text-align: center;">${eudr.chName}</td>
						<td style="width: 20%; text-align: center;">
							<a href="${ctx}/userAndDataRole/delete?id=${eudr.id}">删除</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
  	</div>
	<div class="pagination">${page}</div>
</body>
</html>