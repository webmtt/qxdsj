<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
<title>添加子类型</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>

<script type="text/javascript">	
var ctx = "${ctx}";
var ctxStatic = "${ctxStatic}";
function updateSort(){
	var chnname=$("#chnname").val();
	var elesetcode=$("#elesetcode").val();
	var id=$("#id").val();
	if(chnname==null||chnname==""){
		alert("请添加分组名称！");
		return false;
	}else{
		$.ajax({
			url : "${ctx}/eleSetEleGroup/updateEleGroup",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				"elesetcode" : elesetcode,
				"chnname" : chnname,
				"id":id
			},
			success : function(result) {
				var status=result.status;
				if(status=="0"){
					alert("修改成功！");
					parent.closeModal();
				}else if(status=="1"){
					alert("修改失败！");
					parent.closeModal();
				}
				
			}
		});
	}
}
function closeWin(){
	 parent.closeModal();
}
</script>
</head>

<body style="width:100%;height:100%;">
    <div>
        <form id="inputForm"  action="${ctx}/eleSetEleGroup/saveElement" method="post" enctype="multipart/form-data" class="form-horizontal" >        
           <input type="hidden" id="elesetcode"  name="elesetcode" value="${elesetcode}"/>
            <input type="hidden" id="id"  name="id" value="${eleSetEleGroupDef.id}"/>
           <div class="control-group" style="margin-top:50px;">
				<label class="control-label"><a style="color: red;">*</a>分组名称：</label>
				<div class="controls">	
				    <input type="text" id="chnname"  name="chnname" htmlEscape="false" class="required" value="${eleSetEleGroupDef.chnname}"/>			
				</div>
			</div>
			<div class="form-actions pagination-left">
					<input id="btnSubmit" class="btn btn-primary" type="button" value="保存" onclick="updateSort();"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  id="btnCancel" class="btn" type="button" value="关闭" onclick="closeWin();"/>
					
			</div>
         </form>
    </div>	
</body>
</html>

