<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>数据服务列表</title>
<meta name="decorator" content="default"/>
<script src="${ctxStatic}/jquery-ui/jquery-ui.js" type="text/javascript"></script>
<link href="${ctxStatic}/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">	
</script>
</head>
<body>
    <ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0)">相同检索条件的资料</a></li>
	</ul>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>中文名</th></tr>
			<c:forEach items="${list}" var="dataCategory">
				<tr id="${dataCategory[0]}">
					<td style="width: 20%;">
					${dataCategory[1]}
					</td>
				</tr>
			</c:forEach>
		</table>
	 </form>
</body>
</html>