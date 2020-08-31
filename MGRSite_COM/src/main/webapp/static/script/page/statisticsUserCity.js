var tableData , //table data
		chartData, //chart data
		end_time,  //
		start_time,	
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
function getData (start_time,end_time){
	$.ajax({
		url:'statuserinfo_statUserCityByTime.action',
		type:'post',
		cache:false,
		dataType:'json',
		async:false,
		data:{
			"start_time":start_time =="" ? "" : start_time,
			"end_time":end_time =="" ? "" : end_time
		},
		beforeSend:function(){
			
	    },
	    complete:function(){
	    },
		success:function(data){
	    	tableData = data.list;
	    	chartData = data.picture;
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
		value:"3",
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
			if(typeValue=='4'){
				$("#typeAction").attr("action","statuserinfo_corelist.action");
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
		}
//		{
//			typeValue:"4",
//			typeName:'重要用户清单'	
//		}
		]
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
	
	//初始化年份选择下拉框
	$('.year').combobox({
		url:'stataccess_statYears.action',
	    valueField:'yearValue',      //值
	    textField:'yearName',   	//显示的名称
	    editable:false,
	    width:70,
	    value:"0",
	    onSelect:function(){
			if($('.year').combobox("getValue") =="0"){
				$("#typeAction").attr("action","statuserinfo_citylist.action");
				$("#typeAction").submit();
			}
		}
	});  
	
	
	getData();
	
	
	//初始化数据表格
	$('.tlists').datagrid({    
		idField:'flashcontent',
	    url:'statuserinfo_statUserCityByTime.action', 
	    title:'<div align="center">会员地域分布列表<div align="center">',
	    halign:'center',
	    loadMsg:'正在努力加载数据，请稍后...', //在从远程站点加载数据的时候显示提示消息。
	    //pagination:true, //如果为true，则在DataGrid控件底部显示分页工具栏。
	    rownumbers:true, //如果为true，则显示一个行号列。
	    multiSort:true, //定义是否允许多列排序。  配合Column的sorttable属性使用,
	    data:tableData,
	    columns:[[    
	        {field:'city',title:'地域',width:200,sortable:true,align:'right',halign:'center'},
	        {field:'general',title:'一般会员数',width:175,sortable:true,align:'right',halign:'center'},
	        {field:'core',title:'核心会员数',width:175,sortable:true,align:'right',halign:'center'},
	        {field:'sum',title:'总会员数',width:195,sortable:true,align:'right',halign:'center'}
	    ]]    
	});  

	//自定义起始年月查询
	$("#btn2").on("click",function(){
		start_time = $('#startTime').datebox("getValue").replace(/-/g, "");
		end_time = $('#endTime').datebox("getValue").replace(/-/g, "");
//		$('.tlists').datagrid('reload',{
//			start_time: start_time,
//			end_time: end_time
//		});
		getData(start_time,end_time);
		$('.tlists').datagrid('loadData',tableData); //重新加载table数据
		//重新加载chart数据
		chart.series[0].setData(chartData.general);
		chart.series[1].setData(chartData.core);
		chart.redraw();
	});
	
	//选择时段查询
	$("#btn").on("click",function(){
		var year = $('.year').combobox("getValue"),
		 	month = $('#month').combobox("getValue").split(",") , 
		 	quarter;  //季度或上半年、下半年
		if(year!='0'){
			if(month!=0){
				start_time = year + month[0];
				end_time = year + month[1];
			}else{
				quarter = $(".quarter:checked").val().split(","),
				start_time = year + quarter[0];
				end_time = year + quarter[1];
			}
			
			getData(start_time,end_time);
			$('.tlists').datagrid('loadData',tableData);
			chart.series[0].setData(chartData.general);
			chart.series[1].setData(chartData.core);
			chart.redraw();
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
        chart: {
            type: 'column'
        },
        title: {
            text: '按地域统计会员注册数'
        },
        
        xAxis: {
            categories: chartData.city
        },
        yAxis: {
            min: 0,
            title: {
                text: '单位：人（次）'
            }
        },
        tooltip: {
        	headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f} 人（次）</b></td></tr>',
            footerFormat: '</table>',
            //数据提示框显示的位置
//            positioner: function () {	        	        
//            },
            shared: true,
            useHTML: true
        },
        plotOptions: {

        },
        credits:{
		      enabled:false//不显示highCharts版权信息
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