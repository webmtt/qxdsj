package com.thinkgem.jeesite.mybatis.modules.filedecode.util;

import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean.MonthValueTab;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/4/13
 */
public class ReflectUtil {
      //保留一位小数
      private static DecimalFormat decimalFormat = new DecimalFormat("#.0");
    /**
     使用反射机制动态调用dto的set方法根据参数 属性名 如 name 调用dto的 setName方法 完成赋值
     * @param dto
     * @param name
     * @param value
     * @throws Exception
     */
    public static void setValue(Object dto,String name,Object value){
        if(isExistField(name,dto)) {
            Method[] m = dto.getClass().getDeclaredMethods();
            for (int i = 0; i < m.length; i++) {
                try {
                    String t1 = ("set" + name).toLowerCase();
                    String t2 = m[i].getName().toLowerCase();
                    Class[] paramTypes = m[i].getParameterTypes();
                    if (t1.equals(t2)) {
                        String paramType=null;
//                        for (Class class2 : paramTypes) {
//                            paramType=class2.getName();
//                            if(paramType.indexOf("Float")>-1){
//                                //保留1位小数，4舍5入
//                                Float t=Float.parseFloat(value+"");
//                                BigDecimal bigD = new BigDecimal(t);
//                                float newValue = bigD .setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
//                                value=newValue;
//                            }
//                        }
                        m[i].invoke(dto, value);
                        break;
                    }
                } catch (Exception e) {
//                 e.printStackTrace();
                    System.out.println(("set" + name).toLowerCase() + "----" + m[i].getName().toLowerCase() + "---" + value);
                    System.out.println(dto.getClass() + "----" + name);
                }
            }
        }
    }
    /* 使用反射机制动态调用dto的get方法根据参数 属性名 如 name 调用dto的 getName方法 并获得返回值
     * @param dto
     * @param name
     * @return
     * @throws Exception
     */
    public static Object getValue(Object dto,String name) {
        try {
            if(isExistField(name,dto)) {
                Method[] m = dto.getClass().getMethods();
                for (int i = 0; i < m.length; i++) {
                    if (("get" + name).toLowerCase().equals(m[i].getName().toLowerCase())) {
                        return m[i].invoke(dto);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     * */
    public static Map<String,String> getFiledsInfo(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        Map<String,String> result=new HashMap<>();
        for(int i=0;i<fields.length;i++){
            result.put(fields[i].getName(), fields[i].getType().getName()+"");
        }
        return result;
    }
    /***
     * 获取私有成员变量的值
     *
     */
    public static Object getPrivateValue(Object instance, String fieldName)
            throws IllegalAccessException, NoSuchFieldException {

        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // 参数值为true，禁止访问控制检查

        return field.get(instance);
    }
    /**
     * 判断你一个类是否存在某个属性（字段）
     *
     * @param field 字段
     * @param obj   类对象
     * @return true:存在，false:不存在, null:参数不合法
     */
    public static Boolean isExistField(String field, Object obj) {
        if (obj == null || StringUtils.isEmpty(field)) {
            return null;
        }
        Object o = JSONObject.toJSON(obj);
        JSONObject jsonObj = new JSONObject();
        if (o instanceof JSONObject) {
            jsonObj = (JSONObject) o;
        }
        char[] chars = new char[1];
        chars[0] = field.charAt(0);
        String temp = new String(chars);
        if (chars[0] >= 'A' && chars[0] <= 'Z') {//当为字母时，则转换为小写答
            field= field.replaceFirst(temp, temp.toLowerCase());
        }
        return jsonObj.containsKey(field);
    }

    public static void main(String[] args) {
        Map<String,String> map=ReflectUtil.getFiledsInfo(new MonthValueTab());
    }
}
