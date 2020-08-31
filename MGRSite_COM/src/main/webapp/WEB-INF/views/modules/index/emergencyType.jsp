<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>列表</title>
<meta name="decorator" content="default"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">
.table td i{
	margin:0 2px;
	height:20px;
	}
	
table{
/* padding:10px; */
margin-right:20px
}
/* .form-control{
		width: 96%;
	} */
  .uneditable-input {
    display: inline-block;
    height: 20px;
    margin-bottom: 0px;
    font-size: 14px;
    line-height: 20px;
    color: rgb(85, 85, 85);
    vertical-align: middle;
    padding: 0px 0px;
    border-radius: 4px;
}	
 input{
 	border:none;
 	margin:0 0 0 0px;
 	padding: 0px 0px;
 	border-style:none none none none; 
	background-color:transparent;
	ight: 20px;
    padding: 0;
    margin-bottom: 0;
     display: inline-block;
    height: 20px;
    margin-bottom: 0px;
    font-size: 14px;
    line-height: 20px;
    color: rgb(85, 85, 85);
    vertical-align: middle;
    padding: 0px 0px;
    border-radius: 4px;
 }
.active{
	display: list-item;
    text-align: -webkit-match-parent;
}
.a {
    outline: none;
}

.a {
    color: #2fa4e7;
    text-decoration: none;
}
.a:hover, .a:focus {
    color: #157ab5;
    text-decoration: underline;
}

.a:hover, .a:active {
    outline: 0;
}
</style>
<script type="text/javascript">	
$(document).ready(function() {
	$("#treeTable").treeTable({expandLevel : 3});
});
function updateSort() {
	loading('正在提交，请稍等...');
	$("#listForm").attr("action", "${ctx}/portalMenuDef/updateSort");
	$("#listForm").submit();
}
function add(){
	var flag  = false;
	var flag1 = false;
	$("input[id='type']").each(function(){
			if($(this).val()==''||$.trim($(this).val()).length == 0 ){
				flag=true;
				$(this).focus();
				return false;
			}
	});
	$("input[id='typeCode']").each(function(){
			if($(this).val()==''||$.trim($(this).val()).length == 0 ){
				flag1=true;
				$(this).focus();
				return false;
			}
	});
	if(flag){
		 alert('应急类型名称为必填');
	}else if(flag1){
		 alert('应急类型编码为必填');
	}else{
	 	var con = confirm("确定保存吗？");
		if(con){
			/* var province = $("#province").val();
			var provinceCode = $("#provinceCode").val();
			var keyid = "yjProvinceRuler" */
			var action ="${ctx}/emergencyController/typeAdd";
			document.listForm.action=action;
			$("#listForm").submit();
			sendAjax();
			}
	}
}

/* function addClomn(){
	var prov = $("#type1").val();
	var code = $("#typeCode1").val();
	if(prov!=""&code!=""){
		$("#treeTable").append(
		'<tr><td width="10%" style="text-align:center;"><input readonly="readonly"  id="type" name="type" type="text" value="'+prov+'"/></td>'
		+'<td width="10%" style="text-align:center;"><input readonly="readonly"  id="typeCode" name="typeCode" type="text" value="'+code+'"/></td>'
		+'<td style="text-align:center;width:10%;"><input type="button"  value="删除" onclick="deleteClomn(this)"></td>'
		+'</tr>'
		);
	}else{
		alert('应急类型名称和应急类型编码为必填');
	}
} */
function addClomn(){
		var prov = $("#type1").val();
		var pcode = $("#typeCode1").val();
		if(prov!=""&pcode!=""){
			$.ajax({
	                url: "${ctx}/emergencyController/typeCheck",
	                type: 'post',
	                cache: false,
	                data: {"prov":prov,"pcode":pcode},
	                dataType: 'json',
	//                async: false,
	                success: function (result) {
	                    if (result.code == 2) {
	                        alert("该应急类型名称已经存在，请核对！");
	                    }  
	                    if(result.code == 3) {
	                        alert("该应急类型编码已经存在，请核对！");
	                    }
	                    if(result.code == 1) {	                        
								$("#treeTable").append(
								'<tr><td width="10%" style="text-align:center;"><input readonly="readonly"  id="type" name="type"  value="'+prov+'"/></td>'
								+'<td width="10%" style="text-align:center;"><input readonly="readonly"  id="typeCode" name="typeCode"  value="'+pcode+'"/></td>'
								+'<td style="text-align:center;width:10%;"><input type="button"  value="删除" onclick="deleteClomn(this)"></td>'
								+'</tr>'
								);	                    
	                }
	                    
	            }
	           });
		}else{
			alert('应急类型名称和应急类型编码为必填');
		}
		
		
}
 function deleteClomn(r){  
	    var i=r.parentNode.parentNode.rowIndex;  
		document.getElementById('treeTable').deleteRow(i);  
}
function sendAjax() {
			var cleanType="idata" ;
			$.ajax({
				type: "post",
				url: '${ctx}/sys/cleansuccess?cleanType=' + cleanType,
				cache: false,
				async: false,  
				success: function () {
				/* alert('缓存清理成功'); */
				}
			});
		}
function refash(){
	var con = confirm("确定取消吗？");
		if(con){
			window.location.href="${ctx}/emergencyController/getType";
		}
}
function save(){
	var prov = $("#type1").val();
	var code = $("#typeCode1").val();
	if(prov!=""&code!=""){
		$("#type2").val(prov);
		$("#typeCode2").val(code);
		$("#type2").show();
		$("#typeCode2").show();
		$("#button1").show();
	}else{
		alert('应急类型名称和应急类型编码为必填')
	}
}
/*  $(function(){
    // li CSS选中状态切换
    
    $("#elist li").click(function() {
    
        $(this).siblings('li').removeClass('active');  // 删除其他兄弟元素的样式
        alert('缓存清理成功1');
        $(this).addClass('active'); // 添加当前元素的样式 
        alert('缓存清理成功2');
        });
      }); */
</script>
</head>

<body>
	<ul id ="elist" class="nav nav-tabs">
		<li><a href="${ctx}/emergencyController/energencyList">应急信息列表</a></li>
		<li><a href="${ctx}/emergencyController/getProvence">应急省份维护</a></li>
		<li class="active"><a href="${ctx}/emergencyController/getType">应急类型维护</a></li>
		<li><a href="${ctx}/emergencyController/getLevel">应急级别维护</a></li>
	</ul>
	<form role="form" id="listForm" name="listForm" method="post" action="">
		<table style="text-align:center;width:30%;float:left;" id="treeTable" class="table table-striped table-bordered table-condensed sortable">
			<tr><th>应急类型名称</th><th>应急类型编码</th><th>操作</th></tr>
			<c:forEach items="${all}" var="emergency">
				<tr>
					<td style="text-align:center;width:10%;">
						<input readonly="readonly" name="type" value="${emergency.type}" />
					</td>
					<td style="text-align:center;width:10%;">
						<input readonly="readonly"  name="typeCode"  value="${emergency.typeCode}" />
					</td>
					<td style="text-align:center;width:10%;"><input type="button" class="a" value="删除" onclick="deleteClomn(this)"></td>
				</tr>
			</c:forEach>
			
		</table>
		<div style="text-align:center;width:5%;height:100%">	</div>
		<table id="eTable" style="text-align:center;width:30%;float:left;" class="table table-striped table-bordered table-condensed">
			<tr>
				<td style="text-align:center;width:10%;">应急类型名称</td>
				<td style="text-align:center;width:10%;">
					<input type="text"  id="type1" name="type1" value=""/>
				</td>
			</tr>
			<tr>
				<td style="text-align:center;width:10%;">应急类型编码</td>
				<td style="text-align:center;width:10%;">
					<input type="text"  id="typeCode1" name="typeCode1" value=""/>
				</td>
			</tr>
			 <tr>
				<td colspan="2" style="text-align:center;width:20%;">
					<button class="btn btn-primary" type="button" onclick="addClomn();">增加</button>
				</td>
						
			</tr>
	 	</table>
	 
		
	 </form>
	 <div>
	  <table style="text-align:center;width:50%;clear:both;margin-left:50px;">
	 	<tr>
	 			<!-- <td style="text-align:center;width:10%;">
					<button class="btn btn-primary" type="button" onclick="addClomn();">增加</button>
				</td> -->
						
	 			<td style="text-align:center;width:10%;">
	 				<button   class="btn btn-primary"  type="button"  onclick="add();">保存</button>
	 			</td>
	 			<td style="text-align:center;width:10%;">
	 				<button class="btn btn-primary"  type="button" onclick="refash();">取消</button>
	 			</td>
	 		</tr>
	 	</table>
	 </div>
</body>
<script>


</script>
</html>