package cma.cimiss2.dpc.indb.agme.dc_agme_grass_species;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.config.StartConfig;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * -------------------------------------------------------------------------------
 * <br>.
 *
 * @author zhaoxiaojun
 * ---------------------------------------------------------------------------------
 * @Title: GrassSpeciesMainThread.java
 * @Package cma.cimiss2.dpc.indb.agme.dc_agme_grass_species
 * @Description: 天然草场多样性
 * <p>
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年10月12日 下午2:42:42  Initial creation.
 * </pre>
 */
public class GrassSpeciesMainThread {

    public static void start(){
        BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
        int threadCount = 1;   // 线程池的大小
        ArrayList<CTSCodeMap> ctsCodeMaps = new ArrayList<>();
        threadCount = StartConfig.getThreadCount();
        String ctS = StartConfig.ctsCode("S002_cts_code");
        String sodSs[] = StartConfig.sodCodes("S002_sod_code");
        String reportSods[] = StartConfig.reportSodCodes();
        String valuetables[] = StartConfig.valueTables("S002_value_table_name");
        String reportTable = StartConfig.reportTable();
        for(int i = 0; i < valuetables.length; i ++){
            CTSCodeMap codeMap = new CTSCodeMap();
            codeMap.setCts_code(ctS);
            codeMap.setSod_code(sodSs[i]);
//            codeMap.setReport_sod_code(reportSods[i]);
            codeMap.setValue_table_name(valuetables[i]);
            codeMap.setReport_table_name(reportTable);
            ctsCodeMaps.add(codeMap);
        }
        // 启动固定线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        // 消息中间件
        if(StartConfig.fileLoop() == 0) {
            for (int i = 0; i < threadCount; i++) {
                executor.execute(new GrassSpeciesSubThread(diQueues));
            }
        }
    }
}