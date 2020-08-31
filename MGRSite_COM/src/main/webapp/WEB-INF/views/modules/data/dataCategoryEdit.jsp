<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
<title>编辑数据类型</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
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
var cataid="${dataCategoryDef.categoryid}"
var serverUrl="${imgServerUrl}";
var imageurl="${dataCategoryDef.imageurl}";
var iconurl="${dataCategoryDef.iconurl}";
var largeiconurl="${dataCategoryDef.largeiconurl}";
var middleiconurl="${dataCategoryDef.middleiconurl}";
$(function(){
		$.validator.addMethod("chinese", function(value, element, param){
			//方法中又有三个参数:value:被验证的值， element:当前验证的dom对象，param:参数(多个即是数组)
			//alert(value + "," + $(element).val() + "," + param[0] + "," + param[1]);
			return new RegExp(/^[\u4E00-\u9FA5]+$/).test(value);

		}, "必须全为中文");
		$.validator.addMethod("english", function(value, element, param){
			//方法中又有三个参数:value:被验证的值， element:当前验证的dom对象，param:参数(多个即是数组)
			//alert(value + "," + $(element).val() + "," + param[0] + "," + param[1]);
			return new RegExp(/[a-zA-Z]+$/).test(value);

		}, "必须全为英文");
		
	$("#inputForm").validate({
		rules:{
			chnname:{
				chinese:true
			},
			/* shortchnname:{
				chinese:true
			},
			engname:{
				english:true
			},
			shortengname:{
				english:true
			} */
			
		}
    }); 
	if(imageurl==undefined||imageurl==""){
		$("#imageurl_IMG").hide();
	}else{
		$("#imageurl_IMG img").attr("src",serverUrl+imageurl);
		$("#imageurl_IMG").show();
	}
	if(iconurl==undefined||iconurl==""){
		$("#iconurl_IMG").hide();
	}else{
		$("#iconurl_IMG img").attr("src",serverUrl+iconurl);
		$("#iconurl_IMG").show();
	}
	if(largeiconurl==undefined||largeiconurl==""){
		$("#largeiconurl_IMG").hide();
	}else{
		$("#largeiconurl_IMG img").attr("src",serverUrl+largeiconurl);
		$("#largeiconurl_IMG").show();
	}
	if(middleiconurl==undefined||middleiconurl==""){
		$("#middleiconurl_IMG").hide();
	}else{
		$("#middleiconurl_IMG img").attr("src",serverUrl+middleiconurl);
		$("#middleiconurl_IMG").show();
	}
	 $("#dataServiceId").change(function(){
		 var pNode=$("#dataServiceId").val();
		 alert(pNode);
	 });
	 if("${type}"==0){
		 $(".BIGTYPE0").hide();
		 $(".BIGTYPE").show();
	 }else{
		 $(".BIGTYPE0").show();
		 $(".BIGTYPE").hide();
	 }
	 $(":radio").click(function(){
		 $("#type").val($(this).val());
		  if( $(this).val()==0){
			  $(".BIGTYPE").show();
			  $(".BIGTYPE0").hide();
		  }else{
			  $(".BIGTYPE").hide();
			  $(".BIGTYPE0").show();
		  }
	  });
});
function showType(value){
         $("#linkurl").val("");
         $("#datacount").val("");
         $(".templatefile").hide();
         $(".linkurl").hide();
         $(".datacount").hide();
      if(value==1){
        $(".datacount").show();
      }else if(value==2){
        $(".templatefile").show();
      }else if(value==3){
        $(".linkurl").show();
      }     

}
function imgUpload(icon){
	var imgPath=icon.value;
	if(/.*[\u4e00-\u9fa5]+.*$/.test(imgPath))
	{
	alert("文件路径不能含有汉字！");
	$("#"+icon.id).val();
	return false;
	}else{
	var id=icon.id;
	var imgDiv="";
	$.ajaxFileUpload({  
        url:'${ctx}/dataService/imgUpload',  
        secureuri:false,  
        fileElementId:id,//file标签的id 
        data:{'imgDiv':id},
        dataType: 'json',//返回数据的类型  
        success: function (data, status) {  
        		imgDiv=data.imgDiv;
        		imgDiv=imgDiv.substring(0,parseInt(imgDiv.length)-1);
        		$("#"+imgDiv+"_Msg").empty();
        	if(data.returnCode=="0"){
        		$("#"+imgDiv).val(data.imgUrl);       			
        		$("#"+imgDiv+"_IMG img").attr("src",serverUrl+data.imgUrl);
        		$("#"+imgDiv+"_IMG").show();
        	}else{
        		/* alert("上传文件失败"); */
        		$("#"+imgDiv+"_Msg").css("color","red");
        		$("#"+imgDiv+"_Msg").text("上传图片失败");
        	}
        },
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
function changeDataSub(){
	var dataSubclass = $("#DataSubclass option:checked").val();
	$("#pid").val(dataSubclass);
}
function openwin(){
	parent.open(ctx+"/dataService/showPage1");
}	 
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/dataService/list">资料种类列表</a></li>
		<li class="active"><a href="javascript:void(0)">编辑资料种类</a></li> 
	</ul>
	<form:form id="inputForm" modelAttribute="dataCategoryDef" action="${ctx}/dataService/updateDataCategory" method="post" enctype="multipart/form-data" class="form-horizontal" >
         
        <form:hidden path="categoryid" id="categoryid" value="${categoryid}"/>
      	<input type="hidden" id="parent" name="parent.categoryid" value="${pid}">
        <form:hidden path="imageurl" id="imageurl"/> 
        <form:hidden path="iconurl" id="iconurl"/> 
        <form:hidden path="largeiconurl" id="largeiconurl"/>
        <form:hidden path="middleiconurl" id="middleiconurl"/>
        <form:hidden path="showuserrankid" id="showuserrankid" value="0" /> 
        <input type="hidden" id="type" name="type" value="${type}">
         <input type="hidden" id="pid" name="pid" value="${pid}">
        <div class="control-group">
		     <input id="openWin" class="btn btn-primary" type="button" value="查看示例" onclick="openwin()" style="margin-left:200px;"/>
		</div>	
		<div class="BIGTYPE0">
			<div class="control-group">
			     <label class="control-label">大类选择：</label>
			     <div class="controls">	
				     <select id="DataSubclass"  name="DataSubclass" onChange="changeDataSub(this)">
							<c:forEach items="${plist}" var="dataCategory">
							    <c:if test="${pid==dataCategory.categoryid}">
							        <option value="${dataCategory.categoryid}" type="${dataCategory.categoryid}" selected = "selected">${dataCategory.chnname}</option>
							    </c:if>
							    <c:if test="${pid!=dataCategory.categoryid}">
							        <option value="${dataCategory.categoryid}" type="${dataCategory.categoryid}">${dataCategory.chnname}</option>
							    </c:if>
							</c:forEach>
					 </select>
				 </div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>中文名称：</label>
			<div class="controls">				
				<form:input path="chnname" id="chnname" htmlEscape="false" class="required" cssStyle="width:350px;"  />
			</div>
		</div>
		<div class="BIGTYPE">	
		 <div class="control-group">
			<label class="control-label">数据说明：</label>
			<div class="controls">				
				<form:textarea path="chndescription" id="chndescription" htmlEscape="false" cssStyle="width:500px;height:100px;"/>
			</div>
		</div>
			<div class="control-group">
				<label class="control-label">时间序列：</label>
				<div class="controls">
					<form:input path="timeseq" id="timeseq" htmlEscape="false"  cssStyle="width:350px;"  />
				</div>
			</div>

<%--		<div id="imageurl_IMG" class="control-group">--%>
<%--						<label class="control-label">示例图片：</label>--%>
<%--						<div class="controls">	--%>
<%--						<img  src="">--%>
<%--					</div>--%>
<%--					</div>--%>
<%--	      <div class="control-group">--%>
<%--				<label class="control-label">上传示例图片：</label>--%>
<%--				<div class="controls">--%>
<%--					<div id="iconbox" style="display:inline;">--%>
<%--					 <input type="file" id="imageurl1" name="imageurl1" onchange="imgUpload(this)"/><span id="imageurl1_Msg"></span>--%>
<%--					</div>				--%>
<%--				</div>	 --%>
<%--			</div>--%>
<%--		      <div class="control-group">--%>
<%--				<label class="control-label">资料数量：</label>--%>
<%--				<div class="controls">--%>
<%--					<form:input path="datacount" id="datacount" htmlEscape="false" cssStyle="width:350px;"/>--%>
<%--				</div>--%>
<%--		   </div>--%>
<%--					<div id="iconurl_IMG" class="control-group">--%>
<%--						<label class="control-label">图标：</label>--%>
<%--						<div class="controls">	--%>
<%--						<img  src="">--%>
<%--					</div>--%>
<%--					</div>--%>
<%--			&lt;%&ndash; 	</c:if> &ndash;%&gt;--%>
<%--		      <div class="control-group">--%>
<%--					<label class="control-label">上传图标：</label>--%>
<%--					<div class="controls">--%>
<%--						<div  style="display:inline;">--%>
<%--						  <input type="file" id="iconurl2" name="iconurl2" onchange="imgUpload(this)"/><span id="iconurl_Msg"></span>--%>
<%--						</div>				--%>
<%--					</div>	 --%>
<%--				</div>--%>
<%--					<div id="largeiconurl_IMG" class="control-group">--%>
<%--						<label class="control-label">大图标：</label>--%>
<%--						<div class="controls">	--%>
<%--						<img  src="">--%>
<%--					</div>--%>
<%--					</div>--%>
<%--		      <div class="control-group">--%>
<%--					<label class="control-label">上传大图标：</label>--%>
<%--					<div class="controls">--%>
<%--						<div  style="display:inline;">--%>
<%--						  <input type="file" id="largeiconurl3" name="largeiconurl3" onchange="imgUpload(this)"/><span id="largeiconurl_Msg"></span>--%>
<%--						</div>				--%>
<%--					</div>	 --%>
<%--				</div>--%>
<%--				&lt;%&ndash; <c:if test="${!empty dataCategoryDef.middleiconurl}"> &ndash;%&gt;--%>
<%--					<div id="middleiconurl_IMG"  class="control-group">--%>
<%--						<label class="control-label">中图标：</label>--%>
<%--						<div class="controls">	--%>
<%--						<img src="">--%>
<%--					</div>--%>
<%--					</div>--%>
<%--				&lt;%&ndash; </c:if> &ndash;%&gt;--%>
<%--				--%>
<%--		      <div class="control-group">--%>
<%--					<label class="control-label">上传中图标：</label>--%>
<%--					<div class="controls">--%>
<%--						<div  style="display:inline;">--%>
<%--						  <input type="file" id="middleiconurl4" name="middleiconurl4" onchange="imgUpload(this)"/><span id="middleiconurl_Msg"></span>--%>
<%--						</div>				--%>
<%--					</div>	 --%>
<%--			</div>--%>

		</div>
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="orderno" id="orderno" htmlEscape="false" cssStyle="width:350px;"/>
			</div>
		</div>
		  <div class="control-group"> 
		     <div class="controls">		
			   <input id="btnSubmit" class="btn btn-primary" type="submit" value="提交"/>
	           <input id="btnCancel" class="btn" type="button" value="取消" onclick="history.go(-1)"/>
		      </div>
		   </div>	 
			</div>
</form:form>
</body>
</html>

