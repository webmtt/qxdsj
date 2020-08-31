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
		alert("项显示名称");
		return false;
	}else if(""==itemvalue||undefined==itemvalue){
		alert("项值");
		return false;
	}else{
		return true;
	}
}
	
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/searchCondCfg/SearchCondItemslist2?itemtype=${itemtype}&pid=${pid}">条件字典列表</a></li>
		<li class="active"><a >新增字典详情</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form id="inputForm"  action="${ctx}/searchCondCfg/saveCondItems" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
    <input type="hidden" id="itemtype" name="itemtype" value="${itemtype}"/>
     <input type="hidden" id="pid" name="pid" value="${pid}"/>
    <div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>项显示名称：</label>
		<div class="controls">				
			<input type="text" id="itemcaption"   name="itemcaption" htmlEscape="false" class="required" cssStyle="width:350px;"  />
		</div>
	</div>    
    <div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>项值：</label>
		<div class="controls">				
			<input type="text" id="itemvalue"  value="" name="itemvalue" htmlEscape="false" class="required" cssStyle="width:350px;"  />
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

