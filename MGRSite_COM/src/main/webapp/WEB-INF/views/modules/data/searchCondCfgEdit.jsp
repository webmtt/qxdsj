<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/dialog.jsp"%>

<html>
<head>
<title>添加子类型</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<style type="text/css">
.TimeSel{width:100%;float:left;}
.comDiV{width: 100%;
float: left;
margin-top: 10px;}
.isValueLimit,.ComConfig{width: 100%;
float: left;
}
#TimeSel_valueLimit{margin-top:10px;width: 70px;}
#defaultValue,#xunChose,.searchType,#TimeSel_SubType,.endDate{margin-top:10px;}
.control-label{float:left;margin-top:10px;width: 130px !important;}
.valueRange{float:left;}
.valueRange{float:left;}
.defaultValue{float:left;}
.defaultValue2{float:left;}
.conTemp1,.conTemp2,.conTemp3{float:left;margin-top:10px;}
.limitShow{float:left;width: 100%;}
.CheckBoxdiv{float:left;}
.endDatediv{float:left;width:100%;}
.limitCheckBox{float:left;margin-top:10px;}
.defaultText{float:left;margin-top: 15px;margin-right: 5px;}
.currentMonth{float:left;margin-top: 15px;margin-right: 5px;}
.stationTitle,.elementTitle,.elementfTitle,.formatTitle,.qcselTitle{float:left;margin-top:5px;margin-bottom:5px;font-weight:bold;font-size:16px;width:100%;}
.stationItems{float:left;margin-top:5px;margin-bottom:5px;width:100%;}
.ComConfigShow{display:none;}
</style>
<script type="text/javascript">	
var ctx = "${ctx}";
var ctxStatic = "${ctxStatic}";
$(function(){
	//是否有值的限制
	isvalueLimit();
	defaultCondition();
	stationCurrent();
	endDateLimit();
	$("#GroupDefaultSearch").val('${GroupDefaultSearch}');
})

function defaultCurremt(){
	var searchType='${searchType}';
	$("#TimeSel_Type").val(searchType); 
	searchType();
}
//是否有值的限制
function isvalueLimit(){
	var value= '${isValueLimit}';
	 $(":radio[name='isvalueLimit'][value='" + value + "']").prop("checked", "checked");
	var valuelimitunit= '${valuelimitunit}';
	if(value=="1"){
		 $(".limitShow").show();
		 $("#TimeSel_valueLimit").val(valuelimitunit);
	 }else{
		 $(".limitShow").hide();
	 }
	$("input[name=isvalueLimit]").click(function(){
		var value0= $("input[name='isvalueLimit']:checked").val();
		 if(value0=="1"){
			 $(".limitShow").show();
		 }else{
			 $(".limitShow").hide();
		 }
	 });
}
function endDateLimit(){
	var value='${enddatetype}';
	 $(":radio[name='endDateType'][value='" + value + "']").prop("checked", "checked");
	if(value=="0"){
		 $("#endDateStatus").hide();
	 }else{
		 $("#endDateStatus").show();
	 }
	$("input[name=endDateType]").click(function(){
		var value0= $("input[name='endDateType']:checked").val();
		 if(value0=="0"){
			 $("#endDateStatus").hide();
		 }else{
			 $("#endDateStatus").show();
		 }
	 });
}
//默认时间的逻辑限制-开始加载
function defaultTime0(){
	//var subType = $("#TimeSel_SubType option:checked").val();
    var subType='${searchSubType}';
	 $("#defaultValue").empty();
	 $("#endDate").empty();
	if(subType=="yyyyMMddHHmm"||subType=="yyyyMMddHHmm"||subType=="yyyyMMddHH"||subType=="yyyyMMddHHmmss"){
		var html="<option value='CurrDate'>当日</option>"+
        "<option value='CurrHour'>当小时</option>"+
        "<option value='CurrMonth'>当月</option>"+
        "<option value='CurrYear'>当年</option>";
        $("#defaultValue").append(html);
        $("#endDate").append(html);
	}else if(subType=="yyyyMMdd"){
		var html="<option value='CurrDate'>当日</option>"+
        "<option value='CurrMonth'>当月</option>"+
        "<option value='CurrYear'>当年</option>";
        $("#defaultValue").append(html);
        $("#endDate").append(html);
	}else if(subType=="yyyyMM"){
		var html="<option value='CurrMonth'>当月</option>"+
        "<option value='CurrYear'>当年</option>";
        $("#defaultValue").append(html);
        $("#endDate").append(html);
	}else if(subType=="yyyy"){
		var html="<option value='CurrYear'>当年</option>";
        $("#defaultValue").append(html);
        $("#endDate").append(html);
	}
	if(subType=="yyyyMMXU"){
		var defaultvalue='${dValue}';
		$("#defaultValue").val(defaultvalue);
		var startTime2='${startTime2}';
		var endTime2='${endTime2}';
		var xunStyle1='${xunStyle1}';
		var xunStyle2='${xunStyle2}';
		$("#startTime2").val(startTime2);
		$("#endTime2").val(endTime2);
		$("#xunStyle1").val(xunStyle1);
		$("#xunStyle2").val(xunStyle2);
	}else if(subType=="yyyyMMHU"){
		
	}else if(subType=="yyyy-MMdd"){
		var html="<option value='CurrYear'>当年</option>";
        $("#endDate").append(html);
		var startTime4='${startTime4}';
		var startTime44='${startTime44}';
		var endTime4='${endTime4}';
		var endTime44='${endTime44}';
		$("#startTime4").val(startTime4);
		$("#startTime44").val(startTime44);
		$("#endTime4").val(endTime4);
		$("#endTime44").val(endTime44);
	}else if(subType=="yyyyMMdd2H"){
		var html="<option value='CurrDate'>当日</option>"+
        "<option value='CurrHour'>当小时</option>"+
        "<option value='CurrMonth'>当月</option>"+
        "<option value='CurrYear'>当年</option>";
		 $("#endDate").append(html);
	}else if(subType=="yyyyMMdd4H"){
		var html="<option value='CurrDate'>当日</option>"+
        "<option value='CurrHour'>当小时</option>"+
        "<option value='CurrMonth'>当月</option>"+
        "<option value='CurrYear'>当年</option>";
		 $("#endDate").append(html);
	}else{
		defaultValue0();
		defaultChose0();
	}
	//结束日期选中
    var enddate='${enddate}';
    var opts = document.getElementById("endDate"); 
    if(enddate!=null||enddate!=""){
    	for(var i=0;i<opts.options.length;i++){  
            if(enddate==opts.options[i].value){  
                   opts.options[i].selected = 'selected';  
                   break;  
              }  
        }  
    }
}
function defaultValue0(){
	var defaultvalue='${defaultvalue}';
	if(defaultvalue!=null){
		var values=defaultvalue.split(";");
		var st=values[0].split(">")[1];
		if(st!=null){
			$("#startTime").val(0-Number(st));
		}
		var et=values[1].split(">")[1];
		if(et!=null){
			$("#endTime").val(0-Number(et));
		}
		$("#defaultValue").val(values[0].split(">")[0].replace("<"));
	}
	
}
function defaultChose0(){
	$(".defaultText").empty();
	var defaultValue = $("#defaultValue option:checked").text();
	$(".defaultText").html(defaultValue+"-");
}
//默认时间的逻辑限制
function defaultTime(){
	var subType = $("#TimeSel_SubType option:checked").val();
   // var subType='${searchSubType}';
	$("#defaultValue").empty();
	$("#endDate").empty();
	if(subType=="yyyyMMddHHmm"||subType=="yyyyMMddHHmm"||subType=="yyyyMMddHH"||subType=="yyyyMMddHHmmss"){
		var html="<option value='CurrDate'>当日</option>"+
        "<option value='CurrHour'>当小时</option>"+
        "<option value='CurrMonth'>当月</option>"+
        "<option value='CurrYear'>当年</option>";
        $("#defaultValue").append(html);
        $("#endDate").append(html);
        defaultValue();
	}else if(subType=="yyyyMMdd"){
		var html="<option value='CurrDate'>当日</option>"+
        "<option value='CurrMonth'>当月</option>"+
        "<option value='CurrYear'>当年</option>";
        $("#defaultValue").append(html);
        $("#endDate").append(html);
        defaultValue();
	}else if(subType=="yyyyMM"){
		var html="<option value='CurrMonth'>当月</option>"+
        "<option value='CurrYear'>当年</option>";
        $("#defaultValue").append(html);
        $("#endDate").append(html);
        defaultValue();
	}else if(subType=="yyyy"){
		var html="<option value='CurrYear'>当年</option>";
        $("#defaultValue").append(html);
        $("#endDate").append(html);
        defaultValue();
	}else if(subType=="yyyyMMXU"){
		var html="<option value='CurrDate'>当日</option>";
        $("#endDate").append(html);
        defaultValue();
	}else if(subType=="yyyy-MMdd"){//历史同期
		var html="<option value='CurrYear'>当年</option>";
        $("#endDate").append(html);
	}else if(subType=="yyyyMMdd2H"){
		var html="<option value='CurrDate'>当日</option>"+
        "<option value='CurrHour'>当小时</option>"+
        "<option value='CurrMonth'>当月</option>"+
        "<option value='CurrYear'>当年</option>";
		 $("#endDate").append(html);
	}else if(subType=="yyyyMMdd4H"){
		var html="<option value='CurrDate'>当日</option>"+
        "<option value='CurrHour'>当小时</option>"+
        "<option value='CurrMonth'>当月</option>"+
        "<option value='CurrYear'>当年</option>";
		 $("#endDate").append(html);
	}
	 defaultChose();
}
function defaultValue(){
	var defaultvalue='${defaultvalue}';
	if(defaultvalue!=null){
		var values=defaultvalue.split(";");
		var st=values[0].split(">")[1];
		if(st!=null){
			$("#startTime").val(0-Number(st));
		}
		var et=values[1].split(">")[1];
		if(et!=null){
			$("#endTime").val(0-Number(et));
		}
		$("#defaultValue").val(values[0].split(">")[0].replace("<"));
	}
	
}
function searchType(){
	var searchType = $("#TimeSel_Type option:checked").val();
	$("#TimeSel_SubType").empty();
	if(searchType=="TimeScope"){
		var html="<option value='yyyyMMddHHmmss'>年月日时分秒</option>"+
		"<option value='yyyyMMddHHmm'>年月日时分</option>"+
		"<option value='yyyyMMddHH'>年月日时</option>"+
		"<option value='yyyyMMdd'>年月日</option>"+
		"<option value='yyyyMM'>年月</option>"+
		"<option value='yyyy'>年</option>"+
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
function searchType0(){
	var searchType = $("#TimeSel_Type option:checked").val();
	$("#TimeSel_SubType").empty();
	if(searchType=="TimeScope"){
		var html="<option value='yyyyMMddHHmmss'>年月日时分秒</option>"+
		"<option value='yyyyMMddHHmm'>年月日时分</option>"+
		"<option value='yyyyMMddHH'>年月日时</option>"+
		"<option value='yyyyMMdd'>年月日</option>"+
		"<option value='yyyyMM'>年月</option>"+
		"<option value='yyyy'>年</option>"+
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
	$("#TimeSel_SubType").attr("value",'${searchSubType}');
}
//二级类型的选择时间
function searchSubType(){
	onCurrent();
}
function onCurrent(){
	if("${groupcode}"=='TimeSel'){
		$(".isValueLimit").hide();
		$(".valueRange").hide();
		$(".defaultValue").hide();
		$(".conTemp1").hide();
		$(".conTemp2").hide();
		$(".conTemp3").hide();
		$(".conTemp4").hide();
		var searchType = $("#TimeSel_Type option:checked").val();
		if(searchType=="TimeScope"){
			var subValue = $("#TimeSel_SubType option:checked").val();
			$(".isValueLimit").show();
			$(".defaultValue").show();
			//conTemp1
			if(subValue=="yyyyMMddHH"||subValue=="yyyyMMdd"||subValue=="yyyyMMddHHmm"||subValue=="yyyyMMddHHmmss"
				||subValue=="yyyyMM"||subValue=="yyyy"){
				$(".conTemp1").show();
			}else if(subValue=="yyyyMMXU"){
				$(".conTemp2").show();
			}else if(subValue=="yyyyMMHU"){
				$(".conTemp3").show();
			}
		}else if(searchType=="TimeCycle"){
			var subValue = $("#TimeSel_SubType option:checked").val();
			$(".isValueLimit").show();
			$(".defaultValue").show();
			$(".conTemp4").show();
		}else if(searchType=="DataScope"){
			var subValue = $("#TimeSel_SubType option:checked").val();
			$(".isValueLimit").show();
			$(".valueRange").show();
			$(".defaultValue2").show();
			$(".endDate").hide();
		}else if(searchType=="ComConfig"){
			var subValue = $("#TimeSel_SubType option:checked").val();
			$(".valueRange").show();
			$(".defaultValue2").show();
			$(".endDate").hide();
		}
		defaultTime();
	}
}

//默认进去加载的shuju 
function defaultCondition(){
	if("${groupcode}"=='TimeSel'){
		$(".isValueLimit").hide();
		$(".valueRange").hide();
		$(".defaultValue").hide();
		$(".defaultValue2").hide();
		$(".conTemp1").hide();
		$(".conTemp2").hide();
		$(".conTemp3").hide();
		$(".conTemp4").hide();
		//$("#TimeSel_Type option:checked").val('${searchType}');
		$("#TimeSel_Type").attr("value",'${searchType}');
		var searchType = $("#TimeSel_Type option:checked").val();
		if(searchType=="TimeScope"){
			//$("#TimeSel_SubType option:checked").val('${searchSubType}');
			$("#TimeSel_SubType").attr("value",'${searchSubType}');
			var subValue = $("#TimeSel_SubType option:checked").val();
			$(".isValueLimit").show();
			$(".defaultValue").show();
			//conTemp1
			if(subValue=="yyyyMMddHH"||subValue=="yyyyMMdd"||subValue=="yyyyMMddHHmm"||subValue=="yyyyMMddHHmmss"
				||subValue=="yyyyMM"||subValue=="yyyy"){
				$(".conTemp1").show();
				
			}else if(subValue=="yyyyMMXU"){
				$(".conTemp2").show();
				
			}else if(subValue=="yyyyMMHU"){
				$(".conTemp3").show();
				
			}
		}else if(searchType=="TimeCycle"){
			$(".isValueLimit").show();
			$(".defaultValue").show();
			$(".conTemp4").show();
			searchType0();
		}else if(searchType=="DataScope"){
			$("#TimeSel_SubType").attr("value",'${searchSubType}');
			$(".isValueLimit").show();
			$(".valueRange").show();
			$("#minValue").val('${minvalue}');
			$("#maxValue").val('${maxvalue}');
			$("#defaultValue2").val('${defaultvalue}');
			$(".defaultValue2").show();
			searchType0();
		}else if(searchType=="ComConfig"){
			$("#TimeSel_SubType").attr("value",'${searchSubType}');
			$(".valueRange").show();
			$("#minValue").val('${minvalue}');
			$("#maxValue").val('${maxvalue}');
			$("#defaultValue2").val('${defaultvalue}');
			$(".defaultValue2").show();
			searchType0();
		}
		defaultTime0();
	}else if("${groupcode}"=='ElementSel'){
		var dom='${searchType}';
		$("[name = ElementSel]:radio").each(function () {
			if($(this).val()==dom){
				$(this).attr("checked", true);
			}
        });
	}else if("${groupcode}"=='ElementFilter'){
		var dom='${searchType}';
		$("[name = ElementFilter]:radio").each(function () {
			if($(this).val()==dom){
				$(this).attr("checked", true);
			}
            
        });
	}else{
		if("${searchType}"=="ComConfig"){
			$(".ComConfigShow").show();
			$("input:radio[name='ComConfig']").eq(1).attr("checked",'checked');
			$("#ComConfig_chname").val('${ComConfig_chname}');
			$("#ComConfig_subCode").val('${ComConfig_subCode}');
			$("#ComConfig_SM").val('${ComConfig_SM}');
		}else if("${searchType}"=="ALLSel"){
			$(".ComConfigShow").hide();
			$("input:radio[name='ComConfig']").eq(0).attr("checked",'checked');
			$("#ComConfig_chname").val('${ComConfig_chname}');
			getComConfigDefault();
		}
	}
}
function getComConfigDefault(){
	 $("input[name=ComConfig]").click(function(){
		 var ComConfig=$("input[name='ComConfig']:checked").val();
		 if(ComConfig=="ALLSel"){
			 $(".ComConfigShow").hide();
		 }else if(ComConfig=="ComConfig"){
			 $(".ComConfigShow").show();
		 }
	  });
}
function defaultChose(){
	$(".defaultText").empty();
	var defaultValue = $("#defaultValue option:checked").text();
	$(".defaultText").html(defaultValue+"-");
}
//台站选择的情况
function StationSelC(){
	$(".ALLSel").hide();
	$(".StationLevel").hide();
	$(".StationList").hide();
	$(".StationGIS").hide();
	$(".Region").hide();
	$(".Spatial").hide();
	$(".StationScope").hide();
	var StationSel = $("#StationSel option:checked").val();
	$("."+StationSel).show();
}
function stationCurrent(){
    //默认都隐藏
	$(".ALLSel").hide();
	$(".StationLevel").hide();
	$(".StationList").hide();
	$(".StationGIS").hide();
	$(".Region").hide();
	$(".Spatial").hide();
	$(".StationScope").hide();
	$("#StationSel").val('${StationSel}');
	var StationSel = $("#StationSel option:checked").val();
	$("."+StationSel).show();
	if('${StationSel}'=="StationLevel"){
		$("#StationSel_ALLSel").val('${StationSel_ALLSel}');
		$("#StationLevel_SM").val('${StationLevel_SM}');
		$("#StationSel_IsHidden").val('${StationSel_IsHidden}');
	}else if('${StationSel}'=="StationList"){
		$("#StationSel_StationList").val('${StationSel_StationList}');
		$("#StationList_Type").val('${StationList_Type}');
		$("#StationList_SM").val('${StationList_SM}');
		$("#StationList_defaultvalue").val('${StationList_defaultvalue}');
	}else if('${StationSel}'=="StationGIS"){
		$("#StationGIS_Type").val('${StationGIS_Type}');
		$("#StationGIS_SM").val('${StationGIS_SM}');
		var isValueLimit='${isValueLimit}';
		if(isValueLimit=="0"){
			$(".limitShow").hide();
		}else if(isValueLimit=="1"){
			$(".limitShow").show();
		}
	}else if('${StationSel}'=="Region"){
		$("#StationSel_Spatial").val('${StationSel_Spatial}');
		$("#searchSubType_Spatial").val('${searchSubType_Spatial}');
	}else if('${StationSel}'=="Spatial"){
		
	}
}
function saveBottom(){
	var groupcode='${groupcode}';
	if(groupcode=='ComConfig'){
		var ComConfig=$("input[name='ComConfig']:checked").val();
		var ComConfig_chname=$("#ComConfig_chname").val();
		if(ComConfig=='undefined'){
			alert("请选择类型！");
			return false;
		}else if(ComConfig_chname==""||ComConfig_chname==null){
			alert("请填写中文名称！");
			return false;
		}
	}else if(groupcode=='QCSel'){
		var QCSel_chname=$("#QCSel_chname").val();
		if(QCSel_chname==""||QCSel_chname==null){
			alert("请填写中文名称！");
			return false;
		}
	}else if(groupcode=='FormatSel'){
		var FormatSel_chname=$("#FormatSel_chname").val();
		if(FormatSel_chname==""||FormatSel_chname==null){
			alert("请填写中文名称！");
			return false;
		}
	}
	$.ajax({
		url : "${ctx}/dataSearchDef/saveUpdateSearchSetDef",
		type : "post",
		dataType : "json",
		async : true,
		data : $('#inputForm').serialize(),
		success : function(result) {
			if(result.status==0){
				alert("条件修改成功！");
				 parent.closeModal();
			}else if(result.status==0){
				alert("条件修改失败！");
				 parent.closeModal();
			}
			
		}
	});
}
function closeWin(){
	 parent.closeModal();
}
</script>
</head>

<body>
    <div style="width:530px;height:auto;">
	<form id="inputForm"  action="${ctx}/dataSearchDef/saveUpdateSearchSetDef" method="post" enctype="multipart/form-data" class="form-horizontal" >
	<input type="hidden" value="${groupcode}" id="groupcode" name="groupcode"/>
	<input type="hidden" value="${searchSetCode}" id="searchSetCode" name="searchSetCode"/>
	<input type="hidden" value="${id}" id="id" name="id"/>
	<c:if test="${groupcode=='TimeSel'}">
          <div class="TimeSel">
	          <div class="comDiV">
	             <label class="control-label">时间范围：</label>
	             <select id="TimeSel_Type" class="searchType" name="TimeSel_Type" onChange="searchType(this)">
		              <option value="TimeScope">时间范围选择（开始/结束）</option>
		              <option value="TimeCycle">时间周期选择</option>
		              <option value="DataScope">时间范围（间隔）</option>
		              <option value="ComConfig">其他时间选择</option>
	             </select>
	          </div>
	          <div class="comDiV">
	              <label class="control-label">时间类型：</label>
	              <select id="TimeSel_SubType" name="TimeSel_SubType" onChange="searchSubType(this)">
		                <option value="yyyyMMddHHmmss">年月日时分秒</option>
		                <option value='yyyyMMddHH'>年月日时</option>
						<option value='yyyyMMdd'>年月日</option>
						<option value='yyyyMM'>年月</option>
						<option value='yyyy'>年</option>
						<option value="yyyyMMHU">年月候</option>
						<option value="yyyyMMXU">年月旬</option>
						<option value="yyyyMMdd2H">2个小时值（00／12）</option>
						<option value="yyyyMMdd4H">4个小时值（00／06／12／18）</option>
	              </select>
	          </div>
	          <!-- DataScope和TimeScope -->
	          <div class="isValueLimit">
	             <label class="control-label">检索时间限制：</label>
	             <div class="CheckBoxdiv">
	                 <input style="float:left;margin-top:15px;" type="radio" value="1" name="isvalueLimit"  
	                   /><div style="float:left;margin-top:10px;">是</div>
	                 <input style="float:left;margin-top:15px;" type="radio"  value="0" name="isvalueLimit" 
	                 /><div style="float:left;margin-top:10px;">否</div>
	             </div>
	             <div class="limitShow">
		             <label class="control-label">检索时间限制：</label>
		             <input type="text" name="valueLimit" value="${valuelimit}" style="width:50px;margin-top: 10px;" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/>
		             <select id="TimeSel_valueLimit" name="TimeSel_valueLimit">
		                   <option value="Year">年</option>
		                   <option value="Month">月</option>
		                   <option value="Day">日</option>
		                   <option value="Hour">时</option>
		                   <option value="Count">个数</option>
		             </select>
		             <span style="color:red;">*（该处填写数字）</span>
	             </div>
	          </div>
	          <!-- ComConfig和DataScope 都是存在的 -->
	          <div class="valueRange">
	              <label class="control-label">值范围：</label>
	              <input type="text" name="minValue" id="minValue" style="width:50px;margin-top: 10px;" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/> ~ <input type="text" name="maxValue" id="maxValue" style="width:50px;margin-top: 10px;" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/>
	          </div>
	          <div class="endDate" style="float:left;">
	              <label class="control-label">结束日期限制：</label>
	              <div class="CheckBoxdiv">
	                 <input style="float:left;margin-top:15px;" type="radio" value="0" name="endDateType" checked="checked"/><div style="float:left;margin-top:10px;">不限制</div>
	                 <!--  
	                 <input style="float:left;margin-top:15px;" type="radio"  value="1" name="endDateType" /><div style="float:left;margin-top:10px;">日期限制</div>
	                 -->
	                 <input style="float:left;margin-top:15px;" type="radio"  value="2" name="endDateType" /><div style="float:left;margin-top:10px;">计算限制</div>
	              </div>
	              <div class="endDatediv" id="endDateStatus">
		              <label class="control-label">结束日期类型：</label>
	                  <select id="endDate" name="endDate" style="width:80px;margin-top: 10px;">
		                   <option value="CurrDate">当日</option>
		                   <option value="CurrHour">当小时</option>
		                   <option value="CurrMonth">当月</option>
		                   <option value="CurrYear">当年</option>
		              </select>
	              </div>
	          </div>
	          <!-- 默认值  都可能有 -->
	          <div class="defaultValue">
	              <!-- yyyy-MMdd和 yyyyMMddHH（小时） -->
	              <div class="conTemp1">
	                   <p style="color:red;text-align: center;">*默认日期（开始结束）时间</p>
	                   <label class="control-label">默认日期：</label>
			               <select id="defaultValue" name="defaultValue" style="width:80px;" onChange="defaultChose(this)">
			                   <option value="CurrDate">当日</option>
			                   <option value="CurrHour">当小时</option>
			                   <option value="CurrMonth">当月</option>
			                   <option value="CurrYear">当年</option>
			               </select>
		               <br/>
		               <label class="control-label">开始时间：</label>
		               <div class="defaultText">当日-</div>
		               <input type="text" name="startTime" id="startTime" style="width:40px;margin-top: 10px;float:left;" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/> 
		               <label style="float:left;margin-top:15px;margin-left:10px;">结束时间：</label>
		               <div class="defaultText">当日-</div>
		               <input type="text" name="endTime" id="endTime" style="width:40px;margin-top: 10px;float:left;" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/>
	              </div>
	              <div class="conTemp2">
	                   <!-- yyyyMMXU当前月（两种情况）-->
	                   <p style="color:red;text-align: center;">默认日期（开始结束）时间</p>
	                   <label class="control-label">默认日期：</label>
	                   <select id="xunChose" name="xunChose" style="float:left;">
	                        <option value="DIV">上旬/中旬／下旬表示“01/02/03”</option>
	                        <option value="SEQ">上旬/中旬／下旬表示“01/11/21”</option>
		               </select>
		               <br/>
	                   <label class="control-label">开始时间：</label>
	                   <div class="currentMonth">当月-</div>
		               <input type="text" name="startTime2" id="startTime2" style="width:40px;margin-top: 10px;float:left;" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/> 
		               <select id="xunStyle1" name="xunStyle1" style="width:70px;float:left;margin-top: 10px;">
	                        <option value="1" checked="checked">上旬</option>
	                        <option value="2">中旬</option>
	                        <option value="3">下旬</option>
		               </select>
		               <br/>
		               <label class="control-label">结束时间：</label>
		               <div class="currentMonth">当月-</div>
		               <input type="text" name="endTime2" id="endTime2" style="width:40px;margin-top: 10px;float:left;" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/>
		               <select id="xunStyle2" name="xunStyle2"  style="width:70px;float:left;margin-top: 10px;">
	                        <option value="1" checked="checked">上旬</option>
	                        <option value="2">中旬</option>
	                        <option value="3">下旬</option>
		               </select>
	              </div>
	              <!--yyyyMMHU 候  -->
	              <div  class="conTemp3">
	                 <!-- yyyyMMXU当前月（两种情况）-->
	                   <p style="color:red;text-align: center;">默认日期（开始结束）时间</p>
	                   <label class="control-label">默认日期：</label>
	                   <select id="houChose" name="houChose" style="float:left;">
	                        <option value="DIV">上旬/中旬／下旬表示“01/02/03”</option>
	                        <option value="SEQ">上旬/中旬／下旬表示“01/11/21”</option>
		               </select>
		               <br/>
	                   <label class="control-label">开始时间：</label>
	                   <div class="currentMonth">当月-</div>
		               <input type="text" name="startTime3" style="width:40px;margin-top: 10px;float:left;" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/> 
		               <select id="houStyle1" name="houStyle1" style="width:70px;float:left;margin-top: 10px;">
	                        <option value="1" checked="checked">上旬</option>
	                        <option value="2">中旬</option>
	                        <option value="3">下旬</option>
		               </select>
		               <br/>
		               <label class="control-label">结束时间：</label>
		               <div class="currentMonth">当月-</div>
		               <input type="text" name="endTime3" style="width:40px;margin-top: 10px;float:left;" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/>
		               <select id="houStyle2" namee="houStyle2" style="width:70px;float:left;margin-top: 10px;">
	                        <option value="1" checked="checked">上旬</option>
	                        <option value="2">中旬</option>
	                        <option value="3">下旬</option>
		               </select>
	              </div>
	               <!-- 历史同期 -->
	              <div class="conTemp4">
	                   <label class="control-label">开始时间：</label>
	                   <div class="currentMonth">当年-</div>
		               <input type="text" name="startTime4" id="startTime4" style="width:40px;margin-top: 10px;float:left;" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/>
		               <label class="control-label">月日：</label>              
		               <input type="text" name="startTime44" id="startTime44" style="width:40px;margin-top: 10px;float:left;"/>
		               <br/>
		               <span style="color:red;">*月日填写规则：1月1日填写0101</span> 
		               <br/>
		               <label class="control-label">结束时间：</label>
		               <div class="currentMonth">当年-</div>
		               <input type="text" name="endTime4" id="endTime4" style="width:40px;margin-top: 10px;float:left;" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/>
		               <label class="control-label">月日：</label>
		               <input type="text" name="endTime44" id="endTime44" style="width:40px;margin-top: 10px;float:left;"/>
		               <br/>
		               <span style="color:red;">*月日填写规则：1月1日填写0101</span> 
	              </div> 
	               
	          </div>
	           <div class="defaultValue2">
	                <label class="control-label">默认值：</label>
		            <input type="text" name="defaultValue2" id="defaultValue2" style="width:40px;margin-top: 10px;float:left;"/>
		            <span style="color:red;margin-top:10px;float:left;">*（该处填写数字，多个以分号;分割）</span>
	          </div>
	       </div>
	
	</c:if>
	<c:if test="${groupcode=='StationSel'}">
	           <p class="stationTitle">台站选择类型：</p>
	           <div class="StationSel">
	              <select id="StationSel" name="StationSel" onChange="StationSelC(this)">
	                   <option value="ALLSel">所有台站</option>
	                   <option value="StationLevel">台站级别</option>
	                   <option value="StationList">台站列表</option>
	                   <option value="StationGIS">地图选站</option>
	                   <option value="Region">行政区划</option>
	                   <option value="Spatial">地图范围</option>
	                   <option value="StationScope">台站范围</option>
	              </select>
	           </div>
	            <!-- 台站级别 -->
		           <div class="StationLevel">
		             <p class="stationTitle">接口是否提供台站参数:</p>
		             <div class="stationItems">
			             <select id="StationSel_ALLSel" name="StationSel_ALLSel">
			                 <option value="eleValueRanges">否</option>
			                 <option value="staLevels">是</option>
			             </select>
			             <span style="color:red;">*（如果存在staIds台站参数选择是，无staIds台站参数选择否）</span>
			         </div>
			         <div class="stationItems">
		                <p class="stationTitle">级别选择类型:</p>
		                <select id="StationLevel_SM" name="StationLevel_SM">
		                     <option value="S">单选</option>
			                 <option value="M">多选</option>
			            </select>
		             </div>
		             <div class="stationItems">
		                <p class="stationTitle">是否显示:</p>
		                <select id="StationSel_IsHidden" name="StationSel_IsHidden">
			                 <option value="0">显示</option>
			                 <option value="1">隐藏</option>
			            </select>
		             </div>
		           </div>
		       <!-- 台站列表 -->
		           <div class="StationList">
		             <p class="stationTitle">接口是否提供台站参数：</p>
		             <select id="StationSel_StationList" name="StationSel_StationList">
		                 <option value="eleValueRanges">是</option>
		                 <option value="staIds">否</option>
		             </select>
		             <span style="color:red;">*（如果存在staIds台站参数选择是，无staIds台站参数选择否）</span>
		             <div class="stationItems">
		                 <p class="stationTitle">台站名称过滤：</p>
		                 <select id="StationList_Type" name="StationList_Type">
			                 <option value="0">是</option>
			                 <option value="1">否</option>
			             </select>
		             </div>
		             <div class="stationItems">
		                 <p class="stationTitle">默认台站：</p>
		                 <input type="text" id="StationList_defaultvalue" name="StationList_defaultvalue"
		                 style="width:100px;margin-top: 10px;float:left;"/>
		             </div>
		             <div class="stationItems">
		                <p class="stationTitle">台站级别过滤:</p>
		                <select id="StationList_SM" name="StationList_SM">
		                     <option value="FM">台站级别过滤，台站级别多选</option>
			                 <option value="FS">台站级别过滤，台站级别单选</option>
			                 <option value="XX">不过滤</option>
			            </select>
		             </div>
		           </div>
	           <!-- 地图选战 -->
		           <div class="StationGIS">
		             <div class="stationItems">
		                 <p class="stationTitle">台站名称过滤：</p>
		                 <select id="StationGIS_Type" name="StationGIS_Type">
			                 <option value="0">是</option>
			                 <option value="1">否</option>
			             </select>
		             </div>
		             <div class="stationItems">
		                <p class="stationTitle">台站级别过滤:</p>
		                <select id="StationGIS_SM" name="StationGIS_SM">
		                     <option value="FM">台站级别过滤，台站级别多选</option>
			                 <option value="FS">台站级别过滤，台站级别单选</option>
			                 <option value="XX">不过滤</option>
			            </select>
		             </div>
		              <div class="isValueLimit">
	             <p class="stationTitle">是否值范围限制 ：</p>
	             <div class="CheckBoxdiv">
	                 <input style="float:left;margin-top:15px;" type="radio" value="0" name="isvalueLimit" /><div style="float:left;margin-top:10px;">否</div>
	                 <input style="float:left;margin-top:15px;" type="radio"  value="1" name="isvalueLimit" checked="checked"/><div style="float:left;margin-top:10px;">是</div>
	             </div>
	             <div class="limitShow">
		             <p class="stationTitle">检索范围限制：</p>
		             <input type="text" name="valueLimit" value="${valuelimit}" style="width:50px;margin-top: 10px;" onkeyup="value=value.replace(/[^\-?\d.]/g,'')"/>
		             <select id="TimeSel_valueLimit" name="TimeSel_valueLimit">
		                   <option value="Count">个数</option>
		             </select>
		             <span style="color:red;">*（该处填写数字）</span>
	             </div>
	          </div>
		           </div>
		       <!-- 行政区划 -->
		           <div class="Region">
		             <p class="stationTitle">接口是否提供行政选择参数:</p>
		             <select id="StationSel_Region" name="StationSel_Region">
		                 <option value="eleValueRanges">否</option>
		                 <option value="adminCodes">是</option>
		             </select>
		             <br/>
		             <span style="color:red;">*（如果存在adminCodes行政选择参数选择是，无adminCodes行政选择参数选择否）</span>
		             <div class="stationItems">
		                 <p class="stationTitle">台站级别过滤：</p>
		                 <select id="searchSubType_Region" name="searchSubType_Region">
			                <option value="FM">台站级别过滤，台站级别多选</option>
			                 <option value="FS">台站级别过滤，台站级别单选</option>
			                 <option value="XX">不过滤</option>
			             </select>
		             </div>
		           </div>
		        <!-- 地图范围 -->
		           <div class="Spatial">
		             <p class="stationTitle">接口是否提供地图范围选择参数:</p>
		             <select id="StationSel_Spatial" name="StationSel_Spatial">
		                 <option value="eleValueRanges">否</option>
		                 <option value="minLon;maxLon;minLat;maxLat">是</option>
		             </select>
		              <br/>
		             <span style="color:red;">*（如果存在minLon;maxLon;minLat;maxLat范围选择参数选择是，无minLon;maxLon;minLat;maxLat范围选择参数选择否）</span>
		             <div class="stationItems">
		                 <p class="stationTitle">台站级别过滤：</p>
		                 <select id="searchSubType_Spatial" name="searchSubType_Spatial">
			                 <option value="FM">台站级别过滤，台站级别多选</option>
			                 <option value="FS">台站级别过滤，台站级别单选</option>
			                 <option value="XX">不过滤</option>
			             </select>
		             </div>
		           </div>
	</c:if>
	<c:if test="${groupcode=='ElementSel'}">
	    <div class="ElementSel">
	         <div class="elementTitle">要素类型选择：</div>
	         <input type="radio" name="ElementSel" value="ALLSel"/>所有要素
	         <input type="radio" name="ElementSel"  value="Element"/>要素选择
	    </div>
	</c:if>
	<c:if test="${groupcode=='ElementFilter'}">
	    <div class="ElementFilter">
	         <div class="elementfTitle">要素过滤选择：</div>
	          <input type="radio" name="ElementFilter" value="ALLSel"/>不过滤
	         <input type="radio" name="ElementFilter" value="EleFilter"/>要素过滤
	    </div>
	</c:if>
	<c:if test="${groupcode=='FormatSel'}">
	    <div class="formatTitle">名称：</div>
	    <input type="text" id="FormatSel_chname" name="FormatSel_chname" value="${FormatSel_chname}" style="width:100px;margin-top: 10px;float:left;"/>
	</c:if>
	<c:if test="${groupcode=='QCSel'}">
	      <div class="formatTitle">名称：</div>
	    <input type="text" id="QCSel_chname" name="QCSel_chname" value="${QCSel_chname}" style="width:100px;margin-top: 10px;float:left;"/>
	</c:if>
	<c:if test="${groupcode!='QCSel'&&groupcode!='FormatSel'&&groupcode!='ElementFilter'&&groupcode!='ElementSel'
	&&groupcode!='StationSel'&&groupcode!='TimeSel'}">
	    <div class="ComConfigStyle">
	         <label class="control-label">类型选择：</label>
	         <div class="controls" style="margin-left:10px;margin-top:10px;float:left;">	
		         <input type="radio" name="ComConfig" value="ALLSel"/>权限
		         <input type="radio" name="ComConfig"  value="ComConfig"/>字典表配置
	         </div>
	 </div>
	   <div class="stationItems">
	     <label class="control-label">中文名称：</label>
		 <input type="text" name="ComConfig_chname" id="ComConfig_chname" style="width:300px;margin-top: 10px;float:left;"/>
		</div>
		<div class="ComConfigShow">
			 <div class="stationItems">
			 <label class="control-label">过滤字段：</label>
			 <input type="text" name="ComConfig_subCode" id="ComConfig_subCode" style="width:300px;margin-top: 10px;float:left;"/>
			 </div>
			 <div class="stationItems">
			  <label class="control-label">选择类型：</label>
	                <select id="ComConfig_SM" name="ComConfig_SM" style="margin-top:5px;">
	                     <option value="S">单选</option>
		                 <option value="M">多选</option>
		            </select>
			 </div>
		 </div>
	</c:if>
	<div class="control-group" style="margin-top:20px;float:left;"> 
		<div class="controls">		
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保存" onclick="saveBottom();"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input  id="btnCancel" class="btn" type="button" value="关闭" onclick="closeWin();"/>
			
		</div>     
	</div>	
		 
    </form>
</div>
</body>
</html>

