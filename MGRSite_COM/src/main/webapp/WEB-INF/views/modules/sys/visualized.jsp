<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8" />
    <link href="${ctxStatic}/bootstrap/2.3.1/css_cerulean/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/common/jeesite.css" rel="stylesheet" type="text/css"/>
    <%--<link href="${ctxStatic}/act/diagram-viewer/style.css" rel="stylesheet" type="text/css"/>--%>
    <link href="${ctxStatic}/layui-v2.5.5/layui/css/layui.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/common/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/common/leftnav.css" rel="stylesheet" type="text/css"/>
    <script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxStatic}/SuperSlide/jquery.SuperSlide.2.1.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxStatic}/layui-v2.5.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxStatic}/common/leftnav.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>
<form:form id="inputForm" modelAttribute="uuid" action="${ctx}/sys/deploy/visualized?id=${uuid}" method="post" class="form-horizontal">
<div class="account-l fl" style="width:14%;height: 500px;float: left" >
    <ul id="accordion" class="accordion">
        <li>
            <div class="link"><i class="fa fa-star"></i>可视化展示分类<i class="fa fa-chevron-down"></i></div>
            <ul class="submenu">

    <c:forEach items="${list}" var="url">
        <c:if test="${fn:contains(url,'00')}">
                <li id="${url}"><a onclick=method('${url}')>配置一</a></li>
        </c:if>
        <c:if test="${fn:contains(url,'01')}">
            <li id="${url}"><a onclick=method('${url}')>配置二</a></li>
        </c:if>
        <c:if test="${fn:contains(url,'10')}">
            <li id="${url}"><a onclick=method('${url}')>配置三</a></li>
        </c:if>
        <c:if test="${fn:contains(url,'11')}">
            <li id="${url}"><a onclick=method('${url}')>配置四</a></li>
        </c:if>
        <c:if test="${fn:contains(url,'20')}">
            <li id="${url}"><a onclick=method('${url}')>配置五</a></li>
        </c:if>
        <c:if test="${fn:contains(url,'21')}">
            <li id="${url}"><a onclick=method('${url}')>配置六</a></li>
        </c:if>
    </c:forEach>
            </ul>
        </li>
    </ul>
</div>
    <div id="rightSe" style="float: right;width:85%;height: 100%;"></div>
</form:form>

</body>
<script type="text/javascript">
    $("a:first").click();
    function method(name) {
        $("#rightSe").empty();
        $("#rightSe").load('${ctx}'+name);
    }
</script>
</html>
