<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>月报记录</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}
	.addRefer{
	background-color: #3daae9;
    border-radius: 4px;
    color: #fff;
    display: inline-block;
    height: 20px;
    margin-left: 10px;
    text-align: center;
    text-decoration: none;
    width: 60px;
	}
	.addRefer:hover{
		color: #fff;
	}
	.title {
		background-color: #f5f5f5;
		border-radius: 4px;
		color: #555555;
		display: table-cell;
		font-size: 15px;
		height: 40px;
		line-height: 40px;
		margin: 10px;
		vertical-align: middle;
		width: 1160px;
		}
		.type {
font-weight: bold;
float: left;
padding-left: 10px;
}
	</style>
	<script type="text/javascript">
	
	function page(n,s){
		var pid=$("#DataClass option:checked").val();
		var categoryid=$("#DataSubclass option:checked").val();
		var dataCode=$("#metadata option:checked").val();
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#form1").attr("action","${ctx}/dataLinks/dataLinksList?datacode="+dataCode+"&categoryid"+categoryid+"&pid="+pid);
		$("#form1").submit();
    	return false;
    }
	function changeData(){
		//获取大类选择的id
		var DataClass = $("#DataClass option:checked").val();
		$("#pid").val(DataClass);
		$.ajax({
			url : "${ctx}/dataReferDef/getDataSubClassList?DataClass="+DataClass,
			type : "post",
			dataType : "json",
			async : true,
			success : function(result) {
			var list = result;
			if(DataClass==17){
				var id=$("#DataClass option:checked").val();
				$("#categoryid").val(id);
				page();
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
		page();
	}
	function dataChose(){
		var id=$("#metadata option:checked").val();
		$("#dataCode").val(id);
		page();
	}
	function searchInfo(){
		$("#form1").submit();
	}
	function addReturn(){
		var categoryid=$("#categoryid").val();
		var pid=$("#pid").val();
		var id=$("#metadata option:checked").val();
		if(id==0||id==null){
			alert("请选择一种数据资料！");
		}else{
			window.location.href="${ctx}/dataLinks/add?dataCode="+id+"&categoryid="+categoryid+"&pid="+pid+"&pageType=1";
		}
	}
	function sortReturn(){
		var categoryid=$("#categoryid").val();
		var pid=$("#pid").val();
		var id=$("#metadata option:checked").val();
		if(id==0||id==null){
			alert("请选择一种数据资料！");
		}else{
			window.location.href="${ctx}/dataLinks/sortDataLinks?dataCode="+id+"&categoryid="+categoryid+"&pid="+pid+"&pageType=1";
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
			url : "${ctx}/dataLinks/excelUpload",
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
	})
	</script>
</head>
<body>
	
	<ul class="nav nav-tabs">
		<li class="active"><a <a href="javascript:void(0)">资料链接列表</a></li>
		<li><a href="javascript:void(0)" onclick="addReturn()">新增链接</a></li>
		<li><a href="javascript:void(0)" onclick="sortReturn()">链接排序</a></li>
		<label>
			<input id="file" type="file" name="file" class="input-medium" style="display:none;"/>
			<span id="controls" class="btn btn-primary"><span>批量导入</span></span></label>
		&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/report/supSurveystation/download?fileName=资料链接模板.xlsx" download="资料链接模板.xlsx" >模板下载</a>
	</ul>
	<div class="title">
	<div class="type">资料大类：</div>
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
				<label>数据资料：</label>
				<select id="metadata"  name="metadata" onChange="dataChose(this)" style="width:280px;">
				<option value="0">全部</option>
				<c:forEach items="${dlist}" var="CategoryDataRelt">
				      <c:if test="${datacode == CategoryDataRelt[0]}">
				         <option value="${CategoryDataRelt[0]}" type="${CategoryDataRelt[0]}" selected = "selected">${CategoryDataRelt[1]}</option>
				      </c:if>
				      <c:if test="${datacode!=CategoryDataRelt[0]}">
				          <option value="${CategoryDataRelt[0]}" type="${CategoryDataRelt[0]}">${CategoryDataRelt[1]}</option>
				      </c:if>
				</c:forEach>
			</select>
	<form:form id="form1" modelAttribute="dataLinks" action="${ctx}/dataLinks/dataLinksList" method="post" style="float: right;height: 40;margin: 0px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="datacode" name="datacode" type="hidden" value="${datacode}">
		<input id="categoryid" name="categoryid" type="hidden" value="${categoryid}">
		<input id="pid" name="pid" type="hidden" value="${pid}">
	</form:form>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
		<thead>
  			<th>序号</th>
  			<th>数据资料</th>
    		<th>相关链接名称</th> 
    		<th>链接类型</th>
    		<th>排序</th>  	
    		<th>操作</th>
  		</thead>
  		<tbody>		
  			<c:forEach items="${page.list}" var="dataLinks" varStatus="status">
  			<tr>
  			<td>${status.index+1}</td>
  			<td>${dataLinks[2] }</td>  
    		<td>${dataLinks[3] }</td>  
    		<td>
    			<c:if test="${dataLinks[5] eq '0' }">
    				说明文档
    			</c:if>
    			<c:if test="${dataLinks[5] eq '1' }">
    				相关信息
    			</c:if>
    			<c:if test="${dataLinks[5] eq '2' }">
    				相关数据
    			</c:if>
    			<c:if test="${dataLinks[5] eq '3' }">
    				相关产品
    			</c:if>
    			<c:if test="${dataLinks[5] eq '4' }">
    				数据中心
    			</c:if>
<%--    			<c:if test="${dataLinks[5] eq '5' }">--%>
<%--    				FTP--%>
<%--    			</c:if>--%>
    			<c:if test="${dataLinks[5] eq '6' }">
    				相关种类
    			</c:if>
    		</td>
    		<td>${dataLinks[6] }</td>	
    		<td>   		
    			<a href="${ctx}/dataLinks/dataLinksUpdate?linkid=${dataLinks[0]}&datacode=${dataLinks[1]}&categoryid=${categoryid}&pid=${pid}"">&nbsp;修改&nbsp;</a>
    			<c:if test="${dataLinks[7] eq '0' }">
    			   <a href="${ctx}/dataLinks/delete?linkid=${dataLinks[0]}&datacode=${dataLinks[1]}&categoryid=${categoryid}&pid=${pid}"
    				 onclick="return confirmx('确认要置为无效吗？', this.href)">&nbsp;无效&nbsp;</a> 
    			</c:if>
    			<c:if test="${dataLinks[7] eq '1' }">
    			   <a style="color:red;" href="${ctx}/dataLinks/delete?linkid=${dataLinks[0]}&datacode=${dataLinks[1]}&categoryid=${categoryid}&pid=${pid}"
    				 onclick="return confirmx('确认要置为有效吗？', this.href)">&nbsp;有效&nbsp;</a> 
    			</c:if>
    			 
    		</td>
    	</tr>
		</c:forEach>
  		</tbody>
	</table>
	<div class="pagination">${page}</div>	
</body>
</html>