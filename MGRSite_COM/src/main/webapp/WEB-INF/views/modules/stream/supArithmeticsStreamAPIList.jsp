<%@ page import="java.net.InetAddress" %>
<%@ page import="java.net.SocketException" %>
<%@ page import="java.net.NetworkInterface" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.net.Inet4Address" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>算法池接口</title>
    <meta name="decorator" content="default"/>
    <style type="text/css">
        li {
            display: inline;
        / / 一行显示 float: left;
        / / 靠左显示
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            var domain = document.domain;

            //获取当前网址，
            var curPath = window.document.location.href;
            //获取主机地址之后的目录，
            var pathName = window.document.location.pathname;
            var pos = curPath.indexOf(pathName);
            //获取主机地址
            var localhostPaht = curPath.substring(0, pos);
            //获取带"/"的项目名，
            var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);


            console.log(location.host);

        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }


    </script>
    <%
        Enumeration<NetworkInterface> nis;
        String ip = null;
        try {
            nis = NetworkInterface.getNetworkInterfaces();
            for (; nis.hasMoreElements();) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                for (; ias.hasMoreElements();) {
                    InetAddress ia = ias.nextElement();
                    //ia instanceof Inet6Address && !ia.equals("")
                    if (ia instanceof Inet4Address && !ia.getHostAddress().equals("127.0.0.1")) {
                        ip = ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    %>
<%--    <%--%>
<%--        String ip = request.getHeader("x-forwarded-for");--%>
<%--        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {--%>
<%--            ip = request.getHeader("Proxy-Client-IP");--%>
<%--        }--%>
<%--        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {--%>
<%--            ip = request.getHeader("WL-Proxy-Client-IP");--%>
<%--        }--%>
<%--        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {--%>
<%--            ip = request.getHeader("HTTP_CLIENT_IP");--%>
<%--        }--%>
<%--        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {--%>
<%--            ip = request.getHeader("HTTP_X_FORWARDED_FOR");--%>
<%--        }--%>
<%--        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {--%>
<%--            ip = request.getRemoteAddr();--%>
<%--        }--%>
<%--    %>--%>
    <%
        String path = request.getContextPath();
        InetAddress addr = InetAddress.getLocalHost();
        byte[] ipAddr = addr.getAddress();
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i] & 0xFF;
        }
        String basePath = request.getScheme() + "://" + ipAddrStr + ":" + request.getServerPort() + path + "/";
        String ports = ":" + request.getServerPort() + path + "/";
    %>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/stream/supArithmeticsStreamAPI">算法池接口列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="supArithmeticsStream" action="${ctx}/stream/supArithmeticsStreamAPI"
           method="post" class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>算法池名称：</label>
            <form:input path="streamName" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<tags:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>算法池名称</th>
        <th>接口地址</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="supArithmeticsStream">
        <tr>
            <td><a href="${ctx}/stream/supArithmeticsStream/form?id=${supArithmeticsStream.id}">
                    ${supArithmeticsStream.streamName}
            </a></td>
                <%--                <td>--%>
                <%--                   <%=basePath%>stream/supArithmeticsStream/common?streamId=${supArithmeticsStream.id}&object=${supArithmeticsStream.purpose}--%>
                <%--                </td>--%>
            <td>
                http://<%=ip%><%=ports%>stream/supArithmeticsStream/common?streamId=${supArithmeticsStream.id}&meter=&object=
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
<div>
    <label style="color: red;font-size: 20px">API调用说明：</label><br>
    <label style="color: #0e90d2;font-size: 15px">调用地址案例如：http://<%=ip%><%=ports%>
        stream/supArithmeticsStream/common?streamId=40c7ce552a24bed81cd387136484259&object=2,3<br>
        其中：http://<%=ip%><%=ports%>stream/supArithmeticsStream/common为通用的接口地址
        streamId和object为接口参数，streamId为一个算法池的编号，object为要处理的数据。
    </label>
</div>
</body>
</html>