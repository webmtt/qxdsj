<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>   
    <title>门户栏</title>
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
		<li class="active"><a href="${ctx}/portalpicshow/portalpicshowarrange/portalpicshowList?area=${area}">产品列表</a>
		 <li><a href="${ctx}/portalpicshow/portalpicshowarrange/portalpicshowAdd?area=${area}">新增产品</a></li> 
		</li>
	</ul>
	<form:form id="form1" action="${ctx}/portalpicshow/portalpicshowarrange/portalpicshowList?area=${area}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<tags:message content="${message}"/>
   	<div>
   		<table id="tablelist" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
   			<thead>
   				
   				<th style="text-align: center;">产品编码</th>
   				<th style="text-align: center;">名称</th>
   				<th style="text-align: center;">链接地址</th>
   				 <th width="10%" style="text-align: center;">操作</th> 
   			</thead>
   			<tbody>
   				<c:forEach items="${ lists}" var="tele">
   					<tr>
   						
   						<td>${tele.picCode}</td>
   						<td>${tele.chnName }</td>
   						<td>${tele.linkURL }</td>
   						<td style="text-align: center;"><a href="${ctx}/portalpicshow/portalpicshowarrange/teleDel?picCode=${tele.picCode}&area=${area }" onclick="return confirmx('确认要删除该记录吗？', this.href)">删除</a></td>
   					</tr>
   				</c:forEach>
   			</tbody>
   		</table>
   	</div>
   	 <div class="pagination">${page}</div> 
  </body>
</html>
