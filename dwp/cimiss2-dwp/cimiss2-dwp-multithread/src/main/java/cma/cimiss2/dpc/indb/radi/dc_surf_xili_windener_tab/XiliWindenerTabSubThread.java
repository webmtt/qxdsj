package cma.cimiss2.dpc.indb.radi.dc_surf_xili_windener_tab;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.radi.*;
import cma.cimiss2.dpc.decoder.radi.XiliWindenerTab;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.radi.dc_surf_xili_windener_tab.service.XiliWindenerTabService;
import com.rabbitmq.client.*;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.cimiss2.dwp.util.ReadXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * @author ：YCK
 * @date ：Created in 2019/11/18 0018 11:13
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class XiliWindenerTabSubThread implements Runnable{
    private ParseResult<SurfXiliWindenerTab> parseResult = new ParseResult<SurfXiliWindenerTab>(false) ;
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

//    public XiliWindenerTabSubThread(String topic) {
//        this.topic = topic;
//    }
//    @Override
//    public void run() {
//        try {
//            if (topic != null && !("".equals(topic))) {
//                String pathname = "D://PBL_WE_FT_54102_190806.TXT";
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
    public static String cts_code = StartConfig.ctsCode("S001_cts_code");
    public static String sod_code = StartConfig.sodCode("S001_sod_code");
    public XiliWindenerTabSubThread(BlockingQueue<StatDi> diQueues) {
        XiliWindenerTabService.diQueues = diQueues;
    }

    @Override
    public void run() {
        // 读取rabbitmq 配置文件
        RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();
        try {
            List<Channel> listMse = new ArrayList();
            List listQue = new ArrayList();
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
            final Channel channels = connection.createChannel();
            final Channel channelss = connection.createChannel();
            // 声明消费对列，如果队列不存在，则自动创建
            channel.queueDeclare(StartConfig.queueName("S001_mdo_rabbitMQ_queueName"), true, false, false, null);
            // 定义消费者
            channels.queueDeclare(StartConfig.queueName("S001_mde_rabbitMQ_queueName"), true, false, false, null);
            channelss.queueDeclare(StartConfig.queueName("S001_tim_rabbitMQ_queueName"), true, false, false, null);
            // 定义消费者
            listMse.add(channel);
            listMse.add(channels);
            listMse.add(channelss);
            listQue.add("S001_mdo_rabbitMQ_queueName");
            listQue.add("S001_mde_rabbitMQ_queueName");
            listQue.add("S001_tim_rabbitMQ_queueName");
            for(int i = 0;i < listMse.size();i++){
                detalMes(listMse.get(i),listQue.get(i).toString());
            }

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
                            ",端口号："+rabbitMQConfig.getPort()+"，对列："+StartConfig.queueName("S001_rabbitMQ_queueName")+"]");
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
    private Action processMsg(String message, Date recv_time, StringBuffer loggerBuffer) {
        File file = new File(message);
        String filename = message.substring(message.lastIndexOf("//")+2,message.length());
        ParseResult<String> decodingInfo = new ParseResult<>(false) ;
        // 数据解码返回解码结果集
        XiliWindenerTab xiliWindenerTab = new XiliWindenerTab();
        Map<String,Object> resultMap = xiliWindenerTab.decode(file,filename);
        List<SurfXiliWindenerHfsTab> surfXiliWindenerHfsTabList = null;
        List<SurfXiliWindenerMdeTab> surfXiliWindenerMdeTabList = null;
        List<SurfXiliWindenerMdoTab> surfXiliWindenerMdoTabList = null;
        List<SurfXiliWindenerTimTab> surfXiliWindenerTimTabList = null;
        decodingInfo= (ParseResult<String>) resultMap.get("decodingInfo");
        // 判断是否解码成功
        if (decodingInfo.isSuccess()) {
            surfXiliWindenerHfsTabList = (List<SurfXiliWindenerHfsTab>) resultMap.get("surfXiliWindenerHfsTabList");
            surfXiliWindenerMdeTabList = (List<SurfXiliWindenerMdeTab>) resultMap.get("surfXiliWindenerMdeTabList");
            surfXiliWindenerMdoTabList = (List<SurfXiliWindenerMdoTab>) resultMap.get("surfXiliWindenerMdoTabList");
            surfXiliWindenerTimTabList = (List<SurfXiliWindenerTimTab>) resultMap.get("surfXiliWindenerTimTabList");
            List<ReportError> reportErrors = parseResult.getError();
            for (int i = 0; i < reportErrors.size(); i++) {
                loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
            }
            // 获取解码实例对象集
            DataBaseAction action = null;
            loggerBuffer.append(cts_code);
            //入库
            if (StartConfig.getDatabaseType() == 2) {
                action = XiliWindenerTabService.processSuccessReport(surfXiliWindenerHfsTabList,surfXiliWindenerMdeTabList,surfXiliWindenerMdoTabList,surfXiliWindenerTimTabList, recv_time, cts_code, loggerBuffer, filename);
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
            List<ReportError> reportErrors = parseResult.getError();
            for (int i = 0; i < reportErrors.size(); i++) {
                loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
            }

            ParseResult.ParseInfo parseInfo = parseResult.getParseInfo();
            if(parseInfo != null) {
                infoLogger.error("\n read file error："+cts_code+"\n  error description:"+parseInfo.getDescription());
            }
            return Action.ACCEPT;
        }
    }
    private void detalMes(Channel channel,String QueName){
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                Date recv_time = new Date();
                String message = new String(body, "UTF-8");
                System.out.println(message.substring(message.indexOf(":")+1,message.length()));
                StringBuffer loggerBuffer = new StringBuffer();
                Action action  = processMsg(message.substring(message.indexOf(":")+1,message.length()), recv_time,loggerBuffer);
//                Action action = null;
//                List list = null;
//                try {
//                    list = ReadXml.readStringXml(message);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                for (int i = 0; i < list.size(); i++) {
//                    System.out.println(list.get(i));
//                    action = processMsg(list.get(i).toString(), recv_time,loggerBuffer);
//                }
                // 获取消息体
                messageLogger.info(message + "\n");
                // 消息处理
                if(action == Action.ACCEPT){
                    // 消息消费确认机制
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }else if (action == Action.RETRY) {
                    channel.basicReject(envelope.getDeliveryTag(), false);
                }

                infoLogger.info(loggerBuffer.toString());
            }
        };
        try {
            channel.basicConsume(StartConfig.queueName(QueName), false, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
