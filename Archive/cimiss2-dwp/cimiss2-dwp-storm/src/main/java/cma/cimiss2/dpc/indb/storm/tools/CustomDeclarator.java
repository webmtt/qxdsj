package cma.cimiss2.dpc.indb.storm.tools;

import com.rabbitmq.client.Channel;
import io.latent.storm.rabbitmq.Declarator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Aouthor: xzh
 * @create: 2017-11-21 19:41
 */

public class CustomDeclarator implements Declarator {

    private final String exchange;
    private final String queue;
    private final String routingKey;

    public CustomDeclarator(String exchange, String queue) {
        this(exchange, queue, "");
    }

    public CustomDeclarator(String exchange, String queue, String routingKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
    }

    @Override
    public void execute(Channel channel) {
        // you're given Decode RabbitMQ Channel so you're free to wire up your exchange/queue bindings as you see fit
        try {
            Map<String, Object> args = new HashMap<String, Object>();
            channel.queueDeclare(queue, true, false, false, args);
            channel.exchangeDeclare(exchange, "topic", true);
            channel.queueBind(queue, exchange, routingKey);
        } catch (IOException e) {
            throw new RuntimeException("Error executing rabbitmq declarations.", e);
        }
    }
}

