package cma.cimiss2.dpc.fileloop.mq;

import java.io.UnsupportedEncodingException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;

public class BaseConsumer extends UltimateConsumer{


    BaseConsumer(String queue) {
        super(queue);
    }

    BaseConsumer(String topic,String exchange) {
        super(topic,exchange);
    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] body)  {
        try {
            String message = new String(body,"UTF-8");
            System.out.println(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
