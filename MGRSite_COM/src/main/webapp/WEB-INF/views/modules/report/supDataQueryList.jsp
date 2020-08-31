<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>报表数据查询页面</title>
    <meta name="decorator" content="default" charset="UTF-8"/>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <%@ include file="/WEB-INF/views/include/dialog.jsp" %>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/report/supDatafileinfo/query">数据查询</a></li>
    <li class="active"><a href="${ctx}/report/supDatafileinfo/queryNew">新数据查询</a></li>
</ul>
<form id="searchForm" class="breadcrumb form-search" action="${ctx}/report/supDatafileinfo/queryData" method="post"
      style="height: 73px;">
    <li>
        <div style="position: relative;">
            <label>台站列表：</label>
            <input id="stationId" type="hidden" autocomplete="off"/>
            <input id="stationName" style="width: 700px" autocomplete="off" readonly="readonly"/>
            <span style="color:red">*</span>
            <a href="#" data-toggle="modal" data-target="#myModal"
               onclick="getTreeStationData()">选择</a>
            <label>查询类型：</label>
            <select id="queryType" name="queryType" style="width: 110px" onchange="showMonthConfig()">
                <option value="1">定时值</option>
                <option value="2" selected>日值</option>
                <option value="3">旬值</option>
                <option value="4">月值</option>
                <option value="5">年值/跨月值</option>
            </select>
            <span style="color:red">*</span>
            <span>
                  <label>时间选择:</label>
                  <input id="startTime" class="time" type="text" autocomplete="off" style="width: 172px;"/>
            </span>
            <span>
                    <label>-</label>
                    <input id="endTime" class="time" type="text" autocomplete="off" style="width: 172px;"/>
                    <span style="color:red">*</span>
                </span>
        </div>
        <div style="position: relative; top:17px">
            <label>要素列表：</label>
            <input id="addproject" type="hidden" autocomplete="off"/>
            <input id="paramId" type="hidden" autocomplete="off"/>
            <input id="paramName" style="width: 700px" autocomplete="off" readonly="readonly"/>
            <span style="color:red">*</span>
            <a href="#" data-toggle="modal" data-target="#myModal"
               onclick="getListByDataType()">选择</a>

        </div>

    </li>
    <li style="position: relative;left: 47px;top: -15px;">
        <input id="btnSubmit" class="btn btn-primary" type="button" value="查询" onclick="queryDataCheck(1,1,30)"/>
        <span style="margin-left: 40px;">
             <input id="exportData" class="btn btn-primary" type="button" value="导出" onclick="exportDataInfo(2)"/>
        </span>
    </li>

</form>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
  <thead>
    <tr id="header">
        <th>站点名称</th>
    </tr>
  </thead>
    <tbody id="datalist">

    </tbody>
</table>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" style="width: 336px">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header"
                 style="height: 30px;vertical-align:middle;padding-bottom: 2px;padding-top: 5px;">
                <table>
                    <tr>
                        <td><h4 class="modal-title" id="myModalLabel" style="float: left;width: 294px;"></h4>
                        </td>
                        <td>
                            <button type="button" class="close" data-dismiss="modal"
                                    aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="modal-body">
                <div id="treeDemo" class="ztree table_ztree" >
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="sure()">确定</button>
                <button type="button" class="btn btn-primary" onclick="closemodel()">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="pagination"></div>

<script type="text/javascript">
    var di = {};
    var isStation=true;
    $(function () {
        //站点信息
        // addStationList();
        $('#myModal').hide();

        //获取文件类型
        // getFileType();
        $('.time').bind('focus',function(){WdatePicker()});

        //默认颜色
        $('#startTime').unbind('focus');
        $('#startTime').bind('focus',function(){WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});});
        $('#endTime').unbind('focus');
        $('#endTime').bind('focus',function(){WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});});
        //获取要素
        // getListByDataType();
    });
    //站点信息
    function getTreeStationData() {
        var zNodes = [];
        $.ajax({
            url: "${ctx}/report/supSurveystation/getStation",
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
                        });
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
                                enable: true
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
                    // treeObj.expandAll(true);
                    isStation=true;
                    $("#myModalLabel").html("台站选择");
                    var ids =$("#stationId").val();
                    if(ids!=""){
                        selectDataDemo(ids);
                    }
                }

            }
        });

    }
    function selectDataDemo(ids){
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");  //得到该tree
        if(ids!="") {
            var idss = ids.split(",");
            //遍历获取的数据 构造数组
            $.each(idss, function (i, item) {
                var node = treeObj.getNodeByParam('id', item);
                if (node) {
                    node.checked = true;
                    treeObj.updateNode(node);
                    var parentid=getParentNode(node);
                    var parentids=parentid.split(",");
                    $.each(parentids, function (j, pitem) {
                        var parentNode = treeObj.getNodeByParam('id', pitem);
                        if (parentNode) {
                            parentNode.checked = true;
                            treeObj.updateNode(parentNode);
                        }
                    });
                }
            });
        }
    }
    function getParentNode(node) {
        if (node == null)
            return "";
        var parentid = node.id;
        var pNode = node.getParentNode();
        if (pNode != null) {
            parentid = getParentNode(pNode) + "," + parentid;
        }
        return parentid;
    }
    //获取文件类型
    function getFileType() {
        $.ajax({
            url: "${ctx}/report/supDatafileinfo/getDictForFileType",
            data: {
                "type": "file_type"
            },
            type: "post",
            async: true,
            success: function (result) {
                var jsobj = eval("(" + result + ")");
                if (jsobj != null) {
                    var info = "";
                    for (var i in jsobj) {
                        info += "<option value='" + jsobj[i].value + "'>" + jsobj[i].label + "</option>";
                    }
                    $("#fileType").html(info);
                }
            }
        });
    }
function getListByDataType() {
    var dataType=$("#queryType").val();
    var zNodes=[];
    isStation=false;
    $("#treeDemo").html("");
    $.ajax({
        url: "${ctx}/report/supReportsearchconf/getListByDataType",
        data: {
            dataType: dataType
        },
        type: "post",
        dataType: "JSON",
        async: true,
        success: function (result) {
            if (result != "" || result.length>0) {
                //遍历获取的数据 构造数组
                $.each(result, function (i, item) {
                    zNodes.push({
                        id: item.id,
                        pId: item.pid,
                        name: item.name,
                        NODE_VALUE: item.id
                    });
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
                            enable: true
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
                $("#myModalLabel").html("要素选择");
                var ids =$("#paramId").val();
                if(ids!=""){
                    selectDataDemo(ids);
                }
            }

        }
    });
}
function sure(){
        if(isStation){
            var treeObj = $.fn.zTree.getZTreeObj("treeDemo");  //得到该tree
            var stationName="";
            var stationId="";
           var nodes=treeObj.getCheckedNodes(true);
            for(var i=0;i<nodes.length;i++){
                if(nodes[i].children==undefined){
                    var id=nodes[i].id;
                    var name=nodes[i].name;
                    stationName+=name+",";
                    stationId+=id+",";
                }

            }
            $("#stationName").val(stationName.substr(0,stationName.length-1));
            $("#stationId").val(stationId.substr(0,stationId.length-1));
        }else{
            var treeObj = $.fn.zTree.getZTreeObj("treeDemo");  //得到该tree
            var paramName="";
            var paramCode="";
            var addproject="";
            var nodes=treeObj.getCheckedNodes(true);
            for(var i=0;i<nodes.length;i++){
                if(nodes[i].children==undefined){
                    var id=nodes[i].id;
                    var name=nodes[i].name;
                    if(id!=undefined&&name!=undefined){
                        paramName+=name+",";
                        paramCode+=id+",";
                    }

                }else{
                    var id=nodes[i].id;
                    if(id!=undefined){
                        addproject+=id+",";
                    }

                }

            }
            $("#addproject").val(addproject.substr(0,addproject.length-1));
            $("#paramName").val(paramName.substr(0,paramName.length-1));
            $("#paramId").val(paramCode.substr(0,paramCode.length-1));
            changeTableHead();
        }
    closemodel();
}
    //项目信息
    function addProject(di) {
        var ids=null;
        if (di.type == "root") {
            di.id = "1";
        }else if(di.type=="last"){
            ids=$("#paramId").val().split(",");
        }

        $.ajax({
                url: "${ctx}/report/supReportsearchconf/getProject",
                data: {
                    parentId: di.id,
                    param_type:di.queryType
                },
                type: "post",
                dataType: "JSON",
                async: true,
                success: function (result) {
                    if (result.length != 0) {
                        debugger;
                        var info = '';
                        var count=1;
                        if(di.type=="last"){
                            $(".modal-body").empty();
                            info+="<table><tr>";
                        }
                        for (var i = 0; i < result.length; i++) {
                            if (result[i].paramName.indexOf("旬值") > -1) {
                                info += "<option value='" + result[i].id + "_3'>" + result[i].paramName + "</option>";
                            } else if (result[i].paramName.indexOf("日值") > -1) {
                                info += "<option value='" + result[i].id + "_1'>" + result[i].paramName + "</option>";
                            } else if (result[i].paramName.indexOf("定时值") > -1) {
                                info += "<option value='" + result[i].id + "_5'>" + result[i].paramName + "</option>";
                            } else if (result[i].paramName.indexOf("年值") > -1) {
                                info += "<option value='" + result[i].id + "_4'>" + result[i].paramName + "</option>";
                            } else if (result[i].paramName.indexOf("月值") > -1) {
                                info += "<option value='" + result[i].id + "_2'>" + result[i].paramName + "</option>";
                            } else if(di.type=="last"){
                                if(ids!=null&&ids!=""&&ids.indexOf(result[i].paramCode)>-1){
                                    info += "<td><input type='checkbox' checked='checked' value='" + result[i].paramCode+","+result[i].paramName+ "' name='paramInfo'/><span>" + result[i].paramName + "</span></td>";
                                }else{
                                    info += "<td><input type='checkbox' value='" + result[i].paramCode+","+result[i].paramName+ "' name='paramInfo'/><span>" + result[i].paramName + "</span></td>";
                                }
                                if(count%5==0&&count>0){
                                    // info+="<br/>";
                                    info+="</tr><tr>";
                                }
                                count++;
                            }else{
                                info += "<option value='" + result[i].id + "'>" + result[i].paramName + "</option>";
                            }
                        }

                        if (di.type == "root") {
                            $("#addproject").html($("#addproject").html() + info);
                        } else if (di.type == "last") {
                            isStation=false;
                            info+="</tr></table>";
                            $(".modal-body").html(info);
                            $("#myModalLabel").html("数据信息");
                            // $("#queryData").html(info);
                        } else {
                            $("#queryType").html(info);
                        }
                    }
                }
            }
        );

    }
    function addMonthList() {
        var start = 0;
        var info = "";
        while (true) {
            start = start + 1;
            info += "<option value='" + start + "'>" + start + "月</option>";
            $("#startMonthList").html(info);
            $("#endMonthList").html(info);
            if (start == 12) {
                break;
            }
        }
    }

    function getQuertyType() {
        var di = {};
        di.id = $("#addproject").val();
        di.type="type";
        di.queryType=$("#queryType").val();
        addProject(di);
    }

    function showMonthConfig() {
        var id = $("#queryType").val();
        if (id== "2") {//日值
            $('#startTime').unbind('focus');
            $('#startTime').bind('focus',function(){WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});});
            $('#endTime').unbind('focus');
            $('#endTime').bind('focus',function(){WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});});
        } else if(id== "1"){//定时
            $('#startTime').unbind('focus');
            $('#startTime').bind('focus',function(){WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH'});});
            $('#endTime').unbind('focus');
            $('#endTime').bind('focus',function(){WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH'});});
        }else if(id== "5"){//年值
            $('#startTime').unbind('focus');
            $('#startTime').bind('focus',function(){WdatePicker({skin:'whyGreen',dateFmt:'yyyy'});});
            $('#endTime').unbind('focus');
            $('#endTime').bind('focus',function(){WdatePicker({skin:'whyGreen',dateFmt:'yyyy'});});
        } else if(id== "3"||id== "4"){//旬值、月值
            $('#startTime').unbind('focus');
            $('#startTime').bind('focus',function(){WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM'});});
            $('#endTime').unbind('focus')
            $('#endTime').bind('focus',function(){WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM'});});
        }
        $("#addproject").val("");
        $("#paramId").val("");
        changeTableHead();
    }


    function changeTableHead() {
        //获取当前选择项.
        var options = $("#paramId").val().split(",");
        if(options==null){
            return;
        }
        var info = "";
        info += "<th>站点名称</th>";
        var querytype = $("#queryType").val();
        if ("2"==querytype) { // 日
            info += "<th>年</th>";
            info += "<th>月</th>";
            info += "<th>日</th>";
        } else if ("4"==querytype){// 月
            info += "<th>年</th>";
            info += "<th>月</th>";
        }else if("3"==querytype){//旬
            info += "<th>年</th>";
            info += "<th>月</th>";
            info += "<th>旬</th>";
        } else if ("5"==querytype) { // 年
            info += "<th>年</th>";
        } if ("1"==querytype) { // 定时值
            info += "<th>年</th>";
            info += "<th>月</th>";
            info += "<th>日</th>";
            info += "<th>时</th>";
        }
        for (var t in options) {
            info += "<th>" + options[t]+ "</th>";
        }
        $("#header").html(info);
        $("#datalist").html("");
    }

    function queryDataCheck(type,no,size) {
        var flag = false;
        var station = $("#stationId").val();
        var project = $("#addproject").val();
        var querytype = $("#queryType").val();
        var startime = $("#startTime").val();
        var queryData = $("#paramId").val();
        var endtime = $("#endTime").val();
        // var filetype = $("#fileType").val();
        if (null == station||""==station || "请选择" == project || "请选择" == querytype || "请选择" == startime || "请选择" == endtime  || null == queryData) {
            flag = true;
        }
        if (flag) {
            alert("必填项不能为空!");
            return;
        }
        if (startime > endtime) {
            alert("开始年份不能大于终止年份！");
            return;
        }
        if(queryData.split(",")==""){
            alert("查询数据未选择或者查询数据的参数代号未配置！");
            return;
        }
        var f = document.getElementsByTagName("form")[0];

        // f.action =f.action+"?stations="+station+"&project="+project+"&queryType="+querytype+"&startime="+startime+"&endtime=" +
        // endtime+"&queryData="+queryData+"&type="+type+"&filetype="+filetype;
        $.ajax({
            url: f.action,
            data: {
                "stations": station,
                "project": project,
                "queryType": querytype,
                "startime": startime,
                "endtime": endtime,
                "queryData": queryData,
                "type": type,
                "pageno":no,
                "pagesize":size
            },
            type: 'post',
            success: function (result) {
                var jsobj = eval("(" + result + ")");
                var info = "";
                var list=jsobj.datainfo.list;
                for (var i = 0, length = list.length; i < length; i++) {
                    info += "<tr>";
                    info += "<th>" + list[i].station + "</th>";
                    if ("1"==querytype) { // 定时
                        info += "<th>" + list[i].yearvalue + "</th>";
                        info += "<th>" + list[i].monthvalue + "</th>";
                        info += "<th>" + list[i].dayvalue + "</th>";
                        info += "<th>" + list[i].hourvalue + "</th>";
                    } else if ("4"==querytype){// 月
                        info += "<th>" + list[i].yearvalue + "</th>";
                        info += "<th>" + list[i].monthvalue + "</th>";
                    }else if("3"==querytype){//旬
                        info += "<th>" + list[i].yearvalue + "</th>";
                        info += "<th>" + list[i].monthvalue + "</th>";
                        info += "<th>" + list[i].meadowvalue + "</th>";
                    } else if("2"==querytype){//日
                        info += "<th>" + list[i].yearvalue + "</th>";
                        info += "<th>" + list[i].monthvalue + "</th>";
                        info += "<th>" + list[i].dayvalue + "</th>";
                    } else if ("5"==querytype) { // 年
                        info += "<th>" + list[i].yearvalue + "</th>";
                    }
                    for (var t = 0, length1 = queryData.split(",").length; t < length1; t++) {
                        var colu = "column" + t;
                        info += "<th>" + list[i][colu] + "</th>";
                    }
                    info += "</tr>"
                }
                $(".pagination").html(jsobj.pageinfo);
                $("#datalist").html(info);

            }
        });
        // f.submit();
    }
    function page(n,s){
        queryDataCheck(1,n,s);
    }
    function exportDataInfo(type) {
        var station = $("#stationId").val();
        var project = $("#addproject").val();
        var querytype = $("#queryType").val();
        var startime = $("#startTime").val();
        var queryData = $("#paramId").val();
        var endtime = $("#endTime").val();
        var filetype = $("#fileType").val();

        var time = 500;
        var options = $("#queryData option:selected");//获取当前选择项
        var headInfo = ["站点名称"];
        if ("2"==querytype) { // 日
            headInfo.push("年");
            headInfo.push("月");
            headInfo.push("日");
        } else if ("4"==querytype){// 月
            headInfo.push("年");
            headInfo.push("月");
        }else if("3"==querytype){//旬
            headInfo.push("年");
            headInfo.push("月");
            headInfo.push("旬");
        } else if ("5"==querytype) { // 年
            headInfo.push("年");
        }else if("1"==querytype){//定时
            headInfo.push("年");
            headInfo.push("月");
            headInfo.push("日");
            headInfo.push("时");
        }
        //获取当前选择项.
        var options = $("#paramName").val().split(",");
        for (var t in options) {
            headInfo.push(options[t]);
        }

        // 创建form元素
        var temp_form = document.createElement("form");
        // 设置form属性
        temp_form.action = '${ctx}/report/supDatafileinfo/queryData';
        temp_form.target = "_self";
        temp_form.method = "post";
        temp_form.style.display = "none";
        // 处理需要传递的参数

        var opt = document.createElement("textarea");
        opt.name = "stations";
        opt.value = station;
        temp_form.appendChild(opt);

        opt = document.createElement("textarea");
        opt.name = "project";
        opt.value = project;
        temp_form.appendChild(opt);

        opt = document.createElement("textarea");
        opt.name = "type";
        opt.value = type;
        temp_form.appendChild(opt);

        opt = document.createElement("textarea");
        opt.name = "queryType";
        opt.value = querytype;
        temp_form.appendChild(opt);

        opt = document.createElement("textarea");
        opt.name = "startime";
        opt.value = startime;
        temp_form.appendChild(opt);

        opt = document.createElement("textarea");
        opt.name = "endtime";
        opt.value = endtime;
        temp_form.appendChild(opt);

        opt = document.createElement("textarea");
        opt.name = "queryData";
        opt.value = queryData;
        temp_form.appendChild(opt);

        opt = document.createElement("textarea");
        opt.name = "filetype";
        opt.value = filetype;
        temp_form.appendChild(opt);

        opt = document.createElement("textarea");
        opt.name = "pageno";
        opt.value = 1;
        temp_form.appendChild(opt);

        opt = document.createElement("textarea");
        opt.name = "pagesize";
        opt.value = 30;
        temp_form.appendChild(opt);

        opt = document.createElement("textarea");
        opt.name = "headInfo";
        opt.value = JSON.stringify(headInfo);
        temp_form.appendChild(opt);

        document.body.appendChild(temp_form);
        // 提交表单
        temp_form.submit();
    }

    function closemodel() {
        $('#myModal').modal('hide');
    }

</script>

</body>

</html>
