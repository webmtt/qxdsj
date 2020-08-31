package com.thinkgem.jeesite.modules.portal.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="MEETING_TYPE")
public class MeetingType {
	@Id
	private int id;
	private String type_Name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeName() {
		return type_Name;
	}

	public void setTypeName(String typeName) {
		this.type_Name = typeName;
	}
}
