package com.thinkgem.jeesite.mybatis.modules.arithmetic.util;

import java.net.URI;
import java.util.*;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 *
 * 类: HttpClient <br>
 * 描述: httpclient工具类 <br>
 * 作者: song<br>
 * 时间: 2017年7月21日 下午3:15:27
 */
public class HttpClientUtil {

    static CloseableHttpClient client = null;
    static {
        client = HttpClients.createDefault();
    }

    /**
     *
     * 方法: get <br>
     * 描述: get请求 <br>
     * 作者: Teacher song<br>
     * 时间: 2017年7月21日 下午3:15:25
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String get(String url,HashMap<String, Object> params){
        try {
            HttpGet httpGet = new HttpGet();
            Set<String> keySet = params.keySet();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(url).append("?t=").append(System.currentTimeMillis());
            for (String key : keySet) {
                stringBuffer.append("&").append(key).append("=").append(params.get(key));
            }
            httpGet.setURI(new URI(stringBuffer.toString()));
            CloseableHttpResponse execute = client.execute(httpGet);
            int statusCode = execute.getStatusLine().getStatusCode();
            if (200 != statusCode) {
                return "500";
            }
            return EntityUtils.toString(execute.getEntity(), "utf-8");
        }catch (Exception e) {
            e.printStackTrace();
            return "500";
        }
    }
    /**
     *
     * 方法: post <br>
     * 描述: post请求 <br>
     * 作者: Teacher song<br>
     * 时间: 2017年7月21日 下午3:20:31
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String post(String url,HashMap<String, Object> params) {
        try {
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(new URI(url));
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                NameValuePair e = new BasicNameValuePair(key, params.get(key).toString());
                parameters.add(e);
            }
            HttpEntity entity = new UrlEncodedFormEntity(parameters , "utf-8");
            httpPost.setEntity(entity );
            CloseableHttpResponse execute = client.execute(httpPost);
            int statusCode = execute.getStatusLine().getStatusCode();
            if (200 != statusCode) {
                return "500";
            }
            return EntityUtils.toString(execute.getEntity(), "utf-8");
        }catch (Exception e) {
            e.printStackTrace();
            return "500";
        }
    }

    public static void main(String[] args) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        String result = HttpClientUtil.post("http://10.62.89.55/cimiss-web/api?userId=BEHT_FLZX_lxd8135&pwd=123456&interfaceId=getSurfEleByTimeRangeAndStaID&dataCode=SURF_CHN_MUL_HOR&elements=Station_Name,Datetime,Station_Id_C,TEM&timeRange=[20200701000000,20200706010000]&staIds=53420&orderBy=Datetime:asc&dataFormat=json", params);
        System.out.println(result);
    }
}

