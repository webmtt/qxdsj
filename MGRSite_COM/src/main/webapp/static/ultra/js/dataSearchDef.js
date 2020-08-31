//检索定制分项的修改功能-要素
function editDataSearch(datacode,categoryid,pid){
	$("#iframeModel2").attr("src",ctx+"/dataSearchDef/editDataSearch?datacode="+datacode+"&categoryid="+categoryid+"&pid="+pid);
	$('#myModalLabel2').html("修改检索定制分项");
	$('#myModal2').show();
	$('#myModal2').modal('toggle');
}
//检索定制分项的修改功能-文件
function editDataSearch2(datacode,categoryid,pid){
	$("#iframeModel2").attr("src",ctx+"/dataSearchDef/editDataSearch2?datacode="+datacode+"&categoryid="+categoryid+"&pid="+pid);
	$('#myModalLabel2').html("修改检索定制分项");
	$('#myModal2').show();
    $('#myModal2').modal('toggle');
}


