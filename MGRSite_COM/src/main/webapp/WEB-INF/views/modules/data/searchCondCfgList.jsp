<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>通配字典表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">	
var ctx="${ctx}";


</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/searchCondCfg/list">条件通配列表</a></li>
		<li><a href="${ctx}/searchCondCfg/toAdd">通配新增</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form:form id="form1" name="form1" modelAttribute="searchCondCfg" action="${ctx}/searchCondCfg/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div align="left">
			<label>检索配置代码：</label>
			<input type="text" id="searchconfigcode" name="searchconfigcode" value="${searchconfigcode}" />
			&nbsp;<input  id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page()"/>
		</div>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
		<thead>
  			<th>序号</th>
    		<th>项名称</th>  
    		<th>项值</th>
    		<th>排序号</th> 
    		<th>操作</th>
  		</thead>
  		<tbody class="fs_tbody">		
  			<c:forEach items="${list}" var="searchCondCfg" varStatus="status">
				<tr>
					<td>${status.index+1}</td>
					<td>${searchCondCfg.searchconfigcode}</td>
					<td>${searchCondCfg.datasource}</td>
					<td>${searchCondCfg.chndescription }</td>
					<td>${searchCondCfg.engdescription }</td>
					<td>
						<a href="${ctx}/searchCondCfg/toEdit?searchconfigcode=${searchCondCfg.searchconfigcode}">修改</a> 
						<a onclick="return confirmx('要删除该记录吗？', this.href)" href="${ctx}/searchCondCfg/delete?searchconfigcode=${searchCondCfg.searchconfigcode}">删除</a>
					</td>
				</tr>
			</c:forEach>
  		</tbody>
	</table>
</body>
</html>