package cma.cimiss2.dpc.indb.detection.pen;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static cma.cimiss2.dpc.indb.detection.pen.FileUtils.traverseFolder;

/**
 * @Description:
 * @Aouthor: xzh
 * @create: 2018-05-22 14:20
 */
public class FilePolling implements Runnable {
        public static BlockingQueue<String> files = new LinkedBlockingQueue<String>();
//    public static ConcurrentLinkedDeque<String> files = new ConcurrentLinkedDeque<String>();
    String src;
    String event;

    public FilePolling(String src, String event) {
        this.src = src;
        this.event = event;
    }

    @Override
    public void run() {
        traverseFolder(src, event, FilePolling.files);

    }
}
