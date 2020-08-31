<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>算法池管理</title>
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
            var tu = $("#params").val();
            $("#params").blur(function () {
                var str = $("#params").val().replace(/(^\s*)|(\s*$)/g, '');//去除空格;
                if (str == '' || str == undefined || str == null) {
                    $("#params").val(tu);
                }
            })
        });
        function add(){
            var dateValue = $("#params").val();
            var id = $("#id").val();
            var purposeValue = $("#purposeValue").val();
            $.ajax({
                type: "post",
                url: "${ctx}/stream/supArithmeticsStream/tests",
                data: {
                    url: dateValue,
                    id: id,
                    purposeValue:purposeValue
                },
                success: function (res) {
                    var mycars=new Array()
                    console.log(res.DS)
                    for(var i=0;i<res.DS.length;i++){
                        var obj =res.DS[i];
                        mycars[i] = JSON.stringify(obj);
                    }
                    $("#pridictValue").html(mycars);
                }
            });
        }
        function clearValue(){
            $("#params").val("")
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/stream/supArithmeticsStream/">算法池列表</a></li>
    <li class="active"><a
            href="${ctx}/stream/supArithmeticsStream/form?id=${supArithmeticsStream.id}">算法池${not empty supArithmeticsStream.id?'测试':''}</a>
    </li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="supArithmeticsStream" action="${ctx}/stream/supArithmeticsStream/testss" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <tags:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">API接口：</label>
        <div class="controls">
            <form:input id="params" path="purpose" onclick="clearValue()" htmlEscape="false" maxlength="500" style="width:80%;" class="input-xlarge required"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">算法参数：</label>
        <div class="controls">
            <form:input id="purposeValue" path="params"  htmlEscape="false" maxlength="100" style="width:80%;" class="input-xlarge required"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">预期值：</label>
        <div class="controls">
            <textarea id="pridictValue"  rows="10" style="width:80%;"></textarea>
        </div>
    </div>
    <div class="form-actions">
        <input type="button" onclick="add()" class="btn btn-primary" value="查看数据">&nbsp;
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="通过测试"/>&nbsp;
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>