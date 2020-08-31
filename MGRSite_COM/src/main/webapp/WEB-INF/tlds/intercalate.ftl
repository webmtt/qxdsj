<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8" />
    <link href="<%=request.getContextPath()%>/static/bootstrap/2.3.1/css_cerulean/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/static/common/jeesite.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/static/act/diagram-viewer/style.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/static/layui-v2.5.5/layui/css/layui.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/static/common/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/static/common/leftnav.css" rel="stylesheet" type="text/css"/>
    <script src="<%=request.getContextPath()%>/static/jquery/jquery-1.9.1.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="<%=request.getContextPath()%>/static/SuperSlide/jquery.SuperSlide.2.1.js" type="text/javascript" charset="utf-8"></script>
    <script src="<%=request.getContextPath()%>/static/layui-v2.5.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
    <script src="<%=request.getContextPath()%>/static/common/leftnav.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
        function page(n,s){
            if(n) $("#pageNo").val(n);
            if(s) $("#pageSize").val(s);
            return false;
        }
    </script>
</head>
<body>
<#--<div class="account-l fl" style="width:15%;height: 500px;float: left" >
    <ul id="accordion" class="accordion">
        <li>
            <div class="link"><i class="fa fa-star"></i>可视化展示分类<i class="fa fa-chevron-down"></i></div>
            <ul class="submenu">
            <#list page as user>
                <li id="publishpurchase"><a onclick="window.location='/visua/'"+${url}>配置一</a></li>
            </#list>
            </ul>
        </li>
    </ul>
</div>-->
<div class="slider">
    <div class="main" >
        <div class="main-i main-i-active">
            <img src="<%=request.getContextPath()%>/static/images/banner11.jpg"/>
        </div>
        <div class="main-i">
            <img src="<%=request.getContextPath()%>/static/images/banner2.jpg"/>
        </div>
        <div class="main-i">
            <img src="<%=request.getContextPath()%>/static/images/banner3.jpg"/>
        </div>
        <div class="main-i">
            <img src="<%=request.getContextPath()%>/static/images/banner4.jpg"/>
        </div>
        <div class="main-i">
            <img src="<%=request.getContextPath()%>/static/images/banner5.jpg"/>
        </div>
    </div>
    <div class ="main1">
        <div class="main1-i">
            <img src="<%=request.getContextPath()%>/static/images/banner5.jpg"/>
        </div>
    </div>

    <#--<div class="ctrl">
        <a class="ctrl-i ctrl-i-active" href="javascript:;">
            <img src="<%=request.getContextPath()%>/static/images/banner11.jpg"/>
        </a>
        <a class="ctrl-i" href="javascript:;">
            <img src="<%=request.getContextPath()%>/static/images/banner2.jpg"/>
        </a>
        <a class="ctrl-i" href="javascript:;">
            <img src="<%=request.getContextPath()%>/static/images/banner3.jpg"/>
        </a>
        <a class="ctrl-i" href="javascript:;">
            <img src="<%=request.getContextPath()%>/static/images/banner4.jpg"/>
        </a>
        <a class="ctrl-i" href="javascript:;">
            <img src="<%=request.getContextPath()%>/static/images/banner5.jpg"/>
        </a>
    </div>-->
    <div id = "tabledata">
        <blockquote class="layui-elem-quote layui-text">
            图片列表展示
        </blockquote>
        <table id="conTable" class="table table-striped table-bordered table-condensed">
            <thead><tr><th class="sort-column login_name">文件名</th><th class="sort-column name">文件大小(M)</th><th>文件创建时间</th><th>文件路径</th></thead>
            <tbody>
            <#list page as user>
            <#--<c:forEach items="${page.list}" var="user">-->
                <tr>
                    <td>${user.loginName}</td>
                    <td>${user.name}</td>
                    <td>${user.mobile}</td>
                    <td><a href="/sys/deploy/download?path=${user.loginIp}">${user.loginIp}</a></td>
                </tr>
            </#list>
            <#--</c:forEach>-->
            </tbody>
        </table>
        <#--<div class="pagination">${page}</div>-->
    </div>
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    </fieldset>
    <blockquote class="layui-elem-quote layui-text">
        文件列表展示
    </blockquote>
    <div id="fileMenu" style="width: 100%;height: 500px;">
        <div class="account-l fl" style="width:15%;height: 500px;float: left" >
            <div class="list-title">文件分类</div>
            <ul id="accordion" class="accordion">
                <li>
                    <div class="link"><i class="fa fa-star"></i>产品管理<i class="fa fa-chevron-down"></i></div>
                    <ul class="submenu">
                        <li id="shop"><a>查看店铺</a></li>
                        <li id="publicproducts"><a>发布产品</a></li>
                        <li id="productlists"><a>查看产品</a></li>
                        <li id="mysaled"><a>已卖出产品</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <div style="float: left;width: 2px;height: 500px;background: #ddd"></div>
        <div id = "tablelist" style="width:82%;height: 500px;overflow-y:scroll;float:right">
            <table id="listTable" class="table table-striped table-bordered table-condensed">
                <thead><tr><th class="sort-column login_name">文件名</th><th class="sort-column name">文件大小(M)</th><th>文件创建时间</th><th>文件路径</th></thead>
                <tbody>
                <#--<c:forEach items="${page1.list}" var="user1">-->
                <#list page1 as user1>
                    <tr>
                        <td>${user1.loginName}</td>
                        <td>${user1.name}</td>
                        <td>${user1.mobile}</td>
                        <td><a href="/sys/deploy/download?path=${user1.loginIp}">${user1.loginIp}</a></td>
                    </tr>
                <#--</c:forEach>-->
                </#list>
                </tbody>
            </table>
           <#-- <div class="pagination">${page1}</div>-->
        </div>
    </div>
    <div id = "tableFile">
        <table id="listTable" class="table table-striped table-bordered table-condensed">
            <thead><tr><th class="sort-column login_name">文件名</th><th class="sort-column name">文件大小(M)</th><th>文件创建时间</th><th>文件路径</th></thead>
            <tbody>
            <#list page1 as user1>
            <#--<c:forEach items="${page1.list}" var="user1">-->
                <tr>
                    <td>${user1.loginName}</td>
                    <td>${user1.name}</td>
                    <td>${user1.mobile}</td>
                    <td><a href="/sys/deploy/download?path=${user1.loginIp}">${user1.loginIp}</a></td>
                </tr>
            </#list>
            <#--</c:forEach>-->
            </tbody>
        </table>
        <#--<div class="pagination">${page1}</div>-->
    </div>
</div>

<script type="text/javascript">
    var a = ${photoType};
    var b = ${fileType};
    if(a === 1 && b===1){
        $('.main').show();
        $('.main1').hide();
        $('#tabledata').hide();
        $('#fileMenu').show();
        $('#tableFile').hide();
    }else if(a===2 && b===1){
        $('#tabledata').show();
        //$('.ctrl').hide();
        $('.main').hide();
        $('.main1').hide();
        $('#fileMenu').show();
        $('#tableFile').hide();
    }else if (a===0 && b===1) {
        //$('.ctrl').hide();
        $('.main').hide();
        $('#tabledata').hide();
        $('.main1').show();
        $('#fileMenu').show();
        $('#tableFile').hide();
    }else if (a===1 && b===0){
        $('.main').show();
        $('.main1').hide();
        $('#tabledata').hide();
        $('#fileMenu').hide();
        $('#tableFile').show();
    }else if (a===2 && b===0){
        $('#tabledata').show();
        //$('.ctrl').hide();
        $('.main').hide();
        $('.main1').hide();
        $('#fileMenu').hide();
        $('#tableFile').show();
    }else if(a===0 && b===0){
        //$('.ctrl').hide();
        $('.main').hide();
        $('#tabledata').hide();
        $('.main1').show();
        $('#fileMenu').hide();
        $('#tableFile').show();
    }
    var index=0,timer;

    $(".ctrl-i").click(function () {
        clearInterval(timer);
        index=$(this).index();
        slide (index);
        timerF();
    })

    $(document).ready(function () {
        timerF();
    })

    function timerF () {
        window.clearInterval(timer);
        timer=setInterval(function(){
            index++;
            if (index>=5) {
                index=0;
            }
            slide(index);
        },1500)
    }

    function slide (index) {
        $(".ctrl .ctrl-i").eq(index).addClass("ctrl-i-active").siblings().removeClass("ctrl-i-active");
        $(".main .main-i").eq(index).addClass("main-i-active");
        setTimeout(function(){
            $(".main .main-i").eq(index).siblings().removeClass("main-i-active");
        },500)
    }

    //鼠标移到图片上禁止滚动，鼠标移开开始滚动
    $(".main-i").hover(function () {
        clearInterval(timer);
    },function () {
        timerF();
    })
</script>
</body>

</html>
