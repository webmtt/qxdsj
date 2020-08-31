package org.cimiss2.dwp.tools;

import org.cimiss2.dwp.tools.config.StartConfig;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;



public class LogRollingFileAppender extends RollingFileAppender<ILoggingEvent>{
	@Override
	public void start() {
		TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = (TimeBasedRollingPolicy<ILoggingEvent>) getRollingPolicy();
		if(getName().equalsIgnoreCase("dayValue")) {
			System.out.println(StartConfig.logProcessPath());
			rollingPolicy.setFileNamePattern(StartConfig.logProcessPath());
			this.setRollingPolicy(rollingPolicy);
			rollingPolicy.start();
			System.out.println(rollingPolicy.getActiveFileName());
//			this.setFile(rollingPolicy.getActiveFileName());
		}else {
			rollingPolicy.setFileNamePattern(StartConfig.logMessagePath());
			this.setRollingPolicy(rollingPolicy);
			rollingPolicy.start();
			System.out.println(rollingPolicy.getActiveFileName());
//			this.setFile(rollingPolicy.getActiveFileName());
		}
		super.start();
	}
	
	
	

}
