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
            width: 100%;
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
    <li class="active"><a href="">产品清单配置</a></li>
</ul>
<%--<form class="layui-form" id="form">--%>
<form:form id="searchForm" name ="searchForm" modelAttribute="productRepert" action="${ctx}/productRepert/list" method="post" class="breadcrumb form-search">
<blockquote class="layui-elem-quote layui-text">
    天气实况产品
</blockquote>
        <input type="text" id="alerteunit" value="${productRepert.MSP1}" name="title" lay-verify="title" autocomplete="off" placeholder="请输入天气实况产品，用逗号隔开" class="layui-input"><br/>
<blockquote class="layui-elem-quote layui-text">
    基础预报产品
</blockquote>
        <input type="text" id="alertetype" value="${productRepert.MSP2}" name="title" lay-verify="title" autocomplete="off" placeholder="请输入基础预报产品，用逗号隔开" class="layui-input"><br/>
<blockquote class="layui-elem-quote layui-text">
    气象服务产品
</blockquote>
        <input type="text" id="alerteproduct" value="${productRepert.MSP3}" name="title" lay-verify="title" autocomplete="off" placeholder="请输入气象服务产品，用逗号隔开" class="layui-input"><br/>
</form:form>

<input id="btnnSubmit" class="btn btn-primary" type="submit" value="保 存" style="margin-left: 1000px;margin-top: 15px"/>&nbsp;
<%--</form>--%>
<script>
    $(function () {
        $('#btnnSubmit').click(function () {
            var MSP1 = $("#alerteunit").val();
            var MSP2 = $("#alertetype").val();
            var MSP3 = $("#alerteproduct").val();
            var datas = JSON.stringify({"MSP1": MSP1, "MSP2": MSP2, "MSP3": MSP3});
            //ajax提交
            $.ajax({
                async : false,
                url: '${ctx}/productRepert/saveConfig',
                type: 'post',
                dataType : 'json',
                contentType : 'application/json',
                data:datas,
                success: function (data) {
                    if (data==1){
                        layui.use('layer', function(){
                            layer.msg("产品清单配置保存成功", {
                                time: 3000,
                                end: function () {
                                    var index = parent.layer.getFrameIndex(window.name);
                                    parent.layer.close(index);
                                }
                            });
                        })
                    }else{
                        layui.use('layer', function(){
                            layer.msg("产品清单配置保存失败", {
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
