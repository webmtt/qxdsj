package org.cimiss2.dwp.tools.ots;

import com.alicloud.openservices.tablestore.AsyncClient;

/**
 * @Description:
 * @Aouthor: xzh
 * @create: 2018-05-03 18:07
 */
public class OTS {
    public static String endpoint = "http://rdb.cn-beijing-gjqx-d01.mysql.mcloud.nmic.cn";
    public static String accessId = "RtnMdDl1SUdv0L7r";
    public static String accessKey = "WiuzAUiEjSQrOpWmycTpEFOnMG9Kof";
    public static String instance = "rdb";
    //    public static String dataTable = "TestOTSHelper";


    public static AsyncClient client = null;
    public synchronized static AsyncClient getClient() {
        if (client == null) {
            client = new AsyncClient(endpoint, accessId, accessKey, instance);
        }
        return client;
    }
}
