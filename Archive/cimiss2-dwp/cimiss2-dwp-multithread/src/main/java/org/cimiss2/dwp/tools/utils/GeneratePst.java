package org.cimiss2.dwp.tools.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.annotation.Column;

/**
 * <br>
 * @Title:  GeneratePst.java
 * @Package org.cimiss2.dwp.z_agme.pheno_test
 * @Description: 将实体类中的值设置到对应的PreparedStatement中
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年1月5日 下午4:07:19   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class GeneratePst {

	private static final Logger logger = LoggerFactory.getLogger(GeneratePst.class);
	private static final String COLUMN_GROUP_REGEX = "\\(.*?\\)";
	private static final String SUPERFLUOUS_CHARACTER = "\\(|\\)|\\s+|`";

	/**
	 * @Title: processByColumnName
	 * @Description: 根据实体类中设置的注解name设置对应的PreparedStatement中sql的值
	 * @param object 已有值的实体类
	 * @param pst 已设置好带有sql语句需要设置值的PreparedStatement
	 * @param sql 带有字段和对应问号的sql语句，此sql中必须包含字段
	 * @throws:
	 */
	public static void processByColumnName(Object object, PreparedStatement pst, String sql) {
		Class<? extends Object> cls = object.getClass();

		Field[] fields = cls.getDeclaredFields(); // 获取实体类的所有属性，返回Field数组

		Matcher m = Pattern.compile(COLUMN_GROUP_REGEX).matcher(sql);
		String[] columns = null;
		if (m.find()) {
			String columnGroup = m.group(0);
			columns = columnGroup.replaceAll(SUPERFLUOUS_CHARACTER, "").toUpperCase().split(",");
		}

		for (Field field : fields) {

			Column annotation = field.getAnnotation(Column.class);

			String value = annotation.value().trim();// 注解name对应数据库字段
			int index = ArrayUtils.indexOf(columns, value.toUpperCase()); // 此字段在sql中的位置，即：第几个字段所在的值

			process(object, pst, cls, field, index);
		}
	}

	/**
	 * 
	 * @Title: processByColumnOrder
	 * @Description: 根据实体类中设置的注解order（设置须与sql中对应的字段位置序号一致）设置对应的值
	 * @param object 已有值 的实体类
	 * @param pst 已设置好带有sql语句需要设置值的PreparedStatement
	 * @param sql 带有字段和对应问号的sql语句，此sql中必须包含字段
	 * @throws:
	 */
	/*public static void processByColumnOrder(Object object, PreparedStatement pst, String sql) {
		Class<? extends Object> cls = object.getClass();

		Field[] fields = cls.getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		for (Field field : fields) {

			Column annotation = field.getAnnotation(Column.class);

			int order = annotation.order(); // 注解字段序号

			process(object, pst, cls, field, order);
		}
	}*/

	/**
	 * 
	 * @Title: process
	 * @Description: 为PreparedStatement设置值
	 * @param object 已有值 的实体类
	 * @param pst 已设置好带有sql语句需要设置值的PreparedStatement
	 * @param cls
	 * @param field
	 * @param order 注解字段序号
	 * @throws:
	 */
	private static void process(Object object, PreparedStatement pst, Class<? extends Object> cls, Field field, int order) {
		String name = field.getName(); // 获取属性的名字
		name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
		String type = field.getGenericType().toString(); // 获取属性的类型

		Method m;
		Object value = null;
		try {
			m = cls.getMethod("get" + name);
			value = m.invoke(object); // 调用getter方法获取属性值
		} catch (NoSuchMethodException e) {
			logger.error(" \n获取getter方法，请检查", e);
		} catch (IllegalAccessException e) {
			logger.error(" \n获取getter方法值失败，请检查", e);
		} catch (InvocationTargetException e) {
			logger.error(" \n获取getter方法值失败，请检查", e);
		}
		try {
			if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
				pst.setString(order, (String) value);

			} else if (type.equals("class java.lang.Integer")) {
				pst.setInt(order, (Integer) value);
			} else if (type.equals("class java.lang.Boolean")) {
				pst.setBoolean(order, (Boolean) value);
			} else if (type.equals("class java.util.Date")) {
				Timestamp timestamp = new Timestamp(((Date) value).getTime());
				pst.setTimestamp(order, timestamp);
			} else if (type.equals("class java.lang.Double")) {
				pst.setBigDecimal(order, NumberUtil.FormatNumOrNine((Double) value));
			} else if (type.equals("class java.math.BigDecimal")) {
				pst.setBigDecimal(order, (BigDecimal) value);
			} else if (type.equals("class java.lang.Long")) {
				pst.setLong(order, (Long) value);
			} else if (type.equals("class java.lang.Short")) {
				pst.setShort(order, (Short) value);
			} else if (type.equals("class java.lang.Byte")) {
				pst.setByte(order, (Byte) value);
			} else if (type.equals("class java.lang.Float")) {
				pst.setFloat(order, (Float) value);
			} else if (type.equals("class java.lang.Character")) {
				pst.setString(order, (String) value);
			}
		} catch (SQLException e) {
			logger.error(" \n设置字段值失败", e);
		}
	}
}
