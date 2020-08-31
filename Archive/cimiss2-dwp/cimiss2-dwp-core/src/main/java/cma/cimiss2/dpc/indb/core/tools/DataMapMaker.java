package cma.cimiss2.dpc.indb.core.tools;

import org.nutz.lang.Mirror;

import cma.cimiss2.dpc.decoder.annotation.Column;
import cma.cimiss2.dpc.decoder.bean.QCElement;
import cma.cimiss2.dpc.decoder.tools.enumeration.Quality;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataMapMaker {
    /**
     * 通过 bean 的注解生成 dao.inser 所需的 map
     * <p>
     * o
     *
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Map<String, Object> make(Object o, int index) throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> result = new HashMap<String, Object>();
        Mirror mirror = Mirror.me(o);
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            Column[] columns = field.getAnnotationsByType(Column.class);
            Object fieldValue = mirror.getValue(o, field.getName());
            // 遍历所有 Column 注解
            c:
            for (Column column : columns) {
                String columnName = column.value().trim();
                String pattern = column.pattern();
                if (!pattern.isEmpty() && field.getType() == Date.class) {
                    DateFormat formatter = new SimpleDateFormat(pattern);
                    result.put(columnName, formatter.format(fieldValue));
                } else if (field.getType() == QCElement.class) {
                    QCElement qe = (QCElement) fieldValue;
                    if (qe == null) {
                        result.put(columnName, sNull(null));
                    } else {
                        result.put(columnName, sNull(qe.getValue()));
                        result.put(columnName.replaceFirst("V", "Q"),
                                // TODO 暂时只入第二个质控代码
                                ((Quality) qe.getQuality().get(index)).getCode());

                    }

                } else if (field.getType() == List.class) {
                    if (field.get(o) != null) {
                        for (int i = 0; i < ((List) fieldValue).size(); i++) {
                            Object item = ((List) fieldValue).get(i);
                            if (!DataMapMaker.isWrapperType(item.getClass())) { // 如果不是基本类型，不入库。例如每分钟降水量，是带着质控码的，被包裹成
                                // QCElement
                                // ，入库时是另外处理
                                continue c;
                            }
                            result.put(columnName + "_" + alignRight(String.valueOf(i), 2, '0'), sNull(item));
                        }
                    }
                } else {
                    result.put(columnName, sNull(fieldValue));
                }
            }

        }
        return result;
    }

    private static Object sNull(Object o) {
        return o == null ? 999999 : o;

    }

    /**
     * 在字符串左侧填充一定数量的特殊字符
     *
     * @param width 字符数量
     * @param c     字符
     * @return 新字符串
     * @ o
     * 可被 toString 的对象
     */
    public static String alignRight(String s, int width, char c) {
        if (null == s)
            return null;
        int len = s.length();
        if (len >= width)
            return s;
        return new StringBuilder().append(dup(c, width - len)).append(s).toString();
    }

    /**
     * 复制字符
     *
     * @param c   字符
     * @param num 数量
     * @return 新字符串
     */
    public static String dup(char c, int num) {
        if (c == 0 || num < 1)
            return "";
        StringBuilder sb = new StringBuilder(num);
        for (int i = 0; i < num; i++)
            sb.append(c);
        return sb.toString();
    }

    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }

}
