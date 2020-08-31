package cma.cimiss2.dpc.decoder.bean.detection;

import java.util.Date;

import cma.cimiss2.dpc.decoder.annotation.Column;
/**
 * 
 * <br>
 * @Title:  RadiMulChn.java
 * @Package cma.cimiss2.dpc.decoder.bean.radi.sads
 * @Description:    (检测评估结果公共实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月20日 下午2:18:51   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class RadiMulChn {
	

	/**
	 * @Fields fcstLead : 资料时间
	 */
	@Column(value = "D_DATETIME")
	private Date datetime ;
	/**
	 * @Fields fcstLead : 资料观测年
	 */
	@Column(value = "V04001")
	private Long dateYear ;
	/**
	 * @Fields fcstLead : 资料观测月
	 */
	@Column(value = "V04002")
	private Long dateMonth ;
	/**
	 * @Fields fcstLead : 资料观测日
	 */
	@Column(value = "V04003")
	private Long dateDay ;
	/**
	 * @Fields fcstLead : 资料观测时
	 */
	@Column(value = "V04004")
	private Long dateHour ;
	
	/**
	 * @Fields version : 版本号
	 */
	@Column(value = "V_ VERSION")
	private String version ;
	
	/**
	 * @Fields model : 用户提供的模式字符串名称
	 */
	@Column(value = "V_MODEL")
	private String model ;
	
	/**
	 * @Fields fcstLead : 预报的lead time
	 */
	@Column(value = "V_FCSTTIME")
	private Long fcstTime ;
	
	/**
	 * @Fields fcstValidBeg : 预报的vaild起始时间
	 */
	@Column(value = "V_FBEGTIME")
	private Date fBegTime ;
	
	/**
	 * @Fields fcstValidEnd : 预报的vaild终止时间
	 */
	@Column(value = "V_FENDTIME")
	private Date fEndTime ;
	
	/**
	 * @Fields obsLead :观测的lead time
	 */
	@Column(value = "V_OBSTIME")
	private Long obsTime;
	
	/**
	 * @Fields obsValidBeg : 观测的vaild起始时间
	 */
	@Column(value = "V_OBEGTIME")
	private Date oBegTime ;
	
	/**
	 * @Fields obsValidEnd : 观测的vaild终止时间
	 */
	@Column(value = "V_OENDTIME")
	private Date oEndTime;
	
	/**
	 * @Fields fcstVar : 模式变量
	 */
	@Column(value = "V_FCSTVAR")
	private String fcstVar;
	
	/**
	 * @Fields fcstLev : 选择预报变量的层
	 */
	@Column(value = "V_FCSTLEV")
	private String fcstLev;
	
	/**
	 * @Fields obsVar : 观测变量
	 */
	@Column(value = "V_OBSVAR")
	private String obsVar;
	
	/**
	 * @Fields obsLev : 选择观测变量的层
	 */
	@Column(value = "V_OBSLEV")
	private String obsLev;
	

	/**
	 * @Fields obtype : 观测信息类型选择
	 */
	@Column(value = "V_OBTYPE")
	private String obtype;
	
	/**
	 * @Fields vxMask : 验证指示应用了mask网格或多段线区域的mask区域
	 */
	@Column(value = "V_VXMASK")
	private String vxMask;
	
	/**
	 * @Fields interpMthd : 预报使用的插值方法
	 */
	@Column(value = "V_MTHD")
	private String mthd;
	
	/**
	 * @Fields interpPnts : 插值使用的点数
	 */
	@Column(value = "V_PNTS")
	private Double pnts;
	
	/**
	 * @Fields fcstThresh : 预报使用的阈值
	 */
	@Column(value = "V_FTHRESH")
	private String fThresh;
	
	/**
	 * @Fields obsThresh :观测使用的阈值
	 */
	@Column(value = "V_OTHRESH")
	private String oThresh;
	
	/**
	 * @Fields COV_THRESH : Point-Stat中的NA
	 */
	@Column(value = "V_CTHRESH")
	private String cThresh ;
	
	/**
	 * @Fields 	alpha : 置信区间中使用的误差百分比值
	 */
	@Column(value = "V_ALPHA")
	private String alpha ;
	
	/**
	 * @Fields lineType : 输出行类型
	 */
	@Column(value = "V_LINETYPE")
	private String lineType ;
	


	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Long getFcstTime() {
		return fcstTime;
	}

	public void setFcstTime(Long fcstTime) {
		this.fcstTime = fcstTime;
	}

	public Date getfBegTime() {
		return fBegTime;
	}

	public void setfBegTime(Date fBegTime) {
		this.fBegTime = fBegTime;
	}

	public Date getfEndTime() {
		return fEndTime;
	}

	public void setfEndTime(Date fEndTime) {
		this.fEndTime = fEndTime;
	}

	public Long getObsTime() {
		return obsTime;
	}

	public void setObsTime(Long obsTime) {
		this.obsTime = obsTime;
	}

	public Date getoBegTime() {
		return oBegTime;
	}

	public void setoBegTime(Date oBegTime) {
		this.oBegTime = oBegTime;
	}

	public Date getoEndTime() {
		return oEndTime;
	}

	public void setoEndTime(Date oEndTime) {
		this.oEndTime = oEndTime;
	}

	public String getFcstVar() {
		return fcstVar;
	}

	public void setFcstVar(String fcstVar) {
		this.fcstVar = fcstVar;
	}

	public String getFcstLev() {
		return fcstLev;
	}

	public void setFcstLev(String fcstLev) {
		this.fcstLev = fcstLev;
	}

	public String getObsVar() {
		return obsVar;
	}

	public void setObsVar(String obsVar) {
		this.obsVar = obsVar;
	}

	public String getObsLev() {
		return obsLev;
	}

	public void setObsLev(String obsLev) {
		this.obsLev = obsLev;
	}

	public String getObtype() {
		return obtype;
	}

	public void setObtype(String obtype) {
		this.obtype = obtype;
	}

	public String getVxMask() {
		return vxMask;
	}

	public void setVxMask(String vxMask) {
		this.vxMask = vxMask;
	}

	public String getMthd() {
		return mthd;
	}

	public void setMthd(String mthd) {
		this.mthd = mthd;
	}

	public Double getPnts() {
		return pnts;
	}

	public void setPnts(Double pnts) {
		this.pnts = pnts;
	}

	public String getfThresh() {
		return fThresh;
	}

	public void setfThresh(String fThresh) {
		this.fThresh = fThresh;
	}

	public String getoThresh() {
		return oThresh;
	}

	public void setoThresh(String oThresh) {
		this.oThresh = oThresh;
	}

	public String getcThresh() {
		return cThresh;
	}

	public void setcThresh(String cThresh) {
		this.cThresh = cThresh;
	}

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public Long getDateYear() {
		return dateYear;
	}

	public void setDateYear(Long dateYear) {
		this.dateYear = dateYear;
	}

	public Long getDateMonth() {
		return dateMonth;
	}

	public void setDateMonth(Long dateMonth) {
		this.dateMonth = dateMonth;
	}

	public Long getDateDay() {
		return dateDay;
	}

	public void setDateDay(Long dateDay) {
		this.dateDay = dateDay;
	}

	public Long getDateHour() {
		return dateHour;
	}

	public void setDateHour(Long dateHour) {
		this.dateHour = dateHour;
	}

	
	
}
