<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
<title>添加子类型</title>
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

</style>

<script type="text/javascript">
	
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
				orderNo:{
					shuzi:true
				}
			}
	    }); 
	});
	
	function check(){
		var CHNName = $("#CHNName").val();
		var orderNo = $("#orderNo").val();
		var linkUrl = $("#linkUrl").val();
		if(""==CHNName||undefined==CHNName){
			alert("请输入中文名称");
			return false;
		}/* else if(""==orderNo||undefined==orderNo){
			alert("请输入排序号");
			return false;
		} */else if(""==linkUrl||undefined==linkUrl){
			alert("请输入链接地址");
			return false;
		}else{
			return true;
		}
		
	}
	
</script>

</head>
<body>
	
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/funcItmesDef/list">栏目维护列表</a></li>
		<li class="active"><a href="${ctx}/funcItmesDef/toAdd?funcitemid=${funcitemid}&pid=${pid}">添加下级导航菜单</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form id="inputForm" action="${ctx}/funcItmesDef/save" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()"> 
		<input type="hidden" id="funcitemid" name="funcitemid" value="${funcitemid}"/>
      	<input type="hidden" id="parent" name="parent.funcitemid" value="${pid}">
      	
      	<div class="control-group">
      		<label class="control-label">父节点：</label>
        	<div class="controls">
        		<label>
        			<tags:treeselect id="parent" name="treeNodeId" value="" labelName="treeNodeName" labelValue="${pname}"
							title="数据服务菜单" url="/funcItmesDef/getTreeList" extId=""/>
        		</label>
        	</div>
        </div>
        
        <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>中文名称：</label>
			<div class="controls">				
				<input type="text" id="CHNName" value="" name="CHNName" htmlEscape="false" class="required" cssStyle="width:350px;" />
			</div>
		</div>
        
        <div class="control-group">
			<label class="control-label">中文短名：</label>
			<div class="controls">		
				<input type="text" id="shortCHNName" value="" name="shortCHNName" htmlEscape="false" cssStyle="width:350px;" />		
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">中文描述：</label>
			<div class="controls">		
				<input type="text" id="CHNDescription" value="" name="CHNDescription" htmlEscape="false" cssStyle="width:350px;" />		
			</div>
		</div>
        
        <div class="control-group">
			<label class="control-label">关键词：</label>
			<div class="controls">
				<input type="text" id="keyWord" value="" name="keyWord" htmlEscape="false" cssStyle="width:350px;" />		
				<span style="color: red;">* 多个关键词用、分割</span>
			</div>
		</div>
		
		<!-- <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>排序号：</label>
			<div class="controls">	
				<input type="text" id="orderNo" value="" name="orderNo" htmlEscape="false" class="required" cssStyle="width:350px;" />
			</div>
		</div> -->
		
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>链接地址：</label>
			<div class="controls">
				<input type="text" id="linkUrl" value="" name="linkUrl" htmlEscape="false" class="required" cssStyle="width:350px;" />		
			</div>
	    </div>
	    
	    <!-- <div class="control-group">
	    	<label class="control-label"><a style="color: red;">*</a>是否有效：</label>
	    	<div class="controls" id="invalid">
	    		<input type="radio" id="invalid1" name="invalid" value="0" checked="checked" />有效
	    		<input type="radio" id="invalid2" name="invalid" value="1" />无效
	    	</div>
	    </div> -->
	    
	    <div class="control-group"> 
	    	<div class="controls">		
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>
				<input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
			</div>
		</div>
		
	</form>
	
</body>
</html>