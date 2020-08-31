<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>数据服务列表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">
$(document).ready(function() {
	$("#treeTable").treeTable({expandLevel : 3});
});

function uploadFile() {
	var file = $("#file").val();
	file = file.substring(file.lastIndexOf('.'), file.length);
	if (file == '') {
		alert("上传文件不能为空！");
	} else if (file != '.xlsx' && file != '.xls') {
		alert("请选择正确的excel类型文件！");
	} else {
		ajaxFileUpload();
	}
}
function ajaxFileUpload() {
	var fileObj = document.getElementById("file").files[0];
	var form = new FormData();
	form.append("file", fileObj);
	$.ajax({
		url : "${ctx}/dataService/excelUpload",
		type : "POST",
		async : false,
		data : form,
		processData : false,
		contentType : false,
		beforeSend : function() {
			console.log("正在进行，请稍候");
		},
		success:function(){
			alert("上传成功");
			location.reload();//刷新当前页面
		},
		error:function(){
			alert("上传失败");
		}
	});
}
$(function () {
	var $input =  $("#file");
	// ①为input设定change事件
	$input.change(function () {
		//    ②如果value不为空，调用文件加载方法
		if($(this).val() != ""){
			uploadFile(this);
		}
	})
})
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0)">资料种类列表</a></li>
		<li><a href="${ctx}/dataService/addDataCategoryDef">新增资料种类</a></li>
<%--		<li><a href="${ctx}/dataService/sortCategory">排序（大类）</a></li>--%>
<%--		<label>--%>
<%--			<input id="file" type="file" name="file" class="input-medium" style="display:none;"/>--%>
<%--			<span id="controls" class="btn btn-primary"><span>批量导入</span></span></label>--%>
<%--		&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/report/supSurveystation/download?fileName=资料种类模板.xlsx" download="资料种类模板.xlsx" >模板下载</a>--%>
	</ul>
	</ul>
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr>
				<th>中文名</th>
				<th>数据说明</th>
			    <th>时间序列</th>
			   <th style="text-align:center;">排序</th>
			     <th style="text-align:center;">操作</th>
			</tr>
			<c:forEach items="${dataCategoryDefs}" var="dataCategory">
				<tr id="${dataCategory.categoryid}" pId="${dataCategory.parent.categoryid}">
					<td style="width: 8%;">
<%--					<c:if test="${!empty dataCategory.iconurl}">--%>
<%--					<img src="${imgServiceUrl}/${dataCategory.iconurl}">--%>
<%--					</c:if>--%>
					</img>${dataCategory.chnname}</td>
					<td title="${dataCategory.chndescription}">${fn:substring(dataCategory.chndescription, 0, 63)}</td>
					<td>${dataCategory.timeseq}</td>
					<td>${dataCategory.orderno}</td>
						<td style="text-align:center;">
					<a href="${ctx}/dataService/addDataCategoryDef?categoryid=${dataCategory.categoryid}&pid=${dataCategory.parent.categoryid}">修改</a>
				 	<c:choose>
<%--				 	<c:when test="${dataCategory.parent.categoryid eq '0'&&dataCategory.categoryid!=17  }">--%>
<%--				 		&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/dataService/addDataCategoryDef?pid=${dataCategory.categoryid}">新增资料子类</a>&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--				 		<c:if test="${dataCategory.invalid==0}">--%>
<%--				 		    <a onclick="return confirmx('要把该类型及所有子类型改为无效吗？', this.href)" href="${ctx}/dataService/deleteCategory?pid=0&id=${dataCategory.categoryid}">无效</a>--%>
<%--				 		</c:if>--%>
<%--				 		<c:if test="${dataCategory.invalid==1}">--%>
<%--				 		    <a onclick="return confirmx('要把该类型及所有子类型改为有效吗？', this.href)" href="${ctx}/dataService/deleteCategory?pid=0&id=${dataCategory.categoryid}">有效</a>--%>
<%--				 		</c:if>--%>
<%--&lt;%&ndash;				 		&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/dataService/sortCategory?pid=0&id=${dataCategory.categoryid}" >排序</a>&ndash;%&gt;--%>
<%--				 	</c:when>--%>
				 	<c:when test="${dataCategory.parent.categoryid eq '0'&& dataCategory.categoryid==17 }">
				 		&nbsp;&nbsp;&nbsp;&nbsp;
				 		<c:if test="${dataCategory.invalid==0}">
				 		   <a onclick="return confirmx('要把该类型及所有子类型改为无效吗？', this.href)" href="${ctx}/dataService/deleteCategory?pid=0&id=${dataCategory.categoryid}">无效</a>
				 		</c:if>
				 		<c:if test="${dataCategory.invalid==1}">
				 		   <a onclick="return confirmx('要把该类型及所有子类型改为有效吗？', this.href)" href="${ctx}/dataService/deleteCategory?pid=0&id=${dataCategory.categoryid}">有效</a>
				 		</c:if>
				 	</c:when>
				 	<c:otherwise>
					&nbsp;<a href="${ctx}/dataService/getDataDetail?categoryid=${dataCategory.categoryid}&pid=${dataCategory.parent.categoryid}">查看资料列表</a>&nbsp;
					    <c:if test="${dataCategory.invalid==0}">
					       <a onclick="return confirmx('要把该类型及所有子类型改为无效吗？', this.href)" href="${ctx}/dataService/deleteCategory?pid=${dataCategory.parent.categoryid}&id=${dataCategory.categoryid}">无效</a>
					    </c:if>
					    <c:if test="${dataCategory.invalid==1}">
					       <a style="color:red;" onclick="return confirmx('要把该类型及所有子类型改为有效吗？', this.href)" href="${ctx}/dataService/deleteCategory?pid=${dataCategory.parent.categoryid}&id=${dataCategory.categoryid}">有效</a>
					    </c:if>
				 	</c:otherwise>
				 	</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
	 </form>
</body>
</html>