$(function() {
	// 初始化统计类型下拉框
	$("#type")
			.combobox(
					{
						valueField : "typeValue",
						textField : 'typeName',
						value : "5",
						panelHeight : "auto",
						onSelect : function(record) {
							var typeValue = record.typeValue;
							if (typeValue == '1') {
								$("#typeAction")
										.attr(
												"action",
												ctx
														+ "/statistics/statisticsAccessInfo/statisticsAccessInfoTotalList");
							}
							if (typeValue == '2') {
								$("#typeAction")
										.attr(
												"action",
												ctx
														+ "/statistics/statisticsAccessInfo/statisticsAccessInfoProList");
							}
							if (typeValue == '3') {
								$("#typeAction")
										.attr(
												"action",
												ctx
														+ "/statistics/statisticsAccessInfo/statisticsAccessInfoOrgList");
							}
							if (typeValue == '4') {
								$("#typeAction")
										.attr(
												"action",
												ctx
														+ "/statistics/statisticsAccessInfo/statisticsAccessInfoItemList");
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
						}, {
							typeValue : "2",
							typeName : '分省统计'
						}, {
							typeValue : "3",
							typeName : '大院用户单位统计'
						}, {
							typeValue : "4",
							typeName : '栏目统计'
						}, {
							typeValue : "5",
							typeName : '数据下载统计'
						} ]
					});
	
	$('#selfunction3').combobox({
		url : ctx+'/statistics/statisticsAccessInfo/getOrgAll',
		valueField : 'orgValue', // 值
		textField : 'orgName', // 显示的名称
		editable : false,
		width : 120,
		value : '0'
	});
	// 初始化 栏目列表

	$('#selfunction').combobox({
		url : ctx + '/statistics/statisticsAccessInfo/getFunctionB',
		editable : false,
		valueField : 'funcItemId',
		textField : 'funcItemName',
		value : "70201",
		panelHeight : "auto",
		onSelect:function(rec){  
			 _zhbid.combobox({
                 disabled: false,
                 url: ctx + '/statistics/statisticsAccessInfo/getFunctionB?pfuncItemId='+rec.funcItemId,
                 valueField: 'funcItemId',
                 textField: 'funcItemName'
             }).combobox('clear');
	    },
	    onLoadSuccess: function () {//设置默认值  
	    	 _zhbid.combobox({
                 disabled: false,
                 url: ctx + '/statistics/statisticsAccessInfo/getFunctionB?pfuncItemId='+70201,
                 valueField: 'funcItemId',
                 textField: 'funcItemName'
             }).combobox('clear');
        }  
	});
	 var _zhbid = $('#selfunction2').combobox({
         disabled: true,
         valueField: 'funcItemId',
         textField: 'funcItemName',
          onLoadSuccess: function () { //加载完成后,设置选中第一项
                 var val = $(this).combobox("getData");
                 for (var item in val[0]) {
                     if (item == "funcItemId") {
                         $(this).combobox("select", val[0][item]);
                     }
                 }
          }
     });
	$(".btnTip").hide();
	$(".btnTipp").hide();
	$(".btnTippp").hide();
	//start_time = GetDateStr(0).replace(/-/g, "");
	//end_time = start_time;
	var funcItemId = $('#selfunction').datebox("getValue");
	if(funcItemId==null||funcItemId==""){
		funcItemId="7020110";
	}
	org = $('#selfunction3').datebox("getValue");
	start_time = $('#startTime2').val().replace(/-/g, "");
	end_time = $('#endTime2').val().replace(/-/g, "");
	getData(start_time, end_time, "", funcItemId,org);
	// 初始化数据表格
	$('.tlists').datagrid({
		idField : 'flashcontent',
		loadMsg : '正在努力加载数据，请稍后...', // 在从远程站点加载数据的时候显示提示消息。
		rownumbers : true, // 如果为true，则显示一个行号列。
		multiSort : true, // 定义是否允许多列排序。 配合Column的sorttable属性使用,
		data : tableData,
		columns : [ [ {
			field : 'name',
			title : '栏目',
			width : 210,
			sortable : true
		}, {
			field : 'sum',
			title : 'IP',
			width : 210,
			sortable : true
		}, {
			field : 'sum2',
			title : 'PV',
			width : 210,
			sortable : true
		}, {
			field : 'sum3',
			title : '停留时间',
			width : 210,
			sortable : true
		} ] ]
	});
	$("#btn").on("click",function() {
		start_time = $('#startTime').val().replace(/-/g, "");
		end_time = $('#endTime').val().replace(/-/g, "");
				var temp=compareTime(start_time,end_time,0);
				if(temp!=1){
					return false;
				}
					$(".btnTippp").hide();
					var funcItemId = $('#selfunction').datebox("getValue");
					org = $('#selfunction3').datebox("getValue");
					getData(start_time, end_time, "1", funcItemId,org);
					$('.tlists').datagrid('loadData', tableData);
					chart.xAxis[0].setCategories(chartData.name);
					chart.series[0].remove(false);
					chart.series[0].remove(false);
					chart.series[0].remove(false);
					chart.addSeries({
						name : 'IP',
						type : 'column',
						color : '#4572A7',
						yAxis : 0,
						data : chartData.sum,
						pointWidth : 10
					});
					chart.addSeries({
						name : 'PV',
						type : 'column',
						color : '#89A54E',
						yAxis : 1,
						data : chartData.sum2,
						pointWidth : 10
					});
					chart.addSeries({
						name : '停留时间',
						type : 'column',
						color : '#aa4643',
						yAxis : 2,
						data : chartData.sum3,
						pointWidth : 10
					});
					chart.redraw();
	});
	// 日
	$("#btn2").on("click", function() {
		start_time = $('#startTime2').val().replace(/-/g, "");
		end_time = $('#endTime2').val().replace(/-/g, "");
		var temp=compareTime(start_time,end_time,1);
		if(temp!=1){
			return false;
		}
		$(".btnTippp").hide();
		var temp=compareTime(start_time,end_time,1);
		if(temp!=1){
			return false;
		}
		var funcItemId = $('#selfunction').datebox("getValue");
		org = $('#selfunction3').datebox("getValue");
		getData(start_time, end_time, "2", funcItemId,org);
		$('.tlists').datagrid('loadData', tableData); // 重新加载table数据
		// 重新加载chart数据
		chart.xAxis[0].setCategories(chartData.name);
		chart.series[0].remove(false);
		chart.series[0].remove(false);
		chart.series[0].remove(false);
		chart.addSeries({
			name : 'IP',
			type : 'column',
			color : '#4572A7',
			yAxis : 0,
			data : chartData.sum,
			pointWidth : 10
		});
		chart.addSeries({
			name : 'PV',
			type : 'column',
			color : '#89A54E',
			yAxis : 1,
			data : chartData.sum2,
			pointWidth : 10
		});
		chart.addSeries({
			name : '停留时间',
			type : 'column',
			color : '#aa4643',
			yAxis : 2,
			data : chartData.sum3,
			pointWidth : 10
		});
		chart.redraw();
	});
	// 月
	$("#btn3").on(
			"click",
			function() {
				start_time = $('#startTime3').val().replace(/-/g, "");
				end_time = $('#endTime3').val().replace(/-/g, "");
				var temp=compareTime(start_time,end_time,2);
				if(temp!=1){
					return false;
				}
				$(".btnTippp").hide();
				var funcItemId = $('#selfunction').datebox("getValue");
				if(funcItemId=='0'){
		        	$(".btnTippp").show();
		        	return false;
				}
					var funcItemId = $('#selfunction').datebox("getValue");
					var funcItemId2 = $('#selfunction2').datebox("getValue");
					//二级没有选择
					org = $('#selfunction3').datebox("getValue");
					if(funcItemId2==null||funcItemId2==""||funcItemId2=="0"){
						getData(start_time, end_time, "3", funcItemId,org);
					}else{
						getData(start_time, end_time, "3", funcItemId2,org);
					}
					$('.tlists').datagrid('loadData', tableData);
					chart.xAxis[0].setCategories(chartData.name);
					chart.series[0].remove(false);
					chart.series[0].remove(false);
					chart.series[0].remove(false);
					chart.addSeries({
						name : 'IP',
						type : 'line',
						color : '#4572A7',
						yAxis : 0,
						data : chartData.sum,
						pointWidth : 10
					});
					chart.addSeries({
						name : 'PV',
						type : 'line',
						color : '#89A54E',
						yAxis : 1,
						data : chartData.sum2,
						pointWidth : 10
					});
					chart.addSeries({
						name : '停留时间',
						type : 'line',
						color : '#aa4643',
						yAxis : 2,
						data : chartData.sum3,
						pointWidth : 10
					});
					chart.redraw();

			});
	// 日-时间维度
	$("#btn4").on("click", function() {
		start_time = $('#startTime4').val().replace(/-/g, "");
		end_time = $('#endTime4').val().replace(/-/g, "");
		var temp=compareTime(start_time,end_time,3);
		if(temp!=1){
			return false;
		}
		var funcItemId = $('#selfunction').datebox("getValue");
		var funcItemId2 = $('#selfunction2').datebox("getValue");
		$(".btnTippp").hide();
		if(funcItemId=='0'){
        	$(".btnTippp").show();
        	return false;
		}
		//二级没有选择
		org = $('#selfunction3').datebox("getValue");
		if(funcItemId2==null||funcItemId2==""||funcItemId2=="0"){
			getData(start_time, end_time, "4", funcItemId,org);
		}else{
			getData(start_time, end_time, "4", funcItemId2,org);
		}
		$('.tlists').datagrid('loadData', tableData); // 重新加载table数据
		// 重新加载chart数据
		chart.xAxis[0].setCategories(chartData.name);
		chart.xAxis[0].setOptions({
			tickInterval : Math.round(chartData.name.length / 6)
		});
		chart.series[0].remove(false);
		chart.series[0].remove(false);
		chart.series[0].remove(false);
		chart.addSeries({
			name : 'IP',
			type : 'line',
			color : '#4572A7',
			yAxis : 0,
			data : chartData.sum,
			pointWidth : 10
		});
		chart.addSeries({
			name : 'PV',
			type : 'line',
			color : '#89A54E',
			yAxis : 1,
			data : chartData.sum2,
			pointWidth : 10
		});
		chart.addSeries({
			name : '停留时间',
			type : 'line',
			color : '#aa4643',
			yAxis : 2,
			data : chartData.sum3,
			pointWidth : 10
		});
		chart.redraw();
	});
	
	$('#flashcontent').highcharts({
		credits : {
			enabled : false
		// 不显示highCharts版权信息
		},

		title : {
			text : 'IP和PV的分栏目访问量统计图表'
		},
		colors : [ '#7CB5EC' ],
		plotOptions : {
			lineWidth : 0.5
		},
		xAxis : {
			categories : chartData.name
		},
		yAxis : [ {
			labels : {
				format : '{value}个',// 格式化Y轴刻度
				style : {
					color : '#89A54E'
				}
			},
			title : {
				text : 'IP',
				style : {
					color : '#89A54E'
				}
			},
			min : 0
		}, { // Secondary yAxis
			title : {
				text : 'PV',
				style : {
					color : '#4572A7'
				}
			},
			labels : {
				format : '{value} 次',
				style : {
					color : '#4572A7'
				}
			},
			min : 0,
			opposite : true
		}, { // Secondary yAxis
			title : {
				text : '停留时间',
				style : {
					color : '#aa4643'
				}
			},
			labels : {
				format : '{value}秒',
				style : {
					color : '#aa4643'
				}
			},
			min : 0,
			opposite : true
		} ],
		tooltip : {
			crosshairs : true,
			shared : true
		},
		series : [ {
			name : 'IP',
			type : 'column',
			color : '#4572A7',
			yAxis : 0,
			pointWidth : 10,
			data : chartData.sum,
			tooltip : {
				valueSuffix : '个'
			}
		}, {
			name : 'PV',
			type : 'column',
			color : '#89A54E',
			yAxis : 1,
			pointWidth : 10,
			data : chartData.sum2,
			tooltip : {
				valueSuffix : '个'
			}
		} , {
			name : '停留时间',
			type : 'column',
			color : '#aa4643',
			yAxis : 2,
			pointWidth : 10,
			data : chartData.sum3,
			tooltip : {
				valueSuffix : '秒'
			}
		} ]
	});
	chart = $('#flashcontent').highcharts();
});
function onSelect() {
	var start_time = $('#startTime').datebox("getValue").replace(/-/g, "");
	var end_time = start_time;
	if (startTime != "" && end_time != "") {
		if ((parseInt(start_time) - parseInt(end_time)) > 0) {
			$("#btn2").after(
					"<span id='error' style='color:red;'>开始日期不能大于结束日期。</span>");
		}
	}
}

function onShowPanel() {
	var $error = $("#error");
	if ($error.length > 0) {
		$error.remove();
	}
}

// 获取table和chart的数据
function getData(start_time, end_time, statUnit, funcItemId,org) {
	$.ajax({
		url : ctx + '/statistics/statisticsAccessInfo/statProByTimeItem2',
		type : 'post',
		cache : false,
		dataType : 'json',
		async : false,
		data : {
			"start_time" : start_time == "" ? "" : start_time,
			"end_time" : end_time == "" ? "" : end_time,
			"statUnit" : statUnit == "" ? "" : statUnit,
			"funcItemId" : funcItemId == "" ? "" : funcItemId,
			"org" : org == "" ? "" : org

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
function GetDateStr(AddDayCount) {
	var dd = new Date();
	dd.setDate(dd.getDate() + AddDayCount);// 获取AddDayCount天后的日期
	var y = dd.getFullYear();
	var m =(dd.getMonth() + 1) > 9 ? (dd.getMonth() + 1) : "0"
		+ (dd.getMonth() + 1);// 获取当前月份的日期
	var d =dd.getDate() > 9 ? dd.getDate() : "0" + dd.getDate();
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
function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel) {  
    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object  
    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData)  
            : JSONData;  

    var CSV = '';  
    //Set Report title in first row or line  

  //  CSV += ReportTitle + '\r\n\n';  

    //This condition will generate the Label/Header  
    if (ShowLabel) {  
        var row = "";  

        //This loop will extract the label from 1st index of on array  
        for ( var index in arrData[0]) {  

            //Now convert each value to string and comma-seprated  
            row += index + ',';  
        }  

        row = row.slice(0, -1);  

        //append Label row with line break  
        CSV += row + '\r\n';  
    }  

    //1st loop is to extract each row  
    for (var i = 0; i < arrData.length; i++) {  
        var row = "";  

        //2nd loop will extract each column and convert it in string comma-seprated  
        for ( var index in arrData[i]) {  
            row += '"' + arrData[i][index] + '",';  
        }  

        row.slice(0, row.length - 1);  

        //add a line break after each row  
        CSV += row + '\r\n';  
    }  

    if (CSV == '') {  
        alert("Invalid data");  
        return;  
    }  

    //Generate a file name  
    var fileName = "MyReport_";  
    //this will remove the blank-spaces from the title and replace it with an underscore  
    fileName += ReportTitle.replace(/ /g, "_");  

    //Initialize file format you want csv or xls  
    var uri = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(CSV);  

    // Now the little tricky part.  
    // you can use either>> window.open(uri);  
    // but this will not work in some browsers  
    // or you will not get the correct file extension      

    //this trick will generate a temp <a /> tag  
    var link = document.createElement("a");  
    link.href = uri;  

    //set the visibility hidden so it will not effect on your web-layout  
    link.style = "visibility:hidden";  
    link.download = fileName + ".csv";  

    //this part will append the anchor tag and remove it after automatic click  
    document.body.appendChild(link);  
    link.click();  
    document.body.removeChild(link);  
}  
$(function(){
	$("#btnExport").click(function() {  
	    var data = JSON.stringify($('.tlists').datagrid('getData').rows);  
	    if (data == '')  
	        return;  

	    JSONToCSVConvertor(data, "Download", true);  
	});  
});
