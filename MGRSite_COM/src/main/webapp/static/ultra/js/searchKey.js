var pathName=window.document.location.pathname;
var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
var root=window.location.href;

$(function(){
	var orderBy = $("#orderBy").val().split(" ");
	$("#tablelist th.sort").each(function(){
		if ($(this).hasClass(orderBy[0])){
			orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="DESC"?"down":"up";
			$(this).html($(this).html()+" <i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
		}
	});
	$("#tablelist th.sort").click(function(){
		var order = $(this).attr("class").split(" ");
		var sort = $("#orderBy").val().split(" ");
		for(var i=0; i<order.length; i++){
			if (order[i] == "sort"){
				order = order[i+1]; 
				break;
			}
		}
		if (order == sort[0]){//已有排序的点到反序
			sort = (sort[1]&&sort[1].toUpperCase()=="DESC"?"ASC":"DESC");
			//$("#orderBy").val(order+" DESC"!=order+" "+sort?"":order+" "+sort);
			$("#orderBy").val(order+" "+sort);
		}else{//第一次点到排序都按降序
			$("#orderBy").val(order+" DESC");
		}
		page();
	});

});

function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	var isActive = $("input[type='radio'][name='status']:checked").val();
	var userType = $("#selects option:selected").val();
	var searchName=$("#searchName").val();
	var userRankID=$("#userRankID option:selected").val();
	if((isActive==null||isActive==undefined)||isActive==""){
		isActive="";
	}
	$("#form1").attr(
			"action",
			projectName+"/searchKey/list");
	$("#form1").submit();		
	return false;
}