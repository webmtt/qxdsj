package com.thinkgem.jeesite.common.utils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件下载工具类
 */
public class FileDownLoadUtil {

    /**
     * 通过文件路径下载文件
     *
     * @param filePath 文件路径
     * @throws Exception
     */
    public static String download(String filePath, HttpServletResponse response, HttpServletRequest request) throws Exception {
        File file = new File(filePath);
            String zipFileName = file.getName();
            if(file.exists()){
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                OutputStream os = null;
                String finalFileName = "";
                try {
                    final String userAgent = request.getHeader("USER-AGENT");
                    if(StringUtils.contains(userAgent, "MSIE")||StringUtils.contains(userAgent,"Trident")){//IE浏览器
                        finalFileName = URLEncoder.encode(zipFileName,"UTF8");
                    }else if(StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
                        finalFileName = new String(zipFileName.getBytes(), "ISO8859-1");
                    }else{
                        finalFileName = URLEncoder.encode(zipFileName,"UTF8");//其他浏览器
                    }
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/force-download");// 设置强制下载不打开
                    response.addHeader("Content-Disposition", "attachment;fileName=" + finalFileName);// 设置文件名
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    return "下载成功";
                }catch (Exception e){

                }finally {

                    if(fis != null){
                        try {
                            fis.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(bis != null){
                        try {
                            bis.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(os != null){
                        try {
                            os.flush();
                            os.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        return "下载失败";
    }
}
