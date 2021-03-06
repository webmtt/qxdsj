<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp" %>
<html>
<head>
	<title>前台日志管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<form:form id="searchForm" action="${ctx}/sys/desklog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>操作菜单：</label><input id="title" name="title" type="text" maxlength="50" class="input-mini" value="${deskLog.title}"/>
			<label>操作用户：</label><input id="createBy.id" name="createBy.id" type="text" maxlength="50" class="input-mini" value="${deskLog.createBy.id}"/>
			<label>URI：</label><input id="requestUri" name="requestUri" type="text" maxlength="50" class="input-mini" value="${deskLog.requestUri}"/>
		</div><div style="margin-top:8px;">
			<label>日期范围：&nbsp;</label><input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="Wdate"
				value="<fmt:formatDate value="${log.beginDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			<label>&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="Wdate"
				value="<fmt:formatDate value="${log.endDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />&nbsp;&nbsp;
			&nbsp;
			&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>操作菜单</th><th>操作用户</th><th>URI</th><th>提交方式</th><th>操作者IP</th><th>操作时间</th></thead>
		<tbody><%request.setAttribute("strEnter", "\n");request.setAttribute("strTab", "\t");%>
		<c:forEach items="${page.list}" var="deskLog">
			<tr>
				<td>${deskLog.title}</td>
				<td>${deskLog.createBy.name}</td>
				<td><strong>${deskLog.requestUri}</strong></td>
				<td>${deskLog.method}</td>
				<td>${deskLog.remoteAddr}</td>
				<td><fmt:formatDate value="${deskLog.createDate}" type="both"/></td>
			</tr>
			<c:if test="${not empty deskLog.exception}"><tr>
				<td colspan="8" style="word-wrap:break-word;word-break:break-all;">
					异常信息: <br/>
					${fn:replace(fn:replace(fns:escapeHtml(deskLog.exception), strEnter, '<br/>'), strTab, '&nbsp; &nbsp; ')}</td>
			</tr></c:if>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>