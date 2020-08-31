<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/dialog.jsp" %>
<html>
<head>
    <title>添加资料</title>
    <meta name="decorator" content="default"/>
    <script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
    <script src="${ctxStatic}/ckeditor/ckeditor.js" type="text/javascript"></script>
    <style type="text/css">
        .sort {
            color: #0663A2;
            cursor: pointer;
        }

        table th, table td {
            text-align: center !important;
            vertical-align: middle !important;
        }

        .searchurl {
            display: none;
        }

        #msg {
            color: red;
        }

        .errorLabel {
            background: rgba(0, 0, 0, 0) url("${ctxStatic}/jquery-validation/1.11.1/images/unchecked.gif") no-repeat scroll 0 0;
            background-position: 0 2px;
            color: #ea5200;
            font-weight: bold;
            margin-left: 10px;
            padding-bottom: 2px;
            padding-left: 18px;
        }
    </style>
    <script type="text/javascript">
        var ctx = "${ctx}";
        var ctxStatic = "${ctxStatic}";
        var imageurl = "${dataDef.imageurl}";
        var serverUrl = "${imgServerUrl}";
        var pageType = '${pageType}';
        var dataclasscode = "";
        var pidName = '${pidName }';
        var categoryid = '${categoryid}';
        var pid = '${pid}';
        var datas = "";
        $(function () {
            $.validator.addMethod("chinese", function (value, element, param) {
                //方法中又有三个参数:value:被验证的值， element:当前验证的dom对象，param:参数(多个即是数组)
                //alert(value + "," + $(element).val() + "," + param[0] + "," + param[1]);  ^[^\u4e00-\u9fa5]{0,}$  ^([a-z_A-Z-.+0-9]+)$
                return new RegExp("^[^\u4e00-\u9fa5]{0,}$").test(value);

            }, "不能填写中文，请填写英文字母、数字或_");
            $.validator.addMethod("english", function (value, element, param) {
                //方法中又有三个参数:value:被验证的值， element:当前验证的dom对象，param:参数(多个即是数组)
                //alert(value + "," + $(element).val() + "," + param[0] + "," + param[1]);  ^[^\u4e00-\u9fa5]{0,}$  ^([a-z_A-Z-.+0-9]+)$
                return new RegExp("^([a-z_A-Z-.+0-9]+)$").test(value);

            }, "只能填写英文字母、数字或_");


            ininCkeditor();
            checkoptionmetagroup();
            if (pageType == '0') {
                $("#addType").show();
                $("#detailList").hide();
                $("#titleBox").hide();
            } else {
                $("#addType").hide();
                $("#detailList").show();
                $("#titleBox").show();
            }
            var arr = ["#insertmode", "#servicemode"];
            var subarr = ["insert", "service"];
            if (imageurl == undefined || imageurl == "") {
                $("#imageurl_IMG").hide();
            } else {
                $("#imageurl_IMG").show();
            }
            for (var i = 0; i < arr.length; i++) {
                var result = [];
                var fualttype = parseInt($(arr[i]).val()).toString(2);
                if (fualttype != "") {
                    result = fualttype.split("").reverse();
                    for (var j = 0; j < result.length; j++) {
                        if (result[j] == 1) {
                            $("input[name=" + subarr[i] + "]").eq(j).prop("checked", true);
                        } else {
                            $("input[name=" + subarr[i] + "]").eq(j).prop("checked", false);
                        }
                    }
                }
            }
            $("#inputForm").validate({
                rules: {
                    datacode: {
                        english: true
                    },
                    insert: {
                        required: true
                    }
                },
                messages: {
                    insert: {
                        required: '请选择接入方式'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    if (element.is(':radio') || element.is(':checkbox')) { //如果是radio或checkbox
                        var eid = element.attr('name'); //获取元素的name属性
                        error.appendTo(element.parent()); //将错误信息添加当前元素的父结点后面
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
            $("#dataclasscode").change(function () {
                ininCkeditor();
            });
            $("#userrankid1").prop("checked", "true");
        });

        function hideCk() {
            var datacode = $("#datacode").val();
            if (datacode == undefined || datacode == "") {
                $("#descriptionInfo").show();
                $("#description").hide();
            } else {
                $("#descriptionInfo").hide();
                $("#description").show();
            }
        }

        function ininCkeditor() {
            dataclasscode = $("#dataclasscode option:selected").val();
            datas = $("#datacode").val();
            CKEDITOR.config.filebrowserImageUploadUrl = "/mgrsite/dataService/imgUplad?datacode=" + datas + "&dataclasscode=" + dataclasscode;
        }

        function showSearchurl() {
            $(".searchurl").show();
        }

        function hideSearchurl() {
            $("#searchurl").val("");
            $(".searchurl").hide();
        }

        function check() {
            var pid = $("#pid").val();
            var datacode = $("#datacode").val();
            var dataclasscode = $("#dataclasscode option:selected").val();
            var dataService = $("#dataService").val();
            var dataService = $("#dataServiceId").val();
            var pidStr = $("#pid").val();
            var cataId = $("#categoryid").val();
            var arr = ["#insertmode", "#servicemode"];
            var subarr = ["insert", "service"];
            for (var i = 0; i < subarr.length; i++) {
                var fualttype = 0;
                $("input[name=" + subarr[i] + "]:checked").each(function () {
                    fualttype += parseInt($(this).val());
                });
                $(arr[i]).val(fualttype);
            }
            var optionmetagroup = "";
            $("input[name=optionmetagroup1]:checked").each(function () {
                optionmetagroup += $(this).val() + ",";
            });
            if (optionmetagroup != "") {
                optionmetagroup = optionmetagroup.substring(0, optionmetagroup.length - 1);
            }
            $("#optionmetagroup").val(optionmetagroup);
            var insert = $("#insertmode").val();
            var service = $("#servicemode").val();
            if (pid == undefined || pid == "") {
                var returnCode = "";
                if (dataService == "" || dataService == undefined) {
                    $("#father label").remove();
                    $("#father").append("<label class=\"errorLabel\">必填信息</label>");
                    alert("请选择父节点");
                    return false;
                } else {
                    $("#father label").remove();
                    var treeId = $("#dataServiceId").val();
                    var datacode = $("#datacode").val();
                    $.ajax({
                        url: ctx + "/dataService/checkDataCode",
                        type: "post",
                        async: false,
                        data: {
                            datacode: datacode,
                            id: treeId
                        },
                        dataType: "text",
                        success: function (result) {
                            $("#msg").empty();
                            $("#btnSubmit").removeAttr("disabled");
                            returnCode = result;
                            if (result == "ok") {
                                /*  $("#inputForm").attr("action",ctx+"/dataService/saveDataDef");
                                 $("#inputForm").submit(); */
                                /*    return true; */
                                $("#udatacode").attr("value", datacode);
                            } else {
                                $("#msg").text("资料代码已存在！请重新输入！");
                                $("#btnSubmit").attr("disabled", "disabled");
                                alert("录入信息有误，请检查");
                                /*  return false; */
                            }
                        }, error: function (result) {
                            return false;
                        }
                    });
                    if (returnCode == "ok") {
                        $("#udatacode").attr("value", datacode);
                        return true;
                    } else {
                        return false;
                    }
                    /* return true; */
                }
                if (insert == undefined || insert == "") {
                    return false;
                }
                if (service == undefined || service == "") {
                    return false;
                }
            } else if (/.*[\u4e00-\u9fa5]+.*$/.test(datacode)) {
                return false;
            }
        }

        function checkCode() {
            var url = ctx + "/dataService/checkDataCode";
            var datacode = $("#datacode").val();
            datas = datacode;
            ininCkeditor();
            var pidStr = $("#pid").val();
            var cataId = $("#categoryid").val();
            if (pidStr.length == 0 && cataId.length == 0) {
                $("#msg").empty();
                $("#btnSubmit").removeAttr("disabled");
                /*   	var treeId=$("#dataServiceId").val();
                      if(treeId.length>0){
                          $.ajax({
                              url : url,
                              type : "post",
                              async : false,
                              data : {
                                  datacode:datacode,
                                  id:treeId
                              },
                              dataType:"text",
                              success : function(result) {
                                  $("#msg").empty();
                                     $("#btnSubmit").removeAttr("disabled");
                                 if(result=="ok"){
                                 }else{
                                   $("#msg").text("资料代码已存在！请重新输入！");
                                   $("#btnSubmit").attr("disabled","disabled");
                                   return false;
                                 }
                              },error : function(result){
                                  return false;
                              }
                              });
                      }else{
                          $("#msg").attr("请先选择父节点");
                      } */
            } else {
                $.ajax({
                    url: url,
                    type: "post",
                    async: false,
                    data: {
                        datacode: datacode,
                        id: categoryid
                    },
                    dataType: "text",
                    success: function (result) {
                        $("#msg").empty();
                        $("#btnSubmit").removeAttr("disabled");
                        if (result == "ok") {
                            $("#udatacode").attr("value", datacode);
                        } else {
                            $("#msg").text("资料代码已存在！请重新输入！");
                            $("#btnSubmit").attr("disabled", "disabled");
                            return false;
                        }
                    }, error: function (result) {
                        return false;
                    }
                });
            }


        }

        function chekbox() {
            var datacode = $("#datacode").val();
            if (datacode == undefined || datacode == "") {
                alert("请先填写资料代码");
                $("#datacode").focus();
                return false;
            } else {
                return true;
            }
        }

        function openwin() {
            parent.open(ctx + "/dataService/showPage");
        }

        function checkoptionmetagroup() {
            var ab = $("input[name=optionmetagroup1]");
            for (var j = 0; j < ab.length; j++) {
                $("." + ab.eq(j).val()).hide();
            }
            $("[name = optionmetagroup1]:checkbox").bind("click", function () {
                if ($(this).attr("checked") == "checked") {
                    $("." + $(this).val()).show();
                } else {
                    $("." + $(this).val()).hide();
                }
            })
        }

        function imgUpload(icon) {
            $("#imgMsg").empty();
            var imgPath = icon.value;
            if (/.*[\u4e00-\u9fa5]+.*$/.test(imgPath)) {
                alert("文件路径不能含有汉字！");
                $("#" + icon.id).val();
                return false;
            } else {
                var id = icon.id;
                var imgDiv = "";
                var dataclasscode = $("#dataclasscode option:selected").val();
                var datacode = $("#datacode").val();
                $.ajaxFileUpload({
                    url: ctx + "/dataService/imgUpload",
                    secureuri: false,
                    fileElementId: id,//file标签的id
                    data: {'imgDiv': id, 'dataclasscode': dataclasscode, 'datacode': datacode},
                    dataType: 'json',//返回数据的类型
                    success: function (data, status) {
                        if (data.returnCode == "0") {
                            imgDiv = data.imgDiv;
                            imgDiv = imgDiv.substring(0, parseInt(imgDiv.length) - 1);
                            $("#" + imgDiv).val(data.imgUrl);
                            $("#" + imgDiv + "_IMG img").attr("src", serverUrl + data.imgUrl);
                            $("#" + imgDiv + "_IMG").show();
                        } else {
                            /* 	alert("上传图片失败"); */
                            $("#imgMsg").css("color", "red");
                            $("#imgMsg").text("上传图片失败");
                        }
                    },
                    error: function (data, status, e) {
                        $("#flag").attr("value", "上传失败");
                        // alert("上传文件大小超过${maxUploadSize}M，请联系管理员上传！");
                        //这里处理的是网络异常，返回参数解析异常，DOM操作异常
                        /*   alert("上传发生异常");  */
                    }
                });
                return true;
            }


        }

        //上传文件
        function addFile(file) {
            var datacode = $("#datacode").val();
            var dataclasscode = $("#dataclasscode option:selected").val();
            var uploadfile = $("#file_0").val();
            if ("" == uploadfile || null == uploadfile) {
                alert("请选择要上传的文件");
                return;
            }
            var upload = document.getElementById("file_0").files;
            var uploadFileName = upload[0].name;
            var uploadtype = uploadFileName.substring(uploadFileName.lastIndexOf("."), uploadFileName.size);
            if (uploadtype != ".doc" && uploadtype != ".DOC") {
                alert("文档格式不正确！");
                return false;
            }
            $.ajaxFileUpload
            (
                {
                    url: '${ctx}/dataService/upload',
                    secureuri: false,
                    data: {datacode: datacode, uploadFileName: uploadFileName},
                    fileElementId: 'file_0',
                    dataType: 'json',
                    success: function (data, status) {
                        if (data.message == '0') {
                            CKEDITOR.instances['chndescription'].setData(restoreHtmlEntity(data.msg));
                        }
                    },
                    error: function (data, status, e)//服务器响应失败处理函数
                    {
                        alert(e + "错误");
                    }

                }
            );
        }

        function restoreHtmlEntity(htmlText) {
            var text = htmlText;
            text = text.replace(/&lt;/g, '<');
            text = text.replace(/&gt;/g, '>');
            text = text.replace(/&amp;/g, '&');
            return text;
        }
    </script>
</head>

<body>

<ul class="nav nav-tabs">
    <li id="detailList"><a
            href="${ctx}/dataService/getDataDetail?pid=${pid}&categoryid=${categoryid}">${chName}-数据资料列表</a></li>
    <li class="active"><a href="javascript:void(0)">新增数据资料</a></li>
</ul>
<%--<form:form id="titleBox" method="post" class="breadcrumb form-search">--%>
<%--    <span style="font-weight: bold;">当前大类：</span>${pidName }--${chName }--%>
<%--</form:form>--%>
<tags:message content="${message}"/>
<form:form id="inputForm" modelAttribute="dataDef" action="${ctx}/dataService/saveDataDef" method="post"
           enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
    <form:hidden path="insertmode" id="insertmode"/>
    <form:hidden path="servicemode" id="servicemode"/>
    <input type="hidden" name="pid" id="pid" value="${pid}">
    <input type="hidden" name="categoryid" id="categoryid" value="${categoryid}"/>
    <input type="hidden" name="dataendflag" id="dataendflag" value="0">
    <form:hidden path="optionmetagroup" id="optionmetagroup"/>
    <form:hidden path="udatacode" id="udatacode"/>
    <form:hidden path="imageurl" id="imageurl"/>
    <div class="control-group">
        <label class="control-label"><a style="color: red;">*</a>父节点：</label>
        <div id="father" class="controls">
            <c:choose>
                <c:when test="${empty categoryid}">
                    <tags:treeselect id="dataService" name="treeNodeId" value="" labelName="treeNodeName"
                                     labelValue="${dataCategoryDef.chnname}"
                                     title="数据服务菜单" url="/dataService/getdataServiceTreeList" extId=""/>
                </c:when>
                <c:otherwise>
                    <label>${chName}</label>
                </c:otherwise>
            </c:choose>
                <%-- <tags:treeselect id="dataService" name="treeNodeId" value="" labelName="treeNodeName" labelValue="${dataCategoryDef.chnname}"
                             title="数据服务菜单" url="/dataService/getdataServiceTreeList"  extId=""  cssClass="required"/> --%>
            <input id="openWin" class="btn btn-primary" type="button" value="查看示例" onclick="openwin()"/>
        </div>
    </div>
    <%--    <div class="control-group">
           <label class="control-label">查询接口资料代码：</label>
           <div class="controls">
               <form:input path="udatacode" id="udatacode" htmlEscape="false" cssStyle="width:350px;"/>
               <label style="color: red;">遵循CIMISS接口编码规范</label>
           </div>
       </div> --%>
<%--    <div class="control-group">--%>
<%--        <label class="control-label"><a style="color: red;">*</a>资料大类编码：</label>--%>
<%--        <div class="controls">--%>
<%--                &lt;%&ndash; <form:hidden path="dataclasscode" id="dataclasscode" htmlEscape="false" cssStyle="width:350px;"/> &ndash;%&gt;--%>
<%--            <form:select path="dataclasscode" id="dataclasscode" items="${dataDic}" itemLabel="chNName"--%>
<%--                         itemValue="dataClassCode">--%>
<%--            </form:select>--%>
<%--        </div>--%>
<%--    </div>--%>
    <%--    <div class="control-group">--%>
    <%--        <label class="control-label">用户级别ID：</label>--%>
    <%--        <div class="controls">--%>
    <%--            <form:radiobuttons path="userrankid" name="userrankid" items="${fns:getDictList('userrankid')}"--%>
    <%--                               itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
    <%--            <label style="color: red;">默认：进行ftp下载，国家局用户：进行检索下载</label>--%>
    <%--        </div>--%>
    <%--    </div>--%>
    <div class="control-group">
        <label class="control-label"><a style="color: red;">*</a>排序号：</label>
        <div class="controls">
            <form:input path="orderno" id="orderno" htmlEscape="false" cssStyle="width:350px;" class="required digits"/>
        </div>
    </div>

    <%-- <c:if test="${!empty dataDef.imageurl}"> --%>

    <%-- </c:if> --%>


    <div class="control-group">
        <label class="control-label" style="font-weight:bold;font-size:16px;">元数据基本信息>></label>
    </div>
    <div class="control-group">
        <label class="control-label"><a style="color: red;">*</a>数据集名称：</label>
        <div class="controls">
            <form:input path="chnname" id="chnname" htmlEscape="false" cssStyle="width:350px;" class="required"/>
        </div>
    </div>
<%--    <div class="control-group">--%>
<%--        <label class="control-label"><a style="color: red;">*</a>数据集链接：</label>--%>
<%--        <div class="controls">--%>
<%--            <form:input path="chUrl" id="chUrl" htmlEscape="false" cssStyle="width:350px;" class="required"/>--%>
<%--        </div>--%>
<%--    </div>--%>
    <div class="control-group">
        <label class="control-label"><a style="color: red;">*</a>数据集代码：</label>
        <div class="controls">
            <form:input path="datacode" id="datacode" htmlEscape="false" cssStyle="width:350px;" class="required"
                        onblur="checkCode()"/><span id="msg"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">更新频率：</label>
        <div class="controls">
            <form:input path="updatefreq" id="updatefreq" htmlEscape="false" cssStyle="width:350px;"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">制作时间：</label>
        <div class="controls">
            <form:input path="producetime" id="producetime" htmlEscape="false" cssStyle="width:350px;"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">空间分辨率：</label>
        <div class="controls">
            <form:input path="spatialresolution" id="spatialresolution" htmlEscape="false" cssStyle="width:350px;"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">参考系：</label>
        <div class="controls">
            <form:input path="refersystem" id="refersystem" htmlEscape="false" cssStyle="width:350px;"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">关键字：</label>
        <div class="controls">
            <form:input path="keywords" id="keywords" htmlEscape="false" cssStyle="width:350px;"/><span
                style="color: red;">多个关键字请以“;”分隔</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">发布时间：</label>
        <div class="controls">
            <form:input path="publishTime" id="publishTime"
                        onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" htmlEscape="false"
                        class="input-medium Wdate"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" style="font-weight:bold;font-size:16px;">元数据详细说明>></label>
    </div>
<%--    <div class="control-group">--%>
<%--        <label class="control-label">上传文件：</label>--%>
<%--        <div class="controls">--%>
<%--            <div id="filebox" style="display:inline;">--%>
<%--                <input type="file" id="file_0" name="upload">--%>
<%--            </div>--%>
<%--            <input type="button" value="上传" onclick="addFile(this);"><span style="color:red;">(只能上传doc文件)</span>--%>
<%--        </div>--%>
<%--    </div>--%>
    <div class="control-group" id="concheck">
        <label class="control-label">资料中文描述：</label>
        <div class="controls">
            <div id="description">
						<textarea name="chndescription" id="chndescription" class="input-xlarge" htmlEscape="true">
                                ${dataDef.chndescription}</textarea>
                <tags:ckeditor replace="chndescription" height="150"></tags:ckeditor>

            </div>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" style="font-weight:bold;font-size:16px;">垂向覆盖范围>>
            <input type="checkbox" name="optionmetagroup1" value="VERTICALSCOPE"/></label>
    </div>
    <div class="VERTICALSCOPE">
        <div class="control-group">
            <label class="control-label">垂向最低：</label>
            <div class="controls">
                <form:input path="verticallowest" id="verticallowest" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">垂向最高：</label>
            <div class="controls">
                <form:input path="verticalhighest" id="verticalhighest" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">垂向度量单位：</label>
            <div class="controls">
                <form:input path="verticalunit" id="verticalunit" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">垂向基准名称：</label>
            <div class="controls">
                <form:input path="verticalbase" id="verticalbase" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" style="font-weight:bold;font-size:16px;">地理覆盖范围>>
            <input type="checkbox" name="optionmetagroup1" value="GEOSCOPE"/></label>
    </div>
    <div class="GEOSCOPE">
        <div class="control-group">
            <label class="control-label">地理范围描述：</label>
            <div class="controls">
                <form:input path="areascope" id="areascope" htmlEscape="false" cssStyle="width:350px;"/><span
                    style="color: red;">多个区域用“,”隔开</span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">最西经度：</label>
            <div class="controls">
                <form:input path="westlon" id="westlon" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">最东经度：</label>
            <div class="controls">
                <form:input path="eastlon" id="eastlon" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">最北纬度：</label>
            <div class="controls">
                <form:input path="northlat" id="northlat" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">最南纬度：</label>
            <div class="controls">
                <form:input path="southlat" id="southlat" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" style="font-weight:bold;font-size:16px;">时间覆盖范围>>
            <input type="checkbox" name="optionmetagroup1" value="TIMESCOPE"/></label>
    </div>
    <div class="TIMESCOPE">
        <div class="control-group">
            <label class="control-label">起始时间：</label>
            <div class="controls">
                <form:input path="databegintime" id="databegintime" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">终止时间：</label>
            <div class="controls">
                <form:input path="dataendtime" id="dataendtime" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">观测或统计频次：</label>
            <div class="controls">
                <form:input path="obsfreq" id="obsfreq" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" style="font-weight:bold;font-size:16px;">联系方法>>
            <input type="checkbox" name="optionmetagroup1" value="LINKINFO"/></label>
    </div>
    <div class="LINKINFO">
        <div class="control-group">
            <label class="control-label">负责人：</label>
            <div class="controls">
                <form:input path="producer" id="producer" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">负责单位：</label>
            <div class="controls">
                <form:input path="productionunit" id="productionunit" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">(负责人)联系信息：</label>
            <div class="controls">
                <form:input path="contactinfo" id="contactinfo" htmlEscape="false" cssStyle="width:350px;"/>
            </div>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" style="font-weight:bold;font-size:16px;">其他相关信息>></label>
    </div>
    <div class="control-group">
        <label class="control-label"><a style="color: red;">*</a>存储类型：</label>
        <div class="controls">
            <form:radiobutton class="radio-input" path="storagetype" name="storagetype" value="0"
                              onclick="hideSearchurl()" checked="checked"/><label
                style="margin-right: 30px;">要素型数据</label>
            <form:radiobutton class="radio-input" path="storagetype" name="storagetype" value="1"
                              onclick="hideSearchurl()"/><label style="margin-right: 30px;">文件型数据</label>
            <form:radiobutton class="radio-input" path="storagetype" name="storagetype" value="2"
                              onclick="showSearchurl()"/><label style="margin-right: 30px;">其他(外接链接)</label>

        </div>
    </div>
    <div class="control-group searchurl">
        <label class="control-label">检索Url：</label>
        <div class="controls">
            <form:input path="searchurl" id="searchurl" htmlEscape="false" cssStyle="width:350px;"/>
        </div>
    </div>
    <!--
    <div class="control-group">
    <label class="control-label"><a style="color: red;">*</a>接入方式：</label>
    <div class="controls">
    <input class="radio-input" name="insert" type="checkbox" value="1" checked="checked"/><label>外接数据源</label>
    <input class="radio-input" name="insert" type="checkbox" value="2"/><label>流程接入</label>
    <input class="radio-input" name="insert" type="checkbox" value="4"/><label>手工上传接入</label>
    </div>
    </div>
    -->
    <%--    <div class="control-group">--%>
    <%--        <label class="control-label"><a style="color: red;">*</a>服务方式：</label>--%>
    <%--        <div class="controls">--%>
    <%--            <input class="radio-input" name="service" type="checkbox" value="1" checked="checked"/><label>数据检索</label>--%>
    <%--            <!----%>
    <%--         <input class="radio-input" name="service" type="checkbox"  value="2"/><label>数据订阅</label>--%>
    <%--         <input class="radio-input" name="service" type="checkbox"  value="4"/><label>FTP下载</label>-->--%>
    <%--            <input class="radio-input" name="service" type="checkbox" value="8"/><label>MUSIC接口服务</label>--%>
    <%--            <!----%>
    <%--           <input class="radio-input" name="service" type="checkbox"  value="16"/><label>TDS接口服务</label>--%>
    <%--           <input class="radio-input" id="serviceLast" name="service" type="checkbox"  value="32"/><label>存储情况</label>--%>
    <%--           -->--%>
    <%--        </div>--%>
    <%--    </div>--%>
    <%--    <div class="control-group">--%>
    <%--        <label class="control-label">数据源编码：</label>--%>
    <%--        <div class="controls">--%>
    <%--            <form:radiobutton path="datasourcecode" value="CMISS" checked="true"/><label>CMISS</label>--%>
    <%--            <form:radiobutton path="datasourcecode" value="MDSS"/><label>MDSS</label>--%>
    <%--            <form:radiobutton path="datasourcecode" value="MARS"/><label>MARS</label>--%>
    <%--            <form:radiobutton path="datasourcecode" value="MARS"/><label>CDC</label>--%>
    <%--            <form:radiobutton path="datasourcecode" value="MARS"/><label>IDATA</label>--%>
    <%--            <label style="color: red;">(默认选择CMISS)</label>--%>
    <%--        </div>--%>
    <%--    </div>--%>
    <%--    <div class="control-group">--%>
    <%--        <label class="control-label"><a style="color: red;">*</a>有无检索定制分项：</label>--%>
    <%--        <div class="controls">--%>
    <%--            <form:radiobutton class="radio-input" path="isincludesub" name="isincludesub" value="0"--%>
    <%--                              checked="checked"/><label style="margin-right: 30px;">无</label>--%>
    <%--            <form:radiobutton class="radio-input" path="isincludesub" name="isincludesub" value="1"/><label--%>
    <%--                style="margin-right: 30px;">有</label>--%>
    <%--            <label style="color: red;">(有检索定制分项即多个检索，无检索定制分项即单个检索！)</label>--%>
    <%--        </div>--%>
    <%--    </div>--%>
    <div class="control-group">
        <label class="control-label"><a style="color: red;">*</a>是否有效：</label>
        <div class="controls">
            <form:radiobutton class="radio-input" path="invalid" name="invalid" value="0" checked="checked"/><label
                style="margin-right: 30px;">是</label>
            <form:radiobutton class="radio-input" path="invalid" name="invalid" value="1"/><label
                style="margin-right: 30px;">否</label>
        </div>
    </div>

    <div class="control-group">
        <div class="controls">
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="提交"/>
            <input id="btnCancel" class="btn" type="button" value="取消" onclick="history.go(-1)"/>
        </div>
    </div>
</form:form>
</body>
</html>

