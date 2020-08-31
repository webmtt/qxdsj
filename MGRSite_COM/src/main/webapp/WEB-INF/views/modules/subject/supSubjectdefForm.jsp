<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>专题产品编辑</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});

			<%--if(${supSubjectdef.procode!=""}){--%>
            <%--    document.getElementById("isPubs").style.display="";--%>
            <%--}--%>
			initData();
		});
		function initData(){

			$('input:checkbox').each(function(){
				var value=$(this)[0].defaultValue;
				if(${fn:contains(supSubjectdef.type,'1')}&&value=='1'){
						$(this)[0].checked = true;
				}
				if(${fn:contains(supSubjectdef.type,'2')}&&value=='2'){
						$(this)[0].checked = true;
				}
				if(${fn:contains(supSubjectdef.type,'3')}&&value=='3'){
						$(this)[0].checked = true;
				}
			})
		}
		function check(){
			var type=null;
			if(${supSubjectdef.parent.id=="1"}){
				type=jQuery("input[name='type']:checked").val();
				${supSubjectdef.type=type};
			}
			return true;
		}
		function checkProCode(type) {
			var procode=$("#procode").val();
			var kind=$("#kind").val();
			debugger;
			$.ajax({
				url: '${ctx}/subject/supSubjectdef/checkProcode',
				type: 'post',
				data: {
					'procode':procode,
					'kind':kind
				},
				asyn: false,
				success: function (result) {
					if("false"==result){
						if(type==1) {
							$("#nameMsg").css("color", "red");
							$("#nameMsg").text("代号已存在");
						}else{
							$("#kindMsg").css("color", "red");
							$("#kindMsg").text("要素种类已存在");
						}
						return false;
					}else{
						if(type==1) {
							$("#nameMsg").text("");
						}else{
							$("#kindMsg").text("");
						}
						return true;
					}

				}
			});

		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/subject/supSubjectdef/list">产品列表</a></li>
		<li class="active"><a href="${ctx}/subject/supSubjectdef/form?id=${supSubjectdef.id}">产品${not empty supSubjectdef.id?'修改':'添加'}</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="supSubjectdef" action="${ctx}/subject/supSubjectdef/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="oldProCode"/>
		<form:hidden path="oldKind"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">上级参数:</label>
			<div class="controls">
				<tags:treeselect id="menu" name="parent.id" value="${supSubjectdef.parent.id}" labelName="parent.name"
								 labelValue="${supSubjectdef.parent.productName}"
								 title="参数列表" url="/subject/supSubjectdef/treeData"
								 extId="${supSubjectdef.id}" cssClass="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品名称：</label>
			<div class="controls">
				<form:input path="productName" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">描述：</label>
			<div class="controls">
				<form:input path="description" htmlEscape="false" maxlength="100" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">关键字：</label>
			<div class="controls">
				<form:input path="keyword" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<c:if test="${supSubjectdef.parent.id!='1'}">
<%--			<div class="control-group">--%>
<%--				<label class="control-label">产品图存储表：</label>--%>
<%--				<div class="controls">--%>
<%--					<form:input path="tableName" htmlEscape="false" maxlength="50" class="input-xlarge " onblur="checkTableName()"/>--%>
<%--					<span id="nameMsg"></span>--%>
<%--				</div>--%>
<%--			</div>--%>
			<div class="control-group">
				<label class="control-label">产品代号：</label>
				<div class="controls">
					<form:input path="procode" htmlEscape="false" maxlength="50" class="input-xlarge " onblur="checkProCode(1)"/>
					<span id="nameMsg"></span>
				</div>
			</div>
		</c:if>
		<c:if test="${supSubjectdef.parent.id=='1'}">
			<div class="control-group">
				<label class="control-label">要素种类：</label>
				<div class="controls">
					<form:input path="kind" htmlEscape="false" maxlength="50" class="input-xlarge " onblur="checkProCode(2)"/>
					<span id="kindMsg"></span>
				</div>
			</div>
		</c:if>
		<c:if test="${supSubjectdef.parent.id=='1'}">
			<div class="control-group">
				<label class="control-label">主页图片：</label>
				<div class="controls">
					<form:input path="smallPng" htmlEscape="false" maxlength="50" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">首页图片：</label>
				<div class="controls">
					<form:input path="bigPng" htmlEscape="false" maxlength="50" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">产品类型：</label>
				<div class="controls">
					<span>
						<input type="checkbox" value="1" name="type"/>首页展示
					</span>
					<span>
						<input type="checkbox" value="2" name="type" />热门产品
					</span>
					<span>
						<input type="checkbox" value="3" name="type"/>特色展示
					</span>

				</div>
			</div>
		</c:if>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="50" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="return check()"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>