package cma.cimiss2.dpc.decoder.bean.sand;

import java.util.Date;

import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
/**
 * 
 * <br>
 * @Title:  SandDds.java
 * @Package cma.cimiss2.dpc.decoder.bean.sand.dds
 * @Description:    (沙尘暴大气降尘总量（DDS）实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月18日 下午5:35:42   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class SandDds extends AgmeReportHeader {
	/**
	 * @Fields stationNumberChina : 区站号(字符) 
	 */
	private String stationNumberChina ;

	/**
	 * @Fields stationHeight : 观测时间 
	 */
	private Date observationTime;
	
	/**
	 * @Fields stationHeight : 观测时间间隔
	 */
	private double observationInterval=999999.0;
	
	/**
	 * @Fields stationHeight : 采样膜编号
	 */
	private  double SampMe_Num;
	
	/**
	 * @Fields stationHeight :开始时间
	 */
	private  String Stime;
	
	/**
	 * @Fields stationHeight :结束时间
	 */
	private  String Etime;
	
	/**
	 * @Fields stationHeight : 集尘缸编号
	 */
	private  double DustCol_Num;
	
	/**
	 * @Fields stationHeight : 集尘缸口面积
	 */
	private  double DustCol_Area;
	
	/**
	 * @Fields stationHeight :硫酸铜加入量
	 */
	private  double CUSO4_Wei;
	
	/**
	 * @Fields stationHeight :采样累积天数
	 */
	private  double accumulateDays;
	
	/**
	 * @Fields stationHeight 平均气压
	 */
	private  double PRS_Avg;
	
	/**
	 * @Fields stationHeight 平均气温
	 */
	private  double TEM_Avg;
	
	/**
	 * @Fields stationHeight 最终样品重量
	 */
	private  double Samp_Wei;
	
	/**
	 * @Fields stationHeight 大气降尘量
	 */
	private  double DustDep_Atsph;

	public String getStationNumberChina() {
		return stationNumberChina;
	}

	public void setStationNumberChina(String stationNumberChina) {
		this.stationNumberChina = stationNumberChina;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	public double getObservationInterval() {
		return observationInterval;
	}

	public void setObservationInterval(double observationInterval) {
		this.observationInterval = observationInterval;
	}

	public double getSampMe_Num() {
		return SampMe_Num;
	}

	public void setSampMe_Num(double sampMe_Num) {
		SampMe_Num = sampMe_Num;
	}

	public String getStime() {
		return Stime;
	}

	public void setStime(String stime) {
		Stime = stime;
	}

	public String getEtime() {
		return Etime;
	}

	public void setEtime(String etime) {
		Etime = etime;
	}

	public double getDustCol_Num() {
		return DustCol_Num;
	}

	public void setDustCol_Num(double dustCol_Num) {
		DustCol_Num = dustCol_Num;
	}

	public double getDustCol_Area() {
		return DustCol_Area;
	}

	public void setDustCol_Area(double dustCol_Area) {
		DustCol_Area = dustCol_Area;
	}

	public double getCUSO4_Wei() {
		return CUSO4_Wei;
	}

	public void setCUSO4_Wei(double cUSO4_Wei) {
		CUSO4_Wei = cUSO4_Wei;
	}

	public double getAccumulateDays() {
		return accumulateDays;
	}

	public void setAccumulateDays(double accumulateDays) {
		this.accumulateDays = accumulateDays;
	}

	public double getPRS_Avg() {
		return PRS_Avg;
	}

	public void setPRS_Avg(double pRS_Avg) {
		PRS_Avg = pRS_Avg;
	}

	public double getTEM_Avg() {
		return TEM_Avg;
	}

	public void setTEM_Avg(double tEM_Avg) {
		TEM_Avg = tEM_Avg;
	}

	public double getSamp_Wei() {
		return Samp_Wei;
	}

	public void setSamp_Wei(double samp_Wei) {
		Samp_Wei = samp_Wei;
	}

	public double getDustDep_Atsph() {
		return DustDep_Atsph;
	}

	public void setDustDep_Atsph(double dustDep_Atsph) {
		DustDep_Atsph = dustDep_Atsph;
	}
	
	

}
