package cma.cimiss2.dpc.decoder.tools.utils;

import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutMpenTab;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliGardTab;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliWindenerTab;
import cma.cimiss2.dpc.decoder.bean.upar.UparDigNmStlFtmTab;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
	public static List<String> getTxtFileContents(File file, String encode) throws UnsupportedEncodingException, FileNotFoundException {
		List<String> list = new ArrayList<String>();
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(new FileInputStream(file), encode);
			br = new BufferedReader(isr);
			String str = null;
			try {
				while (!(str = br.readLine()).equals("######")) {
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

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
		String pathname = "C://Users//Administrator//Desktop//QXDSJ//doc//内蒙本地数据资料//全区无人站数据//ZC500301.011";
		File file = new File(pathname);
		FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
		String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
		fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
		List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
		String[] messages = txtFileContent.get(1).split("\\s+");
		System.out.println(txtFileContent.get(0).substring(5,10));
		ArrayList<String> list = new ArrayList<>();
		for(int i =0;i < list.size();i++){
			System.out.println(list.get(i));
		}

	}
	public static String changeType(String dates) {
		if(dates.contains("0-")){
			dates = dates.substring(dates.indexOf("-"),dates.length());
		}
		return dates;
	}
}
