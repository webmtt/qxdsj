<%--
  Created by IntelliJ IDEA.
  User: 18695
  Date: 2019-12-20
  Time: 下午 1:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<style>
		.inter{
			width: 33%;
			float: left;
			text-align: center;
		}

	</style>
	<title>MUSIC - 接口清单</title>
	<link href="${ctxStatic}/layui-v2.5.5/layui/css/layui.css" rel="stylesheet" type="text/css"/>
	<script src="${ctxStatic}/layui-v2.5.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
</head>

<body>
<div class="left" style="background-color: #f1f1f1;width: 16%;height: 100%;float:left">
	<div id="bg" >
		<div class="leftImge" style="background-color: #69bef3;height: 27px;">
			<label style="font-size: 17px;color: #f9f9fd;">接口清单</label>
		</div>
		<form:form id="searchForm" modelAttribute="interList" action="${ctx}/interdata/interfaceData/interfa/" method="post" class="breadcrumb form-search">
            <span class="layui-breadcrumb" lay-separator="|">
                <c:forEach items="${interList}" var="interfaceData">
					<a  class="inter" href="#bgg" onclick="method('${interfaceData.id}','${interfaceData.name}')">${interfaceData.}</a>
				</c:forEach>
            </span>
		</form:form>
	</div>
	<div style="background-color: #69bef3;height: 27px;margin-top: 72%;">
		<label id ="inters" style="font-size: 17px;color: #f9f9fd;"></label>
	</div>
	<div id="bgg">
	</div>
</div>
<div id="rightdata" style="float:right;width: 83%;height: 46%;">
	<div>
		<p>
			<span style="margin-left: 5px; margin-top: 2px; font-size: 16px; font-weight: bold;">接口调用测试服务</span>
		</p>
	</div>
	<hr style="widows: 100%;">
	<table class="layui-table" id="formTable">
	</table>
</div>
</body>
<script type="text/javascript">
    var intfaceId;
    var dataenc;
    var arrayText = new Array();
    $("span a:first").click();
    //$("div a:first").click();
    function method(id,name) {
        $.ajax({
            type:"post",
            async : false,
            url:"${ctx}/interfacedata/interface/",
            data:{id:id},
            dataType:"json",
            success:function (data) {
                var showState="";
                var showSt="";
                $.each(data, function (i) {
                    showState+="<a href='#rightdata' style='color:#3d8bb1' onclick=getInter("+"'"+data[i].id+"'"+",'"+data[i].dataEncoding+"'"+",'"+data[i].inerfaceId+"'"+")>"+data[i].name+"</a><br>" +
                        "<hr style=\"height:1px;border:none;border-top:1px dashed #12c1c4;\" />";
                    showSt=data[i].dataType;
                });
                document.getElementById("inters").innerHTML=name;
                $("#bgg").html(showState);

            }
        });
    }
    function getInter(id,dataEncoding,inerfaceId) {
        intfaceId = inerfaceId;
        dataenc = dataEncoding;
        $.ajax({
            type:"post",
            async : false,
            url:"${ctx}/interfacedata/allinter/",
            data:{id:id},
            dataType:"json",
            success:function (data) {
                var param;
                param="<tr>" +
                    "<td style='font-weight:bold' colspan='1'>资料类别</td>" +
                    "<td colspan='3'>"+dataEncoding+"</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td style='font-weight:bold' colspan='1'>访问接口</td>" +
                    "<td colspan='3'>"+inerfaceId+"</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td style='background-color:#b0fdff;font-weight:bold' colspan='4'>接口参数</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>参数id</td>" +
                    "<td>参数名称</td>" +
                    "<td>参数类型</td>" +
                    "<td>赋值</td>" +
                    "</tr>";

                for (var i = 0; i <data.length ; i++) {
                    param += "<tr>" +
                        "<td>"+data[i].parameterId+"</td>" +
                        "<td>"+data[i].name+"</td>" +
                        "<td>"+data[i].parameterType+"</td>" +
                        "<td><input id ='"+data[i].parameterId+"' type=\"text\" name='title' lay-verify=\"title\" autocomplete=\"off\" class=\"layui-input\" value=''></td>" +
                        "</tr>";
                    arrayText.push(data[i].parameterId);
                }
                param+="<tr>" +
                    "<td style='font-weight:bold'>返回类型</td>" +
                    "<td colspan='3'>HTML</td>" +
                    "</tr>" +
                    "<td colspan='4'><button onclick='getbutt()' type='button' class='layui-btn layui-btn-normal'>生成并执行URL</button></td>" +
                    "<tr>" +
                    "<td style='font-weight:bold' colspan='1'>URL</td>" +
                    "<td colspan='3'>" +
                    "<textarea style='width: 99%; height: 66px; margin: 2px 0px;' id='URL'></textarea>" +
                    "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td style='font-weight:bold' colspan='1'>返回结果</td>" +
                    "<td colspan='3' style='height: 350'>" +
                    "<iframe style='width: 100%;height: 100%;' src='' id='note'></iframe>" +
                    "</td>" +
                    "</tr>";
                $("#formTable").html(param);
            }
        });
    }
    function getbutt() {
        var text="";
        for (var j = 0; j <arrayText.length ; j++) {
            text += arrayText[j]+"="+$("#"+arrayText[j]).val()+"&";
        }
        var url="http://10.172.89.55/cimiss-web/api?userId=BEXA_YGZX_sxnqzx&pwd=81619505&interfaceId="+intfaceId+"&";
        url = url+text+"dataFormat=html";
        $("#URL").val(url );
        document.getElementById("note").src=url;

    }
</script>
</html>

<%--
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>树结构管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/layui-v2.5.5/layui/layui.all.js" type="text/javascript" charset="utf-8"></script>
	<link href="${ctxStatic}/layui-v2.5.5/layui/css/layui.css" rel="stylesheet" type="text/css"/>
	<style type="text/css">
		li{
			display:inline;   //一行显示
		float:left;    //靠左显示
		}
		a {
			color: #2fa4e7;
		}
	</style>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, ids = [], rootIds = [];
			for (var i=0; i<data.length; i++){
				ids.push(data[i].id);
			}
			ids = ',' + ids.join(',') + ',';
			for (var i=0; i<data.length; i++){
				if (ids.indexOf(','+data[i].parentId+',') == -1){
					if ((','+rootIds.join(',')+',').indexOf(','+data[i].parentId+',') == -1){
                        rootIds.push(data[i].parentId);
                    }
				}
			}
			for (var i=0; i<rootIds.length; i++){
				addRow("#treeTableList", tpl, data, rootIds[i], true);
			}
			$("#treeTable").treeTable({expandLevel : 5});

		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${fns:jsGetVal('row.parentId')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
						blank123:0}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/interf/sysInterface/">API列表</a></li>
		<li><a href="${ctx}/interf/sysInterface/form">API添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="sysInterface" action="${ctx}/interf/sysInterface/" method="post" class="breadcrumb form-search">
		<div class="ul-form">
			<li><label>名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;&nbsp;&nbsp;
            <input id="import" class="btn btn-primary" type="button" value="导入"/>&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="${ctx}/interf/sysInterface/download?fileName=API接口模板.xls" download="API接口模板.xls">
                <input id="downloads" class="btn btn-primary" type="button" value="模板下载"/>
            </a>
			</li>
			<li class="clearfix"></li>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>名称</th>
				<th>编码分类</th>
				<th>创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/interf/sysInterface/form?id={{row.id}}">
				{{row.name}}
			</a></td>
			<td>
				{{row.dataEncoding}}
			</td>
			<td>
				{{row.createDate}}
			</td>
			<td>
   				<a href="${ctx}/interf/sysInterface/form?id={{row.id}}">修改</a>
				<a href="${ctx}/interf/sysInterface/delete?id={{row.id}}" onclick="return confirmx('确认要删除该树结构及所有子树结构吗？', this.href)">删除</a>
				&lt;%&ndash;<c:if test="${row.sort eq '4'}">&ndash;%&gt;
				<a href="${ctx}/interf/sysInterface/form?parent.id={{row.id}}">添加下级目录</a>
				&lt;%&ndash;</c:if>&ndash;%&gt;
			</td>
		</tr>
	</script>
	<script type="text/javascript">
        layui.use('upload', function(){
            var upload = layui.upload;
            upload.render({
                elem: '#import'
                ,url: '/interf/sysInterface/import'
                ,accept: 'file'//普通文件
            });
        });
	</script>
</body>
</html>--%>
