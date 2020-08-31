package cma.cimiss2.dpc.indb.common.mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class EndPoint {

    protected Channel channel;

    protected Connection connection;

    protected String queueName;

    public EndPoint(String queueName) throws  TimeoutException, IOException
    {
        this.queueName = queueName;

        // Create a connection factory
        ConnectionFactory factory = new ConnectionFactory();

        // 与RabbitMQ Server建立连接  
        // 连接到的broker在本机localhost上
        factory.setHost("10.40.120.68");
        factory.setUsername("test");
        factory.setPassword("test");
        factory.setPort(5672);
        // 网络异常自动连接恢复
        factory.setAutomaticRecoveryEnabled(true);
        // 每10秒尝试重试连接一次
        factory.setNetworkRecoveryInterval(10000);
        // getting a connection
        connection = factory.newConnection();

        // creating a channel
        channel = connection.createChannel();

        // declaring a queue for this channel. If queue does not exist,
        // it will be created on the server.
        // queueDeclare的参数：queue 队列名；durable true为持久化；exclusive 是否排外，true为队列只可以在本次的连接中被访问，
        // autoDelete true为connection断开队列自动删除；arguments 用于拓展参数
        channel.queueDeclare(queueName, true, false, false, null);
    }

    /**
     * 关闭channel和connection。并非必须，因为隐含是自动调用的。
     * @throws IOException
     * @throws TimeoutException 
     */
    public void close() throws IOException, TimeoutException
    {
        this.channel.close();
        this.connection.close();
    }
}