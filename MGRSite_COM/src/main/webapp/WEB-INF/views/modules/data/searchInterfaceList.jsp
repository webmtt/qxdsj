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

}</style>
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
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="">数据接口列表</a></li>
	</ul>
	<tags:message content="${message}"/>
	<form:form id="form1" name="form1" modelAttribute="searchInterface" action="${ctx}/searchInterface/searchInterfaceList?datacode=${datacode}" method="post" class="breadcrumb form-search">
	  <div align="left">
			<input type="hidden" id="interfacesetcode" name="interfacesetcode" value="${interfacesetcode}">
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
			  <a href="${ctx}/searchInterface/addInterface?datacode=${datacode}&type=1"><input class="btn btn-primary" style="width:100px;margin-left: 12px;" value="新增接口类型" /></a>
			  <a href="javascript:void(0)" onclick="addInterfaceType()"><input class="btn btn-primary" style="width:60px;margin-left: 12px;" value="新增接口" /></a>
	   </div>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
		<thead>
  			<th width="3%">序号</th>
  			<th width="29%">检索条件编码列表</th>
    		<th width="29%">接口名称</th>  
    		<th width="29%">接口描述</th>  
    		<th width="10%">操作</th>
  		</thead>
  		<tbody class="fs_tbody">		
  			<c:forEach items="${list}" var="searchInterfaceDef" varStatus="status">
				<tr>
					<td>${status.index+1}</td>
					<td>${searchInterfaceDef.searchcodelist}</td>
					<td>${searchInterfaceDef.interfacename}</td>
					<td>${searchInterfaceDef.interfacedesc}</td>
					<td>
						<a href="${ctx}/searchInterface/editInterface?id=${searchInterfaceDef.id}&datacode=${datacode}">修改</a>
						<c:if test="${searchInterfaceDef.invalid==0}">
						      <a href="${ctx}/searchInterface/deleteInterface?id=${searchInterfaceDef.id}&datacode=${datacode}" onclick="return confirmx('确认要更新成无效？', this.href)">无效</a>
						</c:if>
						<c:if test="${searchInterfaceDef.invalid==1}">
						      <a href="${ctx}/searchInterface/deleteInterface?id=${searchInterfaceDef.id}&datacode=${datacode}" onclick="return confirmx('确认要更新成有效？', this.href)">有效</a>
						</c:if>
						
					</td>
				</tr>
			</c:forEach>
  		</tbody>
	</table>
</body>
</html>