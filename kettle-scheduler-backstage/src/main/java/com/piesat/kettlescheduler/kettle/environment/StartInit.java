package com.piesat.kettlescheduler.kettle.environment;

import org.springframework.beans.factory.InitializingBean;

public class StartInit implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		//初始化环境***
		KettleInit.init();
//		KettleInit.environmentInit();
		org.pentaho.di.core.KettleEnvironment.init(false);
	}

}
