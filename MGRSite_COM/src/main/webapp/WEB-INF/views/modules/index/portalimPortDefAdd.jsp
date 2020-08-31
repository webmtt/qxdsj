<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<html>
<head>
	<title>添加文档</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript">
	var ctx = "${ctx}";
	var ctxStatic = "${ctxStatic}";
	var serverUrl="${imgServerUrl}";
	
	function imgUpload(icon){
		var imgPath=icon.value;
		if(/.*[\u4e00-\u9fa5]+.*$/.test(imgPath)){
			alert("文件路径不能含有汉字！");
			$("#"+icon.id).val();
			return false;
		}else{
			var id=icon.id;
			var imgDiv="";
			$.ajaxFileUpload({  
		       url:'${ctx}/portalimPortDef/imgUpload',  
		       secureuri:false,  
		       fileElementId:id,//file标签的id 
		       data:{'imgDiv':id},
		       dataType: 'json',//返回数据的类型  
		       success: function (data, status) {  
		        	if(data.returnCode=="0"){
		        		imgDiv=data.imgDiv;
		        		imgDiv=imgDiv.substring(0,parseInt(imgDiv.length)-1);
		        		$("#"+imgDiv).val(data.imgUrl);
		        		$("#"+imgDiv+"_IMG img").attr("src",serverUrl+data.imgUrl);
		        		$("#"+imgDiv+"_IMG").show();
		        	}else{
		        	/* 	alert("上传图片失败"); */
		        		$("#"+imgDiv+"_Msg").css("color","red");
			       		$("#"+imgDiv+"_Msg").text("上传图片失败");
		        	}
		        },
		       
		       /*
		       success: function (data, status) {  
		       		imgDiv=data.imgDiv;
		       		imgDiv=imgDiv.substring(0,parseInt(imgDiv.length)-1);
		       		$("#"+imgDiv+"_Msg").empty();
		       		if(data.returnCode=="0"){
		       			$("#"+imgDiv).val(data.imgUrl);
			       		$("#"+imgDiv+"_IMG img").attr("src",serverUrl+data.imgUrl);
			       		$("#"+imgDiv+"_IMG").show();
			       		
		       		}else{
			       		
			       		$("#"+imgDiv+"_Msg").css("color","red");
			       		$("#"+imgDiv+"_Msg").text("上传图片失败");
		       		}
		       },*/
		       error: function (data, status, e) {   
		           $("#flag").attr("value","上传失败");  
		          // alert("上传文件大小超过${maxUploadSize}M，请联系管理员上传！"); 
		           //这里处理的是网络异常，返回参数解析异常，DOM操作异常  
		         /*   alert("上传发生异常");  */ 		           
		      }
			});
			return true;
		}
		
	}	
	
	
	
	
	
	function check(){
		var chnname=$("#chnname").val();
		var layerdescription=$("#layerdescription").val();
		//var iconurl1=$("#iconurl1").val();
		//var linkurl=$("#linkurl").val();
		var invalid=$("#invalid input:radio:checked").val();
		if(""==chnname||undefined==chnname){
			alert("请输入中文名称");
			return false;				
		}/*else if(""==layerdescription||undefined==layerdescription){
			alert("请输入描述");
			return false;		
		}else if(""==linkurl||undefined==linkurl){
			alert("请输入URL");
			return false;
		}*/else{
			return true;
		}
		/*
		if(iconurl1==undefined||iconurl1==""){
			$("#iconurl_IMG").hide();
		}else{
		//	$("#iconurl_IMG img").attr("src",serverUrl+iconurl);
			$("#iconurl_IMG").show();
		}*/
		
}
	

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	<li><a href="${ctx}/portalimPortDef/portalimPortDefList">重点推荐栏目列表</a></li>
	<li class="active"><a href="${ctx}/portalimPortDef/toAdd">新增推荐栏目</a></li>
	</ul>
	<tags:message content="${message}"/>
	<form id="inputForm" action="${ctx}/portalimPortDef/save"  method="post" class="form-horizontal" onsubmit="return check()">
		<input type="hidden" id="iconurl" name="iconurl" value=""/>
		<div class="control-group">	
			<label class="control-label">中文名称:</label>
			<div class="controls">						
				<input type="text" id="chnname" value="" name="chnname" htmlEscape="false" cssStyle="width:500px;"/>
			</div>
		</div>
		
		<div class="control-group">	
			<label class="control-label">级别描述:</label>
			<div class="controls">						
				<input type="text" id="layerdescription" value="" name="layerdescription" htmlEscape="false" cssStyle="width:500px;"/>
			</div>
		</div>
		
		
		<div id="iconurl_IMG" class="control-group">
			<label class="control-label">图标：</label>
			<div class="controls">	
			<img  src="">
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">上传图标：</label>
			<div class="controls">
				<div  style="display:inline;">
					<input type="file" id="iconurl1" name="iconurl1"  onchange="imgUpload(this)"/><span id="iconurl_Msg"></span>
				</div>				
			</div>	 
		</div>
		
		
		<div class="control-group">	
			<label class="control-label">链接地址:</label>
			<div class="controls">						
				<input type="text" id="linkurl" value="" name="linkurl" htmlEscape="false" cssStyle="width:500px;"/>
			</div>
		</div>
		
		<!--  
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>排序号:</label>
			<div class="controls">						
				<input type="text"  id="orderno" value="" name="orderno" htmlEscape="false" cssStyle="width:350px;" />
			</div>
		</div>
		-->
		<div class="form-actions">
			 <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp; 
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div> 
	</form>
</body>
</html>