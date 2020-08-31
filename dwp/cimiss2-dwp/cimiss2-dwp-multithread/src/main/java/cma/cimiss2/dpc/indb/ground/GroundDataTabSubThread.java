package cma.cimiss2.dpc.indb.ground;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.radi.RadiDigChnMulTab;
import cma.cimiss2.dpc.decoder.fileDecode.A0File.decode.A0Decoding;
import cma.cimiss2.dpc.decoder.fileDecode.A1File.decoding.A1Decoding;
import cma.cimiss2.dpc.decoder.fileDecode.AFile.decode.ADecoding;
import cma.cimiss2.dpc.decoder.fileDecode.Interface.IFileDecoding;
import cma.cimiss2.dpc.decoder.fileDecode.Rfile.RnumberDecoding;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.*;
import cma.cimiss2.dpc.decoder.radi.DigChnMulTab;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.ground.service.GroundDataTabService;
import cma.cimiss2.dpc.indb.ground.service.InsertDB;
import com.rabbitmq.client.*;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A0文件日值入库MQ消息线程
 * @author yang.kq
 * @version 1.0
 * @date 2020/3/19 9:51
 */
public class GroundDataTabSubThread implements Runnable{
    //辐射数据结果集
    private ParseResult<RadiDigChnMulTab> parseResult = new ParseResult<RadiDigChnMulTab>(false) ;
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static String fileN = null;
    public static String day_cts_code = StartConfig.ctsCode("day_cts_code");
    public static String month_cts_code = StartConfig.ctsCode("month_cts_code");
    public static String hour_cts_code = StartConfig.ctsCode("hour_cts_code");
    public static String meadow_cts_code = StartConfig.ctsCode("meadow_cts_code");
    public static String sunCtsCode = StartConfig.ctsCode("sun_light_cts_code");
    private IFileDecoding decoding = null;
    public GroundDataTabSubThread(BlockingQueue<StatDi> diQueues) {
        GroundDataTabService.diQueues = diQueues;
//        if("A0".equals(FileType)){
//            decoding=new A0Decoding();
//        }else if("A".equals(FileType)){
//            decoding=new ADecoding();
//        }else if("A1".equals(FileType)){
//            decoding=new A1Decoding();
//        }else if("R".equals(FileType)){
//            decoding=new RnumberDecoding();
//        }
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
            // 声明消费对列，如果队列不存在，则自动创建
            String queueName=StartConfig.queueName("File_RabbitMQ_QueueName");
            rabbitMQConfig.setQueueName(queueName);
            // 创建RabbitMQ连接
            Connection connection = factory.newConnection();
            // 创建通道
            final Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);
            // 定义消费者
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    Date recv_time = new Date();
                    String message = new String(body, "UTF-8");
                    System.out.println(message);
                    StringBuffer loggerBuffer = new StringBuffer();
                    // 获取消息体
                    messageLogger.info(message + "\n");
                    // 消息处理
                    Action action  = processMsg(message, recv_time,loggerBuffer);

                    if(action == Action.ACCEPT){
                        // 消息消费确认机制
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }else if (action == Action.RETRY) {
                        channel.basicReject(envelope.getDeliveryTag(), false);
                    }

                    infoLogger.info(loggerBuffer.toString());
                }
            };

            channel.basicConsume(queueName, false, consumer);
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
                            ",端口号："+rabbitMQConfig.getPort()+"，对列："+rabbitMQConfig.getQueueName()+"]");
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
        ParseResult<String> decodingInfo = new ParseResult<>(false) ;
        File file = new File(message);
        String filename = file.getName();
        Map<String,Object> resultMap =null;
        // 数据解码返回解码结果集
        if(filename.toUpperCase().startsWith("R")) {//R辐射资料处理
            DigChnMulTab digChnMulTab = new DigChnMulTab();
//            parseResult = digChnMulTab.decode(file);
            if(parseResult.isSuccess()) {
                decoding=new RnumberDecoding();
                resultMap = decoding.assemblyData(file, sunCtsCode,day_cts_code, hour_cts_code, month_cts_code, meadow_cts_code, filename, parseResult.getData());
            }
        }else{//地面资料处理
            if(filename.toUpperCase().startsWith("A0")){
                decoding=new A0Decoding();
            }else if(filename.toUpperCase().startsWith("A")){
                decoding=new ADecoding();
            }
            resultMap =decoding.assemblyData(file,sunCtsCode,day_cts_code,hour_cts_code,month_cts_code,meadow_cts_code,filename,null);
        }

        List<DayValueTab> dayList=null;
        List<HourValueTab> hourList=null;
        List<SunLightValueTab> sunLightList=null;
        MonthValueTab mvt=null;
        List<MeadowValueTab> meadowList=null;
        List<RadiDigChnMulTab> rlist=null;
        FirstData sf=null;
        if(resultMap!=null) {
            decodingInfo = (ParseResult<String>) resultMap.get("decodingInfo");
        }
        // 判断是否解码成功
        if (decodingInfo.isSuccess()) {
            dayList = (List<DayValueTab>) resultMap.get("dayValue");
            hourList = (List<HourValueTab>) resultMap.get("hourValue");
            sunLightList = (List<SunLightValueTab>) resultMap.get("sunLight");
            mvt= (MonthValueTab) resultMap.get("monthValue");
            meadowList= (List<MeadowValueTab>) resultMap.get("meadowValue");
            rlist= (List<RadiDigChnMulTab>) resultMap.get("radiValue");
            sf= (FirstData) resultMap.get("dataTime");
            List<ReportError> reportErrors = decodingInfo.getError();
            for (int i = 0; i < reportErrors.size(); i++) {
                loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
            }
            // 获取解码实例对象集
            DataBaseAction action = null;
            loggerBuffer.append(day_cts_code+","+hour_cts_code+","+month_cts_code);
            //入库
            if (StartConfig.getDatabaseType() == 2) {
                System.out.println(filename+"文件开始入库");
                action = GroundDataTabService.processDaySuccessReport(sf,rlist,meadowList,mvt,dayList,hourList,sunLightList, recv_time, loggerBuffer, filename);
                System.out.println(filename+"入库完成！");
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
                infoLogger.error("\n read file error："+day_cts_code+","+hour_cts_code+","+month_cts_code+"\n  error description:"+parseInfo.getDescription());
            }
            return Action.ACCEPT;
        }
    }
}
