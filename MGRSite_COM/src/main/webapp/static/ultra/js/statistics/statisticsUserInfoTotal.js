$(function() {
	// 初始化统计类型下拉框
	$("#type").combobox({
						valueField : "typeValue",
						textField : 'typeName',
						value : "1",
						panelHeight : "auto",
						onSelect : function(record) {
							var typeValue = record.typeValue;
							if (typeValue == '1') {
								$("#typeAction")
										.attr("action",
												ctx+"/statistics/statisticsUserInfo/statisticsUserInfoTotalList");
							}
							if (typeValue == '2') {
								$("#typeAction")
										.attr("action",
												ctx+"/statistics/statisticsUserInfo/statisticsUserInfoOrgList");
							}
							if (typeValue == '3') {
								$("#typeAction")
										.attr("action",
												ctx+"/statistics/statisticsUserInfo/statisticsUserInfoProList");
							}
							$("#typeAction").submit();
						},
						data : [ {
							typeValue : "1",
							typeName : '总量统计'
						},{
							typeValue : "2",
							typeName : '大院用户单位统计'
						},{
							typeValue : "3",
							typeName : '分省统计'
						}]
					});
	$(".btnTip").hide();
	$(".btnTipp").hide();
	//初始化折线图
	getData(start_time, end_time, statUnit, statYear);
	// 初始化数据表格
	showtable();
	// 年
	$("#btn1").on(
			"click",
			function() {
				start_time = $('#startTime1').val().replace(/-/g, "");
				end_time = $('#endTime1').val().replace(/-/g, "");
				var temp=compareTime(start_time,end_time,0);
				if(temp!=1){
					return false;
				}
				$(".btnTip:eq(0)").hide();
				$(".btnTipp:eq(0)").hide();
				statUnit = null;
				statYear = null;
				getData(start_time, end_time, "1", statYear);
				$('.tlists').datagrid('load', {
					start_time : start_time,
					end_time : end_time,
					statUnit : "1",
					statYear : null
				});
				// 重绘图表
				chart.xAxis[0].setCategories(chartData.time);
				chart.xAxis[0].setOptions({
					tickInterval : Math.round(chartData.time.length / 6)
				});
				chart.series[0].remove(false);
				chart.addSeries({
					name : '注册用户',
					type : 'column',
					color: '#4572A7',
					yAxis: 0,
					data : chartData.sum
				});
				chart.redraw();

			});

	//月
	$("#btn2").on("click", function() {
		start_time = $('#startTime2').val().replace(/-/g, "");
		end_time = $('#endTime2').val().replace(/-/g, "");
		var temp=compareTime(start_time,end_time,1);
		if(temp!=1){
			return false;
		}
		$(".btnTip:eq(1)").hide();
		$(".btnTipp:eq(1)").hide();
		statUnit = "2";
		statYear = null;
		getData(start_time, end_time, statUnit, statYear);
		$('.tlists').datagrid('load', {
			start_time : start_time,
			end_time : end_time,
			statUnit : "2",
			statYear : null
		});
		// 重绘图表
		chart.xAxis[0].setCategories(chartData.time);
		chart.xAxis[0].setOptions({
			tickInterval : Math.round(chartData.time.length / 6)
		});
		chart.series[0].remove(false);
		chart.addSeries({
			name : '注册用户',
			type : 'column',
			color: '#4572A7',
			yAxis: 0,
			data : chartData.sum
		});
		chart.redraw();
	});
	//日
	$("#btn3").on("click", function() {
			start_time = $('#startTime3').val().replace(/-/g, "");
			end_time = $('#endTime3').val().replace(/-/g, "");
			var temp=compareTime(start_time,end_time,2);
			if(temp!=1){
				return false;
			}
			$(".btnTip:eq(2)").hide();
			$(".btnTipp:eq(2)").hide();
			statUnit = "3";
			getData(start_time, end_time, statUnit, statYear);
			$('.tlists').datagrid('load', {
				start_time : start_time,
				end_time : end_time,
				statUnit : statUnit,
				statYear : statYear
			});
			// 重新加载chart数据
			chart.xAxis[0].setCategories(chartData.time);
			chart.xAxis[0].setOptions({
				tickInterval : Math.round(chartData.time.length / 9)
			});
			chart.series[0].remove(false);
			chart.addSeries({
				name : '注册用户',
				type : 'column',
				color: '#4572A7',
				yAxis: 0,
				data : chartData.sum,
				pointWidth : 30
			});
			chart.redraw();
	});
	/*
	 * summary:季度radio控制 description:选择季度或下半年、上半年时，就禁用选择月份下拉框
	 */

	$('#flashcontent').highcharts({
		credits : {
			enabled : false
		// 不显示highCharts版权信息
		},

		title : {
			text : '注册用户统计图表'
		},
		colors : [ '#7CB5EC' ],
		plotOptions : {
			lineWidth : 0.5
		},

		xAxis : {
			tickInterval : Math.round(chartData.time.length / 9),
			categories : chartData.time
		},
		yAxis: [{
	    	labels: {
	            format: '{value}',//格式化Y轴刻度
	            style: {
	                color: '#89A54E'
	            }
	        },
	        title: {
	            text: '注册用户（人数)',
	            style: {
	                color: '#89A54E'
	            }
	        }
	        ,
	        min:0
	    }],
		tooltip : {
			crosshairs : true,
			shared : true
		},
		series : [ {
			name : '用户',
			color: '#4572A7',
		    yAxis: 0,
			data : chartData.sum,
			tooltip: {
	            valueSuffix: '人（次)'
	        }
		}]
	});
	chart = $('#flashcontent').highcharts();
});
//获取table和chart的数据
function getData(start_time, end_time, statUnit, statYear) {
	$.ajax({
		url : ctx+'/statistics/statisticsUserInfo/statTotalPictureByTime',
		type : 'post',
		cache : false,
		dataType : 'json',
		async : false,
		data : {
			"start_time" : start_time == "" ? "" : start_time,
			"end_time" : end_time == "" ? "" : end_time,
			"statUnit" : statUnit == "" ? "" : statUnit,
			"statYear" : statYear == "" ? "" : statYear
		},
		beforeSend : function() {

		},
		complete : function() {
		},
		success : function(data) {
			chartData = data;
		},
		error : function() {
		}
	});
}
function showtable() {
	$('.tlists')
			.datagrid(
					{
						idField : 'flashcontent',
						url : ctx+'/statistics/statisticsUserInfo/statTotalListByTime',
						data : {
							"start_time" : start_time == "" ? ""
									: start_time,
							"end_time" : end_time == "" ? "" : end_time,
							"statUnit" : statUnit == "" ? "" : statUnit,
							"statYear" : statYear == "" ? "" : statYear
						},
						loadMsg : '正在努力加载数据，请稍后...', //在从远程站点加载数据的时候显示提示消息。
						pagination : true, //如果为true，则在DataGrid控件底部显示分页工具栏。
						pageList : [ 15, 30, 50 ],
						rownumbers : true, //如果为true，则显示一个行号列。
						multiSort : false, //定义是否允许多列排序。  配合Column的sorttable属性使用,
						striped : true,
						singleSelect : true,
						data : tableData,
						title : '注册用户统计列表',
						columns : [ [
								{
									field : 'time',
									title : '时间',
									width : 200,
									sortable : true,
									halign : 'center',
									align : 'center',
									formatter : function(time) {
										if (time.length == 8) {
											return time.substring(0, 4)
													+ '-'
													+ time.substring(4, 6)
													+ '-'
													+ time.substring(6, 8);
										} else if (time.length == 6) {
											return time.substring(0, 4)
													+ '-'
													+ time.substring(4, 6);
										} else {
											return time;
										}
									}
								},{
									field : 'sum',
									title : '注册用户',
									width : 200,
									sortable : true,
									halign : 'center',
									align : 'center'
								}
						] ],
						fitColumns : true
					});

}
function compareTime(startTime,endTime,index){
	if(startTime==null||startTime==""||endTime==null||endTime==""){
		$(".btnTip:eq("+index+")").show();
		return 0;
	}else{
		if(Number(endTime)<Number(startTime)){
			$(".btnTipp:eq("+index+")").show();
			return 0;
		}else{
			$(".btnTip:eq("+index+")").hide();
			$(".btnTipp:eq("+index+")").hide();
			return 1;
		}
	}
}