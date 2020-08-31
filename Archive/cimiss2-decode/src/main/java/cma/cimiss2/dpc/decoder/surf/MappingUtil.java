package cma.cimiss2.dpc.decoder.surf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cma.cimiss2.dpc.decoder.bean.QCElement;
import cma.cimiss2.dpc.decoder.tools.enumeration.CorrectionIndicator;
import cma.cimiss2.dpc.decoder.tools.enumeration.GroundState;
import cma.cimiss2.dpc.decoder.tools.enumeration.ObservationMethod;


/**
 * *****************************************************************************************<br> 
 * <strong>All Rights Reserved By National Meteorological Information Centre (NMIC)</strong><br>
 * *****************************************************************************************<br>

 * Read the conf file to fill the fields. <br>
 * 读取配置文件，以便装填解码实体类中的各属性。
 * 
 * <br>
 * 本类为装填成实体类对象的工具类。提供字段映射的map，类型转换的方法，以及范型对象发射构造的方法。
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 11/20/2017   lihanjie    Initial creation.
 * 11/22/2017   lihanjie    Change the file read code.
 * </pre>
 * 
 * @author lihanjie
 *
 * 
 */
public class MappingUtil {

	/** 日期格式，用于校验和转换报文中的时间字段 */
	public final static String DATE_FORMATE = "yyyyMMddHHmmss";

	/** 国家站资料字段映射的map，类加载后自动读取本工程classpath下的fields_surf.conf */
	private static Map<String, int[]> map = null;
	/** 区域站资料字段映射的map */
	private static Map<String, int[]> regMap = null;
	/** 日值资料字段映射的map */
	private static Map<String, int[]> dayMap = null;
	/** 日照资料字段映射的map */
	private static Map<String, int[]> ssMap = null;

	static { //读取国家站实体类的配置文件
		BufferedReader sc;
		try {
			sc = new BufferedReader(new InputStreamReader(MappingUtil.class
					.getClassLoader()
					.getResourceAsStream("fields_surf.conf")
					));
			String line = null;
			Map<String, int[]> map = new HashMap<>();
			while ((line = sc.readLine()) != null) {
				String[] kv = line.split("=");
				String key = kv[0].trim();
				String[] value = kv[1].trim().split(",");
				int[] vv = new int[value.length];
				for (int i = 0; i < value.length; i++) {
					vv[i] = Integer.parseInt(value[i].trim());
				}
				map.put(key, vv);
			}
			MappingUtil.map = map;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static { //读取国家站实体类的配置文件
		BufferedReader sc;
		try {
			sc = new BufferedReader(new InputStreamReader(MappingUtil.class
					.getClassLoader()
					.getResourceAsStream("fields_surf_reg.conf")
					));
			String line = null;
			Map<String, int[]> regMap = new HashMap<>();
			while ((line = sc.readLine()) != null) {
				String[] kv = line.split("=");
				String key = kv[0].trim();
				String[] value = kv[1].trim().split(",");
				int[] vv = new int[value.length];
				for (int i = 0; i < value.length; i++) {
					vv[i] = Integer.parseInt(value[i].trim());
				}
				regMap.put(key, vv);
			}
			MappingUtil.regMap = regMap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static { //日值实体类的配置文件
		BufferedReader sc;
		try {
			sc = new BufferedReader(new InputStreamReader(MappingUtil.class
					.getClassLoader()
					.getResourceAsStream("fields_surf_day.conf")
					));
			String line = null;
			Map<String, int[]> dayMap = new HashMap<>();
			while ((line = sc.readLine()) != null) {
				String[] kv = line.split("=");
				String key = kv[0].trim();
				String[] value = kv[1].trim().split(",");
				int[] vv = new int[value.length];
				for (int i = 0; i < value.length; i++) {
					vv[i] = Integer.parseInt(value[i].trim());
				}
				dayMap.put(key, vv);
			}
			MappingUtil.dayMap = dayMap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static { //读取日照数据实体类的配置文件
		BufferedReader sc;
		try {
			sc = new BufferedReader(new InputStreamReader(MappingUtil.class
					.getClassLoader()
					.getResourceAsStream("fields_surf_ss.conf")
					));
			String line = null;
			Map<String, int[]> map = new HashMap<>();
			while ((line = sc.readLine()) != null) {
				String[] kv = line.split("=");
				String key = kv[0].trim();
				String[] value = kv[1].trim().split(",");
				int[] vv = new int[value.length];
				for (int i = 0; i < value.length; i++) {
					vv[i] = Integer.parseInt(value[i].trim());
				}
				map.put(key, vv);
			}
			MappingUtil.ssMap = map;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取读取配置文件后生成的MAP(国家站)
	 * @return
	 */
	public static Map<String, int[]> getMapping()  {
		return map;
	}
	
	/**
	 * 获取读取配置文件后生成的MAP(区域站)
	 * @return
	 */
	public static Map<String, int[]> getRegMapping()  {
		return regMap;
	}

	/**
	 * 读取配置文件生成Map的方法
	 * @param fileName  
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private static Map<String, int[]> getMapping(String fileName)
			throws NumberFormatException, IOException {
		BufferedReader sc = new BufferedReader(new InputStreamReader(
				MappingUtil.class.getClassLoader()
						.getResourceAsStream(fileName)));
		String line = null;
		Map<String, int[]> map = new HashMap<>();
		while ((line = sc.readLine()) != null) {
			String[] kv = line.split("=");
			String key = kv[0].trim();
			String[] value = kv[1].trim().split(",");
			int[] vv = new int[value.length];
			for (int i = 0; i < value.length; i++) {
				vv[i] = Integer.parseInt(value[i].trim());
			}
			map.put(key, vv);
		}
		return map;
	}
	
	/**
	 * 获取读取配置文件后生成的MAP(日值数据)
	 * @return
	 */
	public static Map<String, int[]> getDayMapping()  {
		return dayMap;
	}
	
	/**
	 * 获取读取配置文件后生成的MAP(日照数据)
	 * @return
	 */
	public static Map<String, int[]> getSsMapping()  {
		return ssMap;
	}

	/**
	 * 将String类型的数据转换成目标类型<br>
	 * 装填实体类时，所有的字段类型都是String，为了转换成对应的类型，需要用此方法进行转换。
	 * @param cla  目标字段类型,目前支持的类型包括：{@link String}，{@link Double}，{@link Date}，{@link Integer}，{@link ObservationMethod}，{@link GroundState}，{@link CorrectionIndicator}。如果输入了不支持的类型，则返回null
	 * @param val  输入的String值
	 * @return     返回目标对象
	 */
	public static <T> T convertString(Class<T> cla, String val) {
		String name = cla.getSimpleName();
		if (name.contains("String")) {
			T t = cla.cast(val);
			return t;
		} else if (name.contains("Double")) {
			T t = cla.cast(Double.parseDouble(val));
			return t;
		} else if (name.contains("Integer")) {
			T t = cla.cast(Integer.parseInt(val));
			return t;
		} else if (name.contains("Date")) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATE);
			T t = null;
			try {
				t = cla.cast(sdf.parse(val));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return t;
		} else if (name.contains("ObservationMethod")) {
			ObservationMethod om = getByCode(ObservationMethod.class,Integer.parseInt(val));
			T t = null;
			t = cla.cast(om);
			return t;
		} else if (name.contains("GroundState")) {
			GroundState t = GroundState.valueOf("S"+ Integer.parseInt(val));
		return cla.cast(t);
		} else if (name.contains("CorrectionIndicator")) {
			CorrectionIndicator t = CorrectionIndicator.valueOf(val);
		    return cla.cast(t);
		} else {
			System.out.println(cla);
			throw new ClassCastException("String cannot be cast to "+cla.getSimpleName());
		}
	}

	/**
	 * 构造泛型QCElement的实体对象
	 * 
	 * @param cla    范型的实际类型
	 * @param val    需要转换的数据
	 * @param codes  对应的质控码
	 * @return       返回QCElement的实体对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> QCElement<T> newInstanceForElem(Class<T> cla, String val,
			int... codes ) {
		String name = cla.getSimpleName();
		if (name.contains("String")) {
			QCElement<?> t = new QCElement<String>(val, codes);
			return (QCElement<T>) t;
		} else if (name.contains("Double")) {
			QCElement<?> t = new QCElement<Double>(Double.parseDouble(val),
					codes);
			return (QCElement<T>) t;
		} else if (name.contains("Integer")) {
			QCElement<?> t = new QCElement<Integer>(Integer.parseInt(val),
					codes);
			return (QCElement<T>) t;
		} else if (name.contains("Date")) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATE);
			QCElement<?> t = null;
			try {
				t = new QCElement<Date>(sdf.parse(val), codes);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return (QCElement<T>) t;
		}/*
		 * else if(name.contains("Weather")){ QCElement<?> t = new
		 * QCElement<Weather>(Weather.valueOf("W"+Integer.parseInt(val)), code1,
		 * code2, code3); return (QCElement<T>) t; }
		 */else if (name.contains("GroundState")) {
//			if(val.equals("999999")) {
//				QCElement<?> t = new QCElement<GroundState>(GroundState.valueOf("S31"), code1, code2, code3);
//				return (QCElement<T>) t;
//			}
			QCElement<?> t = new QCElement<GroundState>(GroundState.valueOf("S"
					+ Integer.parseInt(val)), codes);
			return (QCElement<T>) t;
		} else {
			return null;
		}
	}

	/**
	 * 根据code获取枚举对象
	 * 
	 * @param <T>        枚举类型
	 * @param enumType   枚举类
	 * @param code       枚举对应的code
	 * @return           返回对应的枚举对象
	 */
	public static <T extends Enum<T>> T getByCode(Class<T> enumType, int code) {
		T r = null;
		for (T t : enumType.getEnumConstants()) {
			try {
				Field f = t.getClass().getDeclaredField("code");
				f.setAccessible(true);
				if (code == f.getInt(t)) {
					r = t;
				}
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return r;
	}
	
}
