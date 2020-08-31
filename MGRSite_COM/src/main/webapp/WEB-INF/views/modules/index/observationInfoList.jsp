<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>首页综合观测</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">	
var ctx="${ctx}";

function page(n,s){
	var chnname=$("#chnname").val();
	$("#form1").attr("action","${ctx}/observationInfo/list?chnname="+chnname);
	$("#form1").submit();
	return false;
}

function updateSort() {
	loading('正在提交，请稍等...');
	$("#form1").attr("action", "${ctx}/observationInfo/updateSort");
	$("#form1").submit();
}

</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/observationInfo/list">首页综合观测</a></li>
	</ul>
	<tags:message content="${message}"/>
	
	<form id="form1" method="post">
		<div align="left">
			<label>中文名称：</label>
			<input type="text" id="chnname" name="chnname" value="${chnname}" />
			&nbsp;<input  id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page()"/>
		</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
		<thead>
  			<th>序号</th>
    		<th>中文名称</th>  
    		<th>链接地址</th>  
    		<th>操作</th>
  		</thead>
  		<tbody class="fs_tbody">		
  			<c:forEach items="${list}" var="observationInfo">
				<tr>
					<td>
					   <input type="hidden" name="ids" value="${observationInfo.id}"/>
					   <input name="sorts" type="text" value="${observationInfo.orderno}" style="width:50px;margin:0;padding:0;text-align:center;">
					</td>
					<td>${observationInfo.chnname}</td>
					<td>${observationInfo.linkurl}</td>
					<td>
						<a href="${ctx}/observationInfo/toEdit?id=${observationInfo.id}">修改</a> 
					</td>
				</tr>
			</c:forEach>
  		</tbody>
	</table>
	<div align="left">
		<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();" />
	</div>
	</form>
</body>
</html>