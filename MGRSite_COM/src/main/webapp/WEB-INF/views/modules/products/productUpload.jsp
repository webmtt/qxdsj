<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>静态资源管理</title>
    <meta name="decorator" content="default"/>
    <script src="${ctxStatic}/layui-v2.5.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
    <link href="${ctxStatic}/layui-v2.5.5/layui/css/layui.css" media="all" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        li{
            display:inline;   //一行显示
        float:left;    //靠左显示
        }
        td {
            white-space:nowrap;overflow:hidden;text-overflow: ellipsis;
        }
        table{

            table-layout:fixed;word-wrap:break-word;

        }
        #showbox {
            display: none;
            position: absolute;
            top: 25%;
            left: 45%;
            width: 25%;
            height: 30%;
            padding: 20px;
            border: 1px solid #69bef3;
            background-color: white;
            z-index:1002;
            overflow: auto;
        }
        .layui-btn {
            display: inline-block;
            height: 20px;
            line-height:10px;
            padding: 0 18px;
            background-color: #48afea;
            color: #fff;
            white-space: nowrap;
            text-align: center;
            font-size: 14px;
            border: none;
            border-radius: 2px;
            cursor: pointer;
        }
        a{
            color: #37a7e8;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function() {
            $("#searchForm").validate({
                /*submitHandler: function(form){
                    loading('正在提交，请稍等...');
                    form.submit();
                },*/
                errorContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        $(function(){
            function number(){
                for(var i=0;i< $(".numberClass").length;i++){
                    $(".numberClass").get(i).innerHTML = i+1;
                }
            }
            number();
        });
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/products/productsConfig">静态资源列表</a></li>
    <%--<li><a href="${ctx}/products/productsConfig/form">产品库修改</a></li>--%>
</ul>
<form:form id="searchForm" name ="searchForm" modelAttribute="productUpload" action="${ctx}/products/productsConfig/productUploadList" method="post" class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <button type="button" class="btn btn-primary" id="upload" style="margin-left: 0px">上传图片</button>
</form:form>
<tags:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th style="width: 10px;">编码</th>
        <th style="width: 200px;">静态图URL</th>
        <th style="width: 80px;">上传时间</th>
        <th style="width: 100px;">LINK</th>
        <th style="width: 100px;">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="productUpload">
        <tr>
            <td align="center"><span class="numberClass"></span></td>
            <td>
              ${productUpload.url}
            </td>
            <td>
                <fmt:formatDate value="${productUpload.creats}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <td>
                ${productUpload.link}
            </td>
            <td id="layerDemo">
                <a href="#" data-toggle="modal" data-target="#myModal" onclick="selectById('${productUpload.id}')">预览</a>
                &nbsp;&nbsp;<a href="${ctx}/products/productsConfig/formProductUp?id=${productUpload.id}">修改</a>
                &nbsp;&nbsp;<a href="${ctx}/products/productsConfig/deleteProductUp?id=${productUpload.id}" onclick="return confirmx('确认要删除该记录吗？', this.href)">删除</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header"
                 style="height: 30px;vertical-align:middle;padding-bottom: 2px;padding-top: 5px;">
                <table>
                    <tr>
                        <td><h4 class="modal-title" id="myModalLabel" style="float: left;width: 518px;">静态资源在线预览</h4>
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
            <div class="modal-body">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="closemodel()">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="pagination">${page}</div>

<script type="text/javascript">
    function selectById(id) {
        $.ajax({
            url: '${ctx}/products/productsConfig/getProductUpload?id='+id,
            type: 'GET',
            success: function (result) {
                $(".modal-body").empty();
                $(".modal-body").html(result.replace("\"","").replace("\"",""));
            }
        });
    }
    function closemodel() {
        $('#myModal').modal('hide');
    }
</script>
<script>
    layui.use('upload', function() {
        var $ = layui.jquery
            , upload = layui.upload;

        //普通图片上传
        var uploadInst = upload.render({
            elem: '#upload'
            , url: '${ctx}/products/productsConfig/upload'
            , done: function (res) {
            }
            , error: function () {
                layer.msg('上传成功');
                location.reload();
            }
        });
    });
</script>
</body>
</html>