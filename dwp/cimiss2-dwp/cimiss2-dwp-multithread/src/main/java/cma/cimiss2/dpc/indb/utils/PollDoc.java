package cma.cimiss2.dpc.indb.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PollDoc {
    public static void main(String[] args) {
        String path = "D:\\java\\work\\QXDSJ\\doc\\内蒙本地数据资料\\近地面通量\\20200620";        //要遍历的路径
              //获取其file对象
        List list = getNames(path);
        for (Object lists : list) {
            System.out.println(lists);
        }
    }
    public static List getNames(String path) {
        File file = new File(path);        //获取其file对象
        return findFile(file);
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
