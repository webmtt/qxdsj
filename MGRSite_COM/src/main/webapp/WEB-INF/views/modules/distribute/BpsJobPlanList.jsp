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
		<li class="active"><a href="${ctx}/dt/bpsJobPlan/jlist">作业计划列表</a></li>
		 <li><a href="${ctx}/dt/bpsJobPlan/add">新增作业计划</a></li> 
	</ul>
	<form:form id="form1" action="${ctx}/dt/bpsJobPlan/jlist" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>数据id：</label>
		<input id="jobName" name="jobName" type="text" htmlEscape="false" maxlength="50" 
		class="input-medium" value="${jobName}"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
	</form:form>
	<tags:message content="${message}"/>
   	<div>
   		<table id="tablelist" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
   			<thead>
   				
   				<th style="text-align: center;">作业名称</th>
   				<th style="text-align: center;">作业描述</th>
   				<th style="text-align: center;">作业参数</th>
   				<th style="text-align: center;">作业时间配置</th>
   				<th width="10%" style="text-align: center;">操作</th> 
   			</thead>
   			<tbody>
   				<c:forEach items="${page.list}" var="col">
   					<tr>
   						<td>${col.jobName}</td>
   						<td>${col.description}</td>
   						<td>${col.jobParameter}</td>
   						<td>${col.triGgerPolicy }</td>
   						<td style="text-align: center;">
   						<a href="${ctx}/dt/bpsJobPlan/edit?jobName=${col.jobName }">编辑</a>
   						&nbsp;&nbsp;&nbsp;
   						<a href="${ctx}/dt/bpsJobPlan/delete?jobName=${col.jobName }" onclick="return confirmx('确认要删除该记录吗？', this.href)">删除</a>
   						</td>
   					</tr>
   				</c:forEach>
   			</tbody>
   		</table>
   	</div>
   	 <div class="pagination">${page}</div> 
  </body>
</html>
