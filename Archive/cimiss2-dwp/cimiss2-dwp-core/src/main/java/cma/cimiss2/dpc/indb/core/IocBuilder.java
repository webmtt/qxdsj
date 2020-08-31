package cma.cimiss2.dpc.indb.core;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.AnnotationIocLoader;
import org.nutz.ioc.loader.combo.ComboIocLoader;
import org.nutz.ioc.loader.json.JsonLoader;

import java.io.Serializable;
import java.util.Base64;


/**
 * 创建 IOC , 将 DAO、config 等依赖加载
 *
 * @author shevawen
 */
public class IocBuilder implements Serializable {

    private static final long serialVersionUID = -427436296422353911L;
    public static  Ioc ioc;

    public static Ioc ioc(String path) {
        if (ioc == null) {
            synchronized (IocBuilder.class) {
                if (ioc == null) {
                    ioc = new NutIoc(new ComboIocLoader(new AnnotationIocLoader("cma.cimiss2.dpc.indb.core.etl.mysqlETL"), new JsonLoader("ioc/")));
                    ioc.get(PropertiesProxy.class, "config").setPaths(path + "db.properties");
                    PropertiesProxy proxy = ioc.get(PropertiesProxy.class, "config");
                    if(proxy.containsKey("rdb.password")){
                    	String p = proxy.get("rdb.password");
                    	String pp = new String(Base64.getDecoder().decode(p.getBytes()));
                    	proxy.set("rdb.password", pp.substring(0, pp.length() - 4));
                    }
                    if(proxy.containsKey("cimiss.password")){
                    	String p = proxy.get("cimiss.password");
                    	String pp = new String(Base64.getDecoder().decode(p.getBytes()));
                    	proxy.set("cimiss.password", pp.substring(0, pp.length() - 4));
                    }
//                    ioc.get(PropertiesProxy.class, "config").setPaths("config/db.properties");
                }
            }
        }
        return ioc;
    }
    
    
    public static void main(String[] args) {
    	IocBuilder.ioc("config/");
	}
}

