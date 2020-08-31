<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>
<html>
<head>
<title>添加资料</title>
<meta name="decorator" content="default" />
<link href="${ctxStatic}/ultra/css/defense.css" rel="stylesheet"/>
<script src="${ctxStatic}/ultra/js/dataFtpDef.js?version=3" type="text/javascript"></script>
<style type="text/css">
.title{
/* width: 1055px; */
width: 815px;
}
#FTPNames{
width: 222px;
}
#FTPURLs{
width: 218px;
}
#FTPURL{
width: 470px;
}

.single{
/* width: 1055px; */
width:815px;
margin-left: 10px;
}
.addBox{
 background-color: #f5f5f5;
 height: 40px;
 margin-left: 10px;
 padding-top: 10px;
 text-align: center;
 /* width: 1055px; */
 width: 815px;
}
.orderNo{
width: 34px;text-align: center;
}
.invalidBox{
	display: table-cell;
    font-size: 16px;
    height: 30px;
    margin-left: 10px;
    padding-left: 42%;
    padding-top: 10px;
    vertical-align: middle;
  /*   width: 1055px; */
    width: 815px;
}
</style>
<script type="text/javascript">
var dataCode='${dataCode}';
var pid='${pid}';
var categoryId='${categoryId}';
var pageType='${pageType}';
var ctx = "${ctx}";
var ctxStatic = "${ctxStatic}";
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li><a href="${ctx}/dataFtpDef/dataFtpList?dataCode=${dataCode}&categoryid=${categoryId}&pid=${pid}">${categoryName}FTP下载列表</a></li>
		<li class="active"><a href="javascript:void(0)">编辑FTP信息</a></li> 	
	</ul>
	<form:form id="titleBox" method="post" class="breadcrumb form-search">
		<span style="font-weight: bold;">当前大类：</span>${pidName }--${categoryName}--${dataDefName} 
	</form:form>
	<tags:message content="${message}"/>
	<input type="hidden" id="invalid" name="invalid" value="${invalid}">
	<input type="hidden" id="categoryId" name="categoryId" value="${categoryId}">
	<input type="hidden" id="jsonStr" name="jsonStr" value="${jsonStr}">
	<input type="hidden" id="pid" name="pid" value="${pid}">
	<div class="title">
		<table class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
			<thead>
				<th style="width: 10px;">序号</th>
				<th style="width: 225px;text-align: center;border-radius: 0px;">FTP名称组</th>
				<!-- <th style="width: 225px;text-align: center;">FTP链接组</th> -->
				<th style="text-align: center;width:480px;">FTP链接</th>
				<th style="text-align: center;">操作</th>
			</thead>
		</table>
	</div>
	<div id="ftps">
		<!-- <form id="form1" class="single">
			<input type="text" id="FTPNames" name="FTPNames" value="1100" readonly="readonly"> 
			<input type="text" id="FTPURLs" name="FTPURLs" value="北京" readonly="readonly"> 
		    <input type="text" id="FTPURL" name="FTPURL" value="20"> 
		</form> -->
		<c:choose>
			<c:when test="${not empty dataFtpList}">
				<c:forEach items="${dataFtpList}" var="dataFtp" varStatus="no">
				<form id="form${no.index}" class="single">
					<label class="orderNo">${no.index+1}</label>
					<input type="text" id="FTPNames" name="FTPNames" value="${dataFtp.ftpName }"> 
					<%-- <input type="text" id="FTPURLs" name="FTPURLs" value="${dataFtp.ftpUrls }">  --%>
			    	<input type="text" id="FTPURL" name="FTPURL" value="${dataFtp.ftpUtl}"> 
			    	<input type="button" value="清空" onclick="delInputBox('form${no.index}')" style="margin-top: -7px;">
				</form>
			</c:forEach>	
			</c:when>
			<c:otherwise>
				<form id="form1" class="single">
					<label class="orderNo">1</label>
					<input type="text" id="FTPNames" name="FTPNames" value=""> 
					<!-- <input type="text" id="FTPURLs" name="FTPURLs" value="">  -->
			    	<input type="text" id="FTPURL" name="FTPURL" value=""> 
			    	<input type="button" value="清空" onclick="delInputBox('form1')" style="margin-top: -7px;">
				</form>
				<form id="form2" class="single">
					<label class="orderNo">2</label>
					<input type="text" id="FTPNames" name="FTPNames" value=""> 
					<!-- <input type="text" id="FTPURLs" name="FTPURLs" value="">  -->
			    	<input type="text" id="FTPURL" name="FTPURL" value=""> 
			    	<input type="button" value="清空" onclick="delInputBox('form2')" style="margin-top: -7px;">
				</form>
				<form id="form3" class="single">
					<label class="orderNo">3</label>
					<input type="text" id="FTPNames" name="FTPNames" value=""> 
					<!-- <input type="text" id="FTPURLs" name="FTPURLs" value="">  -->
			    	<input type="text" id="FTPURL" name="FTPURL" value=""> 
			    		<input type="button" value="清空" onclick="delInputBox('form3')" style="margin-top: -7px;">
				</form>
				<form id="form4" class="single">
					<label class="orderNo">4</label>
					<input type="text" id="FTPNames" name="FTPNames" value=""> 
					<!-- <input type="text" id="FTPURLs" name="FTPURLs" value="">  -->
			    	<input type="text" id="FTPURL" name="FTPURL" value=""> 
			    		<input type="button" value="清空" onclick="delInputBox('form4')" style="margin-top: -7px;">
				</form>
				<form id="form5" class="single">
					<label class="orderNo">5</label>
					<input type="text" id="FTPNames" name="FTPNames" value=""> 
					<!-- <input type="text" id="FTPURLs" name="FTPURLs" value="">  -->
			    	<input type="text" id="FTPURL" name="FTPURL" value=""> 
			    		<input type="button" value="清空" onclick="delInputBox('form5')" style="margin-top: -7px;">
				</form>
				<form id="form6" class="single">
					<label class="orderNo">6</label>
					<input type="text" id="FTPNames" name="FTPNames" value=""> 
					<!-- <input type="text" id="FTPURLs" name="FTPURLs" value="">  -->
			    	<input type="text" id="FTPURL" name="FTPURL" value=""> 
			    	<input type="button" value="清空" onclick="delInputBox('form6')" style="margin-top: -7px;">
				</form>
				<form id="form7" class="single">
					<label class="orderNo">7</label>
					<input type="text" id="FTPNames" name="FTPNames" value=""> 
					<!-- <input type="text" id="FTPURLs" name="FTPURLs" value="">  -->
			    	<input type="text" id="FTPURL" name="FTPURL" value=""> 
			    	<input type="button" value="清空" onclick="delInputBox('form7')" style="margin-top: -7px;">
				</form>
				<form id="form8" class="single">
					<label class="orderNo">8</label>
					<input type="text" id="FTPNames" name="FTPNames" value=""> 
					<!-- <input type="text" id="FTPURLs" name="FTPURLs" value="">  -->
			    	<input type="text" id="FTPURL" name="FTPURL" value=""> 
			    	<input type="button" value="清空" onclick="delInputBox('form8')" style="margin-top: -7px;">
				</form>
				<form id="form9" class="single">
					<label class="orderNo">9</label>
					<input type="text" id="FTPNames" name="FTPNames" value=""> 
					<!-- <input type="text" id="FTPURLs" name="FTPURLs" value="">  -->
			    	<input type="text" id="FTPURL" name="FTPURL" value="">
			    	<input type="button" value="清空" onclick="delInputBox('form9')" style="margin-top: -7px;"> 
				</form>
				<form id="form10" class="single">
					<label class="orderNo">10</label>
					<input type="text" id="FTPNames" name="FTPNames" value=""> 
					<!-- <input type="text" id="FTPURLs" name="FTPURLs" value=""> --> 
			    	<input type="text" id="FTPURL" name="FTPURL" value=""> 
			    	<input type="button" value="清空" onclick="delInputBox('form10')" style="margin-top: -7px;">
				</form>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="addBox">
		<input id="add" class="btn btn-primary" type="button" value="添加" onclick="addDataDef()"/>
	</div>
	<div class="form-actions" style="padding-left: 31%;">
		<input id="save" class="btn btn-primary" type="button" value="保 存" onclick="return saveInfo()"/>&nbsp;
		<input id="btnCancel" class="btn" type="button" value="返 回"
			onclick="history.go(-1)" />
	</div>
</body>
</html>

