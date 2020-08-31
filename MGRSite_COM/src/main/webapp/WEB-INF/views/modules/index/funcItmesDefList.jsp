<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>栏目维护列表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">
	table td{word-wrap: break-word;}
	table tr td:FIRST-CHILD {
		text-overflow: ellipsis;
		-moz-text-overflow: ellipsis;
		overflow: hidden;
		white-space: nowrap;
	}
</style>
<script type="text/javascript">
	
	$(document).ready(function() {
		$("#treeTable").treeTable({expandLevel : 4});
	});
	
	function search(){
		$("#form1").submit();
		return false;
	};
	
</script>
</head>

<body>
	
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/funcItmesDef/list">栏目维护列表</a></li>
	</ul>
	
	<form:form id="form1" modelAttribute="funcItmes" action="${ctx}/funcItmesDef/search" method="post" class="breadcrumb form-search">
		<div>
			<label>中文名称：</label>
			<form:input id="CHNName" path="CHNName" htmlEscape="false" maxlength="50" class="required"/>&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return search();"/>
		</div>
	</form:form>
	
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed" width="100%" style="table-layout:fixed;margin-bottom:0;">
			<tr>
				<th style="width: 22%; text-align: center;">中文名称</th>
				<th style="width: 25%; text-align: center;">链接</th>
				<th style="width: 25%; text-align: center;">描述</th>
				<th style="width: 10%; text-align: center;">关键词</th>
				<th style="width: 18%; text-align: center;">操作</th>
			</tr>
			<c:forEach items="${list}" var="funcItmesDef">
				<tr id="${funcItmesDef.funcItemID}" pId="${funcItmesDef.parent.funcItemID}">
					<c:if test="${fn:length(funcItmesDef.CHNName)>15}">
						<td title="${funcItmesDef.CHNName}">${funcItmesDef.CHNName}</td>
					</c:if>
					<c:if test="${fn:length(funcItmesDef.CHNName)<=15}">
						<td>${funcItmesDef.CHNName}</td>
					</c:if>
					<td>${funcItmesDef.linkUrl}</td>
					<td>${funcItmesDef.CHNDescription}</td>
					<td>${funcItmesDef.keyWord}</td>
					<td style="text-align: center;">
						<a href="${ctx}/funcItmesDef/getDetail?funcitemid=${funcItmesDef.funcItemID}">查看</a>
						&nbsp;
						<a href="${ctx}/funcItmesDef/toEdit?funcitemid=${funcItmesDef.funcItemID}&pid=${funcItmesDef.parent.funcItemID}">修改</a>
						<c:choose>
							<c:when test="${(funcItmesDef.layer eq '0')||(funcItmesDef.layer eq '1')||(funcItmesDef.layer eq '2')||(funcItmesDef.layer eq '3')}">
								&nbsp;
								<a href="${ctx}/funcItmesDef/toAdd?funcitemid=${funcItmesDef.funcItemID}&pid=${funcItmesDef.parent.funcItemID}">添加下级</a>
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
						<c:choose>
							<c:when test="${(funcItmesDef.isHasChild eq '1')}">
								&nbsp;
								<a href="${ctx}/funcItmesDef/toSortOrder?funcitemid=${funcItmesDef.funcItemID}">排序</a>
							</c:when>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<!-- parentID为空或找不到上级单独拿出来 -->
		<table id="tablelist" class="table table-striped table-bordered table-condensed" width="100%" style="table-layout:fixed;border-top:none;">
			<c:forEach items="${noParentList}" var="funcItmesDef">
				<tr id="${funcItmesDef.funcItemID}" pId="${funcItmesDef.parent.funcItemID}">
					<c:if test="${fn:length(funcItmesDef.CHNName)>15}">
						<td style="width: 22%;" title="${funcItmesDef.CHNName}">${funcItmesDef.CHNName}</td>
					</c:if>
					<c:if test="${fn:length(funcItmesDef.CHNName)<=15}">
						<td style="width: 22%;">${funcItmesDef.CHNName}</td>
					</c:if>
					<td style="width: 25%;">${funcItmesDef.linkUrl}</td>
					<td style="width: 25%;">${funcItmesDef.CHNDescription}</td>
					<td style="width: 10%;">${funcItmesDef.keyWord}</td>
					<td style="width: 18%;text-align: center;">
						<a href="${ctx}/funcItmesDef/getDetail?funcitemid=${funcItmesDef.funcItemID}">查看</a>
						&nbsp;
						<a href="${ctx}/funcItmesDef/toEdit?funcitemid=${funcItmesDef.funcItemID}&pid=${funcItmesDef.parent.funcItemID}">修改</a>
						<c:choose>
							<c:when test="${(funcItmesDef.layer eq '0')||(funcItmesDef.layer eq '1')||(funcItmesDef.layer eq '2')||(funcItmesDef.layer eq '3')}">
								&nbsp;
								<a href="${ctx}/funcItmesDef/toAdd?funcitemid=${funcItmesDef.funcItemID}&pid=${funcItmesDef.parent.funcItemID}">添加下级</a>
								
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
						<c:choose>
							<c:when test="${(funcItmesDef.isHasChild eq '1')}">
								&nbsp;
								<a href="${ctx}/funcItmesDef/toSortOrder?funcitemid=${funcItmesDef.funcItemID}">排序</a>
							</c:when>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<script type="text/javascript">
			if($("#messageBox").length>0){
				$("#messageBox").show();
			}
		</script>
		
	</form>

</body>

</html>