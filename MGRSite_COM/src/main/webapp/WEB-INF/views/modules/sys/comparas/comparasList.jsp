<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>参数设置</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script type="text/javascript">
	$(document).ready(function() {
		 //alert('${reportType}');
		 $("#selected").click(function select(){
			var description = $("input[name='description']").val();
			if(description!=null&&description!=""){
				$("#form1").attr("action","${ctx}/sys/comparas/list");
				$("#form1").submit();		
			}
			
		 });
		 
		
	});
	
	
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#form1").attr("action","${ctx}/sys/comparas/list");
		$("#form1").submit();
    	return false;
    }
	
	</script>
</head>
<body>
	
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/comparas/list">参数列表</a></li>
		<li><a href="${ctx}/sys/comparas/add">参数添加</a></li>
	</ul>
	
	<form:form id="form1" modelAttribute="comparas" action="${ctx}/sys/comparas/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>描述：</label><form:input id="description" path="description" htmlEscape="false" maxlength="50" class="required"/>
			&nbsp;<input  class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
		<thead>
  			<th>序号</th>
    		<th>键值</th>
    		<th>名称</th>
    		<th>类型</th>
    		<th>描述</th>
    		<th>布尔参数</th>
    		<th>整型参数</th>
    		<th>路径</th>
    		<th>创建时间</th>
    		<th>操作</th>
  		</thead>
  		<tbody>		
  			<c:forEach items="${page.list}" var="comparas" varStatus="status">
  		<tr>
    		<td>${status.index+1}</td>
    		<td><c:if test="${comparas.keyid!=null}">${comparas.keyid}</c:if></td>
    		<td><c:if test="${comparas.name!=null}">${comparas.name}</c:if></td>
    		<td>${comparas.type}</td>
			<td>${comparas.description}</td>
			<td>${comparas.booleanvalue}</td>
			<td>${comparas.intvalue}</td>
			<td>${comparas.stringvalue}</td>
    		<td><fmt:formatDate value="${comparas.created}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
    		<td><a href="${ctx}/sys/comparas/form?keyid=${comparas.keyid}")">修改</a></td>   		
    	
    	</tr>
		</c:forEach>
  		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
</body>
</html>