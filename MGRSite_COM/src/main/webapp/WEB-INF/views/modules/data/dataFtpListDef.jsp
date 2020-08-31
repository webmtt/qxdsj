<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>FTP列表页面</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}
	#contentTable th{
		text-align: center;
	}
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
		$("#form1").attr("action","${ctx}/dataFtpDef/dataFtpList?dataCode="+dataCode+"&categoryid="+categoryid+"&pid="+pid);
		$("#form1").submit();
    	return false;
    }
	function changeData(){
		//获取大类选择的id
		var DataClass = $("#DataClass option:checked").val();
		$("#pid").val(DataClass);
		$.ajax({
			url : "${ctx}/dataFtpDef/getDataSubClassList?DataClass="+DataClass,
			type : "post",
			dataType : "json",
			async : true,
			success : function(result) {
			var list = result;
			if(DataClass==17){
				var id=$("#DataClass option:checked").val();
				$("#categoryid").val("17");
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
			window.location.href="${ctx}/dataFtpDef/add?dataCode="+id+"&categoryid="+categoryid+"&pid="+pid+"&pageType=1";
		}
	}
	function sortReturn(){
		var categoryid=$("#categoryid").val();
		var pid=$("#pid").val();
		var id=$("#metadata option:checked").val();
		if(id==0||id==null){
			alert("请选择一种数据资料！");
		}else{
			window.location.href="${ctx}/dataFtpDef/sortDataRefer?dataCode="+id+"&categoryid="+categoryid+"&pid="+pid+"&pageType=1";
		}
	}
	$(function(){
		var categoryid="${categoryid}";
		if(categoryid==17){
			$(".DataSubclass").hide();
		}else{
			$(".DataSubclass").show();
		}
	})
	function addReturn(){
		var categoryid=$("#categoryid").val();
		var pid=$("#pid").val();
		var id=$("#metadata option:checked").val();
		if(id==0||id==null){
			alert("请选择一种数据资料！");
		}else{
			window.location.href="${ctx}/dataFtpDef/dataFtpAdd?dataCode="+id+"&categoryId="+categoryid+"&pid="+pid+"&pageType=1";
		}
	}
	</script>
</head>
<body>
	
	<ul class="nav nav-tabs">
		<li class="active"><a href="javascript:void(0)">FTP下载列表</a></li>
		<c:if test="${status==1}">
		    <li><a href="javascript:void(0)" onclick="addReturn()">新增FTP下载</a></li>
		</c:if>
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
				<select id="metadata"  name="metadata" onChange="dataChose(this)">
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
			<form:form id="form1" modelAttribute="dataFtpDef" action="${ctx}/dataFtpDef/dataFtpDefList" method="post" style="float: right;height: 40;margin: 0px;">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				<input id="dataCode" name="dataCode" type="hidden" value="${datacode}">
				<input id="categoryid" name="categoryid" type="hidden" value="${categoryid}">
				<input id="pid" name="pid" type="hidden" value="${pid}">
			</form:form>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
		<thead>
  			<th>序号</font></th>
  			<th>数据资料</font></th>
    		<th>FTP名称</font></th>
    		<th>操作</font></th>
  		</thead>
  		<tbody>		
  			<c:forEach items="${page.list}" var="dataFtpDef" varStatus="status">
  			<tr>
  			<td style="width: 5%;text-align: center;">${status.index+1}</td>  
    		<td style="width: 35%;">${dataFtpDef[1] }</td>
    		<td style="width: 35%;">${dataFtpDef[2] }</td>
    		<td style="width: 20%;text-align: center;">    		
    			<a href="${ctx}/dataFtpDef/dataFtpAdd?dataCode=${dataFtpDef[0]}&categoryId=${categoryid}&pid=${pid}">&nbsp;修改&nbsp;</a>
    			<c:if test="${dataFtpDef[3] eq '0' }">
    			   <a href="${ctx}/dataFtpDef/delete?datacode=${dataFtpDef[0]}&categoryid=${categoryid}&pid=${pid}"
    				 onclick="return confirmx('确认要置为无效吗？', this.href)">&nbsp;无效&nbsp;</a> 
    			</c:if>
    			<c:if test="${dataFtpDef[3] eq '1' }">
    			   <a style="color:red;" href="${ctx}/dataFtpDef/delete?datacode=${dataFtpDef[0]}&categoryid=${categoryid}&pid=${pid}"
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