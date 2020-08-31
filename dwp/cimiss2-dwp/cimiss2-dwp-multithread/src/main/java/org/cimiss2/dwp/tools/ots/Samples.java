package org.cimiss2.dwp.tools.ots;

import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.core.utils.BinaryUtil;
import com.alicloud.openservices.tablestore.model.*;
import org.apache.commons.codec.binary.Hex;


/**
 * Created by yizheng on 2018/5/3.
 */
public class Samples {

    private static String PRIMARY_KEY_HASH = "Hash";
    private static String PRIMARY_KEY_D_RECORD_ID = "D_RECORD_ID";
    private static String TABLE_NAME_KEY = ".table";

    private static String getHashKey(String d_record_id) {
        byte[] md5 = BinaryUtil.calculateMd5(d_record_id.getBytes());
        String hexString = Hex.encodeHexString(md5).substring(0, 4);
        return hexString;
    }

    private static void putRow(SyncClient client, String d_record_id) {
        // 构造主键
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_HASH,
                PrimaryKeyValue.fromString(getHashKey(d_record_id)));
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_D_RECORD_ID,
                PrimaryKeyValue.fromString(d_record_id));
        PrimaryKey primaryKey = primaryKeyBuilder.build();
        RowPutChange rowPutChange = new RowPutChange(PRIMARY_KEY_D_RECORD_ID, primaryKey);
        //加入一些属性列
        for (int i = 0; i < 10; i++) {
            rowPutChange.addColumn(new Column("Col" + i, ColumnValue.fromLong(i)));
        }
        client.putRow(new PutRowRequest(rowPutChange));
    }

}
