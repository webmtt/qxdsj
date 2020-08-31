<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>列表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">
.table td i{margin:0 2px;}
.active{
	display: list-item;
    text-align: -webkit-match-parent;
}
</style>
<script type="text/javascript">	
$(document).ready(function() {
	$("#treeTable").treeTable({expandLevel : 3});
	/* var chk_value = $("#emtype").val().split(",");
	for(var i=0; i<chk_value.length; i++){
		$("input:checkbox[value='"+chk_value[i]+"']").attr('checked','true');
	} */
  
	
});
/* function page(n,s){
		var nation= $("input[name='province'] checked:checked").val();
 		var province=$("input[name='province'] checked:checked").val();
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#form1").attr("action","${ctx}/emergencyController/jsonList?province="+province+"&nation="+nation);
		$("#form1").submit();
    	return false;
    } */
function updateSort() {
	loading('正在提交，请稍等...');
	$("#listForm").attr("action", "${ctx}/portalMenuDef/updateSort");
	$("#listForm").submit();
}
/* function reloadPage(){ 
 	var province= $("input[name='province'] checked:checked").val();
 	var nation = "";
 	window.location.href="${ctx}/emergencyController/energencyList?province="+province+"&nation="+nation;
	/* $("input[name='province'] checked:checked").attr('checked','true');
	$("input[name='nation'] checked:checked").attr('checked','false'); */
	$("input[name='nation']").attr('checked','false');
	$("input[name='province']").attr('checked','true');
//}  */

function reloadPage1(){
	
 	/* var nation = $("input[id='nation'] checked:checked").val();
 	var province =$("input[id='nation1'] checked:checked").val(); */
 	/* var nation =document.getElementById("nation").val();
 	var province =document.getElementById("nation1").val(); */
 	var nation =$("#nation").val();
 	var province =$("#nation1").val();
 	var flag = document.getElementById("nation").checked;
 	var flag1 = document.getElementById("nation1").checked;
 	if(flag==true&flag1==false){
	 	$("input[id='nation']").attr('checked','true');
		/* $("input[id='nation1']").attr('checked','false'); */
		$("input[id='nation1']").removeAttr("checked");
		province ="";
		nation =$("#nation").val();
		/* page(); */
 	}else if(flag==false&flag1==true){
 		/* $("input[id='nation']").attr('checked','false'); */
 		$("input[id='nation']").removeAttr("checked");
		$("input[id='nation1']").attr('checked','true');
		nation="";
		province =$("#nation1").val();
 	}else if(flag==true&flag1==true){
 		$("input[id='nation']").attr('checked','true');
		$("input[id='nation1']").attr('checked','true');
		/* page(); */
 	}
 	else if(flag==false&flag1==false){
 		/* $("input[id='nation']").attr('checked','false');
		$("input[id='nation1']").attr('checked','false'); */
		$("input[id='nation']").removeAttr("checked");
		$("input[id='nation1']").removeAttr("checked");
		nation="";
		province ="";
 	}
 	
	 $.ajax({
		url:'${ctx}/emergencyController/jsonList?province='+province+"&nation="+nation,
		type:'get',
		dataType : "json",
		async : true,
		success : function(result) {
				/* alert(result);
				console.log(result); */
				var string ="";
				if(result.length==0){
					$("#emergency").html(string);
				}else{
					for(var i=0;i<result.length;i++){
					//alert(result[i].type);
					/* '<table id="treeTable" class="table table-striped table-bordered table-condensed">'
					+'<tr><th>来源</th><th>内容</th><th>应急省份</th><th>应急类型</th><th>应急级别</th></tr>' */
					var strs ='<tr >'
						+'<td style="text-align:left;width:10%;">'+result[i].noticeLevel+'</td>'
						+'<td style="text-align:left;width:30%;">'+result[i].emergencyInfo+'</td>'
						+'<td style="text-align:left;width:20%;">'+result[i].province+'</td>'
						+'<td  style="text-align:left;width:20%;">'
					    	if(result[i].type ==null){
					    	strs+='<img style="position: relative; margin:auto 0;" src="../static/ultra/img/cimiss/warning2.png">'
					    	}else{
					    	strs+=result[i].type;
					    	}
						strs=strs
						+'</td>'
						+'<td id="level" style="text-align:left;width:20%;">'
						/* +result[i].level+'<c:if test="result[i].level ==null"><p style="color:red;font-weight: bold;">!<p></c:if> */
						if(result[i].level ==null){
					    	strs+='<img style="position: relative; margin:auto;" src="../static/ultra/img/cimiss/warning2.png">'
					    	}else{
					    	strs+=result[i].level;
					    	}
						strs=strs
						+'</td>'
					+'</tr>';
					 string +=strs;
				
					}
					$("#emergency").html("");
					$("#emergency").append(string);
				
				}
				
				
			}
		
	}); 
	
} 
function test(){
	window.location.href="${ctx}/emergencyController/test";
}

	
</script>
</head>

<body>

	<ul id ="elist" class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0)">应急信息列表</a></li>
		<li id="getProvence"><a href="${ctx}/emergencyController/getProvence">应急省份维护</a></li>
		<li id="getType"><a href="${ctx}/emergencyController/getType">应急类型维护</a></li>
		<li id="getLevel"><a href="${ctx}/emergencyController/getLevel">应急级别维护</a></li>
	</ul>
	<tags:message content="${message}"/>
	<form:form id="form1" modelAttribute="friendLink" action="${ctx}/emergencyController/energencyList" method="post" class="breadcrumb form-search">
   		<div style=" text-align:left;">
   			来源：<input id="emtype" name="emtype" type="hidden" value="nation,province" />
   			<input onClick="reloadPage1();" checked="checked"   type="checkbox"  id="nation" name="nation" value="nation" />国家级
			<input onClick="reloadPage1();" checked="checked"   type="checkbox"  id="nation1" name="province" value="province" />省级
			<a class="btn btn-primary" href="http://idata.cma/idata/web/emergencyNotice/emergencyIndex" target="_blank">最终效果</a>			
		</div>
		
	</form:form>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>来源</th><th>内容</th><th>应急省份</th><th>应急类型</th><th>应急级别</th></tr>
			<tbody id="emergency">
			<c:forEach items="${all}" var="emergency">
				<tr >
					<td style="width: 10%;">${emergency.noticeLevel}</td>
					<td style="text-align:left;width:30%;">${emergency.emergencyInfo}</td>
					<td style="text-align:left;width:20%;">${emergency.province}</td>
						<td  style="text-align:left;width:20%;">${emergency.type}
							
							<c:if test="${emergency.type ==null}">
								<img style="position: relative; margin:auto;" src="../static/ultra/img/cimiss/warning2.png">
							</c:if>
						</td>
					<td id="level" style="text-align:left;width:20%;">${emergency.level}
						<c:if test="${emergency.level ==null}">
								<img style="position: relative; margin:auto;" src="../static/ultra/img/cimiss/warning2.png"/>
						</c:if>
					</td>
				</tr>
				
			</c:forEach>
			</tbody>
		</table>
		
			<!-- <input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="test();"/> -->
		
	 </form>
</body>
</html>