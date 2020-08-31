<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>算法池管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		li{
			display:inline;   //一行显示
		float:left;    //靠左显示
		}
	</style>
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
		<li class="active"><a href="${ctx}/stream/supArithmeticsStream/">算法池列表</a></li>
		<li><a href="${ctx}/stream/supArithmeticsStream/form">算法池添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="supArithmeticsStream" action="${ctx}/stream/supArithmeticsStream/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>算法池名称：</label>
				<form:input path="streamName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>算法池名称</th>
				<th>数据源</th>
				<th>算法名称</th>
				<th>算法顺序</th>
				<th>当前状态</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="supArithmeticsStream">
			<tr>
				<td><a href="${ctx}/stream/supArithmeticsStream/form?id=${supArithmeticsStream.id}">
					${supArithmeticsStream.streamName}
				</a></td>
				<td>
					${supArithmeticsStream.purpose}
				</td>
				<td>
					${supArithmeticsStream.sapId}
				</td>
				<td>
					${supArithmeticsStream.sequence}
				</td>
				<td>
					${fns:getDictLabel(supArithmeticsStream.status, 'ari_state', '')}
				</td>
				<td>
					<fmt:formatDate value="${supArithmeticsStream.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${supArithmeticsStream.remarks}
				</td>
				<td>
    				<a href="${ctx}/stream/supArithmeticsStream/form?id=${supArithmeticsStream.id}">修改</a>
					<a href="${ctx}/stream/supArithmeticsStream/delete?id=${supArithmeticsStream.id}" onclick="return confirmx('确认要删除该算法池吗？', this.href)">删除</a>
<%--					<a href="${ctx}/stream/supArithmeticsStream/preTest?id=${supArithmeticsStream.id}">测试</a>--%>
					<a href="${ctx}/stream/supArithmeticsStream/preTests?id=${supArithmeticsStream.id}">数据源测试</a>
					<a href="${ctx}/stream/supArithmeticsStream/release?id=${supArithmeticsStream.id}">发布</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>