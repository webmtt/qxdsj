<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
<title>编辑数据类型</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<style>
.commonModel{margin-top:20px;}
</style>
<script type="text/javascript">	
var ctx = "${ctx}";
var ctxStatic = "${ctxStatic}";
$(function(){
	dataSearch();
	$('input[type=radio][name=searchsetcode]').change(function() {
        if (this.value == '0') {
        	$("#searchsetcode1").show();
        }else if (this.value == '1') {
        	$("#searchsetcode1").hide();
        }
    });
	$('input[type=radio][name=elementsetcode]').change(function() {
        if (this.value == '0') {
        	$("#elesetcode1").show();
        }else if (this.value == '1') {
        	$("#elesetcode1").hide();
        }
    });
	$('input[type=radio][name=interfacecode]').change(function() {
        if (this.value == '0') {
        	$("#interfacecode1").show();
        }else if (this.value == '1') {
        	$("#interfacecode1").hide();
        }
    });
	var categoryid="${categoryid}";
	if(categoryid=="17"){
		$(".specialCa").hide();
	}
})
function dataSearch(){
	$("#searchsetcode1").hide();
	$("#elesetcode1").hide();
	var searchsetcode=$("input[name='searchsetcode']:checked").val();
	var elementsetcode=$("input[name='elementsetcode']:checked").val();
	if(searchsetcode=="0"){
		$("#searchsetcode1").show();
	}else if(searchsetcode=="1"){
		$("#searchsetcode1").hide();
	}
	if(elementsetcode=="0"){
		$("#elesetcode1").show();
	}else if(elementsetcode=="1"){
		$("#elesetcode1").hide();
	}
}
function changeDataSearch(){
	var categoryid = $("#searchalloption option:checked").val();
	$.ajax({
		url : "${ctx}/dataSearchDef/getSearchCode?categoryid="+ categoryid,
		type : "post",
		dataType : "json",
		async : true,
		success : function(result) {
			$("#searchalloption2").empty();
			if(result.length==0){
				alert("该小类下没有检索集合！请选择其他小类！");
				$("#searchalloption2").empty();
			}else{
				var html="";
				for(var i=0;i<result.length;i++){
					html+="<option value='"+result[i][2]+"'>"+result[i][1]+"</option>";
				}
				$("#searchalloption2").append(html);
			}
		}
    });
}
function changeElementCode(){
	var categoryid = $("#elealloption option:checked").val();
	$.ajax({
		url : "${ctx}/dataSearchDef/getElementCode?categoryid="+ categoryid,
		type : "post",
		dataType : "json",
		async : true,
		success : function(result) {
			$("#elealloption2").empty();
			if(result.length==0){
				alert("该小类下没有检索集合！请选择其他小类！");
				$("#elealloption2").empty();
			}else{
				var html="";
				for(var i=0;i<result.length;i++){
					html+="<option value='"+result[i][2]+"'>"+result[i][1]+"</option>";
				}
				$("#elealloption2").append(html);
			}
		}
    });
}	
function check(){
	var searchsetcode=$("input[name='searchsetcode']:checked").val();
	var elementsetcode=$("input[name='elementsetcode']:checked").val();
	//var interfacecode=$("input[name='interfacecode']:checked").val();
	if(searchsetcode=="0"){
		var searchalloption2 = $("#searchalloption2 option:selected").val();
		if(searchalloption2==null||searchalloption2==""||searchalloption2=="undefined"){
			alert("请选择检索条件集合！");
			return false;
		}
	}
	if(elementsetcode=="0"){
		var elealloption2 = $("#elealloption2 option:selected").val();
		if(elealloption2==null||elealloption2==""||elealloption2=="undefined"){
			alert("请选择检索要素集合！");
			return false;
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
	<form:form id="inputForm" modelAttribute="dataSearchDef" action="${ctx}/dataSearchDef/saveupdateDataSearch" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
      <input type="hidden" id="datacode"  name="datacode" value="${datacode}"/>
      <input type="hidden" id="categoryid"  name="categoryid" value="${categoryid}"/>
      <input type="hidden" id="pid"  name="pid" value="${pid}"/>
	<div class="control-group">
		<label class="control-label">检索条件集合：</label>
			<div class="controls">
			        <input type="radio" value="0" name="searchsetcode" checked="checked"/>选择已有资料的检索条件集合
					<input type="radio" value="1" name="searchsetcode" />新增检索条件集合
			</div>
			<div class="controls" id="searchsetcode1">
			    <div  class="commonModel">
			    <span class="specialCa">
			        <select id="searchalloption" name="searchalloption0" onChange="changeDataSearch(this)">
			           <c:forEach items="${plist}" var="dataCategoryDef">
			             <c:if test="${dataCategoryDef.categoryid==categoryid}">
			                <option value="${dataCategoryDef.categoryid}"  selected="selected">${dataCategoryDef.chnname}</option>
			             </c:if>
			             <c:if test="${dataCategoryDef.categoryid!=categoryid}">
			                <option value="${dataCategoryDef.categoryid}">${dataCategoryDef.chnname}</option>
			             </c:if>
			           </c:forEach>
			        </select>
			    </span>  	
			        <select id="searchalloption2" name="searchalloption" style="min-width:300px;">
			           <c:forEach items="${slist}" var="Cate">
			              <c:if test="${Cate[2]==dataSearchDef.datacode}">
			                  <option value="${Cate[2]}"  selected="selected">${Cate[1]}</option>
			              </c:if>
			              <c:if test="${Cate[2]!=dataSearchDef.datacode}">
			                   <option value="${Cate[2]}">${Cate[1]}</option>
			              </c:if>
			           </c:forEach>
			        </select>  	
			    </div>
			</div>
	</div>
	<div class="control-group">
		<label class="control-label">要素集合：</label>
		<div class="controls">
		    <input type="radio" value="0" name="elementsetcode" checked="checked"/>选择已有资料的要素集合
			<input type="radio" value="1" name="elementsetcode" />新增要素集合
		</div>
		<div class="controls" id="elesetcode1">
		    <div  class="commonModel">
		    <span class="specialCa">
		        <select id="elealloption" name="elealloption0" onChange="changeElementCode(this)">
		           <c:forEach items="${plist}" var="dataCategoryDef">
		                 <c:if test="${dataCategoryDef.categoryid==categoryid}">
		                    <option value="${dataCategoryDef.categoryid}"  selected="selected">${dataCategoryDef.chnname}</option>
		                 </c:if>
		                 <c:if test="${dataCategoryDef.categoryid!=categoryid}">
		                    <option value="${dataCategoryDef.categoryid}">${dataCategoryDef.chnname}</option>
		                 </c:if>
		           </c:forEach>
		        </select>  	
		        </span>
		        <select id="elealloption2" name="elealloption" style="min-width:300px;">
		           <c:forEach items="${elist}" var="Cates">
		              <c:if test="${Cates[2]==dataSearchDef.datacode}">
		                 <option value="${Cates[2]}"  selected="selected">${Cates[1]}</option>
		              </c:if>
		              <c:if test="${Cates[2]!=dataSearchDef.datacode}">
		                 <option value="${Cates[2]}">${Cates[1]}</option>
		              </c:if>
		           </c:forEach>
		        </select>  	
		    </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">检索页面默认排序条件：</label>
		<div class="controls">				
			<input type="text" name="searchpageorderby" id="searchpageorderby" value="${dataSearchDef.searchpageorderby}" htmlEscape="false" cssStyle="width:350px;"/>
		  <br/>
		  <span style="color:red;">多个以“,”分隔，支持asc／desc,如：“datatime:asc”,遵循CIMISS接口规范</span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">默认台站号：</label>
		<div class="controls">				
			<input type="text" name="defaultstaions" id="defaultstaions"  value="${dataSearchDef.defaultstaions}" htmlEscape="false" cssStyle="width:350px;"/>
			<span style="color:red;">优先于检索条件表中的默认值，多个以“,”分隔</span>
		</div>
	</div>
	  <div class="control-group"> 
	     <div class="controls">		
		   <input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>
		   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  id="btnCancel" class="btn" type="button" value="关闭" onclick="closeWin();"/>
	      </div>
	   </div>	 
</form:form>
</body>
</html>

