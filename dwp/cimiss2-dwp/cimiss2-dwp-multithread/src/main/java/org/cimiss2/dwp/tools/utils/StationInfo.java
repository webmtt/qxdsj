package org.cimiss2.dwp.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Aouthor: xzh
 * @create: 2017-11-20 17:54
 */
public class StationInfo {
    public static Map<String,Object> proMap = new HashMap<String, Object>();
//    public static final String CONFIG_COLLECT = "d:/StationInfo_Config.lua";
    public static final String CONFIG_COLLECT ="config/StationInfo_Config.lua";
    static {
//        InputStream is=null;
    	BufferedReader reader = null; 
        try {
//            is = StationInfo.class.getClassLoader().getResource(CONFIG_COLLECT).openStream();
        	File file = new File(CONFIG_COLLECT);
        	reader = new BufferedReader(new FileReader(file));
        	String tempString = null;  
            int line = 1;  
            // 一次读入一行，直到读入null为文件结束  
            while ((tempString = reader.readLine()) != null) {  
                // 显示行号  
            	String[] items = tempString.split("\\s+");
                if(proMap.containsKey(items[0].trim())) {
                	continue;
                }else {
					proMap.put(items[0].trim(), items[1].trim());
				}
            }             
            
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
    }
    public static  Map<String, Object>  getProMap() {
        return proMap;
    }
    
	public static String getAdminCode(String stationNumberChina,String acodeNo) {
		String adminCode = null;
		try {
			String info = (String) proMap.get(stationNumberChina + "+"+acodeNo);
			String[] infos = info.split(",");
			adminCode = infos[5];
		} catch (Exception e) {
			adminCode="999999";
		}
		return adminCode;
	}
    public static void main(String[] args){
        Map<String, Object> proMap = StationInfo.getProMap();
        for (String str : proMap.keySet()) {
            System.out.println(str+"====="+proMap.get(str));
        }
    }
}
