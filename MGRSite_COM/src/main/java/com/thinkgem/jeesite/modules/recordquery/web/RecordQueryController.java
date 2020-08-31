package com.thinkgem.jeesite.modules.recordquery.web;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Content;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.access.Service.AccessFunConfigService;
import com.thinkgem.jeesite.modules.access.entity.AccessFunConfig;
import com.thinkgem.jeesite.modules.recordquery.entity.HostIp;
import com.thinkgem.jeesite.modules.recordquery.service.HostIpService;
import com.thinkgem.jeesite.modules.sys.entity.Comparas;
import com.thinkgem.jeesite.modules.sys.service.ComparasService;

@Controller
@RequestMapping(value = "recordquery/")
public class RecordQueryController extends BaseController {
	@Autowired
	private ComparasService comparasService;
	@Autowired
	private AccessFunConfigService accessFunConfigService;
    @Autowired
    private HostIpService hostIpService;
	@RequestMapping(value = "index")
	public String recordQuery(HttpServletRequest request,
			HttpServletResponse response, Model model, 
			String ipString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
		Calendar calendar=Calendar.getInstance();	
		Date lastDate=new Date();
		calendar.setTime(lastDate);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		model.addAttribute("beginDate", sdf.format(calendar.getTime()));
		model.addAttribute("lastDate", sdf.format(lastDate));
		model.addAttribute("recordqueryUrl", ipString);
		return "modules/recordquery/recordquery";
	}

	@RequestMapping(value = "index1")
	public String recordQuery1(HttpServletRequest request,
			HttpServletResponse response, Model model
			) {
		String beginDate=request.getParameter("startTime");
		String lastDate=request.getParameter("endTime");
		String ipString=request.getParameter("ipString");
		model.addAttribute("beginDate",beginDate);
		model.addAttribute("lastDate",lastDate);
		model.addAttribute("ipString", ipString);
		return "modules/recordquery/recordquery";
	}
	

	@RequestMapping(value = "getJson")
	public void RecordQuery(HttpServletRequest request,
			HttpServletResponse response, Model model, 
			String ipString,String 	startTime,String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar =Calendar.getInstance();
		Comparas comparas = comparasService.findComparas("recordquery");
		int intvalue=comparas.getIntvalue();
		String queryDate = endTime.replace("-", ".");
		String urlstr = "";
		List<AccessFunConfig> records =new ArrayList<AccessFunConfig>();
		
		JSONObject jsonObject;
		int count=0;
		int getcount=0; 
		int j=0;
		int m=1;
		Date date=new Date();
		for(int i=0;records.size()<intvalue && m>-30;i++){
			try {
				if(j==0){
				    date = sdf.parse(queryDate);
				    calendar.setTime(date);
					calendar.add(Calendar.DAY_OF_MONTH, m);			
					date=calendar.getTime();
					urlstr = comparas.getStringvalue();
					//urlstr = urlstr.replace("yyyy.MM.dd", sdf.format(date));
					urlstr = urlstr.replace("yyyy.MM.dd", sdf.format(date).substring(0, 7));
				    m--;
				    count=0;
				}
				long queryTime=calendar.getTimeInMillis();
				calendar.setTime(sdfnew.parse(startTime));
				calendar.add(Calendar.DAY_OF_MONTH, -1);	
				long beginTime=calendar.getTimeInMillis();            
                if(queryTime<beginTime){
                	break;
                }
                //此处jsonstr不用因为es更新而修改
                String jsonstr ="";
				if(j==0){
					jsonstr = "{\"query\": {\"match\": {\"remote_ip\": \""
							+ ipString.trim() + "\"}},"
							+ "\"sort\": {\"@timestamp\": {\"order\": \"desc\"}},\"size\":"
							+ (intvalue-records.size()) + ",\"from\":" + j + "}";
				}else{
                    jsonstr = "{\"query\": {\"match\": {\"remote_ip\": \""
						+ ipString.trim() + "\"}},"
						+ "\"sort\": {\"@timestamp\": {\"order\": \"desc\"}},\"size\":"
						+ (getcount-count) + ",\"from\":" + j + "}";
				}
                
			jsonObject = new JSONObject("{}");
			CloseableHttpClient httpclient = HttpClients.createDefault(); // 创建默认的httpClient实例
			HttpPost httpPost = new HttpPost(urlstr); // 创建httpPost对象
			StringEntity se = new StringEntity(jsonstr, "UTF-8"); // 设置请求的字符串及编码格式
			httpPost.setEntity(se); // 设置httpPost发送Post请求的实体
			CloseableHttpResponse httpResponse = httpclient.execute(httpPost); // 发送http请求
			HttpEntity entity = httpResponse.getEntity(); // 得到返回的数据
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			
			if (statusCode != 200) {
				j=0;
				continue;
			}
	
			/*
			 * 将得到的数据转换为字符串
			 */
			BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String line;
			String result = "";
			
			while ((line = in.readLine()) != null) {
				result += line;
			}
	
			
				jsonObject = new JSONObject(result);
				if(jsonObject.getString("hits")==null || new JSONObject(jsonObject.getString("hits")).getString("total")==null || new JSONObject(jsonObject.getString("hits")).getInt("total")==0){
					j=0;
					continue;
				}
				List<AccessFunConfig> list =parseJSONObject(jsonObject,startTime,endTime);
				count+=count(jsonObject);
				
				records.addAll(list);
				
				j++;
				getcount=new JSONObject(jsonObject.getString("hits")).getInt("total");
				if(getcount==count){
					j=0;
					continue;
				}
				
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
		}
		renderText(JsonMapper.toJsonString(records), response);
	}
	
	@RequestMapping(value = "account")
	public String recordAccount(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
		Calendar calendar=Calendar.getInstance();	
		Date lastDate=new Date();
		calendar.setTime(lastDate);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		model.addAttribute("beginDate", sdf.format(calendar.getTime()));
		model.addAttribute("lastDate", sdf.format(lastDate));
		return "modules/recordquery/recordAccount";
	}
	
	@RequestMapping(value = "getAccount")
	public void getRecordAccount(HttpServletRequest request,
			HttpServletResponse response, Model model, String startTime,String endTime) {
		List<HostIp> hostIp=hostIpService.findAll();
		List<List<Object>> resultList=new ArrayList<List<Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar =Calendar.getInstance();
		Comparas comparas = comparasService.findComparas("recordquery");
		String queryDate = endTime.replace("-", ".");
		if(hostIp.size()>0){
		for(int k=0;k<hostIp.size();k++){
		String urlstr = "";
		JSONObject jsonObject;
		int count=0;
		int getcount=0; 
		int number=0;
		int j=0;
		int m=1;
		Date date=new Date();
		String jsonstr = "";
		for(int i=0;m>-30;i++){
			try {
				if(j==0){
				    date = sdf.parse(queryDate);
				    calendar.setTime(date);
					calendar.add(Calendar.DAY_OF_MONTH, m);			
					date=calendar.getTime();
					urlstr = comparas.getStringvalue();
					//urlstr = urlstr.replace("yyyy.MM.dd", sdf.format(date));
					urlstr = urlstr.replace("yyyy.MM.dd", sdf.format(date).substring(0, 7));
				    m--;
				    count=0;
				}
				long queryTime=calendar.getTimeInMillis();
				calendar.setTime(sdfnew.parse(startTime));
				calendar.add(Calendar.DAY_OF_MONTH, -1);	
				long beginTime=calendar.getTimeInMillis();            
                if(queryTime<beginTime){
                	break;
                }
                if(getcount!=0){
                	 jsonstr = "{\"query\": {\"match\": {\"remote_ip\": \""
							+ hostIp.get(k).getIp().trim() + "\"}},"
							+ "\"sort\": {\"@timestamp\": {\"order\": \"desc\"}},\"size\":"
							+ (getcount-count) + ",\"from\":" + j + "}";
                }else{
				 jsonstr = "{\"query\": {\"match\": {\"remote_ip\": \""
							+ hostIp.get(k).getIp().trim() + "\"}},"
							+ "\"sort\": {\"@timestamp\": {\"order\": \"desc\"}},\"size\":"
							+ 100 + ",\"from\":" + j + "}";
                }
			jsonObject = new JSONObject("{}");
			CloseableHttpClient httpclient = HttpClients.createDefault(); // 创建默认的httpClient实例
			HttpPost httpPost = new HttpPost(urlstr); // 创建httpPost对象
			StringEntity se = new StringEntity(jsonstr, "UTF-8"); // 设置请求的字符串及编码格式
			httpPost.setEntity(se); // 设置httpPost发送Post请求的实体
			CloseableHttpResponse httpResponse = httpclient.execute(httpPost); // 发送http请求
			HttpEntity entity = httpResponse.getEntity(); // 得到返回的数据
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			
			if (statusCode != 200) {
				j=0;
				continue;
			}
	
			/*
			 * 将得到的数据转换为字符串
			 */
			BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String line;
			String result = "";
			
			while ((line = in.readLine()) != null) {
				result += line;
			}
				jsonObject = new JSONObject(result);
				if(jsonObject.getString("hits")==null || new JSONObject(jsonObject.getString("hits")).getString("total")==null || new JSONObject(jsonObject.getString("hits")).getInt("total")==0){
					j=0;
					continue;
				}
				int numb =parseJSONObject1(jsonObject,startTime,endTime);
				count+=count(jsonObject);
	 			number+=numb;	  				
				j++;
				getcount=new JSONObject(jsonObject.getString("hits")).getInt("total");
				if(getcount==count){
					j=0;
					continue;
				}
				
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
		}
		List<Object> result=new ArrayList<Object>();
		result.add(hostIp.get(k).getIp());
		result.add(hostIp.get(k).getName());
		result.add(number);
		resultList.add(result);
		}
		}
		//System.out.println(resultList);
		renderText(JsonMapper.toJsonString(resultList), response);
	}
		
	
	
	@RequestMapping(value = "count")
	public String recordCount(HttpServletRequest request,
			HttpServletResponse response, Model model, 
			String ipString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
		Calendar calendar=Calendar.getInstance();	
		Date lastDate=new Date();
		calendar.setTime(lastDate);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		model.addAttribute("beginDate", sdf.format(calendar.getTime()));
		model.addAttribute("lastDate", sdf.format(lastDate));
		model.addAttribute("recordqueryUrl", ipString);
		return "modules/recordquery/recordCount";
	}

	@RequestMapping(value = "count1")
	public String recordCount1(HttpServletRequest request,
			HttpServletResponse response, Model model
			) {
		String beginDate=request.getParameter("startTime");
		String lastDate=request.getParameter("endTime");
		String ipString=request.getParameter("ipString");
		model.addAttribute("beginDate",beginDate);
		model.addAttribute("lastDate",lastDate);
		model.addAttribute("ipString", ipString);
		return "modules/recordquery/recordCount";
	}
	

	@RequestMapping(value = "getCount")
	public void getRecordCount(HttpServletRequest request,
			HttpServletResponse response, Model model, 
			String ipString,String 	startTime,String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar =Calendar.getInstance();
		Comparas comparas = comparasService.findComparas("recordquery");
		int intvalue=comparas.getIntvalue();
		String queryDate = endTime.replace("-", ".");
		String urlstr = "";
		Map<String,Map<String,Integer>> pmap=new HashMap<String, Map<String,Integer>>();
		JSONObject jsonObject;
		int count=0;
		int getcount=0; 
		int j=0;
		int m=1;
		Date date=new Date();
		String jsonstr = "";
		for(int i=0;m>-30;i++){
			try {
				if(j==0){
				    date = sdf.parse(queryDate);
				    calendar.setTime(date);
					calendar.add(Calendar.DAY_OF_MONTH, m);			
					date=calendar.getTime();
					urlstr = comparas.getStringvalue();
					//urlstr = urlstr.replace("yyyy.MM.dd", sdf.format(date));
					urlstr = urlstr.replace("yyyy.MM.dd", sdf.format(date).substring(0, 7));
				    m--;
				    count=0;
				}
				long queryTime=calendar.getTimeInMillis();
				calendar.setTime(sdfnew.parse(startTime));
				calendar.add(Calendar.DAY_OF_MONTH, -1);	
				long beginTime=calendar.getTimeInMillis();            
                if(queryTime<beginTime){
                	break;
                }
				
                
                if(getcount!=0){
                	 jsonstr = "{\"query\": {\"match\": {\"remote_ip\": \""
							+ ipString.trim() + "\"}},"
							+ "\"sort\": {\"@timestamp\": {\"order\": \"desc\"}},\"size\":"
							+ (getcount-count) + ",\"from\":" + j + "}";
                }else{
				 jsonstr = "{\"query\": {\"match\": {\"remote_ip\": \""
							+ ipString.trim() + "\"}},"
							+ "\"sort\": {\"@timestamp\": {\"order\": \"desc\"}},\"size\":"
							+ 100 + ",\"from\":" + j + "}";
                }
			jsonObject = new JSONObject("{}");
			CloseableHttpClient httpclient = HttpClients.createDefault(); // 创建默认的httpClient实例
			HttpPost httpPost = new HttpPost(urlstr); // 创建httpPost对象
			StringEntity se = new StringEntity(jsonstr, "UTF-8"); // 设置请求的字符串及编码格式
			httpPost.setEntity(se); // 设置httpPost发送Post请求的实体
			CloseableHttpResponse httpResponse = httpclient.execute(httpPost); // 发送http请求
			HttpEntity entity = httpResponse.getEntity(); // 得到返回的数据
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			
			if (statusCode != 200) {
				j=0;
				continue;
			}
	
			/*
			 * 将得到的数据转换为字符串
			 */
			BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String line;
			String result = "";
			
			while ((line = in.readLine()) != null) {
				result += line;
			}
	
			
				jsonObject = new JSONObject(result);
				if(jsonObject.getString("hits")==null || new JSONObject(jsonObject.getString("hits")).getString("total")==null || new JSONObject(jsonObject.getString("hits")).getInt("total")==0){
					j=0;
					continue;
				}
				Map<String, Map<String, Integer>> pmapnew =formatJSONObject(jsonObject,startTime,endTime);
				count+=count(jsonObject);
				if(pmap==null){
					pmap.putAll(pmapnew);
				} 
				for (Entry<String, Map<String, Integer>> pentrynew : pmapnew.entrySet()) { 
					if(pmap.containsKey(pentrynew.getKey())){
						Map<String,Integer> smapnew=pentrynew.getValue();
						Map<String,Integer> smap=pmap.get(pentrynew.getKey());
						for (Entry<String, Integer> sentrynew : smapnew.entrySet()) { 
								if(smap.containsKey(sentrynew.getKey())){
								  smap.put(sentrynew.getKey(),smap.get(sentrynew.getKey())+sentrynew.getValue());
								}else{
								  smap.put(sentrynew.getKey(),sentrynew.getValue());
								}
						}
						pmap.put(pentrynew.getKey(), smap);
					}else{
						pmap.put(pentrynew.getKey(), pentrynew.getValue());
					}
												  					  
				}  				  				
				j++;
				getcount=new JSONObject(jsonObject.getString("hits")).getInt("total");
				if(getcount==count){
					j=0;
					continue;
				}
			
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
		}
		Map<String,Map<String,Integer>> resultMap=new HashMap<String, Map<String,Integer>>();
		
		for (Entry<String, Map<String, Integer>> pentry : pmap.entrySet()) { 
			AccessFunConfig accessFunConfig;
			Map<String,Integer> smap=pentry.getValue();
			Map<String,Integer> sresultMap=new HashMap<String, Integer>();
			for (Entry<String, Integer> sentry : smap.entrySet()) { 
				accessFunConfig=accessFunConfigService.findAccessFunConfigByID(sentry.getKey());
				sresultMap.put(accessFunConfig.getFuncItemName(), sentry.getValue());
				
			}
			accessFunConfig=accessFunConfigService.findAccessFunConfigByID(pentry.getKey());
			resultMap.put(accessFunConfig.getFuncItemName(), sresultMap);
			
		}
		renderText(JsonMapper.toJsonString(resultMap), response);
	}
	
	
	private Map<String,Map<String,Integer>> formatJSONObject(JSONObject data,String startTime,String endTime) {
		Map<String,Map<String,Integer>> pmap=new HashMap<String, Map<String,Integer>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdfnew.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		Calendar calendar0=Calendar.getInstance();
	
		try {
			calendar0.setTime(sdf.parse(startTime));
			long beginTime=calendar0.getTimeInMillis();
			calendar0.setTime(sdf.parse(endTime+" 23:59:59"));
			long lastTime=calendar0.getTimeInMillis();
			JSONObject jsonObject = new JSONObject(data.getString("hits").toString());
			Iterator iterator1 = jsonObject.keys();
			while (iterator1.hasNext()) {

				String key1 = (String) iterator1.next();
				if (key1.equals("hits")) {
					String value1 = jsonObject.getString(key1).replace("[", "")
							.replace("]", "").replace("},{", "}#%&{");
					String arr[] = value1.split("#%&");
				
					for (int i = 0; i < arr.length; i++) {
						if(!arr[i].startsWith("{")){
							return pmap;
						}
						JSONObject objectnew = new JSONObject(arr[i]);
						Iterator iterator2 = objectnew.keys();
						String key2;
						String value;
						while (iterator2.hasNext()) {
							key2 = (String) iterator2.next();
							if (key2.equals("_source")) {
								String pfuncid = null;
								String urlid = null;
								value = objectnew.getString(key2);
								JSONObject object = new JSONObject(value);
								Iterator iterator3 = object.keys();								
								Map<String,Integer> smap=new HashMap<String, Integer>();
								while (iterator3.hasNext()) {
									
									String keynew = (String) iterator3.next();
									if (keynew.equals("@timestamp")
											|| keynew.equals("")) {
										String valuenew = object
												.getString(keynew)
												.replace("T", " ")
												.substring(0, 18);
										long recordTime=sdfnew.parse(valuenew).getTime();
										if(recordTime<beginTime || recordTime>lastTime){
											break;											   
										}									
									}
									
									if(keynew.equals("urlid")){
										urlid=object.getString(keynew);
										if(urlid==null || urlid.length()==0){
											break;
										}
										if(urlid.length()>=3){
											urlid=urlid.substring(0, 3);
										}				
										pfuncid=urlid.substring(0, 1);
									}
									
									
								}
								if(pfuncid!=null && urlid!=null){
									if(pmap==null || !pmap.containsKey(pfuncid)){
										smap.put(urlid, 1);										
									}else{
										smap=pmap.get(pfuncid);
										if(smap.containsKey(urlid)){
											smap.put(urlid, smap.get(urlid)+1);
										}else{
											smap.put(urlid,1);
										}
									}
									pmap.put(pfuncid, smap);	
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pmap;
	}
	private List<AccessFunConfig> parseJSONObject(JSONObject data,String startTime,String endTime) {
		List<AccessFunConfig> list = new ArrayList<AccessFunConfig>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdfnew.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		Calendar calendar0=Calendar.getInstance();
	
		try {
			calendar0.setTime(sdf.parse(startTime));
			long beginTime=calendar0.getTimeInMillis();
			calendar0.setTime(sdf.parse(endTime+" 23:59:59"));
			long lastTime=calendar0.getTimeInMillis();
			JSONObject jsonObject = new JSONObject(data.getString("hits").toString());
			Iterator iterator1 = jsonObject.keys();
			while (iterator1.hasNext()) {

				String key1 = (String) iterator1.next();
				if (key1.equals("hits")) {
					String value1 = jsonObject.getString(key1).replace("[", "")
							.replace("]", "").replace("},{", "}#%&{");
					String arr[] = value1.split("#%&");
				
					for (int i = 0; i < arr.length; i++) {
						if(!arr[i].startsWith("{")){
							return list;
						}
						JSONObject objectnew = new JSONObject(arr[i]);
						Iterator iterator2 = objectnew.keys();
						String key2;
						String value;
						while (iterator2.hasNext()) {
							key2 = (String) iterator2.next();
							if (key2.equals("_source")) {
								value = objectnew.getString(key2);
								JSONObject object = new JSONObject(value);
								AccessFunConfig accessFunConfig = new AccessFunConfig();
								Iterator iterator3 = object.keys();
								while (iterator3.hasNext()) {
									
									String keynew = (String) iterator3.next();
									if (keynew.equals("@timestamp")
											|| keynew.equals("")) {
										String valuenew = object
												.getString(keynew)
												.replace("T", " ")
												.substring(0, 18);
										long recordTime=sdfnew.parse(valuenew).getTime();
										if(recordTime<beginTime || recordTime>lastTime){
											break;											   
										}
										accessFunConfig
												.setRecordTime(sdf.format(sdfnew
														.parse(valuenew)));
										
										
									}
									if(keynew.equals("uripath")){
										accessFunConfig.setUrl(object.getString(keynew));
									}
									//accessFunConfig没有浏览器字段，暂用FuncItemId代替
									if(keynew.equals("user_agent")){
										String userAgent=object.getString(keynew);
										if(userAgent.contains("MSIE")){
											accessFunConfig.setFuncItemId("IE");
										}else if(userAgent.contains("Firefox/")&&userAgent.contains("Gecko/")){
											accessFunConfig.setFuncItemId("Firefox");
										}else if(userAgent.contains("Chrome/")){
											accessFunConfig.setFuncItemId("Chrome");
										}else if(userAgent.contains("Safari/")&&!userAgent.contains("Chrome/")){
											accessFunConfig.setFuncItemId("Safari");
										}else if(userAgent.contains("Gecko")&&userAgent.contains("like")&&userAgent.contains("rv")){
											accessFunConfig.setFuncItemId("IE");
										}else{
											accessFunConfig.setFuncItemId("其他");
										}
									}
									if(keynew.equals("urlid")){
										String urlid=object.getString(keynew);
										if(urlid==null || urlid.length()==0){
											break;
										}
										if(urlid.length()>=3){
											urlid=urlid.substring(0, 3);
										}
										AccessFunConfig accessFunConfig2=accessFunConfigService.findAccessFunConfigByID(urlid);
										accessFunConfig.setFuncItemName(accessFunConfig2.getFuncItemName());
									
										accessFunConfig2=accessFunConfigService.findAccessFunConfigByID(urlid.substring(0, 1));
										accessFunConfig.setParentFuncItemName(accessFunConfig2.getFuncItemName());
									}
									
								}
								if(accessFunConfig.getRecordTime()!=null){
								   list.add(accessFunConfig);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	private int count(JSONObject data) {
		int count=0;	
		try {

			JSONObject jsonObject = new JSONObject(data.getString("hits").toString());
			Iterator iterator1 = jsonObject.keys();
			while (iterator1.hasNext()) {

				String key1 = (String) iterator1.next();
				if (key1.equals("hits")) {
					String value1 = jsonObject.getString(key1).replace("[", "")
							.replace("]", "").replace("},{", "}#%&{");
					String arr[] = value1.split("#%&");
				
					for (int i = 0; i < arr.length; i++) {
						if(!arr[i].startsWith("{")){
							return count;
						}
						JSONObject objectnew = new JSONObject(arr[i]);
						Iterator iterator2 = objectnew.keys();
						String key2;
						String value;
						while (iterator2.hasNext()) {
							key2 = (String) iterator2.next();
							if (key2.equals("_source")) {
								count++;
								
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	private int parseJSONObject1(JSONObject data,String startTime,String endTime) {
		int num=0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdfnew.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		Calendar calendar0=Calendar.getInstance();
	
		try {
			calendar0.setTime(sdf.parse(startTime));
			long beginTime=calendar0.getTimeInMillis();
			calendar0.setTime(sdf.parse(endTime+" 23:59:59"));
			long lastTime=calendar0.getTimeInMillis();
			JSONObject jsonObject = new JSONObject(data.getString("hits").toString());
			Iterator iterator1 = jsonObject.keys();
			while (iterator1.hasNext()) {

				String key1 = (String) iterator1.next();
				if (key1.equals("hits")) {
					String value1 = jsonObject.getString(key1).replace("[", "")
							.replace("]", "").replace("},{", "}#%&{");
					String arr[] = value1.split("#%&");
				
					for (int i = 0; i < arr.length; i++) {
						if(!arr[i].startsWith("{")){
							return num;
						}
						JSONObject objectnew = new JSONObject(arr[i]);
						Iterator iterator2 = objectnew.keys();
						String key2;
						String value;
						while (iterator2.hasNext()) {
							key2 = (String) iterator2.next();
							if (key2.equals("_source")) {
								
								value = objectnew.getString(key2);
								JSONObject object = new JSONObject(value);
							
								Iterator iterator3 = object.keys();
								while (iterator3.hasNext()) {
									
									String keynew = (String) iterator3.next();
									if (keynew.equals("@timestamp")
											|| keynew.equals("")) {
										String valuenew = object
												.getString(keynew)
												.replace("T", " ")
												.substring(0, 18);
										long recordTime=sdfnew.parse(valuenew).getTime();
										if(recordTime<beginTime || recordTime>lastTime){
											break;											   
										}
										num+=1;
									}
									
									
									
								}
								
								}
							}
						}
					}
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

}
