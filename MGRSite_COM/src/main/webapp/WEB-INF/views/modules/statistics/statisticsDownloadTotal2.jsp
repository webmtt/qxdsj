<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE>
<html>
<head>
<title>${fns:getConfig('productName')}</title>
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<link href="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/bootstrap/2.3.1/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/list/list.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/select2/select2.js" type="text/javascript"></script>
<script src="${ctxStatic}/select2/select2_locale_zh-CN.js" type="text/javascript"></script>
<link href="${ctxStatic}/select2/select2.css" type="text/css" rel="stylesheet" />
<link href="${ctxStatic}/fancyBox/source/jquery.fancybox.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/fancyBox/source/jquery.fancybox.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jeesite.min.js" type="text/javascript"></script>
<link rel="shortcut icon" href="${ctxStatic}/favicon.ico">
<link href="${ctxStatic}/images/shujufuwu/main.css" rel="stylesheet" type="text/css" media="screen" title="no title" charset="utf-8" />
<link href="${ctxStatic}/ultra/css/statistics/cstats.css" rel="stylesheet"
	type="text/css" media="screen" title="no title" charset="utf-8" /><link rel="stylesheet" type="text/css" href="${ctxStatic}/script/jquery-easyui-1.2.6/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/script/jquery-easyui-1.2.6/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/ultra/css/statistics/cstats.css" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/ultra/css/statistics/statisticsDownloadTotal.css" />

<script type="text/javascript" src="${ctxStatic}/script/jquery-easyui-1.2.6/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/script/jquery-easyui-1.2.6/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctxStatic}/script/Highcharts4.0.3/highcharts.js"></script>
<script type="text/javascript" src="${ctxStatic}/script/Highcharts4.0.3/modules/exporting.js"></script>
<script type="text/javascript" src="${ctxStatic}/script/Highcharts4.0.3/modules/export-csv.js"></script>
<script type="text/javascript" src="${ctxStatic}/script/EChart/echarts-plain-original-map.js"></script>
<script type="text/javascript" src="${ctxStatic}/script/Highcharts4.0.3/highcharts-3d.js"></script>
<script type="text/javascript" src="${ctxStatic}/images/shujufuwu/swfobject.js"></script>
<script type="text/javascript" src="${ctxStatic}/ultra/js/statistics/statisticsDownloadTotal.js"></script>
<script type="text/javascript">
 var ctx='${ctx}';
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
						<li class="active"><a
							href="${ctx}/statistics/statisticsDownloadTotal/statisticsDownloadTotalList">下载量统计</a>
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
							<h3>气象数据统计平台 -下载量情况统计</h3>
							<div class="form">
								<div class="inputs">
									<strong>统计类型</strong> <input id="type" /> 
									<!--  -->
									<strong>统计方式</strong>	<input id="statUnit" /> 
									<span id="isShowYear"> 
										<label>请选择统计年份:</label>
										<input class="year" />
									</span> 
									<a id="btn3" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">
										确定查询
									</a>
									<span class="btnTip" style="font-size:12px;">
										<font color="#E65221"> 查询年份不能为空！</font>
									</span>
								</div>
							</div>
							<div class="form">
								<div class="inputs">
									<strong>选择时段</strong> 
										<span>
											<label>年份:</label>
											<input	class="year" />
										</span> 
									 	<span>
									 		<label>月份:</label> 
									 		<input id="month" />
									 	</span> 
									 	<span style="padding:0px 0px 0px 1px;"> 
									 		<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">确定查询</a>
									 		<span class="btnTip2" style="font-size:12px;">
									 		 	<font color="#E65221"> 查询年份、月份不能为空！</font>
									 		</span>	
										</span>
								</div>
							</div>
							<div class="form">
								<div class="inputs">
									<strong>自定义起始年月 </strong> 
										<input id="startTime" type="text" />至<input id="endTime" type="text" /> 
										<a id="btn2" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">确定查询</a>
										<span class="btnTip" style="font-size:12px;">
											<font color="#E65221"> 查询年份不能为空! </font>
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