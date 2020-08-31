package com.thinkgem.jeesite.mybatis.modules.task;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.mybatis.common.utils.FileUtils;
import com.thinkgem.jeesite.mybatis.modules.filedecode.thread.FileDecodeThread;
import com.thinkgem.jeesite.mybatis.modules.report.service.ReportLogServices;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang.kq
 * @version 1.0
 * @date 2020/7/13 14:33
 */
public class FileDecodingHandleTask {
    private FileDecodeThread decodeThread=null;
    private Thread thread=null;
    @Autowired
    private ReportLogServices reportLogServices;
    public void task() {
        //新产品图片路径
        String path=Global.getConfig("file_deconde_path");
        List<String> list=new ArrayList<>();
        FileUtils.getFilePath(path,list);
        for (int i = 0; i < list.size(); i++) {
            if(decodeThread==null){
                decodeThread=new FileDecodeThread();
                thread=new Thread(decodeThread);
                thread.start();
            }
            decodeThread.offData(list.get(i));
            if(decodeThread.getReportLogServices()==null){
                decodeThread.setReportLogServices(reportLogServices);
            }
        }

    }
}


