<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>   
    <title>会商直播</title>
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
		var begin=$("#begin").val();	
		var end=$("#end").val();
	/* 	if((begin==null||begin=="")&&(end!=null||end!="")){
			alert("开始时间不能为空");
			return false;
		}else if((begin!=null||begin!="")&&(end==null||end=="")){
			alert("结束时间不能为空");
			return false;
		}else if(end<begin){
			alert("结束时间不能小于开始时间");
			return false;
		} */
		if(end<begin){
			alert("结束时间不能小于开始时间");
			return false;
		} 
		$("#form1").attr("action","${ctx}/tvmeeting/meetingarrange/teleList?begin="+begin+"&end="+end);
		$("#form1").submit();
    	return false;
    }
	</script>
  </head>
  
  <body>
  <ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/tvmeeting/meetingarrange/teleList">会商列表</a>
		 <li><a href="${ctx}/tvmeeting/meetingarrange/teleAdd">新增会商</a></li> 
		 <li><a href="${ctx}/tvmeeting/meetingarrange/teleBatchAdd">批量新增会商</a></li> 
		</li>
	</ul>
	<form:form id="form1" action="${ctx}/tvmeeting/meetingarrange/teleList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
   		<div align="left">
			<label>开始日期：</label><input type="text" class="Wdate" id="begin" name="begin" value="${begin}"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
			<label>结束日期：</label><input type="text" class="Wdate" id="end" name="end" value="${end}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
			&nbsp;<input  id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page()"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
   	<div>
   		<table id="tablelist" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
   			<thead>
   				<th width="10%" style="text-align: center;">日期</th>
   				<th width="5%" style="text-align: center;">开始时间</th>
   				<th width="5%" style="text-align: center;">结束时间</th>
   				<th style="text-align: center;">会议名称</th>
   				<th style="text-align: center;">联系人</th>
   				<th style="text-align: center;">联系方式</th>
   				<th style="text-align: center;">内容</th>
   				 <th width="10%" style="text-align: center;">操作</th> 
   			</thead>
   			<tbody>
   				<c:forEach items="${page.list}" var="tele">
   					<tr>
   						<td style="text-align: center;">${tele.meetingDate }</td>
   						<td style="text-align: center;">${tele.beginTime }</td>
   						<td style="text-align: center;">${tele.endTime }</td>
   						<td>${tele.meetTitle}</td>
   						<td>${tele.contacts }</td>
   						<td>${tele.contactWay }</td>
   						<td>${tele.content }</td>
   						<td style="text-align: center;"><a href="${ctx}/tvmeeting/meetingarrange/teleUpdate?MeetingId=${tele.meetingId }">修改</a>&nbsp;&nbsp;<a href="${ctx}/tvmeeting/meetingarrange/teleDel?MeetingId=${tele.meetingId }&begin=${begin}&end=${end}" onclick="return confirmx('确认要删除该记录吗？', this.href)">删除</a></td>
   					</tr>
   				</c:forEach>
   			</tbody>
   		</table>
   	</div>
   	 <div class="pagination">${page}</div> 
  </body>
</html>
