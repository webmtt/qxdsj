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
	var itemcaption=$("#itemcaption").val();
	var itemvalue=$("#itemvalue").val();
	
	if(""==itemcaption||undefined==itemcaption){
		alert("项显示名称不能为空！");
		return false;
	}else if(""==itemvalue||undefined==itemvalue){
		alert("项值不能为空！");
		return false;
	}else{
		return true;
	}
}
	
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/searchCondCfg/SearchCondItemslist2?itemtype=${searchCondItems.itemtype}">条件字典列表</a></li>
		<li class="active"><a >编辑字典详情</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form id="inputForm"  action="${ctx}/searchCondCfg/updateCondItems" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
    <input type="hidden" id="invalid" name="invalid" value="${searchCondItems.invalid}"/>
    <input type="hidden" id="itemtype" name="itemtype" value="${searchCondItems.itemtype}"/>
     <input type="hidden" id="id" name="id" value="${searchCondItems.id}"/>
     <input type="hidden" id="orderno" name="orderno" value="${searchCondItems.orderno}"/>
    <div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>项显示名称：</label>
		<div class="controls">				
			<input type="text" id="itemcaption"   name="itemcaption" value="${searchCondItems.itemcaption}" htmlEscape="false" class="required" cssStyle="width:350px;"  />
		</div>
	</div>    
    <div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>项值：</label>
		<div class="controls">				
			<input type="text" id="itemvalue"  name="itemvalue" value="${searchCondItems.itemvalue}" htmlEscape="false" class="required" cssStyle="width:350px;"  />
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

