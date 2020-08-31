package cma.cimiss2.dpc.indb.surf;

import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.etl.mysqlETL.AbstractEtl;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @Description: 并行入库线程
 * @Aouthor: xzh
 * @create: 2018-05-10 17:46
 */
public class WriteMysqlTask implements Callable<StatInsert> {
    private List list;
    //        private Class<? extends AbstractEtl> clazz;
    boolean overwriteIfNotExist;
    private String dao;
    private Object obj;

    //DayLightEtl dayLightEtl
    public WriteMysqlTask(Object obj, List list, String dao) {
        this.list = list;
        this.dao = dao;
        this.obj = obj;
    }

    /**
     * 重写线程执行结束回调函数
     *
     * @return
     * @throws Exception
     */
    @Override
    public StatInsert call() throws Exception {
        StatInsert statInsert = null;
        if (obj instanceof AbstractEtl) {
            statInsert = ((AbstractEtl) obj).load(list, dao);
        } else {
            statInsert.setBuffer(new StringBuffer(getClass() + "类型错误！"));
        }

//        AbstractEtl abstractEtl = IocBuilder.ioc("").get(clazz);
//        StatInsert statInsert = abstractEtl.load(list, dao);
        return statInsert;
    }

    /**
     * @param threadName 插入线程名
     * @return
     */
    public synchronized FutureTask<StatInsert> run(String threadName) {
        FutureTask<StatInsert> futureDataTask = null;
        try {
            futureDataTask = new FutureTask<>(this);
            Thread dataWorker = new Thread(futureDataTask, threadName);
            dataWorker.start();
        } catch (Exception e) {
            System.out.println("线程名：" + threadName + "  " + e.getMessage());
        }
        return futureDataTask;
    }

}
