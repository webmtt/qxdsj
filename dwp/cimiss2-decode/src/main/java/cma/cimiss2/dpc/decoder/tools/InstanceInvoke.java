package cma.cimiss2.dpc.decoder.tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InstanceInvoke {
	
	public static void setValues(Object object, String attributeName, Object value) {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");
		if(value.toString().trim().equals(""))
			value = "999999";
		try {
			Class<? extends Object> cls = object.getClass();
			Field field = null;
			try {
				field = cls.getDeclaredField(attributeName);	//如果子类没有找到对应的要素，取父类要素
			} catch (Exception e) {
				try {
					field = cls.getSuperclass().getDeclaredField(attributeName);
				} catch (Exception e1) {
//					e1.printStackTrace();
				}
			}
			
			if(field != null) {
				String name = field.getName(); // 获取属性的名字
				name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
				String type = field.getGenericType().toString(); // 获取属性的类型
				if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
					Method m = cls.getMethod("set" + name, String.class);
					m.invoke(object, value);
				}else if (type.equals("class java.lang.Integer")) {
					Method m = cls.getMethod("set" + name, Integer.class);
					m.invoke(object, value);
				}else if (type.equals("class java.lang.Boolean")) {
					Method m = cls.getMethod("set" + name, Boolean.class);
					m.invoke(object, value);
				}else if (type.equals("class java.util.Date")) {
					Method m = cls.getMethod("set" + name, Date.class);
					try {
						m.invoke(object, SDF.parse(value.toString()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else if (type.equals("class java.lang.Double")) {
					Method m = cls.getMethod("set" + name, Double.class);
					m.invoke(object, Double.parseDouble(value.toString()));
				}else if (type.equals("class java.math.BigDecimal")) {
					Method m = cls.getMethod("set" + name, BigDecimal.class);
					m.invoke(object, value);
				}else if (type.equals("class java.lang.Long")) {
					Method m = cls.getMethod("set" + name, Long.class);
					m.invoke(object, value);
				}else if (type.equals("class java.lang.Short")) {
					Method m = cls.getMethod("set" + name, Short.class);
					m.invoke(object, value);
				}else if (type.equals("class java.lang.Byte")) {
					Method m = cls.getMethod("set" + name, Byte.class);
					m.invoke(object, value);
				}else if (type.equals("class java.lang.Float")) {
					Method m = cls.getMethod("set" + name, Float.class);
					m.invoke(object, value);
				}else if (type.equals("class java.lang.Character")) {
					Method m = cls.getMethod("set" + name, Character.class);
					m.invoke(object, value);
				}
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}
}
