package cma.cimiss2.dpc.indb.rado;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.FileDi;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.IndexConf;
import cma.cimiss2.dpc.indb.common.LoadTableConfig;
import cma.cimiss2.dpc.indb.common.StringSplit;
import cma.cimiss2.dpc.indb.service.ServiceSql;
import cma.cimiss2.dpc.indb.vo.Pair;
import cma.cimiss2.dpc.indb.vo.TableConfig;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.*;
import org.apache.commons.io.FileUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.config.RabbitMQConfig;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.ConfigurationManager;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.LoadPropertiesFile;
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
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * <br>
 *
 * @author wuzuoqiang
 * @Title: IndexSubThread.java
 * @Package org.cimiss2.dwp.RADAR
 * @Description: TODO(雷达卫星消息处理线程 ， 主要功能为索引入库和文件移位)
 *
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月15日 下午7:27:00   wuzuoqiang    Initial creation.
 * </pre>
 */
public class IndexSubThread implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger("loggerInfo"); //消息处理日志
    public static final Logger messagelogger = LoggerFactory.getLogger("messageInfo"); //消息日志
    public static java.sql.Connection sqlconn = null;
    private BlockingQueue<FileDi> diQueues;

    private String ctsCode;

    public IndexSubThread(BlockingQueue<FileDi> diQueues, String ctsCode) {
        // 加载索引库策略的配置文件
        if (!IndexConf.ReadConfig(ConfigurationManager.getJarSuperPath() + "config" + "/index.txt")) {
            System.exit(0);
        }
//        // 加载卫星重命名文件名转换配置文件
//        if (!RenameChangeConf.ReadConfig(ConfigurationManager.getJarSuperPath() + "config" + "/ChangeFile.txt")) {
//            System.exit(0);
//        }
        this.diQueues = diQueues;

        this.ctsCode = ctsCode;
    }

    @Override
    public void run() {
        Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
        TableConfig tabConf = LoadTableConfig.getInstance().getTablesMaps().get(ctsCode);
        String strRecvMode = tabConf.getFileloop();

        // 例:雷达资料-毫米波数据   S024_RabbitMQ.queueName  Z_RADA_HMBR_XL_J.0020.0004.R001_001
        String queueName = tabConf.getQueueName();
        if (strRecvMode.equals("1")) { //是否为轮询目录
//            String retweetType = config.getProperty("retweetType");
            String retweetType = tabConf.getDataType();
            if (retweetType == null || retweetType.isEmpty()) {
                logger.error("未指定轮询的资料类型！");
                System.exit(0);
            }
            String[] retweetTypes = retweetType.split(",");
            HashMap<String, String> dataDirMap = new HashMap<String, String>(); //map<资料类型,轮询目录>
                if (LoadTableConfig.getInstance().getTablesMaps().containsKey(ctsCode)) {
                    String strRetweetDir = tabConf.getRetweetDir();
                    if (strRetweetDir == null || strRetweetDir.isEmpty()) {
                        logger.error("未配置资料类型：" + retweetType + "的轮询目录！");
                    }
                    dataDirMap.put(retweetType, strRetweetDir);
                } else {
                    logger.error("未配置资料类型：" + retweetType + "的入库策略！");
                }
            if (dataDirMap.size() > 0) {
                List<String> fileList = new ArrayList<String>();
                while (true) {
                    try {
                        //进行目录轮询
                        for (HashMap.Entry<String, String> entry : dataDirMap.entrySet()) {
                            File dir = new File(entry.getValue());
                            fileList.clear();
                            listDirectory(dir, fileList);
                            for (int i = 0; i < fileList.size(); i++) {
                                Date recv_time = new Date();
                                String message = entry.getKey() + ":" + fileList.get(i);
                                messagelogger.info("\n retweet dir file:" + message);
                                try {
                                    Action action = processMsg(tabConf, message, recv_time);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    logger.error("文件" + message + "处理错误:" + e.getMessage());
                                }
                            }
                        }
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        // TODO: handle exception
                        logger.error("目录轮询错误:" + e.getMessage());
                    }
                }
            } else {
                logger.error("配置的待轮询资料类型数为0！");
                System.exit(0);
            }
        } else { //消息
            String path = "";
            RabbitMQConfig rabbitMQConfig = null;
            try {
                rabbitMQConfig = new RabbitMQConfig(path);
                ConnectionFactory factory = new ConnectionFactory();
                // 获取rabbitMQ连接信息
                factory.setHost(rabbitMQConfig.getHost());
                factory.setUsername(rabbitMQConfig.getUser());
                factory.setPassword(rabbitMQConfig.getPassword());
                factory.setPort(rabbitMQConfig.getPort());
                // 创建连接
                Connection connection = factory.newConnection();
                final Channel channel = connection.createChannel();
                channel.queueDeclare(queueName, true, false, false, null);
                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
                                               byte[] body) throws IOException {
                        Date recv_time = new Date();
                        // 获取消息体
                        String message = new String(body, "UTF-8");
                        messagelogger.info("\n message:" + message);
                        Action action = processMsg(tabConf, message, recv_time);
                        // 消息确认
                        if (action == Action.RETRY) {
                            channel.basicReject(envelope.getDeliveryTag(), true);
                        } else {
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        }
                    }
                };
                channel.basicConsume(queueName, false, consumer);
            } catch (Exception e) {
                logger.error("\n 消息中间件连接异常" + e.getMessage());
                String event_type = EIEventType.RABBIT_CONNECT_ERROR.getCode();
                EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
                if (ei == null) {
                    logger.error("\n EI配置文件中没有事件类型：" + event_type);
                } else {
                    if (StartConfig.isSendEi()) {
                        ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        ei.setKObject("org.cimiss2.dwp.rada_sate.IndexSubThread");
                        ei.setKEvent("RabbitMQ连接异常");
                        ei.setKIndex("连接信息：[主机：" + rabbitMQConfig.getHost() + ",用户名：" + rabbitMQConfig.getUser() + ",密码：" + rabbitMQConfig.getPassword() +
                                ",端口号：" + rabbitMQConfig.getPort() + "，对列：" + queueName + "]");
                        RestfulInfo restfulInfo = new RestfulInfo();
                        restfulInfo.setType("SYSTEM.ALARM.EI ");
                        restfulInfo.setName("数据解码入库EI告警信息");
                        restfulInfo.setMessage("数据解码入库EI告警信息");
                        restfulInfo.setFields(ei);
                        List<RestfulInfo> restfulInfos = new ArrayList<>();
                        restfulInfos.add(restfulInfo);
                        RestfulSendData.SendData(restfulInfos);
                    }
                }

                System.exit(0);
            }
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
    private Action processMsg(TableConfig tabConf, String message, Date recv_time) {
        //DI信息
        FileDi fileDi = new FileDi();
        try {
            String strStartTime = TimeUtil.getSysTime(); //处理开始时间
            int index = message.indexOf(":");
            // 消息结构体异常
            if (index < 16) {
                logger.error("\n get error msg:" + message);
                return Action.ACCEPT;
            }
            // 获取资料的四级编码
            String strCtsType = message.substring(0, index);
            fileDi.setPROCESS_START_TIME(strStartTime);
            fileDi.setBUSINESS_STATE("1"); //1-失败 0-成功
            fileDi.setPROCESS_STATE("1"); //1-失败 0-成功
            fileDi.setDATA_TYPE_1(strCtsType);
            // 获取资料的绝对路径
            String srcFilePath = message.substring(index + 1, message.length());
            File file = new File(srcFilePath);
            if (!(file != null && file.exists() && file.isFile())) {
                logger.error("\n 文件不存在" + srcFilePath);
                return Action.ACCEPT;
            }
            fileDi.setFILE_NAME_O(file.getName());
            //获取文件属性
            BasicFileAttributes fileAttr = Files.readAttributes(Paths.get(srcFilePath), BasicFileAttributes.class);
            String strArriveTime = TimeUtil.date2String(new Date(fileAttr.lastModifiedTime().toMillis()), "yyyyMMddHHmmss");
            String strRecvTime = TimeUtil.date2String(new Date(fileAttr.lastModifiedTime().toMillis()), "yyyy-MM-dd HH:mm:ss");
            String filesize = String.valueOf(fileAttr.size());
            fileDi.setTRAN_TIME(strRecvTime);
            fileDi.setFILE_SIZE(filesize);
            boolean bIndb = true; //是否入库
            if (LoadTableConfig.getInstance().getTablesMaps().containsKey(strCtsType)) {
                if (tabConf.getStoreType().equals("0")) { //入库方式为0，不入库
                    bIndb = false;
                }
            } else {
                logger.error("未配置资料入库策略,资料类型:" + strCtsType);
                return Action.RETRY;
            }

            //存放新的文件路径
            String strSodType = tabConf.getSodDataType();
            fileDi.setDATA_TYPE(strSodType);
            //进行重命名
            boolean bMoveFileSuc = false; //移动文件是否成功
//            String strDesFilePath = tabConf.getRetweetDir() + file.getName();
            /**
             * strStartTime    yyyy-MM-dd HH:mm:ss
             * 修改目标路径 按照要求 自动创建目标文件夹   2020/20200803
             **/

            String[] items = StringSplit.split(file.getName(), tabConf.getSplitRegex());
            Vector<Pair<String, String>> fields = IndexConf.sm_indexConf.get(strCtsType);
            // 按规则解析 年月日
            String V01301 = getValue(items, fields, "V01301", strCtsType);
//            String time = TimeUtil.getSysTime("yyyyMMdd");
            String time = getValue(items, fields, "date", strCtsType);

            // 年/年月日/站点 yyyy/yyyyMMdd/xxxxx
            String timePath = time.substring(0, 4) + "/" + time + "/" + V01301 + "/";
            String strDesFilePath = tabConf.getStoryPath() + timePath + file.getName();

            // 搬移文件
            if ("0".equals(tabConf.getMoveFileType())) {

                if (strDesFilePath == "" || strDesFilePath.equals("")) {
                    return Action.RETRY;
                } else {
//                bMoveFileSuc = moveFile(srcFilePath, strDesFilePath); //移动文件
                    bMoveFileSuc = moveFileToDirectory(srcFilePath, tabConf.getStoryPath() + timePath, true); //移动文件到指定文件夹
                }

            }else {
                strDesFilePath = srcFilePath;
                bMoveFileSuc = true;
            }
            fileDi.setFILE_NAME_N(new File(strDesFilePath).getName());
            //	String strEndTime = TimeUtil.getSysTime(); //处理结束时间
            // 入索引库
            if (bMoveFileSuc) {
                if (bIndb) {
                    String strFileName = new File(strDesFilePath).getName();
                    if (!tabConf.getIndexTable().isEmpty()) { //若mysql表名不为空，则需入库
                        String strStoreType = tabConf.getStoreType();
                        String strSql = "";
                        String table = tabConf.getIndexTable();
                        StringBuffer strDataTime = new StringBuffer();
                        if (strStoreType.equals("9")) { //其他处理方式,如通过index.txt入库索引表
                            strSql = ServiceSql.genIndexSql(table, strCtsType, strSodType, items,
                                    strStartTime,/*strEndTime,*/file.getName(), filesize, strRecvTime, strDesFilePath, strDataTime);
                        }
                        fileDi.setDATA_TIME(strDataTime.toString());
                        int iRet = mysql_run_sql(strSql);
                        if (iRet == -1) {
                            logger.error("\n 插入索引表失败:" + strSql + "\n");
                            moveFile(strDesFilePath, srcFilePath); //移动文件
                            return Action.RETRY;
                        } else if (iRet == -2) {
                            logger.error("\n 插入索引表失败:" + strSql + "\n");
                            return Action.REJECT;
                        }
                    }
                    /*if (!tabConf.getAtsTable().isEmpty()) { //若ats表名不为空，则需入库
                        ServiceQueue.getServerQueue().add(new FileInfo(strCtsType, strDesFilePath, tabConf.getSplitRegex()));
                    } else {
                        logger.info("\n 没有配置Cassandra入库信息");
                    }*/
                } else { //不入库
                    fileDi.setBUSINESS_STATE("0"); //1-失败 0-成功
                    fileDi.setPROCESS_STATE("0"); //1-失败 0-成功
                    return Action.ACCEPT;
                }
            } else { //移动文件失败
                return Action.RETRY;
            }
            fileDi.setBUSINESS_STATE("0"); //1-失败 0-成功
            fileDi.setPROCESS_STATE("0"); //1-失败 0-成功
            System.out.println(file.getName());
            return Action.ACCEPT;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return Action.REJECT;
        } finally {
            //插入DI信息
            fileDi.setPROCESS_END_TIME(TimeUtil.getSysTime());
            this.diQueues.offer(fileDi);
        }
    }


    /**
     * 按照 index.txt 名称解析规则 获取解析字段
     *
     * @param items
     * @param fields
     * @param valueName
     * @param strCtsType
     * @return java.lang.String
     * @author YuWenjie
     * @date 2020/8/6 17:10
     **/
    private String getValue(String[] items, Vector<Pair<String, String>> fields, String valueName, String strCtsType) {
        // V01301
        if ("V01301".equals(valueName)) {
            return getValueByValueName(items, fields, "V01301", strCtsType);
            // date
        } else if ("date".equals(valueName)) {
            return getValueByValueName(items, fields, "V04001", strCtsType)
                    + getValueByValueName(items, fields, "V04002", strCtsType)
                    + getValueByValueName(items, fields, "V04003", strCtsType);
        }
        return "";
    }

    private String getValueByValueName(String[] items, Vector<Pair<String, String>> fields, String valueName, String strCtsType) {
        int[] iInxArray = new int[3];
        String strFieldVal;
        for (int i = 0; i < fields.size(); i++) {
            int begin, end;
            if (valueName.equals(fields.get(i).getFirst().trim())) {
                strFieldVal = fields.get(i).getSecond().trim();
                if (strFieldVal.contains("$")) {
                    while (strFieldVal.contains("$")) {
                        begin = strFieldVal.indexOf("$");
                        end = strFieldVal.indexOf("}");
                        String var = strFieldVal.substring(begin, end + 1);

                        if (var.contains(":")) {
                            if ((var.contains("${")) && (var.contains("}"))) {
                                String[] index = strFieldVal.substring(begin + 2, end).split(":");
                                for (int m = 0; m < index.length; m++) {
                                    iInxArray[m] = Integer.parseInt(index[m]);
                                }
                                return items[iInxArray[0]].substring(iInxArray[1], iInxArray[1] + iInxArray[2]);
                            } else {
                                logger.error("cts type:" + strCtsType + " wrong format of the index config\n");
                                return "";
                            }
                        } else {
                            iInxArray[0] = Integer.parseInt(strFieldVal.substring(begin + 2, end));
                            return items[iInxArray[0]];
                        }
                    }
                } else {
                    return fields.get(i).getSecond().trim();
                }
            }
        }
        return "";
    }

    private int mysql_run_sql(String strSql) {
        try {
            Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
            String strdbType = config.getProperty("dataBaseType");
//			if (strdbType.equals("2")) { //虚谷数据库
//				sqlconn = ConnectionPoolFactory.getInstance().getConnection("xugu");
//			} else { //阿里云DRDS数据库、万里开源数据库
//				sqlconn = ConnectionPoolFactory.getInstance().getConnection("fileindex");
//			}
            sqlconn = ConnectionPoolFactory.getInstance().getConnection("xugu");
            if (sqlconn == null) {
                return -1;
            }
            Statement stmt = null;
            stmt = sqlconn.createStatement();
            stmt.execute(strSql);
            stmt.close();
        } catch (SQLException e) {
            logger.error("\n 数据库连接异常" + "\n " + strSql + "\n " + e.getMessage());
            return -2;
        } finally {
            try {
                sqlconn.close();
            } catch (SQLException e) {
                logger.error("\n 关闭数据库连接异常:" + e.getMessage());
                return -2;
            }
        }
        return 0;
    }

    /**
     * 函数名：moveFile
     *
     * @param filePath   源文件绝对路径
     * @param targetFile 目标文件绝对路径
     * @return boolean 文件是否移动成功
     */
    private boolean moveFile(String filePath, String targetFile) {
        File file = new File(filePath);
        File targetfile = new File(targetFile);
        try {
            if (targetfile.exists()) { //判断目标文件是否已经存在，若存在则进行覆盖
                if (!targetfile.delete()) {
                    logger.error("\n delete file error: " + targetFile);
                    return false;
                }
            }
            FileUtils.moveFile(file, targetfile);
        } catch (IOException e) {
            //e.printStackTrace();
            logger.error("\n file move error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("\n file move error: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 函数名：moveFileToDirectory
     *
     * @param filePath      源文件绝对路径
     * @param destDir       目标文件目录
     * @param createDestDir If {@code true} create the destination directory,
     *                      otherwise if {@code false} throw an IOException
     * @return boolean 文件是否移动成功
     */
    private boolean moveFileToDirectory(String filePath, String destDir, boolean createDestDir) {
        File file = new File(filePath);
        File destdir = new File(destDir);
        try {
            FileUtils.moveFileToDirectory(file, destdir, createDestDir);
        } catch (IOException e) {
            //e.printStackTrace();
            logger.error("\n file move error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("\n file move error: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 函数名：listDirectory
     *
     * @param dir      待遍历的目录路径
     * @param fileList 返回的目录下文件列表
     * @return 无
     */
    public static void listDirectory(File dir, List<String> fileList) {
        if (!dir.exists()) {
            logger.error("目录：" + dir + "不存在!");
            return;
        }
        if (!dir.isDirectory()) {
            logger.error(dir + "不是目录!");
            return;
        }
        String strFileName;
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) { //目录
                    listDirectory(file, fileList);
                } else { //文件
                    //去除以tmp结尾和.开头的文件名
                    strFileName = file.getName();
                    if ((!strFileName.toLowerCase().endsWith(".tmp")) && (!strFileName.startsWith("."))) {
                        fileList.add(file.getPath());
                    }
                }
            }
        }
    }

}
