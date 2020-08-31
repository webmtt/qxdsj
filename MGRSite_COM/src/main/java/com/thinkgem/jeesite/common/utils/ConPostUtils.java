package com.thinkgem.jeesite.common.utils;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConPostUtils {
  /**
   * @param url 请求接口的url
   * @param formparams 传递的参数
   * @param pageSize 返回的分页
   * @param flag true代表分页返回的数据 false 代表所有的数据（是否所有）
   * @return
   */
  public static Map<String, Object> sendPostGetParasAll(
      String url, List<NameValuePair> formparams, int pageSize) {
    Map<String, Object> map = new HashMap<String, Object>();
    List listAll = new ArrayList();
    // 是否查询出所有数据
    List<NameValuePair> formparamstotal = new ArrayList<NameValuePair>();
    for (int j = 0; j < formparams.size(); j++) {
      formparamstotal.add(formparams.get(j));
    }
    formparamstotal.add(new BasicNameValuePair("pageNum", 1 + ""));
    formparamstotal.add(new BasicNameValuePair("pageSize", pageSize + ""));
    String json = readContentFromHttpGet(url, formparamstotal);
    JSONObject jsonObject = JSONObject.fromObject(json);
    String rowCount = jsonObject.getString("rowCount");
    // 小于给的页数
    if (Integer.valueOf(rowCount) <= pageSize) {
      List list = new ArrayList();
      list = jsonObject.getJSONArray("DS");
      listAll.addAll(list);
      map.put("rowCount", rowCount);
      map.put("list", listAll);
      return map;
    } else {
      map.put("rowCount", rowCount);
      for (int i = 1; i <= Integer.valueOf(rowCount) / pageSize + 1; i++) {
        List<NameValuePair> formparamstemp = new ArrayList<NameValuePair>();
        for (int j = 0; j < formparams.size(); j++) {
          formparamstemp.add(formparams.get(j));
        }
        formparamstemp.add(new BasicNameValuePair("pageNum", i + ""));
        formparamstemp.add(new BasicNameValuePair("pageSize", pageSize + ""));
        String jsontemp = readContentFromHttpGet(url, formparamstemp);
        JSONObject jsonObjecttemp = JSONObject.fromObject(jsontemp);
        List list = new ArrayList();
        list = jsonObjecttemp.getJSONArray("DS");
        listAll.addAll(list);
      }
      map.put("list", listAll);
      return map;
    }
  }
  /**
   * 分页查询
   *
   * @param url
   * @param formparams
   * @param pageSize
   * @param pageNum
   * @return
   */
  public static String sendPostGetParasPage(
      String url, List<NameValuePair> formparams, int pageSize, int pageNum) {
    // 是否查询出所有数据
    List<NameValuePair> formparamstemp = new ArrayList<NameValuePair>();
    for (int i = 0; i < formparams.size(); i++) {
      formparamstemp.add(formparams.get(i));
    }
    formparamstemp.add(new BasicNameValuePair("pageNum", pageNum + ""));
    formparamstemp.add(new BasicNameValuePair("pageSize", pageSize + ""));
    String json = readContentFromHttpGet(url, formparamstemp);
    return json;
  }
  /**
   * 分页查询
   *
   * @param url
   * @param formparams
   * @param pageSize
   * @param pageNum
   * @return
   */
  public static Map<String, Object> sendPostGetParasPage(
      String url, List<NameValuePair> formparams) {
    // 是否查询出所有数据
    List<NameValuePair> formparamstemp = new ArrayList<NameValuePair>();
    for (int i = 0; i < formparams.size(); i++) {
      formparamstemp.add(formparams.get(i));
    }
    String json = readContentFromHttpGet(url, formparamstemp);
    JSONObject jsonObject = JSONObject.fromObject(json);
    String rowCount = jsonObject.getString("rowCount");
    List list = new ArrayList();
    Map<String, Object> map = new HashMap<String, Object>();
    list = jsonObject.getJSONArray("DS");
    map.put("rowCount", rowCount);
    map.put("list", list);
    return map;
  }
  /**
   * 分页查询
   *
   * @param url
   * @param formparams
   * @param pageSize
   * @param pageNum
   * @return
   */
  public static Map<String, Object> sendPostGetParasPage2(
      String url, List<NameValuePair> formparams) {
    // 是否查询出所有数据
    List<NameValuePair> formparamstemp = new ArrayList<NameValuePair>();
    for (int i = 0; i < formparams.size(); i++) {
      formparamstemp.add(formparams.get(i));
    }
    String json = readContentFromHttpGet(url, formparamstemp);
    JSONObject jsonObject = JSONObject.fromObject(json);
    String rowCount = jsonObject.getString("rowCount");
    Map<String, Object> map = new HashMap<String, Object>();
    if (Integer.valueOf(rowCount) == 0) {
      List list = new ArrayList();
      // list = jsonObject.getJSONArray("DS");
      map.put("rowCount", rowCount);
      map.put("list", list);
    } else {
      List list = new ArrayList();
      list = jsonObject.getJSONArray("DS");
      map.put("rowCount", rowCount);
      map.put("list", list);
    }
    return map;
  }
  /**
   * 直接调用的方法 （接口返回的字符串）
   *
   * @param url
   * @param formparams
   * @return
   */
  public static String readContentFromHttpGet(String url, List<NameValuePair> formparams) {
    String result = "";
    CloseableHttpClient httpclient = HttpClients.createDefault();
    // 创建httppost
    HttpPost httppost = new HttpPost(url);
    // 创建参数队列
    UrlEncodedFormEntity uefEntity;
    try {
      uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
      httppost.setEntity(uefEntity);
      CloseableHttpResponse response = httpclient.execute(httppost);
      try {
        HttpEntity entity = response.getEntity();
        result = EntityUtils.toString(entity, "UTF-8");
      } finally {
        response.close();
      }
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // 关闭连接,释放资源
      try {
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return result;
  }

  public static void main(String[] args) {
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    formparams.add(new BasicNameValuePair("timeRange", "[20160328000000,20160419000000]"));
    formparams.add(new BasicNameValuePair("dataCode", "WARNING"));
    String url =
        "http://localhost:8080/idata-search/api?userId=user_nordb&pwd=user_nordb_pwd1&dataFormat=json&interfaceId=getSurfEleByTimeRangeAndStaID";
    Map<String, Object> map = sendPostGetParasAll(url, formparams, 1000);
    String rowCount = (String) map.get("rowCount");
    List list = (List) map.get("list");
    System.out.println("rowCount:" + rowCount + "list:" + list.size());
  }
}
