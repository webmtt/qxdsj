<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新增 门户</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript">


function check(){
  	/* alert("1"); */
	var picCode=$("#picCode").val();
	var chnName=$("#chnName").val();
	var linkurl=$("#linkURL").val();
	var orderNo=$("#sort").val();
   var reg = new RegExp("^[0-9]*$");  
	if(""==picCode||null==picCode){
		alert("picCode不能为空");
		return false;
	}else if(!reg.test(orderNo)){
		alert("请在序号栏输入0~65534数字");
		return false;
	
	}else if(""==orderNo||null==orderNo){
			alert("序号不能为空");
			return false;	
		
		
	}else if(""==chnName||null==chnName){
		alert("名称不能为空");
		return false;
	
	}else if(""==linkurl||null==linkurl){
		alert("链接地址不能为空");
		return false;
	}else{
		return true;
	}
}



</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/portalpicshow/portalpicshowarrange/portalpicshowList?area=${area}">产品列表</a>
		 <li class="active"><a href="${ctx}/portalpicshow/portalpicshowarrange/portalpicshowAdd?area=${area}">新增产品</a></li> 
		</li>
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="portal" action="${ctx}/portalpicshow/portalpicshowarrange/Savepps?area=${area}" method="post" class="form-horizontal" onsubmit="return check()">
  		<form:hidden path="areaItem" />
  		<input type="hidden" name="areaItems" value="${area}"/>
  		
  		<div class="control-group">
			<label class="control-label">序号:</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="50"
						  class="required digits"/>
						<span id="order"></span>
			</div>
		</div>
  		
		<div class="control-group">
			<label class="control-label">产品编码:</label>
			<div class="controls">
				<form:input path="picCode" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<form:input path="chnName" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">链接地址:</label>
			<div class="controls">
					<form:textarea path="linkURL" htmlEscape="false" rows="3"
						maxlength="500" class="input-xlarge" style="width:206px;"/>
			</div>
			</div>
			
			
		<div class="control-group">
			<label class="control-label">是否显示状态:</label>
			<div class="controls">
			   <form:radiobuttons path="invalid" items="${fns:getDictList('invalid')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
  </form:form>
</body>
</html>