package cma.cimiss2.dpc.indb.surf.chn_mul_hor_yea_nat_tab;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;

import cma.cimiss2.dpc.decoder.fileDecode.common.bean.DayValueTab;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.HourValueTab;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.MonthValueTab;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.SunLightValueTab;
import cma.cimiss2.dpc.decoder.surf.ChnMulHorYeaNatTab;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.surf.chn_mul_hor_yea_nat_tab.service.ChnMulHorYeaNatTabService;
import com.rabbitmq.client.*;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ChnMulHorYeaNatTabSubThread implements Runnable{
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
//    public String topic = null;
//
//    public ChnMulHorYeaNatTabSubThread(String topic) {
//        this.topic = topic;
//    }
//    @Override
//    public void run() {
//        try {
//            if (topic != null && !("".equals(topic))) {
//                String pathname = "C:\\Users\\Administrator\\Desktop\\QXDSJ\\doc\\内蒙本地数据资料\\资料样例\\结构化数据\\1.1.1  数字化-A6文件（历史地面小时数据）\\A650639.A83";
//                byte[] fileByte = null;
//                File file = new File(pathname);
//                // 获取文件的编码
//                FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
//                String fileCode = FileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
//                InputStream input = null;
//                try {
//                    input = new FileInputStream(file);
//                    fileByte = new byte[input.available()];
//                    input.read(fileByte);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                byte[] b = fileByte;
//                String filename = file.getName();
//                StringBuffer loggerBuffer = new StringBuffer();
//                processMsg(file, new Date(), loggerBuffer, filename);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            System.exit(0);
//        }
//
//    }
public static String fileN = null;
    public String cts_code = null;
    public static String sod_code = StartConfig.sodCode("day_cts_codee");
    public ChnMulHorYeaNatTabSubThread(BlockingQueue<StatDi> diQueues) {
        ChnMulHorYeaNatTabService.diQueues = diQueues;
    }

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
            channel.queueDeclare(StartConfig.queueName("R023_RabbitMQ_queueName"), true, false, false, null);
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
                    cts_code = message.substring(0,message.indexOf(":"));
                    // 消息处理
                    Action action  = processMsg(message.substring(message.indexOf(":")+1,message.length()), recv_time,loggerBuffer);

                    if(action == Action.ACCEPT){
                        // 消息消费确认机制
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }else if (action == Action.RETRY) {
                        channel.basicReject(envelope.getDeliveryTag(), false);
                    }

                    infoLogger.info(loggerBuffer.toString());
                }
            };

            channel.basicConsume(StartConfig.queueName("R023_RabbitMQ_queueName"), false, consumer);
        } catch (Exception e) {
            String event_type = EIEventType.RABBIT_CONNECT_ERROR.getCode();
            EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
            if(ei == null) {
                infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST："+event_type);
            }else {
                if(StartConfig.isSendEi()) {
                    ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    ei.setKObject("cma.cimiss2.dpc.indb.upar.udua.UDUAMultSubThread");
                    ei.setKEvent("RabbitMQ连接异常");
                    ei.setKIndex("连接信息：[主机："+rabbitMQConfig.getHost()+",用户名："+rabbitMQConfig.getUser()+",密码："+rabbitMQConfig.getPassword()+
                            ",端口号："+rabbitMQConfig.getPort()+"，对列："+StartConfig.queueName("A009_RabbitMQ_queueName")+"]");
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
            infoLogger.error("\n rabbitMQ Create Connection Pool Failed： " +e.getMessage());

            System.exit(0);
        }
    }
    /**
     * @Title: processMsg
     * @Description: TODO(消息处理函数)
     * @param
     * @param
     * @return
     *         : Action 消息确认状态
     * @throws
     */
    @SuppressWarnings("unchecked")
    public Action processMsg(String message, Date recv_time, StringBuffer loggerBuffer) {
        File file = new File(message);
        String filename = message.substring(message.lastIndexOf("//")+2,message.length());
        ParseResult<String> decodingInfo = new ParseResult<>(false) ;
        // 数据解码返回解码结果集
        ChnMulHorYeaNatTab chnMulHorYeaNatTab= new ChnMulHorYeaNatTab();
        Map<String,Object> resultMap = chnMulHorYeaNatTab.decode(file,cts_code,filename);
        List<DayValueTab> dayList=null;
        List<HourValueTab> hourList=null;
        List<SunLightValueTab> sunLightList=null;
        List<MonthValueTab> monthList =null;
        decodingInfo= (ParseResult<String>) resultMap.get("decodingInfo");
        // 判断是否解码成功
        if (decodingInfo.isSuccess()) {
            dayList = (List<DayValueTab>) resultMap.get("dayValue");
            hourList = (List<HourValueTab>) resultMap.get("hourValue");
            sunLightList = (List<SunLightValueTab>) resultMap.get("sunLight");
            monthList = (List<MonthValueTab>) resultMap.get("monthValue");
            List<ReportError> reportErrors = decodingInfo.getError();
        // 判断是否解码成功
            for (int i = 0; i < reportErrors.size(); i++) {
                loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
            }
            // 获取解码实例对象集
            DataBaseAction action = null;
            loggerBuffer.append(cts_code);
            //入库
            if (StartConfig.getDatabaseType() == 2) {
                action = ChnMulHorYeaNatTabService.processSuccessReport(dayList,hourList,sunLightList,monthList, recv_time, cts_code, loggerBuffer, filename);
            } else {
                return Action.RETRY;
            }
            if (action == DataBaseAction.CONNECTION_ERROR) {
                return Action.RETRY;
            }
            else {
                return Action.ACCEPT;
            }

        } else {
            List<ReportError> reportErrors = decodingInfo.getError();
            for (int i = 0; i < reportErrors.size(); i++) {
                loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
            }

            ParseResult.ParseInfo parseInfo = decodingInfo.getParseInfo();
            if(parseInfo != null) {
                infoLogger.error("\n read file error："+cts_code+"\n  error description:"+parseInfo.getDescription());
            }
            return Action.ACCEPT;
        }
    }
}