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
        });
        function ceshi() {
            var tbody = document.getElementById('myTb');
            var rows = tbody.rows;
            var lists = [];
            for (var i = 0; i < rows.length; i++) {//遍历行
                var paramKey = document.getElementsByClassName("paramKey")[i].value;
                var paramValue = document.getElementsByClassName("paramValue")[i].value;
                lists.push(paramValue);
            }
            $("#params").val(lists.join(","));
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
<form:form id="inputForm" modelAttribute="supArithmeticsStream" action="${ctx}/stream/supArithmeticsStream/test"
           method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <tags:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">输入参数：</label>
        <div class="controls">
            <form:hidden id="params" path="params" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
            <form:hidden id="sapId" path="sapId" htmlEscape="true" maxlength="120" class="input-xlarge required" disabled="true"/>

            <table id="contentTable" class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>
                    <th>参数名称</th>
                    <th>参数值</th>
                </tr>
                </thead>

                <c:if test="${empty supArithmeticsStream.id}">
                    <tbody id="myTb">

                    </tbody>
                </c:if>
                <c:if test="${not empty supArithmeticsStream.id}">
                    <script type="text/javascript">
                        $(document).ready(function () {
                            var lists = new Array();
                            var id = document.getElementsByName("sapId")[0].value.split(",")[0];

                            $.ajax({
                                type: "get",
                                url: "${ctx}/arithmetic/supArithmeticsPackage/getById?id="+id,
                                success: function (res) {
                                    console.log(res);
                                    for (var i = 0; i < res.split(",").length; i++) {//遍历行
                                        var a = res.split(",")[i];
                                        lists.push({
                                            paramKey: a.split(":")[0],
                                            paramValue: a.split(":")[1],
                                        });
                                    }
                                    for (var i = 0; i < lists.length; i++) {
                                        var uuid = Math.random();
                                        var uuids = Math.random();
                                        var trTemp = $("<tr id='" + uuid + "'></tr>");
                                        trTemp.append('<td><input id=' + uuids + ' value=' + lists[i].paramKey + ' type="text" htmlEscape="false" class="input-xlarge paramKey required"/></td>');
                                        trTemp.append('<td><input type="text" value=' + lists[i].paramValue + ' htmlEscape="false"  class="input-xlarge paramValue required"/><span class="help-inline"><font color="red">*</font> </span></td>');
                                        trTemp.appendTo("#myTb");
                                    }
                                }
                            });

                        })
                    </script>
                    <tbody id="myTb">

                    </tbody>
                </c:if>

            </table>

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">预期值：</label>
        <div class="controls">
            <form:input path="pridictValue" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="form-actions">
        <input id="btnSubmit"
               class="btn btn-primary" type="submit"
               value="测试" onclick="ceshi()"/>&nbsp;
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>