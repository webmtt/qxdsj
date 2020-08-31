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
												ctx+"/statistics/statisticsAccessInfo/statisticsAccessInfoTotalList");
							}
							if (typeValue == '2') {
								$("#typeAction")
										.attr("action",
												ctx+"/statistics/statisticsAccessInfo/statisticsAccessInfoProList");
							}
							if (typeValue == '3') {
								$("#typeAction")
										.attr("action",
												ctx+"/statistics/statisticsAccessInfo/statisticsAccessInfoOrgList");
							}
							if (typeValue == '4') {
								$("#typeAction")
										.attr("action",
												ctx+"/statistics/statisticsAccessInfo/statisticsAccessInfoItemList");
							}
							if (typeValue == '5') {
								$("#typeAction")
										.attr(
												"action",
												ctx
														+ "/statistics/statisticsAccessInfo/statisticsAccessInfoItemList2");
							}
							$("#typeAction").submit();
						},
						data : [ {
							typeValue : "1",
							typeName : '总量统计'
						},{
							typeValue : "2",
							typeName : '分省统计'
						},{
							typeValue : "3",
							typeName : '大院用户单位统计'
						}, {
							typeValue : "4",
							typeName : '栏目统计'
						} , {
							typeValue : "5",
							typeName : '数据下载统计'
						}]
					});


	$(".btnTip").hide();
	$(".btnTipp").hide();
	$('#selfunction').combobox({
		editable : false,
		valueField : 'sourceType',
		textField : 'sourceName',
		value : "0",
		panelHeight : "auto",
		data : [ {
			sourceType : '0',
			sourceName : '总体量统计'
		}, {
			sourceType : 'center',
			sourceName : '国家级统计'
		}, {
			sourceType : 'province',
			sourceName : '省级统计'
		}]
	});
	//初始化折线图
	getData(start_time, end_time, statUnit, statYear);
	// 初始化数据表格
	showtable();

	// 逐月统计
	$("#btn1").on("click", function() {
		start_time = $('#startTime1').val().replace(/-/g, "");
		end_time = $('#endTime1').val().replace(/-/g, "");
		var temp=compareTime(start_time,end_time,0);
		if(temp!=1){
			return false;
		}
		statUnit =2;
		if (statUnit == "2") {
			var sourceType = $('#selfunction').datebox("getValue");
			getData(start_time, end_time, statUnit, null,sourceType);
			$('.tlists').datagrid('load', {
				start_time : start_time,
				end_time : end_time,
				statUnit : statUnit,
				statYear : null,
				sourceType:sourceType
			});
			// 重新加载chart数据
			chart.xAxis[0].setCategories(chartData.time);
			chart.xAxis[0].setOptions({
				tickInterval : 1
			});
			chart.series[0].remove(false);
			chart.series[0].remove(false);
			chart.addSeries({
				name : 'IP',
				type : 'line',
				color: '#4572A7',
				yAxis: 0,
				data : chartData.sum,
				pointWidth : 30
			});
			chart.addSeries({
				name : 'PV',
				type : 'line',
				color: '#89A54E',
				yAxis: 1,
				data : chartData.sum2,
				pointWidth : 30
			});
			chart.redraw();
		} else {
			$(".btnTip:eq(0)").show();
		}
	});

	// 起始年月日查询
	$("#btn2").on("click", function() {
		start_time = $('#startTime2').val().replace(/-/g, "");
		end_time = $('#endTime2').val().replace(/-/g, "");
		var temp=compareTime(start_time,end_time,1);
		if(temp!=1){
			return false;
		}
		statUnit = "3";
		var sourceType = $('#selfunction').datebox("getValue");
		getData(start_time, end_time, statUnit, statYear,sourceType);
		$('.tlists').datagrid('load', {
			start_time : start_time,
			end_time : end_time,
			statUnit : "3",
			statYear : null,
			sourceType:sourceType
		});
		// 重绘图表
		chart.xAxis[0].setCategories(chartData.time);
		chart.xAxis[0].setOptions({
			tickInterval : Math.round(chartData.time.length / 6)
		});
		chart.series[0].remove(false);
		chart.series[0].remove(false);
		chart.addSeries({
			name : 'IP',
			type : 'line',
			color: '#4572A7',
			yAxis: 0,
			data : chartData.sum
		});
		chart.addSeries({
			name : 'PV',
			type : 'line',
			color: '#89A54E',
			yAxis: 1,
			data : chartData.sum2
		});
		chart.redraw();
	});
	// 自定义起始年月日时查询
	$("#btn3").on("click", function() {
		start_time = $('#startTime3').val().replace(/-/g, "").replace(/\s/g, "");
		end_time = $('#endTime3').val().replace(/-/g, "").replace(/\s/g, "");
		var temp=compareTime(start_time,end_time,2);
		if(temp!=1){
			return false;
		}
		var sourceType = $('#selfunction').datebox("getValue");
		statUnit = "1";
		statYear = null;
		getData(start_time, end_time, statUnit, statYear,sourceType);
		$('.tlists').datagrid('load', {
			start_time : start_time,
			end_time : end_time,
			statUnit : "1",
			statYear : null,
			sourceType : sourceType
		});
		// 重绘图表
		chart.xAxis[0].setCategories(chartData.time);
		chart.xAxis[0].setOptions({
			tickInterval : Math.round(chartData.time.length / 6)
		});
		chart.series[0].remove(false);
		chart.series[0].remove(false);
		chart.addSeries({
			name : 'IP',
			type : 'line',
			color: '#4572A7',
			yAxis: 0,
			data : chartData.sum
		});
		chart.addSeries({
			name : 'PV',
			type : 'line',
			color: '#89A54E',
			yAxis: 1,
			data : chartData.sum2
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
			text : 'IP和PV访问量统计图表'
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
	            format: '{value}个',//格式化Y轴刻度
	            style: {
	                color: '#89A54E'
	            }
	        },
	        title: {
	            text: 'IP',
	            style: {
	                color: '#89A54E'
	            }
	        },
	        min:0
	    }, { // Secondary yAxis
	        title: {
	            text: 'PV',
	            style: {
	                color: '#4572A7'
	            }
	        },
	        labels: {
	            format: '{value} 个',
	            style: {
	                color: '#4572A7'
	            }
	        },
	        min:0,
	        opposite: true
	    }],
		tooltip : {
			crosshairs : true,
			shared : true
		},
		series : [ {
			name : 'IP',
			color: '#4572A7',
		    yAxis: 0,
			data : chartData.sum,
			tooltip: {
	            valueSuffix: '个'
	        }
		},{
			name : 'PV',
			color: '#89A54E',
	        yAxis: 1,
			data : chartData.sum2,
			tooltip: {
	            valueSuffix: '个'
	        }
		} ]
	});
	
	chart = $('#flashcontent').highcharts();
});
//获取table和chart的数据
function getData(start_time, end_time, statUnit, statYear,sourceType) {
	$.ajax({
		url : ctx+'/statistics/statisticsAccessInfo/statTotalPictureByTime',
		type : 'post',
		cache : false,
		dataType : 'json',
		async : false,
		data : {
			"start_time" : start_time == "" ? "" : start_time,
			"end_time" : end_time == "" ? "" : end_time,
			"statUnit" : statUnit == "" ? "" : statUnit,
			"statYear" : statYear == "" ? "" : statYear,
			"sourceType" : sourceType == "" ? "" : sourceType
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
function showtable(sourceType ) {
	$('.tlists')
			.datagrid(
					{
						idField : 'flashcontent',
						url : ctx+'/statistics/statisticsAccessInfo/statTotalListByTime',
						data : {
							"start_time" : start_time == "" ? ""
									: start_time,
							"end_time" : end_time == "" ? "" : end_time,
							"statUnit" : statUnit == "" ? "" : statUnit,
							"statYear" : statYear == "" ? "" : statYear,
							"sourceType" : sourceType == "" ? "" : sourceType
						},
						loadMsg : '正在努力加载数据，请稍后...', //在从远程站点加载数据的时候显示提示消息。
						pagination : true, //如果为true，则在DataGrid控件底部显示分页工具栏。
						pageList : [ 15, 30, 50 ],
						rownumbers : true, //如果为true，则显示一个行号列。
						multiSort : false, //定义是否允许多列排序。  配合Column的sorttable属性使用,
						striped : true,
						singleSelect : true,
						data : tableData,
						title : 'IP和PV访问量统计列表',
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
									title : 'IP',
									width : 200,
									sortable : true,
									align : 'center'
								},{
									field : 'sum2',
									title : 'PV',
									width : 200,
									sortable : true,
									align : 'center'
								}

						] ],
						fitColumns : true
					});

}
formatterDate = function(date) {
	var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
	var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
			+ (date.getMonth() + 1);
	return date.getFullYear() + '-' + month + '-' + day;
};
function GetDateStr(AddDayCount) {
	var dd = new Date();
	dd.setDate(dd.getDate() + AddDayCount);// 获取AddDayCount天后的日期
	var y = dd.getFullYear();
	var m = dd.getMonth() + 1;// 获取当前月份的日期
	var d = dd.getDate();
	return y + "-" + m + "-" + d;
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