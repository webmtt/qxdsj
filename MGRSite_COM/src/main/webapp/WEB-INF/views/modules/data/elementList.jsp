<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>结果要素</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">.table td i{margin:0 2px;}
.typeName  {
background-color: #3daae9;
background-image: linear-gradient(to bottom, #46aeea, #2fa4e7);
background-repeat: repeat-x;
border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
border-radius: 4px;
color: #ffffff;
display: inline-block;
font-size: 14px;
height: 21px;
line-height: 20px;
margin-left: 25px;
text-align: center;
text-decoration: none;
text-shadow: 0 0 0 rgba(0, 0, 0, 0.25);
vertical-align: middle;

}</style>
<script type="text/javascript">	
var ctx="${ctx}";
$(function(){
})
function change(){
	var value = $("#eleSetEleG option:checked").val();
	var pid=$("#pid").val();
	page(value,pid);
}
function page(value,pid) {
	$("#form1").attr(
			"action",
			"${ctx}/eleSetEleG/SearchCondItemslist?itemtype=" + value+"&pid="+pid );
	$("#form1").submit();
	return false;
}
function reviewPass(){
	var eleSetEleG = $("#eleSetEleG option:checked").val();
	   if(confirm("确定要审核通过吗？")){
		   document.inputForm.action= '${ctx}/sys/userInfomation/reviewPass';
 	   document.inputForm.submit();
		}
}
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="">结果要素列表</a></li>
		<li><a href="${ctx}/eleSetEleGroup/addElementDef?elesetcode=${elesetcode}&elegroupcode=${elegroupcode}">新增结果要素</a></li>
		<li><a href="${ctx}/eleSetEleGroup/sortElement?elesetcode=${elesetcode}&elegroupcode=${elegroupcode}">排序</a></li>
	</ul>
	<tags:message content="${message}"/>
	<input id="elesetcode" name="elesetcode" type="hidden" value="${elesetcode}"/>
	<input id="elegroupcode" name="elegroupcode" type="hidden" value="${elegroupcode}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
		<thead>
  			<th>序号</th>
    		<th>接口要素编码</th>  
    		<th>要素名称</th>
    		<th>是否可选</th> 
    		<th>是否值过滤</th>
    		<th>是否默认选中</th>
    		<th>是否质检</th>  
    		<th>操作</th>
  		</thead>
  		<tbody class="fs_tbody">		
  			<c:forEach items="${list}" var="eleSetElementDef" varStatus="status">
				<tr>
					<td>${status.index+1}</td>
					<td>${eleSetElementDef.uelecode}</td>
					<td>${eleSetElementDef.elementname}</td>
					<td>
					    <c:if test="${eleSetElementDef.isoptional=='0' }">
					    必选
					    </c:if>
					    <c:if test="${eleSetElementDef.isoptional=='1' }">
					    可选
					    </c:if>
					</td>
					<td>
					  <c:if test="${eleSetElementDef.isfilter=='0' }">
					     不过滤
					    </c:if>
					    <c:if test="${eleSetElementDef.isfilter=='1' }">
					    过滤
					    </c:if>
					</td>
					<td>
					<c:if test="${eleSetElementDef.isseleted=='0' }">
					     不选中
					    </c:if>
					    <c:if test="${eleSetElementDef.isseleted=='1' }">
					   选中
					    </c:if>
					</td>
					<td>
					<c:if test="${eleSetElementDef.isqc=='0' }">
					     不需要
					    </c:if>
					    <c:if test="${eleSetElementDef.isqc=='1' }">
					  需要
					    </c:if>
					</td>
					<td>
						<a href="${ctx}/eleSetEleGroup/updateElementDef?id=${eleSetElementDef.id}&elesetcode=${elesetcode}&elegroupcode=${elegroupcode}">修改</a>
						<c:if test="${eleSetElementDef.invalid=='0' }">
						     <a href="${ctx}/eleSetEleGroup/deleteElementDef?id=${eleSetElementDef.id}&elesetcode=${elesetcode}&elegroupcode=${elegroupcode}&invalid=${eleSetElementDef.invalid}" onclick="return confirmx('要重置无效吗？', this.href)" >有效</a>
						</c:if> 
						<c:if test="${eleSetElementDef.invalid=='1' }">
						     <a href="${ctx}/eleSetEleGroup/deleteElementDef?id=${eleSetElementDef.id}&elesetcode=${elesetcode}&elegroupcode=${elegroupcode}&invalid=${eleSetElementDef.invalid}" onclick="return confirmx('要重置有效吗？', this.href)" >无效</a>
						</c:if> 
					</td>
				</tr>
			</c:forEach>
  		</tbody>
	</table>
</body>
</html>