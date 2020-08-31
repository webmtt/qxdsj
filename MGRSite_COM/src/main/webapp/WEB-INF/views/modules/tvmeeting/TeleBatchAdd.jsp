<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>    
    <title>会商直播添加页面</title>
   <meta name="decorator" content="default"/>
   <script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
   <script type="text/javascript">
  function check(){
	  /* 	alert("1"); */
		var BeginDate=$("#BeginDate").val();
		var endDate=$("#endDate").val();
		var beginTime=$("#BeginTime").val();
		var endTime=$("#EndTime").val();
		var content=$("#Content").val();
		var MeetTitle=$("#MeetTitle").val();
		var OrganizationUni=$("#OrganizationUni").val();
		var Contacts=$("#Contacts").val();
		var ContactWay=$("#ContactWay").val();
		var MeetRange=$("input[name=MeetRange]");
		if(""==BeginDate||null==BeginDate){
			
			alert("开始日期不能为空");
			return false;
		}else if(""==endDate||null==endDate){
			alert("结束日期不能为空");
			return false;
		}else if(endDate<BeginDate){
			alert("结束日期不能小于开始日期");
			return false;
		}else if(""==beginTime||null==beginTime){
			alert("开始时间不能为空");
			return false;
		}else if(""==endTime||null==endTime){
			alert("结束时间不能为空");
			return false;
		}else if(endTime<=beginTime){
			alert("结束时间不能小于或等于开始时间");
			return false;
		}else if(""==content||null==content){
			alert("内容不能为空");
			return false;
		}else if(""==MeetRange||null==MeetRange||"undefined"==MeetRange){
			alert("会议召开范围不能为空");
	     	return false;
		}else if(""==OrganizationUni||null==OrganizationUni){
			alert("组织单位不能为空");
			return false;
		}else if(""==Contacts||null==Contacts){
			alert("联系人不能为空");
			return false;
		}else if(""==ContactWay||null==ContactWay){
			alert("联系方式不能为空");
			return false;
		}else if(""==MeetTitle||null==MeetTitle){
			alert("会议名称不能为空");
			return false;
		}else{
			return true;
		}
	}
	   
   </script>
  </head>
  
  <body>
   <ul class="nav nav-tabs">
		<li><a href="${ctx}/tvmeeting/meetingarrange/teleList">会商列表</a>
		<li><a href="${ctx}/tvmeeting/meetingarrange/teleAdd">新增会商</a></li>
		<li class="active"><a href="${ctx}/tvmeeting/meetingarrange/teleBatchAdd">批量新增会商</a></li>
		</li>
	</ul>
 <tags:message content="${message}"/>
  <form:form id="inputForm" name="inputForm" modelAttribute="planDefine"  action="${ctx}/tvmeeting/meetingarrange/teleBatchAddSave" method="post" class="form-horizontal" onsubmit="return check()">
  		<div class="control-group">
			<label style="margin-left: 130px;">开始日期:</label>
			<div style="display: inline;width:50px;">
				<input type="text" class="Wdate" id="BeginDate" name="BeginDate" style="width: 100px;" placeholder="请选择日期" value="${nowDay}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>			
			</div>
			结束日期:
			<div style="display: inline;">
				<input type="text" class="Wdate" id="endDate" name="endDate" style="width: 100px;" placeholder="请选择日期" value="${afterMonth}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>			
			</div>
		</div>
	    <div class="control-group">
			<label style="margin-left: 130px;">开始时间:</label>
			<div style="display: inline;width:50px;">
				<input type="text" class="Wdate" id="BeginTime" name="BeginTime" style="width: 100px;" value="${beginTime}" onfocus="WdatePicker({dateFmt:'HH:mm'})"/>
			</div>
			结束时间:
			<div style="display: inline;">
				<input type="text" class="Wdate" id="EndTime" name="EndTime" style="width: 100px;" value="${endTime}" onfocus="WdatePicker({dateFmt:'HH:mm'})"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会议名称:</label>
			<div class="controls">
				<form:input path="MeetTitle" htmlEscape="false" maxlength="50"
						class="required" name="MeetTitle"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">内容:</label>
			<div class="controls">
				<form:textarea path="Content" htmlEscape="false" rows="3"
						maxlength="500" class="input-xlarge" name="Content" />
			</div>
		</div>
			<div class="control-group">
			<label class="control-label">会议级别:</label>
			<div class="controls">
					<form:select path="MeetLevel" name="MeetLevel">
						<form:options items="${fns:getDictList('meetLevel')}"
							itemLabel="label" itemValue="value"
							htmlEscape="false" />
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会议分类:</label>
			<div class="controls">
				<form:select path="MeetType" name="MeetType">
						<form:options items="${fns:getDictList('meetType')}"
							itemLabel="label" itemValue="value"
							htmlEscape="false" />
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会议主题:</label>
			<div class="controls">
				<form:select path="MeetTheme" name="MeetTheme">
						<form:options items="${fns:getDictList('meetTheme')}"
							itemLabel="label" itemValue="value"
							htmlEscape="false" />
					</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会议地点:</label>
			<div class="controls">
				<form:select path="MeetArea" name="MeetArea">
						<form:options items="${fns:getDictList('meetArea')}"
							itemLabel="label" itemValue="value"
							htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会议召开范围:</label>
			<div class="controls">
				<c:forEach items="${fns:getDictList('meetRange')}" var="list">
					<input type="checkbox" name="MeetRange" value="${list.value}" />${list.label}
			    </c:forEach>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否有双流:</label>
			<div class="controls">
			   <form:radiobuttons path="IsDouble" items="${fns:getDictList('isDouble')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">组织单位:</label>
			<div class="controls">
				<form:select path="OrganizationUni">
						<form:options items="${fns:getDictList('organize')}"
							itemLabel="label" itemValue="value"
							htmlEscape="false" />
				</form:select>
			</div>
		</div>
		
		
	<!--  
		<div class="control-group">
			<label class="control-label">组织单位:</label>
			<div class="controls">
				<form:input path="OrganizationUni" name="OrgaizationUni" htmlEscape="false" maxlength="50" class="required" />
			</div>
		</div>
		-->	
		<div class="control-group">
			<label class="control-label">联系人:</label>
			<div class="controls">
				<form:input path="Contacts" name="Contacts" htmlEscape="false" maxlength="50" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系方式:</label>
			<div class="controls">
				<form:input path="ContactWay" name="ContactWay" htmlEscape="false" maxlength="50" class="required" />
			</div>
		</div>		
		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
  </form:form>
  </body>
</html>
