<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>文件管理</title>
    <meta name="decorator" content="default" charset="UTF-8"/>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
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
        });

        function changeFileName() {
            var t = $("#link1").val();
            var f = t.substring(t.lastIndexOf("\\") + 1);
            $("#dataName").val(f);
            //上传数据
            UpladFile();

        }

        //图片上传
        var xhr;

        //上传文件方法
        function UpladFile() {
            var fileObj = document.getElementById("link1").files[0]; // js 获取文件对象
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
        }

        //上传成功响应
        function uploadComplete(evt) {
            //服务断接收完文件返回的结果
            var data = evt.currentTarget.response;
            if (null != data || "" != data) {
                $("#link").val(data);
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
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/report/supDatafileinfo/list">文件列表</a></li>
    <li class="active">
        <a href="${ctx}/report/supDatafileinfo/form?id=${supDatafileinfo.id}">文件${not empty supDatafileinfo.id?'修改':'添加'}
        </a>
    </li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="supDatafileinfo" action="${ctx}/report/supDatafileinfo/save" method="post"
           class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">文件名称：</label>
        <div class="controls">
            <form:input path="dataName" htmlEscape="false" maxlength="50"
                        readonly="${not empty supDatafileinfo.id?true:false}" class="input-xlarge required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">上传文件：</label>
        <div class="controls">
            <form:hidden id="link" path="link" htmlEscape="true" maxlength="120" class="input-xlarge"/>
                <%--				<sys:ckfinder input="link" type="files" uploadPath="/report/supDatafileinfo" selectMultiple="true"/>--%>
                <%--				<span class="help-inline"><font color="red">*</font> </span>--%>
            <input id="link1" type="file" value="上传文件" onchange="changeFileName();"/>
            <br/>
            <progress id="progressBar" value="0" max="100" style="width: 300px;"></progress>
            <span id="percentage"></span><span id="time"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">文件类型：</label>
        <div class="controls">
            <form:select path="dataType" class="input-xlarge required">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('file_type')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">内容开始时间：</label>
        <div class="controls">
            <input name="starttimeCon" type="text" readonly="readonly" maxlength="20"
                   class="input-medium Wdate required"
                   value="<fmt:formatDate value="${supDatafileinfo.starttimeCon}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">内容结束时间：</label>
        <div class="controls">
            <input name="endtimeCon" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
                   value="<fmt:formatDate value="${supDatafileinfo.endtimeCon}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">备注信息：</label>
        <div class="controls">
            <form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="50" class="input-xxlarge "/>
        </div>
    </div>
    <div class="form-actions">
       <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>