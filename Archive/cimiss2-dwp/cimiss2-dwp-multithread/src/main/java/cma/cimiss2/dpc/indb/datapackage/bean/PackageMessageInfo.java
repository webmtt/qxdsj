package cma.cimiss2.dpc.indb.datapackage.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="root")
public class PackageMessageInfo {
	
	private RequestInfo requestInfo;
	private ReponseInfo reponseInfo;
	public RequestInfo getRequestInfo() {
		return requestInfo;
	}
	
	@XmlElement(name="requestInfo")
	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}
	public ReponseInfo getReponseInfo() {
		return reponseInfo;
	}
	@XmlElement(name="reponseInfo")
	public void setReponseInfo(ReponseInfo reponseInfo) {
		this.reponseInfo = reponseInfo;
	}
	
	

}
