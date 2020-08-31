<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>相关链接列表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">	
$(document).ready(function() {
	var chk_value = $("#linktype").val().split(",");
	for(var i=0; i<chk_value.length; i++){
		$("input:checkbox[value='"+chk_value[i]+"']").attr('checked','true');
	}
	
});
function page(){
	var chk_value2 =[];
	$('input[name="friendLink"]:checked').each(function(){
	chk_value2.push($(this).val());
	});
	$("#form1").attr("action","${ctx}/friendLink/list?linktype="+chk_value2);
	$("#form1").submit();
	return false;
}
function updateSort() {
	loading('正在提交，请稍等...');
	$("#listForm").attr("action", "${ctx}/friendLink/updateSort");
	$("#listForm").submit();
}
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0)">相关链接列表</a></li>
		<li><a href="${ctx}/friendLink/add">新增相关链接</a></li> 
	</ul>
	<input id="linktype" name="linktype" type="hidden"
		value="${linktype}" />
	<form:form id="form1" modelAttribute="friendLink" action="${ctx}/friendLink/list" method="post" class="breadcrumb form-search">
   		<div align="left">
			<c:forEach items="${fns:getDictList('friendLink')}" var="list">
				<input type="checkbox" name="friendLink" value="${list.value}" />${list.label}
			</c:forEach>
			<input  id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page()"/>
		</div>
		
	</form:form>
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>中文名</th>
			<th>连接类型</th>
			<th style="text-align:center;">排序</th>
			<th style="text-align:center;">链接地址</th>
			<th style="text-align:center;">操作</th></tr>
			<c:forEach items="${list}" var="friendLink">
				<tr>
					<td style="width: 20%;">
					</img>${friendLink.chnname}</td>
					<td style="width: 20%;">
					</img>${fns:getDictLabel(friendLink.linktype, 'friendLink', '无')}</td>
					<td style="text-align:center;width:20%;">
					   <input type="hidden" name="ids" value="${friendLink.linkid}"/>
					   <input name="sorts" type="text" value="${friendLink.orderno}" style="width:50px;margin:0;padding:0;text-align:center;">
					</td>
					<td style="text-align:center;width:20%;">${friendLink.linkurl}</td>
					<td style="text-align:center;">
					<a href="${ctx}/friendLink/edit?id=${friendLink.linkid}">修改</a> 
<%--					&nbsp;<a href="${ctx}/friendLink/view?id=${friendLink.linkid}">查看</a>&nbsp;--%>
					<a onclick="return confirmx('要删除该类型吗？', this.href)" href="${ctx}/friendLink/delete?id=${friendLink.linkid}">删除</a>
					</td>
				</tr>
			</c:forEach>
		</table>
		<div class="form-actions pagination-left">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存排序" onclick="return updateSort();"/>
		</div>
	 </form>
</body>
</html>