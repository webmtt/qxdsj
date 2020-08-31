//package org.cimiss2.dwp.tools.ots;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//
//import com.alicloud.openservices.tablestore.AsyncClient;
//
//public class OTSDbHelper {
//
//	private static OTSDbHelper instance;
//	private OTSHelper otsHelper;
//	private AsyncClient client = null;
//	private OTSDbHelper() {
//		client = new AsyncClient(Config.endpoint, Config.accessId, Config.accessKey, Config.instance);
//
//	}
//
//	public static synchronized OTSDbHelper getInstance() {
//		if(instance==null){
//            instance=new OTSDbHelper();
//        }
//        return instance;
//	}
//
//	/**
//	 * @Title: insert
//	 * @Description: TODO(这里用一句话描述这个方法的作用)
//	 * @param tablename  数据库表名称
//	 * @param list  解码结果集
//	 * @return OTSBatchResult  返回结果集
//	 * @throws:
//	 */
//	public OTSBatchResult insert(String tablename, List<Map<String, Object>> list) {
//		otsHelper = new OTSHelper(client, tablename);
//		return otsHelper.insert(list, true);
//	}
//
//	public OTSBatchResult insert(String tablename, String secondaryKeyName,List<Map<String, Object>> list) {
//		otsHelper = new OTSHelper(client, tablename, secondaryKeyName);
//		return otsHelper.insert(list, true);
//	}
//
//
//	/**
//	 * @Title: insert
//	 * @Description: TODO(这里用一句话描述这个方法的作用)
//	 * @param tablename
//	 * @param map
//	 * @throws ExecutionException
//	 * @throws Exception
//	 * @throws InterruptedException
//	 * @throws:
//	 */
//	public void insert(String tablename, Map<String, Object> map) throws Exception {
//		otsHelper = new OTSHelper(client, tablename);
//		otsHelper.insert(map, true);
//
//	}
//
//	public void insert(String tablename, Map<String, Object> map, boolean isReWrite) throws Exception{
//		otsHelper = new OTSHelper(client, tablename);
//		otsHelper.insert(map, isReWrite);
//
//	}
//
//	public void insert(String tablename,List<Map<String, Object>> maps, boolean isReWrite) throws Exception{
//		otsHelper = new OTSHelper(client, tablename);
//		otsHelper.insert(maps, isReWrite);
//
//	}
//
//	public void insert_ex(String tablename, Map<String, Object> map) throws Exception {
//		otsHelper = new OTSHelper(client, tablename);
//		otsHelper.insert(map, true);
//	}
//
//	/**
//	 * @Title: insert
//	 * @Description: TODO(这里用一句话描述这个方法的作用)
//	 * @param tablename
//	 * @param secondaryKeyName
//	 * @param map
//	 * @return
//	 * @throws InterruptedException
//	 * @throws ExecutionException void
//	 * @throws:
//	 */
//	public void insert(String tablename, String secondaryKeyName, Map<String, Object> map) throws Exception{
//		otsHelper = new OTSHelper(client, tablename, secondaryKeyName);
//		otsHelper.insert_ex(map, true);
//	}
//
//	/**
//	 * @Title: update
//	 * @Description: TODO(这里用一句话描述这个方法的作用)
//	 * @param tablename
//	 * @param row
//	 * @throws Exception
//	 * @throws:
//	 */
//	public void update(String tablename, Map<String, Object> row) throws Exception {
//		otsHelper = new OTSHelper(client, tablename);
//		otsHelper.update(row);
//	}
//
//	public void update(String tablename, Map<String, Object> row, boolean ignoreIfNotExist) throws Exception {
//		otsHelper = new OTSHelper(client, tablename);
//		otsHelper.update(row, ignoreIfNotExist);
//	}
//
//	/**
//	 * @Title: update
//	 * @Description: TODO(更新一条记录)
//	 * @param tablename 数据库表名称
//	 * @param secondaryKeyName 第二主键的名称
//	 * @param row
//	 * @throws Exception
//	 * @throws:
//	 */
//	public void update(String tablename, String secondaryKeyName, Map<String, Object> row) throws Exception {
//		otsHelper = new OTSHelper(client, tablename, secondaryKeyName);
//		otsHelper.update(row);
//	}
//
//	public void update(String tablename, String secondaryKeyName, Map<String, Object> row, boolean ignoreIfNotExist ) throws Exception {
//		otsHelper = new OTSHelper(client, tablename, secondaryKeyName);
//		otsHelper.update(row, ignoreIfNotExist);
//	}
//
//	public void updateExistRow(Map<String, Object> row) throws Exception {
//        otsHelper.updateExistRow(row);
//    }
//
//
//
//}
