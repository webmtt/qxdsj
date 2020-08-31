<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>    
    <title>修改页面</title>
   <meta name="decorator" content="default"/>
   <script type="text/javascript">
   function check(){
		var datacode=$("#datacode").val();
//		var linktype=$("#linktype").val();
		var linktype=$("#dataLinksLinktype input:radio:checked").val();
		var linkname=$("#linkname").val();
		var linkurl=$("#linkurl").val();
		if(""==datacode||undefined==datacode){
			alert("请输入编码类型");
			return false;				
		}else if(""==linktype||undefined==linktype){
			alert("请选择链接类型");
			return false;		
		}else if(""==linkname||undefined==linkname){
			alert("请输入链接名称");
			return false;
		}else if(""==linkurl||undefined==linkurl){
			alert("请输入URL");
			return false;
		}
   }
   $(function(){
	   	$("#docBox").hide();
		$("#msg").hide();
		$("#linkurl").show();
   })
   function uploadFile(inputId){
		$.ajaxFileUpload({  
	        url:"${ctx}/dataService/docUpload",  
	        secureuri:false,  
	        fileElementId:'docBox',//file标签的id 
	        data:{'dataCode':dataCode},
	        dataType: 'json',//返回数据的类型  
	        success: function (data, status) {  
	        	var linkUrl=data.linkURL;
	        	if(linkUrl!=""&&linkUrl!=undefined){
	        		$("#msg").empty();
	        		$("#msg").text(data.message);
	        		alert(data.linkURL);
	        		$("#linkurl").val(data.linkURL);
	        		var linkname=$("#linkname").val();
	        		if(linkname==""||linkname==undefined){
	        			$("#linkname").val(data.linkName);
	        		}
	        	}else{
	        		$("#msg").empty();
	        		$("#msg").text(data.message);
	        	}
	        	}, error: function (data, status, e) {   
	                $("#flag").attr("value","0"); 
	                //这里处理的是网络异常，返回参数解析异常，DOM操作异常  
	               /*  alert("上传发生异常");  	 */	           
	                alert("上传文件大小超过${maxUploadSize}M，请联系管理员上传！"); 
	            } 
	        });
	}
   function showDocBox(){
		$("#docBox").show();
		$("#msg").show();
		var linkUrl=$("#linkurl").val();
		if(linkUrl!=""&&linkUrl!=""){
			$("#linkurl").show();
		}else{
		$("#linkurl").hide();			
		}
	}
	function hideDocBox(){
		$("#docBox").hide();
		$("#msg").hide();
		$("#linkurl").show();
		$("#linkUrlBox").attr("value","");
	}
   </script>
  </head>
  
  <body>
  <ul class="nav nav-tabs">
		<li><a href="${ctx}/dataLinks/dataLinksList?datacode=${dataCode}&categoryid=${categoryid}&pid=${pid}">${dataDefName}-资料链接列表</a></li>
		<li class="active"><a href="${ctx}/dataLinks/dataLinksUpdate?linkid=${linkid}&datacode=${dataCode}">修改链接</a></li>	
	</ul>
	<form:form  modelAttribute="dataReferDef" method="post" class="breadcrumb form-search">
		<span style="font-weight: bold;">当前位置：</span>${pidName}--${categoryName}--${dataDefName}
	</form:form>
  <form action="${ctx}/dataLinks/update"  method="post" class="form-horizontal" onsubmit="return check()">
  		
  		<input type="hidden" id="linkid" name="linkid" value="${dataLinks.linkid}"> 		
  		<input type="hidden" id="datacode" name="datacode" value="${dataLinks.datacode}">	
  		<input type="hidden" id="categoryid" name="categoryid" value="${categoryid }">  
  		<input type="hidden" id="pid" name="pid" value="${pid}">
  		<input type="hidden" id="orderno" name="orderno" value="${dataLinks.orderno}">
  		<input type="hidden" id="invalid" name="invalid" value="${dataLinks.invalid}">
		<div class="control-group">	
			<label class="control-label"><a style="color: red;">*</a>链接类型:</label>
			<div class="controls" id="dataLinksLinktype">
			<input type="radio" id="linktype0" name="linktype" value="0"  onclick="showDocBox()"
			<c:if test="${dataLinks.linktype eq '0'}">
				checked='checked'
				</c:if>
			/>		
				说明文档
			<input type="radio" id="linktype1" name="linktype" value="1" onclick="showDocBox()"
			
			<c:if test="${dataLinks.linktype eq '1'}">
				checked='checked'
				</c:if>
			
			/>		
				相关信息
			<input type="radio" id="linktype2" name="linktype" value="2" onclick="hideDocBox()"
			
			<c:if test="${dataLinks.linktype eq '2'}">
				checked='checked'
				</c:if>
			
			/>
				相关数据	
			<input type="radio" id="linktype3" name="linktype" value="3" onclick="hideDocBox()"
			
			<c:if test="${dataLinks.linktype eq '3'}">
				checked='checked'
				</c:if>
			
			/>		
				相关产品
			<input type="radio" id="linktype4" name="linktype" value="4" onclick="hideDocBox()"
			
			<c:if test="${dataLinks.linktype eq '4'}">
				checked='checked'
				</c:if>
			
			/>
				数据中心	
<%--			<input type="radio" id="linktype5" name="linktype" value="5"  onclick="hideDocBox()"--%>
<%--			--%>
<%--			<c:if test="${dataLinks.linktype eq '5'}">--%>
<%--				checked='checked'--%>
<%--				</c:if>--%>
<%--			--%>
<%--			/>		--%>
<%--				FTP--%>
			<input type="radio" id="linktype6" name="linktype" value="6" onclick="hideDocBox()"
			
			<c:if test="${dataLinks.linktype eq '6'}">
				checked='checked'
				</c:if>
			
			/>
				相关种类		
			
			</div>
			
		</div>
		
		<div class="control-group">	
			<label class="control-label">链接名:</label>
			<div class="controls">						
				<input type="text" id="linkname" value="${dataLinks.linkname}" name="linkname" htmlEscape="false" cssStyle="width:350px;"/>
			</div>
		</div>
		

		<div class="control-group">
			<label class="control-label">链接URL:</label>
			<div class="controls">						
				<input type="text"  id="linkurl" value="${dataLinks.linkurl}" name="linkurl" htmlEscape="false" cssStyle="width:350px;"/>
				<input type="file" id="docBox" name="docBox" onchange="uploadFile()"><span id="msg" style="color: red;"></span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit"  value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div> 
  </form>
  </body>
</html>
