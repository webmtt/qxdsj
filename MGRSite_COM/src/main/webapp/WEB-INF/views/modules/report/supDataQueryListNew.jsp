<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>报表数据查询页面</title>
    <meta name="decorator" content="default" charset="UTF-8"/>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <%@ include file="/WEB-INF/views/include/dialog.jsp" %>
    <style type="text/css">
        .project{
            width: 7%;
            /*text-align: center;*/
            background: #149def;
            color: white;
            line-height: 70px;
        }
        .param{
            overflow: auto;
            display:flex;
            flex-wrap: wrap;
            width: 93%;
            justify-content: left;
            align-items: center;
        }
        #dataListTable{
            border-collapse:collapse;
            border-spacing:0;
            clear:both;
            width: 100%;
        }
        #dataListTable th,#dataListTable td{
            border:1px solid #dfdfdf;
            text-align:center;
            height: 30px;
            word-break:keep-all;
        }
        #datalist th{
            /*background:#a4aeb9;*/
            /*color:#fff;*/
            /*font-family: "微软雅黑";*/
            /*font-weight:bold;*/
            padding-left:8px;
            padding-right:8px;
            padding-top:10px;
            padding-bottom:10px;
        }
        #header th{
            color:#fff;
            font-family: "微软雅黑";
            /*font-weight:bold;*/
            padding-left:8px;
            padding-right:8px;
            padding-top:10px;
            padding-bottom:10px;
        }
        .title{
            margin: 3px 0;
            height: 30px;
            line-height: 30px;
            font-weight: bolder;
            border-bottom: 2px solid #f57f7f;
            font-size: 21px;
        }
    </style>
</head>
<body>
<ul class="nav nav-tabs">
<%--    <li class="active"><a href="${ctx}/report/supDatafileinfo/query">数据查询</a></li>--%>
    <li class="active"><a href="${ctx}/report/supDatafileinfo/queryNew">数据查询</a></li>
</ul>
<div class="title">
    台站列表 <span style="color:red">*</span>
</div>
<div style="height: 45px;line-height: 45px;">
    <input id="stationId"   style="width: 700px;height: 26PX;border-radius: 4px;border: 1px solid #cccccc;" placeholder="输入台站号(以','分隔)"/>
    <input id="stationName" type="hidden" style="width: 700px" autocomplete="off" readonly="readonly"/>
   <span>
       <input  data-toggle="modal" data-target="#myModal" class="btn btn-primary" type="button" value="台站选择" onclick="getTreeStationData()"/>
   </span>
</div>
<div class="title">查询类型 <span style="color:red">*</span></div>
<div style="height: 45px;line-height: 45px;display:flex;">
    <div style="margin-left: 30px;">
        <input type="radio" value="1" name="queryType"/><span>定时值</span>
    </div>
    <div style="margin-left: 30px;">
        <input type="radio" value="2" checked name="queryType"/><span>日值</span>
    </div>
    <div style="margin-left: 30px;">
        <input type="radio" value="3" name="queryType"/><span>旬值</span>
    </div>
    <div style="margin-left: 30px;">
        <input type="radio" value="4" name="queryType"/><span>月值</span>
    </div>
    <div style="margin-left: 30px;">
        <input type="radio" value="5" name="queryType"/><span>年值/跨月值</span>
    </div>
</div>
 <div class="title">时间范围<span style="color:red">&nbsp;*</span></div>
<div style="height: 45px;line-height: 45px;">
    <label>开始时间：</label>
    <input id="startTime" class="time" type="text" autocomplete="off" style="width: 172px;"/>
    <span style="margin-left: 46px;">
       <label>结束时间：</label>
       <input id="endTime" class="time" type="text" autocomplete="off" style="width: 172px;"/>
    </span>
</div>

</div>
<div class="title">要素选择 <span style="color:red">*</span></div>
<div id="test" style="border:1px solid #cccccc;height:30px"></div>
<div id="paramInfo">

</div>
<input id="projectId" type="hidden" autocomplete="off"/>
<input id="paramId" type="hidden" autocomplete="off"/>
<input id="paramName" type="hidden" autocomplete="off"/>

<div style="position: relative;left: 47px;top: 15px;">
    <div style="text-align: center;">
    <input id="btnSubmit" class="btn btn-primary" type="button" value="数据检索" onclick="queryDataCheck(1,1,30)"/>
    <span style="margin-left: 40px;">
             <input id="exportData" class="btn btn-primary" type="button" value="数据导出" onclick="exportDataInfo(2)"/>
        </span>
    </div>
</div>
<div class="title">结果列表<span style="font-size: 13px;color: darkcyan;">（999999表示缺测）</span></div>
<div style="overflow-x:scroll">
    <table id="dataListTable" class="tablesorter" style="white-space:nowrap;margin-top: 1px; ">
        <thead>
        <tr id="header" style="background: #45aeea;background-position: center right !important;">

        </tr>
        </thead>
        <tbody id="datalist">

        </tbody>
    </table>
</div>
<div class="pagination"></div>
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
<script type="text/javascript">
    var di = {};
    var isStation=true;
    $(function () {
        //获取要素
        getListByDataType();
        $('.time').bind('focus',function(){WdatePicker()});

        //默认颜色
        $('#startTime').unbind('focus');
        $('#startTime').bind('focus',function(){WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});});
        $('#endTime').unbind('focus');
        $('#endTime').bind('focus',function(){WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'});});
        $('input[type=radio][name=queryType]').change(function() {
            showMonthConfig();
        });
    });
    function getListByDataType() {
        var dataType=$("[name='queryType']:checked").val();
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
                    var info="<div style='border-right: 1px solid #cccccc;'><div style='display:flex;border-bottom: 1px solid #cccccc;border-top: 1px solid #cccccc;height:70px;'>"
                    $.each(result, function (i, item) {
                      if(item.pid==""&&i!=0){
                          info+= " </div>" ;
                          info+= "</div>";
                          info+="<div style='display:flex;border-bottom: 1px solid #cccccc; height:70px;'>";
                          info+= "<div class='project'><input id='"+item.id+"' value='"+item.id+"' type='checkbox'/><lable>"+item.name+"</lable></div>";
                          info+= "<div class='param'>";
                      }else{
                          if(i==0){
                              info+= "<div class='project'><input id='"+item.id+"' value='"+item.id+"' type='checkbox'/><lable>"+item.name+"</lable></div>";
                              info+= "<div class='param'>";
                          }else{
                              info+="<div style='text-align: center;margin-left: 30px;'><input id='"+item.id+"' value='"+item.id+"' type='checkbox'/><lable>"+item.name+"</div>";
                          }
                      }
                    });
                    info+="</div>";
                    $("#paramInfo").html(info);
                    //父子联动监听事件
                    parentChildCheck();
                    //子选中监听事件
                    paramCheck();
                    $("#test").hide();
                }

            }
        });
    }
    //参数选中状态
    function paramCheck(){
        $(".param input").on('click', function(item){
            var flag=item.currentTarget.checked;
            var newValue="";
            var newName="";
            var child= document.getElementById(item.currentTarget.id);
            var code=item.currentTarget.defaultValue;
            //参数所在div
            var name=child.parentElement.innerText;
            var oldName=$("#paramName").val();
            var oldvalue=$("#paramId").val();
            //参数大类
            var p1=child.parentElement.parentElement;
            var proj=p1.parentElement.childNodes[0].childNodes[0].defaultValue;
            var oldProject=$("#projectId").val();
            if(flag){
                if(oldvalue==""){
                    newValue=code;
                }else{
                    newValue=oldvalue+","+code;
                }
                if(oldName==""){
                    newName=name;
                }else{
                    newName=oldName+","+name;
                }
                if(oldProject==""||oldProject==undefined){
                    $("#projectId").val(proj);
                }else if(oldProject.indexOf(proj)<0){
                    var newPro= oldProject+","+proj;
                    $("#projectId").val(newPro);
                }
            }else{
                if(oldvalue.indexOf(",")>-1){
                    newValue=oldvalue.replace(code+",","");
                }else{
                    newValue=oldvalue.replace(code,"");
                }
                if(oldName.indexOf(",")>-1){
                    newName=oldName.replace(name+",","");;
                }else{
                    newName=oldName.replace(name,"");
                }
                var p2=p1.children;
                var pc=false;
                $.each(p2, function (i, item) {
                    var cc=item.childNodes[0].checked;
                    if(cc){
                        pc=true;
                    }
                });
                if(!pc){
                    if(oldProject.indexOf(",")>-1){
                        oldProject=oldProject.replace(proj+",","");;
                    }else{
                        oldProject=oldProject.replace(proj,"");
                    }
                    $("#projectId").val(oldProject);
                    console.log("---:"+oldProject);
                }
            }
            $("#paramName").val(newName);
            $("#paramId").val(newValue);
        });

    }
    //父子联动
    function parentChildCheck(){
        $(".project input").on('click', function(item){
            var t=item.currentTarget.checked;
            var child= document.getElementById(item.currentTarget.id);
            var newValue="";
            if(t){
                var code=item.currentTarget.defaultValue;
                var oldvalue=$("#projectId").val();
                if(oldvalue==""){
                    newValue=code;
                }else if(oldvalue.indexOf(code)<0){
                    newValue=oldvalue+","+code;
                }
                $("#projectId").val(newValue);
            }else{
                var code=item.currentTarget.defaultValue;
                var oldvalue=$("#projectId").val();
                if(oldvalue.indexOf(",")>-1){
                    newValue=oldvalue.replace(code+",","");
                    $("#projectId").val(newValue);
                }else{
                    newValue=oldvalue.replace(code,"");
                    $("#projectId").val(newValue);
                }
            }
            //参数所在div
            var m=child.parentElement.nextElementSibling;
            $.each(m.children, function (i, item) {
                item.children[0].click();
            });

        });
    }
    function showMonthConfig() {
        var id =$("[name='queryType']:checked").val();
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
        $("#projectId").val("");
        $("#paramName").val("");
        $("#paramId").val("");
        getListByDataType();
    }


    function changeTableHead() {
        //获取当前选择项.
        var options = $("#paramName").val().split(",");
        var info = "";
        info += "<th>区站号</th>";
        info += "<th>站点名称</th>";
        var querytype = $("[name='queryType']:checked").val();
        if ("2"==querytype) { // 日
            info += "<th>年</th>";
            info += "<th>月</th>";
            info += "<th>日</th>";
        } else if ("4"==querytype){// 月
            info += "<th>年</th>";
            info += "<th>月</th>";
        }else if("3"==querytype){//旬
            info += "<th >年</th>";
            info += "<th >月</th>";
            info += "<th >旬</th>";
        } else if ("5"==querytype) { // 年
            info += "<th >年</th>";
        } if ("1"==querytype) { // 定时值
            info += "<th >年</th>";
            info += "<th >月</th>";
            info += "<th >日</th>";
            info += "<th >时</th>";
        }
        for (var t in options) {
            if(""!=options[t]){
                info += "<th>" + options[t]+ "</th>";
            }
        }
        $("#header").html(info);
        $("#datalist").html("");
    }
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
        }
        closemodel();
    }

    function closemodel() {
        $('#myModal').modal('hide');
    }
    function queryDataCheck(type,no,size) {
        changeTableHead();
        var flag = false;
        var station = $("#stationId").val();
        var project = $("#projectId").val();
        var querytype = $("[name='queryType']:checked").val();
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

        $.ajax({
            url:"${ctx}/report/supDatafileinfo/queryData",
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
                    info += "<th>" + list[i].stationId + "</th>";
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

    }
    function exportDataInfo(type) {
        var station = $("#stationId").val();
        var project = $("#projectId").val();
        var querytype = $("[name='queryType']:checked").val();
        var startime = $("#startTime").val();
        var queryData = $("#paramId").val();
        var endtime = $("#endTime").val();
        var filetype = $("#fileType").val();

        var time = 500;
        var options = $("#queryData option:selected");//获取当前选择项
        var headInfo = ["区站号","站点名称"];
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
        opt.name = "stationIds";
        opt.value = station;
        temp_form.appendChild(opt);

         opt = document.createElement("textarea");
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
    function page(n,s){
        queryDataCheck(1,n,s);
    }
</script>

</body>

</html>
