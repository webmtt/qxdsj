<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
<title>添加子类型</title>
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
	}else if(interfacename==null||interfacename==""){
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
		var interfacesetcode=$("#DataSubclass option:checked").val();
		$("#interfacesetcode").val(interfacesetcode);
		return true;
	}
}
	
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/searchInterface/searchInterfaceList?datacode=${datacode}&interfacesetcode=${interfacesetcode}">检索结果列表</a></li>
		<li class="active"><a>新增接口</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form id="inputForm"  action="${ctx}/searchInterface/saveInterface" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
	    <input type="hidden" name="datacode" value="${datacode}">
	    <input type="hidden" name="interfacesetcode" value="${interfacesetcode}">
	    <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>接口类型：</label>
			<div class="controls">				
				<select id="DataSubclass"  name="DataSubclass" onChange="changeDataSub(this)">
						<c:forEach items="${namelist}" var="obj">
						    <c:if test="${interfacesetcode==obj[0]}">
						        <option value="${obj[0]}" type="${obj[0]}" selected = "selected">${obj[1]}</option>
						    </c:if>
						    <c:if test="${interfacesetcode!=obj[0]}">
						        <option value="${obj[0]}" type="${obj[0]}">${obj[1]}</option>
						    </c:if>
						</c:forEach>
			</select>
			</div>
		</div>
	    <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>检索条件编码列表：</label>
			<div class="controls">				
				<input type="text" id="searchcodelist"   name="searchcodelist" htmlEscape="false" class="required" cssStyle="width:350px;"  />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>接口名称：</label>
			<div class="controls">				
				<input type="text" id="interfacename"  name="interfacename" htmlEscape="false" class="required" cssStyle="width:350px;"  />
			</div>
		</div>
		 <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>接口描述：</label>
			<div class="controls">				
				<input type="text" id="interfacedesc"  name="interfacedesc" htmlEscape="false" class="required" cssStyle="width:350px;"  />
			</div>
		</div>
		 <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>可选检索条件编码列表：</label>
			<div class="controls">				
				<input type="text" id="optioncodelist"   name="optioncodelist"  value="eleValueRanges" htmlEscape="false" class="required" cssStyle="width:350px;"  />
			</div>
		</div>
	   <div class="control-group"> 
	     <div class="controls">		
		   <input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>
	           <input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
	      </div>
	   </div>	 
			
</form>
</body>
</html>

