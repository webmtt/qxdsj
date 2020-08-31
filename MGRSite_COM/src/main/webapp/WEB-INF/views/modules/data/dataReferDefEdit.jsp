<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>    
    <title>修改页面</title>
   <meta name="decorator" content="default"/>
   <script type="text/javascript">
	$(function(){
		$("#inputForm").validate({
			
		});
	})
   </script>
  </head>
  
  <body>
  <ul class="nav nav-tabs">
		<li><a href="${ctx}/dataReferDef/dataReferDefList?dataCode=${dataCode}&categoryid=${categoryid}&pid=${pid}">资料文献列表</a></li>
		<li class="active"><a href="javascript:void(0)">编辑文献</a></li>
		
		</li>
	</ul>
	<form:form  modelAttribute="dataReferDef" method="post" class="breadcrumb form-search">
		<span style="font-weight: bold;">当前位置：</span>${pidName}--${categoryName}--${dataDefName}
	</form:form>
  <form:form id="inputForm" modelAttribute="dataReferDef" action="${ctx}/dataReferDef/update?datacode=${dataCode}"  method="post" class="form-horizontal">	
  		<input type="hidden" id="referid" name="referid" value="${dataReferDef.referid}"> 	
  		<input type="hidden" id="datacode" name="datacode" value="${dataReferDef.datacode}">	
  		<input type="hidden" id="categoryid" name="categoryid" value="${categoryid }">
  		<input type="hidden" id="orderno" name="orderno" value="${dataReferDef.orderno }">  
  		<input type="hidden" id="invalid" name="invalid" value="${dataReferDef.invalid }">    
  		<input type="hidden" id="pid" name="pid" value="${pid}">		
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>文献名称:</label>
			<div class="controls">						
				<input type="text"  id="refername" value="${dataReferDef.refername }" name="refername"  class="required" htmlEscape="false" style="width:650px;"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit"  value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div> 
  </form:form>
  </body>
</html>
