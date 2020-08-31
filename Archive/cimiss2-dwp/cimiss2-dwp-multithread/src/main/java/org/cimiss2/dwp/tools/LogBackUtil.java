package org.cimiss2.dwp.tools;

import java.io.File;
import java.io.IOException;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LogBackUtil {
	public static void loadLogBackConfig(String externalConfigFileLocation) throws IOException, JoranException {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		File externalConfigFile = new File(externalConfigFileLocation);
		if(!externalConfigFile.exists()) {
			throw new IOException("Logback External Config File Parameter does not reference a file that exists");
		}else {
			if(!externalConfigFile.isFile()) {
				throw new IOException("Logback External Config File Parameter exists, but does not reference a file");
			}else {
				if(!externalConfigFile.canRead()) {
					throw new IOException("Logback External Config File exists and is a file, but cannot be read.");
				}else {
					JoranConfigurator configurator = new JoranConfigurator();
					configurator.setContext(loggerContext);
					loggerContext.reset();
					configurator.doConfigure(externalConfigFileLocation);
					StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
				}
				
			}
		}
	}
}
