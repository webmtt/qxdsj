package com.piesat.common.kettle.environment;

import org.springframework.beans.factory.InitializingBean;

public class StartInit implements InitializingBean{

	@Override
	public void afterPropertiesSet() throws Exception {
		//初始化环境***
		com.piesat.common.kettle.environment.KettleInit.init();
//		KettleInit.environmentInit();
		org.pentaho.di.core.KettleEnvironment.init();
	}

}
