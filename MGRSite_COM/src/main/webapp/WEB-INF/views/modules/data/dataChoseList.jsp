<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>资料及相关列表页面</title>
    <meta name="decorator" content="default" />
	<%@include file="/WEB-INF/views/include/dialog.jsp"%>
	<style type="text/css">
	.title{
	 background-color: #f5f5f5;
    border-radius: 4px;
    color: #555555;
    display: table-cell;
    font-size: 15px;
    height: 40px;
    line-height:40px;
    margin: 10px;
    vertical-align: middle;
    width: 1160px;
	}
	.type{
		font-weight: bold;
		float: left;
		padding-left: 10px;
	}
	.typeName{
		float: left;
		width: 580px;
	}
	.typeName a{
	background-color: #3daae9;
    background-image: linear-gradient(to bottom, #46aeea, #2fa4e7);
    background-repeat: repeat-x;
    border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
    border-radius: 4px;
    color: #ffffff;
    display: inline-block;
    font-size: 14px;
    height: 21px;
    line-height:20px;
    margin-left: 25px;
    text-align: center;
    text-decoration: none;
    text-shadow: 0 0 0 rgba(0, 0, 0, 0.25);
    vertical-align: middle;
    width: 78px;
	}
	</style>
	<script type="text/javascript">
	var ctx="${ctx}"
	function page() {
		$("#form1").attr(
					"action",
					"${ctx}/dataService/getDataDetail");
		$("#form1").submit();		
		return false;
	}
	function searchInfo(){
		$("#form1").submit();
	}
	function openwin(){
		parent.open(ctx+"/dataService/showPage3");
	}
	$(function(){
		var categoryid='${categoryid}';
		$("[name = DataSubclass]:checkbox").each(function () {
	       
	    });
		$(".updateOrderNo").click(function(){
			var orderNo=$(this).prev().val();
			var categoryid=$(this).attr("categoryid");
			var dataCode=$(this).attr("dataCode");
			var pid=$(this).attr("pid");
			var noworderno=$("#orderno").val();
			var nameReg=/^[0-9]*[0-9][0-9]*$/;
			if(noworderno==""||noworderno==undefined){
				$("#orderno").focus();
			}else{
				if(!nameReg.test(noworderno)){
					alert("排序字段只能填写正整数，例如0,1,2");
				}else{
					if(confirm("确定要修改排序吗？")){
						 window.location.href=ctx+"/dataService/getDataDetailByNo?categoryid="+categoryid+"&dataCode="+dataCode+"&orderNo="+orderNo+"&pid="+pid;
			    		}
				}
			}			
		})
		$(".dataDefName").keydown(function(event){
			if(event.which == "13"){
				$("#form1").submit();;
			}
			}); 
	});
	function changeData(){
		//获取大类选择的id
		var DataClass = $("#DataClass option:checked").val();
		$("#pid").val(DataClass);
		$.ajax({
			url : "${ctx}/dataSearchDef/getDataSubClassList?DataClass="+DataClass,
			type : "post",
			dataType : "json",
			async : true,
			success : function(result) {
			var list = result;
			if(DataClass==17){
				var id=$("#DataClass option:checked").val();
				$("#categoryid").val(id);
			}else{
				if(result.length==0){
					alert("对不起，该大类下没有子类型，请先添加该大类的子类型！");
				}else{
					var option = ""; 
					for(var i=0;i<list.length;i++){
						var categoryid=list[i].categoryid;
						var chnname=list[i].chnname;
						if(i==0){
							option += "<option value="+categoryid+"  selected = 'selected'>"+chnname+"</option>";
						}else{
							option += "<option value="+categoryid+">"+chnname+"</option>";
						}
						
					}
					$("#DataSubclass").empty();
					$("#DataSubclass").select2("val", null); 
					$("#DataSubclass").append(option);
					$('#DataSubclass').trigger('change');
				}
			}
			
		  }
		});
	}
	function changeDataSub(){
		var id=$("#DataSubclass option:checked").val();
		$("#categoryid").val(id);
		searchInfo();
	}
	function saveChose() {
		var chk_value =[]; 
		$('input[name="checkbox"]:checked').each(function(){ 
		chk_value.push($(this).val()); 
		}); 
		if(chk_value.length==0){
			alert("请选择资料！");
		}else{
			$("#codes").val(chk_value);
			loading('正在提交，请稍等...');
			$("#listForm").attr("action", "${ctx}/dataService/addCategoryDataRelt");
			$("#listForm").submit();
		}
	}
	$(function(){
		var categoryid='${categoryid}';
		if(categoryid==17){
			$(".DataSubclass").hide();
		}else{
			$(".DataSubclass").show();
		}
	})
	</script>
  </head>
  
  <body>
  	<ul class="nav nav-tabs">
		<li><a href="${ctx}/dataService/getDataDetail?pid=${ppid}&categoryid=${pcategoryid}">${chName}-数据资料列表</a></li>
		<li class="active"><a href="javaScript:void(0)">添加已有资料</a></li>
	</ul>
	<div class="title">
  		<span style="font-weight: bold;">当前大类：</span>${parentName}--${chName }</br>
  		<div class="type">选择：</div>
			<select id="DataClass"  name="DataClass"  onChange="changeData(this)">
				<c:forEach items="${plist}" var="dataCategory">
				    <c:if test="${pid==dataCategory.categoryid}">
				       <option value="${dataCategory.categoryid}" type="${dataCategory.categoryid}" selected = "selected">${dataCategory.chnname}</option>
				    </c:if>
				    <c:if test="${pid!=dataCategory.categoryid}">
				       <option value="${dataCategory.categoryid}" type="${dataCategory.categoryid}">${dataCategory.chnname}</option>
				    </c:if>
				</c:forEach>
			</select>
			<span class="DataSubclass">
			<select id="DataSubclass"  name="DataSubclass" onChange="changeDataSub(this)">
				<c:forEach items="${clist}" var="dataCategory">
				    <c:if test="${categoryid==dataCategory.categoryid}">
				        <option value="${dataCategory.categoryid}" type="${dataCategory.categoryid}" selected = "selected">${dataCategory.chnname}</option>
				    </c:if>
				    <c:if test="${categoryid!=dataCategory.categoryid}">
				        <option value="${dataCategory.categoryid}" type="${dataCategory.categoryid}">${dataCategory.chnname}</option>
				    </c:if>
				</c:forEach>
			</select>
			</span>
		<!--  
  		<input id="openWin" class="btn btn-primary" type="button" value="查看示例" onclick="openwin()"/>
  		-->
  		<form id="form1" action="${ctx}/dataService/getDataChose" style="float: right;height: 40;margin: 0px;">
			<input type="hidden" id="categoryid" name="categoryid" value="${categoryid}">
			<input type="hidden" id="chname" name="chname" value="${CategoryName}">
			<input type="hidden" id="pid" name="pid" value="${pid}">
			<input type="hidden" id="ppid" name="ppid" value="${ppid}">
			<input type="hidden" id="chName" name="chName" value="${chName}">
			<input type="hidden" id="pcategoryid" name="pcategoryid" value="${pcategoryid}">
			<input type="text" style="margin-bottom: 0px;" id="dataDefName" name="dataDefName" placeholder="填写资料名称" onblur="searchInfo()"  value="${dataDefName }">
		</form>
  	</div>
  	<tags:message content="${message}"/>
  	<form id="listForm" method="post">
  	<input type="hidden" id="cpid" name="cpid" value="${ppid}">
  	<input type="hidden" id="codes" name="codes" value="${codes}">
  	<input type="hidden" id="pcategoryid" name="pcategoryid" value="${pcategoryid}">
  	<div style="margin-top: 10px;"> 	
	    <table id="tablelist"
			class="table table-striped table-bordered table-condensed"
			style="word-break:break-all; word-wrap:break-word;">
			<thead>
				<th style="text-align: center;">资料名称</th>
				<th style="text-align: center;">操作</th>
			</thead>
			<tbody>
				<c:forEach items="${CategoryDataRelts}" var="CategoryDataRelt">
					<tr>
						 <td style="width: 15%;">${CategoryDataRelt.chnName}</td>
						<td style="width: 10%;text-align: center;"> 
						    <input type="checkbox" name="checkbox" value="${CategoryDataRelt.dataCode }" />
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
  	</div>
  	<div class="form-actions pagination-left">
		<input id="btnSubmit" class="btn btn-primary" type="button" value="添加" onclick="saveChose();"/>
	</div>
  	</form>
  </body>
</html>
