package cma.cimiss2.dpc.indb.storm.main;

import java.util.Map;
import java.util.Properties;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;

import cma.cimiss2.dpc.indb.storm.bufr.BufrTopology;
import cma.cimiss2.dpc.indb.storm.tools.ReadMqIni;

public class StartBufrTopology {

	/**
	 * @param args args0:拓扑名
	 *             args1：config文件路径
	 *             例如： /home/dpc/storm/lib/config/  最后要加/
	 * @description 启动拓扑主程序
	 */
	public static void main(String[] args) {

		try {
			String name = args[0];// 资料类型判断
			String path = args[1];// 配置文件路径
			String localOrCluster = args[2];// 集群模式和本地模式选择 local
			
			String topyName = name; //缺省拓扑名
			int argsLength = args.length;
			if(argsLength >= 4){
				topyName = args[3].trim(); //拓扑名字
			}
			if(argsLength < 2) {
				System.out.println("请配置正确的启动参数:\n"	//
						+ "  资料类型(与table_config.ini中的section一致) 配置文件路径 集群模式(cluster)或本地模式(local) mq消息类型binary/file");
				return;
			} else if(argsLength < 3) {
				System.out.println("请配置正确的启动参数:\n"	//
						+ "  资料类型(与table_config.ini中的section一致) 配置文件路径 集群模式(cluster)或本地模式(local) mq消息类型binary/file");
				return;
			}
			ReadMqIni.init(path);
			ReadMqIni mqIni = ReadMqIni.getIni();
			Map<String, Properties> mqMap = mqIni.get();

			Properties prop = mqMap.get(name);
			// 2020-4-3 chy
//			if(args.length < 4) {
			if(args.length < 5){
				System.out.println("没有填写mq消息类型,默认以binary(二进制数据流格式解析)");
				prop.put("messageType", "binary");
			} else {
				prop.put("messageType", args[4]);//binary/file
			}

			// mqMap.forEach((k, v) -> {

			Object[] convert = new BufrTopology(path, name, prop).buildTopology();
			Config conf = (Config) convert[0];
			StormTopology stormTopology = (StormTopology) convert[1];
			System.out.println("=========================  " + name + "运行 。。。 配置文件路径：" + path + "  ========================================");
			if (args.length > 2 && "local".equals(localOrCluster)) {// 本地模式
				conf.setMaxTaskParallelism(3);
				LocalCluster cluster = new LocalCluster();
				cluster.submitTopology("localTest", conf, stormTopology);
			} else {// 集群模式
				try {
					StormSubmitter.submitTopology(topyName, conf, stormTopology);
				} catch (AlreadyAliveException e) {
					e.printStackTrace();
				} catch (InvalidTopologyException e) {
					e.printStackTrace();
				} catch (AuthorizationException e) {
					e.printStackTrace();
				}
			}
			// });

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
