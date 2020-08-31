<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>   
    <title>回调记录列表</title>
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
		<li ><a href="${ctx}/dt/ddsSearchRecordController/list">进行中回调</a></li>
		 <li class="active"><a href="${ctx}/dt/ddsSearchRecordController/expiredList?dataId=${dataId} ">历史回调</a></li> 
	</ul>
	<form:form id="form1" action="${ctx}/dt/ddsSearchRecordController/expiredList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>数据id：</label>
		<input id="dataId" name="dataId" type="text" htmlEscape="false" maxlength="50" 
		class="input-medium" value="${dataId}"/>
		<label>时间条件开始：</label>
		<input id="timeCondBegin" name="timeCondBegin" type="text" htmlEscape="false" maxlength="50" 
		class="input-medium" value="${timeCondBegin}"/>
		<label>数据回调状态：</label>
		<select id="retriveStatus" name="retriveStatus" type="text" htmlEscape="false" maxlength="50" 
		class="input-medium" >
		<option value=""<c:if test="${retriveStatus =='3'}"> selected="selected" </c:if>>全部</option>
		<%--<option value="0" <c:if test="${retriveStatus =='0'}"> selected="selected" </c:if>> 待回调</option>
		 <option value="1" <c:if test="${retriveStatus =='1'}"> selected="selected" </c:if>>回调中</option>--%>
		<option value="2" <c:if test="${retriveStatus =='2'}"> selected="selected" </c:if>>回调成功</option> 
		<option value="-1" <c:if test="${retriveStatus =='-1'}"> selected="selected" </c:if>>接口无数据</option>
		<option value="-2" <c:if test="${retriveStatus =='-2'}"> selected="selected" </c:if>>回调失败</option>
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
   				<!-- <th style="text-align: center;">ID</th> -->
   				<th style="text-align: center;">数据ID</th>
   				<th style="text-align: center;">时间条件开始</th>
   				<th style="text-align: center;">时间条件结束</th>
   				<th style="text-align: center;">生成时间</th>
   				<th style="text-align: center;">主机名</th>
   				<th style="text-align: center;">数据回调状态</th>
   				<th style="text-align: center;">数据回调开始时间</th>
   				<th style="text-align: center;">数据回调结束时间</th>
   				<th style="text-align: center;">转换状态</th>
   				<th style="text-align: center;">重做次数</th>
   				<th style="text-align: center;">文件名</th>
   				<th style="text-align: center;">文件路径</th>
   				<!-- <th style="text-align: center;">时间延迟单位</th> -->
   				<!-- <th style="text-align: center;">执行频率</th> -->
   				<!-- <th style="text-align: center;">执行频率时间单位</th> -->
   				<!-- <th style="text-align: center;">是否重做</th> -->
   				<!-- <th width="10%" style="text-align: center;">操作</th> --> 
   			</thead>
   			<tbody>
   				<c:forEach items="${page.list}" var="col">
   					<tr>
   						<%-- <td>${col.id}</td> --%>
   					    <td>${col.dataId}</td>
   						<td>${col.timeCondBegin}</td>
   						<td>${col.timeCondEnd}</td>
   						<td>${col.createdTime }</td>
   						<td>${col.hostName }</td>
   						<td>
   							<c:if test="${col.retriveStatus =='0'}">待回调</c:if>
							<c:if test="${col.retriveStatus =='1'}">回调中</c:if>
							<c:if test="${col.retriveStatus =='2'}">回调成功</c:if>
							<c:if test="${col.retriveStatus =='-1'}">接口无数据</c:if>
							<c:if test="${col.retriveStatus =='-2'}">回调失败</c:if>							
   						</td>
   						<td>${col.retriveBeginTime }</td>
   						<td>${col.retriveEndTime}</td>
   						<td>
   							<c:if test="${col.transStatus ==0}">待转换</c:if>
							<c:if test="${col.transStatus ==1}">成功</c:if>
   						</td>
   						<td>${col.redoCount}</td>
   						<td>${col.fileName}</td>
   						<td>${col.filePath}</td>
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
