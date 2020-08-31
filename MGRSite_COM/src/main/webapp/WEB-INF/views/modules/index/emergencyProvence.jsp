<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>列表</title>
<meta name="decorator" content="default"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<script src="${ctxStatic}/jquery/jquery-1.9.1.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery.tablesort.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery.tablesort.min.js" type="text/javascript"></script>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">
.table td i{margin:0 2px;}
table{
/* padding:10px; */
margin-right:20px
}
 input{
 	border:none;
 	border-style:none none none none; 
	background-color:transparent;
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
	$("input[id='province']").each(function(){
			if($(this).val()==''||$.trim($(this).val()).length == 0 ){
				flag=true;
				$(this).focus();
				return false;
			}
	});
	$("input[id='provinceCode']").each(function(){
			if($(this).val()==''||$.trim($(this).val()).length == 0 ){
				flag1=true;
				$(this).focus();
				return false;
			}
	});
	if(flag){
		alert('省份名称为必填');
	}else if(flag1){
		alert('省份编码为必填');
	}else{
	 	var con = confirm("确定保存吗？");
		if(con){
		/* 	var province = $("td[id='province']").text();
			var provinceCode = $("td[id='provinceCode']").text(); */
			var keyid = "yjProvinceRuler"
			var action ="${ctx}/emergencyController/provinceAdd";
			document.listForm.action=action;
			$("#listForm").submit();
			/* sendAjax(); */
			}
	}
}

function addClomn(){
		var prov = $("#province1").val();
		var pcode = $("#provinceCode1").val();
		if(prov!=""&pcode!=""){
			$.ajax({
	                url: "${ctx}/emergencyController/provinceCheck",
	                type: 'post',
	                cache: false,
	                data: {"prov":prov,"pcode":pcode},
	                dataType: 'json',
	//                async: false,
	                success: function (result) {
	                    if (result.code == 2) {
	                        alert("该省份名称已经存在，请核对！");
	                    }  
	                    if(result.code == 3) {
	                        alert("该省份编码已经存在，请核对！");
	                    }
	                    if(result.code == 1) {	                        
								$("#treeTable").append(
								'<tr><td width="10%" style="text-align:center;"><input readonly="readonly"   id="province" name="province"  value="'+prov+'"/></td>'
								+'<td width="10%" style="text-align:center;"><input readonly="readonly"   id="provinceCode" name="provinceCode"  value="'+pcode+'"/></td>'
								+'<td style="text-align:center;width:10%;"><input type="button"  value="删除" onclick="deleteClomn(this)"></td>'
								+'</tr>'
								);	                    
	                }
	                    
	            }
	           });
		}else{
			alert('省份名称和省份编码为必填');
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
			window.location.href="${ctx}/emergencyController/getProvence";
		}
}

function save(){
	var prov = $("#province1").val();
	var code = $("#provinceCode1").val();
	if(prov!=""&code!=""){
		$("#province2").val(prov);
		$("#provinceCode2").val(code);
		$("#province2").show();
		$("#provinceCode2").show();
		$("#button1").show();
	}else{
		alert('省份名称和省份编码为必填')
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
		<li ><a href="${ctx}/emergencyController/energencyList">应急信息列表</a></li>
		<li class="active"><a href="${ctx}/emergencyController/getProvence">应急省份维护</a></li>
		<li ><a href="${ctx}/emergencyController/getType">应急类型维护</a></li>
		<li ><a href="${ctx}/emergencyController/getLevel">应急级别维护</a></li>
	</ul>
	<form role="form" id="listForm" name="listForm" method="post" action="">
		<table style="text-align:center;width:30%;float:left;" id="treeTable" class="table table-striped table-bordered table-condensed sortable">
			<tr><th>省份名称</th><th>省份编码</th><th>操作</th></tr>
			<c:forEach items="${all}" var="emergency">
				<tr>
					<td id="province" style="text-align:center;width:10%;">
						<input readonly="readonly" id="province" name="province" value="${emergency.province}"/>
					</td>
					<td id="provinceCode" style="text-align:center;width:10%;">
						<input readonly="readonly" id="provinceCode" name="provinceCode" value="${emergency.provinceCode}"/>
					</td>
					<td style="text-align:center;width:10%;"><input class="a"; type="button"  value="删除" onclick="deleteClomn(this)"></td>
				</tr>
			</c:forEach>
		</table>
		
		<div style="text-align:center;width:50%;height:100%">	</div>
		<table id="eTable" style="text-align:center;width:30%;float:left;" class="table table-striped table-bordered table-condensed">
			<tr>
				<td style="text-align:center;width:10%;">省份名称</td>
				<td style="text-align:center;width:10%;">
					<input type="text"  id="province1" name="province1" value=""/>
				</td>
			</tr>
			<tr>
				<td style="text-align:center;width:10%;">省份名称</td>
				<td style="text-align:center;width:10%;">
					<input type="text" id="provinceCode1" name="provinceCode1" value=""/>
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
	 			<!-- <td colspan="2" style="text-align:center;width:10%;">
					<button class="btn btn-primary" type="button" onclick="addClomn();">增加</button>
				</td>	 -->
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