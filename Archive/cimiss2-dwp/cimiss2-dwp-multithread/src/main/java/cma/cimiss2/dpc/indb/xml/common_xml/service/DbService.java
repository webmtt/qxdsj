package cma.cimiss2.dpc.indb.xml.common_xml.service;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.DiEiConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.xml.XmlDecodeFile;

/**
 * ************************************
 * 
 * @ClassName: DbService
 * @Auther: liuwenxia
 * @Date: 2019/4/9 18:22
 * @Description:
 * @Copyright: All rights reserver. ************************************
 */

public class DbService {

    public static BlockingQueue<StatDi> diQueues;
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

    /**
     * 报文数据入库
     * 
     * @param parseResult
     * @param filePath
     * @return DataBaseAction
     */
    public static DataBaseAction processSuccessReport(
            ParseResult<HashMap<String, List<Map<String, Object>>>> parseResult, String filePath) {
        DruidPooledConnection connection = null;
        try {
        	if (StartConfig.getDatabaseType() == 0) {
                connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
            } else if (StartConfig.getDatabaseType() == 1) {
                connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            }
            try {                
                List<HashMap<String, List<Map<String, Object>>>> datas = parseResult.getData();
                // 批量插入
                DataBaseAction action = insertDB(connection, datas, filePath);
                if (DataBaseAction.SUCCESS == action) {
                    return DataBaseAction.SUCCESS;
                } else {
                    return DataBaseAction.INSERT_ERROR;
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            infoLogger.error("Database close unusual！", e);
            return DataBaseAction.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            infoLogger.error("Error:", e);
            return DataBaseAction.CONNECTION_ERROR;
        }
    }

    /**
     * 入库详细
     * 
     * @param connection
     * @param datas 解码数据集合
     * @param filePath 文件路径
     * @throws Exception
     */
    private static DataBaseAction insertDB(Connection connection,List<HashMap<String, List<Map<String, Object>>>> maps, String filePath) throws Exception {
        List<String> sqls = new ArrayList<>();
        List<StatDi> dis = new ArrayList<>();
        for (HashMap<String, List<Map<String, Object>>> map : maps) {
            for (String mKey : map.keySet()) {
                List<Map<String, Object>> datas = map.get(mKey);
                String table = mKey;// 表名
                // 把sqls和dis拿出来
                parseData(table, datas, sqls, dis, filePath);
            }
        }
        // 是否存入数据成功，存入一条即成功
        boolean flag = false;
        // 为了在批量执行失败后，可以改用一条条插入 1、把数据都存起来备用 commit的时机是关键
        // 将list分组
        List<List<String>> splitSqls = splitList(sqls, 200);
        List<List<StatDi>> splitDiss = splitList(dis, 200);
        Statement st = null;
        if (connection.getAutoCommit()) {
            connection.setAutoCommit(false);
        }
        try {
	        st = connection.createStatement();
        }catch(SQLException e) {
        	infoLogger.error("error in:connection.createStatement()" + e);
        	return DataBaseAction.INSERT_ERROR;
        }
//        try {
//		    if (StartConfig.getDatabaseType() == 1) {
//		        st.execute("select last_txc_xid()");
//		    }
//        }catch(SQLException e) {
//        	infoLogger.error("error in:Statement.execute(\"select last_txc_xid()\"),database type not correct" + e);
//        	return DataBaseAction.INSERT_ERROR;
//        }
        for (int i = 0; i < splitSqls.size(); i++) {
            List<String> sqlList = splitSqls.get(i);
            List<StatDi> disList = new ArrayList<StatDi>();
            if(splitDiss.size() > i)
            	disList = splitDiss.get(i);
            
            try {
                for (String sql : sqlList) {
                    st.addBatch(sql);                   
                }
                int[] arr = st.executeBatch();
                connection.commit();
                // 把di信息提交
                for (StatDi di : disList) {
                    diQueues.offer(di);
                }
                if (!flag) {
                    flag = checkSuccess(arr);
                }
            } catch (SQLException e) {
                connection.rollback();
                if (!connection.getAutoCommit()) {
                    connection.setAutoCommit(true);
                }
                for (int j = 0; j < sqlList.size(); j++) {
                    String sql = sqlList.get(j);
                    try {
                        st.execute(sql);
                    } catch (SQLException sQLException) {
                        // 一次提交出现问题
                        infoLogger.error("error in:" + sql, sQLException);
                        if (disList != null && disList.size() > 0) {
                            StatDi statDi = disList.get(j);
                            statDi.setBUSINESS_STATE("1");// 0成功，1失败
                            statDi.setPROCESS_STATE("1");// 0成功，1失败
                            diQueues.offer(statDi);
                        }
                    }
                }
                System.out.println("批量插入完成！");
            }
        }
        if (flag) {
            return DataBaseAction.SUCCESS;
        } else {
            return DataBaseAction.INSERT_ERROR;
        }
    }

    /**
     * 拼接sql
     * @param valueSort valueSort
     * @return String
     */
    private static String getSqlValue(ArrayList<String> valueSort, ArrayList<String> typeSort) {
        StringBuffer value = new StringBuffer();
        for (int index = 0; index < valueSort.size(); index++) {
            if ("string".equals(typeSort.get(index).toLowerCase())
                    || "datetime".equals(typeSort.get(index).toLowerCase())) {
                value.append("'" + valueSort.get(index).trim() + "',");
            } else {
                value.append(valueSort.get(index).trim() + ",");
            }
        }
        return value.substring(0, value.length() - 1) + ")";
    }

    /**
     * 获取文件类型：active 为 true 和 forecast
     * @param name
     * @return
     */
    @SuppressWarnings("unused")
    private static boolean getFileTyle(String name) {
        if (name.contains("active")) {
            return true;
        }
        return false;
    }

    /**
     * 获取sql头
     * @param reportTable
     * @param fieldsName
     * @param fieldsSort
     * @return
     */
    private static String getSqlhead(String reportTable, Set<String> fieldsName,
            ArrayList<String> fieldsSort) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into " + reportTable + " (");
        Iterator<String> iterator = fieldsName.iterator();
        while (iterator.hasNext()) {
            String str = iterator.next();
            sql.append(str + ",");
            fieldsSort.add(str);
        } ;
        return sql.substring(0, sql.length() - 1) + ") VALUES (";
    }

    // 把list切片
    public static <T> List<List<T>> splitList(List<T> srcList, int splitSize) throws Exception {
        List<List<T>> lList = new ArrayList<>();
        if (splitSize <= 0) {
            throw new Exception("分片参数错误！");
        }
        // 获取被拆分的数组个数
        int arrSize = srcList.size() % splitSize == 0 ? srcList.size() / splitSize
                : srcList.size() / splitSize + 1;
        for (int i = 0; i < arrSize; i++) {
            List<T> subList = new ArrayList<T>();
            // 把指定索引数据放入到list中
            for (int j = i * splitSize; j <= splitSize * (i + 1) - 1; j++) {
                if (j <= srcList.size() - 1) {
                    subList.add(srcList.get(j));
                }
            }
            lList.add(subList);
        }
        return lList;
    }

    // 检查批量插入是否成功插入一条数据
    private static boolean checkSuccess(int[] arr) {
        for (int i : arr) {
            if (i == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param table
     * @param datas
     * @param sqls
     * @param dis
     * @param filePath
     * @throws ParseException
     */
    private static void parseData(String table, List<Map<String, Object>> datas, List<String> sqls,
            List<StatDi> dis, String filePath) throws ParseException {
        String ctsCode = StartConfig.ctsCode();
        String sodCode = StartConfig.sodCode();// 入库值对应D_DATA_ID
        File file = new File(filePath);
        String fileName = file.getName();
        if (datas.size() > 0) {
            // 根据数据获取字段名
            ArrayList<String> fieldSort = new ArrayList<>();
            Map<String, Object> mData = datas.get(0);
            Set<String> fields = mData.keySet();
            String headSql = getSqlhead(table, fields, fieldSort);
            // 获取字段的类型
            Map<String, String> fieldTypeMap = getFieldType();
            for (Map<String, Object> data : datas) {
                StringBuffer sql = new StringBuffer();
                ArrayList<String> valueSort = new ArrayList<String>();
                ArrayList<String> typeSort = new ArrayList<String>();
                for (int index = 0; index < fieldSort.size(); index++) {
                    valueSort.add(data.get(fieldSort.get(index)).toString());
                    typeSort.add(fieldTypeMap.get(fieldSort.get(index)));
                }
                sql.append(headSql).append(getSqlValue(valueSort, typeSort));
                //System.out.println(sql);
                sqls.add(sql.toString());

                StatDi di = new StatDi();
                di.setFILE_NAME_O(fileName);
                di.setDATA_TYPE(sodCode);
                di.setDATA_TYPE_1(ctsCode);
                di.setTT(sodCode);
                di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
                di.setPROCESS_START_TIME(TimeUtil.getSysTime());
                di.setFILE_NAME_N(fileName);
                di.setBUSINESS_STATE("0"); // 0成功，1失败
                di.setPROCESS_STATE("0"); // 0成功，1失败
                di.setTT(DiEiConfig.TT);
                if (DiEiConfig.DI_VT == 0) {
                    di.setIIiii(DiEiConfig.IIiii);
                    di.setLONGTITUDE(DiEiConfig.Lon);
                    di.setLATITUDE(DiEiConfig.Lat);

                } else if (DiEiConfig.DI_VT == 1) {
                    HashMap<String, String> rules = new HashMap<String, String>();
                    rules.put("IIiii", DiEiConfig.IIiii);
                    rules.put("Lon", DiEiConfig.Lon);
                    rules.put("Lat", DiEiConfig.Lat);
                    for (String ruleName : rules.keySet()) {
                        String rule = rules.get(ruleName);
                        String[] ruleElement = rule.split(";");
                        String value = "";
                        if (ruleElement.length == 2) {
                            value = ruleElement[1];
                        } else {
                            value = ruleElement[0];
                        }
                        @SuppressWarnings("unused")
                        String[] fs = ruleElement[0].split("_");
                        for (String field : fields) {
                        	String tmp = data.get(field) + "";
                        	value = value.replace(field, tmp);
                        }

                        if (ruleName == "IIiii")
                            di.setIIiii(value);
                        if (ruleName == "Lon")
                            di.setLONGTITUDE(value);
                        if (ruleName == "Lat")
                            di.setLATITUDE(value);
                    }
                }

                String rule = DiEiConfig.DATE_TIME;
                String[] ruleElement = rule.split(";");
                String value = "";
                if (ruleElement.length == 2) {
                    value = ruleElement[1];
                } else {
                    value = ruleElement[0];
                }
                @SuppressWarnings("unused")
                String[] fs = ruleElement[0].split("_");
                for (String field : fields) {
                	String tmp = data.get(field) + "";
                	value = value.replace(field, tmp);
                }
                di.setDATA_TIME(value);
                di.setPROCESS_END_TIME(TimeUtil.getSysTime());
                di.setRECORD_TIME(TimeUtil.getSysTime());
                dis.add(di);
            }
        }
    }

    private static Map<String, String> getFieldType() {
        Map<String, String> res = new HashMap<>();
        for (XmlDecodeFile xmlDecodeFile : XmlDecodeFile.handleFieldDecoder) {
            res.put(xmlDecodeFile.getElmentValue(), xmlDecodeFile.getDataType());
        }
        return res;
    }
}
