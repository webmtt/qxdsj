<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>数据角色列表</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <style type="text/css">
        .table td i {
            margin: 0 2px;
        }
    </style>
    <script type="text/javascript">
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").attr("action", "${ctx}/userAndDataRole/list");
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<div>
    <form id="searchForm" modelAttribute="idOrName" action="${ctx}/dataRole/list" method="post"
          class="breadcrumb form-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div>
            <label>条件：</label><input name="idOrName" htmlEscape="false" maxlength="50" class="input-small"
                                     value="${idOrName}" placeholder="编号或名称"/>
            &nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
        </div>
    </form>
</div>
<div style="margin-top: 10px;">
    <table id="tablelist"
           class="table table-striped table-bordered table-condensed"
           style="word-break:break-all; word-wrap:break-word;showCheckbox:true;">
        <thead>
        <th>
            <input type="checkbox" class="checkbox" style="width: 10px" id="checkList" name="checkitem"/>
        </th>
        <th>角色编码</th>
        <th>角色名称</th>
        <th>描述</th>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="eudr">
            <tr>
                <th>
                    <input type="checkbox" class="checkbox" name="checkitem"/>
                </th>
                <td style="width: 20%; text-align: center;">${eudr.dataroleId}</td>
                <td style="width: 20%; text-align: center;">${eudr.dataroleName}</td>
                <td style="width: 20%; text-align: center;">${eudr.descriptionChn}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div class="pagination">${page}</div>
</body>
</html>