<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<html>
<head>
	<title>添加文档</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(function(){
		$("#inputForm").validate({
			
		});
	})
	</script>
	<style type="text/css">
	.title{
	 background-color: #f5f5f5;
    border-radius: 4px;
    color: #555555;
    display: table-cell;
    font-size: 16px;
    height: 40px;
    margin: 10px;
    vertical-align: middle;
    width: 1160px;
	}
	.type{
		font-weight: bold;
		float: left;
		padding-left: 10px;
	}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/dataReferDef/dataReferDefList?dataCode=${dataCode}&categoryid=${categoryid}&pid=${pid}">资料文献列表</a></li>
		<li class="active"><a href="javascript:void(0)">新增文献</a></li> 
	</ul>
	<form:form id="form1" modelAttribute="dataReferDef" method="post" class="breadcrumb form-search">
		<span style="font-weight: bold;">当前位置：</span>${pidName }--${categoryName}--${dataDefName}
	</form:form>
	<%-- <div class="title">
  		<div class="type">当前位置：</div><div class="typeName">${categoryName}--${dataDefName}</div>
  	</div> --%>
	<form id="inputForm" action="${ctx}/dataReferDef/save"  method="post" class="form-horizontal">
		<%-- <div class="control-group">	
			<label class="control-label">引用文献类型:</label>
			<div class="controls">						
				<input type="text" id="datacode" value="${datacode}" name="datacode" htmlEscape="false" cssStyle="width:350px;" />
			</div>
		</div> --%>
		<input type="hidden" id="datacode" name="datacode" value="${dataCode}">
		<input type="hidden" id="categoryid" name="categoryid" value="${categoryid }">
		<input type="hidden" id="pid" name="pid" value="${pid}">
		<input type="hidden" id="pageType" name="pageType" value="${pageType }">
		<input type="hidden" id="orderno" name="orderno" value="0">
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>文献名称:</label>
			<div class="controls">						
				<input type="text"  id="refername" value="" name="refername" htmlEscape="false" cssStyle="width:350px;" class="required"/>
			</div>
		</div>
		<div class="form-actions">
			 <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp; 
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div> 
	</form>
</body>
</html>