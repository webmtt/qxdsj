package org.cimiss2.dwp.main;

import java.lang.reflect.Method;

public class StartTask {

    /**
     *普通的创建Class方式
     * @param clazzName
     * @return
     * @throws Exception
     */
    public static Object createObject(String clazzName) throws Exception{
        Class<?> clazz = Class.forName(clazzName);
        return clazz.newInstance();
    }

    public static void main(String[] args) throws Exception{
        if (args == null || args.length != 2) {
            System.out.println("args is null,args[0]=class ,args[1]=method ");
            System.exit(1);
        }

        String clazzName = args[0];
        String methodName = args[1];
        Class<?> clazz = Class.forName(clazzName);
        Object object = clazz.newInstance();
        Method method = clazz.getMethod(methodName);
        method.invoke(object);
//        Thread.sleep(1000);
//        method.invoke(object);
        return;
    }

}
