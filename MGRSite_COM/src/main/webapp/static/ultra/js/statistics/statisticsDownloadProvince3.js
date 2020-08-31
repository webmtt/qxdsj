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
			value:"3",
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
//		$(".btnTippp").hide();
		
		// 初始化省份选择下拉框
//		$('.province').combobox({
//			url : ctx+'/statistics/statisticsDownloadTotal/getOrgType?orgType=province',
//			valueField : 'orgValue', // 值
//			textField : 'orgName', // 显示的名称
//			editable : false,
//			width : 70,
//			value : '0'
//		});
		
		
		//初始化折线图
		getData (start_time,end_time,timeType);
		//初始化表格
		showtable();

		// 按年统计
		$("#btn1").on("click", function() {
			start_time = $('#startTime1').val().replace(/-/g, "");
			end_time = $('#endTime1').val().replace(/-/g, "");
			var temp=compareTime(start_time,end_time,0);
			if(temp!=1){
				return false;
			}
			timeType="4";
			getData (start_time,end_time,timeType);
			$('.tlists').datagrid('load', {
				start_time : start_time,
				end_time : end_time,
				timeType : timeType
			});
			// 重新加载chart数据
			chart.xAxis[0].setCategories(chartData.province);
			chart.xAxis[0].setOptions({
				tickInterval : 1
			});
			chart.series[0].remove(false);
			chart.addSeries({
				name:'下载量',
				 type: 'column',
		         data: chartData.sum,
		         pointWidth: 30
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
			getData (start_time,end_time,timeType);
			$('.tlists').datagrid('load', {
				start_time : start_time,
				end_time : end_time,
				timeType : timeType
			});
			// 重新加载chart数据
			chart.xAxis[0].setCategories(chartData.province);
			chart.xAxis[0].setOptions({
				tickInterval : 1
			});
			chart.series[0].remove(false);
			chart.addSeries({
				name:'下载量',
				 type: 'line',
		         data: chartData.sum,
		         pointWidth: 30
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
			getData (start_time,end_time,timeType);
			$('.tlists').datagrid('load',{
				start_time: start_time,
				end_time: end_time,
				timeType : timeType
			});
			//重绘图表		
			chart.xAxis[0].setCategories(chartData.province);
			chart.xAxis[0].setOptions({
					tickInterval:Math.round(chartData.province.length/6)
			});
			chart.series[0].remove(false);
			chart.addSeries({
				name:'总下载量',
				 type: 'line',
		         data: chartData.sum
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
		

		$('#flashcontent').highcharts({	        
			credits:{
		      enabled:false//不显示highCharts版权信息
			},
		    title: {
		        text: 'FTP按省下载量统计图表'
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
		        categories:chartData.province
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
		        data: chartData.sum
		    }]
		});
		chart = $('#flashcontent').highcharts();
});

//获取table和chart的数据
function getData (start_time,end_time,timeType){
	$.ajax({
		url:ctx+'/statistics/statisticsDownloadTotal/statProvincePictureByTime2',
		type:'post',
		cache:false,
		dataType:'json',
		async:false,
		data:{
			"start_time":start_time =="" ? "" : start_time,
			"end_time":end_time =="" ? "" : end_time,
			"timeType":timeType==""?"":timeType
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
	    url:ctx+'/statistics/statisticsDownloadTotal/statProvinceListByTime2',  
	    data:{
			"start_time":start_time =="" ? "" : start_time,
			"end_time":end_time =="" ? "" : end_time,
			"timeType":timeType== "" ? "" : timeType
		},
	    loadMsg:'正在努力加载数据，请稍后...', //在从远程站点加载数据的时候显示提示消息。
	    pagination:true, //如果为true，则在DataGrid控件底部显示分页工具栏。
	    pageList:[15,30,50],
	    rownumbers:true, //如果为true，则显示一个行号列。
	    multiSort:false, //定义是否允许多列排序。  配合Column的sorttable属性使用,
	    striped:true,
	    singleSelect:true,
	    data:tableData,
	    title: '<div align="center">下载量统计列表</div>',	    
	    columns:[[    
	        {
	        	field:'province',
	        	title:'省份',
	        	width:200,
	        	sortable:true,
	        	halign:'left',
	        	align:'left'
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






