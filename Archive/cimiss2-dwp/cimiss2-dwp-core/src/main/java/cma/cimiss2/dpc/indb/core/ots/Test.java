package cma.cimiss2.dpc.indb.core.ots;

import com.alicloud.openservices.tablestore.AsyncClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by yizheng on 2018/5/3.
 */
public class Test {

    public static String endpoint = "http://rdb.cn-beijing-gjqx-d01.ots.mcloud.nmic.cn";
    public static String accessId = "RtnMdDl1SUdv0L7r";
    public static String accessKey = "WiuzAUiEjSQrOpWmycTpEFOnMG9Kof";
    public static String instance = "rdb";
    public static String dataTable = "TestOTSHelper";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AsyncClient client = new AsyncClient(endpoint, accessId, accessKey, instance);
        OTSHelper helper = new OTSHelper(client, dataTable);
        List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> row = new HashMap<String, Object>();
            row.put("D_RECORD_ID", "20180503aaa_" + i);
            row.put("Col1", "dads");
            row.put(".table",".table!");
            batchs.add(row);
        }
        // 批量写
        OTSBatchResult result = helper.insert(batchs, true);
        System.out.println(result.getSuccessRowCount());
        System.out.println(result.getFailedRowCount());
        System.out.println(result.getFailedRows());

        // 获取单行
//        Map<String, Object> primaryKey = new HashMap<String, Object>();
//        primaryKey.put("D_RECORD_ID", "20180503aaa_0");
//        Map<String, Object> row = helper.query(primaryKey);
//        System.out.println(row);

        // 更新，再次获取
//        row.put("Col3", true);
//        helper.update(row);
//        row = helper.query(primaryKey);
//        System.out.println(row);
//
//        // 删除，再次获取
//        helper.delete(row);
//        row = helper.query(primaryKey);
//        System.out.println(row);

        client.shutdown();
    }
}
