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
		<li   class="active"><a href="${ctx}/dt/ddsFtpInfo/flist2?dataId=${dataId}">ftp地址列表</a></li>
		 <li><a href="${ctx}/dt/ddsFtpInfo/flist3?dataId=${dataId}">添加ftp地址</a></li> 
	</ul>
	<form:form id="form1" action="${ctx}/dt/ddsFtpInfo/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<tags:message content="${message}"/>
   	<div>
   		<table id="tablelist" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
   			<thead>
   				<th style="text-align: center;">ftp地址ID</th>
   				<th style="text-align: center;">ftp地址Ip</th>
   				<th style="text-align: center;">ftp地址端口</th>
   				<th style="text-align: center;">ftp用户名</th>
   				<th style="text-align: center;">ftp密码</th>
   			</thead>
   			<tbody>
   				<c:forEach items="${list}" var="col">
   					<tr>
   						<td>${col.ftpId}</td>
   						<td>${col.ftpIp}</td>
   						<td>${col.ftpPort}</td>
   						<td>${col.ftpUser }</td>
   						<td>${col.ftpPwd}</td>
   					</tr>
   				</c:forEach>
   			</tbody>
   		</table>
   	</div>
  </body>
</html>
