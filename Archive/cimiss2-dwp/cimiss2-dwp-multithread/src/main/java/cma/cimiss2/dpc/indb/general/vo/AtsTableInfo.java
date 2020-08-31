package cma.cimiss2.dpc.indb.general.vo;

import java.util.List;

/**
 * 
 * <br>
 * @Title:  AtsTableInfo.java   
 * @Package org.cimiss2.dwp.RADAR.bean   
 * @Description:    TODO(ATS入库策略字段bean)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月14日 上午9:02:24   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class AtsTableInfo {
	
	private PathInfo prodCode;
	private PathInfo dataId;
	private PathInfo dataTime;
	private PathInfo station;
	private PathInfo productCenter;
	private PathInfo productDescription;
	private PathInfo productMethod;
	private List<PathInfo> attrs;
	
	
	public PathInfo getDataId() {
		return dataId;
	}
	public void setDataId(PathInfo dataId) {
		this.dataId = dataId;
	}
	public PathInfo getProdCode() {
		return prodCode;
	}
	public void setProdCode(PathInfo prodCode) {
		this.prodCode = prodCode;
	}
	public PathInfo getDataTime() {
		return dataTime;
	}
	public void setDataTime(PathInfo dataTime) {
		this.dataTime = dataTime;
	}
	public PathInfo getProductCenter() {
		return productCenter;
	}
	public void setProductCenter(PathInfo productCenter) {
		this.productCenter = productCenter;
	}
	public PathInfo getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(PathInfo productDescription) {
		this.productDescription = productDescription;
	}
	public PathInfo getProductMethod() {
		return productMethod;
	}
	public void setProductMethod(PathInfo productMethod) {
		this.productMethod = productMethod;
	}
	public List<PathInfo> getAttrs() {
		return attrs;
	}
	public void setAttrs(List<PathInfo> attrs) {
		this.attrs = attrs;
	}
	public PathInfo getStation() {
		return station;
	}
	public void setStation(PathInfo station) {
		this.station = station;
	}

}
