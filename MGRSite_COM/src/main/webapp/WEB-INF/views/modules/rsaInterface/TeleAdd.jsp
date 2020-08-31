<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新增会商</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#attach").hide();
});
function check(){
  	/* alert("1"); */
	var date=$("#MeetingDate").val();
	var begin=$("#BeginTime").val();
	var end=$("#EndTime").val();
	var content=$("#Content").val();
	var MeetTitle=$("#MeetTitle").val();
	var OrganizationUni=$("#OrganizationUni").val();
	var Contacts=$("#Contacts").val();
	var ContactWay=$("#ContactWay").val();
	var MeetRange=$("input[name=MeetRange]").attr("checked");
	var MeetingDate=$("#MeetingDate").val();
	if(""==date||null==date){
		alert("日期不能为空");
		return false;
	}else if(""==begin||null==begin){
		alert("开始时间不能为空");
		return false;
	}else if(""==end||null==end){
		alert("结束时间不能为空");
		return false;
	}else if(end<=begin){
		alert("结束时间不能小于或等于开始时间");
		return false;
	}else if(""==MeetTitle||null==MeetTitle){
		alert("会议名称不能为空");
		return false;
	}else if(""==content||null==content){
		alert("内容不能为空");
		return false;
	}else if(""==MeetRange||null==MeetRange||"undefined"==MeetRange){
		alert("会议召开范围不能为空");
		return false;
	}else if(""==OrganizationUni||null==OrganizationUni){
		alert("组织单位不能为空");
		return false;
	}else if(""==Contacts||null==Contacts){
		alert("联系人不能为空");
		return false;
	}else if(""==ContactWay||null==ContactWay){
		alert("联系方式不能为空");
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
	var id=$("#MeetingId").val();
	var file=$("#fileToUpload").val();
	if(""==file||null==file){
		$("#msg").text("请选择要上传的文件");
		return false;
	}else{
		$.ajaxFileUpload({  
            url:'${ctx}/tvmeeting/meetingarrange/saveAttach',  
            secureuri:false,  
            fileElementId:'fileToUpload',//file标签的id  
            dataType: 'json',//返回数据的类型  
            data:{'id':id},//一同上传的数据  
            success: function (data, status) {  
                $("#id").attr("value",data.id);
               /*  $("#msg").text(data.message); */
               alert(data.message);
                $("#flag").attr("value","1");
               	$("#attach").show();
               	$("#attachList").empty();
               	var html="<thead style='text-align:center;'><th>序号</th><th>文件名</th><th>操作</th></thead><tbody>";
               	$.each(data.list,function(i,n){
               		html+="<tr id="+n.id+"><td style='width: auto;'>"+(i+1)+"</td><td style='width: auto;'>"+n.docName+"</td>";	   
               		html+="</td><td style='width: auto;'>";
               		html+="<a href='javascript:void(0)' onclick='delAttach(\""+n.id+"\",\""+(n.docUrl)+"\",\""+(n.docId)+"\")'>删除</a>";
               		html+="</td></tr>";
               	});
               	$("#attachList").html(html);	
               	$("#filebox").empty();
               	$("#filebox").html("<input type='file' id='fileToUpload' name='file'>");
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
function delAttach(id,docUrl,docId){
	$.ajax({
		url:'${ctx}/tvmeeting/meetingarrange/delById',
		type:'GET',
		data:{ID:id,docUrl:docUrl,docId:docId},
		dataType:'json',
		success:function(result){
			if(result.flag=='true'){
				$("#"+result.ID).hide();
				alert("删除附件成功");
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
		<li><a href="${ctx}/tvmeeting/meetingarrange/teleList">会商列表</a>
		<li class="active"><a href="${ctx}/tvmeeting/meetingarrange/teleAdd">新增会商</a></li>
		<li><a href="${ctx}/tvmeeting/meetingarrange/teleBatchAdd">批量新增会商</a></li>
		</li>
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="planDefine" action="${ctx}/tvmeeting/meetingarrange/teleSave" method="post" class="form-horizontal" onsubmit="return check()">
  		<form:hidden path="MeetingId" />
  		<div class="control-group">
			<label class="control-label">日期:</label>
			<div class="controls">
				<input type="text" class="Wdate" id="MeetingDate" name="MeetingDate" placeholder="请选择日期" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>			
			</div>
		</div>
	    <div class="control-group">
			<label style="margin-left: 130px;">开始时间:</label>
			<div style="display: inline;width:30px;">
				<input type="text" class="Wdate" id="BeginTime" name="BeginTime" style="width: 60px;" onfocus="WdatePicker({dateFmt:'HH:mm'})"/>
			</div>
			结束时间:
			<div style="display: inline;">
				<input type="text" class="Wdate" id="EndTime" name="EndTime" style="width: 60px;" onfocus="WdatePicker({dateFmt:'HH:mm'})"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会议名称:</label>
			<div class="controls">
				<form:input path="MeetTitle" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">内容:</label>
			<div class="controls">
				<form:textarea path="Content" htmlEscape="false" rows="3"
						maxlength="500" class="input-xlarge" />
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
			<label class="control-label">添加文件</label>
			<div class="controls">
				<div id="filebox" style="display:inline;">
				<input type="file" id="fileToUpload" name="file">
				</div>
				<input type="button" value="上传" onclick="return checkupload()" ><span style="color:red;">上传文件大小不得超过${maxUploadSize}M</span>
				<span id="docurl"></span>
			</div>	 
		</div>
		<div class="control-group">
			<label class="control-label">会议级别:</label>
			<div class="controls">
					<form:select path="MeetLevel">
						<form:options items="${fns:getDictList('meetLevel')}"
							itemLabel="label" itemValue="value"
							htmlEscape="false" />
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会议分类:</label>
			<div class="controls">
				<form:select path="MeetType">
						<form:options items="${fns:getDictList('meetType')}"
							itemLabel="label" itemValue="value"
							htmlEscape="false" />
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会议主题:</label>
			<div class="controls">
				<form:select path="MeetTheme">
						<form:options items="${fns:getDictList('meetTheme')}"
							itemLabel="label" itemValue="value"
							htmlEscape="false" />
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会议地点:</label>
			<div class="controls">
				<form:select path="MeetArea">
						<form:options items="${fns:getDictList('meetArea')}"
							itemLabel="label" itemValue="value"
							htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会议召开范围:</label>
			<div class="controls">
				<c:forEach items="${fns:getDictList('meetRange')}" var="list">
					<input type="checkbox" name="MeetRange" value="${list.value}" />${list.label}
			    </c:forEach>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否有双流:</label>
			<div class="controls">
			   <form:radiobuttons path="IsDouble" items="${fns:getDictList('isDouble')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">组织单位:</label>
			<div class="controls">
				<form:select path="OrganizationUni">
						<form:options items="${fns:getDictList('organize')}"
							itemLabel="label" itemValue="value"
							htmlEscape="false" />
				</form:select>
			</div>
		</div>
		
	<!--  	
		
		<div class="control-group">
			<label class="control-label">组织单位:</label>
			<div class="controls">
				<form:input path="OrganizationUni" htmlEscape="false" maxlength="50" class="required" />
			</div>
		</div>
		-->
		<div class="control-group">
			<label class="control-label">联系人:</label>
			<div class="controls">
				<form:input path="Contacts" htmlEscape="false" maxlength="50" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系方式:</label>
			<div class="controls">
				<form:input path="ContactWay" htmlEscape="false" maxlength="50" class="required" />
			</div>
		</div>		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
  </form:form>
</body>
</html>