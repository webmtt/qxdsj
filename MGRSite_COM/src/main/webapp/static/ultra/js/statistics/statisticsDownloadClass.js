var tableData , //table data
	chartData, //chart data
	end_time,  //
	start_time,	
	timeType,
	chart; //chart组件

$(function(){

	//初始化统计类型下拉框
	$("#type").combobox({
		valueField:"typeValue",
		textField:'typeName',
		value:"2",
		panelHeight:"auto",
		onSelect:function(record){
		var typeValue = record.typeValue;
			if(typeValue=='1'){
				$("#typeAction").attr("action",ctx+"/statistics/statisticsDownloadTotal/statisticsDownloadTotalList");		
			}
			if(typeValue=='2'){
				$("#typeAction").attr("action",ctx+"/statistics/statisticsDownloadTotal/statisticsDownloadClassList");	
			}
			if(typeValue=='3'){
				$("#typeAction").attr("action",ctx+"/statistics/statisticsDownloadTotal/statisticsDownloadProvinceList");	
			}
			if(typeValue=='4'){
				$("#typeAction").attr("action",ctx+"/statistics/statisticsDownloadTotal/statisticsDownloadCenterList");	
			}
			$("#typeAction").submit();
		},
		data:[{
			typeValue:"1",
			typeName:'按总量统计'
		},{
			typeValue:"2",
			typeName:'按分类统计'	
		},{
			typeValue:"3",
			typeName:'按省统计'	
		},{
			typeValue:"4",
			typeName:'按大院用户机构统计'	
		}]
	});

	
	//开始时不显示提示
	$(".btnTip").hide();
	$(".btnTipp").hide();
	$('#selfunction').combobox({
		url : ctx+'/statistics/statisticsDownloadTotal/getOrgType',
		valueField : 'orgValue', // 值
		textField : 'orgName', // 显示的名称
		editable : false,
		width : 150,
		value : '0'
	});
	var org = $('#selfunction').datebox("getValue");
	//初始化折线图
	getData (start_time,end_time,timeType,org);
	//初始化表格
	showtable(org);

	// 按年统计
	$("#btn1").on("click", function() {
		start_time = $('#startTime1').val().replace(/-/g, "");
		end_time = $('#endTime1').val().replace(/-/g, "");
		var temp=compareTime(start_time,end_time,0);
		if(temp!=1){
			return false;
		}
		timeType="4";
		org = $('#selfunction').datebox("getValue");
		getData (start_time,end_time,timeType,org);
		$('.tlists').datagrid('load', {
			start_time : start_time,
			end_time : end_time,
			timeType : timeType,
			org : org
		});
		// 重新加载chart数据
		//副标题设置
		var subtitle = {  text: ''}
		chart.setTitle(null, subtitle);
		
		chart.series[0].remove(false);
		chart.addSeries({
			type: 'pie',
	        name: '比例',
	        data: chartData
		});
		chart.redraw();
	});
	
	// 按月统计
	$("#btn2").on("click", function() {
		start_time = $('#startTime2').val().replace(/-/g, "");
		end_time = $('#endTime2').val().replace(/-/g, "");
		var temp=compareTime(start_time,end_time,1);
		if(temp!=1){
			return false;
		}
		timeType="6";
		org = $('#selfunction').datebox("getValue");
		getData (start_time,end_time,timeType,org);
		$('.tlists').datagrid('load', {
			start_time : start_time,
			end_time : end_time,
			timeType : timeType,
			org : org
		});
		// 重新加载chart数据
		//副标题设置
		var subtitle = {  text: ''}
		chart.setTitle(null, subtitle);
		
		chart.series[0].remove(false);
		chart.addSeries({
			type: 'pie',
	        name: '比例',
	        data: chartData
		});
		chart.redraw();
	});
	
	//按日查询
	$("#btn3").on("click",function(){
		start_time = $('#startTime3').val().replace(/-/g, "");
		end_time = $('#endTime3').val().replace(/-/g, "");
		var temp=compareTime(start_time,end_time,2);
		if(temp!=1){
			return false;
		}
		timeType="8";
		org = $('#selfunction').datebox("getValue");
		getData (start_time,end_time,timeType,org);
		$('.tlists').datagrid('load',{
			start_time: start_time,
			end_time: end_time,
			timeType : timeType,
			org : org
		});
		//重绘图表
		//副标题设置
		var subtitle = {  text: ''}
		chart.setTitle(null, subtitle);
		
		
		chart.series[0].remove(false);
		chart.addSeries({
			type: 'pie',
	        name: '比例',
	        data: chartData
		});
		chart.redraw();
	});
	
	
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

	
	/**
	 * 获取当前日期
	 */
	function getDate(){
		var myDate = new Date();
		var year=myDate.getFullYear();    //获取完整的年份(4位,1970-????)
		var month = myDate.getMonth() + 1;  //获取当前月份(0-11,0代表1月)
		var strDate = myDate.getDate();     //获取当前日(1-31)
	   
		if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
		
		return year+"-"+month+"-"+strDate
	}
	
	
	 $('#flashcontent').highcharts({
	    chart: {
	        type: 'pie',
	        options3d: {
	            enabled: true,
	            alpha: 45,
	            beta: 0
	        }
	    },
	    title: {
	        text: '数据下载服务量统计图表'
	    },
//	    subtitle: {
//            text:"(2016-01-01~"+getDate()+")"
//        },
	    tooltip: {
	        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>' 	
	    },
	    plotOptions: {
	        pie: {
	            allowPointSelect: true,
	            cursor: 'pointer',
	            depth: 35,
	            dataLabels: {
	                enabled: true,
	                formatter: function(){
	    				var name = this.point.name;
	    				var percentage = this.point.percentage;
	    				var showname = '';
	    				if(name.length>15){
	    						showname = name.substr(0,15)+"……";
	    				}else{
	    					showname = name;      					
	    				}
	    				return showname+":"+percentage.toFixed(1)+"%";
	    			}
	            }       		
	        }
	    },
	    credits:{
		      enabled:false//不显示highCharts版权信息
		},
	    series: [{
	        type: 'pie',
	        name: '比例',
	        data: chartData
	    }]
	});

	 chart = $('#flashcontent').highcharts();
});


//获取table和chart的数据
function getData (start_time,end_time,timeType,org){
	$.ajax({
		url:ctx+'/statistics/statisticsDownloadTotal/statClassByTime_picture2',
		type:'post',
		cache:false,
		dataType:'json',
		async:false,
		data:{
			"start_time":start_time =="" ? "" : start_time,
			"end_time":end_time =="" ? "" : end_time,
			"timeType":timeType==""?"":timeType,
					"org":org==""?"":org
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
	$.ajax({
		url:ctx+'/statistics/statisticsDownloadTotal/statClassByTime_list2',
		type:'post',
		cache:false,
		dataType:'json',
		async:false,
		data:{
			"start_time":start_time =="" ? "" : start_time,
			"end_time":end_time =="" ? "" : end_time,
			"timeType":timeType==""?"":timeType,
					"org":org==""?"":org
		},
		beforeSend:function(){
			
	    },
	    complete:function(){
	    },
		success:function(data){
	    	tableData = data;
	    	
	    },
		error:function(){
			
		}
	});
}


function showtable(){
	$('.tlists').datagrid({    
		idField:'flashcontent',
	    url:ctx+'/statistics/statisticsDownloadTotal/statClassByTime_list2',
	    loadMsg:'正在努力加载数据，请稍后...', //在从远程站点加载数据的时候显示提示消息。
	    pagination:true, //如果为true，则在DataGrid控件底部显示分页工具栏。
	    pageList:[15,30,50],
	    rownumbers:true, //如果为true，则显示一个行号列。
	    multiSort:true, //定义是否允许多列排序。  配合Column列属性的sorttable  sortable:true 属性使用,
	   fitColumns:true,
	    columns:[[    
	        {field:'title',title:'数据集',width:410}  ,
	        {field:'orderNumber',title:'下载量',width:110} 
	    ]]    
	});  
}






