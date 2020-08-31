<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/dialog.jsp" %>
<html>
<head>
    <title>查看用户</title>
    <style type="text/css">
        .content {
            font-family: '微软雅黑';
            vertical-align: top;
            min-width: 525px;
            min-height: 228px;
        }

        .name {
            font-size: 14px;
            font-weight: bold;
            margin: 5px 5px;
            text-align: right;
        }
    </style>
</head>
<body>
<div class="content">
    <table>
        <tr>
            <td class="name">姓名：</td>
            <td>${UserInfo.chName}</td>
            <td class="name">登录名:</td>
            <td>${UserInfo.userName }</td>
        </tr>
<%--        <tr>--%>
<%--            <td class="name">邮箱：</td>--%>
<%--            <td colspan="3">${UserInfo.emailName }</td>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <td class="name">用户类型：</td>--%>
<%--            <td>${fns:getDictLabel(UserInfo.userType,'userType', '')}</td>--%>
<%--            <td></td>--%>
<%--            <td></td>--%>
<%--        </tr>--%>
        <tr>
            <td class="name">办公电话：</td>
            <td>${UserInfo.phone }</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <c:choose>
                <c:when test="${datarole!=''}">
                    <td class="name">数据角色：</td>
                    <td>${datarole}</td>
                </c:when>
                <c:otherwise>
                    <td class="name">数据资料：</td>
                    <td>${UserInfo.dataInfo}</td>
                </c:otherwise>
            </c:choose>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td class="name">工作单位：</td>
            <td colspan="3">${UserInfo.orgID } </td>
        </tr>
<%--        <tr>--%>
<%--            <td class="name">职位：</td>--%>
<%--            <td>${position }</td>--%>
<%--            <td class="name">职称：</td>--%>
<%--            <td>${jobtitle }</td>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <td class="name">年龄范围：</td>--%>
<%--            <td>${agerange }</td>--%>
<%--            <td class="name">手机类型：</td>--%>
<%--            <td>${phonemodel }</td>--%>
<%--        </tr>--%>
        <tr>
            <td class="name">微信号：</td>
            <td>${UserInfo.wechatNumber }</td>
            <td class="name">手机号：</td>
            <td>${UserInfo.mobile }</td>
        </tr>
        <tr>
            <%--            <td class="name">用户级别：</td>--%>
            <%--            <td>--%>
            <%--                &lt;%&ndash;					${fns:getDictLabel(UserInfo.userRankID,'UserRankId', '')}&ndash;%&gt;--%>
            <%--                <c:if test="${UserInfo.userRankID==2}">国家级用户</c:if>--%>
            <%--                <c:if test="${UserInfo.userRankID==1}">非国家级用户</c:if>--%>

            <%--            </td>--%>
            <%-- <td>${UserInfo.userRankID }</td> --%>
            <td class="name">激活状态：</td>
            <c:if test="${UserInfo.isActive eq 2}">
            <td style="color: red;">
                </c:if>
                <c:if test="${UserInfo.isActive eq 0}">
            <td style="color:#ffe04f;font-weight: bold;">
                </c:if>
                <c:if test="${UserInfo.isActive eq 1}">
            <td style="color: #5fa207;font-weight: bold;">
                </c:if>
                <c:if test="${UserInfo.isActive eq 3}">
            <td style="color: #97d2f3;font-weight: bold;">
                </c:if>
                <c:if test="${UserInfo.isActive eq 4}">
            <td style="color: #97d2f3;font-weight: bold;">
                </c:if>
                <!-- <td style="color: red;"> -->${fns:getDictLabel(UserInfo.isActive,'userExam', '')}</td>
        </tr>
        <tr>
            <td class="name">最后登录日期：</td>
            <td><fmt:formatDate value="${UserInfo.lastLoginDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td class="name">记录创建时间：</td>
            <td><fmt:formatDate value="${UserInfo.created }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <%-- <td>${UserInfo.created }</td> --%>
            <td class="name">记录创建主机名：</td>
            <td>${UserInfo.createdBy }</td>
        </tr>
        <tr>
            <td class="name">记录更新时间：</td>
            <td><fmt:formatDate value="${UserInfo.updated }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <%-- <td>${UserInfo.updated }</td> --%>
            <td class="name">记录更新主机名：</td>
            <td>${UserInfo.updatedBy }</td>
        </tr>
        <c:if test="${UserInfo.isActive eq 2}">
            <tr>
                <td class="name">审核不通过原因：</td>
                <td>${reason }</td>
            </tr>
        </c:if>
    </table>
</div>
</body>
</html>
