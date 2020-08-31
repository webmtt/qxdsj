package com.thinkgem.jeesite.modules.access.Service;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.CacheMapUtil;
import com.thinkgem.jeesite.modules.access.dao.AccessIPDao;
import com.thinkgem.jeesite.modules.access.dao.PortalIPInforDao;
import com.thinkgem.jeesite.modules.access.entity.AccessFunConfig;
import com.thinkgem.jeesite.modules.access.entity.AccessIP;
import com.thinkgem.jeesite.modules.access.entity.PortalIPInfor;
import com.thinkgem.jeesite.modules.recordquery.dao.FtpUrlDataDao;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.monoid.json.JSONObject;
import us.monoid.web.Content;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AccessIPService extends BaseService {
	@Autowired
	private AccessIPDao accessipdao;
	@Autowired
	private AccessFuncInfoService accessfuncinfoservice;
	@Autowired
	private AccessFuncDateInfoService accessfuncdatainfoservice;
	@Autowired
	private AccessFunConfigService accessFunConfigService;
	@Autowired
	private ComparasService comparasService;
	@Autowired
	private FtpUrlDataDao ftpUrlDataDao;
	@Autowired
	private PortalIPInforDao portalIPInforDao;
	public List<AccessIP> findByDate(String date) {
		return this.accessipdao.findByDate(date);
	}
	
	public String findDataByDate(String date,String showType) {
		List<Object[]> list = this.accessipdao.findDataByDate(date,showType);
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		int total = 0;
		int top = 1;
		int others = 0;
		for (Object[] objects : list) {
			if(top <= 5){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("proname", objects[0]);
				map.put("ipnum", objects[1]);
				list1.add(map);
			}else{
				others += Integer.valueOf(objects[1] + "");
			}
			total += Integer.valueOf(objects[1] + "");
			top++;
		}
		if(others!=0){
			Map<String, Object> mapOther = new HashMap<String, Object>();
			mapOther.put("proname", "其他");
			mapOther.put("ipnum", others);
			list1.add(mapOther);
		}
		Map<String, Object> maptemp = new HashMap<String, Object>();
		for (Map<String, Object> map : list1) {
			String sum = map.get("ipnum") + "";
			String key = (String) map.get("proname");
			String value1 = String.valueOf((maptemp.get(key) == null ? 0 : maptemp.get(key)));
			int value2 = Integer.valueOf(sum) + Integer.valueOf(value1);
			maptemp.put(key, value2);
		}
		JSONArray ja_result = new JSONArray();
		Set<String> set = maptemp.keySet();
		int i = 0;
		for (String string : set) {
			JSONArray ja_val = new JSONArray();
			float p =0f;
		    if(total==0){
				p=0;
			}else{
			    p = ((float) Integer.valueOf(maptemp.get(string) + "") / (float) total) * 100;
			}
			ja_val.add(0, string);
			ja_val.add(1, p==0?"-":p);
			ja_result.add(i, ja_val);
			i++;
		}

		return ja_result.toString();
	}
	public String findPvDataByDate(String date,String showType) {
		List<Object[]> list = this.accessipdao.findPvDataByDate(date,showType);
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		int total = 0;
		int top = 1;
		int others = 0;
		for (Object[] objects : list) {
			if(top <= 5){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("proname", objects[0]);
				map.put("ipnum", objects[1]);
				list1.add(map);
			}else{
				others += Integer.valueOf(objects[1] + "");
			}
			total += Integer.valueOf(objects[1] + "");
			top++;
		}
		if(others!=0){
			Map<String, Object> mapOther = new HashMap<String, Object>();
			mapOther.put("proname", "其他");
			mapOther.put("ipnum", others);
			list1.add(mapOther);
		}
		
		Map<String, Object> maptemp = new HashMap<String, Object>();
		for (Map<String, Object> map : list1) {
			String sum = map.get("ipnum") + "";
			String key = (String) map.get("proname");
			String value1 = String.valueOf((maptemp.get(key) == null ? 0 : maptemp.get(key)));
			int value2 = Integer.valueOf(sum) + Integer.valueOf(value1);
			maptemp.put(key, value2);
		}
		JSONArray ja_result = new JSONArray();
		Set<String> set = maptemp.keySet();
		int i = 0;
		for (String string : set) {
			JSONArray ja_val = new JSONArray();
			float p =0f;
		    if(total==0){
				p=0;
			}else{
			    p = ((float) Integer.valueOf(maptemp.get(string) + "") / (float) total) * 100;
			}
			ja_val.add(0, string);
			ja_val.add(1, p==0?"-":p);
			ja_result.add(i, ja_val);
			i++;
		}

		return ja_result.toString();
	}
	public String findPvDataByDate(String date1, String date2,String showType) {
		List<Object[]> list = this.accessipdao.findPvDataByDate(date1, date2,showType);
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		int total = 0;
		int top = 1;
		int others = 0;
		for (Object[] objects : list) {
			if(top <= 5){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("proname", objects[0]);
				map.put("ipnum", objects[1]);
				list1.add(map);
			}else{
				others += Integer.valueOf(objects[1] + "");
			}
			total += Integer.valueOf(objects[1] + "");
			top++;
		}
		if(others!=0){
			Map<String, Object> mapOther = new HashMap<String, Object>();
			mapOther.put("proname", "其他");
			mapOther.put("ipnum", others);
			list1.add(mapOther);
		}
		
		Map<String, Object> maptemp = new HashMap<String, Object>();
		for (Map<String, Object> map : list1) {
			String sum = map.get("ipnum") + "";
			String key = (String) map.get("proname");
			String value1 = String.valueOf((maptemp.get(key) == null ? 0 : maptemp.get(key)));
			int value2 = Integer.valueOf(sum) + Integer.valueOf(value1);
			maptemp.put(key, value2);
		}
		JSONArray ja_result = new JSONArray();
		Set<String> set = maptemp.keySet();
		int i = 0;
		for (String string : set) {
			JSONArray ja_val = new JSONArray();
			float p;
			if(total==0){
				p=0;
			}else{
			    p = ((float) Integer.valueOf(maptemp.get(string) + "") / (float) total) * 100;
			}
			ja_val.add(0, string);
			ja_val.add(1, p==0?"-":p);
			ja_result.add(i, ja_val);
			i++;
		}
		return ja_result.toString();
	}
	  //今日，昨日，一周，一月各个省份ip
	  @Transactional
	    public int countSource(int day,String source) {
		  int ip =0;
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			long endTime=0;
			long benginTime=0;
			if(day==0){
				try {
					benginTime = sdf.parse(sdf.format(date)).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				endTime = date.getTime();
			}else{
				try {
					endTime=sdf.parse(sdf.format(date)).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar calendar = Calendar.getInstance();
				try {
					calendar.setTime(sdf.parse(sdf.format(date)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				calendar.add(Calendar.DATE,day);
				benginTime=calendar.getTimeInMillis();
			}
			List<PortalIPInfor> list =portalIPInforDao.findByProName(source);
			if(list.size()>0){
				source=list.get(0).getEnname();
			}
			Comparas comparas = comparasService.findComparas("accessipNum");
			String queryUrl =comparas.getStringvalue();
			/*String queryData="{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"filtered\": {\"query\": {"
			        +"\"match\": {\"source\": \"SOURCE\"}},\"filter\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {"
	                  +"\"gte\": minTime,\"lte\": maxTime}}}]}}}}}";*/
			String queryData="{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"bool\": {\"filter\": {\"match\" : { \"source\" : \"SOURCE\" }},\"must\": {\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}}}}";
			queryData=queryData.replace("minTime", String.valueOf(benginTime));
			queryData=queryData.replace("maxTime", String.valueOf(endTime));
			queryData=queryData.replace("SOURCE", source);
			System.out.println("query---"+queryData);
			try {
				Resty r = new Resty();
				//Content content = new Content("", queryData.getBytes());
				Content content = Resty.content(queryData);
				JSONResource data = r.json(queryUrl, content);
				JSONObject jsonstr = (JSONObject)data.get("aggregations");
				JSONObject jsonstr1 = (JSONObject)jsonstr.get("1");
				ip =(Integer)jsonstr1.get("value");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ip;
	  }
	public String findDataByDateMap(String date,String showType,int day) {
		List<Object[]> list = this.accessipdao.findDataByDate(date,showType);
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		String[] mergeArea = new String[]{"气象报社", "科技大楼（气象/气候）", "华云", "气科院", "气象学会", "气象出版社",
				"公服中心", "探测中心", "信息中心", "局机关", "干部学院", "机关服务中心", "财务核算中心", "卫星中心", "华风影视集团", "北京"
		};
		int mergeOther = 0;
		for (Object[] objects : list) {
			boolean flag = false;
			String proname = (String)objects[0];
			for(int i = 0; i < mergeArea.length; i++){
				if(proname.equals(mergeArea[i])){
					mergeOther += Integer.valueOf(objects[1] + "");
					flag = true;
					break;
				}
			}
			if(flag == false){
				Map<String, Object> map = new HashMap<String, Object>();

				if(day==-1){
					Integer ipnum=(Integer) CacheMapUtil.getCache("yesterdayIPNum"+objects[0], null);
					if(ipnum==null){
						ipnum=countSource(-1,(String)objects[0]);
						CacheMapUtil.putCache("yesterdayIPNum"+objects[0], ipnum);
						map.put("ipnum", ipnum);
					}else{
						map.put("ipnum", ipnum);
					}
				}else if(day==0){
					Integer ipnum=(Integer) CacheMapUtil.getCache("todaydayIPNum"+objects[0], null);
					if(ipnum==null){
						ipnum=countSource(0,(String)objects[0]);
						CacheMapUtil.putCache("todaydayIPNum"+objects[0], ipnum);
						map.put("ipnum", ipnum);
					}else{
						map.put("ipnum", ipnum);
					}
				}else{
					map.put("ipnum", objects[1]);
				}
				if(objects[0].equals("内蒙")){
					objects[0] = "内蒙古";
				}
				map.put("proname", objects[0]);
				list1.add(map);
			}
		}
		Map<String, Object> mapOther = new HashMap<String, Object>();
		if(mergeOther!=0){
			mapOther.put("proname", "北京");
			mapOther.put("ipnum", mergeOther);
		}
		list1.add(mapOther);
		String json = "[";
		for(Map<String, Object> map: list1){
			json +=  "{\"name\":" +"\"" + map.get("proname") +"\","+ "\"value\":" + map.get("ipnum") + "},";
		}
		json = json.substring(0, json.length() - 1) + "]";
		json = "{\"data\": "+json +"}"; 
		return json;
	}
	
	public String findPvDataByDateMap(String date,String showType) {
		List<Object[]> list = this.accessipdao.findPvDataByDate(date,showType);
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		String[] mergeArea = new String[]{"气象报社", "科技大楼（气象/气候）", "华云", "气科院", "气象学会", "气象出版社",
				"公服中心", "探测中心", "信息中心", "局机关", "干部学院", "机关服务中心", "财务核算中心", "卫星中心", "华风影视集团", "北京"
		};
		int mergeOther = 0;
		for (Object[] objects : list) {
			boolean flag = false;
			String proname = (String)objects[0];
			for(int i = 0; i < mergeArea.length; i++){
				if(proname.equals(mergeArea[i])){
					mergeOther += Integer.valueOf(objects[1] + "");
					flag = true;
					break;
				}
			}
			if(flag == false){
				Map<String, Object> map = new HashMap<String, Object>();
				if(objects[0].equals("内蒙")){
					objects[0] = "内蒙古";
				}
				map.put("proname", objects[0]);
				map.put("ipnum", objects[1]);
				list1.add(map);
			}
		}
		Map<String, Object> mapOther = new HashMap<String, Object>();
		if(mergeOther!=0){
			mapOther.put("proname", "北京");
			mapOther.put("ipnum", mergeOther);
		}
		list1.add(mapOther);
		String json = "[";
		for(Map<String, Object> map: list1){
			json +=  "{\"name\":" +"\"" + map.get("proname") +"\","+ "\"value\":" + map.get("ipnum") + "},";
		}
		json = json.substring(0, json.length() - 1) + "]";
		json = "{\"data\": "+json +"}"; 
		return json;
	}
	
	public String findDataByDate(String date1, String date2,String showType) {
		List<Object[]> list = this.accessipdao.findDataByDate(date1, date2,showType);
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		int total = 0;
		int top = 1;
		int others = 0;
		for (Object[] objects : list) {
			if(top <= 5){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("proname", objects[0]);
				map.put("ipnum", objects[1]);
				list1.add(map);
			}else{
				others += Integer.valueOf(objects[1] + "");
			}
			total += Integer.valueOf(objects[1] + "");
			top++;
		}
		if(others!=0){
			Map<String, Object> mapOther = new HashMap<String, Object>();
			mapOther.put("proname", "其他");
			mapOther.put("ipnum", others);
			list1.add(mapOther);
		}
		
		Map<String, Object> maptemp = new HashMap<String, Object>();
		for (Map<String, Object> map : list1) {
			String sum = map.get("ipnum") + "";
			String key = (String) map.get("proname");
			String value1 = String.valueOf((maptemp.get(key) == null ? 0 : maptemp.get(key)));
			int value2 = Integer.valueOf(sum) + Integer.valueOf(value1);
			maptemp.put(key, value2);
		}
		JSONArray ja_result = new JSONArray();
		Set<String> set = maptemp.keySet();
		int i = 0;
		for (String string : set) {
			JSONArray ja_val = new JSONArray();
			float p;
			if(total==0){
				p=0;
			}else{
			    p = ((float) Integer.valueOf(maptemp.get(string) + "") / (float) total) * 100;
			}
			ja_val.add(0, string);
			ja_val.add(1, p==0?"-":p);
			ja_result.add(i, ja_val);
			i++;
		}
		return ja_result.toString();
	}
	
	public String findDataByDateMap(String date1, String date2,String showType,int day) {
		List<Object[]> list = this.accessipdao.findDataByDate(date1, date2,showType);
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		String[] mergeArea = new String[]{"气象报社", "科技大楼（气象/气候）", "华云", "气科院", "气象学会", "气象出版社",
				"公服中心", "探测中心", "信息中心", "局机关", "干部学院", "机关服务中心", "财务核算中心", "卫星中心", "华风影视集团", "北京"
		};
		int mergeOther = 0;
		for (Object[] objects : list) {
			boolean flag = false;
			String proname = (String)objects[0];
			for(int i = 0; i < mergeArea.length; i++){
				if(proname.equals(mergeArea[i])){
					mergeOther += Integer.valueOf(objects[1] + "");
					flag = true;
					break;
				}
			}
			if(flag == false){
				Map<String, Object> map = new HashMap<String, Object>();
				if(day==-7){
					Integer ipnum=(Integer) CacheMapUtil.getCache("lastWeekIPNum"+objects[0], null);
					if(ipnum==null){
						ipnum=countSource(-7,(String)objects[0]);
						CacheMapUtil.putCache("lastWeekIPNum"+objects[0], ipnum);
						map.put("ipnum", ipnum);
					}else{
						map.put("ipnum", ipnum);
					}
				}else if(day==-30){
					Integer ipnum=(Integer) CacheMapUtil.getCache("lastMonthIPNum"+objects[0], null);
					if(ipnum==null){
						ipnum=countSource(-30,(String)objects[0]);
						CacheMapUtil.putCache("lastMonthIPNum"+objects[0], ipnum);
						map.put("ipnum", ipnum);
					}else{
						map.put("ipnum", ipnum);
					}
				}else{
					map.put("ipnum", objects[1]);
				}
				if(objects[0].equals("内蒙")){
					objects[0] = "内蒙古";
				}
				map.put("proname", objects[0]);
				list1.add(map);
			}
		}
		Map<String, Object> mapOther = new HashMap<String, Object>();
		if(mergeOther!=0){
			mapOther.put("proname", "北京");
			mapOther.put("ipnum", mergeOther);
		}
		list1.add(mapOther);
		String json = "[";
		for(Map<String, Object> map: list1){
			json +=  "{\"name\":" +"\"" + map.get("proname") +"\","+ "\"value\":" + map.get("ipnum") + "},";
		}
		json = json.substring(0, json.length() - 1) + "]";
		json = "{\"data\": "+json +"}"; 
		return json;
	}
	
	public String findPvDataByDateMap(String date1, String date2,String showType) {
		List<Object[]> list = this.accessipdao.findPvDataByDate(date1, date2,showType);
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		String[] mergeArea = new String[]{"气象报社", "科技大楼（气象/气候）", "华云", "气科院", "气象学会", "气象出版社",
				"公服中心", "探测中心", "信息中心", "局机关", "干部学院", "机关服务中心", "财务核算中心", "卫星中心", "华风影视集团", "北京"
		};
		int mergeOther = 0;
		for (Object[] objects : list) {
			boolean flag = false;
			String proname = (String)objects[0];
			for(int i = 0; i < mergeArea.length; i++){
				if(proname.equals(mergeArea[i])){
					mergeOther += Integer.valueOf(objects[1] + "");
					flag = true;
					break;
				}
			}
			if(flag == false){
				Map<String, Object> map = new HashMap<String, Object>();
				if(objects[0].equals("内蒙")){
					objects[0] = "内蒙古";
				}
				map.put("proname", objects[0]);
				map.put("ipnum", objects[1]);
				list1.add(map);
			}
		}
		Map<String, Object> mapOther = new HashMap<String, Object>();
		if(mergeOther!=0){
			mapOther.put("proname", "北京");
			mapOther.put("ipnum", mergeOther);
		}
		list1.add(mapOther);
		String json = "[";
		for(Map<String, Object> map: list1){
			json +=  "{\"name\":" +"\"" + map.get("proname") +"\","+ "\"value\":" + map.get("ipnum") + "},";
		}
		json = json.substring(0, json.length() - 1) + "]";
		json = "{\"data\": "+json +"}"; 
		System.out.println(json);
		return json;
	}
	
	public List<Map<String, Object>> topAddress(String date,String showType) {
		List<Object[]> list = this.accessipdao.findDataByDate(date,showType);
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		int total = 0;
		int top = 1;
		int others = 0;
		for (Object[] objects : list) {
			if(top <= 10){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("proname", objects[0]);
				map.put("ipnum", objects[1]);
				list1.add(map);
			}else{
				others += Integer.valueOf(objects[1] + "");
			}
			total += Integer.valueOf(objects[1] + "");
			top++;
		}
		/*Map<String, Object> mapOther = new HashMap<String, Object>();
		mapOther.put("proname", "其他");
		mapOther.put("ipnum", others);
		list1.add(mapOther);*/
		return list1;
		/*Map<String, Object> mapdata = this.accessipdao.topAddress();
		List<Object[]> list = (List<Object[]>) mapdata.get("list");
		List<Map<String, Object>> listres = new ArrayList<Map<String, Object>>();
		for (Object[] objects : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("proname", objects[0]);
			map.put("ipnum", objects[1]);
			map.put("date1", mapdata.get("date1"));
			map.put("date2", mapdata.get("date2"));
			listres.add(map);
		}
		return listres;*/
	}
	
	public List<Map<String, Object>> topAddress(String date1, String date2,String showType) {
		List<Object[]> list = this.accessipdao.findDataByDate(date1, date2,showType);
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		int total = 0;
		int top = 1;
		int others = 0;
		for (Object[] objects : list) {
			if(top <= 10){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("proname", objects[0]);
				map.put("ipnum", objects[1]);
				list1.add(map);
			}else{
				others += Integer.valueOf(objects[1] + "");
			}
			total += Integer.valueOf(objects[1] + "");
			top++;
		}
		/*Map<String, Object> mapOther = new HashMap<String, Object>();
		mapOther.put("proname", "其他");
		mapOther.put("ipnum", others);
		list1.add(mapOther);*/
		return list1;
	}

	public int getStatisticIPNum(int i) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(cal.getTime());
		int result = 0;
		switch(i){
			case 1:
				result = accessipdao.getStatisticIPNum(date);
				break;
			case 2:
				cal.add(Calendar.DAY_OF_MONTH, -1);
				String date2 = sdf.format(cal.getTime());
				result = accessipdao.getStatisticIPNum(date2);
				break;
			case 3:
				cal.add(Calendar.DAY_OF_MONTH, -7);
				String date3 = sdf.format(cal.getTime());
				result = accessipdao.getStatisticIPNum(date, date3);
				break;
			case 4:
				cal.add(Calendar.DAY_OF_MONTH, -30);
				String date4 = sdf.format(cal.getTime());
				result = accessipdao.getStatisticIPNum(date, date4);
				break;
			case 5:
				result = accessipdao.getStatisticPVNum();
				break;
		}
		return result;
	}
	
	
	//统计每个栏目ip
	  @Transactional
	  public Map countIPNum(int day, String menuId,String showType) {
	 		if (menuId.equals("")) {
	 			menuId = "0";
	 		}
	 		List<AccessFunConfig> list = accessFunConfigService
	 				.findSubItems(menuId);
	 		if(list.size()==0){
	 			list = accessFunConfigService.findItems(menuId);
	 		}
	 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 		Date date = new Date();
	 		Calendar calendar = Calendar.getInstance();
	 		Comparas comparas = comparasService.findComparas("accessipNum");
	 		String queryUrl = comparas.getStringvalue();// 获取路径;
	 		String queryData = "";
	 		Map map = new HashMap();
	 		for (int n = 0; n < list.size(); n++) {
	 			String funcItemId = list.get(n).getFuncItemId();
	 			String itempath_placeholder = "";
	 			if(funcItemId.length()==1){
	 				itempath_placeholder = funcItemId+"*";
	 			}else if(funcItemId.length()==3){
	 				itempath_placeholder = funcItemId.substring(0, 1)+"_"+funcItemId+"*";
	 			}else if(funcItemId.length()==5){
	 				itempath_placeholder = funcItemId.substring(0, 1)+"_"+funcItemId.substring(0, 3)+"_"+funcItemId+"*";
	 			}else if(funcItemId.length()==7){
	 				itempath_placeholder = funcItemId.substring(0, 1)+"_"+funcItemId.substring(0, 3)+"_"+funcItemId.substring(0, 5)+"_"+funcItemId+"*";
	 			}
	 			
	 			//int id = Integer.valueOf(funcItemId);
	 			long minTime = 0L;
	 			long maxTime = 0L;
	 			int count = 0;
	 			if(showType!=null&&!"".equals(showType)){
	 				/*queryData="{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\":{"
	 						    +"\"filtered\": {\"query\": {\"bool\":{\"must\":[{\"match\": {\"pfuncid\":\""+id+"\"}},"
	 						   +"{\"match\": {\"sourceType\": \""+showType+"\"}}]}},\"filter\": {\"bool\": {\"must\": ["
	 						    +"{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}]}}}}}";*/
	 				//queryData="{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"bool\": {\"filter\":{\"match\": {\"pfuncid\": \""+id+"\"}},\"filter\":{\"match\": {\"sourceType\": \""+showType+"\"}},\"must\": {\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}}}}";
	 				queryData="{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"bool\": {\"filter\":{\"wildcard\": {\"itempath\": \""+itempath_placeholder+"\"}},\"filter\":{\"match\": {\"sourceType\": \""+showType+"\"}},\"must\": {\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}}}}";
	 			}else{
	 				/*queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": " +
		 					"{\"query\": \"*\",\"analyze_wildcard\": true}},\"filter\": " +
		 					"{\"bool\": {\"must\": [{\"query\": {\"query_string\": " +
		 					"{\"query\": \"*\",\"analyze_wildcard\": true}}},{\"range\": " +
		 					"{\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": " +
		 					"[]}}}},\"size\": 0,\"aggs\": {\"2\": {\"date_range\":" +
		 					" {\"field\": \"@timestamp\",\"ranges\": [{\"from\": \"minTime\",\"to\": \"maxTime\"}" +
		 					"]},\"aggs\": {\"3\": {\"filters\": {\"filters\": {\"pfuncid:"+id+"\":" +
		 							" {\"query\": {\"query_string\": {\"query\": \"pfuncid:"+id+"\",\"analyze_wildcard\": true}}}" +
		 							"}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}" +
		 							"}}}}";*/
	 				//queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\": [{\"from\": \"minTime\",\"to\": \"maxTime\"}]},\"aggs\": {\"3\": {\"filters\": {\"filters\": {\"pfuncid:"+id+"\": {\"query_string\": {\"query\": \"pfuncid:"+id+"\",\"analyze_wildcard\": true}}}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}}}";
	 				queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\": [{\"from\": \"minTime\",\"to\": \"maxTime\"}]},\"aggs\": {\"3\": {\"filters\": {\"filters\": {\"query\": {\"wildcard\": {\"itempath\": \""+itempath_placeholder+"\"}}}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}}}";
	 			}
	 			
	 			if (day == 0) {// 今天
	 				
	 				try {
	 					minTime = sdf.parse(sdf.format(date)).getTime();
	 					maxTime = date.getTime();
	 					/*if (!menuId.equals("0")) {
	 						queryData = queryData.replace("pfuncid", "funcid");
	 					}*/
	 					queryData = queryData.replace("minTime",
	 							String.valueOf(minTime));
	 					queryData = queryData.replace("maxTime",
	 							String.valueOf(maxTime));
	 					Resty r = new Resty();
	 					
	 					//Content content = new Content("", queryData.getBytes());
	 					Content content = Resty.content(queryData);
	 					JSONResource data = r.json(queryUrl, content);
	 					String jsonstr = data.get("aggregations").toString();
	 					jsonstr = jsonstr.replace("{", "").replace("}", "")
	 							.replace("[", "").replace("]", "")
	 							.replace("\"", "");
	 					String jsonarr[] = jsonstr.split(",");
	 					for (int i = 0; i < jsonarr.length; i++) {
	 						String subarr[] = jsonarr[i].split(":");
	 						for (int j = 0; j < subarr.length; j++) {
	 							if (subarr[j].equals("value")) {
	 								count = Integer.valueOf(subarr[j + 1]);
	 							}
	 						}
	 					}
	 				} catch (Exception e) {

	 					e.printStackTrace();
	 				}

	 			}
	 			if (day == -1) {// 昨天
	 				
	 				try {
	 					calendar.setTime(sdf.parse(sdf.format(date)));
	 					calendar.add(Calendar.DAY_OF_MONTH, day);
	 					minTime = calendar.getTimeInMillis();
	 					maxTime = new Long(
	 							sdf.parse(sdf.format(date)).getTime() - 1);
	 					/*if (!menuId.equals("0")) {
	 						queryData = queryData.replace("pfuncid", "funcid");
	 					}*/
	 					queryData = queryData.replace("minTime",
	 							String.valueOf(minTime));
	 					queryData = queryData.replace("maxTime",
	 							String.valueOf(maxTime));
	 					Resty r = new Resty();
	 					//Content content = new Content("", queryData.getBytes());
	 					Content content = Resty.content(queryData);
	 					JSONResource data = r.json(queryUrl, content);
	 					String jsonstr = data.get("aggregations").toString();
	 					jsonstr = jsonstr.replace("{", "").replace("}", "")
	 							.replace("[", "").replace("]", "")
	 							.replace("\"", "");
	 					String jsonarr[] = jsonstr.split(",");
	 					for (int i = 0; i < jsonarr.length; i++) {
	 						String subarr[] = jsonarr[i].split(":");
	 						for (int j = 0; j < subarr.length; j++) {
	 							if (subarr[j].equals("value")) {
	 								count = Integer.valueOf(subarr[j + 1]);
	 							}
	 						}
	 					}
	 				} catch (Exception e) {

	 					e.printStackTrace();
	 				}
	 			}
	 			if (day == -7) {// 一周
	 				
	 				try {
	 					calendar.setTime(date);
	 					calendar.add(Calendar.DAY_OF_MONTH, day);
	 					minTime = calendar.getTimeInMillis();
	 					maxTime = date.getTime();
	 					/*if (!menuId.equals("0")) {
	 						queryData = queryData.replace("pfuncid", "funcid");
	 					}*/
	 					queryData = queryData.replace("minTime",
	 							String.valueOf(minTime));
	 					queryData = queryData.replace("maxTime",
	 							String.valueOf(maxTime));
	 					queryData = queryData.replace("now-30d/d", "now-7d/d");
	 					Resty r = new Resty();
	 					//Content content = new Content("", queryData.getBytes());
	 					Content content = Resty.content(queryData);
	 					JSONResource data = r.json(queryUrl, content);
	 					String jsonstr = data.get("aggregations").toString();
	 					jsonstr = jsonstr.replace("{", "").replace("}", "")
	 							.replace("[", "").replace("]", "")
	 							.replace("\"", "");
	 					String jsonarr[] = jsonstr.split(",");
	 					for (int i = 0; i < jsonarr.length; i++) {
	 						String subarr[] = jsonarr[i].split(":");
	 						for (int j = 0; j < subarr.length; j++) {
	 							if (subarr[j].equals("value")) {
	 								count = Integer.valueOf(subarr[j + 1]);
	 							}
	 						}
	 					}
	 				} catch (Exception e) {

	 					e.printStackTrace();
	 				}
	 			}
	 			if (day == -30) {// 一月
	 				
	 				try {
	 					calendar.setTime(date);
	 					calendar.add(Calendar.DAY_OF_MONTH, day);
	 					minTime = calendar.getTimeInMillis();
	 					maxTime = date.getTime();
	 					/*if (!menuId.equals("0")) {
	 						queryData = queryData.replace("pfuncid", "funcid");
	 					}*/
	 					queryData = queryData.replace("minTime",
	 							String.valueOf(minTime));
	 					queryData = queryData.replace("maxTime",
	 							String.valueOf(maxTime));
	 					Resty r = new Resty();
	 					//Content content = new Content("", queryData.getBytes());
	 					Content content = Resty.content(queryData);
	 					JSONResource data = r.json(queryUrl, content);
	 					String jsonstr = data.get("aggregations").toString();
	 					jsonstr = jsonstr.replace("{", "").replace("}", "")
	 							.replace("[", "").replace("]", "")
	 							.replace("\"", "");
	 					String jsonarr[] = jsonstr.split(",");
	 					for (int i = 0; i < jsonarr.length; i++) {
	 						String subarr[] = jsonarr[i].split(":");
	 						for (int j = 0; j < subarr.length; j++) {
	 							if (subarr[j].equals("value")) {
	 								count = Integer.valueOf(subarr[j + 1]);
	 							}
	 						}
	 					}
	 				} catch (Exception e) {

	 					e.printStackTrace();
	 				}
	 			}

	 			map.put(String.valueOf(funcItemId), count);
	 		}
	 		return map;
	 	}
	  
	  
	  //今日，昨日，一周，一月ip
	  	@Transactional
		public int count(int day,String showType) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			long minTime = 0L;
			long maxTime = 0L;
			Comparas comparas = comparasService.findComparas("accessipNum");
			String queryUrl =comparas.getStringvalue();
			int count = 0;
			if (day == 0) {// 今天
				String queryData=null;
				if(showType!=null&&!"".equals(showType)){
					/*queryData="{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"filtered\": {\"query\": {"
					        +"\"match\": {\"sourceType\": \"showType\"}},\"filter\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {"
					                  +"\"gte\": minTime,\"lte\": 00000000000}}}]}}}}}";*/
					queryData = "{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"bool\": {\"must\": {\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": 00000000000}}},\"filter\": {\"match\": {\"sourceType\": \"showType\"}}}}}";
					queryData = queryData.replace("showType",showType);
				}else{
					/*queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": "
							+ "{\"query\": \"*\",\"analyze_wildcard\": true}},\"filter\":"
							+ " {\"bool\": {\"must\": [ {\"query\": {\"query_string\": "
							+ "{\"query\": \"*\",\"analyze_wildcard\": true }}},{\"range\":"
							+ " {\"@timestamp\": {\"gte\": minTime,\"lte\": 00000000000}}"
							+ "}], \"must_not\": []}}}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\":"
							+ " {\"field\": \"@timestamp\",\"interval\": \"1d\",\"pre_zone\": \"+08:00\",\"pre_zone_adjust_large_interval\": true,\"min_doc_count\": 1,\"extended_bounds\":"
							+ " {\"min\": minTime,\"max\": 00000000000}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";*/
					queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": 00000000000}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1d\",\"min_doc_count\": 1,\"extended_bounds\": {\"min\": minTime,\"max\": 00000000000}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";
				}
				

				try {
					minTime = sdf.parse(sdf.format(date)).getTime();
					maxTime = date.getTime();
					queryData = queryData.replace("minTime",
							String.valueOf(minTime));
					queryData = queryData.replace("00000000000",
							String.valueOf(maxTime));
					Resty r = new Resty();
					//Content content = new Content("", queryData.getBytes());
					Content content = Resty.content(queryData);
					JSONResource data = r.json(queryUrl, content);
					String jsonstr = data.get("aggregations").toString();
					jsonstr = jsonstr.replace("{", "").replace("}", "")
							.replace("[", "").replace("]", "").replace("\"", "");
					String jsonarr[] = jsonstr.split(",");
					for (int i = 0; i < jsonarr.length; i++) {
						String subarr[] = jsonarr[i].split(":");
						for (int j = 0; j < subarr.length; j++) {
							if (subarr[j].equals("value")) {
								count = Integer.valueOf(subarr[j + 1]);
							}
						}
					}
				} catch (Exception e) {

					e.printStackTrace();
				}

			}
			if (day == -1) {// 昨天
				String queryData=null;
				if(showType!=null&&!"".equals(showType)){
					/*queryData="{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"filtered\": {\"query\": {"
					        +"\"match\": {\"sourceType\": \"showType\"}},\"filter\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {"
					                  +"\"gte\": minTime,\"lte\": maxTime}}}]}}}}}";*/
					queryData = "{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"bool\": {\"must\": {\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}},\"filter\": {\"match\": {\"sourceType\": \"showType\"}}}}}";
					queryData = queryData.replace("showType",showType);
				}else{
					/*queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": "
							+ "{\"query\": \"*\",\"analyze_wildcard\": true}},\"filter\":"
							+ " {\"bool\": {\"must\": [ {\"query\": {\"query_string\": "
							+ "{\"query\": \"*\",\"analyze_wildcard\": true }}},{\"range\":"
							+ " {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}"
							+ "}], \"must_not\": []}}}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\":"
							+ " {\"field\": \"@timestamp\",\"interval\": \"1d\",\"pre_zone\": \"+08:00\",\"pre_zone_adjust_large_interval\": true,\"min_doc_count\": 1,\"extended_bounds\":"
							+ " {\"min\": minTime,\"max\": maxTime}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";*/
					queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1d\",\"min_doc_count\": 1,\"extended_bounds\": {\"min\": minTime,\"max\": maxTime}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";
				}
				try {
					calendar.setTime(sdf.parse(sdf.format(date)));
					calendar.add(Calendar.DAY_OF_MONTH, day);
					minTime = calendar.getTimeInMillis();
					maxTime = new Long(sdf.parse(sdf.format(date)).getTime() - 1);
					queryData = queryData.replace("minTime",
							String.valueOf(minTime));
					queryData = queryData.replace("maxTime",
							String.valueOf(maxTime));
					Resty r = new Resty();
					//Content content = new Content("", queryData.getBytes());
					Content content = Resty.content(queryData);
					JSONResource data = r.json(queryUrl, content);
					String jsonstr = data.get("aggregations").toString();
					jsonstr = jsonstr.replace("{", "").replace("}", "")
							.replace("[", "").replace("]", "").replace("\"", "");
					String jsonarr[] = jsonstr.split(",");
					for (int i = 0; i < jsonarr.length; i++) {
						String subarr[] = jsonarr[i].split(":");
						for (int j = 0; j < subarr.length; j++) {
							if (subarr[j].equals("value")) {
								count = Integer.valueOf(subarr[j + 1]);
							}
						}
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			if (day == -7) {// 一周
				String queryData=null;
				if(showType!=null&&!"".equals(showType)){
					/*queryData="{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"filtered\": {\"query\": {"
					        +"\"match\": {\"sourceType\": \"showType\"}},\"filter\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {"
					                  +"\"gte\": minTime,\"lte\": maxTime}}}]}}}}}";*/
					queryData = "{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"bool\": {\"must\": {\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}},\"filter\": {\"match\": {\"sourceType\": \"showType\"}}}}}";
					queryData = queryData.replace("showType",showType);
				}else{
					/*queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": {\"query\": \"*\",\"analyze_wildcard\": true}"
							+ "},\"filter\": {\"bool\": {\"must\": [{\"query\": {\"query_string\": {\"analyze_wildcard\": true,\"query\": \"*\"}"
							+ "}},{\"range\": {\"@timestamp\": {\"gte\": minTime, \"lte\": maxTime}}} ],\"must_not\": []"
							+ "}}}},\"size\": 0,\"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\": ["
							+ "{\"from\": \"now-30d/d\",\"to\": \"now\"}]},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";*/
					queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\": [{\"from\": \"now-30d/d\",\"to\": \"now\"}]},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";
				}
				try {
					calendar.setTime(date);
					calendar.add(Calendar.DAY_OF_MONTH, day);
					minTime = calendar.getTimeInMillis();
					maxTime = date.getTime();
					queryData = queryData.replace("minTime",
							String.valueOf(minTime));
					queryData = queryData.replace("maxTime",
							String.valueOf(maxTime));
					queryData = queryData.replace("now-30d/d", "now-7d/d");
					Resty r = new Resty();
					//Content content = new Content("", queryData.getBytes());
					Content content = Resty.content(queryData);
					System.out.println("77"+queryData);
					JSONResource data = r.json(queryUrl, content);
					String jsonstr = data.get("aggregations").toString();
					jsonstr = jsonstr.replace("{", "").replace("}", "")
							.replace("[", "").replace("]", "").replace("\"", "");
					String jsonarr[] = jsonstr.split(",");
					for (int i = 0; i < jsonarr.length; i++) {
						String subarr[] = jsonarr[i].split(":");
						for (int j = 0; j < subarr.length; j++) {
							if (subarr[j].equals("value")) {
								count = Integer.valueOf(subarr[j + 1]);
							}
						}
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			if (day == -30) {// 一月
				String queryData=null;
				if(showType!=null&&!"".equals(showType)){
					/*queryData="{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"filtered\": {\"query\": {"
					        +"\"match\": {\"sourceType\": \"showType\"}},\"filter\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {"
					                  +"\"gte\": minTime,\"lte\": maxTime}}}]}}}}}";*/
					queryData = "{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"bool\": {\"must\": {\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}},\"filter\": {\"match\": {\"sourceType\": \"showType\"}}}}}";
					queryData = queryData.replace("showType",showType);
				}else{
					/*queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": {\"query\": \"*\",\"analyze_wildcard\": true}"
							+ "},\"filter\": {\"bool\": {\"must\": [{\"query\": {\"query_string\": {\"analyze_wildcard\": true,\"query\": \"*\"}"
							+ "}},{\"range\": {\"@timestamp\": {\"gte\": minTime, \"lte\": maxTime}}} ],\"must_not\": []"
							+ "}}}},\"size\": 0,\"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\": ["
							+ "{\"from\": \"now-30d/d\",\"to\": \"now\"}]},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";*/
					queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\": [{\"from\": \"now-30d/d\",\"to\": \"now\"}]},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";
				}
				try {
					calendar.setTime(date);
					calendar.add(Calendar.DAY_OF_MONTH, day);
					minTime = calendar.getTimeInMillis();
					maxTime = date.getTime();
					queryData = queryData.replace("minTime",
							String.valueOf(minTime));
					queryData = queryData.replace("maxTime",
							String.valueOf(maxTime));
					Resty r = new Resty();
					//Content content = new Content("", queryData.getBytes());
					Content content = Resty.content(queryData);
					JSONResource data = r.json(queryUrl, content);
					String jsonstr = data.get("aggregations").toString();
					jsonstr = jsonstr.replace("{", "").replace("}", "")
							.replace("[", "").replace("]", "").replace("\"", "");
					String jsonarr[] = jsonstr.split(",");
					for (int i = 0; i < jsonarr.length; i++) {
						String subarr[] = jsonarr[i].split(":");
						for (int j = 0; j < subarr.length; j++) {
							if (subarr[j].equals("value")) {
								count = Integer.valueOf(subarr[j + 1]);
							}
						}
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			if (day == 1) {// 累计
				String queryData=null;
				if(showType!=null&&!"".equals(showType)){
					/*queryData="{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"filtered\": {\"query\": {"
					        +"\"match\": {\"sourceType\": \"showType\"}},\"filter\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {"
					                  +"\"gte\": minTime,\"lte\": maxTime}}}]}}}}}";*/
					queryData = "{\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}},\"query\": {\"bool\": {\"must\": {\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}},\"filter\": {\"match\": {\"sourceType\": \"showType\"}}}}}";
					queryData = queryData.replace("showType",showType);
				}else{
					/*queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": " +
							"{\"query\": \"*\",\"analyze_wildcard\": true}},\"filter\": {\"bool\":" +
							" {\"must\": [{\"query\": {\"query_string\": {\"query\": \"*\"," +
							"\"analyze_wildcard\": true}}},{\"range\": {\"@timestamp\": " +
							"{\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}" +
							"}}},\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": " +
							"{\"field\": \"remote_ip\"}}}}";*/
					queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}";
				}
				try {
					Comparas compara = comparasService.findComparas("ipbaseNumber");
					Integer ipbaseNumber=compara.getIntvalue();
					calendar.setTime(date);
					calendar.add(Calendar.YEAR, -5);
					minTime = calendar.getTimeInMillis();
					maxTime = date.getTime();
					queryData = queryData.replace("minTime",
							String.valueOf(minTime));
					queryData = queryData.replace("maxTime",
							String.valueOf(maxTime));
					Resty r = new Resty();
					//Content content = new Content("", queryData.getBytes());
					Content content = Resty.content(queryData);
					JSONResource data = r.json(queryUrl, content);
					String jsonstr = data.get("aggregations").toString();
					jsonstr = jsonstr.replace("{", "").replace("}", "")
							.replace("[", "").replace("]", "").replace("\"", "");
					String jsonarr[] = jsonstr.split(",");
					for (int i = 0; i < jsonarr.length; i++) {
						String subarr[] = jsonarr[i].split(":");
						for (int j = 0; j < subarr.length; j++) {
							if (subarr[j].equals("value")) {
								count = Integer.valueOf(subarr[j + 1]);
							}
						}
					}
					count=count+ipbaseNumber;
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			return count;
		}
		//根据日期获得IP
		public int count(String datestr,String showType) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			long minTime = 0L;
			long maxTime = 0L;
			Comparas comparas = comparasService.findComparas("accessipNum");
			String queryUrl = comparas.getStringvalue();// 获取路径;
			int count = 0;
				/*String queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": "
						+ "{\"query\": \"*\",\"analyze_wildcard\": true}},\"filter\":"
						+ " {\"bool\": {\"must\": [ {\"query\": {\"query_string\": "
						+ "{\"query\": \"*\",\"analyze_wildcard\": true }}},{\"range\":"
						+ " {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}"
						+ "}], \"must_not\": []}}}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\":"
						+ " {\"field\": \"@timestamp\",\"interval\": \"1d\",\"pre_zone\": \"+08:00\",\"pre_zone_adjust_large_interval\": true,\"min_doc_count\": 1,\"extended_bounds\":"
						+ " {\"min\": minTime,\"max\": maxTime}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";*/
				String queryData = "{\"query\": {\"bool\": {\"must\": [ {\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1d\",\"min_doc_count\": 1,\"extended_bounds\": {\"min\": minTime,\"max\": maxTime}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";
				try {
					
					calendar.setTime(sdf.parse(sdf.format(date)));
					if(datestr.equals(sdf.format(calendar.getTime()))){
						count=(Integer) CacheMapUtil.getCache("todayIPNum", 0);
						if(count==0){
							count = count(0,showType);
						}
						return count;
					}					
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					if(datestr.equals(sdf.format(calendar.getTime()))){
						count=(Integer) CacheMapUtil.getCache("yesterdayIPNum", 0);
						if(count==0){
							count = count(-1,showType);
						}
						return count;
					}
				
					minTime = sdf.parse(datestr).getTime();
					calendar.setTime(sdf.parse(datestr));
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					maxTime = calendar.getTimeInMillis()-1;	
					queryData = queryData.replace("minTime",
							String.valueOf(minTime));
					queryData = queryData.replace("maxTime",
							String.valueOf(maxTime));
					Resty r = new Resty();
					//Content content = new Content("", queryData.getBytes());
					Content content = Resty.content(queryData);
					JSONResource data = r.json(queryUrl, content);
					String jsonstr = data.get("aggregations").toString();
					jsonstr = jsonstr.replace("{", "").replace("}", "")
							.replace("[", "").replace("]", "").replace("\"", "");
					String jsonarr[] = jsonstr.split(",");
					for (int i = 0; i < jsonarr.length; i++) {
						String subarr[] = jsonarr[i].split(":");
						for (int j = 0; j < subarr.length; j++) {
							if (subarr[j].equals("value")) {
								count = Integer.valueOf(subarr[j + 1]);
							}
						}
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
				return count;

			}
		

	// 根据日期获得IP
	public int countByRange(String sdatestr, String edatestr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		long minTime = 0L;
		long maxTime = 0L;
		Comparas comparas = comparasService.findComparas("accessipNum");
		String queryUrl = comparas.getStringvalue();// 获取路径;

		int count = 0;

		/*String queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": "
				+ "{\"query\": \"*\",\"analyze_wildcard\": true}},\"filter\":"
				+ " {\"bool\": {\"must\": [ {\"query\": {\"query_string\": "
				+ "{\"query\": \"*\",\"analyze_wildcard\": true }}},{\"range\":"
				+ " {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}"
				+ "}], \"must_not\": []}}}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\":"
				+ " {\"field\": \"@timestamp\",\"interval\": \"1d\",\"pre_zone\": \"+08:00\",\"pre_zone_adjust_large_interval\": true,\"min_doc_count\": 1,\"extended_bounds\":"
				+ " {\"min\": minTime,\"max\": maxTime}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";*/
		String queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": mintime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1d\",\"min_doc_count\": 1,\"extended_bounds\": {\"min\": mintime,\"max\": maxTime}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";
		try {
			minTime = sdf.parse(sdatestr).getTime();
			maxTime = sdf.parse(edatestr).getTime();
			queryData = queryData.replace("minTime", String.valueOf(minTime));
			queryData = queryData.replace("maxTime", String.valueOf(maxTime));
			Resty r = new Resty();
			//Content content = new Content("", queryData.getBytes());
			Content content = Resty.content(queryData);
			JSONResource data = r.json(queryUrl, content);
			String jsonstr = data.get("aggregations").toString();
			jsonstr = jsonstr.replace("{", "").replace("}", "").replace("[", "").replace("]", "").replace("\"", "");
			String jsonarr[] = jsonstr.split(",");
			for (int i = 0; i < jsonarr.length; i++) {
				String subarr[] = jsonarr[i].split(":");
				for (int j = 0; j < subarr.length; j++) {
					if (subarr[j].equals("value")) {
						count = Integer.valueOf(subarr[j + 1]);
						return count;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	//分省下载量(昨天，过去一周.....)
	public String downLoadSizeByType1(String sdatestr,String edatestr,String showType){
		sdatestr=sdatestr.replace("-","");
		edatestr=edatestr.replace("-","");
		double dataSize=ftpUrlDataDao.getDownSizeByType(sdatestr, edatestr, showType);
		 NumberFormat ddf=NumberFormat.getNumberInstance() ;
	        ddf.setMaximumFractionDigits(2); 
	        if(dataSize!=0){
	        	return ddf.format(dataSize/(1024*1024*1024.00));
	        }else {
	        	return "0";
	        }
	}
	//分省下载量(今天)
		@Transactional
		public String downLoadSizeBytype(String sdatestr,String showType) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			long minTime = 0L;
			long maxTime = 0L;
			Comparas comparas = comparasService.findComparas("downLoadSizeURL");
			String queryUrl = comparas.getStringvalue();// 获取路径;
			double count = 0;			
			/*String queryData = "{\"size\": 0,\"aggs\": {\"1\": {\"sum\": {\"field\": \"fileSize\"}}},\"query\": {"+
			        "\"filtered\": {\"query\": {\"bool\": {\"must\": [{\"match\": {\"sourceType\": \"provinceAndCenter\""+
			         "}}]}},\"filter\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,"+
			          "\"lte\": maxTime}}}]}}}}}";*/
			String queryData = "{\"size\": 0,\"aggs\": {\"1\": {\"sum\": {\"field\": \"fileSize\"}}},\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"filter\": {\"match\": {\"sourceType\": \"provinceAndCenter\"}}}}}";
				try {
					minTime = sdf.parse(sdatestr).getTime();
					//queryUrl=queryUrl.replace("yyyy.MM.dd",sdatestr.replace("-", "."));													
					queryUrl=queryUrl.replace("yyyy.MM.dd",sdatestr.replace("-", ".").substring(0, 7));													
					calendar.setTime(sdf.parse(sdatestr));
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					maxTime = calendar.getTimeInMillis()-1;	
					queryData = queryData.replace("minTime",
							String.valueOf(minTime));
					queryData = queryData.replace("maxTime",
							String.valueOf(maxTime));
					queryData = queryData.replace("provinceAndCenter",
							showType);
					System.out.println("queryData:"+queryData);
					Resty r = new Resty();
					//Content content = new Content("", queryData.getBytes());
					Content content = Resty.content(queryData);
					JSONResource data = r.json(queryUrl, content);
					JSONObject jsonstr = (JSONObject)data.get("aggregations");
					JSONObject jsonstr1 = (JSONObject)jsonstr.get("1");
					count=jsonstr1.getDouble("value");
				} catch (Exception e) {

					e.printStackTrace();
				}
				 NumberFormat ddf=NumberFormat.getNumberInstance() ;
			        ddf.setMaximumFractionDigits(2); 
			        if(count!=0){
			        	return ddf.format(count/(1024*1024*1024.00));
			        }else {
			        	return "0";
			        }
				
			
		}
		
		//下载量
		@Transactional
		public String downLoadSize(String sdatestr,String... edatestr) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			long minTime = 0L;
			long maxTime = 0L;
			Comparas comparas = comparasService.findComparas("downLoadSizeURL");
			String queryUrl = comparas.getStringvalue();// 获取路径;

			double count = 0;
			
			
				/*String queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\":" +
						" {\"query\": \"*\",\"analyze_wildcard\": true }},\"filter\": " +
						"{\"bool\": {\"must\": [{\"query\": {\"query_string\": " +
						"{ \"query\": \"*\",\"analyze_wildcard\": true}}},{\"range\": {\"@timestamp\":" +
						" {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}}}}," +
						"\"size\": 0,\"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\":" +
						" [{\"from\": \"minTime\",\"to\": \"maxTime\"}]},\"aggs\": {\"1\": {\"sum\": {\"field\": \"fileSize\"}}}}}}";*/
				String queryData = "{\"query\": {\"bool\": {\"must\": [{\"query_string\": {\"query\": \"*\",\"analyze_wildcard\": true}}, {\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\": [{\"from\": \"minTime\",\"to\": \"maxTime\"}]},\"aggs\": {\"1\": {\"sum\": {\"field\": \"fileSize\"}}}}}}";
				try {
					minTime = sdf.parse(sdatestr).getTime();
					if(edatestr.length>0){
						queryUrl=queryUrl.replace("yyyy.MM.dd","*");
						maxTime = sdf.parse(edatestr[0]).getTime();
					}else {
						queryUrl=queryUrl.replace("yyyy.MM.dd","*");													
						calendar.setTime(sdf.parse(sdatestr));
						calendar.add(Calendar.DAY_OF_MONTH, 1);
						maxTime = calendar.getTimeInMillis()-1;	
					}
					queryData = queryData.replace("minTime",
							String.valueOf(minTime));
					queryData = queryData.replace("maxTime",
							String.valueOf(maxTime));
					System.out.println("asd"+queryData);
					Resty r = new Resty();
					//Content content = new Content("", queryData.getBytes());
					Content content = Resty.content(queryData);
					JSONResource data = r.json(queryUrl, content);
					String jsonstr = data.get("aggregations").toString();
					jsonstr = jsonstr.replace("{", "").replace("}", "")
							.replace("[", "").replace("]", "").replace("\"", "");
					String jsonarr[] = jsonstr.split(",");
					for (int i = 0; i < jsonarr.length; i++) {
						String subarr[] = jsonarr[i].split(":");
						for (int j = 0; j < subarr.length; j++) {
							if (subarr[j].equals("value")) {
								String str=subarr[j + 1];
								if(str.indexOf("E")!=-1){
									int num=Integer.valueOf(str.substring(str.indexOf("E")+1));
									str=str.substring(0, str.indexOf("E"));
									count=Double.valueOf(str)*Math.pow(10, num);
								}else{
								    count =Double.valueOf(subarr[j + 1]);
								}
							}
						}
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
				 NumberFormat ddf=NumberFormat.getNumberInstance() ;
			        ddf.setMaximumFractionDigits(2); 
			        if(count!=0){
			        	return ddf.format(count/(1024*1024*1024.00));
			        }else {
			        	return "0";
			        }
				
			}
		
	// 下载量
	public String downLoadSizeByDateRange(String sdatestr, String edatestr) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		long minTime = 0L;
		long maxTime = 0L;
		Comparas comparas = comparasService.findComparas("downLoadSizeURL");
		String queryUrl = comparas.getStringvalue();// 获取路径;
		double count = 0;
		/*String queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\":"
				+ " {\"query\": \"*\",\"analyze_wildcard\": true }},\"filter\": "
				+ "{\"bool\": {\"must\": [{\"query\": {\"query_string\": "
				+ "{ \"query\": \"*\",\"analyze_wildcard\": true}}},{\"range\": {\"@timestamp\":"
				+ " {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}}}},"
				+ "\"size\": 0,\"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\":"
				+ " [{\"from\": \"minTime\",\"to\": \"maxTime\"}]},\"aggs\": {\"1\": {\"sum\": {\"field\": \"fileSize\"}}}}}}";*/
		String queryData = "{\"query\": {\"bool\": {\"must\": [{\"query_string\": {\"query\": \"*\",\"analyze_wildcard\": true}}, {\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\": [{\"from\": \"minTime\",\"to\": \"maxTime\"}]},\"aggs\": {\"1\": {\"sum\": {\"field\": \"fileSize\"}}}}}}";
		try {
			minTime = sdf.parse(sdatestr).getTime();
			queryUrl = queryUrl.replace("yyyy.MM.dd", "*");
			maxTime = sdf.parse(edatestr).getTime();
			queryData = queryData.replace("minTime", String.valueOf(minTime));
			queryData = queryData.replace("maxTime", String.valueOf(maxTime));
			Resty r = new Resty();
			//Content content = new Content("", queryData.getBytes());
			Content content = Resty.content(queryData);
			JSONResource data = r.json(queryUrl, content);
			String jsonstr = data.get("aggregations").toString();
			jsonstr = jsonstr.replace("{", "").replace("}", "").replace("[", "").replace("]", "").replace("\"", "");
			String jsonarr[] = jsonstr.split(",");
			for (int i = 0; i < jsonarr.length; i++) {
				String subarr[] = jsonarr[i].split(":");
				for (int j = 0; j < subarr.length; j++) {
					if (subarr[j].equals("value")) {
						String str = subarr[j + 1];
						if (str.indexOf("E") != -1) {
							int num = Integer.valueOf(str.substring(str.indexOf("E") + 1));
							str = str.substring(0, str.indexOf("E"));
							count = Double.valueOf(str) * Math.pow(10, num);
						} else {
							count = Double.valueOf(subarr[j + 1]);
						}
					}
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		NumberFormat ddf = NumberFormat.getNumberInstance();
		ddf.setMaximumFractionDigits(2);
		if (count != 0) {
			return ddf.format(count / (1024 * 1024 * 1024.00));
		} else {
			return "0";
		}

	}
	public List<Object[]>  getHourIPPVloadByRange(String sdate, String edate) {
		return this.accessipdao.getHourIPPVloadByRange(sdate, edate);
	}
		
}
