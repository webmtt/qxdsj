package com.thinkgem.jeesite.modules.portal.entity;


import javax.persistence.*;

@Entity
@Table(name="MEETING_INFO")
public class MeetingInfo{

	private int id;
	
	private int tid;
	
	private long invalid;
	
	private String startDate;
	
	private String place;
	
	private String endDate;
	
	private String host;
	
	private String organizer;
	
	private String participants;
	
	private String name;
	
	private String abstracts;
	
//	@Column(name="DOWNLOAD_ENTRANCE")
//	private String download_entrance;
	
	private String thumbnail;
	
//	@Transient
//	private String time;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public String getParticipants() {
		return participants;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

/*	public String getDownload_entrance() {
		return download_entrance;
	}

	public void setDownload_entrance(String download_entrance) {
		this.download_entrance = download_entrance;
	}*/

	public String getThumbnail() {
		return thumbnail;
	}

	public long getInvalid() {
		return invalid;
	}

	public void setInvalid(long invalid) {
		this.invalid = invalid;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

//	public String getTime() {
//		return time;
//	}
//
//	public void setTime(String time) {
//		this.time = time;
//	}

}
