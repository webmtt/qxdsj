package cma.cimiss2.dpc.indb.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskMain  {

    public void start(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future future = executorService.submit(new TestTaskRun());//子线程启动
        try {
            future.get();//需要捕获两种异常
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        executorService.shutdown();
    }

}
