<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>导出excel</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.success{
			color:green;
		}
		.error{
		color:red;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#cleanButton").click(function(){
				var url="${ctx}/exportexcel/excelDownload";
				openDownloadDialog(url);
				/*$.ajax({                          
		               type: "post",    
		               url: '${ctx}/exportexcel/excelDownload',          
		               cache: false,             
		               async: false,                      
		               success: function (data) {
		              $("#remind").html("excel已导出").addClass("success"); 
		              //alert(data);
		               }
		              
		        });*/
				
			});
			
		});
		/**
		 * 通用的打开下载对话框方法，没有测试过具体兼容性
		 * @param url 下载地址，也可以是一个blob对象，必选
		 * @param saveName 保存文件名，可选
		 */
		function openDownloadDialog(url)
		{
		 if(typeof url == 'object' && url instanceof Blob)
		 {
		  url = URL.createObjectURL(url); // 创建blob地址
		 }
		 var aLink = document.createElement('a');
		 aLink.href = url;
		 //aLink.download = saveName || ''; // HTML5新增的属性，指定保存文件名，可以不要后缀，注意，file:///模式下不会生效
		 var event;
		 if(window.MouseEvent) event = new MouseEvent('click');
		 else
		 {
		  event = document.createEvent('MouseEvents');
		  event.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
		 }
		 aLink.dispatchEvent(event);
		}
	</script>
		
</head>
<body>
	<div>
		<div class="form-actions">
			<input id="cleanButton" class="btn btn-primary" type="button" value="导出excel"></input>&nbsp;<span id="remind"></span>		 
		</div>
		
	
	</div>
</body>
</html>