/*
 * @(#)ItemsIpNum.java 2016-4-14
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
public class ItemsIpNum implements Comparable<ItemsIpNum>{
	private String items;
	private int ipNum;
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public int getIpNum() {
		return ipNum;
	}
	public void setIpNum(int ipNum) {
		this.ipNum = ipNum;
	}
	public ItemsIpNum(String items, int ipNum) {
		super();
		this.items = items;
		this.ipNum = ipNum;
	}
	public ItemsIpNum() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int compareTo(ItemsIpNum itemsIpNum){
		return ipNum - itemsIpNum.getIpNum();
	}
}
