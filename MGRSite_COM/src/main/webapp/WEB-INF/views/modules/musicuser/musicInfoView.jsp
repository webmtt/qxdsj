<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/dialog.jsp" %>
<html>
<head>
    <title>用户管理</title>
    <meta name="decorator" content="default"/>
</head>
<body>

<br/>
<form:form id="inputForm" modelAttribute="musicInfo" method="post"
           class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">music账号：</label>
        <div class="controls">
            <form:input path="name" htmlEscape="false" maxlength="50" class="input-xlarge required"
                        readonly="true"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">工作单位：</label>
        <div class="controls">
            <input value="${orgname}" readonly="true"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">备注信息：</label>
        <div class="controls">
            <form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="50" class="input-xxlarge "
                           style="width: 270px;height: 73px;resize:none" readonly="true"/>
        </div>
    </div>
</form:form>
</body>
</html>