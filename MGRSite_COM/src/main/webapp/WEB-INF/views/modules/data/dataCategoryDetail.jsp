<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>编辑数据服务</title>
<meta name="decorator" content="default" />

<style type="text/css">
.sort {
	color: #0663A2;
	cursor: pointer;
}
table th,table td{
	text-align:center !important;
	vertical-align: middle !important;
}
.linkurl,.datacount,.templatefile{
   display:none;
}
</style>
<script type="text/javascript">	
var ctx = "${ctx}";
var ctxStatic = "${ctxStatic}";

       $(function(){
         
           
       });
function showType(value){
         $("#linkurl").val("");
         $("#datacount").val("");
         $(".templatefile").hide();
         $(".linkurl").hide();
         $(".datacount").hide();
      if(value==1){
        $(".datacount").show();
      }else if(value==2){
        $(".templatefile").show();
      }else if(value==3){
        $(".linkurl").show();
      }     

}
		
function check(){
	      
		  return true;
}
/*function fileUpload(imgId){
			var f=document.getElementById(imgId).value.toLowerCase();
			if(f==null || f==undefined || f==""){
			  return true;
			}		       
		        if(!/\.(bmp|gif|jpg|jpeg|png)$/.test(f)){
		          alert("图标请上传图片！");
		          return false;		        
		       }
				$.ajaxFileUpload({  
		            url:'${ctx}/cimissApp/iconUpload',  
		            secureuri:false,  
		            fileElementId:imgId,//file标签的id  
		            dataType: 'json',//返回数据的类型  
		            data:{id:$("#id").val()},//一同上传的数据  
		            success: function (data, status) { 
		              $("#appIcon").val(data);
		              return true;
		            },error: function (data, status, e) {   
				                alert("上传失败");
				                return false;   
				                //这里处理的是网络异常，返回参数解析异常，DOM操作异常  
				              //   alert("上传发生异常");   		           
				            }  
		        });  		
				
		} 	*/
</script>
function openwin(){
	parent.open(ctx+"/dataService/showPage1");
}
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/dataService/list">资料种类列表</a></li>
		<li class="active"><a href="${ctx}/dataService/addDataCategoryDef">编辑资料种类</a></li> 
	</ul>
	<form:form id="inputForm" modelAttribute="dataCategoryDef" action="${ctx}/dataService/updateDataCategory" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
         
        <form:input path="categoryid" id="categoryid"/>
       <%--  <form:input path="parentid" id="parentid"/>  --%>
        <form:input path="invalid" id="invalid" value="0"/>
        <form:input path="imageurl" id="imageurl"/> 
        <form:input path="iconurl" id="iconurl"/> 
        <form:input path="largeiconurl" id="largeiconurl"/>
        <form:input path="middleiconurl" id="middleiconurl"/>
        <form:input path="templatefile" id="templatefile"/>    
        
        <div class="control-group">
		     <input id="openWin" class="btn btn-primary" type="button" value="查看示例" onclick="openwin()" style="margin-left:200px;"/>
		</div>	
		
		<div class="control-group">
		     <label class="control-label">类型选择：</label>
		     <input type="radio" value="0" name="zllx" id="0"/>资料大类
             <input type="radio" value="1" name="zllx"  id="1"/>资料子类
		</div>
		<div class="control-group">
			<label class="control-label">中文名称：</label>
			<div class="controls">				
				<form:input path="chnname" id="chnname" htmlEscape="false" cssStyle="width:350px;"/>
			</div>
		</div>	
		 <div class="control-group">
			<label class="control-label">数据种类中文描述：</label>
			<div class="controls">				
				<form:textarea path="chndescription" id="chndescription" htmlEscape="false" cssStyle="width:500px;height:100px;"/>
			</div>
		</div>
				<c:if test="${!empty dataCategoryDef.imageurl}">
					<div class="control-group">
						<label class="control-label">示例图片：</label>
						<div class="controls">	
						<img src="${imgUrl}${dataCategoryDef.imageurl}">
					</div>
					</div>
				</c:if>
				
		      <div class="control-group">
					<label class="control-label">上传示例图片：</label>
					<div class="controls">
						<div id="iconbox" style="display:inline;">
						  <input type="file" id="imageurl1" name="imageurl1"/>
						</div>				
					</div>	 
				</div>
		    <div class="control-group">
			<label class="control-label">显示方式：</label>
				<div class="controls">				
					<form:radiobutton class="radio-input" path="showtype" name="showtype" value="0" onclick="showType(0)"/><label style="margin-right: 30px;">显示下级子类</label>
			        <form:radiobutton class="radio-input" path="showtype" name="showtype" value="1" onclick="showType(1)"/><label style="margin-right: 30px;">自动所有下级的资料</label>
			        <form:radiobutton class="radio-input" path="showtype" name="showtype" value="2" onclick="showType(2)"/><label style="margin-right: 30px;">模板</label>
			        <form:radiobutton class="radio-input" path="showtype" name="showtype" value="3" onclick="showType(3)"/><label style="margin-right: 30px;">url链接（进入相应页面）</label>
					<form:radiobutton class="radio-input" path="showtype" name="showtype" value="4" onclick="showType(4)"/><label style="margin-right: 30px;">url链接（打开新的页面）</label>
				</div>
		   </div>
		      <div class="control-group datacount">
				<label class="control-label">资料数量：</label>
				<div class="controls">				
					<form:input path="datacount" id="datacount" htmlEscape="false" cssStyle="width:350px;"/>
				</div>
		   </div>
		      <div class="control-group linkurl">
				<label class="control-label">URL链接：</label>
				<div class="controls">				
					<form:input path="linkurl" id="linkurl" htmlEscape="false" cssStyle="width:350px;"/>
				</div>
		   </div>
		    <div class="control-group templatefile">
					<label class="control-label">上传模板：</label>
					<div class="controls">
						<div  style="display:inline;">
						  <input type="file" id="templatefile1" name="templatefile1"/>
						</div>				
					</div>	 
				</div>
		  <div class="control-group">
				<label class="control-label">用户级别：</label>
				<div class="controls">				
					<form:input path="showuserrankid" id="showuserrankid" htmlEscape="false" value="0" cssStyle="width:350px;"/>
				</div>
		   </div>
		   
		   <c:if test="${!empty dataCategoryDef.iconurl}">
					<div class="control-group">
						<label class="control-label">图标：</label>
						<div class="controls">	
						<img src="${imgUrl}${dataCategoryDef.iconurl}">
					</div>
					</div>
				</c:if>
				
		      <div class="control-group">
					<label class="control-label">上传图标：</label>
					<div class="controls">
						<div  style="display:inline;">
						  <input type="file" id="iconurl2" name="iconurl2"/>
						</div>				
					</div>	 
				</div>
				<c:if test="${!empty dataCategoryDef.largeiconurl}">
					<div class="control-group">
						<label class="control-label">大图标：</label>
						<div class="controls">	
						<img src="${imgUrl}${dataCategoryDef.largeiconurl}">
					</div>
					</div>
				</c:if>
				
		      <div class="control-group">
					<label class="control-label">上传大图标：</label>
					<div class="controls">
						<div  style="display:inline;">
						  <input type="file" id="largeiconurl3" name="largeiconurl3"/>
						</div>				
					</div>	 
				</div>
				<c:if test="${!empty dataCategoryDef.middleiconurl}">
					<div class="control-group">
						<label class="control-label">中图标：</label>
						<div class="controls">	
						<img src="${imgUrl}${dataCategoryDef.middleiconurl}">
					</div>
					</div>
				</c:if>
				
		      <div class="control-group">
					<label class="control-label">上传中图标：</label>
					<div class="controls">
						<div  style="display:inline;">
						  <input type="file" id="middleiconurl4" name="middleiconurl4"/>
						</div>				
					</div>	 
				</div> 
		  <div class="control-group"> 
		     <div class="controls">		
			   <input id="btnSubmit" class="btn btn-primary" type="submit" value="提交"/>
	           <input id="btnCancel" class="btn" type="button" value="取消" onclick="history.go(-1)"/>
		      </div>
		   </div>	 
			
</form:form>
</body>
</html>

