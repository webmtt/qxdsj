var pathName=window.document.location.pathname;
var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
var root=window.location.href;
function check(){
	alert("1");
	var totalValue=$("#total").val();
	var editValue=$("#edit").val();
	var riskValue=$("#risk").val();
	var time=$("#time").val();
	if(time==null||time==undefined||time==""){
		alert("日期不能为空");
		return false;
	}
	else if(totalValue==null||totalValue==undefined||totalValue==""){
		alert("整体风险数值不能为空");
		return false;
	}
	else if(editValue==null||editValue==undefined||editValue==""){
		alert("配置风险数值不能为空");
		return false;
	}
	else if(riskValue==null||riskValue==undefined||riskValue==""){
		alert("漏洞风险数值不能为空");
		return false;
	}else{
		return true;
	}
}
function checkData(){
	var time=$("#time").val();
	var value=$("#value").val();
	if(time==null||time==undefined||time==""){
		alert("日期不能为空");
		return false;
	}else if(value==null||value==undefined||value==""){
		alert("数值不能为空");
	}else{
		return true;
	}
}