<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>用户管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#myModal").hide();
            $("#dataroleModel").hide();
        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
        function selectMuiscInfoById(id, datarolename) {

            $.ajax({
                url: '${ctx}/musicuser/musicInfo/view',
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
            $(".close").click();
        }

        function showBigImg() {
            $("#myModal").show();
        }

    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/musicuser/musicInfo/list">用户列表</a></li>
    <li><a href="${ctx}/musicuser/musicInfo/form">用户添加</a></li>
</ul>
<form:form id="searchForm" modelAttribute="musicInfo" action="${ctx}/musicuser/musicInfo/list" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <div class="ul-form">
        <label>用户名：</label>
        <form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
    </div>
</form:form>
<tags:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>music用户</th>
        <th>工作单位</th>
        <th>更新者</th>
        <th>更新时间</th>
        <th>备注信息</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="musicInfo">
        <tr>
            <td>
                <label style="color:green"><a href="javascript:void(0);" data-toggle="modal" data-target="#dataroleModel"
                                              onclick="selectMuiscInfoById('${musicInfo.id}','${musicInfo.name}')">${musicInfo.name}</a></label>

            </td>
            <td>
                    ${musicInfo.orgid}
            </td>
            <td>
                  ${fns:getUserById(musicInfo.updateBy.id).name}
            </td>
            <td>
                <fmt:formatDate value="${musicInfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <td>
                    ${musicInfo.remarks}
            </td>
            <td>
                <a href="${ctx}/musicuser/musicInfo/form?id=${musicInfo.id}">修改</a>
                <a href="${ctx}/musicuser/musicInfo/delete?id=${musicInfo.id}"
                   onclick="return confirmx('确认要删除该用户吗？', this.href)">删除</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="modal fade" id="dataroleModel" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" style="width: 632px">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header"
                 style="height: 30px;vertical-align:middle;padding-bottom: 2px;padding-top: 5px;">
                <table>
                    <tr>
                        <td><h4 class="modal-title" id="myModalLabel" style="float: left;width: 591px;"></h4>
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
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="closeDataRoleModel()">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="pagination">${page}</div>
</body>
</html>