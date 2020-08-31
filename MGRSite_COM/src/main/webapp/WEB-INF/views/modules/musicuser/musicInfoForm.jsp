<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>用户管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });

        function check() {
            if (${not empty musicInfo.id?false:true}) {
                checkNameOrOrg();
            }
            var org = $("#orgid").val();
            if ("000" == org) {
                alert("请选择工作单位！");
                return false;
            }
            return true;
        }

        function checkNameOrOrg() {
            var name = $("#name").val();
            var orgid = "";
            if (${not empty musicInfo.id?false:true}) {//修改时不验证账号
                $.ajax({
                    url: '${ctx}/musicuser/musicInfo/checkUserName',
                    type: 'post',
                    data: {'name': name, 'orgid': orgid},
                    asyn: 'false;',
                    dataType: 'text',
                    success: function (result) {
                        if (result == "true") {
                            alert("用户名已存在！");
                            return;
                        }
                    }
                });
            }
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/musicuser/musicInfo/list">用户列表</a></li>
    <li class="active"><a
            href="${ctx}/musicuser/musicInfo/form?id=${musicInfo.id}">用户${not empty musicInfo.id?'修改':'添加'}</a></li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="musicInfo" action="${ctx}/musicuser/musicInfo/save" method="post"
           class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">music账号：</label>
        <div class="controls">
            <form:input path="name" htmlEscape="false" maxlength="50" class="input-xlarge required"
                        onBlur="checkNameOrOrg()"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">密码：</label>
        <div class="controls">
            <form:input path="password" htmlEscape="false" maxlength="50" type="password"
                        class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">工作单位：</label>
        <div class="controls">
            <form:select path="orgid" class="input-xlarge required">
                <form:option value="000" label="请选择"/>
                <form:options items="${oneList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
            </form:select>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">备注信息：</label>
        <div class="controls">
            <form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="50" class="input-xxlarge "/>
        </div>
    </div>
    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="return check()"/>&nbsp;
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>