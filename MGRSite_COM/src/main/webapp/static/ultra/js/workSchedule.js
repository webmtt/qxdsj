var pathName=window.document.location.pathname;
var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
var root=window.location.href;

function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	var type=$("#type").val();
	var dateTime=$("#dateTime").val();
		$("#form1").attr(
				"action",
				projectName+"/workSchedule/index?type="+type+"&dateTime=" + dateTime);
		$("#form1").submit();		
	return false;
}
