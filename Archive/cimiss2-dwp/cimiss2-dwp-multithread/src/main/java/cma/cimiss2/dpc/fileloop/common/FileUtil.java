package cma.cimiss2.dpc.fileloop.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileUtil {
	
	/**
	    *   写入文件,末尾自动添加 utf-8 编码
	 * @param path
	 * @param str
	 */
	public static void writeLog(String path, String str) {
		try {
			File file = new File(path);
			if (!file.exists())
				file.createNewFile();
			FileOutputStream out = new FileOutputStream(file); // true表示追加
			StringBuffer sb = new StringBuffer();
			sb.append(str + "\r\n");
			out.write(sb.toString().getBytes("utf-8"));//
			out.close();
		} catch (IOException ex) {
			System.out.println(ex.getStackTrace());
		}
	}
	
	
	/**
	    *   创建文件
	 * @param file_path
	 */
	public static void createNewFile(String file_path) {
		File myFilePath = new File(file_path);
		try {
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	    *   写入文件,末尾自动添加
	 * @param path
	 * @param str
	 * @param is_append
	 * @param encode
	 */
	public static void writeLog(String path, String str, boolean is_append, String encode) {
		try {
			File file = new File(path);
			if (!file.exists())
				file.createNewFile();
			FileOutputStream out = new FileOutputStream(file, is_append); // true表示追加
			StringBuffer sb = new StringBuffer();
			sb.append(str + "\r\n");
			out.write(sb.toString().getBytes(encode));//
			out.close();
		} catch (IOException ex) {
			System.out.println(ex.getStackTrace());
		}
	}

	/**
	   *    得到一个文件夹下所有文件
	 * @param fold_path
	 * @return
	 */
	public static List<String> getAllFileNameInFold(String fold_path) {
		List<String> file_paths = new ArrayList<String>();
		LinkedList<String> folderList = new LinkedList<String>();
		folderList.add(fold_path);
		while (folderList.size() > 0) {
			File file = new File(folderList.peekLast());
			folderList.removeLast();
			File[] files = file.listFiles();
			ArrayList<File> fileList = new ArrayList<File>();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					folderList.add(files[i].getPath());
				} else {
					fileList.add(files[i]);
				}
			}
			for (File f : fileList) {
				file_paths.add(f.getAbsoluteFile().getPath());
			}
		}
		return file_paths;
	}

	/**
	    *    递归删除文件或者目录
	 * @param file_path
	 */
	public static void deleteEveryThing(String file_path) {
		try {
			File file = new File(file_path);
			if (!file.exists()) {
				return;
			}
			if (file.isFile()) {
				file.delete();
			} else {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					String root = files[i].getAbsolutePath();   // 得到子文件或文件夹的绝对路径
					deleteEveryThing(root);
				}
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
