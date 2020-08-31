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
	var datacode=$("#datacode").val();
	var invalid=$("#invalid input:radio:checked").val();
}
	
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/searchSetDef/list">数据检索条件集合定义列表</a></li>
		<li class="active"><a href="${ctx}/searchSetDef/toEdit?id=${id}">编辑数据检索条件集合定义</a></li>  
	</ul>
	<tags:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="searchSetDef" action="${ctx}/searchSetDef/edit" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
      <input type="hidden" id="id" name="id" value="${searchSetDef.id}"> 		
      
       <div class="control-group">
		<label class="control-label">检索条件集合编码：</label>
		<div class="controls">				
			<input type="text" id="searchsetcode"  value="${searchSetDef.searchsetcode }" name="searchsetcode" htmlEscape="false" class="required" cssStyle="width:350px;"  />
		</div>
	</div>    
	 
	<div class="control-group">
		<label class="control-label">检索条件分组编码：</label>
		<div class="controls">				
			<input type="text" name="searchgroupcode" id="searchgroupcode" value="${searchSetDef.searchgroupcode }" htmlEscape="false" cssStyle="width:350px;"/>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">检索条件分组中文名称：</label>
		<div class="controls">				
			<input type="text" name="chnname" id="chnname" value="${searchSetDef.chnname }" htmlEscape="false" cssStyle="width:350px;"/>
		</div>
	</div> 
	 
	<div class="control-group">
		<label class="control-label">检索条件分组英文名称：</label>
		<div class="controls">				
			<input type="text" id="engname" value="${searchSetDef.engname }" name="engname" htmlEscape="false"  cssStyle="width:350px;"/>
		</div>
	</div>    
	 	     
	<div class="control-group">
		<label class="control-label">排序号：</label>
		<div class="controls">				
			<input type="text" name="orderno" id="orderno" value="${searchSetDef.orderno }" htmlEscape="false" cssStyle="width:350px;"/>
		</div>
	</div>
	
	<div class="control-group">
			<label class="control-label">是否可选: </label>
			<div class="controls" id="isoptional">						
				<input type="radio" id="isoptional1" name="isoptional" value="0"  
				
				<c:if test="${searchSetDef.isoptional eq '0'}">
				checked='checked'
				</c:if>
				
				/>		
				有效
				<input type="radio" id="isoptional2" name="isoptional" value="1"
				
				<c:if test="${searchSetDef.isoptional eq '1'}">
				checked='checked'
				</c:if>
				
				/>
				无效			
			
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">是否无效:</label>
			<div class="controls" id="invalid">						
				<input type="radio" id="invalid1" name="invalid" value="0"  
				
				<c:if test="${searchSetDef.invalid eq '0'}">
				checked='checked'
				</c:if>
				
				/>		
				有效
				<input type="radio" id="invalid2" name="invalid" value="1"
				
				<c:if test="${searchSetDef.invalid eq '1'}">
				checked='checked'
				</c:if>
				
				/>
				无效			
			
			</div>
		</div> 
		
		<div class="control-group">
			<label class="control-label">创建时间:</label>
			<div class="controls">
				<input  type="text"  id="created" name="created" cssClass="Wdate" value="${searchSetDef.created }" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
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

