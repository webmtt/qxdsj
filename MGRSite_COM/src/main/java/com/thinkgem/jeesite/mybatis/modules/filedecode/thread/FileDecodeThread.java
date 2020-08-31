package com.thinkgem.jeesite.mybatis.modules.filedecode.thread;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.mybatis.common.utils.FileUtils;
import com.thinkgem.jeesite.mybatis.modules.filedecode.AFile.decode.ADecoding;
import com.thinkgem.jeesite.mybatis.modules.filedecode.Interface.IFileDecoding;
import com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean.*;
import com.thinkgem.jeesite.mybatis.modules.filedecode.decode.A0Decoding;
import com.thinkgem.jeesite.mybatis.modules.filedecode.decode.InsertDB;
import com.thinkgem.jeesite.mybatis.modules.report.service.ReportLogServices;


import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author yang.kq
 * @version 1.0
 * @date 2020/7/13 15:14
 */
public class FileDecodeThread implements Runnable{
    private BlockingQueue<String> queue=new LinkedBlockingQueue<>();
    private String day_cts_code=null;
    private String hour_cts_code=null;
    private String month_cts_code=null;
    private String meadow_cts_code=null;
    private  ReportLogServices reportLogServices=null;
    public FileDecodeThread(){
        day_cts_code= Global.getConfig("day_cts_code");
        hour_cts_code= Global.getConfig("month_cts_code");
        month_cts_code= Global.getConfig("hour_cts_code");
        meadow_cts_code= Global.getConfig("meadow_cts_code");
    }

    public ReportLogServices getReportLogServices() {
        return reportLogServices;
    }

    public void setReportLogServices(ReportLogServices reportLogServices) {
        this.reportLogServices = reportLogServices;
    }

    public void offData(String file){
        if(!queue.contains(file)) {
            this.queue.offer(file);
        }
    }

    @Override
    public void run() {
        IFileDecoding decoding=null;
        ParseResult<String> decodingInfo = new ParseResult<>(false) ;
        Map<String,Object> resultMap=null;
        String path=null;
        InsertDB db=null;
        while(true) {
            try {
                //无数据时线程挂起
                path = this.queue.take();
            } catch (Exception e) {
                e.printStackTrace();
            }
            File file = new File(path);
            String fileName = file.getName();
            if (fileName.toUpperCase().startsWith("A0")) {
                decoding = new A0Decoding();
            } else if (fileName.toUpperCase().startsWith("A")) {
                decoding = new ADecoding();
            }
            try {
                //开始解码
                resultMap = decoding.assemblyData(file, day_cts_code, hour_cts_code, month_cts_code, meadow_cts_code, fileName, null);
                //读取解码文件完成，删除文件
                FileUtils.deleteFile(path);
                List<DayValueTab> dayList = null;
                List<HourValueTab> hourList = null;
                List<SunLightValueTab> sunLightList = null;
                MonthValueTab mvt = null;
                List<MeadowValueTab> meadowList = null;
                List<RadiDigChnMulTab> rlist = null;
                if (resultMap != null) {
                    decodingInfo = (ParseResult<String>) resultMap.get("decodingInfo");
                }
                FirstData sf=null;
                // 判断是否解码成功
                if (decodingInfo.isSuccess()) {
                    dayList = (List<DayValueTab>) resultMap.get("dayValue");
                    hourList = (List<HourValueTab>) resultMap.get("hourValue");
                    sunLightList = (List<SunLightValueTab>) resultMap.get("sunLight");
                    mvt = (MonthValueTab) resultMap.get("monthValue");
                    meadowList = (List<MeadowValueTab>) resultMap.get("meadowValue");
                    rlist = (List<RadiDigChnMulTab>) resultMap.get("radiValue");
                    sf= (FirstData) resultMap.get("dataTime");
                    System.out.println(fileName+"文件开始入库");
                    if(db==null){
                        db=new InsertDB();
                    }
                    db.processDaySuccessReport(sf,rlist, meadowList, mvt, dayList, hourList, sunLightList, new Date(), fileName,reportLogServices);
                    System.out.println(fileName+"文件入库完成");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
