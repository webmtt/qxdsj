package cma.cimiss2.dpc.indb.vo;
/**
 * 
 * <br>
 * @Title:  PathInfo.java   
 * @Package org.cimiss2.dwp.RADAR.bean   
 * @Description:    TODO(路径和表策略实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月15日 下午7:30:07   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class PathInfo {
	
	private int type;
	private String pathStr;
	private int pos;
	private int start;
	private int end;
	
	public PathInfo(int type, String str, int pos, int start, int end){
		this.type = type;
		this.pathStr = str;
		this.pos = pos;
		this.start = start;
		this.end = end;
	}
	
	public PathInfo(int type, String str, int pos){
		this.type = type;
		this.pathStr = str;
		this.pos = pos;
	}
	
	public PathInfo(int type, String str){
		this.type = type;
		this.pathStr = str;
	}
	
	public PathInfo(){
		
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getStr() {
		return pathStr;
	}
	public void setStr(String str) {
		this.pathStr = str;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}

}
