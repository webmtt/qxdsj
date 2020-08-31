<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>修改数据角色</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
    <script type="text/javascript">

        function checkIdOrName(type) {
            var flag = false;
            var id = $("#dataroleId").val();
            var name = $("#dataroleName").val()
            if (type == 1) {
                name = undefined;
            } else {
                id = undefined;
            }
            $.ajax({
                url: '${ctx}/dataRole/checkNameOrId',
                type: 'post',
                data: {'id': id, 'name': name},
                dataType: 'text',
                asyn: false,
                success: function (result) {
                    if (result == "0" && type == 1) {
                        $("#idMsg").css("color", "green");
                        $("#idMsg").text("编号可用");
                    } else if (type == 1) {
                        $("#idMsg").css("color", "red");
                        $("#idMsg").text("编号已存在");
                        flag = true;
                    } else if (result == "0" && type == 2) {
                        $("#nameMsg").css("color", "green");
                        $("#nameMsg").text("名称可用");
                    } else if (2 == type) {
                        $("#nameMsg").css("color", "red");
                        $("#nameMsg").text("名称重复");
                        flag = true;
                    }

                }
            });
            return flag;
        }

    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a id="dataRoleList" href="${ctx}/dataRole/list">数据角色列表</a></li>
    <li class="active"><a href="javaScript:void(0)">修改数据角色</a></li>
</ul>
<br/>
<%--<input type="hidden" id="unitIds" value="${unitIds }">--%>
<form:form id="inputForm" modelAttribute="dataRole" action="${ctx}/dataRole/save" method="post"
           class="form-horizontal">
    <tags:message content="${message}"/>
    <form:hidden path="dataroleId" id="dataroleId"/>
<%--    <div class="control-group">--%>
<%--        <label class="control-label">角色编号:</label>--%>
<%--        <div class="controls">--%>
<%--            <form:input path="dataroleId" id="dataroleId" htmlEscape="false" maxlength="50"/>--%>
<%--            <span id="idMsg"></span>--%>
<%--        </div>--%>
<%--    </div>--%>
    <div class="control-group">
        <label class="control-label">角色名称:</label>
        <div class="controls">
            <form:input path="dataroleName" id="dataroleName" htmlEscape="false" maxlength="50"/>
            <span id="nameMsg"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">序号:</label>
        <div class="controls">
            <form:input path="orderNo" id="orderNo" htmlEscape="false" maxlength="50" onblur="clearMsg()"/>
            <span id="noMsg"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">描述:</label>
        <div class="controls">
            <form:input path="descriptionChn" id="descriptionChn" htmlEscape="false" maxlength="50"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">专题产品授权:</label>
        <div class="controls">
            <div id="treeDemo" class="ztree table_ztree" style="">
            </div>
            <input id="nodes" style="display: none;"/>
            <span id="daMsg"></span>
        </div>
    </div>
    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="addNodeSelect()"
        />&nbsp;
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>

<script type="text/javascript">
    $(document).ready(function () {
        getTreeData();
    });

    function getTreeData() {
        var zNodes = [];
        $.ajax({
            url: '${ctx}/subject/supSubjectdef/getSubjectInfo',
            type: 'post',
            dataType: 'text',
            success: function (result) {
                var jsobj = eval("(" + result + ")");
                if (jsobj != "" || jsobj != undefined) {
                    //遍历获取的数据 构造数组
                    $.each(jsobj, function (i, item) {
                        zNodes.push({
                            id: item.id,
                            pId: item.pid,
                            name: item.name,
                            NODE_VALUE: item.id
                        })
                        ;
                    });
                    var setting = {
                            view: {
                                showLine: true,
                                showIcon: true,
                                nameIsHTML: true
                            },
                            data: {
                                simpleData: {
                                    enable: true  //简单数据模式
                                }
                            }
                            ,
                            async: {
                                enable: false
                            }
                            ,
                            check: {
                                enable: true,
                                chkStyle:
                                    "checkbox",
                                chkboxType:
                                    {
                                        "Y":
                                            "ps", "N":
                                            "ps"
                                    }
                            }
                            ,
                            callback: {
                                // beforeCollapse: beforeCollapse,   //折叠之前回调函数
                                // beforeExpand: beforeExpand,       //展开之前回调函数
                                // onNodeCreated: onNodeCreated,     //用于捕获节点生成 DOM 后的事件回调函数
                                // onExpand: zTreeOnExpand,          //节点被展开触发事件
                                //MouseDown: zTreeOnMouseDown,
                                onRightClick: null,//OnRightClick
                                // onClick: zTreeOnClick
                            }
                        }
                    ;
                    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                    // var treeObj = $.fn.zTree.getZTreeObj("treeDemo");  //得到该tree
                    // treeObj.expandAll(true);
                }
                if (${not empty dataRole.dataroleId?true:false}) {
                    getSelectData();
                }
            }
        });

    }
    function clearMsg(){
        var orderNo = $("#orderNo").val();
        var reg=/^[0-9]*$/;
        if ("" == orderNo) {
            $("#noMsg").css("color", "red");
            $("#noMsg").text("序号不能为空");
            return;
        }else if(!(reg.test(orderNo))){
            $("#noMsg").css("color", "red");
            $("#noMsg").text("请输入数字");
            return;
        }else {
            $("#noMsg").css("color", "green");
            $("#noMsg").text("序号可用");
            return;
        }
    }
    function addNodeSelect() {
        var dataroleId = $("#dataroleId").val();
        // if ("" == dataroleId) {
        //     $("#idMsg").css("color", "red");
        //     $("#idMsg").text("编号不能为空");
        //     return;
        // }
        if (${not empty dataRole.dataroleId?false:checkIdOrName(1)}) {
            return;
        }
        var dataroleName = $("#dataroleName").val();

        if ("" == dataroleName) {
            $("#nameMsg").css("color", "red");
            $("#nameMsg").text("名称不能为空");
            return;
        }
        if (${not empty dataRole.dataroleId?false:checkIdOrName(2)}) {
            return;
        }
        var orderNo = $("#orderNo").val();
        var reg=/^[0-9]*$/;
        if ("" == orderNo) {
            $("#noMsg").css("color", "red");
            $("#noMsg").text("序号不能为空");
            return;
        }
        if(!reg.test(orderNo)){
            $("#noMsg").css("color", "red");
            $("#noMsg").text("请输入数字");
            return;
        }
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes = treeObj.getCheckedNodes(true);
        if (0 == nodes.length) {
            $("#daMsg").css("color", "red");
            $("#daMsg").text("授权不能为空");
            return;
        }
        var selectData = [];
        for (var i in nodes) {
            // var t = {};
            // t.id = nodes[i].id;
            if(selectData.indexOf(nodes[i].id)>-1){
                continue;
            }
            selectData.push(nodes[i].id);
        }
        // var select = JSON.stringify(selectData);
        var f = document.getElementsByTagName("form")[0];
        // f.action = f.action + "?nodes=" + select;
        //定义一个form表单
        var form = $("<form>");
        form.attr("id", "saveDataRole");
        form.attr("style", "display:none");
        form.attr("target", "");
        form.attr("method", "post");
        form.attr("action", f.action);
        var input1 = $("<input>");
        input1.attr("type", "hidden");
        input1.attr("name", "dataroleId");
        input1.attr("value", dataroleId);
        form.append(input1);
        var input2 = $("<input>");
        input2.attr("type", "hidden");
        input2.attr("name", "dataroleName");
        input2.attr("value", dataroleName);
        form.append(input2);
        var input3= $("<input>");
        input3.attr("type", "hidden");
        input3.attr("name", "orderNo");
        input3.attr("value", orderNo);
        form.append(input3);
        var input4= $("<input>");
        input4.attr("type", "hidden");
        input4.attr("name", "descriptionChn");
        input4.attr("value", $("#descriptionChn").val());
        form.append(input4);
        var input5= $("<input>");
        input5.attr("type", "hidden");
        input5.attr("name", "nodes");
        input5.attr("value",selectData);
        form.append(input5);

        $("body").append(form);//将表单放置在web中
        form.submit();//表单提交

        // f.submit();
    }

    function getSelectData() {
        var dataid = "${dataRole.dataroleId}";
        $.ajax({
            url: '${ctx}/dataRole/getselectData',
            data: {"dataroleid": dataid},
            type: 'post',
            dataType: 'text',
            success: function (result) {
                var jsobj = eval("(" + result + ")");
                var treeObj = $.fn.zTree.getZTreeObj("treeDemo");  //得到该tree
                if (jsobj != "" || jsobj != undefined) {
                    debugger;
                    //遍历获取的数据 构造数组
                    $.each(jsobj, function (i, item) {
                        var node = treeObj.getNodeByParam('id', item.dataId);
                        if (node) {
                            node.checked = true;
                            treeObj.updateNode(node);
                        }
                    });
                }
            }
        });
    }
</script>
</body>

</html>