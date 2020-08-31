<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<html>
<head>
    <title>算法池管理</title>
    <meta name="decorator" content="default"/>

    <script type="text/javascript">
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        $(document).ready(function () {
            document.getElementById("chufashijian").style.display="none";
            var idValue = document.getElementsByName("id")[0].value;
            $("#streamName").blur(function(){
                var dateValue = $("#streamName").val();
                $.ajax({
                    type: "post",
                    url: "${ctx}/stream/supArithmeticsStream/findObjectByAirName",
                    data: {
                        streamName: dateValue
                    },
                    success: function (res) {
                        if (idValue == "" || idValue == null ) {
                            if(res != ""){
                                $("#streamName").css("background-color", "red");
                                $("#btnSubmit").attr("disabled", true);
                                document.getElementById("chufashijian").style.display="";
                            }else {
                                $("#streamName").css("background-color", "");
                                $("#btnSubmit").attr("disabled", false);
                                document.getElementById("chufashijian").style.display="none";
                            }
                        } else {
                            if (res.id != idValue && res != "") {
                                $("#streamName").css("background-color", "red");
                                $("#btnSubmit").attr("disabled", true);
                                document.getElementById("chufashijian").style.display="";
                            }else {
                                $("#streamName").css("background-color", "");
                                $("#btnSubmit").attr("disabled", false);
                                document.getElementById("chufashijian").style.display="none";
                            }
                        }
                    }
                });
            })
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
            $("#addTr").click(function () {
                var uuid = Math.random();
                var uuids = Math.random();
                var uuidsq = Math.random();
                var trTemp = $("<tr id='" + uuid + "'></tr>");
                trTemp.append("<td><input class='btn btn-primary' type='button' value='清除' onclick='clearData(this)'></td>");
                trTemp.append('<td><input id=' + uuids + ' type="text" htmlEscape="false"  class="input-xlarge sapIdName required" onclick="onbox(' + uuids + ')"/></td>');
                trTemp.append('<td><input id=' + uuidsq + ' type="text" onblur="blurEvent(' + uuidsq + ')" htmlEscape="false" class="input-xlarge sequenceName " onkeyup="value=value.replace(/[^\\-?\\d.]/g,\'\')"/><label id=' + (uuidsq+1) + ' style="display: none" class="error">必填信息</label></td>');
                trTemp.appendTo("#myTb");
            });
        });
        function blurEvent(uuidsq){
            var ndesc = document.getElementById(uuidsq).valueOf().value;
            if (typeof(ndesc)=="undefined" || ndesc=='' || ndesc==null){
                document.getElementById(uuidsq+1).style.display="";
                $("#btnSubmit").attr("disabled", true);
            }else {
                document.getElementById(uuidsq+1).style.display="none";
                $("#btnSubmit").attr("disabled", false);
            }
        }
        function clearData(obj) {
            var index = $(obj).parents("tr").index(); //这个可获取当前tr的下标 未使用
            $(obj).parents("tr").remove(); //实现删除tr
        }

        function onbox(uuids) {
            top.$.jBox($("#importBox").html(), {
                title: "算法选择", buttons: {"确认": "ok"},
                submit: function (v, h, f) {
                    if (v == "ok") {
                        document.getElementById(uuids).value = f.svalue.split(",")[1];
                    }
                }
            })
        }

        function baocun() {
            var tbody = document.getElementById('myTb');
            var rows = tbody.rows;
            var sapIdNames = [];
            var sequenceNames = [];
            for (var i = 0; i < rows.length; i++) {//遍历行
                sapIdNames.push(document.getElementsByClassName("sapIdName")[i].value);
                sequenceNames.push(document.getElementsByClassName("sequenceName")[i].value);
            }
            $("#links").val(sapIdNames);
            $("#linkss").val(sequenceNames);
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/stream/supArithmeticsStream/">算法池列表</a></li>
    <li class="active"><a
            href="${ctx}/stream/supArithmeticsStream/form?id=${supArithmeticsStream.id}">算法池${not empty supArithmeticsStream.id?'修改':'添加'}</a>
    </li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="supArithmeticsStream" action="${ctx}/stream/supArithmeticsStream/save"
           method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <tags:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">算法池名称：</label>
        <div class="controls">
            <form:input id="streamName" path="streamName" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
            <label id="chufashijian" class="error">算法池名重复</label>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">数据源选择：</label>
        <div class="controls">
            <form:input path="purpose" htmlEscape="false" maxlength="500" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">算法名称：</label>
        <div class="controls">
            <form:hidden id="linkss" path="sequence" htmlEscape="true" maxlength="120" class="input-xlarge required"/>
            <form:hidden id="links" path="sapId" htmlEscape="true" maxlength="120" class="input-xlarge required"/>
            <table id="contentTable" class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>
                    <th><input id="addTr" class="btn btn-primary" type="button" value="新增"/></th>
                    <th>算法名称</th>
                    <th>顺序</th>
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
                            for (var i = 0; i < document.getElementsByName("sapId")[0].value.split(",").length; i++) {//遍历行
                                lists.push({
                                    sapId: document.getElementsByName("sapId")[0].value.split(",")[i],
                                    sequence: document.getElementsByName("sequence")[0].value.split(",")[i]
                                });
                            }
                            for (var i = 0; i < lists.length; i++) {
                                var uuid = Math.random();
                                var uuids = Math.random();
                                var trTemp = $("<tr id='" + uuid + "'></tr>");
                                trTemp.append("<td><input class='btn btn-primary' type='button' value='清除' onclick='clearData(this)'></td>");
                                trTemp.append('<td><input id=' + uuids + ' value=' + lists[i].sapId + ' type="text" htmlEscape="false" class="input-xlarge sapIdName required" onclick="onbox(' + uuids + ')" /></td>');
                                trTemp.append('<td><input type="text" value=' + lists[i].sequence + ' htmlEscape="false"  class="input-xlarge sequenceName"/></td>');
                                trTemp.appendTo("#myTb");
                            }
                        })
                    </script>
                    <tbody id="myTb">

                    </tbody>
                </c:if>
            </table>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">备注信息：</label>
        <div class="controls">
            <form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="50" class="input-xxlarge "/>
        </div>
    </div>
    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary"
               type="submit"
               value="保 存" onclick="baocun()"/>&nbsp;
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
    <div id="importBox" class="hide">
        <%--<form:form id="searchForm" modelAttribute="supArithmeticsPackage" action="${ctx}/arithmetic/supArithmeticsPackage/" method="post" class="breadcrumb form-search">--%>
        <%--<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>--%>
        <%--<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>--%>
        <%--</form:form>--%>
        <%--<tags:message content="${message}"/>--%>
        <table id="contentTables" class="table table-striped table-bordered table-condensed">
            <thead>
            <tr>
                <th>选择</th>
                <th>算法编号</th>
                <th>算法名称</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.list}" var="supArithmeticsPackage">
                <tr>
                    <td style="text-align:center;">
                        <input name="svalue" type="radio"
                               value="${supArithmeticsPackage.id},${supArithmeticsPackage.ariName}"/>
                    </td>
                    <td style="text-align:center;">
                            ${supArithmeticsPackage.id}
                    </td>
                    <td>
                            ${supArithmeticsPackage.ariName}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">${page}</div>
    </div>
</body>
</html>