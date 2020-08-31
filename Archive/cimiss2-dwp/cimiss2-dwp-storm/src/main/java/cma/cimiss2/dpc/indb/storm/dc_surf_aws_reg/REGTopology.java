package cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg;

import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.core.tools.StormConfig;
import cma.cimiss2.dpc.indb.storm.DIEIBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.mysql.mysqlCumPreBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.mysql.mysqlDecodeBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.mysql.mysqlHorBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.mysql.mysqlMinBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.mysql.mysqlMinPreBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.mysql.mysqlRepBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.ots.otsDecodeBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.ots.otsHorBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.ots.otsMinBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.ots.otsRepBolt;
import cma.cimiss2.dpc.indb.storm.tools.CustomDeclarator;
import cma.cimiss2.dpc.indb.storm.tools.MessageScheme;
import io.latent.storm.rabbitmq.Declarator;
import io.latent.storm.rabbitmq.RabbitMQSpout;
import io.latent.storm.rabbitmq.config.ConnectionConfig;
import io.latent.storm.rabbitmq.config.ConsumerConfig;
import io.latent.storm.rabbitmq.config.ConsumerConfigBuilder;
import org.apache.storm.Config;
import org.apache.storm.generated.StormTopology;

import java.util.Map;

import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.ConnectionFactory;
/**
 * 区域站拓扑配置
 * Created by xzh on 2017/07/31.
 */
public class REGTopology {
	public static final Logger logger = LoggerFactory.getLogger("loggerInfo");
    private String path;
    private String RABBITMQ_HOST;
    private int RABBITMQ_PORT;
    private String RABBITMQ_USER;
    private String RABBITMQ_PASS;
    private String EXCHANGE_NAME;
    private String QUEUE_NAME;
    private int spoutNum;
    private int boltNum;
    private int DIEIboltNum;
    ///
    private int minpreBoltNum;
    
    private int workerNum;
    private int ackers;
    private int queueNum;
    private String batchSize;
    private String horTable;
    private String minTable;
    private String minPreTable;
    private String cumPreTable;
    private String repTable;
    private String intervalTime;
    private String dataSource;
    private String dataFlow;
    private Boolean diOption;
    private Boolean eiOption;
    private String cts_code;
    private String hor_sod_code;
    private String mul_sod_code;
    private String pre_sod_code;
    private String accu_sod_code;
    private String report_sod_code;
    public static Map<String, Map<String, String>> map;

    public REGTopology(String path) {
        this.path = path;
        logger.info("db file path: " + path + "db.properties");
        logger.info("global path: " + path + "dpc_global_config.properties");
        ConfigurationManager.loadPro(path, "db.properties");
        ConfigurationManager.loadPro(path, "dpc_global_config.properties");
        cma.cimiss2.dpc.indb.core.ots.Config.endpoint= ConfigurationManager.getString("ots.url");
        cma.cimiss2.dpc.indb.core.ots.Config.accessId= ConfigurationManager.getString("ots.accessId");
        cma.cimiss2.dpc.indb.core.ots.Config.accessKey= ConfigurationManager.getString("ots.accessKey");
        cma.cimiss2.dpc.indb.core.ots.Config.instance= ConfigurationManager.getString("ots.instance");

        map = StormConfig.getConfig(path + "REGConfig.xml").getMaps();
        queueNum = Integer.valueOf(map.get("rabbitmq").get("num"));

        batchSize = map.get("base").get("BATCHSIZE");
        horTable = map.get("base").get("TABLE_HOR");
        minTable = map.get("base").get("TABLE_MIN");
        minPreTable = map.get("base").get("TABLE_MIN_PRE");
        cumPreTable = map.get("base").get("TABLE_CUM_PRE");
        repTable = map.get("base").get("TABLE_REP");
        intervalTime = map.get("base").get("INTERVALTIME");
        dataSource = map.get("base").get("DATASOURCE");
        dataFlow = map.get("base").get("DATA_FLOW");
        cts_code = map.get("base").get("CTS_CODE");
        hor_sod_code = map.get("base").get("HOR_SOD_CODE");
        mul_sod_code = map.get("base").get("MUL_SOD_CODE");
        pre_sod_code = map.get("base").get("PRE_SOD_CODE");
        accu_sod_code = map.get("base").get("ACCU_SOD_CODE");
        report_sod_code = map.get("base").get("REPORT_SOD_CODE");
        
        //DIEISender.LOCAL_DI_OPTION = Boolean.parseBoolean(map.get("DIEI").get("DI_OPTION"));
        //DIEISender.LOCAL_EI_OPTION = Boolean.parseBoolean(map.get("DIEI").get("EI_OPTION"));
        diOption = Boolean.parseBoolean(map.get("DIEI").get("DI_OPTION"));
        eiOption = Boolean.parseBoolean(map.get("DIEI").get("EI_OPTION"));
        DIEISender.LOCAL_DI_OPTION = diOption;
        DIEISender.LOCAL_EI_OPTION = eiOption;
    }

    public Object[] buildTopology() {
        MessageScheme scheme = new MessageScheme();
        //--创建TopologyBuilder类实例
        TopologyBuilder builder = new TopologyBuilder();
        workerNum = Integer.parseInt(map.get("storm").get("WORKER"));
        ackers = Integer.parseInt(map.get("storm").get("ACKER"));
        for (int i = 1; i <= queueNum; i++) {
            //RABBITMQ_HOST = map.get("rabbitmq" + i).get("HOST");
            //RABBITMQ_PORT = Integer.parseInt(map.get("rabbitmq" + i).get("PORT"));
            //RABBITMQ_USER = map.get("rabbitmq" + i).get("USER");
            //RABBITMQ_PASS = map.get("rabbitmq" + i).get("PASSWORD");
        	RABBITMQ_HOST = ConfigurationManager.getString("RabbitMQ.host");
            RABBITMQ_PORT = ConfigurationManager.getInteger("RabbitMQ.port");
            RABBITMQ_USER = ConfigurationManager.getString("RabbitMQ.user");
            RABBITMQ_PASS = ConfigurationManager.getString("RabbitMQ.passWord");
        	EXCHANGE_NAME = map.get("rabbitmq" + i).get("EXCHANGE_NAME");
            QUEUE_NAME = map.get("rabbitmq" + i).get("QUEUE");
            spoutNum = Integer.parseInt(map.get("rabbitmq" + i).get("SPOUT"));
            boltNum = Integer.parseInt(map.get("rabbitmq" + i).get("BOLT"));
            DIEIboltNum = Integer.parseInt(map.get("rabbitmq" + i).get("DIEIBOLT"));
            minpreBoltNum = Integer.parseInt(map.get("rabbitmq" + i).get("MINPREBOLT"));
            //创建rabbitmq spout
            ConnectionConfig connConfig = new ConnectionConfig(RABBITMQ_HOST, RABBITMQ_PORT,RABBITMQ_USER, RABBITMQ_PASS,ConnectionFactory.DEFAULT_VHOST,60);
            Declarator decl = new CustomDeclarator(EXCHANGE_NAME, QUEUE_NAME);
            ConsumerConfig spoutConfig = new ConsumerConfigBuilder().connection(connConfig)
                    .queue(QUEUE_NAME)
                    .prefetch(50)
                    .requeueOnFail()
                    .build();

            IRichSpout spout = new RabbitMQSpout(scheme, decl);
            //注册spout和bolt
            builder.setSpout("REGSpout-" + QUEUE_NAME, spout, spoutNum)
                    .addConfigurations(spoutConfig.asMap())
                    .setMaxSpoutPending(100).setNumTasks(spoutNum);

            switch (dataSource) {
                case "2":
                    builder.setBolt("REG-decode-Bolt-" + QUEUE_NAME, new otsDecodeBolt(), boltNum).shuffleGrouping("REGSpout-" + QUEUE_NAME);
                    builder.setBolt("REG-hor-Bolt-" + QUEUE_NAME, new otsHorBolt(), boltNum).shuffleGrouping("REG-decode-Bolt-" + QUEUE_NAME, "REG-HOR-Bolt");
                    builder.setBolt("REG-min-Bolt-" + QUEUE_NAME, new otsMinBolt(), boltNum).shuffleGrouping("REG-decode-Bolt-" + QUEUE_NAME, "REG-MIN-Bolt");
                    builder.setBolt("REG-rep-Bolt-" + QUEUE_NAME, new otsRepBolt(), boltNum).shuffleGrouping("REG-decode-Bolt-" + QUEUE_NAME, "REG-REP-Bolt");
                    break;
                case "1":
                    builder.setBolt("REG-decode-Bolt-" + QUEUE_NAME, new mysqlDecodeBolt(), boltNum).shuffleGrouping("REGSpout-" + QUEUE_NAME).setNumTasks(boltNum);
                    builder.setBolt("REG-hor-Bolt-" + QUEUE_NAME, new mysqlHorBolt(), boltNum).shuffleGrouping("REG-decode-Bolt-" + QUEUE_NAME, "REG-HOR-Bolt").setNumTasks(boltNum);
                    builder.setBolt("REG-min-Bolt-" + QUEUE_NAME, new mysqlMinBolt(), boltNum).shuffleGrouping("REG-decode-Bolt-" + QUEUE_NAME, "REG-MIN-Bolt").setNumTasks(boltNum);
                    builder.setBolt("REG-min-pre-Bolt-" + QUEUE_NAME, new mysqlMinPreBolt(), minpreBoltNum).shuffleGrouping("REG-decode-Bolt-" + QUEUE_NAME, "REG-MIN-PRE-Bolt").setNumTasks(minpreBoltNum);
                    builder.setBolt("REG-cum-pre-Bolt-" + QUEUE_NAME, new mysqlCumPreBolt(), minpreBoltNum).shuffleGrouping("REG-decode-Bolt-" + QUEUE_NAME, "REG-CUM-PRE-Bolt").setNumTasks(minpreBoltNum);
                    builder.setBolt("REG-rep-Bolt-" + QUEUE_NAME, new mysqlRepBolt(), boltNum).shuffleGrouping("REG-decode-Bolt-" + QUEUE_NAME, "REG-REP-Bolt").setNumTasks(boltNum);
                    break;
                case "0":
                    builder.setBolt("REG-decode-Bolt-" + QUEUE_NAME, new mysqlDecodeBolt(), boltNum).shuffleGrouping("REGSpout-" + QUEUE_NAME);
                    builder.setBolt("REG-hor-Bolt-" + QUEUE_NAME, new mysqlHorBolt(), boltNum).shuffleGrouping("REG-decode-Bolt-" + QUEUE_NAME, "REG-HOR-Bolt");
                    builder.setBolt("REG-min-Bolt-" + QUEUE_NAME, new mysqlMinBolt(), boltNum).shuffleGrouping("REG-decode-Bolt-" + QUEUE_NAME, "REG-MIN-Bolt");
                    builder.setBolt("REG-rep-Bolt-" + QUEUE_NAME, new mysqlRepBolt(), boltNum).shuffleGrouping("REG-decode-Bolt-" + QUEUE_NAME, "REG-REP-Bolt");
                    break;
            }
            if (DIEISender.GLOBAL_DI_OPTION && DIEISender.LOCAL_DI_OPTION
                    || DIEISender.GLOBAL_EI_OPTION && DIEISender.LOCAL_EI_OPTION) {
                builder.setBolt("DIEIBolt-" + QUEUE_NAME, new DIEIBolt(), DIEIboltNum)
                        .shuffleGrouping("REG-hor-Bolt-" + QUEUE_NAME)
                        .shuffleGrouping("REG-min-Bolt-" + QUEUE_NAME)
                        .shuffleGrouping("REG-min-pre-Bolt-" + QUEUE_NAME)
                        .shuffleGrouping("REG-cum-pre-Bolt-" + QUEUE_NAME);

            }
        }
        StormTopology stormTopology = builder.createTopology();
        Config conf = new Config();
        conf.setNumWorkers(workerNum);
        conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 200);
        conf.put(Config.TOPOLOGY_TRANSFER_BUFFER_SIZE, 32);
        conf.put(Config.TOPOLOGY_EXECUTOR_RECEIVE_BUFFER_SIZE, 16384);
        conf.put(Config.TOPOLOGY_EXECUTOR_SEND_BUFFER_SIZE, 16384);
        conf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 600);//240
        
        conf.put("configPath", path);
        conf.put("batchSize", batchSize);
        conf.put("horTable", horTable);
        conf.put("minTable", minTable);
        conf.put("minPreTable", minPreTable);
        conf.put("cumPreTable", cumPreTable);
        conf.put("repTable", repTable);
        conf.put("intervalTime", intervalTime);
        conf.put("dataBaseType", dataSource);
        conf.put("dataFlow", dataFlow);
        conf.put("cts_code", cts_code);
        conf.put("hor_sod_code", hor_sod_code);
        conf.put("mul_sod_code", mul_sod_code);
        conf.put("pre_sod_code", pre_sod_code);
        conf.put("accu_sod_code", accu_sod_code);
        conf.put("report_sod_code", report_sod_code);
        conf.put("diOption",diOption);
        conf.put("eiOption", diOption);
        conf.setNumAckers(ackers);
        return new Object[]{conf, stormTopology};
    }

}
