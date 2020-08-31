<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
<title>编辑数据类型</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script type="text/javascript">	
var ctx = "${ctx}";
var ctxStatic = "${ctxStatic}";

function check(){
	var searchcodelist=$("#searchcodelist").val();
	var interfacename=$("#interfacename").val();
	var interfacedesc=$("#interfacedesc").val();
	var optioncodelist=$("#optioncodelist").val();
	var interfacesetcode=$("#interfacesetcode").val();
	if(searchcodelist==null||searchcodelist==""){
		alert("请输入检索条件编码列表");
		return false;
	}else if(interfacename==null||interfacename=""){
		alert("请输入接口名称");
		return false;
	}else if(interfacedesc==null||interfacedesc==""){
		alert("请输入接口描述");
		return false;
	}else if(optioncodelist==null||optioncodelist==""){
		alert("请输入可选检索条件编码列表");
		return false;
	}else if(interfacesetcode==null||interfacesetcode==""){
		alert("请输入接口集合编码");
		return false;
	}else{
		return true;
	}
}
	
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/searchInterface/searchInterfaceList?datacode=${datacode}">检索结果列表</a></li>
		<li class="active"><a>编辑检索结果</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="searchInterfaceDef" action="${ctx}/searchInterface/saveInterface" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
      <input type="hidden" id="id" name="id" value="${searchInterfaceDef.id}">
       <input type="hidden" id="invalid" name="invalid" value="${searchInterfaceDef.invalid}">
        <input type="hidden" name="datacode" value="${datacode}">
       <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>检索条件编码列表：</label>
			<div class="controls">				
				<input type="text" id="searchcodelist"   name="searchcodelist" htmlEscape="false" class="required" cssStyle="width:350px;"  />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">接口名称：</label>
			<div class="controls">
			    <form:input path="interfacename" htmlEscape="false" cssStyle="width:350px;" class="required"/>				
			</div>
		</div>
		 <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>接口描述：</label>
			<div class="controls">
			    <form:input path="interfacedesc" htmlEscape="false" cssStyle="width:350px;" class="required"/>					
			</div>
		</div>
		 <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>可选检索条件编码列表：</label>
			<div class="controls">
			     <form:input path="optioncodelist" htmlEscape="false" cssStyle="width:350px;" class="required"/>					
			</div>
		</div>
		 <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>接口集合编码：</label>
			<div class="controls">	
			     <form:input path="interfacesetcode" htmlEscape="false" cssStyle="width:350px;" class="required"/>				
			</div>
		</div>   
		  <div class="control-group"> 
		     <div class="controls">		
			   <input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>
	           <input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		      </div>
		   </div>	 
			
</form:form>
</body>
</html>

