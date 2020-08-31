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
			height: 4%;
			float: left;
			text-align: center;
		}

	</style>
	<title>MUSIC - 接口清单</title>
	<script src="${ctxStatic}/layui-v2.5.5/layui/layui.all.js" type="text/javascript" charset="utf-8"></script>
	<link href="${ctxStatic}/layui-v2.5.5/layui/css/layui.css" media="all" rel="stylesheet" type="text/css"/>
	<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
	<link href="${ctxStatic}/layui-v2.5.5/multiple-select.css" rel="stylesheet" type="text/css"/>
	<script src="${ctxStatic}/layui-v2.5.5/multiple-select.js" type="text/javascript"></script>

</head>

<body>
<%--<div style='width:2px;border:0.2px solid #69bef3;float:left;height:100%;'></div>--%>
<%--<div class="left" style="width: 16%;height: 100%;float:left">
	<div id="bg" >
		<div class="leftImge" style="background-color: #47afea;height: 27px;">
			<label style="font-size: 17px;color: #f9f9fd;">资料清单</label>
		</div>
		<form:form id="searchForm" modelAttribute="interList" action="${ctx}/interdata/interfaceData/interfa/" method="post" class="breadcrumb form-search">
            <span class="layui-breadcrumb" lay-separator="|">
                <c:forEach items="${interList}" var="interfaceData">
						<a class="inter" href="#bgg" onclick="method('${interfaceData.dataClassId}','${interfaceData.dataClassName}')">${interfaceData.dataClassName}</a>
					&lt;%&ndash;<a class="inter" href="#bgg" onclick="method('${interfaceData.dataClassId}','${interfaceData.dataClassName}')">${interfaceData.dataClassName}</a>&ndash;%&gt;
				</c:forEach>
            </span>
		</form:form>
	</div>
	<div style="background-color: #69bef3;height: 27px;margin-top: 72%;">
		<label id ="inters" style="font-size: 17px;color: #f9f9fd;"></label>
	</div>
	<div id="bgg"style="height:66%;width:100%;overflow:auto">
	</div>
</div>--%>
<%--<div style='width:2px;border:0.2px solid #69bef3;float:left;height:100%;'></div>--%>
<div id="rightdata" style="width: 100%;height: 50%;">
	<div>
		<p>
			<span style="margin-left: 5px; margin-top: 2px; font-size: 16px; font-weight: bold;">接口服务</span>
		</p>
	</div>
	<hr style="widows: 100%;">
	<table class="layui-table" id="formTable">
		<tr>
			<td style="font-weight:bold;font-size: 15px" colspan="1">资料类别</td>
			<td colspan="3">
				<select id="projectcd" style="width: 600px;height: 30px;">
				</select>
			</td>
		</tr>
		<tr>
			<td style="font-weight:bold;font-size: 15px" colspan="1">资料名称</td>
			<td colspan="3">
				<select id="projectcdname" style="width: 600px;height: 30px;">
				</select>
			</td>
		</tr>
		<tr>
			<td style='font-weight:bold;font-size: 15px' colspan='1'>访问接口</td>
			<td colspan='3'>
				<select id="projectcdinter" style="width: 600px;height: 30px;">
				</select>
			</td>
			</tr>
		<tr>
		<td style='background-color:#b0fdff;font-weight:bold;font-size: 15px' colspan='4'>接口参数</td>
		</tr>
		<tr>
			<td style="font-weight:bold;">参数id</td>
			<td style="font-weight:bold;">参数名称</td>
			<td style="font-weight:bold;">参数类型</td>
			<td style="font-weight:bold;">参数说明</td>
		</tr>
	</table>
</div>
</body>
<script>
    /*初始化加载项目选择框*/
	var getdata = function(cd) {
        $.ajax({
            type: 'post',
            data:{"id":cd},
            url: '${ctx}/interdata/interfaceData/interface',
            dataType: 'json',
            success: function (data) {
                var opts = "";
                $.each(data, function (i) {
                    var values = data[i].dataCode+"("+data[i].dataName+")";
                    opts += "<option value='" + data[i].dataCode + "'>" + values + "</option>";
                });
                /*给下拉框付值*/
                $("#projectcdname").append(opts);
                if(data[0] && data[0].dataCode) {
                    $("#projectcdinter").find("option").remove();
                    getinter(data[0].dataCode)
                }
            },
            error: function () {
                alert('菜单加载异常!');
            }
        })
	};
    var getinter = function(id) {
        $.ajax({
            type: 'post',
            data:{"id":id},
            url: '${ctx}/interdata/interfaceData/projectcdinter',
            dataType: 'json',
            success: function (data) {
                var opts = "";
                $.each(data, function (i) {
                    var values = data[i].CUSTOM_API_ID+"("+data[i].name+")";
                    opts += "<option value='" + data[i].CUSTOM_API_ID + "'>" + values + "</option>";
                });
                /*给下拉框付值*/
                $("#projectcdinter").append(opts);
                if(data[0] && data[0].CUSTOM_API_ID) {
                    $("tr[id=papam]").remove();
                    getCanname(data[0].CUSTOM_API_ID)
                }
            },
            error: function () {
                alert('菜单加载异常!');
            }
        })
    };
    var getCanname = function(id) {
        $.ajax({
            type: 'post',
            data:{"id":id},
            url: '${ctx}/interdata/interfaceData/interElement',
            dataType: 'json',
            success: function (data) {
                var opts = "";
                $.each(data, function (i) {
                    var list = data[i].interList;
                        $.each(list, function (j) {
							opts +="<tr id='papam'>"
							opts +=
							"<td>"+list[j].PARAM_ID+"</td>" +
							"<td>"+list[j].PARAM_NAME+"</td>" +
							"<td>"+list[j].IS_SELF_DEFINE+"</td>" +
							"<td>"+list[j].VALUE_FORMAT+"</td>";
                    		opts +='</tr>'
                        });
                });
                $('#formTable').find('tbody').append(opts)
				// console.log('$(\'#formTable\').children(\'tr\')=', )
                // $("#element").html(opts);
            },
            error: function () {
                alert('菜单加载异常!');
            }
        })
    };
    $(function () {
        $.ajax({
            type: 'post',
            url: '${ctx}/interdata/interfaceData/interfams',
            dataType: 'json',
            success: function (data) {
                var opts = "";
                $.each(data, function (i) {
                    var values = data[i].dataClassId+"("+data[i].dataClassName+")";
                    opts += "<option value='" + data[i].dataClassId + "'>" + values + "</option>";
                });
                /*给下拉框付值*/
                $("#projectcd").append(opts);
                if(data[0] && data[0].dataClassId) {
                    getdata(data[0].dataClassId)
				}


            },
            error: function () {
                alert('菜单加载异常!');
            }
        });
        $("#projectcd").change(function(){
            var cd=$("#projectcd").val();
            $("#projectcdname").find("option").remove();
			getdata(cd);

        });
        $("#projectcdname").change(function(){
            var cd=$("#projectcdname").val();
            $("#projectcdinter").find("option").remove();
            getinter(cd);

        });
        $("#projectcdinter").change(function(){
            var cd=$("#projectcdinter").val();
            $("tr[id=papam]").remove();
            getCanname(cd);

        });
    });
</script>
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
            url:"${ctx}/interdata/interfaceData/interface",
            data:{id:id},
            dataType:"json",
            success:function (data) {
                console.log(data)
                var showState="";
                var showSt="";
                $.each(data, function (i) {
                    showState+="<a href='#rightdata' style='color:#3d8bb1' onclick=getInter("+"'"+id+"'"+",'"+data[i].dataName+"'"+",'"+name+"'"+")>"+data[i].dataName+"</a><br>" +
                        "<hr style=\"height:1px;border:none;border-top:1px dashed #12c1c4;\" />";
                    showSt=data[i].dataType;
                });
                document.getElementById("inters").innerHTML=name;
                $("#bgg").html(showState);

            }
        });
    }
</script>
</html>