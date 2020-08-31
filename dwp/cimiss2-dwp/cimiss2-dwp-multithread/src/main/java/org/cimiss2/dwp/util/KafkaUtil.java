package org.cimiss2.dwp.util;/**
 * Created by zzj on 2019/7/9.
 */

import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.utils.Bytes;
import org.cimiss2.dwp.tools.KafkaConsumerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Date;

/**
 * @program: cimiss2-dwp
 * @description:
 * @author: zzj
 * @create: 2019-07-09 09:43
 **/
public class KafkaUtil implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(KafkaUtil.class);
    protected String[] topics;
    @Override
    public void run() {
        CassandraUtil.getSession();
        SqlConnUtil.getFileIndexdruidDataSource();
        SqlConnUtil.getRdbDataSource();
        KafkaConsumer<String, Bytes> kafkaConsumer = KafkaConsumerFactory.getInstance().getConsumer(topics);
        while (true) {
            ConsumerRecords<String, Bytes> records = kafkaConsumer.poll(Duration.ofMillis(100));

            boolean readDone = false;
            for (ConsumerRecord<String, Bytes> record : records) {

                try {
                    String buff= new String(record.value().get(), "UTF-8");
                    JSONObject jsonObject = JSON.parseObject(buff);
                    String path = (String) jsonObject.get("data");
                    String topic = record.topic();
                    Date eventTime = new Date();
                    Action action = processMsg(path,topic, eventTime);
                    if (action == Action.ACCEPT) {
                        readDone = true;
                    }
                } catch (Exception e) {
                   logger.error(ExceptionUtil.getException(e));
                }

            }
            if (readDone == true) {
                kafkaConsumer.commitAsync();
            }
        }
    }

    protected Action processMsg(String path, String ddateId, Date eventTime) {
        return Action.ACCEPT;

    }
}

