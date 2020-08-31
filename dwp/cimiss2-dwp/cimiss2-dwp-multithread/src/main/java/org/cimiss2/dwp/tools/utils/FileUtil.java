package org.cimiss2.dwp.tools.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.FileUtils;
import org.cimiss2.dwp.tools.config.StartConfig;

public class FileUtil {


	/**
	 * @Title: ValidFilePath
	 * @Description: 格式化路径，前后增加斜杠
	 * @param path 需要格式化的路径
	 * @return String 格式化后的路径
	 * @throws
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
	 * @Title: FormatPath
	 * @Description: 格式化路径，反斜杠转正斜杠，多斜杠转单一斜杠
	 * @param path 需要格式化的路径
	 * @return String 格式化后的路径
	 * @throws
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
	
	public static void traverseFolder(String path, BlockingQueue<String> files) {
		
		File file = new File(path);
		if(file.exists() && file.isDirectory()) {
			File[] listFiles = file.listFiles();
			Arrays.sort(listFiles);
			if(listFiles != null) {
				for (int i = 0; i < listFiles.length; i++) {
					File tempFile = listFiles[i];
					if(tempFile.isDirectory()) {
						traverseFolder(tempFile.getPath(), files);
					}else if (tempFile.isFile()) {
						if(files.size() > 10000) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}else {
							if(!files.contains(tempFile.getPath())) {
								files.offer(tempFile.getPath());
							}
						}
					}
				}
			}
			
		}
	}
	
	public static boolean moveFile(String filepath) {

		String targetFile = filepath.replace(StartConfig.fileLoopPath(), StartConfig.moveFilePath());
		File destFile = new File(targetFile);
		
		File srcFile = new File(filepath);
		
		try {
			FileUtils.moveFile(srcFile, destFile);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static void main(String[] args) {
		BlockingQueue<String> files = new LinkedBlockingQueue<String>();
		traverseFolder("D:\\CIMISS2\\2018040100", files);
	}

}
