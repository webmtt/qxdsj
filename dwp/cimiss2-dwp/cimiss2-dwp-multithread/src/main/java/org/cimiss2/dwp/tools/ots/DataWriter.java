//package org.cimiss2.dwp.tools.ots;
//
//import com.alicloud.openservices.tablestore.AsyncClient;
//import com.alicloud.openservices.tablestore.DefaultTableStoreWriter;
//import com.alicloud.openservices.tablestore.TableStoreWriter;
//import com.alicloud.openservices.tablestore.model.RowUpdateChange;
//import com.alicloud.openservices.tablestore.writer.WriterConfig;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicLong;
//
//public class DataWriter {
//    private long recordStart;
//    private long recordEnd;
//    private long concurrency;
//    private int intervalInSecond;
//    private String tableName;
//    private TableStoreWriter writer;
//    public AtomicLong totalCount = new AtomicLong(0);
//
//    private AtomicBoolean stopped = new AtomicBoolean(true);
//    private List<Thread> workThreads = new ArrayList<Thread>();
//
//    public DataWriter(long recordStart, long recordEnd, long concurrency, int intervalInSecond, String tableName, TableStoreWriter writer) {
//        this.recordStart = recordStart;
//        this.recordEnd = recordEnd;
//        this.concurrency = concurrency;
//        this.intervalInSecond = intervalInSecond;
//        this.tableName = tableName;
//        this.writer = writer;
//    }
//
//    private class WorkThread implements Runnable {
//        private long start;
//        private long end;
//        private int intervalInSecond;
//        private TableStoreWriter writer;
//
//        WorkThread(long start, long end, int intervalInSecond, TableStoreWriter writer) {
//            this.start = start;
//            this.end = end;
//            this.intervalInSecond = intervalInSecond;
//            this.writer = writer;
//        }
//
//        public void run() {
//            long lastPoint = System.currentTimeMillis();
//            while (!stopped.get()) {
//                for (long i = start; i < end; i++) {
//                    String v01301 = new StringBuilder(Long.toHexString(i)).reverse().toString();
//                    String d_record_id = v01301 + lastPoint;
//                    DataRecordCol100 record = new DataRecordCol100(v01301, lastPoint, d_record_id);
//                    RowUpdateChange ruc = new RowUpdateChange(tableName);
//                    ruc.setPrimaryKey(record.getPrimaryKey());
//                    try {
//                        ruc.put(record.getAttributes());
//                    } catch (IOException e) {
//                        throw new IllegalStateException(e);
//                    }
//                    writer.addRowChange(ruc);
//                    totalCount.incrementAndGet();
//                }
//
//                long now = System.currentTimeMillis();
//                while (!stopped.get() && now - lastPoint < intervalInSecond * 1000) {
//                    try {
//                        Thread.sleep(1000);
//                        now = System.currentTimeMillis();
//                    } catch (InterruptedException e) {
//                    }
//                }
//                lastPoint = now;
//            }
//        }
//    }
//
//    public void start() {
//        stopped.set(false);
//        long[] jobStartPoints = createJob();
//        for (int i = 0; i < jobStartPoints.length; i++) {
//            long start = jobStartPoints[i];
//            long end = recordEnd;
//            if (i < jobStartPoints.length - 1) {
//                end = jobStartPoints[i+1];
//            }
//            workThreads.add(new Thread(new WorkThread(start, end, intervalInSecond, writer)));
//        }
//
//        for (Thread t : workThreads) {
//            t.start();
//        }
//    }
//
//    private long[] createJob() {
//        long count = recordEnd - recordStart;
//        long[] jobs;
//        if (count <= concurrency) {
//            jobs = new long[(int)count];
//            for (int i = 0; i < jobs.length; i++) {
//                jobs[i] = 1;
//            }
//        } else {
//            long avgInterval = count / concurrency;
//            jobs = new long[(int)concurrency];
//            for (int i = 0; i < jobs.length; i++) {
//                jobs[i] = avgInterval;
//            }
//
//            long left = count % concurrency;
//            for (int i = 0, j = 0; i < left; i++, j++) {
//                jobs[j] += 1;
//            }
//        }
//
//        long[] jobStartPoints = new long[jobs.length];
//        jobStartPoints[0] = recordStart;
//        for (int i = 1; i < jobs.length; i++) {
//            jobStartPoints[i] = jobStartPoints[i - 1] + jobs[i - 1];
//        }
//        return jobStartPoints;
//    }
//
//    public void stop() throws InterruptedException {
//        if (workThreads != null) {
//            stopped.set(true);
//            for (Thread t : workThreads) {
//                t.join();
//            }
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        int recordStart = 0;
//        int recordEnd = 200000;
//        int concurrency = 50;
//        int interval = 1;
//
//        if (args.length > 0) {
//            Config.endpoint = args[0];
//            recordStart = Integer.parseInt(args[1]);
//            recordEnd = Integer.parseInt(args[2]);
//            concurrency = Integer.parseInt(args[3]);
//            interval = Integer.parseInt(args[4]);
//        }
//
//        AsyncClient client = new AsyncClient(Config.endpoint, Config.accessId,
//                Config.accessKey, Config.instance);
//        String tableName = Config.dataTable;
//
//        Executor executor = Executors.newFixedThreadPool(concurrency);
//        WriterConfig config = new WriterConfig();
//        TableStoreWriter writer = new DefaultTableStoreWriter(client, tableName, config, null, executor);
//        final DataWriter dw = new DataWriter(recordStart, recordEnd, concurrency, interval, tableName, writer);
//        dw.start();
//
//        Thread printSpeed = new Thread(new Runnable() {
//            public void run() {
//                while (true) {
//                    long last = dw.totalCount.get();
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                    }
//
//                    long latest = dw.totalCount.get();
//                    System.out.println("Speed: " + (latest - last) + " records/s.");
//                    last = latest;
//                }
//            }
//        });
//        printSpeed.start();
//    }
//}
