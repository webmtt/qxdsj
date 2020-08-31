<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>列表</title>
<meta name="decorator" content="default"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<style type="text/css">
.table td i{margin:0 2px;}
 input{
 	margin:0 0 0 0px;
 	border:none;
 	border-style:none none none none; 
	background-color:transparent;
 }
/*  .textinput{
	height: 98%;
	width: 98%;
	color: #555;
	background-color: #6fafe9;
 } */
 
 
 table{
/* padding:10px; */
margin-right:20px
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
/* $(document).ready(function() {
	$("#treeTable").treeTable({expandLevel : 3});
}); */
function updateSort() {
	loading('正在提交，请稍等...');
	$("#listForm").attr("action", "${ctx}/portalMenuDef/updateSort");
	$("#listForm").submit();
}
function add(){
	var flag  = false;
	var flag1 = false;
	$("input[id='level']").each(function(){
			if($(this).val()==''||$.trim($(this).val()).length == 0 ){
				flag=true;
				$(this).focus();
				return false;
			}
	});
	$("input[id='levelCode']").each(function(){
			if($(this).val()==''||$.trim($(this).val()).length == 0 ){
				flag1=true;
				$(this).focus();
				return false;
			}
	});
	if(flag){
		alert('应急级别名称为必填');
	}else if(flag1){
		alert('应急级别编码为必填');
	}else{
	 	var con = confirm("确定保存吗？");
		if(con){
			/* var province = $("#province").val();
			var provinceCode = $("#provinceCode").val();
			var keyid = "yjProvinceRuler" */
			var action ="${ctx}/emergencyController/levelAdd";
			document.listForm.action=action;
			$("#listForm").submit();
			sendAjax();
			}
	}
}

/* function addClomn(){
	var prov = $("#level1").val();
	var code = $("#levelCode1").val();
	if(prov!=""&code!=""){
		$("#treeTable").append(
		'<tr><td width="10%" style="text-align:center;"><input readonly="readonly"   id="level" name="level" type="text" value="'+prov+'"/></td>'
		+'<td width="10%" style="text-align:center;"><input readonly="readonly"  id="levelCode" name="levelCode" type="text" value="'+code+'"/></td>'
		+'<td style="text-align:center;width:10%;"><input type="button"  value="删除" onclick="deleteClomn(this)"></td>'
		+'</tr>'
		);
	}else{
		alert('应急级别名称和应急级别编码为必填');
	}
} */
function addClomn(){
		var prov = $("#level1").val();
		var pcode = $("#levelCode1").val();
		if(prov!=""&pcode!=""){
			$.ajax({
	                url: "${ctx}/emergencyController/levelCheck",
	                type: 'post',
	                cache: false,
	                data: {"prov":prov,"pcode":pcode},
	                dataType: 'json',
	//                async: false,
	                success: function (result) {
	                    if (result.code == 2) {
	                        alert("该应急级别名称已经存在，请核对！");
	                    }  
	                    if(result.code == 3) {
	                        alert("该应急类型编码已经存在，请核对！");
	                    }
	                    if(result.code == 1) {	                        
								$("#treeTable").append(
								'<tr><td width="10%" style="text-align:center;"><input readonly="readonly"   id="level" name="level"  value="'+prov+'"/></td>'
								+'<td width="10%" style="text-align:center;"><input readonly="readonly"  id="levelCode" name="levelCode"  value="'+pcode+'"/></td>'
								+'<td style="text-align:center;width:10%;"><input type="button"  value="删除" onclick="deleteClomn(this)"></td>'
								+'</tr>'
								);	                    
	                }
	                    
	            }
	           });
		}else{
			alert('应急级别名称和应急级别编码为必填');
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
			window.location.href="${ctx}/emergencyController/getLevel";
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
//列表排序
function TableSort() { 
  this.tbl = document.getElementById("treeTable"); 
  this.lastSortedTh = null; 
  if (this.tbl && this.tbl.nodeName == "TABLE") { 
    var headings = this.tbl.tHead.rows[0].cells; 
    for (var i = 0; headings[i]; i++) { 
      if (headings[i].className.match(/asc|dsc/)) { 
        this.lastSortedTh = headings[i]; 
      } 
    } 
    this.makeSortable(); 
  } 
} 
TableSort.prototype.makeSortable = function() { 
  var headings = this.tbl.tHead.rows[0].cells; 
  for (var i = 0; headings[i]; i++) { 
    headings[i].cIdx = i; 
    var a = document.createElement("a"); 
    a.href = "#"; 
    a.innerHTML = headings[i].innerHTML; 
    a.onclick = function(that) { 
      return function() { 
        that.sortCol(this); 
        return false; 
      } 
    }(this); 
    headings[i].innerHTML = ""; 
    headings[i].appendChild(a); 
  } 
} 
TableSort.prototype.sortCol = function(el) { 
  var rows = this.tbl.rows; 
  var alpha = [], numeric = []; 
  var aIdx = 0, nIdx = 0; 
  var th = el.parentNode; 
  var cellIndex = th.cIdx; 
  
  for (var i = 1; rows[i]; i++) { 
    var cell = rows[i].cells[cellIndex]; 
    var content = cell.textContent ? cell.textContent : cell.innerText; 
    var num = content.replace(/(\$|\,|\s)/g, ""); 
    if (parseFloat(num) == num) { 
      numeric[nIdx++] = { 
        value : Number(num), 
        row : rows[i] 
      } 
    } else { 
      alpha[aIdx++] = { 
        value : content, 
        row : rows[i] 
      } 
    } 
  } 
  function bubbleSort(arr, dir) { 
    var start, end; 
    if (dir === 1) { 
      start = 0; 
      end = arr.length; 
    } else if (dir === -1) { 
      start = arr.length - 1; 
      end = -1; 
    } 
    var unsorted = true; 
    while (unsorted) { 
      unsorted = false; 
      for (var i = start; i != end; i = i + dir) { 
        if (arr[i + dir] && arr[i].value > arr[i + dir].value) { 
          var temp = arr[i]; 
          arr[i] = arr[i + dir]; 
          arr[i + dir] = temp; 
          unsorted = true; 
        } 
      } 
    } 
    return arr; 
  } 
  
  var col = [], top, bottom; 
  if (th.className.match("asc")) { 
    top = bubbleSort(alpha, -1); 
    bottom = bubbleSort(numeric, -1); 
    th.className = th.className.replace(/asc/, "dsc"); 
  } else { 
    top = bubbleSort(numeric, 1); 
    bottom = bubbleSort(alpha, 1); 
    if (th.className.match("dsc")) { 
      th.className = th.className.replace(/dsc/, "asc"); 
    } else { 
      th.className += "asc"; 
    } 
  } 
  if (this.lastSortedTh && th != this.lastSortedTh) { 
    this.lastSortedTh.className = this.lastSortedTh.className.replace( 
        /dsc|asc/g, ""); 
  } 
  this.lastSortedTh = th; 
  col = top.concat(bottom); 
  var tBody = this.tbl.tBodies[0]; 
  for (var i = 0; col[i]; i++) { 
    tBody.appendChild(col[i].row); 
  } 
} 
function list() { 
  
  var sales = document.getElementById('treeTable'); 
  var sortTable = new TableSort('treeTable'); 
} 

</script>
</head>

<body>
	<ul id ="elist" class="nav nav-tabs">
		<li ><a href="${ctx}/emergencyController/energencyList">应急信息列表</a></li>
		<li><a href="${ctx}/emergencyController/getProvence">应急省份维护</a></li>
		<li><a href="${ctx}/emergencyController/getType">应急类型维护</a></li>
		<li class="active"><a href="${ctx}/emergencyController/getLevel">应急级别维护</a></li>
	</ul>
	<form role="form" id="listForm" name="listForm" method="post" action="">
		<table style="text-align:center;width:30%;float:left;" id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>应急级别名称</th><th>应急级别编码</th><th>操作</th></tr>
			<c:forEach items="${all}" var="emergency">
				<tr>
					<td style="text-align:center;width:10%;">
						<input readonly="readonly"  name="level" value="${emergency.level}" />
					</td>
					<td style="text-align:center;width:10%;">
						<input readonly="readonly" name="levelCode"  value="${emergency.levelCode}" />
					</td>
					<td style="text-align:center;width:10%;"><input class="a" type="button"  value="删除" onclick="deleteClomn(this)"></td>
				</tr>
			</c:forEach>
			
		</table>
		<div style="text-align:center;width:5%;height:100%">	</div>
		<table id="eTable" style="text-align:center;width:30%;float:left;" class="table table-striped table-bordered table-condensed">
			
			<tr>
				<td style="text-align:center;width:10%;">应急级别名称</td>
				<td style="text-align:center;width:10%;">
					<input class="textinput" type="text"  id="level1" name="level1" value=""/>
				</td>	
			</tr>
			<tr>
				<td style="text-align:center;width:10%;">应急级别编码</td>	
				<td style="text-align:center;width:10%;">
					<input class="textinput"  type="text"  id="levelCode1" name="levelCode1" value=""/>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align:center;width:10%;">
					<button class="btn btn-primary" type="button" onclick="addClomn();">增加</button>
				</td>			
			</tr> 
	 	</table>
	 
		
	 </form>
	 <div>
	  <table style="text-align:center;width:40%;clear:both;margin-left:50px;">
	 	<tr>	
	 	<!-- <td colspan="2" style="text-align:center;width:10%;">
					<button class="btn btn-primary" type="button" onclick="addClomn();">增加</button>
				</td> -->
	 			<td style="text-align:center;width:10%;">
	 				<button   class="btn btn-primary"  type="button"  onclick="add();">保存</button>
	 			</td>
	 			<td style="text-align:center;width:10%;">
	 				<button class="btn btn-primary"  type="button" onclick="refash();">取消</button>
	 			</td>
	 			<!-- <td style="text-align:center;width:10%;">
	 				<button class="btn btn-primary"  type="button" onclick="list();">排序</button>
	 			</td> -->
	 		</tr>
	 	</table>
	 </div>
</body>
<script>


</script>
</html>