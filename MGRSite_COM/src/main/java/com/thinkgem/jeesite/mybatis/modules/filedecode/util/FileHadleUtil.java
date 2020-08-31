package com.thinkgem.jeesite.mybatis.modules.filedecode.util;

import java.io.*;
import java.util.ArrayList;

/**
 * 文件读取工具类
 */
public class FileHadleUtil {
    /**
     * 按行读取文件
     * @param filename 文件路径
     * @return
     */
    public static ArrayList<String> readFromTextFile(File filename,String fileCode) {

        InputStreamReader reader = null;
        ArrayList<String> strArray = null;
        BufferedReader br=null;
        try {
            strArray = new ArrayList<String>();
            reader = new InputStreamReader(new FileInputStream(filename),fileCode);
           br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                strArray.add(line);
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strArray;
    }
}
