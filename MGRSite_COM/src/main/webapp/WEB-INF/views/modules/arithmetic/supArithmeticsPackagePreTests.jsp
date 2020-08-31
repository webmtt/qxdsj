<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>算法管理</title>
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
            var tu = $("#purposeValue").val();
            $("#purposeValue").blur(function () {
                var str = $("#purposeValue").val().replace(/(^\s*)|(\s*$)/g, '');//去除空格;
                if (str == '' || str == undefined || str == null) {
                    $("#purposeValue").val(tu);
                }
            })
        });
        function add(){
            var dateValue = $("#params").val();
            var id = $("#id").val();
            var purposeValue = $("#purposeValue").val();
            $.ajax({
                type: "post",
                url: "${ctx}/arithmetic/supArithmeticsPackage/tests",
                data: {
                    url: dateValue,
                    id: id,
                    purposeValue:purposeValue
                },
                success: function (res) {
                    var mycars=new Array()
                    console.log(res)
                    for(var i=0;i<res.DS.length;i++){
                        var obj = res.DS[i];
                        mycars[i] = JSON.stringify(obj);
                    }
                    $("#pridictValue").html(mycars);
                }
            });
        }
        function clearValue(){
            $("#purposeValue").val("")
        }

    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/arithmetic/supArithmeticsPackage/">算法列表</a></li>
    <li class="active"><a
            href="${ctx}/arithmetic/supArithmeticsPackage/form?id=${supArithmeticsPackage.id}">算法${not empty supArithmeticsPackage.id?'预测试':'null'}
        </a></li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="supArithmeticsPackage" action="${ctx}/arithmetic/supArithmeticsPackage/testss" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <tags:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">API接口：</label>
        <div class="controls">
            <form:input id="params" path="params" htmlEscape="false" maxlength="500" style="width:80%;" class="input-xlarge required"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">算法参数：</label>
        <div class="controls">
            <form:input id="purposeValue" path="purpose" onclick="clearValue()" htmlEscape="false" maxlength="100" style="width:80%;color:#5F9EA0"  class="input-xlarge required"/>
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