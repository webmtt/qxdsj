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
	$.ajax({
		url:'statuserinfo_statUserListByTime.action',
		type:'post',
		cache:false,
		dataType:'json',
		async:false,
		data:{
			"start_time":start_time =="" ? "" : start_time,
			"end_time":end_time =="" ? "" : end_time,
			"timeType":"" ? "":"yyyymmDD",
			"statUnit":statUnit=="" ? "":statUnit,
			"statYear":statYear=="" ? "":statYear		
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
	$.ajax({
		url:'statuserinfo_statUserPictureByTime.action',
		type:'post',
		cache:false,
		dataType:'json',
		async:false,
		data:{
			"start_time":start_time =="" ? "" : start_time,
			"end_time":end_time =="" ? "" : end_time,
			"timeType":"" ? "":"yyyymmDD",
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
				$("#typeAction").attr("action","statuserinfo_totallist.action");		
			}
			if(typeValue=='2'){
				$("#typeAction").attr("action","statuserinfo_fieldlist.action");		
			}
			if(typeValue=='3'){
				$("#typeAction").attr("action","statuserinfo_citylist.action");	
			}
			$("#typeAction").submit();
		},
		data:[{
			typeValue:"1",
			typeName:'按总量统计'
		},{
			typeValue:"2",
			typeName:'按行业统计'
		},{
			typeValue:"3",
			typeName:'按地域统计'	
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
	
	//初始化年份选择下拉框
	$('.year').combobox({
		url:'stataccess_statYears.action',
	    valueField:'yearValue',      //值
	    textField:'yearName',   	//显示的名称
	    editable:false,
	    width:70,
	    value:'0'
	});  
	
	
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
			monthValue: '0201,02.31',
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
	
	
	getData(start_time,end_time);
	//初始化数据表格
	$('.tlists').datagrid({    
		idField:'flashcontent',
	    url:'statuserinfo_statUserListByTime.action',  
		queryParams: {
			'start_time':start_time =="" ? "" : start_time,
			"end_time":end_time =="" ? "" : end_time,
			"statUnit":statUnit,
			"statYear":statYear
		},
	    loadMsg:'正在努力加载数据，请稍后...', //在从远程站点加载数据的时候显示提示消息。
	    pagination:true, //如果为true，则在DataGrid控件底部显示分页工具栏。
	    pageList:[15,30,50],
	    rownumbers:true, //如果为true，则显示一个行号列。
	    multiSort:false, //定义是否允许多列排序。  配合Column的sorttable属性使用,
	    striped:true,
	    singleSelect:true,
	    data:tableData,
	    title: '用户信息统计列表',	    
	    columns:[[    
	        {field:'all',title:'总会员数',width:120,sortable:true,align:'right'},
	        {field:'general',title:'一般会员数',width:120,sortable:true,align:'right'},
	        {field:'core',title:'核心会员数',width:120,sortable:true,align:'right'},	        
	        {field:'time',title:'时间',width:170,sortable:true,halign:'center',align:'right',
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
	
	
	
	//逐年，季，月统计查询
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
			//重新加载chart数据		
			chart.xAxis[0].setCategories(chartData.time);
			chart.xAxis[0].setOptions({ 
					tickInterval:1		
			});
			chart.series[0].remove(false);
			chart.series[0].remove(false);
			chart.addSeries({
				name:'一般会员',
				 type: 'column',
		         data: chartData.general,
		         pointWidth: 15
			});
			chart.addSeries({
				name:'核心会员',
				 type: 'column',
		         data: chartData.core,
		         pointWidth: 15
			});
			chart.redraw();
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
	
		//重新加载chart数据
		chart.xAxis[0].setCategories(chartData.time);
		chart.xAxis[0].setOptions({
				tickInterval:Math.round(chartData.time.length/6)
		});
		chart.series[0].remove(false);
		chart.series[0].remove(false);
		chart.addSeries({
			name:'一般会员',
			 type: 'line',
	         data: chartData.general,
	         pointWidth: 15
		});
		chart.addSeries({
			name:'核心会员',
			 type: 'line',
	         data: chartData.core,
	         pointWidth: 15
		});
		chart.redraw();
		
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
			chart.xAxis[0].setCategories(chartData.time);
			chart.xAxis[0].setOptions({
					tickInterval:Math.round(chartData.time.length/6)
			});
			chart.series[0].remove(false);
			chart.series[0].remove(false);
			chart.addSeries({
				name:'一般会员',
				 type: 'line',
		         data: chartData.general,
		         pointWidth: 15
			});
			chart.addSeries({
				name:'核心会员',
				 type: 'line',
		         data: chartData.core,
		         pointWidth: 15
			});			
			chart.redraw();
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
	
	
	$('#flashcontent').highcharts({	            
        title: {
            text: '用户信息统计图表'
        },	           
        colors: [ '#7CB5EC','#F6A764'],
        plotOptions: {
        	lineWidth: 0.5
        },
        xAxis: {	              
            tickInterval:Math.round(chartData.time.length/6),
            categories:chartData.time
        },
        yAxis: {
            title: {
                text: '单位：人次'
            },
            min:0
        },
        tooltip: {	                
            crosshairs:true,
            shared:true
        },	             
        series: [{
            name: '一般会员',
            data: chartData.general
        },{
            name: '核心会员',
            data: chartData.core
        }]
    });

	chart = $('#flashcontent').highcharts();
});