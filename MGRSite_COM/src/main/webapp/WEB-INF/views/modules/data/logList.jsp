<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>日志管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
	<style type="text/css">
	#contentTable th{text-align:center;vertical-align:middle;}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
	</ul>
	<form:form id="searchForm" action="${ctx}/data/stat/logList" method="post" class="breadcrumb form-search">
		<div>
			<label>开始日期：</label><input id="startTime" name="startTime" type="text" readonly="readonly" maxlength="30" class="input-medium Wdate"
				value="${startTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			<label>结束日期：</label><input id="endTime" name="endTime" type="text" readonly="readonly" maxlength="30" class="input-medium Wdate"
				value="${endTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="统计"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		   <tr>
		      <th rowspan="2">坐席/工号</th><th colspan="6">呼入</th><th colspan="5">呼出</th>
		   </tr>
		   <tr>
		      <th>呼入次数</th><th>接听次数</th><th>应答率</th><th>平均振铃时长(秒)</th><th>平均接听时长(秒)</th><th>接听总时长(小时)</th>
		      <th>呼出次数</th><th>接通次数</th><th>应答率</th><th>平均呼出时长(秒)</th><th>呼出总时长(小时)</th>
		   </tr>
		</thead>
		<tbody>
		    <c:forEach items="${listall}" var="Stat">
			<tr>
			    <td>${Stat.servicer}</td>
				<td>${Stat.total1}</td>
				<td>${Stat.total2}</td>
				<td>${Stat.total3}%</td>
				<td>${Stat.total4}</td>
				<td>${Stat.total5}</td>
				<td>${Stat.total6}</td>
				<td>${Stat.total7}</td>
				<td>${Stat.total8}</td>
				<td>${Stat.total9}%</td>
				<td>${Stat.total10}</td>
				<td>${Stat.total11}</td>
			</tr>
		</c:forEach>
		<c:forEach items="${totallist}" var="Stat">
			<tr>
			    <td>${Stat.servicer}</td>
				<td>${Stat.total1}</td>
				<td>${Stat.total2}</td>
				<td>${Stat.total3}%</td>
				<td>${Stat.total4}</td>
				<td>${Stat.total5}</td>
				<td>${Stat.total6}</td>
				<td>${Stat.total7}</td>
				<td>${Stat.total8}</td>
				<td>${Stat.total9}%</td>
				<td>${Stat.total10}</td>
				<td>${Stat.total11}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>