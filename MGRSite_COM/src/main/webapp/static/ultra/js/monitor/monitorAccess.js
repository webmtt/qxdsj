var chartData,
	chartData2,
	chartData3,
	url = "http://"+window.document.location.hostname+"/cimissapiweb/stat%20_findByDeptOrBran.action",
	color = ['#4572A7', '#89A54E'],
	deptOrBranId = "all"; //单位或部门ID
	userid="user_pmsc_share",			//API用户ID
	timeType ='hour', //hour day  month  year
	className="user_pmsc_share",
	queryType = "data",   //要查询的是接口、资料、用户
	dataOrInterface = "user",
	titleSuffix="";
//var json = '{"sname":["公服中心一<br>体化加工平<br>台<br>","智能网格实<br>况陆面子系<br>统（CLD<br>AS）<br>","华信获取C<br>IMISS<br>数据<br>","会商室实时<br>业务林玉成<br>","数据网<br>","强天气监测<br>预报一体化<br>平台<br>","气候应用服<br>务平台资料<br>调用接口孙<br>除荣 <br>","智能预报系<br>统<br>","灾害性天气<br>短时临近预<br>报系统(S<br>WAN)<br>","M4系统<br>","气象服务图<br>形产品制作<br>","天气雷达基<br>数据拼图系<br>统/张乐坚<br>","全国海洋信<br>息共享系统<br>","数值模式出<br>图<br>","北京市空气<br>质量预报预<br>警平台<br>","个人研究<br>","在北京市气<br>象台业务系<br>统上测试C<br>IMISS<br>的可用性<br>","滚动读取地<br>面实况信息<br>","中国多源降<br>水融合系统<br>CMPA/<br>谷军霞<br>","数据同化分<br>析，检验诊<br>断<br>","温室气体业<br>务软件平台<br>调用CIM<br>ISS数据<br>","临近降水预<br>报系统开发<br>","中国气象频<br>道《风云进<br>行时》节目<br>直播触屏展<br>示系统数据<br>接入触屏系<br>统/赵笔锋<br>","天气网产品<br>研发<br>","海洋融合资<br>料系统建设<br>","科研/王丙<br>兰<br>","预警发布数<br>据支撑系统<br>/丰德恩<br>","业务内网<br>","hejia<br>njun<br>","用于读取观<br>测和模式数<br>据制作客观<br>预报产品<br>","专业产品研<br>发辛宏伟<br>","公服中心数<br>据服务系统<br>","大气环境模<br>拟<br>","数值预报N<br>WP基于C<br>IMISS<br>的检索系统<br>开发/李然<br>","国家级人影<br>业务系统<br>","气象灾害信<br>息管理系统<br>/朱海军<br>","实时下载地<br>面数据-孙<br>靖<br>","研究室朱晨<br>/用于ci<br>miss数<br>据下载与处<br>理<br>","MESIS<br>","服务岗为用<br>户制作离线<br>数据使用<br>","业务应用<br>","bdwan<br>gshua<br>i<br>","专业气象服<br>务产品研发<br>","王铸<br>","姜灵峰<br>","杨美玲<br>","精细化预报<br>","华云星地通<br>，下载观测<br>数据，应用<br>于风云4号<br>PGS工程<br>化<br>","气象应用室<br>业务系统<br>","中国兴农网<br>/邓学志<br>","CMA陆面<br>数据同化系<br>统（CLD<br>AS）/姜<br>志伟；调取<br>地面站点观<br>测数据<br>","实时提取站<br>点数据，供<br>气科院用户<br>使用<br>","数值预报业<br>务运行<br>","戴至修<br>","三维云分析<br>系统<br>","卫星监测分<br>析与遥感应<br>用系统SM<br>ART<br>","资料提取<br>","国际交换气<br>候月报编码<br>系统<br>","LAPS/<br>黄琰<br>","FY-3 <br>质量检验系<br>统为检验卫<br>星反演产品<br>京都，而获<br>取地面、高<br>空、雷达等<br>数据<br>","农业气象中<br>心CAgM<br>SS的开发<br>帐号/吴门<br>新<br>","资料同化<br>","马新成<br>","业务运行，<br>系统开放<br>","接口测试<br>","全国智能网<br>格预报实况<br>分析系统<br>","业务科研<br>","一键发布平<br>台<br>","CIPAS<br>2.0<br>","陕西用于获<br>取国家级已<br>接入但省级<br>未接入的资<br>料<br>","迟文学<br>","极端气候系<br>统<br>","服务产品加<br>工处理<br>","交通气象科<br>研<br>","spds2<br>s123<br>","天气雷达、<br>风廓线雷达<br>质量控制及<br>相关产品研<br>究<br>","cldas<br>/李显风<br>","开发测试(<br>非实时用户<br>)<br>","(数值模式<br>天气学检验<br>/代刊）<br>","MICAP<br>S3<br>","用于气象服<br>务数据支撑<br>平台的数据<br>获取<br>","Linux<br>","dunhu<br>ang<br>","人影项目（<br>华信一部）<br>","用于土壤水<br>分数据处理<br>与应用分析<br>平台使用<br>","分钟降水产<br>品加工/高<br>金兵<br>","数值预报科<br>研运行<br>","数据检索<br>","测试<br>","工作学习<br>","张京江<br>","全球大气再<br>分析实时系<br>统<br>","用于项目/<br>课题研究。<br>","MESIS<br>系统业务应<br>用<br>","雷电数据质<br>量控制<br>","业务<br>","台风预报业<br>务<br>","李雁鹏，用<br>于MDSS<br>到CIMI<br>SS数据源<br>切换<br>","四川精细化<br>(华信三部<br>)<br>","获取气象数<br>据进行分析<br>","数据调取<br>","山洪系统 <br> 陈京华<br>","业务运行<br>","AAA<br>","科研<br>","Meng <br>Zhaol<br>in<br>","cipas<br>1开发<br>","科研（GC<br>GM/马玉<br>平）<br>","信息库<br>","颜京辉<br>","刘瑞霞<br>","中国天气网<br>数据加工<br>","全球基础数<br>据产品及均<br>一化和数据<br>产品研制<br>","调用数据<br>","多源海温融<br>合系统/韩<br>帅<br>","调用资料，<br>业务科研应<br>用。<br>","wangm<br>y@cma<br>.gov.<br>cn<br>","ABC<br>","天气预测科<br>研业务AI<br>X/Lin<br>ux/Wi<br>n/ 陈涛<br>","111<br>","云地面观测<br>自动化<br>","数据服务<br>","高静<br>","农业气象中<br>心的研究人<br>员帐号/吴<br>门新<br>","雷达资料质<br>量控制及产<br>品应用培训<br>系统<br>","NMIC_<br>ZLS_D<br>L<br>","气科院的M<br>ICAPS<br>系统<br>","APi<br>","资料服务室<br>","王琦-系统<br>测试<br>","服务岗<br>","调取数据<br>","MICAP<br>S4<br>","编程/郭庆<br>燕<br>","shili<br>u910<br>","测试<br>","CIMIS<br>S<br>","地面业务测<br>试<br>","科研业务<br>","下载气象数<br>据<br>","天气雷达算<br>法与业务试<br>验系统<br>","雷电预警预<br>报系统<br>","winow<br>s64/曹<br>云<br>","海洋观测业<br>务<br>","科研，业务<br>公共气象服<br>务中心/赵<br>晓栋<br>","强对流天气<br>分析曹冬杰<br>","业务内网栏<br>目监控<br>","业务及科研<br>工作<br>","科研<br>","全球常规资<br>料实时PR<br>EPBUF<br>R格式转换<br>流程<br>","CIMIS<br>S<br>","全球卫星定<br>位气象水汽<br>国家级处理<br>平台/梁宏<br>","music<br>管理账户<br>","检索下载全<br>球高空、飞<br>机等观测资<br>料及再分析<br>产品，开展<br>常规资料质<br>量控制、评<br>估和偏差订<br>正工作<br>"],"qtime":[302.9583,279.1194,169.1096,163.3969,147.1102,140.8227,107.8361,106.6361,94.9951,72.7943,67.7203,61.8892,55.3648,53.3321,44.5638,40.7578,39.9786,31.9124,27.4057,23.4975,21.8068,13.059,12.6289,12.254,11.6788,11.4673,10.769,9.3545,7.8241,6.748,6.3666,6.3562,6.0792,5.8959,5.3502,5.2033,5.0432,5.0348,4.8001,4.6434,4.0178,3.8091,3.7901,3.65,3.5746,3.5453,3.0545,2.3529,2.2923,2.2153,2.2086,1.5967,1.5533,1.5069,1.3386,1.2718,1.1675,0.9657,0.8307,0.7884,0.7218,0.6162,0.6072,0.5959,0.5132,0.5126,0.4227,0.3937,0.3922,0.3732,0.3645,0.3592,0.2521,0.2499,0.2147,0.1768,0.1716,0.1584,0.1454,0.1347,0.1297,0.1251,0.1157,0.1049,0.0921,0.089,0.0843,0.0773,0.0745,0.0677,0.0634,0.0582,0.0541,0.0476,0.0402,0.0401,0.0324,0.0276,0.0263,0.0258,0.0218,0.0198,0.0173,0.0143,0.0143,0.0093,0.0091,0.0091,0.0071,0.0066,0.0065,0.0063,0.0059,0.0057,0.0051,0.005,0.0044,0.0043,0.0042,0.0036,0.0035,0.0033,0.0033,0.003,0.0028,0.0027,0.0026,0.0025,0.0024,0.0024,0.0021,0.0021,0.0021,0.002,0.0016,0.0016,0.0016,0.0014,0.0014,0.0012,9.0E-4,8.0E-4,7.0E-4,7.0E-4,6.0E-4,5.0E-4,5.0E-4,5.0E-4,4.0E-4,3.0E-4,1.0E-4,1.0E-4,1.0E-4,1.0E-4],"dsize":[1151.17,19.65,4582.36,10527.37,177.04,2110,332.87,170.65,8.95,882.95,5.95,731,728.25,1607.21,12,6.81,0.4,1178.75,183.14,15093.01,32.79,688.99,330.56,1061.74,430.69,147.49,12.59,1.9,17.91,674.61,1231.77,1399.08,1.28,339.43,1979.18,125.93,98.88,27.23,1.42,0.044,357.81,0.21,290.44,10.07,1.39,0.38,20.98,118.68,5.93,1.27,66.37,89.14,571.41,14.04,164,24.06,9.96,0.0051,39.42,34.09,2.9,203.08,0.73,6.95,83.51,288.1,27.83,6.9,0.69,0.034,0.093,2.61,0.0082,0.005,0.0046,3,3.75,8.0E-9,1.25,1.63,73.88,0.6,0.18,6.97,0.44,0.23,2.78,1.45,0.024,0.025,3.1E-4,7.67,0.16,9.73,3.74,0.32,0.011,0.19,0.005,1.42,0.0059,0.0015,0.1,0.043,1.77,6.0E-4,1.13,12.83,0.0018,0.15,0.088,6.4E-5,0.19,0.35,6.0E-5,0.061,0.074,0,6.21,5.9E-4,4.4E-4,0.0048,0.022,0.0044,0.058,2.6E-4,0.26,6.8E-5,0.0011,1.0E-5,7.4E-5,2.1E-4,2.3E-6,1.1E-4,5.8E-5,0.096,3.6E-5,7.2E-5,1.2E-4,0.0081,0.2,0,0.027,7.4E-4,0.003,9.5E-4,4.9E-5,6.5E-5,1.2E-6,2.6E-6,0,1.5E-6,1.1E-5,3.6E-6],"userName":["user_pmsc_share","NMIC_YJS_NMIC_YJS_hans","NMIC_YKS_hitec","NMC_YBS_linyc","usr_sjw","NMC_QTQ_swpc","NCC_FZYK_clap","PMSC_XTKFS_aiweather","NMC_XTKF_SWAN_NMC","NMC_XTKF_M4","NMIC_ZLS_qxtxcp","MOC_LDS_RADAR","NMC_TFHY_zhaowei0045","NMIC_ZLS_NAFP","BEPK_HJZX_JJJ_HJZX","NMIC_XTS_xiongay","BEPK_QXT_wangguorong","NMC_XTKF_nwfd","NMIC_YJS_GUJUNXIA","NMC_SZYB_YANGJX","MOC_DQGC_fangsx","NMC_YBS_schen","PMSC_CPS_zhaobf","PMSC_TQW_YUJIN","NMIC_YJS_03170317","PMSC_ZYYHJQXS_wangbl","PMSC_XTKFS_fengdeen","user_nmic_idata","CAMS_DQCFS_hejianjun","NMC_YBS_liucouhua","PMSC_TQW_XHW","user_pmsc_fyx","CAMS_DQCFS_CHH","NMC_SZYB_TSW","CAMS_RYZX_LIDQ","NCC_PGS_SUPERMAP","NMC_XTKF_sjlinger","NMIC_YJS_zhuchen","nmc_mesis","NMIC_ZLS_chenjh_1","NMC_YBS_qhf618","NMIC_YKS_bdwangshuai","PMSC_ZYQXT_wis1","NMC_QXFW_juecefuwu","NSMC_YGS_jianglf","NMC_YBS_yangmeiling","BEPK_QXT_wyebd","NSMC_KZS_ZHANGXZ","PMSC_QXYYS_qxyys","PMSC_xn121_weather","NMIC_YJS_ldas","CAMS_XXB_camszwh","NMC_SZYB_nwp","PMSC_ZYQXT_dzx","NMIC_YJS_zhuzhi1991","NSMC_YGS_ZYP","NCC_FZYK_liubei","NMIC_XTS_CSCI","PMSC_XTKFS_huangy","NSMC_YGS_NSMC_FY3_QCS","NMC_NYQX_developer","NMC_SZYB_HANWEI","BEPK_QXT_bjryb","PMSC_XTKFS_luyetao","NMIC_XTS_hujin","NMIC_YJS_NMIC_YJS_CMPASv2","PMSC_ZYYHJQXS_zrw","COC_XCB_xsy","NCC_FZYK_CIPAS2","NMIC_XTS_BEXA_HELIN","MOC_DQGC_FLZ","NCC_FWS_gaorong","PMSC_ZYQXT_USR","PMSC_ZYQXT_CZY","NCC_FZYK_spds2s123","CAMS_ZHSYS_RADAR","NMIC_YJS_lxf","user_nordb","NMC_YBS_daikan","user_nmc_m3","PMSC_SJYY_caoyz","CAMS_QHS_chenhm","NSMC_WXQX_liyuan","HX_RY","MOC_GCS_ASMDC","PMSC_XTKFS_gaojinbing","NMC_SZYB_nwp_ex","NMIC_ZLS_liuym","NMIC_XTS_lgg","NMIC_YJS_nuist_liu","BEPK_QXT_ZJJ","NMIC_YJS_CRA","PMSC_ZYQXT_sheroya","NMC_XTKF_yinduasan","MOC_GCS_wangzhichao","NMIC_ZLS_13520072259","NMC_TFHY_qianqf","PMSC_SJYY_LIYP","HX_SC_JXH","NMC_YBS_112","NMIC_ZLS_xuyan","NMIC_ZLS_BuWei","NMIC_YJS_YuanF","NMC_YBS_test","NMC_YBS_zhoujun","MOC_LDS_storm","NCC_FZYK_cipas1","CAMS_NQS_upwind","NMIC_ZLS_XXK","NCC_MSS_yanjh","NMC_YBS_ruix_liu","PMSC_TQW_chinaweather","NMIC_YJS_yangsu","NMIC_ZLS_hr08","NMIC_YJS_zz1991","NMIC_YJS_clj","NMIC_YJS_wangmy","NMC_YBS_YY","NMC_YBS_chentao","MOC_ZLJK_111","MOC_SYJD_taofa","NMIC_ZLS_fengmn","NMIC_ZLS_gaoj","NMC_NYQX_researcher","CMATC_YWPX_jiangxiaofei","NMIC_ZLS_DL","CAMS_XXB_micaps","NMIC_ZLS_fengax","NMIC_ZLS_data_cma","NMIC_XTS_wq","NMIC_ZLS_liuyj","NMIC_YJS_wanghy","NMC_XTKF_tricker","NMIC_ZLS_bella","NCC_YCS_shiliu910","NMIC_ZLS_test01","NMIC_ZLS_luolx123","MOC_GCS_lh","PMSC_ZYYHJQXS_3577666","NMC_XTKF_fsol","CAMS_ZHSYS_wuchong","CAMS_ZHSYS_ttpkq","NMC_NYQX_caoy","MOC_GCS_lixiaoxia","PMSC_ZYQXT_zhaoxiaodong","NSMC_YGS_NSMC_YGS_chao","NMIC_ZLS_liuna","PMSC_ZYQXT_miaol","NMIC_XTS_jialidong","NMIC_YJS_zhangt","NMIC_YKS_emily","MOC_GCS_MOIST","NMIC_ZLS_musicadmin","NMIC_YJS_liaojie"]}';	

function selectById(id){
	$.ajax({
		url:ctx+'/user/getUserById',
		type:'POST',
		data:{'id':id},
		async: false,
		dataType:'html',
		success:function(result){
			$(".modal-body").empty();
			$(".modal-body").html(result);
			$("#myModalLabel").text("用户详细信息");
			$("#myModal").addClass("in");
			$("#myModal").css("display","block");
		}
	});
}
function closemodel(){
	$("#myModal").removeClass("in");
	$("#myModal").css("display","none");
}
function getOrderInfoByUserId(id){
	$.ajax({
		url:ctx+'/monitor/access/getOrderInfoByUserId',
		data: {
			"stime": stime,
			"etime": etime,
			"userId":id
		},
		type: 'post',
		cache: false,
		dataType: 'json',
		async: false,
		success: function(data) {
			var str = "";
			var names = data.names;
			var counts = data.counts;
			var filesizes = data.filesizes;
			var dataCodes = data.dataCodes;
			var length = dataCodes.length;
			str +="<html><body>"
			for(i=0;i<length;i++){
				str +="<table><tr><td  width='150'>资料名称:</td><td>"+names[i]+"</td></tr>";
				str +="<tr><td  width='150'>资料订单量:</td><td>"+counts[i]+"</td></tr>";
				str +="<tr><td  width='150'>资料数据量:</td><td>"+filesizes[i]+"(GB)</td></tr>";
				str +="<tr><td  width='150'>资料编码:</td><td>"+dataCodes[i]+"</td></tr></table>";
				str +="<br/>"
				//str += "资料名称："+names[i]+",资料订单量："+counts[i]+"资料数据量："+filesizes[i]+"资料编码："+dataCodes[i]+"<br/>";
			}
			str +="</body></html>"
			$(".modal-body").empty();
			$(".modal-body").html(str);
			$("#myModalLabel").text("按资料统计该用户订单信息");
			$("#myModal").addClass("in");
			$("#myModal").css("display","block");
		}	
	})
}

function getData() {
	//$('#myModal').hide();
	console.log(stime);
	console.log(etime);
	$.ajax({
		url: url,
		data: {
			"stime": stime,
			"etime": etime,
			"areaName": "",
			"deptOrBranId": deptOrBranId,
			"userid":userid,
			"timeType":timeType,
			"className":className,
			"queryType": queryType,
			"dataOrInterface":dataOrInterface
		},
		type: 'post',
		cache: false,
		dataType: 'json',
		async: false,
		success: function(data) {
			chartData = data;
		}
	});
	//chartData = JSON.parse(json);
	$.ajax({
		url:ctx+'/monitor/access/getTop10OrderCountByUser',
		data: {
			"stime": stime,
			"etime": etime
		},
		type: 'post',
		cache: false,
		dataType: 'json',
		async: false,
		success: function(data) {
			chartData2 = data;
		}
	});
	$.ajax({
		url:ctx+'/monitor/access/getTop10FtpDownloadOrderByIp',
		data: {
			"stime": stime,
			"etime": etime
		},
		type: 'post',
		cache: false,
		dataType: 'json',
		async: false,
		success: function(data) {
			chartData3 = data;
		}
	});
};

// 绘制第一个图表
function redrawChart() {
	//url='findByDeptOrBran';
	getData();
	if(chartData!=undefined){
		var max;
		max = chartData.sname.length > 9 ? 9 : chartData.sname.length - 1;
		if( typeof(chartData.sname) != 'undefined'){
			if (chartData.sname.length > 0) {
				for(var i=0; i< chartData.sname.length; i++){
					if(chartData.sname[i].length > 50){
						chartData.sname[i] = chartData.sname[i].substring(0,49);
					}
					
				}
				userid = className = chartData.userName[0];
				userNames = chartData.userName;
				userids = chartData.sname;
				userName = chartData.sname[0];
			}else{
				userid = "";
				userName = "";
			}
		}else {
			userid = "";
			userName = "";
		}
		$("#containerOne").highcharts({
			credits: {
				enabled: false
			},
			chart: {
				panning: true,
				type: 'column'
			},
			exporting:{
				sourceWidth:1200,
				scale:1
			},
			title: {
				text:  '用户访问情况分析'+ '<br/>（' + titleSuffix + '）'
			},
			xAxis: [{
				categories: chartData.sname,
				labels: {
					align: 'center',
					style: {
						fontSize: '13px',
						fontFamily: '微软雅黑'
					}
				},
				min: 0,
				max: max,
			}],
			yAxis: [{
				title: {
					text: '数据量(GB)',
					style: {
						color: color[0]
					}
				},
				labels: {
					style: {
						color: color[0]
					}
				},
				min: 0
			}, {
				title: {
					text: '访问次数(万次)',
					style: {
						color: color[1]
					}
				},
				labels: {
					style: {
						color: color[1]
					}
				},
				min: 0,
				opposite: true
			}],
			tooltip: {
				shared:true,
				formatter: function() {
					var s = '';
					 $.each(this.points, function(){
		        	    	if(s == '')
		        	    		s = '<b>'+ this.key.replace(new RegExp("<br>","gm"),"") +'</b>';
		        	    	s += '<br/>' + this.series.name+":"+this.y ;
		        	    });	
					 return s;
				}
			},
			plotOptions: {
				series: {
					//pointWidth: 10
					//,groupPadding: 0.8

				},
				column: {
					//pointWidth: 30,//柱粗细
					pointPadding: 0.2,
					borderWidth: 0,
					cursor: 'pointer',
					point: {
						events: {
							click: function() {
								userid = className  = this.category;
								userName = this.category.replace(new RegExp("<br>","gm"),"")
								for ( var i = 0; i < userids.length; i++) {
	                	  			if(userids[i]==userid){
	                	  				userid = className = userNames[i];
	                	  				break;
	                	  			}
	                	  		}
							}
						}
					}
				}
			},
			series: [{
				name: '数据量（GB）',
				color: color[0],
				type: 'column',
				yAxis: 0,
				data: chartData.dsize,
				tooltip: {
					valueSuffix: ' GB'
				}
			}, {
				name: '访问次数（万次）',
				color: color[1],
				type: 'column',
				yAxis: 1,
				data: chartData.qtime,
				tooltip: {
					valueSuffix: '万条'
				}
			}]
		});
	}
	
	//订单图表
	var chart = Highcharts.chart('containerTwo', {
		credits: {
			enabled: false
		},
		chart: {
			panning: true,
			type: 'column'
		},
		exporting:{
			sourceWidth:1200,
			scale:1
		},
		title: {
			text:  '用户订单量和数据量'+ '<br/>（' + titleSuffix + '）'
		},
		xAxis: [{
			categories: chartData2.names,
			labels: {
				align: 'center',
				style: {
					fontSize: '13px',
					fontFamily: '微软雅黑',
					cursor: 'pointer'
				}
			},
			min: 0,
			max: max,
		}],
		yAxis: [{
			title: {
				text: '数据量(GB)',
				style: {
					color: color[0]
				}
			},
			labels: {
				style: {
					color: color[0]
				}
			},
			min: 0
		}, {
			title: {
				text: '订单量(次)',
				style: {
					color: color[1]
				}
			},
			labels: {
				style: {
					color: color[1]
				}
			},
			min: 0,
			opposite: true
		}],
		tooltip: {
			shared:true,
			formatter: function() {
				var s = '';
				 $.each(this.points, function(){
	        	    	if(s == '')
	        	    		s = '<b>'+ this.key.replace(new RegExp("<br>","gm"),"") +'</b>';
	        	    	s += '<br/>' + this.series.name+":"+this.y ;
	        	    });	
				 return s;
			}
		},
		plotOptions: {
			series: {
				cursor: 'pointer',
				point: {
					events: {
						click: function () {
							//alert('Category: ' + this.category + ', value: ' + this.y);
							var  index = this.x;
							var id = chartData2.ids[index];
							//selectById(id);
							/**/
							getOrderInfoByUserId(id);
						}
					}
				}
			}
		},
		series: [{
			name: '数据量（GB）',
			color: color[0],
			type: 'column',
			yAxis: 0,
			data: chartData2.filesizes,
			tooltip: {
				valueSuffix: ' GB'
			}
		}, {
			name: '订单量（次）',
			color: color[1],
			type: 'column',
			yAxis: 1,
			data: chartData2.counts,
			tooltip: {
				valueSuffix: '条'
			}
		}]
	}, function(c) {
		// 方法2：给坐标轴标签 DOM 添加点击事件， 并根据事件坐标计算出 x 下标值
		Highcharts.addEvent(c.xAxis[0].labelGroup.element, 'click', e => {
			e = c.pointer.normalize(e);
			//此处根据鼠标点击的位置计算出x轴的下标，由向上取整改为四舍五入计算
			//let index = Math.ceil(c.xAxis[0].toValue(e.chartX));
			let index = Math.round(c.xAxis[0].toValue(e.chartX));
			var id = chartData2.ids[index];
			selectById(id);
		});
	});
	
	
	//FTP访问量和下载量图表
	
	var chart = Highcharts.chart('containerThree', {
		credits: {
			enabled: false
		},
		chart: {
			panning: true,
			type: 'column'
		},
		exporting:{
			sourceWidth:1200,
			scale:1
		},
		title: {
			text:  'FTP访问量和文件量'+ '<br/>（' + titleSuffix + '）'
		},
		xAxis: [{
			categories: chartData3.ips,
			labels: {
				align: 'center',
				style: {
					fontSize: '13px',
					fontFamily: '微软雅黑',
					cursor: 'pointer'
				}
			},
			min: 0,
			max: max,
		}],
		yAxis: [{
			title: {
				text: '数据量(GB)',
				style: {
					color: color[0]
				}
			},
			labels: {
				style: {
					color: color[0]
				}
			},
			min: 0
		}, {
			title: {
				text: '访问次数(次)',
				style: {
					color: color[1]
				}
			},
			labels: {
				style: {
					color: color[1]
				}
			},
			min: 0,
			opposite: true
		}],
		tooltip: {
			shared:true,
			formatter: function() {
				var s = '';
				 $.each(this.points, function(){
	        	    	if(s == '')
	        	    		s = '<b>'+ this.key.replace(new RegExp("<br>","gm"),"") +'</b>';
	        	    	s += '<br/>' + this.series.name+":"+this.y ;
	        	    });	
				 return s;
			}
		},
		series: [{
			name: '数据量（GB）',
			color: color[0],
			type: 'column',
			yAxis: 0,
			data: chartData3.filesizes,
			tooltip: {
				valueSuffix: ' GB'
			}
		}, {
			name: '访问次数（次）',
			color: color[1],
			type: 'column',
			yAxis: 1,
			data: chartData3.counts,
			tooltip: {
				valueSuffix: '次'
			}
		}]
	}, function(c) {
		// 方法2：给坐标轴标签 DOM 添加点击事件， 并根据事件坐标计算出 x 下标值
		Highcharts.addEvent(c.xAxis[0].labelGroup.element, 'click', e => {
			e = c.pointer.normalize(e);
			//let index = Math.ceil(c.xAxis[0].toValue(e.chartX));
			let index = Math.round(c.xAxis[0].toValue(e.chartX));
			var str ="";
			str +="<html><body>"
			str +="<table><tr><td  width='100'>局:</td><td>"+chartData3.pNames[index]+"</td></tr>";
			str +="<tr><td  width='100'>科:</td><td>"+chartData3.names[index]+"</td></tr>";
			str +="<tr><td  width='100'>室:</td><td>"+chartData3.posts[index]+"</td></tr>";
			str +="<tr><td  width='100'>职位:</td><td>"+chartData3.positions[index]+"</td></tr>";
			str +="<tr><td  width='100'>职称:</td><td>"+chartData3.titles[index]+"</td></tr></table>";
			str +="<br/>"
			str +="</body></html>"
			$("#myModalLabel").text("IP详细信息");
			$(".modal-body").empty();
			$(".modal-body").html(str);
			$("#myModal").addClass("in");
			$("#myModal").css("display","block");
		});
	});
	
}


// 设置默认时间
function settime() {
	var curr_time = new Date();
	curr_time.setDate(curr_time.getDate() - 7);
	$("#sTime").datebox("setValue", myformatter(curr_time));
	stime = myformatterToStr(curr_time);
	var curr_time = new Date();
	curr_time.setDate(curr_time.getDate());
	$("#eTime").datebox("setValue", myformatter(curr_time));
	etime = myformatterToStr(curr_time);
	titleSuffix = myformatterToTitleSuffix(curr_time);
}


function queryData(s, e, index) {
	if (s != null && e != null) {
		var curr_time = new Date();
		curr_time.setDate(curr_time.getDate() + s);
		var str = myformatter(curr_time) + curr_time.setDate(curr_time.getDate() + e);
		$("#endTime").datebox("setValue", myformatter(curr_time));
	}
}

function myformatterToStr(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + "" + (m < 10 ? ('0' + m) : m) + "" + (d < 10 ? ('0' + d) : d);
}

function myformatterToTitleSuffix(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + '年' + (m < 10 ? ('0' + m) : m) + '月' + (d < 10 ? ('0' + d) : d) + "日";
}

function onSelectSingleSt(date) {
	stime = myformatterToStr(date);
}

function onSelectSingleEt(date) {
	etime = myformatterToStr(date);
}

var colo;
function setColumnChart(title, x, y, obj, index, colo) {

}

function myformatterToHours(date) {         
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	var h = date.getHours();
	return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d + " " + h + ":00");
}

function myformatter(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
}

function myparser(s) {
	if (!s) return new Date();
	var ss = (s.split('-'));
	var y = parseInt(ss[0], 10);
	var m = parseInt(ss[1], 10);
	var d = parseInt(ss[2], 10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
		return new Date(y, m - 1, d);
	} else {
		return new Date();
	}
}

function queryData2(s, e, sh, eh) {
	if (s != null && e != null && sh != null && eh != null) {
		var curr_time = new Date();
		curr_time.setDate(curr_time.getDate() + s);
		var str = myformatter(curr_time) + " " + sh + ":00";
		var curr_time = new Date();
		curr_time.setDate(curr_time.getDate() + e);
		var str = myformatter(curr_time) + " " + eh + ":00";
		$("#singleTime").datetimebox("setValue", str);
	}
}

function strToDate(str) {
	 var dateStrs = str.split("-");
	 var year = parseInt(dateStrs[0], 10);
	 var month = parseInt(dateStrs[1], 10) - 1;
	 var day = parseInt(dateStrs[2], 10);	
	 var date = new Date(year, month, day);
	 return date;
	}


$(function() {
	Highcharts.setOptions({  
       colors: ['#058DC7', '#50B432', '#ED561B','#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4']  
	});  
 
	
	/*$("#userStati").css("color", "#B90DA0");
	$('#statics').addClass('topMenu_active');
	*/
	
	$("#serchBtn").click(function() {
		var startTime = $("#sTime").datebox("getValue");
		var endTime = $("#eTime").datebox("getValue");
		stime = startTime.replace(/-/g, "");
		etime = endTime.replace(/-/g, "");
		var s = new Date(Date.parse(startTime.replace(/-/g, "/"))); // 转换成Data();
		var e = new Date(Date.parse(endTime.replace(/-/g, "/")));
		titleSuffix = myformatterToTitleSuffix(s) + "-" + myformatterToTitleSuffix(e);
		redrawChart();
	});
	$("#serchBtnWeek").click(function() {
		var startTime = dateRangeUtil.getCurrentWeek()[0].Format("yyyy-MM-dd");
		var endTime = dateRangeUtil.getCurrentWeek()[1].Format("yyyy-MM-dd");
		stime = startTime.replace(/-/g, "");
		etime = endTime.replace(/-/g, "");
		var s = new Date(Date.parse(startTime.replace(/-/g, "/"))); // 转换成Data();
		var e = new Date(Date.parse(endTime.replace(/-/g, "/")));
		titleSuffix = myformatterToTitleSuffix(s) + "-" + myformatterToTitleSuffix(e);
		redrawChart();
	});
	$("#serchBtnMonth").click(function() {
		var startTime = dateRangeUtil.getCurrentMonth()[0].Format("yyyy-MM-dd");
		var endTime = dateRangeUtil.getCurrentMonth()[1].Format("yyyy-MM-dd");
		stime = startTime.replace(/-/g, "");
		etime = endTime.replace(/-/g, "");
		var s = new Date(Date.parse(startTime.replace(/-/g, "/"))); // 转换成Data();
		var e = new Date(Date.parse(endTime.replace(/-/g, "/")));
		titleSuffix = myformatterToTitleSuffix(s) + "-" + myformatterToTitleSuffix(e);
		redrawChart();
	});
	$("#serchBtnSeason").click(function() {
		var startTime = dateRangeUtil.getCurrentSeason()[0].Format("yyyy-MM-dd");
		var endTime = dateRangeUtil.getCurrentSeason()[1].Format("yyyy-MM-dd");
		stime = startTime.replace(/-/g, "");
		etime = endTime.replace(/-/g, "");
		var s = new Date(Date.parse(startTime.replace(/-/g, "/"))); // 转换成Data();
		var e = new Date(Date.parse(endTime.replace(/-/g, "/")));
		titleSuffix = myformatterToTitleSuffix(s) + "-" + myformatterToTitleSuffix(e);
		redrawChart();
	});
	settime();
	
	$("#serchBtn").trigger("click");
				
});


//对Date的扩展，将 Date 转化为指定格式的String   
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
//例子：   
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
Date.prototype.Format = function (fmt) { //author: meizz   
 var o = {
     "M+": this.getMonth() + 1,                 //月份   
     "d+": this.getDate(),                    //日   
     "h+": this.getHours(),                   //小时   
     "m+": this.getMinutes(),                 //分   
     "s+": this.getSeconds(),                 //秒   
     "q+": Math.floor((this.getMonth() + 3) / 3), //季度   
     "S": this.getMilliseconds()             //毫秒   
 };
 if (/(y+)/.test(fmt))
     fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
 for (var k in o)
     if (new RegExp("(" + k + ")").test(fmt))
         fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
 return fmt;
}

/**
* 日期范围工具类
*/
var dateRangeUtil = (function () {
    /***
    * 获得当前时间
    */
    this.getCurrentDate = function () {
        return new Date();
    };

    /***
    * 获得本周起止时间
    */
    this.getCurrentWeek = function () {
        //起止日期数组  
        var startStop = new Array();
        //获取当前时间  
        var currentDate = this.getCurrentDate();
        //返回date是一周中的某一天  
        var week = currentDate.getDay();
        //返回date是一个月中的某一天  
        var month = currentDate.getDate();

        //一天的毫秒数  
        var millisecond = 1000 * 60 * 60 * 24;
        //减去的天数  
        var minusDay = week != 0 ? week - 1 : 6;
        //alert(minusDay);  
        //本周 周一  
        var monday = new Date(currentDate.getTime() - (minusDay * millisecond));
        //本周 周日  
        var sunday = new Date(monday.getTime() + (6 * millisecond));
        //添加本周时间  
        startStop.push(monday); //本周起始时间  
        //添加本周最后一天时间  
        startStop.push(sunday); //本周终止时间  
        //返回  
        return startStop;
    };

    /***
    * 获得本月的起止时间
    */
    this.getCurrentMonth = function () {
        //起止日期数组  
        var startStop = new Array();
        //获取当前时间  
        var currentDate = this.getCurrentDate();
        //获得当前月份0-11  
        var currentMonth = currentDate.getMonth();
        //获得当前年份4位年  
        var currentYear = currentDate.getFullYear();
        //求出本月第一天  
        var firstDay = new Date(currentYear, currentMonth, 1);


        //当为12月的时候年份需要加1  
        //月份需要更新为0 也就是下一年的第一个月  
        if (currentMonth == 11) {
            currentYear++;
            currentMonth = 0; //就为  
        } else {
            //否则只是月份增加,以便求的下一月的第一天  
            currentMonth++;
        }


        //一天的毫秒数  
        var millisecond = 1000 * 60 * 60 * 24;
        //下月的第一天  
        var nextMonthDayOne = new Date(currentYear, currentMonth, 1);
        //求出上月的最后一天  
        var lastDay = new Date(nextMonthDayOne.getTime() - millisecond);

        //添加至数组中返回  
        startStop.push(firstDay);
        startStop.push(lastDay);
        //返回  
        return startStop;
    };

    /**
    * 得到本季度开始的月份
    * @param month 需要计算的月份
    ***/
    this.getQuarterSeasonStartMonth = function (month) {
        var quarterMonthStart = 0;
        var spring = 0; //春  
        var summer = 3; //夏  
        var fall = 6;   //秋  
        var winter = 9; //冬  
        //月份从0-11  
        if (month < 3) {
            return spring;
        }

        if (month < 6) {
            return summer;
        }

        if (month < 9) {
            return fall;
        }

        return winter;
    };

    /**
    * 获得该月的天数
    * @param year年份
    * @param month月份
    * */
    this.getMonthDays = function (year, month) {
        //本月第一天 1-31  
        var relativeDate = new Date(year, month, 1);
        //获得当前月份0-11  
        var relativeMonth = relativeDate.getMonth();
        //获得当前年份4位年  
        var relativeYear = relativeDate.getFullYear();

        //当为12月的时候年份需要加1  
        //月份需要更新为0 也就是下一年的第一个月  
        if (relativeMonth == 11) {
            relativeYear++;
            relativeMonth = 0;
        } else {
            //否则只是月份增加,以便求的下一月的第一天  
            relativeMonth++;
        }
        //一天的毫秒数  
        var millisecond = 1000 * 60 * 60 * 24;
        //下月的第一天  
        var nextMonthDayOne = new Date(relativeYear, relativeMonth, 1);
        //返回得到上月的最后一天,也就是本月总天数  
        return new Date(nextMonthDayOne.getTime() - millisecond).getDate();
    };

    /**
    * 获得本季度的起止日期
    */
    this.getCurrentSeason = function () {
        //起止日期数组  
        var startStop = new Array();
        //获取当前时间  
        var currentDate = this.getCurrentDate();
        //获得当前月份0-11  
        var currentMonth = currentDate.getMonth();
        //获得当前年份4位年  
        var currentYear = currentDate.getFullYear();
        //获得本季度开始月份  
        var quarterSeasonStartMonth = this.getQuarterSeasonStartMonth(currentMonth);
        //获得本季度结束月份  
        var quarterSeasonEndMonth = quarterSeasonStartMonth + 2;

        //获得本季度开始的日期  
        var quarterSeasonStartDate = new Date(currentYear, quarterSeasonStartMonth, 1);
        //获得本季度结束的日期  
        var quarterSeasonEndDate = new Date(currentYear, quarterSeasonEndMonth, this.getMonthDays(currentYear, quarterSeasonEndMonth));
        //加入数组返回  
        startStop.push(quarterSeasonStartDate);
        startStop.push(quarterSeasonEndDate);
        //返回  
        return startStop;
    };

    /***
    * 得到本年的起止日期
    * 
    */
    this.getCurrentYear = function () {
        //起止日期数组  
        var startStop = new Array();
        //获取当前时间  
        var currentDate = this.getCurrentDate();
        //获得当前年份4位年  
        var currentYear = currentDate.getFullYear();

        //本年第一天  
        var currentYearFirstDate = new Date(currentYear, 0, 1);
        //本年最后一天  
        var currentYearLastDate = new Date(currentYear, 11, 31);
        //添加至数组  
        startStop.push(currentYearFirstDate);
        startStop.push(currentYearLastDate);
        //返回  
        return startStop;
    };

    /**
    * 返回上一个月的第一天Date类型
    * @param year 年
    * @param month 月
    **/
    this.getPriorMonthFirstDay = function (year, month) {
        //年份为0代表,是本年的第一月,所以不能减  
        if (month == 0) {
            month = 11; //月份为上年的最后月份  
            year--; //年份减1  
            return new Date(year, month, 1);
        }
        //否则,只减去月份  
        month--;
        return new Date(year, month, 1); ;
    };

    /**
    * 获得上一月的起止日期
    * ***/
    this.getPreviousMonth = function () {
        //起止日期数组  
        var startStop = new Array();
        //获取当前时间  
        var currentDate = this.getCurrentDate();
        //获得当前月份0-11  
        var currentMonth = currentDate.getMonth();
        //获得当前年份4位年  
        var currentYear = currentDate.getFullYear();
        //获得上一个月的第一天  
        var priorMonthFirstDay = this.getPriorMonthFirstDay(currentYear, currentMonth);
        //获得上一月的最后一天  
        var priorMonthLastDay = new Date(priorMonthFirstDay.getFullYear(), priorMonthFirstDay.getMonth(), this.getMonthDays(priorMonthFirstDay.getFullYear(), priorMonthFirstDay.getMonth()));
        //添加至数组  
        startStop.push(priorMonthFirstDay);
        startStop.push(priorMonthLastDay);
        //返回  
        return startStop;
    };


    /**
    * 获得上一周的起止日期
    * **/
    this.getPreviousWeek = function () {
        //起止日期数组  
        var startStop = new Array();
        //获取当前时间  
        var currentDate = this.getCurrentDate();
        //返回date是一周中的某一天  
        var week = currentDate.getDay();
        //返回date是一个月中的某一天  
        var month = currentDate.getDate();
        //一天的毫秒数  
        var millisecond = 1000 * 60 * 60 * 24;
        //减去的天数  
        var minusDay = week != 0 ? week - 1 : 6;
        //获得当前周的第一天  
        var currentWeekDayOne = new Date(currentDate.getTime() - (millisecond * minusDay));
        //上周最后一天即本周开始的前一天  
        var priorWeekLastDay = new Date(currentWeekDayOne.getTime() - millisecond);
        //上周的第一天  
        var priorWeekFirstDay = new Date(priorWeekLastDay.getTime() - (millisecond * 6));

        //添加至数组  
        startStop.push(priorWeekFirstDay);
        startStop.push(priorWeekLastDay);

        return startStop;
    };

    /**
    * 得到上季度的起始日期
    * year 这个年应该是运算后得到的当前本季度的年份
    * month 这个应该是运算后得到的当前季度的开始月份
    * */
    this.getPriorSeasonFirstDay = function (year, month) {
        var quarterMonthStart = 0;
        var spring = 0; //春  
        var summer = 3; //夏  
        var fall = 6;   //秋  
        var winter = 9; //冬  
        //月份从0-11  
        switch (month) {//季度的其实月份  
            case spring:
                //如果是第一季度则应该到去年的冬季  
                year--;
                month = winter;
                break;
            case summer:
                month = spring;
                break;
            case fall:
                month = summer;
                break;
            case winter:
                month = fall;
                break;

        };

        return new Date(year, month, 1);
    };

    /**
    * 得到上季度的起止日期
    * **/
    this.getPreviousSeason = function () {
        //起止日期数组  
        var startStop = new Array();
        //获取当前时间  
        var currentDate = this.getCurrentDate();
        //获得当前月份0-11  
        var currentMonth = currentDate.getMonth();
        //获得当前年份4位年  
        var currentYear = currentDate.getFullYear();
        //上季度的第一天  
        var priorSeasonFirstDay = this.getPriorSeasonFirstDay(currentYear, currentMonth);
        //上季度的最后一天  
        var priorSeasonLastDay = new Date(priorSeasonFirstDay.getFullYear(), priorSeasonFirstDay.getMonth() + 2, this.getMonthDays(priorSeasonFirstDay.getFullYear(), priorSeasonFirstDay.getMonth() + 2));
        //添加至数组  
        startStop.push(priorSeasonFirstDay);
        startStop.push(priorSeasonLastDay);
        return startStop;
    };

    /**
    * 得到去年的起止日期
    * **/
    this.getPreviousYear = function () {
        //起止日期数组  
        var startStop = new Array();
        //获取当前时间  
        var currentDate = this.getCurrentDate();
        //获得当前年份4位年  
        var currentYear = currentDate.getFullYear();
        currentYear--;
        var priorYearFirstDay = new Date(currentYear, 0, 1);
        var priorYearLastDay = new Date(currentYear, 11, 1);
        //添加至数组  
        startStop.push(priorYearFirstDay);
        startStop.push(priorYearLastDay);
        return startStop;
    };

    return this;
})();