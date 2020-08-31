<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>条件排序</title>
<meta name="decorator" content="default"/>
<script src="${ctxStatic}/jquery-ui/jquery-ui.js" type="text/javascript"></script>
<link href="${ctxStatic}/jquery-ui/jquery-ui.min.css" rel="stylesheet" type="text/css"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">	
$(document).ready(function() {
	
});
function updateSort() {
	loading('正在提交，请稍等...');
	$("#listForm").attr("action", "${ctx}/dataSearchDef/searchCondChose");
	$("#listForm").submit();
}
function reverse(){//勾选两个复选框，序号交换
	
	var chk=$('input[name="checkbox"]:checked');
	var s=[];
	$('input[name="checkbox"]:checked').each(function(){ 
		s.push($(this).val()); 
	}); 
		document.form1.action= '${ctx}/dataSearchDef/searchCondChose?cids='+s.toString();
	    document.form1.submit();
}
function closeWin(){
	 parent.closeModal();
}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0)">条件列表</a></li> 	
	</ul>
	<form id="form1" name="form1" method="post">
	     <input type="hidden" id="searchgroupcode" name="searchgroupcode" value="${searchgroupcode}">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr>
			<th></th>
	  			<th>中文名</th><th style="text-align:center;">排序</th></tr>
			<tbody>
				<c:forEach items="${list}" var="searchCondDef">
					<tr id="${searchCondDef.id}">
					    <td style="text-align:center;width:5%;">
					    <c:if test="${searchCondDef.groupdefaultsearch==0}">
					      <input type="checkbox" name="checkbox" value="${searchCondDef.id }" />
					    </c:if>
					      <c:if test="${searchCondDef.groupdefaultsearch==1}">
					      <input type="checkbox" name="checkbox" checked="checked" value="${searchCondDef.id }" />
					     </c:if>
					    </td> 
						<td style="width: 50%;">${searchCondDef.chnname}</td>
						<td style="width: 45%;">${searchCondDef.orderno}</td>
						 <input type="hidden" name="ids" value="${searchCondDef.id}"/>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="form-actions pagination-left">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保存" onclick="reverse();"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  id="btnCancel" class="btn" type="button" value="关闭" onclick="closeWin();"/>
		</div>
	 </form>
</body>
</html>