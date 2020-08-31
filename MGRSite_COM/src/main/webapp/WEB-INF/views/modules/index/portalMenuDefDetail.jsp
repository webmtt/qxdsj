<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>查看详情</title>
<meta name="decorator" content="default" />

<style type="text/css">
.sort {
	color: #0663A2;
	cursor: pointer;
}
table th,table td{
	text-align:center !important;
	vertical-align: middle !important;
}
.linkurl{
   display:none;
}
</style>
<script type="text/javascript">	
var ctx = "${ctx}";
var ctxStatic = "${ctxStatic}";
		
</script>

</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/portalMenuDef/list">导航菜单栏目列表</a></li>
		<li class="active"><a href="${ctx}/portalMenuDef/getDetail">查看详情</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="portalMenuDef"  method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
         
       
		<div class="control-group">
			<label class="control-label">中文名称：</label>
			<div class="controls">				
				<form:input path="chnname" id="chnname" htmlEscape="false" cssStyle="width:500px;" disabled="true"/>
			</div>
		</div>	
		<!-- 
		 <div class="control-group">
			<label class="control-label">排序号：</label>
				<div class="controls">				
					<form:input path="orderno" id="orderno" htmlEscape="false" cssStyle="width:500px;" disabled="true" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" onblur="this.v();"/>
				</div>
		   </div>
		   -->	 
		   <div class="control-group ">
				<label class="control-label">链接地址：</label>
				<div class="controls">				
					<form:input path="linkurl" id="linkurl" htmlEscape="false" cssStyle="width:500px;" disabled="true"/>
				</div>
		   </div>
			<div class="control-group">
				<label class="control-label"><a style="color: red;">*</a>是否有效：</label>
				<div class="controls">				
					<form:radiobutton class="radio-input" path="invalid" name="invalid" value="0" disabled="true"/><label style="margin-right: 30px;">是</label>
			        <form:radiobutton class="radio-input" path="invalid" name="invalid" value="1" disabled="true"/><label style="margin-right: 30px;">否</label>
				</div>
		   </div>
		    
		 <div class="control-group"> 
		     <div class="controls">		
	           <input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		      </div>
		   </div>
		
			
</form:form>
</body>
</html>

