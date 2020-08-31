package com.thinkgem.jeesite.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class WordHelper {
	public static final String environment="2";
	
	/**
	 * pdf杞瑂wf
	 * @param pdfFile test.pfd
	 * @param swfFile test.swf
	 * @return
	 */
	@SuppressWarnings("unused")
	public static int pdf2swf(String sourcePath, String destPath,String pdftoswfPath){
		File dest = new File(destPath);
		if(!dest.exists()){
			File dir = dest.getParentFile();
			if (dest!=null&&!dest.exists()) {
				dest.mkdirs();
			}
		}
		// 婧愭枃浠朵笉瀛樺湪鍒欒繑鍥�
		File source = new File(sourcePath);
		if (!source.exists()) {
			return -1;
		}
//		String fileName = source.getName().substring(0,source.getName().lastIndexOf("."));
//		String destFile=destPath+"/"+fileName+".swf";
		String destFile=destPath+source.getName().substring(0,source.getName().lastIndexOf("."))+".swf";
		System.out.println(destFile);
		
		Runtime r = Runtime.getRuntime();
		int res=1;
		
		if (source.exists()) {
			if (environment.equals("1")) {// windows鐜澶勭悊
				try {
					String SWFTOOLS_PATH=pdftoswfPath+"static\\SWFTools\\";
					String[] envp = new String[1];
					envp[0] = "PATH=\"" + SWFTOOLS_PATH + "\"";
					String command = SWFTOOLS_PATH + "pdf2swf.exe -z -s flashversion=9 \"" + sourcePath + "\" -o \""
							+ destFile + "\"";
					System.out.println(SWFTOOLS_PATH);
					System.out.println(command);
					Process p = Runtime.getRuntime().exec(command, envp);
					System.out.print(loadStream(p.getInputStream()));
					System.err.print(loadStream(p.getErrorStream()));
					System.out.print(loadStream(p.getInputStream()));
					System.err.println("****swf杞崲鎴愬姛锛屾枃浠惰緭鍑猴細" + destFile + "****");
					// if (dest.exists()) {
					// dest.delete();
					// }
				} catch (Exception e) {
					System.out.println("swf杞崲澶辫触");
					res = 0;
					e.printStackTrace();
				}
			} else if (environment.equals("2")) {// linux鐜澶勭悊
				try {

					Process p = r.exec("pdf2swf " + sourcePath + " -o " + destFile + " -T 9");
					System.out.print(loadStream(p.getInputStream()));
					System.err.print(loadStream(p.getErrorStream()));
					System.err.println("****swf杞崲鎴愬姛锛屾枃浠惰緭鍑猴細" + destFile + "****");
					// if (dest.exists()) {
					// dest.delete();
					// }
				} catch (Exception e) {
					System.out.println("swf杞崲澶辫触");
					res = 0;
					e.printStackTrace();
				}
			}
		} else {
			res = 0;
			System.out.println("****pdf涓嶅瓨鍦�鏃犳硶杞崲****");

		}
		return res;
	}

	static String loadStream(InputStream in) throws IOException {
		int ptr = 0;
		in = new BufferedInputStream(in);
		StringBuffer buffer = new StringBuffer();

		while ((ptr = in.read()) != -1) {
			buffer.append((char) ptr);
		}

		return buffer.toString();
	}
	
	

}
