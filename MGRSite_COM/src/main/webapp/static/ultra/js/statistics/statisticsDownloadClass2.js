var tableData , //table data
	chartData, //chart data
	end_time,  //
	start_time,	
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

	
	$(".btnTip2:eq(0)").hide();
	
	//初始化年份选择下拉框
	$('.year').combobox({
		url:ctx+'/statistics/statisticsDownloadTotal/statYears',
	    valueField:'yearValue',      //值
	    textField:'yearName',   	//显示的名称
	    editable:false,
	    width:70,
	    value:'0'
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


	getData();

	//初始化数据表格
	showtable();

	//自定义起始年月查询
	$("#btn2").on("click",function(){
		start_time = $('#startTime').datebox("getValue").replace(/-/g, "");
		end_time = $('#endTime').datebox("getValue").replace(/-/g, "");
		$('.tlists').datagrid('reload',{
			start_time: start_time,
			end_time: end_time
		});
		getData(start_time,end_time);
		$('.tlists').datagrid('loadData',tableData); //重新加载table数据
		//重新加载chart数据
		chart.series[0].remove(false);
		chart.addSeries({
			 type: 'pie',
	         name: '比例',
	         data: chartData
		});
		chart.redraw();
		
	});

	//选择时段查询
	$("#btn").on("click",function(){
		var year = $('.year').combobox("getValue");
		var month = $('#month').combobox("getValue").split(","); 
		if(year!='0' && month!=0){
			$(".btnTip2:eq(0)").hide();
//			if(month!=0){
				start_time = year + month[0];
				end_time = year + month[1];
//			}
			getData(start_time,end_time);
			$('.tlists').datagrid('reload',{
				start_time: start_time,
				end_time: end_time
			});
			$('.tlists').datagrid('loadData',tableData);
			chart.series[0].remove(false);
			chart.addSeries({
				 type: 'pie',
		         name: 'Browser share',
		         data: chartData
			});
			chart.redraw();
		}else{
			$(".btnTip2:eq(0)").show();
		}
		
	});


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
	        text: 'FTP分类统计下载量'
	    },
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
	    				var showname = '';
	    				if(name.length>15){
	    						showname = name.substr(0,15)+"……";
	    				}else{
	    					showname = name;      					
	    				}
	    				return showname;
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
function getData (start_time,end_time){
	$.ajax({
		url:ctx+'/statistics/statisticsDownloadTotal/statClassByTime_picture',
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
	    	chartData = data;
	    },
		error:function(){
			
		}
	});
	$.ajax({
		url:ctx+'/statistics/statisticsDownloadTotal/statClassByTime_list',
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
	    	tableData = data;
	    	
	    },
		error:function(){
			
		}
	});
}


function showtable(){
	$('.tlists').datagrid({    
		idField:'flashcontent',
	    url:ctx+'/statistics/statisticsDownloadTotal/statClassByTime_list',
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



/*
$(document).ready(function() {
	//文件导出
	$("#expExcel").click(function(){
		var url = ctx+'/statistics/statisticsDownloadTotal/exportExcel';
		openPostWindow(url, "1", "new");
	});
	// 表格排序
});
*/
function openPostWindow(url, type, name){  
    var tempForm = document.createElement("form");  
    tempForm.id="tempForm1";  
    tempForm.method="post";  
    tempForm.action=url;  
    tempForm.target=name;  
    var typeInp = document.createElement("input");  
    typeInp.type="hidden";  
    typeInp.name= "type";
    typeInp.value= type;
   tempForm.appendChild(typeInp);   
   document.body.appendChild(tempForm);  
   openWindow(name);
   tempForm.submit();
   document.body.removeChild(tempForm);
}	
	

function openWindow(name){  
    window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');   
} 

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








