package cma.cimiss2.dpc.indb.detection.day;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.core.tools.*;
import cma.cimiss2.dpc.indb.detection.DIEISenderThread;

import com.rabbitmq.client.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cma.cimiss2.dpc.indb.detection.day.FileUtils.remove;

/**
 * @Description: 多线程基类
 * @Aouthor: xzh
 * @create: 2018-01-25 12:04
 */
public abstract class BaseThread<T> implements Runnable {
    public Action action = Action.ACCEPT;
    ExecutorService EIDIexecutor = Executors.newFixedThreadPool(2);
    List<RestfulInfo> diInfo = new ArrayList<>();
    List<RestfulInfo> eiInfo = new ArrayList<>();
    long eiStartTime = System.currentTimeMillis();
    long eiEndTime = System.currentTimeMillis();
    long diStartTime = System.currentTimeMillis();
    long diEndTime = System.currentTimeMillis();
    //    protected AsyncClient client = OTS.getClient();
    protected int repCorrectNum;
    protected int repErrorNum;
    protected int dataCorrectNum;
    protected int dataErrorNum;
    protected String dataSource = ConfigurationManager.getString("dataBaseType");
    protected String dataTable = ConfigurationManager.getString("db-table");
    protected String dataDayTable = ConfigurationManager.getString("V14032-db-table");
    protected String repTable = ConfigurationManager.getString("report-table");
    protected String host = ConfigurationManager.getString("RabbitMQ.host");
    protected String name = ConfigurationManager.getString("RabbitMQ.user");
    protected String password = ConfigurationManager.getString("RabbitMQ.passWord");
    protected int port = ConfigurationManager.getInteger("RabbitMQ.port");
    protected String queue = ConfigurationManager.getString("RabbitMQ.queueName");
    protected List<Map<String, Object>> reports;
    protected ParseResult<T> result;
    protected List<ReportError> errorList;
    protected File file;
//    protected  Channel channel;
//    protected long tag;

    public BaseThread() {

    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ParseResult<T> getResult() {
        return result;
    }

    public void setResult(ParseResult<T> result) {
        this.result = result;
    }

    public List<ReportError> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<ReportError> errorList) {
        this.errorList = errorList;
    }

    @Override
    public void run() {
        if ("0".equals(ConfigurationManager.getString("fileloop"))) {
            mqSource();
        } else if ("1".equals(ConfigurationManager.getString("fileloop"))) {
            fileSource();
        }
    }

    /**
     * 消息来源为rabbitmq
     */
    private void mqSource() {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setUsername(name);
            factory.setPassword(password);
            factory.setPort(port);
            factory.setAutomaticRecoveryEnabled(true);
            Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();
            channel.queueDeclare(queue, true, false, false, null);
            Consumer consumer = new DefaultConsumer(channel) {
                long tag;

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    tag = envelope.getDeliveryTag();
                    String message = new String(body, "UTF-8");
                    StringBuffer buffer = new StringBuffer();
                    //List<RestfulInfo> diInfo = new ArrayList<>();
                    //List<RestfulInfo> eiInfo = new ArrayList<>();
                    buffer.append(message.split(":")[1] + "\n");
                    try {
                        formate(message, buffer, diInfo, eiInfo);
                        if (result.isSuccess()) {
                            write(message, buffer, diInfo, eiInfo);
                            for (int i = 0; i < errorList.size(); i++) {
                                ReportError reportError = errorList.get(i);
                                buffer.append("解码失败:" + message + "\n" +
                                        "错误原因:" + reportError.getMessage() + "\n" +
                                        "行号:" + reportError.getPositionx() + "\n" +
                                        "错误片段摘录:" + reportError.getSegment() + "\n");
                                HashMap<String, String> map = new HashMap<>();
                                map.put("message", reportError.getMessage());
                                map.put("path", "");
                                List<RestfulInfo> info = DIEISender.makeEI(map);
                                eiInfo.addAll(info);
                            }
                            action = Action.ACCEPT;
                        } else {
                            //解码完全错误
                            if (errorList != null && errorList.size() > 0) {
                                for (int i = 0; i < errorList.size(); i++) {
                                    ReportError reportError = errorList.get(i);
                                    reportError.getMessage();
                                    buffer.append("解码失败:" + message + "\n" +
                                            "错误原因:" + reportError.getMessage() + "\n" +
                                            "行号:" + reportError.getPositionx() + "\n" +
                                            "错误片段摘录:" + reportError.getSegment() + "\n");
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("message", reportError.getMessage());
                                    map.put("path", "");
                                    List<RestfulInfo> info = DIEISender.makeEI(map);
                                    eiInfo.addAll(info);
                                }
                                action = Action.REJECT;
                            } else {
                                int code = result.getParseInfo().getCode();
                                switch (code) {
                                    case 1:
                                        //非法格式
                                        buffer.append("解码失败:" + message + "\n" +
                                                "错误原因:非法格式" + "\n");
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("message", "非法格式");
                                        map.put("path", "");
                                        List<RestfulInfo> info = DIEISender.makeEI(map);
                                        eiInfo.addAll(info);
                                        return;
                                    case 2:
                                        //文件不存在，空文件
                                        buffer.append("解码失败:" + message + "\n" +
                                                "错误原因:文件不存在或为空文件" + "\n");
//                                DIEISender.EI("文件不存在或为空文件", path);
                                        HashMap<String, String> map1 = new HashMap<>();
                                        map1.put("message", "文件不存在或为空文件");
                                        map1.put("path", "");
                                        List<RestfulInfo> info2 = DIEISender.makeEI(map1);
                                        eiInfo.addAll(info2);
                                        return;
                                }
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        action = Action.REJECT;
                        buffer.append("解码入库失败:" + message + "\n" +
                                "错误原因:" + e + "\n");
                    } finally {

                        if (action == Action.ACCEPT) {
                            channel.basicAck(tag, false);
                            finallyAccept(buffer);///
                            diEndTime = System.currentTimeMillis();
                            if (DIEISender.GLOBAL_DI_OPTION && DIEISender.LOCAL_DI_OPTION) {
                            	if(diInfo.size() >= 200 || (diEndTime - diStartTime)/1000 >= 3){
                            		EIDIexecutor.execute(new DIEISenderThread(diInfo, 0));
                            		diStartTime = System.currentTimeMillis();
                            		diInfo.clear();
                            	}
                            }
                            eiEndTime = System.currentTimeMillis();
                            if (DIEISender.GLOBAL_EI_OPTION && DIEISender.LOCAL_EI_OPTION) {
                            	if(eiInfo.size() >= 200 || (eiEndTime - eiStartTime)/1000 >= 3){
                            		EIDIexecutor.execute(new DIEISenderThread(eiInfo, 1));
                            		 eiStartTime = System.currentTimeMillis(); 
                            		 eiInfo.clear();
                            	}
                            }
                        } else if (action == Action.RETRY) {
                            try {
                                System.out.println("进入final====RETRY......");//不需要设置
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                channel.basicNack(tag, false, false);
                                System.out.println("进入final====REJECT......");
                                buffer.append("消息错误 REJECT" + "\n");
                                System.out.println(buffer.toString());
                                finallyReject(buffer);
                                diEndTime = System.currentTimeMillis();
                                if (DIEISender.GLOBAL_DI_OPTION && DIEISender.LOCAL_DI_OPTION) {
                                	if(diInfo.size() >= 200 || (diEndTime - diStartTime)/1000 >= 3){
                                		EIDIexecutor.execute(new DIEISenderThread(diInfo, 0));
                                		diStartTime = System.currentTimeMillis();
                                		diInfo.clear();
                                	}
                                }
                                eiEndTime = System.currentTimeMillis();
                                if (DIEISender.GLOBAL_EI_OPTION && DIEISender.LOCAL_EI_OPTION) {
                                	if(eiInfo.size() >= 200 || (eiEndTime - eiStartTime)/1000 >= 3){
                                		EIDIexecutor.execute(new DIEISenderThread(eiInfo, 1));
                                		 eiStartTime = System.currentTimeMillis(); 
                                		 eiInfo.clear();
                                	}
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            channel.basicConsume(queue, false, consumer);
        } catch (Exception e) {
            rabbitConnErrot(e);
        }
    }

    /**
     * 消息来源为文件轮询
     */
    private void fileSource() {
        while (!FilePolling.files.isEmpty()) {
            String take = null;
            try {
                take = FilePolling.files.take();
                LogUtil.info("messageInfo", take);
                execute(take);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void execute(String message) throws IOException {
        StringBuffer buffer = new StringBuffer();
        //List<RestfulInfo> diInfo = new ArrayList<>();
        //List<RestfulInfo> eiInfo = new ArrayList<>();
        buffer.append(message.split(":")[1] + "\n");
        try {
            formate(message, buffer, diInfo, eiInfo);
            if (result.isSuccess()) {
                write(message, buffer, diInfo, eiInfo);
                for (int i = 0; i < errorList.size(); i++) {
                    ReportError reportError = errorList.get(i);
                    buffer.append("解码失败:" + message + "\n" +
                            "错误原因:" + reportError.getMessage() + "\n" +
                            "行号:" + reportError.getPositionx() + "\n" +
                            "错误片段摘录:" + reportError.getSegment() + "\n");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("message", reportError.getMessage());
                    map.put("path", "");
                    List<RestfulInfo> info = DIEISender.makeEI(map);
                    eiInfo.addAll(info);
                }
                action = Action.ACCEPT;
            } else {
                //解码完全错误
                if (errorList != null && errorList.size() > 0) {
                    for (int i = 0; i < errorList.size(); i++) {
                        ReportError reportError = errorList.get(i);
                        reportError.getMessage();
                        buffer.append("解码失败:" + message + "\n" +
                                "错误原因:" + reportError.getMessage() + "\n" +
                                "行号:" + reportError.getPositionx() + "\n" +
                                "错误片段摘录:" + reportError.getSegment() + "\n");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("message", reportError.getMessage());
                        map.put("path", "");
                        List<RestfulInfo> info = DIEISender.makeEI(map);
                        eiInfo.addAll(info);
                    }
                    action = Action.REJECT;
                } else {
                    int code = result.getParseInfo().getCode();
                    switch (code) {
                        case 1:
                            //非法格式
                            buffer.append("解码失败:" + message + "\n" +
                                    "错误原因:非法格式" + "\n");
                            HashMap<String, String> map = new HashMap<>();
                            map.put("message", "非法格式");
                            map.put("path", "");
                            List<RestfulInfo> info = DIEISender.makeEI(map);
                            eiInfo.addAll(info);
                            return;
                        case 2:
                            //文件不存在，空文件
                            buffer.append("解码失败:" + message + "\n" +
                                    "错误原因:文件不存在或为空文件" + "\n");
//                                DIEISender.EI("文件不存在或为空文件", path);
                            HashMap<String, String> map1 = new HashMap<>();
                            map1.put("message", "文件不存在或为空文件");
                            map1.put("path", "");
                            List<RestfulInfo> info2 = DIEISender.makeEI(map1);
                            eiInfo.addAll(info2);
                            return;
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            action = Action.REJECT;
            buffer.append("解码入库失败:" + message + "\n" +
                    "错误原因:" + e + "\n");
            e.printStackTrace();
        } finally {

            if (action == Action.ACCEPT) {
                finallyAccept(buffer);///
                diEndTime = System.currentTimeMillis();
                if (DIEISender.GLOBAL_DI_OPTION && DIEISender.LOCAL_DI_OPTION) {
                	if(diInfo.size() >= 200 || (diEndTime - diStartTime)/1000 >= 3){
                		EIDIexecutor.execute(new DIEISenderThread(diInfo, 0));
                		diStartTime = System.currentTimeMillis();
                		diInfo.clear();
                	}
                }
                eiEndTime = System.currentTimeMillis();
                if (DIEISender.GLOBAL_EI_OPTION && DIEISender.LOCAL_EI_OPTION) {
                	if(eiInfo.size() >= 200 || (eiEndTime - eiStartTime)/1000 >= 3){
                		EIDIexecutor.execute(new DIEISenderThread(eiInfo, 1));
                		 eiStartTime = System.currentTimeMillis();
                		 eiInfo.clear();
                	}
                }
            } else if (action == Action.RETRY) {
                try {
                    System.out.println("进入final====RETRY......");//不需要设置
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    System.out.println("进入final====REJECT......");
                    buffer.append("消息错误 REJECT" + "\n");
                    System.out.println(buffer.toString());
                    finallyReject(buffer);
                    diEndTime = System.currentTimeMillis();
                    if (DIEISender.GLOBAL_DI_OPTION && DIEISender.LOCAL_DI_OPTION) {
                    	if(diInfo.size() >= 200 || (diEndTime - diStartTime)/1000 >= 3){
                    		EIDIexecutor.execute(new DIEISenderThread(diInfo, 0));
                    		diStartTime = System.currentTimeMillis(); 
                    		diInfo.clear();
                    	}
                    }
                    eiEndTime = System.currentTimeMillis();
                    if (DIEISender.GLOBAL_EI_OPTION && DIEISender.LOCAL_EI_OPTION) {
                    	if(eiInfo.size() >= 200 || (eiEndTime - eiStartTime)/1000 >= 3){
                    		EIDIexecutor.execute(new DIEISenderThread(eiInfo, 1));
                    		 eiStartTime = System.currentTimeMillis();
                    		 eiInfo.clear();
                    	}
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //入库完成 开始移动文件
            String target = ConfigurationManager.getString("target");
            if (target != null && !"".equals(target)) {
            	String[] targets = target.split(",");
            	String string = ConfigurationManager.getString("src");
            	String[] srcs = string.split(",");
            	
            	for (int i = 0; i < srcs.length; i++) {
            		boolean remove = remove(message.split(":")[1], srcs[i], targets[i]);
				}
            }
        }
    }


    private void rabbitConnErrot(Exception e) {
        String event_type = EIEventType.RABBIT_CONNECT_ERROR.getCode();
        EI ei = EIConfig.getEiConfig("").getEiMaps().get(event_type);
        if (ei == null) {
//            LOGGER.error("EI配置文件中没有事件类型：" + event_type);
        } else {
            ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            ei.setKObject("org.cimiss2.dwp.z_surf_dayLight.DayLightSubThread");
            ei.setKEvent("RabbitMQ连接异常");
            ei.setKIndex("连接信息：[主机：" + host + ",用户名：" + name + ",密码：" + password +
                    ",端口号：" + port + "，对列：" + queue + "]");
            RestfulInfo restfulInfo = new RestfulInfo();
            restfulInfo.setType("SYSTEM.ALARM.EI ");
            restfulInfo.setName("数据解码入库EI告警信息");
            restfulInfo.setMessage("数据解码入库EI告警信息");
            restfulInfo.setFields(ei);
            List<RestfulInfo> restfulInfos = new ArrayList<>();
            restfulInfos.add(restfulInfo);
            RestfulSendData.getInstance("config/RestfulInfo.js").SendData(restfulInfos, 1);
        }
        System.exit(0);
    }

    public abstract void write(String message, StringBuffer buffer, List<RestfulInfo> diInfo, List<RestfulInfo> eiInfo);

    public abstract void formate(String message, StringBuffer buffer, List<RestfulInfo> diInfo, List<RestfulInfo> eiInfo);

    public abstract void finallyAccept(StringBuffer buffer);

    public abstract void finallyReject(StringBuffer buffer);


}
