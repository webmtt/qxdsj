var tableData , //table data
		chartData, //chart data
		end_time,  //
		start_time,	
		statUnit,
		statYear,
		chart; //chart组件


function onSelect(){
	var start_time = $('#startTime').datebox("getValue").replace(/-/g, "");
	var end_time = $('#endTime').datebox("getValue").replace(/-/g, "");
	if(startTime!="" && end_time!="" ){
		if((parseInt(start_time) -  parseInt(end_time))>0){
			$("#btn2").after("<span id='error' style='color:red;'>开始日期不能大于结束日期。</span>");
		} 
	}
}


function onShowPanel(){
	var $error = $("#error");
	if($error.length>0){
		$error.remove();
	}
}

//获取table和chart的数据
function getData (start_time,end_time,statUnit,statYear){
//	$.ajax({
//		url:'statdownload_statTotalListByTime.action',
//		type:'post',
//		cache:false,
//		dataType:'json',
//		async:false,
//		data:{
//			"start_time":start_time =="" ? "" : start_time,
//			"end_time":end_time =="" ? "" : end_time,
//			"statUnit":statUnit=="" ? "":statUnit,
//			"statYear":statYear=="" ? "":statYear
//		},
//		beforeSend:function(){
//			
//	    },
//	    complete:function(){
//	    },
//		success:function(data){
//	    	tableData = data;
//
//	    },
//		error:function(){
//			
//		}
//	});
	$.ajax({
		url:'${ctx}/sys/StatisticsDownload/statTotalPictureByTime',
		type:'post',
		cache:false,
		dataType:'json',
		async:false,
		data:{
			"start_time":start_time =="" ? "" : start_time,
			"end_time":end_time =="" ? "" : end_time,
					"statUnit":statUnit=="" ? "":statUnit,
							"statYear":statYear=="" ? "":statYear
		},
		beforeSend:function(){
			
	    },
	    complete:function(){
	    },
		success:function(data){
	    	chartData = data;
	    },
		error:function(){			
		}
	});
}

function redrawChart(){
	$('#flashcontent').highcharts({
		credits:{
	      enabled:false//不显示highCharts版权信息
		},
        chart: {
            zoomType: 'xy'
        },
        title: {
        	 text: '<div align="center">订单情况统计图表</div>'
        },
//        subtitle: {
//            text: 'Source: WorldClimate.com'
//        },
        xAxis: [{
        	 tickInterval:Math.round(chartData.time.length/9),
             categories:chartData.time
        }],
        yAxis: [{ // Primary yAxis
            labels: {
                format: '{value}个',
                style: {
                    color: '#89A54E'
                }
            },
            title: {
                text: '订单量',
                style: {
                    color: '#89A54E'
                }
            }
        }],
        tooltip: {
            shared: true
        },
        legend: {
            layout: 'vertical',
            align: 'left',
            x: 120,
            verticalAlign: 'top',
            y: 100,
            floating: true,
            backgroundColor: '#FFFFFF'
        },
        series: [ {
            name: '订单量',
            color: '#89A54E',
            type: 'spline',
            data: chartData.sum,
            tooltip: {
                valueSuffix: '个'
            }
        }]
    });
}


$(function(){
	//初始化统计类型下拉框
	$("#type").combobox({
		valueField:"typeValue",
		textField:'typeName',
		value:"1",
		panelHeight:"auto",
		onSelect:function(record){
		var typeValue = record.typeValue;
			if(typeValue=='1'){
				$("#typeAction").attr("action","${ctx}/sys/userInfomation/statisticsDownloadTotalList");		
			}
			if(typeValue=='2'){
				$("#typeAction").attr("action","${ctx}/sys/userInfomation/statisticsDownloadClassList");	
			}
			$("#typeAction").submit();
		},
		data:[{
			typeValue:"1",
			typeName:'按总量统计'
		},{
			typeValue:"2",
			typeName:'按分类统计'	
		}]
	});
	
	//初始化统计方式下拉框
	$("#statUnit").combobox({
		valueField:"unitValue",
		textField:'unitName',
		value:"1",
		panelHeight:"auto",
		onSelect:function(record){
		var unitValue = record.unitValue;
			if(unitValue=="1"){
				$("#isShowYear").hide();	
			}
			if(unitValue=="2"){
				$("#isShowYear").show();	
			}
			if(unitValue=="3"){
				$("#isShowYear").show();
			}
		},
		data:[{
			unitValue:"1",
			unitName:'逐年统计'
		},{
			unitValue:"2",
			unitName:'逐季统计'	
		},{
			unitValue:"3",
			unitName:'逐月统计'	
		}]
	});
	$("#isShowYear").hide();
	$(".btnTip").hide();
	
	//初始化季度选择下拉框
	$('#quarter').combobox({    
	    valueField:'quarterValue',      //值
	    textField:'quarterName',   	//显示的名称
	    editable:false,
	    panelHeight:120,
	    value:'0',
	    data: [{
			quarterValue: '0',
			quarterName: '请选择'
		},{
	    	quarterValue: '01.01,03.31',
	    	quarterName: '一季度'
		},{
			quarterValue: '04.01,06.31',
			quarterName: '二季度'
		},{
			quarterValue: '07.01,09.31',
			quarterName: '三季度'
		},{
			quarterValue: '10.01,12.31',
			quarterName: '四季度'
		}]
	}); 
	
	//初始化月份选择下拉框
	$('#month').combobox({    
	    valueField:'monthValue',      //值
	    textField:'monthName',   	//显示的名称
	    editable:false,
	    width:70,
	    value:'0',
	    onSelect:function(record){
			if(record.monthValue!='0'){
				$(".quarter").removeAttr("checked");
			}
		},
	    data: [{
			monthValue: '0',
			monthName: '请选择'
		},{
	    	monthValue: '0101,0131',
	    	monthName: '一月'
		},{
			monthValue: '0201,0231',
			monthName: '二月'
		},{
			monthValue: '0301,0331',
			monthName: '三月'
		},{
	    	monthValue: '0401,0431',
	    	monthName: '四月'
		},{
			monthValue: '0501,0531',
			monthName: '五月'
		},{
			monthValue: '0601,0631',
			monthName: '六月'
		},{
	    	monthValue: '0701,0731',
	    	monthName: '七月'
		},{
			monthValue: '0801,0831',
			monthName: '八月'
		},{
			monthValue: '0901,0931',
			monthName: '九月'
		},{
	    	monthValue: '1001,1031',
	    	monthName: '十月'
		},{
			monthValue: '1101,1131',
			monthName: '十一月'
		},{
			monthValue: '1201,1231',
			monthName: '十二月'
		}]
	}); 
	

	//初始化日期插件
	$('#startTime').datebox({    
	    onSelect: onSelect,
    	onShowPanel:onShowPanel
	});  
	
	$('#endTime').datebox({    
		onSelect: onSelect,
	    onShowPanel:onShowPanel
	});  
	
	//初始化年份选择下拉框
	$('.year').combobox({
		url:'${ctx}/sys/StatisticsDownload/statYears',
	    valueField:'yearValue',      //值
	    textField:'yearName',   	//显示的名称
	    editable:false,
	    width:70,
	    value:'0'
	});  
	
//	$.ajax({
//		url:'statdownload_statTotalPictureByTime.action',
//		type:'post',
//		cache:false,
//		dataType:'json',
//		async:true,
//		beforeSend:function(){
//	        var top = $('#flashcontent').offset().top;
//			var left = $('#flashcontent').offset().left;
//			var left2 = left+320;
//    		$("<div class=\"datagrid-mask-msg\"></div>").html("加载中...").appendTo("#flashcontent").css({display:"block",left:left2,top:top}); //
//	    },
//	    complete:function(){
//	         $("#flashcontent").find("div.datagrid-mask-msg").remove();
//	    },
//		success:function(data){	    	 
//	    	$('#flashcontent').highcharts({	        
//	    		credits:{
//			      enabled:false//不显示highCharts版权信息
//				},
//	            title: {
//	                text: '<div align="center">下载量统计折线图</div>'
//	            },	           
//	            colors: ['#7CB5EC'],
//	            plotOptions: {
//	            	lineWidth: 0.5
//	            },
//	            xAxis: {	              
//	                tickInterval:Math.round(data.time.length/9),
//	                categories:data.time
//	            },
//	            yAxis: {
//	                title: {
//	                    text: '单位：GB'
//	                },
//	                min:0,
//	                max:500
//	            },
//	            tooltip: {	                
//	                crosshairs:true,
//	                shared:true
//	            },	             
//	            series: [{
//	                name: '下载量',
//	                data: data.sum
//	            }]
//	        });
//				
//		},
//		error:function(){
//			
//		}
//	});
	
	getData(start_time,end_time,statUnit,statYear);
	
	
	function onLoadSuccess(data){
				//console.log(JSON.parse(JSON.stringify(data)));
				//data.rows.each(function(index,ele){
				//	console.log(data.rows[index].sum);
				//});
				var sum ,sum2;
				for ( var i = 0; i < data.rows.length; i++) {
					var rs = data.rows;
					var temp = rs[i].sum;
					//console.log(temp);
					var temp2 = rs[i].sum2;
					//console.log(temp2);
					if(i==0){
						sum = Number(temp.substr(0,temp.indexOf("G")));
						sum2 = Number(temp2);
					}else{
						sum+= Number(temp.substr(0,temp.indexOf("G")));
						sum2+= Number(temp2);
					}
				}
				//console.log(sum+"GB");
				//console.log(sum2+"次");
				$('.tlists').datagrid('insertRow',{
					index: 0,	// 索引从0开始
					row: {
						sum: sum+"GB",
						sum2: sum2,
						time: '总量'
					}
				});

				
				
				
				
	}
	//初始化数据表格
	$('.tlists').datagrid({    
		idField:'flashcontent',
	    url:'${ctx}/sys/StatisticsDownload/statdownload_statTotalListByTime',  
	    data:{
			"start_time":start_time =="" ? "" : start_time,
			"end_time":end_time =="" ? "" : end_time,
			"statUnit":statUnit=="" ? "":statUnit,
			"statYear":statYear=="" ? "":statYear
		},
	    loadMsg:'正在努力加载数据，请稍后...', //在从远程站点加载数据的时候显示提示消息。
	    pagination:true, //如果为true，则在DataGrid控件底部显示分页工具栏。
	    pageList:[15,30,50],
	    rownumbers:true, //如果为true，则显示一个行号列。
	    multiSort:false, //定义是否允许多列排序。  配合Column的sorttable属性使用,
	    striped:true,
	    singleSelect:true,
	    data:tableData,
	    onLoadSuccess:onLoadSuccess,
	    title: '<div align="center">访问情况统计列表</div>',	    
	    columns:[[    
	        {field:'sum',title:'下载量',width:200,sortable:true,align:'right'},     
	        {field:'sum2',title:'访问量',width:200,sortable:true,align:'right',formatter: function(sum2){
	        	return sum2+"次";
	        }},     
	        {field:'time',title:'时间',width:200,sortable:true,halign:'center',align:'center',
	        	formatter: function(time){
					if (time.length==8){
						return time.substring(0,4)+'-'+time.substring(4,6)+'-'+time.substring(6,8);
					} else if(time.length==6){
						return time.substring(0,4)+'-'+time.substring(4,6);
					}else{
						return time;
					}
	        	},
	        	styler: function(time){return 'padding-right:35px;';}
	        }
      
	    ]],
	    fitColumns:true	    
	});  
	
	
	
	//逐年，季，月统计
	$("#btn3").on("click",function(){
		statUnit = $("#statUnit").combobox("getValue");
		if(statUnit!="1"){
			statYear = $('.year:eq(0)').combobox("getValue");
		}		
		if(statUnit=="1"||statYear!="0"){
			$(".btnTip:eq(0)").hide();
			getData(start_time,end_time,statUnit,statYear);
			$('.tlists').datagrid('load',{
				statUnit: statUnit,
				statYear: statYear
			});	
			
			redrawChart();
//			//重新加载chart数据		
//			chart.xAxis[0].setCategories(chartData.time);
//			chart.xAxis[0].setOptions({ 
//					tickInterval:1		
//			});
//			chart.series[0].remove(false);
//			chart.addSeries({
//				name:'访问量',
//				 type: 'column',
//		         data: chartData.sum,
//		         pointWidth: 30
//			});
//			chart.redraw();
		}else{
			$(".btnTip:eq(0)").show();
		}
	});
	

	//自定义起始年月查询
	$("#btn2").on("click",function(){
		start_time = $('#startTime').datebox("getValue").replace(/-/g, "");
		end_time = $('#endTime').datebox("getValue").replace(/-/g, "");
		statUnit = null;
		statYear = null;
		getData(start_time,end_time,statUnit,statYear);
		$('.tlists').datagrid('load',{
			start_time: start_time,
			end_time: end_time,
			statUnit: null,
			statYear: null
		});
		
		redrawChart();
//		//重绘图表		
//		chart.xAxis[0].setCategories(chartData.time);
//		chart.xAxis[0].setOptions({
//				tickInterval:Math.round(chartData.time.length/6)
//		});
//		chart.series[0].remove(false);
//		chart.addSeries({
//			name:'总访问量',
//			 type: 'line',
//	         data: chartData.sum
//		});
//		chart.redraw();
	});
	
	//选择时段查询
	$("#btn").on("click",function(){
	  var year = $('.year:eq(1)').combobox("getValue"),
		 	month = $('#month').combobox("getValue").split(",") , 
		 	quarter;  //季度或上半年、下半年
		if(year!='0'){
			$(".btnTip:eq(1)").hide();
			if(month!=0){
				start_time = year + month[0];
				end_time = year + month[1];
			}else{
				quarter = $(".quarter:checked").val().split(","),
				start_time = year + quarter[0];
				end_time = year + quarter[1];
			}
			
			statUnit = null;
			statYear = null;
			getData(start_time,end_time,statUnit,statYear);
			$('.tlists').datagrid('load',{
				start_time: start_time,
				end_time: end_time,
				statUnit: null,
				statYear: null
			});
			
			redrawChart();
//			//重绘图表		
//			chart.xAxis[0].setCategories(chartData.time);
//			chart.xAxis[1].setCategories(chartData.time);
//			chart.xAxis[1].setOptions({
//					tickInterval:Math.round(chartData.time.length/6)
//			});
//			chart.series[0].remove(false);
//			chart.series[0].remove(false);
//			chart.addSeries({
//	            name: '访问量',
//	            color: '#4572A7',
//	            type: 'column',
//	            yAxis: 1,
//	            data: chartData.sum2,
//	            tooltip: {
//	                valueSuffix: '次'
//	            }
//
//	        }, {
//	            name: '下载量',
//	            color: '#89A54E',
//	            type: 'spline',
//	            data: chartData.sum,
//	            tooltip: {
//	                valueSuffix: 'GB'
//	            }
//	        });
//			chart.redraw();
		}else{
			$(".btnTip:eq(1)").show();
		}
		
	});
	
	/*
	 * summary:季度radio控制
	 * description:选择季度或下半年、上半年时，就禁用选择月份下拉框
	 * */
	$(".quarter").on("click",function(){
		if($(this).attr("checked")=="checked"){
			$('#month').combobox("setValue",'0');
		}
	});
	
	
//	$('#flashcontent').highcharts({	        
//		credits:{
//	      enabled:false//不显示highCharts版权信息
//		},
//        title: {
//            text: '<div align="center">下载量统计图表</div>'
//        },	           
//        colors: ['#7CB5EC'],
//        plotOptions: {
//        	lineWidth: 0.5
//        },
//        xAxis: {	              
//            tickInterval:Math.round(chartData.time.length/9),
//            categories:chartData.time
//        },
//        yAxis: {
//            title: {
//                text: '单位：GB'
//            },
//            min:0
//        },
//        tooltip: {	                
//            crosshairs:true,
//            shared:true
//        },	             
//        series: [{
//            name: '访问量',
//            type: 'column',
//            yAxis: 1,
//            color: '#4572A7',
//            data: chartData.sum2
//        },{
//            name: '下载量',
//            data: chartData.sum,
//            color: '#89A54E',
//            type: 'spline'
//        }]
//    });
	
	
//	$('#flashcontent').highcharts({
//		credits:{
//	      enabled:false//不显示highCharts版权信息
//		},
//        chart: {
//            zoomType: 'xy'
//        },
//        title: {
//            text: 'Average Monthly Temperature and Rainfall in Tokyo'
//        },
//        subtitle: {
//            text: 'Source: WorldClimate.com'
//        },
//        xAxis: [{
//        	 tickInterval:Math.round(chartData.time.length/9),
//             categories:chartData.time
//        }],
//        yAxis: [{ // Primary yAxis
//            labels: {
//                format: '{value}°C',
//                style: {
//                    color: '#89A54E'
//                }
//            },
//            title: {
//                text: 'Temperature',
//                style: {
//                    color: '#89A54E'
//                }
//            }
//        }, { // Secondary yAxis
//            title: {
//                text: 'Rainfall',
//                style: {
//                    color: '#4572A7'
//                }
//            },
//            labels: {
//                format: '{value} mm',
//                style: {
//                    color: '#4572A7'
//                }
//            },
//            opposite: true
//        }],
//        tooltip: {
//            shared: true
//        },
//        legend: {
//            layout: 'vertical',
//            align: 'left',
//            x: 120,
//            verticalAlign: 'top',
//            y: 100,
//            floating: true,
//            backgroundColor: '#FFFFFF'
//        },
//        series: [{
//            name: '访问量',
//            color: '#4572A7',
//            type: 'column',
//            yAxis: 1,
//            data: chartData.sum2,
//            tooltip: {
//                valueSuffix: ' mm'
//            }
//
//        }, {
//            name: '下载量',
//            color: '#89A54E',
//            type: 'spline',
//            data: chartData.sum,
//            tooltip: {
//                valueSuffix: '°C'
//            }
//        }]
//    });
	
	redrawChart();
	
	var chart = $('#flashcontent').highcharts();
	
	
	
});