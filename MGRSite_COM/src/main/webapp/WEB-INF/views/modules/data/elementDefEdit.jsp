<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
<title>编辑数据类型</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script type="text/javascript">	
var ctx = "${ctx}";
var ctxStatic = "${ctxStatic}";

function check(){
	var uelecode=$("#uelecode").val();
	var elementname=$("#elementname").val();
	if(""==uelecode||undefined==uelecode){
		alert("请输入接口要素编码");
		return false;
	}else if(""==elementname||undefined==elementname){
		alert("请输入要素名称");
		return false;
	}else{
		return true;
	}
}
	
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/eleSetEleGroup/elementList?elesetcode=${elesetcode}&elegroupcode=${elegroupcode}">结果要素列表</a></li>
		<li class="active"><a>编辑结果要素</a></li> 
	</ul>
	<tags:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="eleSetElementDef" action="${ctx}/eleSetEleGroup/saveUpdateElement" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
      <input type="hidden" id="id" name="id" value="${eleSetElementDef.id}">
       <input type="hidden" id="elesetcode" name="elesetcode" value="${elesetcode}"> 	
        <input type="hidden" id="elegroupcode" name="elegroupcode" value="${elegroupcode}"> 	 		
      <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>接口要素编码：</label>
			<div class="controls">				
				<input type="text" id="uelecode"   name="uelecode" htmlEscape="false" value="${eleSetElementDef.uelecode}" class="required" cssStyle="width:350px;"  />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">要素(存储)编码：</label>
			<div class="controls">				
				<input type="text" id="celecode"  name="celecode" htmlEscape="false" value="${eleSetElementDef.celecode}" class="required" cssStyle="width:350px;"  />
			</div>
		</div>
		 <div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>要素名称：</label>
			<div class="controls">				
				<input type="text" id="elementname"  name="elementname" htmlEscape="false" value="${eleSetElementDef.elementname}" class="required" cssStyle="width:350px;"  />
			</div>
		</div>
		 <div class="control-group">
			<label class="control-label">数据类型：</label>
			<div class="controls">				
				<input type="text" id="datatype" name="datatype" htmlEscape="false" value="${eleSetElementDef.datatype}" class="required" cssStyle="width:350px;"  />
			</div>
		</div>
		 <div class="control-group">
			<label class="control-label">要素单位：</label>
			<div class="controls">				
				<input type="text" id="dataunit"  value="" name="dataunit" htmlEscape="false" value="${eleSetElementDef.dataunit}" class="required" cssStyle="width:350px;"  />
			</div>
		</div>   
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>是否可选：</label>
			<div class="controls">				
				<input type="radio" name="isoptional"  value="0" <c:if test="${eleSetElementDef.isoptional eq '0'}">
				checked='checked'
				</c:if>/>必选
				<input type="radio" name="isoptional" value="1" <c:if test="${eleSetElementDef.isoptional eq '1'}">
				checked='checked'
				</c:if>/>可选
			</div>
		</div>   
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>是否值过滤：</label>
			<div class="controls">				
				<input type="radio" name="isfilter" value="0"  <c:if test="${eleSetElementDef.isfilter eq '0'}">
				checked='checked'
				</c:if>/>不过滤
				<input type="radio" name="isfilter" value="1" <c:if test="${eleSetElementDef.isfilter eq '1'}">
				checked='checked'
				</c:if>/>过滤
			</div>
		</div>   
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>是否默认选中：</label>
			<div class="controls">				
				<input type="radio" name="isseleted" value="0" <c:if test="${eleSetElementDef.isseleted eq '0'}">
				checked='checked'
				</c:if>/>不选中
				<input type="radio" name="isseleted" value="1" <c:if test="${eleSetElementDef.isseleted eq '1'}">
				checked='checked'
				</c:if>/>选中
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><a style="color: red;">*</a>是否质检：</label>
			<div class="controls">				
				<input type="radio" name="isqc" value="0" <c:if test="${eleSetElementDef.isqc eq '0'}">
				checked='checked'
				</c:if>/>不需要
				<input type="radio" name="isqc"  value="1" <c:if test="${eleSetElementDef.isqc eq '1'}">
				checked='checked'
				</c:if>/>需要
			</div>
		</div>
		  <div class="control-group"> 
		     <div class="controls">		
			   <input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>
	           <input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		      </div>
		   </div>	 
			
</form:form>
</body>
</html>

