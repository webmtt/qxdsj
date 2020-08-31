var date = new Date();
var month = date.getMonth()+1;
var year = date .getFullYear();
var betimes = year+"-"+month+"-01 00:00:00 , "+year+"-"+month+"-30 00:00:00";
$(document).ready(function () {
    // layui.use('laydate', function() {
    //     var laydate = layui.laydate;
    //     $("#betimes").attr("value",betimes);
    //     addTimes(betimes);
    //     //日期时间选择器
    //     laydate.render({
    //         elem: '#betimes'
    //         ,type: 'datetime'
    //         ,range: ','
    //         ,format: 'yyyy-MM-dd HH:mm:ss'
    //         ,done: function(value){
    //             addTimes(value);
    //         }
    //     });
    // });
    // function addTimes(batter){
    //     $.ajax({
    //         type: 'POST',
    //         async: false,
    //         url: 'main/getPhysics.shtml',
    //         data: {
    //             'standbe':batter
    //         },
    //         success: function (data) {
    //             console.log(data);
    //             var bodyChart = echarts.init(document.getElementById("bodyLines"));
    //             var bodyoption = {
    //                 title : {
    //                     text: '云上北疆-获取/推送-库表 数量统计'
    //                 },
    //                 tooltip : {
    //                     trigger: 'axis'
    //                 },
    //                 legend: {
    //                     data:['获取','推送']
    //                 },
    //                 xAxis : [
    //                     {
    //                         type : 'category',
    //                         boundaryGap : false,
    //                         data : data.data.baseMap.changetime
    //                     }
    //                 ],
    //                 yAxis : [
    //                     {
    //                         type : 'value',
    //                         axisLabel : {
    //                             formatter: '{value}'
    //                         }
    //                     }
    //                 ],
    //                 series : [
    //                     {
    //                         name:"获取",
    //                         type:'line',
    //                         data:data.data.baseMap.synchronize
    //                     },
    //                     {
    //                         name:"推送",
    //                         type:'line',
    //                         data:data.data.baseMap.change
    //                     }
    //                 ]
    //             };
    //             bodyChart.setOption(bodyoption);
    //             $(window).resize(bodyChart.resize);
    //             var FileChart = echarts.init(document.getElementById("fileLines"));
    //             var fileoption = {
    //                 title : {
    //                     text: '云上北疆-获取/推送-文件 数量统计'
    //                 },
    //                 tooltip : {
    //                     trigger: 'axis'
    //                 },
    //                 legend: {
    //                     data:['获取','推送']
    //                 },
    //                 xAxis : [
    //                     {
    //                         type : 'category',
    //                         boundaryGap : false,
    //                         data : data.data.fileMap.changetime
    //                     }
    //                 ],
    //                 yAxis : [
    //                     {
    //                         type : 'value',
    //                         axisLabel : {
    //                             formatter: '{value}'
    //                         }
    //                     }
    //                 ],
    //                 series : [
    //                     {
    //                         name:'获取',
    //                         type:'line',
    //                         data:data.data.fileMap.synchronize
    //                     },
    //                     {
    //                         name:'推送',
    //                         type:'line',
    //                         data:data.data.fileMap.change
    //                     }
    //                 ]
    //             };
    //             FileChart.setOption(fileoption);
    //             $(window).resize(FileChart.resize);
    //             var apiChart = echarts.init(document.getElementById("apiLines"));
    //             var apioption = {
    //                 title : {
    //                     text: '云上北疆-获取/推送-接口 数量统计'
    //                 },
    //                 tooltip : {
    //                     trigger: 'axis'
    //                 },
    //                 legend: {
    //                     data:['获取','推送']
    //                 },
    //                 xAxis : [
    //                     {
    //                         type : 'category',
    //                         boundaryGap : false,
    //                         data : data.data.apiMap.changetime
    //                     }
    //                 ],
    //                 yAxis : [
    //                     {
    //                         type : 'value',
    //                         axisLabel : {
    //                             formatter: '{value}'
    //                         }
    //                     }
    //                 ],
    //                 series : [
    //                     {
    //                         name:'获取',
    //                         type:'line',
    //                         data:data.data.apiMap.synchronize
    //                     },
    //                     {
    //                         name:'推送',
    //                         type:'line',
    //                         data:data.data.apiMap.change
    //                     }
    //                 ]
    //             };
    //             apiChart.setOption(apioption);
    //             $(window).resize(apiChart.resize);
    //         },
    //         error: function () {
    //             window.location.href="view/loginUI.shtml";
    //         },
    //         dataType: 'json'
    //     });
    // }
	/*判断是否为管理员*/
	$.ajax({
        type: 'POST',
        async: false,
        url: 'index/isAdmin.shtml',
        data: {},
        success: function (data) {
        	if (!data){
        		$("#isAdmin").hide();
        	}
        },
        error: function () {
        	window.location.href="view/loginUI.shtml";
        },
        dataType: 'json'
    });
	/*获取全部在监控的任务*/
	$.ajax({
        type: 'POST',
        async: false,
        url: 'main/allRuning.shtml',
        data: {},
        success: function (data) {
        	$("#allNum").text(data);
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
	/*获取在监控的转换数*/
	$.ajax({
        type: 'POST',
        async: false,
        url: 'trans/monitor/getAllMonitorTrans.shtml',
        data: {},
        success: function (data) {
        	$("#transNum").text(data);
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });	
	/*获取在监控的作业数*/
	$.ajax({
        type: 'POST',
        async: false,
        url: 'job/monitor/getAllMonitorJob.shtml',
        data: {},
        success: function (data) {
        	$("#jobNum").text(data);
        },
        error: function () {
            alert("请求失败！请刷新页面重试");
        },
        dataType: 'json'
    });
	$.ajax({
        type: 'POST',
        async: false,
        url: 'main/getKettleLine.shtml',
        data: {},
        success: function (data) {
        	 console.log(data.data.trans);
            var lineChart = echarts.init(document.getElementById("kettleLine"));
    	    var lineoption = {
    	        title : {
    	            text: '7天内作业和转换的监控状况'
    	        },
    	        tooltip : {
    	            trigger: 'axis'
    	        },
    	        legend: {
    	            data:['作业','转换']
    	        },
    	        xAxis : [
    	            {
    	                type : 'category',
    	                boundaryGap : false,
    	                data : data.data.legend
    	            }
    	        ],
    	        yAxis : [
    	            {
    	                type : 'value',
    	                axisLabel : {
    	                    formatter: '{value}'
    	                }
    	            }
    	        ],
    	        series : [
    	            {
    	                name:data.data.trans.name,
    	                type:'line',
    	                data:data.data.trans.data,
    	                markPoint : {
    	                    data : [
    	                        {type : 'max', name: '最大值'},
    	                        {type : 'min', name: '最小值'}
    	                    ]
    	                },
    	            },
    	            {
    	                name:data.data.job.name,
    	                type:'line',
    	                data:data.data.job.data,
    	                markPoint : {
    	                    data : [
    	                    	{type : 'max', name: '最大值'},
    	                        {type : 'min', name: '最小值'}
    	                    ]
    	                },
    	            }
    	        ]
    	    };
    	    lineChart.setOption(lineoption);
    	    $(window).resize(lineChart.resize);
        },
        error: function () {
        	window.location.href="view/loginUI.shtml";
        },
        dataType: 'json'
    });
});

function MonitorTransFormatter(value, row, index){
	var MonitorTrans = "";
	$.ajax({
        type: 'POST',
        async: false,
        url: 'trans/getTrans.shtml',
        data: {
            "transId": value          
        },
        success: function (data) {
        	var Trans = data.data;
        	MonitorTrans = Trans.transName;		        	 				        	 
        },
        error: function () {
            alert("系统出现问题，请联系管理员");
        },
        dataType: 'json'
    });
	return MonitorTrans;
}; 
function MonitorJobFormatter(value, row, index){
	var MonitorJob = "";
	$.ajax({
        type: 'POST',
        async: false,
        url: 'job/getJob.shtml',
        data: {
            "jobId": value          
        },
        success: function (data) {
        	var job = data.data;
        	MonitorJob = job.jobName;		        	 				        	 
        },
        error: function () {
            alert("系统出现问题，请联系管理员");
        },
        dataType: 'json'
    });
	return MonitorJob;
}; 

function queryParams(params) {
	var temp = { limit: 10, offset: params.offset };
    return temp;
};

function searchTrans(){
	$('#transMonitorList').bootstrapTable('refresh', "main/getTransList.shtml");
}

function searchJobs(){
	$('#jobMonitorList').bootstrapTable('refresh', "main/getJobList.shtml");
}