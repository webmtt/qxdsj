package cma.cimiss2.dpc.quickqc.cfg;

import cma.cimiss2.dpc.quickqc.bean.MaxMonthParaInfo;

import java.util.Map;

public class RHourParamCfg extends MaxMinDaychinaCfg{
	private Map<String, MaxMonthParaInfo> rHourParamInfoMaps;
	private static RHourParamCfg rHourParamCfg;

	private RHourParamCfg() {
		this.rHourParamInfoMaps = loadMaxMonthCfg("config/RHourParam.txt");
	}

	public static RHourParamCfg getRHourParamCfg() {
		if(rHourParamCfg == null) {
			rHourParamCfg = new RHourParamCfg();
		}
		return rHourParamCfg;
	}

	public Map<String, MaxMonthParaInfo> getRHourParamInfoMaps() {
		return rHourParamInfoMaps;
	}
//	private static RHourParamCfg rHourParamCfg;
//
//	private RHourParamCfg() {
//		loadRHourParamCfg("config/RHourParam.txt");
//	}
//
//	private void loadRHourParamCfg(String pathname) {
//		File file = new File(pathname);
//		if(file != null && file.exists() && file.isFile()) {
//			FileReader fileReader = null;
//			BufferedReader bufferedReader = null;
//			try {
//				fileReader = new FileReader(file);
//				bufferedReader = new BufferedReader(fileReader);
//				String line=null;
//				while ((line = bufferedReader.readLine()) != null) {
//					String[] items = line.trim().split("\\s+");
//					if(items.length != 8) {
//
//					}else {
//						MaxMinValues maxMinValues = new MaxMinValues();
////						maxMinValues.setMax_exceeding_threshold(Double.parseDouble(items[]));
//					}
//
//				}
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}finally {
//
//				if(bufferedReader != null) {
//					try {
//						bufferedReader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//
//				if(fileReader != null) {
//					try {
//						fileReader.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//
//
//			}
//		}else {
//		}
	

}
