function openPostWindow(url, type,Strname, name)  
      {  
       var tempForm = document.createElement("form");  
       tempForm.id="tempForm1";  
       tempForm.method="post";  
       tempForm.action=url;  
       tempForm.target=name;  
       var typeInp = document.createElement("input");  
       typeInp.type="hidden";  
       typeInp.name= "type"
    	   typeInp.value= type;
      var strnameInp = document.createElement("input");  
      strnameInp.type="hidden";  
      strnameInp.name= "Strname"
    	  strnameInp.value= Strname;
      
      tempForm.appendChild(typeInp);   
      tempForm.appendChild(strnameInp);   
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

$(function(){
	$("#expExcel").click(function(){
		var Strname = $("input[name='Strname']").val();
		var url = "serlog_exporExcel.action";
		openPostWindow(url, "1",Strname, "new") ;
	});
	
	$("#expExcelTemplate").click(function(){
		var Strname = $("input[name='Strname']").val();
		var url = "serlog_exporExcel.action";
		openPostWindow(url, "0",Strname, "new") ;
	});
	
	$("#impExcel2").click(function(){
//		$("#uploadForm").form("",{
//			success:function(result){
//				var result=eval('('+result+')');
//				if(result.errorMsg){
//					//$.messager.alert("系统提示",result.errorMsg);
//				}else{
//					a
		
//					
//				}
//			}
//		});
		$("#uploadForm").submit();
//		$("#winImg").trigger("click");
//		window.parent.location.href="serlog_test.action";
	});
		
});