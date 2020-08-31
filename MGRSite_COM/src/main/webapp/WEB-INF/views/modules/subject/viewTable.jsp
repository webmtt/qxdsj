<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/dialog.jsp" %>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title></title>
    <style type="text/css">
        tr{
            margin-top: 3px;
        }
    </style>
</head>
<body>
<div style="margin-top: 10px;">
    <table id="tablelist"
           class="table table-striped table-bordered table-condensed"
           style="word-break:break-all; word-wrap:break-word;">
        <thead>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="tableinfo">
            <tr>
                <td>
                        ${tableinfo.columnName}
                </td>
                <td>
                        ${tableinfo.dataType}
                </td>
                <td>
                        ${tableinfo.characterMaximumLength}
                </td>
                <td>
                        ${tableinfo.columnComment}
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
