<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/dialog.jsp" %>
<%@include file="/WEB-INF/views/include/treeview.jsp" %>
<html>
<head>
    <title>查看数据角色</title>
    <style type="text/css">
        .content {
            font-family: '微软雅黑';
            vertical-align: top;
            min-width: 300px;
            min-height: 228px;
        }

        .name {
            font-size: 14px;
            font-weight: bold;
            margin: 5px 5px;
            text-align: right;
        }
    </style>
</head>
<script type="text/javascript">
    function getselectDataView() {
        var zNodes = [];
        var dataid = "${dataRole.dataroleId}";
        $.ajax({
            url: '${ctx}/dataRole/getselectDataView',
            data: {"dataroleid": dataid},
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
                                enable: false,
                                chkStyle:
                                    "checkbox",
                                chkboxType:
                                    {
                                        "Y":
                                            "ps", "N":
                                            "s"
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
                    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");  //得到该tree
                    treeObj.expandAll(true);
                }
            }
        });
    }

    $(document).ready(function () {
        getselectDataView();
    });
</script>
<body>
<div class="content">
    <table>
<%--        <tr>--%>
<%--            <td class="name" style="position: relative;left: 4px;">角色编号：</td>--%>
<%--            <td style="width: 266px;">${dataRole.dataroleId}</td>--%>
<%--        </tr>--%>
        <tr>
            <td class="name" style="position: relative;left: 34px;">角色名称:</td>
            <td style="position: relative;left: 34px;">${dataRole.dataroleName}</td>
        </tr>
        <tr>
    <td class="name" style="position: relative;left: 34px;">序号:</td>
        <td style="position: relative;left: 34px;">${dataRole.orderNo}</td>
    </tr>
    <tr>
        <td class="name" style="position: relative;left: 34px;">描述:</td>
        <td style="position: relative;left: 34px;">${dataRole.descriptionChn}</td>
    </tr>
        <tr>
            <td class="name" style="position: absolute;top: 77px;">专题产品授权：</td>
            <td>
                <div class="controls">
                    <div id="treeDemo"
                         class="ztree table_ztree" style="position: relative;left: 28px;">
                    </div>
                    <span id="daMsg"></span>
                </div>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
