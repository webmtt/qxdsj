<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>栏目维护列表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
	table td{word-wrap: break-word;}
</style>
<script type="text/javascript">
	
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		
		$("#form1").submit();
    	return false;
    }
	
</script>
</head>

<body>
	
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/funcItmesDef/list">栏目维护列表</a></li>
	</ul>
	
	<form:form id="form1" modelAttribute="funcItmesDef" action="${ctx}/funcItmesDef/search" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>中文名称：</label>
			<form:input id="CHNName" path="CHNName" htmlEscape="false" maxlength="50" class="required"/>&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
		</div>
	</form:form>
	
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="tablelist" class="table table-striped table-bordered table-condensed" width="100%" style="table-layout:fixed;">
			<tr>
				<th style="width: 20%; text-align: center;">中文名称</th>
				<th style="width: 30%; text-align: center;">链接</th>
				<th style="width: 25%; text-align: center;">描述</th>
				<th style="width: 10%; text-align: center;">关键词</th>
				<th style="width: 15%; text-align: center;">操作</th>
			</tr>
			<c:forEach items="${page.list}" var="funcItmesDef">
				<tr id="${funcItmesDef.funcItemID}" pid="${funcItmesDef.parentID}">
					<td>${funcItmesDef.CHNName}</td>
					<td>${funcItmesDef.linkUrl}</td>
					<td>${funcItmesDef.CHNDescription}</td>
					<td>${funcItmesDef.keyWord}</td>
					<td style="text-align: center;">
						<a href="${ctx}/funcItmesDef/getDetail?funcitemid=${funcItmesDef.funcItemID}">查看</a>
						&nbsp;
						<a href="${ctx}/funcItmesDef/toEdit?funcitemid=${funcItmesDef.funcItemID}&pid=${funcItmesDef.parentID}">修改</a>
						<c:choose>
							<c:when test="${(funcItmesDef.layer eq '0')||(funcItmesDef.layer eq '1')||(funcItmesDef.layer eq '2')||(funcItmesDef.layer eq '3')}">
								&nbsp;
								<a href="${ctx}/funcItmesDef/toAdd?funcitemid=${funcItmesDef.funcItemID}&pid=${funcItmesDef.parentID}">添加下级</a>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${funcItmesDef.invalid eq '0'}">
								&nbsp;
								<a onclick="return confirmx('确定要把该栏目设为无效吗？', this.href)" href="${ctx}/funcItmesDef/invalidOrNot?funcitemid=${funcItmesDef.funcItemID}">无效</a>
							</c:when>
							<c:otherwise>
								&nbsp;
								<a onclick="return confirmx('确定要把该栏目设为有效吗？', this.href)" href="${ctx}/funcItmesDef/invalidOrNot?funcitemid=${funcItmesDef.funcItemID}" style="color:red;">有效</a>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<div class="pagination">${page}</div>
		
		<script type="text/javascript">
			if($("#messageBox").length>0){
				$("#messageBox").show();
			}
		</script>
		
	</form>

</body>

</html>