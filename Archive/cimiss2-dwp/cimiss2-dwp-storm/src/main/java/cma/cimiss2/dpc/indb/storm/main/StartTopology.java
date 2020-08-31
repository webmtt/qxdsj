package cma.cimiss2.dpc.indb.storm.main;

import cma.cimiss2.dpc.indb.storm.dc_surf_aws_babj.BABJTopology;
import cma.cimiss2.dpc.indb.storm.dc_surf_aws_reg.REGTopology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.nutz.ioc.Ioc;

public class StartTopology {
   public static Ioc ioc;

    /**
     * @param args args0:拓扑名
     *             Z_SURF_BABJ  国家站小时,分钟数据
     *             Z_SURF_REG   区域站小时,分钟数据
     *             Z_SURF_PERF   雨量小时,分钟数据
     *             args1：config文件路径
     *             例如： /home/dpc/storm/lib/config/  最后要加/
     * @description 启动拓扑主程序
     */
    public static void main(String[] args) {
        Object[] convert = null;
        try {
            String name = args[0];//资料类型判断
            String path = args[1];//配置文件路径
            String localOrCluster = args[2];// 集群模式和本地模式选择 local
//            ioc = IocBuilder.ioc(path);
            switch (name) {

                case "Z_SURF_BABJ":
                    convert = new BABJTopology(path).buildTopology();
                    break;
                case "Z_SURF_REG":
                    convert = new REGTopology(path).buildTopology();
                    break;
//                case "Z_SURF_PERF":
//                    convert = new PERFTopology(path).buildTopology();
//                    break;
                default:
                    System.out.println("名字错误，没有找到对应拓扑。。。");
                    return;
            }
            Config conf = (Config) convert[0];
            StormTopology stormTopology = (StormTopology) convert[1];
            System.out.println("=========================  " + name + "运行 。。。 配置文件路径：" + path + "  ========================================");
            if (args.length > 2 && "local".equals(localOrCluster)) {//本地模式
                conf.setMaxTaskParallelism(3);
                LocalCluster cluster = new LocalCluster();
                cluster.submitTopology("test", conf, stormTopology);
            } else {//集群模式
                StormSubmitter.submitTopology(name, conf, stormTopology);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
