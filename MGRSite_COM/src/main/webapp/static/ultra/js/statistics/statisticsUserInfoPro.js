$(function() {
	// 初始化统计类型下拉框
	$("#type").combobox({
						valueField : "typeValue",
						textField : 'typeName',
						value : "3",
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
	getData();
	// 初始化数据表格
	$('.tlists').datagrid({
		idField : 'flashcontent',
		loadMsg : '正在努力加载数据，请稍后...', // 在从远程站点加载数据的时候显示提示消息。
		rownumbers : true, // 如果为true，则显示一个行号列。
		multiSort : true, // 定义是否允许多列排序。 配合Column的sorttable属性使用,
		data : tableData,
		columns : [ [ {
			field : 'name',
			title : '单位',
			width : 400,
			sortable : true
		}, {
			field : 'sum',
			title : '注册量',
			width : 400,
			sortable : true
		}]]
	});

	// 自定义起始年月查询
	$("#btn2").on("click", function() {
		start_time = $('#startTime').val().replace(/-/g, "");
		end_time = $('#endTime').val().replace(/-/g, "");
		var temp=compareTime(start_time,end_time,0);
		if(temp!=1){
			return false;
		}
		$(".btnTip:eq(0)").hide();
		$(".btnTipp:eq(0)").hide();
		getData(start_time, end_time);
		$('.tlists').datagrid('loadData', tableData); // 重新加载table数据
		// 重新加载chart数据
		chart.xAxis[0].setCategories(chartData.name);
		chart.series[0].remove(false);
		chart.addSeries({
			name : '注册量',
			type : 'column',
			color : '#7CB5EC',
			data : chartData.sum,
			pointWidth : 10
		});
		chart.redraw();
	});
	 $('#flashcontent').highcharts({
		    credits : {
				enabled : false
			// 不显示highCharts版权信息
			},
		    title: {
		        text: '分省统计注册量'
		    },
		    colors: ['#7CB5EC'],
		    plotOptions: {
		    	lineWidth: 0.5
		    },
		    xAxis: {	              
		    	type: 'category',
		    	labels : {
					rotation : -45
				},
		        categories:chartData.name
		    },
		    yAxis: {
		        title: {
		            text: '单位：人（次）'
		        },
		        min:0
		    },
		    tooltip: {	                
		        crosshairs:true,
		        shared:true
		    },	             
		    series: [{
		        name: '注册量',
		        type: 'column',
		        data: chartData.sum
		    }]
		});
	chart = $('#flashcontent').highcharts();
});
// 获取table和chart的数据
function getData(start_time, end_time,statUnit) {
	$.ajax({
		url : ctx+'/statistics/statisticsUserInfo/statProByTime',
		type : 'post',
		cache : false,
		dataType : 'json',
		async : false,
		data : {
			"start_time" : start_time == "" ? "" : start_time,
			"end_time" : end_time == "" ? "" : end_time,
		    "statUnit" : statUnit == "" ? "" : statUnit
					
		},
		beforeSend : function() {

		},
		complete : function() {
		},
		success : function(data) {
			chartData = data.pic;
			tableData = data.list;
		},
		error : function() {

		}
	});
}
formatterDate = function(date) {
	var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
	var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
	+ (date.getMonth() + 1);
	return date.getFullYear() + '-' + month + '-' + day;
	};
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