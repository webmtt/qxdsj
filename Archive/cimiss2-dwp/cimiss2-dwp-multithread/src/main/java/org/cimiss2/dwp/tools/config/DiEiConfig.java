package org.cimiss2.dwp.tools.config;

import java.io.FileInputStream;
import java.util.Properties;


public class DiEiConfig {
	
	public static int DI_VT;

	//IIiii=0_1_2_3_4_5_6_7_9
	public static String IIiii;
	//Lon=7
	public static String Lon;
	//Lat=6
	public static String Lat;
	//DATA_TIME=1_2_3_4_5
	public static String DATE_TIME;
	//TT
	public static String TT;
	
	public Properties dieiProperties;
	
	public static boolean parseResult;
	
	public static String propertiesFile="config/common_xml/di_ei_config.properties";
	
	public DiEiConfig(String file) {
		dieiProperties = new Properties();
		parseResult = false;
    	try {
    		propertiesFile = file;
    		dieiProperties.load(new FileInputStream(file));
    		IIiii = dieiProperties.getProperty("IIiii");
        	Lon = dieiProperties.getProperty("Lon");
        	Lat = dieiProperties.getProperty("Lat");
        	DATE_TIME = dieiProperties.getProperty("DATE_TIME");
        	TT = dieiProperties.getProperty("TT");
        	DI_VT = Integer.parseInt( dieiProperties.getProperty("DI_VT"));
        	parseResult = true;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} 
	}

}
