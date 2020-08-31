/**  
 * All rights Reserved, Designed By www.huaxin-hitec.com
 * @Title:  SateFileDI.java   
 * @Package cma.cimiss2.dpc.decoder.tools.common   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: 吴佐强     
 * @date:   2018年12月26日 上午9:00:22   
 * @version V1.0 
 * @Copyright: 2018 www.huaxin-hitec.com Inc. All rights reserved. 
 * 注意：本内容仅限于华云信息技术工程有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cma.cimiss2.dpc.indb.framework.di.bean;

/**  
 * *********************************************************************** 
 * @ClassName:  SateFileDI   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 吴佐强
 * @date:   2018年12月26日 上午9:00:22   
 *     
 * @Copyright: 2018 www.huaxin-hitec.com Inc. All rights reserved. 
 * 注意：本内容仅限于华云信息技术工程有限公司内部传阅，禁止外泄以及用于其他的商业目 
 * ***********************************************************************
 */
public class SateFileDI extends FileDi implements Cloneable{
	/** 
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	 */ 
	private static final long serialVersionUID = 1L;
	/**
	 * 	有卫星名称时填写，没有不用体现本字段
	 */
	private String SATE_NAME;
	/**
	 * 有卫星仪器名称时填写本字段，没有不用体现本字段
	 */
	private String SATE_DEV_NAME;
	/**
	 * 有卫星仪器通道名称时填写本字段，没有不用体现本字段
	 */
	private String SATE_DEVCHA_NAME;
	/**
	 * 	有产品标识时填写本字段，没有不用体现本字段
	 */
	private String PROD_NAME;
	
	public String getSATE_NAME() {
		return SATE_NAME;
	}
	
	public void setSATE_NAME(String sATE_NAME) {
		SATE_NAME = sATE_NAME;
	}
	
	public String getSATE_DEV_NAME() {
		return SATE_DEV_NAME;
	}
	
	public void setSATE_DEV_NAME(String sATE_DEV_NAME) {
		SATE_DEV_NAME = sATE_DEV_NAME;
	}
	
	public String getPROD_NAME() {
		return PROD_NAME;
	}
	
	public void setPROD_NAME(String pROD_NAME) {
		PROD_NAME = pROD_NAME;
	}

	public String getSATE_DEVCHA_NAME() {
		return SATE_DEVCHA_NAME;
	}

	public void setSATE_DEVCHA_NAME(String sATE_DEVCHA_NAME) {
		SATE_DEVCHA_NAME = sATE_DEVCHA_NAME;
	}
	
	@Override
	public SateFileDI clone() throws CloneNotSupportedException {
		SateFileDI sateFileDI = null;  
        try{  
        	sateFileDI = (SateFileDI)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return sateFileDI;  

	}
	
	
}
