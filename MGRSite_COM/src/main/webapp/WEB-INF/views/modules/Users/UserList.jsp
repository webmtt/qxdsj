<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>用户列表</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <script src="${ctxStatic}/ultra/js/user.js?v=1" type="text/javascript"></script>
    <script type="text/javascript">
        $(function () {
            /* 	$('#myModal').modal('hide'); */
            $('#myModal').hide();
            var orderBy = $("#orderBy").val().split(" ");
            $("#tablelist th.sort").each(function () {
                console.log(orderBy)
                if ($(this).hasClass(orderBy[0])) {
                    orderBy[1] = orderBy[1] && orderBy[1].toUpperCase() == "DESC" ? "down" : "up";
                    console.log($(this).html())
                    $(this).html($(this).html() + " <i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
                }
            });
            $("#tablelist th.sort").click(function () {
                var order = $(this).attr("class").split(" ");
                var sort = $("#orderBy").val().split(" ");
                for (var i = 0; i < order.length; i++) {
                    if (order[i] == "sort") {
                        order = order[i + 1];
                        break;
                    }
                }
                if (order == "created") {
                    if (order == sort[0]) {
                        sort = (sort[1] && sort[1].toUpperCase() == "ASC" ? "DESC" : "ASC");
                        $("#orderBy").val(order + " DESC" != order + " " + sort ? "" : order + " " + sort);
                    } else {
                        $("#orderBy").val(order + " ASC");
                    }
                    page();
                } else {
                    if (order == sort[0]) {
                        sort = (sort[1] && sort[1].toUpperCase() == "DESC" ? "ASC" : "DESC");
                        $("#orderBy").val(order + " DESC" != order + " " + sort ? "" : order + " " + sort);
                    } else {
                        $("#orderBy").val(order + " ASC");
                    }
                    page();
                }
            });

        });
    </script>
</head>

<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/user/userList">用户列表</a></li>
    <li><a href="${ctx}/user/addUserPage">添加用户</a></li>
    <%-- <li><a href="${ctx}/user/addUserAppPage">添加APP用户</a></li> --%>
</ul>
<form:form id="form1" action="${ctx}/user/userList" method="post"
           class="breadcrumb form-search" commandName="customer">
    <input id="pageNo" name="pageNo" type="hidden" value="${pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${pageSize}"/>
    <input id="orderBy" name="orderBy" type="hidden"
           value="${orderBy}"/>
    <div align="left">
        <span style="font-weight: bold;">用户状态：</span>
        <input type="radio"
               value="5"
        <c:if test="${IsActive eq '5'}">
               checked="checked"
        </c:if> name="status">全部
            <%--        <input type="radio" value="4"--%>
            <%--        <c:if test="${IsActive eq '4'}">--%>
            <%--               checked="checked"--%>
            <%--        </c:if> name="status">待激活--%>
        <input type="radio"
               value="0"
        <c:if test="${IsActive eq '0'}">
               checked="checked"
        </c:if> name="status">待审核
        <input type="radio" value="1"
        <c:if test="${IsActive eq '1'}">
               checked="checked"
        </c:if> name="status">审核通过
        <input type="radio" value="2"
        <c:if test="${IsActive eq '2'}">
               checked="checked"
        </c:if> name="status">未通过
            <%--        <input type="radio" value="3"--%>
            <%--        <c:if test="${IsActive eq '3'}">--%>
            <%--               checked="checked"--%>
            <%--        </c:if> name="status">待审核--%>
<%--        &nbsp;&nbsp;&nbsp;&nbsp; <span style="font-weight: bold;">用户类型：</span>--%>
<%--            &lt;%&ndash; <form:select path="userType"><form:options items="${typeList}" htmlEscape="false"/></form:select> &ndash;%&gt;--%>
<%--        <select id="selects" style="width:140px">--%>
<%--            <option value="">请选择用户类型</option>--%>
<%--            <option value="1"--%>
<%--                    <c:if test="${userType eq 1}">--%>
<%--                        selected="selected"--%>
<%--                    </c:if>>展示系统登录--%>
<%--            </option>--%>
<%--            <option value="2"--%>
<%--                    <c:if test="${userType eq 2}">--%>
<%--                        selected="selected"--%>
<%--                    </c:if>>管理站点登录--%>
<%--            </option>--%>
<%--            <option value="3"--%>
<%--                    <c:if test="${userType eq 3}">--%>
<%--                        selected="selected"--%>
<%--                    </c:if>>展示和管理站点同时登录--%>
<%--            </option>--%>
<%--        </select>--%>
            <%--        &nbsp;&nbsp;&nbsp;&nbsp; <span style="font-weight: bold;">用户级别：</span>--%>
            <%--            &lt;%&ndash; <form:select path="userType"><form:options items="${typeList}" htmlEscape="false"/></form:select> &ndash;%&gt;--%>
            <%--        <select id="userRankID" style="width:140px">--%>
            <%--            <option value="">请选择用户级别</option>--%>
            <%--            <option value="2" <c:if test="${userRankID eq '2'}"> selected="selected" </c:if>>国家级用户</option>--%>
            <%--            <option value="1" <c:if test="${userRankID eq '1'}"> selected="selected" </c:if>>非国家级用户</option>--%>
            <%--        </select>--%>
        <input type="text" id="searchName" style="width:160px" name="searchName" value="${searchName}"
               placeholder="登录名/姓名/手机号">
        &nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
                     value="查询" onclick="return page()"/>
    </div>
</form:form>
<tags:message content="${message}"/>
<div>
    <table id="tablelist"
           class="table table-striped table-bordered table-condensed"
           style="word-break:break-all; word-wrap:break-word;">
        <thead>
        <th style="text-align: center;">姓名</th>
        <th style="text-align: center;">登录名</th>
        <th style="text-align: center;">工作单位</th>
        <th style="text-align: center;">手机号</th>
        <th style="text-align: center;">办公电话</th>
<%--        <th style="text-align: center;">用户类型</th>--%>
        <th style="text-align: center;">产品库访问地址</th>
        <%--				<th style="text-align: center;">用户级别</th>--%>
        <th style="text-align: center;">用户状态</th>
        <th style="text-align: center;width:180px" class="sort created">创建时间</th>
        <th style="text-align: center;width:130px">操作</th>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="user">
            <tr>
                <td style="width: 8%;">${user.chName}</td>
                <td style="width: 11%;">${user.userName}</td>
                <td style="width: 11%;">${user.orgID} </td>
                <td style="width: 10%;">${user.mobile}</td>
                <td style="width: 10%;">${user.phone}</td>
<%--                <td style="width: 10%;">${fns:getDictLabel(user.userType,'userType', '')}&lt;%&ndash; ${user.userType} &ndash;%&gt;</td>--%>
                    <%--                <td style="width: 10%;">--%>
                    <%--                    <c:if test="${user.userRankID==2}">国家级用户</c:if>--%>
                    <%--                    <c:if test="${user.userRankID==1}">非国家级用户</c:if>--%>
                    <%--                </td>--%>
                <td style="width: 13%;">${user.url}</td>
                <td style="width: 5%;">${fns:getDictLabel(user.isActive,'userExam', '')}<%-- ${user.isActive} --%></td>
                <td style="width: 10%;text-align:center"><fmt:formatDate value="${user.created }"
                                                                         pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td style="width: 12%;text-align: center;">
                    <a href="#" data-toggle="modal" data-target="#myModal"
                       onclick="selectById('${user.iD}')">查看</a>
                    <c:if test="${sysuser=='系统管理员'}">
                        &nbsp;&nbsp;<a href="${ctx}/user/resetUserPassword?id=${user.iD}">重置密码</a>
                    </c:if>

                    <c:choose>
                        <c:when test="${user.isActive eq 0}">
                            &nbsp;&nbsp;<a href="${ctx}/user/getUser?id=${user.iD}">审核</a>
                            &nbsp;&nbsp;<a href="${ctx}/user/delUserById?id=${user.iD}"
                            onclick="return confirmx('确认要删除该记录吗？', this.href)">删除</a>
                        </c:when>
                        <c:otherwise>
<%--                            <c:choose>--%>
<%--                                <c:when test="${user.isActive eq 2}">--%>
                            &nbsp;&nbsp;        <a href="${ctx}/user/addUserPage?id=${user.iD}">修改</a>
                            &nbsp;&nbsp;        <a href="${ctx}/user/delUserById?id=${user.iD}"
                                        onclick="return confirmx('确认要删除该记录吗？', this.href)">删除</a>
<%--                                </c:when>--%>
<%--                                <c:otherwise>--%>
<%--                                    <a href="${ctx}/user/delUserById?id=${user.iD}"--%>
<%--                                       onclick="return confirmx('确认要删除该记录吗？', this.href)">删除</a>--%>
<%--                                </c:otherwise>--%>
<%--                            </c:choose>--%>
                        </c:otherwise>
                    </c:choose>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/user/deleteUserProduct?id=${user.iD}">取消URL授权</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header"
                     style="height: 30px;vertical-align:middle;padding-bottom: 2px;padding-top: 5px;">
                    <table>
                        <tr>
                            <td><h4 class="modal-title" id="myModalLabel" style="float: left;width: 518px;">用户详细信息</h4>
                            </td>
                            <td>
                                <button type="button" class="close" data-dismiss="modal"
                                        aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="modal-body">

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="closemodel()">关闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="pagination">${page}</div>
<script type="text/javascript">
    if ($("#messageBox").length > 0) {
        $("#messageBox").show();
    }
</script>
</body>
</html>
