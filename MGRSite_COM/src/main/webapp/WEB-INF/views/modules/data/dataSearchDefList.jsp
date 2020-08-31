<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>数据资料--检索定义表</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<link href="${ctxStatic}/ultra/css/dataSearchList.css?ver=1" rel="stylesheet" />
<script src="${ctxStatic}/ultra/js/dataSearchDef.js?ver=3" type="text/javascript"></script>
<style type="text/css">
.selectName{font-size: 16px;
background: #42ace9;
color: #ffffff;
margin-left: 20px;
cursor: pointer;
padding: 5px;}
 a:hover {
text-decoration: none;
}
.interfaceType{
font-weight:bold;
font-size:16px;
}
.groupDefault{font-weight:bold;}
</style>
<script type="text/javascript">
	var ctx = "${ctx}";
	//新增条件的临时变量
	var storageType="${storageType}";
	$(function() {
		$("[name = DataSubclass]:checkbox").each(function() {
			for ( var i = 0; i < org.length; i++) {
				if ($(this).val() == org[i]) {
					$(this).prop("checked", true);
				}
			}

		});
		onCurrent();
		$(function(){
			var categoryid='${categoryid}';
			if(categoryid==17){
				$(".DataSubclass").hide();
			}else{
				$(".DataSubclass").show();
			}
		})
	});

	function page(n, s) {
		var pid = $("#DataClass option:checked").val();
		var id = $("#DataSubclass option:checked").val();
		var dataid = $("#metadata option:checked").val();
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#form1").attr(
				"action",
				"${ctx}/dataSearchDef/list?pid=" + pid + "&id=" + id
						+ "&dataid=" + dataid);
		$("#form1").submit();
		return false;
	}
	/**
	 *大类选择事件
	 */
	function changeData() {
		//获取大类选择的id
		var DataClass = $("#DataClass option:checked").val();
		$("#pid").val(DataClass);
		$
				.ajax({
					url : "${ctx}/dataSearchDef/getDataSubClassList?DataClass="
							+ DataClass,
					type : "post",
					dataType : "json",
					async : true,
					success : function(result) {
						var list = result;
						if(DataClass=="17"){
							var id=$("#DataClass option:checked").val();
							$("#categoryid").val(id);
							$(".DataSubclass").hide();
							$.ajax({
								url : "${ctx}/dataSearchDef/getCDatacodeList?DataSubclass=17",
								type : "post",
								dataType : "json",
								async : true,
								success : function(result) {
									var list = result;
									if (list.length == 0) {
										alert("对不起，该子类型下面没有数据检索的数据资料！请您添加！");
										$("#metadata").empty();
										$("#metadata").select2("val", null);
										$("#datacode").val("");
										page();
									} else {
										var option = "";
										for ( var i = 0; i < list.length; i++) {
											var datacode = list[i][0];
											var name = list[i][1];
											if (i == 0) {
												option += "<option value="+datacode+"  selected = 'selected'>"
														+ name + "</option>";
											} else {
												option += "<option value="+datacode+">"
														+ name + "</option>";
											}

										}
										$("#metadata").empty();
										$("#metadata").select2("val", null);
										$("#metadata").append(option);
										$('#metadata').trigger('change');
									}
								}
							});
						}else{
							if (result.length == 0) {
								alert("对不起，该大类下没有子类型，请先添加该大类的子类型！");
							} else {
								var option = "";
								for ( var i = 0; i < list.length; i++) {
									var categoryid = list[i].categoryid;
									var chnname = list[i].chnname;
									if (i == 0) {
										option += "<option value="+categoryid+"  selected = 'selected'>"
												+ chnname + "</option>";
									} else {
										option += "<option value="+categoryid+">"
												+ chnname + "</option>";
									}

								}
								$("#DataSubclass").empty();
								$("#DataSubclass").select2("val", null);
								$("#DataSubclass").append(option);
								$('#DataSubclass').trigger('change');
							}
						}
					}
				});
	}
	function changeDataSub() {
		var DataSubclass = $("#DataSubclass option:checked").val();
		$("#categoryid").val(DataSubclass);
		$.ajax({
					url : "${ctx}/dataSearchDef/getCDatacodeList?DataSubclass="
							+ DataSubclass,
					type : "post",
					dataType : "json",
					async : true,
					success : function(result) {
						var list = result;
						if (list.length == 0) {
							alert("对不起，该子类型下面没有数据资料！请您添加！");
						} else {
							var option = "";
							for ( var i = 0; i < list.length; i++) {
								var datacode = list[i][0];
								var name = list[i][1];
								if (i == 0) {
									option += "<option value="+datacode+"  selected = 'selected'>"
											+ name + "</option>";
								} else {
									option += "<option value="+datacode+">"
											+ name + "</option>";
								}

							}
							$("#metadata").empty();
							$("#metadata").select2("val", null);
							$("#metadata").append(option);
							$('#metadata').trigger('change');
						}
					}
				});
	}
	//数据资料选择
	function dataChose() {
		var datacode = $("#metadata option:checked").val();
		$("#datacode").val(datacode);
		searchInfo();
	}
	function searchInfo() {
		$("#form1").submit();
	}
	//根据datacode去查询检索条件集合
	function getSearchSetDef(code,index,type,datacode) {
		$(".conditionshow").hide();
		if($("#"+index).hasClass("mainKey")){
			$("#"+index).removeClass("mainKey");
		}
		//index_第几个  type 类型 0,1,2  searchCondition  elementCondition elementCondition
		var temp=index.replace("index_","");
		if(type==0){
			$(".searchCondition"+temp).show();
			checkSearchCode(datacode,index);
			openSearchCode(code,temp);
		}else if(type==1){
			checkelesetcode(datacode,index);
			$(".elementCondition"+temp).show();
			if('${storageType}'==0){
				OpenElementGroup(code,temp);
			}else if('${storageType}'==1){
				OpenElementGroup2(code,temp);
			}
		}else if(type==2){
			$(".interfaceCondition"+temp).show();
			interfaceShow(code,temp,datacode);
		}
	}
	//检验是否有相同的检索条件
	function checkSearchCode(datacode,index){
		$.ajax({
			url : "${ctx}/dataSearchDef/checkSearchCode",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				"datacode":datacode
			},
			success : function(result) {
				if(result.status=="0"){
					$(".dataSearch"+index).hide();
				}else if(result.status=="1"){
					$(".dataSearch"+index).show();
				}
			}
		});
	}
	//相同检索条件的列表
	function getSearchsetcodeList(datacode){
		$('#myModalLabel').html("相同检索条件的数据资料");
		 $("#iframeModel").attr("src","${ctx}/dataSearchDef/getSearchsetcodeList?datacode="+datacode);
		 $(".TimeSel").show();
		 $('#myModal').show();
		 $('#myModal').modal('toggle');	
	}
	//相同结果要素的列表
	function getElesetcodeList(datacode){
		$('#myModalLabel').html("相同结果要素的数据资料");
		 $("#iframeModel").attr("src","${ctx}/dataSearchDef/getElesetcodeList?datacode="+datacode);
		 $(".TimeSel").show();
		 $('#myModal').show();
		 $('#myModal').modal('toggle');	
	}
	//检验是否有相同结果要素
	function checkelesetcode(datacode,index){
		$.ajax({
			url : "${ctx}/dataSearchDef/checkelesetCode",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				"datacode":datacode
			},
			success : function(result) {
				if(result.status=="0"){
					$(".elesetcode"+index).hide();
				}else if(result.status=="1"){
					$(".elesetcode"+index).show();
				}
			}
		});
	}
	//根据datacode重新查询下
	function interfaceShow(code,temp,datacode){
		if($("#interfaceContent22"+temp).hasClass("mainKey")){
			$("#interfaceContent22"+temp).removeClass("mainKey");
		}else{
			$("#interfaceContent22"+temp).addClass("mainKey");
		}
		$(".interfaceshow"+temp).empty();
		$.ajax({
			url : "${ctx}/searchInterface/InterfaceShow",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				"interfacesetcode":code,
				"datacode":datacode
			},
			success : function(result) {
				var status=result.status;
				var name=result.name;
				var namelist=result.namelist;
				var interfacesetcode=result.interfacesetcode;
				var html="";
				if(status=="0"){
					html+="<div class='controls'><input type='text' value=''  style='margin-top:10px;' readOnly='true'  />";
					html+="<input class='btn btn-primary' style='width:90px;margin-left: 12px;' onclick=\"interfacechose('"+code+"','"+temp+"','"+datacode+"');\"  value='选择接口集合'\>";
					html+="<input class='btn btn-primary' style='width:90px;margin-left: 12px;' onclick=\"getInterfaceDef('"+datacode+"');\" value='编辑接口集合'\></div>";
				}else if(status=="1"){
					html+="<div class='controls'><input type='text' value='"+name+"'  style='margin-top:10px;' readOnly='true' />";
					html+="<input class='btn btn-primary' style='width:90px;margin-left: 12px;' onclick=\"interfacechose('"+code+"','"+temp+"','"+datacode+"');\"  value='选择接口集合'\>";
					html+="<input class='btn btn-primary' style='width:90px;margin-left: 12px;' onclick=\"getInterfaceDef('"+datacode+"');\"  value='编辑接口集合' \></div>";
				}
				$(".interfaceshow"+temp).append(html);
			}
		});
		
	}
	function interfaceShow2(code,temp,datacode){
		$(".interfaceshow"+temp).empty();
		$.ajax({
			url : "${ctx}/searchInterface/InterfaceShow",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				"interfacesetcode":code,
				"datacode":datacode
			},
			success : function(result) {
				var status=result.status;
				var name=result.name;
				var namelist=result.namelist;
				var interfacesetcode=result.interfacesetcode;
				var html="";
				if(status=="0"){
					html+="<div class='controls'><input type='text' value=''  style='margin-top:10px;' readOnly='true' />";
					html+="<input class='btn btn-primary' style='width:90px;margin-left: 12px;' onclick=\"interfacechose('"+code+"','"+temp+"','"+datacode+"');\"  value='选择接口集合'\>";
					html+="<input class='btn btn-primary' style='width:90px;margin-left: 12px;' onclick=\"getInterfaceDef('"+datacode+"');\" value='编辑接口集合'\></div>";
				}else if(status=="1"){
					html+="<div class='controls'><input type='text' value='"+name+"'  style='margin-top:10px;' readOnly='true' />";
					html+="<input class='btn btn-primary' style='width:90px;margin-left: 12px;' onclick=\"interfacechose('"+code+"','"+temp+"','"+datacode+"');\"  value='选择接口集合'\>";
					html+="<input class='btn btn-primary' style='width:90px;margin-left: 12px;' onclick=\"getInterfaceDef('"+datacode+"');\"  value='编辑接口集合' \></div>";
				}
				$(".interfaceshow"+temp).append(html);
			}
		});
		
	}
	//接口选定
	var interfacesetcode="";
	var datacode0="";
	function interfacechose(code,temp,datacode){
		interfacesetcode=code;
		datacode0=datacode;
		index=temp;
		$('#myModalLabel2').html("选择接口集合");
		$("#iframeModel2").attr("src","${ctx}/searchInterface/searchInterfaceCoList?datacode="+datacode+"&interfacesetcode="+interfacesetcode);
		 $('#myModal2').show();
		 $('#myModal2').modal('toggle');
	}
	//选定接口
	function interfacechose2(code,datacode,temp){
		//接口绑定事件
		if(confirm("确定要选定接口吗？")){
			var interfacesetcode=$("#interfaceSelect"+temp+" option:checked").val();
			$.ajax({
				url : "${ctx}/searchInterface/InterfaceBind",
				type : "post",
				dataType : "json",
				async : true,
				data : {
					"interfacesetcode" : interfacesetcode,
					"datacode" : datacode
				},
				success : function(result) {
					var status=result.status;
					if(status=="0"){
						alert("选定失败！");
					}else if(status=="1"){
						alert("选定成功！");
					}
					interfaceShow(interfacesetcode,temp,datacode);
				}
			});
		}
	}
	
	//大开检索条件的页面
	function openSearchCode(searchsetcode,index){
		if($("#SearchContent"+index).hasClass("mainKey")){
			$("#SearchContent"+index).removeClass("mainKey");
		}else{
			$("#SearchContent"+index).addClass("mainKey");
		}
		
		$.ajax({
			url : "${ctx}/dataSearchDef/getSearchSetDef",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				"searchsetcode" : searchsetcode
			},
			success : function(result) {
				showGroupHtml(result,index+"_search");
			}
		});
	}
	//根据查出的分组结果展示
	function  showGroupHtml(result,id){
		var glist=result.list1;
		var clist=result.list2;
		$("#"+id).empty();
		var html="";
		for ( var i = 0; i < glist.length; i++) {
			var searchgroupcode1=glist[i].searchgroupcode;
			var groupCode=glist[i].searchgroupcode;
			groupCode=groupCode.split("-")[groupCode.split("-").length-1];
            html+="<tr><td>"+glist[i].chnname+"<a href='javascript:void(0)' onclick=\"updateGpCondition('"+glist[i].searchsetcode+"','"+glist[i].id+"','"+id+"')\">&nbsp;&nbsp;&nbsp;修改</a>";
            html+="<a href='javascript:void(0)' onclick=\"addCondition('"+glist[i].searchsetcode+"','"+groupCode+"','"+searchgroupcode1+"','"+id+"')\">&nbsp;&nbsp;&nbsp;新增条件&nbsp;&nbsp;&nbsp;</a>";
            if(glist[i].invalid==0){
            	html+="<a href='javascript:void(0)' onclick=\"deleteGroup('"+glist[i].id+"','"+id+"',0);\">无效</a>&nbsp;&nbsp;&nbsp;";
            }else if(glist[i].invalid==1){
            	html+="<a href='javascript:void(0)' onclick=\"deleteGroup('"+glist[i].id+"','"+id+"',1);\">有效</a>&nbsp;&nbsp;&nbsp;";
            }
            html+="<a href='javascript:void(0)' onclick=\"updategroupdefault('"+glist[i].searchgroupcode+"','"+glist[i].searchsetcode+"','"+id+"')\">默认选中</a>";
            
            html+="</td><td id="+glist[i].searchgroupcode+">";
            //条件的集合  groupdefaultsearch 分组默认
            for(var j=0;j<clist.length;j++){
            	if(glist[i].searchgroupcode==clist[j].searchgroupcode){
            		if(groupCode=="StationSel"||groupCode=="ElementSel"||groupCode=="ElementFilter"
            				||groupCode=="FormatSel"||groupCode=="QCSel"||groupCode=="TimeSel"){
            			if(clist[j].groupdefaultsearch==1){
            				html+="<span class='groupDefault'>"+clist[j].chnname+"</span>&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode+"','"+clist[j].id+"','"+glist[i].searchsetcode+"','"+id+"')\">修改</a>&nbsp;&nbsp;&nbsp;";
                   		}else{
                   			html+="<span>"+clist[j].chnname+"</span>&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode+"','"+clist[j].id+"','"+glist[i].searchsetcode+"','"+id+"')\">修改</a>&nbsp;&nbsp;&nbsp;";
                   		}
            			if(clist[j].invalid==0){
            				html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+glist[i].searchgroupcode+"','"+clist[j].id+"',0,'"+id+"','"+glist[i].id+"')\">无效</a>&nbsp;&nbsp;&nbsp;<br/>";
            			}else{
            				html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+glist[i].searchgroupcode+"','"+clist[j].id+"',1,'"+id+"','"+glist[i].id+"')\">有效</a>&nbsp;&nbsp;&nbsp;<br/>";
            			}
           		    }else{
           		    	if(clist[j].groupdefaultsearch==1){
           		    		html+="<span class='groupDefault'>"+clist[j].chnname+"</span>&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode+"','"+clist[j].id+"','"+glist[i].searchsetcode+"','"+id+"')\">修改</a>&nbsp;&nbsp;&nbsp;";
                   		}else{
                   			html+="<span>"+clist[j].chnname+"</span>&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode+"','"+clist[j].id+"','"+glist[i].searchsetcode+"','"+id+"')\">修改</a>&nbsp;&nbsp;&nbsp;";
                   		}
           		    	if(clist[j].searchtype=="ComConfig"){
           		    		html+="<a href='javascript:void(0)' onclick=\"editDetail('"+glist[i].searchgroupcode+"','"+clist[j].id+"')\">字典表配置编辑</a>&nbsp;&nbsp;&nbsp;";
           		    		if(clist[j].invalid==0){
                				html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+glist[i].searchgroupcode+"','"+clist[j].id+"',0,'"+id+"','"+glist[i].id+"')\">无效</a>&nbsp;&nbsp;&nbsp;<br/>";
                			}else if(clist[j].invalid==1){
                				html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+glist[i].searchgroupcode+"','"+clist[j].id+"',1,'"+id+"','"+glist[i].id+"')\">有效</a>&nbsp;&nbsp;&nbsp;<br/>";
                			}
           		    	}else{
           		    		if(clist[j].invalid==0){
                				html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+glist[i].searchgroupcode+"','"+clist[j].id+"',0,'"+id+"','"+glist[i].id+"')\">无效</a>&nbsp;&nbsp;&nbsp;<br/>";
                			}else if(clist[j].invalid==1){
                				html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+glist[i].searchgroupcode+"','"+clist[j].id+"',1,'"+id+"','"+glist[i].id+"')\">有效</a>&nbsp;&nbsp;&nbsp;<br/>";
                			}
           		    	}
           		    	
           		    }
            	}
            }
            html+="</td></tr>";
		}
		$("#"+id).append(html);
	}
	function updategroupdefault(searchgroupcode,searchsetcode,id){
		index=id.replace("_search","");
		searchsetcode0=searchsetcode;
		$('#myModalLabel').html("条件默认选中");
		 $("#iframeModel").attr("src","${ctx}/dataSearchDef/updategroupdefault?searchgroupcode="+searchgroupcode);
		 $(".TimeSel").show();
		 $('#myModal').show();
		 $('#myModal').modal('toggle');	
	}
	/*
   function updategroupdefault(ids,groupdefaultsearch,id,index){
	   if(groupdefaultsearch=="1"){
		   if(confirm("确定要置为分组默认不选中么？")){
				$.ajax({
					url : "${ctx}/dataSearchDef/updategroupdefault",
					type : "post",
					dataType : "json",
					async : true,
					data : {
						"id":id,
						"ids":ids
					},
					success : function(result) {
						if(result.status==0){
							alert("更新成功！");
							showGroupHtml(result,index);
						}else{
							alert("更新失败！");
							showGroupHtml(result,index);
						}
						
					}
				});
			   }
	   }else if(groupdefaultsearch=="0"){
		   if(confirm("确定要置为分组默认选中么？")){
				$.ajax({
					url : "${ctx}/dataSearchDef/updategroupdefault",
					type : "post",
					dataType : "json",
					async : true,
					data : {
						"id":id,
						"ids":ids
					},
					success : function(result) {
						if(result.status==0){
							alert("更新成功！");
							showGroupHtml(result,index);
						}else{
							alert("更新失败！");
							showGroupHtml(result,index);
						}
						
					}
				});
			   }
	   }
	  
   }
	*/
	//根据id删除分组
   function deleteGroup(id,index,temp){
	   var returnname=""
	   if(temp==0){
		   returnname="您确定分组要更新成无效么？";
	   }else if(temp==1){
		   returnname="您确定要分组更新成有效么？";
	   }
	   if(confirm(returnname)){
		$.ajax({
			url : "${ctx}/dataSearchDef/deleteGroup",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				"id":id
			},
			success : function(result) {
				if(result.status==0){
					alert("更新成功！");
					showGroupHtml(result,index);
				}else{
					alert("更新失败！");
					showGroupHtml(result,index);
				}
				
			}
		});
	   }
   }
	//根据条件的id去删除刷新
   function deleteCondition(groupCode,id,temp,indexs,pid){
		var returnname="";
		if(temp==0){
			returnname="您确定要把条件更新成无效么";
		}else if(temp==1){
			returnname="您确定要把条件更新成有效么";
		}
	   if(confirm(returnname)){
    	   $.ajax({
   			url : "${ctx}/dataSearchDef/deleteCondition",
   			type : "post",
   			dataType : "json",
   			async : true,
   			data : {
   				"id":id
   			},
   			success : function(result) {
   				if(result.status==0){
   					alert("更新成功！");
   					$("#"+groupCode).empty();
   					var list=result.list;
   					var html="";
   					var groupCode2=groupCode.split("-")[groupCode.split("-").length-1];
   					if(groupCode2=="StationSel"||groupCode2=="ElementSel"||groupCode2=="ElementFilter"
   	    				||groupCode2=="FormatSel"||groupCode2=="QCSel"||groupCode2=="TimeSel"){
   						for(var j=0;j<list.length;j++){
      						if(list[j].groupdefaultsearch==1){
      							 html+="<span class='groupDefault'>"+list[j].chnname+"</span>&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode2+"','"+list[j].id+"','"+pid+"','"+indexs+"')\">修改条件</a>&nbsp;&nbsp;&nbsp;";
      	               		}else{
      	               		     html+="<span>"+list[j].chnname+"</span>&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode2+"','"+list[j].id+"','"+pid+"','"+indexs+"')\">修改条件</a>&nbsp;&nbsp;&nbsp;";
      	               		}
      						 if(list[j].invalid==0){
      							 html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+groupCode+"','"+list[j].id+"',0,'"+indexs+"','"+pid+"');\">无效</a><br/>";
      						 }else if(list[j].invalid==1){
      							 html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+groupCode+"','"+list[j].id+"',1,'"+indexs+"','"+pid+"');\">有效</a><br/>";
      						 }
      						
      					}
   					}else{
   						for(var j=0;j<list.length;j++){
   							if(list[j].searchtype=="ComConfig"){
   								if(list[j].groupdefaultsearch==1){
   									html+="<span class='groupDefault'>"+list[j].chnname+"</span>&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode2+"','"+list[j].id+"','"+pid+"','"+indexs+"')\">修改</a>&nbsp;&nbsp;&nbsp;"+
      								 "<a href='javascript:void(0)' onclick=\"editDetail('"+groupCode+"','"+list[j].id+"')\">字典表配置编辑</a>&nbsp;&nbsp;&nbsp;";
         	               		}else{
	         	               		html+="<span>"+list[j].chnname+"</span>&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode2+"','"+list[j].id+"','"+pid+"','"+indexs+"')\">修改</a>&nbsp;&nbsp;&nbsp;"+
	  								 "<a href='javascript:void(0)' onclick=\"editDetail('"+groupCode+"','"+list[j].id+"')\">字典表配置编辑</a>&nbsp;&nbsp;&nbsp;";
	         	               	} 
   								if(list[j].invalid==0){
         							 html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+groupCode+"','"+list[j].id+"',0,'"+indexs+"','"+pid+"');\">无效</a><br/>";
         						 }else if(list[j].invalid==1){
         							 html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+groupCode+"','"+list[j].id+"',1,'"+indexs+"','"+pid+"');\">有效</a><br/>";
         						 }
         						
   							}else{
   								if(list[j].groupdefaultsearch==1){
   									html+="<span class='groupDefault'>"+list[j].chnname+"</span>&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode2+"','"+list[j].id+"','"+pid+"','"+indexs+"')\">修改条件</a>&nbsp;&nbsp;&nbsp;";
         	               		}else{
         	               		    html+="<span>"+list[j].chnname+"</span>&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode2+"','"+list[j].id+"','"+pid+"','"+indexs+"')\">修改条件</a>&nbsp;&nbsp;&nbsp;";
         	               		}
         						 if(list[j].invalid==0){
         							 html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+groupCode+"','"+list[j].id+"',0,'"+indexs+"','"+pid+"');\">无效</a><br/>";
         						 }else if(list[j].invalid==1){
         							 html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+groupCode+"','"+list[j].id+"',1,'"+indexs+"','"+pid+"');\">有效</a><br/>";
         						 }
         						
   							}
      						 
      					}
   					}
   					$("#"+groupCode).append(html);
   				}else{
   					alert("更新失败！");
   				}
   				
   			}
   		  });
		}
   } 
	/*
	function freshCondition(groupCode,id){
    	   $.ajax({
   			url : "${ctx}/dataSearchDef/freshCondition",
   			type : "post",
   			dataType : "json",
   			async : true,
   			data : {
   				"groupCode":groupCode
   			},
   			success : function(list) {
   				$("#"+groupCode).empty();
				var html="";
				var groupCode2=groupCode.split("-")[groupCode.split("-").length-1];
				if(groupCode2=="StationSel"||groupCode2=="ElementSel"||groupCode2=="ElementFilter"
    				||groupCode2=="FormatSel"||groupCode2=="QCSel"||groupCode2=="TimeSel"){
					for(var j=0;j<list.length;j++){
	       		    	 html+=list[j].chnname+"&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode2+"','"+list[j].id+"')\">修改条件</a>&nbsp;&nbsp;&nbsp;";
	       		    	 if(list[j].invalid==0){
	       		    		html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+groupCode+"','"+list[j].id+"',0);\">有效</a><br/>";
	       		    	 }else if(list[j].invalid==1){
	       		    		html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+groupCode+"','"+list[j].id+"',1);\">无效</a><br/>";
	       		    	 }
	       		    	if(list[j].groupdefaultsearch==1){
 	               			html+="&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updategroupdefault('"+pid+"','"+list[j].groupdefaultsearch+"','"+list[j].id+"','"+id+"')\">默认选中</a>&nbsp;&nbsp;&nbsp;<br/>";
 	               		}else{
 	               			html+="&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updategroupdefault('"+pid+"','"+list[j].groupdefaultsearch+"','"+list[j].id+"','"+id+"')\">默认不选中</a>&nbsp;&nbsp;&nbsp;<br/>";
 	               		}
					}
				}else{
					for(var j=0;j<list.length;j++){
						if(list[j].searchtype=="ComConfig"){
	       		    	     html+=list[j].chnname+"&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode2+"','"+list[j].id+"')\">修改条件</a>&nbsp;&nbsp;&nbsp;"+
	       		    	     "<a href='javascript:void(0)' onclick=\"editDetail('"+groupCode+"','"+list[j].id+"')\">字典表配置编辑</a>&nbsp;&nbsp;&nbsp;";
	       		    	  if(list[j].invalid==0){
	       		    		html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+groupCode+"','"+list[j].id+"',0);\">有效</a><br/>";
	       		    	  }else if(list[j].invalid==1){
	       		    		html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+groupCode+"','"+list[j].id+"',1);\">无效</a><br/>";
	       		    	  }
	       		    	}else{
	       		    	 html+=list[j].chnname+"&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updateCondition('"+groupCode2+"','"+list[j].id+"')\">修改条件</a>&nbsp;&nbsp;&nbsp;";
	       		    	if(list[j].invalid==0){
	       		    		html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+groupCode+"','"+list[j].id+"',0);\">有效</a><br/>";
	       		    	  }else if(list[j].invalid==1){
	       		    		html+="<a href='javascript:void(0)' onclick=\"deleteCondition('"+groupCode+"','"+list[j].id+"',1);\">无效</a><br/>";
	       		    	  }
		            	}
						if(list[j].groupdefaultsearch==1){
 	               			html+="&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updategroupdefault('"+pid+"','"+list[j].groupdefaultsearch+"','"+list[j].id+"','"+id+"')\">默认选中</a>&nbsp;&nbsp;&nbsp;<br/>";
 	               		}else{
 	               			html+="&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"updategroupdefault('"+pid+"','"+list[j].groupdefaultsearch+"','"+list[j].id+"','"+id+"')\">默认不选中</a>&nbsp;&nbsp;&nbsp;<br/>";
 	               		}
					}
				}
				$("#"+groupCode).append(html);
   			  }
   		  });
   }
	*/
	//添加条件
	function addCondition(searchsetcode,value,searchgroupcode,id){
		index=id.replace("_search","");
		searchsetcode0=searchsetcode;
		$(".TimeSel").hide();
		$(".StationSel").hide();
		$(".ElementSel").hide();
		$(".ElementFilter").hide();
		$(".FormatSel").hide();
		$(".QCSel").hide();
		onCurrent();
		$('#myModalLabel').html("新增条件");
		 if(value=="TimeSel"){
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/addCondition?groupcode=TimeSel&searchgroupcode="+searchgroupcode);
			 $(".TimeSel").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }else if(value=="StationSel"){
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/addCondition?groupcode=StationSel&searchgroupcode="+searchgroupcode);
			 $(".StationSel").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }else if(value=="ElementSel"){
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/addCondition?groupcode=ElementSel&searchgroupcode="+searchgroupcode);
			 $(".ElementSel").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }else if(value=="ElementFilter"){
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/addCondition?groupcode=ElementFilter&searchgroupcode="+searchgroupcode);
			 $(".ElementFilter").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }else if(value=="FormatSel"){
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/addCondition?groupcode=FormatSel&searchgroupcode="+searchgroupcode);
			 $(".FormatSel").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }else if(value=="QCSel"){
			 //质量级别
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/addCondition?groupcode=QCSel&searchgroupcode="+searchgroupcode);
			 $(".QCSel").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }else{
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/addCondition?groupcode=ComConfig&searchgroupcode="+searchgroupcode);
			 $(".ComConfig").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }
	}
	//通配详情 字典表编辑
	function editDetail(value,id){
		$('#myModalLabel2').html("字典表编辑");
		 $("#iframeModel2").attr("src","${ctx}/searchCondCfg/SearchCondItemslist?pid="+id);
		 $('#myModal2').show();
		 $('#myModal2').modal('toggle');	
	}
	function updateCondition(value,id,searchsetcode,temp){
		index=temp.replace("_search","");
		searchsetcode0=searchsetcode;
		$('#myModalLabel').html("修改检索条件集合详情");
		 if(value=="TimeSel"){
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/updateCondition?groupcode=TimeSel&id="+id);
			 $(".TimeSel").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }else if(value=="StationSel"){
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/updateCondition?groupcode=StationSel&id="+id);
			 $(".StationSel").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }else if(value=="ElementSel"){
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/updateCondition?groupcode=ElementSel&id="+id);
			 $(".ElementSel").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }else if(value=="ElementFilter"){
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/updateCondition?groupcode=ElementFilter&id="+id);
			 $(".ElementFilter").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }else if(value=="FormatSel"){
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/updateCondition?groupcode=FormatSel&id="+id);
			 $(".FormatSel").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }else if(value=="QCSel"){
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/updateCondition?groupcode=QCSel&id="+id);
			 $(".QCSel").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }else{
			 $("#iframeModel").attr("src","${ctx}/dataSearchDef/updateCondition?groupcode=ComConfig&id="+id);
			 $(".ComConfig").show();
			 $('#myModal').show();
			 $('#myModal').modal('toggle');	
		 }
	}
	function searchType(){
		var searchType = $("#TimeSel_Type option:checked").val();
		$("#TimeSel_SubType").empty();
		$("#TimeSel_SubType").select2("val", null); 
		if(searchType=="TimeScope"){
			var html="<option value='yyyyMMddHHmmss'>年月日时分秒</option>"+
			"<option value='yyyyMMHU'>年月候</option>"+
			"<option value='yyyyMMXU'>年月旬</option>"+
			"<option value='yyyyMMdd2H'>2个小时值（00／12）</option>"+
			"<option value='yyyyMMdd4H'>4个小时值（00／06／12／18）</option>";
			$("#TimeSel_SubType").append(html);
			$('#TimeSel_SubType').trigger('change');
		}else if(searchType=="TimeCycle"){
			var html="<option value='yyyy-MMdd'>历年同期</option>";
			$("#TimeSel_SubType").append(html);
			$('#TimeSel_SubType').trigger('change');
		}else if(searchType=="DataScope"){
			var html="<option value=''>日序选择</option>";
			$("#TimeSel_SubType").append(html);
			$('#TimeSel_SubType').trigger('change');
		}else if(searchType=="ComConfig"){
			var html="<option value='MonthSpan'>月序选择</option>"+
			"<option value='TenDaySpan'>旬序选择</option>"+
			"<option value='FiveDaySpan'>候序选择</option>";
			$("#TimeSel_SubType").append(html);
			$('#TimeSel_SubType').trigger('change');
		}
		
	}
	function searchSubType(){
		onCurrent();
	}
	function onCurrent(){
		$(".isValueLimit").hide();
		$(".valueRange").hide();
		$(".defaultValue").hide();
		$(".conTemp1").hide();
		$(".conTemp2").hide();
		var searchType = $("#TimeSel_Type option:checked").val();
		if(searchType=="TimeScope"){
			var subValue = $("#TimeSel_SubType option:checked").val();
			$(".isValueLimit").show();
			$(".defaultValue").show();
			if(subValue=="yyyyMMddHH"||subValue=="yyyyMMdd"||subValue=="yyyyMMddHHmm"||subValue=="yyyyMMddHHmmss"){
				$(".conTemp1").show();
			}else if(subValue=="yyyyMMXU"){
				$(".conTemp2").show();
			}
		}else if(searchType=="TimeCycle"){
			var subValue = $("#TimeSel_SubType option:checked").val();
			$(".isValueLimit").show();
			$(".defaultValue").show();
		}else if(searchType=="DataScope"){
			var subValue = $("#TimeSel_SubType option:checked").val();
			$(".isValueLimit").show();
			$(".valueRange").show();
			$(".defaultValue").show();
		}else if(searchType=="ComConfig"){
			var subValue = $("#TimeSel_SubType option:checked").val();
			$(".valueRange").show();
			$(".defaultValue").show();
		}
	}
	function modelSubmit(){
		if(searchType=="TimeScope"){
			var subValue = $("#TimeSel_SubType option:checked").val();
		}else if(searchType=="TimeCycle"){
			var subValue = $("#TimeSel_SubType option:checked").val();
		}else if(searchType=="DataScope"){
			var subValue = $("#TimeSel_SubType option:checked").val();
		}else if(searchType=="ComConfig"){
			var subValue = $("#TimeSel_SubType option:checked").val();
		}
	}
	//展开要素的分组-要素型
	function OpenElementGroup(elesetcode,index){
		if($("#SearchContent2"+index).hasClass("mainKey")){
			$("#SearchContent2"+index).removeClass("mainKey");
		}else{
			$("#SearchContent2"+index).addClass("mainKey");
		}
		 $.ajax({
	   			url : "${ctx}/eleSetEleGroup/eleSetEleGroupDef",
	   			type : "post",
	   			dataType : "json",
	   			async : true,
	   			data : {
	   				"elesetcode":elesetcode
	   			},
	   			success : function(result) {
	   				var clist=result.clist;
	   				var flist=result.flist;
	   				elementGroupHtml(clist,flist,index);
	   			}
	   		});
	}
	//展开要素的分组-文件型
	function OpenElementGroup2(elesetcode,index){
		if($("#SearchContent22"+index).hasClass("mainKey")){
			$("#SearchContent22"+index).removeClass("mainKey");
		}else{
			$("#SearchContent22"+index).addClass("mainKey");
		}
		 $.ajax({
	   			url : "${ctx}/eleSetEleGroup/eleSetEleGroupDef2",
	   			type : "post",
	   			dataType : "json",
	   			async : true,
	   			data : {
	   				"elesetcode":elesetcode
	   			},
	   			success : function(result) {
	   				var flist=result.flist;
	   				elementGroupHtml2(flist,index,elesetcode);
	   			}
	   		});
	}
	//新增要素分组   文件型没有分组  要素型有分组
  function 	addElementGroup(elesetcode,id){
		//添加分组
		elesetcode0= elesetcode;
		index=id;
		$("#iframeModel").attr("src",ctx+"/eleSetEleGroup/addEleGroup?elesetcode="+elesetcode);
		$('#myModalLabel').html("新增条件分组");
		$('#myModal').show();
		$('#myModal').modal('toggle');
   
  }
	//新增要素分组   文件型没有分组  要素型有分组
  function 	editElementGroup(elesetcode,id,temp){
		//添加分组
		elesetcode0= elesetcode;
		index=temp;
		$("#iframeModel").attr("src",ctx+"/eleSetEleGroup/editEleGroup?elesetcode="+elesetcode+"&id="+id);
		$('#myModalLabel').html("修改条件分组");
		$('#myModal').show();
		$('#myModal').modal('toggle');
   
  }
  function elementGroupHtml(clist,flist,id){
	  $("#"+id+"_element").empty();
		var html="";
		for(var i=0;i<clist.length;i++){
			html+="<tr><td class='"+clist[i].elegroupcode+"'>"+clist[i].chnname+
			"&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"editElementGroup('"+clist[i].elesetcode+"','"+clist[i].id+"','"+id+"')\">修改</a>&nbsp;&nbsp;&nbsp;"+
			"&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"addEleDef('"+clist[i].elesetcode+"','"+clist[i].elegroupcode+"','"+id+"')\">新增结果要素</a>&nbsp;&nbsp;&nbsp;";
			if(clist[i].invalid=="0"){
				html+="<a href='javascript:void(0)' onclick=\"deleteEleGroup('"+clist[i].id+"','"+clist[i].elesetcode+"','"+id+"',0)\">无效</a></td><td>";
			}else if(clist[i].invalid=="1"){
				html+="<a href='javascript:void(0)' onclick=\"deleteEleGroup('"+clist[i].id+"','"+clist[i].elesetcode+"','"+id+"',1)\">有效</a></td><td>";
			}
			for(var j=0;j<flist.length;j++){
				if(clist[i].elegroupcode==flist[j].elegroupcode){
					html+=flist[j].elementname+"<br/>";
				}
			}
			html+="<td></tr>";
		}
		 $("#"+id+"_element").append(html);
}
  //要素-文件型情况
  function elementGroupHtml2(flist,id,elesetcode){
	  $("#"+id+"_element2").empty();
		var html="";
			html+="<tr><td class='default_element'>默认要素组"+
			"&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"addEleDef2('"+elesetcode+"','"+id+"')\">新增结果要素</a></td><td>";
			for(var j=0;j<flist.length;j++){
				html+=flist[j].elementname+"<br/>";
			}
			html+="<td></tr>";
		 $("#"+id+"_element2").append(html);
}
function deleteEleGroup(id,elesetcode,index,temp){
	var returnname="";
	if(temp==0){
		returnname="您确定把结果分组更新成无效吗？";
	}else if(temp==1){
		returnname="您确定把结果分组更新成有效吗？";
	}
	if(confirm(returnname)){
		$.ajax({
			url : "${ctx}/eleSetEleGroup/deleteEleGroup",
			async : true,
			type : 'POST',
			data : "elesetcode=" + elesetcode+"&id="+id,
			dataType : "json",
			success : function(result) {
				var status=result.status;
				if(status==0){
					elementGroupHtml(result.clist,result.flist,index);
					alert("更新成功！");
				}else if(status==1){
					alert("更新失败！");
				}
			}
		});
		}else{
		}
	
}
var elesetcode0="";
var index="";
var searchsetcode0="";
//新增检索条件分组
function addGroup(searchSetCode,id){
	index=id.replace("_search","");
	searchsetcode0=searchSetCode;
	$("#iframeModel").attr("src",ctx+"/dataSearchDef/dataSearchGroup?type=conditionGroup&searchSetCode="+searchSetCode+"&index="+index);
	$('#myModalLabel').html("新增检索条件分组");
	$('#myModal').show();
	$('#myModal').modal('toggle');
}
function updateGpCondition(searchSetCode,id,temp){
	index=temp.replace("_search","");
	searchsetcode0=searchSetCode;
	$("#iframeModel").attr("src",ctx+"/dataSearchDef/updateGpCondition?type=conditionGroup&searchSetCode="+searchSetCode+"&id="+id);
	$('#myModalLabel').html("修改检索条件分组");
	$('#myModal').show();
	$('#myModal').modal('toggle');
}
function closeModal(){
	$('#myModal').hide();
	$('#myModal').modal('hide');
}
function closeModal2(){
	$('#myModal2').hide();
	$('#myModal2').modal('hide');
}
function addEleDef(elesetcode,elegroupcode,id){
	$('#myModalLabel2').html("新增结果分组要素");
	elesetcode0= elesetcode;
	index=id;
	$("#iframeModel2").attr("src","${ctx}/eleSetEleGroup/elementList?elesetcode="+elesetcode+"&elegroupcode="+elegroupcode);
	 $('#myModal2').show();
	 $('#myModal2').modal('toggle');	
}
function addEleDef2(elesetcode,id){
	$('#myModalLabel2').html("新增结果分组要素");
	elesetcode0= elesetcode;
	index=id;
	$("#iframeModel2").attr("src","${ctx}/eleSetEleGroup/elementList?elesetcode="+elesetcode);
	 $('#myModal2').show();
	 $('#myModal2').modal('toggle');	
}
//接口编辑
function getInterfaceDef(datacode){
	$('#myModalLabel2').html("数据接口编辑");
	$("#iframeModel2").attr("src","${ctx}/searchInterface/searchInterfaceList?datacode="+datacode);
	 $('#myModal2').show();
	 $('#myModal2').modal('toggle');
}
//要素的分组排序
function sortElementGroup(elesetcode,id){
	$('#myModalLabel2').html("结果分组排序");
	elesetcode0= elesetcode;
	index=id;
	$("#iframeModel2").attr("src","${ctx}/eleSetEleGroup/sortElementGroup?elesetcode="+elesetcode);
	 $('#myModal2').show();
	 $('#myModal2').modal('toggle');
}
function sortSearchGroup(searchsetcode,id){
	$('#myModalLabel2').html("结果分组排序");
	searchsetcode0= searchsetcode;
	index=id;
	 $("#iframeModel2").attr("src","${ctx}/dataSearchDef/sortSearchGroup?searchsetcode="+searchsetcode0);
	 $('#myModal2').show();
	 $('#myModal2').modal('toggle');
}
function sortSearchCondDef(searchsetcode,id){
	$('#myModalLabel2').html("检索条件排序");
	searchsetcode0= searchsetcode;
	index=id;
	$("#iframeModel2").attr("src","${ctx}/dataSearchDef/sortSearchCondDef?GroupCode="+searchsetcode0);
	 $('#myModal2').show();
	 $('#myModal2').modal('toggle');
}
$(function () { $('#myModal').on('hide.bs.modal', function () {
      var src=$("#iframeModel").attr("src");
      if(src.indexOf("addCondition")>-1){//添加条件
    	 // freshCondition(searchgroupcode0,index);
    	  getSearchCodeResult(searchsetcode0,index);
      }else if(src.indexOf("updateCondition")>-1){
    	  getSearchCodeResult(searchsetcode0,index);
      }else if(src.indexOf("dataSearchGroup")>-1){
      	  getSearchCodeResult(searchsetcode0,index);
      }else if(src.indexOf("addEleGroup")>-1){
    	  getEleGroupResult(elesetcode0, index);
      }else if(src.indexOf("updategroupdefault")>-1){
    	  getSearchCodeResult(searchsetcode0,index);
      }else if(src.indexOf("editEleGroup")>-1){
    	  getEleGroupResult(elesetcode0, index);
      }
    })
 });

	$(function() {
		$('#myModal2').on('hide.bs.modal', function() {
			var src = $("#iframeModel2").attr("src");
			if (src.indexOf("elementList") > -1) {
				if (storageType == 0) {//要素
					getEleResult(elesetcode0, index);
				} else {
					getEleResult2(elesetcode0, index);
				}
			} else if (src.indexOf("sortElementGroup") > -1) {
				getEleGroupResult(elesetcode0, index);
			} else if (src.indexOf("sortSearchGroup") > -1) {
				getSearchCodeResult(searchsetcode0, index);
			} else if (src.indexOf("sortSearchCondDef") > -1) {
				getSearchCodeResult(searchsetcode0, index);
			} else if (src.indexOf("searchInterfaceCoList") > -1) {//接口选择
				interfaceShow2(interfacesetcode, index, datacode0);
			}else if(src.indexOf("editDataSearch") > -1){
				page();
			}else if(src.indexOf("editDataSearch2") > -1){
				page();
			}
			//editDataSearch shua'xin
		})
	});
	function getEleResult(elesetcode0, index) {
		var num=Math.round(Math.random()*10000)
		$.ajax({
			url : "${ctx}/eleSetEleGroup/eleSetEleGroupDef",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				"elesetcode" : elesetcode0,
				"num":num
			},
			success : function(result) {
				var clist = result.clist;
				var flist = result.flist;
				elementGroupHtml(clist, flist, index);
			}
		});
	}
	function getEleResult2(elesetcode0, index) {
		var num=Math.round(Math.random()*10000)
		$.ajax({
			url : "${ctx}/eleSetEleGroup/eleSetEleGroupDef2",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				"elesetcode" : elesetcode0,
				"num":num
			},
			success : function(result) {
				var flist = result.flist;
				elementGroupHtml2(flist, index, elesetcode0);
			}
		});
	}
	function getEleGroupResult(elesetcode, index) {
		$.ajax({
			url : "${ctx}/eleSetEleGroup/eleSetEleGroupDef",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				"elesetcode" : elesetcode
			},
			success : function(result) {
				var clist = result.clist;
				var flist = result.flist;
				elementGroupHtml(clist, flist, index);
			}
		});
	}
	function getSearchCodeResult(searchsetcode, index) {
		$.ajax({
			url : "${ctx}/dataSearchDef/getSearchSetDef",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				"searchsetcode" : searchsetcode
			},
			success : function(result) {
				showGroupHtml(result, index + "_search");
			}
		});
	}
</script>
</head>

<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dataSearchDef/list">数据检索列表</a>
		</li>
		<c:if test="${isIncludeSub==0}">
		    <c:if test="${status==0}">
		       <li class="isIncludeSub1"><a href="${ctx}/dataSearchDef/dataSearchAdd?categoryid=${categoryid}&datacode=${datacode}&pid=${pid}">检索订制配置</a></li>
		    </c:if>
		</c:if>
		<c:if test="${isIncludeSub==1}">
			<li class="isIncludeSub2"><a href="${ctx}/dataSearchDef/dataSearchAdd2?categoryid=${categoryid}&datacode=${datacode}&pid=${pid}">新增检索订制</a>
			</li>
		</c:if>
	</ul>
	<tags:message content="${message}" />
	<div class="title">
		<form id="form1" action="${ctx}/dataSearchDef/list" method="post">
			<input type="hidden" id="categoryid" name="categoryid"
				value="${categoryid}"> <input type="hidden" id="datacode"
				name="datacode" value="${datacode}"> <input type="hidden"
				id="pid" name="pid" value="${pid}"> <label>资料大类：</label> <select
				id="DataClass" name="DataClass" onChange="changeData(this)">
				<c:forEach items="${plist}" var="dataCategory">
					<c:if test="${pid==dataCategory.categoryid}">
						<option value="${dataCategory.categoryid}"
							type="${dataCategory.categoryid}" selected="selected">${dataCategory.chnname}</option>
					</c:if>
					<c:if test="${pid!=dataCategory.categoryid}">
						<option value="${dataCategory.categoryid}"
							type="${dataCategory.categoryid}">${dataCategory.chnname}</option>
					</c:if>
				</c:forEach>
			</select> 
			<span class="DataSubclass">
			<label>资料子类：</label> <select id="DataSubclass" name="DataSubclass"
				onChange="changeDataSub(this)">
				<c:forEach items="${clist}" var="dataCategory">
					<c:if test="${categoryid==dataCategory.categoryid}">
						<option value="${dataCategory.categoryid}"
							type="${dataCategory.categoryid}" selected="selected">${dataCategory.chnname}</option>
					</c:if>
					<c:if test="${categoryid!=dataCategory.categoryid}">
						<option value="${dataCategory.categoryid}"
							type="${dataCategory.categoryid}">${dataCategory.chnname}</option>
					</c:if>
				</c:forEach>
			</select> 
			</span>
			<label>数据资料：</label> <select id="metadata" name="metadata"
				onChange="dataChose(this)">
				<c:forEach items="${dlist}" var="CategoryDataRelt">
					<c:if test="${datacode == CategoryDataRelt[0]}">
						<option value="${CategoryDataRelt[0]}"
							type="${CategoryDataRelt[0]}" selected="selected">${CategoryDataRelt[1]}</option>
					</c:if>
					<c:if test="${datacode!=CategoryDataRelt[0]}">
						<option value="${CategoryDataRelt[0]}"
							type="${CategoryDataRelt[0]}">${CategoryDataRelt[1]}</option>
					</c:if>
				</c:forEach>
			</select>
		</form>
	</div>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<th>序号</th>
			<th>检索定制分项</th>
			<th>检索条件集合</th>
			<th>结果要素集合</th>
			<th>数据接口集合</th>
			<c:if test="${isIncludeSub==1}">
				<th>资料中文名称</th>
				<th>检索名称</th>
			</c:if>
		</thead>
		<tbody class="fs_tbody">
			<c:forEach items="${list}" var="dataSearchDef" varStatus="status">
				<tr>
					<td>${status.index+1}</td>
					<c:if test="${isIncludeSub==0}">
					   <td>
							<a href="javascript:void(0);" onclick="editDataSearch('${dataSearchDef.datacode}','${categoryid}','${pid}');">修改</a>
							<c:if test="${dataSearchDef.invalid==0}">
							   <a onclick="return confirmx('要更新为无效吗？', this.href)"
							href="${ctx}/dataSearchDef/delete?datacode=${dataSearchDef.datacode}">无效</a>
							</c:if>
							<c:if test="${dataSearchDef.invalid==1}">
							   <a onclick="return confirmx('要更新为有效吗？', this.href)"
							href="${ctx}/dataSearchDef/delete?datacode=${dataSearchDef.datacode}">有效</a>
							</c:if>
						</td>
					</c:if>
					<c:if test="${isIncludeSub==1}">
					    <td>
							<a href="javascript:void(0);"  onclick="editDataSearch2('${dataSearchDef.datacode}','${categoryid}','${pid}');">修改</a>
							<c:if test="${dataSearchDef.invalid==0}">
							   <a onclick="return confirmx('要更新为无效吗？', this.href)"
							href="${ctx}/dataSearchDef/delete?datacode=${dataSearchDef.datacode}">无效</a>
							</c:if>
							<c:if test="${dataSearchDef.invalid==1}">
							   <a onclick="return confirmx('要更新为有效吗？', this.href)"
							href="${ctx}/dataSearchDef/delete?datacode=${dataSearchDef.datacode}">有效</a>
							</c:if>
						</td>
					</c:if>
					<td>
					    <a onclick="getSearchSetDef('${dataSearchDef.searchsetcode}','index_${status.index+1}','0','${dataSearchDef.datacode}');"
							href="javascript:void(0);">编辑/查看</a>
					</td>
					<td>
					    <a onclick="getSearchSetDef('${dataSearchDef.elesetcode}','index_${status.index+1}','1','${dataSearchDef.datacode}');"
							href="javascript:void(0);">编辑/查看</a>
					</td>
					<td>
						<a onclick="getSearchSetDef('${dataSearchDef.interfacesetcode}','index_${status.index+1}','2','${dataSearchDef.datacode}');"
							href="javascript:void(0);">编辑/查看</a>
					</td>
					<c:if test="${isIncludeSub==1}">
						<td>${dataSearchDef.datachnname }</td>
						<td>${dataSearchDef.searchname }</td>
					</c:if>
				</tr>
				<tr id="index_${status.index+1}" class="mainKey">
					<td colspan="14">
						<div class="searchMainShow">
							<div class="searchCondition${status.index+1} conditionshow">
							<div class="SearchContent1 mainKey" id="SearchContent${status.index+1}">
								<div class="SearchConFig3">
									<div class="SearchTitle1">检索条件维护</div>
									<div class="SearchOption1">
										<input class="btn btn-primary clickBot2" value="新增分组"
											onclick="addGroup('${dataSearchDef.searchsetcode}','${status.index+1}');">
											<input class="btn btn-primary clickBot2" value="分组排序" 
											onclick="sortSearchGroup('${dataSearchDef.searchsetcode}','${status.index+1}');">
											<input class="btn btn-primary clickBot2" value="条件排序" 
											onclick="sortSearchCondDef('${dataSearchDef.searchsetcode}','${status.index+1}');">
									</div>
								     <div class="SearchOption1  dataSearchindex_${status.index+1}  mainKey">
								          <span style="color:red;font-size:16px;margin-left:20px;">存在相同检索条件的资料</span>
								          <input class="btn btn-primary clickBot2" value="查看" 
											onclick="getSearchsetcodeList('${dataSearchDef.datacode}');">
								     </div>
								</div>
									<table
										class="table table-striped table-bordered table-condensed">
										<thead>
											<th>检索条件集合分组</th>
											<th>检索条件（加粗为默认选中条件）</th>
										</thead>
										<tbody id="${status.index+1}_search" class="group_condition">
										</tbody>
									</table>
								</div>
							</div>
							<!-- 要素型 -->
							<div class="elementCondition${status.index+1} conditionshow">
							<c:if test="${storageType==0}">
							<div class="SearchContent2 mainKey" id="SearchContent2${status.index+1}">
								<div class="SearchConFig3">
									<div class="SearchTitle2">结果要素维护（要素型）</div>
									<div class="SearchOption2">
									     <input class="btn btn-primary clickBot2" value="新增结果分组" style="width:100px;;margin-left:10px;"
											onclick="addElementGroup('${dataSearchDef.elesetcode}','${status.index+1}');">
										 <input class="btn btn-primary clickBot2" value="结果分组排序" style="width:100px;margin-left:10px;"
										 onclick="sortElementGroup('${dataSearchDef.elesetcode}','${status.index+1}');">
									</div>
								     <div class="SearchOption2 elesetcodeindex_${status.index+1} mainKey">
								          <span style="color:red;font-size:16px;margin-left:20px;">存在相同结果要素的资料</span>
								          <input class="btn btn-primary clickBot2" value="查看" 
											onclick="getElesetcodeList('${dataSearchDef.datacode}');">
								     </div>
								</div>
									<table class="table table-striped table-bordered table-condensed">
										<thead>
											<th>结果要素分组</th>
											<th>结果要素</th>
										</thead>
										<tbody id="${status.index+1}_element" class="group_result">
										
										</tbody>
									</table>
								</div>
							</c:if>
							<!-- 文件型 -->
							<c:if test="${storageType==1}">
							  <div class="SearchContent22 mainKey" id="SearchContent22${status.index+1}">
								<div class="SearchConFig3">
									<div class="SearchTitle2">结果要素维护（文件型）</div>
								</div>
									<table class="table table-striped table-bordered table-condensed">
										<thead>
											<th>结果要素分组</th>
											<th>结果要素</th>
										</thead>
										<tbody id="${status.index+1}_element2" class="group_result">
										
										</tbody>
									</table>
								</div>
							</c:if>
							</div>
							<div class="interfaceCondition${status.index+1} conditionshow">
							<!--  
								<div class="SearchConFig3">
									<div class="SearchTitle3">检索接口</div>
									<div class="SearchOption3"></div>
								</div>
								-->
								     <div class="interfaceContent22 mainKey" id="interfaceContent22${status.index+1}">
										<div class="SearchContent3">
										     <div class="SearchTitle3">数据接口维护：</div>
											 <div class="interfaceshow${status.index+1}">
											 
											 </div>
									 </div>
								</div>
							</div>
						</div>
						</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="modal fade" id="myModal" tabindex="-1" >
		<div class="modal-dialog" style="height:420px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span>&times;</span>
	                </button>
					<h4 class="modal-title" id="myModalLabel">新增条件：</h4>
				</div>
				    <div class="modal-body">
				       <iframe id="iframeModel" style="width:530px;height:320px;" src="${ctx}/dataSearchDef/getBlank"></iframe>
				    </div>
		    </div>
	    </div>
	</div>
	<div class="modal fade" id="myModal2" tabindex="-1" style="width:850px;text-align:center;margin-left:-420px;">
		<div class="modal-dialog" style="height:420px;width:850px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span>&times;</span>
	                </button>
					<h4 class="modal-title" id="myModalLabel2" style="text-align:left;">新增条件：</h4>
				</div>
				    <div class="modal-body">
				       <iframe id="iframeModel2" style="width:800px;height:340px;" src="${ctx}/dataSearchDef/getBlank" ></iframe>
				    </div>
		    </div>
	    </div>
	</div>
</body>
</html>