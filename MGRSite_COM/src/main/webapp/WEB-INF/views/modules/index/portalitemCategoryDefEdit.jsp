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
.linkurl,.datacount,.templatefile{
   display:none;
}
.editIcon{
  background-color: #f3f3f0;
    border: 1px solid;
    border-radius: 4px;
    height: 26px;
    line-height: 25px;
    text-align: center;
    width: 46px;
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
	
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/portalitemCategoryDef/list">气象业务栏目列表</a></li>
		<li class="active"><a href="${ctx}/portalitemCategoryDef/toEdit?pid=${pid}&funcitemid=${funcitemid}">编辑气象业务栏目</a></li>  
		
	</ul>
	<tags:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="portalitemCategoryDef" action="${ctx}/portalitemCategoryDef/edit" method="post" enctype="multipart/form-data" class="form-horizontal" >
         
        <input type="hidden" id="funcitemid" name="funcitemid" value="${funcitemid}"/>
      	<input type="hidden" id="parent" name="parent.funcitemid" value="${pid}">
        
        
        <c:choose>
        	<c:when test="${pid eq '0'}">
        	</c:when>
        	<c:otherwise> 
        		<div class="control-group">
				    <label class="control-label">父节点：</label>
					<div class="controls">	
						<label>
							<tags:treeselect id="parent" name="treeNodeId" value="" labelName="treeNodeName" labelValue="${pname}"
							title="数据服务菜单" url="/portalitemCategoryDef/getTreeList"  extId=""/>
						</label>
					</div>
				</div>	
        	</c:otherwise>
        </c:choose> 
        
        
        <!-- 
         <div class="control-group">
			<label class="control-label">父节点：</label>
				<div class="controls">	
					<label>
						<tags:treeselect id="parent" name="treeNodeId" value="" labelName="treeNodeName" labelValue="${pname}"
							title="数据服务菜单" url="/portalitemCategoryDef/getTreeList"  extId=""/><span style="color: red;">若不选，添加的类型将作为父类型显示</span>
					</label>
				</div>
		</div>
         -->
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>中文名称：</label>
			<div class="controls">				
				<form:input path="chnname" id="chnname" htmlEscape="false" class="required" cssStyle="width:350px;"  />
			</div>
		</div>	
		
		<div class="control-group">
			<label class="control-label">中文短名：</label>
			<div class="controls">				
				<form:input path="shortchnname" id="shortchnname" htmlEscape="false" cssStyle="width:350px;"/>
			</div>
		</div>
		
		<!-- 
		<div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>菜单级别：</label>
		<div class="controls" >
		<form:radiobutton class="radio-input" path="layer" name="layer" value="0"/><label style="margin-right: 30px;">顶级菜单</label>
		<form:radiobutton class="radio-input" path="layer" name="layer" value="1"/><label style="margin-right: 30px;">一级菜单</label>
		<form:radiobutton class="radio-input" path="layer" name="layer" value="2"/><label style="margin-right: 30px;">二级菜单</label>
		<form:radiobutton class="radio-input" path="layer" name="layer" value="3"/><label style="margin-right: 30px;">三级菜单</label>
		</div>
		</div>
		 -->
		
		
		
		<div class="control-group">
			<label class="control-label">中文描述：</label>
			<div class="controls">				
				<form:textarea path="chndescription" id="chndescription" htmlEscape="false" cssStyle="width:350px;"/>
			</div>
		</div>	
		
		<div class="control-group">
			<label class="control-label"><a style="color: red;"></a>排序号：</label>
				<div class="controls">				
					<form:input path="orderno" id="orderno" htmlEscape="false"  cssStyle="width:350px;" class="required digits"/>
				</div>
		   </div>    
		     
		 <div class="control-group">
				<label class="control-label">链接地址：</label>
				<div class="controls">				
					<form:input path="linkurl" id="linkurl" htmlEscape="false" cssStyle="width:350px;"/>
				</div>
		   </div>
		<!--  
			<div class="control-group">
				<label class="control-label"><a style="color: red;">*</a>是否有效：</label>
				<div class="controls">				
					<form:radiobutton class="radio-input" path="invalid" name="invalid" value="0"/><label style="margin-right: 30px;">是</label>
			        <form:radiobutton class="radio-input" path="invalid" name="invalid" value="1"/><label style="margin-right: 30px;">否</label>
				</div>
		   </div>
		 -->   
		  <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>是否展开：</label>
			<div class="controls">	
				<form:radiobutton class="radio-input" path="isopen" name="isopen" value="0"/>&nbsp;&nbsp;
					不展开
				&nbsp;&nbsp;&nbsp;&nbsp;
				<form:radiobutton class="radio-input" path="isopen" name="isopen" value="1"/>&nbsp;&nbsp;
					展开
				&nbsp;&nbsp;&nbsp;&nbsp;
				<form:radiobutton class="radio-input" path="isopen" name="isopen" value="2"/>&nbsp;&nbsp;
					不展开无按钮
				&nbsp;&nbsp;&nbsp;&nbsp;
				<span style="color: red;">** 业务内网首页-气象业务，二级菜单下的选项是否默认展开</span>
			</div>
		</div>
		 
		  
		  
		  
		  <div class="control-group"> 
		     <div class="controls">		
			   <input id="btnSubmit" class="btn btn-primary" type="submit" value="提交"/>
	           <input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		      </div>
		   </div>	 
			
</form:form>
</body>
</html>

