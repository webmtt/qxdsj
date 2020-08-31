<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<html>
<head>
<title>通配字典表</title>
<meta name="decorator" content="default"/>
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
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
	var defaultvalue="${defaultvalue}";
	if(defaultvalue!=null&&defaultvalue!=""){
		var values=defaultvalue.split(",");
		var objSelect =document.getElementById("searchCondItems")
		for(var i=0;i<objSelect.options.length;i++) {
			for(var j=0;j<values.length;j++){
				if(objSelect.options[i].value==values[j]){
					 objSelect.options[i].selected = true;  
				   }  
			}
        }  
	}
})
function getAllItemValuesByString(objSelectId) {
 var selectItemsValuesStr = "";
 var objSelect = document.getElementById(objSelectId);
 if (null != objSelect && typeof(objSelect) != "undefined") {
      var length = objSelect.options.length
        var temp=0;
        for(var i = 0; i < length; i = i + 1) {
          if(objSelect.options[i].selected == true){
        	  if (0 == temp) {
                  selectItemsValuesStr = objSelect.options[i].value;
               } else {
                  selectItemsValuesStr = selectItemsValuesStr + "," + objSelect.options[i].value;
               }
        	  temp=temp+1;
          }
        }   
     }  
     return selectItemsValuesStr;
}
function change(){
	var value = $("#searchCondCfg option:checked").val();
	var pid=$("#pid").val();
	page(value,pid);
}
function page(value,pid) {
	$("#form1").attr(
			"action",
			"${ctx}/searchCondCfg/SearchCondItemslist?itemtype=" + value+"&pid="+pid );
	$("#form1").submit();
	return false;
}
function reviewPass(){
	var searchCondCfg = $("#searchCondCfg option:checked").val();
	   if(confirm("确定要审核通过吗？")){
		   document.inputForm.action= '${ctx}/sys/userInfomation/reviewPass';
 	   document.inputForm.submit();
		}
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
function saveMoren(){
	var  searchAttach=$("#searchAttach").val();
	if(searchAttach==null||searchAttach==""){
		alert("请先选定字典类型！");
		return false;
	}else{
		if(confirm("确定要保存默认值吗？")){
			var searchCondItems=getAllItemValuesByString("searchCondItems");
			$("#defaultvalue").val(searchCondItems);
			var itemtype="${itemtype}";
			var pid="${pid}";
			$("#form2").attr("action","${ctx}/searchCondCfg/searchCondCfgSave?itemtype=" + itemtype+"&pid="+pid );
			$("#form2").submit();
			}
	}
}
function closeWin(){
	 parent.closeModal2();
}
</script>
</head>

<body>

	<ul class="nav nav-tabs">
	</ul>
	<tags:message content="${message}"/>
	<input id="pid" name="pid" type="hidden" value="${pid}"/>
	<input id="searchAttach" name="searchAttach" type="hidden" value="${searchAttach}"/>
	<form:form id="form1" name="form1" modelAttribute="searchCondCfg" action="${ctx}/searchCondCfg/SearchCondItemslist&pid=${pid}" 
	                  method="post" style="margin-top:30px;">
		<div align="left" style="margin-left:10px;">
			<label>字典类型：</label>
			<select id="searchCondCfg" name="searchCondCfg" onChange="change(this)" style="width:270px;">
				<c:forEach items="${alist}" var="searchCondCfg" varStatus="status">
				   <c:if test="${searchCondCfg.searchconfigcode==itemtype}">
				      <option value="${searchCondCfg.searchconfigcode}" selected = "selected">${searchCondCfg.chndescription}</option>
				   </c:if>
				   <c:if test="${searchCondCfg.searchconfigcode!=itemtype}">
				      <option value="${searchCondCfg.searchconfigcode}">${searchCondCfg.chndescription}</option>
				   </c:if>
				</c:forEach>
			</select>
			<c:if test="${empty searchAttach}">
				   <a style="margin-left:15px;" href="${ctx}/searchCondCfg/searchCondCfgBindC?itemtype=${itemtype}&pid=${pid}" onclick="return confirmx('您确定要选定选择的字典类型吗？', this.href)">
				         <input class="btn btn-primary" type="button" value="选定"/></a>
				</c:if>
				<c:if test="${not empty searchAttach}">
				   <a style="margin-left:15px;" href="${ctx}/searchCondCfg/searchCondCfgBindC?itemtype=${itemtype}&pid=${pid}" onclick="return confirmx('您确定要重新选定选择的字典类型吗？', this.href)">
				         <input class="btn btn-primary" type="button" value="重新选定"/></a>
				</c:if>
				<a style="margin-left:15px;" href="${ctx}/searchCondCfg/SearchCondItemslist2?itemtype=${itemtype}&pid=${pid}" >
				         <input class="btn btn-primary" type="button" value="字典表编辑"/></a>
			    <br/>
				<!--  
			    <a href="${ctx}/searchCondCfg/toAdd?itemtype=${itemtype}&pid=${pid}" onclick="return confirmx('您确定要新增通配类型吗？', this.href)" class="typeName">新增通配类型</a>&nbsp;&nbsp;&nbsp;&nbsp;
			     <a href="javascript:void(0)" onclick="interfaceBind()" class="typeName">选择默认值</a>
			    <a href="${ctx}/searchCondCfg/AddSearchCondItems?itemtype=${itemtype}&pid=${pid}" class="typeName">新增字典类型详情</a>
			    <a href="${ctx}/searchCondCfg/sortSearchCondItems?itemtype=${itemtype}&pid=${pid}" class="typeName">详情排序</a>
			    -->
		</div>
	</form:form>
	<form action="${ctx}/searchCondCfg/searchCondCfgSave?itemtype=${itemtype}&pid=${pid}" id="form2">
	   <input id="defaultvalue" name="defaultvalue" type="hidden" value=""/>
	    <input id="ppid" name="ppid" type="hidden" value="${pid}"/>
	     <input id="itemtype" name="itemtype" type="hidden" value="${itemtype}"/>
	   <div style="float:left;margin-top:10px;margin-bottom:10px;width:100%;">
	    <div class="control-group" style="margin-left: 10px;"> 
	         <label class="control-label">默认选项：</label>
		        <select id="searchCondItems" name="searchCondItems"  multiple="multiple" size="8" >
				    <c:forEach items="${list}" var="searchCondItems" varStatus="status">
				         <option value="${searchCondItems.itemvalue}">${searchCondItems.itemcaption}</option>
				    </c:forEach>
				</select>
		</div>
		<div class="form-actions pagination-left" style="width:100%;">
			<input id="btnSubmit" class="btn btn-primary"  style="margin-left: 20%;" type="button" value="保存" onclick="saveMoren();"/>
		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  id="btnCancel" class="btn" type="button" value="关闭" onclick="closeWin();"/>
		</div>
	 </div>
	 </form>
</body>
</html>