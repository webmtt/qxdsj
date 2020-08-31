<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>新增角色用户</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#userList").change(function(){
				var optionValue = $("#userList option:selected").val();
				if(optionValue != "0"){
					$("#userid").attr("value",optionValue);
				}
			});
			$("#dataroleList").change(function(){
				var optionValue = $("#dataroleList option:selected").val();
				if(optionValue != "0"){
					$("#roleid").attr("value",optionValue);
				}
			});
			
			$(".select2-input").each(function(){
				$(this).keyup(function(){
					var url = "${ctx}/userAndDataRole/searchUser";
					var params = {"userCodeOrName":$(this).val()};
					$.post(url, params, function(result){
						var userList = eval(result);
						if(userList.length == 0) {
							$("#userList").html();
							$(".select2-results").each(function(){
								$(this).html();
							});
						} else {
							$("#userList").html("<option value='0'>用户名（账号）</option>");
							$(".select2-results").each(function(){
								$(this).html("<li class='select2-results-dept-0 select2-result select2-result-selectable'><div class='select2-result-label'>用户名（账号）</div></li>");
							});
							for(var i=0; i<userList.length; i++){
								$("#userList").append("<option value='"+userList[i].iD+"'>"+userList[i].chName+"（"+userList[i].userName+"）</option>");
								$(".select2-results").each(function(){
									$(this).append("<li class='select2-results-dept-0 select2-result select2-result-selectable'><div class='select2-result-label' onclick='selectLi(this,\""+userList[i].iD+"\")'>"+userList[i].chName+"（"+userList[i].userName+"）</div></li>");
								});
							}
						}
					});
				})
			})
		});
		
		function selectLi(data,userId) {
			//alert($(data).text()+"    "+userId);
			//$(".select2-choice:first").html($(data).text());
			$(".select2-choice:first").html("<span>"+$(data).text()+"</span><abbr class='select2-search-choice-close'></abbr><div><b><b/></div>");
			$('#userList option').each(function(){
				if($(this).val() == userId) {
					$(this).attr("selected","selected");
				}
			});
			$('#userList').trigger("change");
		}
		
		function validata() {
			var userid = $("#userList option:selected").val();
			if(userid == "0") {
				alert("请选择用户");
				return false;
			};
			var roleid = $("#dataroleList option:selected").val();
			if(roleid == "0") {
			alert("请选择角色");
				return false;
			}
		}
	</script>
</head>
<body>
	<div>
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/userAndDataRole/list">角色用户列表</a></li>
			<li class="active"><a href="javascript:void(0)">新增角色用户</a></li>
		</ul>
	</div>
	<tags:message content="${message}"/>
	<form id="inputForm" modelAttribute="user" action="${ctx}/userAndDataRole/save" method="post" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">用户:</label>
			<div class="controls">
				<select id="userList" style="width: 300px">
					<option value="0">用户名（账号）</option>
					<c:forEach items="${userList }" var="user"> 
						<option value="${user.iD }"
						<c:if test="${user.iD eq userDatarole.userId}">
							selected="selected"
						</c:if>
						>${user.chName}（${user.userName}）</option>
					</c:forEach>
				</select>
			</div>
			<input type="hidden" name="userId" id="userid">
		</div>
		<div class="control-group">
			<label class="control-label">角色:</label>
			<div class="controls">
				<select id="dataroleList" style="width: 300px">
					<option value="0">角色名（编码）</option>
					<c:forEach items="${dataroleList }" var="role"> 
						<option value="${role.dataroleId }" 
						<c:if test="${role.dataroleId eq userDatarole.dataroleId}">
							selected="selected"						
						</c:if>
						>${role.dataroleName}（${role.dataroleId}）</option>
					</c:forEach>
				</select>
			</div>
			<input type="hidden" name="dataroleId" id="roleid">
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="return validata()"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
	
</body>
</html>