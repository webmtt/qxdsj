package cma.cimiss2.dpc.indb.storm.tools.ots;

import com.aliyun.openservices.ots.internal.OTSClient;
import com.aliyun.openservices.ots.internal.model.ListStreamRequest;
import com.aliyun.openservices.ots.internal.model.ListStreamResult;
import com.aliyun.openservices.ots.internal.model.StreamRecord;
import com.aliyun.openservices.ots.internal.streamclient.*;
import com.aliyun.openservices.ots.internal.streamclient.lease.LeaseManager;
import com.aliyun.openservices.ots.internal.streamclient.lease.ShardLease;
import com.aliyun.openservices.ots.internal.streamclient.lease.ShardLeaseSerializer;
import com.aliyun.openservices.ots.internal.streamclient.lease.interfaces.ILeaseManager;
import com.aliyun.openservices.ots.internal.streamclient.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class DataConsumer {
    String dataTable = Config.dataTable;
    String statusTable = Config.streamStatusTable;

    AtomicLong totalCount = new AtomicLong(0);

    class RecordProcessor implements IRecordProcessor {
        private String workerId;
        private
        int count = 0;

        public RecordProcessor(String workerId) {
            this.workerId = workerId;
        }

        public void initialize(InitializationInput initializationInput) {
//            System.out.println("Record processor '" + workerId + "' started." + initializationInput.getShardInfo());
        }

        public void processRecords(ProcessRecordsInput processRecordsInput) {
                for (StreamRecord record : processRecordsInput.getRecords()) {
                    // process records
                    count++;
                    totalCount.incrementAndGet();
//                    System.out.println(record);
                }

            try {
                processRecordsInput.getCheckpointer().checkpoint();
            } catch (ShutdownException e) {
                e.printStackTrace();
            } catch (StreamClientException e) {
                e.printStackTrace();
            } catch (DependencyException e) {
                e.printStackTrace();
            }
        }

        public void shutdown(ShutdownInput shutdownInput) {
        }
    }

    class RecordProcessorFactory implements IRecordProcessorFactory {

        private final String workerIdentifier;
        private final OTSClient client;

        public RecordProcessorFactory(String workerIdentifier, OTSClient client) {
            this.workerIdentifier = workerIdentifier;
            this.client = client;
        }

        public IRecordProcessor createProcessor() {
            return new DataConsumer.RecordProcessor(workerIdentifier);
        }
    }

    class RetryStrategy implements IRetryStrategy {

        public boolean shouldRetry(RetryableAction actionName, Exception ex, int retries) {
            return true;
        }

        public long getBackoffTimeMillis(RetryableAction actionName, Exception ex, int retries) {
            return 100;
        }
    }

    public Worker newWorker(OTSClient client, String dataTableName, String statusTableName, String streamId, String workerId) {
        StreamConfig streamConfig = new StreamConfig();
        streamConfig.setOTSClient(client);
        streamConfig.setDataTableName(dataTableName);
        streamConfig.setStatusTableName(statusTableName);

        ShardLeaseSerializer leaseSerializer = new ShardLeaseSerializer(statusTableName, streamId);
        ILeaseManager<ShardLease> leaseManager = new LeaseManager<ShardLease>(client, streamConfig.getStatusTableName(), leaseSerializer, new DataConsumer.RetryStrategy(), 100);
        Worker worker = new Worker(workerId, new ClientConfig(), streamConfig, new DataConsumer.RecordProcessorFactory(workerId, client), Executors.newCachedThreadPool(), leaseManager);
        return worker;
    }

    public void start(String workerPrefix, int workerCount) {
        OTSClient client = new OTSClient(Config.endpoint, Config.accessId, Config.accessKey, Config.instance);

        try {
            ListStreamRequest request = new ListStreamRequest(dataTable);
            ListStreamResult response = client.listStream(request);
            String streamId = response.getStreams().get(0).getStreamId();


            List<Thread> threads = new ArrayList<Thread>();
            for (int i = 0; i < workerCount; i++) {
                Thread thread = new Thread(newWorker(client, dataTable, statusTable, streamId, workerPrefix + i));
                threads.add(thread);
            }

            for (Thread thread : threads) {
                thread.start();
            }

            Thread printSpeed = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        long last = totalCount.get();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }

                        long latest = totalCount.get();
                        System.out.println("Speed: " + (latest - last) + " records/s.");
                        last = latest;
                    }
                }
            });
            printSpeed.start();

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            client.shutdown();
        }

    }

    public static void main(String[] args) throws Exception {
        int workerCount = 5;

        if (args.length > 0) {
            Config.endpoint = args[0];
            workerCount = Integer.parseInt(args[1]);
        }

        DataConsumer consumer = new DataConsumer();
        consumer.start("worker_" + System.currentTimeMillis(), workerCount);
    }
}
