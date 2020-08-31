<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>数据检索条件集合定义</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">	
var ctx="${ctx}";

function page(n,s){
	var searchsetcode=$("#searchsetcode").val();
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#form1").attr("action","${ctx}/searchSetDef/list?searchsetcode="+searchsetcode);
	$("#form1").submit();
	return false;
}
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/searchSetDef/list">数据检索条件集合定义列表</a></li>
		<li><a href="${ctx}/searchSetDef/toAdd">添加数据检索条件集合定义</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form:form id="form1" name="form1" modelAttribute="searchSetDef" action="${ctx}/searchSetDef/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div align="left">
			<label>检索条件集合编码：</label>
			<input type="text" id="searchsetcode" name="searchsetcode" value="${searchsetcode}" />
			&nbsp;<input  id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page()"/>
		</div>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
		<thead>
  			<th>序号</th>
    		<th>检索条件集合编码</th>  
    		<th>检索条件分组编码</th>
    		<th>检索条件分组中文名称</th> 
    		<th>检索条件分组英文名称</th>  
    		<th>排序号</th>  
    		<!--<th>是否可选</th>  
    		<th>是否无效</th>   --> 
    		<th>操作</th>
  		</thead>
  		<tbody class="fs_tbody">		
  			<c:forEach items="${page.list}" var="searchSetDef" varStatus="status">
				<tr>
					<td>${status.index+1}</td>
					<td>${searchSetDef.searchsetcode}</td>
					<td>${searchSetDef.searchgroupcode}</td>
					<td>${searchSetDef.chnname }</td>
					<td>${searchSetDef.engname }</td>
					<!-- <td>${searchSetDef.isoptional }</td>
					<td>${searchSetDef.invalid }</td> -->
					<td>${searchSetDef.orderno }</td>
					<td>
						<a href="${ctx}/searchSetDef/toEdit?id=${searchSetDef.id}">修改</a> 
						<a onclick="return confirmx('要删除该记录吗？', this.href)" href="${ctx}/searchSetDef/delete?id=${searchSetDef.id}">删除</a>
					</td>
				</tr>
			</c:forEach>
  		</tbody>
	</table>
	 <div class="pagination">${page}</div> 
</body>
</html>