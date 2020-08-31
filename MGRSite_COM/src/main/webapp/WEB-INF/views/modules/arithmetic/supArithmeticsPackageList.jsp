<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>算法管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
	</script>
	<style type="text/css">
		li{
			display:inline;   //一行显示
			float:left;    //靠左显示
		}
	</style>

</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/arithmetic/supArithmeticsPackage/">算法列表</a></li>
		<li><a href="${ctx}/arithmetic/supArithmeticsPackage/form">算法添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="supArithmeticsPackage" action="${ctx}/arithmetic/supArithmeticsPackage/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>算法名称：</label>
				<form:input path="ariName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>算法名称</th>
				<th>算法包路径</th>
				<th>算法类路径</th>
				<th>方法名</th>
				<th>算法参数</th>
				<th>当前状态</th>
				<th>创建者</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="supArithmeticsPackage">
			<tr>
				<td><a href="${ctx}/arithmetic/supArithmeticsPackage/form?id=${supArithmeticsPackage.id}">
					${supArithmeticsPackage.ariName}
				</a></td>
				<td>
					${supArithmeticsPackage.ariPackageName}
				</td>
				<td>
					${supArithmeticsPackage.classUrl}
				</td>
				<td>
					${supArithmeticsPackage.ariMethod}
				</td>
				<td>
					${supArithmeticsPackage.purpose}
				</td>
				<td>
					${fns:getDictLabel(supArithmeticsPackage.status, 'ari_state', '')}
				</td>
				<%--<td>--%>
					<%--${fns:getDictLabel(supArithmeticsPackage.createBy.id, 'sys_user_type', '')}--%>
				<%--</td>--%>
				<td>
						${fns:getUserById(supArithmeticsPackage.createBy.id).name}
				</td>
				<td>
					<fmt:formatDate value="${supArithmeticsPackage.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${supArithmeticsPackage.remarks}
				</td>
				<td>
    				<a href="${ctx}/arithmetic/supArithmeticsPackage/form?id=${supArithmeticsPackage.id}">修改</a>
					<a href="${ctx}/arithmetic/supArithmeticsPackage/delete?id=${supArithmeticsPackage.id}" onclick="return confirmx('确认要删除该算法吗？', this.href)">删除</a>
<%--					<a href="${ctx}/arithmetic/supArithmeticsPackage/preTest?id=${supArithmeticsPackage.id}">测试</a>--%>
					<a href="${ctx}/arithmetic/supArithmeticsPackage/preTests?id=${supArithmeticsPackage.id}">数据源测试</a>
					<a href="${ctx}/arithmetic/supArithmeticsPackage/release?id=${supArithmeticsPackage.id}">发布</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>