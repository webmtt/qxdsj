package cma.cimiss2.dpc.quickqc.cfg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;
import cma.cimiss2.dpc.quickqc.bean.MaxMinValues;
import cma.cimiss2.dpc.quickqc.bean.MaxMonthParaInfo;


public class MaxMinDaychinaCfg {
	/**
	 *   加载以下配置文件
	 * 气压		P0maxMinDaychina.txt
	 * 气温		TmaxMinDaychina.txt
	 * 草温		TgmaxMinDaychina.txt
	 * 地表温度	D0maxMinDaychina.txt
	 * 5cm地温	D05maxMinDaychina.txt
	 * 10cm地温	D10maxMinDaychina.txt
	 * 15cm地温	D15maxMinDaychina.txt
	 * 20cm地温	D20maxMinDaychina.txt
	 * 40cm地温	D40maxMinDaychina.txt
	 * 80cm地温	D80maxMinDaychina.txt
	 * 160cm地温	D160maxMinDaychina.txt
	 * 320cm地温	D320maxMinDaychina.txt
	 * 相对湿度	EmaxMinDaychina.txt
	 * @param pathname  配置文件路径
	 * @return   Map<String, MaxMinDaychinaInfo>  map集合
	 */
	public Map<String, MaxMinDaychinaInfo> loadMaxMinDaychinaCfg(String pathname) {
		Map<String, MaxMinDaychinaInfo> maps = new HashMap<String, MaxMinDaychinaInfo>();
		File file = new File(pathname);
		if(file != null && file.exists() && file.isFile()) {
			FileReader fileReader = null;
			BufferedReader bufferedReader = null;
			try {
				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				String line=null;
				while ((line = bufferedReader.readLine()) != null) {
					String[] items = line.trim().split("\\s+");
					if(items.length != 8) {
					}else {
						MaxMinValues maxMinValues = new MaxMinValues();
						maxMinValues.setExceedingThreshold(Double.parseDouble(items[5].trim()));
						maxMinValues.setMaxValue(Double.parseDouble(items[6].trim()));
						maxMinValues.setMinValue(Double.parseDouble(items[7].trim()));
						if (maps.containsKey(items[0].trim())) {
							maps.get(items[0].trim()).put(Integer.parseInt(items[1].trim()), maxMinValues);
						}else {
							MaxMinDaychinaInfo maxMinDaychinaInfo = new MaxMinDaychinaInfo();
							maxMinDaychinaInfo.setStationCode(items[0].trim());
							maxMinDaychinaInfo.setLongitude(Double.parseDouble(items[2].trim()));
							maxMinDaychinaInfo.setLatitude(Double.parseDouble(items[3].trim()));
							maxMinDaychinaInfo.setAltitude(Double.parseDouble(items[4].trim()));
							maxMinDaychinaInfo.put(Integer.parseInt(items[1].trim()), maxMinValues);
							maps.put(maxMinDaychinaInfo.getStationCode(), maxMinDaychinaInfo);
						}						
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				
				if(bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if(fileReader != null) {
					try {
						fileReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				
			}
		}else {
		}
		
		return maps;
		
	}
	
	/**
	 * 小时降水量	RHourParam.txt
	 * 风速		FmaxMonthPara.txt
	 * @param pathname
	 * @return
	 */
	public Map<String, MaxMonthParaInfo> loadMaxMonthCfg(String pathname){
		Map<String, MaxMonthParaInfo> maps = new HashMap<String, MaxMonthParaInfo>();
		File file = new File(pathname);
		if(file != null && file.exists() && file.isFile()) {
			FileReader fileReader = null;
			BufferedReader bufferedReader = null;
			try {
				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				String line=null;
				while ((line = bufferedReader.readLine()) != null) {
					String[] items = line.trim().split("\\s+");
					if(items.length != 16) {
					}else {
						MaxMonthParaInfo maxMonthParaInfo = new MaxMonthParaInfo();
						maxMonthParaInfo.setStationCode(items[0].trim());
						maxMonthParaInfo.setLongitude(Double.parseDouble(items[1].trim()));
						maxMonthParaInfo.setLatitude(Double.parseDouble(items[2].trim()));
						maxMonthParaInfo.setAltitude(Double.parseDouble(items[3]));
						maxMonthParaInfo.setJan_max_value(Double.parseDouble(items[4].trim()));
						maxMonthParaInfo.setFeb_max_value(Double.parseDouble(items[5].trim()));
						maxMonthParaInfo.setMar_max_value(Double.parseDouble(items[6].trim()));
						maxMonthParaInfo.setApr_max_value(Double.parseDouble(items[7].trim()));
						maxMonthParaInfo.setMay_max_value(Double.parseDouble(items[8].trim()));
						maxMonthParaInfo.setJun_max_value(Double.parseDouble(items[9].trim()));
						maxMonthParaInfo.setJul_max_value(Double.parseDouble(items[10].trim()));
						maxMonthParaInfo.setAug_max_value(Double.parseDouble(items[11].trim()));
						maxMonthParaInfo.setSep_max_value(Double.parseDouble(items[12].trim()));
						maxMonthParaInfo.setOct_max_value(Double.parseDouble(items[13].trim()));
						maxMonthParaInfo.setNov_max_value(Double.parseDouble(items[14].trim()));
						maxMonthParaInfo.setDec_max_value(Double.parseDouble(items[15].trim()));
						
						if(maps.containsKey(maxMonthParaInfo.getStationCode())) {
						}else {
							maps.put(maxMonthParaInfo.getStationCode(), maxMonthParaInfo);
						}
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				
				if(bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if(fileReader != null) {
					try {
						fileReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				
			}
		}else {
		}
		
		return maps;
	}
	
	
	

}
