<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>用户审核</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/dialog.jsp" %>
    <script src="${ctxStatic}/ultra/js/user.js?v=1" type="text/javascript"></script>
    <script type="text/javascript">
        $(function () {
            $("#myModal").hide();
            $("#dataroleModel").hide();
            $("#reason").hide();
            // $("#content").hide();
            $("#btnSubmit").click(function () {
                var id = $("#iD").val();
                var content = $("#content").val();
                var isActive = $("input[type='radio'][name='status']:checked").val();
                var iscreate = $("input[type='radio'][name='iscreat']:checked").val();
                var selectDataRole=null;
                if("1"==isActive&&${listRole==null}){
                    var obj = document.getElementsByName("datarole");
                    var check_val = [];
                    for (k in obj) {
                        if (obj[k].checked)
                            check_val.push(obj[k].value);
                    }
                    if(check_val.length==0){
                        alert('请选择数据角色！');
                        return;
                    }
                    selectDataRole=JSON.stringify(check_val);
                }

                var areaId = $("input[type='checkbox'][id='r']:checked");
                var url = "";
                var i = 1;
                $(areaId).each(function () {
                    if (i < areaId.length) {
                        url += this.attributes['value'].value + "=" + this.attributes['value'].value + "&";
                    } else {
                        url += this.attributes['value'].value + "=" + this.attributes['value'].value;
                    }
                    i++;
                });
                $("#form").attr("action", projectName + "/user/activeUser?id=" + id + "&content=" + content + "&isActive=" + isActive + "&iscreate="+iscreate+"&selectDataRole="+selectDataRole+"&" + url);
                $("#form").submit();

            });
        });

        function selectDataroleById(id, datarolename) {

            $.ajax({
                url: '${ctx}/dataRole/viewDataRole',
                type: 'POST',
                data: {'id': id},
                dataType: 'html',
                success: function (result) {
                    $("#dataroleInfo").empty();
                    $("#dataroleInfo").html(result);
                }
            });
            $("#myModalLabel").html(datarolename + "信息");
        }
        function selectMuiscInfoById(id, datarolename) {

            $.ajax({
                url: '${ctx}/musicuser/musicInfo/view',
                type: 'POST',
                data: {'id': id},
                dataType: 'html',
                success: function (result) {
                    $("#dataroleInfo").empty();
                    $("#dataroleInfo").html(result);
                }
            });
            $("#myModalLabel").html(datarolename + "信息");
        }
        function closeDataRoleModel() {
            $(".close").click();
        }

        function showBigImg() {
            $("#myModal").show();
        }

        function showbox(item) {
            if ("1" == item) {
                $("#reason").hide();
                $("#content").hide();
            }
            if ("2" == item) {
                $("#reason").show();
                $("#content").show();
            }
            // var areaId = $("input[type='checkbox'][id='r']:checked");
            // var flag = false;
            // $(areaId).each(function () {
            //     if (this.attributes['value'].value == 'others') {
            //         flag = true;
            //     }
            // });
            // if (flag) {
            //     $("#content").show();
            // } else {
            //     $("#content").hide();
            // }
        }
        //获取数据角色
        function getDataRole() {
            $("#isShow").attr("style","display:show();");
            $.ajax({
                url: '${ctx}/dataRole/getDataRoleInfo',
                async: false,
                type: 'POST',
                dataType: 'json',
                success: function (result) {
                    if (result != "" && result.length > 0) {
                        var info = "";
                        for (var i in result) {
                            var flag = false;
                            if (flag) {
                                info += "<input type='checkbox' value='" + result[i].dataroleId + "' name='datarole' checked='" + flag + "'/><span>" + result[i].dataroleName + "</span>";
                            } else {
                                info += "<input type='checkbox' value='" + result[i].dataroleId + "' name='datarole'/><span>" + result[i].dataroleName + "</span>";
                            }
                            $("#roleData").html(info);

                        }

                    }
                }
            });
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/user/userList">用户列表</a></li>
    <li class="active"><a href="javascript:void(0)">用户审核</a></li>
</ul>
<tags:message content="${message}"/>
<form:form id="form" method="post" class="form-horizontal">
    <input type="hidden" id="iD" name="iD" value="${UserInfo.iD}">
    <div class="control-group">
        <label class="control-label">登录名:</label>
        <div class="controls">
            <label>${UserInfo.userName }</label>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">姓名:</label>
        <div class="controls">
            <label>${UserInfo.chName }</label>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">邮箱:</label>
        <div class="controls">
            <label>${UserInfo.userName }</label>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">办公电话:</label>
        <div class="controls">
            <label>${UserInfo.phone }</label>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">工作单位:</label>
        <div class="controls">
            <label>
                    ${UserInfo.orgID } </label>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">music账户:</label>
        <div class="controls">
            <c:choose>
                <c:when test="${empty musicInfo}">
                    <label title="请添加music账户"><a href="${ctx}/musicuser/musicInfo/form" style="color: red">添加账户</a></label>
                </c:when>
                <c:otherwise>
                    <label style="color:green"><a href="javascript:void(0);" data-toggle="modal" data-target="#dataroleModel"
                                                  onclick="selectMuiscInfoById('${musicInfo.id}','${musicInfo.name}')">${musicInfo.name}</a></label>

                </c:otherwise>
            </c:choose>

        </div>
    </div>
<%--    <div class="control-group">--%>
<%--        <label class="control-label">职位:</label>--%>
<%--        <div class="controls">--%>
<%--            <label>${position }</label>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    <div class="control-group">--%>
<%--        <label class="control-label">职称:</label>--%>
<%--        <div class="controls">--%>
<%--            <label>${jobtitle }</label>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    <div class="control-group">--%>
<%--        <label class="control-label">年龄范围:</label>--%>
<%--        <div class="controls">--%>
<%--            <label>${agerange }</label>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    <div class="control-group">--%>
<%--        <label class="control-label">手机类型:</label>--%>
<%--        <div class="controls">--%>
<%--            <label>${phonemodel }</label>--%>
<%--        </div>--%>
<%--    </div>--%>

    <div class="control-group">
        <label class="control-label">微信号:</label>
        <div class="controls">
            <label>${UserInfo.wechatNumber }</label>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">手机号:</label>
        <div class="controls">
            <label>${UserInfo.mobile }</label>
        </div>
    </div>
<%--    <div class="control-group">--%>
<%--    <label class="control-label">用户类型:</label>--%>
<%--    <div class="controls">--%>
<%--        <label>${fns:getDictLabel(UserInfo.userType,'userType', '')}</label>--%>
<%--    </div>--%>
<%--</div>--%>
    <%--  		<div class="control-group">--%>
    <%--			<label class="control-label">用户级别:</label>--%>
    <%--			<div class="controls">--%>
    <%--				<label>--%>
    <%--				<c:if test="${UserInfo.userRankID eq 2}">--%>
    <%--				国家级用户--%>
    <%--				</c:if>--%>
    <%--				<c:if test="${UserInfo.userRankID eq 1}">--%>
    <%--				非国家级用户--%>
    <%--				</c:if>					--%>
    <%--				</label>--%>
    <%--			</div>--%>
    <%--		</div>--%>
<%--    <div class="control-group">--%>
<%--        <label class="control-label">证件照/协议:</label>--%>
<%--        <div class="controls">--%>
<%--                &lt;%&ndash; <div style="width: 8.6cm;height: 5.4cm;" onclick="showBigImg()">--%>
<%--                    <img  src="${ctx}/user/getCard?id=${UserInfo.iD}" data-toggle="modal" data-target="#myModal" style="width: 8.6cm;height: 5.4cm;border: 1px solid #f1f1f1;cursor:pointer;"  title="单击可放大图片">--%>
<%--                </div> &ndash;%&gt;--%>
<%--            <a href="${ctx}/user/getCard?id=${UserInfo.iD}" target="_blank"> 点击查看</a>--%>
<%--        </div>--%>
<%--    </div>--%>
    <div class="control-group">
        <c:choose>
            <c:when test="${listRole!=null}">
                <label class="control-label">数据获权:</label>
                <div class="controls">
                    <c:forEach items="${listRole}" var="datarole">
                    <span><a href="javascript:void(0);" data-toggle="modal" data-target="#dataroleModel"
                             onclick="selectDataroleById('${datarole.dataroleId}','${datarole.dataroleName}')">${datarole.dataroleName}</a>&nbsp;&nbsp;</span>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <label class="control-label">数据资料:</label>
                <div class="controls">
                    <label>${UserInfo.dataInfo}</label>
                    <span>
                        <a href="javascript:void(0);" onclick="getDataRole()">关联数据角色</a>
                    </span>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="control-group" id="isShow" style="display: none;">
        <label class="control-label">已有数据角色:</label>
        <div class="controls" id="roleData">

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">创建产品库:</label>
        <div class="controls">
            <input type="radio" name="iscreat" value="1" >是
            <input type="radio" name="iscreat" value="0" checked="checked">否
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">审核状态:</label>
        <div class="controls">
            <input type="radio" name="status" value="1" onclick="showbox('1')" checked="checked">通过
            <input type="radio" name="status" value="2" onclick="showbox('2')">未通过
        </div>
    </div>
    <div class="control-group" id="reason">
        <label class="control-label">原因:</label>
        <div class="controls">
<%--            <input type="checkbox" id="r1" value="name">姓名不正确<br/>--%>
<%--            <input type="checkbox" id="r2" value="org">工作单位不正确<br/>--%>
<%--            <input type="checkbox" id="r3" value="email">邮箱不存在<br/>--%>
<%--            <input type="checkbox" id="r4" value="photo">照片不清晰<br/>--%>
<%--            <input type="checkbox" id="r5" value="protocal">请上传持工作证照片或有单位盖章的在职证明，谢谢！<br/>--%>
<%--            <input type="checkbox" id="r6" value="others" onclick="showbox('others')">其它<br/>--%>
            <textarea rows="3" cols="1" id="content"></textarea>
        </div>
    </div>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" style="width: 450px;">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close" style="margin-top: -2px;margin-right: 0px;">
                    <span aria-hidden="true">&times;</span>
                </button>
                <div class="modal-body">
                    <img src="${ctx}/user/getCard?id=${UserInfo.iD}" style="width: 11cm;height:7cm;">
                </div>
            </div>
        </div>
    </div>
    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="button" value="提交"/>&nbsp;
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
<div class="modal fade" id="dataroleModel" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" style="width: 350px">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header"
                 style="height: 30px;vertical-align:middle;padding-bottom: 2px;padding-top: 5px;">
                <table>
                    <tr>
                        <td><h4 class="modal-title" id="myModalLabel" style="float: left;width: 309px;"></h4>
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
            <div class="modal-body" id="dataroleInfo">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="closeDataRoleModel()">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
