package cma.cimiss2.dpc.indb.storm.tools.ots;

import com.alicloud.openservices.tablestore.AsyncClient;
import com.alicloud.openservices.tablestore.TableStoreException;
import com.alicloud.openservices.tablestore.core.utils.BinaryUtil;
import com.alicloud.openservices.tablestore.model.*;

import org.apache.storm.shade.org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by yizheng on 2018/5/3.
 */
public class OTSHelper {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    private static String PRIMARY_KEY_HASH = "Hash";
    private static String PRIMARY_KEY_D_RECORD_ID = "D_RECORD_ID";

    private String tableName;
    private AsyncClient client;
    private String secondaryKeyName = "";
    public OTSHelper(AsyncClient asyncClient, String tableName) {
        this.client = asyncClient;
        this.tableName = tableName;
    }
    
    public OTSHelper(AsyncClient asyncClient, String tableName, String secondaryKeyName) {
        this.client = asyncClient;
        this.tableName = tableName;
        this.secondaryKeyName = secondaryKeyName;
    }
    
    public OTSHelper(String endpoint, String accessId, String accessKey, String instance, String tableName) {
        this.client = new AsyncClient(endpoint, accessId, accessKey, instance);
        this.tableName = tableName;
    }
    
    public void insert(Map<String, Object> map, boolean overwriteIfNotExist) throws InterruptedException, ExecutionException   {
    	PutRowRequest request = buildPutRowRequest(map, overwriteIfNotExist);

    	Future<PutRowResponse> response = client.putRow(request, null);

        response.get();


    }
    
    public void insert_ex(Map<String, Object> map, boolean overwriteIfNotExist) throws InterruptedException, ExecutionException{
    	PutRowRequest request = buildPutRowRequest(map, overwriteIfNotExist);
    	client.putRow(request, null).get();    	
    }
    
    public boolean hasSecondaryKey() {
        return (!secondaryKeyName.isEmpty());
    }

    public OTSBatchResult insert(List<Map<String, Object>> batchInserts, boolean overwriteIfNotExist) {
        List<PutRowRequest> requests = new ArrayList<PutRowRequest>();
        for (Map<String, Object> row : batchInserts) {
            requests.add(buildPutRowRequest(row, overwriteIfNotExist));
        }
        List<Future<PutRowResponse>> futures = new ArrayList<Future<PutRowResponse>>();
        for (PutRowRequest request : requests) {
        	try {
        		futures.add(client.putRow(request, null));
			} catch (Exception e) {
				System.out.println(e);
			}
            
        }
        int successRowCount = 0;
        int failedRowCount = 0;
        List<OTSBatchResult.FailedRowResult> failedRows = new ArrayList<OTSBatchResult.FailedRowResult>();
        for (int i = 0; i < futures.size(); i++) {
            try {
                PutRowResponse response = futures.get(i).get();
                successRowCount++;
            } catch (Exception ex) {
                failedRowCount++;
                OTSBatchResult.FailedRowResult failedRowResult = new OTSBatchResult.FailedRowResult();
                failedRowResult.setRow(batchInserts.get(i));
                failedRowResult.setException(ex);
                failedRows.add(failedRowResult);
            }
        }
        OTSBatchResult batchResult = new OTSBatchResult();
        batchResult.setSuccessRowCount(successRowCount);
        batchResult.setFailedRowCount(failedRowCount);
        batchResult.setFailedRows(failedRows);
        return batchResult;
    }

    public Map<String, Object> query(Map<String, Object> primaryKeyMap) throws TableStoreException, ExecutionException, InterruptedException {
        PrimaryKey primaryKey = buildPrimaryKey(primaryKeyMap);
        GetRowRequest getRowRequest = new GetRowRequest();
        SingleRowQueryCriteria criteria = new SingleRowQueryCriteria(tableName, primaryKey);
        criteria.setMaxVersions(1);
        getRowRequest.setRowQueryCriteria(criteria);
        Future<GetRowResponse> future = client.getRow(getRowRequest, null);
        GetRowResponse response = future.get();
        Row row = response.getRow();
        Map<String, Object> result = new HashMap<String, Object>();
        if (row == null) {
            return result;
        } else {
            result.put(PRIMARY_KEY_D_RECORD_ID,
                    row.getPrimaryKey().getPrimaryKeyColumn(PRIMARY_KEY_D_RECORD_ID).getValue().asString());
            result.put(PRIMARY_KEY_HASH,
                    row.getPrimaryKey().getPrimaryKeyColumn(PRIMARY_KEY_HASH).getValue().asString());
            for (Column column : row.getColumns()) {
                switch (column.getValue().getType()) {
                    case STRING:
                        result.put(column.getName(), column.getValue().asString());
                        break;
                    case INTEGER:
                        result.put(column.getName(), column.getValue().asLong());
                        break;
                    case DOUBLE:
                        result.put(column.getName(), column.getValue().asDouble());
                        break;
                    case BOOLEAN:
                        result.put(column.getName(), column.getValue().asBoolean());
                        break;
                }
            }
        }
        return result;
    }

    public void delete(Map<String, Object> primaryKeyMap) throws TableStoreException, ExecutionException, InterruptedException {
        PrimaryKey primaryKey = buildPrimaryKey(primaryKeyMap);
        DeleteRowRequest deleteRowRequest = new DeleteRowRequest();
        deleteRowRequest.setRowChange(new RowDeleteChange(tableName, primaryKey));
        client.deleteRow(deleteRowRequest, null).get();
    }

    public void update(Map<String, Object> row) throws Exception {
        UpdateRowRequest updateRowRequest = buildUpdateRowRequest(row);
        client.updateRow(updateRowRequest, null).get();
    }
    
    public void update(Map<String, Object> row, boolean ignoreIfNotExist) throws TableStoreException, ExecutionException, InterruptedException {
    	UpdateRowRequest updateRowRequest = buildUpdateRowRequest(row, ignoreIfNotExist);
    	client.updateRow(updateRowRequest, null).get();
    }

    	       

        
    /**
     * 由于ots是分布式数据库，不支持count，统计总条数需要全部扫描一遍，数据量大时本接口返回会很慢。
     */
    public long countAllRows(int concurrent) throws ExecutionException, InterruptedException {
        GetRangeRequest[] requests = new GetRangeRequest[concurrent];
        Future<GetRangeResponse>[] futures = new Future[concurrent];
        String lastSplitPoint = "";
        for (int i = 0; i < concurrent; i++) {
            RangeRowQueryCriteria criteria = new RangeRowQueryCriteria(tableName);
            criteria.setMaxVersions(1);
            PrimaryKeyBuilder startPKBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder()
                    .addPrimaryKeyColumn(PRIMARY_KEY_HASH, PrimaryKeyValue.fromString(lastSplitPoint))
                    .addPrimaryKeyColumn(PRIMARY_KEY_D_RECORD_ID, PrimaryKeyValue.INF_MIN);

            lastSplitPoint = String.format("%04x", (65536 / concurrent) * (i + 1));
            PrimaryKeyBuilder endPKBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
            if (i == (concurrent - 1)) {
                endPKBuilder.addPrimaryKeyColumn(PRIMARY_KEY_HASH, PrimaryKeyValue.INF_MAX);
            } else {
                endPKBuilder.addPrimaryKeyColumn(PRIMARY_KEY_HASH, PrimaryKeyValue.fromString(lastSplitPoint));
            }
            endPKBuilder.addPrimaryKeyColumn(PRIMARY_KEY_D_RECORD_ID, PrimaryKeyValue.INF_MIN);

            if (hasSecondaryKey()) {
                startPKBuilder.addPrimaryKeyColumn(secondaryKeyName, PrimaryKeyValue.INF_MIN);
                endPKBuilder.addPrimaryKeyColumn(secondaryKeyName, PrimaryKeyValue.INF_MIN);
            }
            criteria.setInclusiveStartPrimaryKey(startPKBuilder.build());
            criteria.setExclusiveEndPrimaryKey(endPKBuilder.build());

            GetRangeRequest getRangeRequest = new GetRangeRequest();
            getRangeRequest.setRangeRowQueryCriteria(criteria);
            requests[i] = getRangeRequest;
        }
        for (int i = 0; i < concurrent; i++) {
            futures[i] = client.getRange(requests[i], null);
        }
        // 检查是否所有范围查询均已做完，若未做完，则继续发送查询请求
        long count = 0;
        long lastTime = System.currentTimeMillis();
        while (true) {
            boolean completed = true;
            for (int i = 0; i < futures.length; i++) {
                Future<GetRangeResponse> future = futures[i];
                if (future == null) {
                    continue;
                }
                if (future.isDone()) {
                    GetRangeResponse result = future.get();
                    count += result.getRows().size();
                    if (result.getNextStartPrimaryKey() != null) {
                        // 该范围还未查询完毕，需要从nextStart开始继续往下读。
                        requests[i].getRangeRowQueryCriteria().setInclusiveStartPrimaryKey(result.getNextStartPrimaryKey());
                        futures[i] = client.getRange(requests[i], null);
                        completed = false;
                    } else {
                        futures[i] = null; // 若某个范围查询完毕，则将对应future设置为null
                    }
                } else {
                    completed = false;
                }
            }
            if (completed) {
                break;
            } else {
                try {
                    Thread.sleep(10); // 避免busy wait，每次循环完毕后等待一小段时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (System.currentTimeMillis() - lastTime > 1000) {
                System.out.println("CurrentCount: " + count);
                lastTime = System.currentTimeMillis();
            }
        }
        System.out.println("TotalCount: " + count);
        return count;
    }

    private static String getHashKey(String d_record_id) {
        byte[] md5 = BinaryUtil.calculateMd5(d_record_id.getBytes());
        String hexString = Hex.encodeHexString(md5).substring(0, 4);
        return hexString;
    }

    private PrimaryKey buildPrimaryKey(Map<String, Object> row) {
    	PrimaryKeyBuilder builder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        if (!row.containsKey(PRIMARY_KEY_D_RECORD_ID)) {
            throw new RuntimeException("missing key: " + PRIMARY_KEY_D_RECORD_ID);
        }
        String d_record_id = (String) row.get(PRIMARY_KEY_D_RECORD_ID);
        builder.addPrimaryKeyColumn(PRIMARY_KEY_HASH, PrimaryKeyValue.fromString(getHashKey(d_record_id)));
        builder.addPrimaryKeyColumn(PRIMARY_KEY_D_RECORD_ID, PrimaryKeyValue.fromString(d_record_id));
        if (hasSecondaryKey()) {
            if (row.containsKey(secondaryKeyName)) {
                String secondaryKey = (String) row.get(secondaryKeyName);
                builder.addPrimaryKeyColumn(secondaryKeyName, PrimaryKeyValue.fromString(secondaryKey));
            } else {
                throw new RuntimeException("missing key: " + secondaryKeyName);
            }
        }
        return builder.build();
    }

    private PutRowRequest buildPutRowRequest(Map<String, Object> row, boolean overwriteIfExist) {
        RowPutChange rowPutChange = new RowPutChange(tableName);
        rowPutChange.setPrimaryKey(buildPrimaryKey(row));
        PutRowRequest putRowRequest = new PutRowRequest(rowPutChange);
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getKey().equals(PRIMARY_KEY_D_RECORD_ID)) {
                continue;
            }
            if (entry.getKey().equals(PRIMARY_KEY_HASH)) {
                continue;
            }
            if (entry.getKey().equals(secondaryKeyName)) {
                continue;
            }
            if (entry.getValue() instanceof String) {
                rowPutChange.addColumn(entry.getKey().toUpperCase(), ColumnValue.fromString((String) entry.getValue()));
            } else if (entry.getValue() instanceof Byte[]) {
            	 rowPutChange.addColumn(entry.getKey().toUpperCase(), ColumnValue.fromBinary((byte[]) entry.getValue()));
			}else if (entry.getValue() instanceof Long) {
                rowPutChange.addColumn(entry.getKey().toUpperCase(), ColumnValue.fromLong((Long) entry.getValue()));
            } else if (entry.getValue() instanceof Integer) {
                rowPutChange.addColumn(entry.getKey().toUpperCase(), ColumnValue.fromLong((Integer) entry.getValue()));
            } else if (entry.getValue() instanceof Float) {
            	BigDecimal b = new BigDecimal(String.valueOf(entry.getValue()));
            	rowPutChange.addColumn(entry.getKey().toUpperCase(), ColumnValue.fromDouble((Double) b.doubleValue()));
			} else if (entry.getValue() instanceof Double ) {
                rowPutChange.addColumn(entry.getKey().toUpperCase(), ColumnValue.fromDouble((Double) entry.getValue()));
            } else if (entry.getValue() instanceof Boolean) {
                rowPutChange.addColumn(entry.getKey().toUpperCase(), ColumnValue.fromBoolean((Boolean) entry.getValue()));
            } else if (entry.getValue() instanceof Date) {
                String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
                        Locale.getDefault()).format((Date) entry.getValue());
                rowPutChange.addColumn(entry.getKey().toUpperCase(), ColumnValue.fromString(dateTime));
            } else {
                throw new RuntimeException("unsupport type: " + entry.getValue().getClass().getName());
            }
        }
        if (!overwriteIfExist) {
            rowPutChange.setCondition(new Condition(RowExistenceExpectation.EXPECT_NOT_EXIST));
        }
        return putRowRequest;
    }

    private UpdateRowRequest buildUpdateRowRequest(Map<String, Object> row) {
        RowUpdateChange rowUpdateChange = new RowUpdateChange(tableName);
        rowUpdateChange.setPrimaryKey(buildPrimaryKey(row));
        UpdateRowRequest updateRowRequest = new UpdateRowRequest(rowUpdateChange);
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getKey().equals(PRIMARY_KEY_D_RECORD_ID)) {
                continue;
            }
            if (entry.getKey().equals(PRIMARY_KEY_HASH)) {
                continue;
            }
            if (entry.getKey().equals(secondaryKeyName)) {
                continue;
            }
            if (entry.getValue() instanceof String) {
                rowUpdateChange.put(entry.getKey().toUpperCase(), ColumnValue.fromString((String) entry.getValue()));
            } else if (entry.getValue() instanceof Byte[]) {
            	rowUpdateChange.put(entry.getKey().toUpperCase(), ColumnValue.fromBinary((byte[]) entry.getValue()));
			}else if (entry.getValue() instanceof Long) {
                rowUpdateChange.put(entry.getKey().toUpperCase(), ColumnValue.fromLong((Long) entry.getValue()));
            } else if (entry.getValue() instanceof Integer) {
                rowUpdateChange.put(entry.getKey().toUpperCase(), ColumnValue.fromLong((Integer) entry.getValue()));
            } else if (entry.getValue() instanceof Double) {
                rowUpdateChange.put(entry.getKey().toUpperCase(), ColumnValue.fromDouble((Double) entry.getValue()));
            } else if (entry.getValue() instanceof Float) {
            	BigDecimal b = new BigDecimal(String.valueOf(entry.getValue()));
            	rowUpdateChange.put(entry.getKey().toUpperCase(), ColumnValue.fromDouble((Double) b.doubleValue()));
			}else if (entry.getValue() instanceof Boolean) {
                rowUpdateChange.put(entry.getKey().toUpperCase(), ColumnValue.fromBoolean((Boolean) entry.getValue()));
            } else if (entry.getValue() instanceof Date) {
                String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
                        Locale.getDefault()).format((Date) entry.getValue());
                rowUpdateChange.put(entry.getKey().toUpperCase(), ColumnValue.fromString(dateTime));
            } else {
                throw new RuntimeException("unsupport type: " + entry.getValue().getClass().getName());
            }
        }
        return updateRowRequest;
    }
    
    
    public void updateExistRow(Map<String, Object> row) throws TableStoreException, ExecutionException, InterruptedException {
        UpdateRowRequest updateRowRequest = buildUpdateRowRequest(row, true);
        client.updateRow(updateRowRequest, null).get();
    }
    
    private UpdateRowRequest buildUpdateRowRequest(Map<String, Object> row, boolean ignoreIfNotExist) {
        RowUpdateChange rowUpdateChange = new RowUpdateChange(tableName);
        rowUpdateChange.setPrimaryKey(buildPrimaryKey(row));
        UpdateRowRequest updateRowRequest = new UpdateRowRequest(rowUpdateChange);
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getKey().equals(PRIMARY_KEY_D_RECORD_ID)) {
                continue;
            }
            if (entry.getKey().equals(PRIMARY_KEY_HASH)) {
                continue;
            }
            
			if (entry.getKey().equals(secondaryKeyName)) {
                continue;
            }
            if (entry.getValue() instanceof String) {
                rowUpdateChange.put(entry.getKey(), ColumnValue.fromString((String) entry.getValue()));
            }else if (entry.getValue() instanceof Byte[]) {
            	rowUpdateChange.put(entry.getKey().toUpperCase(), ColumnValue.fromBinary((byte[]) entry.getValue()));
			} else if (entry.getValue() instanceof Long) {
                rowUpdateChange.put(entry.getKey(), ColumnValue.fromLong((Long) entry.getValue()));
            } else if (entry.getValue() instanceof Integer) {
                rowUpdateChange.put(entry.getKey(), ColumnValue.fromLong((Integer) entry.getValue()));
            } else if (entry.getValue() instanceof Double) {
                rowUpdateChange.put(entry.getKey(), ColumnValue.fromDouble((Double) entry.getValue()));
            }else if (entry.getValue() instanceof Float) {
            	BigDecimal b = new BigDecimal(String.valueOf(entry.getValue()));
            	rowUpdateChange.put(entry.getKey().toUpperCase(), ColumnValue.fromDouble((Double) b.doubleValue()));
			} else if (entry.getValue() instanceof Boolean) {
                rowUpdateChange.put(entry.getKey(), ColumnValue.fromBoolean((Boolean) entry.getValue()));
            } else if (entry.getValue() instanceof Date) {
                String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
                        Locale.getDefault()).format((Date) entry.getValue());
                rowUpdateChange.put(entry.getKey(), ColumnValue.fromString(dateTime));
            } else {
                throw new RuntimeException("unsupport type: " + entry.getValue().getClass().getName());
            }
        }
        if (ignoreIfNotExist) {
            rowUpdateChange.setCondition(new Condition(RowExistenceExpectation.EXPECT_EXIST));
        }
        return updateRowRequest;
    }
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
    	AsyncClient asyncClient = new AsyncClient(Config.endpoint, Config.accessId,
                Config.accessKey, Config.instance);
    	
		OTSHelper otsHelper = new OTSHelper(asyncClient, "SURF_AWS_CHN_TAB");
    	otsHelper.countAllRows(1);
	}
}
