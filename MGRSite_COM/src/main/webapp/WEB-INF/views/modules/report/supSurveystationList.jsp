<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>测站信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action", "${ctx}/report/supSurveystation/list");
			$("#searchForm").submit();
        	return false;
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
				url : "${ctx}/report/supSurveystation/excelUpload",
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
		function check(){
			var t=$("#stationNum").val();

		}
		// 只能输入正数
		function clearNoNum(obj) {
			// 只能输入数字和小数点的文本框, 只能输入小数点后两位
			obj.value = obj.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符
			obj.value = obj.value.replace(/^\./g,"");  //验证第一个字符是数字而不是.
			obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.
			obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
			// 只能输入小数点后两位
			obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".").replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
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
		<li class="active"><a href="${ctx}/report/supSurveystation/list">测站列表</a></li>
		<li><a href="${ctx}/report/supSurveystation/form">测站添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="supSurveystation" action="${ctx}/report/supSurveystation/list" method="post" class="breadcrumb form-search" enctype="multipart/form-data">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>区站号：</label>
			<form:input path="stationNum" htmlEscape="false" maxlength="11" class="input-medium" onkeyup="clearNoNum(this)"  placeholder="请输入数字"/>
			<label>站点名称：</label>
			<form:input path="stationName" htmlEscape="false" maxlength="50" class="input-medium"/>
			&nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<label>
			<input id="file" type="file" name="file" class="input-medium" style="display:none;"/>
				<span id="controls" class="btn btn-primary"><span>批量导入</span></span></label>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/report/supSurveystation/download?fileName=区站模板.xlsx" download="区站模板.xlsx" >模板下载</a>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" showCheckbox="true" >
		<thead>
			<tr>
				<th>区站号</th>
				<th>站点名称</th>
				<th>省份</th>
				<th>地市</th>
				<th>区/县</th>
				<th>纬度</th>
				<th>经度</th>
				<th>测站高度</th>
				<th>更新者</th>
				<th>更新时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="supSurveystation">
			<tr>
				<td><a href="${ctx}/report/supSurveystation/form?id=${supSurveystation.id}">
						${supSurveystation.stationNum}
				</a></td>
				<td>
						${supSurveystation.stationName}
				</td>
				<td>
						${supSurveystation.provinces}
				</td>
				<td>
						${supSurveystation.cities}
				</td>
				<td>
						${supSurveystation.county}
				</td>
				<td>
						${supSurveystation.wd}
				</td>
				<td>
						${supSurveystation.jd}
				</td>
				<td>
						${supSurveystation.viewHeight}
				</td>
				<td>
					${fns:getDictLabel(supSurveystation.updateBy.id, 'sys_user_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${supSurveystation.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
    				<a href="${ctx}/report/supSurveystation/form?id=${supSurveystation.id}">修改</a>
					<a href="${ctx}/report/supSurveystation/delete?id=${supSurveystation.id}" onclick="return confirmx('确认要删除该测站信息吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>