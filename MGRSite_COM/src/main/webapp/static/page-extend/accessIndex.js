$(function() {
	//topIPAjax();
	//topPVAjax();
	//topADAjax('4');
	lineGraphAjax('1');	
	//barChartAjaxPVNum('1');
	//barChartAjaxIPNum('1');
	//barChartAjaxStayTime('1');
	type2('1');
	type3('1');
	$("#itemSelector").bind("change", function(){
		
		//var menuType = $(this).val(); // 得到所选择栏目的类型,topItem为顶级栏目,subItem为子栏目
		
		/*
		 * 得到访问统计所要考察的日期类型
		 */
		var selectedType = $("#main_middle .clickbtn").attr("id"); 
		selectedType = selectedType.substring(selectedType.length - 1, selectedType.length);
		
		//itemAjax(menuType, selectedType);	
		type2(selectedType);
	});
	$("#subItemSelector").bind("change", function(){
		/*
		 * 得到访问统计所要考察的日期类型
		 */
		var selectedType = $("#main_middle .clickbtn").attr("id"); 
		selectedType = selectedType.substring(selectedType.length - 1, selectedType.length);
		//subItemAjax(selectedType);
		type2(selectedType);
	});
	
	tableAjax();
});

/*function itemAjax(menuType, selectedType){
	if(menuType == "subItem"){
		$("#subItemSetting").show();
		var menuId = $("#subItemSelector").val();
		type2(menuId, selectedType);
	}else{
		$("#subItemSetting").hide();
		type2('', selectedType);
	}
}

function subItemAjax(selectedType){
	var menuId = $("#subItemSelector").val();
	//alert(menuId);
	type2(menuId, selectedType);
}*/

function lineGraphAjax(type) {
	for(var i = 1; i <= 4; i++){
		$("#clickbtn" + i).removeClass("clickbtn");
		$("#clickbtn" + i).addClass("unclickbtn");
	}
	$("#clickbtn" + type).removeClass("unclickbtn");
	$("#clickbtn" + type).addClass("clickbtn");
	var url = ctx + "/accessinfo/lineGraph";
	$.ajax({
		url : url,
		async : false,
		type : "POST",
		datatype : "json",
		data : {
			type : type
		},
		success : function(result) {
			var interval = 0;
			var data = eval('(' + result + ')');
			if (type == "1" || type == "2") {
				interval = 3;
				lineGraph(data[0].pvcount, data[0].ipcount, data[0].stayTime, data[1].str, interval, type);
			} else if(type == "4"){
				interval = 3;
				var xaxis = new Array();
				var yaxispv = new Array();
				var yaxisip = new Array();
				var yaxisStayTime = new Array();
				//alert(data.length);
				$.each(data, function(i, data0) {
					xaxis[i] = data0[0];
					yaxispv[i] = data0[1];
					yaxisip[i] = data0[2];
					yaxisStayTime[i] = data0[3];
				});
				lineGraph(yaxispv, yaxisip, yaxisStayTime, xaxis, interval, type);
			} else {
				interval = 0;
				var xaxis = new Array();
				var yaxispv = new Array();
				var yaxisip = new Array();
				var yaxisStayTime = new Array();
				//alert(data.length);
				$.each(data, function(i, data0) {
					xaxis[i] = data0[0];
					yaxispv[i] = data0[1];
					yaxisip[i] = data0[2];
					yaxisStayTime[i] = data0[3];
				});
				lineGraph(yaxispv, yaxisip, yaxisStayTime, xaxis, interval, type);
			}
		}
	});
}

function barChartAjaxPVNum(menuId,type) {
	var url = ctx + "/accessinfo/barChart/PVNum";
	$.ajax({
		url : url,
		async : false,
		type : "POST",
		datatype : "json",
		data : {
			menuId: menuId,
			type : type
		},
		success : function(result) {
			var data = eval('(' + result + ')');
			barChartPVNum(data[0].items, data[1].pvnum, type);
		}
	});
}

function barChartAjaxIPNum(menuId, type) {
	var url = ctx + "/accessinfo/barChart/IPNum";
	$.ajax({
		url : url,
		async : false,
		type : "POST",
		datatype : "json",
		data : {
			menuId: menuId,
			type : type
		},
		success : function(result) {
			var data = eval('(' + result + ')');
			barChartIPNum(data[0].items, data[1].ipnum, type);
		}
	});
}

function barChartAjaxStayTime(menuId, type) {
	var url = ctx + "/accessinfo/barChart/StayTime";
	$.ajax({
		url : url,
		async : false,
		type : "POST",
		datatype : "json",
		data : {
			menuId: menuId,
			type : type
		},
		success : function(result) {
			var data = eval('(' + result + ')');
			barChartStayTime(data[0].items, data[1].staytime, type);
		}
	});
}

function pieChartAjax(type) {
	
	var url = ctx + "/accessinfo/pieChart";
	$.ajax({
		url : url,
		async : false,
		type : "POST",
		datatype : "json",
		data : {
			type : type
		},
		success : function(result) {
			var data = eval('(' + result + ')');
			pieChart(data, type);
		}
	});
}

function lineGraph(arr1, arr2, arr3, str, interval, type) {
	//alert(type);
	//var dateStr = "";
	if(type == "1"){
		dateStr = "今日";
		//alert(dateStr);
	}else if(type == "2"){
		dateStr = "昨日";
	}else if(type == "3"){
		dateStr = "过去一周";
	}else{
		dateStr = "过去一月";
	}
	$('#lineGraph').highcharts({
		chart:{
			height:350
		},
		credits : {
			enabled : false
		// 不显示highCharts版权信息
		},
		title : {
			text : dateStr + '访问PV、IP、页面平均停留时长统计',
			x : -20
		},
		subtitle : {
			text : '',
			x : -20
		},
		xAxis : {
			tickmarkPlacement : 'on',
			tickInterval: interval,
			categories : str
		},
		yAxis : [ {
			title : {
				text : 'IP访问量'
			},
			plotLines : [ {
				value : 0,
				width : 1,
				color : '#808080'
			} ],
			labels : {
				format : '{value}',
				style : {
					color : '#808080'
				}
			},
		}, {
			title : {
				text : 'PV访问量'
			},
			plotLines : [ {
				value : 0,
				width : 1,
				color : '#89A54E'
			} ],
			labels : {
				format : '{value}',
				style : {
					color : '#89A54E'
				}
			},
			opposite : true
		}, {
			title : {
				text : '页面平均停留时长(秒)'
			},
			plotLines : [ {
				value : 0,
				width : 1,
				color : '#AA4643'
			} ],
			labels : {
				format : '{value}',
				style : {
					color : '#AA4643'
				}
			},
			opposite : true
		}],
		tooltip : {
			valueSuffix : '',
			crosshairs : true,
			shared : true
		},
		series : [ {
			name : 'PV访问量',
			color : '#89A54E',
			yAxis : 1,
			data : arr1,
			tooltip : {
				valueSuffix : ''
			}
		}, {
			name : 'IP访问量',
			color : '#808080',
			yAxis : 0,
			data : arr2,
			tooltip : {
				valueSuffix : ''
			}
		}, {
			name : '页面平均停留时长(秒)',
			color : '#AA4643',
			yAxis : 2,
			data : arr3,
			tooltip : {
				valueSuffix : ''
			}
		} ]
	});
}

function barChartPVNum(items, pvnum, type) {
	var typeChnName = "";
	if(type == '1'){
		typeChnName = "今日";
	}else if(type == '2'){
		typeChnName = "昨日";
	}else if(type == '3'){
		typeChnName = "过去一周";
	}else{
		typeChnName = "过去一月";
	}
	$('#barChartPVNum').highcharts({
		credits : {
			enabled : false
		// 不显示highCharts版权信息
		},
		chart : {
			type : 'bar',
			height:420,
		},
		title : {
			text : typeChnName + '访问PV统计'
		},
		subtitle : {
			text : ''
		},
		xAxis : {
			categories : items,
			title : {
				text : null
			}
		},
		yAxis : {
			min : 0,
			title : {
				text : '',
				align : 'high'
			},
			labels : {
				overflow : 'justify'
			}
		},
		tooltip : {
			valueSuffix : ''
		},
		plotOptions : {
			bar : {
				dataLabels : {
					enabled : true
				}
			},
			lineWidth : 4
		},
		legend : {
			enabled : false
		},
		credits : {
			enabled : false
		},
		series : [ {
			name : 'PV统计',
			data : pvnum
		}]
	});
}

function barChartIPNum(items, ipnum, type) {
	var typeChnName = "";
	if(type == '1'){
		typeChnName = "今日";
	}else if(type == '2'){
		typeChnName = "昨日";
	}else if(type == '3'){
		typeChnName = "过去一周";
	}else{
		typeChnName = "过去一月";
	}
	$('#barChartIPNum').highcharts({
		credits : {
			enabled : false
		// 不显示highCharts版权信息
		},
		chart : {
			type : 'bar',
			height:420,
		},
		title : {
			text : typeChnName + '访问IP统计'
		},
		subtitle : {
			text : ''
		},
		xAxis : {
			categories : items,
			title : {
				text : null
			}
		},
		yAxis : {
			min : 0,
			title : {
				text : '',
				align : 'high'
			},
			labels : {
				overflow : 'justify'
			}
		},
		tooltip : {
			valueSuffix : ''
		},
		plotOptions : {
			bar : {
				dataLabels : {
					enabled : true
				}
			},
			lineWidth : 4
		},
		legend : {
			enabled : false
		},
		credits : {
			enabled : false
		},
		series : [ {
			name : 'IP统计',
			data : ipnum
		} ]
	});
}

function barChartStayTime(items, staytime, type) {
	var typeChnName = "";
	if(type == '1'){
		typeChnName = "今日";
	}else if(type == '2'){
		typeChnName = "昨日";
	}else if(type == '3'){
		typeChnName = "过去一周";
	}else{
		typeChnName = "过去一月";
	}
	$('#barChartStayTime').highcharts({
		credits : {
			enabled : false
		// 不显示highCharts版权信息
		},
		chart : {
			type : 'bar',
			height:420,
		},
		title : {
			text : typeChnName + '访问页面平均停留时长统计(秒)'
		},
		subtitle : {
			text : ''
		},
		xAxis : {
			categories : items,
			title : {
				text : null
			}
		},
		yAxis : {
			min : 0,
			title : {
				text : '',
				align : 'high'
			},
			labels : {
				overflow : 'justify'
			}
		},
		tooltip : {
			valueSuffix : ''
		},
		plotOptions : {
			bar : {
				dataLabels : {
					enabled : true
				}
			},
			lineWidth : 4
		},
		legend : {
			enabled : false
		},
		credits : {
			enabled : false
		},
		series : [  {
			name : '停留时长',
			data : staytime
		} ]
	});
}


function pieChart(data, type) {
	var typeChnName = "";
	if(type == '1'){
		typeChnName = "今日";
	}else if(type == '2'){
		typeChnName = "昨日";
	}else if(type == '3'){
		typeChnName = "过去一周";
	}else{
		typeChnName = "过去一月";
	}
	$('#pieChart').highcharts({
		credits : {
			enabled : false
		// 不显示highCharts版权信息
		},
		chart : {
			height:350,
		//	plotBackgroundColor : null,
		//	plotBorderWidth : null,
		//	plotShadow : false
		},
		title : {
			text : typeChnName + '网站访问来源统计'
		},
		tooltip : {
			pointFormat : '{series.name}: <b>{point.percentage:.1f}%</b>'
		},
		plotOptions : {
			pie : {
				allowPointSelect : true,
				cursor : 'pointer',
				dataLabels : {
					enabled : true,
					color : '#000000',
					connectorColor : '#000000',
					format : '<b>{point.name}</b>: {point.percentage:.1f} %'
				}
			}
		},
		series : [ {
			type : 'pie',
			name : '访问占比',
			data : data
		} ]
	});
}

function type1(type) {
	lineGraphAjax(type);
}
function type2(type) {
	/*
	 * 切换样式
	 */
	for(var i = 1; i <= 4; i++){
		$("#Bclickbtn" + i).removeClass("clickbtn");
		$("#Bclickbtn" + i).addClass("unclickbtn");
	}
	$("#Bclickbtn" + type).removeClass("unclickbtn");
	$("#Bclickbtn" + type).addClass("clickbtn");
	
	var menuId;
	var menuType = $("#itemSelector").val(); // 得到所选择栏目的类型,topItem为顶级栏目,subItem为子栏目
	if(menuType == "subItem"){
		$("#subItemSetting").show();
		menuId = $("#subItemSelector").val();
	}else{
		$("#subItemSetting").hide();
		menuId = '';
	}
	barChartAjaxPVNum(menuId,type);
	barChartAjaxIPNum(menuId,type);
	barChartAjaxStayTime(menuId,type);
}
function type3(type) {
	for(var i = 1; i <= 4; i++){
		$("#Iclickbtn" + i).removeClass("clickbtn");
		$("#Iclickbtn" + i).addClass("unclickbtn");
	}
	$("#Iclickbtn" + type).removeClass("unclickbtn");
	$("#Iclickbtn" + type).addClass("clickbtn");
	mapAjax(type);
	pieChartAjax(type);
}

function topIPAjax() {
	var url = ctx + "/accessinfo/topip";
	$.ajax({
		url : url,
		async : false,
		type : "POST",
		datatype : "json",
		success : function(result) {
			var data1 = eval('(' + result + ')');
			var k = 0;
			var appendstr = "<table style='color:#808080'><thead><th>IP</th><th>&nbsp;&nbsp;&nbsp;</th><th>次数</th></thead>";
			$.each(data1.data.buckets, function(i, res) {
				if (res.key != '10.10.44.133' && res.key != '10.10.44.131' && res.key != '10.10.44.135' && res.key != '10.20.67.149' && res.key != '10.10.44.132' && res.key != '10.20.67.142') {
					if (k <= 9) {
						appendstr += "<tr><td>" + res.key + "</td><td>&nbsp;&nbsp;&nbsp;</td><td>" + res.doc_count + "</td></tr>";
					}
					k++;
				}
			});
			appendstr += "</table>";
			$(".IPInfo").append(appendstr);
		}
	});
}
function topPVAjax() {
	var url = ctx + "/accessinfo/toppv";
	$.ajax({
		url : url,
		async : false,
		type : "POST",
		datatype : "json",
		success : function(result) {
			var data1 = eval('(' + result + ')');
			var appendstr = "<table style='color:#808080'><thead><th>访问信息</th><th>&nbsp;&nbsp;&nbsp;</th><th>次数</th></thead>";
			$.each(data1, function(i, data) {
				appendstr += "<tr align='center'><td>" + data.itemid + "</td><td>&nbsp;&nbsp;&nbsp;</td><td>" + data.pvnumber + "</td></tr>";
			});
			appendstr += "</table>";
			$(".PVContent").empty();
			$(".PVInfo").empty();
			$(".PVContent").append(data1[0].accessdate + "日" + data1[0].accesstime + "时PV访问排行");
			$(".PVInfo").append(appendstr);
		}
	});
}
function topADAjax(type) {
	var url = ctx + "/accessinfo/topaddress";
	$.ajax({
		url : url,
		async : false,
		type : "POST",
		datatype : "json",
		data : {
			type : type
		},
		success : function(result) {
			var data1 = eval('(' + result + ')');
			var time1 = "";
			var time2 = "";
			var appendstr = "<table style='color:#808080;' ><tr><th>排名</th><th>区域</th><th>访问量</th></tr>";
			$.each(data1, function(i, data) {
				time1 = data.date1;
				time2 = data.date2;
				appendstr += "<tr><td>" + (i + 1) + "</td><td>" + data.proname + "</td><td>" + data.ipnum + "</td></tr>";
			});
			appendstr += "</table>";
			$(".ADContent").empty();
			$(".ADInfo").empty();
			//$(".ADContent").append(time1+"日"+time2+"时最新IP访问排行");
			$(".ADInfo").append(appendstr);
		}
	});
}

function mapAjax(type){
	var myChart = echarts.init(document.getElementById("mapChart"));
	var option;
	var url = ctx + "/accessinfo/mapDistribute";
	$.ajax({
		url: url,
		async: true,
		type: "POST",
		dataType: "json",
		data: {
			type: type
		},
		success: function(data){
			option = {
					title : {
					text: '网站访问量分布',
					subtext: '',
					x:'center'
				},
				tooltip : {
					trigger: 'item'
				},
				legend: {
					 orient: 'vertical',
	    		        x:'center',
	    		        y: 40,
	    		        textStyle:{
	    		        	fontFamily: 'Microsoft YaHei'
	    		        },
	    		        borderColor: "#ccc",
	    		        borderWidth: 1,
	    		        data:['访问量']
				},
				dataRange: {
					min: 0,
					max: 500,
					x: 'left',
					y: 'bottom',
					text:['高','低'],           // 文本，默认为数值文本
					calculable : true
				},
				toolbox: {
					show: true,
					orient : 'vertical',
					x: 'right',
					y: 'center',
					feature : {
						mark : {show: true},
						dataView : {show: true, readOnly: false},
						restore : {show: true},
						saveAsImage : {show: true}
					}
				},
				series : [
					{
						name: "访问量",
						type: 'map',
			            mapType: 'china',
			            roam: false,
			            itemStyle:{
			                normal:{label:{show:true}},
			                emphasis:{label:{show:true}}
			            },
						data: data.data
					}					
				]
			};
			myChart.setOption(option);
		}
	});
	
}

function tableAjax(){
	$("tbody>tr:even").css("background", "#E0FFFF");
	//$("tfoot>tr").css("background", "#E0FFFF");
}


