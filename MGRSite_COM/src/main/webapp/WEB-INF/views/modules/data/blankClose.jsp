<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
<title>编辑数据类型</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript">
 var status="${status}";
 if(status=="0"){
	 alert("保存成功！");
	 parent.closeModal2();
 }else if(status=="1"){
	 alert("保存失败！");
	 parent.closeModal2();
 }else if(status=="2"){
	 alert("保存成功！");
	 parent.closeModal();
 }else if(status=="3"){
	 alert("保存失败！");
	 parent.closeModal();
 }
</script>
</head>

<body>
</body>
</html>

