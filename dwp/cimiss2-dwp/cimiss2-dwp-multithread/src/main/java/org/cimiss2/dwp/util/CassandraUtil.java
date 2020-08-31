package org.cimiss2.dwp.util;/**
 * Created by zzj on 2019/7/8.
 */

import cma.cimiss2.dpc.decoder.tools.utils.DecodeConstant;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;

import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: cimiss2-dwp
 * @description:
 * @author: zzj
 * @create: 2019-07-08 21:51
 **/
public class CassandraUtil {
    private static Properties prop = PropertiesUtil.getInstance().getProperties(DecodeConstant.PATH + "config/db.properties");

    private static Session instance = null;
    private static Cluster cluster = null;
    private static Lock lock = new ReentrantLock();

    private CassandraUtil(){}

    public static Session getSession()
    {
        if (null == instance)
        {
            try
            {
                lock.lock();

                if (null == instance)
                {
                    PoolingOptions poolingOptions = new PoolingOptions();
                    poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL, 2);
                    poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL, 10);
                    cluster = Cluster.builder()
                            .addContactPoints(prop.getProperty("cassandra.url").split(","))
                            .withPort(Integer.parseInt(prop.getProperty("cassandra.port")))
                            .withCredentials(prop.getProperty("cassandra.username"), prop.getProperty("cassandra.password"))
                            .withPoolingOptions(poolingOptions)
                            .build();
                    instance = cluster.connect();
                }
            }
            finally
            {
                lock.unlock();
            }
        }
        return instance;
    }

    public static void close()
    {
        if (null == cluster)
        {
            try
            {
                lock.lock();

                if (null == cluster)
                {
                    cluster.close();
                }
            }
            finally
            {
                lock.unlock();
            }
        }
    }
}

