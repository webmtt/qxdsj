package cma.cimiss2.dpc.indb.storm.bufr;

import java.util.Properties;

import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.TopologyBuilder;

import cma.cimiss2.dpc.indb.storm.DIEIBolt;
import cma.cimiss2.dpc.indb.storm.bufr.mysql.BinaryDecodeAndInsertBolt;
import cma.cimiss2.dpc.indb.storm.bufr.mysql.FileDecodeAndInsertBolt;
import cma.cimiss2.dpc.indb.storm.tools.BinaryMessageScheme2;
import cma.cimiss2.dpc.indb.storm.tools.CustomDeclarator;
import cma.cimiss2.dpc.indb.storm.tools.MessageScheme;
import io.latent.storm.rabbitmq.Declarator;
import io.latent.storm.rabbitmq.RabbitMQSpout;
import io.latent.storm.rabbitmq.config.ConnectionConfig;
import io.latent.storm.rabbitmq.config.ConsumerConfig;
import io.latent.storm.rabbitmq.config.ConsumerConfigBuilder;
import org.apache.storm.Config;
import org.apache.storm.generated.StormTopology;
public class BufrTopology {

	private Properties prop;
	private String section;

	private String configPath;

	public String getConfigPath(){
		return this.configPath;
	}
	public BufrTopology() {
	}

	public BufrTopology(String path) {
		this.configPath = path;
	}

	public BufrTopology(String path, String section, Properties prop) {

		this.configPath = path;
		this.section = section;
		this.prop = prop;
	}

	public Object[] buildTopology()  {

		String host = prop.getProperty("rabbitMQ.host");
		String queueName = prop.getProperty("rabbitMQ.queueName");
		String exchangeName = prop.getProperty("rabbitMQ.exchangeName");
		String routingKey = prop.getProperty("rabbitMQ.routingKey");
		String user = prop.getProperty("rabbitMQ.user");
		String password = prop.getProperty("rabbitMQ.password");
		String port = prop.getProperty("rabbitMQ.port", "5671");
		int spoutNum = Integer.parseInt(prop.getProperty("storm.spout", "6"));
		int boltNum = Integer.parseInt(prop.getProperty("storm.bolt", "12"));
		int boltDIEI = Integer.parseInt(prop.getProperty("storm.DIEIbolt", "6"));
		int workerNum = Integer.parseInt(prop.getProperty("storm.worker", "3"));
		int ackerNum = Integer.parseInt(prop.getProperty("storm.acker", "3"));
		int prefetch = Integer.parseInt(prop.getProperty("storm.prefetch", "200"));
		
		boolean writeFile = Boolean.parseBoolean(prop.getProperty("message.writeFile", "false"));
		String writePath = prop.getProperty("message.writePath", "./");
		
		String dataBaseType = prop.getProperty("message.dataBaseType", "1");
		String fileLoop = prop.getProperty("message.fileloop", "0");
		
		Boolean diOption = Boolean.parseBoolean(prop.getProperty("di.option", "false"));
		Boolean eiOption = Boolean.parseBoolean(prop.getProperty("ei.option", "false"));
		String dataFlow = prop.getProperty("message.data_flow", "BDMAIN");
		int useBBB = Integer.parseInt(prop.getProperty("message.useBBB", "0"));
		
		// 创建rabbitmq spout
//		ConnectionConfig connConfig = new ConnectionConfig(host, user, password);
		ConnectionConfig connConfig = new ConnectionConfig(host, Integer.parseInt(port), user, password, "/", 60); // 单位秒（默认）
		Declarator decl = new CustomDeclarator(exchangeName, queueName, routingKey);

		ConsumerConfig spoutConfig = new ConsumerConfigBuilder().connection(connConfig).queue(queueName).prefetch(prefetch).requeueOnFail().build();
//		ConsumerConfig spoutConfig = new ConsumerConfigBuilder().connection(connConfig).queue(queueName).requeueOnFail().build();
		String msgType = prop.getProperty("messageType");
		// --创建TopologyBuilder类实例
		TopologyBuilder builder = new TopologyBuilder();
		
//		MessageScheme scheme = new MessageScheme();
//		IRichSpout spout = new RabbitMQSpout(scheme, decl);
//		builder.setSpout("BufrSpout-" + queueName, spout, spoutNum).addConfigurations(spoutConfig.asMap()).setMaxSpoutPending(200);
		if ("binary".equals(msgType)) {
			BinaryMessageScheme2 scheme = new BinaryMessageScheme2();
			IRichSpout spout = new RabbitMQSpout(scheme, decl);
			// 注册spout和bolt
			builder.setSpout("BufrSpout-" + queueName, spout, spoutNum).addConfigurations(spoutConfig.asMap()).setMaxSpoutPending(100).setNumTasks(spoutNum);
			builder.setBolt("Bufr-decode-Bolt-" + queueName, new BinaryDecodeAndInsertBolt(), boltNum).shuffleGrouping("BufrSpout-" + queueName).setNumTasks(boltNum);
			
		} 
		else {
			MessageScheme scheme = new MessageScheme();
			IRichSpout spout = new RabbitMQSpout(scheme, decl);
			// 注册spout和bolt
			builder.setSpout("BufrSpout-" + queueName, spout, spoutNum).addConfigurations(spoutConfig.asMap()).setMaxSpoutPending(100).setNumTasks(spoutNum);
			builder.setBolt("Bufr-decode-Bolt-" + queueName, new FileDecodeAndInsertBolt(), boltNum).shuffleGrouping("BufrSpout-" + queueName).setNumTasks(boltNum);
		}
		if (diOption || eiOption) {
            builder.setBolt("DIEIBolt-" + queueName, new DIEIBolt(), boltDIEI)
                    .shuffleGrouping("Bufr-decode-Bolt-" + queueName);
        }
		StormTopology stormTopology = builder.createTopology();

		Config conf = new Config();
		conf.setNumWorkers(workerNum);

		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 10000);
		conf.put(Config.TOPOLOGY_TRANSFER_BUFFER_SIZE, 32);
		conf.put(Config.TOPOLOGY_EXECUTOR_RECEIVE_BUFFER_SIZE, 16384);
		conf.put(Config.TOPOLOGY_EXECUTOR_SEND_BUFFER_SIZE, 16384);
		conf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 240);

		conf.put("section", section);
		conf.put("configPath", configPath);
		
		conf.put("writeFile", writeFile);
		conf.put("writePath", writePath);
		conf.put("dataBaseType", dataBaseType);
		conf.put("fileloop", fileLoop);
		
		conf.put("diOption", diOption);
		conf.put("eiOption", eiOption);
		conf.put("dataFlow", dataFlow);
		conf.put("useBBB", useBBB);
		
		conf.setNumAckers(ackerNum);
		return new Object[] { conf, stormTopology };
	}
}
