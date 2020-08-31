package com.thinkgem.jeesite.mybatis.modules.task;


import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.mybatis.common.utils.DateUtils;
import com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean.MonthValueTab;
import com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean.YearValueTab;
import com.thinkgem.jeesite.mybatis.modules.filedecode.decode.YearDecoding;
import com.thinkgem.jeesite.mybatis.modules.filedecode.service.YearInfoHandleService;

import com.thinkgem.jeesite.mybatis.modules.report.entity.ReportLogInfo;
import com.thinkgem.jeesite.mybatis.modules.report.service.ReportLogServices;
import org.apache.lucene.util.RamUsageEstimator;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 年值数据统计值定时任务
 * @author yang.kq
 * @version 1.0
 * @date 2020/4/20 17:53
 */

public class YearDataHandleTask {

    @Autowired
    private YearInfoHandleService yearInfoHandle;
    @Autowired
    private ReportLogServices reportLogServices;
    public void task(){
        try {
            YearDecoding decoding=new YearDecoding();
            String yearcts= Global.getConfig("year_cts_code");
            String yearTable= Global.getConfig("year_table_name");
            //要执行的任务逻辑写在这里
            List<MonthValueTab> prelist=yearInfoHandle.getLastYearMonthData();
            List<YearValueTab> list=null;
            if(prelist.size()>0) {
                list = decoding.packYearValue(prelist, yearcts, yearInfoHandle);
            }
            if(list!=null) {
                yearInfoHandle.insertYearData(list, yearTable);
                ReportLogInfo reportLogInfo=new ReportLogInfo();
                InetAddress ia=null;
                try {
                    ia = InetAddress.getLocalHost();
                }catch (Exception e){
                    e.printStackTrace();
                }
                reportLogInfo.setId(UUID.randomUUID()+"");
                reportLogInfo.setTime(DateUtils.getDateTime());
                reportLogInfo.setOperitorType("1");
                reportLogInfo.setStationInfo(list.get(0).getV01300()+"");
                reportLogInfo.setDataNum(RamUsageEstimator.humanSizeOf(list));
                reportLogInfo.setDataType("3");
                if(ia!=null){
                    reportLogInfo.setAddr(ia.getHostAddress());
                }
                reportLogServices.insertLog(reportLogInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("定时任务-------年值统计失败！");
        }

    }
}

