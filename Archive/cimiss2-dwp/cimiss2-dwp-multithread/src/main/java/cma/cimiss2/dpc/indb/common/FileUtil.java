package cma.cimiss2.dpc.indb.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;

/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description:</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName: 多线程数据处理</b> cimiss2-dwp-multithread
* <br><b>PackageName: 工具类包</b> cma.cimiss2.dpc.indb.common
* <br><b>ClassName: 文件以及文件夹工具类</b> FileUtil
* <br><b>Date:</b> 2019年5月7日 下午9:26:04
 */
public class FileUtil {
	/**
	 * 递归遍历文件夹
	 * @param filePath  遍历文件的路径
	 * @param filenames  遍历的所有文件的绝对路径
	 */
	public static void getFiles(String filePath, BlockingQueue<String> filenames){
		File file = new File(filePath);
		if(file != null && file.isDirectory()) {
			File[] files = file.listFiles();
			if(files != null) {
				for (int i = 0; i < files.length; i++) {
					// 判断如果为文件夹，则继续遍历
					if(files[i].isDirectory()) {
						getFiles(files[i].getPath(), filenames);
					}else if(files[i].isFile()){
						filenames.add(files[i].getPath());
					}
				}
			}
				
		}
	}
	
	/**
	 * 移动文件
	 * @param srcFile  源文件
	 * @param destFile  目标文件
	 * @throws IOException
	 */
	public static void moveFile(final File srcFile, final File destFile) throws IOException {
		if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        }
        if (destFile.exists()) {
            throw new FileExistsException("Destination '" + destFile + "' already exists");
        }
        if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        }
        final boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
        	if(destFile.exists()) {
        		return;
        	}
            FileUtils.copyFile(srcFile, destFile);
            if (!srcFile.delete()) {
                FileUtils.deleteQuietly(destFile);
                throw new IOException("Failed to delete original file '" + srcFile +
                        "' after copy to '" + destFile + "'");
            }
        }
	}

}
