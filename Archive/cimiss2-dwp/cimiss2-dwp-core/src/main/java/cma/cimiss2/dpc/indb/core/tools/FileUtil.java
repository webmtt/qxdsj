package cma.cimiss2.dpc.indb.core.tools;

import org.nutz.lang.Files;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class FileUtil {


    /**
     * @param path 需要格式化的路径
     * @return String 格式化后的路径
     * @throws
     * @Title: ValidFilePath
     * @Description: 格式化路径，前后增加斜杠
     */
    public static String ValidFilePath(String path) {
        path = FormatPath(path);
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * @param path 需要格式化的路径
     * @return String 格式化后的路径
     * @throws
     * @Title: FormatPath
     * @Description: 格式化路径，反斜杠转正斜杠，多斜杠转单一斜杠
     */
    public static String FormatPath(String path) {
        if (path.indexOf("//") != -1) {
            path = FormatPath(path.replace("//", "/"));
        }
        if (path.indexOf("\\\\") != -1) {
            path = FormatPath(path.replace("\\\\", "\\"));
        }
        if (path.indexOf("\\") != -1) {
            path = FormatPath(path.replace("\\", "/"));
        }
        return path;
    }

    /**
     * 文件移动
     *
     * @param filePath 要移动文件的文件绝对路径
     * @param _from    移动前文件的路径，配置文件中的配置
     * @param _to      移动后文件的路径，配置文件中的配置
     */

    public static boolean remove(String filePath, String _from, String _to) {
        filePath = FormatPath(filePath);
        _from = FormatPath(_from);
        _to = FormatPath(_to);
        _to = filePath.replaceAll(_from, _to);
        File fromFile = new File(filePath);
        File toFile = new File(_to);
        boolean result = false;
        try {
            result = Files.move(fromFile, toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void traverseFolder(String path, String event, BlockingQueue<String> files) {
    	
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
        	System.out.println("Loop File Path: " + file.toPath());
            File[] listFiles = file.listFiles();
            System.out.println("Folder contains # " + listFiles.length);
            for (int i = 0; i < listFiles.length; i++) {
                File tempFile = listFiles[i];
                
                if (tempFile.isDirectory()) {
                    traverseFolder(tempFile.getPath(), event, files);
                } else if (tempFile.isFile()) {
                    if (files.size() > 10000) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        files.offer(event + tempFile.getPath());
                    } else {
                        files.offer(event + tempFile.getPath());
                    }

                }
            }
        }

    }

    public static void traverseFolder(String path, String event, ConcurrentLinkedDeque<String> files) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            LogUtil.info("messageInfo", "=======listFiles=======" + listFiles.length);
            for (int i = 0; i < listFiles.length; i++) {
                File tempFile = listFiles[i];
                if (tempFile.isDirectory()) {
                    traverseFolder(tempFile.getPath(), event, files);
                } else if (tempFile.isFile()) {
                    if (files.size() > 10000) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
//                        files.offer(event+tempFile.getPath());
                        files.offer(event + tempFile.getPath());
                    }

                }
            }

        }

    }


    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> files = new LinkedBlockingQueue<String>();
        traverseFolder("D:\\data\\test1", "", files);
        while (!files.isEmpty()) {
            String take = files.take();
            remove(take, "D:\\data\\test1", "D:\\data\\test");
        }
    }

}
