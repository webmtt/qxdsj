var pathName=window.document.location.pathname;
var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
var root=window.location.href;

function closemodel(){
	$('#myModal').modal('hide');
}
function selectById(id){
	$("#contentTable1  tr:not(:first)").empty("");
	$.ajax({
		url:projectName+'/sys/role/ubri',
		type:'POST',
		data:{'id':id},
		dataType:"json",
		success:function(data){
			$.each(data,function(key,val){
				   $("#contentTable1").append("<tr><td>"+val.loginName+"</td><td>"+val.name+"</td>" +
				   		"<td><input type='button' value='删除' id='"+val.id+"' /></td></tr>");
						   $("#"+val.id+" ").click(function(){ 
							   var userid=val.id;
							   var boo=confirm("确认要删除该角色与此用户的关联关系吗？");
									if(boo){
										$.ajax({
											url:projectName+'/sys/role/deleteUI',
											type:'POST',
											data:{'userid':userid,"roleid":id},
											dataType:"json",
											success:function(data){
												$.each(data,function(key,val){
													var str=val.word
													   if(str=="删除成功"){
														   selectById(id);
														   alert(str);
													   }else{
														   alert(str);
													   }
													
												   });
												
											}
										})
								
									}
							});	   
			});
			
			
			
		}
	});
	
}