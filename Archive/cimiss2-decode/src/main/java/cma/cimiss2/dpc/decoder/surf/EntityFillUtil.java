package cma.cimiss2.dpc.decoder.surf;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cma.cimiss2.dpc.decoder.bean.QCElement;
import cma.cimiss2.dpc.decoder.tools.enumeration.Quality;
import cma.cimiss2.dpc.decoder.tools.enumeration.QualityControl;


/**
 * 
 * *****************************************************************************************<br> 
 * <strong>All Rights Reserved By National Meteorological Information Centre (NMIC)</strong><br>
 * *****************************************************************************************<br>

 * new an object for the decode data and fill the fields. <br>
 * new一个解码的实体，并将对应的数据装填到实体类的属性中。
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 11/20/2017   lihanjie    Initial creation.
 * </pre>
 * 
 * @author lihanjie
 *
 * 
 */
public class EntityFillUtil {

//	/**
//	 * 装填实体类对应的属性，
//	 * 
//	 * @param cla
//	 *            需要封装成的实体类
//	 * @param propFileName
//	 *            配置文件
//	 * @param values
//	 *            一行的数据，对应一个实体类
//	 * @return
//	 */
//	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
//	public static Object fillVal(Class cla, String propFileName, String values) {
//		Object o = null;
//		int index = 0;
//		try {
//			// new 出对象
//			o = cla.newInstance();
//			Map<String, int[]> map = null;
//			try {
//				map = MappingUtil.getMapping(propFileName);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			String[] valArray = values.split(" ");
//			List<String> keys = new ArrayList(map.keySet());
//			// System.out.println(keys.size());
//			for (String key : keys) { // 初始化所有的field
//				Field f = cla.getDeclaredField(key);
//				String name = f.getGenericType().toString(); // 泛型变量和数组变量需分别处理
//				if (name.contains("[")) {// 数组,泛型数组也在此处理
//					if (key.equals("qualityControl")) {
//						int[] val = map.get(key);
//						QualityControl[] vv = new QualityControl[3];
//						if (val.length == 1) {// 正确
//
//						} else {
//							System.out.println("文件格式错误,qualityControl");
//						}
//						for (int i = 0; i < 3; i++) {
//							if (valArray[val[0]].charAt(i) == '0') {
//								vv[i] = QualityControl.MANUAL;
//							} else if (valArray[val[0]].charAt(i) == '1') {
//								vv[i] = QualityControl.SOFTWARE;
//							} else if (valArray[val[0]].charAt(i) == '9') {
//								vv[i] = QualityControl.NO_QC;
//							} else {
//								System.out.println("枚举匹配不上,qualityControl");
//							}
//						}
//					} else if (key.equals("cloudType")) { // String[] 最多8中编码
//						int[] val = map.get(key);
//						String[] tct = new String[8];
//						for (int i = 0; i < tct.length; i++) {
//							tct[i] = valArray[val[0]].substring(i * 3, 3 * (i + 1));
//						}
//						QCElement<String[]> qce = new QCElement<String[]>(tct, Integer.parseInt(valArray[val[1]]),
//								Integer.parseInt(valArray[val[2]]), Integer.parseInt(valArray[val[3]]));
//					} else if (key.equals("cloudType1")) { // 暂不处理
//						// int[] val = map.get(key);
//						// CloudType[] tct = new CloudType[val.length];
//						// for(int i=0;i<val.length;i++){
//						// tct[i] = MappingUtil.getByCode(CloudType.class,
//						// Integer.parseInt(valArray[val[i]]));
//						// if(tct[i]==null){
//						// System.out.println("文件格式错误,cloudType1");
//						// }
//						// }
//					} else if (key.equals("precipitationEveryMinutes")) { // 暂不处理
//						// int[] val = map.get(key);
//						// Integer[] ti = new Integer[val.length];
//						// for(int i=0;i<val.length;i++){
//						// tct[i] = MappingUtil.getByCode(CloudType.class,
//						// Integer.parseInt(valArray[val[i]]));
//						// if(tct[i]==null){
//						// System.out.println("文件格式错误,cloudType1");
//						// }
//						// }
//					} else if (key.equals("weatherManualObservational")) { // 暂不处理
//						// int[] val = map.get(key);
//						// Weather[] tw = new Weather[val.length];
//					} else {
//						System.out.println("文件格式错误,不能识别的数组类型");
//					}
//				} else if (name.contains("<")) {// 泛型
//					int[] val = map.get(key);
//					if (val.length == 4) {
//						f.setAccessible(true);
//						name = name.substring(name.indexOf('<') + 1, name.indexOf('>'));
//						try {
//							QCElement qce = MappingUtil.newInstanceForElem(Class.forName(name), valArray[val[0]],
//									Integer.parseInt(valArray[val[1]]), Integer.parseInt(valArray[val[2]]),
//									Integer.parseInt(valArray[val[3]]));
//							f.set(o, qce);
//						} catch (Exception e) {
//							e.printStackTrace();
//							System.out.println(key + "类型不匹配:" + valArray[val[0]]);
//						}
//					} else { // 格式不正确
//
//					}
//				} else {// 常规对象
//					f.setAccessible(true);
//					Class cc = null;
//					if (name.contains("class ")) {
//						name = name.replace("class ", "");
//					}
//					try {
//						cc = Class.forName(name);
//					} catch (ClassNotFoundException e) {
//						e.printStackTrace();
//					}
//					int[] val = map.get(key);
//					String relvalue = "";
//
//					if (val.length == 1) { // 常规对象，基本数据类型
//						relvalue = valArray[val[0]];
//						f.set(o, MappingUtil.convertString(cc, relvalue));
//					} else { // 自定义数据类型
//
//					}
//				}
//				index++;
//			}
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} finally {
//		}
//
//		return o;
//	}

	/**
	 * 本方法已定制成国家站数据的实体类装填方法 装填实体类对应的属性，自动读取本工程classpath下的fields_surf.conf文件
	 * 
	 * @param cla
	 *            需要封装成的实体类
	 * @param values
	 *            一份完整的数据，字段间空隔分割，按顺序排列，对应一个实体类对象
	 * @return   返回国家站对应实体类cla的对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static Object fillVal(Class cla/* ,String propFileName */, String values) {
		Object o = null;
		int index = 0;
		try {
			// new 出对象
			o = cla.newInstance();
			Map<String, int[]> map = null;
			map = MappingUtil.getMapping();
			String[] valArray = values.split(" ");
			List<String> keys = new ArrayList(map.keySet());
			for (String key : keys) { // 初始化所有的field
				Field f = cla.getDeclaredField(key);
				String name = f.getGenericType().toString(); // 泛型变量和数组变量需分别处理
				if (name.contains("[")) {// 数组,泛型数组也在此处理
					if (key.equals("qualityControl")) {
						int[] val = map.get(key);
						QualityControl[] vv = new QualityControl[3];
						if (val.length == 1) {// 正确

						} else {
							System.out.println("文件格式错误,qualityControl");
							o = "文件格式错误,qualityControl";
							return o;
						}
						for (int i = 0; i < 3; i++) {
							if (valArray[val[0]].charAt(i) == '0') {
								vv[i] = QualityControl.MANUAL;
							} else if (valArray[val[0]].charAt(i) == '1') {
								vv[i] = QualityControl.SOFTWARE;
							} else if (valArray[val[0]].charAt(i) == '9') {
								vv[i] = QualityControl.NO_QC;
							} else {
								System.out.println("枚举匹配不上,qualityControl");
								o = "枚举匹配不上,qualityControl";
								return o;
							}
						}
						f.setAccessible(true);
						f.set(o, vv);
					} else if (key.equals("cloudType")) {
						int[] val = map.get(key);
						String[] tct = new String[8];
						int startIndex = 0;
						for (int i = 0; i < tct.length; i++) {
							String cct = null;
							if(i==0) { //处理前面的-
								cct = valArray[val[0]].substring(startIndex * 3, 3 * (startIndex + 1));
								startIndex++;
								while(cct.contains("-")&&startIndex<8) {
									cct = valArray[val[0]].substring(startIndex * 3, 3 * (startIndex + 1));
									startIndex++;
								}
								if(cct.contains("-")) { //处理都是-的情况
									cct = "999998";
								}
							}else {
								if(startIndex<tct.length) {
									cct = valArray[val[0]].substring(startIndex * 3, 3 * (startIndex + 1));
									startIndex++;
								}else {
									cct = "999998";
								}
							}
							tct[i] = cct.contains("/")?DecodeBABJ.NANStr:cct;
						}
						//因为QC1错误码太多，而且入库程序指定入QC2，因此这里强制将QC1置0
						/*QCElement<String[]> qce = new QCElement<String[]>(tct, Integer.parseInt(valArray[val[1]]),
								Integer.parseInt(valArray[val[2]]), Integer.parseInt(valArray[val[3]]));*/
						QCElement<String[]> qce = new QCElement<String[]>(tct, Integer.parseInt("0"),
								Integer.parseInt(valArray[val[2]]), Integer.parseInt(valArray[val[3]]));
						f.setAccessible(true);
						f.set(o, qce);
					} /*
						 * else if(key.equals("weatherManualObservational")){ //暂不处理 已改成String类型 //
						 * int[] val = map.get(key); // Weather[] tw = new Weather[val.length]; }
						 */else {
						System.out.println("文件格式错误,不能识别的数组类型");
						o = "文件格式错误,不能识别的数组类型";
						return o;
					}
				} else if (name.contains("<")) {// 泛型
				  if (key.equals("precipitationEveryMinutes")) { //处理分钟降雨
					    int[] val = map.get(key);
						List<QCElement<Double>> ti = new ArrayList<>(60);
						for (int i = 0; i < 60; i++) {
							String tempVal = "";
							if(valArray[val[0]].length() != 120){
								tempVal = "999999";
							}else{
								tempVal = valArray[val[0]].substring(2 * i, 2 * (i + 1));
							}
							if(tempVal.contains("/")){
								tempVal = "999999";
							}else if(tempVal.contains(",")){
								tempVal = "999990";
							}else{
								tempVal = String.valueOf(Double.parseDouble(tempVal)/10);
							}
							ti.add(new QCElement<Double>(
											Double.parseDouble(tempVal),
											Quality.getQualityByCode(
													Integer.parseInt(valArray[val[1]].substring(1 * i, 1 * (i + 1)))),
											Quality.getQualityByCode(
													Integer.parseInt(valArray[val[2]].substring(1 * i, 1 * (i + 1)))),
											Quality.getQualityByCode(
													Integer.parseInt(valArray[val[3]].substring(1 * i, 1 * (i + 1))))));
						}
						f.setAccessible(true);
						f.set(o, ti);
						continue;
					}
					int[] val = map.get(key);
					if (val.length == 4) {
						f.setAccessible(true);
						name = name.substring(name.indexOf('<') + 1, name.indexOf('>'));
						try {
							//因为QC1错误码太多，而且入库程序制定入QC2，因此这里强制将QC1置0
							/*QCElement qce = MappingUtil.newInstanceForElem(Class.forName(name), valArray[val[0]],
									Integer.parseInt(valArray[val[1]]), Integer.parseInt(valArray[val[2]]),
									Integer.parseInt(valArray[val[3]]));*/
							QCElement qce = MappingUtil.newInstanceForElem(Class.forName(name), valArray[val[0]],
									Integer.parseInt("0"), Integer.parseInt(valArray[val[2]]),
									Integer.parseInt(valArray[val[3]]));
							f.set(o, qce);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println(key + "类型不匹配:" + valArray[val[0]]);
							o = key + "类型不匹配:" + valArray[val[0]];
							return o;
						}
					} else { // 格式不正确
						o = String.format("%s字段格式不正确", key);
						return o;

					}
				} else {// 常规对象
					f.setAccessible(true);
					Class cc = null;
					if (name.contains("class ")) {
						name = name.replace("class ", "");
					}
					try {
						cc = Class.forName(name);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						o = e.getMessage();
						return o;
					}
					int[] val = map.get(key);
					String relvalue = "";

					if (val.length == 1) { // 常规对象，基本数据类型
						relvalue = valArray[val[0]].replaceAll("\t", " ");
						f.set(o, MappingUtil.convertString(cc, relvalue));
					} else { // 自定义数据类型
						System.out.println(name);
						o = "不支持的自定义类型："+name+",请检查实体类";
						return o;
					}
				}
				index++;
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (SecurityException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} finally {
		}

		return o;
	}

	/**
	 * 本方法已定制成区域站小时数据的实体类装填方法 装填实体类对应的属性，自动读取本工程classpath下的fields_surf_reg.conf文件
	 * 
	 * @param cla
	 *            需要封装成的实体类
	 * @param values
	 *            一份完整的数据，字段间空隔分割，按顺序排列，对应一个实体类对象
	 * @return    返回区域站对应实体类cla的对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static Object fillVal_REG(Class cla/* ,String propFileName */, String values) {
		Object o = null;
		int index = 0;
		try {
			// new 出对象
			o = cla.newInstance();
			Map<String, int[]> regMap = null;
			regMap = MappingUtil.getRegMapping();
			String[] valArray = values.split(" ");
			List<String> keys = new ArrayList(regMap.keySet());
			for (String key : keys) { // 初始化所有的field
				Field f = cla.getDeclaredField(key);
				String name = f.getGenericType().toString(); // 泛型变量和数组变量需分别处理
				if (name.contains("[")) {
					// 数组,泛型数组也在此处理
					if (key.equals("qualityControl")) {
						int[] val = regMap.get(key);
						QualityControl[] vv = new QualityControl[3];
						if (val.length == 1) {// 正确

						} else {
							System.out.println("文件格式错误,qualityControl");
							o = "文件格式错误,qualityControl";
							return o;
						}
						for (int i = 0; i < 3; i++) {
							if (valArray[val[0]].charAt(i) == '0') {
								vv[i] = QualityControl.MANUAL;
							} else if (valArray[val[0]].charAt(i) == '1') {
								vv[i] = QualityControl.SOFTWARE;
							} else if (valArray[val[0]].charAt(i) == '9') {
								vv[i] = QualityControl.NO_QC;
							} else {
								System.out.println("枚举匹配不上,qualityControl");
								o = "枚举匹配不上,qualityControl";
								return o;
							}
						}
						f.setAccessible(true);
						f.set(o, vv);
					} else if (key.equals("cloudType")) {
						int[] val = regMap.get(key);
						String[] tct = new String[8];
						int startIndex = 0;
						for (int i = 0; i < tct.length; i++) {
							String cct = null;
							if(i==0) { //处理前面的-
								cct = valArray[val[0]].substring(startIndex * 3, 3 * (startIndex + 1));
								startIndex++;
								while(cct.contains("-")&&startIndex<8) {
									cct = valArray[val[0]].substring(startIndex * 3, 3 * (startIndex + 1));
									startIndex++;
								}
								if(cct.contains("-")) { //处理都是-的情况
									cct = "999998";
								}
							}else {
								if(startIndex<tct.length) {
									cct = valArray[val[0]].substring(startIndex * 3, 3 * (startIndex + 1));
									startIndex++;
								}else {
									cct = "999998";
								}
							}
							tct[i] = cct.contains("/")?DecodeBABJ.NANStr:cct;
						}
						f.setAccessible(true);
						f.set(o, tct);
					} else {
						System.out.println("文件格式错误,不能识别的数组类型");
						o = "文件格式错误,不能识别的数组类型";
						return o;
					}
				} else if (name.contains("<")) {// 泛型
					if (key.equals("precipitationEveryMinutes")) { //处理分钟降雨
					    int[] val = regMap.get(key);
						List<QCElement<Double>> ti = new ArrayList<>(60);
						for (int i = 0; i < 60; i++) {
							String tempVal = "";
							if(valArray[val[0]].length() != 120) {
								tempVal = "999999";
							} else{
								tempVal = valArray[val[0]].substring(2 * i, 2 * (i + 1));
							}
							if(tempVal.contains("/")){
								tempVal = "999999";
							} else if(tempVal.contains(",")){
								tempVal = "999990";
							} else{
								tempVal = String.valueOf(Double.parseDouble(tempVal) / 10);
							}
							ti.add(new QCElement<Double>(
											Double.parseDouble(tempVal),
											Quality.getQualityByCode(
													Integer.parseInt(valArray[val[1]].substring(1 * i, 1 * (i + 1))))));
						}
						f.setAccessible(true);
						f.set(o, ti);
						continue;
					}
					
					int[] val = regMap.get(key);
					if (val.length == 2) {
						f.setAccessible(true);
						name = name.substring(name.indexOf('<') + 1, name.indexOf('>'));
						try {
							QCElement qce = MappingUtil.newInstanceForElem(Class.forName(name), valArray[val[0]],
									Integer.parseInt(valArray[val[1]]));
							f.set(o, qce);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println(key + "类型不匹配:" + valArray[val[0]]);
							o = key + "类型不匹配:" + valArray[val[0]];
							return o;
						}
					} else { // 格式不正确
						o = "key"+"字段格式不正确";
						return o;
					}
				} else {// 常规对象
					f.setAccessible(true);
					Class cc = null;
					if (name.contains("class ")) {
						name = name.replace("class ", "");
					}
					try {
						cc = Class.forName(name);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						o = e.getMessage();
						return o;
					}
					int[] val = regMap.get(key);
					String relvalue = "";

					if (val.length == 1) { // 常规对象，基本数据类型
						relvalue = valArray[val[0]].replaceAll("\t", " ");
						f.set(o, MappingUtil.convertString(cc, relvalue));
					} else { // 自定义数据类型
						o = "不支持的自定义类型："+name+"，请检查实体类";
						return o;
					}
				}
				index++;
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (SecurityException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} finally {
		}

		return o;
	}
	
	/**
	 * 本方法已定制成日值数据的实体类装填方法 装填实体类对应的属性，自动读取本工程classpath下的fields_surf_day.conf文件
	 * 
	 * @param cla
	 *            需要封装成的实体类
	 * @param values
	 *            一份完整的数据，字段间空隔分割，按顺序排列，对应一个实体类对象
	 * @return    返回日值对应实体类cla的对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static Object fillVal_day(Class cla/* ,String propFileName */, String values) {
		Object o = null;
		int index = 0;
		try {
			// new 出对象
			o = cla.newInstance();
			Map<String, int[]> map = null;
			map = MappingUtil.getDayMapping();
			String[] valArray = values.split(" ");
			List<String> keys = new ArrayList(map.keySet());
			for (String key : keys) { // 初始化所有的field
				Field f = cla.getDeclaredField(key);
				String name = f.getGenericType().toString(); // 泛型变量和数组变量需分别处理
				if (name.contains("[")) {// 数组,泛型数组也在此处理
					if (key.equals("qualityControl")) {
						int[] val = map.get(key);
						QualityControl[] vv = new QualityControl[3];
						if (val.length == 1) {// 正确

						} else {
							System.out.println("文件格式错误,qualityControl");
							o = "文件格式错误,qualityControl";
							return o;
						}
						for (int i = 0; i < 3; i++) {
							if (valArray[val[0]].charAt(i) == '0') {
								vv[i] = QualityControl.MANUAL;
							} else if (valArray[val[0]].charAt(i) == '1') {
								vv[i] = QualityControl.SOFTWARE;
							} else if (valArray[val[0]].charAt(i) == '9') {
								vv[i] = QualityControl.NO_QC;
							} else {
								System.out.println("枚举匹配不上,qualityControl");
								o = null;
								return "枚举匹配不上,qualityControl";
							}
						}
						f.setAccessible(true);
						f.set(o, vv);
					} else {
						System.out.println("文件格式错误,不能识别的数组类型");
						o = "文件格式错误,不能识别的数组类型";
						return o;
					}
				} else if (name.contains("<")) {// 泛型
				  if (key.equals("precipitationEveryMinutes")) { //处理分钟降雨
					    int[] val = map.get(key);
						List<QCElement<Integer>> ti = new ArrayList<>(60);
						for (int i = 0; i < 60; i++) {
							String tempVal = valArray[val[0]].substring(2 * i, 2 * (i + 1));
							ti.add(new QCElement<Integer>(
											Integer.parseInt(DecodeBABJ.NiceForm(tempVal, "%.0f", 1f)),
											Quality.getQualityByCode(
													Integer.parseInt(valArray[val[1]].substring(1 * i, 1 * (i + 1)))),
											Quality.getQualityByCode(
													Integer.parseInt(valArray[val[2]].substring(1 * i, 1 * (i + 1)))),
											Quality.getQualityByCode(
													Integer.parseInt(valArray[val[3]].substring(1 * i, 1 * (i + 1))))));
						}
						f.setAccessible(true);
						f.set(o, ti);
						continue;
					}
					int[] val = map.get(key);
					if (val.length ==2) {
						f.setAccessible(true);
						name = name.substring(name.indexOf('<') + 1, name.indexOf('>'));
						try {
							QCElement qce = MappingUtil.newInstanceForElem(Class.forName(name), valArray[val[0]],
									Integer.parseInt(valArray[val[1]]));
							f.set(o, qce);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println(key + "类型不匹配:" + valArray[val[0]]);
							o = key + "类型不匹配:" + valArray[val[0]];
							return o;
						}
					} else { // 格式不正确
						o = key+"字段格式不正确";
						return o;
					}
				} else {// 常规对象
					f.setAccessible(true);
					Class cc = null;
					if (name.contains("class ")) {
						name = name.replace("class ", "");
					}
					try {
						cc = Class.forName(name);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						o = e.getMessage();
						return o;
					}
					int[] val = map.get(key);
					String relvalue = "";

					if (val.length == 1) { // 常规对象，基本数据类型
						relvalue = valArray[val[0]].replaceAll("\t", " ");
						f.set(o, MappingUtil.convertString(cc, relvalue));
					} else { // 自定义数据类型
						System.out.println(name);
						o = "不支持的类型："+name+"，请检查实体类";
						return o;
					}
				}
				index++;
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (SecurityException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} finally {
		}

		return o;
	}
	
	/**
	 * 本方法已定制成日照数据的实体类装填方法 装填实体类对应的属性，自动读取本工程classpath下的fields_surf_day.conf文件
	 * 
	 * @param cla
	 *            需要封装成的实体类
	 * @param values
	 *            一份完整的数据，字段间空隔分割，按顺序排列，对应一个实体类对象
	 * @return    返回日照对应实体类cla的对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static Object fillVal_ss(Class cla/* ,String propFileName */, String values) {
		Object o = null;
		int index = 0;
		try {
			// new 出对象
			o = cla.newInstance();
			Map<String, int[]> map = null;
			map = MappingUtil.getSsMapping();
			String[] valArray = values.split(" ");
			List<String> keys = new ArrayList(map.keySet());
			for (String key : keys) { // 初始化所有的field
				Field f = cla.getDeclaredField(key);
				String name = f.getGenericType().toString(); // 泛型变量和数组变量需分别处理
				if (name.contains("[")) {// 数组,泛型数组也在此处理
					if (key.equals("qualityControl")) {
						int[] val = map.get(key);
						QualityControl[] vv = new QualityControl[3];
						if (val.length == 1) {// 正确

						} else {
							System.out.println("文件格式错误,qualityControl");
							o = "文件格式错误,qualityControl";
							return o;
						}
						for (int i = 0; i < 3; i++) {
							if (valArray[val[0]].charAt(i) == '0') {
								vv[i] = QualityControl.MANUAL;
							} else if (valArray[val[0]].charAt(i) == '1') {
								vv[i] = QualityControl.SOFTWARE;
							} else if (valArray[val[0]].charAt(i) == '9') {
								vv[i] = QualityControl.NO_QC;
							} else {
								System.out.println("枚举匹配不上,qualityControl");
								o = "枚举匹配不上,qualityControl";
								return o;
							}
						}
						f.setAccessible(true);
						f.set(o, vv);
					} 
					/*else if (key.equals("cloudType")) { //实际不会进入的分支
						int[] val = map.get(key);
						String[] tct = new String[8];
						for (int i = 0; i < tct.length; i++) {
							String cct = valArray[val[0]].substring(i * 3, 3 * (i + 1));
							tct[i] = cct.contains("/")?DecodeBABJ.NANStr:cct;
						}
						QCElement<String[]> qce = new QCElement<String[]>(tct, Integer.parseInt(valArray[val[1]]),
								Integer.parseInt(valArray[val[2]]), Integer.parseInt(valArray[val[3]]));
						f.setAccessible(true);
						f.set(o, qce);
					}*/ /*
						 * else if(key.equals("weatherManualObservational")){ //暂不处理 已改成String类型 //
						 * int[] val = map.get(key); // Weather[] tw = new Weather[val.length]; }
						 */else {
						System.out.println("文件格式错误,不能识别的数组类型");
						o = "文件格式错误,不能识别的数组类型";
						return o;
					}
				} else if (name.contains("<")) {// 泛型
				  if (key.equals("totalSunshineveryHour")) { //处理分钟降雨
					    int[] val = map.get(key);
						List<QCElement<Double>> ti = new ArrayList<>(val[2]);
						for (int i = 0; i < val[2]; i++) {
							String tempVal = valArray[val[0]+i];
							ti.add(new QCElement<Double>(
											Double.parseDouble(tempVal),
											Quality.getQualityByCode(
													Integer.parseInt(valArray[val[1]+i]))));
						}
						f.setAccessible(true);
						f.set(o, ti);
						continue;
					}
					int[] val = map.get(key);
					if (val.length ==2) {
						f.setAccessible(true);
						name = name.substring(name.indexOf('<') + 1, name.indexOf('>'));
						try {
							QCElement qce = MappingUtil.newInstanceForElem(Class.forName(name), valArray[val[0]],
									Integer.parseInt(valArray[val[1]]));
							f.set(o, qce);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println(key + "类型不匹配:" + valArray[val[0]]);
							o = key + "类型不匹配:" + valArray[val[0]];
							return o;
						}
					} else { // 格式不正确
						o = null;
						return o;
					}
				} else {// 常规对象
					f.setAccessible(true);
					Class cc = null;
					if (name.contains("class ")) {
						name = name.replace("class ", "");
					}
					try {
						cc = Class.forName(name);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						o = e.getMessage();
						return o;
					}
					int[] val = map.get(key);
					String relvalue = "";

					if (val.length == 1) { // 常规对象，基本数据类型
						relvalue = valArray[val[0]].replaceAll("\t", " ");
						f.set(o, MappingUtil.convertString(cc, relvalue));
					} else { // 自定义数据类型
						System.out.println(name);
						o = "不支持的类型："+name+"，请检查实体类";
						return o;
					}
				}
				index++;
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (SecurityException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			o = e.getMessage();
			return o;
		} finally {
		}

		return o;
	}
}
