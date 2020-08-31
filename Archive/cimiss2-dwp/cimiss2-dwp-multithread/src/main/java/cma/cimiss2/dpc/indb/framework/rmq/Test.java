package cma.cimiss2.dpc.indb.framework.rmq;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeoutException;

import org.cimiss2.dwp.tools.config.StartConfig;
// 测试类
public class Test {

	public static void main(String[] args) {
		// 按照应用程序启动规范 需要传入两个参数
		// 第一个参数为 应用程序的端口号 是应用程序的唯一标识 在对应的日志文件名中也带有该端口
		// 第二个参数为 应用程序专有配置文件的绝对路径或者相对路径 
		// 一般配置文件按照应用程序部署规范存放在对应目录 可以传相对路径
		int port = 9999;
		if(args.length < 2) {
			System.out.println("Please at least two parameters");
			return;
		}
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.out.println("The first parameter must be the port number");
			return;
		}
		// 判断配置文件是否存在
		File file = new File(args[1].trim());
		if(!file.exists() || !file.isFile()) {
			System.out.println("not find " + args[1].trim());
			return;
		}
		// 判断应用程序端口号是否被暂用
		try {
			ServerSocket serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			
			return;
		}
		StartConfig.setConfigFile(args[1].trim());
		StartConfig.runport = port;
		
		UserMessageProcess process = new UserMessageProcess();
		ThreadPoolConsumer threadPoolConsumer = new ThreadPoolConsumer.ThreadPoolConsumerBuilder()
				.setMessageProcess(process)
				.build();
		try {
			threadPoolConsumer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
