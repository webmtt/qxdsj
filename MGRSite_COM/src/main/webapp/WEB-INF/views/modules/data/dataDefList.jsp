<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>元数据定义表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">	
var ctx="${ctx}";

function page(n,s){
	var chnname=$("#chnname").val();
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#form1").attr("action","${ctx}/dataService/dataDefList?chnname="+chnname);
	$("#form1").submit();
	return false;
}
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dataService/dataDefList">元数据资料列表</a></li>
	</ul>
	<tags:message content="${message}"/>
	<form:form id="form1" name="form1" modelAttribute="dataDef" action="${ctx}/dataService/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div align="left">
			<label>资料代码：</label>
			<input type="text" id="chnname" name="chnname" value="${chnname}" />
			&nbsp;<input  id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page()"/>
		</div>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
		<thead>
  			<th>序号</th>
    		<th>中文名称</th>  
    		<th>操作</th>
  		</thead>
  		<tbody class="fs_tbody">		
  			<c:forEach items="${page.list}" var="dataDef" varStatus="status">
				<tr>
					<td>${status.index+1}</td>
					<td>${dataDef.chnname}</td>
					<td>
						<a href="${ctx}/dataService/toEdit?datacode=${dataDef.datacode}">修改</a> 
						<a onclick="return confirmx('要删除该记录吗？', this.href)" href="${ctx}/dataService/delete?datacode=${dataDef.datacode}">删除</a>
					</td>
				</tr>
			</c:forEach>
  		</tbody>
	</table>
	 <div class="pagination">${page}</div> 
</body>
</html>