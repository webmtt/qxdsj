<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>   
    <title>推送记录列表</title>
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
	/* $(function(){
		//var hostName = $("select[name='hostName'] option:selected").val();
		var hostName = $("#hostNames").val()
		if(hostName!='ds-dds02'){
			$("option[id='ds-dds02']").removeAttr("selected");
			$("option[id='hostName']").attr('selected','true');			
		}
		
	}); */
	function change(){
		
		//$("#${col.hostName}").attr("selected",'true');
		var hostName = $("select[name='hostName'] option:selected").val();
		//var flag = document.getElementById("${col.key}").selected;
		//var flag = $("select[name='hostName'] option:selected").selected;
		//if(flag){
			//$("input[id='nation']").removeAttr("checked");
			//$("input[id='nation1']").attr('checked','true');
			$("option[id='ds-dds02']").removeAttr("selected");
			$("option[value='hostName']").attr('selected','true');
		//}
		//$("opttion[id='${col.hostName}']").attr('selected','true');
	}
	
	</script>
  </head>
  
  <body>
  <ul class="nav nav-tabs">
  		<li ><a href="${ctx}/dt/distribute/list">分发定义列表</a></li>
		<li class="active"><a href="${ctx}/dt/DdsPushFileRecordController/list">进行中推送</a></li>
		 <li><a href="${ctx}/dt/DdsPushFileRecordController/expiredList ">历史推送</a></li> 
	</ul>
	<form:form id="form1" action="${ctx}/dt/DdsPushFileRecordController/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>ftpId：</label>
		<input id="ftpId" name="ftpId" type="text" htmlEscape="false" maxlength="50" 
		class="input-medium" value="${ftpId}"/>
		<label>文件名：</label>
		<input id="fileName" name="fileName" type="text" htmlEscape="false" maxlength="50" 
		class="input-medium" value="${fileName}"/>
		<label>推送状态：</label>
		<select id="pushStatus" name="pushStatus" type="text" htmlEscape="false" maxlength="50" 
		class="input-medium" >
		<option value=""<c:if test="${pushStatus =='3'}"> selected="selected" </c:if>>全部</option>
		<option value="0" <c:if test="${pushStatus =='0'}"> selected="selected" </c:if>> 待推送</option>
		<option value="1" <c:if test="${pushStatus =='1'}"> selected="selected" </c:if>>推送中</option>
		<option value="2" <c:if test="${pushStatus =='2'}"> selected="selected" </c:if>>推送成功</option>
		<option value="-2" <c:if test="${pushStatus =='-2'}"> selected="selected" </c:if>>推送失败</option>
		<%-- <c:if test="${hostName=='all'}">
			<option value="all" selected = "selected">全部</option>		
		</c:if>
		<c:if test="${hostName!='all'}">
			<option value="${hostName}" selected = "selected">${hostName}</option>		
		</c:if> --%>
		
		<%-- <c:forEach items="${dList}" var="col">
				<option id="${col.key}" value="${col.key}">${col.value}</option>
				
				<c:choose>
					<c:when  test="${col.key!='ds-dds02'}">
						<option id="${col.key}" value="${col.value}" >${col.value} </option>																
					</c:when> 
					<c:otherwise>	
						<option id="${col.key}" value="${col.value}"  selected = "selected">${col.value}</option>				
					</c:otherwise>					
				</c:choose> 
		</c:forEach> --%>
		</select>

		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
	</form:form>
	<tags:message content="${message}"/>
   	<div>
   		<table id="tablelist" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
   			<thead>
   				<!-- <th style="text-align: center;">ID</th>  -->
   				<th style="text-align: center;">ftpID</th>
   				<th style="text-align: center;">文件名</th>
   				<th style="text-align: center;">文件路径</th>
   				<th style="text-align: center;">生成时间</th>
   				<th style="text-align: center;">主机名</th>
   				<th style="text-align: center;">推送状态</th>
   				<th style="text-align: center;">推送开始时间</th>
   				<th style="text-align: center;">推送结束时间</th>
   				<th style="text-align: center;">推送次数</th>
   				<th style="text-align: center;">目标路径</th>
   				<!-- <th style="text-align: center;">文件名</th>
   				<th style="text-align: center;">文件路径</th> -->
   				<!-- <th style="text-align: center;">时间延迟单位</th> -->
   				<!-- <th style="text-align: center;">执行频率</th> -->
   				<!-- <th style="text-align: center;">执行频率时间单位</th> -->
   				<!-- <th style="text-align: center;">是否重做</th> -->
   				<!-- <th width="10%" style="text-align: center;">操作</th> --> 
   			</thead>
   			<tbody>
   				<c:forEach items="${page.list}" var="col">
   					<tr>
   						<%-- <td>${col.id}</td>  --%>
   					    <td>${col.ftpId}</td>
   						<td>${col.fileName}</td>
   						<td>${col.filePath}</td>
   						<td>${col.createdTime }</td>
   						<td>${col.hostName }</td>
   						<td>
   							<c:if test="${col.pushStatus =='0'}">待推送</c:if>
							<c:if test="${col.pushStatus =='1'}">推送中</c:if>
							<c:if test="${col.pushStatus =='2'}">推送成功</c:if>
							<c:if test="${col.pushStatus =='-2'}">推送失败</c:if>							
   						</td>
   						<td>${col.pushBeginTime }</td>
   						<td>${col.pushEndTime}</td>
   						<%-- <td>
   							<c:if test="${col.transStatus ==0}">待转换</c:if>
							<c:if test="${col.transStatus ==1}">成功</c:if>
   						</td> --%>
   						<td>${col.pushNum}</td>
   						<td>${col.targetPath}</td>
   						<%-- <td style="text-align: center;">
   						<a href="${ctx}/dt/ddsSearchCondInfo/slist?dataId=${col.dataId }">检索条件配置</a>
   						<br>
   						<a href="${ctx}/dt/ddsFtpInfo/flist2?dataId=${col.dataId }">ftp配置</a>
   						<br>
   						<a href="${ctx}/dt/bpsJobPlan/jlist?jobName=${col.dataId }">推送信息配置</a>
   						<br> 
   						<a href="${ctx}/dt/distribute/edit?Id=${col.id }">编辑</a>
   						&nbsp;&nbsp;&nbsp;
   						<c:if test="${col.invalid ==1}">
   						<a style="color:red;"  href="${ctx}/dt/distribute/delete?Id=${col.id }" onclick="return confirmx('确认要把该记录改为有效吗？', this.href)">有效</a>  											
   						</c:if>
   						<c:if test="${col.invalid ==0}">
   						<a  href="${ctx}/dt/distribute/delete?Id=${col.id }" onclick="return confirmx('确认要把该记录改为无效吗？', this.href)">无效</a>   						
   						</c:if>
   						</td> --%>
   					</tr>
   				</c:forEach>
   			</tbody>
   		</table>
   	</div>
   	 <div class="pagination">${page}</div> 
  </body>
</html>
