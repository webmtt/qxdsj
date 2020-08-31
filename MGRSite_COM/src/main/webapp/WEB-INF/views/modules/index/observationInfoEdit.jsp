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
var imgurl="${observationInfo.imgurl}";

$(function(){
	//var imgurl=$("#imgurl").val();
	if(imgurl==undefined||imgurl==""){
		$("#imgurl_IMG").hide();
	}else{
		//$("#imgurl_IMG img").attr("src",serverUrl+imgurl);
		$("#imgurl_IMG").show();
	}
	
});


function imgUpload(icon){
	$("#imgurl_Msg").empty();
	var imgPath=icon.value;
	if(/.*[\u4e00-\u9fa5]+.*$/.test(imgPath)){
		alert("文件路径不能含有汉字！");
		$("#"+icon.id).val();
		return false;
	}else{
		var id=icon.id;
		var imgDiv="";
		$.ajaxFileUpload({  
	       url:'${ctx}/observationInfo/imgUpload',  
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



</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/observationInfo/list">首页综合观测</a></li>
		<li class="active"><a href="${ctx}/observationInfo/toEdit?id=${id}">编辑综合观测</a></li>
		
	</ul>
	<tags:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="observationInfo" action="${ctx}/observationInfo/edit" method="post" enctype="multipart/form-data" class="form-horizontal" >
        <input type="hidden" id="id" name="id" value="${id}"/>
        <input type="hidden" id="invalid" name="invalid" value="${observationInfo.invalid}"/>
        <input type="hidden" id="imgurl" name="imgurl" value="${observationInfo.imgurl }"/>
      
       
        <div class="control-group">	
			<label class="control-label">中文名称:</label>
			<div class="controls">						
				<input type="text" id="chnname" value="${observationInfo.chnname }" name="chnname" htmlEscape="false" cssStyle="width:350px;"/>
			</div>
		</div>
		
		<div class="control-group">	
			<label class="control-label">中文短名称:</label>
			<div class="controls">						
				<input type="text" id="shortchnname" value="${observationInfo.shortchnname }" name="shortchnname" htmlEscape="false" cssStyle="width:350px;"/>
			</div>
		</div>
		
		<c:if test="${!empty observationInfo.imgurl}">
			<div id="imgurl_IMG" class="control-group">
				<label class="control-label">图标：</label>
				<div class="controls">	
					<img  src="${imgServerUrl}${observationInfo.imgurl}">
				</div>
			</div>
		</c:if>
		 
		
		<div class="control-group">
			<label class="control-label">上传图标：</label>
			<div class="controls">
				<div  style="display:inline;">
					<input type="file" id="imgurl1" name="imgurl1" onchange="imgUpload(this)"/><span id="imgurl_Msg"></span>
				</div>				
			</div>	 
		</div>
		
		
		<div class="control-group">	
			<label class="control-label">链接地址:</label>
			<div class="controls">						
				<input type="text" id="linkurl" value="${observationInfo.linkurl }" name="linkurl" htmlEscape="false" cssStyle="width:350px;"/>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>排序号:</label>
			<div class="controls">						
				<input type="text"  id="orderno" value="${observationInfo.orderno }" name="orderno" htmlEscape="false" cssStyle="width:350px;" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">数量:</label>
			<div class="controls">						
				<input type="text"  id="procount" value="${observationInfo.procount }" name="procount" htmlEscape="false" cssStyle="width:350px;" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">过去日期:</label>
			<div class="controls">						
				<input type="text"  id="pastdate" value="${observationInfo.pastdate }" name="pastdate" htmlEscape="false" cssStyle="width:350px;" />
			</div>
		</div>
		 
		<div class="control-group">
			<label class="control-label">创建时间:</label>
			<div class="controls">
				<input  type="text"  id="created" name="created" cssClass="Wdate" value="${observationInfo.created }" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
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

