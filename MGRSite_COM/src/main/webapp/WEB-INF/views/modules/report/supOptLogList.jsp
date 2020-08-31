<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>查询日值管理</title>
    <meta name="decorator" content="default"/>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/report/logList/list">测站列表</a></li>
</ul>

<form:form id="searchForm" modelAttribute="reportLogInfo" action="${ctx}/report/logList/list" method="post" class="breadcrumb form-search" enctype="multipart/form-data">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <div style="margin-top:8px;">
        <label>操作类型：</label>
        <select id="opt_type">
            <option value="">全部</option>
            <c:choose>
                <c:when test="${optType=='1'}">
                    <option value="1" selected>入库</option>
                </c:when>
                <c:otherwise>
                    <option value="1" >入库</option>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${optType=='2'}">
                    <option value="2" selected>查询</option>
                </c:when>
                <c:otherwise>
                    <option value="2" >查询</option>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${optType=='3'}">
                    <option value="3" selected>下载</option>
                </c:when>
                <c:otherwise>
                    <option value="3" >下载</option>
                </c:otherwise>
            </c:choose>


        </select>
        <label>日期范围：&nbsp;</label><input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="Wdate"
                                         value="<fmt:formatDate value="${log.beginDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
        <label>&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="Wdate"
                                                                    value="<fmt:formatDate value="${log.endDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />&nbsp;&nbsp;

        &nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="searchData()"/>&nbsp;&nbsp;
    </div>
</form:form>
<tags:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed" showCheckbox="true" >
    <thead>
    <tr>
        <th>区站号</th>
        <th>操作类型</th>
        <th>数据对象大小</th>
        <th>数据类型</th>
        <th>操作地址</th>
        <th>更新时间</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="reportLogInfo">
        <tr>
            <td>
                ${reportLogInfo.stationInfo}
            </td>
            <td>
                <c:if test="${reportLogInfo.operitorType=='1'}">
                    入库
                </c:if>
                <c:if test="${reportLogInfo.operitorType=='2'}">
                    查询
                </c:if>
                <c:if test="${reportLogInfo.operitorType=='3'}">
                    下载
                </c:if>
            </td>
            <td>
                ${reportLogInfo.dataNum}
            </td>
            <td>
                <c:if test="${reportLogInfo.dataType=='1'}">
                    定时值
                </c:if>
                <c:if test="${reportLogInfo.dataType=='2'}">
                    日值
                </c:if>
                <c:if test="${reportLogInfo.dataType=='3'}">
                    旬值
                </c:if>
                <c:if test="${reportLogInfo.dataType=='4'}">
                    月值
                </c:if>
                <c:if test="${reportLogInfo.dataType=='5'}">
                    年值
                </c:if>
            </td>
            <td>
                    ${reportLogInfo.addr}
            </td>
            <td>
                    ${reportLogInfo.time}
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
<script type="text/javascript">

    function page(n,s){
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        var opt_type=$("#opt_type").val();
        var startTime=$("#beginDate").val();
        var endTime=$("#endDate").val();
        $("#searchForm").attr("action", "${ctx}/report/logList/list?start_time="+startTime+"&&end_time="+endTime+"&&opt_type="+opt_type);
        $("#searchForm").submit();
        return false;
    }
    function searchData(){
        var f = document.getElementsByTagName("form")[0];
        var startTime=$("#beginDate").val();
        var endTime=$("#endDate").val();
        var opt_type=$("#opt_type").val();
        f.action="${ctx}/report/logList/list?start_time="+startTime+"&&end_time="+endTime+"&&opt_type="+opt_type;
        f.submit();

    }
</script>
</body>
</html>