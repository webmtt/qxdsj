<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">	
	$(document).ready(function(){
		$("#inputForm").validate({
			submitHandler: function(form){
				/* loading('正在提交，请稍等...'); */
				form.submit();
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
		$("#mobile").on("blur",function(){
			var tell=$(this).val();
			console.log(tell)
			$.ajax({
				url:"${ctx}/user/checkUserTell",
				type:"post",
				data:{"userName":tell,"id":$("#iD").val()},
				success:function(data){
					console.log(data)
					if(data==1){
						$("#mobileBox").addClass("error");
						if($("#mobileBox label").length>0){
							$("#mobileBox label").text("您的手机号已存在，请修改").show()
						}else{
							$("<label for='mobile' class='error'>您的手机号已存在，请修改</label>").appendTo($("#mobileBox"))
						}
					}else{
						$("#mobileBox").removeClass("error");
					}
				}
			})
		})
		$("#psecond").hide();
			$("#pthird").hide();
			$("#s2id_psecond").hide();
			$("#s2id_pthird").hide();
		$("#pfirst").change(function(){
			var pid=$("#pfirst option:selected").val();
			$("#psecond").hide();
			$("#s2id_psecond span").empty();
			$("#pthird").hide();
			$("#s2id_pthird").hide();
			$("#s2id_pthird span").empty();
			if(pid!=undefined&&pid!="000"){
				findOrgList("psecond",pid);	
				$("#orgID").attr("value",pid);
			}else{
				$("#psecond").empty();
				$("#s2id_psecond span").empty();
				$("#s2id_pthird").hide();
				$("#s2id_pthird span").empty();
			}
			saveValue();
		});
		$("#psecond").change(function(){
			$("#s2id_pthird span").empty();
			var pid=$("#psecond option:selected").val();
			$("#pthird").hide();
			$("#s2id_pthird").hide();
			if(pid!=undefined){
				findOrgList("pthird",pid);	
				$("#orgID").attr("value",pid);
			}
			saveValue();
		});
		$("#pthird").change(function(){
			var pid=$("#pthird option:selected").val();
			saveValue();
		});
		$("#serviceLevelList").change(function(){
			var servicelevel=$("#serviceLevelList option:selected").val();
			$("#servicelevel").attr("value",servicelevel);
		});
		function findOrgList(listOrder,pid){
			$.ajax({
				url:'${ctx}/org/orgListByPid',
				async:false,
				type:'POST',
				data:{'pid':pid},
				dataType:'json',
				success:function(result){
					var html="";
					if(listOrder=="second"){
						$("#second").empty();
						if(result.list!=undefined&&result.list!=""){
							$.each(result.list,function(i,n){
								html+="<option value="+n.id+">"+n.name+"</option>";
							});
							$("#second").html(html);
							$("#second").show();
							$("#s2id_second").show();
							$("#orgID").attr("value","");
						}
					}else if(listOrder=="psecond"){
						html = "<option value=''>请选择</option>";
						$("#psecond").empty();
						if(result.list!=undefined&&result.list!=""){
						$.each(result.list,function(i,n){
							html+="<option value="+n.id+">"+n.name+"</option>";
						});
						$("#psecond").html(html);
						$("#psecond").show();
						$("#s2id_psecond").show();
						$("#orgID").attr("value","");
						}
					}else if(listOrder=="third"){
						$("#third").empty();
						if(result.list!=undefined&&result.list!=""){
						$.each(result.list,function(i,n){
							html+="<option value="+n.id+">"+n.name+"</option>";
						});
						$("#third").html(html);
						$("#third").show();
						$("#s2id_third").show();
						$("#orgID").attr("value","");
						}
					}else if(listOrder=="pthird"){
						$("#pthird").empty();
						if(result.list!=undefined&&result.list!=""){
						$.each(result.list,function(i,n){
							html+="<option value="+n.id+">"+n.name+"</option>";
						});
						$("#pthird").html(html);
						$("#pthird").show();
						$("#s2id_pthird").show();
						$("#orgID").attr("value","");
						}
					}
				}
			});
		}
		function saveValue(){
			var pid;
			if($("#nativeList").is(':visible')){
				if($("#first").is(':visible')){
					if($("#second").is(':visible')){
						if($("#third").is(':visible')){
							pid=$("#third option:selected").val();								
						}else{
							pid=$("#second option:selected").val();	
						}
					}else{
						pid=$("#first option:selected").val();	
					}
				}				
			}else if($("#nonativeList").is(':visible')){
					if($("#psecond").is(':visible')){
						if($("#pthird").is(':visible')){
							pid=$("#pthird option:selected").val();	
						}else{
							pid=$("#psecond option:selected").val();	
						}
					}else{
						pid=$("#pfirst option:selected").val();	
					}
			}
				$("#orgID").attr("value",pid);				
		}
		
		var unitIds = $("#unitIds").val();
		var unitIdArray  = unitIds.split("-");
		//非国家级
			$("#pfirst option[value='"+unitIdArray[0]+"']").attr("selected","selected");
			$("#pfirst").change();
			$("#psecond option[value='"+unitIdArray[1]+"']").attr("selected","selected");
			$("#psecond").change();
			if(unitIdArray.length>2){
				$("#pthird option[value='"+unitIdArray[2]+"']").attr("selected","selected");
				$("#pthird").change();
			}
	})
	function check(){
		var position=$("#user_position").val();
		if(position==undefined||position==""||position=="0"){
			alert("请选择职位");
			return false;
		}
		var job=$("#user_jobtitle").val();
		if(job==undefined||job==""||job=="0"){
			alert("请选择职称");
			return false;
		}
		var age=$('input:radio[name="user_agerange"]:checked').val();
		if(age==undefined||job==null){
			alert("请选择年龄段");
			return false;
		}
		var phonemodel=$('input:radio[name="user_phonemodel"]:checked').val();
		if(phonemodel==undefined||phonemodel==null){
			alert("请选择手机型号");
			return false;
		}
		var status=$("#mobileBox").hasClass("error");
		if(status){
			alert("您的手机号已注册，请重新输入")
			return false;
		}
		var orgID=$("#orgID").val();
		if(orgID==undefined||orgID==""||orgID=="000"){
			alert("请选择工作单位");
			return false;
		}
		return true;
	}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/user/userList">用户列表</a></li>
		<li><a href="${ctx}/user/addUserPage">添加用户</a></li>
		<li class="active"><a href="${ctx}/user/addUserAppPage">${not empty userInfo.iD?'修改':'添加'}APP用户</a></li>
	</ul><br/>
	<input type="hidden" id="unitIds" value="${unitIds }">
	<form:form id="inputForm" modelAttribute="userInfo" action="${ctx}/user/addAppUser" method="post" class="form-horizontal">
		<tags:message content="${message}"/>
		<form:hidden path="iD" id="iD"/>
		<form:hidden path="orgID" id="orgID" value="${userInfo.orgID }"/>
		<form:hidden path="regSource" id="regSource"/>
		<div class="control-group">
			<label class="control-label"><span style="color: red;margin:0px 6px;">*</span>姓名:</label>
			<div class="controls">
				<form:input path="chName"  htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color: red;margin:0px 6px;">*</span>手机:</label>
			<div class="controls" id="mobileBox">
				<form:input path="mobile"  htmlEscape="false" maxlength="11" class="required mobile" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color: red;margin:0px 6px;">*</span>省份:</label>
			<div class="controls">
				<form:input path="province" id="province" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label"><span style="color: red;margin:0px 6px;">*</span>单位:</label>
			<div class="controls">
				<form:input path="company" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div> --%>
		 <div class="control-group">
			<label class="control-label"><span style="color: red;margin:0px 6px;">*</span>工作单位:</label>
				<div class="controls" id="nonativeList">				
				<select id="pfirst">
					<option value="000">请选择</option>
					<c:forEach items="${poneList}" var="orgone">
						<option value="${orgone.id}">${orgone.name}</option>					
					</c:forEach>
				</select>
				<select id="psecond">
						<option value="000"></option>					
				</select>
				<select id="pthird">
						<option value="000"></option>					
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color: red;margin:0px 6px;">*</span>职位:</label>
			<div class="controls" id="nativeList3">				
				<select id="user_position" name="user_position">
					<option value="0">请选择</option>
					<c:forEach items="${positionList}" var="orgone" varStatus="status">
					      <option value="${orgone.value}" <c:if test="${orgone.value == userInfo.position}">selected</c:if>>${orgone.label}</option>	
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color: red;margin:0px 6px;">*</span>职称:</label>
			<div class="controls" id="nativeList3">				
				<select id="user_jobtitle" name="user_jobtitle">
					<option value="0">请选择</option>
					<c:forEach items="${JobList}" var="orgone">
						<option value="${orgone.value}" <c:if test="${orgone.value == userInfo.jobTitle}">selected</c:if>>${orgone.label}</option>					
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color: red;margin:0px 6px;">*</span>年龄:</label>
			<div class="controls">
				<c:forEach items="${ageList}" var="orgone" varStatus="status">
				    <c:if test="${ status.index ==0}">
				      <input type="radio" value="${orgone.value}" name="user_agerange" checked="checked">${orgone.label}	
				    </c:if>
				    <c:if test="${ status.index !=0}">
				      <input type="radio" value="${orgone.value}" name="user_agerange" <c:if test="${orgone.value == userInfo.ageRange}">checked="checked"</c:if>>${orgone.label}	
				    </c:if>
								
				</c:forEach>
		<!-- 		<input type="radio" value="1" name="user_agerange"	>18-25
				<input type="radio" value="2" name="user_agerange">30-40 -->
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color: red;margin:0px 6px;">*</span>手机类型:</label>
			<div class="controls">
				<c:forEach items="${phoneList}" var="orgone" varStatus="status">
				  <c:if test="${ status.index ==0}">
					<input type="radio" value="${orgone.value}" name="user_phonemodel" checked="checked">${orgone.label}
					</c:if>
					<c:if test="${ status.index !=0}">
					<input type="radio" value="${orgone.value}" name="user_phonemodel" <c:if test="${orgone.value == userInfo.phoneModel}">checked="checked"</c:if>>${orgone.label}
					</c:if>				
				</c:forEach>
				<!-- <input type="radio" value="1" name="user_phonemodel"	>IOS
				<input type="radio" value="2" name="user_phonemodel">安卓 -->
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">办公电话:</label>
			<div class="controls">
				<form:input path="phone" id="phone" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">微信号:</label>
			<div class="controls">
				<form:input path="wechatNumber" htmlEscape="false"  maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span style="color: red;margin:0px 6px;">*</span>服务级别:</label>
			<div class="controls">
				<select id="serviceLevelList">
					<c:forEach items="${serviceLevelList}" var="serviceLevel">
						<option value="${serviceLevel.servicelevel}" <c:if test="${userInfo.servicelevel == serviceLevel.servicelevel}">selected="selected"</c:if>>
							${serviceLevel.servicelevel}级（${serviceLevel.subordernum}个子订单，${serviceLevel.filesumsize}M下载量）
						</option>	
					</c:forEach>
				</select>
				<form:hidden path="servicelevel" id="servicelevel"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" onclick="return check()" value="保 存"/>
		</div>
	</form:form>
	
</body>
</html>