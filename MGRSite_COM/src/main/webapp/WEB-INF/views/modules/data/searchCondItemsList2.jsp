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
$(function(){
})
function change(){
	var value = $("#searchCondCfg option:checked").val();
	var pid=$("#pid").val();
	page(value,pid);
}
function page(value,pid) {
	$("#form1").attr(
			"action",
			"${ctx}/searchCondCfg/SearchCondItemslist2?itemtype=" + value+"&pid="+pid );
	$("#form1").submit();
	return false;
}
function interfaceBind(){
	var itemtype='${itemtype}';
	var pid='${pid}';
	var chk=$('input[name="checkbox"]:checked');
	var chk_value ="";
	if(chk.length>0){
		for(var i=0;i<chk.length;i++){
			if(i==chk.length-1){
				chk_value=chk_value+chk[i].value;
			}else{
				chk_value=chk_value+chk[i].value+",";
			}
			
		}
		if(confirm("确定要绑定吗？")){
			var url="${ctx}/searchCondCfg/searchCondCfgBind?pid="+pid+"&itemtype="+itemtype+"&defaultvalue="+chk_value;
			window.location=url;
 		}
	}else{
		alert("请选择一个默认值！");
	}
}
</script>
</head>

<body>

	<ul class="nav nav-tabs">
	    <li><a href="${ctx}/searchCondCfg/SearchCondItemslist?itemtype=${itemtype}&pid=${pid}">字典表选定维护</a></li>
		<li class="active"><a href="">条件字典列表</a></li>
	</ul>
	<tags:message content="${message}"/>
	<input id="pid" name="pid" type="hidden" value="${pid}"/>
	<form:form id="form1" name="form1" modelAttribute="searchCondCfg" action="${ctx}/searchCondCfg/SearchCondItemslist&id=${id}" method="post" class="breadcrumb form-search">
		<div align="left">
			<label>字典类型：</label>
			<select id="searchCondCfg" name="searchCondCfg" onChange="change(this)">
				<c:forEach items="${alist}" var="searchCondCfg" varStatus="status">
				   <c:if test="${searchCondCfg.searchconfigcode==itemtype}">
				      <option value="${searchCondCfg.searchconfigcode}" selected = "selected">${searchCondCfg.chndescription}</option>
				   </c:if>
				   <c:if test="${searchCondCfg.searchconfigcode!=itemtype}">
				      <option value="${searchCondCfg.searchconfigcode}">${searchCondCfg.chndescription}</option>
				   </c:if>
				</c:forEach>
			</select>
				<a style="margin-left:10px;" href="${ctx}/searchCondCfg/toAdd?itemtype=${itemtype}&pid=${pid}"><input class="btn btn-primary" type="button" value="新增字典类型"/></a>
			    <a style="margin-left:10px;" href="${ctx}/searchCondCfg/AddSearchCondItems?itemtype=${itemtype}&pid=${pid}"><input class="btn btn-primary" type="button" value="新增字典详情"/></a>
			    <a style="margin-left:10px;" href="${ctx}/searchCondCfg/sortSearchCondItems?itemtype=${itemtype}&pid=${pid}"><input class="btn btn-primary" type="button" value="字典详情排序"/></a>
		</div>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
		<thead>
  			<th>序号</th>
    		<th>项名称</th>  
    		<th>项值</th>
    		<th>排序号</th> 
    		<th>操作</th>
  		</thead>
  		<tbody class="fs_tbody">		
  			<c:forEach items="${list}" var="searchCondItems" varStatus="status">
				<tr>
					<td>${status.index+1}</td>
					<td>${searchCondItems.itemcaption}</td>
					<td>${searchCondItems.itemvalue}</td>
					<td>${searchCondItems.orderno }</td>
					<td>
						<a href="${ctx}/searchCondCfg/updateSearchCondItems?id=${searchCondItems.id}&pid=${pid}">修改</a>
						<c:if test="${searchCondItems.invalid=='0' }">
						     <a href="${ctx}/searchCondCfg/deleteCondItems?id=${searchCondItems.id}&invalid=${searchCondItems.invalid}&itemtype=${searchCondItems.itemtype}&pid=${pid}" onclick="return confirmx('要重置无效吗？', this.href)" >无效</a>
						</c:if> 
						<c:if test="${searchCondItems.invalid=='1' }">
						     <a href="${ctx}/searchCondCfg/deleteCondItems?id=${searchCondItems.id}&invalid=${searchCondItems.invalid}&itemtype=${searchCondItems.itemtype}&pid=${pid}" onclick="return confirmx('要重置有效吗？', this.href)" >有效</a>
						</c:if> 
					</td>
				</tr>
			</c:forEach>
  		</tbody>
	</table>
</body>
</html>