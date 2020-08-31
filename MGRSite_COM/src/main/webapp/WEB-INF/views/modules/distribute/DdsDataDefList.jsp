<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>   
    <title>分发定义列表</title>
    <meta name="decorator" content="default" />
	<%@include file="/WEB-INF/views/include/dialog.jsp"%>
	<style type="text/css">
	.sort {
	color: #0663A2;
	cursor: pointer;
	}
	</style>
	<script type="text/javascript">	
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		 
		$("#form1").submit();
    	return false;
    }
	// $(function(){
		/* timingRefresh(); 
		var datas=$('#dataS').val();
		var datat=$('#dataT').val();
		$("#dataStatus option").each(function(i,value){
		   $(value).attr("selected",false);
		   if($(value).val()==datas){
		   $(value).attr("selected","selected");
		   $("#s2id_dataStatus>a>span").html($(value).html());
		   }
		}); */
		//var hostName = $("select[name='hostName'] option:selected").val()
		//$("#hostName option").each(function(i,value){
		   //$(value).attr("selected",false);
		  // if($(value).val()==hostName){
		   //$(value).attr("selected","selected");
		   //$("#s2id_dataType>a>span").html($(value).html());
		//  // }
		//});;
	//}); 
	/* $(document).ready(function(){
		var hostName = $("#hostNames").val()
		if(hostName!='all'){
			//$("option[id='ds-dds02']").removeAttr("selected");
			$("option[value='hostName']").attr('selected','selected');	
			//$("option[id='hostName']").setAttribute("selected","true")
		}
		
	});  */
/* 	function myinit(){
		var hostName = $("#hostNames").val()
		if(hostName!='all'){
			//$("option[id='ds-dds02']").removeAttr("selected");
			$("option[value='hostName']").attr('selected','selected');
			$("option[value='hostName']").attr('selected','true');
			$("option[id='hostName']").attr("selected","true");
			$("option[id='hostName']").attr('selected','selected');	
		}
	} */
	/*  window.onload = function(){
		var hostName = $("#hostNames").val();
		debugger
		if(hostName!='all'){
	       /*  $("[value=]"+hostName).attr('selected','selected')
			$("#hostName").attr('selected','selected');	
			$("#hostName").attr('selected','true');	 */
			

	

	
	</script>
  </head>
  
  <body >
  <ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dt/distribute/list">分发定义列表</a></li>
		 <li><a href="${ctx}/dt/distribute/add">新增分发类型</a></li> 
	</ul>
	<form:form id="form1" action="${ctx}/dt/distribute/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>数据id：</label>
		<input id="jobName" name="dataId" type="text" htmlEscape="false" maxlength="50" 
		class="input-medium" value="${dataId}"/>
		<label>主机名：</label>
		<input id = "hostNames"type="hidden" value="${hostN}"/>
		<select id="hostName" name="hostName" type="text" htmlEscape="false" maxlength="50" 
		class="input-medium" >
		<%-- <c:if test="${hostN!='all'}">
			<option value="${hostN}" selected = "selected">${hostN}</option>		
		</c:if> 
			<c:if test="${hostN=='all'}">
			<option value="all" selected = "selected">全部</option>		
			</c:if>  --%>
		<c:forEach items="${dList}" var="keyword" varStatus="id">
		 <c:if test="${keyword=='all'}">
				<option id="${keyword}" value="${keyword}"  >全部</option>			
			</c:if>
			<c:if test="${keyword!='all'}">
			    <c:if test="${keyword==hostN}">
				   <option id="${keyword}" value="${keyword}"  selected  >${keyword} </option>		
				</c:if> 
				 <c:if test="${keyword!=hostN}">
				   <option id="${keyword}" value="${keyword}"  >${keyword} </option>		
				</c:if> 	
			</c:if>  																						
		</c:forEach>
		</select>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
	</form:form>
	<tags:message content="${message}"/>
   	<div>
   		<table id="tablelist" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
   			<thead>
   				 <!-- <th style="text-align: center;">ID</th> -->
   				<th style="text-align: center;">数据ID</th>
   				<th style="text-align: center;">接口类型</th>
   				<th style="text-align: center;">数据格式</th>
   				<th style="text-align: center;">命名格式</th>
   				<th style="text-align: center;">时间格式</th>
   				<th style="text-align: center;">推送目标路径</th>
   				<th style="text-align: center;">检索文件路径</th>
   				<th style="text-align: center;">主机名</th>
   				<th style="text-align: center;">时间跨度</th>
   				<!-- <th style="text-align: center;">时间跨度单位</th> -->
   				<th style="text-align: center;">时间延迟</th>
   				<!-- <th style="text-align: center;">时间延迟单位</th> -->
   				<th style="text-align: center;">延迟分钟</th>
   				<th style="text-align: center;">执行频率</th>
   				<!-- <th style="text-align: center;">执行频率时间单位</th> -->
   				<th style="text-align: center;">是否重做</th>
   				<th width="10%" style="text-align: center;">操作</th> 
   			</thead>
   			<tbody>
   				<c:forEach items="${page.list}" var="col">
   					<tr>
   						 <%-- <td>${col.id}</td>  --%>
   					    <td>${col.dataId}</td>
   						<td>${col.interfaceType}</td>
   						<td>${col.dataFormat}</td>
   						<td>${col.nameFormat }</td>
   						<td>${col.timeFormat}</td>
   						<td>${col.pushTargetPath }</td>
   						<td>${col.searchTargetPath}</td>
   						<td>${col.hostName}</td>
   						<td>${col.timeSpan}
   						<c:if test="${col.spanUnit =='Second'}">秒</c:if>
						<c:if test="${col.spanUnit =='Minute'}">分</c:if>
						<c:if test="${col.spanUnit =='Hour'}">时</c:if>
						<c:if test="${col.spanUnit =='Day'}">天</c:if>
						<c:if test="${col.spanUnit =='Ten'}">旬</c:if>
						<c:if test="${col.spanUnit =='Month'}">月</c:if>
						<c:if test="${col.spanUnit =='Year'}">年</c:if>
   						</td>
   						<td>${col.delay}
   						<c:if test="${col.delayUnit =='Second'}">秒</c:if>
						<c:if test="${col.delayUnit =='Minute'}">分</c:if>
						<c:if test="${col.delayUnit =='Hour'}">时</c:if>
						<c:if test="${col.delayUnit =='Day'}">天</c:if>
						<c:if test="${col.delayUnit =='Ten'}">旬</c:if>
						<c:if test="${col.delayUnit =='Month'}">月</c:if>
						<c:if test="${col.delayUnit =='Year'}">年</c:if>
   						</td>
   						<td>${col.delayMinute}</td>
   						<td>${col.timeRate}
   						<c:if test="${col.rateUnit =='Second'}">秒</c:if>
						<c:if test="${col.rateUnit =='Minute'}">分</c:if>
						<c:if test="${col.rateUnit =='Hour'}">时</c:if>
						<c:if test="${col.rateUnit =='Day'}">天</c:if>
						<c:if test="${col.rateUnit =='Ten'}">旬</c:if>
						<c:if test="${col.rateUnit =='Month'}">月</c:if>
						<c:if test="${col.rateUnit =='Year'}">年</c:if>
   						</td>
   						<td>
   						<c:if test="${col.isRedo ==0}">否</c:if>
						<c:if test="${col.isRedo ==1}">是</c:if>
   						</td>
   						<td style="text-align: center;">
   						<a href="${ctx}/dt/ddsSearchCondInfo/slist?dataId=${col.dataId }">检索条件配置</a>
   						<br>
   						<a href="${ctx}/dt/ddsFtpInfo/flist2?dataId=${col.dataId }">ftp配置</a>
   						<br>
   						<a href="${ctx}/dt/ddsSearchRecordController/list?dataId=${col.dataId }">查看回调记录</a>
   						<br>  
   						<a href="${ctx}/dt/DdsPushFileRecordController/list?dataId=${col.dataId }&fileName=${col.nameFormat }">查看推送记录</a>
   						<br>  
   						<a href="${ctx}/dt/distribute/edit?Id=${col.id }">编辑</a>
   						&nbsp;&nbsp;&nbsp;
   						<c:if test="${col.invalid ==1}">
   						<a style="color:red;"  href="${ctx}/dt/distribute/delete?Id=${col.id }" onclick="return confirmx('确认要把该记录改为有效吗？', this.href)">有效</a>  											
   						</c:if>
   						<c:if test="${col.invalid ==0}">
   						<a  href="${ctx}/dt/distribute/delete?Id=${col.id }" onclick="return confirmx('确认要把该记录改为无效吗？', this.href)">无效</a>   						
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
