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
		//方法中又有三个参数:value:被验证的值， element:当前验证的dom对象，param:参数(多个即是数组)
		//alert(value + "," + $(element).val() + "," + param[0] + "," + param[1]);
		return new RegExp(/[a-zA-Z]+$/).test(value);

	}, "必须全为英文");
	$.validator.addMethod("shuzi", function(value, element, param){
		//方法中又有三个参数:value:被验证的值， element:当前验证的dom对象，param:参数(多个即是数组)
		//alert(value + "," + $(element).val() + "," + param[0] + "," + param[1]);
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
	
	//var showtype=$("#showtype").val();
	var orderno=$("#orderno").val();
	
	//var layer=$("#layer input:radio:checked").val();
	//var invalid=$("#invalid input:radio:checked").val();
	var isopen=$("#isopen input:radio:checked").val();
	
	/*if(""==layer||undefined==layer){
		alert("请选择级别");
		return false;		
	}else if(""==showtype||undefined==showtype){
		alert("请输入显示类型");
		return false;
	}else*/ if(""==orderno||undefined==orderno){
		alert("请输入排序号");
		return false;
	}else if(""==isopen||undefined==isopen){
		alert("请选择是否打开");
		return false
	}else{
		return true;
	}
}
	
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/portalitemCategoryDef/list">气象业务栏目列表</a></li>
		<li class="active"><a href="${ctx}/portalitemCategoryDef/toAdd?pid=${pid}&funcitemid=${funcitemid}">添加下级</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form id="inputForm"  action="${ctx}/portalitemCategoryDef/save" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
		<input type="hidden" id="funcitemid" name="funcitemid" value="${funcitemid}"/>
      	<input type="hidden" id="parent" name="parent.funcitemid" value="${pid}">
		
		<div class="control-group">
			<label class="control-label">父节点：</label>
			<div class="controls">	
				<label>
					<tags:treeselect id="parent" name="treeNodeId" value="" labelName="treeNodeName" labelValue="${pname}"
					title="数据服务菜单" url="/portalitemCategoryDef/getTreeList"  extId=""/>
				</label>
			</div>
		</div> 
		
		
		
		 
		
    <div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>中文名称：</label>
		<div class="controls">				
			<input type="text" id="chnname"  value="" name="chnname" htmlEscape="false" class="required" cssStyle="width:350px;"  />
		</div>
	</div>    
	<div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>中文短名：</label>
		<div class="controls">				
			<input type="text" id="shortchnname"  value="" name="shortchnname" htmlEscape="false" class="required" cssStyle="width:350px;"  />
		</div>
	</div>	
	
	<!-- 
	<div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>菜单级别：</label>
		<div class="controls" id="layer">
			<input type="radio" id="layer0" name="layer" value="0" />		
				顶级菜单
			<input type="radio" id="layer1" name="layer" value="1" />		
				一级菜单
			<input type="radio" id="layer2" name="layer" value="2" />
				二级菜单	
			<input type="radio" id="layer3" name="layer" value="3" />
				三级菜单	
		</div>
	</div>	
	--> 	
	
	<!--showtype数据库值存1
	<div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>显示类型：</label>
		<div class="controls">	
			
			<input type="text" id="showtype"  value="1" name="showtype" htmlEscape="false" class="required" cssStyle="width:350px;"  />
		</div>
	</div>
		-->	
	<!--   -->		
	<div class="control-group">
		<label class="control-label"><a style="color: red;"></a>排序号：</label>
		<div class="controls">				
			<input type="text" id="orderno" value="" name="orderno" htmlEscape="false"  cssStyle="width:350px;" class="required digits"/>
		</div>
	</div>    
	 	     
		 <div class="control-group linkurl">
				<label class="control-label">链接地址：</label>
				<div class="controls">				
					<input type="text" name="linkurl" id="linkurl" value="" htmlEscape="false" cssStyle="width:350px;"/>
				</div>
		   </div>
	<!--   	
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>是否有效：</label>
			<div class="controls"  id="invalid">						
			<input type="radio" id="invalid1" name="invalid" value="0" checked="checked" />		
				有效
			<input type="radio" id="invalid2" name="invalid" value="1"/>
				无效				
			</div>
								
	  </div>
	 -->	 	
		<div class="control-group">
			<label class="control-label">中文描述：</label>
			<div class="controls">				
				<input type="text" name="chndescription" id="chndescription" value="" htmlEscape="false" cssStyle="width:350px;"/>
			</div>
		</div>	
		
		<div class="control-group">
			<label class="control-label">是否展开：</label>
			<div class="controls"  id="isopen">						
				<input type="radio" id="invalid1" name="isopen" value="0" checked="checked" />&nbsp;&nbsp;		
					不展开
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="radio" id="invalid2" name="isopen" value="1"/>&nbsp;&nbsp;
					全展开
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="radio" id="invalid2" name="isopen" value="2"/>&nbsp;&nbsp;
					不展开无按钮
				&nbsp;&nbsp;&nbsp;&nbsp;
				<span style="color: red;">** 业务内网首页-气象业务，二级菜单下的选项是否默认展开</span>				
			</div>
		</div>
		
		  
		  <div class="control-group"> 
		     <div class="controls">		
			   <input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>
	           <input id="btnCancel" class="btn" type="button" value="取消" onclick="history.go(-1)"/>
		      </div>
		   </div>	 
			
</form>
</body>
</html>

