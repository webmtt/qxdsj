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
	var searchsetcode=$("#searchsetcode").val();
	var searchgroupcode=$("#searchgroupcode").val();
	var chnname=$("#chnname").val();
	var orderno=$("#orderno").val();
	var invalid=$("#invalid input:radio:checked").val();
	var isoptional=$("#isoptional input:radio:checked").val();
	
	if(""==searchsetcode||undefined==searchsetcode){
		alert("请输入检索条件集合编码");
		return false;
	}else if(""==searchgroupcode||undefined==searchgroupcode){
		alert("请输入检索条件分组编码");
		return false;
	}else if(""==chnname||undefined==chnname){
		alert("请输入检索条件分组中文名称");
		return false;
	}else if(""==orderno||undefined==orderno){
		alert("请输入排序号");
		return false;
	}else if(""==invalid||undefined==invalid){
		alert("请选择是否无效");
		return false
	}else if(""==isoptional||undefined==isoptional){
		alert("请选择是否可选");
		return false
	}else{
		return true;
	}
}
	
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/searchSetDef/list">数据检索条件集合定义列表</a></li>
		<li class="active"><a href="${ctx}/searchSetDef/toAdd">添加数据检索条件集合定义</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form id="inputForm"  action="${ctx}/searchSetDef/save" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
    
    <div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>检索条件集合编码：</label>
		<div class="controls">				
			<input type="text" id="searchsetcode"  value="" name="searchsetcode" htmlEscape="false" class="required" cssStyle="width:350px;"  />
		</div>
	</div>    
	 
	<div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>检索条件分组编码：</label>
		<div class="controls">				
			<input type="text" name="searchgroupcode" id="searchgroupcode" value="" htmlEscape="false" cssStyle="width:350px;"/>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>检索条件分组中文名称：</label>
		<div class="controls">				
			<input type="text" name="chnname" id="chnname" value="" htmlEscape="false" cssStyle="width:350px;"/>
		</div>
	</div> 
	 
	<div class="control-group">
		<label class="control-label">检索条件分组英文名称：</label>
		<div class="controls">				
			<input type="text" id="engname" value="" name="engname" htmlEscape="false"  cssStyle="width:350px;"/>
		</div>
	</div>    
	 	     
		 
	<div class="control-group">
		<label class="control-label"><a style="color: red;">*</a>排序号：</label>
		<div class="controls">				
			<input type="text" name="orderno" id="orderno" value="" htmlEscape="false" cssStyle="width:350px;"/>
		</div>
	</div>
	
	<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>是否可选：</label>
			<div class="controls"  id="isoptional">						
			<input type="radio" id="isoptional1" name="isoptional" value="0" checked="checked" />		
				是
			<input type="radio" id="isoptional2" name="isoptional" value="1"/>
				否				
			</div>
								
	  	</div>
	
		<div class="control-group">
			<label class="control-label">是否有效：</label>
			<div class="controls"  id="invalid">						
			<input type="radio" id="invalid1" name="invalid" value="0" checked="checked" />		
				有效
			<input type="radio" id="invalid2" name="invalid" value="1"/>
				无效				
			</div>
								
	  	</div>
		 
		
		<div class="control-group">
			<label class="control-label">创建时间:</label>
			<div class="controls">
				<input  type="text"  id="created" name="created" cssClass="Wdate" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
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

