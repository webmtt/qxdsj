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
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);

		var id = $("#DataClass option:checked").val();
		$("#categoryid").val(id);
		$("#form1").attr(
					"action",
					"${ctx}/dataService/getDataDetail");
		$("#form1").submit();
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

<%--		${categoryid}&chName=${CategoryName}&pid=--%>

		$.ajax({
			url : "${ctx}/dataSearchDef/getDataSubClassList?DataClass="+DataClass,
			type : "post",
			dataType : "json",
			async : true,
			success : function(result) {
				var list = result;
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
		});
	}
	function changeDataSub(){
		var id=$("#DataSubclass option:checked").val();
		var DataClass = $("#DataClass option:checked").val();
		if(DataClass==17){
			$("#categoryid").val(17);
		}else{
			$("#categoryid").val(id);
		}
		searchInfo();
	}
	function addReturn(){
		var categoryid=$("#categoryid").val();
		var pid=$("#pid").val();
		var chName=$("#DataSubclass option:checked").text();
		window.location.href="${ctx}/dataService/getDataChose?pcategoryid="+categoryid+"&chName="+chName+"&ppid="+pid;
	}
	$(function(){
		var categoryid='${categoryid}';
		if(categoryid==17){
			$(".DataSubclass").hide();
		}else{
			$(".DataSubclass").show();
		}
	})
	function uploadFile() {
		var file = $("#file").val();
		file = file.substring(file.lastIndexOf('.'), file.length);
		if (file == '') {
			alert("上传文件不能为空！");
		} else if (file != '.xlsx' && file != '.xls') {
			alert("请选择正确的excel类型文件！");
		} else {
			ajaxFileUpload();
		}
	}
	function ajaxFileUpload() {
		var fileObj = document.getElementById("file").files[0];
		var form = new FormData();
		form.append("file", fileObj);
		$.ajax({
			url : "${ctx}/dataService/dataExcelUpload",
			type : "POST",
			async : false,
			data : form,
			processData : false,
			contentType : false,
			beforeSend : function() {
				console.log("正在进行，请稍候");
			},
			success:function(){
				alert("上传成功");
				location.reload();//刷新当前页面
			},
			error:function(){
				alert("上传失败");
			}
		});
	}
	$(function () {
		var $input =  $("#file");
		// ①为input设定change事件
		$input.change(function () {
			//    ②如果value不为空，调用文件加载方法
			if($(this).val() != ""){
				uploadFile(this);
			}
		})
	});
	</script>
  </head>
  
  <body>
  	<ul class="nav nav-tabs">
		<li class="active"><a href="javaScript:void(0)">数据资料列表</a></li>
		<li><a href="${ctx}/dataService/addDataDef?pageType=1" onclick="addDataDef()">新增数据资料</a></li>
<%--		<li><a href="javascript:void(0)" onclick="addReturn();">添加已有资料</a></li>--%>
<%--		<li><a href="${ctx}/dataService/sortDataDef?categoryid=${categoryid}&pid=${pid}">资料排序</a></li>--%>
<%--		<label>--%>
<%--			<input id="file" type="file" name="file" class="input-medium" style="display:none;"/>--%>
<%--			<span id="controls" class="btn btn-primary"><span>批量导入</span></span></label>--%>
<%--		&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/report/supSurveystation/download?fileName=数据资料模板.xlsx" download="数据资料模板.xlsx" >模板下载</a>--%>
	</ul>
	<div class="title">
  		<div class="type">资料类别：</div>
			<select id="DataClass"  name="DataClass"  onChange="page(1, 10) ">
				<c:forEach items="${plist}" var="dataCategory">
					<c:choose>
						<c:when test="${categoryid==dataCategory.categoryid}">
							<option value="${dataCategory.categoryid}" type="${dataCategory.categoryid}" selected = "selected">${dataCategory.chnname}</option>
						</c:when>
						<c:otherwise>
							<option value="${dataCategory.categoryid}" type="${dataCategory.categoryid}">${dataCategory.chnname}</option>
						</c:otherwise>
					</c:choose>

				</c:forEach>
			</select>
<%--			<span class="DataSubclass">--%>
<%--				<select id="DataSubclass"  name="DataSubclass" onChange="changeDataSub(this)">--%>
<%--					<c:forEach items="${clist}" var="dataCategory">--%>
<%--					    <c:if test="${categoryid==dataCategory.categoryid}">--%>
<%--					        <option value="${dataCategory.categoryid}" type="${dataCategory.categoryid}" selected = "selected">${dataCategory.chnname}</option>--%>
<%--					    </c:if>--%>
<%--					    <c:if test="${categoryid!=dataCategory.categoryid}">--%>
<%--					        <option value="${dataCategory.categoryid}" type="${dataCategory.categoryid}">${dataCategory.chnname}</option>--%>
<%--					    </c:if>--%>
<%--					</c:forEach>--%>
<%--				</select>--%>
<%--			</span>--%>
		<!--  
  		<input id="openWin" class="btn btn-primary" type="button" value="查看示例" onclick="openwin()"/>
  		-->
  		<form id="form1" action="${ctx}/dataService/getDataDetail" style="float: right;height:40px;margin: 0px;">
			<input type="hidden" id="pageNo" name="pageNo" value="${pageNo}">
			<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}">
			<input type="hidden" id="categoryid" name="categoryid" value="${categoryid}">
			<input type="hidden" id="chname" name="chname" value="${CategoryName}">
			<input type="hidden" id="pid" name="pid" value="${pid}">
			<input type="text" id="dataDefName" name="dataDefName" placeholder="填写资料名称" onblur="searchInfo()"  value="${dataDefName }" style="margin-top: 5px;">
		</form>
  	</div>
  	<tags:message content="${message}"/>
  	<div style="margin-top: 10px;"> 	
	    <table id="tablelist"
			class="table table-striped table-bordered table-condensed"
			style="word-break:break-all; word-wrap:break-word;">
			<thead>
				<th style="text-align: center;">资料名称</th>
<%--				<th style="text-align: center;">相关链接</th>--%>
<%--				<th style="text-align: center;">引用文献</th>--%>
<%--				<th style="text-align: center;">服务内容</th>--%>
				<th style="text-align: center;">排序</th>
				<th style="text-align: center;">操作</th>
			</thead>
			<tbody>
				<c:forEach items="${CategoryDataRelts}" var="CategoryDataRelt">
					<tr>
						 <td style="width: 36%;">${CategoryDataRelt.chnName}</td>
						<%--  <td>${CategoryDataRelt.chnDecription}</td> --%>
<%--						 <td style="width: 10%;text-align: center;">--%>
<%--						 	<c:forEach items="${CategoryDataRelt.linkList }" var="link" varStatus="linkSize">--%>
<%--						 		<span>[${linkSize.count}]</span><span>${link.linkname}</span></br>--%>
<%--						 	</c:forEach>--%>
<%--						 	<a href="${ctx}/dataLinks/dataLinksList?datacode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid}">编辑/查看</a>						 	--%>
<%--						 </td>--%>
<%--						 <td style="width: 10%;text-align: center;">--%>
<%--						 <a href="${ctx}/dataReferDef/dataReferDefList?dataCode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid}">编辑/查看</a>						 --%>
<%--						 </td>--%>
<%--						 <td style="width: 28%;">--%>
<%--						    <c:if test="${CategoryDataRelt.serviceMode eq '1'}">--%>
<%--						 	     <span>数据检索</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/dataSearchDef/list?datacode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid }&pageType=0">编辑/查看</a>						 	--%>
<%--						 	     </br>--%>
<%--						 	</c:if>--%>
<%--						 	<c:if test="${CategoryDataRelt.serviceMode eq '4'}">--%>
<%--						 	     <span>FTP下载</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/dataFtpDef/dataFtpList?dataCode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid }&pageType=0">编辑/查看</a>						 	--%>
<%--						 	     </br>--%>
<%--						 	</c:if>--%>
<%--						 	<c:if test="${CategoryDataRelt.serviceMode eq '5'}">--%>
<%--						 	     <span>数据检索</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/dataSearchDef/list?datacode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid }&pageType=0">编辑/查看</a>						 	--%>
<%--						 	     </br>--%>
<%--						 	     <span>检索定制</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/dataSearchDef/list?datacode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid }&pageType=0">编辑/查看</a>						 	--%>
<%--						 	     </br>--%>
<%--						 	</c:if>--%>
<%--						 	<c:if test="${CategoryDataRelt.serviceMode eq '8'}">--%>
<%--						 	     <span>MUSIC接口服务</span>--%>
<%--						 	     </br>--%>
<%--						 	</c:if>--%>
<%--						 	<c:if test="${CategoryDataRelt.serviceMode eq '9'}">--%>
<%--						 	     <span>数据检索</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/dataSearchDef/list?datacode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid }&pageType=0">编辑/查看</a>						 	--%>
<%--						 	     </br>--%>
<%--						 	     <span>MUSIC接口服务</span>					 	--%>
<%--						 	     </br>--%>
<%--						 	</c:if>--%>
<%--						 	<c:if test="${CategoryDataRelt.serviceMode eq '12'}">--%>
<%--						 	     <span>FTP下载</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/dataFtpDef/dataFtpList?dataCode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid }&pageType=0">编辑/查看</a>						 	--%>
<%--						 	     </br>--%>
<%--						 	     <span>MUSIC接口服务</span>					 	--%>
<%--						 	     </br>--%>
<%--						 	</c:if>--%>
<%--						 	<c:if test="${CategoryDataRelt.serviceMode eq '13'}">--%>
<%--						 	     <span>检索定制</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/dataSearchDef/list?datacode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid }&pageType=0">编辑/查看</a>						 	--%>
<%--						 	     </br>--%>
<%--						 	      <span>FTP下载</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/dataFtpDef/dataFtpList?dataCode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid }&pageType=0">编辑/查看</a>						 	--%>
<%--						 	     </br>--%>
<%--						 	     <span>MUSIC接口服务</span>					 	--%>
<%--						 	     </br>--%>
<%--						 	</c:if>--%>
<%--						 	--%>
<%--						 </td>--%>
						 <td style="width: 5%;text-align: center;">${CategoryDataRelt.orderno}</td>
						<td style="width: 12%;text-align: center;"> 
						<a href="${ctx}/dataService/edit?datacode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid}">编辑</a> &nbsp;
						<c:if test="${CategoryDataRelt.invalid==0}">
						     <a href="${ctx}/dataService/deleteDataDef?dataCode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid}" onclick="return confirmx('确认要置为无效吗？', this.href)">无效</a>
						</c:if>
						<c:if test="${CategoryDataRelt.invalid==1}">
						     <a style="color:red;" href="${ctx}/dataService/deleteDataDef?dataCode=${CategoryDataRelt.dataCode}&categoryid=${categoryid}&pid=${pid}" onclick="return confirmx('确认要置为有效吗？', this.href)">有效</a>
						</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
  	</div>
  	<div class="pagination">${page}</div>
  </body>
</html>
