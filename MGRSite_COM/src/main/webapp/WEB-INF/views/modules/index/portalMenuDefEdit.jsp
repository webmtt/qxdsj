<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
<title>编辑数据类型</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>

<link href="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.js" type="text/javascript"></script>
<link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/bootstrap/2.3.1/js/bootstrap.min.js" type="text/javascript"></script>
<link href="${ctxStatic}/fancyBox/source/jquery.fancybox.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/fancyBox/source/jquery.fancybox.js" type="text/javascript"></script>
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>

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
$(function(){
		$.validator.addMethod("chinese", function(value, element, param){
			//方法中又有三个参数:value:被验证的值， element:当前验证的dom对象，param:参数(多个即是数组)
			//alert(value + "," + $(element).val() + "," + param[0] + "," + param[1]);
			return new RegExp(/.*[\u4e00-\u9fa5]+.*$/).test(value);

		}, "必须全为中文");
		$.validator.addMethod("english", function(value, element, param){
			return new RegExp(/[a-zA-Z]+$/).test(value);

		}, "必须全为英文");
		$.validator.addMethod("shuzi", function(value, element, param){
			return new RegExp(/[0-9]+$/).test(value);

		}, "必须全为数字");
		
	$("#inputForm").validate({
		rules:{
			orderno:{
				shuzi:true
			}
			
		}
    }); 
	
});

function check(){
	var chnname=$("#chnname").val();
	var linkurl=$("#linkurl").val();
	var orderno=$("#orderno").val();
	if(""==chnname||undefined==chnname){
		alert("请输入中文名");
		return false;
	}else if(""==linkurl||undefined==linkurl){
		alert("请输入链接地址");
		return false;
	}/*else if(""==orderno||undefined==orderno){
		alert("请输入排序号");
		return false;
	}*/else{
		return true;
	}
}
	
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/portalMenuDef/list">导航菜单栏目列表</a></li>
		<li class="active"><a href="${ctx}/portalMenuDef/toEdit?pid=${pid}&menuid=${menuid}">编辑导航菜单栏目</a></li>  
		
	</ul>
	<tags:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="portalMenuDef" action="${ctx}/portalMenuDef/edit" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
        <input type="hidden" id="menuid" name="menuid" value="${menuid}"/>
        <form:hidden path="invalid"/>
        <form:hidden path="orderno"/>
      	<input type="hidden" id="parent" name="parent.menuid" value="${pid}">
        
        
        <c:choose>
        	<c:when test="${pid eq '0'}">
        	</c:when>
        	<c:otherwise> 
        		<div class="control-group">
				    <label class="control-label">父节点：</label>
					<div class="controls">	
						<label>
							<tags:treeselect id="parent" name="treeNodeId" value="" labelName="treeNodeName" labelValue="${pname}"
							title="数据服务菜单" url="/portalMenuDef/getTreeList"  extId=""/>
						</label>
					</div>
				</div>	
        	</c:otherwise>
        </c:choose> 
        
        
       
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>中文名称：</label>
			<div class="controls">				
				<form:input path="chnname" id="chnname" htmlEscape="false" class="required" cssStyle="width:350px;"  />
			</div>
		</div>	
		
		<!--
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>排序号：</label>
				<div class="controls">				
					<form:input path="orderno" id="orderno" htmlEscape="false"  cssStyle="width:350px;" class="required digits"/>
				</div>
		   </div>    
		 -->    
		 <div class="control-group">
				<label class="control-label"><a style="color: red;">*</a>链接地址：</label>
				<div class="controls">				
					<form:input path="linkurl" id="linkurl" htmlEscape="false" cssStyle="width:350px;"/>
				</div>
		 </div>
		  <!-- 
			<div class="control-group">
				<label class="control-label"><a style="color: red;">*</a>是否有效：</label>
				<div class="controls" id="invalid">				
					<form:radiobutton class="radio-input" path="invalid" name="invalid" value="0"/>
					&nbsp;&nbsp;是&nbsp;&nbsp;&nbsp;&nbsp;
			        <form:radiobutton class="radio-input" path="invalid" name="invalid" value="1"/>
			         &nbsp;&nbsp;否
				</div>
		   </div>
		
		<div class="control-group">
			<label class="control-label">创建时间:</label>
			<div class="controls">
				<input  type="text"  id="created" name="created" cssClass="Wdate" value="${portalMenuDef.created}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
			</div>
		</div>    
		  -->   
		  <div class="control-group"> 
		     <div class="controls">		
			   <input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>
	           <input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		      </div>
		   </div>	 
			
</form:form>
</body>
</html>

