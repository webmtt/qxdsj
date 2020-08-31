package com.thinkgem.jeesite.modules.index.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "SUP_SEARCHKEYINFO")
public class SearchKeyInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String searchkey;
	private int searchcolumnnum;
	private int searchnum;
	private int searchsumnum;
	private int invalid;
	@Id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSearchkey() {
		return searchkey;
	}
	public void setSearchkey(String searchkey) {
		this.searchkey = searchkey;
	}
	public int getSearchcolumnnum() {
		return searchcolumnnum;
	}
	public void setSearchcolumnnum(int searchcolumnnum) {
		this.searchcolumnnum = searchcolumnnum;
	}
	public int getSearchnum() {
		return searchnum;
	}
	public void setSearchnum(int searchnum) {
		this.searchnum = searchnum;
	}
	public int getSearchsumnum() {
		return searchsumnum;
	}
	public void setSearchsumnum(int searchsumnum) {
		this.searchsumnum = searchsumnum;
	}
	public int getInvalid() {
		return invalid;
	}
	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}
	@Override
	public String toString() {
		return "SearckKeyInfo [id=" + id + ", searchkey=" + searchkey + ", searchcolumnnum=" + searchcolumnnum
				+ ", searchnum=" + searchnum + ", searchsumnum=" + searchsumnum + ", invalid=" + invalid + "]";
	}
	


	

	
}
