<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>查看详情</title>
<meta name="decorator" content="default" />

<style type="text/css">
	input[type=text]{width:100%;border:0;outline:none;background:#fff;box-shadow: none;}
	.detailTxt{width: 350px; margin-top: 4px;}
</style>

<script type="text/javascript">
	var ctx = "${ctx}";
	var ctxStatic = "${ctxStatic}";
</script>

</head>

<body>
	
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/funcItmesDef/list">栏目维护列表</a></li>
		<li class="active"><a href="${ctx}/funcItmesDef/getDetail?funcitemid=${funcitemid}">查看详情</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="funcItmesDef" action="${ctx}/funcItmesDef/toEdit" method="post" enctype="multipart/form-data" class="form-horizontal">
		
		<input type="hidden" id="funcitemid" name="funcitemid" value="${funcitemid}"/>
		<input type="hidden" id="pid" name="pid" value="${pid}"/>
		
		<div class="control-group">
      		<label class="control-label">中文名称：</label>
      		<div class="controls">
      			<div class="detailTxt">${funcItmesDef.CHNName}</div>
      			<%-- <form:input path="CHNName" id="CHNName" htmlEscape="false" class="required" cssStyle="width:350px;" onclick="this.blur()"/> --%>
      		</div>
      	</div>
      	
      	<div class="control-group">
			<label class="control-label">中文短名：</label>
			<div class="controls">
				<div class="detailTxt">${funcItmesDef.shortCHNName}</div>
				<%-- <form:input path="shortCHNName" id="shortCHNName" htmlEscape="false" cssStyle="width:350px;" onclick="this.blur()"/> --%>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">中文描述：</label>
			<div class="controls">
				<div class="detailTxt">${funcItmesDef.CHNDescription}</div>
				<%-- <form:input path="CHNDescription" id="CHNDescription" htmlEscape="false" cssStyle="width:350px;" onclick="this.blur()"/> --%>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">关键词：</label>
			<div class="controls">
				<div class="detailTxt">${funcItmesDef.keyWord}</div>
				<%-- <form:input path="keyWord" id="keyWord" htmlEscape="false" cssStyle="width:350px;" onclick="this.blur()"/> --%>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">排序号：</label>
			<div class="controls">
				<div class="detailTxt">${funcItmesDef.orderNo}</div>
				<%-- <form:input path="orderNo" id="orderNo" htmlEscape="false"  cssStyle="width:350px;" onclick="this.blur()"/> --%>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">链接地址：</label>
			<div class="controls">
				<div class="detailTxt">${funcItmesDef.linkUrl}</div>
				<%-- <form:input path="linkUrl" id="linkUrl" htmlEscape="false" cssStyle="width:350px;" onclick="this.blur()"/> --%>
			</div>
	    </div>
	    
	    <div class="control-group">
	    	<div class="controls">
	    		<input id="btnSubmit" class="btn btn-primary" type="submit" value="修改"/>
				<input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
			</div>
		</div>
		
		<script type="text/javascript">
			if($("#messageBox").length>0){
				$("#messageBox").show();
			}
		</script>
		
	</form:form>
	
</body>

</html>