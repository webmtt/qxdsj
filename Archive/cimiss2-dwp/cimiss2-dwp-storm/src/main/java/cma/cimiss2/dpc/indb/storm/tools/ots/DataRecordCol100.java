package cma.cimiss2.dpc.indb.storm.tools.ots;

import com.alicloud.openservices.tablestore.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yizheng on 2018/5/3.
 */
public class DataRecordCol100 {

    String v01301;
    long d_update_time;
    String d_record_id;

    public DataRecordCol100(String v01301, long updateTime, String recordId) {
        this.v01301 = v01301;
        this.d_update_time = updateTime;
        this.d_record_id = recordId;
    }

    public PrimaryKey getPrimaryKey() {
        PrimaryKey primaryKey = PrimaryKeyBuilder.createPrimaryKeyBuilder()
                .addPrimaryKeyColumn("Hash", PrimaryKeyValue.fromString(v01301))
                .addPrimaryKeyColumn("D_RECORD_ID", PrimaryKeyValue.fromString(d_record_id))
                .build();
        return primaryKey;
    }

    public List<Column> getAttributes() throws IOException {
        List<Column> attributes = new ArrayList<Column>();
        for (int i = 0; i < 100; i++) {
            attributes.add(new Column("col" + i, ColumnValue.fromDouble(i)));
        }
        return attributes;
    }
}
