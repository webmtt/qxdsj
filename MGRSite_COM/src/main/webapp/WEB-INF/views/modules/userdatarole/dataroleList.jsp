<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>数据角色列表</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
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

        function selectDataroleById(id, datarolename) {
            $.ajax({
                url: '${ctx}/dataRole/viewDataRole',
                type: 'POST',
                data: {'id': id},
                dataType: 'html',
                success: function (result) {
                    $("#dataroleInfo").empty();
                    $("#dataroleInfo").html(result);
                }
            });
            $("#myModalLabel").html(datarolename + "信息");
        }

        function closeDataRoleModel() {
            $('#dataroleModel').modal('hide');
        }


    </script>
</head>
<body>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="javascript:void(0)">数据角色列表</a></li>
        <li><a href="${ctx}/dataRole/addDataRole">添加数据角色</a></li>
    </ul>
    <form id="searchForm" modelAttribute="idOrName" action="${ctx}/dataRole/list" method="post"
          class="breadcrumb form-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div>
            <label>条件：</label><input name="idOrName" htmlEscape="false" maxlength="50" class="input-small"
                                     value="${idOrName}" placeholder="角色编号或名称"/>
            &nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
        </div>
    </form>
</div>
<tags:message content="${message}"/>
<div style="margin-top: 10px;">
    <table id="tablelist"
           class="table table-striped table-bordered table-condensed"
           style="word-break:break-all; word-wrap:break-word;">
        <thead>
<%--        <th>角色编号</th>--%>
        <th>角色名称</th>
        <th>描述</th>
        <th>序号</th>
        <th style="text-align: center;">操作</th>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="eudr">
            <tr>
<%--                <td style="width: 20%; text-align: center;">${eudr.dataroleId}</td>--%>
                <td style="width: 20%; text-align: center;">${eudr.dataroleName}</td>
                <td style="width: 20%; text-align: center;">${eudr.descriptionChn}</td>
                <td style="width: 20%; text-align: center;">${eudr.orderNo}</td>
                <td style="width: 20%; text-align: center;">
                    <a href="javascript:void(0);" data-toggle="modal" data-target="#dataroleModel"
                       onclick="selectDataroleById('${eudr.dataroleId}','${eudr.dataroleName}')">查看</a>&nbsp;&nbsp;
                    <a href="${ctx}/interdata/interfaceData/interfam?id=${eudr.dataroleId}">API授权</a>&nbsp;&nbsp;
                    <%--<a href="${ctx}/sys/deploy/getdeploy?id=${eudr.dataroleId}">配置</a>&nbsp;&nbsp;--%>
                    <a href="${ctx}/dataRole/updateDataRole?id=${eudr.dataroleId}">修改</a>&nbsp;&nbsp;
                    <c:if test="${eudr.orderNo==0?false:true}">
                        <a href="${ctx}/dataRole/delete?id=${eudr.dataroleId}">删除</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div class="pagination">${page}</div>
<div class="modal fade" id="dataroleModel" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" style="width: 350px">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header"
                 style="height: 30px;vertical-align:middle;padding-bottom: 2px;padding-top: 5px;">
                <table>
                <tr>
                    <td><h4 class="modal-title" id="myModalLabel" style="float: left;width: 309px;"></h4>
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
            <div class="modal-body" id="dataroleInfo">
            </div>
            <div class="modal-footer" style="width: auto">
                <button type="button" class="btn btn-primary" onclick="closeDataRoleModel()">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    if ($("#messageBox").length > 0) {
        $("#messageBox").show();
    }
</script>
</body>
</html>