package cma.cimiss2.dpc.indb.surf;

import cma.cimiss2.dpc.indb.core.bean.StatInsert;
import cma.cimiss2.dpc.indb.core.etl.otsETL.AbstractEtl;
import cma.cimiss2.dpc.indb.core.ots.OTSHelper;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @Description: 并行入库线程
 * @Aouthor: xzh
 * @create: 2018-05-10 17:46
 */
public class WriteOTSTask implements Callable<StatInsert> {
    private List list;
    private OTSHelper helper;
    //    private Class<? extends AbstractEtl> clazz;
    boolean overwriteIfNotExist;
    private Object obj;

    /*public WriteOTSTask(Class<? extends AbstractEtl> clazz, List list, OTSHelper helper, boolean overwriteIfNotExist) {
        this.clazz = clazz;
        this.list = list;
        this.helper = helper;
        this.overwriteIfNotExist = overwriteIfNotExist;
    }*/
    public WriteOTSTask(Object obj, List list, OTSHelper helper, boolean overwriteIfNotExist) {
        this.obj = obj;
        this.list = list;
        this.helper = helper;
        this.overwriteIfNotExist = overwriteIfNotExist;
    }

    /**
     * 重写线程执行结束回调函数
     *
     * @return
     * @throws Exception
     */
    @Override
    public StatInsert call() throws Exception {
//        AbstractEtl abstractEtl = clazz.newInstance();
//        StatInsert statInsert = abstractEtl.load(list, helper, overwriteIfNotExist);
        StatInsert statInsert = null;
        if (obj instanceof AbstractEtl) {
            statInsert = ((AbstractEtl) obj).load(list, helper, overwriteIfNotExist);
        }
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
