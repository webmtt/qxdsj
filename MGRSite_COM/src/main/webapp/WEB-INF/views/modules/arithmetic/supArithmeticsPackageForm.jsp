<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>算法管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            document.getElementById("chufashijian").style.display = "none";
            $("#name").focus();
            var idValue = document.getElementsByName("id")[0].value;
            $("#ariName").blur(function () {
                var dateValue = $("#ariName").val();
                console.log($("#ariName").val())
                $.ajax({
                    type: "post",
                    url: "${ctx}/arithmetic/supArithmeticsPackage/findObjectByAirName",
                    data: {
                        airName: dateValue
                    },
                    success: function (res) {
                        if (idValue == "" || idValue == null) {
                            if (res != "") {
                                $("#ariName").css("background-color", "red");
                                $("#btnSubmit").attr("disabled", true);
                                document.getElementById("chufashijian").style.display = "";
                            } else {
                                $("#ariName").css("background-color", "");
                                $("#btnSubmit").attr("disabled", false);
                                document.getElementById("chufashijian").style.display = "none";
                            }
                        } else {
                            if (res.id != idValue && res != "") {
                                $("#ariName").css("background-color", "red");
                                $("#btnSubmit").attr("disabled", true);
                                document.getElementById("chufashijian").style.display = "";
                            } else {
                                $("#ariName").css("background-color", "");
                                $("#btnSubmit").attr("disabled", false);
                                document.getElementById("chufashijian").style.display = "none";
                            }
                        }
                    }
                });
            })
            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });

            $("#addTr").click(function () {
                var uuid = Math.random();
                var uuids = Math.random();
                var uuidsq = Math.random();
                var trTemp = $("<tr id='" + uuid + "'></tr>");
                trTemp.append("<td><input class='btn btn-primary' type='button' value='清除' onclick='clearData(this)'></td>");
                trTemp.append('<td><input id=' + uuids + ' type="text" htmlEscape="false" maxlength="255" class="input-xlarge paramKey required"/></td>');
                trTemp.append('<td><input id=' + uuidsq + ' type="text" htmlEscape="false" maxlength="255" class="input-xlarge paramValue " </td>');
                trTemp.appendTo("#myTb");
            });
        });

        function clearData(obj) {
            var index = $(obj).parents("tr").index(); //这个可获取当前tr的下标 未使用
            $(obj).parents("tr").remove(); //实现删除tr
        }

        //文件上传
        var xhr;

        //上传文件方法
        function UpladFile() {
            var fileObj = document.getElementById("link1").files[0]; // js 获取文件对象
            if (fileObj.name.toString().endsWith("jar")) {
                var url = "${ctx}/report/supDatafileinfo/fileUpload"; // 接收上传文件的后台地址
                var form = new FormData(); // FormData 对象
                form.append("file", fileObj); // 文件对象
                xhr = new XMLHttpRequest();  // XMLHttpRequest 对象
                xhr.open("post", url, true); //post方式，url为服务器请求地址，true 该参数规定请求是否异步处理。
                xhr.onload = uploadComplete; //请求完成
                xhr.onerror = uploadFailed; //请求失败
                xhr.upload.onprogress = progressFunction;//【上传进度调用方法实现】
                xhr.upload.onloadstart = function () {//上传开始执行方法
                    ot = new Date().getTime();   //设置上传开始时间
                    oloaded = 0;//设置上传开始时，以上传的文件大小为0
                };
                xhr.send(form); //开始上传，发送form数据
            } else {
                alert("文件类型有误!")
            }
        }

        //上传成功响应
        function uploadComplete(evt) {
            //服务断接收完文件返回的结果
            var data = evt.currentTarget.response;
            var t = $("#link1").val();
            var f = t.substring(t.lastIndexOf("\\") + 1, t.length);
            if (null != data || "" != data) {
                $("#link").val(data + f);
            } else {
                alert("上传失败！");
            }

        }

        //上传失败
        function uploadFailed(evt) {
            alert("上传失败！");
        }

        //取消上传
        function cancleUploadFile() {
            xhr.abort();
        }


        //上传进度实现方法，上传过程中会频繁调用该方法
        function progressFunction(evt) {
            var progressBar = document.getElementById("progressBar");
            var percentageDiv = document.getElementById("percentage");
            // event.total是需要传输的总字节，event.loaded是已经传输的字节。如果event.lengthComputable不为真，则event.total等于0
            if (evt.lengthComputable) {//
                progressBar.max = evt.total;
                progressBar.value = evt.loaded;
                percentageDiv.innerHTML = Math.round(evt.loaded / evt.total * 100) + "%";
            }
            var time = document.getElementById("time");
            var nt = new Date().getTime();//获取当前时间
            var pertime = (nt - ot) / 1000; //计算出上次调用该方法时到现在的时间差，单位为s
            ot = new Date().getTime(); //重新赋值时间，用于下次计算
            var perload = evt.loaded - oloaded; //计算该分段上传的文件大小，单位b
            oloaded = evt.loaded;//重新赋值已上传文件大小，用以下次计算
            //上传速度计算
            var speed = perload / pertime;//单位b/s
            var bspeed = speed;
            var units = 'b/s';//单位名称
            if (speed / 1024 > 1) {
                speed = speed / 1024;
                units = 'k/s';
            }
            if (speed / 1024 > 1) {
                speed = speed / 1024;
                units = 'M/s';
            }
            speed = speed.toFixed(1);
            //剩余时间
            var resttime = ((evt.total - evt.loaded) / bspeed).toFixed(1);
            time.innerHTML = '，速度：' + speed + units + '，剩余时间：' + resttime + 's';
            if (bspeed == 0) time.innerHTML = '上传已取消';
        }

        function onbox(uuids) {
            top.$.jBox($("#importBox").html(), {
                title: "算法添加案例：", buttons: {"确认": "ok"}
            })
        }

        function baocun() {
            var tbody = document.getElementById('myTb');
            var rows = tbody.rows;
            var lists = [];
            for (var i = 0; i < rows.length; i++) {//遍历行
                var paramKey = document.getElementsByClassName("paramKey")[i].value;
                var paramValue = document.getElementsByClassName("paramValue")[i].value;
                var a = paramKey +":"+paramValue;
                lists.push(a);
            }
            $("#purpose").val(lists);
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/arithmetic/supArithmeticsPackage/">算法列表</a></li>
    <li class="active"><a
            href="${ctx}/arithmetic/supArithmeticsPackage/form?id=${supArithmeticsPackage.id}">算法${not empty supArithmeticsPackage.id?'修改':'添加'}
    </a></li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="supArithmeticsPackage" action="${ctx}/arithmetic/supArithmeticsPackage/save"
           method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <tags:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">算法名称：</label>
        <div class="controls">
            <form:input id="ariName" path="ariName" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
        <div class="controls" id="chufashijian">
            <label style="color: red">算法名重复</label>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">算法包路径：</label>
        <div class="controls">
            <form:input id="link" path="ariPackageName" htmlEscape="true" maxlength="240"
                        class="input-xlarge required"/><br>
            <input type="button" value="查看案例" onclick="onbox()"/>
            <input id="link1" type="file" value="上传文件" onchange="UpladFile()"/>
            <br/>
            <progress id="progressBar" value="0" max="100" style="width: 300px;"></progress>
            <span id="percentage"></span><span id="time"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">算法类路径：</label>
        <div class="controls">
            <form:input path="classUrl" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">方法名：</label>
        <div class="controls">
            <form:input path="ariMethod" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">算法参数：</label>
        <div class="controls">
            <form:hidden id="purpose" path="purpose" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
            <table id="contentTable" class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>
                    <th><input id="addTr" class="btn btn-primary" type="button" value="新增"/></th>
                    <th>参数名称</th>
                    <th>参数说明</th>
                </tr>
                </thead>

                <c:if test="${empty supArithmeticsPackage.id}">
                    <tbody id="myTb">

                    </tbody>
                </c:if>
                <c:if test="${not empty supArithmeticsPackage.id}">
                    <script type="text/javascript">
                        $(document).ready(function () {
                            var lists = new Array();
                            for (var i = 0; i < document.getElementsByName("purpose")[0].value.split(",").length; i++) {//遍历行
                                var a = document.getElementsByName("purpose")[0].value.split(",")[i];
                                lists.push({
                                    paramKey: a.split(":")[0],
                                    paramValue: a.split(":")[1],
                                });
                            }
                            for (var i = 0; i < lists.length; i++) {
                                var uuid = Math.random();
                                var uuids = Math.random();
                                var trTemp = $("<tr id='" + uuid + "'></tr>");
                                trTemp.append("<td><input class='btn btn-primary' type='button' value='清除' onclick='clearData(this)'></td>");
                                trTemp.append('<td><input id=' + uuids + ' value=' + lists[i].paramKey + ' type="text" htmlEscape="false" class="input-xlarge paramKey required"/></td>');
                                trTemp.append('<td><input type="text" value=' + lists[i].paramValue + ' htmlEscape="false"  class="input-xlarge paramValue"/></td>');
                                trTemp.appendTo("#myTb");
                            }
                        })
                    </script>
                    <tbody id="myTb">

                    </tbody>
                </c:if>

            </table>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">备注信息：</label>
        <div class="controls">
            <form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="50" class="input-xxlarge "/>
        </div>
    </div>
    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary"
               type="submit"
               value="保 存" onclick="baocun()"/>&nbsp;
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
<div id="importBox" class="hide">
    <label style="color: red">
        &nbsp;&nbsp;&nbsp;算法名称：判断是否有空字符<br>
        &nbsp;&nbsp;&nbsp;算法包路径：commons-lang-2.6.jar<br>
        &nbsp;&nbsp;&nbsp;算法类路径：org.apache.commons.lang3.StringUtils<br>
        &nbsp;&nbsp;&nbsp;方法名：isBlank<br>
        &nbsp;&nbsp;&nbsp;算法参数：判断是否有空字符<br>
        &nbsp;&nbsp;&nbsp;备注信息：判断是否有空字符<br>
    </label>
</div>
</body>
</html>