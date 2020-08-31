var data = data || {};
var dask = dask || {};
var chartData =  chartData || {};
var qiehuanFlag = false;

//弹出蒙版
var docEle = function()
{
    return document.getElementById(arguments[0]) || false;
}

function openNewDiv(_id)
{
    var m = "mask";
    if (docEle(_id)) document.body.removeChild(docEle(_id));
    if (docEle(m)) document.body.removeChild(docEle(m));

    //mask遮罩层

    var newMask = document.createElement("div");
    newMask.id = m;
    newMask.style.position = "absolute";
    newMask.style.zIndex = "1";
    _scrollWidth = Math.max(document.body.scrollWidth,document.documentElement.scrollWidth);
    _scrollHeight = Math.max(document.body.scrollHeight,document.documentElement.scrollHeight);
    newMask.style.width = _scrollWidth + "px";
    newMask.style.height = _scrollHeight + "px";
    newMask.style.top = "0px";
    newMask.style.left = "0px";
    newMask.style.background = "#33393C";
    newMask.style.filter = "alpha(opacity=40)";
    newMask.style.opacity = "0.40";
    document.body.appendChild(newMask);

    //新弹出层

    var newDiv = document.createElement("div");
    newDiv.id = _id;
    newDiv.style.position = "absolute";
    newDiv.style.zIndex = "9999";
    newDivWidth = 400;
    newDivHeight = 200;
    newDiv.style.width = newDivWidth + "px";
    newDiv.style.height = newDivHeight + "px";
    newDiv.style.top = (document.body.scrollTop + document.body.clientHeight/2 - newDivHeight/2) + "px";
    newDiv.style.left = (document.body.scrollLeft + document.body.clientWidth/2 - newDivWidth/2) + "px";
    newDiv.style.background = "#EFEFEF";
    newDiv.style.border = "1px solid #860001";
    newDiv.style.padding = "5px";
    newDiv.innerHTML = "";
    document.body.appendChild(newDiv);

    //弹出层滚动居中

    function newDivCenter()
    {
        newDiv.style.top = (document.body.scrollTop + document.body.clientHeight/2 - newDivHeight/2) + "px";
        newDiv.style.left = (document.body.scrollLeft + document.body.clientWidth/2 - newDivWidth/2) + "px";
    }
    if(document.all)
    {
        window.attachEvent("onscroll",newDivCenter);
    }
    else
    {
        window.addEventListener('scroll',newDivCenter,false);
    }

    //关闭新图层和mask遮罩层
    var newA = document.createElement("div");
    newA.innerHTML ="正在查询数据，请稍候。。。";
    newA.id="closeDiv";
    $(newA).css("text-align","center");
    $(newA).css("line-height","200px");
    newA.onclick = function(){
        if(document.all)
        {
            window.detachEvent("onscroll",newDivCenter);
        }
        else
        {
            window.removeEventListener('scroll',newDivCenter,false);
        }
        document.body.removeChild(docEle(_id));
        document.body.removeChild(docEle(m));
        return false;
    }
    newDiv.appendChild(newA);
}

$(function() {
	// 初始化统计类型下拉框
	$(".btnTip").hide();
	$(".btnTipp").hide();
	//初始化折线图
	//getData(start_time, end_time, statUnit, statYear);
	// 初始化数据表格
	//showtable();
	// 起始年月日查询
	$(".btn2").on("click", function() {
		var num = $(this).parent().attr("id");
		startTime1 = $(this).parent().find(":input[id='startTime1']").val();
		endTime1 = $(this).parent().find(":input[id='endTime1']").val();
		startTime2 = $(this).parent().find(":input[id='startTime2']").val();
		endTime2 = $(this).parent().find(":input[id='endTime2']").val();
		var temp1=compareTime(startTime1.replace(/-/g, ""),endTime1.replace(/-/g, ""),num-1);
		var temp2=compareTime(startTime2.replace(/-/g, ""),endTime2.replace(/-/g, ""),num-1);
		if(temp1!=1){
			return false;
		}
		if(temp2!=1){
			return false;
		}
		var level = $("input[name=level]:checked").val();
		var sort = $("input[name=sort]:checked").val();
		getCimissAccessData(level,sort,startTime1, endTime1, startTime2, endTime2,num);
	});
});
//获取table和chart的数据
function getCimissAccessData(level,sort,startTime1, endTime1,startTime2,endTime2,num) {
	openNewDiv('newDiv');
	$.ajax({
		url : ctx+'/statistics/statisticsCimissAccess/statisticsTotalList',
		type : 'post',
		cache : false,
		dataType : 'json',
		async : true,
		data : {
			"level" : level == "" ? "" : level,
			"sort" : sort == "" ? "" : sort,
			"startTime1" : startTime1 == "" ? "" : startTime1,
			"endTime1" : endTime1 == "" ? "" : endTime1,
			"startTime2" : startTime2 == "" ? "" : startTime2,
			"endTime2" : endTime2 == "" ? "" : endTime2,
			"num" : num
		},
		complete : function() {
			$("#closeDiv").click();
		},
		success : function(data) {
			MoveRunningDiv();
			chartData = data;
			//查询成功默认显示饼图
			$(".column").css("display","none");
			$(".tlists").css("display","none");
			$(".tableDiv").css("display","none");
			$(".pie").css("display","block");
			qiehuanFlag = false;
			$("input[name=flag]").removeAttr('checked');
			$("#radioReset")[ 0 ].checked = true;
			
			getPie("char_3","CIMISS访问量统计（单位：次）",data.numList1);
			getPie("char_4","CIMISS对比访问量统计（单位：次）",data.numList2);
			getPie("char_1","CIMISS数据下载量（单位：GB）",data.datasizeList1);
			getPie("char_2","CIMISS对比数据下载量（单位：GB）",data.datasizeList2);
		},
		error : function() {
		}
	});
}

function getPie(divId,title,list){
	
	$('#'+divId).highcharts({
	    chart: {
	        plotBackgroundColor: null,
	        plotBorderWidth: null,
	        plotShadow: false
	        
	    },
	    colors:[
	            '#7cb5ec',
	            '#ffc000',
	            '#90ed7d',
	            '#f7a35c',
	            '#9bbb59',
	            '#f15c80',
	            '#e4d354',
	            '#8085e8',
	            '#8d4653',
	            '#91e8e1',
	            '#a99bbd'
	          ],
	    title: {
	    	text: title,
	    	style:{
	    		fontWeight: 'bold', 
	    		fontSize : '10px',
	    		fontFamily:'微软雅黑',
	    		color:'#585858'
	    	}
	    },
		lang: {  
	        noData: "暂无数据"  
	    },  
	    noData: {  
	        style: {  
	            fontSize: '12px',  
	            color: '#A9A9A9'  
	        }  
	  },
	    exporting:{
			 enabled:false 
		},
	    tooltip: {
	        pointFormat: '{point.y}({point.percentage:.1f}%)'
	    },
	    legend:{
	    	enabled:false,
	    	itemStyle:{
	    		fontFamily:'微软雅黑',
	    		fontSize : '8px'//图例后面文字大小
	    	},
	    	symbolHeight:8,//修改图例大小，默认12
	    	symbolWidth:8  	
	    },
	    plotOptions: {
	        pie: {
	            allowPointSelect: true,
	            cursor: 'pointer',
	            dataLabels: {
	            	 enabled: true,
	            	 distance:10,
	            	 padding:0,
	            	 formatter: function() {  
	//                     if (this.percentage >= 0.05)  
	//                         return this.point.name + ',<br/>'+this.point.y +'('+this.percentage.toFixed(1) + ' %'+')'; //这里进行判断（看这里）  
	                    if(this.point.name.length>8){
	                    	return this.point.name.substring(0,8)+"...";
	                    }else{
	                    	return this.point.name;                   	
	                    }	
	            	 },
	                style: {
	//                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'gray',
	                	fontFamily:'微软雅黑',
	                    fontSize:'10px'//修改字体样式
	                }
	            },
	            showInLegend: true,
	            size:140
	        }
	    },
	    exporting:{
	    	buttons:{
	    		contextButton:{
	    			align:"right",
	    			height:20,
	    			//verticalAlign:"bottom"
	    		}
	    	}
	    },
	    credits: {
	        enabled: false
	    },
	    series: [{
	        type: 'pie',
	        data: list
	    }]
	});
}

function getTable(){
	$('.tlists').datagrid({
		loadMsg : '正在努力加载数据，请稍后...', //在从远程站点加载数据的时候显示提示消息。
		rownumbers : true, //如果为true，则显示一个行号列。
		multiSort : false, //定义是否允许多列排序。  配合Column的sorttable属性使用,
		striped : true,
		fitColumns : true,
		singleSelect : true,
		data : chartData.tableList,
		title : 'CIMISS访问量统计表',
		columns : [[
				{
					field : 'name',
					title : '名称',
					sortable : true,
					align : 'center'
				},{
					field : 'num1',
					title : '次数（次）',
					sortable : true,
					width:200,
					align : 'center'
				},{
					field : 'datasize1',
					title : '数据量（GB）',
					sortable : true,
					width:200,
					align : 'center'
				},{
					field : 'num2',
					title : '次数（次）',
					sortable : true,
					width:200,
					align : 'center'
				},{
					field : 'datasize2',
					title : '数据量（GB）',
					sortable : true,
					width:200,
					align : 'center'
				}

		]]
	})
}

function getBar(divId,title,flag){
	var list1;
	var list2;
	var valueSuffix;
	if(flag==0){
		list1 = chartData.numList1;
		list2 = chartData.numList2;
		valueSuffix = "次";
		$('#'+divId).highcharts({
			credits : {
				enabled : false
			// 不显示highCharts版权信息
			},
			chart: {
		        type: 'column'
		    },
			title : {
				text : title
			},
			colors : [ '#7CB5EC' ],
			plotOptions : {
				 column: {
		            borderWidth: 0
		        }
			},

			xAxis : {
				categories : chartData.namelist
			},
			yAxis: [{
		    	labels: {
		            format: '{value}次',//格式化Y轴刻度
		            style: {
		                color: '#89A54E'
		            }
		        },
		        title: {
		            text: '访问量',
		            style: {
		                color: '#89A54E'
		            }
		        },
		        min:0,
		        opposite: true
		    }],
			tooltip : {
				crosshairs : true,
				shared : true
			},
			series : [ {
				name : '次数',
				color: '#4572A7',
			    yAxis: flag,
				data : list1,
				tooltip: {
		            valueSuffix: valueSuffix
		        }
			},{
				name : '对比次数',
				color: '#89A54E',
		        yAxis: flag,
				data : list2,
				tooltip: {
		            valueSuffix: valueSuffix
		        }
			} ]
		});
	}else if(flag==1){
		list1 = chartData.datasizeList1;
		list2 = chartData.datasizeList2;
		valueSuffix = "GB";
		$('#'+divId).highcharts({
			credits : {
				enabled : false
			// 不显示highCharts版权信息
			},
			chart: {
		        type: 'column'
		    },
			title : {
				text : title
			},
			colors : [ '#7CB5EC' ],
			plotOptions : {
				 column: {
		            borderWidth: 0
		        }
			},

			xAxis : {
				categories : chartData.namelist
			},
			yAxis: [{ // Secondary yAxis
		        title: {
		            text: '数据量',
		            style: {
		                color: '#4572A7'
		            }
		        },
		        labels: {
		            format: '{value} GB',
		            style: {
		                color: '#4572A7'
		            }
		        },
		        min:0,
		        opposite: true
		    }],
			tooltip : {
				crosshairs : true,
				shared : true
			},
			series : [ {
				name : '数据量',
				color: '#4572A7',
			    yAxis: 0,
				data : list1,
				tooltip: {
		            valueSuffix: valueSuffix
		        }
			},{
				name : '对比数据量',
				color: '#89A54E',
		        yAxis: 0,
				data : list2,
				tooltip: {
		            valueSuffix: valueSuffix
		        }
			} ]
		});
	}
	
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
	var m = dd.getMonth() + 1;// 获取当前月份的日期
	var d = dd.getDate();
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

function TableOrChar(){
	if(!qiehuanFlag){
		$(".pie").css("display","none");
		$(".column").css("display","none");
		$(".tlists").css("display","block");
		$(".tableDiv").css("display","block");
		getTable();
		qiehuanFlag =true;
	}else if(qiehuanFlag){
		$(".tlists").css("display","none");
		$(".pie").css("display","block");
		$(".column").css("display","none");
		$(".tableDiv").css("display","none");
		$("input[name=flag]").removeAttr('checked');
		$("#radioReset")[ 0 ].checked = true;
		qiehuanFlag =false;
	}
}

function  download(){
	testx(JSON.stringify(chartData.tableList));
}
function  changeBar(){
	var flag = $("input[name=flag]:checked").val();
	if(flag==1){
		$(".column").css("display","none");
		$(".tlists").css("display","none");
		$(".tableDiv").css("display","none");
		$(".pie").css("display","block");
	}else if(flag==2){
		$(".pie").css("display","none");
		$(".tlists").css("display","none");
		$(".tableDiv").css("display","none");
		$(".column").css("display","block");
		getBar("flashcontent1","cimiss访问量统计",0);
		getBar("flashcontent2","cimiss数据量统计",1);
	}
}
function AddRunningDiv() {
    $("<div class=\"datagrid-mask\"></div>").css({ display: "block", width: "100%", height: $(document).height() }).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候...").appendTo("body").css({ display: "block", left: ($(document.body).outerWidth(true) - 190) / 2, top: ($(document).height() - 280)/2});
} 
function MoveRunningDiv() {
    $("div[class='datagrid-mask']").remove();
    $("div[class='datagrid-mask-msg']").remove();
}  
function changeTime(id){
	if(id==1){
		$("#1").attr("style","display:block");
	}else{
		$("#1").attr("style","display:none");
	}
}