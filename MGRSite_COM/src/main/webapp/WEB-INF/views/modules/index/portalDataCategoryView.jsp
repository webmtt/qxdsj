<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新增物品</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	
});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/portalData/portalDataCategoryList">数据列表</a></li>
		 <li class="active"><a href="${ctx}/portalData/addPortalData">查看数据</a></li> 
	</ul>
<tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="portalDataCategoryDef" action="${ctx}/portalData/savePortalData" method="post" class="form-horizontal" onsubmit="return check()">
  		<form:hidden path="id" />
  		<!-- 
  		<div class="control-group">
            <label class="control-label">父节点：</label>
			<div class="controls">	
		   		<label>
		   		<tags:treeselect id="parent" name="treeNodeId" value="${chnName}" labelName="treeNodeName" labelValue="${chnName}"
				title="数据服务菜单" url="/portalData/getPortalDataTreeList"  extId=""/><span style="color: red;">若不选，添加的类型将作为父类型显示</span>
		   		</label>
			</div>
		</div>
		 -->
		 <c:choose>
        	<c:when test="${empty pid}">
       	       <div class="control-group">
				        <label class="control-label">父节点：</label>
							<div class="controls">	
						   		<label>
						   		<tags:treeselect id="pid" name="treeNodeId" value="${portalDataCategoryDef.parent.id}" labelName="parent.name" labelValue="${portalDataCategoryDef.parent.chnName}"
								title="数据服务菜单" url="/portalData/getPortalDataTreeList"  extId=""/>
						   		</label>
							</div>
						</div>
        	</c:when>
        	<c:otherwise>
        		  <c:choose>
        			<c:when test="${pid eq '0'}">
        			    <div class="control-group">
				        <label class="control-label">父节点：</label>
							<div class="controls">	
						   		<label>
						   		<tags:treeselect id="pid" name="treeNodeId" value="${portalDataCategoryDef.parent.id}" labelName="parent.name" labelValue="${portalDataCategoryDef.parent.chnName}"
								title="数据服务菜单" url="/portalData/getPortalDataTreeList"  extId=""/>
						   		</label>
							</div>
						</div>
        			</c:when>
        			<c:otherwise> 
        				 <div class="control-group">
				        <label class="control-label">父节点：</label>
							<div class="controls">	
						   		<label>
						   		<tags:treeselect id="pid" name="treeNodeId" value="${portalDataCategoryDef.parent.id}" labelName="parent.name" labelValue="${portalDataCategoryDef.parent.chnName}"
								title="数据服务菜单" url="/portalData/getPortalDataTreeList"  extId=""/>
						   		</label>
							</div>
						</div>	
        		  </c:otherwise>
        		</c:choose>  
        	</c:otherwise>
        </c:choose>	
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<form:input path="chnName" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">中文短名称:</label>
			<div class="controls">
				<form:input path="shortChnName" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">链接地址:</label>
			<div class="controls">
				<form:input path="linkurl" htmlEscape="false" maxlength="50"
						class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">跳转类型:</label>
			<div class="controls">
			<form:radiobuttons path="showType" items="${fns:getDictList('showType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">描述:</label>
			<div class="controls">
				<form:textarea path="chnDescription" htmlEscape="false" rows="3"
						maxlength="50"  class="input-xlarge" style="width:206px;"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">序号:</label>
			<div class="controls">
				<form:input path="orderNo" htmlEscape="false" maxlength="50"
						class="required" name="orderNo" />
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
  </form:form>
</body>
</html>