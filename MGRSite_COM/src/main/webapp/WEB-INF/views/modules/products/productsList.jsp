<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>产品库管理</title>
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
    <li class="active"><a href="${ctx}/products/productsConfig">产品库列表</a></li>
    <%--<li><a href="${ctx}/products/productsConfig/form">产品库修改</a></li>--%>
</ul>
<form:form id="searchForm" name ="searchForm" modelAttribute="products" action="${ctx}/products/productsConfig" method="post" class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <div class="ul-form">
        <li><label>名称：</label>
            <form:input path="prodname" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li><label>用户：</label>
            <form:input path="username" htmlEscape="false" maxlength="20" class="input-medium"/>
        </li>
        <li><label>创建时间：</label>
            <input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${products.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> -
            <input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${products.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
        </li>
        <li class="btns" style="margin-left: 40px;"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
        <%--<button type="button" class="btn btn-primary" id="upload" style="margin-left: 120px">上传图片</button>--%>
        <button type="button" class="btn btn-primary" id="addProducts" onClick="window.open('${url}#/product/create')" style="margin-left: 5px;">新增产品</button>
        <%--<a href="javascript:" >新增产品</a>--%>
        <li class="clearfix"></li>
    </div>
</form:form>
<tags:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th style="width: 10px;">编码</th>
        <th style="width: 120px;">名称</th>
        <th style="width: 160px;">产品库要素信息</th>
        <th style="width: 150px;">授权用户</th>
        <th style="width: 200px;">URL地址</th>
        <th style="width: 100px;">创建时间</th>
        <th style="width: 120px;">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="products">
        <tr>
            <td align="center"><span class="numberClass"></span></td>
            <td>
                    ${products.prodname}
            </td>
            <td>
                <SPAN>${products.product}</SPAN>
            </td>
            <td>
                    <SPAN>${products.username}</SPAN>
            </td>
            <td>
                    <SPAN>${products.url}</SPAN>
            </td>
            <td>
                <fmt:formatDate value="${products.create}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <td id="layerDemo">
                <a href="${ctx}/products/productsConfig/form?id=${products.id}">修改</a>
                &nbsp;&nbsp;<a href="${ctx}/products/productsConfig/delete?id=${products.id}" onclick="return confirmx('确认要删除该记录吗？', this.href)">删除</a>
                <c:if test="${fns:getUser().name=='管理员'}">
                &nbsp;&nbsp;<a href="javascript:" onClick="window.open('${url}#/product/create?id=${products.id}')">布局管理</a>
                </c:if>
                &nbsp;&nbsp;<a href="javascript:" onclick="getAllQuser('${products.url}','${products.id}')">授权</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
<div id="showbox" onMouseOver="this.style.display='block';" onMouseOut="this.style.display='none';"></div>

<script type="text/javascript">
    $(function() {
        function showBox(obj, box) {
            var timer = null;
            $(obj).on("mouseover", function(e) {
                clearTimeout(timer);
                var clientX = e.clientX;
                var clientY = e.clientY;
                var txt = $(this).text();
                timer = setTimeout(function() {
                    $(box).css("left", clientX).css("top", clientY);
                    if (txt == "") {
                        $(box).hide();
                    } else {
                        $(box).show();
                        $(box).html(txt);
                    }
                }, 500);
            });
            $(obj).on("mouseout", function() {
                clearTimeout(timer);
                $(box).hide();
            });
        }
        showBox("#contentTable > tbody td SPAN", "#showbox");
    });
</script>
<script>
    function getAllQuser(url,id){
                layui.use('layer', function(){ //独立版的layer无需执行这一句
                    var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
                    //触发事件
                    //多窗口模式，层叠置顶
                    layer.open({
                        type: 2 //此处以iframe举例
                        ,title: '选择用户'
                        ,area: ['500px', '300px']
                        ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
                        ,shade:0
                        ,maxmin: true
                        ,closeBtn: false
                        ,offset: [150,1000]
                        ,content:'${ctx}/products/productsConfig/getUser?id='+id
                        ,btn: ['确定', '关闭']
                        ,yes: function(index, layero){
                            var usernames = $.makeArray(layero.find('iframe').contents().find(':checkbox[name="item"]:checked').map(function(){
                                return this.value;
                            })).join(',');
                            console.log(usernames);
                            if(usernames == "" || usernames == null || usernames == undefined){
                                layer.msg("请选择需要授权的人员");
                            }else{
                                $.ajax({
                                    url: '${ctx}/products/productsConfig/saveUser',
                                    type: 'get',
                                    data: {
                                        usernames: usernames,
                                        url: url,
                                        id:id
                                    },
                                    success: function () {
                                        location.reload();
                                    }
                                });

                                layer.closeAll();
                                layer.msg("授权成功");
                            }
                        }
                        ,zIndex: layer.zIndex //重点1
                        ,success: function(layero){
                            layer.setTop(layero); //重点2
                        }
                    });

                    $('#layerDemo .layui-btn').on('click', function(){
                        var othis = $(this), method = othis.data('method');
                        active[method] ? active[method].call(this, othis) : '';
                    });

                });
            /*}*/
            /*})*/
    };

    layui.use('upload', function() {
        var $ = layui.jquery
            , upload = layui.upload;

        //普通图片上传
        var uploadInst = upload.render({
            elem: '#upload'
            , url: '${ctx}/products/productsConfig/upload' //改成您自己的上传接口
            , done: function (res) {
            }
            , error: function () {
                layer.msg('上传成功');
            }
        });
    });
</script>
</body>
</html>