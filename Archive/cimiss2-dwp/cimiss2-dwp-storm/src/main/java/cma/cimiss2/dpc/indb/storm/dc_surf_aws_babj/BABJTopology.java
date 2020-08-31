package cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj;

import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.core.tools.StormConfig;
import cma.cimiss2.dpc.indb.storm.DIEIBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.mysql.MysqGlblHorBolt;
//import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.mysql.MysqGlblHorBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.mysql.MysqlDecodeBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.mysql.MysqlHorBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.mysql.MysqlMinBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.mysql.MysqlRepBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.mysql.MysqlWeatherBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.mysql.mysqlCumPreBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.mysql.mysqlMinPreBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.ots.otsDecodeBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.ots.otsHorBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.ots.otsMinBolt;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.ots.otsRepBolt;
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

import com.rabbitmq.client.ConnectionFactory;

/**
 * 国家站拓扑配置
 * Created by xzh on 2017/07/31.
 */
public class BABJTopology {
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
    private int workerNum;
    private int ackers;
    private int queueNum;
    private String batchSize;
    private String horTable;
    // 2019-11-4 chy
    private String glbHorTable;
    // 2020-3-3 chy
    private String weatherTable;
    
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
    
    // 2019-11-4
    private String glb_hor_sod_code;
    //2020-3-3 chy
    private String weather_sod_code;
    
    private String mul_sod_code;
    private String pre_sod_code;
    private String accu_sod_code;
    private String report_sod_code;
    public static Map<String, Map<String, String>> map;

    public BABJTopology(String path) {
        this.path = path;
        ConfigurationManager.loadPro(path, "db.properties");
        ConfigurationManager.loadPro(path, "dpc_global_config.properties");
        cma.cimiss2.dpc.indb.core.ots.Config.endpoint= ConfigurationManager.getString("ots.url");
        cma.cimiss2.dpc.indb.core.ots.Config.accessId= ConfigurationManager.getString("ots.accessId");
        cma.cimiss2.dpc.indb.core.ots.Config.accessKey= ConfigurationManager.getString("ots.accessKey");
        cma.cimiss2.dpc.indb.core.ots.Config.instance= ConfigurationManager.getString("ots.instance");
        map = StormConfig.getConfig(path + "BABJConfig.xml").getMaps();
        queueNum = Integer.valueOf(map.get("rabbitmq").get("num"));
        batchSize = map.get("base").get("BATCHSIZE");
        horTable = map.get("base").get("TABLE_HOR");
        // 2019-11-4 chy
        glbHorTable = map.get("base").get("TABLE_GLB_HOR");
        // 2020-3-3 chy
        weatherTable = map.get("base").get("TABLE_WEATHER");
        
        minTable = map.get("base").get("TABLE_MIN");
        minPreTable = map.get("base").get("TABLE_MIN_PRE");
        cumPreTable = map.get("base").get("TABLE_CUM_PRE");
        repTable = map.get("base").get("TABLE_REP");
        intervalTime = map.get("base").get("INTERVALTIME");
        dataSource = map.get("base").get("DATASOURCE");
        dataFlow = map.get("base").get("DATA_FLOW");
        cts_code = map.get("base").get("CTS_CODE");
        hor_sod_code = map.get("base").get("HOR_SOD_CODE");
        // 2019-11-4 chy
        glb_hor_sod_code = map.get("base").get("GLB_HOR_SOD_CODE");
        // 2020-3-3 chy
        weather_sod_code = map.get("base").get("WEATHER_SOD_CODE");
        
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
            //创建rabbitmq spout
            ConnectionConfig connConfig = new ConnectionConfig(RABBITMQ_HOST, RABBITMQ_PORT,RABBITMQ_USER, RABBITMQ_PASS,ConnectionFactory.DEFAULT_VHOST,60);
            Declarator decl = new CustomDeclarator(EXCHANGE_NAME, QUEUE_NAME);
            ConsumerConfig spoutConfig = new ConsumerConfigBuilder().connection(connConfig)
                    .queue(QUEUE_NAME)
                    .prefetch(200)
                    .requeueOnFail()
                    .build();

            IRichSpout spout = new RabbitMQSpout(scheme, decl);
            //注册spout和bolt
            builder.setSpout("BABJSpout-" + QUEUE_NAME, spout, spoutNum)
                    .addConfigurations(spoutConfig.asMap())
                    .setMaxSpoutPending(100);//不管用
            switch (dataSource) {
                case "2":
                    builder.setBolt("BABJ-decode-Bolt-" + QUEUE_NAME, new otsDecodeBolt(), boltNum).shuffleGrouping("BABJSpout-" + QUEUE_NAME);
                    builder.setBolt("BABJ-hor-Bolt-" + QUEUE_NAME, new otsHorBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-HOR-Bolt");
                    builder.setBolt("BABJ-min-Bolt-" + QUEUE_NAME, new otsMinBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-MIN-Bolt");
                    builder.setBolt("BABJ-rep-Bolt-" + QUEUE_NAME, new otsRepBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-REP-Bolt");
                    break;
                case "1":
                    builder.setBolt("BABJ-decode-Bolt-" + QUEUE_NAME, new MysqlDecodeBolt(), boltNum).shuffleGrouping("BABJSpout-" + QUEUE_NAME);
                    builder.setBolt("BABJ-hor-Bolt-" + QUEUE_NAME, new MysqlHorBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-HOR-Bolt");
                    builder.setBolt("BABJ-min-Bolt-" + QUEUE_NAME, new MysqlMinBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-MIN-Bolt");
                    builder.setBolt("BABJ-min-pre-Bolt-" + QUEUE_NAME, new mysqlMinPreBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-MIN-PRE-Bolt");
                    builder.setBolt("BABJ-cum-pre-Bolt-" + QUEUE_NAME, new mysqlCumPreBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-CUM-PRE-Bolt");
                    builder.setBolt("BABJ-rep-Bolt-" + QUEUE_NAME, new MysqlRepBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-REP-Bolt");
                    // 2019-11-4 chy
                    builder.setBolt("BABJ-glb-hor-Bolt-" + QUEUE_NAME, new MysqGlblHorBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-GLB-HOR-Bolt");
                    // 2020-3-3  chy
                    builder.setBolt("BABJ-weather-Bolt-" + QUEUE_NAME, new MysqlWeatherBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-WEATHER-Bolt");
                    
                    break;
                case "0":
                    builder.setBolt("BABJ-decode-Bolt-" + QUEUE_NAME, new MysqlDecodeBolt(), boltNum).shuffleGrouping("BABJSpout-" + QUEUE_NAME);
                    builder.setBolt("BABJ-hor-Bolt-" + QUEUE_NAME, new MysqlHorBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-HOR-Bolt");
                    builder.setBolt("BABJ-min-Bolt-" + QUEUE_NAME, new MysqlMinBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-MIN-Bolt");
                    builder.setBolt("BABJ-rep-Bolt-" + QUEUE_NAME, new MysqlRepBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-REP-Bolt");
                 // 2019-11-4 chy
//                    builder.setBolt("BABJ-glb-hor-Bolt-" + QUEUE_NAME, new MysqGlblHorBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-GLB-HOR-Bolt");
                   // 2020-3-3 chy
                    builder.setBolt("BABJ-weather-Bolt-" + QUEUE_NAME, new MysqlWeatherBolt(), boltNum).shuffleGrouping("BABJ-decode-Bolt-" + QUEUE_NAME, "BABJ-WEATHER-Bolt");
                    break;
            }


            if (DIEISender.GLOBAL_DI_OPTION && DIEISender.LOCAL_DI_OPTION
                    || DIEISender.GLOBAL_EI_OPTION && DIEISender.LOCAL_EI_OPTION) {
                builder.setBolt("DIEIBolt-" + QUEUE_NAME, new DIEIBolt(), DIEIboltNum)
                        .shuffleGrouping("BABJ-hor-Bolt-" + QUEUE_NAME)
                        .shuffleGrouping("BABJ-min-Bolt-" + QUEUE_NAME)
                        .shuffleGrouping("BABJ-cum-pre-Bolt-" + QUEUE_NAME)
                        .shuffleGrouping("BABJ-min-pre-Bolt-" + QUEUE_NAME)
                        .shuffleGrouping("BABJ-glb-hor-Bolt-" + QUEUE_NAME)
                		.shuffleGrouping("BABJ-weather-Bolt-" + QUEUE_NAME); // 2020-3-3
            }
        }

        StormTopology stormTopology = builder.createTopology();

        Config conf = new Config();
        conf.setNumWorkers(workerNum);

        conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 200);
        conf.put(Config.TOPOLOGY_TRANSFER_BUFFER_SIZE, 32);
        conf.put(Config.TOPOLOGY_EXECUTOR_RECEIVE_BUFFER_SIZE, 16384);
        conf.put(Config.TOPOLOGY_EXECUTOR_SEND_BUFFER_SIZE, 16384);
        conf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 240);
        conf.put("configPath", path);
        conf.put("batchSize", batchSize);
        conf.put("horTable", horTable);
        // 2019-11-4 chy
        conf.put("glbHorTable", glbHorTable);
        // 2020-3-3 chy
        conf.put("weatherTable", weatherTable);
        
        conf.put("minTable", minTable);
        conf.put("minPreTable", minPreTable);
        conf.put("cumPreTable", cumPreTable);
        conf.put("repTable", repTable);
        conf.put("intervalTime", intervalTime);
        conf.put("dataBaseType", dataSource);
        conf.put("dataFlow", dataFlow);
        conf.put("cts_code", cts_code);
        conf.put("hor_sod_code", hor_sod_code);
        
        // 2019-11-4 chy
        conf.put("glb_hor_sod_code", glb_hor_sod_code);
        // 2020-3-3 chy
        conf.put("weather_sod_code", weather_sod_code);
        
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
