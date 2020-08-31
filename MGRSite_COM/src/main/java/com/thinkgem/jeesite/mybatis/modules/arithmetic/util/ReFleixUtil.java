package com.thinkgem.jeesite.mybatis.modules.arithmetic.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author yck
 * @version 1.0
 * @date 2019/11/25 9:51
 */
public class ReFleixUtil {
    //参数格式:"E:\\work\\jeesite\\src\\main\\java\\com\\thinkgem\\jeesite\\modules\\arithmetic\\util\\commons-lang-2.6.jar","org.apache.commons.lang.StringUtils","isBlank","1234"
//    public static Object functionHandle(String jarPath, String classUrl, String methods, String params) {
//        try {
//            /*动态加载指定jar包调用其中某个类的方法*/
//            File file = new File(jarPath); // jar包的路径
//            URL url = file.toURI().toURL();
//            ClassLoader loader = new URLClassLoader(new URL[]{url}); // 创建类加载器
//            Class<?> clazz = loader.loadClass(classUrl); // 加载指定类，注意一定要带上类的包名
//            //返回值
//            Object result = null;
//            //找到方法
//            Method[] mes = clazz.getMethods();
//            Method m = null;
//            for (Method ms : mes) {
//                if (ms.getName().equals(methods)) {
//                    m = ms;
//                }
//            }
//            if (m.toString().substring(m.toString().indexOf("("), m.toString().indexOf(")") + 1).contains(",")) {
//                String[] p = m.toString().substring(m.toString().indexOf("("), m.toString().indexOf(")") + 1).split(",");
//                Class<?>[] typeA = new Class[p.length]; // 传入要调用的方法的参数类型
//                Object[] objsA = new Object[p.length]; // 传入要调用的方法的具体参数
//                String[] param = params.split(",");
//                for (int i = 0, t = p.length; i < t; i++) {
//                    if (p[i].contains("String")) {
//                        typeA[i] = String.class;
//                        objsA[0] = param[i];
//                    } else if (p[i].contains("int") || p[i].contains("Integer")) {
//                        typeA[i] = Integer.class;
//                        int t1 = Integer.parseInt(param[i]);
//                        objsA[0] = t1;
//                    } else if (p[i].contains("float")) {
//                        typeA[i] = Float.class;
//                        float f = Float.parseFloat(param[i]);
//                        objsA[i] = f;
//                    } else if (p[i].contains("long")) {
//                        typeA[i] = long.class;
//                        long t2 = Long.parseLong(param[i]);
//                        objsA[i] = t2;
//                    }else if(p[i].contains("double")) {
//                        typeA[i] = double.class;
//                        double t3 = Double.parseDouble(param[i]);
//                        objsA[i] = t3;
//                    }
//                }
//                Method method = clazz.getMethod(methods, typeA); // 获取要被调用的特定方法  getName(String xx)
//                result = method.invoke(clazz, objsA); // 调用方法，获取方法的返回值。
//            } else {
//                String p = m.toString().substring(m.toString().indexOf("("), m.toString().indexOf(")") + 1);
//                Class<?> typeA = String.class;
//                if (p.contains("String")) {
//                    typeA = String.class;
//                } else if (p.contains("int") || p.contains("Integer")) {
//                    typeA = Integer.class;
//                } else if (p.contains("float")) {
//                    typeA = Float.class;
//                } else if (p.contains("long")) {
//                    typeA = long.class;
//                }else if(p.contains("double")) {
//                    typeA = double.class;
//                }
//                Method method = clazz.getMethod(methods, typeA); // 获取要被调用的特定方法  getName(String xx)
//                result = method.invoke(clazz, params); // 调用方法，获取方法的返回值。
//            }
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    public static JSONObject functionHandles(String jarPath, String classUrl, String methods,String results, String params) {
        JSONObject result = null;
        try {
            /*动态加载指定jar包调用其中某个类的方法*/
            File file = new File(jarPath); // jar包的路径
            URL url = file.toURI().toURL();
            ClassLoader loader = new URLClassLoader(new URL[]{url}); // 创建类加载器
            Class<?> clazz = loader.loadClass(classUrl); // 加载指定类，注意一定要带上类的包名
            //返回值

            //找到方法
            Method[] mes = clazz.getMethods();
            Method m = null;
            for (Method ms : mes) {
                if (ms.getName().equals(methods)) {
                    m = ms;
                }
            }
            if (m.toString().substring(m.toString().indexOf("("), m.toString().indexOf(")") + 1).contains(",")) {
                String[] p = m.toString().substring(m.toString().indexOf("("), m.toString().indexOf(")") + 1).split(",");
                Class<?>[] typeA = new Class[p.length]; // 传入要调用的方法的参数类型
                Object[] objsA = new Object[p.length]; // 传入要调用的方法的具体参数
                String[] param = params.split(",");
                objsA[0] = results;
                typeA[0] = String.class;
                 for (int i = 1;i < p.length; i++) {
                    if (p[i].contains("String")) {
                        typeA[i] = String.class;
                        objsA[i] = param[i-1];
                    } else if (p[i].contains("int") || p[i].contains("Integer")) {
                        typeA[i] = Integer.class;
                        int t1 = Integer.parseInt(param[i-1]);
                        objsA[i] = t1;
                    } else if (p[i].contains("float")) {
                        typeA[i] = Float.class;
                        float f = Float.parseFloat(param[i-1]);
                        objsA[i] = f;
                    } else if (p[i].contains("long")) {
                        typeA[i] = long.class;
                        long t2 = Long.parseLong(param[i-1]);
                        objsA[i] = t2;
                    }else if(p[i].contains("double")) {
                        typeA[i] = double.class;
                        double t3 = Double.parseDouble(param[i-1]);
                        objsA[i] = t3;
                    }
                }
                Method method = clazz.getMethod(methods, typeA); // 获取要被调用的特定方法  getName(String xx)
                result = JSONObject.fromObject(method.invoke(clazz, objsA)); // 调用方法，获取方法的返回值。
            } else {
                String p = m.toString().substring(m.toString().indexOf("("), m.toString().indexOf(")") + 1);
                Class<?> typeA = String.class;
                if (p.contains("String")) {
                    typeA = String.class;
                } else if (p.contains("int") || p.contains("Integer")) {
                    typeA = Integer.class;
                } else if (p.contains("float")) {
                    typeA = Float.class;
                } else if (p.contains("long")) {
                    typeA = long.class;
                }else if(p.contains("double")) {
                    typeA = double.class;
                }
                Method method = clazz.getMethod(methods, typeA); // 获取要被调用的特定方法  getName(String xx)
                result = JSONObject.fromObject(method.invoke(clazz, params)); // 调用方法，获取方法的返回值。
            }
        } catch (Exception e) {
            String json =   "{\"code\":400,\"DS\":[{\"Message\":'API接口错误或算法参数传入错误',\"data\":''}],\"message\":\"查询数据失败\"}";
            result = JSONObject.fromObject(json);
        }
        return result;
    }
}
