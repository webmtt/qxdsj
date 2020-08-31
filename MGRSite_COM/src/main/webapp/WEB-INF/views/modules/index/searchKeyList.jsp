<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>搜索排行</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script src="${ctxStatic}/ultra/js/searchKey.js?v=3" type="text/javascript"></script>
</head>

<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/searchKey/list">搜索排行</a></li>
	</ul>
	<form:form id="form1" action="${ctx}/searchKey/list" method="post"
		class="breadcrumb form-search" commandName="customer">
		<input id="pageNo" name="pageNo" type="hidden" value="${pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${pageSize}" />
		<input id="orderBy" name="orderBy" type="hidden"
			value="${orderBy}" />
		<div align="left">
			<span style="font-weight: bold;">关键词：</span> 
			<input type="text" id="searchKeyWord" style="width:160px" name="searchKeyWord" value="${searchKeyWord}" placeholder="关键词">
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page()" />
		</div>
	</form:form>
	<tags:message content="${message}" />
	<div>
		<table id="tablelist"
			class="table table-striped table-bordered table-condensed"
			style="word-break:break-all; word-wrap:break-word;">
			<thead>
				<th style="text-align: center;">排名</th>
				<th style="text-align: center;">关键词</th>
				<th style="text-align: center;" class="sort searchsumnum">搜索总次数</th>
				<th style="text-align: center;" class="sort searchcolumnnum">栏目搜索次数</th>
				<th style="text-align: center;" class="sort searchnum">资料搜索次数</th>
			</thead>
			<tbody>
				<c:forEach items="${page.list}" var="searchKey" varStatus="status">
					<tr>
						<td style="width: 2%;">${(page.pageNo - 1) * page.pageSize + status.index + 1}</td>
						<td style="width: 8%;">${searchKey.searchkey}</td>
						<td style="width: 11%;">${searchKey.searchsumnum}</td>
						<td style="width: 11%;">${searchKey.searchcolumnnum} </td>
						<td style="width: 10%;">${searchKey.searchnum}</td>
						
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
	</div>
	<div class="pagination">${page}</div>
	
</body>
</html>
