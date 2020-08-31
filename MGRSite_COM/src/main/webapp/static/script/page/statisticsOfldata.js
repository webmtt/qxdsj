function openPostWindow(url, data, name)  
      {  
       var tempForm = document.createElement("form");  
       tempForm.id="tempForm1";  
       tempForm.method="post";  
       tempForm.action=url;  
       tempForm.target=name;  
       var hideInput = document.createElement("input");  
      hideInput.type="hidden";  
      hideInput.name= "jsonStr"
      hideInput.value= data;
      tempForm.appendChild(hideInput);   
     // tempForm.attachEvent("onsubmit",function(){ openWindow(name); });
      document.body.appendChild(tempForm);  
      openWindow(name);
      //tempForm.fireEvent("onsubmit");
      tempForm.submit();
      document.body.removeChild(tempForm);
 }
     
  function openWindow(name)  
  {  
      window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');   
  } 
  

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
		url:'statofldata_ofldata.action',
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
//	    	chartData = data.picture;
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
	data:[{
		typeValue:"1",
		typeName:'总览'
	}]
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
	    value:'0'
	});  
	
	
	//初始化数据表格
	$('.tlists').datagrid({    
		idField:'flashcontent',
	    url:'statofldata_ofldata.action', 
	    title:'用户定制需求清单',
	    loadMsg:'正在努力加载数据，请稍后...', //在从远程站点加载数据的时候显示提示消息。
//	    singleSelect:true,
	    striped:true,
	    pagination:true, //如果为true，则在DataGrid控件底部显示分页工具栏。
	    pageNumber:1,
	    pageSize:30,
	    pageList:[10,20,30,50,100],
	    fitColumns:true,
	    rownumbers:true, //如果为true，则显示一个行号列。
	    multiSort:false, //定义是否允许多列排序。  配合Column的sorttable属性使用,
	    data:tableData,
	    toolbar:'#tb2',
	    columns:[[    
	        {field:'ck',checkbox:true},
	        {field:'trade',title:'行业',sortable:true},
	        {field:'company',title:'单位全称',sortable:true},
	        {field:'proname',title:'项目信息',width:500,sortable:true},
	        {field:'datasize',title:'数据量（MB）',sortable:true}
	    ]]    
	});  

	//自定义起始年月查询
	$("#btn2").on("click",function(){
		start_time = $('#startTime').datebox("getValue").replace(/-/g, "");
		end_time = $('#endTime').datebox("getValue").replace(/-/g, "");
		$('.tlists').datagrid('load',{
			start_time: start_time,
			end_time: end_time
		});
	});
	$(".btnTip").hide();
	//选择时段查询
	$("#btn").on("click",function(){
		var year = $('.year').combobox("getValue"),
		 	month = $('#month').combobox("getValue").split(",") , 
		 	quarter;  //季度或上半年、下半年
		if(year!='0'){
			$(".btnTip").hide();
			if(month!=0){
				start_time = year + month[0];
				end_time = year + month[1];
			}else{
				quarter = $(".quarter:checked").val().split(","),
				start_time = year + quarter[0];
				end_time = year + quarter[1];
			}
			$('.tlists').datagrid('load',{
				start_time: start_time,
				end_time: end_time
			});
		}else{
			$(".btnTip").show();
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
	
	
	$("#expor2").click(function(){
		var $tlists_detail = $('.tlists');
		var rows  = $tlists_detail.datagrid("getSelections");
		var jsonStr ;
		if(rows.length==0){
			var data   = $tlists_detail.datagrid("getData");  
			jsonStr = JSON.stringify(data.rows)
		}else{
			jsonStr = JSON.stringify(rows);
		}
		var url = "statofldata_exporExcel.action";
		//window.open ('statservs_exporExcel2.action?jsonStr='+jsonStr) ;
		openPostWindow(url, jsonStr, "new")  ;
	});
	
	
	
});