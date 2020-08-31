package cma.cimiss2.dpc.fileloop.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre
 * (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * <b>Description: 根据文件名模板获取资料四级编码</b><br>
 * 
 * @author wuzuoqiang
 * @version 1.0
 * @Note <b>ProjectName:</b> cimiss2-dwp-multithread <br>
 *       <b>PackageName:</b> cma.cimiss2.dpc.fileloop.common <br>
 *       <b>ClassName:</b> FileNameCheck <br>
 *       <b>Date:</b> 2019年12月9日 下午1:49:09
 */
public class FileNameCheck {

	public static void traverseFolder(String path) {
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			
			Arrays.sort(files);
			if (files.length == 0) {
				
				System.out.println("文件夹是空的!" + file.getAbsolutePath());
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						System.out.println(file2.getAbsolutePath());
						traverseFolder(file2.getAbsolutePath());
					} else {
						txt2String(new File("config/filematch.txt"), file2.getName());
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
	}

	 /**
     * 读取txt文件的内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static Map<String, String> txt2String(File file, String fileName) {
        Map<String, String> fileNameMap = new HashMap<String, String>();
        Map<String, String> resMap = new HashMap<String, String>();
        String fileValue = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            String valuse = "";
            String key = "";
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                if (!s.contains("data_type")) {
                    if (s.contains("sub_type")) {
                        String substring = s.substring(10);
                        valuse = substring;
                    } else if (s.length() > 2) {
                        key = s;
                    }
                    if (!key.isEmpty() && !valuse.isEmpty()) {
                    	
                        String replaceStr = key;
                        if (!fileNameMap.containsKey(replaceStr)) {
                            fileNameMap.put(replaceStr, valuse);
                        }
                        // 正则表达式规则
                        String regEx = replaceStr;
                        // 编译正则表达式
                        Pattern pattern = Pattern.compile(regEx);
                        // 忽略大小写的写法
                        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(fileName);
                        // 查找字符串中是否有匹配正则表达式的字符/字符串
                        boolean rs = matcher.find();
                        if (rs) {
                            fileValue = replaceStr;
                            
                            resMap.put("data_type", fileNameMap.get(fileValue));
                            continue;
                        }
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return fileNameMap.get(fileValue);
        if(resMap.size() == 0) {
        	System.out.println(fileName);
        	return null;
        }else {
			return resMap;
		}
    }

	
	
	public static void main(String[] args) {
		
		//Z_RADR_I_Z9662_20190211174800_P_DOR_SA_V_10_230_43.662.bin
		//Z_RADR_I_Z9662_20190211123000_P_DOR_SA_V_10_230_5.662.bin
		System.out.println(FileNameCheck.txt2String(new  File("config/filematch.txt"), "Z_NAFP_C_BCSH_20190903000000_P_surface-warms_Myanmar-f00.BIN").get("data_type").trim());
//		String path = args[0].trim();
		
//		traverseFolder(path);
	}
}
