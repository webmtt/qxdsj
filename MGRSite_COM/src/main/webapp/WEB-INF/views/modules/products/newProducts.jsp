<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>产品库配置</title>
    <meta name="decorator" content="default"/>
    <script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/layui-v2.5.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
    <link href="${ctxStatic}/layui-v2.5.5/layui/css/layui.css" media="all" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        input[type="text"]{
            display: block;
            width: 99%;
            padding-left: 10px;
            height: 38px;
            border-width: 1px;
            border-style: solid;
            background-color: #fff;
            border-radius: 2px;
            outline: 0;
            -webkit-appearance: none;
            transition: all .3s;
            -webkit-transition: all .3s;
            box-sizing: border-box;
        }
        .layui-form-label{
            font-size:15px;
        }
    </style>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="">产品库配置</a></li>
</ul>
<%--<form class="layui-form" id="form">--%>
    <blockquote class="layui-elem-quote layui-text">
        最新预警产品
    </blockquote>
<form:form id="searchForm" name ="searchForm" modelAttribute="newproducts1" action="${ctx}/newProducts/list" method="post" class="breadcrumb form-search">
    <div class="layui-form-item">
        <label class="layui-form-label">制作单位</label>
        <div class="layui-input-block">
            <input type="text" id="alerteunit" value="${newproducts1.unit}" name="title" lay-verify="title" autocomplete="off" placeholder="请输入制作单位编码，用逗号隔开" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">业务门类</label>
        <div class="layui-input-block">
            <input type="text" id="alertetype" value="${newproducts1.types}" name="title" lay-verify="title" autocomplete="off" placeholder="请输入业务门类编码，用逗号隔开" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">产品子类</label>
        <div class="layui-input-block">
            <input type="text" id="alerteproduct" value="${newproducts1.product}" name="title" lay-verify="title" autocomplete="off" placeholder="请输入产品子类编码，用逗号隔开" class="layui-input">
        </div>
    </div>
</form:form>
<form:form id="searchForm" name ="searchForm" modelAttribute="newproducts2" action="${ctx}/newProducts/list" method="post" class="breadcrumb form-search">
    <blockquote class="layui-elem-quote layui-text">
        最新预报产品
    </blockquote>
    <div class="layui-form-item">
        <label class="layui-form-label">制作单位</label>
        <div class="layui-input-block">
            <input type="text" id="forecastunit" value="${newproducts2.unit}" name="title" lay-verify="title" autocomplete="off" placeholder="请输入制作单位编码，用逗号隔开" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">业务门类</label>
        <div class="layui-input-block">
            <input type="text" id="forecasttype" value="${newproducts2.types}" name="title" lay-verify="title" autocomplete="off" placeholder="请输入业务门类编码，用逗号隔开" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">产品子类</label>
        <div class="layui-input-block">
            <input type="text" id="forecastproduct" value="${newproducts2.product}" name="title" lay-verify="title" autocomplete="off" placeholder="请输入产品子类编码，用逗号隔开" class="layui-input">
        </div>
    </div>
</form:form>
    <input id="btnnSubmit" class="btn btn-primary" type="submit" value="保 存" style="margin-left: 1000px;margin-top: 15px"/>&nbsp;
<%--</form>--%>
<script>
    $(function () {
        $('#btnnSubmit').click(function () {
            var alerteunit = $("#alerteunit").val();
            var alertetype = $("#alertetype").val();
            var alerteproduct = $("#alerteproduct").val();
            var forecastunit = $("#forecastunit").val();
            var forecasttype = $("#forecasttype").val();
            var forecastproduct = $("#forecastproduct").val();
            var datas = JSON.stringify({"alerteunit": alerteunit, "alertetype": alertetype, "alerteproduct": alerteproduct, "forecastunit": forecastunit, "forecasttype": forecasttype, "forecastproduct": forecastproduct});
            //ajax提交
            $.ajax({
                async : false,
                url: '${ctx}/newProducts/saveConfig',
                type: 'post',
                dataType : 'json',
                contentType : 'application/json',
                data:datas,
                   success: function (data) {
                       if (data==1){
                           layui.use('layer', function(){
                           layer.msg("最新产品配置保存成功", {
                               time: 3000,
                               end: function () {
                                   var index = parent.layer.getFrameIndex(window.name);
                                   parent.layer.close(index);
                               }
                           });
                           })
                       }else{
                           layui.use('layer', function(){
                           layer.msg("最新产品配置保存失败", {
                               time: 3000,
                               end: function () {
                                   var index = parent.layer.getFrameIndex(window.name);
                                   parent.layer.close(index);
                               }
                           });
                           })
                       }

                   }
            })
        })
    })

</script>
</body>
</html>
