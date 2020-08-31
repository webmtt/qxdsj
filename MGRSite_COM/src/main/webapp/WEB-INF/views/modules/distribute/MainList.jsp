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
		<li class="active"><a href="${ctx}/dt/distribute/list">分发定义列表</a>
		 <li><a href="${ctx}/dt/distribute/columnAdd?area=${area}">新增专栏</a></li> 
		</li>
	</ul>
	<form:form id="form1" action="${ctx}/column/columnarrange/columnList?area=${area}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<tags:message content="${message}"/>
   	<div>
   		<table id="tablelist" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
   			<thead>
   				
   				<th style="text-align: center;">名称</th>
   				<th style="text-align: center;">层次描述</th>
   				<th style="text-align: center;">图片地址</th>
   				<th style="text-align: center;">链接地址</th>
   				 <th width="10%" style="text-align: center;">操作</th> 
   			</thead>
   			<tbody>
   				<c:forEach items="${page.list}" var="col">
   					<tr>
   						
   						<td>${col.chnName}</td>
   						<td>${col.layerDescription }</td>
   						<td>${col.columnImageURL}</td>
   						<td>${col.linkURL }</td>
   						<td style="text-align: center;"><a href="${ctx}/column/columnarrange/teleDel?columnItemID=${col.columnItemID }&area=${area}" onclick="return confirmx('确认要删除该记录吗？', this.href)">删除</a></td>
   					</tr>
   				</c:forEach>
   			</tbody>
   		</table>
   	</div>
   	 <div class="pagination">${page}</div> 
  </body>
</html>
