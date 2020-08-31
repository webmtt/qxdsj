<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>个人信息</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript" src="../jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#inputForm").validate({
                submitHandler: function(form){
                    //loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
            $("#btnImport").click(function () {
                top.$.jBox($("#importBox").html(), {
                    title: "产品子类别选择", buttons: {"确认": "ok"},
                    submit: function (v, h, f) {
                        /*if (v == "ok") {
                            $("#link").val(f.svalue);
                            console.log(f.svalue)
                        }*/
                    }
                });
                //var sapIds = "${supArithmeticsStream.sapId}";
                /*for (i = 0; i < document.getElementsByName("svalue").length; i++) {
                    var name = document.getElementsByName("svalue");
                    for (j = 0; j < sapIds.split(",").length; j++) {
                        var mycars = sapIds.split(",");
                        if (name[i].value == mycars[j]) {
                            console.log(name[i])
                            name[i].checked = true;
                        }
                    }
                }*/
            });
            $("#btnImports").click(function () {
                top.$.jBox($("#importBox").html(), {
                    title: "业务门类选择", buttons: {"确认": "ok"},
                    submit: function (v, h, f) {
                        /*if (v == "ok") {
                            $("#link").val(f.svalue);
                            console.log(f.svalue)
                        }*/
                    }
                });
                //var sapIds = "${supArithmeticsStream.sapId}";
                /*for (i = 0; i < document.getElementsByName("svalue").length; i++) {
                    var name = document.getElementsByName("svalue");
                    for (j = 0; j < sapIds.split(",").length; j++) {
                        var mycars = sapIds.split(",");
                        if (name[i].value == mycars[j]) {
                            console.log(name[i])
                            name[i].checked = true;
                        }
                    }
                }*/
            });
        });


    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/deploy/getdeploy">用户配置</a></li>
</ul><br/>
<form:form id="inputForm" modelAttribute="uuid" action="${ctx}/sys/deploy/save?id=${uuid}" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">访问配置：</label>
        <div class="controls">
            <input type="radio" name="urlType" value="0" checked="checked" style="margin-left: 17px">外网访问&nbsp;&nbsp;&nbsp;
            <input type="radio" name="urlType" value="1">内网访问
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">产品子类别：</label>
        <div class="controls">
            <input id="btnImport"  class="btn btn-primary" type="button" value="选择产品子类别"/></li>
            <span class="help-inline"><font color="red">*</font> </span>
            <div id="productType" class="hide">
                <tags:message content="${message}"/>
                <table id="contentTable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr>
                        <th>选择</th>
                        <th>产品子类别编号</th>
                        <th>产品子类别名称</th>
                    </tr>
                    </thead>
                    <tbody>
                        <%--<c:forEach items="${1}" var="supArithmeticsPackage">
                            <tr>
                                <td style="text-align:center;">
                                    <input name="svalue" type="checkbox" value="${1}"/>
                                </td>
                                <td style="text-align:center;">
                                        ${1}
                                </td>
                                <td>
                                        ${1}
                                </td>
                            </tr>
                        </c:forEach>--%>
                    </tbody>
                </table>
                    <%--<div class="pagination">${1}</div>--%>
            </div>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">业务门类：</label>
        <div class="controls">
            <input id="btnImports"  class="btn btn-primary" type="button" value="选择业务门类"/></li>
            <span class="help-inline"><font color="red">*</font> </span>
            <div id="serviceType" class="hide">
                <tags:message content="${message}"/>
                <table id="contentTable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr>
                        <th>选择</th>
                        <th>业务门类编号</th>
                        <th>业务门类名称</th>
                    </tr>
                    </thead>
                    <tbody>
                        <%--<c:forEach items="${1}" var="supArithmeticsPackage">
                            <tr>
                                <td style="text-align:center;">
                                    <input name="svalue" type="checkbox" value="${1}"/>
                                </td>
                                <td style="text-align:center;">
                                        ${1}
                                </td>
                                <td>
                                        ${1}
                                </td>
                            </tr>
                        </c:forEach>--%>
                    </tbody>
                </table>
                    <%--<div class="pagination">${1}</div>--%>
            </div>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">图片展示：</label>
        <input type="radio" name="photoType" value="0" checked="checked" style="margin-left: 17px">单图展示&nbsp;&nbsp;&nbsp;
        <input type="radio" name="photoType" value="1">多图展示&nbsp;&nbsp;&nbsp;
        <input type="radio" name="photoType" value="2">列表展示
    </div>
    <div class="control-group">
        <label class="control-label">文件展示：</label>
        <input type="radio" name="fileType" value="0" checked="checked" style="margin-left: 17px">列表展示&nbsp;&nbsp;&nbsp;
        <input type="radio" name="fileType" value="1">树形展示
    </div>
    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;&nbsp;&nbsp;
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>
