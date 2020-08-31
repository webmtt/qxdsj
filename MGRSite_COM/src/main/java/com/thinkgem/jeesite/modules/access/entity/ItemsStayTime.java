/*
 * @(#)ItemsStayTime.java 2016-4-14
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.access.entity;

/**
 * 描述：
 *
 * @author oytf
 * @version 1.0 2016-4-14
 */
public class ItemsStayTime implements Comparable<ItemsStayTime>{
	private String items;
	private int stayTime;
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public int getStayTime() {
		return stayTime;
	}
	public void setStayTime(int stayTime) {
		this.stayTime = stayTime;
	}
	public ItemsStayTime(String items, int stayTime) {
		super();
		this.items = items;
		this.stayTime = stayTime;
	}
	public ItemsStayTime() {
		super();
		// TODO Auto-generated constructor stub
	} 
	public int compareTo(ItemsStayTime itemsStayTime){
		return stayTime - itemsStayTime.getStayTime();
	}
}
