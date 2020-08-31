<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户建议列表</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script type="text/javascript">
		$(document).ready(function() {
			// 表格排序
			var chk_value = $("#chk_value").val();
			$("input:radio[value='"+chk_value+"']").attr('checked','true');
			var orderBy = $("#orderBy").val().split(" ");
			$("#contentTable th.sort").each(function(){
				if ($(this).hasClass(orderBy[0])){
					orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="DESC"?"down":"up";
					$(this).html($(this).html()+" <i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
				}
			});
			$("#contentTable th.sort").click(function(){
				var order = $(this).attr("class").split(" ");
				var sort = $("#orderBy").val().split(" ");
				for(var i=0; i<order.length; i++){
					if (order[i] == "sort"){order = order[i+1]; break;}
				}
				if (order == sort[0]){
					sort = (sort[1]&&sort[1].toUpperCase()=="DESC"?"ASC":"DESC");
					$("#orderBy").val(order+" DESC"!=order+" "+sort?"":order+" "+sort);
				}else{
					$("#orderBy").val(order+" ASC");
				}
				page();
			});
		});
		function page(n,s){
			var fDStatus = $('input:radio[name="radio"]:checked').val();
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/feed/list?fDStatus="+fDStatus);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/feed/list">用户建议列表</a></li>
	</ul>
	<input id="chk_value" name="chk_value" type="hidden"
		value="${fDStatus}" />
	<form:form id="searchForm" modelAttribute="userFeedBack" action="${ctx}/sys/feed/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<div>
			<label>建议标题：</label><form:input path="fDTitle" htmlEscape="false" maxlength="50" class="input-small"/>
			<label>单位类型：</label>
			<form:select path="unitId" class="input-small">
			    <form:option value="0" label="全部"/>
				<form:options items="${fns:getUnitList('unitId')}"
					itemLabel="name" itemValue="id" htmlEscape="false" />
			</form:select>
			<input type="radio" name="radio"  value="0" />待回复
			<input type="radio" name="radio"  value="1" />已回复
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th class="sort loginName">建议标题</th><th class="sort name">建议时间</th>
		<th>建议状态</th><th>目标单位</th><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="userFeedBack">
			<tr>
				<td><a href="${ctx}/sys/feed/form?id=${userFeedBack.id}">${userFeedBack.fDTitle}</a></td>
				<td>${userFeedBack.fDTime}</td>
				<td>${fns:getDictLabel(userFeedBack.fDStatus, 'reviewflag','')}</td>
				<td>${fns:getDictLabel(userFeedBack.unitId, 'unitId','')} ${TargetUnitId }</td>
				<td>
					<c:if test="${TargetUnitId==userFeedBack.unitId }">
						<c:if test="${userFeedBack.fDStatus=='0'}">
				   			<a href="${ctx}/feed/form?id=${userFeedBack.id}">回复建议</a>
						</c:if>
    					<c:if test="${userFeedBack.fDStatus=='1'}">
				   			<a href="${ctx}/feed/view?id=${userFeedBack.id}">查看</a>
						</c:if>	
						<a href="${ctx}/feed/delFeedById?id=${userFeedBack.id}" onclick="return confirmx('确认要删除该建议吗？', this.href)">删除</a>
					</c:if>
					<c:if test="${TargetUnitId!=userFeedBack.unitId }">
				   		<a href="${ctx}/feed/view?id=${userFeedBack.id}">查看</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>