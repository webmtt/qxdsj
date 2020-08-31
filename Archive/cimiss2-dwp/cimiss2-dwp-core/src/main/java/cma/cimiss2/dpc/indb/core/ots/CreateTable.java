package cma.cimiss2.dpc.indb.core.ots;

import com.aliyun.openservices.ots.internal.OTSClient;
import com.aliyun.openservices.ots.internal.model.*;
import com.aliyun.openservices.ots.internal.utils.PartitionSplitUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yizheng on 2018/5/3.
 */
public class CreateTable {

    private static void createTable(OTSClient ots) {
        TableMeta meta = new TableMeta("surf_wea_chn_day_all_nat_tab");//修改表名
        meta.addPrimaryKeyColumn("Hash", PrimaryKeyType.STRING);//不需要更改
        meta.addPrimaryKeyColumn("D_RECORD_ID", PrimaryKeyType.STRING);//设置主键 D_RECORD_ID
//        meta.addPrimaryKeyColumn("secondaryKeyName", PrimaryKeyType.STRING);//需要双主键的话 可自己添加 主键 secondaryKeyName
        TableOptions opts = new TableOptions();
        opts.setMaxVersions(1);
        opts.setTimeToLive(-1);//过期时间 单位秒 -1表示永远不过期  可以自定义
        CreateTableRequest ct = new CreateTableRequest(meta);
        ct.setTableOptions(opts);
        List<PartitionRange> pl = new ArrayList<PartitionRange>();
        pl.addAll(PartitionSplitUtil.splitHex(64, true));
        ct.setPartitionRangeList(pl);
        ct.setReservedThroughput(new ReservedThroughput(new CapacityUnit(1, 1)));
        ct.setStreamSpecification(new StreamSpecification(true, 24));
        ots.createTable(ct);
    }

    public static void main(String[] args) {
//        OTSClient otsClient = new OTSClient(Config.endpoint, Config.accessId,
//                Config.accessKey, Config.instance);
//        createTable(otsClient);
//        otsClient.shutdown();
    }

}
