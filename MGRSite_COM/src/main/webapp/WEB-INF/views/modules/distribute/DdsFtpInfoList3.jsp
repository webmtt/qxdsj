<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>   
    <title>调度任务配置</title>
    <meta name="decorator" content="default" />
	<%@include file="/WEB-INF/views/include/dialog.jsp"%>
	<style type="text/css">
	.sort {
	color: #0663A2;
	cursor: pointer;
	}
	</style>
	<script type="text/javascript">	
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		
		$("#form1").submit();
    	return false;
    }
	$(function(){
		var chk_value = $("#chk_value").val().split("#");
		for(var i=0; i<chk_value.length; i++){
			$("input:checkbox[value='"+chk_value[i]+"']").attr('checked','true');
		}
	})
	function saveChose() {
		var chk_value =[]; 
		$('input[name="ids"]:checked').each(function(){ 
		chk_value.push($(this).val()); 
		}); 
		if(chk_value.length==0){
			alert("请选择ftp地址！");
		}else{
			$("#chk_value").val(chk_value);
			loading('正在提交，请稍等...');
			$("#listForm").attr("action", "${ctx}/dt/ddsPushConfInfo/addDdsPushConfInfo");
			$("#listForm").submit();
		}
	}
	
	</script>
  </head>
  
  <body>
  <ul class="nav nav-tabs">
        <li><a href="${ctx}/dt/distribute/list">分发定义列表</a></li>
		<li ><a href="${ctx}/dt/ddsFtpInfo/flist2?dataId=${dataId}">ftp地址列表</a></li>
		 <li   class="active"><a href="${ctx}/dt/ddsFtpInfo/flist3?dataId=${dataId}">添加ftp地址</a></li> 
	</ul>
	<tags:message content="${message}"/>
		<form id="listForm" method="post">
	<input id="chk_value" name="chk_value" type="hidden"
		value="${chk_value}" />
		<input id="dataId" name="dataId" type="hidden"
		value="${dataId}" />
   	<div>
   		<table id="tablelist" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
   			<thead>
   				<th style="text-align: center;">ftp地址ID</th>
   				<th style="text-align: center;">ftp地址Ip</th>
   				<th style="text-align: center;">ftp地址端口</th>
   				<th style="text-align: center;">ftp用户名</th>
   				<th style="text-align: center;">ftp密码</th>
   				<th style="text-align: center;">操作</th>
   			</thead>
   			<tbody>
   				<c:forEach items="${list}" var="col">
   					<tr>
   						<td>${col.ftpId}</td>
   						<td>${col.ftpIp}</td>
   						<td>${col.ftpPort}</td>
   						<td>${col.ftpUser }</td>
   						<td>${col.ftpPwd}</td>
   						<td style="width: 10%;text-align: center;"> 
						    <input type="checkbox" name="ids" value="${col.ftpId }" />
						</td>
   					</tr>
   				</c:forEach>
   			</tbody>
   		</table>
   	</div>
   		<div class="form-actions pagination-left">
		<input id="btnSubmit" class="btn btn-primary" type="button" value="添加" onclick="saveChose();"/>
	</div>
  	</form>
  </body>
</html>
