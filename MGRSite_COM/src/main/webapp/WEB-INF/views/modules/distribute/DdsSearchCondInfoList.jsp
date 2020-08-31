<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>   
    <title>调度任务配置</title>
    <meta name="decorator" content="default" />
	<%@include file="/WEB-INF/views/include/dialog.jsp"%>
	<style type="text/css">
	.sort {
	color: #0663A2;
	cursor: pointer;
	}
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
        <li><a href="${ctx}/dt/distribute/list">分发定义列表</a></li>
		<li class="active"><a href="${ctx}/dt/ddsSearchCondInfo/slist?dataId=${dataId}">检索条件列表</a></li>
		<li><a href="${ctx}/dt/ddsSearchCondInfo/add?dataId=${dataId}">新增检索条件</a></li> 
	</ul>
	<form:form id="form1" action="${ctx}/dt/ddsSearchCondInfo/slist" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<tags:message content="${message}"/>
   	<div>
   		<table id="tablelist" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
   			<thead>
   				
   				<th width="20%" style="text-align: center;">数据id</th>
   				<th width="60%" style="text-align: center;">检索条件</th>
   				<th width="10%" style="text-align: center;">排序号</th>
   				<th width="10%" style="text-align: center;">操作</th> 
   			</thead>
   			<tbody>
   				<c:forEach items="${list}" var="col">
   					<tr>
   						<td>${col.dataId}</td>
   						<td>${col.searchCond}</td>
   						<td>${col.orderNo}</td>
   						<td style="text-align: center;">
   						<a href="${ctx}/dt/ddsSearchCondInfo/edit?id=${col.id }&dataId=${col.dataId}">编辑</a>
   						&nbsp;&nbsp;&nbsp;
   						<a href="${ctx}/dt/ddsSearchCondInfo/delete?id=${col.id}&dataId=${col.dataId}" onclick="return confirmx('确认要删除该记录吗？', this.href)">删除</a>
   						</td>
   					</tr>
   				</c:forEach>
   			</tbody>
   		</table>
   	</div>
   	 <div class="pagination">${page}</div> 
  </body>
</html>
