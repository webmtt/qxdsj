<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>通配字典表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}
.typeName  {
background-color: #3daae9;
background-image: linear-gradient(to bottom, #46aeea, #2fa4e7);
background-repeat: repeat-x;
border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
border-radius: 4px;
color: #ffffff;
display: inline-block;
font-size: 14px;
height: 21px;
line-height: 20px;
margin-left: 25px;
text-align: center;
text-decoration: none;
text-shadow: 0 0 0 rgba(0, 0, 0, 0.25);
vertical-align: middle;
}
.contentTable{display:none;}
</style>
<script type="text/javascript">	
var ctx="${ctx}";
function page(n,s){
	var interfacesetcode=$("#DataSubclass option:checked").val();
	$("#interfacesetcode").val(interfacesetcode);
	$("#form1").submit();
	return false;
}
$(function(){
	var interfacesetcode='${interfacesetcode}';
	/*
	$("[name = checkbox]:checkbox").each(function () {
        if ($(this).val()==interfacesetcode) {
        	$(this).attr("checked", true);
        	$(this).parent().parent().css({"color":"red"});
        	return false;
           }
    });
	*/
})
function interfaceBind(){
	var datacode='${datacode}';
	var chk=$('input[name="checkbox"]:checked');
	if(chk.length==1){
		if(confirm("确定要绑定吗？")){
			var url="${ctx}/searchInterface/searchInterfaceBind?datacode="+datacode+"&interfacesetcode="+chk[0].value;
			window.location=url;
 		}
	}else{
		alert("请选择一个接口！");
	}
}
function changeDataSub(){
	page();
}
function addInterfaceType(){
	var datacode="${datacode}";
	var interfacesetcode=$("#DataSubclass option:checked").val();
	var href="${ctx}/searchInterface/addInterface?datacode="+datacode+"&type=0&interfacesetcode="+interfacesetcode;
	window.location.href=href;
}
function updateSort(){
	var interfacesetcode=$("#DataSubclass option:checked").val();
	var datacode="${datacode}";
	$.ajax({
		url : "${ctx}/searchInterface/InterfaceBind",
		type : "post",
		dataType : "json",
		async : true,
		data : {
			"interfacesetcode" : interfacesetcode,
			"datacode" : datacode
		},
		success : function(result) {
			var status=result.status;
			if(status=="0"){
				alert("选定失败！");
				parent.closeModal2();
			}else if(status=="1"){
				alert("选定成功！");
				parent.closeModal2();
			}
			
		}
	});
}
function closeWin(){
	 parent.closeModal2();
}
function opendetail(){
	if($("#contentTable").hasClass("contentTable")){
		$("#contentTable").removeClass("contentTable");
	}else{
		$("#contentTable").addClass("contentTable");
	}
	
}
</script>
</head>

<body>

	<ul class="nav nav-tabs">
	</ul>
	<tags:message content="${message}"/>
	<form:form id="form1" name="form1" modelAttribute="searchInterface" action="${ctx}/searchInterface/searchInterfaceCoList?datacode=${datacode}" method="post" class="breadcrumb form-search">
	  <div align="left">
			<input type="hidden" id="interfacesetcode" name="interfacesetcode" value="${interfacesetcode}">
			<label>选择接口类型：</label>
			<select id="DataSubclass"  name="DataSubclass" onChange="changeDataSub(this)">
						<c:forEach items="${namelist}" var="obj">
						    <c:if test="${interfacesetcode==obj[0]}">
						        <option value="${obj[0]}" type="${obj[0]}" selected = "selected">${obj[1]}</option>
						    </c:if>
						    <c:if test="${interfacesetcode!=obj[0]}">
						        <option value="${obj[0]}" type="${obj[0]}">${obj[1]}</option>
						    </c:if>
						</c:forEach>
			</select>
			 <a style="margin-left:30px;"><input  class="btn btn-primary" type="button" value="查看详情" onclick="opendetail();"/></a>
	   </div>
	</form:form>
			<table id="contentTable"  class="table table-striped table-bordered table-condensed contentTable" style="word-break:break-all; word-wrap:break-word;">
				<thead>
		  			<th width="4%">序号</th>
		  			<th width="22%">检索条件编码列表</th>
		    		<th width="22%">接口名称</th>  
		    		<th width="22%">接口描述</th>  
		  		</thead>
		  		<tbody class="fs_tbody">		
		  			<c:forEach items="${list}" var="searchInterfaceDef" varStatus="status">
						<tr>
							<td>${status.index+1}</td>
							<td>${searchInterfaceDef.searchcodelist}</td>
							<td>${searchInterfaceDef.interfacename}</td>
							<td>${searchInterfaceDef.interfacedesc}</td>
						</tr>
					</c:forEach>
		  		</tbody>
			</table>
			<div class="form-actions pagination-left" style="text-align:center;">
					<input id="btnSubmit" class="btn btn-primary" type="button" value="保存" onclick="updateSort();"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  id="btnCancel" class="btn" type="button" value="关闭" onclick="closeWin();"/>
			</div>
</body>
</html>