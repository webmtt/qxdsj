package org.cimiss2.dwp.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PollDoc {
    public static void main(String[] args) {
        String path = "C:\\Users\\piesat\\Desktop\\近地面通量\\20200601";        //要遍历的路径
        File file = new File(path);        //获取其file对象
        List list = findFile(file);
        for (Object lists : list) {
            System.out.println(lists);
        }
    }

    private static List findFile(File file) {
        List list = new ArrayList<>();
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (f.isDirectory()) {    //若是目录，则递归打印该目录下的文件
                list = findFile(f);
            }
            if (f.isFile()) {        //若是文件，直接打印
                list.add(f);
            }
        }
        return list;
    }


}
