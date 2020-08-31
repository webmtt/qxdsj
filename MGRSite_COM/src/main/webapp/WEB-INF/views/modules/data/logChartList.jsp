<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>日志管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript"
	src="${ctxStatic}/script/Highcharts4.0.3/highcharts.js"></script>
	<script type="text/javascript"
	src="${ctxStatic}/script/Highcharts4.0.3/modules/exporting.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/script/Highcharts4.0.3/modules/export-csv.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/script/Highcharts4.0.3/highcharts-3d.js"></script>
	<script type="text/javascript">
	var ctx = "${ctx}";
	var ctxStatic = "${ctxStatic}";
		$(function(){
			getChart();
		})
		function getchartOn(result){
			 $('#container').highcharts({
			        chart: {
			            type: 'column'
			        },
			        title: {
			            text: '语音统计柱形图'
			        },
			        xAxis: {
			            categories: result.list
			        },
			        yAxis: {
			            min: 0,
			            title: {
			                text: '统计总量'
			            },
			            stackLabels: {
			                enabled: true,
			                style: {
			                    fontWeight: 'bold',
			                    color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
			                }
			            }
			        },
			        legend: {
			            align: 'right',
			            x: -30,
			            verticalAlign: 'top',
			            y: 25,
			            floating: true,
			            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
			            borderColor: '#CCC',
			            borderWidth: 1,
			            shadow: false
			        },
			        tooltip: {
			            pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b><br/>',
			            shared: true
			        },
			        plotOptions: {
			            column: {
			                stacking: 'normal',
			                dataLabels: {
			                    enabled: true,
			                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
			                    style: {
			                        textShadow: '0 0 3px black'
			                    }
			                }
			            }
			        },
	                credits: {
	                    enabled: false
	                },
			        series: [{
			            name: '呼出',
			            data: result.list1
			        }, {
			            name: '呼损',
			            data: result.list2
			        }, {
			            name: '未接通',
			            data: result.list3
			        }, {
			            name: '接通',
			            data: result.list4
			        }]
			    });
		}
		function getChart(){
			var type=$("#DataClass option:checked").val();
			var startTime=$("#startTime").val();
			var endTime=$("#endTime").val();
			$.ajax({
				url:ctx+'/data/stat/getChartData',
				type:'POST',
				data:{'startTime':startTime,'endTime':endTime,'type':type},
				dataType:'json',
				success:function(result){
					getchartOn(result)
				}
			});
		}
		function changeData(){
			var type=$("#DataClass option:checked").val();
			$("#timeshow").empty();
			if(type=="day"){
				var startTime="${startTime}";
				var endTime="${endTime}";
				$("#timeshow").append("<label>开始日期：</label><input id='startTime' name='startTime' type='text' readonly='readonly' maxlength='30' class='input-small Wdate'"+
					" value='"+startTime+"' onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});\"/>"+
				" <label>结束日期：</label><input id='endTime' name='endTime' type='text' readonly='readonly' maxlength='30' class='input-small Wdate' "+
					" value='"+endTime+"' onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});\"/>");
			}else if(type=="month"){
				var startTime="${startTime2}";
				var endTime="${endTime2}";
				$("#timeshow").append("<label>开始日期：</label><input id='startTime' name='startTime' type='text' readonly='readonly' maxlength='30' class='input-small Wdate'"+
						" value='"+startTime+"' onclick=\"WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});\"/>"+
					" <label>结束日期：</label><input id='endTime' name='endTime' type='text' readonly='readonly' maxlength='30' class='input-small Wdate' "+
						" value='"+endTime+"' onclick=\"WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});\"/>");
			}else if(type=="year"){
				var startTime="${startTime3}";
				var endTime="${endTime3}";
				$("#timeshow").append("<label>开始日期：</label><input id='startTime' name='startTime' type='text' readonly='readonly' maxlength='30' class='input-small Wdate'"+
						" value='"+startTime+"' onclick=\"WdatePicker({dateFmt:'yyyy',isShowClear:false});\"/>"+
					" <label>结束日期：</label><input id='endTime' name='endTime' type='text' readonly='readonly' maxlength='30' class='input-small Wdate' "+
						" value='"+endTime+"' onclick=\"WdatePicker({dateFmt:'yyyy',isShowClear:false});\"/>");
			}
			getChart(type);
		}
	</script>
	<style type="text/css">
	#contentTable th{text-align:center;vertical-align:middle;}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		
	</ul>
		<div class="breadcrumb form-search">
		    <label>日期类型：</label>
		    <select id="DataClass" class="input-small" onChange="changeData(this)">
		        <option value="day">日</option>
		        <option value="month">月</option>
		        <option value="year">年</option>
		    </select>
		    <span id="timeshow">
				<label>开始日期：</label><input id="startTime" name="startTime" type="text" readonly="readonly" maxlength="30" class="input-small Wdate"
					value="${startTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<label>结束日期：</label><input id="endTime" name="endTime" type="text" readonly="readonly" maxlength="30" class="input-small Wdate"
					value="${endTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
		    </span>
			&nbsp;&nbsp;<input id="btnSubmit" style="width:80px;" class="btn btn-primary"  type="bottom" value="统计" onclick="getChart()"/>
		</div>
	<div class="">
	    <div id="container" style="min-width:400px;height:400px"></div>
	</div>
</body>
</html>