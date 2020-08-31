$(function(){
	var forms=$(".single");
	if(forms.length<10){
		$(".addBox").show();
	}else{
		$(".addBox").hide();
	}
})
function addDataDef(){
	var forms=$(".single");
	if(forms.length<10){
		var str="<form id=\"form"+forms.length+"\" class=\"single\"><label class=\"orderNo\" style=\"margin-right:4px;\">"+(forms.length+1)+"</label><input type=\"text\" id=\"FTPNames\" name=\"FTPNames\" style=\"margin-right:4px;\">" 
//				+"<input type=\"text\" id=\"FTPURLs\" name=\"FTPURLs\" style=\"margin-right:4px;\">" 
				+"<input type=\"text\" id=\"FTPURL\" name=\"FTPURL\" ><input type=\"button\" value=\"清空\" onclick=\"delInputBox('form"+forms.length+"')\" style=\"margin-top: -7px;margin-left:5px;\"></form>";
		var content=$("#ftps").html();
		content+=str;
		$("#ftps").empty();
		$("#ftps").html(content);
		if((forms.length+1)==10){
			$(".addBox").hide();			
		}else{
			$(".addBox").show();
		}
	}else{
		$(".addBox").hide();
	}
}
function saveInfo(){
	var flag=false;
	var invalid=$("#invalid").val();
		var str="{\"list\":[";
		$("#ftps form").each(function(i){
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
		str=str.substring(0,m) + str.substring(m+1,str.length);
/*		alert(str);
		if(str.length>420){*/
			$.ajax({
				url:"../dataFtpDef/save",  
				data:{
					"dataCode":dataCode,
					"pid":pid,
					"categoryId":categoryId,
					"pageType":pageType,
					"jsonStr":str,
					"invalid":invalid
					
				},  
				dataType:"json",  
				cache:false ,
				success:function(result){
					alert(result.status);
					window.location.href=ctx+"/dataFtpDef/dataFtpList?dataCode="+result.dataCode+"&pid="+pid+"&categoryid="+categoryId
				}			 
			});		
			flag=true;
		/*}else{
			alert("信息不能为空，请填写");
			flag=false;
		}	*/	
	return flag;
}
function delInputBox(formId){
	var inpus=$("#"+formId).serializeArray();
	for(var item in inpus){
		$("#"+formId+" #"+inpus[item].name).attr("value","");		
	}
}