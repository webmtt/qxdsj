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
		<li   class="active"><a href="${ctx}/dt/ddsFtpInfo/flist">ftp地址列表</a></li>
		 <li><a href="${ctx}/dt/ddsFtpInfo/add">新增ftp地址</a></li> 
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
   				<th width="10%" style="text-align: center;">操作</th> 
   			</thead>
   			<tbody>
   				<c:forEach items="${list}" var="col">
   					<tr>
   						<td>${col.ftpId}</td>
   						<td>${col.ftpIp}</td>
   						<td>${col.ftpPort}</td>
   						<td>${col.ftpUser }</td>
   						<td>${col.ftpPwd}</td>
   						<td style="text-align: center;">
   						<a href="${ctx}/dt/DdsPushFileRecordController/list?ftpId=${col.ftpId }">查看推送记录</a>
   						<br>
   						<a href="${ctx}/dt/ddsFtpInfo/edit?ftpId=${col.ftpId }">编辑</a>
   						&nbsp;&nbsp;&nbsp;
   						<a href="${ctx}/dt/ddsFtpInfo/delete?ftpId=${col.ftpId }" onclick="return confirmx('确认要删除该记录吗？', this.href)">删除</a>
   						</td>
   					</tr>
   				</c:forEach>
   			</tbody>
   		</table>
   	</div>
   	 <div class="pagination">${page}</div> 
  </body>
</html>
