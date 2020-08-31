package com.thinkgem.jeesite.modules.access.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.CacheMapUtil;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.Users.service.UserInfoService;
import com.thinkgem.jeesite.modules.access.Service.AccessFunConfigService;
import com.thinkgem.jeesite.modules.access.Service.AccessFuncDateInfoService;
import com.thinkgem.jeesite.modules.access.Service.AccessFuncInfoService;
import com.thinkgem.jeesite.modules.access.Service.AccessIPService;
import com.thinkgem.jeesite.modules.access.dao.AccessDataDao;
import com.thinkgem.jeesite.modules.access.dao.AccessFuncInfoDao;
import com.thinkgem.jeesite.modules.access.entity.AccessFunConfig;
import com.thinkgem.jeesite.modules.data.service.CdrexService;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Controller
@RequestMapping("accessinfo")
public class AccessController extends BaseController {
	@Autowired
	private AccessFuncInfoService accessfuncinfoservice;
	@Autowired
	private AccessFuncDateInfoService accessfuncdatainfoservice;
	@Autowired
	private AccessFunConfigService accessFunConfigService;
	@Autowired
	private AccessIPService accessipservice;
	@Autowired
	private ComparasService comparasService;
	@Autowired
	private CdrexService cdrexService;
	@Autowired
	private AccessDataDao accessDataDao;
	@Autowired
	private AccessFuncInfoDao accessfuncinfodao;
	@Autowired
	private UserInfoService userInfoService;
	
	@RequestMapping(value ="/searchAccessFunConfig")
	@ResponseBody
	public List<Map<String, Object>> getAccessFunConfigList(@RequestParam(required=false) Long extId, Map<String, Object> paramMap,
			HttpServletRequest request, HttpServletResponse response){
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<AccessFunConfig> list=accessFunConfigService.findAll();
		for(int i = 0; i < list.size(); i++){
			AccessFunConfig accessFunConfig = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", accessFunConfig.getFuncItemId());
			map.put("pId", accessFunConfig.getParentFuncitemId());
			map.put("name", accessFunConfig.getFuncItemName());
			mapList.add(map);
		}
		Map<String, Object> rootmap = Maps.newHashMap();
		rootmap.put("id", "0");
		rootmap.put("pId", "00");
		rootmap.put("name", "顶级栏目");
		mapList.add(rootmap);
		return mapList;
	}
	@RequestMapping(value = "/getUserMessage")
	public void getUserMessage(HttpServletRequest request, HttpServletResponse response, Model model,String stime,
			String etime) {
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String accessip="";
		if(request.getHeader("x-forwarded-for")==null){
			accessip=request.getRemoteAddr();
		}else{
			accessip=request.getHeader("x-forwarded-for");
		}
		System.out.println(accessip+"accessip");
		Map map=new HashMap();
		String ips=comparasService.getComparasByKey("userMessage-ip").toString();
		String ip[]=ips.split(";");
		Boolean flag=false;
		for(int i=0;i<ip.length;i++){
			if(ip[i].equals(accessip)){
				flag=true;
			}
		}
		String errorMes="";
		int status=0;
		try {
			sdf2.parse(stime);
			sdf2.parse(etime);
		} catch (Exception e) {
			errorMes="日期格式有误！";
			status=1;
		}
		
		if(flag){
			map.put("status", 0);
		}else{
			errorMes="您当前的ip无法访问！";
			map.put("status", 1);
			status=1;
		}
		map.put("errorMes", errorMes);
		List<Map> list=userInfoService.getUserListByTime(stime, etime);
		if(status==0){
			map.put("result", list);
		}
		renderText(JsonMapper.toJsonString(map), response);
	}
	@RequestMapping(value = "/statisticIndex")
	public String AccessIndex(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String type=request.getParameter("type");
		String showType=request.getParameter("showType");
		List<AccessFunConfig> itemList = accessFunConfigService
				.findFirstItems();
		model.addAttribute("itemList", itemList);
		// List<String> list = new ArrayList<String>();
		int todayPVNum = 0;
		int todayIPNum = 0;
		String todayDownNum ;
		int yesterdayPVNum = 0;
		int yesterdayIPNum = 0;
		String  yesterdayDownNum ;
		int lastWeekPVNum = 0;
		int lastWeekIPNum = 0;
		String  lastWeekDownNum ;
		int lastMonthPVNum = 0;
		int lastMonthIPNum = 0;
		String  lastMonthDownNum ;
		int totalPVNum = 0;
		int totalIPNum = 0;
		String allDownNum;		
		
		//今天的pv
		todayPVNum=(Integer) CacheMapUtil.getCache("todayPVNum"+(showType==null?"":showType), 0);
		if(todayPVNum==0){
			todayPVNum = accessfuncinfoservice.getStatisticPVNum(1,showType);
			CacheMapUtil.putCache("todayPVNum"+(showType==null?"":showType), todayPVNum);
		}
		model.addAttribute("todayPVNum", todayPVNum);
		
		//今天的下载量
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		todayDownNum= (String) CacheMapUtil.getCache("todayDownNum"+(showType==null?"":showType),"0");
		if(todayDownNum.equals("0")){			
			if(showType!=null&&!"".equals(showType)){
				todayDownNum = accessipservice.downLoadSizeBytype( sdf.format(date),showType);
				CacheMapUtil.putCache("todayDownNum"+showType, todayDownNum,60);
			}else{
				todayDownNum = accessipservice.downLoadSize( sdf.format(date));
				CacheMapUtil.putCache("todayDownNum", todayDownNum);
			}
			
		}
		model.addAttribute("todayDownNum", todayDownNum);
		
		//今天的ip
		todayIPNum=(Integer) CacheMapUtil.getCache("todayIPNum"+(showType==null?"":showType), 0);
		if(todayIPNum==0){
			todayIPNum = accessipservice.count(0,showType);
			CacheMapUtil.putCache("todayIPNum"+(showType==null?"":showType), todayIPNum);
		}
		model.addAttribute("todayIPNum", todayIPNum);
		
		
	
		//昨天的pv
		yesterdayPVNum=(Integer) CacheMapUtil.getCache("yesterdayPVNum"+(showType==null?"":showType), 0);
		if(yesterdayPVNum==0){
			yesterdayPVNum = accessfuncinfoservice.getStatisticPVNum(2,showType);
			CacheMapUtil.putCache("yesterdayPVNum"+(showType==null?"":showType), yesterdayPVNum);
		}
		model.addAttribute("yesterdayPVNum", yesterdayPVNum);
		//昨天的下载
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		yesterdayDownNum= (String) CacheMapUtil.getCache("yesterdayDownNum"+(showType==null?"":showType),"0");
		if(yesterdayDownNum.equals("0")){
			if(showType!=null&&!"".equals(showType)){
				yesterdayDownNum = accessipservice.downLoadSizeByType1(sdf.format(calendar.getTime()),sdf.format(date),showType);
				CacheMapUtil.putCache("yesterdayDownNum"+showType, yesterdayDownNum,60);
			}else{
				yesterdayDownNum = accessipservice.downLoadSize(sdf.format(calendar.getTime()));
				CacheMapUtil.putCache("yesterdayDownNum", yesterdayDownNum);
			}
			
		}
		model.addAttribute("yesterdayDownNum", yesterdayDownNum);
		
		//昨天的ip
		// yesterdayIPNum = accessipservice.getStatisticIPNum(2);
		yesterdayIPNum=(Integer) CacheMapUtil.getCache("yesterdayIPNum"+(showType==null?"":showType), 0);
		if(yesterdayIPNum==0){
			yesterdayIPNum = accessipservice.count(-1,showType);
			CacheMapUtil.putCache("yesterdayIPNum"+(showType==null?"":showType), yesterdayIPNum);
		}
		model.addAttribute("yesterdayIPNum", yesterdayIPNum);
		

		//上周的pv
		lastWeekPVNum=(Integer) CacheMapUtil.getCache("lastWeekPVNum"+(showType==null?"":showType), 0);
		if(lastWeekPVNum==0){
			lastWeekPVNum = accessfuncinfoservice.getStatisticPVNum(3,showType);
			CacheMapUtil.putCache("lastWeekPVNum"+(showType==null?"":showType), lastWeekPVNum);
		}
		model.addAttribute("lastWeekPVNum", lastWeekPVNum);
		
		//一周的下载量
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		lastWeekDownNum= CacheMapUtil.getCache("lastWeekDownNum"+(showType==null?"":showType),"0").toString();
		if(lastWeekDownNum.equals("0")){
			if(showType!=null&&!"".equals(showType)){
				lastWeekDownNum = accessipservice.downLoadSizeByType1(sdf.format(calendar.getTime()),sdf.format(date),showType);
				CacheMapUtil.putCache("lastWeekDownNum"+showType, lastWeekDownNum,60);
			}else{
				lastWeekDownNum = accessipservice.downLoadSize(sdf.format(calendar.getTime()),sdf.format(date));
				CacheMapUtil.putCache("lastWeekDownNum", lastWeekDownNum);
			}
		}
		model.addAttribute("lastWeekDownNum", lastWeekDownNum);
		
		//上周的ip
		lastWeekIPNum=(Integer) CacheMapUtil.getCache("lastWeekIPNum"+(showType==null?"":showType), 0);
		if(lastWeekIPNum==0){
			lastWeekIPNum = accessipservice.count(-7,showType);
			CacheMapUtil.putCache("lastWeekIPNum"+(showType==null?"":showType), lastWeekIPNum);
		}
		model.addAttribute("lastWeekIPNum", lastWeekIPNum);

		//上个月的pv
		lastMonthPVNum=(Integer) CacheMapUtil.getCache("lastMonthPVNum"+(showType==null?"":showType), 0);
		if(lastMonthPVNum==0){
			lastMonthPVNum = accessfuncinfoservice.getStatisticPVNum(4,showType);
			CacheMapUtil.putCache("lastMonthPVNum"+(showType==null?"":showType), lastMonthPVNum);
		}
		model.addAttribute("lastMonthPVNum", lastMonthPVNum);
		
		//上个月的下载量
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		lastMonthDownNum= CacheMapUtil.getCache("lastMonthDownNum"+(showType==null?"":showType),"0").toString();
		if(lastMonthDownNum.equals("0")){
			if(showType!=null&&!"".equals(showType)){
				lastMonthDownNum = accessipservice.downLoadSizeByType1(sdf.format(calendar.getTime()),sdf.format(date),showType);
				CacheMapUtil.putCache("lastMonthDownNum"+showType, lastMonthDownNum,60);
			}else{
				lastMonthDownNum = accessipservice.downLoadSize(sdf.format(calendar.getTime()),sdf.format(date));
				CacheMapUtil.putCache("lastMonthDownNum", lastMonthDownNum);
			}
		}
		model.addAttribute("lastMonthDownNum", lastMonthDownNum);
		
		//上个月的ip
		lastMonthIPNum=(Integer) CacheMapUtil.getCache("lastMonthIPNum"+(showType==null?"":showType), 0);
		if(lastMonthIPNum==0){
			lastMonthIPNum = accessipservice.count(-30,showType);
			CacheMapUtil.putCache("lastMonthIPNum"+(showType==null?"":showType), lastMonthIPNum);
		}
		model.addAttribute("lastMonthIPNum", lastMonthIPNum);
	
		//总的pv
		totalPVNum=(Integer) CacheMapUtil.getCache("totalPVNum"+(showType==null?"":showType), 0);
		if(totalPVNum==0){
			totalPVNum = accessfuncinfoservice.getStatisticPVNum(5,showType);
			CacheMapUtil.putCache("totalPVNum"+(showType==null?"":showType), totalPVNum);
		}
		model.addAttribute("totalPVNum", totalPVNum);
		
		//总的下载量
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1000);
		allDownNum= CacheMapUtil.getCache("allDownNum"+(showType==null?"":showType),"0").toString();
		if(allDownNum.equals("0")){
			if(showType!=null&&!"".equals(showType)){
				allDownNum = accessipservice.downLoadSizeByType1(sdf.format(calendar.getTime()),sdf.format(date),showType);
				CacheMapUtil.putCache("allDownNum"+showType, allDownNum,60);
			}else{
				allDownNum = accessipservice.downLoadSize(sdf.format(calendar.getTime()),sdf.format(date));
				CacheMapUtil.putCache("allDownNum", allDownNum);
			}
		}
		model.addAttribute("allDownNum", allDownNum);
		
		//总的ip
		totalIPNum=(Integer) CacheMapUtil.getCache("totalIPNum"+(showType==null?"":showType), 0);
		if(totalIPNum==0){
			totalIPNum = accessipservice.count(1,showType);
			CacheMapUtil.putCache("totalIPNum"+(showType==null?"":showType), totalIPNum);
		}
		model.addAttribute("totalIPNum", totalIPNum);
		model.addAttribute("type", showType);
		if("center".equals(showType)){
			return "modules/access/accessIndexNation";
		}else if("province".equals(showType)){
			return "modules/access/accessIndexProvice";
		}else{
			return "modules/access/accessIndex";
		}
		
	}

	/*
	 * 折线图lineGraph 条形图barChart 柱状图histogram 圆饼图pieChart
	 */

	@RequestMapping(value = "/lineGraph")
	public void lineGraph(HttpServletRequest request,
			HttpServletResponse response) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(cal.getTime());
		String str = request.getParameter("type");
		String showType=request.getParameter("showType");
		Integer type = Integer.valueOf(str == null ? "1" : str);
		int interval = 0;
		switch (type) {
		case 1:
			List<Map<String, ?>> list1 = findLineByDate(date, type,showType);
			renderText(JsonMapper.toJsonString(list1), response);
			break;
		case 2:
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String date2 = sdf.format(cal.getTime());
			List<Map<String, ?>> list2 = findLineByDate(date2, type,showType);
			renderText(JsonMapper.toJsonString(list2), response);
			break;
		case 3:
			cal.add(Calendar.DAY_OF_MONTH, -7);
			String date3 = sdf.format(cal.getTime());
			interval = 7;
			List<?> list3 = accessfuncinfoservice.findBetweenDate(date3, date,showType);
			renderText(JsonMapper.toJsonString(list3), response);
			break;
		case 4:
			cal.add(Calendar.DAY_OF_MONTH, -30);
			String date4 = sdf.format(cal.getTime());
			interval = 30;
			List<?> list4 = accessfuncinfoservice.findBetweenDate(date4, date,showType);
			renderText(JsonMapper.toJsonString(list4), response);
			break;
		case 5:
			cal.add(Calendar.YEAR, -1);
			String date5 = sdf.format(cal.getTime());
			interval = 365;
			List<?> list5 = accessfuncinfoservice.findYearBetweenDate(date5, date,showType);
			renderText(JsonMapper.toJsonString(list5), response);
			break;	
		default:
			break;
		}
	}

	/*
	 * 将栏目id，变为栏目名称
	 */

	private Map translateName(Map<String, Integer> dataMap) {

		List<AccessFunConfig> listitems = this.accessFunConfigService
				.findFirstItems();
		Map<String, Integer> reMap = new HashMap<String, Integer>();
		/*
		 * for(AccessFunConfig ac :listitems){
		 * if(dataMap.containsKey(ac.getFuncItemId())){ Map<String,Integer>
		 * realMap = new HashMap();
		 * realMap.put(ac.getFuncItemName(),dataMap.get(ac.getFuncItemId()));
		 * reMap.put(ac.getFuncItemId(),realMap); } }
		 */

		/*
		 * for(String itemid:dataMap.keySet()){ boolean flag = true;
		 * for(AccessFunConfig ac :listitems){
		 * if(ac.getFuncItemId().equals(itemid)){
		 * reMap.put(ac.getFuncItemName(),dataMap.get(itemid)); flag = false;
		 * break; } } if(flag){ reMap.put(itemid,dataMap.get(itemid)); } }
		 */
		for (AccessFunConfig ac : listitems) {
			boolean flag = true;
			if(dataMap!=null){
				for (String itemid : dataMap.keySet()) {
					if (ac.getFuncItemId().equals(itemid)) {
						reMap.put(ac.getFuncItemName(), dataMap.get(itemid));
						flag = false;
						break;
					}
				}
			}
			if (flag == true) {
				reMap.put(ac.getFuncItemName(), 0);
			}
		}
		return reMap;
	}

	/**
	 * 
	 * @param dataMap
	 *            已经按栏目Id划分好的访问数据
	 * @param menuId
	 * @return
	 */
	private Map translateName2(Map<String, Integer> dataMap, String menuId) {
		List<AccessFunConfig> listitems = this.accessFunConfigService
				.findSubItems(menuId); // 得到子栏目Id及子栏目名字
		if(listitems.size()==0){
			listitems = this.accessFunConfigService
					.findItems(menuId); // 得到栏目Id及子栏目名字
			
		}
		Map<String, Integer> reMap = new HashMap<String, Integer>();
		/*
		 * for(AccessFunConfig ac :listitems){
		 * if(dataMap.containsKey(ac.getFuncItemId())){ Map<String,Integer>
		 * realMap = new HashMap();
		 * realMap.put(ac.getFuncItemName(),dataMap.get(ac.getFuncItemId()));
		 * reMap.put(ac.getFuncItemId(),realMap); } }
		 */

		/*
		 * for(String itemid:dataMap.keySet()){ boolean flag = true;
		 * for(AccessFunConfig ac :listitems){
		 * if(ac.getFuncItemId().equals(itemid)){
		 * reMap.put(ac.getFuncItemName(),dataMap.get(itemid)); flag = false;
		 * break; } } }
		 */

		for (AccessFunConfig ac : listitems) {
			boolean flag = true;
			if(dataMap!=null){
				for (String itemid : dataMap.keySet()) {
					if (ac.getFuncItemId().equals(itemid)) {
						reMap.put(ac.getFuncItemName(), dataMap.get(itemid));
						flag = false;
						break;
					}
				}
			}
			if (flag) {
				reMap.put(ac.getFuncItemName(), 0);
			}
		}
		return reMap;
	}

	private List<Entry<String, Integer>> sortData(Map<String, Integer> map) {
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1,
                               Entry<String, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});
		return list;
	}

	private List<Entry<String, Integer>> sortDataByMonth(Map<String, Integer> map) {
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1,
                               Entry<String, Integer> o2) {
				return (Integer.parseInt(o1.getKey() )- Integer.parseInt(o2.getKey() ));
			}
		});
		return list;
	}

	private List convertToListForJson(List<Entry<String, Integer>> list,
			String itemname, String valname) {
		List relist = new ArrayList<Map<String, ?>>();
		Map<String, String[]> name = new HashMap<String, String[]>();
		Map<String, String[]> value = new HashMap<String, String[]>();
		String[] nameitems = new String[list.size()];
		Integer[] valueitems = new Integer[list.size()];

		for (int i = 0; i < list.size(); i++) {
			Entry<String, Integer> mapping = list.get(i);
			nameitems[i] = mapping.getKey();
			valueitems[i] = mapping.getValue();
		}
		Map<String, String[]> map1 = new HashMap<String, String[]>();
		map1.put(itemname, nameitems);
		relist.add(map1);
		Map<String, Integer[]> map2 = new HashMap<String, Integer[]>();
		map2.put(valname, valueitems);
		relist.add(map2);
		return relist;
	}

	@RequestMapping(value = "/barChart/PVNum")
	public void barChartPVNum(HttpServletRequest request,
			HttpServletResponse response) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(cal.getTime());
		String today = date;
		String menuId = request.getParameter("menuId"); // 得到栏目Id
		String str = request.getParameter("type"); // 判断统计的类型，type=1(今日),type=2(昨日),type=3(过去一周),type=4(过去一月)
		String showType=request.getParameter("showType");
		Integer type = Integer.valueOf(str == null ? "1" : str);
		
		Map<String, Integer> dataMap = new HashMap<String, Integer>();
		if (menuId == "") {
			logger.info("-----------type--------"+type);
			logger.info("-----------showType--------"+showType);
			logger.info("-----------menuId--------"+menuId);
			switch (type) {
			case 1:
				dataMap = this.accessfuncdatainfoservice.findByDatePVNum(date,null,showType);
				break;
			case 2:
				cal.add(Calendar.DAY_OF_MONTH, -1);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice.findByDatePVNum(date,null,showType);
				break;
			case 3:
				cal.add(Calendar.DAY_OF_MONTH, -7);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice.findByDatePVNum(date,
						today,showType);
				break;
			case 4:
				cal.add(Calendar.DAY_OF_MONTH, -30);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice.findByDatePVNum(date,
						today,showType);
				break;
			case 5:
				cal.add(Calendar.YEAR, -1);
				date = sdf.format(cal.getTime());
//				dataMap = this.accessfuncdatainfoservice.findByDatePVNum(date,
//						today,showType);
				dataMap = accessfuncinfoservice.findYearBetweenAll(date, today,showType,menuId);
				break;	
			default:
				break;
			}
		} else {
			switch (type) {
			case 1:
				dataMap = this.accessfuncdatainfoservice
						.findByDatePVNumSub(date,null,showType,menuId);
				break;
			case 2:
				cal.add(Calendar.DAY_OF_MONTH, -1);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice
						.findByDatePVNumSub(date,null,showType,menuId);
				break;
			case 3:
				cal.add(Calendar.DAY_OF_MONTH, -7);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice.findByDatePVNumSub(
						date, today,showType,menuId);
				break;
			case 4:
				cal.add(Calendar.DAY_OF_MONTH, -30);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice.findByDatePVNumSub(
						date, today,showType,menuId);
				break;
			case 5:
				cal.add(Calendar.YEAR, -1);
				date = sdf.format(cal.getTime());
//				dataMap = this.accessfuncdatainfoservice.findByDatePVNum(date,
//						today,showType);
				dataMap = accessfuncinfoservice.findYearBetweenAll(date, today,showType,menuId);
				break;
			default:
				break;
			}
		}
		Map<String, Integer> reMap;
		if (menuId == ""&&type!=5) {
			reMap = translateName(dataMap); // 将itemId(栏目Id)转换成itemName(栏目名字),便于前端显示
		} else if(menuId!=""&&type!=5) {
			reMap = translateName2(dataMap, menuId); // 将subItemId(子栏目Id)转换成subItemName(子栏目名字),便于前端显示
		}else if(type==5){
			reMap=dataMap;
		}else {
			reMap=dataMap;
		}
		if(type==5){
			List<Entry<String, Integer>> sortList = this.sortDataByMonth(dataMap); // 按月份排序
			renderText(JsonMapper.toJsonString(convertToListForJson(sortList,
					"items", "pvnum")), response);
		}else{
			List<Entry<String, Integer>> sortList = this.sortData(reMap); // 对各个栏目按照PVNum递减进行排序
			renderText(JsonMapper.toJsonString(convertToListForJson(sortList,
					"items", "pvnum")), response);
		}
		
	}
	
	
	
	
	@RequestMapping(value = "/barChart/IPNum")
	public void barChartIPNum(HttpServletRequest request,
			HttpServletResponse response) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(cal.getTime());
		String today = date;
		String menuId = request.getParameter("menuId");
		String str = request.getParameter("type");
		String showType=request.getParameter("showType");
		Integer type = Integer.valueOf(str == null ? "1" : str);
		Map<String, Integer> dataMap = new HashMap();
		if (type == 5) {
			cal.add(Calendar.YEAR, -1);
			date = sdf.format(cal.getTime());
			dataMap = accessfuncinfoservice.findYearBetweenAllIp(date, today,showType,menuId);
		
		}else{
			int day = 0;
			if (type == 2) {
				day = -1;
			}
			if (type == 3) {
				day = -7;
			}
			if (type == 4) {
				day = -30;
			}
			int[] days=new int[]{0,-1,-7,-30};
			for(int d=0;d<days.length;d++){
				if(day==days[d]){
					Map<String, Map<String, Integer>> IPNumMap=(Map<String, Map<String, Integer>>) CacheMapUtil.getCache("IPcountNum"+day, null);
					//IPNumMap=null;
					if(IPNumMap!=null){
						dataMap= IPNumMap.get("IP"+day+menuId+(showType==null?"":showType));
					}
					if(IPNumMap==null || dataMap==null){
					      dataMap = accessipservice.countIPNum(day, menuId,showType);
					      if(IPNumMap==null){ 
					        IPNumMap=new HashMap<String, Map<String,Integer>>();
					      }
						  IPNumMap.put("IP"+day+menuId+(showType==null?"":showType), dataMap);
						  CacheMapUtil.putCache("IPcountNum"+day, IPNumMap);
					  
					}else{
						dataMap=IPNumMap.get("IP"+day+menuId+(showType==null?"":showType));
					}
				}
			}
		}
		
		
		Map<String, Integer> reMap;
		if (menuId == ""&&type!=5) {
			reMap = translateName(dataMap); // 将itemId(栏目Id)转换成itemName(栏目名字),便于前端显示
		} else if(menuId!=""&&type!=5) {
			reMap = translateName2(dataMap, menuId); // 将subItemId(子栏目Id)转换成subItemName(子栏目名字),便于前端显示
		}else if(type==5){
			reMap=dataMap;
		}else{
			reMap=dataMap;
		}
		if(type==5){
			List<Entry<String, Integer>> sortList = this.sortDataByMonth(dataMap); // 按月份排序
			renderText(JsonMapper.toJsonString(convertToListForJson(sortList,
					"items", "ipnum")), response);
		}else{
			List<Entry<String, Integer>> sortList = this.sortData(reMap); // 对各个栏目按照PVNum递减进行排序
			renderText(JsonMapper.toJsonString(convertToListForJson(sortList,
					"items", "ipnum")), response);
		}
	}

	@RequestMapping(value = "/barChart/StayTime")
	public void barChartStayTime(HttpServletRequest request,
			HttpServletResponse response) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(cal.getTime());
		String today = date;
		String menuId = request.getParameter("menuId");
		String str = request.getParameter("type");
		String showType=request.getParameter("showType");
		Integer type = Integer.valueOf(str == null ? "1" : str);
		Map<String, Integer> dataMap = new HashMap();
		if (menuId == "") {
			switch (type) {
			case 1:
				dataMap = this.accessfuncdatainfoservice
						.findByDateStayTime(date,null,showType);
				break;
			case 2:
				cal.add(Calendar.DAY_OF_MONTH, -1);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice
						.findByDateStayTime(date,null,showType);
				break;
			case 3:
				cal.add(Calendar.DAY_OF_MONTH, -7);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice.findByDateStayTime(
						date, today,showType);
				break;
			case 4:
				cal.add(Calendar.DAY_OF_MONTH, -30);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice.findByDateStayTime(
						date, today,showType);
				break;
			case 5:
				cal.add(Calendar.YEAR, -1);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice.findByDateStayTime(
						date, today,showType);
				break;	
			default:
				break;
			}
		} else {
			switch (type) {
			case 1:
				dataMap = this.accessfuncdatainfoservice
						.findByDateStayTimeSub(date,null,showType,menuId);
				break;
			case 2:
				cal.add(Calendar.DAY_OF_MONTH, -1);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice
						.findByDateStayTimeSub(date,null,showType,menuId);
				break;
			case 3:
				cal.add(Calendar.DAY_OF_MONTH, -7);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice.findByDateStayTimeSub(
						date, today,showType,menuId);
				break;
			case 4:
				cal.add(Calendar.DAY_OF_MONTH, -30);
				date = sdf.format(cal.getTime());
				dataMap = this.accessfuncdatainfoservice.findByDateStayTimeSub(
						date, today,showType,menuId);
				break;
			default:
				break;
			}
		}
		Map<String, Integer> reMap;
		if (menuId == "") {
			reMap = translateName(dataMap);
		} else {
			reMap = translateName2(dataMap, menuId);
		}
		List<Entry<String, Integer>> sortList = this.sortData(reMap);
		renderText(JsonMapper.toJsonString(convertToListForJson(sortList,
				"items", "staytime")), response);
	}

	@RequestMapping(value = "/pieChart")
	public void pieChart(HttpServletRequest request,
			HttpServletResponse response) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(cal.getTime());
		String str = request.getParameter("type");
		String showType=request.getParameter("showType");
		Integer type = Integer.valueOf(str == null ? "1" : str);
		switch (type) {
		case 1:
			String json1 = accessipservice.findDataByDate(date,showType);
			renderText(json1, response);
			break;
		case 2:
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String date2 = sdf.format(cal.getTime());
			String json2 = accessipservice.findDataByDate(date2,showType);
			renderText(json2, response);
			break;
		case 3:
			cal.add(Calendar.DAY_OF_MONTH, -7);
			String date3 = sdf.format(cal.getTime());
			String json3 = accessipservice.findDataByDate(date3, date,showType);
			renderText(json3, response);
			break;
		case 4:
			cal.add(Calendar.DAY_OF_MONTH, -30);
			String date4 = sdf.format(cal.getTime());
			String json4 = accessipservice.findDataByDate(date4, date,showType);
			renderText(json4, response);
			break;
		case 5:
			cal.add(Calendar.YEAR, -1);
			String date5 = sdf.format(cal.getTime());
			String json5 = accessipservice.findDataByDate(date5, date,showType);
			renderText(json5, response);
			break;	
		default:
			break;
		}

	}
	@RequestMapping(value = "/piePvChart")
	public void piePvChart(HttpServletRequest request,
			HttpServletResponse response) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(cal.getTime());
		String str = request.getParameter("type");
		String showType=request.getParameter("showType");
		Integer type = Integer.valueOf(str == null ? "1" : str);
		switch (type) {
		case 1:
			String json1 = accessipservice.findPvDataByDate(date,showType);
			renderText(json1, response);
			break;
		case 2:
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String date2 = sdf.format(cal.getTime());
			String json2 = accessipservice.findPvDataByDate(date2,showType);
			renderText(json2, response);
			break;
		case 3:
			cal.add(Calendar.DAY_OF_MONTH, -7);
			String date3 = sdf.format(cal.getTime());
			String json3 = accessipservice.findPvDataByDate(date3, date,showType);
			renderText(json3, response);
			break;
		case 4:
			cal.add(Calendar.DAY_OF_MONTH, -30);
			String date4 = sdf.format(cal.getTime());
			String json4 = accessipservice.findPvDataByDate(date4, date,showType);
			renderText(json4, response);
			break;
		case 5:
			cal.add(Calendar.YEAR, -1);
			String date5 = sdf.format(cal.getTime());
			String json5 = accessipservice.findPvDataByDate(date5, date,showType);
			renderText(json5, response);
			break;	
		default:
			break;
		}

	}

	@RequestMapping(value = "/mapDistribute")
	public void mapChart(HttpServletRequest request,
			HttpServletResponse response) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(cal.getTime());
		String str = request.getParameter("type");
		String showType=request.getParameter("showType");
		Integer type = Integer.valueOf(str == null ? "1" : str);
		switch (type) {
		case 1:
			String json1 = accessipservice.findDataByDateMap(date,showType,0);
			renderText(json1, response);
			break;
		case 2:
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String date2 = sdf.format(cal.getTime());
			String json2 = accessipservice.findDataByDateMap(date2,showType,-1);
			renderText(json2, response);
			break;
		case 3:
			cal.add(Calendar.DAY_OF_MONTH, -7);
			String date3 = sdf.format(cal.getTime());
			String json3 = accessipservice.findDataByDateMap(date3, date,showType,-7);
			renderText(json3, response);
			break;
		case 4:
			cal.add(Calendar.DAY_OF_MONTH, -30);
			String date4 = sdf.format(cal.getTime());
			String json4 = accessipservice.findDataByDateMap(date4, date,showType,-30);
			renderText(json4, response);
			break;
		case 5:
			cal.add(Calendar.YEAR, -1);
			String date5 = sdf.format(cal.getTime());
			//String json5 = accessipservice.findDataByDateMap(date5, date,showType);
			//System.out.println("aa"+json5);
			String json5="{\"data\": [{\"name\":\"湖南\",\"value\":2169},{\"name\":\"四川\",\"value\":950},{\"name\":\"浙江\",\"value\":1043},{\"name\":\"内蒙古\",\"value\":785},{\"name\":\"云南\",\"value\":1775},{\"name\":\"福建\",\"value\":404},{\"name\":\"山西\",\"value\":2311},{\"name\":\"陕西\",\"value\":1750},{\"name\":\"贵州\",\"value\":288},{\"name\":\"山东\",\"value\":1725},{\"name\":\"湖北\",\"value\":1916},{\"name\":\"广东\",\"value\":1335},{\"name\":\"青海\",\"value\":576},{\"name\":\"宁夏\",\"value\":440},{\"name\":\"辽宁\",\"value\":440},{\"name\":\"江西\",\"value\":2489},{\"name\":\"上海\",\"value\":576},{\"name\":\"江苏\",\"value\":1043},{\"name\":\"甘肃\",\"value\":167},{\"name\":\"河北\",\"value\":1438},{\"name\":\"安徽\",\"value\":177},{\"name\":\"河南\",\"value\":2177},{\"name\":\"新疆\",\"value\":225},{\"name\":\"重庆\",\"value\":183},{\"name\":\"天津\",\"value\":149},{\"name\":\"广西\",\"value\":2335},{\"name\":\"黑龙江\",\"value\":1043},{\"name\":\"吉林\",\"value\":128},{\"name\":\"西藏\",\"value\":1750},{\"name\":\"海南\",\"value\":210},{\"name\":\"北京\",\"value\":1179}]}";
			renderText(json5, response);
			break;	
		default:
			break;
		}
	}
	
	@RequestMapping(value = "/mapPvDistribute")
	public void mapPvChart(HttpServletRequest request,
			HttpServletResponse response) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(cal.getTime());
		String str = request.getParameter("type");
		String showType=request.getParameter("showType");
		Integer type = Integer.valueOf(str == null ? "1" : str);
		switch (type) {
		case 1:
			String json1 = accessipservice.findPvDataByDateMap(date,showType);
			renderText(json1, response);
			break;
		case 2:
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String date2 = sdf.format(cal.getTime());
			String json2 = accessipservice.findPvDataByDateMap(date2,showType);
			renderText(json2, response);
			break;
		case 3:
			cal.add(Calendar.DAY_OF_MONTH, -7);
			String date3 = sdf.format(cal.getTime());
			String json3 = accessipservice.findPvDataByDateMap(date3, date,showType);
			renderText(json3, response);
			break;
		case 4:
			cal.add(Calendar.DAY_OF_MONTH, -30);
			String date4 = sdf.format(cal.getTime());
			String json4 = accessipservice.findPvDataByDateMap(date4, date,showType);
			renderText(json4, response);
			break;
		case 5:
			cal.add(Calendar.YEAR, -1);
			String date5 = sdf.format(cal.getTime());
			String json5 = accessipservice.findPvDataByDateMap(date5, date,showType);
			renderText(json5, response);
			break;	
		default:
			break;
		}
	}

	@RequestMapping(value = "/topip")
	public void topIP(HttpServletResponse response) {
		Object topIP = this.accessfuncinfoservice.getIPTop();
		renderText(topIP.toString(), response);
	}

	@RequestMapping(value = "/topaddress")
	public void topaddress(HttpServletRequest request,
			HttpServletResponse response) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(cal.getTime());
		String str = request.getParameter("type");
		String showType=request.getParameter("showType");
		Integer type = Integer.valueOf(str == null ? "1" : str);
		switch (type) {
		case 1:
			List<Map<String, Object>> listres = this.accessipservice
					.topAddress(date,showType);
			renderText(JsonMapper.toJsonString(listres), response);
			break;
		case 2:
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String date2 = sdf.format(cal.getTime());
			List<Map<String, Object>> listres2 = this.accessipservice
					.topAddress(date2,showType);
			renderText(JsonMapper.toJsonString(listres2), response);
			break;
		case 3:
			cal.add(Calendar.DAY_OF_MONTH, -7);
			String date3 = sdf.format(cal.getTime());
			List<Map<String, Object>> listres3 = this.accessipservice
					.topAddress(date3, date,showType);
			renderText(JsonMapper.toJsonString(listres3), response);
			break;
		case 4:
			cal.add(Calendar.DAY_OF_MONTH, -30);
			String date4 = sdf.format(cal.getTime());
			List<Map<String, Object>> listres4 = this.accessipservice
					.topAddress(date4, date,showType);
			renderText(JsonMapper.toJsonString(listres4), response);
			break;
		default:
			break;
		}
	}

	private List<Map<String, ?>> findLineByDate(String date, int type,String showType) {
		Map<String, int[]> map = accessfuncinfoservice.findByDate(date, type,showType); // 存放一天访问的ipCount,
																				// pvCount,
																				// stayTime的统计数据
		Map<String, String[]> map1 = new HashMap<String, String[]>();
		if (type == 1) {
			String hourStr =this.accessfuncinfodao.findMaxDate(date);
			int hour = Integer.valueOf(hourStr==null?"0":hourStr) + 1;
			String[] str = new String[hour];
			for (int i = 0; i < str.length; i++) {
				if (i < 10) {
					str[i] = "0" + i;
				} else {
					str[i] = i + "";
				}
			}
			map1.put("str", str); // 存放小时的字符串
		} else {
			String[] str = new String[24];
			for (int i = 0; i < str.length; i++) {
				if (i < 10) {
					str[i] = "0" + i;
				} else {
					str[i] = i + "";
				}
			}
			map1.put("str", str); // 存放一天24小时的字符串
		}

		List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
		list.add(map); // 将一天的访问情况加入list中
		list.add(map1); // 将一天24小时的字符串加入list中
		return list;
	}

	@RequestMapping(value = "/getIPPVload")
	public void getIPPVload(String dateStr, HttpServletRequest request, HttpServletResponse response, Model model) {
		String showType=request.getParameter("showType");
;		int IPnum = accessipservice.count(dateStr.trim(),showType);
		String loadnum = accessipservice.downLoadSize(dateStr.trim());
		int PVnum = accessfuncinfoservice.getPVNum(dateStr.trim());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		try {
			dateStr = sdf1.format(sdf.parse(dateStr));
		} catch (Exception e) {
			e.printStackTrace();
		}
		int phoneNum = cdrexService.getphoneNum(dateStr.trim());
		String json = "{\"IPnum\":\"" + IPnum + "\",\"PVnum\":\"" + PVnum + "\",\"loadnum\":\"" + loadnum
				+ "\",\"phoneNum\":\"" + phoneNum + "\"}";
		renderText(json, response);
	}
	

	@RequestMapping(value = "/getIPPVloadByRange")
	public void getIPPVloadByRange(String sdateStr,String edateStr, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		int IPnum = accessipservice.countByRange(sdateStr.trim(),edateStr.trim());
		String loadnum = accessipservice.downLoadSizeByDateRange(sdateStr.trim(),edateStr.trim());
		int PVnum = accessfuncinfoservice.getPVNumByRange(sdateStr.trim(),edateStr.trim());
		String json = "{\"IPnum\":\"" + IPnum + "\",\"PVnum\":\"" + PVnum + "\",\"loadnum\":\"" + loadnum + "\"}";
		renderText(json, response);
	}
	
	
	@RequestMapping(value = "/getHourIPPVloadByRange")
	public void getHourIPPVloadByRange(String sdateStr,String edateStr, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		List<Object[]>  list = accessipservice.getHourIPPVloadByRange(sdateStr, edateStr);
		List<Map> reList = new ArrayList();
		for (Object[] objects : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("accessdate", objects[0]);
				map.put("accesstime", objects[1]);
				map.put("pvnumber", objects[2]);
				map.put("ipnum", objects[3]);
				reList.add(map);
		}
		renderText(JsonMapper.toJsonString(reList), response);
	}
	
}
