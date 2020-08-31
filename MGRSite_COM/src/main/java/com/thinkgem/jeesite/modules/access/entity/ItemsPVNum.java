/*
 * @(#)ItemsPVNum.java 2016-4-14
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.access.entity;





/**
 * 描述：包含item,pvnum属性
 *
 * @author oytf
 * @version 1.0 2016-4-14
 */
public class ItemsPVNum implements Comparable<ItemsPVNum>{
	String item;
	int pvnum;
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public int getPvnum() {
		return pvnum;
	}
	public void setPvnum(int pvnum) {
		this.pvnum = pvnum;
	}
	public ItemsPVNum(String item, int pvnum) {
		super();
		this.item = item;
		this.pvnum = pvnum;
	}
	public ItemsPVNum() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int compareTo(ItemsPVNum itemsPVNum){
		return pvnum - itemsPVNum.getPvnum();
	}
}
