package cma.cimiss2.dpc.indb.xml.common_xml.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.DiEiConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alicloud.openservices.tablestore.ClientException;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
/**
 * ************************************
 * @ClassName: OTSService
 * @Auther: liuwenxia
 * @Date: 2019/3/27 10:47
 * @Description: 模板配置ots入库
 * @Copyright: All rights reserver.
 * ************************************
 */

public class OTSService {
    public static BlockingQueue<StatDi> diQueues;
    public static Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * ots入库
     * @param decodeResult
     * @param filePath
     * @return DataBaseAction
     */
    public static DataBaseAction processSuccessReport(ParseResult<HashMap<String,List<Map<String,Object>>>> decodeResult, String filePath) {
        List<HashMap<String,List<Map<String,Object>>>> tablesValues = decodeResult.getData();
        //有多个表，各个表入库一次，获取一个结果 
        int successSize = 0;
        try {
            for (HashMap<String,List<Map<String,Object>>> tablesValue : tablesValues) {
                successSize += insertTable(tablesValue, filePath);
            }
        }catch (Exception e) {
            e.printStackTrace();
            infoLogger.error("Database connect  error:", e);
            return DataBaseAction.CONNECTION_ERROR;
        }
        if(successSize > 0) {
            
            return DataBaseAction.SUCCESS;// 至少一条成功,返回SUCCESS
        }
        return DataBaseAction.INSERT_ERROR;
    }
    /**
             *      要素表入库
     * @param 
     * @param filePath
     * @return OTSBatchResult
     */
    private static  int insertTable(HashMap<String,List<Map<String,Object>>> tableValue,String filePath) {
        int successRowCount = 0;
        int totalCount = 0;
        for (String table : tableValue.keySet()) {
            List<Map<String, Object>> rows = tableValue.get(table);
            totalCount += rows.size();
            for (Map<String, Object> row : rows) {
                boolean flag = insert(row,table,filePath);
                if(flag) {
                    successRowCount ++;
                }
            }
        }
        infoLogger.info(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
        infoLogger.info(" INSERT COUNT : " + totalCount + "\n");
        infoLogger.info(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
        infoLogger.info(" INSERT FAILED COUNT : " + (totalCount - successRowCount) + "\n");
        return successRowCount;
    }
    
    /**
         *     单条记录入库并记录DI信息
    * @param data
    * @param filePath
    * @return OTSBatchResult
    */
    @SuppressWarnings("unused")
    private static boolean insert(Map<String,Object> data,String table,String filePath) {
        Map<String, Object> row = data;
        StatDi di = new StatDi();
        try {
        File file = new File(filePath);
        String fileName = file.getName();
        di.setFILE_NAME_O(fileName);
        di.setDATA_TYPE(StartConfig.sodCode());
        di.setDATA_TYPE_1(StartConfig.ctsCode());
        di.setTT(StartConfig.sodCode());
        di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
        di.setPROCESS_START_TIME(TimeUtil.getSysTime());
        di.setFILE_NAME_N(fileName);
        di.setBUSINESS_STATE("0"); //0成功，1失败
        di.setPROCESS_STATE("0");  //0成功，1失败
        di.setTT(DiEiConfig.TT);
        Set<String> fields = data.keySet();
        if(DiEiConfig.DI_VT == 0) {
            di.setIIiii(DiEiConfig.IIiii);
            di.setLONGTITUDE(DiEiConfig.Lon);
            di.setLATITUDE(DiEiConfig.Lat);
        }else if(DiEiConfig.DI_VT == 1) {
            HashMap<String,String> rules = new HashMap<String,String>();                                
            rules.put("IIiii",DiEiConfig.IIiii);
            rules.put("Lon",DiEiConfig.Lon);
            rules.put("Lat",DiEiConfig.Lat);
            for(String ruleName:rules.keySet()) {
                String rule = rules.get(ruleName);
                String[] ruleElement = rule.split(";");
                String value = "";
                if(ruleElement.length == 2) {
                    value = ruleElement[1];
                }else {
                    value = ruleElement[0];
                }
                String[] fs = ruleElement[0].split("_");
                for(String field:fields) {
                    value = value.replace(field,data.get(field)+"");
                }
                
                if(ruleName == "IIiii") di.setIIiii(value);
                if(ruleName == "Lon") di.setLONGTITUDE(value);
                if(ruleName == "Lat") di.setLATITUDE(value);
            }                               
        }
        String rule = DiEiConfig.DATE_TIME;
        String[] ruleElement = rule.split(";");
        String value = "";
        if(ruleElement.length == 2) {
            value = ruleElement[1];
        }else {
            value = ruleElement[0];
        }
        String[] fs = ruleElement[0].split("_");
        for(String field:fields) {
            value = value.replace(field,data.get(field)+"");
        }
        di.setDATA_TIME(value);
        di.setPROCESS_END_TIME(TimeUtil.getSysTime());
        di.setRECORD_TIME(TimeUtil.getSysTime());
            OTSDbHelper.getInstance().insert(table, row);
            diQueues.offer(di);
            return true;
        } catch (Exception e) {
            infoLogger.error(e.getMessage() + "\n");
            infoLogger.error(row + "\n");
            di.setPROCESS_STATE("1");
            diQueues.offer(di);
            if (e.getClass() == ClientException.class) {
                infoLogger.error("ots database connect failed");
            }
            return false;
        }
    }
}
