<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新增物品</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#attach").hide();
});
function check(){
  	/* alert("1"); */
	var chnName=$("#chnName").val();
	var layerDescription=$("#layerDescription").val();
	var linkURL=$("#linkURL").val();
	var orderNo=$("#orderNo").val();
	 var reg = new RegExp("^[0-9]*$");  
	if(""==chnName||null==chnName){
		alert("名称不能为空");
		return false;
	}else if(""==layerDescription||null==layerDescription){
		alert("描述不能为空");
		return false;
	
	}else if(""==linkURL||null==linkURL){
		alert("链接地址不能为空");
		return false;
	}else if(""==orderNo||null==orderNo){
		alert("序号不能为空");
		return false;
	}else if(!reg.test(orderNo)){
		alert("请在序号栏输入0~65534数字");
		return false;
		
	}else{
		return true;
	}
}
function checkupload(){
	var file=$("#fileToUpload").val();
	if(""==file||null==file){
		alert("请选择要上传的文件");
		return false;
	}else{
		var last=file.split(".");					
		fileUpload();
		return true;
	}	
}
function fileUpload(){
	var area='${area}';
	var file=$("#fileToUpload").val();
	
	if(""==file||null==file){
		$("#msg").text("请选择要上传的文件");
		return false;
	}else{
		$.ajaxFileUpload({  
            url:'${ctx}/column/columnarrange/saveAttach',  
            secureuri:false,  
            fileElementId:'fileToUpload',//file标签的id  
            dataType: 'json',//返回数据的类型  
            data:{'area':area},//一同上传的数据  
            success: function (data, status) {  
                $("#id").attr("value",data.id);
               /*  $("#msg").text(data.message); */
               alert(data.message);
               var list=data.list;
               var fileName=data.fileName;
               $("#columnItemIDs").attr("value",list.columnItemID);
                $("#flag").attr("value","1");
               	$("#attach").show();
               	$("#attachList").empty();
               	var html="<thead style='text-align:center;'><th>序号</th><th>文件名</th><th>操作</th></thead><tbody>";
               
               			html+="<tr id="+area+"><td style='width: auto;'>"+1+"</td><td style='width: auto;'>"+fileName+"</td>";	   
                   		html+="</td><td style='width: auto;'>";
                   		html+="<a href='javascript:void(0)' onclick='delAttach(\""+area+"\",\""+list.columnImageURL+"\",\""+(list.columnItemID)+"\")'>删除</a>";
                   		html+="</td></tr>";
               		
               
               	$("#attachList").html(html);	
               	$("#filebox").empty();
               	$("#filebox").html("<input type='file' id='fileToUpload' name='file'>");
               	$("#upload").hide();
            },  
            error: function (data, status, e) {   
                $("#flag").attr("value","上传失败");  
                alert("上传文件大小超过${maxUploadSize}M，请联系管理员上传！"); 
                //这里处理的是网络异常，返回参数解析异常，DOM操作异常  
              /*   alert("上传发生异常");  */ 		           
            }  
        });  	
		return true;
	}
	 
} 
function delAttach(area,columnImageURL,columnItemID){
	$.ajax({
		url:'${ctx}/column/columnarrange/delById',
		type:'GET',
		data:{ID:area,docUrl:columnImageURL,docId:columnItemID},
		dataType:'json',
		success:function(result){
			if(result.flag=='true'){
				$("#"+result.ID).hide();
				alert("删除附件成功");
				$("#attach").hide();
				$("#upload").show();
			}else if(result.flag=='false'){
     			alert("删除附件失败");
			}
				
		},
	});
}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/column/columnarrange/columnList?area=${area}">专栏列表</a></li>
		 <li class="active"><a href="${ctx}/column/columnarrange/columnAdd?area=${area}">新增专栏</a></li> 
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="column" action="${ctx}/column/columnarrange/Savecolumn?area=${area}" method="post" class="form-horizontal" onsubmit="return check()">
  		<form:hidden path="areaItem" />
  		<input type="hidden" name="areaItems" value="${area}"/>
  		<form:hidden path="columnItemID" />
  		<input type="hidden" id="columnItemIDs" name="columnItemIDs" value="">
  		<div class="control-group">
			<label class="control-label">序号:</label>
			<div class="controls">
				<form:input path="orderNo" htmlEscape="false" maxlength="50"
						class="required" name="orderNo" />
			</div>
		</div>
  		
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<form:input path="chnName" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">层次描述:</label>
			<div class="controls">
				<form:textarea path="layerDescription" htmlEscape="false" rows="3"
						maxlength="50"  class="input-xlarge" style="width:206px;"/>
			</div>
		</div>
		<div class="control-group" id="attach">
			<label class="control-label">附件列表:</label>
			<div class="controls">
				<div style="display: inline-block;">
<!-- 				<table id="attachList"  style="width: auto;" border="1">				 -->
				<table id="attachList"  class="table table-striped table-bordered table-condensed"
		style="word-break:break-all; word-wrap:break-word;">				
				</table>
				</div>
				<div style="display: inline-block;margin-bottom: 0px;padding-bottom: 0px;">
				<span id="msg" style="color: red;"></span>
				</div>
			</div>			
		</div>
		<div class="control-group" id="upload">
			<label class="control-label">添加图片</label>
			<div class="controls">
				<div id="filebox" style="display:inline;">
				<input type="file" id="fileToUpload" name="file">
				</div>
				<input type="button" value="上传" onclick="return checkupload()" ><span style="color:red;">上传文件大小不得超过${maxUploadSize}M</span>
				<span id="docurl"></span>
			</div>	 
		</div>
		
		
		<div class="control-group">
			<label class="control-label">是否显示状态:</label>
			<div class="controls">
			   <form:radiobuttons path="invalid" name="invalid" items="${fns:getDictList('invalid')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">类型:</label>
			<div class="controls">
					<form:select path="columnType">
						<form:options items="${fns:getDictList('columnType')}"
							itemLabel="label" itemValue="value"
							htmlEscape="false" />
					</form:select>
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">链接地址:</label>
			<div class="controls">
					<form:textarea path="linkURL" htmlEscape="false" rows="3"
						maxlength="50" class="input-xlarge" style="width:206px;"/>
			</div>
			</div>
		
		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
  </form:form>
</body>
</html>