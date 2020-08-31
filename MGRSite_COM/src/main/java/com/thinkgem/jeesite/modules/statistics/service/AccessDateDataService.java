package com.thinkgem.jeesite.modules.statistics.service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import us.monoid.web.Content;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.CacheMapUtil;
import com.thinkgem.jeesite.modules.statistics.dao.AccessDateDataDao;
import com.thinkgem.jeesite.modules.statistics.dao.AccessDateIpDao;
import com.thinkgem.jeesite.modules.statistics.entity.AccessDateData;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;

@Service
public class AccessDateDataService extends BaseService {
	@Autowired
	private AccessDateDataDao accessDateDataDao;
	@Autowired
	private ComparasService comparasService;
	public List<AccessDateData> findByDate(String date) {
		return this.accessDateDataDao.findByDate(date);
	}
    /**
     * 根据日期 查询
     * @param date
     * @return
     */
	public Map<String, Object> findDataByDate(String date) {
		List<Object[]> list = this.accessDateDataDao.findDataByDate(date);
		List nameList=new ArrayList();
		List ipList=new ArrayList();
		List pvList=new ArrayList();
		for (int i=0;i<list.size();i++) {
			Object[] obj=list.get(i);
			nameList.add(obj[0]);
			ipList.add(Integer.valueOf(obj[1].toString()));
			pvList.add(Integer.valueOf(obj[2].toString()));
			
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nameList", nameList);
		map.put("ipList", ipList);
		map.put("pvList", pvList);
		return map;
	}
	/**
	 * 查询年份
	 * @return
	 */
	public List<String> statYears(){
	     return this.accessDateDataDao.statYears();
		
	}
	/**
	 * 查询单位
	 * @return
	 */
	public List<String> getOrg(String org){
	     return this.accessDateDataDao.getOrg(org);
		
	}
	public List<String[]> getOrgAll(){
	     return this.accessDateDataDao.getOrgAll();
		
	}
	public List<String> getname(String org){
		 return this.accessDateDataDao.getname(org);
	}
	public List getOrgId(String org){
	     return this.accessDateDataDao.getOrgId(org);
		
	}
	public List<Object[]> getSumByTime(String start_time,String end_time,String timeType,Integer page,Integer row,String orderType,String sourceType) {
		return accessDateDataDao.getSumByTime(start_time, end_time, timeType, page, row, orderType,sourceType);
	
	}
	public List<Object[]> getSumByTimeItem(String start_time,String end_time,String funcItemId,String timeType,Integer page,Integer row,String orderType) {
		return accessDateDataDao.getSumByTimeItem(start_time, end_time,funcItemId, timeType, page, row, orderType);
	
	}
	public List<Object[]> getSumByTimeItemB(String start_time,String end_time,String funcItemId,String timeType,Integer page,Integer row,String orderType) {
		return accessDateDataDao.getSumByTimeItemB(start_time, end_time,funcItemId, timeType, page, row, orderType);
	
	}
	public List<Object[]> getSumByTimeItem0(String start_time,String end_time,String funcItemId,String timeType,Integer page,Integer row,String orderType,String org) {
		return accessDateDataDao.getSumByTimeItem0(start_time, end_time,funcItemId, timeType, page, row, orderType,org);
	
	}
	public List<Object[]> getSumByTimeItemA(String start_time,String end_time,String funcItemId,String timeType,Integer page,Integer row,String orderType,String org) {
		return accessDateDataDao.getSumByTimeItemA(start_time, end_time,funcItemId, timeType, page, row, orderType,org);
	
	}
	public List<Object[]> getSumByTimeItem2(String start_time,String end_time,String funcItemId,String timeType,Integer page,Integer row,String orderType) {
		return accessDateDataDao.getSumByTimeItem2(start_time, end_time,funcItemId, timeType, page, row, orderType);
	
	}
	public List<Object[]> getSumByTimeItem22(String start_time,String end_time,String funcItemId,String timeType,Integer page,Integer row,String orderType,String org) {
		return accessDateDataDao.getSumByTimeItem22(start_time, end_time,funcItemId, timeType, page, row, orderType,org);
	
	}
	public List<Object[]> getSumByTimeItem3(String start_time,String end_time,String funcItemId,String timeType,Integer page,Integer row,String orderType) {
		return accessDateDataDao.getSumByTimeItem3(start_time, end_time,funcItemId, timeType, page, row, orderType);
	
	}
	public List<Object[]> getSumByTimeItem3A(String start_time,String end_time,String funcItemId,String timeType,Integer page,Integer row,String orderType) {
		return accessDateDataDao.getSumByTimeItem3A(start_time, end_time,funcItemId, timeType, page, row, orderType);
	
	}
	public List<Object[]> getSumByTimeItem4(String start_time,String end_time,String funcItemId,String timeType,Integer page,Integer row,String orderType,String org) {
		return accessDateDataDao.getSumByTimeItem4(start_time, end_time,funcItemId, timeType, page, row, orderType,org);
	
	}
	public List<Object[]> getSumByTimeItem4A(String start_time,String end_time,String funcItemId,String timeType,Integer page,Integer row,String orderType,String org) {
		return accessDateDataDao.getSumByTimeItem4A(start_time, end_time,funcItemId, timeType, page, row, orderType,org);
	
	}
	/**
	 * 根据时间-日 进行分省查询
	 * @param date
	 * @return
	 */
	public String findDataByDateMap(String date) {
		List<Object[]> list = this.accessDateDataDao.findDataByDate(date);
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
	

	
	
	public List<Map<String, Object>> topAddress(String date) {
		List<Object[]> list = this.accessDateDataDao.findDataByDate(date);
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
		/*Map<String, Object> mapdata = this.accessDateDataDao.topAddress();
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
	
	  
	  //今日，昨日，一周，一月ip
		public int count(int day) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			long minTime = 0L;
			long maxTime = 0L;
			Comparas comparas = comparasService.findComparas("accessipNum");
			String queryUrl = comparas.getStringvalue();// 获取路径;

			int count = 0;
			if (day == 0) {// 今天
				/*String queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": "
						+ "{\"query\": \"*\",\"analyze_wildcard\": true}},\"filter\":"
						+ " {\"bool\": {\"must\": [ {\"query\": {\"query_string\": "
						+ "{\"query\": \"*\",\"analyze_wildcard\": true }}},{\"range\":"
						+ " {\"@timestamp\": {\"gte\": minTime,\"lte\": 00000000000}}"
						+ "}], \"must_not\": []}}}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\":"
						+ " {\"field\": \"@timestamp\",\"interval\": \"1d\",\"pre_zone\": \"+08:00\",\"pre_zone_adjust_large_interval\": true,\"min_doc_count\": 1,\"extended_bounds\":"
						+ " {\"min\": minTime,\"max\": 00000000000}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";*/
				String queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": 00000000000}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1d\",\"min_doc_count\": 1,\"extended_bounds\": {\"min\": minTime,\"max\": 00000000000}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";
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
				/*String queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": "
						+ "{\"query\": \"*\",\"analyze_wildcard\": true}},\"filter\":"
						+ " {\"bool\": {\"must\": [ {\"query\": {\"query_string\": "
						+ "{\"query\": \"*\",\"analyze_wildcard\": true }}},{\"range\":"
						+ " {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}"
						+ "}], \"must_not\": []}}}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\":"
						+ " {\"field\": \"@timestamp\",\"interval\": \"1d\",\"pre_zone\": \"+08:00\",\"pre_zone_adjust_large_interval\": true,\"min_doc_count\": 1,\"extended_bounds\":"
						+ " {\"min\": minTime,\"max\": maxTime}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";*/
				String queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1d\",\"min_doc_count\": 1,\"extended_bounds\": {\"min\": minTime,\"max\": 00000000000}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";
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
				/*String queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": {\"query\": \"*\",\"analyze_wildcard\": true}"
						+ "},\"filter\": {\"bool\": {\"must\": [{\"query\": {\"query_string\": {\"analyze_wildcard\": true,\"query\": \"*\"}"
						+ "}},{\"range\": {\"@timestamp\": {\"gte\": minTime, \"lte\": maxTime}}} ],\"must_not\": []"
						+ "}}}},\"size\": 0,\"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\": ["
						+ "{\"from\": \"now-30d/d\",\"to\": \"now\"}]},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";*/
				String queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0, \"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\": [{\"from\": \"now-30d/d\",\"to\": \"now\"}]},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";
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
				/*String queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": {\"query\": \"*\",\"analyze_wildcard\": true}"
						+ "},\"filter\": {\"bool\": {\"must\": [{\"query\": {\"query_string\": {\"analyze_wildcard\": true,\"query\": \"*\"}"
						+ "}},{\"range\": {\"@timestamp\": {\"gte\": minTime, \"lte\": maxTime}}} ],\"must_not\": []"
						+ "}}}},\"size\": 0,\"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\": ["
						+ "{\"from\": \"now-30d/d\",\"to\": \"now\"}]},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";*/
				String queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0, \"aggs\": {\"2\": {\"date_range\": {\"field\": \"@timestamp\",\"ranges\": [{\"from\": \"now-30d/d\",\"to\": \"now\"}]},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";
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
				/*String queryData = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": " +
						"{\"query\": \"*\",\"analyze_wildcard\": true}},\"filter\": {\"bool\":" +
						" {\"must\": [{\"query\": {\"query_string\": {\"query\": \"*\"," +
						"\"analyze_wildcard\": true}}},{\"range\": {\"@timestamp\": " +
						"{\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}" +
						"}}},\"size\": 0,\"aggs\": {\"1\": {\"cardinality\": " +
						"{\"field\": \"remote_ip\"}}}}";*/
				String queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0, \"aggs\": {\"1\": {\"cardinality\":{\"field\": \"remote_ip\"}}}}";
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
		public int count(String datestr) {
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
				String queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1d\",\"min_doc_count\": 1,\"extended_bounds\": {\"min\": minTime,\"max\": maxTime}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";
				try {
					
					calendar.setTime(sdf.parse(sdf.format(date)));
					if(datestr.equals(sdf.format(calendar.getTime()))){
						count=(Integer) CacheMapUtil.getCache("todayIPNum", 0);
						if(count==0){
							count = count(0);
						}
						return count;
					}					
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					if(datestr.equals(sdf.format(calendar.getTime()))){
						count=(Integer) CacheMapUtil.getCache("yesterdayIPNum", 0);
						if(count==0){
							count = count(-1);
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
		String queryData = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": minTime,\"lte\": maxTime}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"2\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1d\",\"min_doc_count\": 1,\"extended_bounds\": {\"min\": minTime,\"max\": maxTime}},\"aggs\": {\"1\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}}}";
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
		
		
		//下载量
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
						//queryUrl=queryUrl.replace("yyyy.MM.dd",sdatestr.replace("-", ".")));													
						queryUrl=queryUrl.replace("yyyy.MM.dd",sdatestr.replace("-", ".").substring(0, 7));													
						calendar.setTime(sdf.parse(sdatestr));
						calendar.add(Calendar.DAY_OF_MONTH, 1);
						maxTime = calendar.getTimeInMillis()-1;	
					}
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
		
		
}
