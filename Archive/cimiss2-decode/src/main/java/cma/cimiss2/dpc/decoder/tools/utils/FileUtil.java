package cma.cimiss2.dpc.decoder.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	/**
	 * @Title: getFileNameNoEx 
	 * @Description: TODO(获取文件名， 不带扩展名) 
	 * @param filename
	 * @return String
	 * @throws:
	 */
	public static String getFileNameNoEx(String filename) {
		if(filename != null && filename.length() > 0) {
			int dot = filename.lastIndexOf('.');
			if(dot > -1 && dot < filename.length()) {
				return filename.substring(0, dot);
			}
		}
		
		return filename;
	}

	/**
	 * 
	 * @Title: getTxtFileContent
	 * @Description: 将文本文件数据按行转成list返回，默认使用utf-8编码
	 * @param file
	 * @return List<String>
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static List<String> getTxtFileContent(File file) throws UnsupportedEncodingException, FileNotFoundException {
		return getTxtFileContent(file, "utf-8");
	}
	/**
	 * @Title: getTxtFileContent
	 * @Description: 将文本文件数据按行转成list返回
	 * @param file 文件
	 * @param encode 文件编码
	 * @return List<String>
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static List<String> getTxtFileContent(File file, String encode) throws UnsupportedEncodingException, FileNotFoundException {
		List<String> list = new ArrayList<String>();
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(new FileInputStream(file), encode);
			br = new BufferedReader(isr);
			String str = null;
			try {
				while ((str = br.readLine()) != null) {
					if (!str.trim().equals("")) {
						list.add(str);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			if(br != null ) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return list;
	}
}
