package cma.cimiss2.dpc.decoder.fileDecode.util.reflect;

import cma.cimiss2.dpc.decoder.fileDecode.common.bean.DayValueTab;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/4/13
 */
public class ReflectUtil {
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
                    String paramType=null;
                    if (t1.equals(t2)) {
//                        for (Class class2 : paramTypes) {
//                            paramType=class2.getName();
//                            if("paramType".indexOf("Float")>-1){
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

}
