<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <base href="<%=basePath %>">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑数据分类</title>
    <link href="static/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="static/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="static/css/animate.css" rel="stylesheet">
    <link href="static/css/style.css" rel="stylesheet">
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="ibox float-e-margins">
        <div class="ibox-title">
            <h5>编辑数据分类</h5>
            <div class="ibox-tools">
                <a class="collapse-link">
                    <i class="fa fa-chevron-up"></i>
                </a>
                <a class="close-link">
                    <i class="fa fa-times"></i>
                </a>
            </div>
        </div>
        <div class="ibox-content">
            <form class="form-horizontal m-t" id="DataForm">
                <input type="hidden" name="dataId" id="dataId" value="${dataId }">
                <div class="form-group">
                    <label class="col-sm-3 control-label">数据分类名称：</label>
                    <div class="col-sm-7">
                        <input id="dataName" name="dataName" type="text" placeholder="请输入数据分类名称" class="form-control"
                               aria-required="true">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label required">资料大类：</label>
                    <div class="col-sm-7">
                        <select id="dataBigClass" name="dataBigClass" class="form-control" aria-required="true">
                            <option value="" selected>请选择资料大类</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label required">资料小类：</label>
                    <div class="col-sm-7">
                        <select id="dataSmallClass" name="dataSmallClass" class="form-control" aria-required="true">
                            <option value="" selected>请选择资料小类</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label required">服务对象：</label>
                    <div class="col-sm-7">
                        <input id="dataServiceObject" name="dataServiceObject" type="text" placeholder="请输入服务对象"
                               class="form-control" aria-required="true">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label required">单条数据量（字节）：</label>
                    <div class="col-sm-7">
                        <input id="dataSize" name="dataSize" type="text" placeholder="请输入单条数据量" class="form-control"
                               aria-required="true">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-9 col-sm-offset-3">
                        <button class="btn btn-w-m btn-primary" type="submit"><i class="fa fa-check"
                                                                                 aria-hidden="true"></i>&nbsp;保存
                        </button>
                        <button class="btn btn-w-m btn-primary" type="button" onclick="cancel()"><i class="fa fa-reply"
                                                                                                    aria-hidden="true"></i>&nbsp;取消
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- 全局js -->
<script src="static/js/jquery.min.js?v=2.1.4"></script>
<!-- jQuery Validation plugin javascript-->
<script src="static/js/plugins/validate/jquery.validate.min.js"></script>
<script src="static/js/plugins/validate/messages_zh.min.js"></script>
<script src="static/js/bootstrap.min.js?v=3.3.6"></script>
<!-- layer javascript -->
<script src="static/js/plugins/layer/layer.min.js"></script>
<!-- 自定义js -->
<script src="static/js/content.js?v=1.0.0"></script>
<script>
    $(document).ready(function () {
        dataInit();
        reset();
    });

    var dataInit = function () {
        $("#dataBigClass").change(function () {
            var dataBigClass = $(this).val();
            $.ajax({
                type: 'POST',
                async: false,
                url: 'data/getDataSmallClassList.shtml',
                data: {
                    "dataBigClass": dataBigClass
                },
                success: function (data) {
                    $("#dataSmallClass ").find("option").remove();
                    $("#dataSmallClass").append('<option  value="">请选择资料小类</option>');
                    for (var i = 0; i < data.length; i++) {
                        $("#dataSmallClass").append('<option  value="' + data[i] + '">' + data[i] + '</option>');
                    }
                },
                error: function () {
                    alert("请求失败！请刷新页面重试");
                },
                dataType: 'json'
            });
        });
    }

    var dataBigClassInit = function (dataBigClass) {
        $.ajax({
            type: 'POST',
            async: false,
            url: 'data/getDataBigClassList.shtml',
            data: {},
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    $("#dataBigClass").append('<option value="' + data[i] + '">' + data[i] + '</option>');
                }
                $("#dataBigClass").find("option[value=" + dataBigClass + "]").prop("selected", true);
            },
            error: function () {
                alert("请求失败！请刷新页面重试");
            },
            dataType: 'json'
        });
    }

    var dataSmallClassInit = function (dataBigClass) {
        $.ajax({
            type: 'POST',
            async: false,
            url: 'data/getDataSmallClassList.shtml',
            data: {
                "dataBigClass": dataBigClass
            },
            success: function (data) {
                $("#dataSmallClass ").find("option").remove();
                $("#dataSmallClass").append('<option  value="">请选择资料小类</option>');
                for (var i = 0; i < data.length; i++) {
                    $("#dataSmallClass").append('<option  value="' + data[i] + '">' + data[i] + '</option>');
                }
            },
            error: function () {
                alert("请求失败！请刷新页面重试");
            },
            dataType: 'json'
        });
    }

    var reset = function () {
        var dataId = $("#dataId").val();
        $.ajax({
            type: 'POST',
            async: false,
            url: 'data/getData.shtml',
            data: {
                dataId: dataId
            },
            success: function (data) {
                var data = data.data;
                dataBigClassInit(data.dataBigClass);
                dataSmallClassInit(data.dataBigClass);
                $("#dataName").val(data.dataName);
                // $("#dataBigClass").find("option[value=" + data.dataBigClass + "]").prop("selected", true);
                $("#dataSmallClass").find("option[value=" + data.dataSmallClass + "]").prop("selected", true);
                $("#dataServiceObject").val(data.dataServiceObject);
                $("#dataSize").val(data.dataSize);
            },
            error: function () {
                alert("请求失败！请刷新页面重试");
            },
            dataType: 'json'
        });
    }
    $.validator.setDefaults({
        highlight: function (element) {
            $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
        },
        success: function (element) {
            $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
        },
        errorElement: "span",
        errorPlacement: function (error, element) {
            if (element.is(":radio") || element.is(":checkbox")) {
                error.appendTo(element.parent().parent().parent());
            } else {
                error.appendTo(element.parent());
            }
        },
        errorClass: "help-block m-b-none",
        validClass: "help-block m-b-none"
    });
    $().ready(function () {
        var icon = "<i class='fa fa-times-circle'></i> ";
        $("#DataForm").validate({
            rules: {
                dataName: {
                    required: true,
                    maxlength: 50,
                    remote: {
                        type: 'POST',
                        cache: false,
                        url: 'data/IsDataExist.shtml',
                        data: {
                            dataId: function () {
                                return $("#dataId").val();
                            },
                            dataName: function () {
                                return $("#dataName").val();
                            }
                        }
                    }
                },
                dataBigClass: {
                    required: true,
                },
                dataSmallClass: {
                    required: true,
                },
                dataServiceObject: {
                    required: true,
                },
                dataSize: {
                    required: true,
                    digits: true,
                },
            },
            messages: {
                dataName: {
                    required: icon + "请输入数据分类名称",
                    maxlength: icon + "数据分类名称长度不能超过50",
                    remote: icon + ("数据分类名称已存在，请重新输入！")
                },
                dataBigClass: {
                    required: icon + "请选择资料大类",
                },
                dataSmallClass: {
                    required: icon + "请选择资料小类",
                },
                dataServiceObject: {
                    required: icon + "请输入服务对象",
                },
                dataSize: {
                    required: icon + "请输入单条数据量（字节）",
                    digits: icon + "请输入正确格式（数字）",
                },
            },
            submitHandler: function (form) {
                $.post("data/update.shtml", decodeURIComponent($(form).serialize(), true), function (data) {
                    var result = JSON.parse(data);
                    if (result.status == "success") {
                        layer.msg('更新成功', {
                            time: 2000,
                            icon: 6
                        });
                        setTimeout(function () {
                            location.href = "view/data/listUI.shtml";
                        }, 2000);
                    } else {
                        layer.msg(result.message, {icon: 2});
                    }
                });
            }
        });
    });
    var cancel = function () {
        location.href = "view/data/listUI.shtml";
    }
</script>
</body>
</html>