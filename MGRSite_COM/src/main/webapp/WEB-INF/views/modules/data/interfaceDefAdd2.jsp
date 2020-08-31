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
	var interfacesetcode=$("#interfacesetcode").val();
	var typeName=$("#typeName").val();
	var searchcodelist=$("#searchcodelist").val();
	var interfacename=$("#interfacename").val();
	var interfacedesc=$("#interfacedesc").val();
	var optioncodelist=$("#optioncodelist").val();
	var interfacesetcode=$("#interfacesetcode").val();
	if(interfacesetcode==null||interfacesetcode==""){
		alert("请输入检索接口类型编码");
		return false;
	}else if(typeName==null||typeName==""){
		alert("请输入检索接口类型名称");
		return false;
	}else if(searchcodelist==null||searchcodelist==""){
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
		var status="0";
		$.ajax({
			url : "${ctx}/searchInterface/checkCode",
			async : false,
			type : 'POST',
			data : "interfacesetcode=" + interfacesetcode,
			dataType : "json",
			success : function(result) {
			   status=result.status;
			}
		});
		if(status=="1"){
			alert("该检索接口类型编码已存在，请重新输入！");
			return false;
		}
		return true;
	}
}
	
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/searchInterface/searchInterfaceList?datacode=${datacode}">检索结果列表</a></li>
		<li class="active"><a>新增接口</a></li> 
	</ul>
	<form id="inputForm"  action="${ctx}/searchInterface/saveInterface2" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
	    <input type="hidden" name="datacode" value="${datacode}">
	    <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>接口类型编码：</label>
			<div class="controls">				
				<input type="text" id="interfacesetcode"   name="interfacesetcode" htmlEscape="false" class="required" cssStyle="width:350px;"  />
			</div>
		</div>
		 <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>接口类型名称：</label>
			<div class="controls">				
				<input type="text" id="typeName"   name="typeName" htmlEscape="false" class="required" cssStyle="width:350px;"  />
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

