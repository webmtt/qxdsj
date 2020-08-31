<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>分组选择</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<style type="text/css">
</style>
<script type="text/javascript">
	var ctx = "${ctx}";
	$(function(){
		 $("input[name=SearchGroup]").click(function(){
			var value= $("input[name='SearchGroup']:checked").val();
			var id= $("input[name='SearchGroup']:checked").attr("id");
			 if(value=="ComConfig"){
				 $(".isdefine").show();
				 $(".isdefine2").hide();
				 $("#chnname").val(id);
			 }else{
				 $(".isdefine").hide();
				 $(".isdefine2").show();
				 $("#chnname2").val(id);
			 }
		 });
	});
	function check(){
		 var temp = document.getElementsByName('IsOptional');
		 var IsOptional="";
      for ( var i = 0; i < temp.length; i++) {
			if (temp[i].checked) {
			    IsOptional = temp[i].value;
			}
		}
        if(IsOptional==""){
        	alert("请选择是否可选！");
        	return false;
        }
        var SearchGroup = document.getElementsByName('SearchGroup'); 
        var groupCode="";
        for ( var i = 0; i < SearchGroup.length; i++) {
			if (SearchGroup[i].checked) {
				groupCode = SearchGroup[i].value;
			}
		}
        var chnname="";
        var seachCode="";
        if(groupCode==""){
        	alert("请选择分组！");
        	return false;
        }else{
        	if(groupCode=="ComConfig"){
        		chnname=document.getElementById('chnname').value;
            	seachCode=document.getElementById('groupcode').value;
            	if(chnname==null||chnname==""){
            		alert("请填写分组名称！");
            		return false;
            	}else if(seachCode==null||seachCode==""){
            		alert("请填写分组编码！");
            		return false;
            	}
        	}else{
        		chnname=document.getElementById('chnname2').value;
        	}
        }
        var url = '${ctx}/dataSearchDef/SaveSearchGroup';
        var searchSetCode='${searchSetCode}';
        var value='${type}';
        $.ajax({
			url : url,
			async : true,
			type : 'POST',
			data : "SearchGroup=" + groupCode + "&IsOptional=" +IsOptional+"&searchSetCode="+searchSetCode+"&value="+value+"&chnname="+chnname+"&seachCode="+seachCode,
			dataType : "json",
			success : function(result) {
				if(result.status==0){
					alert("新增成功！");
					parent.closeModal();
				}else{
					alert("新增失败！该分组已经存在！");
					parent.closeModal();
				}
			}
		});
	}
	function closeWin(){
		 parent.closeModal();
	}
</script>
<style type="text/css">
.SearchGroup{margin-left:10px;}
.title{font-weight:bold;}
.isdefine{display:none;}
.control-label{width:80px !important;}
.controls{margin-left:120px !important;}
</style>
</head>

<body style="width:100%;height:100%;">
    <div style="margin-top:20px;">
       <form id="form1" method="post" class="form-horizontal">
           <div class="control-group">
				<label class="control-label"><span style="color:red;">*</span>分组选择:</label>
				<div class="controls">				
					<c:forEach items="${list}" var="dict">
						<input class="SearchGroup" name="SearchGroup" type="radio" value="${dict.value}" id="${dict.label}"/>${dict.label}
					</c:forEach>
				</div>
			</div>
           <div  class="isdefine">
              <div class="control-group">
	               <label class="control-label"><span style="color:red;">*</span>分组名称：</label>
	               <div class="controls">
	                   <input type="text" id="chnname"  name="chnname" class="required"/>
	               </div>
	               <label class="control-label"><span style="color:red;">*</span>分组编码：</label>
	               <div class="controls">
	                  <input type="text"  id="groupcode" name="groupcode" class="required"/>
	               </div>
               </div>
           </div>
           <div class="isdefine2">
	           <div class="control-group">
	               <label class="control-label">分组名称：</label>
	               <div class="controls">
	                   <input type="text" id="chnname2"  name="chnname2" class="required"/>
	               </div>
	           </div>
           </div>
           <div class="control-group">
				<label class="control-label"><span style="color:red;">*</span>是否可选：</label>
				<div class="controls">				
					<input name="IsOptional" type="radio" value="0" checked/>必选
                    <input name="IsOptional" type="radio" value="1" />可选
				</div>
			</div>
          <div class="control-group"> 
		      <div class="controls">		
			   <input id="btnSubmit" class="btn btn-primary" type="bottom" value="保存" style="width:30px;" onclick="check()"/>
			   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  id="btnCancel" class="btn" type="button" value="关闭" onclick="closeWin();"/>
		      </div>
	       </div>	
       </form>
    </div>	
</body>
</html>