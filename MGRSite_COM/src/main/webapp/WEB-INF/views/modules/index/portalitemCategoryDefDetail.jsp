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
.linkurl,.datacount,.templatefile{
   display:none;
}
</style>
<script type="text/javascript">	
var ctx = "${ctx}";
var ctxStatic = "${ctxStatic}";

}
		
</script>

</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/portalitemCategoryDef/list">气象业务栏目列表</a></li>
		<li class="active"><a href="${ctx}/portalitemCategoryDef/getDetail">查看详情</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="portalitemCategoryDef"  method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
         
       
		<div class="control-group">
			<label class="control-label">中文名称：</label>
			<div class="controls">				
				<form:input path="chnname" id="chnname" htmlEscape="false" cssStyle="width:500px;"/>
			</div>
		</div>	
		<div class="control-group">
			<label class="control-label">中文短名：</label>
			<div class="controls">				
				<form:input path="shortchnname" id="shortchnname" htmlEscape="false" cssStyle="width:500px;"/>
			</div>
		</div>
		
		 <div class="control-group">
			<label class="control-label">中文描述：</label>
			<div class="controls">				
				<form:textarea path="chndescription" id="chndescription" htmlEscape="false" cssStyle="width:500px;height:100px;"/>
			</div>
		</div>
			
		 <div class="control-group">
			<label class="control-label">排序号：</label>
				<div class="controls">				
					<form:input path="orderno" id="orderno" htmlEscape="false" cssStyle="width:500px;" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" onblur="this.v();"/>
				</div>
		   </div>
		   
		   <div class="control-group ">
				<label class="control-label">链接地址：</label>
				<div class="controls">				
					<form:input path="linkurl" id="linkurl" htmlEscape="false" cssStyle="width:500px;"/>
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

