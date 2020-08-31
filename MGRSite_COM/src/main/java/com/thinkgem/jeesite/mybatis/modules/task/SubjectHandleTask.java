package com.thinkgem.jeesite.mybatis.modules.task;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.modules.sys.utils.ImageUploadUtil;
import com.thinkgem.jeesite.mybatis.common.utils.DateUtils;
import com.thinkgem.jeesite.mybatis.common.utils.FileUtils;
import com.thinkgem.jeesite.mybatis.modules.subject.entity.ProductImgDef;
import com.thinkgem.jeesite.mybatis.modules.subject.service.SupSubjectdefService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yang.kq
 * @version 1.0
 * @date 2020/6/17 17:17
 */
public class SubjectHandleTask {

    @Autowired
    private SupSubjectdefService supSubjectdefService;
    public void task() {

        //前缀路径
        String img_url= Global.getConfig("img_url");
        //专题产品图绝对路径
        String pngurl= img_url+Global.getConfig("subject_png_url")+"/";
        //新产品图片路径
        String pngurl2=Global.getConfig("subject_newpng_url");
            File file=new File(pngurl2);
            File[] fs=file.listFiles();
            if(fs!=null) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < fs.length; i++) {
                    if (fs[i].isFile()) {
                        String fileName = fs[i].getName();
                        String[] us= fileName.split("#");
                        String year=us[2].substring(0,4);
                        String month=us[2].substring(4,6);
                        //移动路径
                        String url=pngurl+us[0]+"/"+us[1]+"/"+year+"/"+month+"/"+us[2];
                        //复制文件
                        boolean flag=FileUtils.copyFile(fs[i].getPath(),url);
                        if(flag){
                            list.add(url);
                        }
                        //删除文件
                        fs[i].delete();
//                        if (fileName.endsWith("rar") || fileName.endsWith("zip")) {//解压缩
//                            list = FileUtils.unZipFilesAndFilePath(fs[i].getAbsolutePath(), pngurl);
//                            FileUtils.deleteFile(fs[i].getAbsolutePath());
//                        }
                    }
                }
                //信息入库
                if (list != null && list.size() > 0) {
                    supSubjectdefService.pngImgImport(list, pngurl,img_url);
                }
            }
            //上传到图片服务器，当前版本不需要
//        try {
//           for(String localurl:list) {
//               File file1 = new File(localurl);
//               InputStream inputStream = new FileInputStream(file1);//转换成输入流的方式
//               String host = Global.getConfig("img_addr");
//               int port = Integer.parseInt(Global.getConfig("img_port"));
//               String userName = Global.getConfig("img_name");
//               String password =Global.getConfig("img_password");
//               String remoteFile=Global.getConfig("img_url")+"/data/img";
//
//               ImageUploadUtil.uploadFile(host, port, userName, password, remoteFile, inputStream, "2020020615");
//           }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public static void main(String[] args) {


    }
}
