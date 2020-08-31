<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE>
<html>
<head>
<title>${fns:getConfig('productName')}</title>
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js"
	type="text/javascript"></script>
<link
	href="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.css"
	type="text/css" rel="stylesheet" />
<script
	src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.js"
	type="text/javascript"></script>
<script
	src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
<link href="${ctxStatic}/fancyBox/source/jquery.fancybox.css"
	type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/fancyBox/source/jquery.fancybox.js"
	type="text/javascript"></script>
<link rel="shortcut icon" href="${ctxStatic}/favicon.ico">
<link href="${ctxStatic}/images/shujufuwu/main.css" rel="stylesheet"
	type="text/css" media="screen" title="no title" charset="utf-8" />
<link href="${ctxStatic}/ultra/css/statistics/cstats.css" rel="stylesheet"
	type="text/css" media="screen" title="no title" charset="utf-8" />
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/script/jquery-easyui-1.2.6/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/script/jquery-easyui-1.2.6/themes/icon.css" />
<script type="text/javascript"
	src="${ctxStatic}/script/jquery-easyui-1.2.6/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/script/jquery-easyui-1.2.6/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/script/Highcharts4.0.3/highcharts.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/script/Highcharts4.0.3/modules/exporting.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/script/Highcharts4.0.3/modules/export-csv.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/script/EChart/echarts-plain-original-map.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/script/Highcharts4.0.3/highcharts-3d.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/ultra/js/statistics/statisticsAccessInfoOrg.js?ver=2"></script>
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/ultra/css/statistics/statisticsAccess.css?ver=1" />
<style type="text/css">
</style>
</div>
<script>
    var ctx='${ctx}';
	var tableData, //table data
	chartData, //chart data
	end_time, //
	start_time, statUnit, statYear, chart; //chart组件
	function onSelect() {
		var start_time = $('#startTime').datebox("getValue").replace(/-/g, "");
		var end_time = $('#endTime').datebox("getValue").replace(/-/g, "");
		if (startTime != "" && end_time != "") {
			if ((parseInt(start_time) - parseInt(end_time)) > 0) {
				$("#btn2")
						.after(
								"<span id='error' style='color:red;'>开始日期不能大于结束日期。</span>");
			}
		}
	}

	function testx(content) {
		var form = $("<form>");
		form.attr('style', 'display:none');
		form.attr('target', '');
		form.attr('method', 'post');
		form.attr('action', '${ctx}/sys/userInfomation/exporExcel');

		var input1 = $('<input>');
		input1.attr('type', 'hidden');
		input1.attr('name', 'content');
		input1.attr('value', encodeURIComponent(content));
		$('body').append(form);
		form.append(input1);

		form.submit();
		form.remove();

	}

	function onShowPanel() {
		var $error = $("#error");
		if ($error.length > 0) {
			$error.remove();
		}
	}
</script>
</head>
<body>
	<div id="main">
		<div id="header" class="navbar navbar-fixed-top">
			<div>
				<table align="center" border="0" cellpadding="0" cellspacing="0"
					width="960">
					<tbody>
						<tr>
							<td>
								<div style="position:relative;width:960px;height:114px;">
									<img src="${ctxStatic}/images/banner1.jpg" height="114"
										width="960px">
									<div
										style="position:absolute;width:960px;height:114px;z-indent:2;left:700px;top:10px;">
										<a> 您好, <shiro:principal property="name" />
										</a> &nbsp;|&nbsp; <a href="${ctx}/login" title="返回管理平台">返回管理平台</a>
										&nbsp;|&nbsp; <a href="${ctx}/logout" title="退出登录">退出</a>
									</div>
								</div></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="div-top">
				<div class="nav-collapse" id="mynav">
					<ul class="nav">
						<li class="active"><a
							href="${ctx}/statistics/statisticsAccessInfo/statisticsAccessInfoTotalList">网站访问情况</a>
						</li>
						<li><a
							href="${ctx}/statistics/statisticsDownloadTotal/statisticsDownloadTotalList">下载量统计</a>
						</li>
						<li><a
							href="${ctx}/statistics/statisticsOrderTotal/statisticsOrderTotalList">订单量统计</a>
						</li>
						<li><a
							href="${ctx}/statistics/statisticsUserInfo/statisticsUserInfoTotalList">用户信息统计</a>
						</li>
						<li><a
							href="${ctx}/statistics/statisticsCimissAccess/statisticsView">CIMISS访问统计</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div id="middle" class="middle">
			<div class="content">
				<div class="col">
					<form id="typeAction"></form>
					<form name="calendar" method="get">
						<div class="filters">
							<h3>气象数据统计平台 - 网站访问分单位情况情况统计</h3>
							<div class="form">
								<div class="inputs">
									<strong>统计类型</strong> <input id="type" />
									<strong>选择单位</strong> 
										<input id="province" class="province" style="width:100px;"/> 
									</span>
								</div>
							</div>
							<div class="form">
								<div class="inputs">
									<strong>起始年月   </strong> <input id="startTime1" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="WdateSearch"/>
									至 <input id="endTime1" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="WdateSearch"/> <a id="btn1" href="#"
										class="easyui-linkbutton"
										data-options="iconCls:'icon-search',plain:true">确定查询</a>
										<span class="btnTip" style="font-size:12px;"><font
										color="#E65221"> 查询日期不能为空！</font></span>
										<span class="btnTipp" style="font-size:12px;"><font
										color="#E65221"> 开始日期不能大于结束日期！</font>
										</span>
										<span class="btnTippp" style="font-size:12px;"><font 
										color="#E65221"> 请选择单位！</font>
										</span>
								</div>
								<div class="clearing"></div>
							</div>
							<div class="form">
								<div class="inputs">
									<strong>起始年月日   </strong> <input id="startTime2" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="WdateSearch"/>
									至 <input id="endTime2" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="WdateSearch"/> <a id="btn2" href="#"
										class="easyui-linkbutton"
										data-options="iconCls:'icon-search',plain:true">确定查询</a>
										<span class="btnTip" style="font-size:12px;"><font
										color="#E65221"> 查询日期不能为空！</font></span>
										<span class="btnTipp" style="font-size:12px;"><font
										color="#E65221"> 开始日期不能大于结束日期！</font>
										</span>
										<span class="btnTippp" style="font-size:12px;"><font 
										color="#E65221"> 请选择单位！</font>
										</span>
								</div>
								<div class="clearing"></div>
							</div>
						</div>
						<div class="reports">
							<h2 class="title01">
								<span class="ttl"></span>
							</h2>
							<div class="chart">
								<div id="flashcontent"></div>
							</div>
							<div class="simple-list">
								<table class="tlists"></table>
							</div>
						</div>
						<p class="back-top">
							<a href="#">返回顶部</a>
						</p>
					</form>
				</div>
			</div>
		</div>
		<div id="footer" class="footer">Copyright &copy;
			2015-${fns:getConfig('copyrightYear')} 业务管理站点</div>
</body>
</html>