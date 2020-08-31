<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>文件管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").attr("action", "${ctx}/report/supDatafileinfo/list");
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/report/supDatafileinfo/list">文件列表</a></li>
   <li><a href="${ctx}/report/supDatafileinfo/form">文件添加</a></li>

</ul>
<form id="searchForm" modelAttribute="supDatafileinfo" action="${ctx}/report/supDatafileinfo/" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

        <div><label style="width: 110px">内容开始时间：</label>
            <input name="starttimeCon" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${supDatafileinfo.starttimeCon}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
            <label style="width: 110px">内容结束时间：</label>
            <input name="endtimeCon" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${supDatafileinfo.endtimeCon}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
            &nbsp; &nbsp; &nbsp; &nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
        </div>

</form>
<tags:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>文件名称</th>
        <th>存储路径</th>
        <th>文件类型</th>
        <th>内容开始时间</th>
        <th>内容结束时间</th>
        <th>状态</th>
        <th>更新者</th>
        <th>更新时间</th>
        <th>备注信息</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="supDatafileinfo">
        <tr>
            <td><a href="${ctx}/report/supDatafileinfo/form?id=${supDatafileinfo.id}">
                    ${supDatafileinfo.dataName}
            </a></td>
            <td>
                    ${supDatafileinfo.link}
            </td>
            <td>
                    ${fns:getDictLabel(supDatafileinfo.dataType, 'file_type', '')}
            </td>
            <td>
                <fmt:formatDate value="${supDatafileinfo.starttimeCon}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <td>
                <fmt:formatDate value="${supDatafileinfo.endtimeCon}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>

            <td>
                    ${fns:getDictLabel(supDatafileinfo.updateBy.id, 'state_type', '')}
            </td>
            <td>
                    ${fns:getDictLabel(supDatafileinfo.updateBy.id, 'sys_user_type', '')}
            </td>
            <td>
                <fmt:formatDate value="${supDatafileinfo.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <td>
                    ${supDatafileinfo.remarks}
            </td>
            <td>
                    <a href="${ctx}/report/supDatafileinfo/form?id=${supDatafileinfo.id}">修改</a>
                    <a href="${ctx}/report/supDatafileinfo/delete?id=${supDatafileinfo.id}"
                       onclick="return confirmx('确认要删除该文件吗？', this.href)">删除</a>
                    <a href="${ctx}/report/supDatafileinfo/loadData?path=${supDatafileinfo.link}&filename=${supDatafileinfo.dataName}">入库</a>
             </td>

        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>