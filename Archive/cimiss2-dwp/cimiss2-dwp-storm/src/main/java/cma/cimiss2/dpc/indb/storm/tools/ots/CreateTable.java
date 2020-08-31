package cma.cimiss2.dpc.indb.storm.tools.ots;

import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.DeleteTableRequest;
import com.aliyun.openservices.ots.internal.OTSClient;
import com.aliyun.openservices.ots.internal.model.*;
import com.aliyun.openservices.ots.internal.utils.PartitionSplitUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yizheng on 2018/5/3.
 */
public class CreateTable {
	
	public static void deleteTables() {
		SyncClient client = new SyncClient(Config.endpoint, Config.accessId,
                Config.accessKey, Config.instance);
		List<String> tables = client.listTable().getTableNames();
		for (String string : tables) {
			System.out.println(string);
//			if(!string.startsWith("Stream")) {
//				client.deleteTable(new DeleteTableRequest(string));
//				System.out.println("delete table " + string);
//			}
			
		}
		
	}
	
	public static void delTable(String tableName) {
		SyncClient client = new SyncClient(Config.endpoint, Config.accessId,
                Config.accessKey, Config.instance);
	   client.deleteTable(new DeleteTableRequest(tableName));
	
	}

    public static void createTable(OTSClient ots, String tableName) {
        TableMeta meta = new TableMeta(tableName);
        meta.addPrimaryKeyColumn("Hash", PrimaryKeyType.STRING);
        meta.addPrimaryKeyColumn("D_RECORD_ID", PrimaryKeyType.STRING);
        TableOptions opts = new TableOptions();
        opts.setMaxVersions(1);
//        7*24*60*60
        opts.setTimeToLive(7*24*60*60);
        CreateTableRequest ct = new CreateTableRequest(meta);
        ct.setTableOptions(opts);
        List<PartitionRange> pl = new ArrayList<PartitionRange>();
        pl.addAll(PartitionSplitUtil.splitHex(64, true));
        ct.setPartitionRangeList(pl);
        ct.setReservedThroughput(new ReservedThroughput(new CapacityUnit(1, 1)));
        ct.setStreamSpecification(new StreamSpecification(true, 7*24));
        ots.createTable(ct);
    }

    public static void createTable(OTSClient ots, String tableName, String secondaryKeyName) {
        TableMeta meta = new TableMeta(tableName);
        meta.addPrimaryKeyColumn("Hash", PrimaryKeyType.STRING);
        meta.addPrimaryKeyColumn("D_RECORD_ID", PrimaryKeyType.STRING);
        meta.addPrimaryKeyColumn(secondaryKeyName, PrimaryKeyType.STRING);
        TableOptions opts = new TableOptions();
        opts.setMaxVersions(1);
        opts.setTimeToLive(-1);
        CreateTableRequest ct = new CreateTableRequest(meta);
        ct.setTableOptions(opts);
        List<PartitionRange> pl = new ArrayList<PartitionRange>();
        pl.addAll(PartitionSplitUtil.splitHex(64, true));
        ct.setPartitionRangeList(pl);
        ct.setReservedThroughput(new ReservedThroughput(new CapacityUnit(1, 1)));
        ct.setStreamSpecification(new StreamSpecification(true, 7*24));
        ots.createTable(ct);
    }

    public static void main(String[] args) {
    	OTSClient ots = new OTSClient(Config.endpoint, Config.accessId,
                Config.accessKey, Config.instance);
    	
//    	RADI_MUL_CHN_BSRN_HOR_TAB
//    	RADI_MUL_CHN_BSRN_MIN_TAB
//    	RADI_MUL_CHN_HOR_TAB
//    	RADI_WEA_CHN_REP_TAB
//    	SURF_WEA_CHN_REP_TAB
//    	delTable("AGME_MICLI_CHN_TAB");
//    	delTable("SEVP_WEFC_TYP_WT_TAB");
    	createTable(ots, "SURF_WEA_CBF_PRE_MIN_TAB");
//    	createTable(ots, "SEVP_WEFC_TYP_WT_TAB","D_ELE_ID");
        
//    	deleteTables();
//    	createTable(ots, tableName, secondaryKeyName);
    }

}
