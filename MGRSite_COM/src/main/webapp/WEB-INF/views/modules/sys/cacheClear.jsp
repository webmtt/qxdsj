<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>缓存清除</title>
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
			$.ajax({                          
		               type: "post",    
		               url: '${ctx}/sys/cleansuccess',          
		               cache: false,             
		               async: false,                      
		               success: function (data) {
		              $("#remind").html("缓存清理成功").addClass("success"); 
		              //alert(data);
		               }
		              
		            });
			});
		});
	</script>
		
</head>
<body>
	<div>
		<div class="form-actions">
			<input id="cleanButton" class="btn btn-primary" type="button" value="清除缓存"></input>&nbsp;<span id="remind"></span>		 
		</div>
		
	
	</div>
</body>
</html>