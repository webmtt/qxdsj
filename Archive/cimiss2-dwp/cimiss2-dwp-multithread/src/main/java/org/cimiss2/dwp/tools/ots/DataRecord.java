package org.cimiss2.dwp.tools.ots;

import com.alicloud.openservices.tablestore.core.utils.CompressUtil;
import com.alicloud.openservices.tablestore.model.Column;
import com.alicloud.openservices.tablestore.model.ColumnValue;
import com.alicloud.openservices.tablestore.model.PrimaryKey;
import com.alicloud.openservices.tablestore.model.PrimaryKeyBuilder;
import com.alicloud.openservices.tablestore.model.PrimaryKeyValue;
import com.alicloud.openservices.tablestore.model.Row;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Table scheme of mysql:
 * CREATE TABLE `surf_wea_chn_min_all_reg_tab` (
 * `D_RECORD_ID` varchar(200) NOT NULL,
 * `D_DATA_ID` varchar(30) DEFAULT NULL,
 * `D_IYMDHM` datetime DEFAULT NULL,
 * `D_RYMDHM` datetime DEFAULT NULL,
 * `D_UPDATE_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
 * `D_DATETIME` datetime NOT NULL,
 * `V04001` decimal(4,0) DEFAULT NULL,
 * `V04002` decimal(2,0) DEFAULT NULL,
 * `V04003` decimal(2,0) DEFAULT NULL,
 * `V04004` decimal(2,0) DEFAULT NULL,
 * `V04005` decimal(2,0) DEFAULT NULL,
 * `V01301` varchar(9) NOT NULL,
 * `V01300` decimal(6,0) DEFAULT NULL,
 * `V05001` decimal(10,4) DEFAULT NULL,
 * `V06001` decimal(10,4) DEFAULT NULL,
 * `V07001` decimal(10,4) DEFAULT NULL,
 * `V02001` decimal(6,0) DEFAULT NULL,
 * `V02301` decimal(6,0) DEFAULT NULL,
 * `V_ACODE` varchar(6) DEFAULT NULL,
 * `V_BBB` char(3) DEFAULT NULL,
 * `V08010` decimal(10,4) DEFAULT NULL,
 * `V07031` decimal(10,4) DEFAULT NULL,
 * `V07032_02` decimal(10,4) DEFAULT NULL,
 * `V07032_01` decimal(10,4) DEFAULT NULL,
 * `V07032_04` decimal(10,4) DEFAULT NULL,
 * `V02180` decimal(10,4) DEFAULT NULL,
 * `V02183` decimal(10,4) DEFAULT NULL,
 * `V02175` decimal(10,4) DEFAULT NULL,
 * `V02177` decimal(1,0) DEFAULT NULL,
 * `V10004` decimal(10,4) DEFAULT NULL,
 * `V10051` decimal(10,4) DEFAULT NULL,
 * `V11041` decimal(10,4) DEFAULT NULL,
 * `V11043` decimal(10,4) DEFAULT NULL,
 * `V11201` decimal(10,4) DEFAULT NULL,
 * `V11202` decimal(10,4) DEFAULT NULL,
 * `V11288` decimal(10,4) DEFAULT NULL,
 * `V11289` decimal(10,4) DEFAULT NULL,
 * `V11290` decimal(10,4) DEFAULT NULL,
 * `V11291` decimal(10,4) DEFAULT NULL,
 * `V11292` decimal(10,4) DEFAULT NULL,
 * `V11293` decimal(10,4) DEFAULT NULL,
 * `V11296` decimal(10,4) DEFAULT NULL,
 * `V12001` decimal(10,4) DEFAULT NULL,
 * `V12003` decimal(10,4) DEFAULT NULL,
 * `V12030_005` decimal(10,4) DEFAULT NULL,
 * `V12030_010` decimal(10,4) DEFAULT NULL,
 * `V12030_015` decimal(10,4) DEFAULT NULL,
 * `V12030_020` decimal(10,4) DEFAULT NULL,
 * `V12030_040` decimal(10,4) DEFAULT NULL,
 * `V12030_080` decimal(10,4) DEFAULT NULL,
 * `V12030_160` decimal(10,4) DEFAULT NULL,
 * `V12030_320` decimal(10,4) DEFAULT NULL,
 * `V12120` decimal(10,4) DEFAULT NULL,
 * `V12314` decimal(10,4) DEFAULT NULL,
 * `V13003` decimal(10,4) DEFAULT NULL,
 * `V13004` decimal(10,4) DEFAULT NULL,
 * `V13011` decimal(10,4) DEFAULT NULL,
 * `V13013` decimal(10,4) DEFAULT NULL,
 * `V13033` decimal(10,4) DEFAULT NULL,
 * `V20001_701_01` decimal(10,4) DEFAULT NULL,
 * `V20001_701_10` decimal(10,4) DEFAULT NULL,
 * `V20010` decimal(10,4) DEFAULT NULL,
 * `V20013` decimal(10,4) DEFAULT NULL,
 * `V20211` varchar(200) DEFAULT NULL,
 * `Q10004` decimal(1,0) DEFAULT NULL,
 * `Q10051` decimal(1,0) DEFAULT NULL,
 * `Q11201` decimal(1,0) DEFAULT NULL,
 * `Q11202` decimal(1,0) DEFAULT NULL,
 * `Q11288` decimal(1,0) DEFAULT NULL,
 * `Q11289` decimal(1,0) DEFAULT NULL,
 * `Q11290` decimal(1,0) DEFAULT NULL,
 * `Q11291` decimal(1,0) DEFAULT NULL,
 * `Q11292` decimal(1,0) DEFAULT NULL,
 * `Q11293` decimal(1,0) DEFAULT NULL,
 * `Q11296` decimal(1,0) DEFAULT NULL,
 * `Q12001` decimal(1,0) DEFAULT NULL,
 * `Q12003` decimal(1,0) DEFAULT NULL,
 * `Q12030_005` decimal(1,0) DEFAULT NULL,
 * `Q12030_010` decimal(1,0) DEFAULT NULL,
 * `Q12030_015` decimal(1,0) DEFAULT NULL,
 * `Q12030_020` decimal(1,0) DEFAULT NULL,
 * `Q12030_040` decimal(1,0) DEFAULT NULL,
 * `Q12030_080` decimal(1,0) DEFAULT NULL,
 * `Q12030_160` decimal(1,0) DEFAULT NULL,
 * `Q12030_320` decimal(1,0) DEFAULT NULL,
 * `Q12120` decimal(1,0) DEFAULT NULL,
 * `Q12314` decimal(1,0) DEFAULT NULL,
 * `Q13003` decimal(1,0) DEFAULT NULL,
 * `Q13004` decimal(1,0) DEFAULT NULL,
 * `Q13011` decimal(1,0) DEFAULT NULL,
 * `Q13013` decimal(1,0) DEFAULT NULL,
 * `Q20001_701_01` decimal(1,0) DEFAULT NULL,
 * `Q20001_701_10` decimal(1,0) DEFAULT NULL,
 * `Q20010` decimal(1,0) DEFAULT NULL,
 * `Q20013` decimal(1,0) DEFAULT NULL,
 * `V_RETAIN1` decimal(6,0) DEFAULT NULL,
 * `V_RETAIN2` decimal(6,0) DEFAULT NULL,
 * `V_RETAIN3` decimal(6,0) DEFAULT NULL,
 * `V_RETAIN4` decimal(6,0) DEFAULT NULL,
 * `V_RETAIN5` decimal(6,0) DEFAULT NULL,
 * `V_RETAIN6` decimal(6,0) DEFAULT NULL,
 * `V_RETAIN7` decimal(6,0) DEFAULT NULL,
 * `V_RETAIN8` decimal(6,0) DEFAULT NULL,
 * `V_RETAIN9` decimal(6,0) DEFAULT NULL,
 * `V_RETAIN10` decimal(6,0) DEFAULT NULL,
 * PRIMARY KEY (`D_RECORD_ID`),
 * KEY `D_UPDATE_TIME` (`D_UPDATE_TIME`) USING BTREE,
 * KEY `auto_shard_key_V01301` (`V01301`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 dbpartition by hash(`V01301`);
 *
 * Sample data:
 *  20180416091000_A3361	A.0010.0001.S001	2018-4-16 10:15:21	2018-4-16 09:10:45	2018-4-16 10:15:21	2018-4-16 09:10:00
 *  2018	4	16	9	10	A3361	653361	39.0156	117.4156	0.0000	0	14	120112	000	999999.0000	0.0000
 *  999999.0000	999999.0000	999999.0000	999998.0000	999999.0000	999998.0000	7	1013.5000	999999.0000	999998.0000	999998.0000
 *  999998.0000	4.7000	999998.0000	999998.0000	186.0000	6.0000	193.0000	6.4000	200.0000	21.6000	999999.0000	999999.0000
 *  999999.0000	999999.0000	999999.0000	999999.0000	999999.0000	999999.0000	999999.0000	999999.0000	999999.0000	26.0000	999999.0000
 *  999998.0000	999999.0000	999999.0000	999998.0000	999998.0000	999999.0000	999999.0000	999998	0	7	0	0	7	7	0	0	0	0	0	0	7	7	7	7	7	7	7	7	7	7	7	0	7	7	7	7	7	7	7
 *
 * Table scheme of OTS:
 *  PrimaryKey: v01301(partition key), d_update_time, d_record_id
 *  Attributes: all other columns
 */
public class DataRecord {
    private String d_record_id; //  varchar(200) NOT NULL
    private String d_data_id; //  varchar(30) DEFAULT NULL
    private long d_iymdhm; //  datetime DEFAULT NULL
    private long d_rymdhm; //  datetime DEFAULT NULL
    private long d_update_time; //  timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
    private long d_datetime; //  datetime NOT NULL
    private long v04001; //  decimal(4,0) DEFAULT NULL
    private long v04002; //  decimal(2,0) DEFAULT NULL
    private long v04003; //  decimal(2,0) DEFAULT NULL
    private long v04004; //  decimal(2,0) DEFAULT NULL
    private long v04005; //  decimal(2,0) DEFAULT NULL
    private String v01301; //  varchar(9) NOT NULL
    private long v01300; //  decimal(6,0) DEFAULT NULL
    private long v05001; //  decimal(10,4) DEFAULT NULL
    private long v06001; //  decimal(10,4) DEFAULT NULL
    private long v07001; //  decimal(10,4) DEFAULT NULL
    private long v02001; //  decimal(6,0) DEFAULT NULL
    private long v02301; //  decimal(6,0) DEFAULT NULL
    private String v_acode; //  varchar(6) DEFAULT NULL
    private String v_bbb; //  char(3) DEFAULT NULL
    private long v08010; //  decimal(10,4) DEFAULT NULL
    private long v07031; //  decimal(10,4) DEFAULT NULL
    private long v07032_02; //  decimal(10,4) DEFAULT NULL
    private long v07032_01; //  decimal(10,4) DEFAULT NULL
    private long v07032_04; //  decimal(10,4) DEFAULT NULL
    private long v02180; //  decimal(10,4) DEFAULT NULL
    private long v02183; //  decimal(10,4) DEFAULT NULL
    private long v02175; //  decimal(10,4) DEFAULT NULL
    private long v02177; //  decimal(1,0) DEFAULT NULL
    private long v10004; //  decimal(10,4) DEFAULT NULL
    private long v10051; //  decimal(10,4) DEFAULT NULL
    private long v11041; //  decimal(10,4) DEFAULT NULL
    private long v11043; //  decimal(10,4) DEFAULT NULL
    private long v11201; //  decimal(10,4) DEFAULT NULL
    private long v11202; //  decimal(10,4) DEFAULT NULL
    private long v11288; //  decimal(10,4) DEFAULT NULL
    private long v11289; //  decimal(10,4) DEFAULT NULL
    private long v11290; //  decimal(10,4) DEFAULT NULL
    private long v11291; //  decimal(10,4) DEFAULT NULL
    private long v11292; //  decimal(10,4) DEFAULT NULL
    private long v11293; //  decimal(10,4) DEFAULT NULL
    private long v11296; //  decimal(10,4) DEFAULT NULL
    private long v12001; //  decimal(10,4) DEFAULT NULL
    private long v12003; //  decimal(10,4) DEFAULT NULL
    private long v12030_005; //  decimal(10,4) DEFAULT NULL
    private long v12030_010; //  decimal(10,4) DEFAULT NULL
    private long v12030_015; //  decimal(10,4) DEFAULT NULL
    private long v12030_020; //  decimal(10,4) DEFAULT NULL
    private long v12030_040; //  decimal(10,4) DEFAULT NULL
    private long v12030_080; //  decimal(10,4) DEFAULT NULL
    private long v12030_160; //  decimal(10,4) DEFAULT NULL
    private long v12030_320; //  decimal(10,4) DEFAULT NULL
    private long v12120; //  decimal(10,4) DEFAULT NULL
    private long v12314; //  decimal(10,4) DEFAULT NULL
    private long v13003; //  decimal(10,4) DEFAULT NULL
    private long v13004; //  decimal(10,4) DEFAULT NULL
    private long v13011; //  decimal(10,4) DEFAULT NULL
    private long v13013; //  decimal(10,4) DEFAULT NULL
    private long v13033; //  decimal(10,4) DEFAULT NULL
    private long v20001_701_01; //  decimal(10,4) DEFAULT NULL
    private long v20001_701_10; //  decimal(10,4) DEFAULT NULL
    private long v20010; //  decimal(10,4) DEFAULT NULL
    private long v20013; //  decimal(10,4) DEFAULT NULL
    private String v20211; //  varchar(200) DEFAULT NULL
    private long q10004; //  decimal(1,0) DEFAULT NULL
    private long q10051; //  decimal(1,0) DEFAULT NULL
    private long q11201; //  decimal(1,0) DEFAULT NULL
    private long q11202; //  decimal(1,0) DEFAULT NULL
    private long q11288; //  decimal(1,0) DEFAULT NULL
    private long q11289; //  decimal(1,0) DEFAULT NULL
    private long q11290; //  decimal(1,0) DEFAULT NULL
    private long q11291; //  decimal(1,0) DEFAULT NULL
    private long q11292; //  decimal(1,0) DEFAULT NULL
    private long q11293; //  decimal(1,0) DEFAULT NULL
    private long q11296; //  decimal(1,0) DEFAULT NULL
    private long q12001; //  decimal(1,0) DEFAULT NULL
    private long q12003; //  decimal(1,0) DEFAULT NULL
    private long q12030_005; //  decimal(1,0) DEFAULT NULL
    private long q12030_010; //  decimal(1,0) DEFAULT NULL
    private long q12030_015; //  decimal(1,0) DEFAULT NULL
    private long q12030_020; //  decimal(1,0) DEFAULT NULL
    private long q12030_040; //  decimal(1,0) DEFAULT NULL
    private long q12030_080; //  decimal(1,0) DEFAULT NULL
    private long q12030_160; //  decimal(1,0) DEFAULT NULL
    private long q12030_320; //  decimal(1,0) DEFAULT NULL
    private long q12120; //  decimal(1,0) DEFAULT NULL
    private long q12314; //  decimal(1,0) DEFAULT NULL
    private long q13003; //  decimal(1,0) DEFAULT NULL
    private long q13004; //  decimal(1,0) DEFAULT NULL
    private long q13011; //  decimal(1,0) DEFAULT NULL
    private long q13013; //  decimal(1,0) DEFAULT NULL
    private long q20001_701_01; //  decimal(1,0) DEFAULT NULL
    private long q20001_701_10; //  decimal(1,0) DEFAULT NULL
    private long q20010; //  decimal(1,0) DEFAULT NULL
    private long q20013; //  decimal(1,0) DEFAULT NULL
    private long v_retain1; //  decimal(6,0) DEFAULT NULL
    private long v_retain2; //  decimal(6,0) DEFAULT NULL
    private long v_retain3; //  decimal(6,0) DEFAULT NULL
    private long v_retain4; //  decimal(6,0) DEFAULT NULL
    private long v_retain5; //  decimal(6,0) DEFAULT NULL
    private long v_retain6; //  decimal(6,0) DEFAULT NULL
    private long v_retain7; //  decimal(6,0) DEFAULT NULL
    private long v_retain8; //  decimal(6,0) DEFAULT NULL
    private long v_retain9; //  decimal(6,0) DEFAULT NULL
    private long v_retain10; //  decimal(6,0) DEFAULT NULL

    private boolean compressed = false;

    public DataRecord(String v01301, long updateTime, String recordId) {
        this.v01301 = v01301;
        this.d_update_time = updateTime;
        this.d_record_id = recordId;

        setAttributes();
    }

    private void setAttributes() {
        this.d_data_id = d_record_id + "_" + d_update_time;
        this.d_iymdhm = 999999;
        this.d_rymdhm = 999999;
        this.d_datetime = 999999;
        this.v04001 = 999999;
        this.v04002 = 999999;
        this.v04003 = 999999;
        this.v04004 = 999999;
        this.v04005 = 999999;
        this.v01300 = 999999;
        this.v05001 = 999999;
        this.v06001 = 999999;
        this.v07001 = 999999;
        this.v02001 = 999999;
        this.v02301 = 999999;
        this.v_acode = "A";
        this.v_bbb = "B";
        this.v08010 = 999999;
        this.v07031 = 999999;
        this.v07032_02 = 999999;
        this.v07032_01 = 999999;
        this.v07032_04 = 999999;
        this.v02180 = 999999;
        this.v02183 = 999999;
        this.v02175 = 999999;
        this.v02177 = 999999;
        this.v10004 = 999999;
        this.v10051 = 999999;
        this.v11041 = 999999;
        this.v11043 = 999999;
        this.v11201 = 999999;
        this.v11202 = 999999;
        this.v11288 = 999999;
        this.v11289 = 999999;
        this.v11290 = 999999;
        this.v11291 = 999999;
        this.v11292 = 999999;
        this.v11293 = 999999;
        this.v11296 = 999999;
        this.v12001 = 999999;
        this.v12003 = 999999;
        this.v12030_005 = 999999;
        this.v12030_010 = 999999;
        this.v12030_015 = 999999;
        this.v12030_020 = 999999;
        this.v12030_040 = 999999;
        this.v12030_080 = 999999;
        this.v12030_160 = 999999;
        this.v12030_320 = 999999;
        this.v12120 = 999999;
        this.v12314 = 999999;
        this.v13003 = 999999;
        this.v13004 = 999999;
        this.v13011 = 999999;
        this.v13013 = 999999;
        this.v13033 = 999999;
        this.v20001_701_01 = 999999;
        this.v20001_701_10 = 999999;
        this.v20010 = 999999;
        this.v20013 = 999999;
        this.v20211 = "C";
        this.q10004 = 999999;
        this.q10051 = 999999;
        this.q11201 = 999999;
        this.q11202 = 999999;
        this.q11288 = 999999;
        this.q11289 = 999999;
        this.q11290 = 999999;
        this.q11291 = 999999;
        this.q11292 = 999999;
        this.q11293 = 999999;
        this.q11296 = 999999;
        this.q12001 = 999999;
        this.q12003 = 999999;
        this.q12030_005 = 999999;
        this.q12030_010 = 999999;
        this.q12030_015 = 999999;
        this.q12030_020 = 999999;
        this.q12030_040 = 999999;
        this.q12030_080 = 999999;
        this.q12030_160 = 999999;
        this.q12030_320 = 999999;
        this.q12120 = 999999;
        this.q12314 = 999999;
        this.q13003 = 999999;
        this.q13004 = 999999;
        this.q13011 = 999999;
        this.q13013 = 999999;
        this.q20001_701_01 = 999999;
        this.q20001_701_10 = 999999;
        this.q20010 = 999999;
        this.q20013 = 999999;
        this.v_retain1 = 999999;
        this.v_retain2 = 999999;
        this.v_retain3 = 999999;
        this.v_retain4 = 999999;
        this.v_retain5 = 999999;
        this.v_retain6 = 999999;
        this.v_retain7 = 999999;
        this.v_retain8 = 999999;
        this.v_retain9 = 999999;
        this.v_retain10 = 999999;
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
        Gson gson = new Gson();
        String value = gson.toJson(this);
        if (compressed) {
            byte[] content = CompressUtil.compress(new ByteArrayInputStream(value.getBytes("utf-8")), new Deflater());
            attributes.add(new Column("ca", ColumnValue.fromBinary(content)));
        } else {
            attributes.add(new Column("a", ColumnValue.fromBinary(value.getBytes("utf-8"))));
        }
        return attributes;
    }

    public static DataRecord fromRow(Row row) throws IOException {
        String value = null;
        if (row.contains("a")) {
            Column attr = row.getLatestColumn("ca");

            byte[] content = attr.getValue().asBinary();
            try {
                byte[] originValue = CompressUtil.decompress(new ByteArrayInputStream(content), 1024, new Inflater());
                value = new String(originValue, "utf-8");
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        } else {
            Column attr = row.getLatestColumn("a");

            value = attr.getValue().asString();
        }

        Gson gson = new Gson();
        return gson.fromJson(value, DataRecord.class);
    }

    public static DataRecord fromJsonBytes(byte[] data) throws IOException {
        String value = new String(data, "utf-8");
        Gson gson = new Gson();
        return gson.fromJson(value, DataRecord.class);
    }

    public static void main(String[] args) throws Exception {
    }
}
