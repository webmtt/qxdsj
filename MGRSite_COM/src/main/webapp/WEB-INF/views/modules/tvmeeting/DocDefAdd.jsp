<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script type="text/javascript">
	$(document).ready(function() {
		var errorInfo='${errorInfo}';
		if(errorInfo!=""){
			alert(errorInfo);
		}		
		var flag='${flag}';
		if("add"==flag){
			$("#flag").text("技术文档新增");
			$("#isShow").show();
			$("#upload").val("保存");
		}else if("update"==flag){
			$("#flag").text("技术文档修改");
			$("#isShow").hide();
			$("#upload").val("修改");
		}
		
		//文件上传
		$("#upload").click(function(){			
			var docId=$("#docId").val();
			var Strname = $("input[name='file']").val();
			var str2 = $("input[name='docName']").val();
			if("保存"==$("#upload").val()){
				if(Strname!=null&&Strname!=""&&str2!=null&&str2!=""){
					$("#form1").attr("action","${ctx}/tvmeeting/techdoc/upload?docDefName="+str2+"");
					$("#form1").submit();	
				}else if (Strname=="") {
					alert("请选择一个文件");
					return false;
				}else if (str2=="") {
					alert("请输入报表名称");
				}		
			}else if("修改"==$("#upload").val()){
				if(str2!=null&&str2!=""){
					$("#form1").attr("action","${ctx}/tvmeeting/techdoc/update?docId='"+docId+"'&docDefName="+str2+"");
					$("#form1").submit();	
				}else if (str2=="") {
					alert("请输入报表名称");
				}		
			}
		
		});
		
		$("#docName").blur(function(){
			var str = $("input[name='docName']").val();
			if (str=="") {
				alert("请输入报表名称");
				return false;  
			}
		});
	});
  </script>
  </head>
  
  <body>
  <ul class="nav nav-tabs">
 		<li><a href="${ctx}/tvmeeting/techdoc/">列表</a></li>
		<li class="active"><a id="flag" href="${ctx}/tvmeeting/techdoc/add">技术文档新增</a></li>
	</ul>
	<form id="form1"  method="post" enctype="multipart/form-data" class="breadcrumb form-search">
	    <table  align="center" class="table table-striped table-bordered table-condensed"  style="word-break:break-all; word-wrap:break-word;">
			<div class="control-group" style="width:70%;float:none">
				<label class="control-label">文档名称</label>
				<div class="controls">
					<input type="text"  name="docName" id="docName" value="${docName}" placeholder="请填写..." 
					maxlength="50" minlength="3" class="required" style="width: 320px;">
				</div>
			</div>
			<div id="isShow" class="control-group" style="width:70%;float:none;">
				<input type="file" name="file" id="file" value="${docDefUrl }" style="width: 330px;"/>
			</div>
			<input type="hidden" id="docId" value="${docId }"/>
			<div class="form-actions" align="left">		
				<input type="button" class="btn btn-primary" id="upload" value="保存"/>&nbsp;
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>			
			</div>
		</table>		
	</form>
  </body>
</html>
