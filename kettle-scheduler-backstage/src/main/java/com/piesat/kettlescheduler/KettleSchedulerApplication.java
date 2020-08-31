package com.piesat.kettlescheduler;

import com.piesat.kettlescheduler.kettle.environment.StartInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KettleSchedulerApplication {

	/*public static void main(String[] args) {
		SpringApplication.run(KettleSchedulerApplication.class, args);
	}*/

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(KettleSchedulerApplication.class);
		springApplication.addInitializers(new StartInitializer());
		springApplication.run(args);
	}

}
