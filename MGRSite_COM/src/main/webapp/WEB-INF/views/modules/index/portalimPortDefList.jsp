<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>气象业务列表</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<style type="text/css">.table td i{margin:0 2px;}</style>
<script type="text/javascript">	
var ctx="${ctx}";

function page(n,s){
	var chnname=$("#chnname").val();
	
	//$("#pageNo").val(n);
	//$("#pageSize").val(s);
	$("#form1").attr("action","${ctx}/portalimPortDef/portalimPortDefList?chnname="+chnname);
	$("#form1").submit();
	return false;
}


function reverse(){//勾选两个复选框，序号交换
	
	var chk=$('input[name="checkbox"]:checked');
	var s=[];
	if(chk.length==2){//交换序号
		chk.each(function(){//遍历选中复选框，执行函数  
		      s.push($(this).val());//将选中的值添加到数组s中  
		});
	
		//s[0]、s[1]
		document.form1.action= '${ctx}/portalimPortDef/reverseData?ids='+s.toString();
	    document.form1.submit();
		
	}else {
		alert("请选择两个复选框!"); 
		return false;
	}
}

function updateSort() {
	loading('正在提交，请稍等...');
	$("#form1").attr("action", "${ctx}/portalimPortDef/updateSort");
	$("#form1").submit();
}

</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/portalimPortDef/portalimPortDefList">重点推荐栏目列表</a></li>
		<li><a href="${ctx}/portalimPortDef/toAdd">新增推荐栏目</a></li>
	</ul>
	<tags:message content="${message}"/>
	<form id="form1"  method="post">
		<div align="left">
			<label>中文名称：</label>
			<input type="text" id="chnname" name="chnname" value="${chnname}" />
			&nbsp;<input  id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page()"/>
		</div>
	
		<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-word;">
			<thead>
				<th></th>
	  			<th>序号</th>
	    		<th>中文名称</th>  
	    		<th>级别描述</th>
	    		<!--上传图标 
	    		<th>iconurl</th>  -->
	    		<th>链接地址</th>  
	    		<th>操作</th>
	  		</thead>
	  		<tbody class="fs_tbody">		
	  			<c:forEach items="${list}" var="portalimPortDef" varStatus="status">
	  			<tr>
	  			<td style="text-align:center;width:5%;"><input type="checkbox" name="checkbox" value="${portalimPortDef.recommenditemid }" /></td>  
	    		<td style="text-align:left;width:5%;">
					<input type="hidden" name="ids" value="${portalimPortDef.recommenditemid}"/>
					<input name="sorts" type="text" value="${portalimPortDef.orderno}" style="width:50px;margin:0;padding:0;text-align:center;">
				</td>
	    		
	    		<td style="text-align:left;width:15%;">${portalimPortDef.chnname }</td>  
	    		<td style="text-align:left;width:30%;">${portalimPortDef.layerdescription }</td>
	    		<!--<td style="text-align:left;width:25%;">${portalimPortDef.iconurl }</td> -->
	    		<td style="text-align:left;width:35%;">${portalimPortDef.linkurl }</td>		
	    		<td style="text-align:left;width:10%;">
	    		   	<a href="${ctx}/portalimPortDef/toEdit?recommenditemid=${portalimPortDef.recommenditemid}">修改</a>&nbsp;&nbsp;	
	    			<a href="${ctx}/portalimPortDef/delete?recommenditemid=${portalimPortDef.recommenditemid}"
	    				 onclick="return confirmx('确认要删除该记录？', this.href)">&nbsp;删除&nbsp;</a>
	    		</td>
	    	</tr>
			</c:forEach>
	  		</tbody>
		</table>
		<div align="left">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存排序" onclick="updateSort();"/>
		</div>
	</form>
	<!-- <div class="pagination">${page}</div> -->
</body>
</html>