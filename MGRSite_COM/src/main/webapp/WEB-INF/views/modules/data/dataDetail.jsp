<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/dialog.jsp" %>
<html>
<head>
    <title>编辑资料</title>
    <meta name="decorator" content="default"/>
    <script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
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

        .serviceModel {
            display: none;
        }

    </style>
    <script type="text/javascript">
        var ctx = "${ctx}";
        var ctxStatic = "${ctxStatic}";
        var dataclasscode = "";
        var imgurl = '${dataDef.imageurl}';
        var datas = "";
        $(function () {
            $.validator.addMethod("english", function (value, element, param) {
                //方法中又有三个参数:value:被验证的值， element:当前验证的dom对象，param:参数(多个即是数组)
                //alert(value + "," + $(element).val() + "," + param[0] + "," + param[1]);  ^[^\u4e00-\u9fa5]{0,}$  ^([a-z_A-Z-.+0-9]+)$
                return new RegExp("^([a-z_A-Z-.+0-9]+)$").test(value);

            }, "只能填写英文字母、数字或_");

            hideCk();
            ininCkeditor();
            if (imgurl == "" || imgurl == undefined) {
                $("#iconImg_IMG").hide();
            }
            var arr = ["#insertmode", "#servicemode"];
            var subarr = ["insert", "service"];
            for (var i = 0; i < arr.length; i++) {
                var result = [];
                var fualttype = parseInt($(arr[i]).val()).toString(2);
                if (fualttype != "") {
                    result = fualttype.split("").reverse();
                    for (var j = 0; j < result.length; j++) {
                        if (result[j] == 1) {
                            $("input[name=" + subarr[i] + "]").eq(j).attr("checked", true);
                        } else {
                            $("input[name=" + subarr[i] + "]").eq(j).attr("checked", false);
                        }
                    }
                }
            }
            var ab = $("input[name=optionmetagroup1]");
            for (var j = 0; j < ab.length; j++) {
                $("." + ab.eq(j).val()).hide();
            }
            var group = $("#optionmetagroup").val().split(",");
            if (group != null && group != "") {
                for (var i = 0; i < group.length; i++) {
                    $("." + group[i]).show();
                    var obj = $("input[name=optionmetagroup1]");
                    for (var j = 0; j < obj.length; j++) {
                        if (obj.eq(j).val() == group[i]) {
                            obj.eq(j).attr("checked", true);
                        }
                    }
                }
            }
            $("[name = optionmetagroup1]:checkbox").bind("click", function () {
                if ($(this).attr("checked") == "checked") {
                    $("." + $(this).val()).show();
                } else {
                    $("." + $(this).val()).hide();
                }
            })
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
            return true;
        }

        function chekbox() {
            var datacode = $("#datacode").val();
            if (datacode == undefined || datacode == "") {
                alert("请先填写资料代码");
                $("#datacode").focus();
            }
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
                            $("#imageurl").attr("value", data.imgUrl);
                            $("#iconImg_IMG").show();
                            $("#iconImg_IMG img").attr("src", serverUrl + data.imgUrl);
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

        function openwin() {
            parent.open(ctx + "/dataService/showPage");
        }
    </script>
</head>

<body>

<ul class="nav nav-tabs">
    <li>
        <a href="${ctx}/dataService/getDataDetail?pid=${pid}&categoryid=${dataCategoryDef.categoryid}">${dataCategoryDef.chnname}-资料列表</a>
    </li>
    <li class="active"><a href="${ctx}/dataService/addDataDef">编辑数据资料</a></li>
</ul>
<form:form id="inputForm" modelAttribute="dataDef" action="${ctx}/dataService/updateDataDef" method="post"
           enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
    <form:hidden path="insertmode" id="insertmode"/>
    <form:hidden path="servicemode" id="servicemode"/>
    <form:hidden path="optionmetagroup" id="optionmetagroup"/>
    <form:hidden path="udatacode" id="udatacode" value="${dataDef.datacode}"/>
    <%--  <form:hidden path="invalid" id="invalid" value="0"/> --%>
    <input type="hidden" name="pid" id="pid" value="${pid}"/>
    <input type="hidden" name="categoryid" id="categoryid" value="${categoryid}">
    <form:hidden path="imageurl" id="imageurl"/>
    <!-- <input type="hidden" name="dataendflag" id="dataendflag"> -->
    <form:hidden path="dataendflag"/>
    <form:hidden path="datacode"/>
    <div class="control-group">
        <label class="control-label">父节点：</label>
        <div class="controls">
                <%--  <tags:treeselect id="dataService" name="treeNodeId" value="" labelName="treeNodeName" labelValue="${dataCategoryDef.chnname}"
                              title="数据服务菜单" url="/dataService/getdataServiceTreeList"  extId=""  cssClass="required"/> --%>
            <label>${dataCategoryDef.chnname}</label>
            <input id="openWin" class="btn btn-primary" type="button" value="查看示例" onclick="openwin()"/>
        </div>
    </div>
    <%--  <div class="control-group">
         <label class="control-label">查询接口资料代码：</label>
         <div class="controls">
             <form:input path="udatacode" id="udatacode" htmlEscape="false" cssStyle="width:350px;"/>
             <label style="color: red;">遵循CIMISS接口编码规范</label>
         </div>
     </div> --%>
<%--    <div class="control-group">--%>
<%--        <label class="control-label"><a style="color: red;">*</a>资料大类编码：</label>--%>
<%--        <div class="controls">--%>
<%--                &lt;%&ndash; <form:input path="dataclasscode" id="dataclasscode" htmlEscape="false" cssStyle="width:350px;"/> &ndash;%&gt;--%>
<%--            <form:select path="dataclasscode" items="${dataDic}" itemLabel="chNName" itemValue="dataClassCode">--%>
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
            <form:input path="orderno" id="orderno" htmlEscape="false" cssStyle="width:350px;" class="required digits"
                        onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)"
                        onblur="this.v();"/>
        </div>
    </div>
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
                <%-- 	<form:input path="datacode" id="datacode" htmlEscape="false" cssStyle="width:350px;"/> --%>
            <label>${dataDef.datacode}</label>
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
            <form:input path="keywords" id="keywords" htmlEscape="false" cssStyle="width:350px;"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">发布时间：</label>
        <div class="controls">
            <form:input path="publishTime" id="publishTime" htmlEscape="false"
                        onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" class="input-medium Wdate"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" style="font-weight:bold;font-size:16px;">元数据详细说明>></label>
    </div>
    <div class="control-group" id="concheck">
        <label class="control-label">资料中文描述：</label>
        <div class="controls">
				<textarea name="chndescription" id="chndescription" class="input-xlarge">
                        ${dataDef.chndescription}</textarea>
            <tags:ckeditor replace="chndescription" height="250"></tags:ckeditor>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" style="font-weight:bold;font-size:16px;">地理覆盖范围>>
            <input type="checkbox" name="optionmetagroup1" value="GEOSCOPE"/>
        </label>
    </div>
    <div class="GEOSCOPE">
        <div class="control-group">
            <label class="control-label">地理范围描述：</label>
            <div class="controls">
                <form:input path="areascope" id="areascope" htmlEscape="false" cssStyle="width:350px;"/>
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
                              onclick="hideSearchurl()"/><label style="margin-right: 30px;">要素型数据</label>
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
    <input class="radio-input" name="insert" type="checkbox" value="1"/><label>外接数据源</label>
    <input class="radio-input" name="insert" type="checkbox" value="2"/><label>流程接入</label>
    <input class="radio-input" name="insert" type="checkbox" value="4"/><label>手工上传接入</label>
    </div>
    </div>
    -->
    <%--		     <div class="control-group">--%>
    <%--				<label class="control-label"><a style="color: red;">*</a>服务方式：</label>--%>
    <%--						<div class="controls">				--%>
    <%--				           	 <input class="radio-input" name="service" type="checkbox"  value="1"/><label>数据检索</label>--%>
    <%--							<span class="serviceModel">--%>
    <%--							 <input class="radio-input" name="service" type="checkbox"  value="2"/><label>数据订阅</label>--%>
    <%--							</span>--%>
    <%--							--%>
    <%--&lt;%&ndash;							 <input class="radio-input" name="service" type="checkbox"  value="4"/><label>FTP下载</label>&ndash;%&gt;--%>
    <%--							 <input class="radio-input" name="service" type="checkbox"  value="8"/><label>MUSIC接口服务</label>--%>
    <%--							<span class="serviceModel">--%>
    <%--							 <input class="radio-input" name="service" type="checkbox"  value="16"/><label>TDS接口服务</label>--%>
    <%--							 <input class="radio-input" name="service" type="checkbox"  value="32"/><label>存储情况</label>--%>
    <%--		                    </span>--%>
    <%--		               </div>--%>
    <%--		               --%>
    <%--		   </div>--%>
    <%--		   <div class="control-group">--%>
    <%--				<label class="control-label">数据源编码：</label>--%>
    <%--				<div class="controls">	--%>
    <%--					<form:radiobutton path="datasourcecode" value="CMISS"/><label>CMISS</label>--%>
    <%--					<form:radiobutton path="datasourcecode" value="MDSS"/><label>MDSS</label>--%>
    <%--					<form:radiobutton path="datasourcecode" value="MARS"/><label>MARS</label>--%>
    <%--					<form:radiobutton path="datasourcecode" value="MARS"/><label>CDC</label>--%>
    <%--					<form:radiobutton path="datasourcecode" value="MARS"/><label>IDATA</label>			--%>
    <%--					&lt;%&ndash; <form:input path="datasourcecode" id="datasourcecode" htmlEscape="false" cssStyle="width:350px;"/> &ndash;%&gt;--%>
    <%--				<label style="color: red;">(默认选择CMISS)</label>--%>
    <%--				</div>--%>
    <%--				--%>
    <%--		   </div>--%>
    <%--		   <div class="control-group">--%>
    <%--				<label class="control-label"><a style="color: red;">*</a>有无检索定制分项：</label>--%>
    <%--				<div class="controls">--%>
    <%--				           <form:radiobutton class="radio-input" path="isincludesub" name="isincludesub" value="0"/><label style="margin-right: 30px;">无</label>--%>
    <%--			           	   <form:radiobutton class="radio-input" path="isincludesub" name="isincludesub" value="1"/><label style="margin-right: 30px;">有</label>--%>
    <%--			     <label style="color: red;">(有检索定制分项即多个检索，无检索定制分项即单个检索！)</label>--%>
    <%--			    </div>--%>
    <%--		   </div>--%>
    <div class="control-group" style="display:none;">
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

