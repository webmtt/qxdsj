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
	src="${ctxStatic}/ultra/js/statistics/statisticsCimissAccessTotal.js?ver=4"></script>
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/ultra/css/statistics/statisticsCimissAccess.css?ver=1" />
<style type="text/css">
.chartsTitle{width: 100%;height: 35px;line-height: 35px;border-bottom: 1px solid #c1c7d5;}
.change{float: right;margin-right: 10px;color: #057fdd;cursor: pointer;}
.change2{float: right;margin-right: 20px;color: #057fdd;}
 .datagrid-mask{
		position:absolute;
		left:0;
		top:0;
		width:100%;
		height:100%;
		background:#ccc;
		opacity:0.3;
		filter:alpha(opacity=30);
		display:none;
	}
	.datagrid-mask-msg{
		position:absolute;
		top:50%;
		margin-top:-20px;
		width:auto;
		height:16px;
		padding:12px 5px 10px 30px;
		/* background:#fff url('img/pagination_loading.gif') no-repeat scroll 5px 10px;
		border:2px solid #6593CF; */
		color:#222;
		display:none;
	}
	.WdateSearch{
		width:115px;
	}
</style>
<script>
    var ctx='${ctx}';
	var tableData, //table data
	end_time, //
	start_time, statUnit, statYear, chart; //chart组件

	function testx(content) {
		var form = $("<form>");
		form.attr('style', 'display:none');
		form.attr('target', '');
		form.attr('method', 'post');
		form.attr('action', '${ctx}/statistics/statisticsCimissAccess/exporExcel');

		var input1 = $('<input>');
		input1.attr('type', 'hidden');
		input1.attr('name', 'jsonStr');
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
						<li><a
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
						<li class="active"><a
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
							<h3>气象数据统计平台 - CIMISS访问量统计</h3>
							<div class="form">
								<div class="inputs">
									<strong>统计级别:</strong> 
									<input name="level" type="radio" checked="checked" value="1" onclick="changeTime(1)"/>国家级
									<input name="level" type="radio" value="2" onclick="changeTime(2)"/>省级
								</div>
							</div>
							<div class="form">
								<div class="inputs">
									<strong>统计维度:</strong>
									<input name="sort" type="radio" checked="checked" value="1" />按单位
									<input name="sort" type="radio" value="2" />按资料
								</div>
							</div>
							<!-- <div class="form">
								<div class="inputs" id="0">
									<strong>统计时间年:   </strong> <input id="startTime1" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="WdateSearch"/>
									至 <input id="endTime1" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="WdateSearch"/> 
									<strong>对比时间年:   </strong> <input id="startTime2" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="WdateSearch"/>
									至 <input id="endTime2" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" class="WdateSearch"/> <a  href="#"
										class="easyui-linkbutton btn2"
										data-options="iconCls:'icon-search',plain:true">确定查询</a>
										<span class="btnTip" style="font-size:12px;"><font
										color="#E65221"> 查询日期不能为空！</font></span>
										<span class="btnTipp" style="font-size:12px;"><font
										color="#E65221"> 开始日期不能大于结束日期！</font></span>
								</div>
							</div> -->
							<div class="form">
								<div class="inputs" id="1">
									<strong>统计时间月:   </strong> <input id="startTime1" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="WdateSearch"/>
									至 <input id="endTime1" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="WdateSearch"/> 
									<strong>对比时间月:   </strong> <input id="startTime2" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="WdateSearch"/>
									至 <input id="endTime2" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" class="WdateSearch"/> <a  href="#"
										class="easyui-linkbutton btn2"
										data-options="iconCls:'icon-search',plain:true">确定查询</a>
										<span class="btnTip" style="font-size:12px;"><font
										color="#E65221"> 查询日期不能为空！</font></span>
										<span class="btnTipp" style="font-size:12px;"><font
										color="#E65221"> 开始日期不能大于结束日期！</font></span>
								</div>
							</div>
							<div class="form">
								<div class="inputs" id="2">
									<strong>统计时间年月日:   </strong> <input id="startTime1" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="WdateSearch"/>
									至 <input id="endTime1" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="WdateSearch"/> 
									<strong>对比时间年月日:   </strong> <input id="startTime2" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="WdateSearch"/>
									至 <input id="endTime2" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" class="WdateSearch"/> <a  href="#"
										class="easyui-linkbutton btn2"
										data-options="iconCls:'icon-search',plain:true">确定查询</a>
										<span class="btnTip" style="font-size:12px;"><font
										color="#E65221"> 查询日期不能为空！</font></span>
										<span class="btnTipp" style="font-size:12px;"><font
										color="#E65221"> 开始日期不能大于结束日期！</font></span>
								</div>
							</div>
								<div class="clearing"></div>
							</div>
						</div>
						<div class="reports">
							<div class="netAccessTop"
								style="margin-top: 10px;height: 850px;" id="nativeAccess">
								<input hidden="hidden" id="flag" value="0">
								<div class="chartsTitle">
									<!-- <div class="change" onclick="test()">测试</div> -->
									<div class="change" onclick="download()">下载</div>
									<div class="change" onclick="TableOrChar()">图表切换</div>
									<div class="change2">
										<input type="radio" id="radioReset" name="flag" checked="checked" value="1" onclick="changeBar()">饼图
										<input type="radio" name="flag" value="2" onclick="changeBar()">柱状图
									</div>
								</div>
								<div id="char_1" class="pie" style="width:45%;float:left;height: 300px;margin-left: 5px;margin-top: 30px;"></div>
								<div id="char_2" class="pie" style="width:45%;float:left;height: 300px;margin-left: 5px;margin-top: 30px;"></div>
								<div id="char_3" class="pie" style="width:45%;float:left;height: 300px;margin-left: 5px;margin-top: 30px;"></div>
								<div id="char_4" class="pie" style="width:45%;float:left;height: 300px;margin-left: 5px;margin-top: 30px;"></div>
								<div id="flashcontent1" class="column" style="display:none;clear: both;" ></div>
								<div id="flashcontent2" class="column" style="display:none;clear: both;" ></div>
								<div id="tableDiv" class="tableDiv" style="display:none" style="margin-top: 10px;width:100%;">
									<table class="tlists" style="display:none">
									</table>
									<div style="margin-right: 20px;float: right;">
										<span style="font-size: 12px;">(后两列为对比数据)</span>
									</div>
									
									
								</div>
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