<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新增物品</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript">
var serverUrl='${imgUrl}';
$(document).ready(function() {
	$("#name").focus();
	$("#inputForm").validate({
		submitHandler: function(form){
			loading('正在提交，请稍等...');
			form.submit();
		},
		errorContainer: "#messageBox",
		errorPlacement: function(error, element) {
			$("#messageBox").text("输入有误，请先更正。");
			if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
				error.appendTo(element.parent().parent());
			} else {
				error.insertAfter(element);
			}
		}
	});
});
var serverUrl="${imgServerUrl}";
var iconurl="${portalImageProductDef.imageUrl}";
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
	//$("#iconurl_IMG").empty();
	var imgPath=icon.value;
	if(/.*[\u4e00-\u9fa5]+.*$/.test(imgPath)){
		alert("文件路径不能含有汉字！");
		$("#"+icon.id).val();
		return false;
	}else{
		var id=icon.id;
		var ids=$("#id");
		var imgDiv="";
		$.ajaxFileUpload({  
	       url:'${ctx}/portalImage/imgUpload',  
	       secureuri:false,  
	       fileElementId:id,//file标签的id 
	       data:{'imgDiv':id,'id':ids},
	       dataType: 'json',//返回数据的类型  
	       success: function (data, status) {  
	    	   //{"imgUrl":"/indeximage/zdlmdz/1.png","returnCode":0,"imgDiv":"iconurl1"}
	        	if(data.returnCode=="0"){
	        		imgDiv=data.imgDiv;
	        		imgDiv=imgDiv.substring(0,parseInt(imgDiv.length)-1);
	        		$("#imageUrl").val(data.imgUrl);
	        		$("#iconurl_IMG img").attr("src",serverUrl+data.imgUrl);
	        		$("#iconurl_IMG").show();
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
		<li ><a href="${ctx}/portalImage/getProductList?type=${type}">数据列表</a></li>
		 <li class="active"><a href="${ctx}/portalImage/editportalImage?type=${type}">编辑数据</a></li> 
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="portalImageProductDef" action="${ctx}/portalImage/savePortalImage" method="post" class="form-horizontal" onsubmit="return check()">
  		<form:hidden path="id" />
  		<form:hidden path="IsStatic" />
  		<form:hidden path="productCode" />
  		<form:hidden path="dataCode" />
  		<form:hidden path="imageUrl" />
  		<input type="hidden" name="type" value="${type}"/>
		<div class="control-group">
			<label class="control-label">标题:</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">提供单位:</label>
			<div class="controls">
				<form:input path="dataSourse" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
			<div id="iconurl_IMG" class="control-group">
				<label class="control-label">图标：</label>
				<div class="controls">	
					<img  src="${imgServerUrl}${portalImageProductDef.imageUrl}"/>
				</div>
			</div>
		
		<div class="control-group">
			<label class="control-label">上传图标：</label>
			<div class="controls">
				<div  style="display:inline;">
					<input type="file" id="iconurl" name="iconurl" onchange="imgUpload(this)"/>
				</div>				
			</div>	 
		</div>
		<div class="control-group">
			<label class="control-label">链接地址:</label>
			<div class="controls">
				<form:input path="linkUrl" htmlEscape="false" maxlength="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">展开类型:</label>
			<div class="controls">
				<form:radiobuttons path="showType" items="${fns:getDictList('showType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
  </form:form>
</body>
</html>