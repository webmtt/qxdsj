package cma.cimiss2.dpc.indb.sevp.dc_sevp_htmhbmsp;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import com.rabbitmq.client.*;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.ConfigurationManager;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ywj
 * @title: HTMHBMSPSubThread
 * @projectName demo2
 * @description: TODO
 * @date 2019/7/11 17:32
 */
public class HTMHBMSPSubThread implements Runnable {
    public static String indexPropertiesFile = ConfigurationManager.getJarSuperPath()+"config/index.txt";
//   public static String indexPropertiesFile = "D:\\QXDSJ\\code\\dwp\\cimiss2-dwp\\cimiss2-dwp-multithread\\src\\main\\java\\cma\\cimiss2\\dpc\\indb\\sevp\\dc_sevp_htmhbmsp\\indexs.txt";
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public String topic = null;
    public static java.sql.Connection sqlconn = null;

    public HTMHBMSPSubThread() {
        // 加载索引库策略的配置文件
        if (!IndexConf.ReadConfig(indexPropertiesFile)) {
            System.exit(0);
        }
    }

    //    @Override
//    public void run() {
//
//        //接受kafka消息
//        String topic = StartConfig.ctsCode();
//        KafkaConsumer<String, Bytes> kafkaConsumer = KafkaConsumerFactory.getInstance().getConsumer(topic);
//
//        //kafka轮询接收消息
//        while (true) {
//            ConsumerRecords<String, Bytes> records = kafkaConsumer.poll(Duration.ofMillis(100));
//            for (ConsumerRecord<String, Bytes> record : records) {
//                try {
//                    Date recv_time = new Date();
////                    String str = "";
////                    try {
////                        str = new String(record.value().get(), "utf-8");
////                    } catch (UnsupportedEncodingException e) {
////                        e.printStackTrace();
////                    }
////                    JSONObject json = JSON.parseObject(str);
////					String js = new String(record.key().getBytes());
////					Map<String, Object> map = JSONObject.parseObject(js);
////                    String filePath = json.getString("NasPath");
////                    filePath = filePath.replace("\\", "/");
////                    String filename = json.getString("FileName");
////                    String sodCode = json.getString("TYPE");
//                    KafkaMessageUtil.parse(new String(record.value().get(), "utf-8"));
//                    String filePath = KafkaMessageUtil.getFilePathAndFileName();
//                    String filename = KafkaMessageUtil.getFileName();
//                    String sodCode = KafkaMessageUtil.getTYPE();
////                    processMsg(filePath +"/" + filename, recv_time, filename, sodCode);
//                    processMsg(filePath, recv_time, filename, sodCode);
//                    kafkaConsumer.commitAsync();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    public static String fileN = null;
    public  String cts_code = null;

    @Override
    public void run() {
        // 读取rabbitmq 配置文件
        RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();
        try {
            ConnectionFactory factory = new ConnectionFactory();
            // 获取rabbitMQ连接信息
            factory.setHost(rabbitMQConfig.getHost());
            factory.setUsername(rabbitMQConfig.getUser());
            factory.setPassword(rabbitMQConfig.getPassword());
            factory.setPort(rabbitMQConfig.getPort());
            // 创建RabbitMQ连接
            Connection connection = factory.newConnection();
            // 创建通道
            final Channel channel = connection.createChannel();
            // 声明消费对列，如果队列不存在，则自动创建
            channel.queueDeclare(rabbitMQConfig.getQueueName(), true, false, false, null);
            // 定义消费者
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    Date recv_time = new Date();
                    String message = new String(body, "UTF-8");
                    System.out.println(message.substring(message.indexOf(":")+1,message.length()));
                    StringBuffer loggerBuffer = new StringBuffer();
                    // 获取消息体
                    messageLogger.info(message + "\n");
                    // 消息处理
                   cts_code =  message.substring(0,message.indexOf(":"));
                   String filename = message.substring(message.lastIndexOf("\\"),message.length());
                    Action action = processMsg(message.substring(message.indexOf(":")+1,message.length()), recv_time, filename, cts_code);

                    if (action == Action.ACCEPT) {
                        // 消息消费确认机制
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } else if (action == Action.RETRY) {
                        channel.basicReject(envelope.getDeliveryTag(), false);
                    }
                    infoLogger.info(loggerBuffer.toString());
                }
            };

            channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
        } catch (Exception e) {
            String event_type = EIEventType.RABBIT_CONNECT_ERROR.getCode();
            EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
            if (ei == null) {
                infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST：" + event_type);
            } else {
                if (StartConfig.isSendEi()) {
                    ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    ei.setKObject("cma.cimiss2.dpc.indb.upar.udua.UDUAMultSubThread");
                    ei.setKEvent("RabbitMQ连接异常");
                    ei.setKIndex("连接信息：[主机：" + rabbitMQConfig.getHost() + ",用户名：" + rabbitMQConfig.getUser() + ",密码：" + rabbitMQConfig.getPassword() +
                            ",端口号：" + rabbitMQConfig.getPort() + "，对列：" + rabbitMQConfig.getQueueName() + "]");
                    RestfulInfo restfulInfo = new RestfulInfo();
                    restfulInfo.setType("SYSTEM.ALARM.EI ");
                    restfulInfo.setName("数据解码入库EI告警信息");
                    restfulInfo.setMessage("数据解码入库EI告警信息");
                    restfulInfo.setFields(ei);
                    List<RestfulInfo> restfulInfos = new ArrayList<>();
                    restfulInfos.add(restfulInfo);
                    RestfulSendData.SendData(restfulInfos, SendType.EI);
                }
            }
            infoLogger.error("\n rabbitMQ Create Connection Pool Failed： " + e.getMessage());

            System.exit(0);
        }
    }

    /**
     * @param message   消息体内容
     * @param recv_time 消息接收时间
     * @return : Action 消息确认状态
     * @throws
     * @Title: processMsg
     * @Description: TODO(消息处理函数)
     */
    /**
     * @param filePath  文件路径
     * @param recv_time 消息接收时间
     * @param filename  文件名称
     * @param sodCode
     * @return
     */
    private Action processMsg(String filePath, Date recv_time, String filename, String sodCode) {
        try {
            //处理开始时间
            String strStartTime = TimeUtil.getSysTime();
            File file = new File(filePath);
            if (!(file != null && file.exists() && file.isFile())) {
                infoLogger.error("\n 文件不存在" + filePath);
                return Action.ACCEPT;//需确认
            }
            //获取文件属性
            BasicFileAttributes fileAttr = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class);
            String strRecvTime = TimeUtil.date2String(new Date(fileAttr.lastModifiedTime().toMillis()), "yyyy-MM-dd HH:mm:ss");
            String filesize = String.valueOf(fileAttr.size());

            String splitRegex = "- . _";
//            String splitRegex = ". _";
            boolean bIndb = true; //是否入库
            boolean bMoveFileSuc = true;
            // 入索引库
            if (bMoveFileSuc) {
                if (bIndb) {
                    String strFileName = new File(filePath).getName();
                    String[] items = StringSplit.split(strFileName, splitRegex);
                    String strSql = "";
                    StringBuffer strDataTime = new StringBuffer();

                    String dataTableName = sodCode;
                    String strTableName = StartConfig.valueTable("value_table_name");
                    String strCtsType = sodCode;
                    strSql = ServiceSql.genIndexOtherSql(strTableName, strCtsType, sodCode, items,
                            strStartTime, file.getName(), filesize, strRecvTime, filePath, strDataTime);

                    int iRet = mysql_run_sql(strSql);
                    if (iRet == -1) {
                        infoLogger.error("\n 插入索引表失败:" + strSql + "\n");
                        return Action.RETRY;
                    } else if (iRet == -2) {
                        infoLogger.error("\n 插入索引表失败:" + strSql + "\n");
                        return Action.REJECT;
                    }
                } else { //不入库
                    return Action.ACCEPT;
                }
            } else { //移动文件失败
                return Action.RETRY;
            }
            return Action.ACCEPT;
        } catch (IOException e) {
            infoLogger.error(e.getMessage());
            return Action.REJECT;
        } finally {
            //插入DI信息
        }
    }


    private int mysql_run_sql(String strSql) {
        try {
            /*Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
            String strdbType = config.getProperty("dataBaseType");
            if (strdbType.equals("2")) { //虚谷数据库
                //使用mysql数据库储存索引
                sqlconn = ConnectionPoolFactory.getInstance().getConnection("fileindex");
            } else { //阿里云DRDS数据库、万里开源数据库
                sqlconn = ConnectionPoolFactory.getInstance().getConnection("cimiss");
            }*/
            sqlconn = ConnectionPoolFactory.getInstance().getConnection("xugu");
            if (sqlconn == null) {
                return -1;
            }
            Statement stmt = null;
            stmt = sqlconn.createStatement();
            stmt.execute(strSql);
//            sqlconn.commit();
            stmt.close();
        } catch (SQLException e) {
            infoLogger.error("\n 数据库连接异常" + "\n " + strSql + "\n " + e.getMessage());
            System.out.println(e);
            return -2;
        } finally {
            if (sqlconn != null) {
                try {
                    sqlconn.close();
                } catch (SQLException e) {
                    infoLogger.error("\n 关闭数据库连接异常:" + e.getMessage());
                    return -2;
                }
            }
        }
        return 0;
    }
}
