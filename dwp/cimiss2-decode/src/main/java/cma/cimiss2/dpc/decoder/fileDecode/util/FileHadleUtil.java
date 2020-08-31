package cma.cimiss2.dpc.decoder.fileDecode.util;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    /**
     * 获取指定路径下所有的文件
     * @param path
     * @return
     */
    public static void getFilePath(String path, List<String> list,String lastTime){
        File file=null;
        try {
            file = new File(path);
            File[] fs = file.listFiles();
            if (fs != null) {
                for (int i = 0; i < fs.length; i++) {
                    String fileName=fs[i].getName();
                    if (fs[i].isFile()&&(!(fileName.endsWith(".zip")||fileName.endsWith(".rar")))) {
                        boolean flag=true;
                        if(lastTime!=null) {
                            long lastFileTime = fs[i].lastModified();
                            long  lastTime1=Long.parseLong(lastTime);
                            if(lastFileTime<=lastTime1){
                                flag=false;
                            }
                        }
                        if(flag) {
                            list.add(fs[i].getAbsolutePath());
                        }
                    } else if (fs[i].isDirectory()) {
                        getFilePath(fs[i].getAbsolutePath(), list,lastTime);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
