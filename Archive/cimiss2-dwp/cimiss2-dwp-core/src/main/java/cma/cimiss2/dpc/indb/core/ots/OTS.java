package cma.cimiss2.dpc.indb.core.ots;

import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import com.alicloud.openservices.tablestore.AsyncClient;

/**
 * @Description:
 * @Aouthor: xzh
 * @create: 2018-05-03 18:07
 */
public class OTS {

    //    public static String dataTable = "TestOTSHelper";


    private static AsyncClient client = null;

    public static AsyncClient getClient() {
        if (client == null) {
            synchronized (OTS.class) {
                if (client == null) {
                    String endpoint = ConfigurationManager.getString("ots.url");
                    String accessId = ConfigurationManager.getString("ots.accessId");
                    String accessKey = ConfigurationManager.getString("ots.accessKey");
                    String instance = ConfigurationManager.getString("ots.instance");
                    System.out.println(endpoint + accessId + accessKey + instance + "================================================");
                    client = new AsyncClient(Config.endpoint, Config.accessId, Config.accessKey, Config.instance);
                }
            }
        }
        return client;
    }

    public static AsyncClient getClient(String endpoint, String accessId, String accessKey, String instance) {
        if (client == null) {
            synchronized (OTS.class) {
                if (client == null)
                    client = new AsyncClient(endpoint, accessId, accessKey, instance);
            }
        }
        return client;
    }

    public static void main(String[] args) {
        ConfigurationManager.loadPro("d:/config/", "db.properties");
        AsyncClient client = getClient();

    }
}