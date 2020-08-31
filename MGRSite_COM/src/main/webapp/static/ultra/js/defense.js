var pathName=window.document.location.pathname;
var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
var root=window.location.href;
$(function(){
	$("#save").on("click", function () {    
		var time=$("#time").val();
		if(time==null||time==""||time==undefined){
			alert("请选择时间");
			return false;
		}
		var str="{\"list\":[";
		$("#units form").each(function(i){
			var form=$(this).serializeArray();
			if(typeof form === "object" && !(form.rows instanceof Array)){				
				var k="{";
				for(var item in form){
						if(item<form.length-1){
							k+="\""+form[item].name+"\":\""+form[item].value+"\",";
						}else{
							k+="\""+form[item].name+"\":\""+form[item].value+"\"";
						}						
				}
				k+="},";
				str+=k;
			}
		});
		str+="]}";
		var m = str.lastIndexOf(',');
		var type=$("#type").val();
		str=str.substring(0,m) + str.substring(m+1,str.length);
		if(str.length>440){
			$.ajax({
				url:projectName+'/netsec/defense/saveList',  
				data:{
					'time':time,
					'type':type,
					'jsonStr':str
					
				},  
				dataType:'html',  
				cache:false ,
				success:function(result){			
						$("body").empty();
						$("body").html(result);			
				}			 
			});			
		}else{
			alert("信息不能为空");
			return false;
		}
    });  
})