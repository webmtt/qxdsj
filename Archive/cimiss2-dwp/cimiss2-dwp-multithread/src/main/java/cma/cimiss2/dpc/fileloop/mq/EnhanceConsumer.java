package cma.cimiss2.dpc.fileloop.mq;

import com.rabbitmq.client.Consumer;

public interface EnhanceConsumer extends Consumer {

    String getQueue() ;

    String getExchange();

}
