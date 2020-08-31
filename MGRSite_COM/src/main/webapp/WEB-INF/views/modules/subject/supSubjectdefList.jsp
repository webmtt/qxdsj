<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>专题产品</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 3}).show();
		});
		function updateSort() {
			loading('正在提交，请稍等...');
			$("#listForm").attr("action", "${ctx}/sys/menu/updateSort");
			$("#listForm").submit();
		}
		function viewTableInfo(tablename) {
			$.ajax({
				url: '${ctx}/subject/supSubjectdef/getColumn',
				type: 'POST',
				data: {'tablename': tablename},
				success: function (result) {
					$("#tableInfo").empty();
					var jsobj=result;
					var info="<table id='tablelist' class='table table-striped table-bordered table-condensed' style='word-break:break-all; word-wrap:break-word;'>";
					if(jsobj.length>0){
						for(var t in jsobj){
							info+="<tr><td>"+jsobj[t].columnName+"</td>";
							info+="<td>"+jsobj[t].dataType+"</td>";
							if(jsobj[t].characterMaximumLength!=undefined) {
								info += "<td>" + jsobj[t].characterMaximumLength + "</td>";
							}else{
								info += "<td></td>";
							}
							info+="<td>"+jsobj[t].columnComment+"</td></tr>";
						}
					}
					info+="</table>";
					$("#tableInfo").html(info);
				}
			});
			$("#myModalLabel").html(tablename + "信息");
		}
		function closeDataRoleModel() {
			$(".close").click();
		}

		function showBigImg() {
			$("#myModal").show();
		}

		function showbox(item) {
			if ("1" == item) {
				$("#reason").hide();
			}
			if ("2" == item) {
				$("#reason").show();
			}
			var areaId = $("input[type='checkbox'][id='r']:checked");
			var flag = false;
			$(areaId).each(function () {
				if (this.attributes['value'].value == 'others') {
					flag = true;
				}
			});
			if (flag) {
				$("#content").show();
			} else {
				$("#content").hide();
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/subject/supSubjectdef/list">产品列表</a></li>
		<li><a href="${ctx}/subject/supSubjectdef/form">产品添加</a></li>
	</ul>
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
			<thead>
			<tr>
				<th>产品名称</th>
				<th>描述</th>
				<th>关键字</th>
				<th>要素种类</th>
				<th>产品代号</th>
				<th>更新者</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody><c:forEach items="${list}" var="supSubjectdef">
				<tr id="${supSubjectdef.id}" pId="${supSubjectdef.parent.id ne '1'?supSubjectdef.parent.id:'0'}">
					<td nowrap>
						<a href="${ctx}/subject/supSubjectdef/form?id=${supSubjectdef.id}">
							${supSubjectdef.productName}
						</a>
					</td>
					<td>
							${supSubjectdef.description}
					</td>
					<td>
							${supSubjectdef.keyword}
					</td>
					<td>
							${supSubjectdef.kind}
					</td>
					<td>
							${supSubjectdef.procode}
					</td>

					<td>
							${fns:getDictLabel(supSubjectdef.updateBy.id, 'sys_user_type', '')}
					</td>
					<td>
						<fmt:formatDate value="${supSubjectdef.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
							${supSubjectdef.remarks}
					</td>
					<td>
						<a href="${ctx}/subject/supSubjectdef/form?id=${supSubjectdef.id}">修改</a>
						<c:choose>
							<c:when test="${supSubjectdef.ispub=='1'}">
								<a href="${ctx}/subject/supSubjectdef/updatepub?id=${supSubjectdef.id}" style="color:green">有效</a>
							</c:when>
							<c:otherwise >
								<c:if test="${supSubjectdef.ispub=='2'}">
									<a href="${ctx}/subject/supSubjectdef/updatepub?id=${supSubjectdef.id}" style="color:red">无效</a>
								</c:if>
							</c:otherwise>
						</c:choose>
						<a href="${ctx}/subject/supSubjectdef/delete?id=${supSubjectdef.id}" onclick="return confirmx('确认要删除该产品吗？', this.href)">删除</a>
						<a href="${ctx}/subject/supSubjectdef/form?parent.id=${supSubjectdef.id}" >添加下级菜单</a>
					</td>
				</tr>
			</c:forEach></tbody>
		</table>
	</form>
	<div class="modal fade" id="viewTableModel"  role="dialog"
		 aria-labelledby="myModalLabel" style="width: 632px;margin-top:-55px">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header"
					 style="height: 30px;vertical-align:middle;padding-bottom: 2px;padding-top: 5px;">
					<table>
						<tr>
							<td><h4 class="modal-title" id="myModalLabel" style="float: left;width: 591px;"></h4>
							</td>
							<td>
								<button type="button" class="close" data-dismiss="modal"
										aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</td>
						</tr>
					</table>
				</div>
				<div class="modal-body" id="tableInfo" style="overflow: hidden">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" onclick="closeDataRoleModel()">关闭</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>