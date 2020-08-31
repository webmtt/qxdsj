package cma.cimiss2.dpc.fileloop.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;


public class CheckConsumerDownThread  implements  Runnable {

    private EnhanceConsumer consumer;

    private final static Logger logger = LoggerFactory.getLogger(CheckConsumerDownThread.class);

    private Channel channel;

    public CheckConsumerDownThread(EnhanceConsumer consumer, Channel channel) {
        this.consumer = consumer;
        this.channel = channel;
    }
    @Override
    public void run() {
        while(true) {
            if (!channel.isOpen()) {
                logger.info("开始重新连接MQ服务");
                MQUtils.receiveByDeclare(consumer);
                logger.info("MQ服务连接恢复");

            } else {
                try {
                    Thread.sleep(30 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
