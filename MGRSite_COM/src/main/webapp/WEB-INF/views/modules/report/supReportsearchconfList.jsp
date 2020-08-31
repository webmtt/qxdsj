<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>参数配置</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 3}).show();
		});
		function updateSort() {
			loading('正在提交，请稍等...');
			$("#listForm").attr("action", "${ctx}/sys/menu/updateSort");
			$("#listForm").submit();
		}

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
				url : "${ctx}/report/supReportsearchconf/excelUpload",
				type : "POST",
				async : false,
				data : form,
				processData : false,
				contentType : false,
				beforeSend : function() {
					console.log("正在进行，请稍候");
				},
				success:function(){
					alert("数据导入成功！");
					location.reload();//刷新当前页面
				},
				error:function(){
					alert("数据导入失败！");
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
			});
		});
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a href="${ctx}/report/supReportsearchconf/list">参数列表</a></li>
	<li><a href="${ctx}/report/supReportsearchconf/form">参数添加</a></li>
	<label style="position: relative;left:27%">
		<input id="file" type="file" name="file" class="input-medium" style="display:none;"/>
		<span id="controls" class="btn btn-primary" ><span>批量导入</span></span></label>
	&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/report/supSurveystation/download?fileName=参数模板.xlsx" download="参数模板.xlsx" style="position: relative;left: 34%">模板下载</a>
</ul>
<tags:message content="${message}"/>
<form id="listForm" method="post">
	<table id="treeTable" class="table table-striped table-bordered table-condensed hide" >
		<thead>
		<tr>
			<th>参数名称</th>
			<th>参数代号</th>
			<th>参数类型</th>
			<th>更新者</th>
			<th>更新时间</th>
			<th>备注信息</th>
			<th>操作</th>
		</tr>
		</thead>
		<tbody><c:forEach items="${list}" var="supReportsearchconf">
			<tr id="${supReportsearchconf.id}" pId="${supReportsearchconf.parent.id ne '1'?supReportsearchconf.parent.id:'0'}" >
				<td nowrap>
					<a href="${ctx}/report/supReportsearchconf/form?id=${supReportsearchconf.id}">
							${supReportsearchconf.paramName}
					</a>
				</td>
				<td>
						${supReportsearchconf.paramCode}
				</td>
				<td>
						${fns:getDictLabel(supReportsearchconf.paramType, 'query_type', '')}
				</td>
				<td>
						${fns:getDictLabel(supReportsearchconf.updateBy.id, 'sys_user_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${supReportsearchconf.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
						${supReportsearchconf.remarks}
				</td>
				<td>
					<a href="${ctx}/report/supReportsearchconf/form?id=${supReportsearchconf.id}">修改</a>
					<a href="${ctx}/report/supReportsearchconf/delete?id=${supReportsearchconf.id}" onclick="return confirmx('要删除该菜单及所有子菜单项吗？', this.href)">删除</a>
                    <c:if test="${supReportsearchconf.parent.id=='1'}">
                        <a href="${ctx}/report/supReportsearchconf/form?parent.id=${supReportsearchconf.id}" >添加下级菜单</a>
					</c:if>
				</td>
			</tr>
		</c:forEach></tbody>
	</table>
</form>
</body>
</html>