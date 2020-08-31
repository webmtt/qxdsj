<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>个人信息</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        var selectDataRoleValue = "";
        $(document).ready(function () {
            /* $("#nativel").prop("checked",true);  */
            getSelectRole();
            getDataRole();
            show();
            var level = $("input[type='radio'][name='level']:checked").val();
            $("#userRankID").attr("value", level);
            //saveValue();
            $("input[type='radio'][name='level']").click(function () {
                var level = $("input[type='radio'][name='level']:checked").val();
                $("#userRankID").attr("value", level);
                if (level == 2) {
                    $("#nativeList").show();
                    $("#nonativeList").hide();
                    $("#orgID").attr("value", "");
                } else {
                    $("#nativeList").hide();
                    $("#nonativeList").show();
                    $("#orgID").attr("value", "");
                }
            });
            $("#userTypeList").change(function () {
                var type = $("#userTypeList option:selected").val();
                $("#userType").attr("value", type);
            });
            $("#serviceLevelList").change(function () {
                var servicelevel = $("#serviceLevelList option:selected").val();
                $("#servicelevel").attr("value", servicelevel);
            });
            $("#first").change(function () {
                var pid = $("#first option:selected").val();
                $("#second").hide();
                $("#s2id_second span").empty();
                $("#third").hide();
                $("#s2id_pthird").hide();
                $("#s2id_third span").empty();
                if (pid != undefined && pid != "000") {
                    findOrgList("second", pid);
                    $("#orgID").attr("value", pid);
                } else {
                    $("#second").empty();
                    $("#s2id_second span").empty();
                    $("#s2id_third").hide();
                    $("#s2id_third span").empty();
                }
                saveValue();
            });
            $("#pfirst").change(function () {
                var pid = $("#pfirst option:selected").val();
                $("#psecond").hide();
                $("#s2id_psecond span").empty();
                $("#pthird").hide();
                $("#s2id_pthird").hide();
                $("#s2id_pthird span").empty();
                if (pid != undefined && pid != "000") {
                    findOrgList("psecond", pid);
                    $("#orgID").attr("value", pid);
                } else {
                    $("#psecond").empty();
                    $("#s2id_psecond span").empty();
                    $("#s2id_pthird").hide();
                    $("#s2id_pthird span").empty();
                }
                saveValue();
            });
            $("#second").change(function () {
                $("#s2id_third span").empty();
                var pid = $("#second option:selected").val();
                if (pid != undefined) {
                    findOrgList("third", pid);
                    $("#orgID").attr("value", pid);
                }
                saveValue();
            });
            $("#third").change(function () {
                var pid = $("#third option:selected").val();
                saveValue();
            });
            $("#psecond").change(function () {
                $("#s2id_pthird span").empty();
                var pid = $("#psecond option:selected").val();
                if (pid != undefined) {
                    findOrgList("pthird", pid);
                    $("#orgID").attr("value", pid);
                }
                saveValue();
            });
            $("#pthird").change(function () {
                var pid = $("#pthird option:selected").val();
                saveValue();
            });


            $("#inputForm").validate({
                submitHandler: function (form) {
                    /* loading('正在提交，请稍等...'); */
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

            var unitIds = $("#unitIds").val();
            var unitIdArray = unitIds.split("-");
            //非国家级
            // if ($("#userRankID").val() == 1) {
            //     $("#pfirst option[value='" + unitIdArray[0] + "']").attr("selected", "selected");
            //     $("#pfirst").change();
            //     $("#psecond option[value='" + unitIdArray[1] + "']").attr("selected", "selected");
            //     $("#psecond").change();
            //     if (unitIdArray.length > 2) {
            //         $("#pthird option[value='" + unitIdArray[2] + "']").attr("selected", "selected");
            //         $("#pthird").change();
            //     }
            // }
            //国家级
            // if ($("#userRankID").val() == 2) {
            $("#first option[value='" + unitIdArray[0] + "']").attr("selected", "selected");
            $("#first").change();
            $("#second option[value='" + unitIdArray[1] + "']").attr("selected", "selected");
            $("#second").change();
            if (unitIdArray.length > 2) {
                $("#third option[value='" + unitIdArray[2] + "']").attr("selected", "selected");
                $("#third").change();
            }
            // }
        });

        //绑定角色列表
        function getSelectRole() {
            $.ajax({
                url: '${ctx}/dataRole/getSelectRole',
                async: false,
                type: 'POST',
                data: {"userid": "${userInfo.iD}"},
                dataType: 'json',
                success: function (result) {
                    if (result!=null&&result != "" && result.length > 0) {
                        selectDataRoleValue = result;
                    }


                }
            })
            ;
        }

        //获取数据角色
        function getDataRole() {
            $.ajax({
                url: '${ctx}/dataRole/getDataRoleInfo',
                async: false,
                type: 'POST',
                dataType: 'json',
                success: function (result) {
                    if (result != "" && result.length > 0) {
                        var info = "";
                        for (var i in result) {
                            var flag = false;
                            for (var j in selectDataRoleValue) {
                                if (selectDataRoleValue[j].dataroleId == result[i].dataroleId) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (flag) {
                                info += "<input type='checkbox' value='" + result[i].dataroleId + "' name='datarole' checked='" + flag + "'/><span>" + result[i].dataroleName + "</span>";
                            } else {
                                info += "<input type='checkbox' value='" + result[i].dataroleId + "' name='datarole'/><span>" + result[i].dataroleName + "</span>";
                            }
                            $("#roleData").html(info);

                        }

                    }
                }
            });
        }

        //职位列表
        function findOrgList(listOrder, pid) {
            $.ajax({
                url: '${ctx}/org/orgListByPid',
                async: false,
                type: 'POST',
                data: {'pid': pid},
                dataType: 'json',
                success: function (result) {
                    var html = "";
                    if (listOrder == "second") {
                        html = "<option value=''>请选择</option>";
                        $("#second").empty();
                        if (result.list != undefined && result.list != "") {
                            $.each(result.list, function (i, n) {
                                html += "<option value=" + n.id + ">" + n.name + "</option>";
                            });
                            $("#second").html(html);
                            $("#second").show();
                            $("#s2id_second").show();
                            $("#orgID").attr("value", "");
                        }
                    } else if (listOrder == "psecond") {
                        html = "<option value=''>请选择</option>";
                        $("#psecond").empty();
                        if (result.list != undefined && result.list != "") {
                            $.each(result.list, function (i, n) {
                                html += "<option value=" + n.id + ">" + n.name + "</option>";
                            });
                            $("#psecond").html(html);
                            $("#psecond").show();
                            $("#s2id_psecond").show();
                            $("#orgID").attr("value", "");
                        }
                    } else if (listOrder == "third") {
                        $("#third").empty();
                        if (result.list != undefined && result.list != "") {
                            $.each(result.list, function (i, n) {
                                html += "<option value=" + n.id + ">" + n.name + "</option>";
                            });
                            $("#third").html(html);
                            $("#third").show();
                            $("#s2id_third").show();
                            $("#orgID").attr("value", "");
                        }
                    } else if (listOrder == "pthird") {
                        $("#pthird").empty();
                        if (result.list != undefined && result.list != "") {
                            $.each(result.list, function (i, n) {
                                html += "<option value=" + n.id + ">" + n.name + "</option>";
                            });
                            $("#pthird").html(html);
                            $("#pthird").show();
                            $("#s2id_pthird").show();
                            $("#orgID").attr("value", "");
                        }
                    }
                }
            });
        }

        function show() {
            $("#second").hide();
            $("#s2id_second").hide();
            $("#third").hide();
            $("#s2id_third").hide();
            $("#psecond").hide();
            $("#s2id_psecond").hide();
            $("#pthird").hide();
            $("#s2id_pthird").hide();
            // var level = $("input[type='radio'][name='level']:checked").val();
            // var type = $("#userTypeList option:selected").val();
            var level = 2;
            var type = 1;
            if (level == 2) {
                $("#nativeList").show();
                $("#nonativeList").hide();
            } else {
                $("#nativeList").hide();
                $("#nonativeList").show();
            }
            $("#userType").attr("value", type);
        }

        function saveValue() {
            var pid;
            if ($("#nativeList").is(':visible')) {
                if ($("#first").is(':visible')) {
                    if ($("#second").is(':visible')) {
                        if ($("#third").is(':visible')) {
                            pid = $("#third option:selected").val();
                        } else {
                            pid = $("#second option:selected").val();
                        }
                    } else {
                        pid = $("#first option:selected").val();
                    }
                }
            } else if ($("#nonativeList").is(':visible')) {
                if ($("#psecond").is(':visible')) {
                    if ($("#pthird").is(':visible')) {
                        pid = $("#pthird option:selected").val();
                    } else {
                        pid = $("#psecond option:selected").val();
                    }
                } else {
                    pid = $("#pfirst option:selected").val();
                }
            }
            $("#orgID").attr("value", pid);
        }

        //表单校验
        function check() {
            debugger;
            var flag = false;
            checkUserName();    //增加用户名校验，防止用户选择浏览器提示内容
            checkMobile();
            var orgID = $("#orgID").val();
            var id = $("#iD").val();
            var position = $("#position").val();
            var job = $("#jobTitle").val();
            var age = $('input:radio[name="ageRange"]:checked').val();
            var obj = document.getElementsByName("datarole");
            var check_val = [];
            for (k in obj) {
                if (obj[k].checked)
                    check_val.push(obj[k].value);
            }
            if(check_val.length==0){
                alert('请选择数据角色！');
                return;
            }
            // var email=$("#emailName").val();
            var phonemodel = $('input:radio[name="phoneModel"]:checked').val();
            if ($("#userNameMsg").css('color') == 'red' || $("#userNameMsg").css('color') == 'rgb(255, 0, 0)') {
                alert('请输入正确的登录名');
                return;
            }
            if (orgID == undefined || orgID == "" || orgID == "000") {
                alert("请选择工作单位");
                return;
            }
            // if (position == undefined || position == "" || position == "0") {
            //     alert("请选择职位");
            //     return;
            // }
            // if (job == undefined || job == "" || job == "0") {
            //     alert("请选择职称");
            //     return;
            // }
            // if (age == undefined || job == null) {
            //     alert("请选择年龄段");
            //     return;
            // }
            // if (phonemodel == undefined || phonemodel == null) {
            //     alert("请选择手机型号");
            //     return false;
            // }
            if ($("#mobileMsg").css('color') == 'red' || $("#mobileMsg").css('color') == 'rgb(255, 0, 0)') {
                alert('请输入正确的手机号');
                return;
            }
            // var reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
            // if(!reg.test(email)){
            //     alert("邮箱格式不正确");
            //     return;
            // }

            var f = document.getElementsByTagName("form")[0];
            f.action = f.action + "?selectRole=" + JSON.stringify(check_val);
            f.submit();
        }

        function checkUserName() {

            if ('${empty userInfo.iD}' == 'true') {
                $("#userNameMsg").empty();
                var userName = $("#userName").val();
                var id = $("#iD").val();
                var nameReg = /^[0-9a-zA-Z]+$/;
                var emailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
                if (!nameReg.test(userName) && !emailReg.test(userName)) {
                    $("#userNameMsg").css("color", "red");
                    $("#userNameMsg").text("用户名不合法");
                } else {
                    $.ajax({
                        url: '${ctx}/user/checkUserName',
                        type: 'post',
                        data: {'userName': userName},
                        dataType: 'text',
                        success: function (result) {
                            if (result == "1") {
                                $("#userNameMsg").css("color", "green");
                                $("#userNameMsg").text("用户名可用");
                            } else {
                                $("#userNameMsg").css("color", "red");
                                $("#userNameMsg").text("用户名已存在");
                            }
                        }
                    });
                }
            }
        }

        function checkMobile() {
            $("#mobileMsg").empty();
            var id = $("#iD").val();
            var mobile = $("#mobile").val();
            var mobileReg = /^(1)\d{10}$/;
            if (!mobileReg.test(mobile)) {
                $("#mobileMsg").css("color", "red");
                $("#mobileMsg").text("手机号不合法");
            } else {
                $.ajax({
                    url: '${ctx}/user/checkMobile',
                    type: 'post',
                    data: {'mobile': mobile, 'id': id},
                    dataType: 'text',
                    success: function (result) {
                        if (result == "1") {
                            $("#mobileMsg").css("color", "green");
                            $("#mobileMsg").text("手机号可用");
                        } else {
                            $("#mobileMsg").css("color", "red");
                            $("#mobileMsg").text("手机号已存在");
                        }
                    }
                });
            }
        }

        function isNameExit(userName) {
            var flag;
            $.ajax({
                url: '${ctx}/user/checkUserName',
                type: 'post',
                data: {'userName': userName},
                dataType: 'text',
                success: function (result) {
                    flag = result;
                }
            });
            return flag;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/user/userList">用户列表</a></li>
    <li class="active"><a href="javaScript:void(0)">${not empty userInfo.iD?'修改':'添加'}用户</a></li>
    <%-- <li><a href="${ctx}/user/addUserAppPage">添加APP用户</a></li> --%>
</ul>
<br/>
<input type="hidden" id="unitIds" value="${unitIds }">
<form:form id="inputForm" modelAttribute="userInfo" action="${ctx}/user/addUser" method="post" class="form-horizontal">
    <tags:message content="${message}"/>
    <form:hidden path="iD" id="iD"/>
    <form:hidden path="url" id="url"/>
    <form:hidden path="orgID" id="orgID" value="${userInfo.orgID }"/>
    <%--    <form:hidden path="cpwd" id="cpwd"/>--%>
    <%--    <form:hidden path="regSource" id="regSource"/>--%>
    <%--    <div class="control-group">--%>
    <%--        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>用户级别:</label>--%>
    <%--        <div class="controls">--%>
    <%--            <input type="radio" id="nativel" value="2" name="level"--%>
    <%--            <c:if test="${userInfo.userRankID ne 1}">--%>
    <%--                   checked="checked"--%>
    <%--            </c:if>--%>
    <%--            >国家级用户--%>
    <%--            <input type="radio" id="nonnativel" value="1" name="level"--%>
    <%--            <c:if test="${userInfo.userRankID eq 1}">--%>
    <%--                   checked="checked"--%>
    <%--            </c:if>--%>
    <%--            >非国家级用户--%>
    <%--            <form:hidden path="userRankID" id="userRankID" value="${userInfo.userRankID }"/>--%>
    <%--        </div>--%>
    <%--    </div>--%>
    <div class="control-group">
        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>数据角色:</label>
        <div class="controls" id="roleData">

        </div>
    </div>
<%--    <div class="control-group">--%>
<%--        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>用户类型:</label>--%>
<%--        <div class="controls">--%>
<%--            <select id="userTypeList">--%>
<%--                <option value="1"--%>
<%--                        <c:if test="${userInfo.userType eq 1}">--%>
<%--                            selected="selected"--%>
<%--                        </c:if>--%>
<%--                >展示系统登录--%>
<%--                </option>--%>
<%--                <option value="2"--%>
<%--                        <c:if test="${userInfo.userType eq 2}">--%>
<%--                            selected="selected"--%>
<%--                        </c:if>--%>
<%--                >管理站点登录--%>
<%--                </option>--%>
<%--                <option value="3"--%>
<%--                        <c:if test="${userInfo.userType eq 3}">--%>
<%--                            selected="selected"--%>
<%--                        </c:if>--%>
<%--                >展示和管理站点同时登录--%>
<%--                </option>--%>
<%--            </select>--%>
<%--            <form:hidden path="userType" id="userType"/>--%>
<%--        </div>--%>
<%--    </div>--%>
    <c:choose>
        <c:when test="${not empty userInfo.iD}">
            <div class="control-group">
                <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>登录名:</label>
                <div class="controls">
                    <form:hidden path="userName" id="userName" htmlEscape="false" maxlength="50"/>
                    <label>${userInfo.userName}</label>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="control-group">
                <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>登录名:</label>
                <div class="controls">
                    <form:input path="userName" id="userName" htmlEscape="false" maxlength="50" class="required"
                                onkeyup="checkUserName()" onblur="checkUserName()" onchange="checkUserName()"/>
                    <span id="userNameMsg" style="color: #666666;">/*请输入字母或数字*/</span>
                </div>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="control-group">
        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>密码:</label>
        <div class="controls">
            <input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3"
                   class="${empty userInfo.iD?'required':''}"/>
            <form:hidden path="password" id="password"/>
                <%-- <form:password  path="password" id="password" name="password" type=  htmlEscape="false" maxlength="50" class="${empty userInfo.iD?'required':''}"/> --%>
            <c:if test="${not empty userInfo.iD}"><span class="help-inline">若不修改密码，请留空。</span></c:if>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>确认密码:</label>
        <div class="controls">
            <input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50"
                   minlength="3" equalTo="#newPassword"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>姓名:</label>
        <div class="controls">
            <form:input path="chName" id="chName" htmlEscape="false" maxlength="50" class="required"/>
            <span id="chNameMsg" style="color: red;"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>工作单位:</label>
        <div class="controls" id="nativeList">
            <select id="first">
                <option value="000">请选择</option>
                <c:forEach items="${oneList}" var="orgone">
                    <option value="${orgone.id}">${orgone.name}</option>
                </c:forEach>
            </select>
<%--            <select id="second">--%>
<%--                <option value="000"></option>--%>
<%--            </select>--%>
<%--            <select id="third">--%>
<%--                <option value="000"></option>--%>
<%--            </select>--%>
        </div>

    </div>
<%--    <div class="control-group">--%>
<%--        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>职位:</label>--%>
<%--        <div class="controls" id="nativeList3">--%>
<%--            <select id="position" name="position">--%>
<%--                <option value="0">请选择</option>--%>
<%--                <c:forEach items="${positionList}" var="orgone" varStatus="status">--%>
<%--                    <option value="${orgone.value}"--%>
<%--                            <c:if test="${orgone.value == userInfo.position}">selected</c:if>>${orgone.label}</option>--%>
<%--                </c:forEach>--%>
<%--            </select>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    <div class="control-group">--%>
<%--        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>职称:</label>--%>
<%--        <div class="controls" id="nativeList4">--%>
<%--            <select id="jobTitle" name="jobTitle">--%>
<%--                <option value="0">请选择</option>--%>
<%--                <c:forEach items="${JobList}" var="orgone">--%>
<%--                    <option value="${orgone.value}"--%>
<%--                            <c:if test="${orgone.value == userInfo.jobTitle}">selected</c:if>>${orgone.label}</option>--%>
<%--                </c:forEach>--%>
<%--            </select>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    <div class="control-group">--%>
<%--        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>年龄:</label>--%>
<%--        <div class="controls">--%>
<%--            <c:forEach items="${ageList}" var="orgone" varStatus="status">--%>
<%--                <c:if test="${ status.index ==0}">--%>
<%--                    <input type="radio" value="${orgone.value}" name="ageRange" checked="checked">${orgone.label}--%>
<%--                </c:if>--%>
<%--                <c:if test="${ status.index !=0}">--%>
<%--                    <input type="radio" value="${orgone.value}" name="ageRange"--%>
<%--                           <c:if test="${orgone.value == userInfo.ageRange}">checked="checked"</c:if>>${orgone.label}--%>
<%--                </c:if>--%>

<%--            </c:forEach>--%>

            <!-- 		<input type="radio" value="1" name="user_agerange"	>18-25
                    <input type="radio" value="2" name="user_agerange">30-40 -->
<%--        </div>--%>
<%--    </div>--%>
<%--    <div class="control-group">--%>
<%--        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>手机类型:</label>--%>
<%--        <div class="controls">--%>
<%--            <c:forEach items="${phoneList}" var="orgone" varStatus="status">--%>
<%--                <c:if test="${ status.index ==0}">--%>
<%--                    <input type="radio" value="${orgone.value}" name="phoneModel" checked="checked">${orgone.label}--%>
<%--                </c:if>--%>
<%--                <c:if test="${ status.index !=0}">--%>
<%--                    <input type="radio" value="${orgone.value}" name="phoneModel"--%>
<%--                           <c:if test="${orgone.value == userInfo.phoneModel}">checked="checked"</c:if>>${orgone.label}--%>
<%--                </c:if>--%>
<%--            </c:forEach>--%>
<%--            <!-- <input type="radio" value="1" name="user_phonemodel" >IOS--%>
<%--            <input type="radio" value="2" name="user_phonemodel">安卓 -->--%>
<%--        </div>--%>
<%--    </div>--%>
    <div class="control-group">
        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>手机:</label>
        <div class="controls">
            <form:input path="mobile" id="mobile" htmlEscape="false" maxlength="11" class="required"
                        onkeyup="checkMobile()"/>
            <span id="mobileMsg"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">办公电话:</label>
        <div class="controls">
            <form:input path="phone" id="phone" htmlEscape="false" maxlength="50"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">微信号:</label>
        <div class="controls">
            <form:input path="wechatNumber" htmlEscape="false" maxlength="50"/>
        </div>
    </div>
<%--    <div class="control-group">--%>
<%--        <label class="control-label">邮箱:</label>--%>
<%--        <div class="controls">--%>
<%--            <form:input path="emailName" htmlEscape="false" maxlength="50"/>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    <div class="control-group">--%>
<%--        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>服务级别:</label>--%>
<%--        <div class="controls">--%>
<%--            <select id="serviceLevelList">--%>
<%--                <c:forEach items="${serviceLevelList}" var="serviceLevel">--%>
<%--                    <option value="${serviceLevel.servicelevel}"--%>
<%--                            <c:if test="${userInfo.servicelevel == serviceLevel.servicelevel}">selected="selected"</c:if>>--%>
<%--                            ${serviceLevel.servicelevel}级（${serviceLevel.subordernum}个子订单，${serviceLevel.filesumsize}M下载量）--%>
<%--                    </option>--%>
<%--                </c:forEach>--%>
<%--            </select>--%>
<%--            <form:hidden path="servicelevel" id="servicelevel"/>--%>
<%--        </div>--%>
<%--    </div>--%>
    <%--    <div class="control-group">--%>
    <%--        <label class="control-label"><span style="color: red;margin:0px 6px;">*</span>用户状态:</label>--%>
    <%--        <div class="controls">--%>
    <%--            <input type="radio" value="4"--%>
    <%--                   <c:if test="${userInfo.isActive=='4'}">checked="checked"</c:if> name="isActive">待激活--%>
    <%--            <input type="radio" value="0"--%>
    <%--                   <c:if test="${userInfo.isActive=='0'}">checked="checked"</c:if> name="isActive">未激活--%>
    <%--            <input type="radio" value="1"--%>
    <%--                   <c:if test="${userInfo.isActive=='1'}">checked="checked"</c:if> name="isActive">已激活--%>
    <%--            <input type="radio" value="2"--%>
    <%--                   <c:if test="${userInfo.isActive=='2'}">checked="checked"</c:if> name="isActive">未通过--%>
    <%--            <input type="radio" value="3"--%>
    <%--                   <c:if test="${userInfo.isActive=='3'}">checked="checked"</c:if> name="isActive">待审核--%>
    <%--        </div>--%>
    <%--    </div>--%>
    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="check()"/>&nbsp;
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>