<%--
  Created by IntelliJ IDEA.
  User: 18695
  Date: 2020-04-10
  Time: 下午 5:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>用户列表</title>
    <script src="${ctxStatic}/layui-v2.5.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
    <link href="${ctxStatic}/layui-v2.5.5/layui/css/layui.css" media="all" rel="stylesheet" type="text/css"/>
</head>
<body>
<form:form id="searchForm" modelAttribute="userInfo" action="${ctx}/products/productsConfig/getUser" method="post" class="breadcrumb form-search">
    <div style="margin-left: 3%">
        <c:if test="${empty usernames}">
            <c:forEach items="${userList}" var="list">
                <p id="form"><input type="checkbox" name="item" value="${list.chName}(${list.userName})"><label>${list.chName}(${list.userName})</label></p><br/>
            </c:forEach>
        </c:if>
        <c:if test="${!empty usernames}">
            <c:forEach items="${usernames}" var="username">
                    <p id="form"><input type="checkbox" name="item" checked="checked" value="${username}"><label>${username}</label></p><br/>
            </c:forEach>
            <c:forEach items="${userList}" var="list">
                <p id="form"><input type="checkbox" name="item" value="${list}"><label>${list}</label></p><br/>
            </c:forEach>
        </c:if>
    </div>
</form:form>
</body>
</html>
