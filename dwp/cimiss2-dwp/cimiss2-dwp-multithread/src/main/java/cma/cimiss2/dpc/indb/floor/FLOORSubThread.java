package cma.cimiss2.dpc.indb.floor;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.FileDi;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.enumeration.Action;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.IndexConf;
import cma.cimiss2.dpc.indb.common.LoadTableConfig;
import cma.cimiss2.dpc.indb.common.RenameChangeConf;
import cma.cimiss2.dpc.indb.common.StringSplit;
import cma.cimiss2.dpc.indb.service.ServiceQueue;
import cma.cimiss2.dpc.indb.service.ServiceSql;
import cma.cimiss2.dpc.indb.vo.FileInfo;
import cma.cimiss2.dpc.indb.vo.PathInfo;
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
public class FLOORSubThread implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger("loggerInfo"); //消息处理日志
    public static final Logger messagelogger = LoggerFactory.getLogger("messageInfo"); //消息日志
    public static java.sql.Connection sqlconn = null;
    private BlockingQueue<FileDi> diQueues;
    public FLOORSubThread(BlockingQueue<FileDi> diQueues) {
        // 加载索引库策略的配置文件
        if (!IndexConf.ReadConfig(ConfigurationManager.getJarSuperPath() + "config" + "/index.txt")) {
            System.exit(0);
        }
        // 加载卫星重命名文件名转换配置文件
        if (!RenameChangeConf.ReadConfig(ConfigurationManager.getJarSuperPath() + "config" + "/ChangeFile.txt")) {
            System.exit(0);
        }
        this.diQueues = diQueues;
    }

    @Override
    public void run() {
        Properties config = LoadPropertiesFile.getInstance().getConfigProperties();
        String strRecvMode = config.getProperty("fileloop");
        if (strRecvMode.equals("1")) { //是否为轮询目录
            String retweetType = config.getProperty("retweetType");
            if (retweetType == null || retweetType.isEmpty()) {
                logger.error("未指定轮询的资料类型！");
                System.exit(0);
            }
            String[] retweetTypes = retweetType.split(",");
            HashMap<String, String> dataDirMap = new HashMap<String, String>(); //map<资料类型,轮询目录>
            for (int i = 0; i < retweetTypes.length; i++) {
                if (LoadTableConfig.getInstance().getTablesMaps().containsKey(retweetTypes[i])) {
                    String strRetweetDir = LoadTableConfig.getInstance().getTablesMaps().get(retweetTypes[i]).getRetweetDir();
                    if (strRetweetDir == null || strRetweetDir.isEmpty()) {
                        logger.error("未配置资料类型：" + retweetTypes[i] + "的轮询目录！");
                        continue;
                    }
                    dataDirMap.put(retweetTypes[i], strRetweetDir);
                } else {
                    logger.error("未配置资料类型：" + retweetTypes[i] + "的入库策略！");
                    continue;
                }
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
                                    Action action = processMsg(message, recv_time);
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
                channel.queueDeclare(StartConfig.queueName("S024_RabbitMQ.queueName"), true, false, false, null);
                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
                                               byte[] body) throws IOException {
                        Date recv_time = new Date();
                        // 获取消息体
                        String message = new String(body, "UTF-8");
                        messagelogger.info("\n message:" + message);
                        Action action = processMsg(message, recv_time);
                        // 消息确认
                        if (action == Action.RETRY) {
                            channel.basicReject(envelope.getDeliveryTag(), true);
                        } else {
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        }
                    }
                };
                channel.basicConsume(StartConfig.queueName("S024_RabbitMQ.queueName"), false, consumer);
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
                                ",端口号：" + rabbitMQConfig.getPort() + "，对列：" + StartConfig.queueName("S024_RabbitMQ.queueName") + "]");
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
    private Action processMsg(String message, Date recv_time) {
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
            TableConfig tabConf = null;
            boolean bIndb = true; //是否入库
            if (LoadTableConfig.getInstance().getTablesMaps().containsKey(strCtsType)) {
                tabConf = LoadTableConfig.getInstance().getTablesMaps().get(strCtsType);
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
            String strDesFilePath = tabConf.getRetweetDir() + file.getName();
            if (strDesFilePath == "" || strDesFilePath.equals("")) {
                return Action.RETRY;
            } else {
                bMoveFileSuc = moveFile(srcFilePath, strDesFilePath); //移动文件
            }
            fileDi.setFILE_NAME_N(new File(strDesFilePath).getName());
            //	String strEndTime = TimeUtil.getSysTime(); //处理结束时间
            // 入索引库
            if (bMoveFileSuc) {
                if (bIndb) {
                    String strFileName = new File(strDesFilePath).getName();
                    String[] items = StringSplit.split(strFileName, tabConf.getSplitRegex());
                    if (!tabConf.getIndexTable().isEmpty()) { //若mysql表名不为空，则需入库
                        String strStoreType = tabConf.getStoreType();
                        String strSql = "";
                        StringBuffer strDataTime = new StringBuffer();
                        if (strStoreType.equals("9")) { //其他处理方式,如通过index.txt入库索引表
                            strSql = ServiceSql.genIndexSql(StartConfig.valueTable("S024_value_table_name"), strCtsType, strSodType, items,
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
                    if (!tabConf.getAtsTable().isEmpty()) { //若ats表名不为空，则需入库
                        ServiceQueue.getServerQueue().add(new FileInfo(strCtsType, strDesFilePath, tabConf.getSplitRegex()));
                    } else {
                        logger.info("\n 没有配置Cassandra入库信息");
                    }
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
     * 函数名：getTargetPath
     *
     * @param filename       文件名
     * @param strSplitRegex  分割符
     * @param pathInfos      路径配置信息
     * @param strNewFileName 重命名文件名
     * @param strDataType    数据类型
     * @return String 目标文件的绝对路径 失败返回""
     */
    private String getTargetPath(String filename, String strSplitRegex, List<PathInfo> pathInfos, String strNewFileName, String strDataType) {
        String targetPath = "";
        try {
            if (strNewFileName.isEmpty()) { //无需重命名
                strNewFileName = filename;
            } else { //进行重命名
                String[] filename_items = StringSplit.split(filename, strSplitRegex);
                if (strDataType.equals("K.0340.0001.R001") || (strDataType.equals("K.0340.0001.R002"))) { //有不存在V_BBB指时需要补NUL
                    strNewFileName = renameGOES15File(strNewFileName, filename_items);
                } else if (strDataType.equals("K.0455.0001.R001")) {
                    strNewFileName = renameFile(strNewFileName, filename_items);
                    strNewFileName = strNewFileName.replace("HRP", "NHE");
                } else if (strDataType.equals("K.0145.0001.R011") || strDataType.equals("K.0145.0001.R012")) {
                    strNewFileName = renameFile(strNewFileName, filename_items);
                    strNewFileName = strNewFileName.replace("NA19", "N19");
                    strNewFileName = strNewFileName.replace("NA18", "N18");
                    strNewFileName = strNewFileName.replace("NA15", "N15");
                    strNewFileName = strNewFileName.replace("HRS", "HIRS");
                    strNewFileName = strNewFileName.replace("AMA", "AMSUA");
                    strNewFileName = strNewFileName.replace("AMB", "AMSUB");
                } else if (strDataType.equals("K.0131.0001.R001") || strDataType.equals("K.0735.0001.R001") || strDataType.equals("K.0736.0001.R001")) {
                    strNewFileName = renameFile(strNewFileName, filename_items);
                    strNewFileName = renameChange(strNewFileName);
                    if ((strNewFileName == "") || strNewFileName.equals("")) {
                        return "";
                    }
                } else {
                    strNewFileName = renameFile(strNewFileName, filename_items);
                }
            }
            String[] newFileNameItems = StringSplit.split(strNewFileName, strSplitRegex);
            for (int i = 0; i < pathInfos.size(); i++) {
                if (pathInfos.get(i).getType() == 0) //字符串值
                {
                    targetPath = targetPath + "/" + pathInfos.get(i).getStr();
                } else if (pathInfos.get(i).getType() == 1) //[pos]
                {
                    targetPath = targetPath + "/" + newFileNameItems[pathInfos.get(i).getPos()];
                } else //[pos:start:length]
                { //type为2或3
                    targetPath = targetPath + "/" + newFileNameItems[pathInfos.get(i).getPos()].
                            substring(pathInfos.get(i).getStart(), pathInfos.get(i).getStart() + pathInfos.get(i).getEnd());
                }
            }
            targetPath = targetPath + "/" + strNewFileName;
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("文件名:" + filename + " 获取文件目标路径失败:" + e.getMessage());
            targetPath = "";
        }

        return targetPath;
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

    /**
     * 函数名：renameFile
     *
     * @param strNewFileName 新文件名
     * @param filename_items 源文件名各分隔段
     * @return 新文件名
     */
    public static String renameFile(String strNewFileName, String[] filename_items) {
        int begin, end;
        int[] iInxArray = new int[3];    //索引截取位置
        while (strNewFileName.contains("[") && strNewFileName.contains("]")) {
            begin = strNewFileName.indexOf("[");
            end = strNewFileName.indexOf("]");
            String var = strNewFileName.substring(begin, end + 1);
            if (var.contains("(") && var.contains(")")) //处理截断路径，如[4(0,4)]
            {
                int left = var.indexOf("("); //左括号位置
                int right = var.indexOf(")"); //有括号位置
                int commaPos = var.indexOf(","); //逗号位置
                iInxArray[0] = Integer.parseInt(var.substring(1, left));
                iInxArray[1] = Integer.parseInt(var.substring(left + 1, commaPos));
                iInxArray[2] = Integer.parseInt(var.substring(commaPos + 1, right));
                strNewFileName = strNewFileName.replace(var, filename_items[iInxArray[0]].substring(iInxArray[1], iInxArray[1] + iInxArray[2]));
            } else //处理[8]
            {
                iInxArray[0] = Integer.parseInt(strNewFileName.substring(begin + 1, end));
                strNewFileName = strNewFileName.replace(var, filename_items[iInxArray[0]]);
            }
        }

        return strNewFileName;
    }

    /**
     * 函数名：renameChange 文件名中特殊转换
     *
     * @param strNewFileName 新文件名
     * @return 新文件名
     */
    public static String renameChange(String strNewFileName) {
        int begin, end;
        HashMap<String, String> dataChange = null;
        //获取当前时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //资料数据时间
        Calendar calData = Calendar.getInstance();
        calData.setTime(cal.getTime());
        while (strNewFileName.contains("${") && strNewFileName.contains("}")) {
            begin = strNewFileName.indexOf("${");
            end = strNewFileName.indexOf("}");
            String var = strNewFileName.substring(begin, end + 1);
            String value = strNewFileName.substring(begin + 2, end);
            if (var.contains("|")) { //包含在配置中的转换
                String[] strChange = value.split("\\|");
                if (strChange[0].equalsIgnoreCase("YYYYDDD")) {
                    calData.set(Calendar.YEAR, Integer.valueOf(strChange[1].substring(0, 4)));
                    calData.set(Calendar.DAY_OF_YEAR, Integer.valueOf(strChange[1].substring(4, 7)));
                    strNewFileName = strNewFileName.replace(var, String.format("%04d%02d%02d", calData.get(Calendar.YEAR), calData.get(Calendar.MONTH) + 1, calData.get(Calendar.DATE)));
                } else if (strChange[0].equalsIgnoreCase("DDD")) { //一年第几天
                    if (Integer.valueOf(strChange[1]) - cal.get(Calendar.DAY_OF_YEAR) > 350) {//防止跨年时不正确
                        calData.add(Calendar.YEAR, -1);
                    }
                    calData.set(Calendar.DAY_OF_YEAR, Integer.valueOf(strChange[1]));
                    strNewFileName = strNewFileName.replace(var, String.format("%02d%02d", calData.get(Calendar.MONTH) + 1, calData.get(Calendar.DATE)));
                } else if (strChange[0].equalsIgnoreCase("YY")) { //年
                    String year = String.format("%04d", calData.get(Calendar.YEAR)).substring(0, 2) + strChange[1];
                    calData.set(Calendar.YEAR, Integer.valueOf(year));
                    if (calData.get(Calendar.YEAR) - cal.get(Calendar.YEAR) > 50) { //防止跨世纪年不正确
                        calData.add(Calendar.YEAR, -100);
                    }
                    strNewFileName = strNewFileName.replace(var, String.format("%04d", cal.get(Calendar.YEAR)));
                } else if (RenameChangeConf.sm_changeConf.containsKey(strChange[0])) {
                    dataChange = RenameChangeConf.sm_changeConf.get(strChange[0]);
                    if (dataChange.containsKey(strChange[1])) {
                        strNewFileName = strNewFileName.replace(var, dataChange.get(strChange[1]));
                    } else {
                        logger.error("未在配置文件ChangeFile.txt定义的转换类型:" + strChange[0] + "找到待转换值:strChange[1]");
                        return "";
                    }
                } else {
                    logger.error("未在配置文件ChangeFile.txt定义转换类型:" + strChange[0]);
                    return "";
                }
            } else {
                if (var.equalsIgnoreCase("${YYYY}")) { //年
                    strNewFileName = strNewFileName.replace(var, String.format("%04d", cal.get(Calendar.YEAR)));
                }
                if (var.equalsIgnoreCase("${MM}")) {
                    strNewFileName = strNewFileName.replace(var, String.format("%02d", cal.get(Calendar.MONTH) + 1));
                }
                if (var.equalsIgnoreCase("${DD}")) {
                    strNewFileName = strNewFileName.replace(var, String.format("%02d", cal.get(Calendar.DATE)));
                }
                if (var.equalsIgnoreCase("${HH}")) {
                    strNewFileName = strNewFileName.replace(var, String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)));
                }
                if (var.equalsIgnoreCase("${MI}")) {
                    strNewFileName = strNewFileName.replace(var, String.format("%02d", cal.get(Calendar.MINUTE)));
                }
                if (var.equalsIgnoreCase("${SS}")) {
                    strNewFileName = strNewFileName.replace(var, String.format("%02d", cal.get(Calendar.SECOND)));
                }
            }
        }

        return strNewFileName;
    }

    /**
     * 函数名：renameGOES15File
     *
     * @param strNewFileName 新文件名
     * @param filename_items 源文件名各分隔段
     * @return 新文件名
     */
    public static String renameGOES15File(String strNewFileName, String[] filename_items) {
        int begin, end;
        //当每月最后一天跨月时，月份为上个月，如A_JCCX01KNES302351_C_BABJ_20180501000732_96887.TXT
        if (Integer.parseInt(filename_items[4].substring(6, 8)) < Integer.parseInt(filename_items[1].substring(10, 12))) {
            Calendar cal = Calendar.getInstance();
            String strYearMon = filename_items[4].substring(0, 6);
            cal.setTime(TimeUtil.String2Date(strYearMon, "yyyyMM"));
            cal.add(Calendar.MONTH, -1);
            filename_items[4] = filename_items[4].replaceAll(strYearMon, TimeUtil.date2String(cal.getTime(), "yyyyMM"));
        }
        int[] iInxArray = new int[3];    //索引截取位置
        while (strNewFileName.contains("[") && strNewFileName.contains("]")) {
            begin = strNewFileName.indexOf("[");
            end = strNewFileName.indexOf("]");
            String var = strNewFileName.substring(begin, end + 1);
            if (var.contains("(") && var.contains(")")) //处理截断路径，如[4(0,4)]
            {
                int left = var.indexOf("("); //左括号位置
                int right = var.indexOf(")"); //有括号位置
                int commaPos = var.indexOf(","); //逗号位置
                iInxArray[0] = Integer.parseInt(var.substring(1, left));
                iInxArray[1] = Integer.parseInt(var.substring(left + 1, commaPos));
                iInxArray[2] = Integer.parseInt(var.substring(commaPos + 1, right));
                if (filename_items[iInxArray[0]].length() < iInxArray[1] + iInxArray[2]) {
                    strNewFileName = strNewFileName.replace(var, "NUL"); //若不存在V_BBB则补NUL
                } else {
                    strNewFileName = strNewFileName.replace(var, filename_items[iInxArray[0]].substring(iInxArray[1], iInxArray[1] + iInxArray[2]));
                }
            } else //处理[8]
            {
                iInxArray[0] = Integer.parseInt(strNewFileName.substring(begin + 1, end));
                strNewFileName = strNewFileName.replace(var, filename_items[iInxArray[0]]);
            }
        }

        return strNewFileName;
    }

}
