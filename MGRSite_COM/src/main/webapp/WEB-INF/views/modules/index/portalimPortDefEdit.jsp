<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
<title>修改</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<style type="text/css">
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
var serverUrl="${imgServerUrl}";
var iconurl="${portalimPortDef.iconurl}";
$(function(){
	//var iconurl=$("#iconurl").val();
	if(iconurl==undefined||iconurl==""){
		$("#iconurl_IMG").hide();
	}else{
		//$("#iconurl_IMG img").attr("src",serverUrl+iconurl);
		$("#iconurl_IMG").show();
	}
	
});


function imgUpload(icon){
	$("#iconurl_IMG").empty();
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
	    	   //{"imgUrl":"/indeximage/zdlmdz/1.png","returnCode":0,"imgDiv":"iconurl1"}
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



</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/portalimPortDef/portalimPortDefList">重点推荐栏目列表</a></li>
		<li class="active"><a href="${ctx}/portalimPortDef/toEdit?recommenditemid=${recommenditemid}">编辑推荐栏目</a></li>
		
	</ul>
	<tags:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="portalimPortDef" action="${ctx}/portalimPortDef/edit" method="post" enctype="multipart/form-data" class="form-horizontal" >
        <input type="hidden" id="recommenditemid" name="recommenditemid" value="${recommenditemid}"/>
        <input type="hidden" id="iconurl" name="iconurl" value="${portalimPortDef.iconurl }"/>
       	<input type="hidden" id="orderno" name="orderno" value="${portalimPortDef.orderno }"   />
       	<input type="hidden" id="invalid" name="invalid" value="${portalimPortDef.invalid }"   />
       
        <div class="control-group">	
			<label class="control-label">中文名称:</label>
			<div class="controls">						
				<input type="text" id="chnname" value="${portalimPortDef.chnname }" name="chnName" htmlEscape="false" cssStyle="width:350px;"/>
			</div>
		</div>
		
		<div class="control-group">	
			<label class="control-label">级别描述:</label>
			<div class="controls">						
				<input type="text" id="layerdescription" value="${portalimPortDef.layerdescription }" name="layerdescription" htmlEscape="false" cssStyle="width:350px;"/>
			</div>
		</div>
		
		 <c:if test="${!empty portalimPortDef.iconurl}">
			<div id="iconurl_IMG" class="control-group">
				<label class="control-label">图标：</label>
				<div class="controls">	
					<img  src="${imgServerUrl}${portalimPortDef.iconurl}">
				</div>
			</div>
		</c:if>
		 
		
		<div class="control-group">
			<label class="control-label">上传图标：</label>
			<div class="controls">
				<div  style="display:inline;">
					<input type="file" id="iconurl1" name="iconurl1" onchange="imgUpload(this)"/><span id="iconurl_Msg"></span>
				</div>				
			</div>	 
		</div>
		
		
		
		<div class="control-group">	
			<label class="control-label">链接地址:</label>
			<div class="controls">						
				<input type="text" id="linkurl" value="${portalimPortDef.linkurl }" name="linkurl" htmlEscape="false" cssStyle="width:350px;"/>
			</div>
		</div>
		
		<!-- 
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>排序号:</label>
			<div class="controls">						
				<input type="text"  id="orderno" value="${portalimPortDef.orderno }" name="orderno" htmlEscape="false" cssStyle="width:350px;" />
			</div>
		</div>
		 -->
		  <div class="control-group"> 
		     <div class="controls">		
			   <input id="btnSubmit" class="btn btn-primary" type="submit" value="提交"/>
	           <input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		      </div>
		   </div>	 
			
</form:form>
</body>
</html>

