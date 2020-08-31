package com.thinkgem.jeesite.modules.access.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PORTAL_IPINFOR")
public class PortalIPInfor {
	private int id;
	private String ipinfor;
	private String proname;
	private String router;
	private String enname;

	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIpinfor() {
		return ipinfor;
	}

	public void setIpinfor(String ipinfor) {
		this.ipinfor = ipinfor;
	}

	public String getProname() {
		return proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}

	public String getRouter() {
		return router;
	}

	public void setRouter(String router) {
		this.router = router;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}
	
	
}
