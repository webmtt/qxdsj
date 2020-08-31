package cma.cimiss2.dpc.indb.sevp.dc_typh_path_nodi.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.sevp.Typhoon1;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSBatchResult;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;

// TODO: Auto-generated Javadoc
/**
 * ************************************.
 *
 * @ClassName: OTSService
 * @Auther: dangjinhu
 * @Date: 2019/3/30 10:18
 * @Description: 台风路径和台风预测--入库--ots
 * 全球台风强度预报资料 M.0004.0003.R001
 * 全球台风强度预报资料 M.0004.0002.R001  
 * @Copyright: All rights reserver.
 * ************************************
 */

public class OTSService {

    /** The list di. */
    private static List<StatDi> listDi = new ArrayList<StatDi>();
    
    /** The Constant messageLogger. */
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    
    /** The Constant infoLogger. */
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    
    /** The di queues. */
    public static BlockingQueue<StatDi> diQueues;
    

    public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	/**
     * ots入库.
     *
     * @param pa the pa
     * @param rece_time the rece time
     * @param filePath the file path
     * @param log the log
     * @return the data base action
     */
    public static DataBaseAction processSuccessReport(ParseResult<Typhoon1> pa, Date rece_time, String filePath, StringBuffer log) {
        String reportTable = StartConfig.reportTable();
        String sodCode = StartConfig.sodCode(); // 入库值对应D_DATA_ID
        String ctsCode = StartConfig.ctsCode();
        File file = new File(filePath);
        String fileN = file.getName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Typhoon1> typhoons = pa.getData();
        List<Boolean> list = new ArrayList<>(); // 统计日期格式异常格式
        List<Map<String, Object> > rows = new ArrayList<>();
        for (int i = 0; i < typhoons.size(); i++) {
            Map<String, Object> row = new HashMap<>();
            Typhoon1 typhoon = typhoons.get(i);
            // di
            StatDi di = new StatDi();
            di.setFILE_NAME_O(fileN);
            di.setDATA_TYPE(sodCode);
            di.setDATA_TYPE_1(ctsCode);
            di.setTT("台风预测|实况");
            di.setTRAN_TIME(TimeUtil.date2String(rece_time, "yyyy-MM-dd HH:mm:ss"));
            di.setPROCESS_START_TIME(TimeUtil.getSysTime());
            di.setFILE_NAME_N(fileN);
            di.setBUSINESS_STATE("0"); //0成功，1失败
            di.setPROCESS_STATE("0");  //0成功，1失败
            String date = typhoon.getDate();
            if (date.length() != 12) {
                log.append("报文时间长度错误：").append(date);
                list.add(false); // 失败
                continue; // 读取下一条记录
            }
            // 生成记录标识
            String recordId;
            if (getFileTyle(fileN)) {
                recordId = date + "_"  + typhoon.getTyphoonNumber()+ "_"+ typhoon.getLatitude() + "_" + typhoon.getLongitude();
            } else {
                recordId = date + "_" + typhoon.getForestTime() + "_" + typhoon.getTyphoonNumber() + "_" + typhoon.getLatitude() + "_" + typhoon.getLongitude();
            }
            String[] ymdhm = TimeUtil.getYmdhm(date); // 获取时间
            row.put("D_RECORD_ID",recordId); // 记录标识
            row.put("D_DATA_ID",sodCode); // 资料标识
            row.put("D_IYMDHM",TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss")); // 入库时间
            row.put("D_RYMDHM",TimeUtil.date2String(rece_time,"yyyy-MM-dd HH:mm:ss")); // 收到时间
            row.put("D_UPDATE_TIME",TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss")); // 更新时间
            row.put("D_DATETIME",TimeUtil.String2Date(date, "yyyyMMddHHmm")); // 资料时间
            row.put("V04001_02",ymdhm[0]); // 资料年
            row.put("V04002_02",ymdhm[1]); // 资料月
            row.put("V04003_02",ymdhm[2]); // 资料日
            row.put("V04004_02",ymdhm[3]); // 资料时
            row.put("V04005_02",ymdhm[4]); // 资料分
            row.put("V04006_02",0); // 资料秒
            row.put("V05310",typhoon.getArea()); // 数据区域范围
            row.put("V_TYPH_NAME",typhoon.getTyphoonName()); // 台风名
            row.put("V04001",ymdhm[0]); // 资料产生年
            row.put("V04002",ymdhm[1]); // 资料产生月
            row.put("V04003",ymdhm[2]); // 资料产生日
            row.put("V04004",ymdhm[3]); // 资料产生时
            row.put("V04005",ymdhm[4]); // 资料产生分
            row.put("V04006",0); // 资料产生秒
            row.put("V01334",typhoon.getTyphoonNumber()); // 台风本地编号
            if (!getFileTyle(fileN)) {
                row.put("V04320",typhoon.getForestTime() ); // 预报时效
            }
            row.put("V05001",typhoon.getLatitude()); // 中心纬度(定位)
            row.put("V06001",typhoon.getLongitude()); // 中心经度(定位)
//            row.put("V19303",typhoon.getStrength()); // 台风强度
            row.put("V11042",typhoon.getStrength()); // 台风强度
            row.put("V01332","999999"); //台风国际编号
            row.put("V_MODE_CODE","ATOF"); 
            row.put("D_SOURCE_ID",ctsCode); // 资料四级编码
            rows.add(row);
            diQueues.offer(di);
        }
        long count = list.stream().filter(s -> s).count();
        if (count == typhoons.size()) {
            // 若相等,所有记录日期异常
            return DataBaseAction.INSERT_ERROR;
        } else{
            OTSBatchResult result = OTSDbHelper.getInstance().insert(reportTable, rows);
            log.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
            log.append(" INSERT SUCCESS COUNT : " + result.getSuccessRowCount() + "\n");
            log.append(" INSERT FAILED COUNT : " + result.getFailedRowCount() + "\n");
            log.append(" INSERT FAILED CONTENT : " + result.getFailedRows() + "\n");
            return DataBaseAction.SUCCESS;
        }

    }

    /**
     * 获取文件类型：active 为 true 和 forecast.
     *
     * @param name the name
     * @return the file tyle
     */
    private static boolean getFileTyle(String name) {
        if (name.contains("active")) {
            return true;
        }
        return false;
    }
}
