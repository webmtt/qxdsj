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
        url:'${ctx}/portalData/imgUpload',  
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
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/portalData/portalDataCategoryList">数据列表</a></li>
		 <li class="active"><a href="${ctx}/portalData/editPortalData">编辑数据</a></li> 
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="portalDataCategoryDef" action="${ctx}/portalData/savePortalData" method="post" class="form-horizontal" >
  		<form:hidden path="id" />
  		<form:hidden path="layer" />
  		<form:hidden path="imageUrl" />
		 <c:choose>
        	<c:when test="${empty pid}">
       	      
        	</c:when>
        	<c:otherwise>
        		  <c:choose>
        			<c:when test="${pid eq '0'}">
        			<div class="control-group">
				        <label class="control-label">父节点：</label>
							<div class="controls">	
						   		<tags:treeselect id="pid" name="treeNodeId" value="${portalDataCategoryDef.parent.id}" labelName="treeNodeName" labelValue="${portalDataCategoryDef.parent.chnName}"
								title="数据服务菜单" url="/portalData/getPortalDataTreeList"  extId=""/>
							</div>
						</div>
        			</c:when>
        			<c:otherwise> 
        				 <div class="control-group">
				        <label class="control-label">父节点：</label>
							<div class="controls">	
						   		<tags:treeselect id="pid" name="treeNodeId" value="${portalDataCategoryDef.parent.id}" labelName="treeNodeName" labelValue="${portalDataCategoryDef.parent.chnName}"
								title="数据服务菜单" url="/portalData/getPortalDataTreeList"  extId=""/>
							</div>
						</div>	
        		  </c:otherwise>
        		</c:choose>  
        	</c:otherwise>
        </c:choose>	
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<form:input path="chnName" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">中文短名称:</label>
			<div class="controls">
				<form:input path="shortChnName" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">链接地址:</label>
			<div class="controls">
				<form:input path="linkurl" htmlEscape="false" maxlength="100"
						/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">跳转类型:</label>
			<div class="controls">
				<form:radiobuttons path="showType" items="${fns:getDictList('showType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">描述:</label>
			<div class="controls">
				<form:textarea path="chnDescription" htmlEscape="false" rows="3"
						maxlength="500"  class="input-xlarge" style="width:406px;"/>
			</div>
		</div>
		<c:if test="${!empty portalDataCategoryDef.imageUrl}">
					<div id="imageUrl_IMG" class="control-group">
						<label class="control-label">示例图片：</label>
						<div class="controls">	
						<img src="${imgUrl}${portalDataCategoryDef.imageUrl}">
					</div>
					</div>
				</c:if>
		<div class="control-group">
			<label class="control-label">上传大类图标：</label>
			<div class="controls">
				<div  style="display:inline;">
				  <input type="file" id="imageUrl1" name="imageUrl1" onchange="imgUpload(this)"/><span id="imageUrl_Msg"></span>
				</div>				
			</div>	 
		</div>
		<div class="control-group">
			<label class="control-label">序号:</label>
			<div class="controls">
				<form:input path="orderNo" htmlEscape="false" maxlength="50"
						class="required" name="orderNo" />
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
  </form:form>
</body>
</html>