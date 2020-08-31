	var tableData , //table data
	chartData, //chart data
	end_time,  //
	start_time,	
	timeType,
	org,
	chart; //chart组件
	
	$(function(){
		//初始化统计类型下拉框
		$("#type").combobox({
			valueField:"typeValue",
			textField:'typeName',
			value:"4",
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
		$(".btnTippp").hide();
		
		// 初始化大院选择下拉框
		$('.province').combobox({
			url : ctx+'/statistics/statisticsDownloadTotal/getOrgType?orgType=center',
			valueField : 'orgValue', // 值
			textField : 'orgName', // 显示的名称
			editable : false,
			width : 70,
			value : '0'
		});
		
		
		//初始化折线图
		getData (start_time,end_time,timeType,org);
		//初始化表格
		showtable();

		// 按年统计
		$("#btn1").on("click", function() {
			var org = $('.province').combobox("getValue");
			if(org!=0){
				$(".btnTippp:eq(0)").hide();
				start_time = $('#startTime1').val().replace(/-/g, "");
				end_time = $('#endTime1').val().replace(/-/g, "");
				var temp=compareTime(start_time,end_time,0);
				if(temp!=1){
					return false;
				}
				timeType="4";
				getData (start_time,end_time,timeType,org);
				$('.tlists').datagrid('load', {
					start_time : start_time,
					end_time : end_time,
					timeType : timeType,
					org   :org
				});
				// 重新加载chart数据
				//副标题设置
				var subtitle = {  text: ''}
				chart.setTitle(null, subtitle);
				
				chart.xAxis[0].setCategories(chartData.time);
				chart.xAxis[0].setOptions({
					tickInterval : 1
				});
				chart.series[0].remove(false);
				chart.addSeries({
					name:'下载量',
					 type: 'column',
					 tooltip: {
			                valueSuffix: ' GB'
			         },
			         data: chartData.sum,
			         pointWidth: 30
				});
				chart.redraw();
			}else{
				$(".btnTippp:eq(0)").show();
			}
			
		});
		
		// 按月统计
		$("#btn2").on("click", function() {
			var org = $('.province').combobox("getValue");
			if(org!=0){
				$(".btnTippp:eq(1)").hide();
				start_time = $('#startTime2').val().replace(/-/g, "");
				end_time = $('#endTime2').val().replace(/-/g, "");
				var temp=compareTime(start_time,end_time,1);
				if(temp!=1){
					return false;
				}
				timeType="6";
				getData (start_time,end_time,timeType,org);
				$('.tlists').datagrid('load', {
					start_time : start_time,
					end_time : end_time,
					timeType : timeType,
					org   :org
				});
				// 重新加载chart数据
				//副标题设置
				var subtitle = {  text: ''}
				chart.setTitle(null, subtitle);
				
				//鼠标悬停时，x轴数值显示
				var x_chartData=[];
				var x_time=chartData.time;
				for(var i=0;i<x_time.length;i++){
					x_chartData[i]=x_time[i].toString().substring(0,4)+"-"+x_time[i].toString().substring(4,6);
				}
				
//				chart.xAxis[0].setCategories(chartData.time);
				chart.xAxis[0].setCategories(x_chartData);
				chart.xAxis[0].setOptions({
					tickInterval : 1
				});
				chart.series[0].remove(false);
				chart.addSeries({
					name:'下载量',
					 type: 'line',
					 tooltip: {
			                valueSuffix: ' GB'
			         },
			         data: chartData.sum,
			         pointWidth: 30
				});
				chart.redraw();
			}else{
				$(".btnTippp:eq(1)").show();
			}
			
		});
		
		//按日查询
		$("#btn3").on("click",function(){
			var org = $('.province').combobox("getValue");
			if(org!=0){
				$(".btnTippp:eq(2)").hide();
				start_time = $('#startTime3').val().replace(/-/g, "");
				end_time = $('#endTime3').val().replace(/-/g, "");
				var temp=compareTime(start_time,end_time,2);
				if(temp!=1){
					return false;
				}
				timeType="8";
				getData (start_time,end_time,timeType,org);
				$('.tlists').datagrid('load',{
					start_time: start_time,
					end_time: end_time,
					timeType : timeType,
					org   :org
				});
				//重绘图表	
				//副标题设置
				var subtitle = {  text: ''}
				chart.setTitle(null, subtitle);
				
				var x_chartData=[];
				var x_time=chartData.time;
				for(var i=0;i<x_time.length;i++){
					x_chartData[i]=x_time[i].toString().substring(0,4)+"-"
								  +x_time[i].toString().substring(4,6)+"-"
								  +x_time[i].toString().substring(6,8);
				}
//				chart.xAxis[0].setCategories(chartData.time);
				chart.xAxis[0].setCategories(x_chartData);
				chart.xAxis[0].setOptions({
						tickInterval:Math.round(chartData.time.length/6)
				});
				chart.series[0].remove(false);
				chart.addSeries({
					name:'总下载量',
					 type: 'line',
					 tooltip: {
			                valueSuffix: ' GB'
			         },
			         data: chartData.sum
				});
				chart.redraw();
			}else{
				$(".btnTippp:eq(2)").show();
			}
			
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

		var x_chartData=[];
		var x_time=chartData.time;
		for(var i=0;i<x_time.length;i++){
			x_chartData[i]=x_time[i].toString().substring(0,4)+"-"
						  +x_time[i].toString().substring(4,6)+"-"
						  +x_time[i].toString().substring(6,8);
		}
		
		$('#flashcontent').highcharts({	        
			credits:{
		      enabled:false//不显示highCharts版权信息
			},
		    title: {
		        text: '数据下载服务量统计图表'
		    },
		    subtitle: {
	            text:"("+ x_chartData[0]+"~"+x_chartData[x_chartData.length-1]+")"
	        },
		    colors: ['#7CB5EC'],
		    plotOptions: {
		    	lineWidth: 0.5
		    },
		    xAxis: {	
		    	tickInterval:0,
		        categories:chartData.time
		    },
		    yAxis: {
		        title: {
		            text: '单位：GB'
		        },
		        min:0
		    },
		    
		    tooltip: {	                
		        crosshairs:true,
		        shared:true
		    },	             
		    series: [{
		        name: '下载量',
		        type: 'column',
		        tooltip: {
	                valueSuffix: ' GB'
	            },
		        data: chartData.sum
		    }]
		});
		chart = $('#flashcontent').highcharts();
});

//获取table和chart的数据
function getData (start_time,end_time,timeType,org){
	$.ajax({
		url:ctx+'/statistics/statisticsDownloadTotal/statCenterPictureByTime3',
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
}	
	
function showtable(){
	
	$('.tlists').datagrid({    
		idField:'flashcontent',
	    url:ctx+'/statistics/statisticsDownloadTotal/statCenterListByTime3',  
	    data:{
			"start_time":start_time =="" ? "" : start_time,
			"end_time":end_time =="" ? "" : end_time,
			"timeType":timeType=="" ? "" : timeType,
			"org":org==""?"":org	
					
		},
	    loadMsg:'正在努力加载数据，请稍后...', //在从远程站点加载数据的时候显示提示消息。
	    pagination:true, //如果为true，则在DataGrid控件底部显示分页工具栏。
	    pageList:[15,30,50],
	    rownumbers:true, //如果为true，则显示一个行号列。
	    multiSort:false, //定义是否允许多列排序。  配合Column的sorttable属性使用,
	    striped:true,
	    singleSelect:true,
	    data:tableData,
	    title: '<div align="center">数据下载服务量统计列表</div>',	    
	    columns:[[    
	        {
	        	field:'time',
	        	title:'大院/时间',
	        	width:200,
	        	sortable:true,
	        	halign:'left',
	        	align:'left',
	        	formatter: function(time){
					if (time.length==8){
						return time.substring(0,4)+'-'+time.substring(4,6)+'-'+time.substring(6,8);
					} else if(time.length==6){
						return time.substring(0,4)+'-'+time.substring(4,6);
					}else{
						return time;
					}
	        	}
	        },
	        {
	        	field:'sum',
	        	title:'下载量',
	        	width:200,
	        	sortable:true,
	        	align:'center'
	        }
	  
	    ]],
	    fitColumns:true	    
	});

}	




