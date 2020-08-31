package org.cimiss2.dwp.tools.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Aouthor: dengyonglaing
 * @create: 2018-1-10 17:54
 */
public class WmoStationInfo {
	public static Map<String, NcdoStationRuleBean> staMap = new HashMap<String, NcdoStationRuleBean>();
	public static ArrayList<String> staList = new ArrayList<String>();
	public static String fileTxt = "config/suf_ncdc_gsod/StationTools/stas_wmo_zzq.txt";
	public static String fileCsv = "config/suf_ncdc_gsod/StationTools/nodc_gsod_station.csv";
	
	static {
		WmoStationInfo aInfo = new WmoStationInfo();
		try { 
			 staList=aInfo.readWmoStation(fileTxt);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		try { 
			staMap=aInfo.readStnRule(fileCsv);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

	public static Map<String, NcdoStationRuleBean> getStaMap() {
		return staMap;
	}
	public static ArrayList<String> getStaList() {
		return staList;
	}

	public static void main(String[] args) {
		WmoStationInfo aInfo = new WmoStationInfo();
		aInfo.readStnRule(fileCsv);
	}



	public static ArrayList<String> readWmoStation(String file) {
		
		ArrayList<String> list = new ArrayList<String>();
		InputStreamReader read = null;
		 BufferedReader bufferedReader = null;
		try {
			read = new InputStreamReader(new FileInputStream(new File(file)));
			bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String items = lineTxt.trim() + "0";
				list.add(items);
			}
			bufferedReader.close();
			read.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (read != null) {
					read.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return list;

	}

	public  HashMap<String, NcdoStationRuleBean> readStnRule(String file) {
		HashMap<String, NcdoStationRuleBean> map = new HashMap<String, NcdoStationRuleBean>();
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try {
			read = new InputStreamReader(new FileInputStream(new File(fileCsv)));
			bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				NcdoStationRuleBean ncdoBean = new NcdoStationRuleBean();
				String[] items = lineTxt.trim().split(",");
				if (items.length == 10) {
					String stn = addZeroForNum(items[0], 6);
					String wban = addZeroForNum(items[1], 5);
					if (map.get(stn + "_" + wban) != null) {
						System.out.println(stn + "_" + wban);
					}
					ncdoBean.setStn(stn);
					ncdoBean.setWban(wban);
					ncdoBean.setStationName(items[2]);
					ncdoBean.setCtry(items[3]);
					ncdoBean.setStCall(items[4]);
					ncdoBean.setLat(items[5]);
					ncdoBean.setLon(items[6]);
					ncdoBean.setElev(items[7]);
					ncdoBean.setBegin(items[8]);
					ncdoBean.setEnd(items[9]);
					map.put(stn + "_" + wban, ncdoBean);

				} else {
					continue;
				}

			}
			bufferedReader.close();
			read.close();
	
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (read != null) {
					read.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return map;

	}

	/**
	 * 字符串左补零
	 * 
	 * @param str
	 * @param strLength
	 * @return
	 */
	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			sb.append("0").append(str);// 左补0
			// sb.append(str).append("0");//右补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}

	public class NcdoStationRuleBean {
		/**
		 * 原始STN站号
		 */
		String stn;
		/**
		 * 原始WBAN站号
		 */
		String Wban;
		/**
		 * STATION NAME名称
		 */
		String stationName;
		String ctry;
		String stCall;
		/**
		 * 纬度
		 */
		String lat;
		/**
		 * 经度
		 */
		String lon;
		/**
		 * 海拔
		 */
		String elev;
		String begin;
		String end;

		public String getStn() {
			return stn;
		}

		public void setStn(String stn) {
			this.stn = stn;
		}

		public String getWban() {
			return Wban;
		}

		public void setWban(String wban) {
			Wban = wban;
		}

		public String getStationName() {
			return stationName;
		}

		public void setStationName(String stationName) {
			this.stationName = stationName;
		}

		public String getCtry() {
			return ctry;
		}

		public void setCtry(String ctry) {
			this.ctry = ctry;
		}

		public String getStCall() {
			return stCall;
		}

		public void setStCall(String stCall) {
			this.stCall = stCall;
		}

		public String getLat() {
			return lat;
		}

		public void setLat(String lat) {
			this.lat = lat;
		}

		public String getLon() {
			return lon;
		}

		public void setLon(String lon) {
			this.lon = lon;
		}

		public String getElev() {
			return elev;
		}

		public void setElev(String elev) {
			this.elev = elev;
		}

		public String getBegin() {
			return begin;
		}

		public void setBegin(String begin) {
			this.begin = begin;
		}

		public String getEnd() {
			return end;
		}

		public void setEnd(String end) {
			this.end = end;
		}

	}

}
