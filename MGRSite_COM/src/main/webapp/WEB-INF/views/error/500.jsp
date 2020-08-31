<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="com.thinkgem.jeesite.common.beanvalidator.BeanValidators"%>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%response.setStatus(200);%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>500 - 系统内部错误</title>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
	<style type="text/css">
		#background{
			position: absolute;
  			top: 50%;
  			left: 50%;
			width:1200px;
			height:500px;
			background-image:url("${ctxStatic}/images/500.jpg");
			margin:-250px 0 0 -600px;
		}
		#backtoindex{
			margin-top:245px;
			margin-left:484px;
			width:100px;
			height:30px;
			float:left;
		}
		#backtoindex:hover{
			cursor:pointer;
		}
		#historyback{
			margin-top:245px;
			margin-left:28px;
			width:100px;
			height:30px;
			float:left;
		}
		#historyback:hover{
			cursor:pointer;
		}
	</style>
</head>
<body>
	<div id="background">
			<div id="backtoindex"  onClick="window.location.href='${ctx}'">
			</div>
			<div id="historyback" onclick="history.go(-1);">
			</div>
	</div>
</body>
</html>