<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>月报记录</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script type="text/javascript">
	$(document).ready(function() {
		var delInfo='${delInfo}';
		//alert(delInfo);
		if(delInfo!=""){
			alert(delInfo);
		}	
	});
	
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#form1").attr("action","${ctx}/tvmeeting/techdoc/list");
		$("#form1").submit();
    	return false;
    }
	
	</script>
</head>
<body>
	
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/tvmeeting/techdoc/">${type }列表</a></li>
		<li><a href="${ctx}/tvmeeting/techdoc/add">技术文档新增</a></li>
	</ul>
	
	<form:form id="form1" modelAttribute="docDef" action="${ctx}/tvmeeting/techdoc/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
		<thead>
  			<th>序号</font></th>
    		<th>文档名称</font></th>		
    		<!-- <th>文档URL</font></th> 	 -->
    		<th>上传日期</th> 
    		<!--<th>是否无效</font></th> -->
    		<!--<th>创建时间</font></th> -->
    		<!--<th>创建主机名</font></th> -->
    		<!--<th>更新时间</font></th> 	 -->	
    		<!--<th>更新主机名</font></th> 	 -->
    		<th>操作</font></th>
  		</thead>
  		<tbody>		
  			<c:forEach items="${page.list}" var="docDef" varStatus="status">
  		<tr>
    		<td>${status.index+1}</td>
    		<td>${docDef.docName }</td>
    		<!--<td>${docDef.docUrl }</td>-->
    		<td><c:if test="${docDef.uploadDate!=null}">${docDef.uploadDate}</c:if></td>   				
    		<!--<td>${docDef.invalid}</td>-->
    		<!--<td><c:if test="${docDef.created!=null}">${docDef.created}</c:if></td>    -->
    		<!--<td><c:if test="${docDef.createdBy!=null}">${docDef.createdBy}</c:if></td>-->
    		<!--<td><c:if test="${docDef.updated!=null}">${docDef.updated}</c:if></td>-->
    		<!--<td><c:if test="${docDef.updatedBy!=null}">${docDef.updatedBy}</c:if></td>-->
    		<td>
    			<a href="${ctx}/tvmeeting/techdoc/delete?docId=${docDef.docId}&docDefUrl=${docDef.docUrl}"
    				 onclick="return confirmx('确认要删除该记录？（此操作会删除对应的文件）', this.href)">&nbsp;删除&nbsp;</a>
    			<a href="${ctx}/tvmeeting/techdoc/findDocDef?docId=${docDef.docId}">&nbsp;修改&nbsp;</a>
    		</td>
    	</tr>
		</c:forEach>
  		</tbody>
	</table>
	<div class="pagination">${page}</div>	
</body>
</html>