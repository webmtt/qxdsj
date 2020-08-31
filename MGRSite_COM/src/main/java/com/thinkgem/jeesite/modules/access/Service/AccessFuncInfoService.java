package com.thinkgem.jeesite.modules.access.Service;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.CacheMapUtil;
import com.thinkgem.jeesite.modules.access.dao.AccessDataDao;
import com.thinkgem.jeesite.modules.access.dao.AccessFuncInfoDao;
import com.thinkgem.jeesite.modules.access.entity.AccessData;
import com.thinkgem.jeesite.modules.access.entity.AccessFuncInfo;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Content;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AccessFuncInfoService extends BaseService {
	@Autowired
	private AccessFuncInfoDao accessfuncinfodao;
	@Autowired
	private ComparasDao comparasdao;
	@Autowired
	private AccessDataDao accessDataDao;


	public Map<String, int[]> findByDate(String date, int type,String showType) {
		List<AccessFuncInfo> list = this.accessfuncinfodao.findByDate(date,showType);
		if(type == 1){
			String hourStr =this.accessfuncinfodao.findMaxDate(date);
			int hour = Integer.valueOf(hourStr==null?"0":hourStr) + 1;
			int[] ipcount = new int[hour];
			int[] pvcount = new int[hour];
			int[] stayTime = new int[hour];
			for (AccessFuncInfo accessFuncInfo : list) {
				int index = Integer.parseInt(accessFuncInfo.getAccesstime());
				ipcount[index] += accessFuncInfo.getIpnum();
				pvcount[index] += accessFuncInfo.getPvnumber();
				stayTime[index] += accessFuncInfo.getStaytime() * accessFuncInfo.getPvnumber();
			}
			for(int i = 0; i < hour; i++){
				if(pvcount[i] != 0){
					stayTime[i] = stayTime[i] / pvcount[i];
				}
			}
			Map<String, int[]> map = new HashMap<String, int[]>();
			map.put("pvcount", pvcount);
			map.put("ipcount", ipcount);
			map.put("stayTime", stayTime);
			return map;
		} else {
			int[] ipcount = new int[24];
			int[] pvcount = new int[24];
			int[] stayTime = new int[24];
			for (AccessFuncInfo accessFuncInfo : list) {
				int index = Integer.parseInt(accessFuncInfo.getAccesstime());
				ipcount[index] += accessFuncInfo.getIpnum();
				pvcount[index] += accessFuncInfo.getPvnumber();
				stayTime[index] += accessFuncInfo.getStaytime() * accessFuncInfo.getPvnumber();
			}
			for(int i = 0; i < 24; i++){
				if(pvcount[i] != 0){
					stayTime[i] = stayTime[i] / pvcount[i];
				}
			}
			Map<String, int[]> map = new HashMap<String, int[]>();
			map.put("pvcount", pvcount);
			map.put("ipcount", ipcount);
			map.put("stayTime", stayTime);
			return map;			
		}
	}

	public List<?> findBetweenDate(String date1, String date2,String sourceType) {
		if (date1.compareTo(date2) > 0) {
			String temp = date1;
			date1 = date2;
			date2 = temp;
		}
		List<?> list = this.accessfuncinfodao.findBetweenDate(date1, date2,sourceType);
		List<AccessData> datalist  = this.accessDataDao.findByDateIPNum(date1, date2);
		Map<String,AccessData> dataMap = new HashMap();
		for(AccessData adata:datalist){
			dataMap.put(adata.getAccessdate().substring(4), adata);
		}
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			Object[] object = (Object[])iterator.next();
			String acdate = object[0].toString();
			if(dataMap.get(acdate)!=null){
				if("center".equals(sourceType)){
					object[2] = dataMap.get(acdate).getCenIpNum();
				}else if("province".equals(sourceType)){
					object[2] = dataMap.get(acdate).getProIpNum();
				}else{
					object[2] = dataMap.get(acdate).getIpnum();
				}
				
			}
			int pvNum = Integer.valueOf(object[1].toString());
			int stayTime = Integer.valueOf(object[3].toString());
			if(pvNum != 0){
				object[3] = stayTime / pvNum;
			}
		}
		return list;
	}
	
	public List<?> findYearBetweenDate(String date1, String date2,String sourceType) {
		if (date1.compareTo(date2) > 0) {
			String temp = date1;
			date1 = date2;
			date2 = temp;
		}
		date1=date1.substring(0,6);
		date2=date2.substring(0,6);
		List<?> datalist  = this.accessDataDao.findByDateIPMonthNum(date1, date2,sourceType);
		return datalist;
	}
	
	public Map<String,Integer> findYearBetweenAll(String date1, String date2,String sourceType,String funId) {
		
		if (date1.compareTo(date2) > 0) {
			String temp = date1;
			date1 = date2;
			date2 = temp;
		}
		date1=date1.substring(0,6);
		date2=date2.substring(0,6);
		Map<String,Integer> map=new HashMap<String,Integer>();
		List<Object[]> datalist=null;
		if(funId==""){
			datalist  = (List<Object[]>)this.accessDataDao.findByDateIPMonthNum(date1, date2,sourceType);
		}else{
			datalist  = (List<Object[]>)this.accessDataDao.findByDateIPMonthNum(date1, date2,sourceType,funId);
		}	
		if(datalist!=null){
			for(Object[] obj:datalist){
				String month=obj[0].toString();
				map.put(month,Integer.parseInt(obj[1].toString()));
			}
		}
		
		return map;
	}
	
public Map<String,Integer> findYearBetweenAllIp(String date1, String date2,String sourceType,String menuId) {
		
		if (date1.compareTo(date2) > 0) {
			String temp = date1;
			date1 = date2;
			date2 = temp;
		}
		date1=date1.substring(0,6);
		date2=date2.substring(0,6);
		Map<String,Integer> map=new HashMap<String,Integer>();
		List<Object[]> datalist =null;
		if(menuId==""){
			System.out.println("menuId1:"+menuId);
			datalist= (List<Object[]>)this.accessDataDao.findByDateIPMonthNum(date1, date2,sourceType);
		}else{
			System.out.println("menuId2:"+menuId);
			datalist= (List<Object[]>)this.accessDataDao.findByDateIPMonthNum(date1, date2,sourceType,menuId);
		}
		for(Object[] obj:datalist){
			String month=obj[0].toString();
			map.put(month,Integer.parseInt(obj[2].toString()));
		}
		return map;
	}
	
	public List<?> findBetweenDate(String date1, String date2, int interval,String showType) {
		if (date1.compareTo(date2) > 0) {
			String temp = date1;
			date1 = date2;
			date2 = temp;
		}
		List<?> list = this.accessfuncinfodao.findBetweenDate(date1, date2,showType);
		int[] ipcount = new int[interval];
		int[] pvcount = new int[interval];
		int[] stayTime = new int[interval];
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			//int index = Integer.parseInt()
		}
		return list;
	}
	/*
	 * public List<AccessFuncInfo> findTopTen(String date) { List<AccessFuncInfo> list = accessfuncinfodao.findTopTen(date); System.out.println(list.get(0).getAccesstime()); return list; }
	 * 
	 * public List<AccessFuncInfo> findTopTen(String date1, String date2) { List<AccessFuncInfo> list = accessfuncinfodao.findTopTen(date1, date2); return null; }
	 */

	public Object getIPTop() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		String date = sdf.format(cal.getTime());
//		Object IPTopTenPrefix = this.comparasdao.getComparasByKey("IPTopTenPrefix");
//		Object IPTopTenSuffix = this.comparasdao.getComparasByKey("IPTopTenSuffix");
		Object IPTopTenPrefix="http://10.20.67.159:9200/logstash-nginx_access_log-";
		//Object IPTopTenPrefix="http://10.20.67.195:9200/logstash-nginx_access_log-";
		Object IPTopTenSuffix="/nginx_access_log/_search";
		String posturl = IPTopTenPrefix + date + IPTopTenSuffix;
		Resty r = new Resty();
		JSONResource data = null;
		String body = "{\"query\": {\"filtered\": {\"query\": {\"query_string\": {\"query\": \"*\",\"analyze_wildcard\": true}},\"filter\": {\"bool\": {\"must\": [{\"query\": {\"query_string\": {\"query\": \"*\",\"analyze_wildcard\": true}}},{\"range\": {\"@timestamp\": {\"gte\": \"now-1h\",\"lte\":\"now\"}}}],\"must_not\": []}}}},\"size\": 0,\"aggs\": {\"data\": {\"terms\": {\"field\": \"remote_ip\",\"size\": 20,\"order\": {\"_count\": \"desc\"}}}}}";
		//String body = "{\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": \"now-1h\",\"lte\": \"now\"}}}],\"must_not\": []}},\"size\": 0,\"aggs\": {\"data\": {\"terms\": {\"field\": \"remote_ip\",\"size\": 20,\"order\": {\"_count\": \"desc\"}}}}}";
		//Content content = new Content("", body.getBytes());
		Content content = Resty.content(body);
		Object aggregations = null;
		try {
			data = r.json(posturl, content);
			JSONObject aggregation = (JSONObject) data.get("aggregations");
			//JSONObject json=(JSONObject) aggregation.get("data");
			System.out.println(aggregation.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aggregations;
	}

	public List<AccessFuncInfo> toppv() {
		List<AccessFuncInfo> pvlist = this.accessfuncinfodao.findtoppv();
		return pvlist;
	}

	private static List<JSONObject> tolist(String str) throws JSONException {
		JSONArray jsonarray = new JSONArray(str);
		List<JSONObject> jsolist = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonarray.length(); i++) {
			JSONObject jso = jsonarray.getJSONObject(i);
			jsolist.add(jso);
		}
		return jsolist;
	}
	
	@Transactional
	public int getStatisticPVNum(int i,String showType) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(cal.getTime());
		int result = 0;
		switch(i){
			case 1:
				result = accessfuncinfodao.getStatisticPVNum(date,showType);
				break;
			case 2:
				cal.add(Calendar.DAY_OF_MONTH, -1);
				String date2 = sdf.format(cal.getTime());
				result = accessfuncinfodao.getStatisticPVNum(date2,showType);
				break;
			case 3:
				cal.add(Calendar.DAY_OF_MONTH, -7);
				String date3 = sdf.format(cal.getTime());
				result = accessfuncinfodao.getStatisticPVNum(date, date3,showType);
				break;
			case 4:
				cal.add(Calendar.DAY_OF_MONTH, -30);
				String date4 = sdf.format(cal.getTime());
				result = accessfuncinfodao.getStatisticPVNum(date, date4,showType);
				break;
			case 5:
				result = accessfuncinfodao.getStatisticPVNum(showType);
				break;
		}
		return result;
	}
	
	
	/*public int getStatisticPVNum(int i) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(cal.getTime());
		int result = 0;
		switch(i){
			case 1:
				result = getPVNum(date);
				break;
			case 2:
				cal.add(Calendar.DAY_OF_MONTH, -1);
				String date2 = sdf.format(cal.getTime());
				result = getPVNum(date2);
				break;
			case 3:
				cal.add(Calendar.DAY_OF_MONTH, -7);
				String date3 = sdf.format(cal.getTime());
				result = getPVNum(date3, date);
				break;
			case 4:
				cal.add(Calendar.DAY_OF_MONTH, -30);
				String date4 = sdf.format(cal.getTime());
				result = getPVNum(date4, date);
				break;
			case 5:
				cal.add(Calendar.DAY_OF_MONTH, -300);
				String date5 = sdf.format(cal.getTime());
				result = getPVNum(date5, date);
				break;
		}
		return result;
	}*/
	
	public int getPVNum(String sdateStr,String... edatestr) {
		String queryUrl = comparasdao.findComparas("logImportFetchUrl").getStringvalue();
		//String queryString = "{\"query\": {\"filtered\": {\"filter\": {\"range\": {\"@timestamp\": {\"gt\": minTime,\"lt\": maxTime}}}}},\"size\": 0,\"from\": 0}";
		String queryString = "{\"query\": {\"range\": {\"@timestamp\": {\"gt\": minTime,\"lt\": maxTime}}},\"size\": 0,\"from\": 0}";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		if(sdf.format(new Date()).equals(sdateStr)){
			int todayPVNum=(Integer) CacheMapUtil.getCache("todayPVNum", 0);
			if(todayPVNum!=0){
				return todayPVNum;
			}
			
		}
		long minTime = 0;
		long maxTime = 0;
		int result = 0;
		try {
			minTime = sdf.parse(sdateStr).getTime();
			if(edatestr.length>0){
				queryUrl=queryUrl.replace("{dateStr}","*");
				maxTime = sdf.parse(edatestr[0]).getTime();
			}else {
				//queryUrl=queryUrl.replace("{dateStr}",sdateStr.replace("-", "."));													
				queryUrl=queryUrl.replace("{dateStr}",sdateStr.replace("-", ".").substring(0, 7));													
				calendar.setTime(sdf.parse(sdateStr));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				maxTime = calendar.getTimeInMillis()-1;	
			}
			queryString = queryString.replace("minTime",String.valueOf(minTime));
			queryString = queryString.replace("maxTime",String.valueOf(maxTime));
			Resty r = new Resty();
			//Content content = new Content("", queryString.getBytes());
			Content content = Resty.content(queryString);
			JSONResource data = r.json(queryUrl, content);
			Object o = data.get("hits.total");
			if (o instanceof Integer) {
				result = Integer.parseInt(o.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	

	public int getPVNumByRange(String sdateStr,String edatestr) {
		String queryUrl = comparasdao.findComparas("logImportFetchUrl").getStringvalue();
		//String queryString = "{\"query\": {\"filtered\": {\"filter\": {\"range\": {\"@timestamp\": {\"gt\": minTime,\"lt\": maxTime}}}}},\"size\": 0,\"from\": 0}";
		String queryString = "{\"query\": {\"range\": {\"@timestamp\": {\"gt\": minTime,\"lt\": maxTime}}},\"size\": 0,\"from\": 0}";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		Calendar calendar = Calendar.getInstance();
		if(sdf.format(new Date()).equals(sdateStr)){
			int todayPVNum=(Integer) CacheMapUtil.getCache("todayPVNum", 0);
			if(todayPVNum!=0){
				return todayPVNum;
			}
			
		}
		long minTime = 0;
		long maxTime = 0;
		int result = 0;
		try {
			minTime = sdf.parse(sdateStr).getTime();
				queryUrl=queryUrl.replace("{dateStr}","*");
				maxTime = sdf.parse(edatestr).getTime();
			queryString = queryString.replace("minTime",String.valueOf(minTime));
			queryString = queryString.replace("maxTime",String.valueOf(maxTime));
			Resty r = new Resty();
			//Content content = new Content("", queryString.getBytes());
			Content content = Resty.content(queryString);
			JSONResource data = r.json(queryUrl, content);
			Object o = data.get("hits.total");
			if (o instanceof Integer) {
				result = Integer.parseInt(o.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public List<Map<String,String>> getList(String sdate,String edate){
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHH");
		long stime=0;
		long etime=0;
		try {
			stime=sdf.parse(sdate).getTime();
			etime=sdf.parse(edate).getTime();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String queryUrl = comparasdao.findComparas("accessipNum").getStringvalue();
		//String queryString = "{\"size\":0,\"aggs\":{\"3\":{\"date_histogram\":{\"field\":\"@timestamp\",\"interval\":\"1h\",\"pre_zone\":\"+08:00\",\"pre_zone_adjust_large_interval\":true,\"min_doc_count\":1,\"extended_bounds\":{\"min\":"+stime+",\"max\":"+etime+"}},\"aggs\":{\"2\":{\"cardinality\":{\"field\":\"remote_ip\"}}}}},\"query\":{\"filtered\":{\"query\":{\"query_string\":{\"analyze_wildcard\":true,\"query\":\"*\"}},\"filter\":{\"bool\":{\"must\":[{\"query\":{\"query_string\":{\"query\":\"*\",\"analyze_wildcard\":true}}},{\"range\":{\"@timestamp\":{\"gte\":"+stime+",\"lte\":"+etime+"}}}],\"must_not\":[]}}}},\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}},\"fragment_size\":2147483647}}";
		String queryString = "{\"size\": 0,\"aggs\": {\"3\": {\"date_histogram\": {\"field\": \"@timestamp\",\"interval\": \"1h\",\"min_doc_count\": 1,\"extended_bounds\": {\"min\": "+stime+",\"max\": "+etime+"}},\"aggs\": {\"2\": {\"cardinality\": {\"field\": \"remote_ip\"}}}}},\"query\": {\"bool\": {\"must\": [{\"range\": {\"@timestamp\": {\"gte\": "+stime+",\"lte\": "+etime+"}}}],\"must_not\": []}}}";
		//Content content = new Content("", queryString.getBytes());
		Content content = Resty.content(queryString);
		Resty r = new Resty();
		try {
			JSONResource data = r.json(queryUrl, content);
			JSONObject aggregations = (JSONObject) data.get("aggregations");
			JSONObject three=aggregations.getJSONObject("3");
			JSONArray buckets=three.getJSONArray("buckets");
			//System.out.println(buckets.toString());
			Date date=new Date();
			for (int i = 0; i < buckets.length(); i++) {
				Map<String,String> map=new HashMap<String, String>();
				JSONObject json = buckets.getJSONObject(i);
				JSONObject valueJson=(JSONObject) json.get("2");
				String value=valueJson.getString("value");
				Long keyLong=Long.valueOf(json.getString("key"));
				date.setTime(keyLong);
				String key=sdf.format(date);
				String accessDate_str=key.substring(0, 8);
				String accessTime_str=key.substring(8);
				String doc_count=json.getString("doc_count");
				map.put("accessdate", accessDate_str);
				map.put("accesstime", accessTime_str);
				map.put("pvnumber", doc_count);
				map.put("ipnum", value);
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	public static void main(String[] args) {
		AccessFuncInfoService af=new AccessFuncInfoService();
		af.getIPTop();
	}
}
