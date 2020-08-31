package org.cimiss2.dwp.tools.utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.cimiss2.dwp.tools.config.StartConfig;

public class TaiWanStationFile {
	public static boolean  deleteTaiWanStationFile() {
		Boolean flag = false;  
		String sPath = StartConfig.stationInfoPath();
//		String sPath="..\\TaiwanStationInfo";
		File file = new File(sPath);  //根据指定的文件名创建File对象
	    // 判断目录或文件是否存在  
	    if (!file.exists()) {  // 不存在返回 false  
	        return flag;  
	    } else {  
	        // 判断是否为文件  
	        if (file.isFile()) {  // 为文件时调用删除文件方法  
	            return deleteFile(sPath);  
	        } else {  // 为目录时调用删除目录方法  
	            return deleteDirectory(sPath);  
	        }  
	    } 

	}
	
	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */  
	public static boolean deleteFile(String sPath) {  
	   boolean flag = false;  
	   File  file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}
	
	/** 
	 * 删除目录（文件夹）以及目录下的文件 
	 * @param   sPath 被删除目录的文件路径 
	 * @return  目录删除成功返回true，否则返回false 
	 */  
	public static boolean deleteDirectory(String sPath) {  
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
	    if (!sPath.endsWith(File.separator)) {  
	        sPath = sPath + File.separator;  
	    }  
	    File dirFile = new File(sPath);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return false;  
	    }  
	    boolean flag = true;  
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = dirFile.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {
	        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	        	String fileAbsolutePath=files[i].getAbsolutePath();
	        	String fileDate=fileAbsolutePath.substring(fileAbsolutePath.lastIndexOf(".")-10,fileAbsolutePath.lastIndexOf("."));//获取文件名上的日期
	        	Date fdate = null;
				try {
					fdate = df.parse(fileDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
	        	Calendar calendar = Calendar.getInstance();
	        	calendar.setTime(fdate);
	        	calendar.add(Calendar.DATE,2); 
	        	calendar.set(Calendar.HOUR_OF_DAY,0);
	        	calendar.set(Calendar.MINUTE,0);
	        	calendar.set(Calendar.SECOND,0);
	        	calendar.set(Calendar.MILLISECOND,0);
	        	Date filedateP=calendar.getTime();//文件名的时间加2天后
	        	
	        	Date now=new Date();
	        	Calendar calendar2 = Calendar.getInstance();
	        	calendar2.setTime(now);
	        	calendar2.set(Calendar.HOUR_OF_DAY,0);
	        	calendar2.set(Calendar.MINUTE,0);
	        	calendar2.set(Calendar.SECOND,0);
	        	calendar2.set(Calendar.MILLISECOND,0);
	        	now=calendar2.getTime();
	        	
	        	if(filedateP.before(now)){
	        		flag = deleteFile(files[i].getAbsolutePath());  
	        	}
	            if (!flag) break;  
	        } //删除子目录  
	        else {  
	            flag = deleteDirectory(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        }  
	    }  
	    if (!flag) return false;  
	    //删除当前目录  
	    if (dirFile.delete()) {  
	        return true;  
	    } else {  
	        return false;  
	    }  
	}
}
