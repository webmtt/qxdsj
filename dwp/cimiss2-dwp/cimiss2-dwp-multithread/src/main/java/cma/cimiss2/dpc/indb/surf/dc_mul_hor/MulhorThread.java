package cma.cimiss2.dpc.indb.surf.dc_mul_hor;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.Mulhor;
import cma.cimiss2.dpc.decoder.surf.DecodeMulhor;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.indb.surf.dc_mul_hor.Service.MulhorService;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;
import java.util.List;

public class MulhorThread implements Runnable{

    private ParseResult<Mulhor> parseResult = new ParseResult<Mulhor>(false) ;
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public String topic = null;

    public MulhorThread(String topic) {
        this.topic = topic;
    }

    /*@Override
    public void run() {
        try {
            if(topic != null && !("".equals(topic))){
                KafkaConsumer<String, Bytes> kafkaConsumer = KafkaConsumerFactory.getInstance().getConsumer(topic);
                while (true) {
                    ConsumerRecords<String, Bytes> records = kafkaConsumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, Bytes> record : records) {
                        byte[] b = record.value().get();
                        String filename = new String(record.key().getBytes());
                        StringBuffer loggerBuffer = new StringBuffer();
                        // 获取文件的编码
                        FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
                        String fileCode = FileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(b)];
                        processMsg(filename, new Date(), loggerBuffer, filename);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            String event_type = EIEventType.KAFKA_CONNECT_ERROR.getCode();
            EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
            if(ei == null) {
                infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST："+event_type);
            }else {
                if(StartConfig.isSendEi()) {
                    ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    ei.setKObject("cma.cimiss2.dpc.indb.upar.light_locat.MultSubThread");
                    ei.setKEvent("kafka连接异常");
                    RestfulInfo restfulInfo = new RestfulInfo();
                    restfulInfo.setType("SYSTEM.ALARM.EI");
                    restfulInfo.setName("数据解码入库EI告警信息");
                    restfulInfo.setMessage("数据解码入库EI告警信息");
                    restfulInfo.setFields(ei);
                    List<RestfulInfo> restfulInfos = new ArrayList<>();
                    restfulInfos.add(restfulInfo);
                    RestfulSendData.SendData(restfulInfos, SendType.EI);
                }
            }
            infoLogger.error("\n kafka Create Connection Pool Failed： " +e.getMessage());

            System.exit(0);
        }
    }*/
    @Override
    public void run() {
        try {
            if (topic != null && !("".equals(topic))) {

                String pathname = "D:\\解码文件\\C4600.xlsx";

                byte[] fileByte = null;

                File file = new File(pathname);

                // 获取文件的编码
                FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
                String fileCode = FileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];

                InputStream input = null;
                try {
                    input = new FileInputStream(file);
                    fileByte = new byte[input.available()];
                    input.read(fileByte);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] b = fileByte;
                String filename = file.getName();
                StringBuffer loggerBuffer = new StringBuffer();
                processMsg(file, new Date(), loggerBuffer, filename);
            }
        } catch (Exception e) {
            e.printStackTrace();

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
    private Action processMsg(File file, Date recv_time, StringBuffer loggerBuffer, String filename) {
        // 数据解码返回解码结果集
        DecodeMulhor decodeMulhor = new DecodeMulhor();
        parseResult = decodeMulhor.decode(file);
        // 判断是否解码成功
        if (parseResult.isSuccess()) {
            List<ReportError> reportErrors = parseResult.getError();
            for (int i = 0; i < reportErrors.size(); i++) {
                loggerBuffer.append(" ERROR REPORT : "+reportErrors.get(i).getSegment()+"\n"+"\t " + reportErrors.get(i).getMessage() + "\n");
            }
            // 获取解码实例对象集
            DataBaseAction action = null;
            loggerBuffer.append(topic);
            //入库
            if (StartConfig.getDatabaseType() == 1 || StartConfig.getDatabaseType() == 0) {
                action = MulhorService.processSuccessReport(parseResult, recv_time, topic, loggerBuffer, filename);
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
                infoLogger.error("\n read file error："+topic+"\n  error description:"+parseInfo.getDescription());
            }
            return Action.ACCEPT;
        }
    }
}
