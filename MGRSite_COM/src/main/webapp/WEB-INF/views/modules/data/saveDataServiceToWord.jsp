<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>快报生成</title>
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
				 $("#remind").html("正在生产快报，请耐心等待！").addClass("success"); 
				 window.open("${ctx}/ServiceExpress/saveWord");
			});
		});
	</script>
		
</head>
<body>
	<div>
		<div class="form-actions">
			<input id="cleanButton" class="btn btn-primary" type="button" value="快报生成"></input>&nbsp;<span id="remind"></span>		 
		</div>
		
	
	</div>
</body>
</html>